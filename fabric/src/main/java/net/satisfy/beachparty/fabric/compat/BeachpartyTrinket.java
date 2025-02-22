package net.satisfy.beachparty.fabric.compat;

import dev.emi.trinkets.api.*;
import net.minecraft.core.BlockPos;
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
import org.joml.Vector3d;

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
                if (!player.isInvulnerableTo(player.level().damageSources().onFire())) {
                    player.hurt(player.level().damageSources().onFire(), 1 - fireDamageReduction);
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
                player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), -1, 0, false, false));
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
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), -1, 0, false, false));
                }
            }
        }
    }

    public static class RubberRingTrinket extends BaseTrinket {
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
                Vec3 motion = player.getDeltaMovement();
                double targetUpwardSpeed = 0.3;
                double newY = motion.y;
                if (motion.y < targetUpwardSpeed) {
                    newY += 0.01;
                    if (newY > targetUpwardSpeed) newY = targetUpwardSpeed;
                }

                double normalSpeed = 0.17;
                double maxSpeed = normalSpeed * 1.17;

                double speedMultiplier = 1.17;
                Vec3 newMotion = new Vec3(motion.x * speedMultiplier, newY, motion.z * speedMultiplier);

                double currentSpeed = Math.sqrt(newMotion.x * newMotion.x + newMotion.z * newMotion.z);
                if (currentSpeed > maxSpeed) {
                    double scale = maxSpeed / currentSpeed;
                    newMotion = new Vec3(newMotion.x * scale, newY, newMotion.z * scale);
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
                Vector3d lookJ = new Vector3d(look.x, 0, look.z);
                if (lookJ.length() == 0) lookJ.set(1, 0, 0);
                Vector3d left = new Vector3d();
                new Vector3d(0, 1, 0).cross(lookJ, left).normalize();
                double offset = 0.5;
                double px = player.getX();
                double py = player.getY() + player.getEyeHeight() - 0.5;
                double pz = player.getZ();
                Vector3d leftPos = new Vector3d(px, py, pz).add(new Vector3d(left).mul(offset));
                Vector3d rightPos = new Vector3d(px, py, pz).sub(new Vector3d(left).mul(offset));

                spawnWaterParticles(player, leftPos, rightPos);
            }
        }

        private void spawnWaterParticles(Player player, Vector3d leftPos, Vector3d rightPos) {
            Random random = new Random();
            double spread = 0.2;
            if (player.getDeltaMovement().lengthSqr() > 0.005) {
                for (int i = 0; i < 3; i++) {
                    spawnParticlePair(player, ParticleTypes.SPLASH, leftPos, rightPos, random, spread);
                    spawnParticlePair(player, ParticleTypes.BUBBLE_POP, leftPos, rightPos, random, spread);
                }
            } else if (player.level().getGameTime() % 40L == 0L) {
                for (int i = 0; i < 2; i++) {
                    spawnParticlePair(player, ParticleTypes.SPLASH, leftPos, rightPos, random, spread);
                }
            }
        }

        private void spawnParticlePair(Player player, ParticleOptions particle, Vector3d leftPos, Vector3d rightPos, Random random, double spread) {
            player.level().addParticle(particle, leftPos.x + (random.nextDouble() * spread - spread / 2), leftPos.y + (random.nextDouble() * spread - spread / 2), leftPos.z + (random.nextDouble() * spread - spread / 2), 0, 0, 0);
            player.level().addParticle(particle, rightPos.x + (random.nextDouble() * spread - spread / 2), rightPos.y + (random.nextDouble() * spread - spread / 2), rightPos.z + (random.nextDouble() * spread - spread / 2), 0, 0, 0);
        }

    }

    public static class SunglassesTrinket extends BaseTrinket {
        public SunglassesTrinket() {
            super(0.12f, ObjectRegistry.SUNGLASSES.get());
        }
    }

    public static class SwimSuitTrinket extends BaseTrinket {
        public SwimSuitTrinket() {
            super(0,
                    ObjectRegistry.TRUNKS.get(),
                    ObjectRegistry.BIKINI.get()
            );
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player && player.isInWater() && player.isSwimming()) {
                Vec3 vec3 = player.getDeltaMovement();
                Vector3d vector3d = new Vector3d(vec3.x, vec3.y, vec3.z);
                vector3d.x *= 1.08;
                vector3d.z *= 1.08;
                player.setDeltaMovement(new Vec3(vector3d.x, vector3d.y, vector3d.z));
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
