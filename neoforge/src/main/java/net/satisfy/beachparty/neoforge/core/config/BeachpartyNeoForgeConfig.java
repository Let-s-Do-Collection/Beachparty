package net.satisfy.beachparty.neoforge.core.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class BeachpartyNeoForgeConfig {
    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec STARTUP_CONFIG;

    public static final ModConfigSpec.BooleanValue ENABLE_PALM_TREES;
    public static final ModConfigSpec.BooleanValue ENABLE_SEASHELLS;
    public static final ModConfigSpec.BooleanValue ENABLE_SANDWAVES;
    public static final ModConfigSpec.BooleanValue ENABLE_MESSAGE_IN_A_BOTTLE;
    public static final ModConfigSpec.IntValue MESSAGE_IN_A_BOTTLE_MAX_COUNT;
    public static final ModConfigSpec.IntValue MESSAGE_IN_A_BOTTLE_INTERVAL;
    public static final ModConfigSpec.BooleanValue ENABLE_SANDCASTLE_TRAMPLING;
    public static final ModConfigSpec.IntValue PARASOL_FIRE_PROTECTION;
    public static final ModConfigSpec.IntValue BEACH_HAT_FIRE_PROTECTION;
    public static final ModConfigSpec.IntValue SUNGLASSES_FIRE_PROTECTION;
    public static final ModConfigSpec.IntValue SWIM_WINGS_FALL_PROTECTION;
    public static final ModConfigSpec.DoubleValue SWIM_SUIT_SPEED_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue RUBBER_RING_SPEED_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue SHOW_MINI_FRIDGE_INFO;
    public static final ModConfigSpec.BooleanValue SHOW_PALM_BAR_INFO;
    public static final ModConfigSpec.BooleanValue SHOW_RADIO_INFO;
    public static final ModConfigSpec.BooleanValue SHOW_SUN_LOUNGER_INFO;
    public static final ModConfigSpec.BooleanValue SHOW_WET_HAY_BALE_INFO;
    public static final ModConfigSpec.IntValue BANNER_EFFECT_RADIUS;
    public static final ModConfigSpec.IntValue BANNER_EFFECT_AMPLIFIER;

    public static final ModConfigSpec.IntValue COCONUT_OPEN_NUTRITION;
    public static final ModConfigSpec.DoubleValue COCONUT_OPEN_SATURATION;
    public static final ModConfigSpec.IntValue RAW_MUSSEL_MEAT_NUTRITION;
    public static final ModConfigSpec.DoubleValue RAW_MUSSEL_MEAT_SATURATION;
    public static final ModConfigSpec.IntValue COOKED_MUSSEL_MEAT_NUTRITION;
    public static final ModConfigSpec.DoubleValue COOKED_MUSSEL_MEAT_SATURATION;
    public static final ModConfigSpec.IntValue COCONUT_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue COCONUT_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue COCONUT_COCKTAIL_EFFECT_DURATION;
    public static final ModConfigSpec.IntValue SWEETBERRIES_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue SWEETBERRIES_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue SWEETBERRIES_COCKTAIL_EFFECT_DURATION;
    public static final ModConfigSpec.IntValue COCOA_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue COCOA_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue COCOA_COCKTAIL_EFFECT_DURATION;
    public static final ModConfigSpec.IntValue PUMPKIN_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue PUMPKIN_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue PUMPKIN_COCKTAIL_EFFECT_DURATION;
    public static final ModConfigSpec.IntValue HONEY_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue HONEY_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue HONEY_COCKTAIL_EFFECT_DURATION;
    public static final ModConfigSpec.IntValue MELON_COCKTAIL_NUTRITION;
    public static final ModConfigSpec.DoubleValue MELON_COCKTAIL_SATURATION;
    public static final ModConfigSpec.IntValue MELON_COCKTAIL_EFFECT_DURATION;

    public static boolean enablePalmTrees;
    public static boolean enableSeashells;
    public static boolean enableSandwaves;
    public static boolean enableMessageInABottle;
    public static int messageInABottleMaxCount;
    public static int messageInABottleInterval;
    public static boolean enableSandcastleTrampling;
    public static int parasolFireProtection;
    public static int beachHatFireProtection;
    public static int sunglassesFireProtection;
    public static int swimWingsFallProtection;
    public static double swimSuitSpeedMultiplier;
    public static double rubberRingSpeedMultiplier;
    public static boolean showMiniFridgeInfo;
    public static boolean showPalmBarInfo;
    public static boolean showRadioInfo;
    public static boolean showSunLoungerInfo;
    public static boolean showWetHayBaleInfo;
    public static int bannerEffectRadius;
    public static int bannerEffectAmplifier;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("What generates on beaches in new chunks.").push("World Generation");
        ENABLE_PALM_TREES = builder.comment("Palm trees generate on beaches.").define("enablePalmTrees", true);
        ENABLE_SEASHELLS = builder.comment("Seashells are scattered across beaches.").define("enableSeashells", true);
        ENABLE_SANDWAVES = builder.comment("Sandwave patterns appear on beaches.").define("enableSandwaves", true);
        builder.pop();

        builder.comment("Everything that happens on the beach.").push("Beach");
        ENABLE_MESSAGE_IN_A_BOTTLE = builder.comment("Messages in a Bottle wash up on beaches.").define("enableMessageInABottle", true);
        MESSAGE_IN_A_BOTTLE_MAX_COUNT = builder.comment("How many Messages in a Bottle can lie around near a player at once.").defineInRange("messageInABottleMaxCount", 2, 1, 64);
        MESSAGE_IN_A_BOTTLE_INTERVAL = builder.comment("Every how many ticks a new Message in a Bottle may wash up. 20 ticks = 1 second.").defineInRange("messageInABottleInterval", 6000, 20, 72000);
        ENABLE_SANDCASTLE_TRAMPLING = builder.comment("Sandcastles crumble into sand when something steps on them. Petrified sandcastles stay.").define("enableSandcastleTrampling", true);
        PARASOL_FIRE_PROTECTION = builder.comment("Fire damage in percent that a Beach Parasol nearby takes away.").defineInRange("parasolFireProtection", 4, 0, 100);
        builder.pop();

        builder.comment("What the beach clothes and swim gear do while worn.").push("Wearables");
        BEACH_HAT_FIRE_PROTECTION = builder.comment("Fire damage in percent that the Beach Hat takes away.").defineInRange("beachHatFireProtection", 8, 0, 100);
        SUNGLASSES_FIRE_PROTECTION = builder.comment("Fire damage in percent that Sunglasses take away.").defineInRange("sunglassesFireProtection", 12, 0, 100);
        SWIM_WINGS_FALL_PROTECTION = builder.comment("Fall damage in percent that Swim Wings take away.").defineInRange("swimWingsFallProtection", 50, 0, 100);
        SWIM_SUIT_SPEED_MULTIPLIER = builder.comment("Swimming speed while wearing Trunks or a Bikini. 1.0 = normal speed.").defineInRange("swimSuitSpeedMultiplier", 1.08, 0.5, 2.0);
        RUBBER_RING_SPEED_MULTIPLIER = builder.comment("Swimming speed while wearing a Rubber Ring. 1.0 = normal speed.").defineInRange("rubberRingSpeedMultiplier", 1.17, 0.5, 2.0);
        builder.pop();

        builder.comment("Info tooltips that appear when looking at a block. Only affects your own screen.").push("Info Tooltips");
        SHOW_MINI_FRIDGE_INFO = builder.comment("Shows an info tooltip when looking at the Mini Fridge.").define("showMiniFridgeInfo", true);
        SHOW_PALM_BAR_INFO = builder.comment("Shows an info tooltip when looking at the Palm Bar.").define("showPalmBarInfo", true);
        SHOW_RADIO_INFO = builder.comment("Shows an info tooltip when looking at the Radio.").define("showRadioInfo", true);
        SHOW_SUN_LOUNGER_INFO = builder.comment("Shows an info tooltip when looking at a Sun Lounger.").define("showSunLoungerInfo", true);
        SHOW_WET_HAY_BALE_INFO = builder.comment("Shows an info tooltip when looking at a Wet Hay Block.").define("showWetHayBaleInfo", true);
        builder.pop();

        builder.comment("Fine-tuning of the effects.").push("Effects");
        builder.comment("The Completionist Banner grants Water Breathing to players nearby.").push("Completionist Banner Effect");
        BANNER_EFFECT_RADIUS = builder.comment("Radius in blocks in which the Completionist Banner grants Water Breathing.").defineInRange("bannerEffectRadius", 6, 1, 32);
        BANNER_EFFECT_AMPLIFIER = builder.comment("Water Breathing level of the Completionist Banner. 0 = Water Breathing I.").defineInRange("bannerEffectAmplifier", 1, 0, 4);
        builder.pop();
        builder.pop();

        COMMON_CONFIG = builder.build();

        ModConfigSpec.Builder startup = new ModConfigSpec.Builder();

        startup.comment("Hunger, saturation and effect duration in ticks of the food and drinks. Changes need a game restart.").push("Food");
        COCONUT_OPEN_NUTRITION = nutrition(startup, "coconutOpen", "an Open Coconut", 3);
        COCONUT_OPEN_SATURATION = saturation(startup, "coconutOpen", "an Open Coconut", 0.6);
        RAW_MUSSEL_MEAT_NUTRITION = nutrition(startup, "rawMusselMeat", "Raw Mussel Meat", 1);
        RAW_MUSSEL_MEAT_SATURATION = saturation(startup, "rawMusselMeat", "Raw Mussel Meat", 0.3);
        COOKED_MUSSEL_MEAT_NUTRITION = nutrition(startup, "cookedMusselMeat", "Cooked Mussel Meat", 5);
        COOKED_MUSSEL_MEAT_SATURATION = saturation(startup, "cookedMusselMeat", "Cooked Mussel Meat", 0.6);
        COCONUT_COCKTAIL_NUTRITION = nutrition(startup, "coconutCocktail", "a Coconut Cocktail", 1);
        COCONUT_COCKTAIL_SATURATION = saturation(startup, "coconutCocktail", "a Coconut Cocktail", 1.0);
        COCONUT_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "coconutCocktail", "a Coconut Cocktail");
        SWEETBERRIES_COCKTAIL_NUTRITION = nutrition(startup, "sweetberriesCocktail", "a Sweetberry Cocktail", 1);
        SWEETBERRIES_COCKTAIL_SATURATION = saturation(startup, "sweetberriesCocktail", "a Sweetberry Cocktail", 1.0);
        SWEETBERRIES_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "sweetberriesCocktail", "a Sweetberry Cocktail");
        COCOA_COCKTAIL_NUTRITION = nutrition(startup, "cocoaCocktail", "a Cocoa Cocktail", 1);
        COCOA_COCKTAIL_SATURATION = saturation(startup, "cocoaCocktail", "a Cocoa Cocktail", 1.0);
        COCOA_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "cocoaCocktail", "a Cocoa Cocktail");
        PUMPKIN_COCKTAIL_NUTRITION = nutrition(startup, "pumpkinCocktail", "a Pumpkin Cocktail", 1);
        PUMPKIN_COCKTAIL_SATURATION = saturation(startup, "pumpkinCocktail", "a Pumpkin Cocktail", 1.0);
        PUMPKIN_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "pumpkinCocktail", "a Pumpkin Cocktail");
        HONEY_COCKTAIL_NUTRITION = nutrition(startup, "honeyCocktail", "a Honey Cocktail", 1);
        HONEY_COCKTAIL_SATURATION = saturation(startup, "honeyCocktail", "a Honey Cocktail", 1.0);
        HONEY_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "honeyCocktail", "a Honey Cocktail");
        MELON_COCKTAIL_NUTRITION = nutrition(startup, "melonCocktail", "a Melon Cocktail", 1);
        MELON_COCKTAIL_SATURATION = saturation(startup, "melonCocktail", "a Melon Cocktail", 1.0);
        MELON_COCKTAIL_EFFECT_DURATION = effectDuration(startup, "melonCocktail", "a Melon Cocktail");
        startup.pop();

        STARTUP_CONFIG = startup.build();
    }

    private BeachpartyNeoForgeConfig() {
    }

    private static ModConfigSpec.IntValue nutrition(ModConfigSpec.Builder builder, String key, String name, int value) {
        return builder.comment("Hunger points restored by " + name + ". 2 points = 1 drumstick.").defineInRange(key + "Nutrition", value, 0, 20);
    }

    private static ModConfigSpec.DoubleValue saturation(ModConfigSpec.Builder builder, String key, String name, double value) {
        return builder.comment("Saturation given by " + name + ", multiplied with its hunger points.").defineInRange(key + "Saturation", value, 0.0, 2.0);
    }

    private static ModConfigSpec.IntValue effectDuration(ModConfigSpec.Builder builder, String key, String name) {
        return builder.comment("How long the effect of " + name + " lasts in ticks. 20 ticks = 1 second.").defineInRange(key + "EffectDuration", 900, 0, 72000);
    }

    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            sync();
        }
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            sync();
        }
    }

    private static void sync() {
        enablePalmTrees = ENABLE_PALM_TREES.get();
        enableSeashells = ENABLE_SEASHELLS.get();
        enableSandwaves = ENABLE_SANDWAVES.get();
        enableMessageInABottle = ENABLE_MESSAGE_IN_A_BOTTLE.get();
        messageInABottleMaxCount = MESSAGE_IN_A_BOTTLE_MAX_COUNT.get();
        messageInABottleInterval = MESSAGE_IN_A_BOTTLE_INTERVAL.get();
        enableSandcastleTrampling = ENABLE_SANDCASTLE_TRAMPLING.get();
        parasolFireProtection = PARASOL_FIRE_PROTECTION.get();
        beachHatFireProtection = BEACH_HAT_FIRE_PROTECTION.get();
        sunglassesFireProtection = SUNGLASSES_FIRE_PROTECTION.get();
        swimWingsFallProtection = SWIM_WINGS_FALL_PROTECTION.get();
        swimSuitSpeedMultiplier = SWIM_SUIT_SPEED_MULTIPLIER.get();
        rubberRingSpeedMultiplier = RUBBER_RING_SPEED_MULTIPLIER.get();
        showMiniFridgeInfo = SHOW_MINI_FRIDGE_INFO.get();
        showPalmBarInfo = SHOW_PALM_BAR_INFO.get();
        showRadioInfo = SHOW_RADIO_INFO.get();
        showSunLoungerInfo = SHOW_SUN_LOUNGER_INFO.get();
        showWetHayBaleInfo = SHOW_WET_HAY_BALE_INFO.get();
        bannerEffectRadius = BANNER_EFFECT_RADIUS.get();
        bannerEffectAmplifier = BANNER_EFFECT_AMPLIFIER.get();
    }
}
