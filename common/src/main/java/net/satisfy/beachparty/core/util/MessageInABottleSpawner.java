package net.satisfy.beachparty.core.util;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.platform.PlatformHelper;

import java.util.List;

public class MessageInABottleSpawner {
    private static final int SPAWN_RADIUS = 48;
    private static final int COUNT_RADIUS = 32;
    private static final int ATTEMPTS = 16;

    public static void init() {
        TickEvent.SERVER_LEVEL_POST.register(MessageInABottleSpawner::tick);
    }

    private static void tick(ServerLevel level) {
        if (!PlatformHelper.isMessageInABottleEnabled()) return;
        if (level.getGameTime() % PlatformHelper.getMessageInABottleInterval() != 0) return;
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) return;
        RandomSource random = level.random;
        attemptSpawn(level, players.get(random.nextInt(players.size())).blockPosition(), random, PlatformHelper.getMessageInABottleMaxCount());
    }

    private static void attemptSpawn(ServerLevel level, BlockPos center, RandomSource random, int maxCount) {
        for (int i = 0; i < ATTEMPTS; i++) {
            int x = center.getX() + random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            int z = center.getZ() + random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            if (!level.hasChunk(x >> 4, z >> 4)) continue;
            BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z), z);
            if (!level.getBiome(pos).is(BiomeTags.IS_BEACH)) continue;
            if (!level.isEmptyBlock(pos) || !level.getBlockState(pos.below()).is(BlockTags.SAND)) continue;
            if (countNearby(level, pos) >= maxCount) return;
            level.setBlock(pos, ObjectRegistry.MESSAGE_IN_A_BOTTLE.get().defaultBlockState(), Block.UPDATE_ALL);
            return;
        }
    }

    private static int countNearby(ServerLevel level, BlockPos center) {
        Block bottle = ObjectRegistry.MESSAGE_IN_A_BOTTLE.get();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int count = 0;
        for (int x = center.getX() - COUNT_RADIUS; x <= center.getX() + COUNT_RADIUS; x++) {
            for (int z = center.getZ() - COUNT_RADIUS; z <= center.getZ() + COUNT_RADIUS; z++) {
                if (!level.hasChunk(x >> 4, z >> 4)) continue;
                pos.set(x, level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1, z);
                if (level.getBlockState(pos).is(bottle)) count++;
            }
        }
        return count;
    }
}
