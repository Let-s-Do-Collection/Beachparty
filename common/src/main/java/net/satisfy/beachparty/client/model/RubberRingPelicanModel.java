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
public class RubberRingPelicanModel<T extends Entity> extends EntityModel<T> implements ChestplateModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("rubber_ring_pelican"), "main");
    private final ModelPart rubber_ring;

    public RubberRingPelicanModel(ModelPart root) {
        this.rubber_ring = root.getChild("rubber_ring");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rubber_ring = partdefinition.addOrReplaceChild("rubber_ring", CubeListBuilder.create().texOffs(31, 18).addBox(-10.0F, -4.0F, 2.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(7, 0).addBox(-13.0F, -4.0F, -1.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -8.0F, -5.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 18).addBox(-8.0F, -6.0F, -13.0F, 4.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 24.0F, -6.0F));

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