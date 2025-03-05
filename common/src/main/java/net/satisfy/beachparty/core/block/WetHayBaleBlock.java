package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.entity.WetHayBaleBlockEntity;
import org.jetbrains.annotations.NotNull;

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
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(Items.CLAY_BALL)) {
            if (level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity be) {
                be.preventDrying();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (PotionUtils.getPotion(itemStack) == Potions.WATER) {
            if (level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity be) {
                be.removeProtection();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }
}
