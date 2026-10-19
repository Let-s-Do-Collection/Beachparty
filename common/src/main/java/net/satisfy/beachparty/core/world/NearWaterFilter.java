package net.satisfy.beachparty.core.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.satisfy.beachparty.core.registry.PlacerTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class NearWaterFilter extends PlacementFilter {
    public static final MapCodec<NearWaterFilter> CODEC = Codec.intRange(1, 8).fieldOf("radius").xmap(NearWaterFilter::new, filter -> filter.radius);
    private static final int MIN_DEPTH = -4;

    private final int radius;

    public NearWaterFilter(int radius) {
        this.radius = radius;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        for (int dy = MIN_DEPTH; dy <= 0; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (context.getLevel().getFluidState(check.setWithOffset(pos, dx, dy, dz)).is(FluidTags.WATER)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlacerTypeRegistry.NEAR_WATER.get();
    }
}
