package net.mrmisc.essenceofthewild.entity.custom.cow;

import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.LevelBiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CowEntity extends Cow implements VariantCarrier, GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.cow.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.cow.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.cow.run");

    private static final int PANIC_DURATION = 60;

    private static final double BABY_GAIT_SPEED = 1.5D;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private final VariantSlot<Variant> variant = new VariantSlot<>(this.entityData, VARIANT, CowVariants.SET);

    private int panicTicks;

    public CowEntity(EntityType<? extends Cow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController)
                .setAnimationSpeedHandler(CowEntity::gaitSpeed));
    }

    private PlayState movementController(AnimationState<CowEntity> state) {
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isPanicking() ? RUN : WALK);
    }

    private static double gaitSpeed(CowEntity cow) {
        return cow.isBaby() ? BABY_GAIT_SPEED : 1.0D;
    }

    public boolean isPanicking() {
        return this.entityData.get(PANICKING);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide) {
            this.panicTicks = PANIC_DURATION;
            this.entityData.set(PANICKING, true);
        }
        return hurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.panicTicks > 0 && --this.panicTicks == 0) {
            this.entityData.set(PANICKING, false);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(CowEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> PANICKING =
            SynchedEntityData.defineId(CowEntity.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(PANICKING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag != null && variant.isStoredIn(pDataTag)) {
            variant.load(pDataTag);
            return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        }
        setVariant(pickVariant(pLevel.getLevel(), blockPosition()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
    }

    public Cow getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        CowEntity child = new CowEntity(EOTWEntities.COW.get(), pLevel);
        child.setVariant(this.getVariant());
        return child;
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
        return CowVariants.pick(new LevelBiomeQuery(level, pos), level.random::nextBoolean);
    }
}
