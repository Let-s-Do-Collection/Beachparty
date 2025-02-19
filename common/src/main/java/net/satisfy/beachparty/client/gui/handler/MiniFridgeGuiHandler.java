package net.satisfy.beachparty.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.gui.handler.slot.PalmBarOutputSlot;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class MiniFridgeGuiHandler extends AbstractContainerMenu {
    private final ContainerData propertyDelegate;

    public MiniFridgeGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(2), new SimpleContainerData(2));
    }

    public MiniFridgeGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), syncId);
        this.propertyDelegate = propertyDelegate;

        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
        addDataSlots(propertyDelegate);
    }

    public int getShakeXProgress() {
        int progress = this.propertyDelegate.get(0);
        int totalProgress = this.propertyDelegate.get(1);
        if (totalProgress == 0 || progress == 0) {
            return 0;
        }
        return progress * 22 / totalProgress + 1;
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new PalmBarOutputSlot(playerInventory.player, inventory, 0, 116, 35));
        this.addSlot(new Slot(inventory, 1, 56, 35));
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();

        final int outputSlot = 0;
        final int inputSlot = 1;
        final int playerInvStart = 2;
        final int hotbarStart = playerInvStart + 27;
        final int hotbarEnd = hotbarStart + 9;

        if (index == outputSlot) {
            if (!moveItemStackTo(stack, playerInvStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, originalStack);
        } else if (index == inputSlot) {
            if (!moveItemStackTo(stack, playerInvStart, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= playerInvStart) {
            if (!moveItemStackTo(stack, inputSlot, inputSlot + 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == originalStack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
