package net.satisfy.beachparty.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.fabric.config.ConfigFabric;
import net.satisfy.beachparty.fabric.mixin.WoodTypeAccessor;
import net.satisfy.beachparty.platform.PlatformHelper;
import net.satisfy.beachparty.registry.ObjectRegistry;

import java.util.function.Supplier;

public class PlatformHelperImpl {
    static ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registry = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Beachparty.MOD_ID, name), block.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registry = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Beachparty.MOD_ID, name), item.get());
        return () -> registry;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Beachparty.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.scalable(width, height)).trackRangeChunks(clientTrackingRange).build());
        return () -> registry;
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> registerBlockEntityType(String name, Supplier<T> blockEntity) {
        T registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Beachparty.MOD_ID, name), blockEntity.get());
        return () -> registry;
    }

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(PlatformHelper.BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks) {
        return FabricBlockEntityTypeBuilder.create(blockEntitySupplier::create, validBlocks).build();
    }

    public static WoodType createWoodType(String name, BlockSetType setType) {
        return new WoodType(name, setType);
    }

    public static WoodType registerWoodType(WoodType woodType) {
        return WoodTypeAccessor.invokeRegister(woodType);
    }

    public static void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (config.worldgen.destroySandcastle) {
            world.setBlock(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState(), 3);
        }
    }


}
