package net.satisfy.beachparty.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class SwimWingsModel<T extends Entity> extends EntityModel<T> implements ChestplateModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("swim_wings"), "main");
    private final ModelPart right_arm;
    private final ModelPart left_arm;

    public SwimWingsModel(ModelPart root) {
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 3).addBox(-8.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 3).mirror().addBox(-8.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.375F, 0.0F, 0.0F);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        right_arm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.45F, -0.05F, 0.0F);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        left_arm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public void copyBody(ModelPart body, ModelPart leftArm, ModelPart rightArm) {
        this.left_arm.copyFrom(leftArm);
        this.right_arm.copyFrom(rightArm);
    }
}