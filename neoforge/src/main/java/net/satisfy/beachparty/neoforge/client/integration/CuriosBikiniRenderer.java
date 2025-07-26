package net.satisfy.beachparty.neoforge.client.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.beachparty.client.model.BikiniModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.Objects;

public class CuriosBikiniRenderer implements ICurioRenderer {
    private final BikiniModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosBikiniRenderer() {
        this.model = new BikiniModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BikiniModel.LAYER_LOCATION));
        this.texture = BeachpartyIdentifier.identifier("textures/models/armor/bikini.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (stack.isEmpty() || !(stack.getItem() instanceof DyeableBeachpartyArmorItem)) return;
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            if (stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("Visible") && !stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("Visible")) return;
        }
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;
        int colorInt = ((DyeableBeachpartyArmorItem) stack.getItem()).getColor();
        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (livingEntity.isCrouching()) {
            matrixStack.translate(0F, 0.2F, 0F);
            matrixStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(20)));
        }
        matrixStack.pushPose();
        renderColoredCutoutModel(model, texture, matrixStack, renderTypeBuffer, light, livingEntity, colorInt);
        matrixStack.popPose();
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void renderColoredCutoutModel(BikiniModel<LivingEntity> model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int light, T entity, int color) {
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, color);
    }
}
