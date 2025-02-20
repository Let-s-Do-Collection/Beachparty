package net.satisfy.beachparty.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class SandBucketBlock extends HorizontalDirectionalBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> box(4, 0, 6, 10, 8, 12);

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
        Block emptySandBucketBlock = ObjectRegistry.SAND_BUCKET_BLOCK_EMPTY.get();
        Block sandBucketBlock = ObjectRegistry.SAND_BUCKET_BLOCK_FILLED.get();

        if (state.getBlock() == emptySandBucketBlock && itemStack.getItem() == Items.SAND) {
            itemStack.shrink(1);
            world.setBlockAndUpdate(pos, sandBucketBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)));
            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, hit);
    }

    public static class SandCastleBlock extends Block {
        public static final BooleanProperty TALL_TOWER;
        public static final VoxelShape TALL_TOWER_SHAPE = Block.box(11.0, 0.0, 11.0, 15.0, 15.0, 15.0);
        public static final BooleanProperty RIGHT_TOWER;
        public static final VoxelShape RIGHT_TOWER_SHAPE = Block.box(1.0, 1.0, 11.0, 5.0, 12.0, 15.0);
        public static final BooleanProperty TOP_TOWER;
        public static final VoxelShape TOP_TOWER_SHAPE = Block.box(5.0, 6.0, 5.0, 11.0, 9.0, 11.0);
        public static final BooleanProperty LEFT_TOWER;
        public static final VoxelShape LEFT_TOWER_SHAPE = Block.box(11.0, 1.0, 1.0, 15.0, 12.0, 5.0);
        private static final VoxelShape BASE_SHAPE = Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0), Block.box(2.0, 1.0, 2.0, 14.0, 6.0, 14.0));
        public static final BooleanProperty PETRIFIED;

        static {
            TALL_TOWER = BooleanProperty.create("tall");
            RIGHT_TOWER = BooleanProperty.create("right");
            TOP_TOWER = BooleanProperty.create("top");
            LEFT_TOWER = BooleanProperty.create("left");
            PETRIFIED = BooleanProperty.create("petrified");
        }

        public SandCastleBlock(Properties settings) {
            super(settings.randomTicks());
            this.registerDefaultState(this.stateDefinition.any()
                    .setValue(TALL_TOWER, false)
                    .setValue(RIGHT_TOWER, false)
                    .setValue(TOP_TOWER, false)
                    .setValue(LEFT_TOWER, false)
                    .setValue(PETRIFIED, false));
        }

        @Override
        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            VoxelShape shape = Shapes.or(BASE_SHAPE);
            if (state.getValue(TALL_TOWER)) {
                shape = Shapes.or(shape, TALL_TOWER_SHAPE);
            }
            if (state.getValue(RIGHT_TOWER)) {
                shape = Shapes.or(shape, RIGHT_TOWER_SHAPE);
            }
            if (state.getValue(LEFT_TOWER)) {
                shape = Shapes.or(shape, LEFT_TOWER_SHAPE);
            }
            if (state.getValue(TOP_TOWER)) {
                shape = Shapes.or(shape, TOP_TOWER_SHAPE);
            }
            return shape;
        }

        @Override
        public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack handStack = player.getItemInHand(hand);

            if (PotionUtils.getPotion(handStack) == Potions.WATER && !state.getValue(PETRIFIED)) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
                world.setBlockAndUpdate(pos, state.setValue(PETRIFIED, true));

                if (world.isClientSide) {
                    BlockState smoothSandState = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                    for (int i = 0; i < 8; i++) {
                        world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, smoothSandState),
                                pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5,
                                pos.getY() + 1.0,
                                pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5,
                                0, 0.1, 0);
                    }
                    for (int i = 0; i < 6; i++) {
                        world.addParticle(ParticleTypes.SPLASH,
                                pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5,
                                pos.getY() + 1.0,
                                pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5,
                                0, 0.1, 0);
                    }
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }

            if (handStack.getItem() == ObjectRegistry.SAND_BUCKET_FILLED.get() && !hasAllTowers(state)) {
                BooleanProperty tower = getTowerHitPos(hit);
                if (!state.getValue(tower)) {
                    world.setBlockAndUpdate(pos, state.setValue(tower, true));
                    exchangeStack(handStack, player, new ItemStack(ObjectRegistry.SAND_BUCKET_EMPTY.get()));

                    if (world.isClientSide) {
                        for (int i = 0; i < 8; i++) {
                            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState()), pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, pos.getY() + 1.0, pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, 0, 0.1, 0);
                        }
                        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            } else if (handStack.getItem() == ObjectRegistry.SAND_BUCKET_EMPTY.get()) {
                if (hasNoTowers(state)) {
                    world.destroyBlock(pos, false);
                    exchangeStack(handStack, player, new ItemStack(ObjectRegistry.SAND_BUCKET_FILLED.get()));

                    if (world.isClientSide) {
                        for (int i = 0; i < 6; i++) {
                            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState()), pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, pos.getY() + 1.0, pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, 0, 0.1, 0);
                        }
                        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                } else {
                    BooleanProperty tower = getTowerHitPos(hit);
                    if (state.getValue(tower)) {
                        world.setBlockAndUpdate(pos, state.setValue(tower, false));
                        exchangeStack(handStack, player, new ItemStack(ObjectRegistry.SAND_BUCKET_FILLED.get()));

                        if (world.isClientSide) {
                            for (int i = 0; i < 6; i++) {
                                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState()), pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, pos.getY() + 1.0, pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5, 0, 0.1, 0);
                            }
                            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f, false);
                        }
                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
            return InteractionResult.PASS;
        }

        @Override
        public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
            if (!state.getValue(PETRIFIED)) {
                world.setBlockAndUpdate(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState());
            } else if (entity.getType() == EntityType.ZOMBIE) {
                world.destroyBlock(pos, true);
            }
        }

        @Override
        public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
            if (!state.getValue(PETRIFIED) && (world.isRainingAt(pos) || world.isRainingAt(pos.above()))) {
                world.setBlockAndUpdate(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState());
                return;
            }
            if (!state.canSurvive(world, pos)) {
                world.destroyBlock(pos, true);
            }
        }

        private void exchangeStack(ItemStack handStack, Player player, ItemStack possibleReturnStack) {
            ItemStack returnStack = !player.getAbilities().instabuild ? possibleReturnStack : handStack;
            if (player.isCreative()) {
                if (!player.getInventory().contains(returnStack)) {
                    player.getInventory().add(returnStack);
                }
                return;
            }
            Inventory inventory = player.getInventory();
            int slot = inventory.findSlotMatchingItem(handStack);
            handStack.shrink(1);
            if (player.getInventory().getItem(slot).isEmpty()) {
                if (!inventory.add(slot, returnStack)) {
                    player.drop(returnStack, false);
                }
            } else {
                if (!inventory.add(returnStack)) {
                    player.drop(returnStack, false);
                }
            }
        }

        private BooleanProperty getTowerHitPos(BlockHitResult hitResult) {
            double x = hitResult.getLocation().x();
            double z = hitResult.getLocation().z();

            double relX = x - hitResult.getBlockPos().getX();
            double relZ = z - hitResult.getBlockPos().getZ();

            if (relX >= 0.5 && relZ >= 0.5) {
                return TALL_TOWER;
            } else if (relX < 0.5 && relZ >= 0.5) {
                return RIGHT_TOWER;
            } else if (relX >= 0.5 && relZ < 0.5) {
                return LEFT_TOWER;
            } else {
                return TOP_TOWER;
            }
        }

        private boolean hasAllTowers(BlockState state) {
            return state.getValue(TALL_TOWER) && state.getValue(RIGHT_TOWER) && state.getValue(TOP_TOWER) && state.getValue(LEFT_TOWER);
        }

        private boolean hasNoTowers(BlockState state) {
            return !state.getValue(TALL_TOWER) && !state.getValue(RIGHT_TOWER) && !state.getValue(TOP_TOWER) && !state.getValue(LEFT_TOWER);
        }

        @Override
        public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
            if (!state.canSurvive(world, pos)) {
                world.scheduleTick(pos, this, 1);
            }
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }

        @Override
        public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
            BlockPos blockPos = pos.below();
            return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, Direction.UP);
        }

        @Override
        public @NotNull ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
            return new ItemStack(ObjectRegistry.SAND_BUCKET_FILLED.get());
        }

        public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
            return false;
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(TALL_TOWER, RIGHT_TOWER, TOP_TOWER, LEFT_TOWER, PETRIFIED);
        }
    }

    public static class SandPileBlock extends SandBlock {
        private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);

        public SandPileBlock(int color, Properties settings) {
            super(color, settings);
        }

        public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }

        public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
            if (entity instanceof LivingEntity && entity.getType() != EntityType.TURTLE) {
                entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
            }
        }
    }
}
