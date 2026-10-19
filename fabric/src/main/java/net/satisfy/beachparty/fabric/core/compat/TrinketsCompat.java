package net.satisfy.beachparty.fabric.core.compat;

import dev.emi.trinkets.api.TrinketsApi;
import net.satisfy.foundation.armor.Wearing;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static void init() {
        Wearing.registerSlotProvider((entity, item) -> TrinketsApi.getTrinketComponent(entity)
                .map(component -> component.isEquipped(item))
                .orElse(false));
    }
}
