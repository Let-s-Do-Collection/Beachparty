package net.satisfy.beachparty;

import dev.architectury.hooks.item.tool.AxeItemHooks;
import net.satisfy.beachparty.core.event.CommonEvents;
import net.satisfy.beachparty.core.registry.*;

public class Beachparty {
    public static final String MOD_ID = "beachparty";

    public static void init() {
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        TabRegistry.init();
        PlacerTypeRegistry.init();
        MobEffectRegistry.init();
        SoundEventRegistry.init();
        ScreenHandlerTypeRegistry.init();
        CommonEvents.init();
        RecipeTypeRegistry.init();
    }

    public static void commonSetup() {
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_LOG.get(), ObjectRegistry.STRIPPED_PALM_LOG.get());
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_WOOD.get(), ObjectRegistry.STRIPPED_PALM_WOOD.get());
    }
}