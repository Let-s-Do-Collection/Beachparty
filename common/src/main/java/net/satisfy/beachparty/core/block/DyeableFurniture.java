package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.satisfy.foundation.util.DyeHelper;

public final class DyeableFurniture {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final DyeColor DEFAULT_COLOR = DyeColor.LIGHT_BLUE;

    private DyeableFurniture() {
    }

    public static boolean canDye(ItemStack stack, BlockState state) {
        return stack.getItem() instanceof DyeItem dye && state.getValue(COLOR) != dye.getDyeColor();
    }

    public static ItemInteractionResult dye(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, BlockPos... linked) {
        DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
        if (!level.isClientSide) {
            for (BlockPos other : linked) {
                BlockState otherState = level.getBlockState(other);
                if (otherState.is(state.getBlock())) {
                    level.setBlock(other, otherState.setValue(COLOR, color), Block.UPDATE_ALL);
                }
            }
        }
        return DyeHelper.apply(stack, color, state.setValue(COLOR, color), level, pos, player);
    }

    public static int tint(BlockState state, int tintIndex) {
        return tintIndex == 0 ? state.getValue(COLOR).getTextureDiffuseColor() : -1;
    }
}
