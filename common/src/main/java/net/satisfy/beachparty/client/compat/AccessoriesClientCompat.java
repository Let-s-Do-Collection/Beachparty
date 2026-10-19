package net.satisfy.beachparty.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.foundation.client.armor.ArmorModels;

public final class AccessoriesClientCompat {
    private AccessoriesClientCompat() {
    }

    public static void init() {
        for (Item wearable : BeachPartyClient.wearables()) {
            AccessoriesRendererRegistry.registerRenderer(wearable, WearableRenderer::new);
        }
    }

    private static class WearableRenderer implements AccessoryRenderer {
        @Override
        public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, PoseStack poseStack, EntityModel<M> model, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (model instanceof HumanoidModel<?> humanoid && stack.getItem() instanceof ArmorItem armor) {
                ArmorModels.render(poseStack, buffers, stack, armor.getEquipmentSlot(), light, humanoid);
            }
        }
    }
}
