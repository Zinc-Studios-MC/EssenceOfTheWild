package net.mrmisc.essenceofthewild.entity.custom.chicken;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.EOTWNestHelper;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class ChickenEntity extends Chicken implements VariantCarrier {
    private static final int LAY_NEST_SEARCH_TICKS = 1200;
    private static final int DELIVERY_TIMEOUT_TICKS = 2400;
    private static final int NEST_SIT_TICKS = 7 * 20;
    private static final double NEST_CAPTURE_DISTANCE_SQR = 6.25D;
    private static final double NEST_NAV_REPATH_DISTANCE_SQR = 0.16D;
    private static final double NEST_SEAT_Y_OFFSET = 0.25D;
    private static final double NEST_CENTERING_STEP = 0.18D;
    private static final double NEST_VERTICAL_SETTLE_DISTANCE_SQR = 0.09D;
    private static final double NEST_VERTICAL_READY_DISTANCE = 0.06D;
    private static final double NEST_VERTICAL_SETTLE_SPEED = 0.35D;
    private static final int GUARD_REPATH_MIN_TICKS = 45;
    private static final int GUARD_REPATH_RANDOM_TICKS = 55;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Nullable
    private BlockPos nestTarget;
    @Nullable
    private BlockPos guardedNest;
    private boolean forcedNestDelivery;
    private int deliveryTicks;
    private int nestSitTicks;
    private int nestSearchCooldown;
    private int guardRepathCooldown;

    public ChickenEntity(EntityType<? extends Chicken> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariantById(tag.getString("Variant"));
    }

    @Override
    public @Nullable ChickenEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        ChickenEntity child = new ChickenEntity(EOTWEntities.CHICKEN.get(), pLevel);
        child.setVariant(getVariant());
        return child;
    }

    @Override
    public void aiStep() {
        if (!level().isClientSide) {
            tickNestDelivery();
            tickNestGuarding();
        }

        super.aiStep();

        if (!level().isClientSide
                && nestTarget != null
                && horizontalDistanceToNestSqr(nestTarget) <= NEST_CAPTURE_DISTANCE_SQR) {
            getNavigation().stop();
            settleOntoNest(nestTarget);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            setupAnimationStates();
        }
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
        ServerPlayer serverPlayer = getLoveCause();
        if (serverPlayer == null && partner.getLoveCause() != null) {
            serverPlayer = partner.getLoveCause();
        }

        if (serverPlayer != null) {
            serverPlayer.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this, partner, null);
        }

        setAge(6000);
        partner.setAge(6000);
        resetLove();
        partner.resetLove();
        level.broadcastEntityEvent(this, (byte) 18);

        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            level.addFreshEntity(new ExperienceOrb(level, getX(), getY(), getZ(), random.nextInt(7) + 1));
        }

        Optional<BlockPos> nest = EOTWNestHelper.findNearestAvailableNest(level, blockPosition(), EOTWNestHelper.NEST_SEARCH_RADIUS);
        if (nest.isPresent()) {
            ChickenEntity nestingParent = partner instanceof ChickenEntity chickenPartner && random.nextBoolean() ? chickenPartner : this;
            nestingParent.startForcedNestDelivery(nest.get());
        } else {
            EOTWNestHelper.dropEggOnGround(this);
        }
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(ChickenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING_ON_NEST =
            SynchedEntityData.defineId(ChickenEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CENTERING_ON_NEST =
            SynchedEntityData.defineId(ChickenEntity.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public MobVariant getVariant() {
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < ChickenVariants.ALL.size())
                ? ChickenVariants.ALL.get(i)
                : ChickenVariants.ALL.get(0);
    }

    @Override
    public String getVariantId() {
        return getVariant().id();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SITTING_ON_NEST, false);
        this.entityData.define(CENTERING_ON_NEST, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", getVariant().id());
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, ChickenVariants.ALL.indexOf(variant));
    }

    public MobVariant pickVariant(Level level, BlockPos pos) {
        if (level.getBiome(pos).is(Tags.Biomes.IS_COLD)) {
            return level().random.nextBoolean() ? ChickenVariants.COLD : ChickenVariants.COLD_BROWN;
        } else if (level.getBiome(pos).is(Tags.Biomes.IS_HOT)) {
            return level().random.nextBoolean() ? ChickenVariants.WARM : ChickenVariants.WARM_BLACK;
        }
        return level.random.nextBoolean() ? ChickenVariants.BASIC : ChickenVariants.BASIC_GREY;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }

        SpawnGroupData spawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pReason == MobSpawnType.NATURAL || pReason == MobSpawnType.CHUNK_GENERATION) {
            EOTWNestHelper.tryPlaceNaturalNest(pLevel, this.blockPosition(), this.random);
        }

        return spawnData;
    }

    public void setVariantById(String id) {
        for (int i = 0; i < ChickenVariants.ALL.size(); i++) {
            if (ChickenVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    public int getEggTimeTicks() {
        return eggTime;
    }

    public boolean isDeliveringNestEgg() {
        return nestTarget != null || forcedNestDelivery;
    }

    public boolean isGuardingNest() {
        return guardedNest != null;
    }

    public boolean isSittingOnNestDelivery() {
        return entityData.get(SITTING_ON_NEST);
    }

    public boolean isCenteringOnNestDelivery() {
        return entityData.get(CENTERING_ON_NEST);
    }

    private void tickNestDelivery() {
        if (isBaby() || isChickenJockey() || !isAlive()) {
            clearNestDelivery();
            return;
        }

        if (nestSearchCooldown > 0) {
            nestSearchCooldown--;
        }

        if (nestTarget == null) {
            if (forcedNestDelivery) {
                findForcedNestOrDropEgg();
            } else if (eggTime <= LAY_NEST_SEARCH_TICKS && nestSearchCooldown <= 0) {
                Optional<BlockPos> nest = EOTWNestHelper.findNearestAvailableNest(level(), blockPosition(), EOTWNestHelper.NEST_SEARCH_RADIUS);
                if (nest.isPresent()) {
                    startNestDelivery(nest.get(), false);
                } else {
                    nestSearchCooldown = 200;
                }
            }
        }

        if (nestTarget == null) {
            return;
        }

        if (!EOTWNestHelper.isUsableNest(level(), nestTarget)) {
            if (forcedNestDelivery) {
                findForcedNestOrDropEgg();
            } else {
                clearNestDelivery();
            }
            return;
        }

        deliveryTicks++;
        eggTime = Math.max(eggTime, 20);

        if (deliveryTicks > DELIVERY_TIMEOUT_TICKS) {
            EOTWNestHelper.dropEggOnGround(this);
            resetEggTimer();
            clearNestDelivery();
            return;
        }

        double horizontalDistanceSqr = horizontalDistanceToNestSqr(nestTarget);
        if (horizontalDistanceSqr > NEST_CAPTURE_DISTANCE_SQR) {
            nestSitTicks = 0;
            setSittingOnNestDelivery(false);
            setCenteringOnNestDelivery(false);
            getNavigation().moveTo(nestTarget.getX() + 0.5D, nestTarget.getY(), nestTarget.getZ() + 0.5D, 1.1D);
            return;
        }

        getNavigation().stop();
        getLookControl().setLookAt(nestTarget.getX() + 0.5D, nestTarget.getY() + 0.35D, nestTarget.getZ() + 0.5D);
        settleOntoNest(nestTarget);

        boolean centeredOnNest = horizontalDistanceToNestSqr(nestTarget) <= NEST_NAV_REPATH_DISTANCE_SQR
                && Math.abs(getY() - getNestSeatY(nestTarget)) <= NEST_VERTICAL_READY_DISTANCE;
        setCenteringOnNestDelivery(!centeredOnNest);
        setSittingOnNestDelivery(centeredOnNest);

        if (!centeredOnNest) {
            nestSitTicks = 0;
            return;
        }

        if (nestSitTicks < NEST_SIT_TICKS) {
            nestSitTicks++;
            return;
        }

        int eggCount = EOTWNestHelper.randomClutchSize(random);
        if (EOTWNestHelper.placeEggsInNest(this, nestTarget, eggCount)) {
            EOTWNestHelper.playEggPlaced(this);
            startGuardingNest(nestTarget);
            resetEggTimer();
        } else if (forcedNestDelivery) {
            EOTWNestHelper.dropEggOnGround(this);
            resetEggTimer();
        }

        clearNestDelivery();
    }

    private void findForcedNestOrDropEgg() {
        Optional<BlockPos> nest = EOTWNestHelper.findNearestAvailableNest(level(), blockPosition(), EOTWNestHelper.NEST_SEARCH_RADIUS);
        if (nest.isPresent()) {
            startNestDelivery(nest.get(), true);
        } else {
            EOTWNestHelper.dropEggOnGround(this);
            clearNestDelivery();
        }
    }

    private void startForcedNestDelivery(BlockPos target) {
        startNestDelivery(target, true);
    }

    private void startNestDelivery(BlockPos target, boolean forced) {
        nestTarget = target.immutable();
        forcedNestDelivery = forced;
        deliveryTicks = 0;
        nestSitTicks = 0;
        guardedNest = null;
        guardRepathCooldown = 0;
        setSittingOnNestDelivery(false);
        setCenteringOnNestDelivery(false);
    }

    private void clearNestDelivery() {
        nestTarget = null;
        forcedNestDelivery = false;
        deliveryTicks = 0;
        nestSitTicks = 0;
        setSittingOnNestDelivery(false);
        setCenteringOnNestDelivery(false);
    }

    private void resetEggTimer() {
        eggTime = random.nextInt(6000) + 6000;
    }

    private void tickNestGuarding() {
        if (guardedNest == null || isBaby() || forcedNestDelivery || nestTarget != null) {
            return;
        }

        if (!EOTWNestHelper.hasEggs(level(), guardedNest)) {
            guardedNest = null;
            return;
        }

        if (guardRepathCooldown > 0) {
            guardRepathCooldown--;
        }

        if (!getNavigation().isDone() && guardRepathCooldown > 0 && guardedNest.distToCenterSqr(position()) <= 49.0D) {
            return;
        }

        Optional<Vec3> target = EOTWNestHelper.findGuardTarget(level(), guardedNest, random);
        target.ifPresent(vec3 -> getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0D));
        guardRepathCooldown = GUARD_REPATH_MIN_TICKS + random.nextInt(GUARD_REPATH_RANDOM_TICKS);
    }

    private void startGuardingNest(BlockPos pos) {
        guardedNest = pos.immutable();
        guardRepathCooldown = 0;
    }

    private void settleOntoNest(BlockPos pos) {
        double targetX = pos.getX() + 0.5D;
        double targetY = getNestSeatY(pos);
        double targetZ = pos.getZ() + 0.5D;

        double dx = targetX - getX();
        double dz = targetZ - getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        double step = Math.min(NEST_CENTERING_STEP, horizontalDistance);

        double x = horizontalDistance <= 1.0E-4D ? targetX : getX() + dx / horizontalDistance * step;
        double y = horizontalDistance * horizontalDistance <= NEST_VERTICAL_SETTLE_DISTANCE_SQR
                ? getY() + (targetY - getY()) * NEST_VERTICAL_SETTLE_SPEED
                : getY();
        double z = horizontalDistance <= 1.0E-4D ? targetZ : getZ() + dz / horizontalDistance * step;

        setDeltaMovement(Vec3.ZERO);
        setPos(x, y, z);
    }

    private double horizontalDistanceToNestSqr(BlockPos pos) {
        double dx = (pos.getX() + 0.5D) - getX();
        double dz = (pos.getZ() + 0.5D) - getZ();
        return dx * dx + dz * dz;
    }

    private double getNestSeatY(BlockPos pos) {
        return pos.getY() + NEST_SEAT_Y_OFFSET;
    }

    private void setSittingOnNestDelivery(boolean sitting) {
        if (entityData.get(SITTING_ON_NEST) != sitting) {
            entityData.set(SITTING_ON_NEST, sitting);
        }
    }

    private void setCenteringOnNestDelivery(boolean centering) {
        if (entityData.get(CENTERING_ON_NEST) != centering) {
            entityData.set(CENTERING_ON_NEST, centering);
        }
    }

    private void setupAnimationStates() {
        if (!onGround() && Math.abs(getDeltaMovement().y) > 0.02D) {
            flapAnimationState.startIfStopped(tickCount);
        } else {
            flapAnimationState.stop();
        }

        if (idleAnimationTimeout <= 0) {
            idleAnimationTimeout = random.nextInt(40) + 80;
            idleAnimationState.startIfStopped(tickCount);
        } else {
            idleAnimationTimeout--;
        }
    }
}
