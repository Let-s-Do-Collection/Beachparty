package net.satisfy.beachparty.core.registry;

import net.satisfy.foundation.compostable.FoundationCompostables;

public class CompostablesRegistry {
    public static void init() {
        FoundationCompostables.register(0.6F, ObjectRegistry.PALM_SPROUT);
        FoundationCompostables.register(0.6F, ObjectRegistry.PALM_LEAVES);
        FoundationCompostables.register(0.3F, ObjectRegistry.COCONUT);
        FoundationCompostables.register(0.3F, ObjectRegistry.COCONUT_OPEN);
        FoundationCompostables.register(0.3F, ObjectRegistry.COOKED_MUSSEL_MEAT);
        FoundationCompostables.register(0.3F, ObjectRegistry.RAW_MUSSEL_MEAT);
        FoundationCompostables.register(0.3F, ObjectRegistry.COCONUT_COCKTAIL);
        FoundationCompostables.register(0.3F, ObjectRegistry.SWEETBERRIES_COCKTAIL);
        FoundationCompostables.register(0.3F, ObjectRegistry.COCOA_COCKTAIL);
        FoundationCompostables.register(0.3F, ObjectRegistry.PUMPKIN_COCKTAIL);
        FoundationCompostables.register(0.3F, ObjectRegistry.MELON_COCKTAIL);
        FoundationCompostables.register(0.3F, ObjectRegistry.HONEY_COCKTAIL);
    }
}
