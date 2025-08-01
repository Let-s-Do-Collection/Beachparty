package net.satisfy.beachparty.fabric.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.client.model.FloatyBoatModel;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.fabric.client.renderer.*;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

public class BeachpartyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();
        registerBoatModels();

        ArmorRenderer.register(new HelmetRenderer(), BEACH_HAT.get(), SUNGLASSES.get());
        ArmorRenderer.register(new ChestplateRenderer(), RUBBER_RING_PINK.get(), RUBBER_RING_BLUE.get(), RUBBER_RING_STRIPPED.get(), RUBBER_RING_AXOLOTL.get(), RUBBER_RING_PELICAN.get(), BIKINI.get(), SWIM_WINGS.get());
        ArmorRenderer.register(new LeggingsRenderer(), TRUNKS.get(), CROCS.get());

        TrinketRendererRegistry.registerRenderer(BEACH_HAT.get(), new HelmetTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(SUNGLASSES.get(), new HelmetTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(BIKINI.get(), new DyeableChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(SWIM_WINGS.get(), new DyeableChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(RUBBER_RING_PELICAN.get(), new ChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(RUBBER_RING_AXOLOTL.get(), new ChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(RUBBER_RING_STRIPPED.get(), new ChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(RUBBER_RING_BLUE.get(), new ChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(RUBBER_RING_PINK.get(), new ChestplateTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(TRUNKS.get(), new DyeableLeggingsTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CROCS.get(), new DyeableLeggingsTrinketRenderer());
    }

    private void registerBoatModels() {
        for (PalmBoatEntity.Type type : PalmBoatEntity.Type.values()) {
            String modId = Beachparty.MOD_ID;
            ModelLayerLocation modelLayerLocation = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(modId, type.getModelLocation()), "main");
            if (type == PalmBoatEntity.Type.FLOATY) {
                EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, FloatyBoatModel::createBodyModel);
            } else {
                EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, BoatModel::createBodyModel);
            }
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(modId, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }
}
