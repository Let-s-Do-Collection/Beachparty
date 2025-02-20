package net.satisfy.beachparty.forge.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.satisfy.beachparty.core.item.TrinketsArmorItem;
import net.satisfy.beachparty.core.registry.ArmorRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(TrinketsArmorItem.class)
public abstract class HelmetItemMixin extends ArmorItem {
    @Shadow
    @Final
    private ResourceLocation getTexture;

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                Item item = itemStack.getItem();
                if (item == ObjectRegistry.BEACH_HAT.get()) {
                    return ArmorRegistry.HelmetModel(item, original.getHead());
                } else if (item == ObjectRegistry.SUNGLASSES.get()) {
                    return ArmorRegistry.HelmetModel(item, original.getHead());
                } else if (item == ObjectRegistry.RUBBER_RING_PINK.get()) {
                    return ArmorRegistry.chestplateModel(item, original.body, original.leftArm, original.rightArm);
                }
                else if (item == ObjectRegistry.RUBBER_RING_STRIPPED.get()) {
                    return ArmorRegistry.chestplateModel(item, original.body, original.leftArm, original.rightArm);
                }
                else if (item == ObjectRegistry.RUBBER_RING_BLUE.get()) {
                    return ArmorRegistry.chestplateModel(item, original.body, original.leftArm, original.rightArm);
                }
                else if (item == ObjectRegistry.RUBBER_RING_PELICAN.get()) {
                    return ArmorRegistry.chestplateModel(item, original.body, original.leftArm, original.rightArm);
                }
                else if (item == ObjectRegistry.RUBBER_RING_AXOLOTL.get()) {
                    return ArmorRegistry.chestplateModel(item, original.body, original.leftArm, original.rightArm);
                }
                return ArmorRegistry.HelmetModel(item, original.getHead());
            }
        });
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getTexture.toString();
    }

    private HelmetItemMixin(ArmorMaterial armorMaterial, Type armorType, Properties itemProperties) {
        super(armorMaterial, armorType, itemProperties);
    }
}

