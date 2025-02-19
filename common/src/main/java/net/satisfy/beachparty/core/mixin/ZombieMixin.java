package net.satisfy.beachparty.core.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.satisfy.beachparty.core.entity.goals.ApproachSandCastleGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin {
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addApproachSandCastleGoal(CallbackInfo ci) {
        ((MobAccessor) this).getGoalSelector().addGoal(1, new ApproachSandCastleGoal((Mob)(Object)this));
    }
}
