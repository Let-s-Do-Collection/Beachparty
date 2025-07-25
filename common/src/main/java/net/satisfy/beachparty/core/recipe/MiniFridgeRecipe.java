package net.satisfy.beachparty.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

public class MiniFridgeRecipe implements Recipe<RecipeInput> {

    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final int craftingTime;

    public MiniFridgeRecipe(NonNullList<Ingredient> inputs, ItemStack output, int craftingTime) {
        this.inputs = inputs;
        this.output = output;
        this.craftingTime = craftingTime;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return BeachpartyUtil.matchesRecipe(recipeInput, inputs, 1, 1);
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.copy();
    }

    public @NotNull ResourceLocation getId() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.getId();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public static class Serializer implements RecipeSerializer<MiniFridgeRecipe> {

        public static final StreamCodec<RegistryFriendlyByteBuf, MiniFridgeRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public static final MapCodec<MiniFridgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(list -> {
                            Ingredient[] ingredients = list.toArray(Ingredient[]::new);
                            if (ingredients.length == 0) {
                                return DataResult.error(() -> "No ingredients for Cauldron recipe");
                            } else {
                                return ingredients.length > 1 ? DataResult.error(() -> "Too many ingredients for Cauldron recipe") : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                            }
                        }, DataResult::success).forGetter(MiniFridgeRecipe::getIngredients),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(miniFridgeRecipe -> miniFridgeRecipe.output),
                Codec.INT.fieldOf("crafting_time").forGetter(MiniFridgeRecipe::getCraftingTime)
                ).apply(instance, MiniFridgeRecipe::new)
        );

        @Override
        public MapCodec<MiniFridgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MiniFridgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static @NotNull MiniFridgeRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            nonNullList.replaceAll((ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(buf);
            int craftingTime = buf.readVarInt();
            return new MiniFridgeRecipe(nonNullList, itemStack, craftingTime);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, MiniFridgeRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            buf.writeVarInt(recipe.craftingTime);
        }
    }
}
