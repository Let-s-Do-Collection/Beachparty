package net.satisfy.beachparty.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.fabric.core.config.BeachpartyFabricConfig;

import java.util.function.Supplier;

public class PlatformHelperImpl {
    private static BeachpartyFabricConfig config() {
        return AutoConfig.getConfigHolder(BeachpartyFabricConfig.class).getConfig();
    }

    public static boolean isPalmTreeGeneration() {
        return config().worldGeneration.enablePalmTrees;
    }

    public static boolean isSeashellGeneration() {
        return config().worldGeneration.enableSeashells;
    }

    public static boolean isSandwaveGeneration() {
        return config().worldGeneration.enableSandwaves;
    }

    public static boolean isMessageInABottleEnabled() {
        return config().beach.enableMessageInABottle;
    }

    public static int getMessageInABottleMaxCount() {
        return config().beach.messageInABottleMaxCount;
    }

    public static int getMessageInABottleInterval() {
        return config().beach.messageInABottleInterval;
    }

    public static boolean isSandcastleTrampling() {
        return config().beach.enableSandcastleTrampling;
    }

    public static float getParasolFireProtection() {
        return config().beach.parasolFireProtection / 100.0f;
    }

    public static float getBeachHatFireProtection() {
        return config().wearables.beachHatFireProtection / 100.0f;
    }

    public static float getSunglassesFireProtection() {
        return config().wearables.sunglassesFireProtection / 100.0f;
    }

    public static float getSwimWingsFallProtection() {
        return config().wearables.swimWingsFallProtection / 100.0f;
    }

    public static double getSwimSuitSpeedMultiplier() {
        return config().wearables.swimSuitSpeedMultiplier;
    }

    public static double getRubberRingSpeedMultiplier() {
        return config().wearables.rubberRingSpeedMultiplier;
    }

    public static boolean showMiniFridgeInfo() {
        return config().infoTooltips.showMiniFridgeInfo;
    }

    public static boolean showPalmBarInfo() {
        return config().infoTooltips.showPalmBarInfo;
    }

    public static boolean showRadioInfo() {
        return config().infoTooltips.showRadioInfo;
    }

    public static boolean showSunLoungerInfo() {
        return config().infoTooltips.showSunLoungerInfo;
    }

    public static boolean showWetHayBaleInfo() {
        return config().infoTooltips.showWetHayBaleInfo;
    }

    public static int getBannerEffectRadius() {
        return config().effects.completionistBannerEffect.bannerEffectRadius;
    }

    public static int getBannerEffectAmplifier() {
        return config().effects.completionistBannerEffect.bannerEffectAmplifier;
    }

    public static int getCoconutOpenNutrition() {
        return config().food.coconutOpenNutrition;
    }

    public static float getCoconutOpenSaturation() {
        return config().food.coconutOpenSaturation;
    }

    public static int getRawMusselMeatNutrition() {
        return config().food.rawMusselMeatNutrition;
    }

    public static float getRawMusselMeatSaturation() {
        return config().food.rawMusselMeatSaturation;
    }

    public static int getCookedMusselMeatNutrition() {
        return config().food.cookedMusselMeatNutrition;
    }

    public static float getCookedMusselMeatSaturation() {
        return config().food.cookedMusselMeatSaturation;
    }

    public static int getCoconutCocktailNutrition() {
        return config().food.coconutCocktailNutrition;
    }

    public static float getCoconutCocktailSaturation() {
        return config().food.coconutCocktailSaturation;
    }

    public static int getCoconutCocktailEffectDuration() {
        return config().food.coconutCocktailEffectDuration;
    }

    public static int getSweetberriesCocktailNutrition() {
        return config().food.sweetberriesCocktailNutrition;
    }

    public static float getSweetberriesCocktailSaturation() {
        return config().food.sweetberriesCocktailSaturation;
    }

    public static int getSweetberriesCocktailEffectDuration() {
        return config().food.sweetberriesCocktailEffectDuration;
    }

    public static int getCocoaCocktailNutrition() {
        return config().food.cocoaCocktailNutrition;
    }

    public static float getCocoaCocktailSaturation() {
        return config().food.cocoaCocktailSaturation;
    }

    public static int getCocoaCocktailEffectDuration() {
        return config().food.cocoaCocktailEffectDuration;
    }

    public static int getPumpkinCocktailNutrition() {
        return config().food.pumpkinCocktailNutrition;
    }

    public static float getPumpkinCocktailSaturation() {
        return config().food.pumpkinCocktailSaturation;
    }

    public static int getPumpkinCocktailEffectDuration() {
        return config().food.pumpkinCocktailEffectDuration;
    }

    public static int getHoneyCocktailNutrition() {
        return config().food.honeyCocktailNutrition;
    }

    public static float getHoneyCocktailSaturation() {
        return config().food.honeyCocktailSaturation;
    }

    public static int getHoneyCocktailEffectDuration() {
        return config().food.honeyCocktailEffectDuration;
    }

    public static int getMelonCocktailNutrition() {
        return config().food.melonCocktailNutrition;
    }

    public static float getMelonCocktailSaturation() {
        return config().food.melonCocktailSaturation;
    }

    public static int getMelonCocktailEffectDuration() {
        return config().food.melonCocktailEffectDuration;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, BeachpartyIdentifier.identifier(name), FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.scalable(width, height)).trackRangeChunks(clientTrackingRange).build());
        return () -> registry;
    }
}
