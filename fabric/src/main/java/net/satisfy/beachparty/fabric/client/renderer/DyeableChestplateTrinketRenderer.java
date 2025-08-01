package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;

public class DyeableChestplateTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if (!(itemStack.getItem() instanceof DyeableBeachpartyArmorItem armorItem)) return;
        CustomData tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        if (tag.copyTag() != null && tag.contains("Visible") && !tag.copyTag().getBoolean("Visible")) return;

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            Model model = ArmorRegistry.chestplateModel(armorItem, humanoidModel.body, humanoidModel.leftArm, humanoidModel.rightArm);

            int color = armorItem.getColor();

            model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(armorItem.getTexture())), i, OverlayTexture.NO_OVERLAY, color);
        }
    }
}
