package net.satisfy.beachparty.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.platform.PlatformHelper;
import org.joml.Vector3d;

public class MessageInABottleSpawner {
    private static BlockPos lastValidSpawn = null;
    private static long lastSpawnTime = 0;

    @SuppressWarnings("deprecation")
    private static void attemptSpawn(ServerLevel level, int maxCount) {
        if (level.players().isEmpty()) return;
        RandomSource random = level.random;
        var player = level.players().get(random.nextInt(level.players().size()));

        if (lastValidSpawn == null || (level.getGameTime() - lastSpawnTime) > 12000) {
            lastValidSpawn = findValidSpawnPos(level, player.blockPosition());
            lastSpawnTime = level.getGameTime();
        }

        if (lastValidSpawn == null) return;

        int spawnX = lastValidSpawn.getX();
        int spawnY = lastValidSpawn.getY();
        int spawnZ = lastValidSpawn.getZ();
        int count = 0;

        for (int i = 0; i < 200; i++) {
            int x = spawnX - 50 + random.nextInt(101);
            int z = spawnZ - 50 + random.nextInt(101);
            BlockPos pos = new BlockPos(x, spawnY, z);

            if (!level.hasChunkAt(pos)) continue;

            if (level.getBlockState(pos).getBlock() == ObjectRegistry.MESSAGE_IN_A_BOTTLE.get()) {
                count++;
                if (count >= maxCount) break;
            }
        }

        if (count >= maxCount) return;

        BlockState state = ObjectRegistry.MESSAGE_IN_A_BOTTLE.get().defaultBlockState();
        Vector3d vec = new Vector3d(lastValidSpawn.getX() + 0.5, lastValidSpawn.getY(), lastValidSpawn.getZ() + 0.5);
        BlockPos placePos = new BlockPos((int) vec.x, (int) vec.y, (int) vec.z);

        if (!level.hasChunkAt(placePos)) return;

        if (level.isEmptyBlock(placePos) && level.getBlockState(placePos.below()).is(BlockTags.SAND)) {
            level.setBlock(placePos, state, 3);
        }
    }

    private static BlockPos findValidSpawnPos(ServerLevel level, BlockPos center) {
        RandomSource random = level.random;
        for (int i = 0; i < 10; i++) {
            int offsetX = random.nextInt(200) - 100;
            int offsetZ = random.nextInt(200) - 100;
            BlockPos newPos = center.offset(offsetX, 0, offsetZ);
            var biome = level.getBiome(newPos);
            if (biome.is(BiomeTags.IS_BEACH)) {
                int spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, newPos.getX(), newPos.getZ()) - 2;
                return new BlockPos(newPos.getX(), spawnY, newPos.getZ());
            }
        }
        return null;
    }

    public static void tick(ServerLevel level) {
        if (!PlatformHelper.allowBottleSpawning()) return;
        int interval = PlatformHelper.getBottleSpawnInterval();
        int maxCount = PlatformHelper.getBottleMaxCount();
        if (level.getGameTime() % interval == 0) attemptSpawn(level, maxCount);
    }
}
