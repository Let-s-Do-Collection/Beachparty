package net.satisfy.beachparty.neoforge.client.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.core.registry.MobEffectRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Random;

public class CuriosWearableTrinket {
    private static final Random RANDOM = new Random();

    public static boolean isCurioEquipped(Player player, Item... curios) {
        return CuriosApi.getCuriosHelper().findFirstCurio(player, stack -> {
            for (Item curio : curios) {
                if (stack.getItem() == curio) {
                    return true;
                }
            }
            return false;
        }).isPresent();
    }

    public static class BaseCurio implements ICurioItem {
        private final Item[] conflictingItems;

        public BaseCurio(Item... conflictingItems) {
            this.conflictingItems = conflictingItems;
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player) {
                removeEffect(player);
            }
        }

        protected void removeEffect(Player player) {
        }

        @Override
        public boolean canEquip(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!(entity instanceof Player player)) {
                return false;
            }
            return !CuriosWearableTrinket.isCurioEquipped(player, conflictingItems);
        }
    }

    public static class CrocsCurio extends BaseCurio {
        public CrocsCurio() {
            super(ObjectRegistry.CROCS.get());
        }

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
            super.onEquip(slotContext, prevStack, stack);
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && !player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.OCEAN_WALK.get()))) {
                player.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.OCEAN_WALK.get()), -1, 0, false, false));
            }
        }

        @Override
        protected void removeEffect(Player player) {
            player.removeEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.OCEAN_WALK.get()));
        }
    }

    public static class RubberRingCurio extends BaseCurio {
        public RubberRingCurio() {
            super(
                    ObjectRegistry.RUBBER_RING_AXOLOTL.get(),
                    ObjectRegistry.RUBBER_RING_PELICAN.get(),
                    ObjectRegistry.RUBBER_RING_BLUE.get(),
                    ObjectRegistry.RUBBER_RING_STRIPPED.get(),
                    ObjectRegistry.RUBBER_RING_PINK.get()
            );
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!(entity instanceof Player player)) return;

            if (player.isInWater()) {
                Vec3 motion = player.getDeltaMovement();

                if (motion.y > 0.08) return;

                double targetUpwardSpeed = 0.3;
                double newY = motion.y;
                if (motion.y < targetUpwardSpeed) {
                    newY += 0.01;
                    if (newY > targetUpwardSpeed) newY = targetUpwardSpeed;
                }
                double maxSpeed = 0.3;
                double newX = Math.min(motion.x * 1.08, maxSpeed);
                double newZ = Math.min(motion.z * 1.08, maxSpeed);
                player.setDeltaMovement(new Vec3(newX, newY, newZ));

                BlockPos pos = player.blockPosition().above();
                int maxIterations = 10;
                while (player.level().getBlockState(pos).getFluidState().isEmpty() && maxIterations > 0) {
                    pos = pos.below();
                    maxIterations--;
                }
                double targetY = pos.getY();
                if (player.getY() > targetY && !player.isUnderWater()) {
                    player.setPos(player.getX(), targetY, player.getZ());
                }

                Vec3 viewDir = player.getViewVector(1.0f);
                Vec3 sideOffset = new Vec3(-viewDir.z, 0, viewDir.x).normalize().scale(0.45);

                Vec3 leftHandPos = player.position().add(sideOffset).add(0, player.getEyeHeight() - 0.4, 0);
                Vec3 rightHandPos = player.position().subtract(sideOffset).add(0, player.getEyeHeight() - 0.4, 0);
                spawnWaterParticles(player, leftHandPos, rightHandPos);
            }
        }

        private void spawnWaterParticles(Player player, Vec3 leftPos, Vec3 rightPos) {
            if (!player.level().isClientSide()) return;

            double spread = 0.2;
            double motionThreshold = 0.005;

            if (player.getDeltaMovement().lengthSqr() > motionThreshold) {
                for (int i = 0; i < 2; i++) {
                    spawnParticlePair(player, ParticleTypes.SPLASH, leftPos, rightPos, spread);
                    spawnParticlePair(player, ParticleTypes.BUBBLE_POP, leftPos, rightPos, spread);
                }
            } else if (player.level().getGameTime() % 20L == 0L) {
                for (int i = 0; i < 4; i++) {
                    spawnRandomSplash(player, spread);
                }
            }
        }

            private void spawnParticlePair(Player player, ParticleOptions particle, Vec3 leftPos, Vec3 rightPos, double spread) {
            spawnParticle(player, particle, leftPos, spread);
            spawnParticle(player, particle, rightPos, spread);
        }

        private void spawnParticle(Player player, ParticleOptions particle, Vec3 pos, double spread) {
            player.level().addParticle(particle,
                    pos.x + (RANDOM.nextDouble() * spread - spread / 2),
                    pos.y + (RANDOM.nextDouble() * spread - spread / 2),
                    pos.z + (RANDOM.nextDouble() * spread - spread / 2),
                    0, 0, 0);
        }

        private void spawnRandomSplash(Player player, double spread) {
            double offset = 0.25;
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double xOffset = Math.cos(angle) * offset;
            double zOffset = Math.sin(angle) * offset;

            double x = player.getX() + xOffset;
            double y = player.getY() + player.getEyeHeight() - 0.5 + (RANDOM.nextDouble() * spread - spread / 2);
            double z = player.getZ() + zOffset;

            player.level().addParticle(ParticleTypes.SPLASH, x, y, z, 0, 0, 0);
        }
    }

    public static class SwimSuitCurio extends BaseCurio {
        public SwimSuitCurio() {
            super(
                    ObjectRegistry.TRUNKS.get(),
                    ObjectRegistry.BIKINI.get()
            );
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && player.isInWater() && player.isSwimming()) {
                Vec3 newMotion = player.getDeltaMovement().multiply(1.08, 1, 1.08);
                double maxSpeed = 0.3;
                if (newMotion.lengthSqr() > maxSpeed * maxSpeed) {
                    newMotion = newMotion.normalize().scale(maxSpeed);
                }
                player.setDeltaMovement(new Vec3(newMotion.x, player.getDeltaMovement().y, newMotion.z));
            }
        }
    }

    public static class SwimWingsCurio extends BaseCurio {
        public SwimWingsCurio() {
            super(ObjectRegistry.SWIM_WINGS.get());
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && !player.level().isClientSide()) {
                if (!player.onGround() && player.getDeltaMovement().y < -0.1F && player.fallDistance > 3.0F) {
                    if (!player.getCooldowns().isOnCooldown(ObjectRegistry.SWIM_WINGS.get())) {
                        player.fallDistance *= 0.5F;
                        player.getCooldowns().addCooldown(ObjectRegistry.SWIM_WINGS.get(), 100);
                    }
                }
            }
        }

        @Override
        protected void removeEffect(Player player) {
            player.getCooldowns().removeCooldown(ObjectRegistry.SWIM_WINGS.get());
        }
    }
}
