package net.satisfy.beachparty.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.PalmBarBlock;
import net.satisfy.beachparty.core.block.PalmBarGlassBlock;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.foundation.tooltip.TooltipBorder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PalmBarInfoProvider implements BlockInfoProvider {
    private static final ResourceLocation SHAKE_ICON = BeachpartyIdentifier.identifier("textures/gui/shake.png");
    private static final int SHAKE_ICON_SIZE = 20;
    private static final int SHAKE_START_COLOR = 0xFFFFD700;
    private static final int SHAKE_END_COLOR = 0xFFE0301E;
    private static final float SHAKE_PULSE_SPEED = 0.2F;

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showPalmBarInfo()) {
            return List.of();
        }
        if (state.getBlock() instanceof PalmBarGlassBlock) {
            pos = pos.below();
        }
        if (!(level.getBlockEntity(pos) instanceof PalmBarBlockEntity bar)) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        if (!PalmBarBlock.hasRoom(level, pos)) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.palm_bar.needs_room").withStyle(ChatFormatting.GOLD)));
            return sections;
        }
        if (bar.hasLeftovers()) {
            sections.add(InfoSection.icons(Component.translatable("hud.beachparty.palm_bar.on_bar"), copies(bar.getLeftovers()), InfoSection.ROW_COLUMNS).withDecorations());
            return sections;
        }
        ItemStack result = bar.getResult();
        if (!result.isEmpty()) {
            sections.add(InfoSection.icons(Component.translatable("hud.beachparty.palm_bar.ready").withStyle(ChatFormatting.GREEN), List.of(result.copy()), InfoSection.ROW_COLUMNS).withDecorations());
            return sections;
        }
        if (bar.getIngredients().isEmpty()) {
            List<ItemStack> start = PalmBarBlockEntity.getStartItems(level);
            if (!start.isEmpty()) {
                sections.add(InfoSection.icons(Component.translatable("hud.beachparty.palm_bar.start_with"), start, InfoSection.GRID_COLUMNS));
            }
            return sections;
        }
        if (bar.getMatched() != null) {
            float pulse = (Mth.sin(level.getGameTime() * SHAKE_PULSE_SPEED) + 1.0F) * 0.5F;
            int color = FastColor.ARGB32.lerp(pulse, SHAKE_START_COLOR, SHAKE_END_COLOR) & 0xFFFFFF;
            sections.add(InfoSection.image(Component.translatable("hud.beachparty.palm_bar.shake").withStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(color))), SHAKE_ICON, SHAKE_ICON_SIZE));
            return sections;
        }
        List<ItemStack> next = bar.getNextItems();
        if (next.isEmpty()) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.palm_bar.no_recipe").withStyle(ChatFormatting.RED)));
        } else {
            sections.add(InfoSection.icons(Component.translatable("hud.beachparty.palm_bar.next"), next, InfoSection.GRID_COLUMNS));
        }
        return sections;
    }

    @Override
    public void beforeBackground(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof PalmBarGlassBlock) {
            pos = pos.below();
        }
        if (level.getBlockEntity(pos) instanceof PalmBarBlockEntity bar && bar.getResult().isEmpty() && bar.getMatched() != null && PalmBarBlock.hasRoom(level, pos)) {
            TooltipBorder.mark(0xF0FFD700, 0xF0B8860B);
        }
    }

    private static List<ItemStack> copies(List<ItemStack> stacks) {
        return stacks.stream().map(ItemStack::copy).toList();
    }
}
