package net.satisfy.beachparty.core.block;

import net.satisfy.foundation.seat.SeatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PalmBarStoolBlock extends Block {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () ->
            Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.6875, 0.6875);

    public PalmBarStoolBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(DyeableFurniture.COLOR, DyeableFurniture.DEFAULT_COLOR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DyeableFurniture.COLOR);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return voxelShapeSupplier.get();
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (DyeableFurniture.canDye(itemStack, blockState)) {
            return DyeableFurniture.dye(itemStack, blockState, world, blockPos, player);
        }
        return SeatUtil.onUse(world, player, hand, hit, -0.25);
    }
}

