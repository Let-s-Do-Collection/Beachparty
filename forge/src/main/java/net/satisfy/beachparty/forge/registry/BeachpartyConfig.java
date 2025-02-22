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
    public static final ForgeConfigSpec.BooleanValue SPAWN_MESSAGE_IN_A_BOTTLE;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        SPAWN_PALMS = COMMON_BUILDER.comment("Generate Palm Trees").define("spawnPalms", true);
        SPAWN_SEASHELLS = COMMON_BUILDER.comment("Generate Seashells").define("spawnSeashells", true);
        SPAWN_SANDWAVES = COMMON_BUILDER.comment("Generate Sandwaves").define("spawnSandwaves", true);
        SPAWN_MESSAGE_IN_A_BOTTLE = COMMON_BUILDER.comment("Generate Message in a Bottle").define("spawnMessageInABottle", true);

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
