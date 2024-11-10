package net.satisfy.beachparty.core.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

public class TikiBarRecipe implements Recipe<Container> {
    final ResourceLocation id;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    public TikiBarRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        return BeachpartyUtil.matchesRecipe(inventory, inputs, 1, 2);
    }

    @Override
    public @NotNull ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TIKI_BAR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.TIKI_BAR_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<TikiBarRecipe> {

        @Override
        public @NotNull TikiBarRecipe fromJson(ResourceLocation id, JsonObject json) {
            final var ingredients = BeachpartyUtil.deserializeIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for TikiBar Recipe");
            } else if (ingredients.size() > 4) {
                throw new JsonParseException("Too many ingredients for TikiBar Recipe");
            } else {
                return new TikiBarRecipe(id, ingredients, ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")));
            }
        }

        @Override
        public @NotNull TikiBarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            final var ingredients = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            return new TikiBarRecipe(id, ingredients, buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TikiBarRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (Ingredient ingredient : recipe.inputs) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.output);
        }
    }
}