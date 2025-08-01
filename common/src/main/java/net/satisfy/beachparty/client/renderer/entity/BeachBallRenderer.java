package net.satisfy.beachparty.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.client.model.BeachBallModel;
import net.satisfy.beachparty.core.entity.BeachBallEntity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class BeachBallRenderer extends MobRenderer<BeachBallEntity, BeachBallModel<BeachBallEntity>> {
    public BeachBallRenderer(EntityRendererProvider.Context context) {
        super(context, new BeachBallModel<>(context.bakeLayer(BeachBallModel.LAYER_LOCATION)), 0.2f);
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