package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.client.gui.handler.MiniFridgeGuiHandler;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import net.satisfy.beachparty.core.world.ImplementedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MiniFridgeBlockEntity extends BlockEntity implements ImplementedInventory, BlockEntityTicker<MiniFridgeBlockEntity>, MenuProvider {
    public static final int CAPACITY = 2;
    private static final int[] SLOTS_FOR_INPUT = new int[]{1};
    private static final int[] SLOTS_FOR_OUTPUT = new int[]{0};
    private static final int OUTPUT_SLOT = 0;
    private static final int INPUT_SLOT = 1;
    protected float experience;
    private NonNullList<ItemStack> inventory;
    private int fermentationTime = 0;
    private int totalFermentationTime;

    private final ContainerData propertyDelegate = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fermentationTime;
                case 1 -> totalFermentationTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> fermentationTime = value;
                case 1 -> totalFermentationTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public MiniFridgeBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.MINI_FRIDGE_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory, provider);
        this.fermentationTime = nbt.getInt("FermentationTime");
        this.totalFermentationTime = nbt.getInt("TotalFermentationTime");
        this.experience = nbt.getFloat("Experience");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, this.inventory, provider);
        compoundTag.putInt("FermentationTime", this.fermentationTime);
        compoundTag.putInt("TotalFermentationTime", this.totalFermentationTime);
        compoundTag.putFloat("Experience", this.experience);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, MiniFridgeBlockEntity blockEntity) {
        if (world.isClientSide) return;
        boolean dirty = false;
        List<RecipeHolder<MiniFridgeRecipe>> recipes = world.getRecipeManager()
                .getAllRecipesFor(RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
        Optional<MiniFridgeRecipe> recipeType = Optional.ofNullable(getRecipe(recipes, inventory));
        assert level != null;
        RegistryAccess access = level.registryAccess();
        if (recipeType.isPresent() && canCraft(recipeType.get(), access)) {
            if (this.fermentationTime == 0) {
                this.totalFermentationTime = recipeType.get().getCraftingTime();
            }
            this.fermentationTime++;
            if (this.fermentationTime >= this.totalFermentationTime) {
                this.fermentationTime = 0;
                craft(recipeType.get(), access);
                dirty = true;
            }
        } else {
            this.fermentationTime = 0;
        }
        if (dirty) {
            setChanged();
        }
    }

    private boolean canCraft(MiniFridgeRecipe recipe, RegistryAccess access) {
        if (recipe == null || recipe.getResultItem(access).isEmpty()) {
            return false;
        }
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        ItemStack outputStack = this.getItem(OUTPUT_SLOT);
        return !inputStack.isEmpty() && (outputStack.isEmpty() || outputStack == recipe.getResultItem(access));
    }

    private void craft(MiniFridgeRecipe recipe, RegistryAccess access) {
        if (!canCraft(recipe, access)) {
            return;
        }
        final ItemStack recipeOutput = recipe.getResultItem(access);
        final ItemStack outputSlotStack = this.getItem(OUTPUT_SLOT);
        if (outputSlotStack.isEmpty()) {
            setItem(OUTPUT_SLOT, recipeOutput.copy());
        }
        ItemStack inputStack = this.getItem(INPUT_SLOT);
        inputStack.shrink(1);
        setItem(INPUT_SLOT, inputStack.isEmpty() ? ItemStack.EMPTY : inputStack);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_FOR_OUTPUT;
        } else {
            return SLOTS_FOR_INPUT;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (slot == INPUT_SLOT) {
            this.totalFermentationTime = 50;
            this.fermentationTime = 0;
            setChanged();
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == INPUT_SLOT;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot == OUTPUT_SLOT;
    }

    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        return this.level.getBlockEntity(this.worldPosition) == this &&
                player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ObjectRegistry.MINI_FRIDGE.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new MiniFridgeGuiHandler(syncId, inv, this, this.propertyDelegate);
    }

    private MiniFridgeRecipe getRecipe(List<RecipeHolder<MiniFridgeRecipe>> recipes, NonNullList<ItemStack> inventory) {
        recipeLoop:
        for (RecipeHolder<MiniFridgeRecipe> recipeHolder : recipes) {
            MiniFridgeRecipe recipe = recipeHolder.value();
            for (Ingredient ingredient : recipe.getIngredients()) {
                boolean ingredientFound = false;
                for (int slotIndex = 1; slotIndex < inventory.size(); slotIndex++) {
                    ItemStack slotItem = inventory.get(slotIndex);
                    if (ingredient.test(slotItem)) {
                        ingredientFound = true;
                        break;
                    }
                }
                if (!ingredientFound) {
                    continue recipeLoop;
                }
            }
            return recipe;
        }
        return null;
    }
}
