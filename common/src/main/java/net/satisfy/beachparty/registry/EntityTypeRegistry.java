package net.satisfy.beachparty.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.entity.*;
import net.satisfy.beachparty.entity.ChairEntity;
import net.satisfy.beachparty.entity.CoconutEntity;
import net.satisfy.beachparty.util.BeachpartyIdentifier;

import java.util.function.Supplier;

import static net.satisfy.beachparty.registry.ObjectRegistry.*;

public final class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.ENTITY_TYPE);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();

    public static final RegistrySupplier<BlockEntityType<TikiBarBlockEntity>> TIKI_BAR_BLOCK_ENTITY = registerBlockEntity("tiki_bar", () -> BlockEntityType.Builder.of(TikiBarBlockEntity::new, ObjectRegistry.TIKI_BAR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<MiniFridgeBlockEntity>> MINI_FRIDGE_BLOCK_ENTITY = registerBlockEntity("mini_fridge", () -> BlockEntityType.Builder.of(MiniFridgeBlockEntity::new, ObjectRegistry.MINI_FRIDGE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(CabinetBlockEntity::new, CABINET.get()).build(null));

    public static final RegistrySupplier<EntityType<ChairEntity>> CHAIR = registerEntity("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build(new BeachpartyIdentifier("chair").toString()));
    public static final RegistrySupplier<EntityType<CoconutEntity>> COCONUT = registerEntity("coconut", () -> EntityType.Builder.<CoconutEntity>of(CoconutEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new BeachpartyIdentifier("coconut").toString()));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new BeachpartyIdentifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(path, type);
    }

    public static void init() {
        ENTITY_TYPES.register();
    }
}