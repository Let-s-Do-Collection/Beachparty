package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class PalmTableBlock extends LineConnectingBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    public static final VoxelShape TOP_SHAPE;
    public static final VoxelShape[] LEG_SHAPES;

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP_SHAPE = box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);
        LEG_SHAPES = new VoxelShape[]{
                box(0, 0, 0, 2, 13, 2),
                box(14, 0, 0, 16, 13, 2),
                box(14, 0, 14, 16, 13, 16),
                box(0, 0, 14, 2, 13, 16)
        };
    }

    public PalmTableBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis().isHorizontal()) {
            Direction facing = state.getValue(FACING);
            BlockPos leftPos = pos.relative(facing.getCounterClockWise());
            BlockPos rightPos = pos.relative(facing.getClockWise());
            boolean leftConnected = world.getBlockState(leftPos).getBlock() == this;
            boolean rightConnected = world.getBlockState(rightPos).getBlock() == this;
            if (leftConnected && rightConnected) {
                state = state.setValue(TYPE, BeachpartyUtil.LineConnectingType.MIDDLE);
            } else if (leftConnected) {
                state = state.setValue(TYPE, BeachpartyUtil.LineConnectingType.RIGHT);
            } else if (rightConnected) {
                state = state.setValue(TYPE, BeachpartyUtil.LineConnectingType.LEFT);
            } else {
                state = state.setValue(TYPE, BeachpartyUtil.LineConnectingType.NONE);
            }
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        BeachpartyUtil.LineConnectingType type = state.getValue(TYPE);

        if (type == BeachpartyUtil.LineConnectingType.MIDDLE) {
            return TOP_SHAPE;
        }

        if ((direction == Direction.NORTH && type == BeachpartyUtil.LineConnectingType.RIGHT) ||
                (direction == Direction.SOUTH && type == BeachpartyUtil.LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[1], LEG_SHAPES[2]);
        }

        if ((direction == Direction.NORTH && type == BeachpartyUtil.LineConnectingType.LEFT) ||
                (direction == Direction.SOUTH && type == BeachpartyUtil.LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[3]);
        }

        if ((direction == Direction.EAST && type == BeachpartyUtil.LineConnectingType.LEFT) ||
                (direction == Direction.WEST && type == BeachpartyUtil.LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[1]);
        }

        if ((direction == Direction.EAST && type == BeachpartyUtil.LineConnectingType.RIGHT) ||
                (direction == Direction.WEST && type == BeachpartyUtil.LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[2], LEG_SHAPES[3]);
        }

        return Shapes.or(TOP_SHAPE, LEG_SHAPES);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
