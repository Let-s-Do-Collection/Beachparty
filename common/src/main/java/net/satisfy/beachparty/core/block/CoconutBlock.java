package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class CoconutBlock extends Block {
    public static final IntegerProperty STACK = IntegerProperty.create("stack", 1, 3);
    private final VoxelShape SHAPE = Shapes.box(0.2, 0, 0.2, 0.8, 0.9, 0.8);

    public CoconutBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.defaultBlockState().setValue(STACK, 1));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == this.asItem()) {
            if (state.getBlock() instanceof CoconutBlock && state.getValue(STACK) < 3) {
                world.setBlock(pos, state.setValue(STACK, state.getValue(STACK) + 1), Block.UPDATE_ALL);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        } else if (stack.isEmpty() && !player.getOffhandItem().isEmpty()) {
            if (!player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }
            if (state.getValue(STACK) >= 1) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide) {
                world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(STACK, 1));
                player.getOffhandItem().shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else if (stack.isEmpty()) {
            if (state.getValue(STACK) > 1) {
                world.setBlock(pos, state.setValue(STACK, state.getValue(STACK) - 1), Block.UPDATE_ALL);
            } else if (state.getValue(STACK) == 1) {
                world.destroyBlock(pos, false);
            }
            player.addItem(this.asItem().getDefaultInstance());
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STACK);
    }


}
