package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.core.item.TrinketsArmorItem;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.Objects;

public class LeggingsRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        Model model;
        ResourceLocation texture;
        if (stack.getItem() instanceof DyeableBeachpartyArmorItem dyeableArmorItem) {
            model = ArmorRegistry.LeggingsModel(dyeableArmorItem, contextModel.body, contextModel.rightLeg, contextModel.leftLeg);
            texture = dyeableArmorItem.getTexture();

            int color = Objects.requireNonNull(dyeableArmorItem.getDefaultInstance().get(DataComponents.DYED_COLOR)).rgb();

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(texture));
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);

            ResourceLocation overlayTexture = BeachpartyIdentifier.identifier("textures/models/armor/trunks_overlay.png");
            VertexConsumer overlayConsumer = vertexConsumers.getBuffer(model.renderType(overlayTexture));
            model.renderToBuffer(matrices, overlayConsumer, light, OverlayTexture.NO_OVERLAY, color);

        } else if (stack.getItem() instanceof TrinketsArmorItem beachpartyArmorItem) {
            model = ArmorRegistry.LeggingsModel(beachpartyArmorItem, contextModel.body, contextModel.rightLeg, contextModel.leftLeg);
            texture = beachpartyArmorItem.getTexture();

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(texture));
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, Objects.requireNonNull(beachpartyArmorItem.getDefaultInstance().get(DataComponents.DYED_COLOR)).rgb());
        }
    }
}

