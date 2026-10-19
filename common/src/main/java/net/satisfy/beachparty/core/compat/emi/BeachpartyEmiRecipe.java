package net.satisfy.beachparty.core.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiConsumer;

public class BeachpartyEmiRecipe extends BasicEmiRecipe {
    private final BiConsumer<BeachpartyEmiRecipe, WidgetHolder> widgets;

    public BeachpartyEmiRecipe(EmiRecipeCategory category, ResourceLocation id, int width, int height, List<EmiIngredient> inputs, List<EmiStack> outputs, BiConsumer<BeachpartyEmiRecipe, WidgetHolder> widgets) {
        super(category, id, width, height);
        this.inputs.addAll(inputs);
        this.outputs.addAll(outputs);
        this.widgets = widgets;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        this.widgets.accept(this, widgets);
    }
}
