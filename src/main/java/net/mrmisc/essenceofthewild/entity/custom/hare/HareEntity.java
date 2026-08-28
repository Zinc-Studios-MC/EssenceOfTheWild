package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.entity.util.LevelBiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HareEntity extends Rabbit implements GeoEntity, VariantCarrier {
    private static final double RUN_SPEED_MODIFIER = 1.5D;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.hare.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.hare.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.hare.run");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public HareEntity(EntityType<? extends Rabbit> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new MoveControl(this);
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<MobVariant> variant = new VariantSlot<>(this.entityData, VARIANT, HareVariants.SET);
    private static final EntityDataAccessor<Boolean> RUNNING =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MOVING =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));
    }

    private PlayState movementController(AnimationState<HareEntity> state) {
        if (!this.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isRunning() ? RUN : WALK);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            double hSpeed = this.getDeltaMovement().horizontalDistance();
            boolean moving = hSpeed > 0.025D;
            boolean running = moving && this.getMoveControl().getSpeedModifier() > RUN_SPEED_MODIFIER;
            this.entityData.set(MOVING, moving);
            this.entityData.set(RUNNING, running);
        }
    }

    @Override
    public void startJumping() {
        if (shouldJumpObstacle()) {
            super.startJumping();
        }
    }

    private boolean shouldJumpObstacle() {
        if (this.horizontalCollision || this.isInWaterOrBubble()) {
            return true;
        }
        Path path = this.getNavigation().getPath();
        return path != null && !path.isDone() && path.getNextEntityPos(this).y > this.getY() + 0.5D;
    }

    public boolean isRunning() {
        return this.entityData.get(RUNNING);
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
    }

    public MobVariant getRabbitVariant() {
        return variant.get();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(RUNNING, false);
        this.entityData.define(MOVING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
    }

    public void setVariant(MobVariant v) {
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

    private MobVariant pickVariant(Level level, BlockPos pos) {
        return HareVariants.pick(new LevelBiomeQuery(level, pos));
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
        return Objects.requireNonNull(super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
    }
}
