package net.satisfy.beachparty.neoforge.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BeachpartyConfig {
    public static ModConfigSpec COMMON_CONFIG;

    public static final ModConfigSpec.BooleanValue SPAWN_PALMS;
    public static final ModConfigSpec.BooleanValue SPAWN_SEASHELLS;
    public static final ModConfigSpec.BooleanValue SPAWN_SANDWAVES;
    public static final ModConfigSpec.BooleanValue ALLOW_BOTTLE_SPAWNING;
    public static final ModConfigSpec.IntValue BOTTLE_MAX_COUNT;
    public static final ModConfigSpec.IntValue BOTTLE_SPAWN_INTERVAL;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        SPAWN_PALMS = COMMON_BUILDER.comment("Generate Palm Trees").define("spawnPalms", true);
        SPAWN_SEASHELLS = COMMON_BUILDER.comment("Generate Seashells").define("spawnSeashells", true);
        SPAWN_SANDWAVES = COMMON_BUILDER.comment("Generate Sandwaves").define("spawnSandwaves", true);
        ALLOW_BOTTLE_SPAWNING = COMMON_BUILDER.comment("Allow spawning of Message in a Bottle").define("messageInABottle.allowSpawning", true);
        BOTTLE_MAX_COUNT = COMMON_BUILDER.comment("Maximum number of bottles that can exist simultaneously").defineInRange("messageInABottle.maxCount", 2, 1, Integer.MAX_VALUE);
        BOTTLE_SPAWN_INTERVAL = COMMON_BUILDER.comment("Interval in ticks between bottle spawn attempts").defineInRange("messageInABottle.spawnInterval", 6000, 20, Integer.MAX_VALUE);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static boolean spawnPalms;
    private static boolean spawnSeashells;
    private static boolean spawnSandwaves;
    private static boolean allowSpawning;
    private static int maxCount;
    private static int spawnInterval;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        spawnPalms = SPAWN_PALMS.get();
        spawnSeashells = SPAWN_SEASHELLS.get();
        spawnSandwaves = SPAWN_SANDWAVES.get();
        allowSpawning = ALLOW_BOTTLE_SPAWNING.get();
        maxCount = BOTTLE_MAX_COUNT.get();
        spawnInterval = BOTTLE_SPAWN_INTERVAL.get();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
    }
}
