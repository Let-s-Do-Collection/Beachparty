package net.satisfy.beachparty.registry;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.*;
import net.satisfy.beachparty.block.furniture.*;
import net.satisfy.beachparty.entity.BeachpartyBoatEntity;
import net.satisfy.beachparty.item.*;
import net.satisfy.beachparty.item.BeachpartyArmorItem;
import net.satisfy.beachparty.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.util.BeachpartyIdentifier;
import net.satisfy.beachparty.util.BeachpartyUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Beachparty.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> SAND_PILE = registerWithoutItem("sand_pile", () -> new SandPileBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.SAND)));
    public static final RegistrySupplier<Block> THATCH = registerWithItem("thatch", () -> new HayBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistrySupplier<Block> THATCH_STAIRS = registerWithItem("thatch_stairs", () -> new StairBlock(THATCH.get().defaultBlockState(), BlockBehaviour.Properties.copy(THATCH.get()).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> THATCH_SLAB = registerWithItem("thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).strength(2.0F).sound(SoundType.WOOD).explosionResistance(3.0F)));
    public static final RegistrySupplier<Block> LOUNGE_CHAIR = registerWithItem("lounge_chair", () -> new LoungeChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> CHAIR = registerWithItem("chair", () -> new ChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> TABLE = registerWithItem("table", () -> new TableBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> BEACH_CHAIR = registerWithItem("beach_chair", () -> new BenchBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> DECK_CHAIR = registerWithItem("deck_chair", () -> new DeckChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> TIKI_CHAIR = registerWithItem("tiki_chair", () -> new TikiChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> TIKI_BAR = registerWithItem("tiki_bar", () -> new TikiBarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> CABINET = registerWithItem("cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS), () -> SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, () -> SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE));
    public static final RegistrySupplier<Block> MINI_FRIDGE = registerWithItem("mini_fridge", () -> new MiniFridgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.COPPER)));
    public static final RegistrySupplier<Block> RADIO = registerWithItem("radio", () -> new RadioBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Item> OVERGROWN_DISC = registerItem("overgrown_disc", () -> new RecordItem(1, SoundEventRegistry.RADIO_BEACHPARTY.get(), getSettings().stacksTo(1), 196));
    public static final RegistrySupplier<Block> MESSAGE_IN_A_BOTTLE = registerWithoutItem("message_in_a_bottle", () -> new MessageInABottleBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), Block.box(4.0f, 0.0f, 4.0f, 12.0f, 6.0f, 12.0f)));
    public static final RegistrySupplier<Item> MESSAGE_IN_A_BOTTLE_ITEM = registerItem("message_in_a_bottle", () -> new MessageInABottleItem(ObjectRegistry.MESSAGE_IN_A_BOTTLE.get(), getSettings()));
    public static final RegistrySupplier<Block> SEASHELL_BLOCK = registerWithoutItem("seashell_block", () -> new SeashellBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).instabreak().noParticlesOnBreak()));
    public static final RegistrySupplier<Item> SEASHELL = registerItem("seashell", () -> new SeashellItem(SEASHELL_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Block> SAND_BUCKET_BLOCK = registerWithoutItem("sand_bucket_block", () -> new SandBucketBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT)));
    public static final RegistrySupplier<Block> EMPTY_SAND_BUCKET_BLOCK = registerWithoutItem("empty_sand_bucket_block", () -> new SandBucketBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_WART)));
    public static final RegistrySupplier<Item> SAND_BUCKET = registerItem("sand_bucket", () -> new SandBucketItem(SAND_BUCKET_BLOCK.get(), getSettings().stacksTo(1)));
    public static final RegistrySupplier<Item> EMPTY_SAND_BUCKET = registerItem("empty_sand_bucket", () -> new SandBucketItem(EMPTY_SAND_BUCKET_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Block> COCONUT_BLOCK = registerWithoutItem("coconut_block", () -> new CoconutBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO)));
    public static final RegistrySupplier<Item> COCONUT = registerItem("coconut", () -> new CoconutItem(COCONUT_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Item> COCONUT_OPEN = registerItem("coconut_open", () -> new Item(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Block> COCONUT_COCKTAIL = registerCocktail("coconut_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.DAMAGE_BOOST);
    public static final RegistrySupplier<Block> SWEETBERRIES_COCKTAIL = registerCocktail("sweetberries_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.ABSORPTION);
    public static final RegistrySupplier<Block> COCOA_COCKTAIL = registerCocktail("cocoa_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.REGENERATION);
    public static final RegistrySupplier<Block> PUMPKIN_COCKTAIL = registerCocktail("pumpkin_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.FIRE_RESISTANCE);
    public static final RegistrySupplier<Block> HONEY_COCKTAIL = registerCocktail("honey_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.DIG_SPEED);
    public static final RegistrySupplier<Block> MELON_COCKTAIL = registerCocktail("melon_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.LUCK);
    public static final RegistrySupplier<Item> SWEETBERRY_MILKSHAKE = registerItem("sweetberry_milkshake", () -> new IceCreamItem(getSettings().food(Foods.COOKED_BEEF)));
    public static final RegistrySupplier<Item> COCONUT_MILKSHAKE = registerItem("coconut_milkshake", () -> new IceCreamItem(getSettings().food(Foods.COOKED_BEEF)));
    public static final RegistrySupplier<Item> CHOCOLATE_MILKSHAKE = registerItem("chocolate_milkshake", () -> new IceCreamItem(getSettings().food(Foods.COOKED_BEEF)));
    public static final RegistrySupplier<Block> REFRESHING_DRINK = registerCocktail("refreshing_drink", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()));
    public static final RegistrySupplier<Block> SWEETBERRY_SUNDAE = registerCocktail("sweetberry_sundae", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()));
    public static final RegistrySupplier<Block> COCONUT_SUNDAE = registerCocktail("coconut_sundae", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()));
    public static final RegistrySupplier<Block> CHOCOLATE_SUNDAE = registerCocktail("chocolate_sundae", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()));
    public static final RegistrySupplier<Item> ICECREAM_COCONUT = registerItem("icecream_coconut", () -> new IceCreamItem(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> ICECREAM_MELON = registerItem("icecream_melon", () -> new IceCreamItem(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> ICECREAM_CACTUS = registerItem("icecream_cactus", () -> new IceCreamItem(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> ICECREAM_SWEETBERRIES = registerItem("icecream_sweetberries", () -> new IceCreamItem(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> ICECREAM_CHOCOLATE = registerItem("icecream_chocolate", () -> new IceCreamItem(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> RAW_MUSSEL_MEAT = registerItem("raw_mussel_meat", () -> new Item(getSettings().food(Foods.POTATO)));
    public static final RegistrySupplier<Item> COOKED_MUSSEL_MEAT = registerItem("cooked_mussel_meat", () -> new Item(getSettings().food(Foods.BAKED_POTATO)));
    public static final RegistrySupplier<Block> BEACH_TOWEL = registerWithItem("beach_towel", () -> new BeachTowelBlock(DyeColor.WHITE, BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak().mapColor(DyeColor.WHITE)));
    public static final RegistrySupplier<Item> BEACH_HAT = registerItem("beach_hat", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.BEACH_HAT, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), new BeachpartyIdentifier("textures/models/armor/beach_hat.png")));
    public static final RegistrySupplier<Item> SUNGLASSES = registerItem("sunglasses", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.SUNGLASSES, ArmorItem.Type.HELMET, getSettings(), new BeachpartyIdentifier("textures/models/armor/sunglasses.png")));
    public static final RegistrySupplier<Item> TRUNKS = registerItem("trunks", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.TRUNKS, ArmorItem.Type.LEGGINGS, 16715535, getSettings().rarity(Rarity.COMMON), new BeachpartyIdentifier("textures/models/armor/trunks.png")));
    public static final RegistrySupplier<Item> BIKINI = registerItem("bikini", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.BIKINI, ArmorItem.Type.CHESTPLATE, 987135, getSettings().rarity(Rarity.COMMON), new BeachpartyIdentifier("textures/models/armor/bikini.png")));
    public static final RegistrySupplier<Item> CROCS = registerItem("crocs", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.CROCS, ArmorItem.Type.BOOTS, 1048335, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/crocs.png")));
    public static final RegistrySupplier<Item> SWIM_WINGS = registerItem("swim_wings", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.SWIM_WINGS, ArmorItem.Type.CHESTPLATE, 0xFFD700, getSettings(), new BeachpartyIdentifier("textures/models/armor/swim_wings.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_BLUE = registerItem("rubber_ring_blue", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.COMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_blue.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_PINK = registerItem("rubber_ring_pink", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.COMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_pink.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_STRIPPED = registerItem("rubber_ring_stripped", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.COMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_stripped.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_PELICAN = registerItem("rubber_ring_pelican", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.RARE), new BeachpartyIdentifier("textures/models/armor/rubber_ring_pelican.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_AXOLOTL = registerItem("rubber_ring_axolotl", () -> new BeachpartyArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.RARE), new BeachpartyIdentifier("textures/models/armor/rubber_ring_axolotl.png")));
    public static final RegistrySupplier<Item> POOL_NOODLE = registerItem("pool_noodle", () -> new PoolNoodleItem(Tiers.WOOD, 0, -1.4F, getSettings()));
    public static final RegistrySupplier<Block> BEACH_PARASOL = registerWithItem("beach_parasol", () -> new ParasolBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> TIKI_TORCH = registerWithoutItem("tiki_torch", () -> new TorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistrySupplier<Block> TIKI_WALL_TORCH = registerWithoutItem("tiki_wall_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD).dropsLike(TIKI_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistrySupplier<Item> TIKI_TORCH_ITEM = registerItem("tiki_torch_item", () -> new StandingAndWallBlockItem(ObjectRegistry.TIKI_TORCH.get(), ObjectRegistry.TIKI_WALL_TORCH.get(), getSettings(), Direction.DOWN));
    public static final RegistrySupplier<Block> TALL_TIKI_TORCH = registerWithItem("tall_tiki_torch", () -> new TallTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistrySupplier<Block> SANDCASTLE = registerWithoutItem("sandcastle", () -> new SandCastleBlock(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final Supplier<Item> FLOATY = registerItem("floaty", () -> new BeachpartyBoatItem(false, BeachpartyBoatEntity.Type.FLOATY, new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> FLOATY_CHEST_BOAT = registerItem("floaty_chest_boat", () -> new BeachpartyBoatItem(true, BeachpartyBoatEntity.Type.FLOATY, new Item.Properties().stacksTo(1)));


    /**
     //TODO
     * Fix TableBlock - doesn't connect properly
     * Fix SandBucketBlock - Texture, Model
     * Fix BeachChairBlock - Voxelshape, connection
     * Fix BeachChair - Voxelshape, Top/Bottom Part
     * Fix Coconut/ChocolateSundae break particles
     * Fix ForgeMixin
     * Add NeverMelt Icon
     */

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }


    private static FoodProperties cocktailFoodComponent(MobEffect effect) {
        FoodProperties.Builder component = new FoodProperties.Builder().nutrition(1).saturationMod(1);
        if (effect != null) component.effect(new MobEffectInstance(effect, 900), 1.0f);
        return component.build();
    }

    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }

    private static <T extends Block> RegistrySupplier<T> registerCocktail(String name, Supplier<T> block, MobEffect effect) {
        RegistrySupplier<T> toReturn = registerWithoutItem(name, block);
        registerItem(name, () -> new DrinkBlockItem(
                toReturn.get(),
                getSettings(settings -> settings.food(cocktailFoodComponent(effect)))
        ));
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<T> registerCocktail(String name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = registerWithoutItem(name, block);
        registerItem(name, () -> new DrinkBlockItem(
                toReturn.get(),
                getSettings(settings -> settings.food(cocktailFoodComponent(MobEffectRegistry.NEVERMELT.get())))
        ));
        return toReturn;
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return BeachpartyUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new BeachpartyIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return BeachpartyUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new BeachpartyIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return BeachpartyUtil.registerItem(ITEMS, ITEM_REGISTRAR, new BeachpartyIdentifier(path), itemSupplier);
    }
}

