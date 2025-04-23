package net.satisfy.beachparty.forge.registry;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.io.File;

public class BeachpartyConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue SPAWN_PALMS;
    public static final ForgeConfigSpec.BooleanValue SPAWN_SEASHELLS;
    public static final ForgeConfigSpec.BooleanValue SPAWN_SANDWAVES;
    public static final ForgeConfigSpec.BooleanValue ALLOW_BOTTLE_SPAWNING;
    public static final ForgeConfigSpec.IntValue BOTTLE_MAX_COUNT;
    public static final ForgeConfigSpec.IntValue BOTTLE_SPAWN_INTERVAL;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        SPAWN_PALMS = COMMON_BUILDER.comment("Generate Palm Trees").define("spawnPalms", true);
        SPAWN_SEASHELLS = COMMON_BUILDER.comment("Generate Seashells").define("spawnSeashells", true);
        SPAWN_SANDWAVES = COMMON_BUILDER.comment("Generate Sandwaves").define("spawnSandwaves", true);
        ALLOW_BOTTLE_SPAWNING = COMMON_BUILDER.comment("Allow spawning of Message in a Bottle").define("messageInABottle.allowSpawning", true);
        BOTTLE_MAX_COUNT = COMMON_BUILDER.comment("Maximum number of bottles that can exist simultaneously").defineInRange("messageInABottle.maxCount", 2, 1, Integer.MAX_VALUE);
        BOTTLE_SPAWN_INTERVAL = COMMON_BUILDER.comment("Interval in ticks between bottle spawn attempts").defineInRange("messageInABottle.spawnInterval", 6000, 20, Integer.MAX_VALUE);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
    }

    public static void loadConfig(ForgeConfigSpec spec, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .preserveInsertionOrder()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        spec.setConfig(file);
        file.save();
    }
}
