package net.mrmisc.essenceofthewild.event.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RatVillageSpawnEvent {

    private static final int SPAWN_INTERVAL = 1200;
    private static final int VILLAGE_RADIUS = 48;
    private static final int MIN_OCCUPIED_BEDS = 4;
    private static final int MAX_RATS_NEARBY = 5;

    private static final Map<ResourceKey<Level>, Integer> COOLDOWNS = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) {
            return;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return;
        }

        ResourceKey<Level> key = level.dimension();
        int cooldown = COOLDOWNS.getOrDefault(key, SPAWN_INTERVAL) - 1;
        if (cooldown > 0) {
            COOLDOWNS.put(key, cooldown);
            return;
        }
        COOLDOWNS.put(key, SPAWN_INTERVAL);

        trySpawnNearVillage(level);
    }

    private static void trySpawnNearVillage(ServerLevel level) {
        Player player = level.getRandomPlayer();
        if (player == null) {
            return;
        }

        RandomSource random = level.random;
        int dx = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        int dz = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos pos = player.blockPosition().offset(dx, 0, dz);

        if (!level.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) {
            return;
        }
        if (!NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, pos, EOTWEntities.RAT.get())) {
            return;
        }
        if (!level.isCloseToVillage(pos, 2)) {
            return;
        }

        long beds = level.getPoiManager().getCountInRange(
                holder -> holder.is(PoiTypes.HOME), pos, VILLAGE_RADIUS, PoiManager.Occupancy.IS_OCCUPIED);
        if (beds <= MIN_OCCUPIED_BEDS) {
            return;
        }

        List<RatEntity> nearby = level.getEntitiesOfClass(
                RatEntity.class, new AABB(pos).inflate(VILLAGE_RADIUS, 8.0D, VILLAGE_RADIUS));
        if (nearby.size() >= MAX_RATS_NEARBY) {
            return;
        }

        spawnRat(level, pos);
    }

    private static void spawnRat(ServerLevel level, BlockPos pos) {
        RatEntity rat = EOTWEntities.RAT.get().create(level);
        if (rat == null) {
            return;
        }
        rat.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
        rat.moveTo(pos, 0.0F, 0.0F);
        level.addFreshEntityWithPassengers(rat);
    }
}
