package net.satisfy.beachparty.fabric.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.fabric.client.renderer.*;
import org.lwjgl.glfw.GLFW;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

public class BeachpartyFabricClient implements ClientModInitializer {
    private static KeyMapping dashKey;

    @Override
    public void onInitializeClient() {
        BeachPartyClient.preInitClient();
        BeachPartyClient.initClient();
        registerKeybind();
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
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getModelLocation()), "main"), BoatModel::createBodyModel);
            EntityModelLayerRegistry.registerModelLayer(new ModelLayerLocation(new ResourceLocation(modId, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }

    public static void registerKeybind() {
        dashKey = new KeyMapping("key.beachparty.dash", GLFW.GLFW_KEY_R, "key.categories.beachparty");
        KeyBindingHelper.registerKeyBinding(dashKey);
    }

    public static boolean isDashKeyPressed() {
        return dashKey != null && dashKey.isDown();
    }
}
