package net.satisfy.beachparty.core.compat.jei;

/*
@JeiPlugin
public class BeachpartyJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MiniFridgeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new palmBarCategory(registration.getJeiHelpers().getGuiHelper()));
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<MiniFridgeRecipe> fridgeRecipes = rm.getAllRecipesFor(RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
        registration.addRecipes(MiniFridgeCategory.MINI_FRIDGE, fridgeRecipes);

        List<palmBarRecipe> palmBarRecipes = rm.getAllRecipesFor(RecipeRegistry.palm_BAR_RECIPE_TYPE.get());
        registration.addRecipes(palmBarCategory.palm_BAR, palmBarRecipes);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new BeachpartyIdentifier("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance(), MiniFridgeCategory.MINI_FRIDGE);
        registration.addRecipeCatalyst(ObjectRegistry.palm_BAR.get().asItem().getDefaultInstance(), palmBarCategory.palm_BAR);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MiniFridgeGuiHandler.class, ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), MiniFridgeCategory.MINI_FRIDGE, 1, 2, 3, 36);
        registration.addRecipeTransferHandler(palmBarGuiHandler.class, ScreenHandlerTypesRegistry.palm_BAR_GUI_HANDLER.get(), palmBarCategory.palm_BAR, 1, 2, 3, 36);
    }

    public static void addSlot(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient) {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
    }
}
 */
