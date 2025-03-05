package net.satisfy.beachparty.core.world.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.satisfy.beachparty.core.block.HangingCoconutBlock;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.PlacerTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((placer) -> foliagePlacerParts(placer).apply(placer, PalmFoliagePlacer::new));

    public PalmFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
        super(pRadius, pOffset);
    }

    private static void createQuadrant(Direction direction, BlockPos startingPos, LevelSimulatedReader pLevel, FoliagePlacer.FoliageSetter foliageSetter, RandomSource pRandom, TreeConfiguration pConfig) {
        BlockPos.MutableBlockPos pos = startingPos.mutable();

        pos.move(direction);
        tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);

        if (pRandom.nextInt(2) == 0) {
            if (pLevel.isStateAtPosition(pos.below(), BlockBehaviour.BlockStateBase::isAir)) {
                foliageSetter.set(pos.below(), ObjectRegistry.HANGING_COCONUT.get().defaultBlockState().setValue(HangingCoconutBlock.AGE, pRandom.nextInt(3)));
            }
        }
        if (pRandom.nextInt(2) == 0) {
            if (pLevel.isStateAtPosition(pos.below().relative(direction.getCounterClockWise()), BlockBehaviour.BlockStateBase::isAir)) {
                foliageSetter.set(pos.below().relative(direction.getCounterClockWise()), ObjectRegistry.HANGING_COCONUT.get().defaultBlockState().setValue(HangingCoconutBlock.AGE, pRandom.nextInt(3)));
            }
        }

        for (int i = 0; i < 2; i++) {
            pos.move(direction);
            tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);
            pos.move(Direction.DOWN);
            tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);
        }

        pos.set(startingPos);
        pos.move(direction).move(direction.getCounterClockWise());
        tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);
        pos.move(Direction.DOWN).move(direction.getCounterClockWise());
        tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);
        pos.move(direction);
        tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos.relative(direction.getClockWise()));
        for (int i = 0; i < 3; i++) {
            tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, pos);
            pos.move(Direction.DOWN);
        }
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlacerTypesRegistry.PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader pLevel, FoliageSetter foliageSetter, RandomSource pRandom, TreeConfiguration pConfig, int i, FoliageAttachment pAttachment, int j, int k, int l) {
        BlockPos startingPos = pAttachment.pos();

        tryPlaceLeaf(pLevel, foliageSetter, pRandom, pConfig, startingPos);

        createQuadrant(Direction.NORTH, startingPos, pLevel, foliageSetter, pRandom, pConfig);
        createQuadrant(Direction.EAST, startingPos, pLevel, foliageSetter, pRandom, pConfig);
        createQuadrant(Direction.SOUTH, startingPos, pLevel, foliageSetter, pRandom, pConfig);
        createQuadrant(Direction.WEST, startingPos, pLevel, foliageSetter, pRandom, pConfig);
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        return false;
    }
}