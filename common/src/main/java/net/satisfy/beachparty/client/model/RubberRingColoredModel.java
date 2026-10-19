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

public class RubberRingColoredModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.identifier("rubber_ring"), "main");

    public RubberRingColoredModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 0).addBox(-5.5F, -2.5F, -15.5F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(39, 26).addBox(5.5F, 1.5F, -4.5F, -8.0F, -4.0F, -8.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
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
            poseStack.translate(-0.1F, 0.4F, 0.7F);
        } else {
            poseStack.translate(-0.1F, 0.6F, 0.5F);
        }
        body.render(poseStack, buffer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
    }
}
