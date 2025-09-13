package net.satisfy.beachparty.neoforge.core.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.satisfy.beachparty.Beachparty;

public final class BeachpartyNeoForgeConfig {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final ModConfigSpec.BooleanValue SPAWN_PALMS;
    public static final ModConfigSpec.BooleanValue SPAWN_SEASHELLS;
    public static final ModConfigSpec.BooleanValue SPAWN_SANDWAVES;
    public static final ModConfigSpec.BooleanValue ALLOW_BOTTLE_SPAWNING;
    public static final ModConfigSpec.IntValue BOTTLE_MAX_COUNT;
    public static final ModConfigSpec.IntValue BOTTLE_SPAWN_INTERVAL;

    private static boolean spawnPalms;
    private static boolean spawnSeashells;
    private static boolean spawnSandwaves;
    private static boolean allowBottleSpawning;
    private static int bottleMaxCount;
    private static int bottleSpawnInterval;

    static {
        final ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        SPAWN_PALMS = b.define("spawnPalms", true);
        SPAWN_SEASHELLS = b.define("spawnSeashells", true);
        SPAWN_SANDWAVES = b.define("spawnSandwaves", true);

        b.push("messageInABottle");
        ALLOW_BOTTLE_SPAWNING = b.define("allowSpawning", true);
        BOTTLE_MAX_COUNT = b.defineInRange("maxCount", 2, 1, Integer.MAX_VALUE);
        BOTTLE_SPAWN_INTERVAL = b.defineInRange("spawnInterval", 6000, 20, Integer.MAX_VALUE);
        b.pop();

        COMMON_CONFIG = b.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading e) {
        if (e.getConfig().getSpec() == COMMON_CONFIG) sync();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading e) {
        if (e.getConfig().getSpec() == COMMON_CONFIG) sync();
    }

    private static void sync() {
        spawnPalms = SPAWN_PALMS.get();
        spawnSeashells = SPAWN_SEASHELLS.get();
        spawnSandwaves = SPAWN_SANDWAVES.get();
        allowBottleSpawning = ALLOW_BOTTLE_SPAWNING.get();
        bottleMaxCount = BOTTLE_MAX_COUNT.get();
        bottleSpawnInterval = BOTTLE_SPAWN_INTERVAL.get();
    }

    public static boolean spawnPalms() { return spawnPalms; }
    public static boolean spawnSeashells() { return spawnSeashells; }
    public static boolean spawnSandwaves() { return spawnSandwaves; }
    public static boolean allowBottleSpawning() { return allowBottleSpawning; }
    public static int bottleMaxCount() { return bottleMaxCount; }
    public static int bottleSpawnInterval() { return bottleSpawnInterval; }
}
