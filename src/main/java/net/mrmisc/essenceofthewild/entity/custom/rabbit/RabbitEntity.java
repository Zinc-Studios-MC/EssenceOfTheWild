package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RabbitEntity extends Rabbit {
    // Above this move-speed multiplier the rabbit runs (smooth glide); below it, it walks (small hops).
    private static final double RUN_SPEED_MODIFIER = 1.5D;

    public RabbitEntity(EntityType<? extends Rabbit> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // Replace the vanilla rabbit's jump-based move control with smooth ground movement.
        this.moveControl = new MoveControl(this);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(RabbitEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RUNNING =
            SynchedEntityData.defineId(RabbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MOVING =
            SynchedEntityData.defineId(RabbitEntity.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
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

    // Smooth ground movement for both walk and run (so the walk/run animations actually play);
    // only jump to climb obstacles/up-paths or out of water.
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

    private void setupAnimationStates() {

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public MobVariant getRabbitVariant() {
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < RabbitVariants.ALL.size())
                ? RabbitVariants.ALL.get(i)
                : RabbitVariants.ALL.get(0);
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
        tag.putString("Variant", getRabbitVariant().id());
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, RabbitVariants.ALL.indexOf(variant));
    }

    private MobVariant pickVariant(Level level, BlockPos pos) {
        if (level.getBiome(pos).is(Tags.Biomes.IS_COLD)) {
            return RabbitVariants.COLD;
        }
        return level.random.nextBoolean() ? RabbitVariants.BASIC : RabbitVariants.BASIC_WHITE;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
        return Objects.requireNonNull(super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag));
    }

    private void setVariantById(String id) {
        for (int i = 0; i < RabbitVariants.ALL.size(); i++) {
            if (RabbitVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariantById(tag.getString("Variant"));
    }
}
