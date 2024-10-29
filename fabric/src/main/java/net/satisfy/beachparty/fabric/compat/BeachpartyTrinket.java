package net.satisfy.beachparty.fabric.compat;

import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.registry.ObjectRegistry;

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
    }

    public static class SwimWingsTrinket extends BaseTrinket {
        public SwimWingsTrinket() {
            super(0, ObjectRegistry.SWIM_WINGS.get());
        }
    }
}
