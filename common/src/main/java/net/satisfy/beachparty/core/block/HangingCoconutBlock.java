package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
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
import org.joml.Vector3f;

@SuppressWarnings("deprecation")
public class HangingCoconutBlock extends FallingBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0, 7.0, 4.0, 12.0, 15.0, 12.0),
            Block.box(3.0, 5.0, 3.0, 13.0, 15.0, 13.0),
            Block.box(2.0, 3.0, 2.0, 14.0, 15.0, 14.0)
    };
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
        return level.getBlockState(pos.above()).is(ObjectRegistry.PALM_LEAVES.get());
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP && !this.canSurvive(state, level, currentPos)) {
            if (!level.isClientSide()) {
                level.scheduleTick(currentPos, this, 1);
            }
            return state;
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (random.nextInt(3) == 0) {
            if (age < 2) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            } else if (pos.getY() >= level.getMinBuildHeight() && isFree(level.getBlockState(pos.below()))) {
                FallingBlockEntity fallingEntity = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
                falling(fallingEntity);
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isEmptyBlock(pos.above()) && pos.getY() >= level.getMinBuildHeight() && isFree(level.getBlockState(pos.below()))) {
            FallingBlockEntity fallingEntity = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
            falling(fallingEntity);
            level.removeBlock(pos, false);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0 && state.getValue(AGE) == 2) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() - 0.05;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(new DustParticleOptions(new Vector3f(0.545f, 0.345f, 0.169f), 1.0f), x, y, z, 0.0, -0.2, 0.0);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingEntity) {
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
}
