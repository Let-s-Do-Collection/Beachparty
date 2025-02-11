package net.satisfy.beachparty;

import net.satisfy.beachparty.core.event.CommonEvents;
import net.satisfy.beachparty.core.registry.*;
import dev.architectury.hooks.item.tool.AxeItemHooks;

public class Beachparty {
    public static final String MOD_ID = "beachparty";

    public static void init() {
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        TabRegistry.init();
        MobEffectRegistry.init();
        SoundEventRegistry.init();
        ScreenHandlerTypesRegistry.init();
        CommonEvents.init();
        RecipeRegistry.init();
    }

    public static void commonSetup() {
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_LOG.get(), ObjectRegistry.STRIPPED_PALM_LOG.get());
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_WOOD.get(), ObjectRegistry.STRIPPED_PALM_WOOD.get());
    }
}