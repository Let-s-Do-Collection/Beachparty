package net.satisfy.beachparty.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "beachparty")
@Config.Gui.Background("beachparty:textures/block/sandwaves.png")
public class BeachpartyFabricConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public WorldGenerationSettings worldGeneration = new WorldGenerationSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public BeachSettings beach = new BeachSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public WearableSettings wearables = new WearableSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InfoTooltipSettings infoTooltips = new InfoTooltipSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public EffectsSettings effects = new EffectsSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public FoodSettings food = new FoodSettings();

    public static class WorldGenerationSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enablePalmTrees = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableSeashells = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableSandwaves = true;
    }

    public static class BeachSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableMessageInABottle = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int messageInABottleMaxCount = 2;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 20, max = 72000)
        public int messageInABottleInterval = 6000;

        @ConfigEntry.Gui.Tooltip
        public boolean enableSandcastleTrampling = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int parasolFireProtection = 4;
    }

    public static class WearableSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int beachHatFireProtection = 8;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int sunglassesFireProtection = 12;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int swimWingsFallProtection = 50;

        @ConfigEntry.Gui.Tooltip
        public double swimSuitSpeedMultiplier = 1.08;

        @ConfigEntry.Gui.Tooltip
        public double rubberRingSpeedMultiplier = 1.17;
    }

    public static class InfoTooltipSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean showMiniFridgeInfo = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showPalmBarInfo = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showRadioInfo = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showSunLoungerInfo = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showWetHayBaleInfo = true;
    }

    public static class EffectsSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public CompletionistBannerEffectSettings completionistBannerEffect = new CompletionistBannerEffectSettings();

        public static class CompletionistBannerEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
            public int bannerEffectRadius = 6;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
            public int bannerEffectAmplifier = 1;
        }
    }

    public static class FoodSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int coconutOpenNutrition = 3;

        @ConfigEntry.Gui.Tooltip
        public float coconutOpenSaturation = 0.6f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int rawMusselMeatNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float rawMusselMeatSaturation = 0.3f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int cookedMusselMeatNutrition = 5;

        @ConfigEntry.Gui.Tooltip
        public float cookedMusselMeatSaturation = 0.6f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int coconutCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float coconutCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int coconutCocktailEffectDuration = 900;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int sweetberriesCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float sweetberriesCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int sweetberriesCocktailEffectDuration = 900;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int cocoaCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float cocoaCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int cocoaCocktailEffectDuration = 900;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int pumpkinCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float pumpkinCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int pumpkinCocktailEffectDuration = 900;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int honeyCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float honeyCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int honeyCocktailEffectDuration = 900;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int melonCocktailNutrition = 1;

        @ConfigEntry.Gui.Tooltip
        public float melonCocktailSaturation = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 72000)
        public int melonCocktailEffectDuration = 900;
    }

    @Override
    public void validatePostLoad() {
        wearables.swimSuitSpeedMultiplier = clamp(wearables.swimSuitSpeedMultiplier, 0.5, 2.0);
        wearables.rubberRingSpeedMultiplier = clamp(wearables.rubberRingSpeedMultiplier, 0.5, 2.0);
        food.coconutOpenSaturation = clamp(food.coconutOpenSaturation);
        food.rawMusselMeatSaturation = clamp(food.rawMusselMeatSaturation);
        food.cookedMusselMeatSaturation = clamp(food.cookedMusselMeatSaturation);
        food.coconutCocktailSaturation = clamp(food.coconutCocktailSaturation);
        food.sweetberriesCocktailSaturation = clamp(food.sweetberriesCocktailSaturation);
        food.cocoaCocktailSaturation = clamp(food.cocoaCocktailSaturation);
        food.pumpkinCocktailSaturation = clamp(food.pumpkinCocktailSaturation);
        food.honeyCocktailSaturation = clamp(food.honeyCocktailSaturation);
        food.melonCocktailSaturation = clamp(food.melonCocktailSaturation);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(float saturation) {
        return Math.max(0.0f, Math.min(2.0f, saturation));
    }
}
