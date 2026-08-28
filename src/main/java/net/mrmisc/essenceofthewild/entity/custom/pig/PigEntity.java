package net.mrmisc.essenceofthewild.entity.custom.pig;

import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.entity.util.LevelBiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PigEntity extends Pig implements VariantCarrier {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public PigEntity(EntityType<? extends Pig> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(PigEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<MobVariant> variant = new VariantSlot<>(this.entityData, VARIANT, PigVariants.SET);

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public MobVariant getVariant() {
        return variant.get();
    }

    @Override
    public String getVariantId() {
        return variant.id();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
    }

    public void setVariant(MobVariant v) {
        variant.set(v);
    }

    private MobVariant pickVariant(Level level, BlockPos pos) {
        return PigVariants.pick(new LevelBiomeQuery(level, pos), level.random::nextBoolean);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void setVariantById(String id) {
        variant.setById(id);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if(this.hasPassenger(pPassenger)){
            double x = this.getX();
            double y = this.getY() + 0.35;
            double z = this.getZ();
            pCallback.accept(pPassenger, x, y, z);
        }
    }

    @Override
    public @Nullable Pig getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        PigEntity child = new PigEntity(EOTWEntities.PIG.get(), pLevel);
        child.setVariant(this.getVariant());
        return child;
    }
}
