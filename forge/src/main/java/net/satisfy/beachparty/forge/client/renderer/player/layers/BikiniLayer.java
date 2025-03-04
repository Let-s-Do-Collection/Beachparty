package net.satisfy.beachparty.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.BikiniModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class BikiniLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final BikiniModel<T> model;

    public BikiniLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new BikiniModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BikiniModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof Player) {
            ItemStack legsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (legsStack.isEmpty() || !(legsStack.getItem() instanceof DyeableBeachpartyArmorItem)) return;
            if (legsStack.hasTag()) {
                assert legsStack.getTag() != null;
                if (legsStack.getTag().contains("Visible") && !legsStack.getTag().getBoolean("Visible")) return;
            }
            int colorInt = ((DyeableBeachpartyArmorItem) legsStack.getItem()).getColor(legsStack);
            float red = ((colorInt >> 16) & 0xFF) / 255f;
            float green = ((colorInt >> 8) & 0xFF) / 255f;
            float blue = (colorInt & 0xFF) / 255f;
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, light, entity, red, green, blue);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return new BeachpartyIdentifier("textures/models/armor/bikini.png");
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void renderColoredCutoutModel(BikiniModel<T> model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float red, float green, float blue) {
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
    }
}
