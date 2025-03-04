package net.satisfy.beachparty.forge.client.integration;

import net.minecraft.core.BlockPos;
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

public class CuriosWearableTrinket {
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
        private final float fireDamageReduction;
        private final Item[] conflictingItems;

        public BaseCurio(float fireDamageReduction, Item... conflictingItems) {
            this.fireDamageReduction = fireDamageReduction;
            this.conflictingItems = conflictingItems;
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!(entity instanceof Player player)) return;

            if (fireDamageReduction > 0 && (player.isOnFire() || player.getRemainingFireTicks() > 0)) {
                player.clearFire();
            }
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
            super(0, ObjectRegistry.CROCS.get());
        }

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
            super.onEquip(slotContext, prevStack, stack);
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && !player.hasEffect(MobEffectRegistry.OCEAN_WALK.get())) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), -1, 0, false, false));
            }
        }

        @Override
        protected void removeEffect(Player player) {
            player.removeEffect(MobEffectRegistry.OCEAN_WALK.get());
        }
    }

    public static class RubberRingCurio extends BaseCurio {
        public RubberRingCurio() {
            super(0,
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
                while (player.level().getBlockState(pos).getFluidState().isEmpty()) {
                    pos = pos.below();
                }
                double targetY = pos.getY();
                if (player.getY() > targetY && !player.isUnderWater()) {
                    player.setPos(player.getX(), targetY, player.getZ());
                }
            }
        }
    }

    public static class SwimSuitCurio extends BaseCurio {
        public SwimSuitCurio() {
            super(0,
                    ObjectRegistry.TRUNKS.get(),
                    ObjectRegistry.BIKINI.get()
            );
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && player.isInWater() && player.isSwimming()) {
                Vec3 motion = player.getDeltaMovement();
                double maxSpeed = 0.3;
                double newX = Math.min(motion.x * 1.08, maxSpeed);
                double newZ = Math.min(motion.z * 1.08, maxSpeed);
                player.setDeltaMovement(new Vec3(newX, motion.y, newZ));
            }
        }
    }

    public static class SwimWingsCurio extends BaseCurio {
        public SwimWingsCurio() {
            super(0, ObjectRegistry.SWIM_WINGS.get());
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide() && entity instanceof Player player) {
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
