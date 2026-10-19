package net.satisfy.beachparty.core.mixin.wearable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.beachparty.core.event.CommonEvents;
import net.satisfy.beachparty.core.event.WearableEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityWearableMixin {
    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float beachparty$reduceWornDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return CommonEvents.modifyDamage(entity, source, WearableEvents.modifyDamage(entity, source, amount));
    }
}
