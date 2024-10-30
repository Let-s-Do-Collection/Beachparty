package net.satisfy.beachparty.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.furniture.BeachTowelBlock;
import net.satisfy.beachparty.forge.registry.BeachpartyConfig;
import net.satisfy.beachparty.registry.CompostablesRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;

@Mod(Beachparty.MOD_ID)
public class BeachpartyForge {
    public BeachpartyForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Beachparty.MOD_ID, modEventBus);
        BeachpartyConfig.loadConfig(BeachpartyConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("beachparty.toml").toString());

        Beachparty.init();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
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
        public static void playerSetSpawn(PlayerSetSpawnEvent event)
        {
            Level level = event.getEntity().level();

            if(event.getNewSpawn() != null)
            {
                Block block = level.getBlockState(event.getNewSpawn()).getBlock();

                if(!level.isClientSide && block instanceof BeachTowelBlock && !event.isForced())
                {
                    event.setCanceled(true);
                }
            }
        }
    }
}
