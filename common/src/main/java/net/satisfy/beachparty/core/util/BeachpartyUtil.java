package net.satisfy.beachparty.core.util;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;

public class BeachpartyUtil {
    public static void registerColorWeapon(Item item, int defaultColor) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> tintIndex == 0 ? getColor(stack, defaultColor) : 0xFFFFFFFF, item);
    }

    static int getColor(ItemStack itemStack, int defaultColor) {
        DyedItemColor dyed = itemStack.get(DataComponents.DYED_COLOR);
        if (dyed != null) return 0xFF000000 | dyed.rgb();
        CompoundTag displayTag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("display");
        if (displayTag.contains("color", Tag.TAG_ANY_NUMERIC)) return 0xFF000000 | displayTag.getInt("color");
        return 0xFF000000 | defaultColor;
    }

    public static boolean matchesRecipe(RecipeInput inventory, NonNullList<Ingredient> recipe, int startIndex, int endIndex) {
        final List<ItemStack> validStacks = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            final ItemStack stackInSlot = inventory.getItem(i);
            if (!stackInSlot.isEmpty())
                validStacks.add(stackInSlot);
        }
        for (Ingredient entry : recipe) {
            boolean matches = false;
            for (ItemStack item : validStacks) {
                if (entry.test(item)) {
                    matches = true;
                    validStacks.remove(item);
                    break;
                }
            }
            if (!matches) {
                return false;
            }
        }
        return true;
    }
}
