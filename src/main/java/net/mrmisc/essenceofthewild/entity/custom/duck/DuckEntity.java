package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.mrmisc.essenceofthewild.entity.util.LevelBiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.EOTWNestHelper;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class DuckEntity extends Chicken implements VariantCarrier {
    private static final int LAY_NEST_SEARCH_TICKS = 1200;
    private static final int DELIVERY_TIMEOUT_TICKS = 2400;
    private static final int NEST_SIT_TICKS = 7 * 20;
    private static final double NEST_CAPTURE_DISTANCE_SQR = 2.0D;
    private static final double NEST_NAV_REPATH_DISTANCE_SQR = 0.16D;
    private static final double NEST_SEAT_Y_OFFSET = 0.25D;
    private static final double NEST_CENTERING_STEP = 0.18D;
    private static final double NEST_VERTICAL_SETTLE_DISTANCE_SQR = 0.09D;
    private static final double NEST_VERTICAL_READY_DISTANCE = 0.06D;
    private static final double NEST_VERTICAL_SETTLE_SPEED = 0.35D;
    private static final int GUARD_REPATH_MIN_TICKS = 45;
    private static final int GUARD_REPATH_RANDOM_TICKS = 55;
    private static final int FISH_SEARCH_INTERVAL = 20;
    private static final int FISH_HUNT_COOLDOWN_MIN = 140;
    private static final int FISH_HUNT_COOLDOWN_RANDOM = 160;
    private static final double FISH_SEARCH_RANGE = 10.0D;
    private static final double FISH_EAT_DISTANCE_SQR = 2.25D;

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(DuckEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<Variant> variant = new VariantSlot<>(this.entityData, VARIANT, DuckVariants.SET);
    private static final EntityDataAccessor<Boolean> SITTING_ON_NEST =
            SynchedEntityData.defineId(DuckEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CENTERING_ON_NEST =
            SynchedEntityData.defineId(DuckEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIVING =
            SynchedEntityData.defineId(DuckEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    public final AnimationState waterIdleAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    public final AnimationState diveAnimationState = new AnimationState();
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
    private int fishTargetId = -1;
    private int fishSearchCooldown;
    private int fishHuntCooldown;

    private static final double IMPRINT_RADIUS = 12.0D;
    @Nullable
    private UUID imprintUuid;
    private boolean imprinted;

    public DuckEntity(EntityType<? extends Chicken> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 9.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.COD), false));
        this.goalSelector.addGoal(4, new DuckImprintFollowGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SITTING_ON_NEST, false);
        this.entityData.define(CENTERING_ON_NEST, false);
        this.entityData.define(DIVING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
        tag.putBoolean("Imprinted", this.imprinted);
        if (this.imprintUuid != null) {
            tag.putUUID("Imprint", this.imprintUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
        this.imprinted = tag.getBoolean("Imprinted");
        this.imprintUuid = tag.hasUUID("Imprint") ? tag.getUUID("Imprint") : null;
    }

    @Override
    public @Nullable DuckEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        DuckEntity child = new DuckEntity(EOTWEntities.DUCK.get(), level);
        child.setVariant(this.getVariant());
        return child;
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
            DuckEntity nestingParent = partner instanceof DuckEntity duckPartner && random.nextBoolean() ? duckPartner : this;
            nestingParent.startForcedNestDelivery(nest.get());
        } else {
            EOTWNestHelper.dropEggOnGround(this);
        }
    }

    @Override
    public void aiStep() {
        if (!level().isClientSide) {
            tickNestDelivery();
            tickNestGuarding();
            tickFishHunting();
            tickImprinting();
        }

        super.aiStep();

        if (!level().isClientSide
                && nestTarget != null
                && horizontalDistanceToNestSqr(nestTarget) <= NEST_CAPTURE_DISTANCE_SQR) {
            getNavigation().stop();
            settleOntoNest(nestTarget);
        }

        if (!level().isClientSide) {
            tickSteadyFloat();
        }
    }

    private static final double FLOAT_SUBMERGE = 0.5D;
    private static final double EYE_FLOAT_CLEARANCE = 0.05D;

    private void tickSteadyFloat() {
        if (!isInWater() || isDivingAnimationActive() || nestTarget != null || fishTargetId >= 0) {
            return;
        }
        double targetY = computeFloatY();
        if (Double.isNaN(targetY)) {
            return;
        }
        double step = Mth.clamp(targetY - getY(), -0.15D, 0.15D);
        setPos(getX(), getY() + step, getZ());
        Vec3 dm = getDeltaMovement();
        setDeltaMovement(dm.x, 0.0D, dm.z);
        this.fallDistance = 0.0F;
    }

    private double computeFloatY() {
        int x = Mth.floor(getX());
        int z = Mth.floor(getZ());
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, Mth.floor(getY()), z);
        if (!level().getFluidState(cursor).is(FluidTags.WATER)) {
            return Double.NaN;
        }
        while (level().getFluidState(cursor).is(FluidTags.WATER)) {
            cursor.move(0, 1, 0);
        }
        double surface = cursor.getY();
        double floatFeet = surface - FLOAT_SUBMERGE;
        double eyesAboveSurface = surface - getEyeHeight() + EYE_FLOAT_CLEARANCE;
        return Math.max(floatFeet, eyesAboveSurface);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            setupAnimationStates();
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(damageSource, looting, recentlyHit);
        int count = 1 + random.nextInt(Math.max(1, looting + 1));
        spawnAtLocation(new ItemStack(EOTWItems.DUCK_FEATHER.get(), count));
    }

    public Variant getVariant() {
        return variant.get();
    }

    public void setVariant(Variant v) {
        variant.set(v);
    }

    @Override
    public String getVariantId() {
        return variant.id();
    }

    @Override
    public void setVariantById(String id) {
        variant.setById(id);
    }

    private Variant pickVariant(Level level, BlockPos pos) {
        return DuckVariants.pick(new LevelBiomeQuery(level, pos), level.random::nextBoolean);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        boolean naturalBrood = reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION;
        DuckGroupData duckGroupData = spawnData instanceof DuckGroupData data ? data : null;

        SpawnGroupData finalized = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        if (dataTag != null && dataTag.contains("Variant")) {
            variant.load(dataTag);
            return finalized;
        }

        if (!naturalBrood) {
            setVariant(pickVariant(level.getLevel(), blockPosition()));
            return finalized;
        }

        if (duckGroupData == null) {
            boolean brood = this.random.nextInt(5) == 0;
            int ducklings = brood ? 2 + this.random.nextInt(3) : 0;
            duckGroupData = new DuckGroupData(pickVariant(level.getLevel(), blockPosition()).id(), ducklings);
        }

        setVariantById(duckGroupData.variantId);
        if (duckGroupData.motherSpawned) {
            if (duckGroupData.ducklingsRemaining > 0) {
                setBaby(true);
                duckGroupData.ducklingsRemaining--;
            }
        } else {
            duckGroupData.motherSpawned = true;
            if (duckGroupData.ducklingsRemaining > 0) {
                EOTWNestHelper.tryPlaceNaturalNest(level, this.blockPosition(), this.random);
            }
        }

        return duckGroupData;
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

    public boolean isDivingAnimationActive() {
        return entityData.get(DIVING);
    }

    public void imprintOn(LivingEntity entity) {
        this.imprintUuid = entity.getUUID();
        this.imprinted = true;
    }

    @Nullable
    public LivingEntity getImprintTarget() {
        if (this.imprintUuid == null || !(level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        Entity entity = serverLevel.getEntity(this.imprintUuid);
        return entity instanceof LivingEntity living ? living : null;
    }

    private void tickImprinting() {
        if (!isBaby() || this.imprinted) {
            return;
        }
        LivingEntity candidate = findImprintCandidate();
        if (candidate != null) {
            this.imprintUuid = candidate.getUUID();
            this.imprinted = true;
        }
    }

    @Nullable
    private LivingEntity findImprintCandidate() {
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (Entity entity : level().getEntities(this, getBoundingBox().inflate(IMPRINT_RADIUS), this::isImprintCandidate)) {
            double dist = distanceToSqr(entity);
            if (dist < bestDist) {
                bestDist = dist;
                best = (LivingEntity) entity;
            }
        }
        return best;
    }

    private boolean isImprintCandidate(Entity entity) {
        if (entity == this) {
            return false;
        }
        if (entity instanceof Player player) {
            return !player.isSpectator();
        }
        return entity instanceof Animal animal && !animal.isBaby();
    }

    private void tickFishHunting() {
        if (isBaby() || !isAlive() || nestTarget != null || forcedNestDelivery) {
            clearFishTarget();
            setDiving(false);
            return;
        }

        if (fishHuntCooldown > 0) {
            fishHuntCooldown--;
        }

        if (!isInWaterOrBubble()) {
            clearFishTarget();
            setDiving(false);
            return;
        }

        Entity fish = getFishTarget();
        if (!isValidFishTarget(fish)) {
            clearFishTarget();
            if (fishHuntCooldown > 0) {
                setDiving(false);
                return;
            }

            if (fishSearchCooldown > 0) {
                fishSearchCooldown--;
                setDiving(false);
                return;
            }

            fish = findNearbyFish();
            if (fish == null) {
                fishSearchCooldown = FISH_SEARCH_INTERVAL;
                setDiving(false);
                return;
            }

            fishTargetId = fish.getId();
        }

        Vec3 targetPos = fish.position().add(0.0D, fish.getBbHeight() * 0.35D, 0.0D);
        Vec3 travel = targetPos.subtract(position());
        if (travel.lengthSqr() <= FISH_EAT_DISTANCE_SQR) {
            eatFish(fish);
            return;
        }

        Vec3 direction = travel.normalize();
        setDiving(direction.y < -0.08D || fish.getY() + fish.getBbHeight() * 0.5D < getY());
        setDeltaMovement(getDeltaMovement().scale(0.78D).add(direction.scale(isDivingAnimationActive() ? 0.09D : 0.065D)));
        hasImpulse = true;
        getLookControl().setLookAt(fish, 30.0F, 30.0F);
    }

    private void eatFish(Entity fish) {
        fish.discard();
        playSound(SoundEvents.GENERIC_EAT, 0.7F, 0.95F + random.nextFloat() * 0.2F);
        fishHuntCooldown = FISH_HUNT_COOLDOWN_MIN + random.nextInt(FISH_HUNT_COOLDOWN_RANDOM);
        fishSearchCooldown = FISH_SEARCH_INTERVAL;
        clearFishTarget();
        setDiving(false);
    }

    @Nullable
    private Entity getFishTarget() {
        return fishTargetId >= 0 ? level().getEntity(fishTargetId) : null;
    }

    private void clearFishTarget() {
        fishTargetId = -1;
    }

    @Nullable
    private Entity findNearbyFish() {
        AABB searchBox = getBoundingBox().inflate(FISH_SEARCH_RANGE, 4.0D, FISH_SEARCH_RANGE);
        return level().getEntities(this, searchBox, entity -> entity instanceof AbstractFish fish && fish.isAlive() && fish.isInWater())
                .stream()
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);
    }

    private boolean isValidFishTarget(@Nullable Entity entity) {
        return entity instanceof AbstractFish fish
                && fish.isAlive()
                && fish.isInWater()
                && distanceToSqr(fish) <= FISH_SEARCH_RANGE * FISH_SEARCH_RANGE * 1.5D;
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

    private void setDiving(boolean diving) {
        if (entityData.get(DIVING) != diving) {
            entityData.set(DIVING, diving);
        }
    }

    private void setupAnimationStates() {
        if (!onGround() && !isInWaterOrBubble()) {
            flapAnimationState.startIfStopped(tickCount);
        } else {
            flapAnimationState.stop();
        }

        if (isInWaterOrBubble()) {
            waterIdleAnimationState.startIfStopped(tickCount);
            swimAnimationState.startIfStopped(tickCount);
        } else {
            waterIdleAnimationState.stop();
            swimAnimationState.stop();
        }

        if (isDivingAnimationActive()) {
            diveAnimationState.startIfStopped(tickCount);
        } else {
            diveAnimationState.stop();
        }

        if (idleAnimationTimeout <= 0) {
            idleAnimationTimeout = random.nextInt(40) + 40;
            idleAnimationState.start(tickCount);
        } else {
            idleAnimationTimeout--;
        }
    }

    private static class DuckGroupData extends AgeableMob.AgeableMobGroupData {
        private final String variantId;
        private int ducklingsRemaining;
        private boolean motherSpawned;

        private DuckGroupData(String variantId, int ducklingsRemaining) {
            super(false);
            this.variantId = variantId;
            this.ducklingsRemaining = ducklingsRemaining;
        }
    }
}
