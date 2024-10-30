package net.satisfy.beachparty.item;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MessageInABottleItem extends BlockItem {
    public MessageInABottleItem(Block block, Properties settings) {
        super(block, settings);
    }

    public static @Nullable ItemStack getRandomMap(Entity entity) {
        int lootChest = entity.level().getRandom().nextInt(4);

        ItemStack map = null;

        switch (lootChest) {
            case 0 -> // Buried Treasure
                    map = createMansionMap(entity);
            case 1 -> // Shipwreck Map
                    map = createShipwreckMap(entity);
            case 2 -> // Shipwreck Supply
                    map = createMonumentMap(entity);
            case 3 -> // Shipwreck Treasure
                    map = createTreasureMap(entity);
        }
        return map;
    }

    public static ItemStack createMonumentMap(Entity entity) {
        return createMap(entity, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecoration.Type.MONUMENT);
    }

    public static ItemStack createMansionMap(Entity entity) {
        return createMap(entity, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecoration.Type.MANSION);
    }

    public static ItemStack createShipwreckMap(Entity entity) {
        return createMap(entity, StructureTags.SHIPWRECK, "filled_map.shipwreck", MapDecoration.Type.RED_X);
    }

    public static ItemStack createTreasureMap(Entity entity) {
        return createMap(entity, StructureTags.ON_TREASURE_MAPS, "filled_map.treasure", MapDecoration.Type.RED_X);
    }

    public static @Nullable ItemStack createMap(Entity entity, TagKey<Structure> structure, String nameKey, MapDecoration.Type iconType) {
        if (!(entity.level() instanceof ServerLevel serverWorld)) {
            return null;
        }
        BlockPos blockPos = serverWorld.findNearestMapStructure(structure, entity.blockPosition(), 100, true);
        if (blockPos != null) {
            ItemStack itemStack = MapItem.create(serverWorld, blockPos.getX(), blockPos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(serverWorld, itemStack);
            MapItemSavedData.addTargetDecoration(itemStack, blockPos, "+", iconType);
            itemStack.setHoverName(Component.translatable(nameKey));
            return itemStack;
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide()) {
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }

        Random random = new Random();
        int randomNumber = random.nextInt(100);
        ItemStack itemStack;

        if (randomNumber < 50) {
            itemStack = new ItemStack(Items.PAPER);
        } else if (randomNumber < 80) {
            itemStack = new ItemStack(Items.MAP);
        } else {
            itemStack = getRandomMap(user);
        }
        if (itemStack == null) itemStack = new ItemStack(Items.PAPER);

        ItemEntity entity = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), itemStack);
        world.addFreshEntity(entity);

        ItemStack glassBottleStack = new ItemStack(Items.GLASS_BOTTLE, 1);
        ItemEntity glassBottleEntity = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), glassBottleStack);
        world.addFreshEntity(glassBottleEntity);

        user.getItemInHand(hand).shrink(1);

        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

}

