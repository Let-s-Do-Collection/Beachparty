package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class PalmSignBlockEntity extends SignBlockEntity {

    public PalmSignBlockEntity(BlockEntityType<? extends PalmSignBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PalmSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EntityTypeRegistry.BEACHPARTY_SIGN.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EntityTypeRegistry.BEACHPARTY_SIGN.get();
    }
}