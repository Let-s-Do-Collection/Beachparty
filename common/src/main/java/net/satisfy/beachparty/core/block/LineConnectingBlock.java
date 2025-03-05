package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class LineConnectingBlock extends Block {
    public static final DirectionProperty FACING;
    public static final EnumProperty<BeachpartyUtil.LineConnectingType> TYPE;

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        TYPE = BeachpartyUtil.LINE_CONNECTING_TYPE;
    }

    public LineConnectingBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(((this.stateDefinition.any().setValue(FACING, Direction.NORTH)).setValue(TYPE, BeachpartyUtil.LineConnectingType.NONE)));
    }

    public @NotNull InteractionResult use(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6) {
        return InteractionResult.PASS;

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, facing);

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        return switch (facing) {
            case EAST ->
                    blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.south()), world.getBlockState(clickedPos.north())));
            case SOUTH ->
                    blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.west()), world.getBlockState(clickedPos.east())));
            case WEST ->
                    blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.north()), world.getBlockState(clickedPos.south())));
            default ->
                    blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.east()), world.getBlockState(clickedPos.west())));
        };
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        Direction facing = state.getValue(FACING);
        BeachpartyUtil.LineConnectingType type;
        switch (facing) {
            case EAST -> type = getType(state, world.getBlockState(pos.south()), world.getBlockState(pos.north()));
            case SOUTH -> type = getType(state, world.getBlockState(pos.west()), world.getBlockState(pos.east()));
            case WEST -> type = getType(state, world.getBlockState(pos.north()), world.getBlockState(pos.south()));
            default -> type = getType(state, world.getBlockState(pos.east()), world.getBlockState(pos.west()));
        }
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
        }
        world.setBlock(pos, state, 3);
    }

    public BeachpartyUtil.LineConnectingType getType(BlockState state, BlockState left, BlockState right) {
        if (left.getBlock() == Blocks.AIR || right.getBlock() == Blocks.AIR) {
            return BeachpartyUtil.LineConnectingType.NONE;
        }

        boolean shape_left_same = isConnectable(left, state);
        boolean shape_right_same = isConnectable(right, state);

        if (shape_left_same && shape_right_same) {
            return BeachpartyUtil.LineConnectingType.MIDDLE;
        } else if (shape_left_same) {
            return BeachpartyUtil.LineConnectingType.LEFT;
        } else if (shape_right_same) {
            return BeachpartyUtil.LineConnectingType.RIGHT;
        }
        return BeachpartyUtil.LineConnectingType.NONE;
    }

    protected boolean isConnectable(BlockState state1, BlockState state2) {
        return (state1.getBlock() == state2.getBlock() && state1.getValue(FACING) == state2.getValue(FACING));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}