package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.core.block.entity.PalmSignBlockEntity;
import org.jetbrains.annotations.NotNull;

public class PalmWallSignBlock extends WallSignBlock {
    public PalmWallSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PalmSignBlockEntity(pos, state);
    }
}