package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.world.NearWaterFilter;
import net.satisfy.beachparty.core.world.PalmSpacingFilter;
import net.satisfy.beachparty.core.world.placers.CrookedTrunkPlacer;
import net.satisfy.beachparty.core.world.placers.PalmFoliagePlacer;

public class PlacerTypeRegistry {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.FOLIAGE_PLACER_TYPE);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.TRUNK_PLACER_TYPE);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.PLACEMENT_MODIFIER_TYPE);

    public static final RegistrySupplier<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPES.register("palm_foliage_placer", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));
    public static final RegistrySupplier<TrunkPlacerType<CrookedTrunkPlacer>> CROOKED_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("crooked_trunk_placer", () -> new TrunkPlacerType<>(CrookedTrunkPlacer.CODEC));
    public static final RegistrySupplier<PlacementModifierType<NearWaterFilter>> NEAR_WATER = PLACEMENT_MODIFIER_TYPES.register("near_water", () -> () -> NearWaterFilter.CODEC);
    public static final RegistrySupplier<PlacementModifierType<PalmSpacingFilter>> PALM_SPACING = PLACEMENT_MODIFIER_TYPES.register("palm_spacing", () -> () -> PalmSpacingFilter.CODEC);

    public static void init() {
        FOLIAGE_PLACER_TYPES.register();
        TRUNK_PLACER_TYPES.register();
        PLACEMENT_MODIFIER_TYPES.register();
    }
}
