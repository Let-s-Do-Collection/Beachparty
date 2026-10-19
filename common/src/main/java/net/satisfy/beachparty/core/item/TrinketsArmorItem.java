package net.satisfy.beachparty.core.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.beachparty.core.event.WearableEvents;
import net.satisfy.foundation.armor.TexturedArmorItem;

import java.util.List;

public class TrinketsArmorItem extends TexturedArmorItem {
    public TrinketsArmorItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties, ResourceLocation texture) {
        super(armorMaterial, type, properties, texture);
    }

    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return getTexture();
    }

    public void toggleVisibility(ItemStack itemStack) {
        CompoundTag data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean isVisible = !data.contains("Visible") || data.getBoolean("Visible");
        data.putBoolean("Visible", !isVisible);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack holdingStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && holdingStack.isEmpty()) {
            toggleVisibility(slotStack);
            return true;
        }

        return super.overrideOtherStackedOnMe(slotStack, holdingStack, slot, clickAction, player, slotAccess);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.beachparty.when_worn").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFAF3E0))));
        tooltip.add(Component.translatable("tooltip.beachparty.effect." + this.getDescriptionId(), WearableEvents.tooltipArgs(this)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD4B483))));
        tooltip.add(Component.empty());
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean isVisible = !data.contains("Visible") || data.getBoolean("Visible");
        tooltip.add(Component.translatable(isVisible ? "tooltip.beachparty.toggle.hide" : "tooltip.beachparty.toggle.show").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5CB85C))));
    }
}
