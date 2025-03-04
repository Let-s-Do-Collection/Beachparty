package net.satisfy.beachparty.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.BeachSunLounger;
import net.satisfy.beachparty.core.block.BeachTowelBlock;
import net.satisfy.beachparty.core.registry.CompostablesRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.forge.client.integration.CuriosWearableTrinket;
import net.satisfy.beachparty.forge.registry.BeachpartyConfig;
import net.satisfy.beachparty.forge.registry.BeachpartyVillagers;
import net.satisfy.beachparty.platform.forge.PlatformHelperImpl;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(Beachparty.MOD_ID)
public class BeachpartyForge {
    public BeachpartyForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Beachparty.MOD_ID, modEventBus);
        BeachpartyVillagers.register(modEventBus);
        PlatformHelperImpl.ENTITY_TYPES.register(modEventBus);
        BeachpartyConfig.loadConfig(BeachpartyConfig.COMMON_CONFIG, Platform.getConfigFolder().resolve("beachparty.toml").toString());

        Beachparty.init();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
        CuriosApi.registerCurio(ObjectRegistry.BEACH_HAT.get(), new CuriosWearableTrinket.BaseCurio(0.10f));
        CuriosApi.registerCurio(ObjectRegistry.SUNGLASSES.get(), new CuriosWearableTrinket.BaseCurio(0.12f));
        CuriosApi.registerCurio(ObjectRegistry.SWIM_WINGS.get(), new CuriosWearableTrinket.SwimWingsCurio());
        CuriosApi.registerCurio(ObjectRegistry.BIKINI.get(), new CuriosWearableTrinket.SwimSuitCurio());
        CuriosApi.registerCurio(ObjectRegistry.TRUNKS.get(), new CuriosWearableTrinket.SwimSuitCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_BLUE.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PINK.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_STRIPPED.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PELICAN.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.CROCS.get(), new CuriosWearableTrinket.CrocsCurio());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("feet").size(1).icon(new ResourceLocation("minecraft", "item/empty_armor_slot_boots")).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HANDS.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }


    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() == ObjectRegistry.RADIO.get()) {
            event.setCanceled(true);
        }
    }

    @Mod.EventBusSubscriber(modid = Beachparty.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventsHandler {

        @SubscribeEvent
        public static void playerSetSpawn(PlayerSetSpawnEvent event) {
            Level level = event.getEntity().level();

            if (event.getNewSpawn() != null) {
                Block block = level.getBlockState(event.getNewSpawn()).getBlock();

                if (!level.isClientSide && (block instanceof BeachTowelBlock || block instanceof BeachSunLounger) && !event.isForced()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
