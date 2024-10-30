package net.satisfy.beachparty.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.Beachparty;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> BEACHPARTY_TABS = DeferredRegister.create(Beachparty.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BEACHPARTY_TAB = BEACHPARTY_TABS.register("beachparty", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.COCONUT_COCKTAIL.get()))
            .title(Component.translatable("creativetab.beachparty.tab").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))))
            .displayItems((parameters, out) -> {
                out.accept(ObjectRegistry.CABINET.get());
                out.accept(ObjectRegistry.TIKI_BAR.get());
                out.accept(ObjectRegistry.LOUNGE_CHAIR.get());
                out.accept(ObjectRegistry.CHAIR.get());
                out.accept(ObjectRegistry.TABLE.get());
                out.accept(ObjectRegistry.BEACH_CHAIR.get());
                out.accept(ObjectRegistry.DECK_CHAIR.get());
                out.accept(ObjectRegistry.TIKI_CHAIR.get());
                out.accept(ObjectRegistry.THATCH.get());
                out.accept(ObjectRegistry.THATCH_STAIRS.get());
                out.accept(ObjectRegistry.THATCH_SLAB.get());
                out.accept(ObjectRegistry.TIKI_TORCH_ITEM.get());
                out.accept(ObjectRegistry.TALL_TIKI_TORCH.get());
                out.accept(ObjectRegistry.MINI_FRIDGE.get());
                out.accept(ObjectRegistry.RADIO.get());
                out.accept(ObjectRegistry.OVERGROWN_DISC.get());
                out.accept(ObjectRegistry.MESSAGE_IN_A_BOTTLE_ITEM.get());
                out.accept(ObjectRegistry.SEASHELL.get());
                out.accept(ObjectRegistry.SAND_BUCKET.get());
                out.accept(ObjectRegistry.EMPTY_SAND_BUCKET.get());
                out.accept(ObjectRegistry.COCONUT.get());
                out.accept(ObjectRegistry.COCONUT_OPEN.get());
                out.accept(ObjectRegistry.COCONUT_COCKTAIL.get());
                out.accept(ObjectRegistry.SWEETBERRIES_COCKTAIL.get());
                out.accept(ObjectRegistry.COCOA_COCKTAIL.get());
                out.accept(ObjectRegistry.PUMPKIN_COCKTAIL.get());
                out.accept(ObjectRegistry.MELON_COCKTAIL.get());
                out.accept(ObjectRegistry.HONEY_COCKTAIL.get());
                out.accept(ObjectRegistry.SWEETBERRY_MILKSHAKE.get());
                out.accept(ObjectRegistry.COCONUT_MILKSHAKE.get());
                out.accept(ObjectRegistry.CHOCOLATE_MILKSHAKE.get());
                out.accept(ObjectRegistry.SWEETBERRY_SUNDAE.get());
                out.accept(ObjectRegistry.COCONUT_SUNDAE.get());
                out.accept(ObjectRegistry.CHOCOLATE_SUNDAE.get());
                out.accept(ObjectRegistry.REFRESHING_DRINK.get());
                out.accept(ObjectRegistry.ICECREAM_COCONUT.get());
                out.accept(ObjectRegistry.ICECREAM_CACTUS.get());
                out.accept(ObjectRegistry.ICECREAM_CHOCOLATE.get());
                out.accept(ObjectRegistry.ICECREAM_SWEETBERRIES.get());
                out.accept(ObjectRegistry.ICECREAM_MELON.get());
                out.accept(ObjectRegistry.RAW_MUSSEL_MEAT.get());
                out.accept(ObjectRegistry.COOKED_MUSSEL_MEAT.get());
                out.accept(ObjectRegistry.BEACH_TOWEL.get());
                out.accept(ObjectRegistry.BEACH_HAT.get());
                out.accept(ObjectRegistry.SUNGLASSES.get());
                out.accept(ObjectRegistry.TRUNKS.get());
                out.accept(ObjectRegistry.BIKINI.get());
                out.accept(ObjectRegistry.CROCS.get());
                out.accept(ObjectRegistry.SWIM_WINGS.get());
                out.accept(ObjectRegistry.RUBBER_RING_BLUE.get());
                out.accept(ObjectRegistry.RUBBER_RING_PINK.get());
                out.accept(ObjectRegistry.RUBBER_RING_STRIPPED.get());
                out.accept(ObjectRegistry.RUBBER_RING_AXOLOTL.get());
                out.accept(ObjectRegistry.RUBBER_RING_PELICAN.get());
                out.accept(ObjectRegistry.POOL_NOODLE.get());
                out.accept(ObjectRegistry.BEACH_PARASOL.get());

            })
            .build());

    public static void init() {
        BEACHPARTY_TABS.register();
    }
}
