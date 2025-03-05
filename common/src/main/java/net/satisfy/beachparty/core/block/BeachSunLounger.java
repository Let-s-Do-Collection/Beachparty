package net.satisfy.beachparty.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BeachSunLounger extends BedBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    private static final Supplier<VoxelShape> bottomShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0, 0.25, 0, 1, 0.375, 1));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0.36875, 0.0625, 1, 0.49375, 0.9375));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0, 0.1875, 0.25, 0.125));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0.875, 0.1875, 0.25, 1));
        return shape;
    };

    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.EAST, direction, bottomShapeSupplier.get()));
        }
    });

    private static final Supplier<VoxelShape> topShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0, 0.25, 0, 0.3125, 0.375, 1));
        shape = Shapes.or(shape, Shapes.box(0, 0.36875, 0.0625, 0.25, 0.49375, 0.9375));
        shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0, 0.3125, 0.25, 0.125));
        shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.875, 0.3125, 0.25, 1));
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.EAST, direction, topShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> topShapeDownSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0, 0.25, 0, 0.375, 0.375, 1));
        shape = Shapes.or(shape, Shapes.box(0, 0.36875, 0.0625, 0.25, 0.49375, 0.9375));
        shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0, 0.3125, 0.25, 0.125));
        shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.875, 0.3125, 0.25, 1));
        shape = Shapes.or(shape, Shapes.box(0.25, 0.36875, 0.0625, 0.9375, 0.49375, 0.9375));
        shape = Shapes.or(shape, Shapes.box(0.375, 0.25, 0, 1, 0.375, 1));
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_SHAPE_DOWN = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, BeachpartyUtil.rotateShape(Direction.EAST, direction, topShapeDownSupplier.get()));
        }
    });

    public BeachSunLounger(DyeColor dyeColor, Properties properties) {
        super(dyeColor, properties.forceSolidOn());
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, BedPart.FOOT)
                .setValue(DOWN, false)
                .setValue(OCCUPIED, false));
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    public static Direction getOppositePartDirection(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(PART) == BedPart.HEAD) {
            return state.getValue(DOWN) ? TOP_SHAPE_DOWN.get(direction) : TOP_SHAPE.get(direction);
        } else {
            return BOTTOM_SHAPE.get(direction);
        }
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherPart(state.getValue(PART), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART) ? state : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            removeOtherPart(world, pos, state, player);
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getClockWise();
        BlockPos blockPos = ctx.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(direction);
        Level world = ctx.getLevel();
        return world.getBlockState(blockPos2).canBeReplaced(ctx) && world.getWorldBorder().isWithinBounds(blockPos2) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (!world.isClientSide) {
            placeOtherPart(world, pos, state);
        }
    }

    private void placeOtherPart(Level world, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.relative(state.getValue(FACING));
        world.setBlock(blockPos, state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
        world.blockUpdated(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, Block.UPDATE_ALL);
    }

    private void removeOtherPart(Level world, BlockPos pos, BlockState state, Player player) {
        BedPart bedPart = state.getValue(PART);
        if (bedPart == BedPart.FOOT) {
            BlockPos blockPos = pos.relative(getDirectionTowardsOtherPart(bedPart, state.getValue(FACING)));
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(this) && blockState.getValue(PART) == BedPart.HEAD) {
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                if (player != null)
                    world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, DOWN, OCCUPIED);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float factor) {
        super.fallOn(level, state, pos, entity, factor * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(getter, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 deltaMovement = entity.getDeltaMovement();
        if (deltaMovement.y < 0.0D) {
            double bounceFactor = entity instanceof LivingEntity ? 0.75D : 0.8D;
            entity.setDeltaMovement(deltaMovement.x, -deltaMovement.y * 0.3300000262260437D * bounceFactor, deltaMovement.z);
        }
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (villagers.isEmpty()) {
            return false;
        } else {
            villagers.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        }

        if (blockState.getValue(PART) == BedPart.HEAD && player.isShiftKeyDown()) {
            level.setBlock(blockPos, blockState.setValue(DOWN, !blockState.getValue(DOWN)), Block.UPDATE_ALL);
            level.playSound(null, blockPos, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        if (blockState.getValue(PART) != BedPart.HEAD) {
            blockPos = blockPos.relative(blockState.getValue(FACING));
            blockState = level.getBlockState(blockPos);
            if (!blockState.is(this)) {
                return InteractionResult.CONSUME;
            }
        }

        if (!blockState.getValue(DOWN)) {
            return InteractionResult.CONSUME;
        }

        if (!canSetSpawn(level)) {
            level.removeBlock(blockPos, false);
            BlockPos blockPos2 = blockPos.relative(blockState.getValue(FACING).getOpposite());
            if (level.getBlockState(blockPos2).is(this)) {
                level.removeBlock(blockPos2, false);
            }

            Vec3 vec3 = blockPos.getCenter();
            level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
            return InteractionResult.SUCCESS;
        }

        if (blockState.getValue(OCCUPIED)) {
            if (!this.kickVillagerOutOfBed(level, blockPos)) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            }
            return InteractionResult.SUCCESS;
        }

        final BlockState finalBlockState = blockState;
        final BlockPos finalBlockPos = blockPos;

        player.startSleepInBed(finalBlockPos).ifRight(success -> level.setBlock(finalBlockPos, finalBlockState.setValue(OCCUPIED, true), Block.UPDATE_ALL)).ifLeft(bedSleepingProblem -> {
            if (bedSleepingProblem.getMessage() != null) {
                player.displayClientMessage(bedSleepingProblem.getMessage(), true);
            }
        });
        return InteractionResult.SUCCESS;
    }
}

