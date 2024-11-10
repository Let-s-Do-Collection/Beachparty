package net.satisfy.beachparty.core.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.core.compat.rei.category.TikiBarCategory;
import net.satisfy.beachparty.core.recipe.TikiBarRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TikiBarDisplay extends BasicDisplay {

    public TikiBarDisplay(TikiBarRecipe recipe) {
        this(EntryIngredients.ofIngredients(new ArrayList<>(recipe.getIngredients())), Collections.singletonList(EntryIngredients.of(recipe.getResultItem(BasicDisplay.registryAccess()))), Optional.ofNullable(recipe.getId()));
    }

    public TikiBarDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TikiBarCategory.TIKI_BAR_DISPLAY;
    }

}
