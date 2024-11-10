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

public class TrunksModel<T extends Entity> extends EntityModel<T> implements LeggingsModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("trunks"), "main");
    private final ModelPart body;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public TrunksModel(ModelPart root) {
        this.body = root.getChild("body");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, 9.6F, -2.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.225F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.09F, 1.09F, 1.09F);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        right_leg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        left_leg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public void copyBody(ModelPart model, ModelPart left_leg, ModelPart right_leg) {
        this.body.copyFrom(model);
        this.right_leg.copyFrom(right_leg);
        this.left_leg.copyFrom(left_leg);
    }
}