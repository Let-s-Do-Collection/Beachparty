package net.satisfy.beachparty.fabric.client.compat;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.foundation.client.armor.ArmorModels;

public final class TrinketsClientCompat {
    private TrinketsClientCompat() {
    }

    public static void init() {
        TrinketRenderer renderer = (stack, slotReference, contextModel, poseStack, buffers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {
            if (contextModel instanceof HumanoidModel<?> humanoid && stack.getItem() instanceof ArmorItem armor) {
                ArmorModels.render(poseStack, buffers, stack, armor.getEquipmentSlot(), light, humanoid);
            }
        };
        for (Item wearable : BeachPartyClient.wearables()) {
            TrinketRendererRegistry.registerRenderer(wearable, renderer);
        }
    }
}
