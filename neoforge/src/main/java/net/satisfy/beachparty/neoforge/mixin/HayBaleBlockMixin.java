package net.satisfy.beachparty.neoforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HayBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class HayBaleBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("HEAD"))
    private void BP_setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (state.getBlock() instanceof HayBlock) {
            if (!world.isClientSide() && (world.getFluidState(pos).getType() == Fluids.WATER || world.getFluidState(pos.above()).getType() == Fluids.WATER)) {
                world.setBlock(pos, ObjectRegistry.WET_HAY_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }
}
