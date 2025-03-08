package net.satisfy.beachparty.forge.client.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.RubberRingColoredModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosRubberRingRenderer implements ICurioRenderer {

    private final RubberRingColoredModel<LivingEntity> model;

    public CuriosRubberRingRenderer() {
        this.model = new RubberRingColoredModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!slotContext.identifier().equals("belt")) return;
        if (stack.isEmpty() || !isRubberRing(stack)) return;

        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;

        ResourceLocation texture = getRingTexture(stack);
        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        matrixStack.pushPose();
        if (livingEntity.isCrouching()) {
            matrixStack.translate(0.1F, -0.7F, -1F);
            matrixStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(20)));
        } else {
            matrixStack.translate(0.1F, -1.4F, -0.5F);
        }
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }

    private boolean isRubberRing(ItemStack stack) {
        return stack.is(ObjectRegistry.RUBBER_RING_BLUE.get()) ||
                stack.is(ObjectRegistry.RUBBER_RING_PINK.get()) ||
                stack.is(ObjectRegistry.RUBBER_RING_STRIPPED.get());
    }

    private ResourceLocation getRingTexture(ItemStack stack) {
        if (stack.is(ObjectRegistry.RUBBER_RING_PINK.get())) {
            return new BeachpartyIdentifier("textures/models/armor/rubber_ring_pink.png");
        } else if (stack.is(ObjectRegistry.RUBBER_RING_STRIPPED.get())) {
            return new BeachpartyIdentifier("textures/models/armor/rubber_ring_stripped.png");
        } else {
            return new BeachpartyIdentifier("textures/models/armor/rubber_ring_blue.png");
        }
    }
}
