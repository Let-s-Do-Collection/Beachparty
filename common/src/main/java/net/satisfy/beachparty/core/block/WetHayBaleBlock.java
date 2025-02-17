package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.beachparty.core.block.entity.WetHayBaleBlockEntity;

public class WetHayBaleBlock extends RotatedPillarBlock implements EntityBlock {
    public WetHayBaleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WetHayBaleBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, blockState, te) -> {
            if (te instanceof WetHayBaleBlockEntity wetHayBaleBlockEntity) {
                WetHayBaleBlockEntity.tick((ServerLevel) lvl, pos, blockState, wetHayBaleBlockEntity);
            }
        };
    }
}
