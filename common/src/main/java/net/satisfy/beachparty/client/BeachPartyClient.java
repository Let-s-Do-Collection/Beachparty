package net.satisfy.beachparty.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.satisfy.beachparty.client.gui.MiniFridgeGui;
import net.satisfy.beachparty.client.gui.PalmBarGui;
import net.satisfy.beachparty.client.model.*;
import net.satisfy.beachparty.client.renderer.block.CompletionistBannerRenderer;
import net.satisfy.beachparty.client.renderer.block.PalmHangingSignRenderer;
import net.satisfy.beachparty.client.renderer.block.PalmSignRenderer;
import net.satisfy.beachparty.client.renderer.entity.BeachBallRenderer;
import net.satisfy.beachparty.client.renderer.entity.ChairRenderer;
import net.satisfy.beachparty.client.renderer.entity.PalmBoatRenderer;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ScreenHandlerTypeRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class BeachPartyClient {
    public static void initClient() {
        RenderTypeRegistry.register(RenderType.cutout(), PALM_TABLE.get(), PALM_CHAIR.get(), PALM_TORCH.get(), PALM_WALL_TORCH.get(), TALL_PALM_TORCH.get(), THATCH.get(), THATCH_SLAB.get(), MELON_COCKTAIL.get(), COCONUT_COCKTAIL.get(), HONEY_COCKTAIL.get(), THATCH_STAIRS.get(), SWEETBERRIES_COCKTAIL.get(), PUMPKIN_COCKTAIL.get(), COCOA_COCKTAIL.get(), SANDCASTLE.get(), MESSAGE_IN_A_BOTTLE.get(), PALM_SPROUT.get(), BEACH_PARASOL.get(), BEACH_TOWEL.get(), BEACH_SUN_LOUNGER.get(), SEASHELL_BLOCK.get(), SAND_BUCKET_BLOCK_EMPTY.get(), SAND_BUCKET_BLOCK_FILLED.get(), BEACH_GOAL.get(), PALM_DOOR.get(), PALM_TRAPDOOR.get(), PALM_BAR_STOOL.get());

        RenderTypeRegistry.register(RenderType.translucent(), PALM_GLASS_PANE.get(), PALM_GLASS.get());

        MenuRegistry.registerScreenFactory(ScreenHandlerTypeRegistry.PALM_BAR_GUI_HANDLER.get(), PalmBarGui::new);
        MenuRegistry.registerScreenFactory(ScreenHandlerTypeRegistry.MINI_FRIDGE_GUI_HANDLER.get(), MiniFridgeGui::new);

        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEACHPARTY_SIGN.get(), PalmSignRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEACHPARTY_HANGING_SIGN.get(), PalmHangingSignRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEACHPARTY_BANNER.get(), CompletionistBannerRenderer::new);

        BeachpartyUtil.registerColorArmor(TRUNKS.get(), 16715535);
        BeachpartyUtil.registerColorArmor(BIKINI.get(), 987135);
        BeachpartyUtil.registerColorArmor(CROCS.get(), 1048335);
        BeachpartyUtil.registerColorArmor(SWIM_WINGS.get(), 0xFF5800);
        BeachpartyUtil.registerColorWeapon(POOL_NOODLE.get(), 1017855);
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.BEACH_BALL, BeachBallRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.CHAIR, ChairRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.COCONUT, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.PALM_BOAT, context -> new PalmBoatRenderer<>(context, false));
        EntityRendererRegistry.register(EntityTypeRegistry.PALM_CHEST_BOAT, context -> new PalmBoatRenderer<>(context, true));
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(BeachHatModel.LAYER_LOCATION, BeachHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(SunglassesModel.LAYER_LOCATION, SunglassesModel::createBodyLayer);
        EntityModelLayerRegistry.register(RubberRingColoredModel.LAYER_LOCATION, RubberRingColoredModel::createBodyLayer);
        EntityModelLayerRegistry.register(RubberRingAxolotlModel.LAYER_LOCATION, RubberRingAxolotlModel::createBodyLayer);
        EntityModelLayerRegistry.register(RubberRingPelicanModel.LAYER_LOCATION, RubberRingPelicanModel::createBodyLayer);
        EntityModelLayerRegistry.register(BikiniModel.LAYER_LOCATION, BikiniModel::createBodyLayer);
        EntityModelLayerRegistry.register(SwimWingsModel.LAYER_LOCATION, SwimWingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(TrunksModel.LAYER_LOCATION, TrunksModel::createBodyLayer);
        EntityModelLayerRegistry.register(CrocsModel.LAYER_LOCATION, CrocsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BeachBallModel.LAYER_LOCATION, BeachBallModel::createBodyLayer);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
        EntityModelLayerRegistry.register(FloatyBoatModel.LAYER_LOCATION, BeachHatModel::createBodyLayer);
    }
}