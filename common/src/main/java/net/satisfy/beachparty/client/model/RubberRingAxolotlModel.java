package net.satisfy.beachparty.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class RubberRingAxolotlModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.identifier("rubber_ring_axolotl"), "main");

    public RubberRingAxolotlModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 18).addBox(-10.0F, -4.0F, 2.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-13.0F, -4.0F, -1.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-10.0F, -5.0F, -5.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).mirror().addBox(-6.0F, -3.0F, 10.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(6, 8).addBox(-13.0F, -8.0F, -2.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(6, 8).mirror().addBox(-4.0F, -8.0F, -2.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.ZERO);
        body.addOrReplaceChild("right_foot_r1", CubeListBuilder.create().texOffs(5, 0).addBox(-2.0F, -5.0F, -1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.0F, 0.0F, 0.0F, -0.4363F));
        body.addOrReplaceChild("left_foot_r1", CubeListBuilder.create().texOffs(5, 0).addBox(-2.0F, -5.0F, -1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 3.0F, -2.0F, 0.0F, 0.0F, 0.5236F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int packedColor) {
        poseStack.pushPose();
        if (crouching) {
            poseStack.translate(0.4F, 0.9F, 0F);
        } else {
            poseStack.translate(0.4F, 0.7F, -0.4F);
        }
        body.render(poseStack, buffer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
    }
}
