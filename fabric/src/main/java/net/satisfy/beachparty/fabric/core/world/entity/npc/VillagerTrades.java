package net.satisfy.beachparty.fabric.core.world.entity.npc;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

@SuppressWarnings("deprecation, unused")
public class VillagerTrades {
    private static final ResourceLocation SANDYMERCHANT_POI_IDENTIFIER = BeachpartyIdentifier.identifier("sandymerchant_poi");
    public static final PoiType SANDYMERCHANT_POI = PointOfInterestHelper.register(SANDYMERCHANT_POI_IDENTIFIER, 1, 12, ObjectRegistry.PALM_BAR.get());
    public static final VillagerProfession SANDYMERCHANT = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, BeachpartyIdentifier.identifier("sandymerchant"), VillagerProfessionBuilder.create().id(BeachpartyIdentifier.identifier("sandymerchant")).workstation(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, SANDYMERCHANT_POI_IDENTIFIER)).build());

    public static void init() {
        TradeOfferHelper.registerVillagerOffers(SANDYMERCHANT, 1, factories -> {
            factories.add(new SellItemFactory(ObjectRegistry.PALM_SPROUT.get(), 8, 1, 3));
            factories.add(new SellItemFactory(ObjectRegistry.COCONUT_OPEN.get(), 2, 1, 2));
            factories.add(new SellItemFactory(ObjectRegistry.PALM_TORCH_ITEM.get(), 4, 17, 4));
            factories.add(new SellItemFactory(ObjectRegistry.PALM_LEAVES.get(), 7, 1, 4));
        });
        TradeOfferHelper.registerVillagerOffers(SANDYMERCHANT, 2, factories -> {
            factories.add(new SellItemFactory(ObjectRegistry.THATCH.get(), 4, 9, 2));
            factories.add(new SellItemFactory(ObjectRegistry.PALM_PLANKS.get(), 6, 22, 2));
            factories.add(new SellItemFactory(ObjectRegistry.SANDWAVES.get(), 3, 4, 2));
            factories.add(new SellItemFactory(ObjectRegistry.POOL_NOODLE.get(), 27, 1, 10));
        });
        TradeOfferHelper.registerVillagerOffers(SANDYMERCHANT, 3, factories -> {
            factories.add(new SellItemFactory(ObjectRegistry.PALM_FLOORBOARD.get(), 6, 1, 7));
            factories.add(new SellItemFactory(ObjectRegistry.BEACH_PARASOL.get(), 5, 1, 5));
            factories.add(new SellItemFactory(ObjectRegistry.BEACH_TOWEL.get(), 3, 1, 5));
            factories.add(new SellItemFactory(ObjectRegistry.OVERGROWN_DISC.get(), 36, 1, 10));
            factories.add(new SellItemFactory(ObjectRegistry.SAND_BUCKET_FILLED.get(), 11, 1, 5));

        });
        TradeOfferHelper.registerVillagerOffers(SANDYMERCHANT, 4, factories -> {
            factories.add(new SellItemFactory(ObjectRegistry.RADIO.get(), 44, 1, 12));
            factories.add(new SellItemFactory(ObjectRegistry.BEACH_BALL.get(), 51, 1, 12));
            factories.add(new SellItemFactory(ObjectRegistry.BEACH_GOAL.get(), 37, 1, 8));
            factories.add(new SellItemFactory(ObjectRegistry.COCONUT_COCKTAIL.get(), 12, 1, 4));
        });
        TradeOfferHelper.registerVillagerOffers(SANDYMERCHANT, 5, factories -> {
            factories.add(new SellItemFactory(ObjectRegistry.MINI_FRIDGE.get(), 14, 1, 2));
            factories.add(new SellItemFactory(ObjectRegistry.TRUNKS.get(), 41, 1, 10));
            factories.add(new SellItemFactory(ObjectRegistry.BIKINI.get(), 56, 1, 12));
            factories.add(new SellItemFactory(ObjectRegistry.RUBBER_RING_BLUE.get(), 61, 1, 12));
            factories.add(new SellItemFactory(ObjectRegistry.RUBBER_RING_PINK.get(), 58, 1, 12));
            factories.add(new SellItemFactory(ObjectRegistry.MESSAGE_IN_A_BOTTLE_ITEM.get(), 22, 1, 4));
            factories.add(new SellItemFactory(ObjectRegistry.FLOATY_BOAT.get(), 39, 1, 8));
            factories.add(new SellItemFactory(ObjectRegistry.BEACH_HAT.get(), 45, 1, 8));
        });
    }

    static class BuyForOneEmeraldFactory implements net.minecraft.world.entity.npc.VillagerTrades.ItemListing {
        private final Item buy;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BuyForOneEmeraldFactory(ItemLike item, int price, int maxUses, int experience) {
            this.buy = item.asItem();
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack itemStack = new ItemStack(this.buy, this.price);
            return new MerchantOffer(new ItemCost(Items.EMERALD), itemStack, this.maxUses, this.experience, this.multiplier);
        }
    }

    static class SellItemFactory implements net.minecraft.world.entity.npc.VillagerTrades.ItemListing {
        private final ItemStack sell;
        private final int price;
        private final int count;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellItemFactory(Block block, int price, int count, int maxUses, int experience) {
            this(new ItemStack(block), price, count, maxUses, experience);
        }

        public SellItemFactory(Block item, int price, int count, int experience) {
            this(new ItemStack(item), price, count, 12, experience);
        }

        public SellItemFactory(Item item, int price, int count, int experience) {
            this(new ItemStack(item), price, count, 12, experience);
        }

        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            this(new ItemStack(item), price, count, maxUses, experience);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience) {
            this(stack, price, count, maxUses, experience, 0.05F);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience, float multiplier) {
            this.sell = stack;
            this.price = price;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemCost(Items.EMERALD, this.price),
                    new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier
            );
        }
    }
}