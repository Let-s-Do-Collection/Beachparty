package net.satisfy.beachparty.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.furniture.BeachTowelBlock;
import net.satisfy.beachparty.fabric.compat.trinkets.TrinketsCompatibility;
import net.satisfy.beachparty.fabric.config.ConfigFabric;
import net.satisfy.beachparty.registry.CompostablesRegistry;
import net.satisfy.beachparty.util.BeachpartyIdentifier;
import net.satisfy.beachparty.world.PlacedFeatures;

import java.util.function.Predicate;

public class BeachpartyFabric implements ModInitializer {
    public static boolean trinketsLoaded;

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        Beachparty.init();
        CompostablesRegistry.init();
        addBiomeModification();
        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            boolean onClient = player.level().isClientSide;
            BlockState blockState = player.level().getBlockState(sleepingPos);
            return !(!onClient && blockState.getBlock() instanceof BeachTowelBlock);
        });

        trinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

        if (trinketsLoaded) {
            TrinketsCompatibility.load();
        }
    }

    private void addBiomeModification() {
        ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();

        BiomeModification world = BiomeModifications.create(new BeachpartyIdentifier("world_features"));

        Predicate<BiomeSelectionContext> beachBiomes = getBeachpartySelector();

        if (config.worldgen.spawnSeashells) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SEASHELLS_KEY)
            );
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SEASHELLS_KEY)
            );
        }

        if (config.worldgen.spawnMessageInABottle) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.MESSAGE_IN_A_BOTTLE_KEY)
            );
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.MESSAGE_IN_A_BOTTLE_KEY)
            );
        }
    }

    private static Predicate<BiomeSelectionContext> getBeachpartySelector() {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, new BeachpartyIdentifier("beach")));
    }
}
