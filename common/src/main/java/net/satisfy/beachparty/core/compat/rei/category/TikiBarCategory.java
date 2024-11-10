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
import net.satisfy.beachparty.core.compat.rei.display.TikiBarDisplay;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

import java.util.List;

public class TikiBarCategory implements DisplayCategory<TikiBarDisplay> {
    public static final CategoryIdentifier<TikiBarDisplay> TIKI_BAR_DISPLAY = CategoryIdentifier.of(Beachparty.MOD_ID, "tiki_bar_display");

    @Override
    public CategoryIdentifier<TikiBarDisplay> getCategoryIdentifier() {
        return TIKI_BAR_DISPLAY;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.beachparty.tiki_bar_category");
    }

    @Override
    public Renderer getIcon() {
        //TODO CHANGE TO TIKIBAR
        return EntryStacks.of(ObjectRegistry.BEACH_HAT.get());
    }

    @Override
    public int getDisplayWidth(TikiBarDisplay display) {
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public List<Widget> setupDisplay(TikiBarDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - getDisplayWidth(display) / 2 - 4, bounds.getCenterY() - getDisplayHeight() / 2 + 14);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 54, startPoint.y + 9))
                .animationDurationTicks(50));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 88, startPoint.y + 9)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        if (display.getInputEntries().size() < 1)
            widgets.add(Widgets.createSlotBackground(new Point(startPoint.x + 32, startPoint.y)));
        else
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 32, startPoint.y)).entries(display.getInputEntries().get(0)).markInput());

        if (display.getInputEntries().size() < 2)
            widgets.add(Widgets.createSlotBackground(new Point(startPoint.x + 32, startPoint.y + 20)));
        else
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 32, startPoint.y + 20)).entries(display.getInputEntries().get(1)).markInput());

        return widgets;
    }
}
