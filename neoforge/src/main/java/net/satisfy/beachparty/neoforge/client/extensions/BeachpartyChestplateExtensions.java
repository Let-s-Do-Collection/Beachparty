package net.satisfy.beachparty.neoforge.client.extensions;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.satisfy.beachparty.core.registry.ArmorRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class BeachpartyChestplateExtensions implements IClientItemExtensions {
    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
        if (slot == EquipmentSlot.CHEST &&
                (stack.getItem() == ObjectRegistry.RUBBER_RING_BLUE.get()
                        || stack.getItem() == ObjectRegistry.RUBBER_RING_PINK.get()
                        || stack.getItem() == ObjectRegistry.RUBBER_RING_STRIPPED.get()
                        || stack.getItem() == ObjectRegistry.RUBBER_RING_AXOLOTL.get()
                        || stack.getItem() == ObjectRegistry.RUBBER_RING_PELICAN.get())) {
            return ArmorRegistry.chestplateModel(stack.getItem(), original.body, original.leftArm, original.rightArm);
        }
        return original;
    }
}
