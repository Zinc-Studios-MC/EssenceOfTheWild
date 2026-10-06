package net.mrmisc.essenceofthewild.entity.custom.fox;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.worldgen.util.EOTWBiomeTags;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class FoxEntity extends Fox implements GeoEntity, OwnableEntity {
    private static final EntityDataAccessor<Boolean> BLACK = SynchedEntityData.defineId(FoxEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(FoxEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> ORDERED_SIT = SynchedEntityData.defineId(FoxEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(FoxEntity.class, EntityDataSerializers.INT);
    private static final UUID POUNCE_DAMAGE = UUID.fromString("5c3cc1b4-a3c5-4e76-8c6e-7f4e2cd92f51");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.fox.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.fox.walk");
    private static final RawAnimation STALK = RawAnimation.begin().thenLoop("animation.fox.walk2");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.fox.run");
    private static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.fox.sleep");
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.fox.sit");
    private static final RawAnimation POUNCE = RawAnimation.begin().thenLoop("animation.fox.pounce");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean petGoals;
    private int pounceCooldown;

    public FoxEntity(EntityType<? extends Fox> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(BLACK, false);
        entityData.define(OWNER, Optional.empty());
        entityData.define(ORDERED_SIT, false);
        entityData.define(COLLAR_COLOR, DyeColor.RED.getId());
    }

    public boolean isBlack() {
        return entityData.get(BLACK);
    }

    public void setBlack(boolean black) {
        entityData.set(BLACK, black);
    }

    @Override
    public UUID getOwnerUUID() {
        return entityData.get(OWNER).orElse(null);
    }

    public boolean isTame() {
        return getOwnerUUID() != null;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(entityData.get(COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor color) {
        entityData.set(COLLAR_COLOR, color.getId());
    }

    public boolean isOrderedToSit() {
        return entityData.get(ORDERED_SIT);
    }

    public void setOrderedToSit(boolean sit) {
        entityData.set(ORDERED_SIT, sit);
        setSitting(sit && getMainHandItem().isEmpty());
        if (isSitting()) {
            setSpeed(0);
            setXxa(0);
            setJumping(false);
        }
        setTarget(null);
        getNavigation().stop();
    }

    public void tame(Player player) {
        entityData.set(OWNER, Optional.of(player.getUUID()));
        setCollarColor(DyeColor.RED);
        setPersistenceRequired();
        installPetGoals();
    }

    private void installPetGoals() {
        if (!isTame() || petGoals || level().isClientSide) {
            return;
        }
        goalSelector.getAvailableGoals().forEach(goal -> goal.stop());
        targetSelector.getAvailableGoals().forEach(goal -> goal.stop());
        goalSelector.removeAllGoals(goal -> true);
        targetSelector.removeAllGoals(goal -> true);
        clearStates();
        setIsPouncing(false);
        setTarget(null);
        setCanPickUpLoot(false);
        petGoals = true;
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new Goal() {
            {
                setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
            }

            @Override
            public boolean canUse() {
                return isOrderedToSit() && getMainHandItem().isEmpty() && onGround() && !isInWaterOrBubble();
            }

            @Override
            public void start() {
                getNavigation().stop();
                setSitting(true);
            }

            @Override
            public void stop() {
                setSitting(false);
            }
        });
        goalSelector.addGoal(2, new FoxFollowGoal(this));
        goalSelector.addGoal(3, new FoxHuntGoal(this));
        goalSelector.addGoal(4, new BreedGoal(this, 1) {
            @Override
            public boolean canUse() {
                return !isOrderedToSit() && getMainHandItem().isEmpty() && super.canUse();
            }
        });
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1) {
            @Override
            public boolean canUse() {
                return !isOrderedToSit() && getMainHandItem().isEmpty() && super.canUse();
            }
        });
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new FoxOwnerTargetGoal(this, true));
        targetSelector.addGoal(2, new FoxOwnerTargetGoal(this, false));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public boolean readyToHunt() {
        return isTame() && isAlive() && !isBaby() && !isOrderedToSit() && getMainHandItem().isEmpty();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isSitting();
    }

    public boolean canPounce() {
        return readyToHunt() && pounceCooldown == 0;
    }

    public void beginPounce() {
        setIsPouncing(true);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (pounceCooldown > 0) {
                pounceCooldown--;
            }
            if (isTame() && !getMainHandItem().isEmpty()) {
                setTarget(null);
            }
        }
        super.tick();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (isTame()) {
            if (!readyToHunt() || getOwnerUUID().equals(target.getUUID()) || isAlliedTo(target)) {
                return false;
            }
            if (target instanceof OwnableEntity pet && getOwnerUUID().equals(pet.getOwnerUUID())) {
                return false;
            }
            if (target instanceof Player player && getOwner() instanceof Player owner && !owner.canHarmPlayer(player)) {
                return false;
            }
        }
        return super.canAttack(target);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target != null && isTame() && !canAttack(target) ? null : target);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isTame() && isBaby() && stack.is(Items.CHICKEN)) {
            if (!level().isClientSide && !ForgeEventFactory.onAnimalTame(this, player)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                tame(player);
                level().broadcastEntityEvent(this, (byte) 18);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && player.getUUID().equals(getOwnerUUID()) && stack.getItem() instanceof DyeItem dye) {
            if (!level().isClientSide && dye.getDyeColor() != getCollarColor()) {
                setCollarColor(dye.getDyeColor());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isTame() && player.getUUID().equals(getOwnerUUID()) && !isFood(stack)) {
            if (!level().isClientSide) {
                setOrderedToSit(!isOrderedToSit());
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && isTame() && getMainHandItem().isEmpty() && !level().isClientSide) {
            setOrderedToSit(false);
        }
        return hurt;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (isTame() && (!readyToHunt() || !(target instanceof LivingEntity living) || !canAttack(living))) {
            return false;
        }
        boolean pounce = isTame() && isPouncing();
        var damage = getAttribute(Attributes.ATTACK_DAMAGE);
        if (pounce) {
            damage.addTransientModifier(new AttributeModifier(POUNCE_DAMAGE, "fox pounce", 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        boolean hit;
        try {
            hit = super.doHurtTarget(target);
        } finally {
            if (pounce) {
                damage.removeModifier(POUNCE_DAMAGE);
            }
        }
        if (hit && pounce) {
            pounceCooldown = 400;
        }
        if (hit && isTame() && isPouncing() && target instanceof Player player && getMainHandItem().isEmpty()) {
            var inventory = player.getInventory();
            var slots = new ArrayList<Integer>();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                if (!inventory.getItem(i).isEmpty()) {
                    slots.add(i);
                }
            }
            if (!slots.isEmpty()) {
                int slot = slots.get(random.nextInt(slots.size()));
                fetch(inventory.removeItemNoUpdate(slot));
                inventory.setChanged();
                player.containerMenu.broadcastChanges();
            }
        }
        return hit;
    }

    public void fetch(ItemStack stack) {
        setItemSlot(EquipmentSlot.MAINHAND, stack);
        setGuaranteedDrop(EquipmentSlot.MAINHAND);
        setTarget(null);
        setSitting(false);
    }

    public void deliver() {
        ItemStack stack = getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }
        ItemEntity drop = new ItemEntity(level(), getX(), getY() + 0.3, getZ(), stack.copy());
        drop.setDefaultPickUpDelay();
        drop.setTarget(getOwnerUUID());
        if (level().addFreshEntity(drop)) {
            setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return !isTame() && super.canHoldItem(stack);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                       @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data, tag);
        boolean blackSpawn = (reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION)
                && level.getBiome(blockPosition()).is(EOTWBiomeTags.BLACK_FOX_SPAWNS);
        setBlack(getVariant() == Type.RED && (blackSpawn || random.nextInt(4) == 0));
        petGoals = false;
        installPetGoals();
        return result;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Black", isBlack());
        if (isTame()) {
            tag.putUUID("Owner", getOwnerUUID());
        }
        tag.putBoolean("OrderedSit", isOrderedToSit());
        tag.putInt("PounceCooldown", pounceCooldown);
        tag.putByte("CollarColor", (byte) getCollarColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Black")) {
            setBlack(tag.getBoolean("Black"));
        }
        entityData.set(OWNER, tag.hasUUID("Owner") ? Optional.of(tag.getUUID("Owner")) : Optional.empty());
        entityData.set(ORDERED_SIT, tag.getBoolean("OrderedSit"));
        setCollarColor(tag.contains("CollarColor") ? DyeColor.byId(tag.getByte("CollarColor")) : DyeColor.RED);
        pounceCooldown = Math.max(0, Math.min(400, tag.getInt("PounceCooldown")));
        petGoals = false;
        installPetGoals();
    }

    @Override
    public Fox getBreedOffspring(ServerLevel level, AgeableMob other) {
        FoxEntity child = EOTWEntities.FOX.get().create(level);
        if (child != null) {
            Fox parent = random.nextBoolean() ? this : (Fox) other;
            child.setVariant(parent.getVariant());
            child.setBlack(parent instanceof FoxEntity fox && fox.isBlack());
        }
        return child;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 2, this::movement));
    }

    private PlayState movement(AnimationState<FoxEntity> state) {
        if (isSleeping()) {
            return state.setAndContinue(SLEEP);
        }
        if (isPouncing() || isFaceplanted()) {
            return state.setAndContinue(POUNCE);
        }
        if (isSitting()) {
            return state.setAndContinue(SIT);
        }
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        if (isCrouching() || isInterested()) {
            return state.setAndContinue(STALK);
        }
        return state.setAndContinue(getDeltaMovement().horizontalDistanceSqr() > 0.01 ? RUN : WALK);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
