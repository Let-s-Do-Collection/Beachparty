package net.satisfy.beachparty.core.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DrinkBlockItem extends BlockItem {
    public DrinkBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag context) {
        PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        List<Pair<Attribute, AttributeModifier>> list3 = Lists.newArrayList();
        if (!potionContents.hasEffects()) {
            tooltip.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        } else {
            potionContents.forEachEffect(mobEffectInstance -> {
                MutableComponent mutableText = Component.translatable(mobEffectInstance.getDescriptionId());
                if (mobEffectInstance.getDuration() > 20) {
                    mutableText = Component.translatable(
                            "potion.withDuration",
                            mutableText, MobEffectUtil.formatDuration(mobEffectInstance, mobEffectInstance.getDuration(), mobEffectInstance.getAmplifier()));
                }
                tooltip.add(mutableText.withStyle(mobEffectInstance.getEffect().value().getCategory().getTooltipFormatting()));
            });
            List<Pair<Attribute, AttributeModifier>> attributeModifiers = Lists.newArrayList();
            if (stack.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
                ItemAttributeModifiers itemAttributeModifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
                itemAttributeModifiers.modifiers().forEach(entry -> {
                    Attribute attribute = entry.attribute().value();
                    double amount = entry.modifier().amount();
                    if (attribute != null) {
                        AttributeModifier modifier = new AttributeModifier(entry.modifier().id(), amount, entry.modifier().operation());
                        attributeModifiers.add(new Pair<>(attribute, modifier));
                    }
                });
            }

            if (!attributeModifiers.isEmpty()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
                for (Pair<Attribute, AttributeModifier> pair : attributeModifiers) {
                    AttributeModifier modifier = pair.getSecond();
                    double amount = modifier.amount();
                    double displayAmount = (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                            ? amount * 100.0
                            : amount;
                    if (amount > 0.0) {
                        tooltip.add(Component.translatable(
                                        "attribute.modifier.plus." + modifier.operation().id(),
                                        ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount),
                                        Component.translatable(pair.getFirst().getDescriptionId()))
                                .withStyle(ChatFormatting.BLUE));
                    } else if (amount < 0.0) {
                        tooltip.add(Component.translatable(
                                        "attribute.modifier.take." + modifier.operation().id(),
                                        ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-displayAmount),
                                        Component.translatable(pair.getFirst().getDescriptionId()))
                                .withStyle(ChatFormatting.RED));
                    }
                }
            }
        }

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }
}