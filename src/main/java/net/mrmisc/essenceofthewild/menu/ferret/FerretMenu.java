package net.mrmisc.essenceofthewild.menu.ferret;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.menu.EOTWMenuTypes;

public class FerretMenu extends AbstractContainerMenu {
    private static final int FERRET_SLOT_COUNT = FerretEntity.INVENTORY_SIZE;

    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = FERRET_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int PLAYER_HOTBAR_FIRST_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int PLAYER_HOTBAR_SLOT_COUNT = 9;

    private static final int FERRET_INVENTORY_X = 98;
    private static final int FERRET_INVENTORY_Y = 18;

    private static final int PLAYER_INVENTORY_X = 8;
    private static final int PLAYER_INVENTORY_Y = 84;

    private static final int PLAYER_HOTBAR_X = 8;
    private static final int PLAYER_HOTBAR_Y = 142;

    private final Container ferretContainer;
    private final FerretEntity ferret;

    public FerretMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, new SimpleContainer(FerretEntity.INVENTORY_SIZE), getFerret(inventory, extraData.readInt()));
    }

    public FerretMenu(int id, Inventory inventory, Container ferretContainer, FerretEntity ferret) {
        super(EOTWMenuTypes.FERRET.get(), id);

        checkContainerSize(ferretContainer, FerretEntity.INVENTORY_SIZE);

        this.ferretContainer = ferretContainer;
        this.ferret = ferret;

        ferretContainer.startOpen(inventory.player);

        addFerretInventory(ferretContainer);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    private static FerretEntity getFerret(Inventory inventory, int entityId) {
        Entity entity = inventory.player.level().getEntity(entityId);
        return entity instanceof FerretEntity ferretEntity ? ferretEntity : null;
    }

    private void addFerretInventory(Container container) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                addSlot(new Slot(
                        container,
                        column + row * 3,
                        FERRET_INVENTORY_X + column * 18,
                        FERRET_INVENTORY_Y + row * 18
                ));
            }
        }
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
        } else if (!moveItemStackTo(stack, 0, FERRET_SLOT_COUNT, false)) {
            if (index < PLAYER_HOTBAR_FIRST_SLOT_INDEX) {
                if (!moveItemStackTo(stack, PLAYER_HOTBAR_FIRST_SLOT_INDEX, PLAYER_HOTBAR_FIRST_SLOT_INDEX + PLAYER_HOTBAR_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_HOTBAR_FIRST_SLOT_INDEX, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return ferret != null
                && ferretContainer.stillValid(player)
                && ferret.isAlive()
                && ferret.distanceTo(player) < 8.0F;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        ferretContainer.stopOpen(player);
    }

    public FerretEntity getFerret() {
        return ferret;
    }
}
