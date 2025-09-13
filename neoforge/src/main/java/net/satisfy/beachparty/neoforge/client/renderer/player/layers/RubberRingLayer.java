package net.satisfy.beachparty.neoforge.client.renderer.player.layers;

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
import net.satisfy.beachparty.client.model.RubberRingColoredModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class RubberRingLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final RubberRingColoredModel<T> model;

    public RubberRingLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new RubberRingColoredModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof Player) {
            ItemStack ringStack = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (ringStack.isEmpty() || !isRubberRing(ringStack)) return;

            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            renderColoredCutoutModel(this.model, getRingTexture(ringStack), poseStack, multiBufferSource, light);
            poseStack.popPose();
        }
    }

    private boolean isRubberRing(ItemStack stack) {
        return stack.is(ObjectRegistry.RUBBER_RING_BLUE.get()) ||
                stack.is(ObjectRegistry.RUBBER_RING_PINK.get()) ||
                stack.is(ObjectRegistry.RUBBER_RING_STRIPPED.get());
    }

    private ResourceLocation getRingTexture(ItemStack stack) {
        if (stack.is(ObjectRegistry.RUBBER_RING_PINK.get())) {
            return BeachpartyIdentifier.identifier("textures/models/armor/rubber_ring_pink.png");
        } else if (stack.is(ObjectRegistry.RUBBER_RING_STRIPPED.get())) {
            return BeachpartyIdentifier.identifier("textures/models/armor/rubber_ring_stripped.png");
        } else {
            return BeachpartyIdentifier.identifier("textures/models/armor/rubber_ring_blue.png");
        }
    }

    private static <T extends LivingEntity> void renderColoredCutoutModel(RubberRingColoredModel<T> model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int light) {model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY);
    }
}
