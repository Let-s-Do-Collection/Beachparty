package net.satisfy.beachparty.core.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class OceanWalkEffect extends MobEffect {
    public OceanWalkEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (!(entity instanceof Player player) || player.isSpectator() || !player.isSprinting()) {
            return;
        }

        Vec3 pos = entity.position();
        Vec3 movement = entity.getDeltaMovement();
        Vec3 futurePos = pos.add(movement);
        BlockPos onPos = entity.getOnPos();
        BlockPos futureBlockPos = new BlockPos((int) futurePos.x, (int) futurePos.y, (int) futurePos.z);

        if (entity.isInWater()) {
            entity.setDeltaMovement(movement.add(0, 0.1, 0));
        } else if (entity.level().getFluidState(onPos).is(FluidTags.WATER)) {
            if (entity.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.FALLING_WATER, pos.x(), pos.y() + 0.1D, pos.z(), 10, 0.2, 0.1, 0.2, 1.5);
            }
            entity.setDeltaMovement(movement.x(), Math.max(movement.y(), 0D), movement.z());
            entity.setOnGround(true);
        } else if (entity.level().getFluidState(futureBlockPos).is(FluidTags.WATER) && movement.y() > -0.8) {
            if (entity.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.FALLING_WATER, pos.x(), pos.y() + 0.1D, pos.z(), 10, 0.2, 0.1, 0.2, 1.5);
            }
            entity.setDeltaMovement(movement.x(), Math.max(movement.y(), movement.y() * 0.5), movement.z());
        }

        super.applyEffectTick(entity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
