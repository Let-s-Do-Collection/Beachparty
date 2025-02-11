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
import net.satisfy.beachparty.core.block.BeachTowelBlock;
import net.satisfy.beachparty.core.registry.CompostablesRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.core.world.PlacedFeatures;
import net.satisfy.beachparty.fabric.compat.trinkets.TrinketsCompatibility;
import net.satisfy.beachparty.fabric.config.ConfigFabric;

import java.util.function.Predicate;

public class BeachpartyFabric implements ModInitializer {
    public static boolean trinketsLoaded;

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        Beachparty.init();
        CompostablesRegistry.init();
        Beachparty.commonSetup();
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

        if (config.spawnSeashells) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SEASHELLS_KEY)
            );
        }

        if (config.spawnPalms) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PALM_TREE_KEY)
            );
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.PALM_TREE_KEY)
            );
        }

        if (config.spawnSandwaves) {
            world.add(ModificationPhase.ADDITIONS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SANDWAVES_KEY)
            );
        } else {
            world.add(ModificationPhase.REMOVALS, beachBiomes, ctx ->
                    ctx.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeatures.SANDWAVES_KEY)
            );
        }

        if (config.spawnMessageInABottle) {
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
