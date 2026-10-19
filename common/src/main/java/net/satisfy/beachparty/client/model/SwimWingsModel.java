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

public class SwimWingsModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.identifier("swim_wings"), "main");

    public SwimWingsModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 3).addBox(-8.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 3).mirror().addBox(-8.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int packedColor) {
        poseStack.pushPose();
        poseStack.translate(0.375F, 0.0F, 0.0F);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        rightArm.render(poseStack, buffer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.45F, -0.05F, 0.0F);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        leftArm.render(poseStack, buffer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
    }
}
