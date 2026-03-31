package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.world.item.Items.*;

public class SheepEntity extends Sheep implements IForgeShearable {

    private static final EntityDataAccessor<Byte> WOOL_ID =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> EATING =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public SheepEntity(EntityType<? extends Sheep> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WOOL_ID, (byte) 0);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(EATING, false);
    }



    private MobVariant pickVariant(Level level, BlockPos pos) {
        if (level.getBiome(pos).is(Tags.Biomes.IS_COLD)) {
            return SheepVariants.COLD;
        } else if (level.getBiome(pos).is(Tags.Biomes.IS_HOT)) {
            return SheepVariants.WARM;
        }
        return level.random.nextBoolean() ? SheepVariants.BASIC : SheepVariants.BASIC_GREY;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    public MobVariant getVariant() {
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < SheepVariants.ALL.size())
                ? SheepVariants.ALL.get(i)
                : SheepVariants.ALL.get(0);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.isEating()) {
            this.eatAnimationState.startIfStopped(this.tickCount);
        } else {
            this.eatAnimationState.stop();
        }

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, SheepVariants.ALL.indexOf(variant));
    }

    public void setEating(boolean eating){
        this.entityData.set(EATING, eating);
    }

    public boolean isEating(){
        return this.entityData.get(EATING);
    }

    private void setVariantById(String id) {
        for (int i = 0; i < SheepVariants.ALL.size(); i++) {
            if (SheepVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof EatBlockGoal);        SheepEatBlockGoal eatBlockGoal = new SheepEatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, eatBlockGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                                 DifficultyInstance difficulty,
                                                 MobSpawnType reason,
                                                 SpawnGroupData spawnData,
                                                 CompoundTag dataTag) {
        this.setColor(DyeColor.WHITE);
        if (dataTag == null || !dataTag.contains("Variant")) {
            this.setVariant(pickVariant(level.getLevel(), this.blockPosition()));
        }

        return Objects.requireNonNull(super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", getVariant().id());
        tag.putByte("Color", (byte) this.getColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariantById(tag.getString("Variant"));
        this.setColor(DyeColor.byId(tag.getByte("Color")));
    }


    @Override
    public Sheep getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        SheepEntity child = new SheepEntity(EOTWEntities.SHEEP.get(), level);
        child.setColor(this.getColor());
        child.setVariant(this.getVariant());
        return child;
    }


    @Override
    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(WOOL_ID) & 15);
    }

    @Override
    public void setColor(DyeColor color) {
        byte b = this.entityData.get(WOOL_ID);
        this.entityData.set(WOOL_ID, (byte)(b & 240 | color.getId() & 15));
    }


}