package net.satisfy.beachparty.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PlatformHelper {
    @ExpectPlatform
    public static void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean allowBottleSpawning() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getBottleMaxCount() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getBottleSpawnInterval() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        throw new AssertionError();
    }
}
