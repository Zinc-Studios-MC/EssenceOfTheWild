package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowVariants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MooshroomEntity extends MushroomCow implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.mooshroom.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.mooshroom.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.mooshroom.run");

    private static final int PANIC_DURATION = 60;

    private static final double BABY_GAIT_SPEED = 1.5D;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Nullable
    private UUID lastLightningBoltUUID;
    @Nullable
    private MobEffect effect;
    private int effectDuration;
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(MooshroomEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<Variant> variant = new VariantSlot<>(this.entityData, VARIANT, MooshroomVariants.SET);
    private static final EntityDataAccessor<Boolean> PANICKING =
            SynchedEntityData.defineId(MooshroomEntity.class, EntityDataSerializers.BOOLEAN);

    private int panicTicks;

    public MooshroomEntity(EntityType<? extends MushroomCow> type, Level level) {
        super(type, level);
    }

    public static boolean checkMooshroomSpawnRules(EntityType<? extends MooshroomEntity> type, LevelAccessor level,
                                                  MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController)
                .setAnimationSpeedHandler(MooshroomEntity::gaitSpeed));
    }

    private PlayState movementController(AnimationState<MooshroomEntity> state) {
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isPanicking() ? RUN : WALK);
    }

    private static double gaitSpeed(MooshroomEntity mooshroom) {
        return mooshroom.isBaby() ? BABY_GAIT_SPEED : 1.0D;
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.BOWL) && !this.isBaby()) {
            boolean flag = false;
            ItemStack itemstack1;
            if (this.effect != null) {
                flag = true;
                itemstack1 = new ItemStack(Items.SUSPICIOUS_STEW);
                SuspiciousStewItem.saveMobEffect(itemstack1, this.effect, this.effectDuration);
                this.effect = null;
                this.effectDuration = 0;
            } else {
                itemstack1 = new ItemStack(Items.MUSHROOM_STEW);
            }

            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, pPlayer, itemstack1, false);
            pPlayer.setItemInHand(pHand, itemstack2);
            SoundEvent soundevent;
            if (flag) {
                soundevent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            } else {
                soundevent = SoundEvents.MOOSHROOM_MILK;
            }

            this.playSound(soundevent, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.getVariant() == MushroomType.BROWN && itemstack.is(ItemTags.SMALL_FLOWERS)) {
            if (this.effect != null) {
                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX() + this.random.nextDouble() / 2.0D, this.getY(0.5D), this.getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
                }
            } else {
                Optional<Pair<MobEffect, Integer>> optional = this.getEffectFromItemStack(itemstack);
                if (optional.isEmpty()) {
                    return InteractionResult.PASS;
                }

                Pair<MobEffect, Integer> pair = optional.get();
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                for(int j = 0; j < 4; ++j) {
                    this.level().addParticle(ParticleTypes.EFFECT, this.getX() + this.random.nextDouble() / 2.0D, this.getY(0.5D), this.getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
                }

                this.effect = pair.getLeft();
                this.effectDuration = pair.getRight();
                this.playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    private Optional<Pair<MobEffect, Integer>> getEffectFromItemStack(ItemStack pStack) {
        SuspiciousEffectHolder suspiciouseffectholder = SuspiciousEffectHolder.tryGet(pStack.getItem());
        return suspiciouseffectholder != null ? Optional.of(Pair.of(suspiciouseffectholder.getSuspiciousEffect(), suspiciouseffectholder.getEffectDuration())) : Optional.empty();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(PANICKING, false);
    }

    public Variant getVariantMooshroom() {
        return variant.get();
    }

    public void setVariant(Variant v) {
        variant.set(v);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        variant.save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        variant.load(tag);
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        UUID uuid = pLightning.getUUID();
        if (!uuid.equals(this.lastLightningBoltUUID)) {
            this.setVariant(this.getVariantMooshroom() == MooshroomVariants.RED ? MooshroomVariants.BROWN : MooshroomVariants.RED);
            this.lastLightningBoltUUID = uuid;
            this.playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
        }
    }

    @Override
    public java.util.@NotNull List<ItemStack> onSheared(@org.jetbrains.annotations.Nullable Player player, @org.jetbrains.annotations.NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        this.gameEvent(GameEvent.SHEAR, player);
        return shearInternal(player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS);
    }

    @Override
    public void shear(SoundSource pCategory) {
        shearInternal(pCategory).forEach(s -> this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(1.0D), this.getZ(), s)));
    }

    private java.util.List<ItemStack> shearInternal(SoundSource pCategory) {
        this.level().playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, pCategory, 1.0F, 1.0F);
        if (!this.level().isClientSide()) {
            CowEntity cow = new CowEntity(EOTWEntities.COW.get(), level());
            cow.setVariant(this.getVariantMooshroom() == MooshroomVariants.RED ? CowVariants.BASIC : CowVariants.BASIC_BROWN);
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.discard();
            cow.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            cow.setHealth(this.getHealth());
            cow.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
                cow.setCustomName(this.getCustomName());
                cow.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistenceRequired()) {
                cow.setPersistenceRequired();
            }

            cow.setInvulnerable(this.isInvulnerable());
            this.level().addFreshEntity(cow);

            List<ItemStack> items = new java.util.ArrayList<>();
            for(int i = 0; i < 5; ++i) {
                items.add(new ItemStack(this.getBlockToDrop().getBlock()));
            }
            return items;
        }
        return java.util.Collections.emptyList();

    }

    public BlockState getBlockToDrop(){
        Variant variant = this.getVariantMooshroom();
        if(variant == MooshroomVariants.RED){
            return Blocks.RED_MUSHROOM.defaultBlockState();
        }
        return Blocks.BROWN_MUSHROOM.defaultBlockState();
    }

    @Nullable
    public MooshroomEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        MooshroomEntity mushroomcow = new MooshroomEntity(EOTWEntities.MOOSHROOM.get(), pLevel);
        mushroomcow.setVariant(this.getOffspringType((MooshroomEntity) pOtherParent));
        return mushroomcow;
    }

    private Variant getOffspringType(MooshroomEntity pMate) {
        Variant mushroomcow$mushroomtype = this.getVariantMooshroom();
        Variant mushroomcow$mushroomtype1 = pMate.getVariantMooshroom();
        Variant mushroomcow$mushroomtype2;
        if (mushroomcow$mushroomtype == mushroomcow$mushroomtype1 && this.random.nextInt(1024) == 0) {
            mushroomcow$mushroomtype2 = mushroomcow$mushroomtype == MooshroomVariants.BROWN ? MooshroomVariants.RED : MooshroomVariants.BROWN;
        } else {
            mushroomcow$mushroomtype2 = this.random.nextBoolean() ? mushroomcow$mushroomtype : mushroomcow$mushroomtype1;
        }

        return mushroomcow$mushroomtype2;
    }

}
