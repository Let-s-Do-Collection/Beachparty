package net.satisfy.beachparty.forge.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.client.model.FloatyBoatModel;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;

@Mod.EventBusSubscriber(modid = Beachparty.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeachpartyClientForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BeachPartyClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BeachPartyClient.initClient();
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (PalmBoatEntity.Type type : PalmBoatEntity.Type.values()) {
            ModelLayerLocation modelLayerLocation = new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getModelLocation()), "main");

            if (type == PalmBoatEntity.Type.FLOATY) {
                event.registerLayerDefinition(modelLayerLocation, FloatyBoatModel::createBodyModel);
            } else {
                event.registerLayerDefinition(modelLayerLocation, BoatModel::createBodyModel);
            }

            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }
}
