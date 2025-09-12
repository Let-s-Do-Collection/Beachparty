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
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class PalmBarGuiHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;

    public PalmBarGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public PalmBarGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.PALM_BAR_GUI_HANDLER.get(), syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addDataSlots(propertyDelegate);
        this.buildBlockEntityContainer();
        this.buildPlayerContainer(playerInventory);
    }

    private void buildBlockEntityContainer() {
        this.addSlot(new Slot(inventory, 0, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(inventory, 1, 38, 25));
        this.addSlot(new Slot(inventory, 2, 56, 25));
        this.addSlot(new Slot(inventory, 3, 38, 43));
        this.addSlot(new Slot(inventory, 4, 56, 43));
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public int getShakeXProgress() {
        int progress = propertyDelegate.get(0);
        int total = propertyDelegate.get(1);
        if (total == 0 || progress == 0) return 0;
        return progress * 22 / total + 1;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index >= 0 && index <= 4) {
            if (!moveItemStackTo(stack, 5, 41, true)) return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(stack, 1, 5, false)) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
}
