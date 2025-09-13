package net.satisfy.beachparty.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.BeachSunLounger;
import net.satisfy.beachparty.core.block.BeachTowelBlock;
import net.satisfy.beachparty.core.registry.CompostablesRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.neoforge.client.integration.CuriosWearableTrinket;
import net.satisfy.beachparty.neoforge.core.config.BeachpartyNeoForgeConfig;
import net.satisfy.beachparty.neoforge.core.registry.BeachpartyVillagers;
import net.satisfy.beachparty.platform.neoforge.PlatformHelperImpl;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(Beachparty.MOD_ID)
public class BeachpartyNeoForge {
    public BeachpartyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        EventBusesHooks.whenAvailable(Beachparty.MOD_ID, IEventBus::start);
        BeachpartyVillagers.register(modEventBus);
        PlatformHelperImpl.ENTITY_TYPES.register(modEventBus);
        Beachparty.init();
        modContainer.registerConfig(ModConfig.Type.COMMON, BeachpartyNeoForgeConfig.COMMON_CONFIG);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
        Beachparty.commonSetup();
        CuriosApi.registerCurio(ObjectRegistry.BEACH_HAT.get(), new CuriosWearableTrinket.BaseCurio());
        CuriosApi.registerCurio(ObjectRegistry.SUNGLASSES.get(), new CuriosWearableTrinket.BaseCurio());
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
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("feet").size(1).icon(ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots")).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HANDS.getMessageBuilder().build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() == ObjectRegistry.RADIO.get()) event.setCanceled(true);
    }

    @EventBusSubscriber(modid = Beachparty.MOD_ID)
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
