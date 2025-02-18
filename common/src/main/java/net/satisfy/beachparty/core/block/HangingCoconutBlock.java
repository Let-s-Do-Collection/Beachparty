package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class HangingCoconutBlock extends FallingBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape SHAPE = Block.box(4.0, 7.0, 4.0, 12.0, 15.0, 12.0);
    private static final float FALL_DAMAGE = 2.0F;
    private static final int FALL_DAMAGE_THRESHOLD = 40;
    private static final float COCONUT_SMASH_VOLUME = 0.7f;
    private static final float COCONUT_SMASH_PITCH_BASE = 0.9f;

    public HangingCoconutBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return aboveState.is(ObjectRegistry.PALM_LEAVES.get());
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP && !this.canSurvive(state, level, currentPos)) {
            if (level instanceof ServerLevel serverLevel) {
                createFallingBlock(serverLevel, currentPos);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        entity.setHurtsEntities(FALL_DAMAGE, FALL_DAMAGE_THRESHOLD);
    }

    @Override
    public @NotNull DamageSource getFallDamageSource(Entity entity) {
        return entity.damageSources().fallingBlock(entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (state.getValue(AGE) == 2 && rand.nextInt(16) == 0) {
            double x = pos.getX() + rand.nextDouble();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextDouble();
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return 6636321;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (random.nextInt(3) == 0 && age < 2) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos) && canFall(level, pos)) {
            createFallingBlock(level, pos);
        } else if (random.nextInt(100) < 5 && canFall(level, pos)) {
            createFallingBlock(level, pos);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        level.playSound(null, pos, SoundEvents.BAMBOO_BREAK, SoundSource.BLOCKS, COCONUT_SMASH_VOLUME, COCONUT_SMASH_PITCH_BASE + level.getRandom().nextFloat() * 0.2f);
        if (level instanceof ServerLevel) {
            RandomSource random = level.getRandom();
            int chance = random.nextInt(100);
            if (chance < 5) {
                popResource(level, pos, new ItemStack(ObjectRegistry.PALM_SPROUT.get().asItem()));
            } else if (chance < 5 + 20) {
                popResource(level, pos, new ItemStack(ObjectRegistry.COCONUT_OPEN.get().asItem()));
            } else if (chance < 5 + 20 + 75) {
                popResource(level, pos, new ItemStack(ObjectRegistry.COCONUT.get().asItem()));
            }
        }
    }

    private boolean canFall(ServerLevel level, BlockPos pos) {
        return pos.getY() >= level.getMinBuildHeight() && isFree(level.getBlockState(pos.below()));
    }

    private void createFallingBlock(ServerLevel level, BlockPos pos) {
        if (!level.getEntitiesOfClass(FallingBlockEntity.class, new net.minecraft.world.phys.AABB(pos)).isEmpty()) {
            return;
        }

        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
        this.falling(fallingBlockEntity);
        level.addFreshEntity(fallingBlockEntity);
        level.removeBlock(pos, false);
    }
}
