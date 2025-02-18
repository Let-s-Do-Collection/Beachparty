package net.satisfy.beachparty.core.registry;

import net.minecraft.world.level.block.ComposterBlock;

public class CompostablesRegistry {
    public static void init() {
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.PALM_SPROUT.get().asItem(), 0.6F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.PALM_LEAVES.get().asItem(), 0.6F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.COCONUT.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.COCONUT_OPEN.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.COOKED_MUSSEL_MEAT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.RAW_MUSSEL_MEAT.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.COCONUT_COCKTAIL.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.SWEETBERRIES_COCKTAIL.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.COCOA_COCKTAIL.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.PUMPKIN_COCKTAIL.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.MELON_COCKTAIL.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ObjectRegistry.HONEY_COCKTAIL.get().asItem(), 0.3F);
    }
}
