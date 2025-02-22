package net.satisfy.beachparty.forge.event;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.forge.registry.BeachpartyVillagers;


import java.util.List;

@Mod.EventBusSubscriber(modid = Beachparty.MOD_ID)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType().equals(BeachpartyVillagers.SANDYMERCHANT.get())) {

            List<VillagerTrades.ItemListing> level1 = event.getTrades().computeIfAbsent(1, k -> new java.util.ArrayList<>());
            level1.add(new BasicItemListing(8, new ItemStack(ObjectRegistry.PALM_SPROUT.get()), 1, 3, 0.05F));
            level1.add(new BasicItemListing(2, new ItemStack(ObjectRegistry.COCONUT_OPEN.get()), 1, 2, 0.05F));
            level1.add(new BasicItemListing(4, new ItemStack(ObjectRegistry.PALM_TORCH_ITEM.get()), 17, 4, 0.05F));
            level1.add(new BasicItemListing(7, new ItemStack(ObjectRegistry.PALM_LEAVES.get()), 1, 4, 0.05F));

            List<VillagerTrades.ItemListing> level2 = event.getTrades().computeIfAbsent(2, k -> new java.util.ArrayList<>());
            level2.add(new BasicItemListing(4, new ItemStack(ObjectRegistry.THATCH.get()), 9, 2, 0.05F));
            level2.add(new BasicItemListing(6, new ItemStack(ObjectRegistry.PALM_PLANKS.get()), 22, 2, 0.05F));
            level2.add(new BasicItemListing(3, new ItemStack(ObjectRegistry.SANDWAVES.get()), 4, 2, 0.05F));
            level2.add(new BasicItemListing(27, new ItemStack(ObjectRegistry.POOL_NOODLE.get()), 1, 10, 0.05F));

            List<VillagerTrades.ItemListing> level3 = event.getTrades().computeIfAbsent(3, k -> new java.util.ArrayList<>());
            level3.add(new BasicItemListing(5, new ItemStack(ObjectRegistry.BEACH_PARASOL.get()), 1, 5, 0.05F));
            level3.add(new BasicItemListing(3, new ItemStack(ObjectRegistry.BEACH_TOWEL.get()), 1, 5, 0.05F));
            level3.add(new BasicItemListing(36, new ItemStack(ObjectRegistry.OVERGROWN_DISC.get()), 1, 10, 0.05F));
            level3.add(new BasicItemListing(11, new ItemStack(ObjectRegistry.SAND_BUCKET_FILLED.get()), 1, 5, 0.05F));

            List<VillagerTrades.ItemListing> level4 = event.getTrades().computeIfAbsent(4, k -> new java.util.ArrayList<>());
            level4.add(new BasicItemListing(44, new ItemStack(ObjectRegistry.RADIO.get()), 1, 12, 0.05F));
            level4.add(new BasicItemListing(51, new ItemStack(ObjectRegistry.BEACH_BALL.get()), 1, 12, 0.05F));
            level4.add(new BasicItemListing(37, new ItemStack(ObjectRegistry.BEACH_GOAL.get()), 1, 8, 0.05F));
            level4.add(new BasicItemListing(12, new ItemStack(ObjectRegistry.COCONUT_COCKTAIL.get()), 1, 4, 0.05F));

            List<VillagerTrades.ItemListing> level5 = event.getTrades().computeIfAbsent(5, k -> new java.util.ArrayList<>());
            level5.add(new BasicItemListing(14, new ItemStack(ObjectRegistry.MINI_FRIDGE.get()), 1, 2, 0.05F));
            level5.add(new BasicItemListing(41, new ItemStack(ObjectRegistry.TRUNKS.get()), 1, 10, 0.05F));
            level5.add(new BasicItemListing(56, new ItemStack(ObjectRegistry.BIKINI.get()), 1, 12, 0.05F));
            level5.add(new BasicItemListing(61, new ItemStack(ObjectRegistry.RUBBER_RING_BLUE.get()), 1, 12, 0.05F));
            level5.add(new BasicItemListing(58, new ItemStack(ObjectRegistry.RUBBER_RING_PINK.get()), 1, 12, 0.05F));
            level5.add(new BasicItemListing(22, new ItemStack(ObjectRegistry.MESSAGE_IN_A_BOTTLE_ITEM.get()), 1, 4, 0.05F));
            level5.add(new BasicItemListing(39, new ItemStack(ObjectRegistry.FLOATY_BOAT.get()), 1, 8, 0.05F));
            level5.add(new BasicItemListing(45, new ItemStack(ObjectRegistry.BEACH_HAT.get()), 1, 8, 0.05F));
        }
    }
}