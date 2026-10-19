package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.Beachparty;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> BEACHPARTY_TABS = DeferredRegister.create(Beachparty.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BEACHPARTY_TAB = BEACHPARTY_TABS.register("beachparty", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.COCONUT_COCKTAIL.get()))
            .title(Component.translatable("creativetab.beachparty.tab"))
            .displayItems((parameters, output) -> {
                acceptBuilding(output);
                acceptBeach(output);
            })
            .build());

    public static void acceptBuilding(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.PALM_LOG.get());
        output.accept(ObjectRegistry.PALM_WOOD.get());
        output.accept(ObjectRegistry.STRIPPED_PALM_LOG.get());
        output.accept(ObjectRegistry.STRIPPED_PALM_WOOD.get());
        output.accept(ObjectRegistry.PALM_PLANKS.get());
        output.accept(ObjectRegistry.PALM_FLOORBOARD.get());
        output.accept(ObjectRegistry.PALM_STAIRS.get());
        output.accept(ObjectRegistry.PALM_SLAB.get());
        output.accept(ObjectRegistry.PALM_FENCE.get());
        output.accept(ObjectRegistry.PALM_FENCE_GATE.get());
        output.accept(ObjectRegistry.PALM_DOOR.get());
        output.accept(ObjectRegistry.PALM_TRAPDOOR.get());
        output.accept(ObjectRegistry.PALM_PRESSURE_PLATE.get());
        output.accept(ObjectRegistry.PALM_BUTTON.get());
        output.accept(ObjectRegistry.PALM_GLASS.get());
        output.accept(ObjectRegistry.PALM_GLASS_PANE.get());
        output.accept(ObjectRegistry.PALM_SIGN_ITEM.get());
        output.accept(ObjectRegistry.PALM_HANGING_SIGN_ITEM.get());
        output.accept(ObjectRegistry.PALM_BOAT.get());
        output.accept(ObjectRegistry.PALM_CHEST_BOAT.get());
        output.accept(ObjectRegistry.THATCH.get());
        output.accept(ObjectRegistry.THATCH_STAIRS.get());
        output.accept(ObjectRegistry.THATCH_SLAB.get());
        output.accept(ObjectRegistry.WET_HAY_BLOCK.get());
        output.accept(ObjectRegistry.SANDWAVES.get());
        output.accept(ObjectRegistry.PALM_LEAVES.get());
        output.accept(ObjectRegistry.PALM_SPROUT.get());
        output.accept(ObjectRegistry.PALM_TORCH_ITEM.get());
        output.accept(ObjectRegistry.TALL_PALM_TORCH.get());
    }

    public static void acceptBeach(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.PALM_CABINET.get());
        output.accept(ObjectRegistry.PALM_BAR.get());
        output.accept(ObjectRegistry.PALM_BAR_STOOL.get());
        output.accept(ObjectRegistry.PALM_CHAIR.get());
        output.accept(ObjectRegistry.PALM_TABLE.get());
        output.accept(ObjectRegistry.BEACH_CHAIR.get());
        output.accept(ObjectRegistry.HOODED_BEACH_CHAIR.get());
        output.accept(ObjectRegistry.BEACH_SUN_LOUNGER.get());
        output.accept(ObjectRegistry.BEACH_TOWEL.get());
        output.accept(ObjectRegistry.BEACH_PARASOL.get());
        output.accept(ObjectRegistry.MINI_FRIDGE.get());
        output.accept(ObjectRegistry.RADIO.get());
        output.accept(ObjectRegistry.BEACH_GOAL.get());
        output.accept(ObjectRegistry.BEACHPARTY_BANNER.get());
        output.accept(ObjectRegistry.COCONUT.get());
        output.accept(ObjectRegistry.COCONUT_OPEN.get());
        output.accept(ObjectRegistry.COCONUT_COCKTAIL.get());
        output.accept(ObjectRegistry.SWEETBERRIES_COCKTAIL.get());
        output.accept(ObjectRegistry.COCOA_COCKTAIL.get());
        output.accept(ObjectRegistry.PUMPKIN_COCKTAIL.get());
        output.accept(ObjectRegistry.MELON_COCKTAIL.get());
        output.accept(ObjectRegistry.HONEY_COCKTAIL.get());
        output.accept(ObjectRegistry.RAW_MUSSEL_MEAT.get());
        output.accept(ObjectRegistry.COOKED_MUSSEL_MEAT.get());
        output.accept(ObjectRegistry.SEASHELL.get());
        output.accept(ObjectRegistry.MESSAGE_IN_A_BOTTLE_ITEM.get());
        output.accept(ObjectRegistry.SAND_BUCKET_EMPTY.get());
        output.accept(ObjectRegistry.SAND_BUCKET_FILLED.get());
        output.accept(ObjectRegistry.OVERGROWN_DISC.get());
        output.accept(ObjectRegistry.MUSIC_DISC_BEACHPARTY.get());
        output.accept(ObjectRegistry.MUSIC_DISC_CARIBBEAN_BEACH.get());
        output.accept(ObjectRegistry.MUSIC_DISC_PRIDELANDS.get());
        output.accept(ObjectRegistry.MUSIC_DISC_VOCALISTA.get());
        output.accept(ObjectRegistry.MUSIC_DISC_WILD_VEINS.get());
        output.accept(ObjectRegistry.BEACH_HAT.get());
        output.accept(ObjectRegistry.SUNGLASSES.get());
        output.accept(ObjectRegistry.TRUNKS.get());
        output.accept(ObjectRegistry.BIKINI.get());
        output.accept(ObjectRegistry.CROCS.get());
        output.accept(ObjectRegistry.SWIM_WINGS.get());
        output.accept(ObjectRegistry.BEACH_BALL.get());
        output.accept(ObjectRegistry.POOL_NOODLE.get());
        output.accept(ObjectRegistry.RUBBER_RING_BLUE.get());
        output.accept(ObjectRegistry.RUBBER_RING_PINK.get());
        output.accept(ObjectRegistry.RUBBER_RING_STRIPPED.get());
        output.accept(ObjectRegistry.RUBBER_RING_AXOLOTL.get());
        output.accept(ObjectRegistry.RUBBER_RING_PELICAN.get());
        output.accept(ObjectRegistry.FLOATY_BOAT.get());
    }

    public static void init() {
        BEACHPARTY_TABS.register();
    }
}
