package net.satisfy.beachparty.core.compat.jei.categorys;

/*
public class palmBarCategory implements IRecipeCategory<palmBarRecipe> {
    public static final RecipeType<palmBarRecipe> palm_BAR = RecipeType.create(Beachparty.MOD_ID, "palm_bar_mixing", palmBarRecipe.class);
    public static final int WIDTH = 124;
    public static final int HEIGHT = 60;
    public static final int WIDTH_OF = 26;
    public static final int HEIGHT_OF = 13;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated shake;
    private final Component localizedName;

    public palmBarCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(palmBarGui.BG, WIDTH_OF, HEIGHT_OF, WIDTH, HEIGHT);
        this.arrow = helper.drawableBuilder(palmBarGui.BG, 177, 26, 22, 10)
                .buildAnimated(50, IDrawableAnimated.StartDirection.LEFT, false);

        this.shake = helper.drawableBuilder(palmBarGui.BG, 179, 2, 15, 20)
                .buildAnimated(50, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.palm_BAR.get().asItem().getDefaultInstance());
        this.localizedName = Component.translatable("rei.beachparty.palm_bar_category");
    }


    @Override
    public void draw(palmBarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, palmBarGui.ARROW_X - WIDTH_OF, palmBarGui.ARROW_Y - HEIGHT_OF);
        shake.draw(guiGraphics, palmBarGui.SHAKE_X - WIDTH_OF, palmBarGui.SHAKE_Y - HEIGHT_OF - 20);
    }

    @Override
    public RecipeType<palmBarRecipe> getRecipeType() {
        return palm_BAR;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, palmBarRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int s = ingredients.size();

        if (s > 0) BeachpartyJEIPlugin.addSlot(builder, 55 - WIDTH_OF, 26 - HEIGHT_OF, ingredients.get(0));
        if (s > 1) BeachpartyJEIPlugin.addSlot(builder, 55 - WIDTH_OF, 44 - HEIGHT_OF, ingredients.get(1));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 128 - WIDTH_OF, 35 - HEIGHT_OF).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
 */
