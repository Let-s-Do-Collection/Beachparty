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

public class RubberRingPelicanModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.identifier("rubber_ring_pelican"), "main");

    public RubberRingPelicanModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(31, 18).addBox(-10.0F, -4.0F, 2.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(7, 0).addBox(-13.0F, -4.0F, -1.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -8.0F, -5.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 18).addBox(-8.0F, -6.0F, -13.0F, 4.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
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
