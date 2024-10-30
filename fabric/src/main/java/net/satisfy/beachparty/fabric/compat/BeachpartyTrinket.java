package net.satisfy.beachparty.fabric.compat;

import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.registry.MobEffectRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;
import org.lwjgl.glfw.GLFW;

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
                player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), 0, 0, false, false));
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
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.OCEAN_WALK.get(), 0, 0, false, false));
                }
            }
        }
    }

    public static class RubberRingTrinket extends BaseTrinket {
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
        public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onEquip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.AQUA_FLOAT.get(), 1, 0, false, false));
            }
        }

        @Override
        public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.onUnequip(stack, slot, entity);
            if (entity instanceof Player player) {
                player.removeEffect(MobEffectRegistry.AQUA_FLOAT.get());
            }
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);
            if (entity instanceof Player player) {
                if (!player.hasEffect(MobEffectRegistry.AQUA_FLOAT.get())) {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.AQUA_FLOAT.get(), 1, 0, false, false));
                }
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
        private static KeyMapping dashKey;

        public SwimWingsTrinket() {
            super(0, ObjectRegistry.SWIM_WINGS.get());
        }

        public static void registerKeybind() {
            dashKey = new KeyMapping("key.beachparty.dash", GLFW.GLFW_KEY_R, "key.categories.beachparty");

            KeyBindingHelper.registerKeyBinding(dashKey);
        }

        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            super.tick(stack, slot, entity);

            if (entity instanceof Player player) {
                if (dashKey != null && player.isInWater() && dashKey.isDown() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
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
