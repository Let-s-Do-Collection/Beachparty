package net.satisfy.beachparty.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;

import java.util.Objects;

public class NeverMeltEffect extends MobEffect {

    private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(
            "NeverMelt Armor", 1.0, AttributeModifier.Operation.ADDITION);

    public NeverMeltEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (!Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).hasModifier(ARMOR_MODIFIER)) {
                Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).addTransientModifier(ARMOR_MODIFIER);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity instanceof Player player) {
            Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).removeModifier(ARMOR_MODIFIER);
        }
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @SuppressWarnings("unused")
    public void onPlayerHurt(Player player, DamageSource source, float amount) {
        if (player.hasEffect(this)) {
            player.addEffect(new MobEffectInstance(this, 600, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        }
    }
}
