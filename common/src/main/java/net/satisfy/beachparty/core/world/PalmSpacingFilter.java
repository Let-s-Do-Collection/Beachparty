package net.satisfy.beachparty.core.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.PlacerTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class PalmSpacingFilter extends PlacementFilter {
    public static final MapCodec<PalmSpacingFilter> CODEC = Codec.intRange(1, 15).fieldOf("radius").xmap(PalmSpacingFilter::new, filter -> filter.radius);
    private static final int MIN_DY = -2;
    private static final int MAX_DY = 4;

    private final int radius;

    public PalmSpacingFilter(int radius) {
        this.radius = radius;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        WorldGenLevel level = context.getLevel();
        Block log = ObjectRegistry.PALM_LOG.get();
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        int radiusSqr = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radiusSqr) continue;
                for (int dy = MIN_DY; dy <= MAX_DY; dy++) {
                    if (level.getBlockState(check.setWithOffset(pos, dx, dy, dz)).is(log)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlacerTypeRegistry.PALM_SPACING.get();
    }
}
