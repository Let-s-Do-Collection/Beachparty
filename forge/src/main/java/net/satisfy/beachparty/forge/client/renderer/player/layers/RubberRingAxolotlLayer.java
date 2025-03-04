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
import net.satisfy.beachparty.client.model.RubberRingAxolotlModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class RubberRingAxolotlLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final RubberRingAxolotlModel<T> model;

    public RubberRingAxolotlLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new RubberRingAxolotlModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RubberRingAxolotlModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, @NotNull T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (entity instanceof Player) {
            ItemStack ringStack = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (ringStack.isEmpty() || !isRubberRing(ringStack)) return;

            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            renderColoredCutoutModel(this.model, getItemTexture(), poseStack, multiBufferSource, light, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    private static ResourceLocation getItemTexture() {
        return new BeachpartyIdentifier("textures/models/armor/rubber_ring_axolotl.png");
    }

    private static boolean isRubberRing(ItemStack stack) {
        return stack.is(ObjectRegistry.RUBBER_RING_AXOLOTL.get());
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void renderColoredCutoutModel(RubberRingAxolotlModel<T> model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float red, float green, float blue) {model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
    }
}
