package net.satisfy.beachparty.core.compat.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.compat.rei.display.MiniFridgeDisplay;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

import java.util.List;

public class MiniFridgeCategory implements DisplayCategory<MiniFridgeDisplay> {
    public static final CategoryIdentifier<MiniFridgeDisplay> MINE_FRIDGE_DISPLAY = CategoryIdentifier.of(Beachparty.MOD_ID, "mini_fridge_display");

    @Override
    public CategoryIdentifier<MiniFridgeDisplay> getCategoryIdentifier() {
        return MINE_FRIDGE_DISPLAY;
    }

    @Override
    public Component getTitle() {
        return ObjectRegistry.MINI_FRIDGE.get().getName();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.MINI_FRIDGE.get());
    }

    @Override
    public int getDisplayWidth(MiniFridgeDisplay display) {
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public List<Widget> setupDisplay(MiniFridgeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - getDisplayWidth(display) / 2 - 4, bounds.getCenterY() - getDisplayHeight() / 2 + 14);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 54, startPoint.y + 9)).animationDurationTicks(50));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 88, startPoint.y + 9)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        if (display.getInputEntries().isEmpty())
            widgets.add(Widgets.createSlotBackground(new Point(startPoint.x + 32, startPoint.y + 9)));
        else
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 32, startPoint.y + 9)).entries(display.getInputEntries().get(0)).markInput());

        return widgets;
    }
}
