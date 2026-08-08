package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

import static net.minecraft.world.item.Items.*;

public class SheepEntity extends Sheep implements IForgeShearable, VariantCarrier, GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sheep.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sheep.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sheep.run");
    private static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.sheep.eat");

    // ticks a sheep keeps running after one hit
    private static final int PANIC_DURATION = 60;

    // lambs reuse the adult clips but move faster for their size, so speed the playback up or their
    // legs slide, eating is left alone since the goal paces that not the movement speed
    private static final double BABY_GAIT_SPEED = 1.5D;

    private static final EntityDataAccessor<Byte> WOOL_ID =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> EATING =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BOOLEAN);

    // anims run clientside but only the server knows about damage, so sync the flag and keep the countdown server only
    private static final EntityDataAccessor<Boolean> PANICKING =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int panicTicks;

    public SheepEntity(EntityType<? extends Sheep> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WOOL_ID, (byte) 0);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(EATING, false);
        this.entityData.define(PANICKING, false);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack filledResult = ItemUtils.createFilledResult(itemstack, pPlayer, EOTWItems.SHEEP_MILK_BUCKET.get().getDefaultInstance());
            pPlayer.setItemInHand(pHand, filledResult);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // the 5 tick transition is what blends idle/walk/run instead of snapping between them
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController)
                .setAnimationSpeedHandler(SheepEntity::gaitSpeed));
        // eating gets its own controller so it can play on top of whatever the legs are doing
        controllers.add(new AnimationController<>(this, "eat", 3, this::eatController));
    }

    private PlayState movementController(AnimationState<SheepEntity> state) {
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        // go by hurt, not speed, a sheep walking to grass moves about as fast as one running from a wolf
        return state.setAndContinue(this.isPanicking() ? RUN : WALK);
    }

    private PlayState eatController(AnimationState<SheepEntity> state) {
        if (!this.isEating()) {
            state.getController().forceAnimationReset();
            return PlayState.STOP;
        }
        return state.setAndContinue(EAT);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private static double gaitSpeed(SheepEntity sheep) {
        return sheep.isBaby() ? BABY_GAIT_SPEED : 1.0D;
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

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, SheepVariants.ALL.indexOf(variant));
    }

    public void setEating(boolean eating){
        this.entityData.set(EATING, eating);
    }

    public boolean isEating(){
        return this.entityData.get(EATING);
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
        if (dataTag != null && dataTag.contains("Variant")) {
            setVariantById(dataTag.getString("Variant"));
            return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        }
        setVariant(pickVariant(level.getLevel(), blockPosition()));
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

    public SheepVariant getVariant() {
        int index = this.entityData.get(VARIANT);
        return index >= 0 && index < SheepVariants.ALL.size()
                ? SheepVariants.ALL.get(index)
                : SheepVariants.ALL.get(0);
    }

    public void setVariant(SheepVariant variant) {
        this.entityData.set(VARIANT, SheepVariants.ALL.indexOf(variant));
    }

    @Override
    public String getVariantId() {
        return getVariant().id();
    }

    @Override
    public void setVariantById(String id) {
        for (int i = 0; i < SheepVariants.ALL.size(); i++) {
            if (SheepVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    private SheepVariant pickVariant(Level level, BlockPos pos) {
        if(level.getBiome(pos).is(Tags.Biomes.IS_COLD)){
            return SheepVariants.COLD;
        }
        if(level.getBiome(pos).is(Tags.Biomes.IS_HOT)){
            return SheepVariants.WARM;
        }
        return level.random.nextBoolean() ? SheepVariants.BASIC : SheepVariants.BASIC_GREY;
    }
}