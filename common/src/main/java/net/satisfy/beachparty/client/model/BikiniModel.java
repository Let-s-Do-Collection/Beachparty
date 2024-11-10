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

public class BikiniModel<T extends Entity> extends EntityModel<T> implements ChestplateModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("bikini"), "main");

    private final ModelPart bikini;

    public BikiniModel(ModelPart root) {
        this.bikini = root.getChild("bikini");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bikini = partdefinition.addOrReplaceChild("bikini", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.075F, 1.075F, 1.075F);
        bikini.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public void copyBody(ModelPart model, ModelPart leftArm, ModelPart rightArm) {
        bikini.copyFrom(model);
    }
}