package net.satisfy.beachparty.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoolNoodleItem extends SwordItem implements DyeableLeatherItem {

    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public PoolNoodleItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties properties) {
        super(toolMaterial, attackDamage, attackSpeed, properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier("Weapon knockback", 3, AttributeModifier.Operation.ADDITION));

        this.attributeModifiers = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Level world, List<Component> tooltip, @NotNull TooltipFlag tooltipContext) {
        tooltip.add(Component.translatable("tooltip.beachparty.dyeable").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }
}
