package net.satisfy.beachparty.core.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.beachparty.client.gui.handler.MiniFridgeGuiHandler;
import net.satisfy.beachparty.client.gui.handler.PalmBarGuiHandler;
import net.satisfy.beachparty.core.compat.jei.categorys.MiniFridgeCategory;
import net.satisfy.beachparty.core.compat.jei.categorys.PalmBarCategory;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class BeachpartyJEIPlugin implements IModPlugin {
    public static void addSlot(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient) {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MiniFridgeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PalmBarCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<RecipeHolder<MiniFridgeRecipe>> fridgeRecipeHolders = rm.getAllRecipesFor(RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
        List<MiniFridgeRecipe> fridgeRecipes = new ArrayList<>();
        fridgeRecipeHolders.forEach(miniFridgeRecipeRecipeHolder -> {
            fridgeRecipes.add(miniFridgeRecipeRecipeHolder.value());
        });
        registration.addRecipes(MiniFridgeCategory.MINI_FRIDGE_FREEZING, fridgeRecipes);
        List<RecipeHolder<PalmBarRecipe>> barHolders = rm.getAllRecipesFor(RecipeRegistry.PALM_BAR_RECIPE_TYPE.get());
        List<PalmBarRecipe> barRecipes = new ArrayList<>();
        barHolders.forEach(palmBarRecipeRecipeHolder -> {
            barRecipes.add(palmBarRecipeRecipeHolder.value());
        });
        registration.addRecipes(PalmBarCategory.PALM_BAR, barRecipes);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return BeachpartyIdentifier.identifier("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance(), MiniFridgeCategory.MINI_FRIDGE_FREEZING);
        registration.addRecipeCatalyst(ObjectRegistry.PALM_BAR.get().asItem().getDefaultInstance(), PalmBarCategory.PALM_BAR);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MiniFridgeGuiHandler.class, ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), MiniFridgeCategory.MINI_FRIDGE_FREEZING, 1, 1, 2, 36);
        registration.addRecipeTransferHandler(PalmBarGuiHandler.class, ScreenHandlerTypesRegistry.PALM_BAR_GUI_HANDLER.get(), PalmBarCategory.PALM_BAR, 1, 4, 2, 36);

    }
}
