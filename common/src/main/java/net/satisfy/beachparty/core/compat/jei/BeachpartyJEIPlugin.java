package net.satisfy.beachparty.core.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.beachparty.core.compat.jei.categorys.MiniFridgeCategory;
import net.satisfy.beachparty.core.compat.jei.categorys.PalmBarCategory;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeTypeRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class BeachpartyJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return BeachpartyIdentifier.identifier("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MiniFridgeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PalmBarCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<RecipeHolder<MiniFridgeRecipe>> fridgeHolders = rm.getAllRecipesFor(RecipeTypeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
        registration.addRecipes(MiniFridgeCategory.RECIPE_TYPE, fridgeHolders.stream().map(RecipeHolder::value).toList());

        List<RecipeHolder<PalmBarRecipe>> barHolders = rm.getAllRecipesFor(RecipeTypeRegistry.PALM_BAR_RECIPE_TYPE.get());
        registration.addRecipes(PalmBarCategory.PALM_BAR, barHolders.stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ObjectRegistry.MINI_FRIDGE.get().asItem()), MiniFridgeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(ObjectRegistry.PALM_BAR.get().asItem().getDefaultInstance(), PalmBarCategory.PALM_BAR);
    }
}
