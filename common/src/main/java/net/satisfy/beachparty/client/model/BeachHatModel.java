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

@SuppressWarnings("unused")
public class BeachHatModel<T extends Entity> extends EntityModel<T> implements HatModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("beach_hat"), "main");
    private final ModelPart beach_hat;

    public BeachHatModel(ModelPart root) {
        this.beach_hat = root.getChild("beach_hat");
        ModelPart kop = beach_hat.getChild("kop");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition beach_hat = partdefinition.addOrReplaceChild("beach_hat", CubeListBuilder.create()
                .texOffs(-17, 13).addBox(-8.5F, -6.0F, -8.5F, 17.0F, 0.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition kop = beach_hat.addOrReplaceChild("kop", CubeListBuilder.create().texOffs(0, 28).addBox(8.5F, -6F, -3.5F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.05F, 1.05F, 1.05F);
        beach_hat.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    public void copyHead(ModelPart model) {
        beach_hat.copyFrom(model);
    }
}