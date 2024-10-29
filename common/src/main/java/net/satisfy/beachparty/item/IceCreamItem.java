package net.satisfy.beachparty.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.registry.MobEffectRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceCreamItem extends Item {
    public IceCreamItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffectRegistry.NEVERMELT.get(), 1500, 0));

            if (stack.is(ObjectRegistry.ICECREAM_CACTUS.get())) {
                player.hurt(level.damageSources().generic(), 6.0F);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 1));
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        MobEffectInstance effectInstance = new MobEffectInstance(MobEffectRegistry.NEVERMELT.get(), 1500, 0);
        MutableComponent effectText = Component.translatable(effectInstance.getDescriptionId());

        if (effectInstance.getDuration() > 20) {
            effectText = Component.translatable("potion.withDuration", effectText, MobEffectUtil.formatDuration(effectInstance, 1.0f)
            );
        }
        tooltip.add(effectText.withStyle(MobEffectRegistry.NEVERMELT.get().getCategory().getTooltipFormatting()));

        if (stack.is(ObjectRegistry.ICECREAM_CACTUS.get())) {
            tooltip.add(Component.translatable("potion.withDuration", Component.translatable(MobEffects.REGENERATION.getDescriptionId()), MobEffectUtil.formatDuration(new MobEffectInstance(MobEffects.REGENERATION, 160, 1), 1.0f)).withStyle(MobEffects.REGENERATION.getCategory().getTooltipFormatting()));
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltip.beachparty.coconut_icecream").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));

        }
    }
}
