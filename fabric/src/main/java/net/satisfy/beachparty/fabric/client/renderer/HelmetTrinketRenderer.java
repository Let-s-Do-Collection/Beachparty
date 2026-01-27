package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.beachparty.core.item.TrinketsArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;

public class HelmetTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if (!(itemStack.getItem() instanceof TrinketsArmorItem armorItem)) return;

        CustomData tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
        CompoundTag data = tag.copyTag();
        if (data.contains("Visible") && !data.getBoolean("Visible")) return;

        if (!(entityModel instanceof HumanoidModel<?> humanoidModel)) return;

        Model model = ArmorRegistry.HelmetModel(armorItem, humanoidModel.hat);
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(armorItem.getTexture())), i, OverlayTexture.NO_OVERLAY);
    }
}