package net.satisfy.beachparty.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.client.model.BeachBallModel;
import net.satisfy.beachparty.core.entity.BeachBallEntity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class BeachBallRenderer extends EntityRenderer<BeachBallEntity> {
    private final BeachBallModel<BeachBallEntity> model;

    public BeachBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BeachBallModel<>(context.bakeLayer(BeachBallModel.LAYER_LOCATION));
        this.shadowRadius = 0.2f;
    }

    @Override
    public void render(BeachBallEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float radius = BeachBallEntity.getRadius();
        poseStack.pushPose();
        poseStack.translate(0, radius, 0);
        poseStack.mulPose(entity.getOrientation(partialTick));
        poseStack.translate(0, -radius, 0);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BeachBallEntity entity) {
        String name = entity.getCustomName() != null ? entity.getCustomName().getString() : "";

        return switch (name) {
            case "MissLilitu" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_misslilitu.png");
            case "CR-055" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_cr055.png");
            case "Jason" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_jason.png");
            case "Satisfy" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_satisfy.png");
            case "Nekonesse" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_nekonesse.png");
            case "MarbledNull" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_marblednull.png");
            case "Steve" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_steve.png");
            case "Pixar" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_pixar.png");
            case "Dirt" -> BeachpartyIdentifier.identifier("textures/entity/beach_ball_dirt.png");
            default -> BeachpartyIdentifier.identifier("textures/entity/beach_ball.png");
        };
    }
}
