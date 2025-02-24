package net.satisfy.beachparty.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.satisfy.beachparty.Beachparty;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public class FloatyBoatModel extends ListModel<Boat> implements WaterPatchModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, "floaty_boat"), "main");

    private static final String LEFT_PADDLE = "left_paddle";
    private static final String RIGHT_PADDLE = "right_paddle";
    private static final String WATER_PATCH = "water_patch";
    private static final String BOTTOM = "bottom";
    private static final String BACK = "back";
    private static final String FRONT = "front";
    private static final String RIGHT = "right";
    private static final String LEFT = "left";
    private final ModelPart leftPaddle;
    private final ModelPart rightPaddle;
    private final ModelPart waterPatch;
    private final ImmutableList<ModelPart> parts;

    public FloatyBoatModel(ModelPart modelPart) {
        this.leftPaddle = modelPart.getChild("left_paddle");
        this.rightPaddle = modelPart.getChild("right_paddle");
        this.waterPatch = modelPart.getChild("water_patch");
        this.parts = this.createPartsBuilder(modelPart).build();
    }

    protected ImmutableList.Builder<ModelPart> createPartsBuilder(ModelPart modelPart) {
        ImmutableList.Builder<ModelPart> builder = new ImmutableList.Builder<>();
        builder.add(modelPart.getChild("bottom"), modelPart.getChild("back"), modelPart.getChild("front"), modelPart.getChild("right"), modelPart.getChild("left"), this.leftPaddle, this.rightPaddle);
        return builder;
    }

    public static void createChildren(PartDefinition partDefinition) {
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(0, 0).mirror()
                        .addBox(-14.0F, -7.0F, -17.0F, 28.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        partDefinition.addOrReplaceChild("front", CubeListBuilder.create()
                        .texOffs(0, 29).mirror()
                        .addBox(-8.0F, 14.0F, -3.0F, 14.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(0, 19).mirror()
                        .addBox(-6.0F, 14.0F, -3.0F, 14.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("right", CubeListBuilder.create()
                        .texOffs(0, 39).mirror()
                        .addBox(-14.0F, 14.0F, -3.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.0F, 0.0F, -3.1416F, 0.0F));

        partDefinition.addOrReplaceChild("left", CubeListBuilder.create()
                        .texOffs(0, 49).mirror()
                        .addBox(-14.0F, 14.0F, -1.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 9.0F));

        partDefinition.addOrReplaceChild("left_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, 9.0F, 0.0F, 0.0F, 0.19634955F));
        partDefinition.addOrReplaceChild("right_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, -9.0F, 0.0F, 3.1415927F, 0.19634955F));

        partDefinition.addOrReplaceChild("water_patch", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 1.5707964F, 0.0F, 0.0F));
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        createChildren(partDefinition);
        return LayerDefinition.create(meshDefinition, 128, 64);
    }

    public void setupAnim(Boat boat, float f, float g, float h, float i, float j) {
        animatePaddle(boat, 0, this.leftPaddle, f);
        animatePaddle(boat, 1, this.rightPaddle, f);
    }

    public @NotNull ImmutableList<ModelPart> parts() {
        return this.parts;
    }

    public @NotNull ModelPart waterPatch() {
        return this.waterPatch;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        poseStack.translate(0.0F, -1.1F, 0.0F);
        for (ModelPart part : this.parts) {
            if (part != this.leftPaddle && part != this.rightPaddle) {
                part.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
        poseStack.popPose();

        this.leftPaddle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightPaddle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private static void animatePaddle(Boat boat, int i, ModelPart modelPart, float f) {
        float g = boat.getRowingTime(i, f);
        modelPart.xRot = Mth.clampedLerp(-1.0471976F, -0.2617994F, (Mth.sin(g) + 1.0F) / 2.0F);
        modelPart.yRot = Mth.clampedLerp(-0.7853982F, 0.7853982F, (Mth.sin(g + 1.0F) + 1.0F) / 2.0F);
        if (i == 1) {
            modelPart.yRot = 3.1415927F - modelPart.yRot;
        }
    }
}
