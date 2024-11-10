package net.satisfy.beachparty.client.renderer;

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

    private static final ResourceLocation TEXTURE = new BeachpartyIdentifier("textures/entity/beach_ball.png");

    @Override
    public @NotNull ResourceLocation getTextureLocation(BeachBallEntity entity) {
        return TEXTURE;
    }
}