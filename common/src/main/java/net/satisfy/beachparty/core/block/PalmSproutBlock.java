package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.beachparty.core.world.ConfiguredFeatures;

import java.util.Optional;

public class PalmSproutBlock extends SaplingBlock {

    public PalmSproutBlock(Properties properties) {
        super(new TreeGrower("palm", Optional.empty(), Optional.of(ConfiguredFeatures.PALM_TREE_KEY), Optional.empty()), Properties.ofFullCopy(Blocks.ACACIA_SAPLING).mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY));
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.SAND);
    }
}

