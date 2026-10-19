package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.MiniFridgeBlock;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.RecipeTypeRegistry;
import net.satisfy.foundation.util.ImplementedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MiniFridgeBlockEntity extends BlockEntity implements ImplementedInventory, BlockEntityTicker<MiniFridgeBlockEntity> {
    public static final int CAPACITY = 2;
    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    private static final int[] SLOTS_FOR_INPUT = new int[]{INPUT_SLOT};
    private static final int[] SLOTS_FOR_OUTPUT = new int[]{OUTPUT_SLOT};
    private NonNullList<ItemStack> inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
    private int fermentationTime;
    private int totalFermentationTime;
    private ItemStack cachedInput = ItemStack.EMPTY;
    private Optional<MiniFridgeRecipe> cachedRecipe = Optional.empty();

    public MiniFridgeBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.MINI_FRIDGE_BLOCK_ENTITY.get(), pos, state);
    }

    public static Optional<RecipeHolder<MiniFridgeRecipe>> findRecipe(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MINI_FRIDGE_RECIPE_TYPE.get()).stream()
                .filter(holder -> !holder.value().getIngredients().isEmpty() && holder.value().getIngredients().getFirst().test(stack))
                .findFirst();
    }

    public static List<ItemStack> getStartItems(Level level) {
        return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.MINI_FRIDGE_RECIPE_TYPE.get()).stream()
                .filter(holder -> !holder.value().getIngredients().isEmpty())
                .map(holder -> holder.value().getIngredients().getFirst().getItems())
                .filter(items -> items.length > 0)
                .map(items -> items[0].copy())
                .toList();
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory, provider);
        this.fermentationTime = nbt.getInt("FermentationTime");
        this.totalFermentationTime = nbt.getInt("TotalFermentationTime");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, this.inventory, provider);
        compoundTag.putInt("FermentationTime", this.fermentationTime);
        compoundTag.putInt("TotalFermentationTime", this.totalFermentationTime);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, MiniFridgeBlockEntity blockEntity) {
        Optional<MiniFridgeRecipe> recipe = currentRecipe(level);
        boolean freezing = recipe.isPresent() && canCraft(recipe.get(), level);
        if (!level.isClientSide && state.getValue(MiniFridgeBlock.FREEZING) != freezing) {
            level.setBlock(pos, state.setValue(MiniFridgeBlock.FREEZING, freezing), Block.UPDATE_ALL);
        }
        if (!freezing) {
            if (this.fermentationTime != 0 && !level.isClientSide) {
                this.fermentationTime = 0;
                setChanged();
            }
            return;
        }
        this.totalFermentationTime = recipe.get().getCraftingTime();
        if (level.isClientSide) {
            this.fermentationTime = Math.min(this.fermentationTime + 1, this.totalFermentationTime);
            return;
        }
        this.fermentationTime++;
        if (this.fermentationTime >= this.totalFermentationTime) {
            this.fermentationTime = 0;
            craft(recipe.get(), level);
        }
    }

    private Optional<MiniFridgeRecipe> currentRecipe(Level level) {
        ItemStack input = getItem(INPUT_SLOT);
        if (input.isEmpty()) {
            return Optional.empty();
        }
        if (!ItemStack.isSameItemSameComponents(input, cachedInput)) {
            cachedInput = input.copyWithCount(1);
            cachedRecipe = findRecipe(level, input).map(RecipeHolder::value);
        }
        return cachedRecipe;
    }

    private boolean canCraft(MiniFridgeRecipe recipe, Level level) {
        ItemStack result = recipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }
        ItemStack output = getItem(OUTPUT_SLOT);
        if (output.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void craft(MiniFridgeRecipe recipe, Level level) {
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack output = getItem(OUTPUT_SLOT);
        if (output.isEmpty()) {
            this.inventory.set(OUTPUT_SLOT, result.copy());
        } else {
            output.grow(result.getCount());
        }
        getItem(INPUT_SLOT).shrink(1);
        setChanged();
    }

    public boolean tryInsert(ItemStack stack) {
        if (this.level == null || findRecipe(this.level, stack).isEmpty()) {
            return false;
        }
        ItemStack input = getItem(INPUT_SLOT);
        if (input.isEmpty()) {
            this.inventory.set(INPUT_SLOT, stack.split(stack.getMaxStackSize()));
            this.fermentationTime = 0;
            setChanged();
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(input, stack) || input.getCount() >= input.getMaxStackSize()) {
            return false;
        }
        input.grow(stack.split(input.getMaxStackSize() - input.getCount()).getCount());
        setChanged();
        return true;
    }

    public ItemStack takeOut() {
        int slot = !getItem(OUTPUT_SLOT).isEmpty() ? OUTPUT_SLOT : INPUT_SLOT;
        ItemStack stack = getItem(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventory.set(slot, ItemStack.EMPTY);
        if (slot == INPUT_SLOT) {
            this.fermentationTime = 0;
        }
        setChanged();
        return stack;
    }

    public ItemStack getInput() {
        return getItem(INPUT_SLOT);
    }

    public ItemStack getOutput() {
        return getItem(OUTPUT_SLOT);
    }

    public int getProgress() {
        return this.totalFermentationTime <= 0 ? 0 : Math.min(100, this.fermentationTime * 100 / this.totalFermentationTime);
    }

    public boolean isOutputBlocked() {
        return this.level != null && findRecipe(this.level, getInput()).map(holder -> !canCraft(holder.value(), this.level)).orElse(false);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? SLOTS_FOR_OUTPUT : SLOTS_FOR_INPUT;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        boolean changedInput = slot == INPUT_SLOT && !ItemStack.isSameItemSameComponents(getItem(INPUT_SLOT), stack);
        this.inventory.set(slot, stack);
        if (changedInput) {
            this.fermentationTime = 0;
        }
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == INPUT_SLOT && this.level != null && findRecipe(this.level, stack).isPresent();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot == OUTPUT_SLOT;
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
