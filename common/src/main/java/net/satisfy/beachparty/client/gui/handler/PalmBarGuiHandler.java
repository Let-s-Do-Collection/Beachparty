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

public class PalmBarGuiHandler extends AbstractContainerMenu {
    protected final ContainerData propertyDelegate;

    public PalmBarGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public PalmBarGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.PALM_BAR_GUI_HANDLER.get(), syncId);
        this.propertyDelegate = propertyDelegate;
        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new PalmBarOutputSlot(playerInventory.player, inventory, 0, 116, 35));
        this.addSlot(new Slot(inventory, 1, 38, 25));
        this.addSlot(new Slot(inventory, 2, 56, 25));
        this.addSlot(new Slot(inventory, 3, 38, 43));
        this.addSlot(new Slot(inventory, 4, 56, 43));
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

    public int getShakeXProgress() {
        int progress = propertyDelegate.get(0);
        int totalProgress = propertyDelegate.get(1);
        if (totalProgress == 0 || progress == 0) {
            return 0;
        }
        return progress * 22 / totalProgress + 1;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            ItemStack copyStack = originalStack.copy();

            if (index == 0) {
                if (!moveItemStackTo(originalStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(originalStack, copyStack);
            } else if (index >= 1 && index <= 4) {
                if (!moveItemStackTo(originalStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5) {
                if (!moveItemStackTo(originalStack, 1, 5, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (originalStack.getCount() == copyStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, originalStack);
            return copyStack;
        }
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

}
