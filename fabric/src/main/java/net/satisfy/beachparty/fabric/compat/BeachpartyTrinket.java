package net.satisfy.beachparty.fabric.compat;

import dev.emi.trinkets.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.core.registry.MobEffectRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.fabric.client.BeachpartyFabricClient;

import java.util.Map;
import java.util.Optional;

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
        private static final int GLIDE_DURATION = 60;
        private static final int COOLDOWN_DURATION = 200;
        private boolean canGlide = true;
        private int cooldown = 0;
        private int glideTicks = 0;
        private boolean jumpKeyPressed = false;
        private boolean glideActivated = false;
        private int jumpPressesInAir = 0;

        public RubberRingTrinket() {
            super(0,
                    ObjectRegistry.RUBBER_RING_AXOLOTL.get(),
                    ObjectRegistry.RUBBER_RING_BLUE.get(),
                    ObjectRegistry.RUBBER_RING_PELICAN.get(),
                    ObjectRegistry.RUBBER_RING_PINK.get(),
                    ObjectRegistry.RUBBER_RING_STRIPPED.get()
            );
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player) {
                if (cooldown > 0) {
                    cooldown--;
                } else {
                    canGlide = true;
                }

                boolean isInAir = !player.onGround;
                boolean jumpKeyCurrentlyPressed = false;

                if (player instanceof LocalPlayer) {
                    jumpKeyCurrentlyPressed = Minecraft.getInstance().options.keyJump.isDown();
                }

                if (!isInAir) {
                    jumpPressesInAir = 0;
                }

                if (isInAir && jumpKeyCurrentlyPressed && !jumpKeyPressed) {
                    jumpPressesInAir++;
                }

                if (jumpPressesInAir >= 2 && glideTicks == 0 && canGlide) {
                    glideTicks = GLIDE_DURATION;
                    canGlide = false;
                    cooldown = COOLDOWN_DURATION;
                    glideActivated = true;

                    for (int i = 0; i < 20; i++) {
                        double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.5;
                        double offsetY = player.getRandom().nextDouble() * 0.5;
                        double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.5;
                        player.level().addParticle(ParticleTypes.POOF,
                                player.getX() + offsetX,
                                player.getY() + offsetY,
                                player.getZ() + offsetZ,
                                0, 0, 0);
                    }
                    player.playSound(SoundEvents.WOOL_FALL, 0.75F, 0.75F);
                }
                if (glideTicks > 0 && glideActivated) {
                    player.setDeltaMovement(player.getDeltaMovement().x, -0.05, player.getDeltaMovement().z);
                    glideTicks--;

                    if (glideTicks == 0) {
                        player.setDeltaMovement(player.getDeltaMovement().x, -0.08, player.getDeltaMovement().z);
                        glideActivated = false;
                    }
                }
                jumpKeyPressed = jumpKeyCurrentlyPressed;
            }
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
        public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onEquip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.TIDE_RUSH.get(), 0, 0, false, false));
            }
        }

        @Override
        public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onUnequip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.removeEffect(MobEffectRegistry.TIDE_RUSH.get());
            }
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player) {
                if (!player.hasEffect(MobEffectRegistry.TIDE_RUSH.get())) {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.TIDE_RUSH.get(), 0, 0, false, false));
                }
            }
        }
    }

    public static class SwimWingsTrinket extends BaseTrinket {
        private static final int DASH_DISTANCE = 6;
        private static final int DASH_COOLDOWN_TICKS = 240;

        public SwimWingsTrinket() {
            super(0, ObjectRegistry.SWIM_WINGS.get());
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);

            if (entity instanceof Player player) {
                if (player.isInWater() && BeachpartyFabricClient.isDashKeyPressed() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                    executeDash(player, stack);
                } else {
                    player.setSwimming(false);
                }
            }
        }

        private void executeDash(Player player, ItemStack stack) {
            Vec3 lookDirection = player.getLookAngle().normalize();
            Vec3 dashVector = lookDirection.scale(DASH_DISTANCE * 0.2);

            player.setDeltaMovement(dashVector);
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
            }

            player.setSwimming(true);
            spawnBubbleParticles(player);

            player.getCooldowns().addCooldown(stack.getItem(), DASH_COOLDOWN_TICKS);
        }

        private void spawnBubbleParticles(Player player) {
            Vec3 playerPosition = player.position().subtract(player.getLookAngle().scale(0.5));
            for (int i = 0; i < 15; i++) {
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.3;
                double offsetY = (player.getRandom().nextDouble() - 0.5) * 0.1;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.3;

                Vec3 particlePosition = playerPosition.add(offsetX, offsetY, offsetZ);
                player.level().addParticle(ParticleTypes.BUBBLE, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
            }
        }
    }
}
