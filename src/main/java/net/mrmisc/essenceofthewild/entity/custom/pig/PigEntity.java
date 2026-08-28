package net.mrmisc.essenceofthewild.entity.custom.pig;

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
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PigEntity extends Pig {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public PigEntity(EntityType<? extends Pig> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(PigEntity.class, EntityDataSerializers.INT);

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
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < PigVariants.ALL.size())
                ? PigVariants.ALL.get(i)
                : PigVariants.ALL.get(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", getVariant().id());
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, PigVariants.ALL.indexOf(variant));
    }

    private MobVariant pickVariant(Level level, BlockPos pos) {
        if (level.getBiome(pos).is(Tags.Biomes.IS_COLD)) {
            return PigVariants.COLD;
        } else if (level.getBiome(pos).is(Tags.Biomes.IS_HOT)) {
            return PigVariants.WARM;
        }
        return level.random.nextBoolean() ? PigVariants.BASIC : PigVariants.BASIC_GREY;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    private void setVariantById(String id) {
        for (int i = 0; i < PigVariants.ALL.size(); i++) {
            if (PigVariants.ALL.get(i).id().equals(id)) {
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
