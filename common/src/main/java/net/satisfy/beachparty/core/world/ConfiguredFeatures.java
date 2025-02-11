package net.satisfy.beachparty.core.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class ConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE_KEY = registerKey("palm_tree");


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new BeachpartyIdentifier(name));
    }
}