package net.satisfy.beachparty.core.block.furniture;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TikiChairBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0.3125, 0.1875, 0.375, 0.375, 0.25, 0.625));
        shape = Shapes.or(shape, Shapes.box(0.625, 0, 0.625, 0.6875, 0.5625, 0.6875));
        shape = Shapes.or(shape, Shapes.box(0.625, 0, 0.3125, 0.6875, 0.5625, 0.375));
        shape = Shapes.or(shape, Shapes.box(0.625, 0.1875, 0.375, 0.6875, 0.25, 0.625));
        shape = Shapes.or(shape, Shapes.box(0.375, 0.3125, 0.3125, 0.625, 0.375, 0.375));
        shape = Shapes.or(shape, Shapes.box(0.375, 0.3125, 0.625, 0.625, 0.375, 0.6875));
        shape = Shapes.or(shape, Shapes.box(0.3125, 0, 0.625, 0.375, 0.5625, 0.6875));
        shape = Shapes.or(shape, Shapes.box(0.3125, 0, 0.3125, 0.375, 0.5625, 0.375));
        shape = Shapes.or(shape, Shapes.box(0.25, 0.5625, 0.3125, 0.75, 0.6875, 0.6875));
        shape = Shapes.or(shape, Shapes.box(0.3125, 0.5625, 0.25, 0.6875, 0.6875, 0.3125));
        shape = Shapes.or(shape, Shapes.box(0.3125, 0.5625, 0.6875, 0.6875, 0.6875, 0.75));
        shape = Shapes.or(shape, Shapes.box(0.3125, 0.6875, 0.3125, 0.6875, 0.75, 0.6875));


        return shape;
    };
    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public TikiChairBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return BeachpartyUtil.onUse(world, player, hand, hit, 0);
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

