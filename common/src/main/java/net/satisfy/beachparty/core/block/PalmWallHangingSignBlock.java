package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.core.block.entity.PalmHangingSignBlockEntity;

public class PalmWallHangingSignBlock extends WallHangingSignBlock {
    public PalmWallHangingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PalmHangingSignBlockEntity(pos, state);
    }
}