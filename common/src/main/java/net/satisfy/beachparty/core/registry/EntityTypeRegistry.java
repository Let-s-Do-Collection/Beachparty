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
import net.satisfy.beachparty.core.block.entity.*;
import net.satisfy.beachparty.core.entity.*;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import net.satisfy.beachparty.platform.PlatformHelper;

import java.util.function.Supplier;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

public final class EntityTypeRegistry {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ChairEntity>> CHAIR = registerEntity("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build(new BeachpartyIdentifier("chair").toString()));
    public static final RegistrySupplier<EntityType<ThrowableCoconutEntity>> COCONUT = registerEntity("coconut", () -> EntityType.Builder.<ThrowableCoconutEntity>of(ThrowableCoconutEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new BeachpartyIdentifier("coconut").toString()));
    public static final RegistrySupplier<EntityType<BeachBallEntity>> BEACH_BALL = registerEntity("beach_ball", () -> EntityType.Builder.of(BeachBallEntity::new, MobCategory.MISC).sized(0.4f, 0.4f).build(new BeachpartyIdentifier("beach_ball").toString()));
    public static final Supplier<EntityType<PalmBoatEntity>> PALM_BOAT = PlatformHelper.registerBoatType("palm_boat", PalmBoatEntity::new, MobCategory.MISC, 1.375F, 0.5625F, 10);
    public static final Supplier<EntityType<PalmChestBoatEntity>> PALM_CHEST_BOAT = PlatformHelper.registerBoatType("palm_chest_boat", PalmChestBoatEntity::new, MobCategory.MISC, 1.375F, 0.5625F, 10);

    public static final RegistrySupplier<BlockEntityType<PalmSignBlockEntity>> BEACHPARTY_SIGN = registerBlockEntity("beachparty_sign", () -> BlockEntityType.Builder.of(PalmSignBlockEntity::new, PALM_SIGN.get(), PALM_WALL_SIGN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<PalmHangingSignBlockEntity>> BEACHPARTY_HANGING_SIGN = registerBlockEntity("beachparty_hanging_sign", () -> BlockEntityType.Builder.of(PalmHangingSignBlockEntity::new, PALM_HANGING_SIGN.get(), ObjectRegistry.PALM_WALL_HANGING_SIGN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<MiniFridgeBlockEntity>> MINI_FRIDGE_BLOCK_ENTITY = registerBlockEntity("mini_fridge", () -> BlockEntityType.Builder.of(MiniFridgeBlockEntity::new, ObjectRegistry.MINI_FRIDGE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<PalmCabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(PalmCabinetBlockEntity::new, PALM_CABINET.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<PalmBarBlockEntity>> palm_BAR_BLOCK_ENTITY = registerBlockEntity("palm_bar", () -> BlockEntityType.Builder.of(PalmBarBlockEntity::new, PALM_BAR.get()).build(null));
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