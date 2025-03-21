package net.satisfy.beachparty.core.util;

import net.minecraft.core.BlockPos;
import java.util.*;

public class SandCastleManager {
    private static final Map<Long, Set<BlockPos>> sandCastleChunks = new HashMap<>();

    private static long getChunkKey(BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        return ((long)chunkX & 0xFFFFFFFFL) | (((long)chunkZ & 0xFFFFFFFFL) << 32);
    }

    public static void registerSandCastle(BlockPos pos) {
        long chunkKey = getChunkKey(pos);
        sandCastleChunks.computeIfAbsent(chunkKey, k -> new HashSet<>()).add(pos);
    }

    public static void unregisterSandCastle(BlockPos pos) {
        long chunkKey = getChunkKey(pos);
        Set<BlockPos> positions = sandCastleChunks.get(chunkKey);
        if (positions != null) {
            positions.remove(pos);
            if (positions.isEmpty()) {
                sandCastleChunks.remove(chunkKey);
            }
        }
    }

    public static BlockPos getNearestSandCastle(BlockPos mobPos) {
        int chunkX = mobPos.getX() >> 4;
        int chunkZ = mobPos.getZ() >> 4;

        BlockPos nearestPos = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                long chunkKey = ((long)(chunkX + dx) & 0xFFFFFFFFL) | (((long)(chunkZ + dz) & 0xFFFFFFFFL) << 32);
                Set<BlockPos> positions = sandCastleChunks.get(chunkKey);
                if (positions != null) {
                    for (BlockPos pos : positions) {
                        double distance = pos.distSqr(mobPos);
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestPos = pos;
                        }
                    }
                }
            }
        }
        return nearestPos;
    }
}
