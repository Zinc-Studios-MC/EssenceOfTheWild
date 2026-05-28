package net.mrmisc.essenceofthewild.block.entity.custom.freezer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.item.custom.IceCream;
import net.mrmisc.essenceofthewild.menu.freezer.WoodenFreezerMenu;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;
import net.mrmisc.essenceofthewild.recipe.freezer.WoodenFreezerRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WoodenFreezerBlockEntity extends BlockEntity implements MenuProvider {
    public static final int CONE_SLOT_0 = 0;
    public static final int CONE_SLOT_1 = 1;
    public static final int CONE_SLOT_2 = 2;
    public static final int FLAVOR_SLOT = 3;
    public static final int ICE_SLOT = 4;
    public static final int MILK_SLOT = 5;
    public static final int SLOT_COUNT = 6;

    public static final int MAX_MILK_LEVEL = 9;
    public static final int MAX_ICE_LEVEL = 6;

    private static final int ICE_FROM_BLOCK = 6;
    private static final int MILK_FROM_BUCKET = 3;
    private static final int BLOCK_MILK_LEVELS = 3;
    public static final int MAX_PROGRESS = 200;

    private int milkLevel = 0;
    private int iceLevel = 0;
    private int progress = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            syncState();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case CONE_SLOT_0, CONE_SLOT_1, CONE_SLOT_2 -> stack.is(EOTWItems.CONE.get()) && !hasIceCreamResults();
                case FLAVOR_SLOT -> isFreezerIngredient(stack);
                case ICE_SLOT -> stack.is(Items.ICE);
                case MILK_SLOT -> stack.is(EOTWItems.SHEEP_MILK_BUCKET.get());
                default -> false;
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            return switch (slot) {
                case CONE_SLOT_0, CONE_SLOT_1, CONE_SLOT_2 -> 1;
                case MILK_SLOT -> 1;
                default -> super.getSlotLimit(slot);
            };
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> MAX_PROGRESS;
                case 2 -> milkLevel;
                case 3 -> iceLevel;
                case 4 -> getCraftableConeCount();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = Mth.clamp(value, 0, MAX_PROGRESS);
                case 2 -> milkLevel = Mth.clamp(value, 0, MAX_MILK_LEVEL);
                case 3 -> iceLevel = Mth.clamp(value, 0, MAX_ICE_LEVEL);
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public WoodenFreezerBlockEntity(BlockPos pos, BlockState state) {
        super(EOTWBlockEntities.WOODEN_FREEZER.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ContainerData getData() {
        return data;
    }

    public int getMilkLevel() {
        return milkLevel;
    }

    public int getIceLevel() {
        return iceLevel;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return MAX_PROGRESS;
    }

    public boolean hasIceCreamResults() {
        for (int slot = CONE_SLOT_0; slot <= CONE_SLOT_2; slot++) {
            if (isIceCream(itemHandler.getStackInSlot(slot))) {
                return true;
            }
        }

        return false;
    }

    public void drops() {
        if (level == null) {
            return;
        }

        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), itemHandler.getStackInSlot(slot));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WoodenFreezerBlockEntity blockEntity) {
        boolean changed = false;

        changed |= blockEntity.loadMilk();
        changed |= blockEntity.loadIce();

        if (blockEntity.canFreeze()) {
            blockEntity.progress++;
            changed = true;

            if (blockEntity.progress >= MAX_PROGRESS) {
                blockEntity.freeze();
                blockEntity.progress = 0;
            }
        } else if (blockEntity.progress != 0) {
            blockEntity.progress = 0;
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
            blockEntity.syncState();
        }
    }

    private boolean loadMilk() {
        ItemStack milkStack = itemHandler.getStackInSlot(MILK_SLOT);

        if (milkLevel > MAX_MILK_LEVEL - MILK_FROM_BUCKET || !milkStack.is(EOTWItems.SHEEP_MILK_BUCKET.get())) {
            return false;
        }

        milkLevel += MILK_FROM_BUCKET;
        itemHandler.setStackInSlot(MILK_SLOT, new ItemStack(Items.BUCKET));
        return true;
    }

    private boolean loadIce() {
        ItemStack iceStack = itemHandler.getStackInSlot(ICE_SLOT);

        if (iceLevel > MAX_ICE_LEVEL - ICE_FROM_BLOCK || !iceStack.is(Items.ICE)) {
            return false;
        }

        iceLevel += ICE_FROM_BLOCK;
        iceStack.shrink(1);
        return true;
    }

    private boolean canFreeze() {
        Optional<WoodenFreezerRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        return getCraftableConeCount() > 0;
    }

    private void freeze() {
        Optional<WoodenFreezerRecipe> recipeOptional = getCurrentRecipe();

        if (recipeOptional.isEmpty() || level == null) {
            return;
        }

        ItemStack result = recipeOptional.get().getResultItem(level.registryAccess());
        int crafted = 0;
        int craftLimit = getCraftableConeCount();

        for (int slot = CONE_SLOT_0; slot <= CONE_SLOT_2; slot++) {
            if (crafted >= craftLimit) {
                break;
            }

            ItemStack stack = itemHandler.getStackInSlot(slot);

            if (stack.is(EOTWItems.CONE.get())) {
                ItemStack output = result.copy();
                output.setCount(1);
                itemHandler.setStackInSlot(slot, output);
                crafted++;
            }
        }

        if (crafted <= 0) {
            return;
        }

        itemHandler.getStackInSlot(FLAVOR_SLOT).shrink(1);

        milkLevel = Math.max(0, milkLevel - crafted);
        iceLevel = Math.max(0, iceLevel - crafted);

        level.playSound(null, worldPosition, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 0.6F, 1.25F);

        setChanged();
        syncState();
    }

    private int getConeCount() {
        int cones = 0;

        for (int slot = CONE_SLOT_0; slot <= CONE_SLOT_2; slot++) {
            if (itemHandler.getStackInSlot(slot).is(EOTWItems.CONE.get())) {
                cones++;
            }
        }

        return cones;
    }

    private Optional<WoodenFreezerRecipe> getCurrentRecipe() {
        return getRecipeFor(itemHandler.getStackInSlot(FLAVOR_SLOT));
    }

    private Optional<WoodenFreezerRecipe> getRecipeFor(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return Optional.empty();
        }

        Container container = new SimpleContainer(stack);
        return level.getRecipeManager().getRecipeFor(EOTWRecipes.WOODEN_FREEZER_TYPE.get(), container, level);
    }

    private boolean isFreezerIngredient(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return level == null || getRecipeFor(stack).isPresent();
    }

    private static boolean isIceCream(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IceCream;
    }

    private void syncState() {
        if (level == null || level.isClientSide) {
            return;
        }

        BlockState state = getBlockState();

        if (!state.hasProperty(WoodenFreezerBlock.CONE_COUNT)
                || !state.hasProperty(WoodenFreezerBlock.CONTENT)
                || !state.hasProperty(WoodenFreezerBlock.MILK_LEVEL)) {
            return;
        }

        FreezerContent content = getVisualContent();
        int coneCount = getVisualCount(content);
        int milkStateLevel = Mth.clamp(Mth.ceil(milkLevel / (float) MILK_FROM_BUCKET), 0, BLOCK_MILK_LEVELS);

        BlockState updatedState = state
                .setValue(WoodenFreezerBlock.CONE_COUNT, coneCount)
                .setValue(WoodenFreezerBlock.CONTENT, content)
                .setValue(WoodenFreezerBlock.MILK_LEVEL, milkStateLevel);

        if (updatedState != state) {
            level.setBlock(worldPosition, updatedState, Block.UPDATE_CLIENTS);
        }
    }

    private int getCraftableConeCount() {
        if (getCurrentRecipe().isEmpty()) {
            return 0;
        }

        return Math.min(getConeCount(), Math.min(milkLevel, iceLevel));
    }

    private FreezerContent getVisualContent() {
        if (hasIceCreamResults()) {
            return FreezerContent.ICECREAM;
        }

        return getConeCount() > 0 ? FreezerContent.CONE : FreezerContent.NO_CONE;
    }

    private int getVisualCount(FreezerContent content) {
        if (content == FreezerContent.NO_CONE) {
            return 0;
        }

        int count = 0;

        for (int slot = CONE_SLOT_0; slot <= CONE_SLOT_2; slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);

            if (content == FreezerContent.CONE && stack.is(EOTWItems.CONE.get())) {
                count++;
            } else if (content == FreezerContent.ICECREAM && isIceCream(stack)) {
                count++;
            }
        }

        return Mth.clamp(count, 0, 3);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
        tag.putInt("MilkLevel", milkLevel);
        tag.putInt("IceLevel", iceLevel);
        tag.putInt("Progress", progress);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        itemHandler.deserializeNBT(tag.getCompound("Inventory"));

        milkLevel = Mth.clamp(tag.getInt("MilkLevel"), 0, MAX_MILK_LEVEL);
        iceLevel = Mth.clamp(tag.getInt("IceLevel"), 0, MAX_ICE_LEVEL);
        progress = Mth.clamp(tag.getInt("Progress"), 0, MAX_PROGRESS);

        sanitizeStacks();
    }

    private void sanitizeStacks() {
        for (int slot = CONE_SLOT_0; slot <= CONE_SLOT_2; slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);

            if (!stack.isEmpty() && stack.getCount() > 1) {
                stack.setCount(1);
            }
        }

        ItemStack milkStack = itemHandler.getStackInSlot(MILK_SLOT);
        if (!milkStack.isEmpty() && milkStack.getCount() > 1) {
            milkStack.setCount(1);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.essenceofthewild.wooden_freezer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new WoodenFreezerMenu(id, inv, this, data);
    }
}
