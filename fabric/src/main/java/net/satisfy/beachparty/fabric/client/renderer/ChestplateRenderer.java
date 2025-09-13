package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.item.TrinketsArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;

public class ChestplateRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack itemStack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        Model model;
        ResourceLocation texture;

        if (itemStack.getItem() instanceof DyeableBeachpartyArmorItem dyeableArmorItem) {
            model = ArmorRegistry.chestplateModel(dyeableArmorItem, contextModel.body, contextModel.leftArm, contextModel.rightArm);
            texture = dyeableArmorItem.getTexture();
            int color = dyeableArmorItem.getColor(itemStack);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(texture));
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);
        } else if (itemStack.getItem() instanceof TrinketsArmorItem beachpartyArmorItem) {
            model = ArmorRegistry.chestplateModel(beachpartyArmorItem, contextModel.body, contextModel.leftArm, contextModel.rightArm);
            texture = beachpartyArmorItem.getTexture();
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(texture));
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
        }
    }
}
