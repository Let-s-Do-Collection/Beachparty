package net.satisfy.beachparty.platform.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.fml.common.Mod;
import net.satisfy.beachparty.Beachparty;

import java.util.List;

@Mod.EventBusSubscriber(modid = Beachparty.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlatformHelperImpl {
    public static void onUseSeashell(Level world, Player player, LootParams lootParams, ItemStack stack) {
    }

    public static void addSeashellTooltip(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
    }

}
