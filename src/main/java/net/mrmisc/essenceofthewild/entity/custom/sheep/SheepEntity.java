package net.mrmisc.essenceofthewild.entity.custom.sheep;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.Variants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SheepEntity extends Sheep implements IForgeShearable {
    private static final EntityDataAccessor<Byte> WOOL_ID = SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(SheepEntity.class, EntityDataSerializers.INT);
    private static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
        map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        map.put(DyeColor.LIME, Blocks.LIME_WOOL);
        map.put(DyeColor.PINK, Blocks.PINK_WOOL);
        map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        map.put(DyeColor.RED, Blocks.RED_WOOL);
        map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });

    private static float[] createSheepColor(DyeColor color) {
        if (color == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = color.getTextureDiffuseColors();
            return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
        }
    }
    private Variants pickVariantBasedOnBiome(Level level) {
        if (level.getBiome(blockPosition()).is(Tags.Biomes.IS_COLD)) {
            return Variants.COLD;
        } else if (level.getBiome(blockPosition()).is(Tags.Biomes.IS_HOT)) {
            return Variants.WARM;
        }
        return level().getRandom().nextBoolean() ? Variants.BASIC : Variants.BASIC_2;
    }

    public Variants getVariantEnum() {
        return Variants.values()[getVariant()];
    }

    public void setVariantEnum(Variants variant) {
        setVariant(variant.ordinal());
    }

    public void ate() {
        super.ate();
        this.setSheared(false);
        if (this.isBaby()) {
            this.ageUp(60);
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Sheared", this.isSheared());
        pCompound.putInt("Variant", getVariant());
        pCompound.putByte("Color", (byte)this.getColor().getId());
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
            pPlayer.setItemInHand(pHand, itemstack1);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    public boolean isSheared() {
        return (this.entityData.get(WOOL_ID) & 16) != 0;
    }

    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.setColor(DyeColor.WHITE);
        Variants variant = pickVariantBasedOnBiome(pLevel.getLevel());
        setVariantEnum(variant);
        return Objects.requireNonNull(super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag));
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSheared(pCompound.getBoolean("Sheared"));
        setVariant(pCompound.getInt("Variant"));
        this.setColor(DyeColor.byId(pCompound.getByte("Color")));
    }

    public SheepEntity(EntityType<? extends Sheep> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private DyeColor getOffspringColor(Animal pFather, Animal pMother) {
        DyeColor dyecolor = ((Sheep)pFather).getColor();
        DyeColor dyecolor1 = ((Sheep)pMother).getColor();
        CraftingContainer craftingcontainer = makeContainer(dyecolor, dyecolor1);
        return this.level().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingcontainer, this.level()).map((p_289444_) -> p_289444_.assemble(craftingcontainer, this.level().registryAccess())).map(ItemStack::getItem).filter(DyeItem.class::isInstance).map(DyeItem.class::cast).map(DyeItem::getDyeColor).orElseGet(() -> this.level().random.nextBoolean() ? dyecolor : dyecolor1);
    }

    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        super.aiStep();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SHEEP_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    private static CraftingContainer makeContainer(DyeColor pFatherColor, DyeColor pMotherColor) {
        CraftingContainer craftingcontainer = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
            public ItemStack quickMoveStack(Player player, int p_218265_) {
                return ItemStack.EMPTY;
            }
            public boolean stillValid(Player player) {
                return false;
            }
        }, 2, 1);
        craftingcontainer.setItem(0, new ItemStack(DyeItem.byColor(pFatherColor)));
        craftingcontainer.setItem(1, new ItemStack(DyeItem.byColor(pMotherColor)));
        return craftingcontainer;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WOOL_ID, (byte) 0);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public Sheep getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        SheepEntity sheep = new SheepEntity(EOTWEntities.SHEEP.get(), pLevel);
        sheep.setColor(this.getOffspringColor(this, (Sheep) pOtherParent));
        return sheep;
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(WOOL_ID) & 15);
    }

    public void setColor(DyeColor pDyeColor) {
        byte b0 = this.entityData.get(WOOL_ID);
        this.entityData.set(WOOL_ID, (byte)(b0 & 240 | pDyeColor.getId() & 15));
    }

    public void setSheared(boolean pSheared) {
        byte b0 = this.entityData.get(WOOL_ID);
        if (pSheared) {
            this.entityData.set(WOOL_ID, (byte)(b0 | 16));
        } else {
            this.entityData.set(WOOL_ID, (byte)(b0 & -17));
        }

    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level level, BlockPos pos) {
        return readyForShearing();
    }

    @NotNull
    @Override
    public List<ItemStack> onSheared(@javax.annotation.Nullable Player player, @org.jetbrains.annotations.NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!world.isClientSide) {
            this.setSheared(true);
            int i = 1 + this.random.nextInt(3);

            List<ItemStack> items = new ArrayList<>();
            for (int j = 0; j < i; ++j) {
                items.add(new ItemStack(ITEM_BY_DYE.get(this.getColor())));
            }
            return items;
        }
        return Collections.emptyList();
    }
}
