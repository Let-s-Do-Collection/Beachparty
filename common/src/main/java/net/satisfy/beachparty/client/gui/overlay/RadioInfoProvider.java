package net.satisfy.beachparty.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.RadioBlock;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadioInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showRadioInfo() || !state.hasProperty(RadioBlock.ON)) {
            return List.of();
        }
        if (state.getValue(RadioBlock.ON)) {
            return List.of(
                    InfoSection.title(Component.translatable("hud.beachparty.radio.status_on").withStyle(ChatFormatting.GOLD)),
                    InfoSection.title(Component.translatable("hud.beachparty.radio.hint_next").withStyle(ChatFormatting.GRAY)),
                    InfoSection.title(Component.translatable("hud.beachparty.radio.hint_off").withStyle(ChatFormatting.GRAY)));
        }
        return List.of(
                InfoSection.title(Component.translatable("hud.beachparty.radio.status_off").withStyle(ChatFormatting.GRAY)),
                InfoSection.title(Component.translatable("hud.beachparty.radio.hint_on").withStyle(ChatFormatting.GRAY)));
    }
}
