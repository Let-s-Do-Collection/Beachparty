package net.satisfy.beachparty.neoforge.core.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.platform.PlatformHelper;

import java.util.Map;
import java.util.function.BooleanSupplier;

public record ConfiguredFeatureBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step, String toggle) implements BiomeModifier {
    private static final Map<String, BooleanSupplier> TOGGLES = Map.of(
            "palm_tree", PlatformHelper::isPalmTreeGeneration,
            "seashells", PlatformHelper::isSeashellGeneration,
            "sandwaves", PlatformHelper::isSandwaveGeneration
    );

    public static final MapCodec<ConfiguredFeatureBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConfiguredFeatureBiomeModifier::biomes),
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(ConfiguredFeatureBiomeModifier::features),
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(ConfiguredFeatureBiomeModifier::step),
            Codec.STRING.fieldOf("toggle").forGetter(ConfiguredFeatureBiomeModifier::toggle)
    ).apply(instance, ConfiguredFeatureBiomeModifier::new));

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Beachparty.MOD_ID);
    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ConfiguredFeatureBiomeModifier>> ADD_FEATURES = SERIALIZERS.register("add_features", () -> CODEC);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !biomes.contains(biome)) return;
        BooleanSupplier enabled = TOGGLES.get(toggle);
        if (enabled != null && !enabled.getAsBoolean()) return;
        features.forEach(feature -> builder.getGenerationSettings().addFeature(step, feature));
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
