package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
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

    public WetHayBaleBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.WET_HAY_BALE_BLOCK_ENTITY.get(), pos, state);
        timer = initializeTimer();
    }

    private int initializeTimer() {
        Level level = getLevel();
        return level != null && level.getBiome(getBlockPos()).is(HOT_BIOME)
                ? 1200 + level.random.nextInt(601)
                : 600 + (level != null ? level.random.nextInt(601) : 600);
    }

    public static void tick(ServerLevel level, BlockPos pos, BlockState state, WetHayBaleBlockEntity be) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        boolean hot = biomeHolder.is(HOT_BIOME);
        RandomSource random = level.getRandom();
        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos pos2 = pos.relative(direction);
            if (!state.canOcclude() || !level.getBlockState(pos2).isFaceSturdy(level, pos2, direction.getOpposite())) {
                double d = pos.getX(), e = pos.getY(), f = pos.getZ();
                if (direction == Direction.DOWN) { e -= 0.05; d += random.nextDouble(); f += random.nextDouble(); }
                else { e += random.nextDouble() * 0.8;
                    d += direction.getAxis() == Direction.Axis.X ? (direction == Direction.EAST ? 1 : 0.05) : random.nextDouble();
                    f += direction.getAxis() == Direction.Axis.X ? random.nextDouble() : (direction == Direction.SOUTH ? 1 : 0.05);
                }
                level.sendParticles(ParticleTypes.DRIPPING_WATER, d, e, f, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        if (hot) level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.1, 0.1, 0.1, 0.0);
        be.timer--;
        if (be.timer <= 0) level.setBlock(pos, hot ? ObjectRegistry.THATCH.get().defaultBlockState() : Blocks.HAY_BLOCK.defaultBlockState(), 3);
        else level.scheduleTick(pos, state.getBlock(), 1);
    }
}
