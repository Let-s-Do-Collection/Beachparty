package net.satisfy.beachparty.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.block.entity.BeachGoalBlockEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TallBeachGoalBlock extends BaseEntityBlock {
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE_BOTTOM_LEFT = makeBottomLeftShape();
    private static final VoxelShape SHAPE_BOTTOM_RIGHT = makeBottomRightShape();
    private static final VoxelShape SHAPE_TOP_LEFT = makeTopLeftShape();
    private static final VoxelShape SHAPE_TOP_RIGHT = makeTopRightShape();

    public TallBeachGoalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, Part.BOTTOM_LEFT).setValue(FACING, Direction.NORTH));
    }

    public static final MapCodec<TallBeachGoalBlock> CODEC = simpleCodec(TallBeachGoalBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    private static VoxelShape makeBottomLeftShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.4375, 0.125, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.4375, 1, 1, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomRightShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.4375, 1, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.5, 0.4375, 0.875, 1, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeTopLeftShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.4375, 1, 0.875, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeTopRightShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.4375, 1, 0.875, 0.5625), BooleanOp.OR);
        return shape;
    }

    public static final Map<Direction, Map<Part, VoxelShape>> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            Map<Part, VoxelShape> partShapeMap = new HashMap<>();
            partShapeMap.put(Part.BOTTOM_LEFT, BeachpartyUtil.rotateShape(Direction.NORTH, direction, SHAPE_BOTTOM_LEFT));
            partShapeMap.put(Part.BOTTOM_RIGHT, BeachpartyUtil.rotateShape(Direction.NORTH, direction, SHAPE_BOTTOM_RIGHT));
            partShapeMap.put(Part.TOP_LEFT, BeachpartyUtil.rotateShape(Direction.NORTH, direction, SHAPE_TOP_LEFT));
            partShapeMap.put(Part.TOP_RIGHT, BeachpartyUtil.rotateShape(Direction.NORTH, direction, SHAPE_TOP_RIGHT));
            map.put(direction, partShapeMap);
        }
    });

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, EntityTypeRegistry.BEACH_GOAL_BLOCK_ENTITY.get(), BeachGoalBlockEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Direction direction = context.getHorizontalDirection().getOpposite();

        if (!canPlaceAt(world, pos, direction)) {
            return null;
        }

        return this.defaultBlockState().setValue(PART, Part.BOTTOM_LEFT).setValue(FACING, direction);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(FACING);

        world.setBlock(pos.above(), this.defaultBlockState().setValue(PART, Part.TOP_LEFT).setValue(FACING, direction), 3);
        world.setBlock(pos.relative(direction.getClockWise()), this.defaultBlockState().setValue(PART, Part.BOTTOM_RIGHT).setValue(FACING, direction), 3);
        world.setBlock(pos.relative(direction.getClockWise()).above(), this.defaultBlockState().setValue(PART, Part.TOP_RIGHT).setValue(FACING, direction), 3);

        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.playSound(null, pos, SoundEvents.CHERRY_WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (placer instanceof Player player && !player.isCreative()) {
            stack.shrink(1);
        }
    }

    private boolean canPlaceAt(Level world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos).canBeReplaced() &&
                world.getBlockState(pos.above()).canBeReplaced() &&
                world.getBlockState(pos.relative(direction.getClockWise())).canBeReplaced() &&
                world.getBlockState(pos.relative(direction.getClockWise()).above()).canBeReplaced();
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        state.getBlock();
        newState.getBlock();
        super.onRemove(state, world, pos, newState, isMoving);
    }

    private BlockPos getBasePos(BlockState state, BlockPos pos) {
        Part part = state.getValue(PART);
        Direction direction = state.getValue(FACING);
        return switch (part) {
            case BOTTOM_LEFT -> pos;
            case TOP_LEFT -> pos.below();
            case BOTTOM_RIGHT -> pos.relative(direction.getCounterClockWise(), 1);
            case TOP_RIGHT -> pos.relative(direction.getCounterClockWise(), 1).below();
        };
    }

    private void destroyAdjacentBlocks(Level world, BlockPos basePos) {
        var blockstate = world.getBlockState(basePos);
        var facing = blockstate.getValue(FACING);

        world.removeBlock(basePos, false);
        world.removeBlock(basePos.above(), false);
        world.removeBlock(basePos.relative(facing.getClockWise(), 1), false);
        world.removeBlock(basePos.relative(facing.getClockWise(), 1).above(), false);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        Part part = state.getValue(PART);
        return SHAPE.get(direction).get(part);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        state.getValue(PART);
        BlockPos basePos = getBasePos(world.getBlockState(pos), pos);
        var entity = world.getBlockEntity(basePos);
        assert entity instanceof BeachGoalBlockEntity;
        BeachGoalBlockEntity bountyBoardBlockEntity = (BeachGoalBlockEntity) entity;
        if (world.isClientSide()) {
            return state;
        }
        var blockEntityTag = new CompoundTag();
        bountyBoardBlockEntity.saveAdditional(blockEntityTag, world.registryAccess());
        var tag = new CompoundTag();
        tag.put("BlockEntityTag", blockEntityTag);
        var stack = new ItemStack(ObjectRegistry.BEACH_GOAL.get());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
        destroyAdjacentBlocks(world, basePos);
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockPos != getBasePos(blockState, blockPos)) {
            return null;
        }
        return new BeachGoalBlockEntity(blockPos, blockState);
    }

    public enum Part implements StringRepresentable {
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right"),
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
