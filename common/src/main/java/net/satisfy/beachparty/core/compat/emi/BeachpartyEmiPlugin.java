package net.satisfy.beachparty.core.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeTypeRegistry;

import java.util.List;

@EmiEntrypoint
public class BeachpartyEmiPlugin implements EmiPlugin {
    public static final BeachpartyEmiCategory PALM_BAR = new BeachpartyEmiCategory("palm_bar_mixing", ObjectRegistry.PALM_BAR.get());
    public static final BeachpartyEmiCategory MINI_FRIDGE = new BeachpartyEmiCategory("mini_fridge_freezing", ObjectRegistry.MINI_FRIDGE.get());

    private static final int SLOT = 18;

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(PALM_BAR);
        registry.addCategory(MINI_FRIDGE);

        registry.addWorkstation(PALM_BAR, EmiStack.of(ObjectRegistry.PALM_BAR.get()));
        registry.addWorkstation(MINI_FRIDGE, EmiStack.of(ObjectRegistry.MINI_FRIDGE.get()));

        RecipeManager manager = registry.getRecipeManager();
        for (RecipeHolder<PalmBarRecipe> holder : manager.getAllRecipesFor(RecipeTypeRegistry.PALM_BAR_RECIPE_TYPE.get())) {
            registry.addRecipe(palmBar(holder));
        }
        for (RecipeHolder<MiniFridgeRecipe> holder : manager.getAllRecipesFor(RecipeTypeRegistry.MINI_FRIDGE_RECIPE_TYPE.get())) {
            registry.addRecipe(miniFridge(holder));
        }
    }

    private static BeachpartyEmiRecipe palmBar(RecipeHolder<PalmBarRecipe> holder) {
        PalmBarRecipe recipe = holder.value();
        List<EmiIngredient> inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        EmiStack output = EmiStack.of(recipe.getResultItem());
        return new BeachpartyEmiRecipe(PALM_BAR, holder.id(), 104, 2 * SLOT, inputs, List.of(output), (self, widgets) -> {
            for (int i = 0; i < 4; i++) {
                widgets.addSlot(i < inputs.size() ? inputs.get(i) : EmiStack.EMPTY, (i / 2) * SLOT, (i % 2) * SLOT);
            }
            widgets.addFillingArrow(44, 9, 2500);
            widgets.addSlot(output, 78, 5).large(true).recipeContext(self);
        });
    }

    private static BeachpartyEmiRecipe miniFridge(RecipeHolder<MiniFridgeRecipe> holder) {
        MiniFridgeRecipe recipe = holder.value();
        EmiIngredient input = EmiIngredient.of(recipe.getIngredients().getFirst());
        EmiStack output = EmiStack.of(recipe.getResultItem(null));
        int time = Math.max(1, recipe.getCraftingTime()) * 50;
        return new BeachpartyEmiRecipe(MINI_FRIDGE, holder.id(), 82, SLOT, List.of(input), List.of(output), (self, widgets) -> {
            widgets.addSlot(input, 4, 0);
            widgets.addFillingArrow(28, 0, time);
            widgets.addSlot(output, 60, 0).recipeContext(self);
        });
    }
}
