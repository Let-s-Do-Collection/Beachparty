package net.satisfy.beachparty.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.beachparty.client.gui.handler.slot.IceSlot;
import net.satisfy.beachparty.client.gui.handler.slot.TikiBarOutputSlot;
import net.satisfy.beachparty.core.recipe.TikiBarRecipe;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;


public class TikiBarGuiHandler extends AbstractRecipeBookGUIScreenHandler {

    public TikiBarGuiHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public TikiBarGuiHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ScreenHandlerTypesRegistry.TIKI_BAR_GUI_HANDLER.get(), syncId, 2, playerInventory, inventory, propertyDelegate);

        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
    }

    private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
        this.addSlot(new TikiBarOutputSlot(playerInventory.player, inventory, 0, 128, 35));
        this.addSlot(new Slot(inventory, 1, 55, 26));
        this.addSlot(new IceSlot(inventory, 2, 55, 44));
    }

    private void buildPlayerContainer(Container playerInventory) {
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

    public int getShakeXProgress() {
        int progress = this.propertyDelegate.get(0);
        int totalProgress = this.propertyDelegate.get(1);
        if (totalProgress == 0 || progress == 0) {
            return 0;
        }
        return progress * 22 / totalProgress + 1;
    }

    public int getShakeYProgress() {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = this.propertyDelegate.get(1);
        if (totalProgress == 0 || progress == 0) {
            return 0;
        }
        return progress * 20 / totalProgress + 1;
    }

//    @Override
//    public List<IRecipeBookGroup> getGroups() {
//        return TikiBarRecipeBookGroup.TIKI_GROUPS;
//    }

    @Override
    public boolean hasIngredient(Recipe<?> recipe) {
        if (recipe instanceof TikiBarRecipe tikiBarRecipe) {
            for (Ingredient ingredient : tikiBarRecipe.getIngredients()) {
                boolean found = false;
                for (Slot slot : this.slots) {
                    if (ingredient.test(slot.getItem())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getCraftingSlotCount() {
        return 2;
    }
}
