package net.satisfy.beachparty.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.BeachSunLounger;
import net.satisfy.beachparty.core.block.BeachTowelBlock;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.neoforge.core.compat.CuriosCompat;
import net.satisfy.beachparty.neoforge.core.config.BeachpartyNeoForgeConfig;
import net.satisfy.beachparty.neoforge.core.registry.BeachpartyVillagers;
import net.satisfy.beachparty.neoforge.core.world.ConfiguredFeatureBiomeModifier;
import net.satisfy.beachparty.platform.neoforge.PlatformHelperImpl;

@Mod(Beachparty.MOD_ID)
public class BeachpartyNeoForge {
    public BeachpartyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        EventBusesHooks.whenAvailable(Beachparty.MOD_ID, IEventBus::start);
        BeachpartyVillagers.register(modEventBus);
        PlatformHelperImpl.ENTITY_TYPES.register(modEventBus);
        Beachparty.init();
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }
        modContainer.registerConfig(ModConfig.Type.COMMON, BeachpartyNeoForgeConfig.COMMON_CONFIG);
        modContainer.registerConfig(ModConfig.Type.STARTUP, BeachpartyNeoForgeConfig.STARTUP_CONFIG);
        modEventBus.addListener(BeachpartyNeoForgeConfig::onLoad);
        modEventBus.addListener(BeachpartyNeoForgeConfig::onReload);
        ConfiguredFeatureBiomeModifier.SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Beachparty.commonSetup();
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
