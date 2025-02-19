package net.satisfy.beachparty.core.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class PlacedFeatures {
    public static final ResourceKey<PlacedFeature> SEASHELLS_KEY = registerKey("seashells");
    public static final ResourceKey<PlacedFeature> PALM_TREE_KEY = registerKey("palm_tree");
    public static final ResourceKey<PlacedFeature> SANDWAVES_KEY = registerKey("sandwaves");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new BeachpartyIdentifier(name));
    }
}
