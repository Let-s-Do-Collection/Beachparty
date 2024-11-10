package net.satisfy.beachparty.fabric.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.fabric.compat.BeachpartyTrinket;

public class TrinketsCompatibility {
    public static void load() {
        TrinketsApi.registerTrinket(ObjectRegistry.BEACH_HAT.get(), new BeachpartyTrinket.BeachhatTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.CROCS.get(), new BeachpartyTrinket.CrocsTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.SUNGLASSES.get(), new BeachpartyTrinket.SunglassesTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.SWIM_WINGS.get(), new BeachpartyTrinket.SwimWingsTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.TRUNKS.get(), new BeachpartyTrinket.SwimSuitTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.BIKINI.get(), new BeachpartyTrinket.SwimSuitTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.RUBBER_RING_STRIPPED.get(), new BeachpartyTrinket.RubberRingTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.RUBBER_RING_PINK.get(), new BeachpartyTrinket.RubberRingTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.RUBBER_RING_BLUE.get(), new BeachpartyTrinket.RubberRingTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.RUBBER_RING_PELICAN.get(), new BeachpartyTrinket.RubberRingTrinket());
        TrinketsApi.registerTrinket(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), new BeachpartyTrinket.RubberRingTrinket());
    }
}