package net.satisfy.beachparty.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.entity.WetHayBaleBlockEntity;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WetHayBaleInfoProvider implements BlockInfoProvider {
    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!PlatformHelper.showWetHayBaleInfo() || !(level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity bale)) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        if (bale.isProtected()) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.sealed").withStyle(ChatFormatting.GREEN)));
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.hint_unseal").withStyle(ChatFormatting.GRAY)));
            return sections;
        }
        if (level.getFluidState(pos).is(Fluids.WATER)) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.in_water").withStyle(ChatFormatting.GOLD)));
        } else if (!level.canSeeSky(pos.above())) {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.needs_sky").withStyle(ChatFormatting.GOLD)));
        } else {
            sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.drying").withStyle(ChatFormatting.YELLOW)));
        }
        boolean hot = level.getBiome(pos).is(WetHayBaleBlockEntity.HOT_BIOME);
        ItemStack result = new ItemStack(hot ? ObjectRegistry.THATCH.get() : Blocks.HAY_BLOCK);
        sections.add(InfoSection.icons(Component.translatable("hud.beachparty.wet_hay_bale.turns_into"), List.of(result), InfoSection.ROW_COLUMNS));
        sections.add(InfoSection.title(Component.translatable("hud.beachparty.wet_hay_bale.hint_seal").withStyle(ChatFormatting.GRAY)));
        return sections;
    }
}
