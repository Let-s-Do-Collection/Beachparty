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
public class RubberRingColoredModel<T extends Entity> extends EntityModel<T> implements ChestplateModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("rubber_ring"), "main");
    private final ModelPart rubber_ring;

    public RubberRingColoredModel(ModelPart root) {
        this.rubber_ring = root.getChild("rubber_ring");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition rubber_ring = modelPartData.addOrReplaceChild("rubber_ring", CubeListBuilder.create().texOffs(3, 0).addBox(-5.5F, -2.5F, -15.5F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(39, 26).addBox(5.5F, 1.5F, -4.5F, -8.0F, -4.0F, -8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.5F, 8.5F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void copyBody(ModelPart model, ModelPart leftArm, ModelPart rightArm) {
        rubber_ring.copyFrom(model);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        Player player = Minecraft.getInstance().player;
        if (player != null && player.getPose() == Pose.CROUCHING) {
            poseStack.translate(-0.1F, 0.4F, 0.7F);
        } else {
            poseStack.translate(-0.1F, 0.6F, 0.5F);
        }

        rubber_ring.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}