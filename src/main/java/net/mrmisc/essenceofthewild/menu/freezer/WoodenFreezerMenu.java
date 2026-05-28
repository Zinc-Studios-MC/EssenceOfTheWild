package net.mrmisc.essenceofthewild.menu.freezer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.menu.EOTWMenuTypes;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;

public class WoodenFreezerMenu extends AbstractContainerMenu {
    public static final int MENU_CONE_SLOT_0 = 0;
    public static final int MENU_CONE_SLOT_1 = 1;
    public static final int MENU_CONE_SLOT_2 = 2;
    public static final int MENU_FLAVOR_SLOT = 3;
    public static final int MENU_ICE_SLOT = 4;
    public static final int MENU_MILK_SLOT = 5;

    private static final int FREEZER_SLOT_COUNT = 6;

    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = FREEZER_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int PLAYER_HOTBAR_FIRST_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int PLAYER_HOTBAR_SLOT_COUNT = 9;

    // EXACT TEXTURE MEANING:
    // LEFT top slot  = ICE
    // RIGHT top slot = MILK
    private static final int ICE_SLOT_X = 17;
    private static final int ICE_SLOT_Y = 20;

    private static final int MILK_SLOT_X = 143;
    private static final int MILK_SLOT_Y = 20;

    private static final int FLAVOR_SLOT_X = 80;
    private static final int FLAVOR_SLOT_Y = 33;

    private static final int LEFT_CONE_SLOT_X = 34;
    private static final int LEFT_CONE_SLOT_Y = 48;

    private static final int CENTER_CONE_SLOT_X = 80;
    private static final int CENTER_CONE_SLOT_Y = 61;

    private static final int RIGHT_CONE_SLOT_X = 126;
    private static final int RIGHT_CONE_SLOT_Y = 48;

    private static final int PLAYER_INVENTORY_X = 7;
    private static final int PLAYER_INVENTORY_Y = 84;

    private static final int PLAYER_HOTBAR_X = 7;
    private static final int PLAYER_HOTBAR_Y = 142;

