package net.satisfy.beachparty.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MiniFridgeInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showMiniFridgeInfo() || !(level.getBlockEntity(pos) instanceof MiniFridgeBlockEntity fridge)) {
            return List.of();
        }
        ItemStack input = fridge.getInput();
        ItemStack output = fridge.getOutput();
        List<InfoSection> sections = new ArrayList<>();
        if (input.isEmpty() && output.isEmpty()) {
            List<ItemStack> start = MiniFridgeBlockEntity.getStartItems(level);
            if (!start.isEmpty()) {
                sections.add(InfoSection.icons(Component.translatable("hud.beachparty.mini_fridge.start_with"), start, InfoSection.GRID_COLUMNS));
            }
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.mini_fridge.hint_add").withStyle(ChatFormatting.GRAY)));
            return sections;
        }
        if (!input.isEmpty()) {
            if (fridge.isOutputBlocked()) {
                sections.add(InfoSection.title(Component.translatable("hud.beachparty.mini_fridge.full").withStyle(ChatFormatting.RED)));
            } else {
                sections.add(InfoSection.title(Component.translatable("hud.beachparty.mini_fridge.progress", fridge.getProgress()).withStyle(ChatFormatting.YELLOW)));
            }
            sections.add(InfoSection.icons(Component.translatable("hud.beachparty.mini_fridge.in_fridge"), List.of(input.copy()), InfoSection.ROW_COLUMNS).withDecorations());
        }
        if (!output.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.beachparty.mini_fridge.ready").withStyle(ChatFormatting.GREEN), List.of(output.copy()), InfoSection.ROW_COLUMNS).withDecorations());
        }
        if (input.isEmpty() || input.getCount() < input.getMaxStackSize()) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.mini_fridge.hint_add").withStyle(ChatFormatting.GRAY)));
        }
        sections.add(InfoSection.title(Component.translatable("hud.beachparty.mini_fridge.hint_take").withStyle(ChatFormatting.GRAY)));
        return sections;
    }
}
