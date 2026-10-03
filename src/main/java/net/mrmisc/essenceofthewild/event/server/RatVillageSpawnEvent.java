package net.mrmisc.essenceofthewild.event.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
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
    private static final int SPAWN_RADIUS = 48;
    private static final int MAX_RATS_NEARBY = 5;

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) {
            return;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                || level.getGameTime() % SPAWN_INTERVAL != 0) {
            return;
        }

        for (ServerPlayer player : level.players()) {
            if (!player.isSpectator()) {
                trySpawnNearPlayer(level, player);
            }
        }
    }

    private static void trySpawnNearPlayer(ServerLevel level, ServerPlayer player) {
        RandomSource random = level.random;
        for (int i = 0; i < 12; i++) {
            int dx = random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            int dz = random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            if (dx * dx + dz * dz < 24 * 24) {
                continue;
            }
            BlockPos column = player.blockPosition().offset(dx, 0, dz);
            if (!level.hasChunksAt(column.getX() - 10, column.getZ() - 10,
                    column.getX() + 10, column.getZ() + 10)) {
                continue;
            }

            BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column);
            if ((level.structureManager().getStructureWithPieceAt(surface, StructureTags.VILLAGE).isValid()
                    || level.isCloseToVillage(surface, 2))
                    && spawnRat(level, surface)) {
                return;
            }
            for (int y = -12; y <= 12; y++) {
                BlockPos pos = column.offset(0, y, 0);
                if (level.structureManager().getStructureWithPieceAt(pos, BuiltinStructures.STRONGHOLD).isValid()
                        && spawnRat(level, pos)) {
                    return;
                }
            }
        }
    }

    private static boolean spawnRat(ServerLevel level, BlockPos pos) {
        if (level.getNearestPlayer(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 24.0D, false) != null
                || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, pos, EOTWEntities.RAT.get())
                || !RatEntity.checkRatSpawnRules(EOTWEntities.RAT.get(), level, MobSpawnType.NATURAL, pos, level.random)) {
            return false;
        }
        if (level.getEntitiesOfClass(RatEntity.class,
                new AABB(pos).inflate(SPAWN_RADIUS, 16.0D, SPAWN_RADIUS)).size() >= MAX_RATS_NEARBY) {
            return false;
        }
        RatEntity rat = EOTWEntities.RAT.get().create(level);
        if (rat == null) {
            return false;
        }
        rat.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        if (!level.noCollision(rat)) {
            return false;
        }
        rat.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
        return level.addFreshEntity(rat);
    }
}
