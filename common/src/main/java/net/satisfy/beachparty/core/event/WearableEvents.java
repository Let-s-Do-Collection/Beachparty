package net.satisfy.beachparty.core.event;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.client.WearableClientHooks;
import net.satisfy.beachparty.core.effect.OceanWalkEffect;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.armor.Wearing;

import java.util.List;
import java.util.function.Supplier;

public class WearableEvents {
    private static final double RUBBER_RING_SURFACE_SEARCH = 4.0;
    private static final Supplier<List<Item>> RUBBER_RINGS = Suppliers.memoize(() -> List.of(ObjectRegistry.RUBBER_RING_BLUE.get(), ObjectRegistry.RUBBER_RING_PINK.get(), ObjectRegistry.RUBBER_RING_STRIPPED.get(), ObjectRegistry.RUBBER_RING_PELICAN.get(), ObjectRegistry.RUBBER_RING_AXOLOTL.get()));
    private static final Supplier<List<Item>> SWIM_SUITS = Suppliers.memoize(() -> List.of(ObjectRegistry.TRUNKS.get(), ObjectRegistry.BIKINI.get()));

    public static void init() {
        TickEvent.PLAYER_POST.register(WearableEvents::onPlayerTick);
    }

    public static float modifyDamage(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof Player player)) return amount;
        if (source.is(DamageTypeTags.IS_FIRE)) {
            float reduction = 0;
            if (Wearing.isWearing(player, ObjectRegistry.BEACH_HAT.get())) reduction += PlatformHelper.getBeachHatFireProtection();
            if (Wearing.isWearing(player, ObjectRegistry.SUNGLASSES.get())) reduction += PlatformHelper.getSunglassesFireProtection();
            return amount * (1 - Math.min(reduction, 1));
        }
        if (source.is(DamageTypeTags.IS_FALL) && Wearing.isWearing(player, ObjectRegistry.SWIM_WINGS.get())) {
            return amount * (1 - PlatformHelper.getSwimWingsFallProtection());
        }
        return amount;
    }

    public static Object[] tooltipArgs(Item item) {
        if (item == ObjectRegistry.BEACH_HAT.get()) return percent(PlatformHelper.getBeachHatFireProtection());
        if (item == ObjectRegistry.SUNGLASSES.get()) return percent(PlatformHelper.getSunglassesFireProtection());
        if (item == ObjectRegistry.SWIM_WINGS.get()) return percent(PlatformHelper.getSwimWingsFallProtection());
        if (SWIM_SUITS.get().contains(item)) return percent(PlatformHelper.getSwimSuitSpeedMultiplier() - 1);
        if (RUBBER_RINGS.get().contains(item)) return percent(PlatformHelper.getRubberRingSpeedMultiplier() - 1);
        return new Object[0];
    }

    private static Object[] percent(double fraction) {
        return new Object[]{Math.round(fraction * 100)};
    }

    private static void onPlayerTick(Player player) {
        if (player.isSpectator()) return;
        if (Wearing.isWearing(player, ObjectRegistry.CROCS.get())) {
            OceanWalkEffect.tick(player);
        }
        if (!player.level().isClientSide()) return;
        if (player.isInWater() && Wearing.isWearingAny(player, RUBBER_RINGS.get())) {
            tickRubberRing(player);
        }
        if (player.isInWater() && player.isSwimming() && Wearing.isWearingAny(player, SWIM_SUITS.get())) {
            double speed = PlatformHelper.getSwimSuitSpeedMultiplier();
            player.setDeltaMovement(player.getDeltaMovement().multiply(speed, 1, speed));
        }
    }

    private static void tickRubberRing(Player player) {
        if (WearableClientHooks.isJumping(player)) return;

        double speed = PlatformHelper.getRubberRingSpeedMultiplier();
        Vec3 motion = player.getDeltaMovement();
        double newY = Math.min(motion.y + 0.01, 0.3);
        Vec3 newMotion = motion.multiply(speed, 1, speed).with(Direction.Axis.Y, newY);
        double maxSpeed = 0.17 * speed;
        if (newMotion.horizontalDistance() > maxSpeed) {
            newMotion = newMotion.normalize().scale(maxSpeed).with(Direction.Axis.Y, newY);
        }
        player.setDeltaMovement(newMotion);

        if (player.level().isLoaded(player.blockPosition())) {
            BlockHitResult hitResult = player.level().clip(new ClipContext(player.position(), player.position().subtract(0, RUBBER_RING_SURFACE_SEARCH, 0), ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
            if (hitResult.getType() != HitResult.Type.MISS) {
                BlockPos pos = hitResult.getBlockPos();
                double targetY = pos.getY();
                if (player.getY() > targetY + 1.0 && !player.isUnderWater()) {
                    player.setPos(player.getX(), targetY + 1.0, player.getZ());
                }
            }
        }

        Vec3 look = player.getLookAngle();
        Vec3 flatLook = new Vec3(look.x, 0, look.z);
        if (flatLook.lengthSqr() == 0) flatLook = new Vec3(1, 0, 0);
        Vec3 left = flatLook.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 leftPos = player.position().add(left.scale(0.5)).add(0, 0.8, 0);
        Vec3 rightPos = player.position().subtract(left.scale(0.5)).add(0, 0.8, 0);
        spawnWaterParticles(player, leftPos, rightPos);
    }

    private static void spawnWaterParticles(Player player, Vec3 leftPos, Vec3 rightPos) {
        double spread = 0.2;
        if (player.getDeltaMovement().lengthSqr() > 0.005) {
            for (int i = 0; i < 3; i++) {
                spawnParticle(player, ParticleTypes.SPLASH, leftPos, spread);
                spawnParticle(player, ParticleTypes.SPLASH, rightPos, spread);
                spawnParticle(player, ParticleTypes.BUBBLE_POP, leftPos, spread);
                spawnParticle(player, ParticleTypes.BUBBLE_POP, rightPos, spread);
            }
        } else if (player.level().getGameTime() % 20L == 0L) {
            RandomSource random = player.getRandom();
            for (int i = 0; i < 5; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double x = player.getX() + Math.cos(angle) * 0.25;
                double y = player.getY() + player.getEyeHeight() - 0.5 + (random.nextDouble() * spread - spread / 2);
                double z = player.getZ() + Math.sin(angle) * 0.25;
                player.level().addParticle(ParticleTypes.SPLASH, x, y, z, 0, 0, 0);
            }
        }
    }

    private static void spawnParticle(Player player, ParticleOptions particle, Vec3 pos, double spread) {
        RandomSource random = player.getRandom();
        player.level().addParticle(particle, pos.x + (random.nextDouble() * spread - spread / 2), pos.y + (random.nextDouble() * spread - spread / 2), pos.z + (random.nextDouble() * spread - spread / 2), 0, 0, 0);
    }
}
