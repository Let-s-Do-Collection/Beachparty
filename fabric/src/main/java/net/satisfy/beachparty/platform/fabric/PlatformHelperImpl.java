package net.satisfy.beachparty.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.fabric.core.config.ConfigFabric;

import java.util.function.Supplier;

public class PlatformHelperImpl {
    static ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();

    public static void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (config.destroySandcastle) {
            world.setBlock(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState(), 3);
        }
    }

    public static boolean allowBottleSpawning() {
        return config.allowBottleSpawning;
    }

    public static int getBottleMaxCount() {
        return config.bottleMaxCount;
    }

    public static int getBottleSpawnInterval() {
        return config.bottleSpawnInterval;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, BeachpartyIdentifier.identifier(name), FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.scalable(width, height)).trackRangeChunks(clientTrackingRange).build());
        return () -> registry;
    }

}
