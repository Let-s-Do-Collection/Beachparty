package net.satisfy.beachparty.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import dev.architectury.platform.Platform;
import net.satisfy.beachparty.client.compat.AccessoriesClientCompat;
import net.satisfy.beachparty.client.gui.overlay.MiniFridgeInfoProvider;
import net.satisfy.beachparty.client.gui.overlay.PalmBarInfoProvider;
import net.satisfy.beachparty.client.gui.overlay.RadioInfoProvider;
import net.satisfy.beachparty.client.gui.overlay.SunLoungerInfoProvider;
import net.satisfy.beachparty.client.gui.overlay.WetHayBaleInfoProvider;
import net.satisfy.beachparty.client.model.*;
import net.satisfy.beachparty.client.renderer.block.CocktailRenderer;
import net.satisfy.beachparty.client.renderer.block.MiniFridgeRenderer;
import net.satisfy.beachparty.client.renderer.block.PalmBarRenderer;
import net.satisfy.beachparty.client.renderer.entity.BeachBallRenderer;
import net.satisfy.beachparty.core.block.DyeableFurniture;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.TabRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import net.satisfy.beachparty.core.util.BeachpartyWoodType;
import net.satisfy.foundation.banner.CompletionistBannerRenderer;
import net.satisfy.foundation.client.armor.ArmorColors;
import net.satisfy.foundation.client.armor.ArmorModels;
import net.satisfy.foundation.client.wood.WoodBoatRenderer;
import net.satisfy.foundation.client.wood.WoodClient;
import net.satisfy.foundation.client.creative.CreativeSideTabs;
import net.satisfy.foundation.client.creative.CreativeTabTooltips;
import net.satisfy.foundation.overlay.BlockInfoOverlay;
import net.satisfy.foundation.rarity.FoundationRarity;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class BeachPartyClient {
    public static void initClient() {
        RenderTypeRegistry.register(RenderType.cutout(), PALM_TABLE.get(), PALM_CHAIR.get(), PALM_TORCH.get(), PALM_WALL_TORCH.get(), TALL_PALM_TORCH.get(), THATCH.get(), THATCH_SLAB.get(), MELON_COCKTAIL.get(), COCONUT_COCKTAIL.get(), HONEY_COCKTAIL.get(), THATCH_STAIRS.get(), SWEETBERRIES_COCKTAIL.get(), PUMPKIN_COCKTAIL.get(), COCOA_COCKTAIL.get(), SANDCASTLE.get(), MESSAGE_IN_A_BOTTLE.get(), PALM_SPROUT.get(), POTTED_PALM_SPROUT.get(), BEACH_PARASOL.get(), BEACH_TOWEL.get(), BEACH_SUN_LOUNGER.get(), SEASHELL_BLOCK.get(), SAND_BUCKET_BLOCK_EMPTY.get(), SAND_BUCKET_BLOCK_FILLED.get(), BEACH_GOAL.get(), PALM_DOOR.get(), PALM_TRAPDOOR.get(), PALM_BAR_STOOL.get(), BEACH_CHAIR.get(), HOODED_BEACH_CHAIR.get());
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tintIndex) -> DyeableFurniture.tint(state, tintIndex), PALM_BAR_STOOL.get(), BEACH_CHAIR.get(), PALM_CHAIR.get(), PALM_TABLE.get(), HOODED_BEACH_CHAIR.get(), BEACH_SUN_LOUNGER.get(), BEACH_TOWEL.get(), BEACH_PARASOL.get());

        RenderTypeRegistry.register(RenderType.translucent(), PALM_GLASS_PANE.get(), PALM_GLASS.get());

        BlockEntityRendererRegistry.register(EntityTypeRegistry.MINI_FRIDGE_BLOCK_ENTITY.get(), MiniFridgeRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.COCKTAIL_BLOCK_ENTITY.get(), CocktailRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.PALM_BAR_BLOCK_ENTITY.get(), PalmBarRenderer::new);
        BlockInfoOverlay.init();
        BlockInfoOverlay.registerProvider(new MiniFridgeInfoProvider());
        BlockInfoOverlay.registerProvider(new PalmBarInfoProvider());
        BlockInfoOverlay.registerProvider(new RadioInfoProvider());
        BlockInfoOverlay.registerProvider(new SunLoungerInfoProvider());
        BlockInfoOverlay.registerProvider(new WetHayBaleInfoProvider());

        WoodClient.registerSignMaterials(BeachpartyWoodType.PALM);
        WoodClient.registerSignRenderers(EntityTypeRegistry.BEACHPARTY_SIGN.get(), EntityTypeRegistry.BEACHPARTY_HANGING_SIGN.get());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEACHPARTY_BANNER.get(), CompletionistBannerRenderer::new);

        ArmorColors.register(TRUNKS.get(), BIKINI.get(), CROCS.get(), SWIM_WINGS.get());
        registerArmorModels();
        BeachpartyUtil.registerColorWeapon(POOL_NOODLE.get(), 1017855);

        CreativeSideTabs.register(TabRegistry.BEACHPARTY_TAB.getKey(),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.beachparty.side.building"), PALM_LOG.get(), TabRegistry::acceptBuilding),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.beachparty.side.beach"), BEACH_BALL.get(), TabRegistry::acceptBeach));
        CreativeTabTooltips.register(TabRegistry.BEACHPARTY_TAB.getKey(), FoundationRarity.RARE);
        if (Platform.isModLoaded("accessories")) {
            AccessoriesClientCompat.init();
        }
    }

    public static Item[] wearables() {
        return new Item[]{BEACH_HAT.get(), SUNGLASSES.get(), RUBBER_RING_PINK.get(), RUBBER_RING_BLUE.get(), RUBBER_RING_STRIPPED.get(), RUBBER_RING_AXOLOTL.get(), RUBBER_RING_PELICAN.get(), BIKINI.get(), SWIM_WINGS.get(), TRUNKS.get(), CROCS.get()};
    }

    public static void registerArmorModels() {
        ArmorModels.register(BeachHatModel.LAYER_LOCATION, BeachHatModel::new, BEACH_HAT.get());
        ArmorModels.register(SunglassesModel.LAYER_LOCATION, SunglassesModel::new, SUNGLASSES.get());
        ArmorModels.register(RubberRingColoredModel.LAYER_LOCATION, RubberRingColoredModel::new, RUBBER_RING_BLUE.get(), RUBBER_RING_PINK.get(), RUBBER_RING_STRIPPED.get());
        ArmorModels.register(RubberRingAxolotlModel.LAYER_LOCATION, RubberRingAxolotlModel::new, RUBBER_RING_AXOLOTL.get());
        ArmorModels.register(RubberRingPelicanModel.LAYER_LOCATION, RubberRingPelicanModel::new, RUBBER_RING_PELICAN.get());
        ArmorModels.register(BikiniModel.LAYER_LOCATION, BikiniModel::new, BIKINI.get());
        ArmorModels.register(SwimWingsModel.LAYER_LOCATION, SwimWingsModel::new, SWIM_WINGS.get());
        ArmorModels.register(TrunksModel.LAYER_LOCATION, TrunksModel::new, TRUNKS.get());
        ArmorModels.register(CrocsModel.LAYER_LOCATION, CrocsModel::new, CROCS.get());
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.BEACH_BALL, BeachBallRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.COCONUT, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.PALM_BOAT, context -> new WoodBoatRenderer(context, false));
        EntityRendererRegistry.register(EntityTypeRegistry.PALM_CHEST_BOAT, context -> new WoodBoatRenderer(context, true));
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
        WoodClient.registerBoatLayers(BeachpartyWoodType.PALM_BOAT);
        EntityModelLayerRegistry.register(WoodBoatRenderer.layer(BeachpartyWoodType.FLOATY_BOAT, false), FloatyBoatModel::createBodyModel);
        EntityModelLayerRegistry.register(WoodBoatRenderer.layer(BeachpartyWoodType.FLOATY_BOAT, true), ChestBoatModel::createBodyModel);
    }
}