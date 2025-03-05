package net.satisfy.beachparty.fabric.core.compat;

import dev.emi.trinkets.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.core.registry.MobEffectRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BeachpartyTrinket {
    public static boolean isTrinketEquipped(Player player, Item... trinkets) {
        for (ItemStack itemStack : player.getArmorSlots()) {
            for (Item trinket : trinkets) {
                if (itemStack.getItem() == trinket) {
                    return true;
                }
            }
        }

        Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(player);
        if (componentOptional.isPresent()) {
            TrinketComponent component = componentOptional.get();
            Map<String, Map<String, TrinketInventory>> inventoryMap = component.getInventory();

            for (Map<String, TrinketInventory> trinketGroup : inventoryMap.values()) {
                for (TrinketInventory trinketInventory : trinketGroup.values()) {
                    for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                        ItemStack trinketStack = trinketInventory.getItem(i);
                        if (!trinketStack.isEmpty()) {
                            for (Item trinket : trinkets) {
                                if (trinketStack.getItem() == trinket) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public abstract static class BaseTrinket implements Trinket {
        private final float fireDamageReduction;
        private final Item[] conflictingItems;

        protected BaseTrinket(float fireDamageReduction, Item... conflictingItems) {
            this.fireDamageReduction = fireDamageReduction;
            this.conflictingItems = conflictingItems;
        }

        @Override
        public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            if (entity instanceof Player player && fireDamageReduction > 0) {
                float damage = Math.max(0, 1 - fireDamageReduction);
                if (damage > 0 && !player.isInvulnerableTo(player.level().damageSources().inFire())) {
                    player.hurt(player.level().damageSources().inFire(), damage);
                }
            }
        }

        @Override
        public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        }

        @Override
        public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            if (!(entity instanceof Player player)) {
                return false;
            }
            return !BeachpartyTrinket.isTrinketEquipped(player, conflictingItems);
        }
    }

    public static class BeachhatTrinket extends BaseTrinket {
        public BeachhatTrinket() {
            super(0.10f, ObjectRegistry.BEACH_HAT.get());
        }
    }

    public static class CrocsTrinket extends BaseTrinket {
        public CrocsTrinket() {
            super(0, ObjectRegistry.CROCS.get());
        }

        @Override
        public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onEquip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), Integer.MAX_VALUE, 0, false, false));
            }
        }

        @Override
        public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onUnequip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.removeEffect(MobEffectRegistry.OCEAN_WALK.get());
            }
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player) {
                if (!player.hasEffect(MobEffectRegistry.OCEAN_WALK.get())) {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), Integer.MAX_VALUE, 0, false, false));
                }
            }
        }
    }

    public static class RubberRingTrinket extends BeachpartyTrinket.BaseTrinket {
        private static final Random RANDOM = new Random();

        public RubberRingTrinket() {
            super(0,
                    ObjectRegistry.RUBBER_RING_AXOLOTL.get(),
                    ObjectRegistry.RUBBER_RING_PELICAN.get(),
                    ObjectRegistry.RUBBER_RING_BLUE.get(),
                    ObjectRegistry.RUBBER_RING_STRIPPED.get(),
                    ObjectRegistry.RUBBER_RING_PINK.get()
            );
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            if (!(entity instanceof Player player)) return;

            if (player.isInWater()) {
                if (player instanceof LocalPlayer && Minecraft.getInstance().options.keyJump.isDown()) {
                    return;
                }

                Vec3 motion = player.getDeltaMovement();
                double newY = Math.min(motion.y + 0.01, 0.3);
                Vec3 newMotion = motion.multiply(1.17, 1, 1.17).with(Direction.Axis.Y, newY);

                double maxSpeed = 0.17 * 1.17;
                if (newMotion.horizontalDistance() > maxSpeed) {
                    newMotion = newMotion.normalize().scale(maxSpeed).with(Direction.Axis.Y, newY);
                }

                player.setDeltaMovement(newMotion);

                BlockPos pos = player.blockPosition().above();
                while (player.level().getBlockState(pos).getFluidState().isEmpty()) {
                    pos = pos.below();
                }
                double targetY = pos.getY();
                if (player.getY() > targetY && !player.isUnderWater()) {
                    player.setPos(player.getX(), targetY, player.getZ());
                }

                Vec3 look = player.getLookAngle();
                Vec3 lookJ = new Vec3(look.x, 0, look.z);
                if (lookJ.lengthSqr() == 0) lookJ = new Vec3(1, 0, 0);
                Vec3 left = lookJ.cross(new Vec3(0, 1, 0)).normalize();
                double offset = 0.5;
                Vec3 leftPos = player.position().add(left.scale(offset)).add(0, 0.8, 0);
                Vec3 rightPos = player.position().subtract(left.scale(offset)).add(0, 0.8, 0);

                spawnWaterParticles(player, leftPos, rightPos);
            }
        }

        private void spawnWaterParticles(Player player, Vec3 leftPos, Vec3 rightPos) {
            double spread = 0.2;
            double motionThreshold = 0.005;

            if (player.getDeltaMovement().lengthSqr() > motionThreshold) {
                for (int i = 0; i < 3; i++) {
                    spawnParticlePair(player, ParticleTypes.SPLASH, leftPos, rightPos, spread);
                    spawnParticlePair(player, ParticleTypes.BUBBLE_POP, leftPos, rightPos, spread);
                }
            } else if (player.level().getGameTime() % 20L == 0L) {
                for (int i = 0; i < 5; i++) {
                    spawnRandomSplash(player, spread);
                }
            }
        }

        private void spawnParticlePair(Player player, ParticleOptions particle, Vec3 leftPos, Vec3 rightPos, double spread) {
            spawnParticle(player, particle, leftPos, spread);
            spawnParticle(player, particle, rightPos, spread);
        }

        private void spawnParticle(Player player, ParticleOptions particle, Vec3 pos, double spread) {
            player.level().addParticle(particle, pos.x + (RANDOM.nextDouble() * spread - spread / 2), pos.y + (RANDOM.nextDouble() * spread - spread / 2), pos.z + (RANDOM.nextDouble() * spread - spread / 2), 0, 0, 0);
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

    public static class SunglassesTrinket extends BaseTrinket {
        public SunglassesTrinket() {
            super(0.12f, ObjectRegistry.SUNGLASSES.get());
        }
    }

    public static class SwimSuitTrinket extends BaseTrinket {
        public SwimSuitTrinket() {
            super(0, ObjectRegistry.TRUNKS.get(), ObjectRegistry.BIKINI.get());
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player && player.isInWater() && player.isSwimming()) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1.08, 1, 1.08));
            }
        }
    }

    public static class SwimWingsTrinket extends BaseTrinket {
        public SwimWingsTrinket() {
            super(0, ObjectRegistry.SWIM_WINGS.get());
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            if (!entity.level().isClientSide() && entity instanceof Player player && player.tickCount > 20) {
                if (!player.getCooldowns().isOnCooldown(ObjectRegistry.SWIM_WINGS.get())) {
                    if (!player.onGround() && player.getDeltaMovement().y < -0.1F && player.fallDistance > 3.0F) {
                        player.fallDistance *= 0.5F;
                        player.getCooldowns().addCooldown(ObjectRegistry.SWIM_WINGS.get(), 2400);
                    }
                }
            }
        }
    }
}
