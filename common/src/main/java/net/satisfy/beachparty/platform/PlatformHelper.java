package net.satisfy.beachparty.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PlatformHelper {
    @ExpectPlatform
    public static boolean isPalmTreeGeneration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isSeashellGeneration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isSandwaveGeneration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isMessageInABottleEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMessageInABottleMaxCount() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMessageInABottleInterval() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isSandcastleTrampling() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getParasolFireProtection() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getBeachHatFireProtection() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getSunglassesFireProtection() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getSwimWingsFallProtection() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getSwimSuitSpeedMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getRubberRingSpeedMultiplier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showMiniFridgeInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showPalmBarInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showRadioInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showSunLoungerInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean showWetHayBaleInfo() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getBannerEffectRadius() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getBannerEffectAmplifier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCoconutOpenNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getCoconutOpenSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getRawMusselMeatNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getRawMusselMeatSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCookedMusselMeatNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getCookedMusselMeatSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCoconutCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getCoconutCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCoconutCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getSweetberriesCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getSweetberriesCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getSweetberriesCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCocoaCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getCocoaCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getCocoaCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getPumpkinCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getPumpkinCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getPumpkinCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getHoneyCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getHoneyCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getHoneyCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMelonCocktailNutrition() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getMelonCocktailSaturation() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMelonCocktailEffectDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        throw new AssertionError();
    }
}
