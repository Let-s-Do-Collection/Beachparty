package net.satisfy.beachparty.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class SandBucketBlock extends HorizontalDirectionalBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> box(4, 0, 6, 10, 6, 12);

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public SandBucketBlock(Properties settings) {
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
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        Block emptySandBucketBlock = ObjectRegistry.EMPTY_SAND_BUCKET_BLOCK.get();
        Block sandBucketBlock = ObjectRegistry.SAND_BUCKET_BLOCK.get();

        if (state.getBlock() == emptySandBucketBlock && itemStack.getItem() == Items.SAND) {
            itemStack.shrink(1);
            world.setBlockAndUpdate(pos, sandBucketBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)));
            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, hit);
    }
}
