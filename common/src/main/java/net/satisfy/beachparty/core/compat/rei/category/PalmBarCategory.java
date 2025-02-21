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
import net.satisfy.beachparty.core.compat.rei.display.PalmBarDisplay;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

import java.util.List;

public class PalmBarCategory implements DisplayCategory<PalmBarDisplay> {
    public static final CategoryIdentifier<PalmBarDisplay> PALM_BAR_DISPLAY = CategoryIdentifier.of(Beachparty.MOD_ID, "palm_bar_display");

    @Override
    public CategoryIdentifier<PalmBarDisplay> getCategoryIdentifier() {
        return PALM_BAR_DISPLAY;
    }

    @Override
    public Component getTitle() {
        return ObjectRegistry.PALM_BAR.get().getName();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.PALM_BAR.get());
    }

    @Override
    public int getDisplayWidth(PalmBarDisplay display) {
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public List<Widget> setupDisplay(PalmBarDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - getDisplayWidth(display) / 2 - 4, bounds.getCenterY() - getDisplayHeight() / 2 + 26);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 62, startPoint.y + 9)).animationDurationTicks(50));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 96, startPoint.y + 9)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 96, startPoint.y + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        for (int i = 0; i < 4; i++) {
            int yOffset = (i % 2) * 20;
            int xOffset = (i / 2) * 20;
            Point slotPos = new Point(startPoint.x + 20 + xOffset, startPoint.y + yOffset);

            if (i < display.getInputEntries().size()) {
                widgets.add(Widgets.createSlot(slotPos).entries(display.getInputEntries().get(i)).markInput());
            } else {
                widgets.add(Widgets.createSlotBackground(slotPos));
            }
        }

        return widgets;
    }
}
