package net.satisfy.beachparty.core.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.satisfy.beachparty.core.compat.rei.category.MiniFridgeCategory;
import net.satisfy.beachparty.core.compat.rei.category.palmBarCategory;
import net.satisfy.beachparty.core.compat.rei.display.MiniFridgeDisplay;
import net.satisfy.beachparty.core.compat.rei.display.palmBarDisplay;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.palmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

public class BeachpartyREIClientPlugin {

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new MiniFridgeCategory());
        registry.add(new palmBarCategory());

        //TODO Change ObjectRegistry.
        registry.addWorkstations(MiniFridgeCategory.MINE_FRIDGE_DISPLAY, EntryStacks.of(ObjectRegistry.BEACH_HAT.get()));
        registry.addWorkstations(palmBarCategory.palm_BAR_DISPLAY, EntryStacks.of(ObjectRegistry.BEACH_HAT.get()));
    }


    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(MiniFridgeRecipe.class, MiniFridgeDisplay::new);
        registry.registerFiller(palmBarRecipe.class, palmBarDisplay::new);
    }
}
