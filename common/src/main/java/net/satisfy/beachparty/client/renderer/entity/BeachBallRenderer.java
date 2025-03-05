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
            case "MissLilitu" -> new BeachpartyIdentifier("textures/entity/beach_ball_misslilitu.png");
            case "Raymond" -> new BeachpartyIdentifier("textures/entity/beach_ball_raymond.png");
            case "CR-055" -> new BeachpartyIdentifier("textures/entity/beach_ball_cr055.png");
            case "Satisfy" -> new BeachpartyIdentifier("textures/entity/beach_ball_satisfy.png");
            case "Nekonesse" -> new BeachpartyIdentifier("textures/entity/beach_ball_nekonesse.png");
            case "Jason13" -> new BeachpartyIdentifier("textures/entity/beach_ball_jason13.png");
            case "MarbledNull" -> new BeachpartyIdentifier("textures/entity/beach_ball_marblednull.png");
            case "TomHanks" -> new BeachpartyIdentifier("textures/entity/beach_ball_tomhanks.png");
            case "Steve" -> new BeachpartyIdentifier("textures/entity/beach_ball_steve.png");
            case "Pixar" -> new BeachpartyIdentifier("textures/entity/beach_ball_pixar.png");
            case "Dirt" -> new BeachpartyIdentifier("textures/entity/beach_ball_dirt.png");
            default -> new BeachpartyIdentifier("textures/entity/beach_ball.png");
        };
    }

}