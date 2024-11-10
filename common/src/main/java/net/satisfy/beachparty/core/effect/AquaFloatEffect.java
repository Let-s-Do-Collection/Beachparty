package net.satisfy.beachparty.core.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AquaFloatEffect extends MobEffect {

    public AquaFloatEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            double swimSpeedBonus = 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0 + swimSpeedBonus, 1.0, 1.0 + swimSpeedBonus));

            if (entity.getDeltaMovement().y < 0) {
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0.0, entity.getDeltaMovement().z);
            }
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
