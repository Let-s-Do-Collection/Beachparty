package net.satisfy.beachparty.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TideRushEffect extends MobEffect {

    public TideRushEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            double swimSpeedBonus = 0.1 + (amplifier * 0.05);
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0 + swimSpeedBonus, 1.0, 1.0 + swimSpeedBonus));
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
