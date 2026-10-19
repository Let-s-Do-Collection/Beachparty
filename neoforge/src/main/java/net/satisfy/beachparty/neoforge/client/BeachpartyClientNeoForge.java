package net.satisfy.beachparty.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.neoforge.client.compat.CuriosClientCompat;
import net.satisfy.foundation.neoforge.client.FoundationArmorExtensions;

@EventBusSubscriber(modid = Beachparty.MOD_ID, value = Dist.CLIENT)
public class BeachpartyClientNeoForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BeachPartyClient.preInitClient();
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(FoundationArmorExtensions.INSTANCE, BeachPartyClient.wearables());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BeachPartyClient.initClient();
            if (ModList.get().isLoaded("curios")) {
                CuriosClientCompat.init();
            }
        });
    }
}
