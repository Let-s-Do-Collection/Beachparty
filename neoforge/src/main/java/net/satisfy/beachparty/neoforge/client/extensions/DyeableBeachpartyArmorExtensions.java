package net.satisfy.beachparty.neoforge.client.extensions;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;
import org.jetbrains.annotations.NotNull;

public class DyeableBeachpartyArmorExtensions implements IClientItemExtensions {
    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
        if (!(stack.getItem() instanceof DyeableBeachpartyArmorItem item)) return original;
        if (slot != item.getEquipmentSlot()) return original;
        if (slot == EquipmentSlot.CHEST) return ArmorRegistry.chestplateModel(stack.getItem(), original.body, original.leftArm, original.rightArm);
        if (slot == EquipmentSlot.LEGS) return ArmorRegistry.LeggingsModel(stack.getItem(), original.body, original.leftLeg, original.rightLeg);
        if (slot == EquipmentSlot.FEET) return original;
        return original;
    }

    @Override
    public int getArmorLayerTintColor(@NotNull ItemStack stack, @NotNull LivingEntity entity, @NotNull ArmorMaterial.Layer layer, int layerIdx, int fallbackColor) {
        if (!(stack.getItem() instanceof DyeableBeachpartyArmorItem item)) return fallbackColor;
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains("Visible") && !tag.getBoolean("Visible")) return 0x00FFFFFF;
        if (layer.dyeable()) return 0xFF000000 | item.getColor(stack);
        return 0xFFFFFFFF;
    }
}
