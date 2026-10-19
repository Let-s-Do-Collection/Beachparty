package net.satisfy.beachparty.platform.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.neoforge.core.config.BeachpartyNeoForgeConfig;

import java.util.List;
import java.util.function.Supplier;

public class PlatformHelperImpl {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Beachparty.MOD_ID);

    public static void onUseSeashell(Level world, Player player, LootParams lootParams, ItemStack stack) {
    }

    public static void addSeashellTooltip(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
    }

    public static boolean isPalmTreeGeneration() {
        return BeachpartyNeoForgeConfig.enablePalmTrees;
    }

    public static boolean isSeashellGeneration() {
        return BeachpartyNeoForgeConfig.enableSeashells;
    }

    public static boolean isSandwaveGeneration() {
        return BeachpartyNeoForgeConfig.enableSandwaves;
    }

    public static boolean isMessageInABottleEnabled() {
        return BeachpartyNeoForgeConfig.enableMessageInABottle;
    }

    public static int getMessageInABottleMaxCount() {
        return BeachpartyNeoForgeConfig.messageInABottleMaxCount;
    }

    public static int getMessageInABottleInterval() {
        return BeachpartyNeoForgeConfig.messageInABottleInterval;
    }

    public static boolean isSandcastleTrampling() {
        return BeachpartyNeoForgeConfig.enableSandcastleTrampling;
    }

    public static float getParasolFireProtection() {
        return BeachpartyNeoForgeConfig.parasolFireProtection / 100.0f;
    }

    public static float getBeachHatFireProtection() {
        return BeachpartyNeoForgeConfig.beachHatFireProtection / 100.0f;
    }

    public static float getSunglassesFireProtection() {
        return BeachpartyNeoForgeConfig.sunglassesFireProtection / 100.0f;
    }

    public static float getSwimWingsFallProtection() {
        return BeachpartyNeoForgeConfig.swimWingsFallProtection / 100.0f;
    }

    public static double getSwimSuitSpeedMultiplier() {
        return BeachpartyNeoForgeConfig.swimSuitSpeedMultiplier;
    }

    public static double getRubberRingSpeedMultiplier() {
        return BeachpartyNeoForgeConfig.rubberRingSpeedMultiplier;
    }

    public static boolean showMiniFridgeInfo() {
        return BeachpartyNeoForgeConfig.showMiniFridgeInfo;
    }

    public static boolean showPalmBarInfo() {
        return BeachpartyNeoForgeConfig.showPalmBarInfo;
    }

    public static boolean showRadioInfo() {
        return BeachpartyNeoForgeConfig.showRadioInfo;
    }

    public static boolean showSunLoungerInfo() {
        return BeachpartyNeoForgeConfig.showSunLoungerInfo;
    }

    public static boolean showWetHayBaleInfo() {
        return BeachpartyNeoForgeConfig.showWetHayBaleInfo;
    }

    public static int getBannerEffectRadius() {
        return BeachpartyNeoForgeConfig.bannerEffectRadius;
    }

    public static int getBannerEffectAmplifier() {
        return BeachpartyNeoForgeConfig.bannerEffectAmplifier;
    }

    public static int getCoconutOpenNutrition() {
        return BeachpartyNeoForgeConfig.COCONUT_OPEN_NUTRITION.get();
    }

    public static float getCoconutOpenSaturation() {
        return BeachpartyNeoForgeConfig.COCONUT_OPEN_SATURATION.get().floatValue();
    }

    public static int getRawMusselMeatNutrition() {
        return BeachpartyNeoForgeConfig.RAW_MUSSEL_MEAT_NUTRITION.get();
    }

    public static float getRawMusselMeatSaturation() {
        return BeachpartyNeoForgeConfig.RAW_MUSSEL_MEAT_SATURATION.get().floatValue();
    }

    public static int getCookedMusselMeatNutrition() {
        return BeachpartyNeoForgeConfig.COOKED_MUSSEL_MEAT_NUTRITION.get();
    }

    public static float getCookedMusselMeatSaturation() {
        return BeachpartyNeoForgeConfig.COOKED_MUSSEL_MEAT_SATURATION.get().floatValue();
    }

    public static int getCoconutCocktailNutrition() {
        return BeachpartyNeoForgeConfig.COCONUT_COCKTAIL_NUTRITION.get();
    }

    public static float getCoconutCocktailSaturation() {
        return BeachpartyNeoForgeConfig.COCONUT_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getCoconutCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.COCONUT_COCKTAIL_EFFECT_DURATION.get();
    }

    public static int getSweetberriesCocktailNutrition() {
        return BeachpartyNeoForgeConfig.SWEETBERRIES_COCKTAIL_NUTRITION.get();
    }

    public static float getSweetberriesCocktailSaturation() {
        return BeachpartyNeoForgeConfig.SWEETBERRIES_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getSweetberriesCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.SWEETBERRIES_COCKTAIL_EFFECT_DURATION.get();
    }

    public static int getCocoaCocktailNutrition() {
        return BeachpartyNeoForgeConfig.COCOA_COCKTAIL_NUTRITION.get();
    }

    public static float getCocoaCocktailSaturation() {
        return BeachpartyNeoForgeConfig.COCOA_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getCocoaCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.COCOA_COCKTAIL_EFFECT_DURATION.get();
    }

    public static int getPumpkinCocktailNutrition() {
        return BeachpartyNeoForgeConfig.PUMPKIN_COCKTAIL_NUTRITION.get();
    }

    public static float getPumpkinCocktailSaturation() {
        return BeachpartyNeoForgeConfig.PUMPKIN_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getPumpkinCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.PUMPKIN_COCKTAIL_EFFECT_DURATION.get();
    }

    public static int getHoneyCocktailNutrition() {
        return BeachpartyNeoForgeConfig.HONEY_COCKTAIL_NUTRITION.get();
    }

    public static float getHoneyCocktailSaturation() {
        return BeachpartyNeoForgeConfig.HONEY_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getHoneyCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.HONEY_COCKTAIL_EFFECT_DURATION.get();
    }

    public static int getMelonCocktailNutrition() {
        return BeachpartyNeoForgeConfig.MELON_COCKTAIL_NUTRITION.get();
    }

    public static float getMelonCocktailSaturation() {
        return BeachpartyNeoForgeConfig.MELON_COCKTAIL_SATURATION.get().floatValue();
    }

    public static int getMelonCocktailEffectDuration() {
        return BeachpartyNeoForgeConfig.MELON_COCKTAIL_EFFECT_DURATION.get();
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).build(name));
    }
}
