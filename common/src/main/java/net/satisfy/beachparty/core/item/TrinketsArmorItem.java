package net.satisfy.beachparty.core.item;

import dev.architectury.platform.Platform;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrinketsArmorItem extends ArmorItem {
    private final ResourceLocation getTexture;

    public TrinketsArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties, ResourceLocation getTexture) {
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

    public void toggleVisibility(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        boolean isVisible = !tag.contains("Visible") || tag.getBoolean("Visible");
        tag.putBoolean("Visible", !isVisible);
        itemStack.setTag(tag);
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
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        if (Platform.isFabric()) {
            tooltip.add(Component.translatable("tooltip.beachparty.trinketsslot")
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFAF3E0))));
        } else if (Platform.isForge()) {
            tooltip.add(Component.translatable("tooltip.beachparty.curiosslot")
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFAF3E0))));
        }

        tooltip.add(Component.translatable("tooltip.beachparty.effect." + this.getDescriptionId())
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD4B483))));

        tooltip.add(Component.empty());

        if (Platform.isFabric()) {
            CompoundTag tag = stack.getOrCreateTag();
            boolean isVisible = !tag.contains("Visible") || tag.getBoolean("Visible");

            Component toggleText = isVisible
                    ? Component.translatable("tooltip.beachparty.toggle.hide").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5CB85C)))
                    : Component.translatable("tooltip.beachparty.toggle.show").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5CB85C)));

            tooltip.add(toggleText);
        }
    }
}
