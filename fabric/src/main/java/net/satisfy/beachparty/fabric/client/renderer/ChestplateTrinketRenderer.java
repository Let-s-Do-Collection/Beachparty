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
import net.satisfy.beachparty.item.BeachpartyArmorItem;
import net.satisfy.beachparty.registry.ArmorRegistry;

public class ChestplateTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if (!(itemStack.getItem() instanceof BeachpartyArmorItem armorItem)) return;
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("Visible") && !tag.getBoolean("Visible")) return;

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            Model model = ArmorRegistry.chestplateModel(armorItem, humanoidModel.body, humanoidModel.leftArm, humanoidModel.rightArm);

            model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(armorItem.getTexture())), i, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }
}
