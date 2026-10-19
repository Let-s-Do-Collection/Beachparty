package net.satisfy.beachparty.core.compat;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.satisfy.foundation.armor.Wearing;

public final class AccessoriesCompat {
    private AccessoriesCompat() {
    }

    public static void init() {
        Wearing.registerSlotProvider((entity, item) -> AccessoriesCapability.getOptionally(entity)
                .map(capability -> capability.isEquipped(item))
                .orElse(false));
    }
}
