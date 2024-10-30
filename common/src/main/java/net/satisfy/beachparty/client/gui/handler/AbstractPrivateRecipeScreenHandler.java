package net.satisfy.beachparty.client.gui.handler;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPrivateRecipeScreenHandler extends AbstractContainerMenu {

    protected AbstractPrivateRecipeScreenHandler(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    public abstract boolean hasIngredient(Recipe<?> recipe);

    public abstract int getCraftingSlotCount();
}
