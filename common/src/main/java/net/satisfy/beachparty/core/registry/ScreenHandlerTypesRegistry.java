package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.gui.handler.MiniFridgeGuiHandler;
import net.satisfy.beachparty.client.gui.handler.PalmBarGuiHandler;

import java.util.function.Supplier;


public class ScreenHandlerTypesRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.MENU);

    public static void init() {
        MENU_TYPES.register();
    }

    public static final RegistrySupplier<MenuType<PalmBarGuiHandler>> PALM_BAR_GUI_HANDLER = create("palm_bar_gui_handler", () -> new MenuType<>(PalmBarGuiHandler::new, FeatureFlags.VANILLA_SET));

    private static <T extends MenuType<?>> RegistrySupplier<T> create(String name, Supplier<T> type) {
        return MENU_TYPES.register(name, type);
    }

    public static final RegistrySupplier<MenuType<MiniFridgeGuiHandler>> MINI_FRIDGE_GUI_HANDLER = create("mini_fridge_gui_handler", () -> new MenuType<>(MiniFridgeGuiHandler::new, FeatureFlags.VANILLA_SET));


}
