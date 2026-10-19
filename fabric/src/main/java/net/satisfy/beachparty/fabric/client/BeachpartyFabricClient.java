package net.satisfy.beachparty.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.fabric.client.compat.TrinketsClientCompat;
import net.satisfy.foundation.fabric.client.FoundationArmorRenderer;

public class BeachpartyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();

        ArmorRenderer.register(FoundationArmorRenderer.INSTANCE, BeachPartyClient.wearables());

        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketsClientCompat.init();
        }
    }
}
