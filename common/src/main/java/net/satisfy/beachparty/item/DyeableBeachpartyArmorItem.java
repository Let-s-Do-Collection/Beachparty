package net.satisfy.beachparty.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DyeableBeachpartyArmorItem extends DyeableArmorItem implements IBeachpartyArmorItem {

    private final ResourceLocation getTexture;
    private final int defaultColor;

    public DyeableBeachpartyArmorItem(ArmorMaterial armorMaterial, Type type, int color, Properties properties, ResourceLocation getTexture) {
        super(armorMaterial, type, properties);
        this.defaultColor = color;
        this.getTexture = getTexture;
    }

    @Override
    public int getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement("display");
        return (compoundTag != null && compoundTag.contains("color", 99)) ? compoundTag.getInt("color") : this.defaultColor;
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
