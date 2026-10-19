package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.foundation.block.ChairBlock;
import net.satisfy.foundation.seat.SeatUtil;
import net.satisfy.foundation.util.ShapeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PalmChairBlock extends ChairBlock {
    private static final Map<Direction, VoxelShape> PALM_SHAPE_LOWER_MAP = net.minecraft.Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, makeLowerShape()));
        }
    });
    private static final Map<Direction, VoxelShape> PALM_SHAPE_UPPER_MAP = net.minecraft.Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, makeUpperShape()));
        }
    });

    public PalmChairBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(DyeableFurniture.COLOR, DyeableFurniture.DEFAULT_COLOR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DyeableFurniture.COLOR);
    }

    private static VoxelShape makeLowerShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.5625, 0.1875, 0.8125, 0.625, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.1875, 0.8125, 0.5625, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.3125, 0.5625, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.6875, 0.3125, 0.5625, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.625, 0.6875, 0.8125, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.625, 0.6875, 0.3125, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.5, 0.25, 0.75, 0.5625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.71875, 0.6875, 1, 0.78125), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeUpperShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.6875, 0.3125, 0.3125, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.3125, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.71875, 0.6875, 0.3125, 0.78125), BooleanOp.OR);
        return shape;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (DyeableFurniture.canDye(itemStack, blockState)) {
            return DyeableFurniture.dye(itemStack, blockState, level, blockPos, player, blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? blockPos.above() : blockPos.below());
        }
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SeatUtil.onUse(level, player, interactionHand, blockHitResult, -0.3);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? PALM_SHAPE_UPPER_MAP.get(state.getValue(FACING)) : PALM_SHAPE_LOWER_MAP.get(state.getValue(FACING));
    }
}
