package net.mrmisc.essenceofthewild.entity.custom.rat;

import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.effect.custom.RabiesEffect;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.util.EOTWEntityUtils;

public class RatEntity extends TamableAnimal implements NeutralMob, VariantCarrier {

    public static final int HARVEST_INVENTORY_SIZE = 6;
    public static final float RABIES_CHANCE = 0.15F;

    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR =
            SynchedEntityData.defineId(RatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(RatEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<RatVariant> variant = new VariantSlot<>(this.entityData, VARIANT, RatVariants.SET);
    private static final EntityDataAccessor<Boolean> ANGRY =
            SynchedEntityData.defineId(RatEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RUNNING =
            SynchedEntityData.defineId(RatEntity.class, EntityDataSerializers.BOOLEAN);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    private int tameThreshold = 6;
    private int tameProgress = 0;

    @Nullable
    private BlockPos composterPos;

    public final SimpleContainer harvestInventory = new SimpleContainer(HARVEST_INVENTORY_SIZE);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public RatEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new RatHarvestGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.1D,
                Ingredient.of(EOTWItems.RED_ONION.get(), EOTWItems.SHEEP_CHEESE_WEDGE.get()), false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(VARIANT, 0);
        this.entityData.define(ANGRY, false);
        this.entityData.define(RUNNING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        boolean fighting = this.getTarget() != null || this.isAngry();
        this.entityData.set(ANGRY, fighting);
        this.entityData.set(RUNNING, this.getTarget() != null);
        this.updatePersistentAnger((ServerLevel) this.level(), true);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1F);
        } else {
            f = 0F;
        }
        this.walkAnimation.update(f, 0.2F);
    }

    public boolean isRatAngry() {
        return this.entityData.get(ANGRY);
    }

    public boolean isRunning() {
        return this.entityData.get(RUNNING);
    }

    @Override
    public boolean doHurtTarget(Entity pTarget) {
        boolean flag = super.doHurtTarget(pTarget);
        if (flag && !this.level().isClientSide()
                && pTarget instanceof Player player
                && !this.isOwnedBy(player)
                && this.random.nextFloat() < RABIES_CHANCE) {
            player.addEffect(new MobEffectInstance(EOTWEffects.RABIES.get(), RabiesEffect.TOTAL_DURATION));
        }
        return flag;
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        if (this.isTame() && isTamedMob(pTarget)) {
            return false;
        }
        return super.canAttack(pTarget);
    }

    @Override
    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (isTamedMob(pTarget)) {
            return false;
        }
        return super.wantsToAttack(pTarget, pOwner);
    }

