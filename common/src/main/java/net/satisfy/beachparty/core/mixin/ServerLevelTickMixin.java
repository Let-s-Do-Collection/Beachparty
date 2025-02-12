package net.satisfy.beachparty.core.mixin;

import net.minecraft.server.level.ServerLevel;
import net.satisfy.beachparty.core.util.MessageInABottleSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelTickMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        MessageInABottleSpawner.tick((ServerLevel) (Object) this);
    }
}