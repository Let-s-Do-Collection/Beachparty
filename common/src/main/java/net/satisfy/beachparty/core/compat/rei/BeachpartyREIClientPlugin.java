package net.satisfy.beachparty.core.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.satisfy.beachparty.core.compat.rei.category.MiniFridgeCategory;
import net.satisfy.beachparty.core.compat.rei.category.PalmBarCategory;
import net.satisfy.beachparty.core.compat.rei.display.MiniFridgeDisplay;
import net.satisfy.beachparty.core.compat.rei.display.PalmBarDisplay;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

public class BeachpartyREIClientPlugin {
    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new MiniFridgeCategory());
        registry.add(new PalmBarCategory());

        registry.addWorkstations(MiniFridgeCategory.MINE_FRIDGE_DISPLAY, EntryStacks.of(ObjectRegistry.MINI_FRIDGE.get()));
        registry.addWorkstations(PalmBarCategory.PALM_BAR_DISPLAY, EntryStacks.of(ObjectRegistry.PALM_BAR.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MiniFridgeRecipe.class, MiniFridgeDisplay::new);
        registry.registerFiller(PalmBarRecipe.class, PalmBarDisplay::new);
    }
}
