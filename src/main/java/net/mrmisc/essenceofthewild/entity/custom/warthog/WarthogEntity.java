package net.mrmisc.essenceofthewild.entity.custom.warthog;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.*;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;

public class WarthogEntity extends AbstractHorse implements GeoEntity, VariantCarrier {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(WarthogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CHARGE = SynchedEntityData.defineId(WarthogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> HEADING = SynchedEntityData.defineId(WarthogEntity.class, EntityDataSerializers.FLOAT);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.warthog.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.warthog.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.warthog.run");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlayAndHold("animation.warthog.attack");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final VariantSlot<Variant> variant = new VariantSlot<>(entityData, VARIANT, WarthogVariants.SET);
    private int cooldown;
    private int chargeTicks;
    private int chaseTicks;
    private int nearTicks;
    private UUID nearbyPlayer;

    public WarthogEntity(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 24).add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.FOLLOW_RANGE, 24).add(Attributes.JUMP_STRENGTH, 0.5);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(VARIANT, 0);
        entityData.define(CHARGE, 0);
        entityData.define(HEADING, 0F);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new ChargeGoal());
        goalSelector.addGoal(2, new RunAroundLikeCrazyGoal(this, 1.4) {
            @Override
            public void tick() {
                Entity rider = getFirstPassenger();
                super.tick();
                if (!isTamed() && !isVehicle() && rider instanceof Player player && canAttack(player)) {
                    setTarget(player);
                    chaseTicks = 160;
                }
            }
        });
        goalSelector.addGoal(3, new BreedGoal(this, 1));
        goalSelector.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(EOTWItems.RED_ONION.get(), Items.GOLDEN_CARROT), false));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return !isVehicle() && super.canUse() && canChargeHit(getLastHurtByMob());
            }

            @Override
            public boolean canContinueToUse() {
                return !isBaby() && !isVehicle() && chaseTicks > 0 && super.canContinueToUse();
            }

            @Override
            public void start() {
                super.start();
                if (isBaby()) {
                    alertOthers();
                    stop();
                } else {
                    chaseTicks = 600;
                }
            }

            @Override
            protected void alertOther(Mob mob, LivingEntity attacker) {
                if (mob instanceof WarthogEntity hog && !hog.isBaby() && hog.canChargeHit(attacker)) {
                    super.alertOther(mob, attacker);
                    hog.chaseTicks = 600;
                }
            }
        });
    }

    @Override
    public int getMaxTemper() {
        return 200;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(EOTWItems.RED_ONION.get());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isFood(stack)) {
            if (!level().isClientSide) {
                if (isBaby()) {
                    ageUp((int) ((-getAge() / 20) * 0.1F), true);
                } else if (getAge() == 0 && canFallInLove()) {
                    setInLove(player);
                } else {
                    return InteractionResult.PASS;
                }
                usePlayerItem(player, hand, stack);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (stack.is(Items.GOLDEN_CARROT)) {
            if (!level().isClientSide) {
                if (isTamed() && getHealth() >= getMaxHealth() && !isBaby()) {
                    return InteractionResult.PASS;
                }
                heal(4);
                modifyTemper(25);
                if (isBaby()) {
                    ageUp(12, true);
                }
                usePlayerItem(player, hand, stack);
                playSound(SoundEvents.PIG_AMBIENT, 1, 1.2F);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isBaby() || isVehicle() || player.isSecondaryUseActive() || getTarget() != null || entityData.get(CHARGE) != 0) {
            return InteractionResult.PASS;
        }
        InteractionResult result = stack.interactLivingEntity(player, this, hand);
        if (result.consumesAction()) {
            return result;
        }
        doPlayerRide(player);
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return isTamed() && !isBaby() && getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    protected boolean canPerformRearing() {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.75;
    }

    @Override
    protected void positionRider(Entity rider, MoveFunction move) {
        super.positionRider(rider, (passenger, x, y, z) -> {
            double yaw = Math.toRadians(yBodyRot);
            move.accept(passenger, x + Math.sin(yaw) * 0.2, y, z - Math.cos(yaw) * 0.2);
        });
    }

    @Override
    protected Vec2 getRiddenRotation(LivingEntity rider) {
        return entityData.get(CHARGE) == 1 ? new Vec2(0, entityData.get(HEADING)) : super.getRiddenRotation(rider);
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travel) {
        int phase = entityData.get(CHARGE);
        return phase == 1 ? new Vec3(0, 0, 1) : phase == 2 ? Vec3.ZERO : super.getRiddenInput(player, travel);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return entityData.get(CHARGE) == 1 ? 0.65F : super.getRiddenSpeed(player);
    }

    public void charge(Player player) {
        if (!level().isClientSide && isAlive() && !isBaby() && isTamed() && getControllingPassenger() == player
                && cooldown == 0 && entityData.get(CHARGE) == 0 && onGround()) {
            beginCharge(player.getYRot());
        }
    }

    private void beginCharge(float yaw) {
        entityData.set(HEADING, yaw);
        entityData.set(CHARGE, 1);
        setYRot(yaw);
        chargeTicks = 40;
        cooldown = 200;
        getNavigation().stop();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (isBaby() || isTamed() || isVehicle() || getTarget() != null || entityData.get(CHARGE) != 0) {
            nearTicks = 0;
            nearbyPlayer = null;
            return;
        }
        boolean protecting = !level().getEntitiesOfClass(WarthogEntity.class,
                getBoundingBox().inflate(8, 4, 8), WarthogEntity::isBaby).isEmpty();
        Player player = level().getNearestPlayer(getX(), getY(), getZ(), protecting ? 6 : 3,
                p -> p instanceof Player near && canChargeHit(near) && getSensing().hasLineOfSight(near));
        if (player == null) {
            nearTicks = 0;
            nearbyPlayer = null;
            return;
        }
        if (!player.getUUID().equals(nearbyPlayer)) {
            nearbyPlayer = player.getUUID();
            nearTicks = 0;
        }
        int delay = protecting ? 40 : 120;
        if (++nearTicks == delay / 2) {
            playSound(SoundEvents.PIG_HURT, 0.7F, 0.8F);
        }
        if (nearTicks >= delay && random.nextInt(20) == 0) {
            setTarget(player);
            chaseTicks = 600;
            nearTicks = 0;
            nearbyPlayer = null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
        }
        if (isBaby()) {
            setTarget(null);
            entityData.set(CHARGE, 0);
            return;
        }
        if (chaseTicks > 0 && --chaseTicks == 0) {
            setTarget(null);
        }
        int phase = entityData.get(CHARGE);
        if (phase == 0) {
            return;
        }
        if (--chargeTicks <= 0 || isInWaterOrBubble() || horizontalCollision) {
            entityData.set(CHARGE, 0);
            return;
        }
        if (phase == 2) {
            return;
        }
        Vec3 dir = Vec3.directionFromRotation(0, entityData.get(HEADING));
        setYRot(entityData.get(HEADING));
        yBodyRot = yHeadRot = getYRot();
        if (!isVehicle()) {
            setDeltaMovement(dir.x * 0.65, getDeltaMovement().y, dir.z * 0.65);
            hasImpulse = true;
        }
        LivingEntity hit = null;
        double nearest = Double.MAX_VALUE;
        for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().expandTowards(dir.scale(0.7)).inflate(0.15), this::canChargeHit)) {
            double distance = distanceToSqr(entity);
            if (distance < nearest && getSensing().hasLineOfSight(entity)) {
                hit = entity;
                nearest = distance;
            }
        }
        if (hit != null) {
            doHurtTarget(hit);
            hit.setDeltaMovement(dir.x * 0.45, 0.95, dir.z * 0.45);
            hit.hasImpulse = true;
            hit.hurtMarked = true;
            entityData.set(CHARGE, 2);
            chargeTicks = 9;
            setDeltaMovement(0, getDeltaMovement().y, 0);
        }
    }

    private boolean canChargeHit(LivingEntity entity) {
        return entity != this && !hasPassenger(entity) && !entity.isPassengerOfSameVehicle(this)
                && !isAlliedTo(entity) && !entity.getUUID().equals(getOwnerUUID()) && canAttack(entity);
    }

    @Override
    public boolean canMate(Animal other) {
        return other != this && other instanceof WarthogEntity && isInLove() && other.isInLove();
    }

    @Override
    public WarthogEntity getBreedOffspring(ServerLevel level, AgeableMob other) {
        WarthogEntity child = EOTWEntities.WARTHOG.get().create(level);
        if (child != null) {
            child.variant.set(other instanceof WarthogEntity parent && random.nextBoolean() ? parent.getVariant() : getVariant());
        }
        return child;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                        SpawnGroupData data, CompoundTag tag) {
        if (tag == null || !tag.contains("Variant")) {
            variant.set(level.getBiome(blockPosition()).is(BiomeTags.IS_BADLANDS) ? WarthogVariants.DESERT : WarthogVariants.BASIC);
        }
        return super.finalizeSpawn(level, difficulty, reason, data, tag);
    }

    public static boolean checkSpawnRules(EntityType<WarthogEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolidRender(level, pos.below()) && level.getRawBrightness(pos, 0) > 8;
    }

    public Variant getVariant() {
        return variant.get();
    }

    @Override
    public String getVariantId() {
        return variant.id();
    }

    @Override
    public void setVariantById(String id) {
        variant.setById(id);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
        tag.putInt("ChargeCooldown", cooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
        cooldown = Math.max(0, tag.getInt("ChargeCooldown"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.PIG_STEP, 0.15F, 1);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 2, state -> {
            int phase = entityData.get(CHARGE);
            if (!isBaby() && phase == 2) {
                return state.setAndContinue(ATTACK);
            }
            if (phase == 1 || getDeltaMovement().horizontalDistanceSqr() > 0.04) {
                return state.setAndContinue(RUN);
            }
            return state.setAndContinue(state.isMoving() ? WALK : IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private class ChargeGoal extends Goal {
        private int attackTicks;

        ChargeGoal() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            return !isBaby() && (entityData.get(CHARGE) != 0 || target != null && target.isAlive()
                    && canChargeHit(target) && distanceToSqr(target) <= getAttributeValue(Attributes.FOLLOW_RANGE) * getAttributeValue(Attributes.FOLLOW_RANGE));
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void tick() {
            if (attackTicks > 0) {
                attackTicks--;
            }
            if (entityData.get(CHARGE) != 0) {
                getNavigation().stop();
                return;
            }
            LivingEntity target = getTarget();
            if (target == null) {
                return;
            }
            getLookControl().setLookAt(target, 30, 30);
            getNavigation().moveTo(target, 1.6);
            if (cooldown == 0 && distanceToSqr(target) < 36 && getSensing().hasLineOfSight(target) && onGround()) {
                Vec3 dir = target.position().subtract(position());
                beginCharge((float) Math.toDegrees(Math.atan2(-dir.x, dir.z)));
            } else if (attackTicks == 0 && distanceToSqr(target) <= getBbWidth() * getBbWidth() * 4 + target.getBbWidth()
                    && getSensing().hasLineOfSight(target)) {
                attackTicks = 20;
                if (doHurtTarget(target)) {
                    entityData.set(CHARGE, 2);
                    chargeTicks = 9;
                    getNavigation().stop();
                    setDeltaMovement(0, getDeltaMovement().y, 0);
                }
            }
        }

        @Override
        public void stop() {
            getNavigation().stop();
            setTarget(null);
        }
    }
}
