package net.satisfy.beachparty.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageInABottleItem extends BlockItem {
    public MessageInABottleItem(Block block, Properties settings) {
        super(block, settings);
    }

    public static @Nullable ItemStack getRandomMap(Entity entity) {
        int index = entity.level().getRandom().nextInt(4);
        return switch (index) {
            case 0 -> createMansionMap(entity);
            case 1 -> createShipwreckMap(entity);
            case 2 -> createMonumentMap(entity);
            case 3 -> createTreasureMap(entity);
            default -> null;
        };
    }

    public static ItemStack createMonumentMap(Entity entity) {
        return createMap(entity, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.OCEAN_MONUMENT.value());
    }

    public static ItemStack createMansionMap(Entity entity) {
        return createMap(entity, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.WOODLAND_MANSION.value());
    }

    public static ItemStack createShipwreckMap(Entity entity) {
        return createMap(entity, StructureTags.SHIPWRECK, "filled_map.shipwreck", MapDecorationTypes.RED_X.value());
    }

    public static ItemStack createTreasureMap(Entity entity) {
        return createMap(entity, StructureTags.ON_TREASURE_MAPS, "filled_map.treasure", MapDecorationTypes.RED_X.value());
    }

    public static @Nullable ItemStack createMap(Entity entity, TagKey<Structure> structure, String nameKey, MapDecorationType iconType) {
        if (!(entity.level() instanceof ServerLevel serverWorld)) {
            return null;
        }
        BlockPos pos = serverWorld.findNearestMapStructure(structure, entity.blockPosition(), 100, true);
        if (pos != null) {
            ItemStack stack = MapItem.create(serverWorld, pos.getX(), pos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(serverWorld, stack);
            MapItemSavedData.addTargetDecoration(stack, pos, "+", BuiltInRegistries.MAP_DECORATION_TYPE.wrapAsHolder(iconType));
            stack.set(DataComponents.CUSTOM_NAME, Component.translatable(nameKey));
            return stack;
        }
        return null;
    }

    private static void spawnItem(Level world, double x, double y, double z, ItemStack stack) {
        world.addFreshEntity(new ItemEntity(world, x, y, z, stack));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide()) {
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }
        var random = world.getRandom();
        int randomNumber = random.nextInt(100);
        ItemStack dropStack;
        if (randomNumber < 50) {
            dropStack = new ItemStack(Items.PAPER);
        } else if (randomNumber < 80) {
            dropStack = new ItemStack(Items.MAP);
        } else {
            dropStack = getRandomMap(user);
        }
        if (dropStack == null) {
            dropStack = new ItemStack(Items.PAPER);
        }
        double x = user.getX();
        double y = user.getY();
        double z = user.getZ();
        spawnItem(world, x, y, z, dropStack);
        spawnItem(world, x, y, z, new ItemStack(Items.GLASS_BOTTLE));
        user.getItemInHand(hand).shrink(1);
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
