package net.satisfy.beachparty.core.util;

import com.mojang.datafixers.util.Pair;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.entity.ChairEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BeachpartyUtil {
    public static final EnumProperty<BeachpartyUtil.LineConnectingType> LINE_CONNECTING_TYPE = EnumProperty.create("type", BeachpartyUtil.LineConnectingType.class);
    private static final Map<ResourceLocation, Map<BlockPos, Pair<ChairEntity, BlockPos>>> CHAIRS = new HashMap<>();

    public static <T extends Block> RegistrySupplier<T> registerWithItem(DeferredRegister<Block> registerB, Registrar<Block> registrarB, DeferredRegister<Item> registerI, Registrar<Item> registrarI, ResourceLocation name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = registerWithoutItem(registerB, registrarB, name, block);
        registerItem(registerI, registrarI, name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(DeferredRegister<Block> register, Registrar<Block> registrar, ResourceLocation path, Supplier<T> block) {
        return Platform.isNeoForge() ? register.register(path.getPath(), block) : registrar.register(path, block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(DeferredRegister<Item> register, Registrar<Item> registrar, ResourceLocation path, Supplier<T> itemSupplier) {
        return Platform.isNeoForge() ? register.register(path.getPath(), itemSupplier) : registrar.register(path, itemSupplier);
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < times; ++i) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.joinUnoptimized(buffer[1], Shapes.box(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX), BooleanOp.OR));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static ItemInteractionResult onUse(Level world, Player player, InteractionHand hand, BlockHitResult hit, double extraHeight) {
        if (world.isClientSide) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (player.isShiftKeyDown()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (BeachpartyUtil.isPlayerSitting(player)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hit.getDirection() == Direction.DOWN) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockPos hitPos = hit.getBlockPos();
        if (!BeachpartyUtil.isOccupied(world, hitPos) && player.getItemInHand(hand).isEmpty()) {
            ChairEntity chair = EntityTypeRegistry.CHAIR.get().create(world);
            assert chair != null;
            chair.moveTo(hitPos.getX() + 0.5D, hitPos.getY() + 0.25D + extraHeight, hitPos.getZ() + 0.5D, 0, 0);
            if (BeachpartyUtil.addChairEntity(world, hitPos, chair, player.blockPosition())) {
                world.addFreshEntity(chair);
                player.startRiding(chair);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static void onStateReplaced(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            ChairEntity entity = BeachpartyUtil.getChairEntity(world, pos);
            if (entity != null) {
                BeachpartyUtil.removeChairEntity(world, pos);
                entity.ejectPassengers();
            }
        }
    }

    public static boolean addChairEntity(Level world, BlockPos blockPos, ChairEntity entity, BlockPos playerPos) {
        if (!world.isClientSide) {
            ResourceLocation id = getDimensionTypeId(world);
            if (!CHAIRS.containsKey(id)) CHAIRS.put(id, new HashMap<>());
            CHAIRS.get(id).put(blockPos, Pair.of(entity, playerPos));
            return true;
        }
        return false;
    }

    public static void removeChairEntity(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            ResourceLocation id = getDimensionTypeId(world);
            if (CHAIRS.containsKey(id)) {
                CHAIRS.get(id).remove(pos);
            }
        }
    }

    public static ChairEntity getChairEntity(Level world, BlockPos pos) {
        if (!world.isClientSide()) {
            ResourceLocation id = getDimensionTypeId(world);
            if (CHAIRS.containsKey(id) && CHAIRS.get(id).containsKey(pos))
                return CHAIRS.get(id).get(pos).getFirst();
        }
        return null;
    }

    public static BlockPos getPreviousPlayerPosition(Player player, ChairEntity chairEntity) {
        if (!player.level().isClientSide()) {
            ResourceLocation id = getDimensionTypeId(player.level());
            if (CHAIRS.containsKey(id)) {
                for (Pair<ChairEntity, BlockPos> pair : CHAIRS.get(id).values()) {
                    if (pair.getFirst() == chairEntity)
                        return pair.getSecond();
                }
            }
        }
        return null;
    }

    public static boolean isOccupied(Level world, BlockPos pos) {
        ResourceLocation id = getDimensionTypeId(world);
        return BeachpartyUtil.CHAIRS.containsKey(id) && BeachpartyUtil.CHAIRS.get(id).containsKey(pos);
    }

    public static boolean isPlayerSitting(Player player) {
        for (ResourceLocation i : CHAIRS.keySet()) {
            for (Pair<ChairEntity, BlockPos> pair : CHAIRS.get(i).values()) {
                if (pair.getFirst().hasPassenger(player))
                    return true;
            }
        }
        return false;
    }

    public static void registerColorArmor(Item item, int defaultColor) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> tintIndex == 0 ? getColor(stack, defaultColor) : 0xFFFFFFFF, item);
    }

    public static void registerColorWeapon(Item item, int defaultColor) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> tintIndex == 0 ? getColor(stack, defaultColor) : 0xFFFFFFFF, item);
    }

    static int getColor(ItemStack itemStack, int defaultColor) {
        DyedItemColor dyed = itemStack.get(DataComponents.DYED_COLOR);
        if (dyed != null) return 0xFF000000 | dyed.rgb();
        CompoundTag displayTag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("display");
        if (displayTag.contains("color", Tag.TAG_ANY_NUMERIC)) return 0xFF000000 | displayTag.getInt("color");
        Vector3i rgb = new Vector3i((defaultColor >> 16) & 255, (defaultColor >> 8) & 255, defaultColor & 255);
        return (255 << 24) | (rgb.x() << 16) | (rgb.y() << 8) | rgb.z();
    }

    private static ResourceLocation getDimensionTypeId(Level world) {
        return world.dimension().location();
    }

    public static boolean matchesRecipe(RecipeInput inventory, NonNullList<Ingredient> recipe, int startIndex, int endIndex) {
        final List<ItemStack> validStacks = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            final ItemStack stackInSlot = inventory.getItem(i);
            if (!stackInSlot.isEmpty())
                validStacks.add(stackInSlot);
        }
        for (Ingredient entry : recipe) {
            boolean matches = false;
            for (ItemStack item : validStacks) {
                if (entry.test(item)) {
                    matches = true;
                    validStacks.remove(item);
                    break;
                }
            }
            if (!matches) {
                return false;
            }
        }
        return true;
    }

    public enum LineConnectingType implements StringRepresentable {
        NONE("none"),
        MIDDLE("middle"),
        LEFT("left"),
        RIGHT("right");

        private final String name;

        LineConnectingType(String type) {
            this.name = type;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
