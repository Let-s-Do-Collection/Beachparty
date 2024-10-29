package net.satisfy.beachparty.item;

import net.minecraft.world.item.ItemStack;

public interface IBeachpartyArmorItem {

    void toggleVisibility(ItemStack itemStack);

    boolean isVisible(ItemStack itemStack);
}
