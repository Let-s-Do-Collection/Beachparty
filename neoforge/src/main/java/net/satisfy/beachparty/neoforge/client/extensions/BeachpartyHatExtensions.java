package net.satisfy.beachparty.neoforge.client.extensions;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.satisfy.beachparty.core.registry.ArmorRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class BeachpartyHatExtensions implements IClientItemExtensions {
    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
        if (slot != EquipmentSlot.HEAD) return original;
        Item item = stack.getItem();

        if (item == ObjectRegistry.BEACH_HAT.get() || item == ObjectRegistry.SUNGLASSES.get()) {
            return ArmorRegistry.HelmetModel(item, original.head);
        }

        return original;
    }
}
