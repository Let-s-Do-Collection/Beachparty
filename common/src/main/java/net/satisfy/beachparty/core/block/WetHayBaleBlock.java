package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos neighbor = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighbor);
            if (!neighborState.canOcclude() || !neighborState.isFaceSturdy(level, neighbor, direction.getOpposite())) {
                double x = pos.getX() + (direction.getStepX() == 0 ? random.nextDouble() : 0.5 + direction.getStepX() * 0.55);
                double y = direction == Direction.DOWN ? pos.getY() - 0.05 : pos.getY() + random.nextDouble() * 0.8;
                double z = pos.getZ() + (direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + direction.getStepZ() * 0.55);
                level.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0, 0.0, 0.0);
            }
        }
        if (level.getBlockEntity(pos) instanceof WetHayBaleBlockEntity bale && bale.isDrying() && random.nextFloat() < (bale.isHot() ? 0.35f : 0.12f)) {
            double x = pos.getX() + 0.2 + random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.2 + random.nextDouble() * 0.6;
            level.addParticle(ParticleTypes.WHITE_SMOKE, x, pos.getY() + 1.02, z, 0.0, 0.015 + random.nextDouble() * 0.01, 0.0);
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
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
