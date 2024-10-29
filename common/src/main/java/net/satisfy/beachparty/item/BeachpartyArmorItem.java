package net.satisfy.beachparty.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BeachpartyArmorItem extends ArmorItem implements IBeachpartyArmorItem {
    private final ResourceLocation getTexture;

    public BeachpartyArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties, ResourceLocation getTexture) {
        super(armorMaterial, type, properties);
        this.getTexture = getTexture;
    }

    public ResourceLocation getTexture() {
        return getTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public void toggleVisibility(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        boolean isVisible = tag.getBoolean("Visible");
        tag.putBoolean("Visible", !isVisible);
        itemStack.setTag(tag);
    }

    @Override
    public boolean isVisible(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.getBoolean("Visible");
    }
}
