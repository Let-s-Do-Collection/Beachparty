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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class MiniFridgeCategory implements IRecipeCategory<MiniFridgeRecipe> {
    public static final RecipeType<MiniFridgeRecipe> RECIPE_TYPE = RecipeType.create(Beachparty.MOD_ID, "mini_fridge_freezing", MiniFridgeRecipe.class);
    public static final ResourceLocation TEXTURE = BeachpartyIdentifier.identifier("textures/gui/freezer_gui.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private static final int WIDTH = 124;
    private static final int HEIGHT = 60;
    private static final int WIDTH_OF = 26;
    private static final int HEIGHT_OF = 13;

    public MiniFridgeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, WIDTH_OF, HEIGHT_OF, WIDTH, HEIGHT);
        this.arrow = helper.drawableBuilder(TEXTURE, 176, 14, 24, 17).buildAnimated(250, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance());
    }

    @Override
    public @NotNull RecipeType<MiniFridgeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return ObjectRegistry.MINI_FRIDGE.get().getName();
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void draw(MiniFridgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
        this.arrow.draw(guiGraphics, 53, 22);
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MiniFridgeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 56 - WIDTH_OF, 35 - HEIGHT_OF).addIngredients(recipe.getIngredients().get(0));
        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116 - WIDTH_OF, 35 - HEIGHT_OF).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
