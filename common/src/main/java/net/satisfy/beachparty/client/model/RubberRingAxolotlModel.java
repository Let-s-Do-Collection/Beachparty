package net.satisfy.beachparty.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

@SuppressWarnings("unused")
public class RubberRingAxolotlModel<T extends Entity> extends EntityModel<T> implements ChestplateModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("rubber_ring_axolotl"), "main");

    private final ModelPart rubber_ring;

    public RubberRingAxolotlModel(ModelPart root) {
        this.rubber_ring = root.getChild("rubber_ring");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rubber_ring = partdefinition.addOrReplaceChild("rubber_ring", CubeListBuilder.create().texOffs(32, 18).addBox(-10.0F, -4.0F, 2.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-13.0F, -4.0F, -1.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-10.0F, -5.0F, -5.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).mirror().addBox(-6.0F, -3.0F, 10.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(6, 8).addBox(-13.0F, -8.0F, -2.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(6, 8).mirror().addBox(-4.0F, -8.0F, -2.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 24.0F, -6.0F));

        PartDefinition right_foot_r1 = rubber_ring.addOrReplaceChild("right_foot_r1", CubeListBuilder.create().texOffs(5, 0).addBox(-2.0F, -5.0F, -1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition left_foot_r1 = rubber_ring.addOrReplaceChild("left_foot_r1", CubeListBuilder.create().texOffs(5, 0).addBox(-2.0F, -5.0F, -1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 3.0F, -2.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        Player player = Minecraft.getInstance().player;
        if (player != null && player.getPose() == Pose.CROUCHING) {
            poseStack.translate(0.4F, 0.9F, 0F);
        } else {
            poseStack.translate(0.4F, 0.7F, -0.4F);
        }

        rubber_ring.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void copyBody(ModelPart model, ModelPart leftArm, ModelPart rightArm) {
        rubber_ring.copyFrom(model);
    }
}