    private final WoodenFreezerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public WoodenFreezerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, getBlockEntity(inventory, extraData), getClientData());
    }

    public WoodenFreezerMenu(int id, Inventory inventory, WoodenFreezerBlockEntity blockEntity, ContainerData data) {
        super(EOTWMenuTypes.WOODEN_FREEZER.get(), id);

        this.blockEntity = blockEntity;
        this.level = inventory.player.level();
        this.data = data;

        addConeSlots(blockEntity);
        addFlavorSlot(blockEntity);
        addIceSlot(blockEntity);
        addMilkSlot(blockEntity);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addDataSlots(data);
    }

    private static WoodenFreezerBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf extraData) {
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(extraData.readBlockPos());

        if (blockEntity instanceof WoodenFreezerBlockEntity woodenFreezerBlockEntity) {
            return woodenFreezerBlockEntity;
        }

        throw new IllegalStateException("Expected a wooden freezer block entity");
    }

    private static ContainerData getClientData() {
        int[] values = new int[5];

        return new ContainerData() {
            @Override
            public int get(int index) {
                return values[index];
            }

            @Override
            public void set(int index, int value) {
                values[index] = value;
            }

            @Override
            public int getCount() {
                return values.length;
            }
        };
    }

    private void addConeSlots(WoodenFreezerBlockEntity blockEntity) {
        addSlot(createConeSlot(blockEntity, WoodenFreezerBlockEntity.CONE_SLOT_0, LEFT_CONE_SLOT_X, LEFT_CONE_SLOT_Y));
        addSlot(createConeSlot(blockEntity, WoodenFreezerBlockEntity.CONE_SLOT_1, CENTER_CONE_SLOT_X, CENTER_CONE_SLOT_Y));
        addSlot(createConeSlot(blockEntity, WoodenFreezerBlockEntity.CONE_SLOT_2, RIGHT_CONE_SLOT_X, RIGHT_CONE_SLOT_Y));
    }

    private SlotItemHandler createConeSlot(WoodenFreezerBlockEntity blockEntity, int handlerSlot, int x, int y) {
        return new SlotItemHandler(blockEntity.getItemHandler(), handlerSlot, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(EOTWItems.CONE.get()) && !blockEntity.hasIceCreamResults();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        };
    }

    private void addFlavorSlot(WoodenFreezerBlockEntity blockEntity) {
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), WoodenFreezerBlockEntity.FLAVOR_SLOT, FLAVOR_SLOT_X, FLAVOR_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isFreezerIngredient(stack);
            }
        });
    }

    private void addIceSlot(WoodenFreezerBlockEntity blockEntity) {
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), WoodenFreezerBlockEntity.ICE_SLOT, ICE_SLOT_X, ICE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.ICE);
            }
        });
    }

    private void addMilkSlot(WoodenFreezerBlockEntity blockEntity) {
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), WoodenFreezerBlockEntity.MILK_SLOT, MILK_SLOT_X, MILK_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(EOTWItems.SHEEP_MILK_BUCKET.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                addSlot(new Slot(
                        inventory,
                        column + row * 9 + 9,
                        PLAYER_INVENTORY_X + column * 18,
                        PLAYER_INVENTORY_Y + row * 18
                ));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int column = 0; column < 9; ++column) {
            addSlot(new Slot(
                    inventory,
                    column,
                    PLAYER_HOTBAR_X + column * 18,
                    PLAYER_HOTBAR_Y
            ));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index < PLAYER_INVENTORY_FIRST_SLOT_INDEX) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_FIRST_SLOT_INDEX, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean movedToFreezer = moveStackToFreezerInput(stack);

            if (!movedToFreezer) {
                if (index < PLAYER_HOTBAR_FIRST_SLOT_INDEX) {
                    if (!moveItemStackTo(stack, PLAYER_HOTBAR_FIRST_SLOT_INDEX, PLAYER_HOTBAR_FIRST_SLOT_INDEX + PLAYER_HOTBAR_SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!moveItemStackTo(stack, PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_HOTBAR_FIRST_SLOT_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return copy;
    }

    private boolean moveStackToFreezerInput(ItemStack stack) {
        if (stack.is(EOTWItems.CONE.get())) {
            return moveItemStackTo(stack, MENU_CONE_SLOT_0, MENU_CONE_SLOT_2 + 1, false);
        }

        if (stack.is(Items.ICE)) {
            return moveItemStackTo(stack, MENU_ICE_SLOT, MENU_ICE_SLOT + 1, false);
        }

        if (stack.is(EOTWItems.SHEEP_MILK_BUCKET.get())) {
            return moveItemStackTo(stack, MENU_MILK_SLOT, MENU_MILK_SLOT + 1, false);
        }

        if (isFreezerIngredient(stack)) {
            return moveItemStackTo(stack, MENU_FLAVOR_SLOT, MENU_FLAVOR_SLOT + 1, false);
        }

        return false;
    }

    private boolean isFreezerIngredient(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Container container = new SimpleContainer(stack);
        return level.getRecipeManager().getRecipeFor(EOTWRecipes.WOODEN_FREEZER_TYPE.get(), container, level).isPresent();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, EOTWBlocks.WOODEN_FREEZER.get());
    }

    public boolean hasConeInMenuSlot(int menuSlot) {
        if (menuSlot < MENU_CONE_SLOT_0 || menuSlot > MENU_CONE_SLOT_2) {
            return false;
        }

        return getSlot(menuSlot).hasItem();
    }

    public boolean hasConeInputInMenuSlot(int menuSlot) {
        if (menuSlot < MENU_CONE_SLOT_0 || menuSlot > MENU_CONE_SLOT_2) {
            return false;
        }

        return getSlot(menuSlot).getItem().is(EOTWItems.CONE.get());
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    public int getMilkLevel() {
        return data.get(2);
    }

    public int getIceLevel() {
        return data.get(3);
    }

    public int getCraftableConeCount() {
        return data.get(4);
    }

    public int getScaledProgress(int width) {
        int maxProgress = getMaxProgress();

        if (maxProgress <= 0 || getProgress() <= 0) {
            return 0;
        }

        return Math.max(0, Math.min(width, getProgress() * width / maxProgress));
    }

    public int getScaledMilk(int width) {
        return Math.max(0, Math.min(width,
                getMilkLevel() * width / WoodenFreezerBlockEntity.MAX_MILK_LEVEL));
    }

    public int getScaledIce(int height) {
        return Math.max(0, Math.min(height,
                getIceLevel() * height / WoodenFreezerBlockEntity.MAX_ICE_LEVEL));
    }
}
