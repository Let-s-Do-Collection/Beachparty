package net.satisfy.beachparty.core.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.core.compat.rei.category.MiniFridgeCategory;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MiniFridgeDisplay extends BasicDisplay {

    public MiniFridgeDisplay(MiniFridgeRecipe recipe) {
        this(EntryIngredients.ofIngredients(new ArrayList<>(recipe.getIngredients())), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(BasicDisplay.registryAccess()))), Optional.ofNullable(recipe.getId()));
    }

    public MiniFridgeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MiniFridgeCategory.MINE_FRIDGE_DISPLAY;
    }

}
