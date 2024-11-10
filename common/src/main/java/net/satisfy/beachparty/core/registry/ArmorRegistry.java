package net.satisfy.beachparty.core.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.beachparty.client.model.*;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {
    private static final Map<Item, HatModel> hatModel = new HashMap<>();
    private static final Map<Item, ChestplateModel> chestplateModel = new HashMap<>();
    private static final Map<Item, LeggingsModel> leggingsModel = new HashMap<>();

    public static Model chestplateModel(Item item, ModelPart baseBody, ModelPart leftArm, ModelPart rightArm) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        ChestplateModel model = chestplateModel.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.RUBBER_RING_BLUE.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_PINK.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_STRIPPED.get()) {
                return new RubberRingColoredModel<>(modelSet.bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_AXOLOTL.get()) {
                return new RubberRingAxolotlModel<>(modelSet.bakeLayer(RubberRingAxolotlModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.RUBBER_RING_PELICAN.get()) {
                return new RubberRingPelicanModel<>(modelSet.bakeLayer(RubberRingPelicanModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.BIKINI.get()) {
                return new BikiniModel<>(modelSet.bakeLayer(BikiniModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.SWIM_WINGS.get()) {
                return new SwimWingsModel<>(modelSet.bakeLayer(SwimWingsModel.LAYER_LOCATION));

            } else {
                return null;
            }
        });

        assert model != null;

        model.copyBody(baseBody, leftArm, rightArm);

        return (Model) model;
    }

    public static Model HelmetModel(Item item, ModelPart baseHead) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        HatModel model = hatModel.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.BEACH_HAT.get()) {
                return new BeachHatModel<>(modelSet.bakeLayer(BeachHatModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.SUNGLASSES.get()) {
                return new SunglassesModel<>(modelSet.bakeLayer(SunglassesModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });

        assert model != null;

        model.copyHead(baseHead);

        return (Model) model;
    }

    public static Model LeggingsModel(Item item, ModelPart baseBody, ModelPart leftLeg, ModelPart rightLeg) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        LeggingsModel model = leggingsModel.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.TRUNKS.get()) {
                return new TrunksModel<>(modelSet.bakeLayer(TrunksModel.LAYER_LOCATION));
            } else if (key == ObjectRegistry.CROCS.get()) {
                return new CrocsModel<>(modelSet.bakeLayer(CrocsModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });

        assert model != null;

        model.copyBody(baseBody, leftLeg, rightLeg);

        return (Model) model;
    }
}
