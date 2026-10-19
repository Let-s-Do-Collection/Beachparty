package net.satisfy.beachparty.neoforge.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.foundation.client.armor.ArmorModels;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class CuriosClientCompat {
    private CuriosClientCompat() {
    }

    public static void init() {
        for (Item wearable : BeachPartyClient.wearables()) {
            CuriosRendererRegistry.register(wearable, WearableRenderer::new);
        }
    }

    private static class WearableRenderer implements ICurioRenderer {
        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoid && stack.getItem() instanceof ArmorItem armor) {
                ArmorModels.render(poseStack, buffers, stack, armor.getEquipmentSlot(), light, humanoid);
            }
        }
    }
}
