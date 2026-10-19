package net.satisfy.beachparty.core.registry;

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
import net.satisfy.foundation.banner.CompletionistBannerEntity;
import net.satisfy.foundation.block.CabinetBlockEntity;
import net.satisfy.foundation.wood.WoodBoat;
import net.satisfy.foundation.wood.WoodChestBoat;
import net.satisfy.foundation.wood.WoodHangingSignBlockEntity;
import net.satisfy.foundation.wood.WoodSignBlockEntity;

import java.util.function.Supplier;

import static net.satisfy.beachparty.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Beachparty.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ThrowableCoconutEntity>> COCONUT = registerEntity("coconut", () -> EntityType.Builder.<ThrowableCoconutEntity>of(ThrowableCoconutEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(BeachpartyIdentifier.identifier("coconut").toString()));
    public static final RegistrySupplier<EntityType<BeachBallEntity>> BEACH_BALL = registerEntity("beach_ball", () -> EntityType.Builder.of(BeachBallEntity::new, MobCategory.MISC).sized(0.4f, 0.4f).build(BeachpartyIdentifier.identifier("beach_ball").toString()));
    public static final Supplier<EntityType<WoodBoat>> PALM_BOAT = PlatformHelper.registerBoatType("palm_boat", WoodBoat::new, MobCategory.MISC, 1.375F, 0.5625F, 10);
    public static final Supplier<EntityType<WoodBoat>> PALM_CHEST_BOAT = PlatformHelper.<WoodBoat>registerBoatType("palm_chest_boat", WoodChestBoat::new, MobCategory.MISC, 1.375F, 0.5625F, 10);

    public static final RegistrySupplier<BlockEntityType<WoodSignBlockEntity>> BEACHPARTY_SIGN = registerBlockEntity("beachparty_sign", () -> BlockEntityType.Builder.of((pos, state) -> new WoodSignBlockEntity(EntityTypeRegistry.BEACHPARTY_SIGN.get(), pos, state), PALM_SIGN.get(), PALM_WALL_SIGN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WoodHangingSignBlockEntity>> BEACHPARTY_HANGING_SIGN = registerBlockEntity("beachparty_hanging_sign", () -> BlockEntityType.Builder.of((pos, state) -> new WoodHangingSignBlockEntity(EntityTypeRegistry.BEACHPARTY_HANGING_SIGN.get(), pos, state), PALM_HANGING_SIGN.get(), ObjectRegistry.PALM_WALL_HANGING_SIGN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<MiniFridgeBlockEntity>> MINI_FRIDGE_BLOCK_ENTITY = registerBlockEntity("mini_fridge", () -> BlockEntityType.Builder.of(MiniFridgeBlockEntity::new, ObjectRegistry.MINI_FRIDGE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of((pos, state) -> new CabinetBlockEntity(EntityTypeRegistry.CABINET_BLOCK_ENTITY.get(), pos, state), PALM_CABINET.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<PalmBarBlockEntity>> PALM_BAR_BLOCK_ENTITY = registerBlockEntity("palm_bar", () -> BlockEntityType.Builder.of(PalmBarBlockEntity::new, PALM_BAR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BeachGoalBlockEntity>> BEACH_GOAL_BLOCK_ENTITY = registerBlockEntity("beach_goal", () -> BlockEntityType.Builder.of(BeachGoalBlockEntity::new, ObjectRegistry.BEACH_GOAL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WetHayBaleBlockEntity>> WET_HAY_BALE_BLOCK_ENTITY = registerBlockEntity("wet_hay_bale", () -> BlockEntityType.Builder.of(WetHayBaleBlockEntity::new, WET_HAY_BLOCK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> BEACHPARTY_BANNER = registerBlockEntity("beachparty_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.BEACHPARTY_BANNER.get(), ObjectRegistry.BEACHPARTY_WALL_BANNER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CocktailBlockEntity>> COCKTAIL_BLOCK_ENTITY = registerBlockEntity("cocktail", () -> BlockEntityType.Builder.of(CocktailBlockEntity::new, COCONUT_COCKTAIL.get(), SWEETBERRIES_COCKTAIL.get(), COCOA_COCKTAIL.get(), PUMPKIN_COCKTAIL.get(), HONEY_COCKTAIL.get(), MELON_COCKTAIL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RadioBlockEntity>> RADIO_BLOCK_ENTITY = registerBlockEntity("radio", () -> BlockEntityType.Builder.of(RadioBlockEntity::new, RADIO.get()).build(null));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(BeachpartyIdentifier.identifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(path, type);
    }

    public static void init() {
        ENTITY_TYPES.register();
    }
}