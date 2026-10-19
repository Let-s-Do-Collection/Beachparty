package net.satisfy.beachparty.core.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class OceanWalkEffect extends MobEffect {
    private static final int SPLASH_PARTICLES = 3;

    public OceanWalkEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return false;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    public static void tick(Player player) {
        if (player.isSpectator() || !player.isSprinting()) return;

        Vec3 pos = player.position();
        Vec3 movement = player.getDeltaMovement();
        Vec3 futurePos = pos.add(movement);
        BlockPos onPos = player.getOnPos();
        BlockPos futureBlockPos = BlockPos.containing(futurePos);

        if (player.isInWater()) {
            player.setDeltaMovement(movement.add(0, 0.1, 0));
        } else if (player.level().getFluidState(onPos).is(FluidTags.WATER)) {
            spawnSplash(player, pos);
            player.setDeltaMovement(movement.x(), Math.max(movement.y(), 0D), movement.z());
            player.setOnGround(true);
        } else if (player.level().getFluidState(futureBlockPos).is(FluidTags.WATER) && movement.y() > -0.8) {
            spawnSplash(player, pos);
            player.setDeltaMovement(movement.x(), Math.max(movement.y(), movement.y() * 0.5), movement.z());
        }
    }

    private static void spawnSplash(Player player, Vec3 pos) {
        if (!player.level().isClientSide()) return;
        RandomSource random = player.getRandom();
        for (int i = 0; i < SPLASH_PARTICLES; i++) {
            player.level().addParticle(ParticleTypes.FALLING_WATER, pos.x() + random.nextGaussian() * 0.2, pos.y() + 0.1D + random.nextGaussian() * 0.1, pos.z() + random.nextGaussian() * 0.2, 0, 0, 0);
        }
    }
}
