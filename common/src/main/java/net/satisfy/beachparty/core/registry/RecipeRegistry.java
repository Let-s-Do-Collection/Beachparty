package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;

import java.util.function.Supplier;

public class RecipeRegistry {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Beachparty.MOD_ID, Registries.RECIPE_SERIALIZER);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.RECIPE_TYPE);

    public static final RegistrySupplier<RecipeSerializer<PalmBarRecipe>> PALM_BAR_RECIPE_SERIALIZER = create("palm_bar_mixing", PalmBarRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<PalmBarRecipe>> PALM_BAR_RECIPE_TYPE = create("palm_bar_mixing");

    public static final RegistrySupplier<RecipeSerializer<MiniFridgeRecipe>> MINI_FRIDGE_RECIPE_SERIALIZER = create("mini_fridge_freezing", MiniFridgeRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeType<MiniFridgeRecipe>> MINI_FRIDGE_RECIPE_TYPE = create("mini_fridge_freezing");



    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create(String name) {
        Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        return RECIPE_TYPES.register(name, type);
    }

    public static void init() {
        RECIPE_SERIALIZERS.register();
        RECIPE_TYPES.register();
    }
}
