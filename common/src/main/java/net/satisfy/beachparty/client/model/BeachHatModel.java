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

public class BeachHatModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.identifier("beach_hat"), "main");

    public BeachHatModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(-17, 13).addBox(-8.5F, -6.0F, -8.5F, 17.0F, 0.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        head.addOrReplaceChild("kop", CubeListBuilder.create().texOffs(0, 28).addBox(8.5F, -6F, -3.5F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int packedColor) {
        poseStack.pushPose();
        poseStack.scale(1.05F, 1.05F, 1.05F);
        head.render(poseStack, buffer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
    }
}
