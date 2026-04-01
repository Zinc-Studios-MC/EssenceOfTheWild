package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MooshroomEntity extends MushroomCow {
    @Nullable
    private UUID lastLightningBoltUUID;
    @Nullable
    private MobEffect effect;
    private int effectDuration;
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(MooshroomEntity.class, EntityDataSerializers.INT);

    public MooshroomEntity(EntityType<? extends MushroomCow> type, Level level) {
        super(type, level);
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
        this.entityData.define(VARIANT, 0); // 0 = red, 1 = brown
    }

    public MobVariant getVariantMooshroom() {
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < MooshroomVariants.ALL.size())
                ? MooshroomVariants.ALL.get(i)
                : MooshroomVariants.ALL.get(0);
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, MooshroomVariants.ALL.indexOf(variant));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", getVariantMooshroom().id());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setVariantById(tag.getString("Variant"));
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
        MobVariant variant = this.getVariantMooshroom();
        if(variant == MooshroomVariants.RED){
            return Blocks.RED_MUSHROOM.defaultBlockState();
        }
        return Blocks.BROWN_MUSHROOM.defaultBlockState();
    }

    private void setVariantById(String id) {
        for (int i = 0; i < MooshroomVariants.ALL.size(); i++) {
            if (MooshroomVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
    }

    @Nullable
    public MooshroomEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        MooshroomEntity mushroomcow = new MooshroomEntity(EOTWEntities.MOOSHROOM.get(), pLevel);
        mushroomcow.setVariant(this.getOffspringType((MooshroomEntity) pOtherParent));
        return mushroomcow;
    }

    private MobVariant getOffspringType(MooshroomEntity pMate) {
        MobVariant mushroomcow$mushroomtype = this.getVariantMooshroom();
        MobVariant mushroomcow$mushroomtype1 = pMate.getVariantMooshroom();
        MobVariant mushroomcow$mushroomtype2;
        if (mushroomcow$mushroomtype == mushroomcow$mushroomtype1 && this.random.nextInt(1024) == 0) {
            mushroomcow$mushroomtype2 = mushroomcow$mushroomtype == MooshroomVariants.BROWN ? MooshroomVariants.RED : MooshroomVariants.BROWN;
        } else {
            mushroomcow$mushroomtype2 = this.random.nextBoolean() ? mushroomcow$mushroomtype : mushroomcow$mushroomtype1;
        }

        return mushroomcow$mushroomtype2;
    }

}
