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
import net.satisfy.beachparty.core.recipe.palmBarRecipe;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;

public class PalmBarGuiHandler extends AbstractContainerMenu {
    private final Container inventory;
    protected final ContainerData propertyDelegate;

    public PalmBarGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public PalmBarGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.PALM_BAR_GUI_HANDLER.get(), syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new PalmBarOutputSlot(playerInventory.player, inventory, 0, 128, 35));
        this.addSlot(new Slot(inventory, 1, 55, 26));
        this.addSlot(new IceSlot(inventory, 2, 55, 44));
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

    public int getShakeYProgress() {
        int progress = propertyDelegate.get(0);
        int totalProgress = propertyDelegate.get(1);
        if (totalProgress == 0 || progress == 0) {
            return 0;
        }
        return progress * 20 / totalProgress + 1;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean isOutputSlotFilled() {
        return this.slots.get(0).hasItem();
    }
}
