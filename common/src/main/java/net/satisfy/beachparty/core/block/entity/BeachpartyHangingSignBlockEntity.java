package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class BeachpartyHangingSignBlockEntity extends BeachpartySignBlockEntity {

    public BeachpartyHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.BEACHPARTY_HANGING_SIGN.get(), blockPos, blockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EntityTypeRegistry.BEACHPARTY_HANGING_SIGN.get();
    }
}
