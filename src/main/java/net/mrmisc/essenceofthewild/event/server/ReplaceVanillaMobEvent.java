package net.mrmisc.essenceofthewild.event.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomVariants;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// swaps vanilla mobs out for ours as they get added to the world
// the biome modifiers only pull them from natural spawn lists, they do nothing for spawn eggs,
// summon, spawners, breeding or worlds that already have vanilla mobs in them, so this covers the rest
// and its also what lets HideVanillaSpawnEggEvent hide the dupe eggs without stranding anyone
@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReplaceVanillaMobEvent {

    // vanilla type to whatever replaces it, keyed on the exact type instead of instanceof because
    // MushroomCow extends Cow and all our mobs extend their vanilla version, so instanceof would
    // match our own mobs and loop forever
    private static final Map<EntityType<?>, Supplier<? extends EntityType<? extends Mob>>> REPLACEMENTS = Map.of(
            EntityType.COW, EOTWEntities.COW,
            EntityType.MOOSHROOM, EOTWEntities.MOOSHROOM,
            EntityType.SHEEP, EOTWEntities.SHEEP,
            EntityType.PIG, EOTWEntities.PIG,
            EntityType.CHICKEN, EOTWEntities.CHICKEN,
            EntityType.RABBIT, EOTWEntities.RABBIT,
            EntityType.SPIDER, EOTWEntities.SPIDER,
            EntityType.CAVE_SPIDER, EOTWEntities.CAVE_SPIDER
    );

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !(level instanceof ServerLevelAccessor serverLevel)) {
            return;
        }

        Entity entity = event.getEntity();
        Supplier<? extends EntityType<? extends Mob>> replacementType = REPLACEMENTS.get(entity.getType());
        if (replacementType == null || !(entity instanceof Mob vanilla)) {
            return;
        }

        Mob replacement = replacementType.get().create(level);
        if (replacement == null) {
            return;
        }

        replacement.moveTo(vanilla.getX(), vanilla.getY(), vanilla.getZ(), vanilla.getYRot(), vanilla.getXRot());
        // let it roll its own variant like a natural spawn would instead of copying every mob's biome
        // rules in here, it has to be moved into place first or the biome lookup hits the wrong chunk
        if (replacement instanceof Animal) {
            replacement.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(replacement.blockPosition()),
                    MobSpawnType.CONVERSION, null, null);
        }

        copyCommonState(vanilla, replacement);
        copySpeciesState(vanilla, replacement);
        boolean hadRiders = copyPassengers(vanilla, replacement);

        if (hadRiders) {
            serverLevel.getLevel().addFreshEntityWithPassengers(replacement);
        } else {
            level.addFreshEntity(replacement);
        }
        event.setCanceled(true);
    }

    private static void copyCommonState(Mob vanilla, Mob replacement) {
        replacement.yHeadRot = vanilla.yHeadRot;
        replacement.yBodyRot = vanilla.yBodyRot;
        replacement.setDeltaMovement(vanilla.getDeltaMovement());
        // has to be after finalizeSpawn since that can turn it into a baby, the age we got is the real one
        if (vanilla instanceof AgeableMob vanillaAgeable && replacement instanceof AgeableMob ageable) {
            ageable.setAge(vanillaAgeable.getAge());
        }
        replacement.setHealth(vanilla.getHealth());
        replacement.setInvulnerable(vanilla.isInvulnerable());
        replacement.setNoAi(vanilla.isNoAi());
        for (MobEffectInstance effect : vanilla.getActiveEffects()) {
            replacement.addEffect(new MobEffectInstance(effect));
        }

        if (vanilla.hasCustomName()) {
            replacement.setCustomName(vanilla.getCustomName());
            replacement.setCustomNameVisible(vanilla.isCustomNameVisible());
        }
        if (vanilla.isPersistenceRequired()) {
            replacement.setPersistenceRequired();
        }
    }

    // per species stuff that Animal has no field for and would just get dropped otherwise
    private static void copySpeciesState(Mob vanilla, Mob replacement) {
        // check MushroomCow before Cow, its a subclass so the cow check would grab it first
        if (vanilla instanceof MushroomCow vanillaMooshroom && replacement instanceof MooshroomEntity mooshroom) {
            // set both variants, ours does the texture and mushroom drop, the vanilla one it inherits
            // is what gates the flower into suspicious stew thing
            boolean brown = vanillaMooshroom.getVariant() == MushroomCow.MushroomType.BROWN;
            mooshroom.setVariant(brown ? MooshroomVariants.BROWN : MooshroomVariants.RED);
            mooshroom.setVariant(brown ? MushroomCow.MushroomType.BROWN : MushroomCow.MushroomType.RED);
        } else if (vanilla instanceof Sheep vanillaSheep && replacement instanceof Sheep sheep) {
            sheep.setColor(vanillaSheep.getColor());
            sheep.setSheared(vanillaSheep.isSheared());
        } else if (vanilla instanceof Pig vanillaPig && replacement instanceof Pig pig && vanillaPig.isSaddled()) {
            pig.equipSaddle(null);
        } else if (vanilla instanceof Chicken vanillaChicken && replacement instanceof Chicken chicken) {
            chicken.setChickenJockey(vanillaChicken.isChickenJockey());
        } else if (vanilla instanceof Rabbit vanillaRabbit && replacement instanceof Rabbit rabbit) {
            rabbit.setVariant(vanillaRabbit.getVariant());
        }
    }

    private static boolean copyPassengers(Mob vanilla, Mob replacement) {
        List<Entity> riders = List.copyOf(vanilla.getPassengers());
        for (Entity rider : riders) {
            rider.stopRiding();
            rider.startRiding(replacement, true);
        }
        return !riders.isEmpty();
    }
}
