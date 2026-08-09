package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AnimationState;
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
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.pathfinder.Path;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HareEntity extends Rabbit {
    // over this speed multiplier the hare runs, under it it walks with little hops
    private static final double RUN_SPEED_MODIFIER = 1.5D;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public HareEntity(EntityType<? extends Rabbit> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // swap out the vanilla rabbit hop control for normal ground movement
        this.moveControl = new MoveControl(this);
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RUNNING =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MOVING =
            SynchedEntityData.defineId(HareEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
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

    // walks and runs on the ground so those anims actually play, only jumps for obstacles or to get out of water
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
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < HareVariants.ALL.size())
                ? HareVariants.ALL.get(i)
                : HareVariants.ALL.get(0);
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
        this.entityData.set(VARIANT, HareVariants.ALL.indexOf(variant));
    }

    private MobVariant pickVariant(Level level, BlockPos pos) {
        // sandy coat in the desert, brown everywhere else it spawns (savanna and plains)
        return level.getBiome(pos).is(Biomes.DESERT) ? HareVariants.YELLOW : HareVariants.BROWN;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
        return Objects.requireNonNull(super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag));
    }

    private void setVariantById(String id) {
        for (int i = 0; i < HareVariants.ALL.size(); i++) {
            if (HareVariants.ALL.get(i).id().equals(id)) {
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
