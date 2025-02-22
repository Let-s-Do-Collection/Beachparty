package net.satisfy.beachparty.core.registry;


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
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.*;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.core.item.*;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import net.satisfy.beachparty.core.util.BeachpartyWoodType;


import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Beachparty.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Item> OVERGROWN_DISC = registerItem("overgrown_disc", () -> new RecordItem(1, SoundEventRegistry.OVER_THE_RAINBOW.get(), getSettings().stacksTo(1), 214));
    public static final RegistrySupplier<Item> COCONUT_OPEN = registerItem("coconut_open", () -> new Item(getSettings().food(Foods.CARROT)));
    public static final RegistrySupplier<Item> RAW_MUSSEL_MEAT = registerItem("raw_mussel_meat", () -> new Item(getSettings().food(Foods.POTATO)));
    public static final RegistrySupplier<Item> COOKED_MUSSEL_MEAT = registerItem("cooked_mussel_meat", () -> new Item(getSettings().food(Foods.BAKED_POTATO)));
    public static final RegistrySupplier<Item> BEACH_HAT = registerItem("beach_hat", () -> new TrinketsArmorItem(ArmorMaterialRegistry.BEACH_HAT, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), new BeachpartyIdentifier("textures/models/armor/beach_hat.png")));
    public static final RegistrySupplier<Item> SUNGLASSES = registerItem("sunglasses", () -> new TrinketsArmorItem(ArmorMaterialRegistry.SUNGLASSES, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.RARE), new BeachpartyIdentifier("textures/models/armor/sunglasses.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_BLUE = registerItem("rubber_ring_blue", () -> new TrinketsArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_blue.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_PINK = registerItem("rubber_ring_pink", () -> new TrinketsArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_pink.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_STRIPPED = registerItem("rubber_ring_stripped", () -> new TrinketsArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/rubber_ring_stripped.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_PELICAN = registerItem("rubber_ring_pelican", () -> new TrinketsArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.RARE), new BeachpartyIdentifier("textures/models/armor/rubber_ring_pelican.png")));
    public static final RegistrySupplier<Item> RUBBER_RING_AXOLOTL = registerItem("rubber_ring_axolotl", () -> new TrinketsArmorItem(ArmorMaterialRegistry.RING, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.RARE), new BeachpartyIdentifier("textures/models/armor/rubber_ring_axolotl.png")));
    public static final RegistrySupplier<Item> POOL_NOODLE = registerItem("pool_noodle", () -> new PoolNoodleItem(Tiers.WOOD, 0, -1.4F, getSettings()));
    public static final RegistrySupplier<Item> TRUNKS = registerItem("trunks", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.TRUNKS, ArmorItem.Type.LEGGINGS, 16715535, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/trunks.png")));
    public static final RegistrySupplier<Item> BIKINI = registerItem("bikini", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.BIKINI, ArmorItem.Type.CHESTPLATE, 987135, getSettings().rarity(Rarity.UNCOMMON), new BeachpartyIdentifier("textures/models/armor/bikini.png")));
    public static final RegistrySupplier<Item> CROCS = registerItem("crocs", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.CROCS, ArmorItem.Type.BOOTS, 1048335, getSettings().rarity(Rarity.EPIC), new BeachpartyIdentifier("textures/models/armor/crocs.png")));
    public static final RegistrySupplier<Item> SWIM_WINGS = registerItem("swim_wings", () -> new DyeableBeachpartyArmorItem(ArmorMaterialRegistry.SWIM_WINGS, ArmorItem.Type.CHESTPLATE, 0xFF5800, getSettings(), new BeachpartyIdentifier("textures/models/armor/swim_wings.png")));
    public static final RegistrySupplier<Block> WET_HAY_BLOCK = registerWithItem("wet_hay_block", () -> new WetHayBaleBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistrySupplier<Block> THATCH = registerWithItem("thatch", () -> new HayBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)));
    public static final RegistrySupplier<Block> THATCH_STAIRS = registerWithItem("thatch_stairs", () -> new StairBlock(THATCH.get().defaultBlockState(), BlockBehaviour.Properties.copy(THATCH.get()).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> THATCH_SLAB = registerWithItem("thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).strength(2.0F).sound(SoundType.WOOD).explosionResistance(3.0F)));
    public static final RegistrySupplier<Block> PALM_LEAVES = registerWithItem("palm_leaves", () -> new PalmLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final Supplier<SaplingBlock> PALM_SPROUT = registerWithItem("palm_sprout", PalmSproutBlock::new);
    public static final RegistrySupplier<Block> STRIPPED_PALM_LOG = registerWithItem("stripped_palm_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_PALM_WOOD = registerWithItem("stripped_palm_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> PALM_LOG = registerWithItem("palm_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> PALM_WOOD = registerWithItem("palm_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> PALM_PLANKS = registerWithItem("palm_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> PALM_STAIRS = registerWithItem("palm_stairs", () -> new StairBlock(PALM_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PALM_PLANKS.get())));
    public static final RegistrySupplier<Block> PALM_SLAB = registerWithItem("palm_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0F).sound(SoundType.WOOD).explosionResistance(3.0F)));
    public static final RegistrySupplier<Block> PALM_FENCE = registerWithItem("palm_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistrySupplier<Block> PALM_FENCE_GATE = registerWithItem("palm_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE), WoodType.BAMBOO));
    public static final RegistrySupplier<Block> PALM_BUTTON = registerWithItem("palm_button", () -> woodenButton(FeatureFlags.VANILLA));
    public static final RegistrySupplier<Block> PALM_PRESSURE_PLATE = registerWithItem("palm_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.BAMBOO));
    public static final RegistrySupplier<Block> PALM_DOOR = registerWithItem("palm_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.BAMBOO));
    public static final RegistrySupplier<Block> PALM_TRAPDOOR = registerWithItem("palm_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), BlockSetType.BAMBOO));
    public static final RegistrySupplier<Block> PALM_TABLE = registerWithItem("palm_table", () -> new PalmTableBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> PALM_BAR = registerWithItem("palm_bar", () -> new PalmBarBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> PALM_CABINET = registerWithItem("palm_cabinet", () -> new PalmCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS), () -> SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, () -> SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE));
    public static final RegistrySupplier<Block> PALM_CHAIR = registerWithItem("palm_chair", () -> new PalmChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BEACH_CHAIR = registerWithItem("beach_chair", () -> new BeachChairBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> HOODED_BEACH_CHAIR = registerWithItem("hooded_beach_chair", () -> new HoodedBeachChair(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> BEACH_SUN_LOUNGER = registerWithItem("beach_sun_lounger", () -> new BeachSunLounger(DyeColor.WHITE, BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).pushReaction(PushReaction.IGNORE).instabreak().mapColor(DyeColor.WHITE)));
    public static final RegistrySupplier<Block> PALM_BAR_STOOL = registerWithItem("palm_bar_stool", () -> new PalmBarStoolBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> BEACH_PARASOL = registerWithItem("beach_parasol", () -> new BeachParasolBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> BEACH_TOWEL = registerWithItem("beach_towel", () -> new BeachTowelBlock(DyeColor.WHITE, BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak().mapColor(DyeColor.WHITE)));
    public static final RegistrySupplier<Block> MINI_FRIDGE = registerWithItem("mini_fridge", () -> new MiniFridgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.COPPER)));
    public static final RegistrySupplier<Block> RADIO = registerWithItem("radio", () -> new RadioBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> MESSAGE_IN_A_BOTTLE = registerWithoutItem("message_in_a_bottle", () -> new MessageInABottleBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), Block.box(4.0f, 0.0f, 4.0f, 12.0f, 6.0f, 12.0f)));
    public static final RegistrySupplier<Item> MESSAGE_IN_A_BOTTLE_ITEM = registerItem("message_in_a_bottle", () -> new MessageInABottleItem(ObjectRegistry.MESSAGE_IN_A_BOTTLE.get(), getSettings()));
    public static final RegistrySupplier<Block> SEASHELL_BLOCK = registerWithoutItem("seashell_block", () -> new SeashellBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).instabreak().noParticlesOnBreak()));
    public static final RegistrySupplier<Item> SEASHELL = registerItem("seashell", () -> new SeashellItem(SEASHELL_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Block> SAND_BUCKET_BLOCK_FILLED = registerWithoutItem("sand_bucket_block_filled", () -> new SandBucketBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT)));
    public static final RegistrySupplier<Item> SAND_BUCKET_FILLED = registerItem("sand_bucket_filled", () -> new SandBucketItem(SAND_BUCKET_BLOCK_FILLED.get(), getSettings().stacksTo(1)));
    public static final RegistrySupplier<Block> SAND_BUCKET_BLOCK_EMPTY = registerWithoutItem("sand_bucket_block_empty", () -> new SandBucketBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_WART)));
    public static final RegistrySupplier<Item> SAND_BUCKET_EMPTY = registerItem("sand_bucket_empty", () -> new SandBucketItem(SAND_BUCKET_BLOCK_EMPTY.get(), getSettings()));
    public static final RegistrySupplier<Block> SANDCASTLE = registerWithoutItem("sandcastle", () -> new SandBucketBlock.SandCastleBlock(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final RegistrySupplier<Block> SAND_PILE = registerWithoutItem("sand_pile", () -> new SandBucketBlock.SandPileBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.SAND)));
    public static final RegistrySupplier<Item> COCONUT = registerItem("coconut", () -> new CoconutItem(getSettings()));
    public static final RegistrySupplier<Block> COCONUT_COCKTAIL = registerCocktail("coconut_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.DAMAGE_BOOST);
    public static final RegistrySupplier<Block> SWEETBERRIES_COCKTAIL = registerCocktail("sweetberries_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.ABSORPTION);
    public static final RegistrySupplier<Block> COCOA_COCKTAIL = registerCocktail("cocoa_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.REGENERATION);
    public static final RegistrySupplier<Block> PUMPKIN_COCKTAIL = registerCocktail("pumpkin_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.FIRE_RESISTANCE);
    public static final RegistrySupplier<Block> HONEY_COCKTAIL = registerCocktail("honey_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.DIG_SPEED);
    public static final RegistrySupplier<Block> MELON_COCKTAIL = registerCocktail("melon_cocktail", () -> new CocktailBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak()), MobEffects.LUCK);
    public static final RegistrySupplier<Item> BEACH_BALL = registerItem("beach_ball", () -> new BeachBallItem(new Item.Properties()));
    public static final RegistrySupplier<Block> BEACH_GOAL = registerWithItem("beach_goal", () -> new BeachGoalBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)));
    public static final RegistrySupplier<Block> PALM_TORCH = registerWithoutItem("palm_torch", () -> new TorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistrySupplier<Block> PALM_WALL_TORCH = registerWithoutItem("palm_wall_torch", () -> new WallTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD).dropsLike(PALM_TORCH.get()), ParticleTypes.FLAME));
    public static final RegistrySupplier<Item> PALM_TORCH_ITEM = registerItem("palm_torch_item", () -> new StandingAndWallBlockItem(ObjectRegistry.PALM_TORCH.get(), ObjectRegistry.PALM_WALL_TORCH.get(), getSettings(), Direction.DOWN));
    public static final RegistrySupplier<Block> TALL_PALM_TORCH = registerWithItem("tall_palm_torch", () -> new TallPalmTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH).noCollission().instabreak().lightLevel((state) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME));
    public static final RegistrySupplier<Block> HANGING_COCONUT = registerWithoutItem("hanging_coconut", () -> new HangingCoconutBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO)));
    public static final RegistrySupplier<Block> SANDWAVES = registerWithItem("sandwaves", () -> new SandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistrySupplier<Block> PALM_SIGN = registerWithoutItem("palm_sign", () -> new PalmStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), BeachpartyWoodType.PALM));
    public static final RegistrySupplier<Block> PALM_WALL_SIGN = registerWithoutItem("palm_wall_sign", () -> new PalmWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN), BeachpartyWoodType.PALM));
    public static final RegistrySupplier<Block> PALM_HANGING_SIGN = registerWithoutItem("palm_hanging_sign", () -> new PalmCeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), BeachpartyWoodType.PALM));
    public static final RegistrySupplier<Block> PALM_WALL_HANGING_SIGN = registerWithoutItem("palm_wall_hanging_sign", () -> new PalmWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), BeachpartyWoodType.PALM));
    public static final RegistrySupplier<Item> PALM_SIGN_ITEM = ITEMS.register("palm_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ObjectRegistry.PALM_SIGN.get(), ObjectRegistry.PALM_WALL_SIGN.get()));
    public static final RegistrySupplier<Item> PALM_HANGING_SIGN_ITEM = ITEMS.register("palm_hanging_sign", () -> new HangingSignItem(ObjectRegistry.PALM_HANGING_SIGN.get(), ObjectRegistry.PALM_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> PALM_BOAT = ITEMS.register("palm_boat", () -> new PalmBoatItem(false, PalmBoatEntity.Type.PALM, new Item.Properties()));
    public static final RegistrySupplier<Item> PALM_CHEST_BOAT = ITEMS.register("palm_chest_boat", () -> new PalmBoatItem(true, PalmBoatEntity.Type.PALM, new Item.Properties()));
    public static final RegistrySupplier<Item> FLOATY_BOAT = ITEMS.register("floaty_boat", () -> new PalmBoatItem(false, PalmBoatEntity.Type.FLOATY, new Item.Properties()));
    public static final RegistrySupplier<Item> FLOATY_CHEST_BOAT = ITEMS.register("floaty_chest_boat", () -> new PalmBoatItem(true, PalmBoatEntity.Type.FLOATY, new Item.Properties()));

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

    private static ButtonBlock woodenButton(FeatureFlag... featureFlags) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
        if (featureFlags.length > 0) {
            properties = properties.requiredFeatures(featureFlags);
        }

        return new ButtonBlock(properties, BlockSetType.BAMBOO, 30, true);
    }

    private static <T extends Block> RegistrySupplier<T> registerCocktail(String name, Supplier<T> block, MobEffect effect) {
        RegistrySupplier<T> toReturn = registerWithoutItem(name, block);
        registerItem(name, () -> new DrinkBlockItem(
                toReturn.get(),
                getSettings(settings -> settings.food(cocktailFoodComponent(effect)))
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

