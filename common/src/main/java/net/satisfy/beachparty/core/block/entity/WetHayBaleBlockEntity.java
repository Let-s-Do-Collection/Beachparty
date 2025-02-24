package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class WetHayBaleBlockEntity extends BlockEntity {
    public static final TagKey<Biome> HOT_BIOME = TagKey.create(Registries.BIOME, new BeachpartyIdentifier("hot_biome"));
    private int timer;
    private boolean isProtected = false;

    public WetHayBaleBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.WET_HAY_BALE_BLOCK_ENTITY.get(), pos, state);
        timer = initializeTimer();
    }

    private int initializeTimer() {
        Level level = getLevel();
        return level instanceof ServerLevel serverLevel && isHotBiome(serverLevel, getBlockPos())
                ? 1200 + level.random.nextInt(601)
                : 600 + (level != null ? level.random.nextInt(601) : 600);
    }

    public static void tick(ServerLevel level, BlockPos pos, BlockState state, WetHayBaleBlockEntity be) {
        if (be.isProtected) {
            level.scheduleTick(pos, state.getBlock(), 1);
            return;
        }
        if (!level.canSeeSky(pos.above()) || level.getFluidState(pos).is(net.minecraft.world.level.material.Fluids.WATER)) {
            level.scheduleTick(pos, state.getBlock(), 1);
            return;
        }
        boolean hot = isHotBiome(level, pos);
        RandomSource random = level.getRandom();
        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos pos2 = pos.relative(direction);
            if (!state.canOcclude() || !level.getBlockState(pos2).isFaceSturdy(level, pos2, direction.getOpposite())) {
                if (random.nextFloat() < 0.3f) {
                    double d = pos.getX(), e = pos.getY(), f = pos.getZ();
                    if (direction == Direction.DOWN) {
                        e -= 0.05;
                        d += random.nextDouble();
                        f += random.nextDouble();
                    } else {
                        e += random.nextDouble() * 0.8;
                        d += direction.getAxis() == Direction.Axis.X ? (direction == Direction.EAST ? 1 : 0.05) : random.nextDouble();
                        f += direction.getAxis() == Direction.Axis.X ? random.nextDouble() : (direction == Direction.SOUTH ? 1 : 0.05);
                    }
                    level.sendParticles(ParticleTypes.DRIPPING_WATER, d, e, f, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        }
        if (hot && random.nextFloat() < 0.25f) {
            level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.6, pos.getY() + 1.25, pos.getZ() + 0.5, 1, 0.05, 0.1875, 0.05, 0.0);
        }
        be.timer--;
        if (be.timer <= 0) {
            level.setBlock(pos, hot ? ObjectRegistry.THATCH.get().defaultBlockState() : net.minecraft.world.level.block.Blocks.HAY_BLOCK.defaultBlockState(), 3);
        } else {
            level.scheduleTick(pos, state.getBlock(), 1);
        }
    }

    public void preventDrying() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CLAY.defaultBlockState()),
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.0, getBlockPos().getZ() + 0.5,
                    10, 0.2, 0.2, 0.2, 0.1);
            serverLevel.playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        isProtected = true;
    }

    public void removeProtection() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.0, getBlockPos().getZ() + 0.5,
                    10, 0.2, 0.2, 0.2, 0.1);
            serverLevel.playSound(null, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        isProtected = false;
        timer = initializeTimer();
    }

    private static boolean isHotBiome(ServerLevel level, BlockPos pos) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        return biomeHolder.is(HOT_BIOME);
    }
}
