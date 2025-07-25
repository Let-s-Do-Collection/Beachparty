package net.satisfy.beachparty.core.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

public class MiniFridgeRecipe implements Recipe<RecipeInput> {

    final ResourceLocation id;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final int craftingTime;

    public MiniFridgeRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output, int craftingTime) {
        this.id = id;
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
        @Override
        public MapCodec<MiniFridgeRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MiniFridgeRecipe> streamCodec() {
            return null;
        }

        /*
        @Override
        public @NotNull MiniFridgeRecipe fromJson(ResourceLocation id, JsonObject json) {
            var jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();
            jsonArray.forEach(element -> ingredients.add(Ingredient.fromJson(element.getAsJsonObject())));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for Mini Fridge Recipe");
            } else if (ingredients.size() > 1) {
                throw new JsonParseException("Too many ingredients for Mini Fridge Recipe");
            }
            int craftingTime = GsonHelper.getAsInt(json, "crafting_time", 100);
            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            return new MiniFridgeRecipe(id, ingredients, ShapedRecipe.itemStackFromJson(resultJson), craftingTime);
        }


        @Override
        public @NotNull MiniFridgeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            final var ingredients = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();
            int craftingTime = buf.readVarInt();
            return new MiniFridgeRecipe(id, ingredients, output, craftingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MiniFridgeRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (Ingredient ingredient : recipe.inputs) {
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
            buf.writeVarInt(recipe.craftingTime);
        }*/
    }
}
