package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.CocktailBlock;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PalmBarBlockEntity extends BlockEntity {
    public static final int MAX_INGREDIENTS = 4;
    public static final int SHAKES = 5;
    public static final int SHAKE_COOLDOWN = 10;
    private final List<ItemStack> ingredients = new ArrayList<>();
    private final NonNullList<ItemStack> leftovers = NonNullList.create();
    private ItemStack result = ItemStack.EMPTY;
    private int shakes;
    private long lastShake = -SHAKE_COOLDOWN;
    private boolean previewDirty = true;
    private List<PalmBarRecipe> candidates = List.of();
    @Nullable
    private PalmBarRecipe matched;

    public PalmBarBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.PALM_BAR_BLOCK_ENTITY.get(), pos, state);
    }

    public static List<PalmBarRecipe> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.PALM_BAR_RECIPE_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .sorted(Comparator.comparingInt((PalmBarRecipe recipe) -> recipe.getIngredients().size()).thenComparing(recipe -> recipe.getResultItem().getDescriptionId()))
                .toList();
    }

    public static List<ItemStack> getStartItems(Level level) {
        return distinctItems(getRecipes(level).stream().map(recipe -> recipe.getIngredients().getFirst()).toList(), List.of());
    }

    private static List<ItemStack> distinctItems(List<Ingredient> ingredients, List<ItemStack> present) {
        Set<Item> seen = new LinkedHashSet<>();
        present.forEach(stack -> seen.add(stack.getItem()));
        List<ItemStack> items = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ItemStack[] options = ingredient.getItems();
            if (options.length > 0 && seen.add(options[0].getItem())) {
                items.add(options[0].copyWithCount(1));
            }
        }
        return items;
    }

    private static boolean fits(PalmBarRecipe recipe, List<ItemStack> stacks) {
        List<Ingredient> needed = recipe.getIngredients();
        if (stacks.isEmpty()) {
            return true;
        }
        if (stacks.size() > needed.size() || !needed.getFirst().test(stacks.getFirst())) {
            return false;
        }
        boolean[] used = new boolean[needed.size()];
        used[0] = true;
        return assign(needed, stacks, 1, used);
    }

    private static boolean assign(List<Ingredient> needed, List<ItemStack> stacks, int index, boolean[] used) {
        if (index == stacks.size()) {
            return true;
        }
        for (int i = 0; i < needed.size(); i++) {
            if (!used[i] && needed.get(i).test(stacks.get(index))) {
                used[i] = true;
                if (assign(needed, stacks, index + 1, used)) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }

    private void refreshPreview() {
        if (!previewDirty || this.level == null) {
            return;
        }
        previewDirty = false;
        candidates = getRecipes(this.level).stream().filter(recipe -> fits(recipe, ingredients)).toList();
        matched = ingredients.isEmpty() ? null : candidates.stream().filter(recipe -> recipe.getIngredients().size() == ingredients.size()).findFirst().orElse(null);
    }

    public List<PalmBarRecipe> getCandidates() {
        refreshPreview();
        return candidates;
    }

    @Nullable
    public PalmBarRecipe getMatched() {
        refreshPreview();
        return matched;
    }

    @Nullable
    public PalmBarRecipe getPreview() {
        PalmBarRecipe recipe = getMatched();
        if (recipe != null) {
            return recipe;
        }
        List<PalmBarRecipe> options = getCandidates();
        return ingredients.isEmpty() || options.isEmpty() ? null : options.getFirst();
    }

    public List<ItemStack> getNextItems() {
        if (isLocked() || ingredients.size() >= MAX_INGREDIENTS) {
            return List.of();
        }
        List<Ingredient> missing = new ArrayList<>();
        for (PalmBarRecipe recipe : getCandidates()) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredients.stream().noneMatch(ingredient)) {
                    missing.add(ingredient);
                }
            }
        }
        return distinctItems(missing, ingredients);
    }

    public boolean canAdd(ItemStack stack) {
        if (stack.isEmpty() || isLocked() || ingredients.size() >= MAX_INGREDIENTS || this.level == null) {
            return false;
        }
        List<ItemStack> next = new ArrayList<>(ingredients);
        next.add(stack.copyWithCount(1));
        return getCandidates().stream().anyMatch(recipe -> fits(recipe, next));
    }

    public void add(ItemStack stack) {
        ingredients.add(stack.split(1));
        markIngredientsChanged();
    }

    public ItemStack takeBack() {
        if (ingredients.isEmpty() || isLocked()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = ingredients.removeLast();
        markIngredientsChanged();
        return stack;
    }

    public List<ItemStack> shake(long time) {
        PalmBarRecipe recipe = getMatched();
        if (recipe == null || !result.isEmpty() || isShaking(time)) {
            return List.of();
        }
        shakes++;
        lastShake = time;
        if (shakes < SHAKES) {
            setChanged();
            return List.of();
        }
        List<ItemStack> remainders = new ArrayList<>();
        for (ItemStack ingredient : ingredients) {
            Item remainder = ingredient.getItem().getCraftingRemainingItem();
            if (remainder != null) {
                remainders.add(new ItemStack(remainder));
            }
        }
        result = recipe.getResultItem().copy();
        ingredients.clear();
        shakes = 0;
        markIngredientsChanged();
        return remainders;
    }

    public ItemStack takeResult() {
        ItemStack taken = result;
        result = ItemStack.EMPTY;
        setChanged();
        return taken;
    }

    public List<ItemStack> takeLeftovers() {
        List<ItemStack> taken = new ArrayList<>(leftovers);
        leftovers.clear();
        setChanged();
        return taken;
    }

    public NonNullList<ItemStack> clearContents() {
        NonNullList<ItemStack> contents = NonNullList.create();
        contents.addAll(ingredients);
        if (!result.isEmpty()) {
            contents.add(result);
        }
        ingredients.clear();
        result = ItemStack.EMPTY;
        shakes = 0;
        markIngredientsChanged();
        return contents;
    }

    @Nullable
    public Block getGlassBlock() {
        PalmBarRecipe recipe = getPreview();
        ItemStack stack = !result.isEmpty() ? result : recipe != null ? recipe.getResultItem() : ItemStack.EMPTY;
        if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof CocktailBlock) {
            return item.getBlock();
        }
        return null;
    }

    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> drops = NonNullList.create();
        drops.addAll(ingredients);
        drops.addAll(leftovers);
        if (!result.isEmpty()) {
            drops.add(result);
        }
        return drops;
    }

    private void markIngredientsChanged() {
        if (ingredients.isEmpty()) {
            shakes = 0;
        }
        previewDirty = true;
        setChanged();
    }

    public boolean isLocked() {
        return shakes > 0 || !result.isEmpty();
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getLeftovers() {
        return leftovers;
    }

    public boolean hasLeftovers() {
        return !leftovers.isEmpty();
    }

    public int getShakes() {
        return shakes;
    }

    public boolean isShaking(long time) {
        return time - lastShake < SHAKE_COOLDOWN && time >= lastShake;
    }

    public long getLastShake() {
        return lastShake;
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        ingredients.clear();
        leftovers.clear();
        result = ItemStack.EMPTY;
        if (nbt.contains("Bar")) {
            CompoundTag bar = nbt.getCompound("Bar");
            NonNullList<ItemStack> stored = NonNullList.withSize(MAX_INGREDIENTS, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(bar.getCompound("Ingredients"), stored, provider);
            stored.stream().filter(stack -> !stack.isEmpty()).forEach(ingredients::add);
            result = ItemStack.parseOptional(provider, bar.getCompound("Result"));
            shakes = bar.getInt("Shakes");
            lastShake = Math.max(bar.getLong("LastShake"), -SHAKE_COOLDOWN);
            NonNullList<ItemStack> rest = NonNullList.withSize(bar.getInt("LeftoverCount"), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(bar.getCompound("Leftovers"), rest, provider);
            rest.stream().filter(stack -> !stack.isEmpty()).forEach(leftovers::add);
        } else if (nbt.contains("Items")) {
            NonNullList<ItemStack> old = NonNullList.withSize(5, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt, old, provider);
            old.stream().filter(stack -> !stack.isEmpty()).forEach(leftovers::add);
            shakes = 0;
        }
        previewDirty = true;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        CompoundTag bar = new CompoundTag();
        NonNullList<ItemStack> stored = NonNullList.withSize(MAX_INGREDIENTS, ItemStack.EMPTY);
        for (int i = 0; i < ingredients.size() && i < MAX_INGREDIENTS; i++) {
            stored.set(i, ingredients.get(i));
        }
        bar.put("Ingredients", ContainerHelper.saveAllItems(new CompoundTag(), stored, true, provider));
        if (!result.isEmpty()) {
            bar.put("Result", result.save(provider));
        }
        bar.putInt("Shakes", shakes);
        bar.putLong("LastShake", lastShake);
        bar.putInt("LeftoverCount", leftovers.size());
        bar.put("Leftovers", ContainerHelper.saveAllItems(new CompoundTag(), leftovers, true, provider));
        nbt.put("Bar", bar);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }
}
