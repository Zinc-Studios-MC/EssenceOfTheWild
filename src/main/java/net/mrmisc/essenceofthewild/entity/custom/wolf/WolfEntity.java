package net.mrmisc.essenceofthewild.entity.custom.wolf;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WolfEntity extends Wolf implements GeoEntity, VariantCarrier {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(WolfEntity.class, EntityDataSerializers.INT);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.wolf.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.wolf.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.wolf.run");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.wolf.swim");
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.wolf.sit");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.wolf.attack");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final VariantSlot<Variant> variant = new VariantSlot<>(entityData, VARIANT, WolfVariants.SET);
    private int meatUses;

    public WolfEntity(EntityType<? extends Wolf> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        targetSelector.removeAllGoals(goal -> goal instanceof NonTameRandomTargetGoal<?>);
        targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false,
                prey -> prey instanceof Sheep || prey instanceof Rabbit || prey instanceof Fox));
        targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(VARIANT, 0);
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
    public boolean canAttack(LivingEntity target) {
        return !isBaby() && super.canAttack(target);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(isBaby() ? null : target);
    }

    @Override
    public boolean isAngry() {
        return !isBaby() && super.isAngry();
    }

    @Override
    public void tick() {
        if (!level().isClientSide && isBaby()) {
            setTarget(null);
            setRemainingPersistentAngerTime(0);
            setPersistentAngerTarget(null);
        }
        super.tick();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (isBaby()) {
            return false;
        }
        boolean hit = super.doHurtTarget(target);
        if (hit && !level().isClientSide) {
            triggerAnim("movement", "attack");
        }
        return hit;
    }

    public static boolean cookedMeat(ItemStack stack) {
        return stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_PORKCHOP) || stack.is(Items.COOKED_CHICKEN)
                || stack.is(Items.COOKED_MUTTON) || stack.is(Items.COOKED_RABBIT);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTame() && getHealth() >= getMaxHealth() && cookedMeat(stack)) {
            if (!getMainHandItem().isEmpty()) {
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
            if (!level().isClientSide) {
                setItemSlot(EquipmentSlot.MAINHAND, stack.copyWithCount(1));
                setDropChance(EquipmentSlot.MAINHAND, 0);
                meatUses = 2;
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float health = getHealth();
        boolean hurt = super.hurt(source, amount);
        ItemStack meat = getMainHandItem();
        if (hurt && !level().isClientSide && isTame() && isAlive() && getHealth() < health
                && meatUses > 0 && cookedMeat(meat)) {
            heal(meat.getFoodProperties(this).getNutrition());
            playSound(SoundEvents.GENERIC_EAT, 1, 1);
            gameEvent(GameEvent.EAT);
            if (--meatUses == 0) {
                setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
        return hurt;
    }

    public static boolean checkSpawnRules(EntityType<WolfEntity> type, ServerLevelAccessor level,
                                          MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (WolfVariants.at(level.getBiome(pos)).isEmpty()) {
            return false;
        }
        var ground = level.getBlockState(pos.below());
        return (ground.is(BlockTags.WOLVES_SPAWNABLE_ON) || ground.is(Blocks.STONE)
                || ground.is(Blocks.GRAVEL) || ground.is(Blocks.SNOW_BLOCK)) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                       @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if (tag == null || !variant.isStoredIn(tag)) {
            variant.set(WolfVariants.pick(level.getBiome(blockPosition()), random));
        }
        return super.finalizeSpawn(level, difficulty, reason, data, tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
        tag.putInt("MeatUses", meatUses);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Health", 99)) {
            setHealth(tag.getFloat("Health"));
        }
        if (variant.isStoredIn(tag)) {
            variant.load(tag);
        }
        meatUses = Math.max(0, Math.min(2, tag.getInt("MeatUses")));
        if (!isTame() || !cookedMeat(getMainHandItem())) {
            meatUses = 0;
        }
        if (meatUses == 0 && cookedMeat(getMainHandItem())) {
            setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        } else if (meatUses > 0) {
            getMainHandItem().setCount(1);
            setDropChance(EquipmentSlot.MAINHAND, 0);
        }
        if (isBaby()) {
            setRemainingPersistentAngerTime(0);
            setPersistentAngerTarget(null);
        }
    }

    @Override
    public Wolf getBreedOffspring(ServerLevel level, AgeableMob other) {
        WolfEntity child = EOTWEntities.WOLF.get().create(level);
        if (child != null) {
            Wolf parent = random.nextBoolean() ? this : (Wolf) other;
            child.setVariantById(parent instanceof WolfEntity wolf ? wolf.getVariantId() : getVariantId());
            if (getOwnerUUID() != null) {
                child.setOwnerUUID(getOwnerUUID());
                child.setTame(true);
            }
        }
        return child;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 2, this::movement).triggerableAnim("attack", ATTACK));
    }

    private PlayState movement(AnimationState<WolfEntity> state) {
        if (isInSittingPose()) {
            return state.setAndContinue(SIT);
        }
        if (isInWaterOrBubble()) {
            return state.setAndContinue(SWIM);
        }
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(getSpeed() > 0.3F || getDeltaMovement().horizontalDistanceSqr() > 0.01 ? RUN : WALK);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
