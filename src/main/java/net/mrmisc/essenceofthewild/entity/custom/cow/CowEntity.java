package net.mrmisc.essenceofthewild.entity.custom.cow;

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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CowEntity extends Cow implements VariantCarrier{

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public CowEntity(EntityType<? extends Cow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(CowEntity.class, EntityDataSerializers.INT);

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", getVariantId());
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag != null && pDataTag.contains("Variant")) {
            setVariantById(pDataTag.getString("Variant"));
            return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        }
        setVariant(pickVariant(pLevel.getLevel(), blockPosition()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
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

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setVariantById(tag.getString("Variant"));
    }

    public Cow getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        CowEntity child = new CowEntity(EOTWEntities.COW.get(), pLevel);
        child.setVariant(this.getVariant());
        return child;
    }

    public CowVariant getVariant() {
        int index = this.entityData.get(VARIANT);
        return index >= 0 && index < CowVariants.ALL.size()
                ? CowVariants.ALL.get(index)
                : CowVariants.ALL.get(0);
    }

    public void setVariant(CowVariant variant) {
        this.entityData.set(VARIANT, CowVariants.ALL.indexOf(variant));
    }

    @Override
    public String getVariantId() {
        return getVariant().id();
    }

    @Override
    public void setVariantById(String id) {
        for (int i = 0; i < CowVariants.ALL.size(); i++) {
            if (CowVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    private CowVariant pickVariant(Level level, BlockPos pos) {
        if(level.getBiome(pos).is(Tags.Biomes.IS_COLD)){
            return CowVariants.COLD;
        }
        if(level.getBiome(pos).is(Tags.Biomes.IS_HOT)){
            return CowVariants.WARM;
        }
        if(level.getBiome(pos).is(Tags.Biomes.IS_COLD)){
            return CowVariants.COLD;
        }
        return level.random.nextBoolean() ? CowVariants.BASIC : CowVariants.BASIC_BROWN;
    }
}
