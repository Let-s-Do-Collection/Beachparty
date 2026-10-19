package net.satisfy.beachparty.core.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class BeachpartyEmiCategory extends EmiRecipeCategory {
    private final ItemLike icon;

    public BeachpartyEmiCategory(String id, ItemLike icon) {
        super(BeachpartyIdentifier.identifier(id), EmiStack.of(icon));
        this.icon = icon;
    }

    @Override
    public Component getName() {
        return this.icon.asItem().getDescription();
    }
}
