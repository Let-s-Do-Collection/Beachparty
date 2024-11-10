package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.entity.BeachGoalBlockEntity;
import net.satisfy.beachparty.core.block.entity.CabinetBlockEntity;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;
import net.satisfy.beachparty.core.block.entity.TikiBarBlockEntity;
import net.satisfy.beachparty.core.entity.*;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.platform.PlatformHelper;

import java.util.function.Supplier;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.CABINET;

public final class EntityTypeRegistry {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ChairEntity>> CHAIR = registerEntity("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build(new BeachpartyIdentifier("chair").toString()));
    public static final RegistrySupplier<EntityType<CoconutEntity>> COCONUT = registerEntity("coconut", () -> EntityType.Builder.<CoconutEntity>of(CoconutEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new BeachpartyIdentifier("coconut").toString()));
    public static final Supplier<EntityType<BeachpartyBoatEntity>> FLOATY_BOAT = PlatformHelper.registerBoatType("floaty_boat", BeachpartyBoatEntity::new, MobCategory.MISC, 1.375F, 0.5625F, 10);
    public static final Supplier<EntityType<BeachpartyChestBoatEntity>> FLOATY_CHEST_BOAT = PlatformHelper.registerBoatType("floaty_chest_boat", BeachpartyChestBoatEntity::new, MobCategory.MISC, 1.375F, 0.5625F, 10);
    public static final RegistrySupplier<EntityType<BeachBallEntity>> BEACH_BALL = registerEntity("beach_ball", () -> EntityType.Builder.of(BeachBallEntity::new, MobCategory.MISC).sized(0.4f, 0.4f).build(new BeachpartyIdentifier("beach_ball").toString()));

    public static final RegistrySupplier<BlockEntityType<MiniFridgeBlockEntity>> MINI_FRIDGE_BLOCK_ENTITY = registerBlockEntity("mini_fridge", () -> BlockEntityType.Builder.of(MiniFridgeBlockEntity::new, ObjectRegistry.MINI_FRIDGE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(CabinetBlockEntity::new, CABINET.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<TikiBarBlockEntity>> TIKI_BAR_BLOCK_ENTITY = registerBlockEntity("tiki_bar", () -> BlockEntityType.Builder.of(TikiBarBlockEntity::new, ObjectRegistry.TIKI_BAR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BeachGoalBlockEntity>> BEACH_GOAL_BLOCK_ENTITY = registerBlockEntity("beach_goal", () -> BlockEntityType.Builder.of(BeachGoalBlockEntity::new, ObjectRegistry.BEACH_GOAL.get()).build(null));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new BeachpartyIdentifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(path, type);
    }

    public static void registerAttributes() {
        EntityAttributeRegistry.register(BEACH_BALL, BeachBallEntity::createMobAttributes);
    }

    public static void init() {
        ENTITY_TYPES.register();
        registerAttributes();
    }
}