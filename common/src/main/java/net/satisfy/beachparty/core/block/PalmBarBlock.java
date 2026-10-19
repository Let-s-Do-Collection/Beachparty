package net.satisfy.beachparty.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.foundation.util.ShapeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PalmBarBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0, 0, 0.1875, 1, 1, 1));
        shape = Shapes.or(shape, Shapes.box(0, 0.8125, 0, 1, 1, 0.1875));
        return shape;
    };
    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public PalmBarBlock(Properties settings) {
        super(settings);
    }

    public static final MapCodec<PalmBarBlock> CODEC = simpleCodec(PalmBarBlock::new);

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof PalmBarBlockEntity bar) {
                Containers.dropContents(world, pos, bar.getDrops());
                bar.clearContents();
            }
            super.onRemove(state, world, pos, newState, moved);
            if (hasGlass(world, pos)) {
                world.removeBlock(pos.above(), false);
            }
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hasGlass(level, pos)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return useItem(stack, level, pos, player);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (hasGlass(level, pos) && !(level.getBlockEntity(pos) instanceof PalmBarBlockEntity bar && bar.hasLeftovers())) {
            return InteractionResult.PASS;
        }
        return useEmpty(level, pos, player);
    }

    public static ItemInteractionResult useItem(ItemStack stack, Level level, BlockPos pos, Player player) {
        if (!(level.getBlockEntity(pos) instanceof PalmBarBlockEntity bar)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            updateGlass(level, pos, bar);
        }
        if (bar.hasLeftovers()) {
            if (!level.isClientSide) {
                bar.takeLeftovers().forEach(leftover -> give(player, leftover));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.isEmpty() || !hasRoom(level, pos) || !bar.canAdd(stack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            ItemStack added = player.getAbilities().instabuild ? stack.copy() : stack;
            bar.add(added);
            updateGlass(level, pos, bar);
            level.playSound(null, pos.above(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.8F, 1.2F + level.random.nextFloat() * 0.3F);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    public static InteractionResult useEmpty(Level level, BlockPos pos, Player player) {
        if (!player.getMainHandItem().isEmpty() || !(level.getBlockEntity(pos) instanceof PalmBarBlockEntity bar)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            updateGlass(level, pos, bar);
        }
        if (bar.hasLeftovers()) {
            if (!level.isClientSide) {
                bar.takeLeftovers().forEach(leftover -> give(player, leftover));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (bar.isShaking(level.getGameTime())) {
            return InteractionResult.CONSUME;
        }
        if (!bar.getResult().isEmpty()) {
            if (!level.isClientSide) {
                give(player, bar.takeResult());
                updateGlass(level, pos, bar);
                level.playSound(null, pos.above(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (player.isShiftKeyDown()) {
            if (bar.getIngredients().isEmpty() || bar.isLocked()) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                give(player, bar.takeBack());
                updateGlass(level, pos, bar);
                level.playSound(null, pos.above(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.6F, 1.2F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (bar.getMatched() == null || !hasRoom(level, pos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            List<ItemStack> remainders = bar.shake(level.getGameTime());
            remainders.forEach(remainder -> give(player, remainder));
            boolean done = !bar.getResult().isEmpty();
            level.playSound(null, pos.above(), done ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, done ? 1.2F : 0.7F, done ? 1.4F : 1.3F + level.random.nextFloat() * 0.4F);
            if (level instanceof ServerLevel server) {
                server.sendParticles(done ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5, done ? 6 : 8, 0.15, 0.1, 0.15, 0.05);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void updateGlass(Level level, BlockPos pos, PalmBarBlockEntity bar) {
        BlockPos above = pos.above();
        boolean wanted = !bar.getIngredients().isEmpty() || !bar.getResult().isEmpty();
        boolean present = hasGlass(level, pos);
        if (wanted && !present && level.getBlockState(above).canBeReplaced()) {
            level.setBlock(above, ObjectRegistry.PALM_BAR_GLASS.get().defaultBlockState(), Block.UPDATE_ALL);
        } else if (!wanted && present) {
            level.removeBlock(above, false);
        }
    }

    public static boolean hasGlass(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof PalmBarGlassBlock;
    }

    public static boolean hasRoom(BlockGetter level, BlockPos pos) {
        return hasGlass(level, pos) || level.getBlockState(pos.above()).canBeReplaced();
    }

    private static void give(Player player, ItemStack stack) {
        if (!stack.isEmpty() && !player.addItem(stack)) {
            player.drop(stack, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PalmBarBlockEntity(pos, state);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }
}
