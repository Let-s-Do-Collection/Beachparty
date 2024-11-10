package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;

public class DyeableChestplateTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if (!(itemStack.getItem() instanceof DyeableBeachpartyArmorItem armorItem)) return;
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("Visible") && !tag.getBoolean("Visible")) return;

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            Model model = ArmorRegistry.chestplateModel(armorItem, humanoidModel.body, humanoidModel.leftArm, humanoidModel.rightArm);

            int color = armorItem.getColor(itemStack);
            float red = (color >> 16 & 255) / 255.0F;
            float green = (color >> 8 & 255) / 255.0F;
            float blue = (color & 255) / 255.0F;

            model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(armorItem.getTexture())), i, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        }
    }
}
