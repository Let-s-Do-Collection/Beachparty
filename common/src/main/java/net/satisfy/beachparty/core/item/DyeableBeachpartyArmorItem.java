package net.satisfy.beachparty.core.item;

import dev.architectury.platform.Platform;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DyeableBeachpartyArmorItem extends ArmorItem {
    private final ResourceLocation getTexture;
    private final int defaultColor;

    public DyeableBeachpartyArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type type, int color, Item.Properties properties, ResourceLocation getTexture) {
        super(BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial), type, properties);
        this.defaultColor = color;
        this.getTexture = getTexture;
    }

    public ResourceLocation getTexture() {
        return getTexture;
    }

    public int getColor() {
        return defaultColor;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public void toggleVisibility(ItemStack itemStack) {
        CustomData tag = CustomData.of(new CompoundTag());
        boolean isVisible = !tag.contains("Visible") || tag.copyTag().getBoolean("Visible");
        tag.copyTag().putBoolean("Visible", !isVisible);
        itemStack.set(DataComponents.CUSTOM_DATA, tag);
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
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (Platform.isFabric()) {
            tooltip.add(Component.translatable("tooltip.beachparty.trinketsslot")
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFAF3E0))));
        } else if (Platform.isNeoForge()) {
            tooltip.add(Component.translatable("tooltip.beachparty.curiosslot")
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFAF3E0))));
        }

        tooltip.add(Component.translatable("tooltip.beachparty.effect." + this.getDescriptionId())
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD4B483))));

        tooltip.add(Component.empty());

        if (Platform.isFabric()) {
            CustomData tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
            boolean isVisible = !tag.contains("Visible") || tag.copyTag().getBoolean("Visible");

            Component toggleText = isVisible
                    ? Component.translatable("tooltip.beachparty.toggle.hide").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5CB85C)))
                    : Component.translatable("tooltip.beachparty.toggle.show").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5CB85C)));

            tooltip.add(toggleText);
        }
    }
}