    private static boolean isTamedMob(LivingEntity target) {
        if (target instanceof TamableAnimal tamable) {
            return tamable.isTame();
        }
        if (target instanceof AbstractHorse horse) {
            return horse.isTamed();
        }
        if (target instanceof OwnableEntity ownable) {
            return ownable.getOwnerUUID() != null;
        }
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (this.level().isClientSide()) {
            boolean canInteract = this.isOwnedBy(pPlayer)
                    || this.isTame()
                    || (!this.isTame() && itemstack.is(EOTWItems.SHEEP_CHEESE_WEDGE.get()))
                    || this.isFood(itemstack);
            return canInteract ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (!this.isTame()) {
            if (itemstack.is(EOTWItems.SHEEP_CHEESE_WEDGE.get())) {
                this.usePlayerItem(pPlayer, pHand, itemstack);
                this.tameProgress++;
                if (this.tameProgress >= this.tameThreshold
                        && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    this.tame(pPlayer);
                    this.setTarget(null);
                    this.setPersistentAngerTarget(null);
                    this.setRemainingPersistentAngerTime(0);
                    this.navigation.stop();
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }
            return super.mobInteract(pPlayer, pHand);
        }

        if (this.isOwnedBy(pPlayer)) {
            if (itemstack.getItem() instanceof DyeItem dyeItem) {
                DyeColor color = dyeItem.getDyeColor();
                if (color != this.getCollarColor()) {
                    this.setCollarColor(color);
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                }
            }

            boolean edible = this.isFood(itemstack) || itemstack.is(EOTWItems.SHEEP_CHEESE_WEDGE.get());
            if (edible && this.getHealth() < this.getMaxHealth()) {
                float nutrition = itemstack.getFoodProperties(this) != null
                        ? itemstack.getFoodProperties(this).getNutrition() : 2;
                this.heal(nutrition);
                this.usePlayerItem(pPlayer, pHand, itemstack);
                return InteractionResult.CONSUME;
            }

            if (itemstack.is(Items.STICK)) {
                EOTWEntityUtils.setRatClicked(this, pPlayer);
                pPlayer.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("message.essenceofthewild.rat.assign_prompt"),
                        true);
                return InteractionResult.CONSUME;
            }

            if (this.isFood(itemstack)) {
                return super.mobInteract(pPlayer, pHand);
            }

            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        RatEntity child = new RatEntity(EOTWEntities.RAT.get(), pLevel);
        child.setVariant(this.getVariant());
        if (this.isTame()) {
            child.setOwnerUUID(this.getOwnerUUID());
            child.setTame(true);
        }
        return child;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(EOTWItems.RED_ONION.get());
    }

    public void assignComposter(BlockPos pos) {
        this.composterPos = pos.immutable();
        this.setPersistenceRequired();
    }

    @Nullable
    public BlockPos getComposterPos() {
        return this.composterPos;
    }

    public boolean hasComposter() {
        return this.composterPos != null;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor pCollarColor) {
        this.entityData.set(DATA_COLLAR_COLOR, pCollarColor.getId());
    }

    public RatVariant getVariant() {
        return variant.get();
    }

    public void setVariant(RatVariant v) {
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

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
            MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.tameThreshold = 6 + this.random.nextInt(7);
        if (pDataTag != null && pDataTag.contains("Variant")) {
            variant.load(pDataTag);
        } else {
            setVariant(RatVariants.randomNatural(pLevel.getRandom()));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public static boolean checkRatSpawnRules(EntityType<RatEntity> type, ServerLevelAccessor level,
            MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        }
        BlockState below = level.getBlockState(pos.below());
        return below.isFaceSturdy(level, pos.below(), Direction.UP)
                && NaturalSpawner.isValidEmptySpawnBlock(level, pos, level.getBlockState(pos),
                        level.getFluidState(pos), type);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        variant.save(pCompound);
        pCompound.putByte("CollarColor", (byte) this.getCollarColor().getId());
        pCompound.putInt("TameThreshold", this.tameThreshold);
        pCompound.putInt("TameProgress", this.tameProgress);
        if (this.composterPos != null) {
            pCompound.putLong("ComposterPos", this.composterPos.asLong());
        }
        pCompound.put("HarvestItems", this.harvestInventory.createTag());
        this.addPersistentAngerSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        variant.load(pCompound);
        if (pCompound.contains("CollarColor")) {
            this.setCollarColor(DyeColor.byId(pCompound.getByte("CollarColor")));
        }
        if (pCompound.contains("TameThreshold")) {
            this.tameThreshold = pCompound.getInt("TameThreshold");
        }
        this.tameProgress = pCompound.getInt("TameProgress");
        this.composterPos = pCompound.contains("ComposterPos")
                ? BlockPos.of(pCompound.getLong("ComposterPos")) : null;
        if (pCompound.contains("HarvestItems", Tag.TAG_LIST)) {
            this.harvestInventory.fromTag(pCompound.getList("HarvestItems", Tag.TAG_COMPOUND));
        }
        if (!this.level().isClientSide()) {
            this.readPersistentAngerSaveData(this.level(), pCompound);
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (int i = 0; i < this.harvestInventory.getContainerSize(); i++) {
            ItemStack stack = this.harvestInventory.getItem(i);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }
        this.harvestInventory.clearContent();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public net.minecraft.sounds.SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    @Override
    public net.minecraft.sounds.SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }
}
