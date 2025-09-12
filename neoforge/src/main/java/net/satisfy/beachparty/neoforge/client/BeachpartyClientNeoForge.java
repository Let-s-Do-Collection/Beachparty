package net.satisfy.beachparty.neoforge.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.client.gui.MiniFridgeGui;
import net.satisfy.beachparty.client.gui.PalmBarGui;
import net.satisfy.beachparty.client.model.FloatyBoatModel;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypesRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.neoforge.client.integration.CuriosRubberRingAxolotlRenderer;
import net.satisfy.beachparty.neoforge.client.integration.CuriosRubberRingPelicanRenderer;
import net.satisfy.beachparty.neoforge.client.integration.CuriosRubberRingRenderer;
import net.satisfy.beachparty.neoforge.client.renderer.player.layers.RubberRingAxolotlLayer;
import net.satisfy.beachparty.neoforge.client.renderer.player.layers.RubberRingLayer;
import net.satisfy.beachparty.neoforge.client.renderer.player.layers.RubberRingPelicanLayer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.function.Function;

@EventBusSubscriber(modid = Beachparty.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BeachpartyClientNeoForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BeachPartyClient.preInitClient();
    }

    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(ScreenHandlerTypesRegistry.PALM_BAR_GUI_HANDLER.get(), PalmBarGui::new);
        event.register(ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), MiniFridgeGui::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BeachPartyClient.initClient();
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_BLUE.get(), CuriosRubberRingRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_PINK.get(), CuriosRubberRingRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_STRIPPED.get(), CuriosRubberRingRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_PELICAN.get(), CuriosRubberRingPelicanRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), CuriosRubberRingAxolotlRenderer::new);
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (PalmBoatEntity.Type type : PalmBoatEntity.Type.values()) {
            if (type == PalmBoatEntity.Type.FLOATY) {
                event.registerLayerDefinition(new ModelLayerLocation(BeachpartyIdentifier.identifier(type.getModelLocation()), "main"), FloatyBoatModel::createBodyModel);
                event.registerLayerDefinition(new ModelLayerLocation(BeachpartyIdentifier.identifier(type.getChestModelLocation()), "main"), FloatyBoatModel::createBodyModel);
            } else {
                event.registerLayerDefinition(new ModelLayerLocation(BeachpartyIdentifier.identifier(type.getModelLocation()), "main"), BoatModel::createBodyModel);
                event.registerLayerDefinition(new ModelLayerLocation(BeachpartyIdentifier.identifier( type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
            }
        }
    }

    @SubscribeEvent
    public static void registerLayersForRenderers(EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, "default", RubberRingLayer::new);
        addLayerToPlayerSkin(event, "default", RubberRingPelicanLayer::new);
        addLayerToPlayerSkin(event, "default", RubberRingAxolotlLayer::new);
        addLayerToPlayerSkin(event, "slim", RubberRingAxolotlLayer::new);
        addLayerToPlayerSkin(event, "slim", RubberRingLayer::new);
        addLayerToPlayerSkin(event, "slim", RubberRingPelicanLayer::new);
    }

    private static <E extends Player, M extends HumanoidModel<E>>
    void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory) {
        LivingEntityRenderer<E, M> renderer = event.getSkin(PlayerSkin.Model.byName(skinName));
        if (renderer != null) renderer.addLayer(factory.apply(renderer));
    }
}
