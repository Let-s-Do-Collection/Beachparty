package net.satisfy.beachparty.core.compat.jei.categorys;


import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.gui.PalmBarGui;
import net.satisfy.beachparty.core.compat.jei.BeachpartyJEIPlugin;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class PalmBarCategory implements IRecipeCategory<PalmBarRecipe> {
    public static final RecipeType<PalmBarRecipe> PALM_BAR = RecipeType.create(Beachparty.MOD_ID, "palm_bar_mixing", PalmBarRecipe.class);
    public static final int WIDTH = 124;
    public static final int HEIGHT = 60;
    public static final int WIDTH_OF = 26;
    public static final int HEIGHT_OF = 13;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public PalmBarCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(PalmBarGui.BG, WIDTH_OF, HEIGHT_OF, WIDTH, HEIGHT);
        this.arrow = helper.drawableBuilder(PalmBarGui.BG, 176, 14, 24, 17).buildAnimated(250, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.PALM_BAR.get().asItem().getDefaultInstance());
    }

    @Override
    public void draw(PalmBarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 53, 22);
    }

    @Override
    public @NotNull RecipeType<PalmBarRecipe> getRecipeType() {
        return PALM_BAR;
    }

    @Override
    public @NotNull Component getTitle() {
        return ObjectRegistry.PALM_BAR.get().getName();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PalmBarRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int s = ingredients.size();

        if (s > 0) BeachpartyJEIPlugin.addSlot(builder, 38 - WIDTH_OF, 25 - HEIGHT_OF, ingredients.get(0));
        if (s > 1) BeachpartyJEIPlugin.addSlot(builder, 38 - WIDTH_OF, 43 - HEIGHT_OF, ingredients.get(1));
        if (s > 2) BeachpartyJEIPlugin.addSlot(builder, 56 - WIDTH_OF, 25 - HEIGHT_OF, ingredients.get(2));
        if (s > 3) BeachpartyJEIPlugin.addSlot(builder, 56 - WIDTH_OF, 43 - HEIGHT_OF, ingredients.get(3));

        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116 - WIDTH_OF, 35 - HEIGHT_OF).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}

