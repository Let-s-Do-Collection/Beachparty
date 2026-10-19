package net.satisfy.beachparty.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.BeachSunLounger;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SunLoungerInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showSunLoungerInfo() || !(state.getBlock() instanceof BeachSunLounger)) {
            return List.of();
        }
        BlockState head = state.getValue(BeachSunLounger.PART) == BedPart.HEAD ? state : level.getBlockState(pos.relative(state.getValue(BedBlock.FACING)));
        if (!(head.getBlock() instanceof BeachSunLounger)) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        if (!head.getValue(BeachSunLounger.DOWN)) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.upright")));
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.hint_lay_flat").withStyle(ChatFormatting.GRAY)));
        } else if (head.getValue(BeachSunLounger.OCCUPIED)) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.occupied").withStyle(ChatFormatting.GOLD)));
        } else {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.lying_flat")));
            if (level.isNight() || level.isThundering()) {
                sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.hint_sleep").withStyle(ChatFormatting.GRAY)));
            }
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.sun_lounger.hint_sit_up").withStyle(ChatFormatting.GRAY)));
        }
        return sections;
    }
}
