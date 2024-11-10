package net.satisfy.beachparty.core.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.beachparty.trinketsslot").withStyle(style -> style.withColor(TextColor.fromRgb(0xFAF3E0))));
        tooltip.add(Component.translatable("tooltip.beachparty.effect." + this.getDescriptionId()).withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.beachparty.canbetoggled").withStyle(style -> style.withColor(TextColor.fromRgb(0x5CB85C))));
    }
}
