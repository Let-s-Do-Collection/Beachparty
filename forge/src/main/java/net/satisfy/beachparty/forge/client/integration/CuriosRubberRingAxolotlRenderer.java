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
import net.satisfy.beachparty.client.model.RubberRingAxolotlModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosRubberRingAxolotlRenderer implements ICurioRenderer {

    private final RubberRingAxolotlModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosRubberRingAxolotlRenderer() {
        this.model = new RubberRingAxolotlModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RubberRingAxolotlModel.LAYER_LOCATION));
        this.texture = new BeachpartyIdentifier("textures/models/armor/rubber_ring_axolotl.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!slotContext.identifier().equals("belt")) return;
        if (stack.isEmpty() || !isRubberRing(stack)) return;

        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;

        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        matrixStack.pushPose();
        if (livingEntity.isCrouching()) {
            matrixStack.translate(-0.4F, -1.4F, -0.5F);
            matrixStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(20)));
        } else {
            matrixStack.translate(-0.4F, -1.5F, 0.4F);
        }
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }

    private static boolean isRubberRing(ItemStack stack) {
        return stack.is(ObjectRegistry.RUBBER_RING_AXOLOTL.get());
    }
}
