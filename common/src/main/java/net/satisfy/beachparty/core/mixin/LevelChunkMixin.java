package net.satisfy.beachparty.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.satisfy.beachparty.core.util.RadioHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Inject(method = "clearAllBlockEntities", at = @At("HEAD"))
    private void onClearBlockEntities(CallbackInfo ci) {
        LevelChunk chunk = (LevelChunk) (Object) this;
        Level level = chunk.getLevel();

        if (level != null && level.isClientSide) {
            Map<BlockPos, BlockEntity> blockEntities = chunk.getBlockEntities();
            for (BlockPos pos : new ArrayList<>(blockEntities.keySet())) {
                RadioHelper.setPlaying(pos.immutable(), 0, false, 0);
            }
        }
    }
}
