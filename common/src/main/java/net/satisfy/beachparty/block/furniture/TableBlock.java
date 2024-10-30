package net.satisfy.beachparty.block.furniture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class TableBlock extends LineConnectingBlock implements SimpleWaterloggedBlock {
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

    public TableBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any().setValue(WATERLOGGED, false)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        BeachpartyUtil.LineConnectingType type = state.getValue(TYPE);

        if (type == BeachpartyUtil.LineConnectingType.MIDDLE) {
            return TOP_SHAPE;
        }

        if ((direction == Direction.NORTH && type == BeachpartyUtil.LineConnectingType.LEFT) || (direction == Direction.SOUTH && type == BeachpartyUtil.LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[3]);
        } else if ((direction == Direction.NORTH && type == BeachpartyUtil.LineConnectingType.RIGHT) || (direction == Direction.SOUTH && type == BeachpartyUtil.LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[1], LEG_SHAPES[2]);
        } else if ((direction == Direction.EAST && type == BeachpartyUtil.LineConnectingType.LEFT) || (direction == Direction.WEST && type == BeachpartyUtil.LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[1]);
        } else if ((direction == Direction.EAST && type == BeachpartyUtil.LineConnectingType.RIGHT) || (direction == Direction.WEST && type == BeachpartyUtil.LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[2], LEG_SHAPES[3]);
        }
        return Shapes.or(TOP_SHAPE, LEG_SHAPES);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
