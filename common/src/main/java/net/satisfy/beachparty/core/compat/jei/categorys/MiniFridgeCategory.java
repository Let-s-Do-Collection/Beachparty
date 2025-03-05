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
import net.satisfy.beachparty.client.gui.MiniFridgeGui;
import net.satisfy.beachparty.core.compat.jei.BeachpartyJEIPlugin;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class MiniFridgeCategory implements IRecipeCategory<MiniFridgeRecipe> {
    public static final RecipeType<MiniFridgeRecipe> MINI_FRIDGE_FREEZING = new RecipeType<>(new ResourceLocation(Beachparty.MOD_ID, "mini_fridge_freezing"), MiniFridgeRecipe.class);
    public static final int WIDTH = 124;
    public static final int HEIGHT = 60;
    public static final int WIDTH_OF = 26;
    public static final int HEIGHT_OF = 13;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public MiniFridgeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(MiniFridgeGui.BG, WIDTH_OF, HEIGHT_OF, WIDTH, HEIGHT);
        this.arrow = helper.drawableBuilder(MiniFridgeGui.BG, 176, 14, 24, 17).buildAnimated(250, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance());
    }

    @Override
    public void draw(MiniFridgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 53, 22);
    }

    @Override
    public @NotNull RecipeType<MiniFridgeRecipe> getRecipeType() {
        return MINI_FRIDGE_FREEZING;
    }

    @Override
    public @NotNull Component getTitle() {
        return ObjectRegistry.MINI_FRIDGE.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, MiniFridgeRecipe recipe, IFocusGroup focuses) {
        BeachpartyJEIPlugin.addSlot(builder, 56 - WIDTH_OF, 35 - HEIGHT_OF, recipe.getIngredients().get(0));
        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116 - WIDTH_OF, 35 - HEIGHT_OF).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
