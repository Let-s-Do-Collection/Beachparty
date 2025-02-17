package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"deprecation", "unused"})
public class BeachParasolBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public BeachParasolBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false));
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, int i) {
        BlockPos blockPos2 = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(HALF, DoubleBlockHalf.LOWER)), i);
        levelAccessor.setBlock(blockPos2, copyWaterloggedFrom(levelAccessor, blockPos2, blockState.setValue(HALF, DoubleBlockHalf.UPPER)), i);
    }

    private static BlockState copyWaterloggedFrom(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, levelReader.isWaterAt(blockPos)) : blockState;
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (blockState2.is(blockState.getBlock()) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState3 = blockState2.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockPos2, blockState3, 35);
                level.levelEvent(player, 2001, blockPos2, Block.getId(blockState2));
            }
        }

    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0.4375, 0, 0.4375, 0.5625, 1, 0.5625));
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, net.minecraft.world.phys.BlockHitResult blockHitResult) {
        ItemStack heldItem = player.getItemInHand(interactionHand);

        if (heldItem.isEmpty() && player.isCrouching()) {
            boolean isOpen = !blockState.getValue(OPEN);
            level.setBlockAndUpdate(blockPos, blockState.setValue(OPEN, isOpen));
            DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
            BlockPos otherPartPos = (doubleBlockHalf == DoubleBlockHalf.LOWER) ? blockPos.above() : blockPos.below();
            BlockState otherPartState = level.getBlockState(otherPartPos);

            if (otherPartState.is(this) && otherPartState.getValue(HALF) != doubleBlockHalf) {
                level.setBlockAndUpdate(otherPartPos, otherPartState.setValue(OPEN, isOpen));
            }

            level.playSound(player, blockPos, isOpen ? SoundEvents.BAMBOO_WOOD_DOOR_OPEN : SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);

            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                double xOffset = level.random.nextGaussian() * 0.02D;
                double yOffset = level.random.nextGaussian() * 0.02D;
                double zOffset = level.random.nextGaussian() * 0.02D;
                level.playLocalSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D,
                        SoundEvents.CHERRY_WOOD_DOOR_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                level.addParticle(ParticleTypes.CRIT, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, xOffset, yOffset, zOffset);
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.UP && (!blockState2.is(this) || blockState2.getValue(HALF) == doubleBlockHalf)) {
            return Blocks.AIR.defaultBlockState();
        } else if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        BlockPos blockPos2 = blockPos.above();
        level.setBlock(blockPos2, copyWaterloggedFrom(level, blockPos2, blockState.setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(blockState, levelReader, blockPos);
        } else {
            BlockState blockState2 = levelReader.getBlockState(blockPos.below());
            return blockState2.is(this) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide) {
            DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
            BlockPos otherPartPos = (doubleBlockHalf == DoubleBlockHalf.LOWER) ? blockPos.above() : blockPos.below();
            BlockState otherPartState = level.getBlockState(otherPartPos);

            if (otherPartState.is(this) && otherPartState.getValue(HALF) != doubleBlockHalf) {
                level.destroyBlock(otherPartPos, true);
            }

            dropResources(blockState, level, blockPos, null, player, player.getMainHandItem());
        }

        super.playerWillDestroy(level, blockPos, blockState, player);
    }


    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, itemStack);
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            BlockPos blockPos2 = blockPos.above();
            BlockState blockState2 = level.getBlockState(blockPos2);

            if (blockState2.is(this) && blockState2.getValue(HALF) == DoubleBlockHalf.UPPER) {
                level.destroyBlock(blockPos2, true);
            }
        } else {
            BlockPos blockPos1 = blockPos.below();
            BlockState blockState1 = level.getBlockState(blockPos1);

            if (blockState1.is(this) && blockState1.getValue(HALF) == DoubleBlockHalf.LOWER) {
                level.destroyBlock(blockPos1, true);
            }
        }
    }


    @Override
    public long getSeed(BlockState blockState, BlockPos blockPos) {
        return Mth.getSeed(blockPos.getX(), blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), blockPos.getZ());
    }

    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
        tooltip.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }
}
