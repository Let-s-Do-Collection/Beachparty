package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.beachparty.core.block.entity.WetHayBaleBlockEntity;

public class WetHayBaleBlock extends RotatedPillarBlock implements EntityBlock {
    public WetHayBaleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WetHayBaleBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, blockState, te) -> {
            if (te instanceof WetHayBaleBlockEntity wetHayBaleBlockEntity) {
                WetHayBaleBlockEntity.tick((ServerLevel) lvl, pos, blockState, wetHayBaleBlockEntity);
            }
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.CLAY_BALL)) {
            if (level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity be) {
                be.preventDrying();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)) {
            if (level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity be) {
                be.removeProtection();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hit);
    }
}
