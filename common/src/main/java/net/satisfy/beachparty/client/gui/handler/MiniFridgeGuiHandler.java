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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.beachparty.client.gui.handler.slot.IceSlot;
import net.satisfy.beachparty.client.gui.handler.slot.PalmBarOutputSlot;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;

public class MiniFridgeGuiHandler extends AbstractContainerMenu {

    private final Container inventory;
    private final ContainerData propertyDelegate;

    public MiniFridgeGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(6), new SimpleContainerData(2));
    }

    public MiniFridgeGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), syncId);
        this.inventory = inventory;
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
        this.addSlot(new PalmBarOutputSlot(playerInventory.player, inventory, 0, 128, 42));
        this.addSlot(new Slot(inventory, 1, 46, 27));
        this.addSlot(new IceSlot(inventory, 2, 59, 43));
    }

    private void buildPlayerContainer(Inventory playerInventory) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
