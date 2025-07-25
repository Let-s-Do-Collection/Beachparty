package net.satisfy.beachparty.neoforge.client.integration;

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
import net.satisfy.beachparty.client.model.BeachHatModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosBeachHatRenderer implements ICurioRenderer {

    private final BeachHatModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosBeachHatRenderer() {
        this.model = new BeachHatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BeachHatModel.LAYER_LOCATION));
        this.texture = BeachpartyIdentifier.identifier("textures/models/armor/beach_hat.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!slotContext.identifier().equals("head")) return;
        if (stack.isEmpty() || !isBeachHat(stack)) return;

        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;

        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        matrixStack.pushPose();
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }


    private static boolean isBeachHat(ItemStack stack) {
        return stack.is(ObjectRegistry.BEACH_HAT.get());
    }
}
