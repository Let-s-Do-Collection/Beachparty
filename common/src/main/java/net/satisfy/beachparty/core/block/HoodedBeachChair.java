package net.satisfy.beachparty.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class HoodedBeachChair extends LineConnectingBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Supplier<VoxelShape> topRightShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 0.9375, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.75, 0.75, 0.9375, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.875, 0.8125, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.874375, 0, 0, 0.936875, 0.8125, 0.75), BooleanOp.OR);
        return shape;
    };
    private static final Supplier<VoxelShape> topLeftShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0, 1, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.75, 0.75, 1, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0, 1, 0.8125, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.063125, 0, 0, 0.125625, 0.8125, 0.75), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_RIGHT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = direction == Direction.NORTH || direction == Direction.SOUTH
                    ? topLeftShapeSupplier.get() : topRightShapeSupplier.get();
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, shape));
        }
    });
    public static final Map<Direction, VoxelShape> TOP_LEFT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = direction == Direction.NORTH || direction == Direction.SOUTH
                    ? topRightShapeSupplier.get() : topLeftShapeSupplier.get();
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, shape));
        }
    });
    private static final Supplier<VoxelShape> topMiddleShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.75, 0.75, 1, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.8125, 0.0625), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_MIDDLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, topMiddleShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> bottomRightShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.1875, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.25, 0.875, 0.375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.375, 0.25, 0.875, 0.5, 0.875), BooleanOp.OR);
        return shape;
    };
    private static final Supplier<VoxelShape> bottomLeftShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.1875, 0.125, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.25, 1, 0.375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.375, 0.25, 1, 0.5, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> BOTTOM_RIGHT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = direction == Direction.NORTH || direction == Direction.SOUTH
                    ? bottomLeftShapeSupplier.get() : bottomRightShapeSupplier.get();
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, shape));
        }
    });
    public static final Map<Direction, VoxelShape> BOTTOM_LEFT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = direction == Direction.NORTH || direction == Direction.SOUTH
                    ? bottomRightShapeSupplier.get() : bottomLeftShapeSupplier.get();
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, shape));
        }
    });
    private static final Supplier<VoxelShape> bottomMiddleShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.25, 1, 0.375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.375, 0.25, 1, 0.5, 0.875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> BOTTOM_MIDDLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, bottomMiddleShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> topShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0, 0.9375, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.75, 0.75, 0.9375, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875625, 0, 0, 0.938125, 0.8125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0, 0.875, 0.8125, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.063125, 0, 0, 0.125625, 0.8125, 0.75), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, topShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> bottomShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.1875, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.1875, 0.125, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.25, 0.875, 0.375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.375, 0.25, 0.875, 0.5, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.125, 0.875, 1, 0.1875), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.SOUTH, direction, bottomShapeSupplier.get()));
        }
    });


    public HoodedBeachChair(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, BeachpartyUtil.LineConnectingType.NONE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos upperPos = pos.above();
        BlockState upperState = state.setValue(HALF, DoubleBlockHalf.UPPER);
        world.setBlock(upperPos, upperState, 3);

        updateTypeProperty(world, pos, state);
        updateTypeProperty(world, upperPos, upperState);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && state.getBlock() != newState.getBlock()) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
            BlockState otherState = world.getBlockState(otherPos);

            if (otherState.getBlock() == this && otherState.getValue(HALF) != half) {
                world.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos basePos = (half == DoubleBlockHalf.LOWER) ? pos : pos.below();
        return BeachpartyUtil.onUse(world, player, hand, new BlockHitResult(hit.getLocation(), hit.getDirection(), basePos, hit.isInside()), 0);
    }

    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        BlockPos basePos = (state.getValue(HALF) == DoubleBlockHalf.LOWER) ? pos : pos.below();
        return basePos.asLong();
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos basePos = (half == DoubleBlockHalf.LOWER) ? pos : pos.below();
        BlockState baseState = world.getBlockState(basePos);

        updateTypeProperty(world, basePos, baseState);

        BlockPos otherPos = basePos.above();
        BlockState otherState = world.getBlockState(otherPos);
        if (otherState.getBlock() == this) {
            world.setBlock(otherPos, otherState.setValue(TYPE, baseState.getValue(TYPE)), 3);
        }
    }

    private void updateTypeProperty(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BeachpartyUtil.LineConnectingType type = getType(state, world, pos, facing);
        if (state.getValue(TYPE) != type) {
            world.setBlock(pos, state.setValue(TYPE, type), 3);
        }
    }

    private BeachpartyUtil.LineConnectingType getType(BlockState state, Level world, BlockPos pos, Direction facing) {
        BlockPos leftPos;
        BlockPos rightPos;
        DoubleBlockHalf half = state.getValue(HALF);

        switch (facing) {
            case EAST -> {
                leftPos = pos.relative(Direction.SOUTH);
                rightPos = pos.relative(Direction.NORTH);
            }
            case SOUTH -> {
                leftPos = pos.relative(Direction.EAST);
                rightPos = pos.relative(Direction.WEST);
            }
            case WEST -> {
                leftPos = pos.relative(Direction.NORTH);
                rightPos = pos.relative(Direction.SOUTH);
            }
            default -> {
                leftPos = pos.relative(Direction.WEST);
                rightPos = pos.relative(Direction.EAST);
            }
        }

        if (half == DoubleBlockHalf.UPPER) {
            leftPos = leftPos.above();
            rightPos = rightPos.above();
        }

        BlockState leftState = world.getBlockState(leftPos);
        BlockState rightState = world.getBlockState(rightPos);

        return determineType(state, leftState, rightState);
    }

    private BeachpartyUtil.LineConnectingType determineType(BlockState state, BlockState leftState, BlockState rightState) {
        boolean connectLeft = isConnectable(state, leftState);
        boolean connectRight = isConnectable(state, rightState);

        if (connectLeft && connectRight) {
            return BeachpartyUtil.LineConnectingType.MIDDLE;
        } else if (connectLeft) {
            return BeachpartyUtil.LineConnectingType.RIGHT;
        } else if (connectRight) {
            return BeachpartyUtil.LineConnectingType.LEFT;
        } else {
            return BeachpartyUtil.LineConnectingType.NONE;
        }
    }

    protected boolean isConnectable(BlockState selfState, BlockState otherState) {
        if (otherState.getBlock() != this) {
            return false;
        }
        return selfState.getValue(FACING) == otherState.getValue(FACING)
                && selfState.getValue(HALF) == otherState.getValue(HALF);
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        BlockState otherState = world.getBlockState(otherPos);

        if (otherState.getBlock() == this && otherState.getValue(HALF) != half) {
            world.destroyBlock(otherPos, false);
        }

        if (half == DoubleBlockHalf.UPPER && world instanceof Level level) {
            Block.popResource(level, pos, new ItemStack(this.asItem()));
        }

        super.destroy(world, pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        DoubleBlockHalf half = state.getValue(HALF);
        BeachpartyUtil.LineConnectingType type = state.getValue(TYPE);

        if (half == DoubleBlockHalf.UPPER) {
            return switch (type) {
                case RIGHT -> TOP_RIGHT_SHAPE.get(direction);
                case LEFT -> TOP_LEFT_SHAPE.get(direction);
                case MIDDLE -> TOP_MIDDLE_SHAPE.get(direction);
                default -> TOP_SHAPE.get(direction);
            };
        } else {
            return switch (type) {
                case RIGHT -> BOTTOM_RIGHT_SHAPE.get(direction);
                case LEFT -> BOTTOM_LEFT_SHAPE.get(direction);
                case MIDDLE -> BOTTOM_MIDDLE_SHAPE.get(direction);
                default -> BOTTOM_SHAPE.get(direction);
            };
        }
    }
}