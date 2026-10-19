package net.satisfy.beachparty;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.hooks.item.tool.AxeItemHooks;
import dev.architectury.platform.Platform;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.satisfy.beachparty.core.compat.AccessoriesCompat;
import net.satisfy.beachparty.core.event.CommonEvents;
import net.satisfy.beachparty.core.event.WearableEvents;
import net.satisfy.beachparty.core.util.MessageInABottleSpawner;
import net.satisfy.beachparty.core.registry.*;
import net.satisfy.foundation.rarity.FoundationRarities;
import net.satisfy.foundation.rarity.FoundationRarity;

public class Beachparty {
    public static final String MOD_ID = "beachparty";

    public static void init() {
        CompostablesRegistry.init();
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        TabRegistry.init();
        PlacerTypeRegistry.init();
        MobEffectRegistry.init();
        SoundEventRegistry.init();
        CommonEvents.init();
        WearableEvents.init();
        MessageInABottleSpawner.init();
        RecipeTypeRegistry.init();
        LifecycleEvent.SETUP.register(Beachparty::registerRarities);
        if (Platform.isModLoaded("accessories")) {
            AccessoriesCompat.init();
        }
    }

    public static void commonSetup() {
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_LOG.get(), ObjectRegistry.STRIPPED_PALM_LOG.get());
        AxeItemHooks.addStrippable(ObjectRegistry.PALM_WOOD.get(), ObjectRegistry.STRIPPED_PALM_WOOD.get());
    }

    private static void registerRarities() {
        BuiltInRegistries.ITEM.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(MOD_ID))
                .forEach(entry -> FoundationRarities.register(entry.getValue(), FoundationRarity.COMMON));
        registerRarity(FoundationRarity.COMMON,
                ObjectRegistry.COCONUT, ObjectRegistry.COCONUT_OPEN, ObjectRegistry.RAW_MUSSEL_MEAT, ObjectRegistry.COOKED_MUSSEL_MEAT, ObjectRegistry.SEASHELL,
                ObjectRegistry.SAND_BUCKET_EMPTY, ObjectRegistry.SAND_BUCKET_FILLED, ObjectRegistry.PALM_SPROUT,
                ObjectRegistry.PALM_TORCH_ITEM, ObjectRegistry.TALL_PALM_TORCH);
        registerRarity(FoundationRarity.UNCOMMON,
                ObjectRegistry.BEACH_PARASOL, ObjectRegistry.BEACH_TOWEL,
                ObjectRegistry.BEACH_BALL, ObjectRegistry.BEACH_GOAL, ObjectRegistry.POOL_NOODLE, ObjectRegistry.SWIM_WINGS, ObjectRegistry.FLOATY_BOAT, ObjectRegistry.FLOATY_CHEST_BOAT,
                ObjectRegistry.RUBBER_RING_BLUE, ObjectRegistry.RUBBER_RING_PINK, ObjectRegistry.RUBBER_RING_STRIPPED,
                ObjectRegistry.TRUNKS, ObjectRegistry.BIKINI);
        registerRarity(FoundationRarity.RARE,
                ObjectRegistry.COCONUT_COCKTAIL, ObjectRegistry.SWEETBERRIES_COCKTAIL, ObjectRegistry.COCOA_COCKTAIL,
                ObjectRegistry.PUMPKIN_COCKTAIL, ObjectRegistry.HONEY_COCKTAIL, ObjectRegistry.MELON_COCKTAIL,
                ObjectRegistry.MINI_FRIDGE, ObjectRegistry.RADIO, ObjectRegistry.MESSAGE_IN_A_BOTTLE_ITEM,
                ObjectRegistry.SUNGLASSES, ObjectRegistry.RUBBER_RING_PELICAN, ObjectRegistry.RUBBER_RING_AXOLOTL,
                ObjectRegistry.OVERGROWN_DISC, ObjectRegistry.MUSIC_DISC_BEACHPARTY, ObjectRegistry.MUSIC_DISC_CARIBBEAN_BEACH,
                ObjectRegistry.MUSIC_DISC_PRIDELANDS, ObjectRegistry.MUSIC_DISC_VOCALISTA, ObjectRegistry.MUSIC_DISC_WILD_VEINS);
        registerRarity(FoundationRarity.EPIC, ObjectRegistry.BEACH_HAT, ObjectRegistry.CROCS);
        registerRarity(FoundationRarity.LEGENDARY, ObjectRegistry.BEACHPARTY_BANNER);
    }

    @SafeVarargs
    private static void registerRarity(FoundationRarity rarity, Supplier<? extends ItemLike>... entries) {
        for (Supplier<? extends ItemLike> entry : entries) {
            FoundationRarities.register(entry.get(), rarity);
        }
    }
}
