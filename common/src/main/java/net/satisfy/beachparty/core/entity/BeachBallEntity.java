package net.satisfy.beachparty.core.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class BeachBallEntity extends Mob {
    private static final float BOUNCE_FACTOR = 0.6f;
    private static final float INITIAL_ROLL_SPEED = 2.0f;
    private static final double FRICTION = 0.95;

    public BeachBallEntity(EntityType<? extends BeachBallEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false);
    }

    public static AttributeSupplier.@NotNull Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(FRICTION, FRICTION, FRICTION));

        if (this.horizontalCollision) {
            Vector3f bounceVelocity = new Vector3f(
                    (float) -this.getDeltaMovement().x * BOUNCE_FACTOR,
                    (float) this.getDeltaMovement().y,
                    (float) -this.getDeltaMovement().z * BOUNCE_FACTOR
            );
            this.setDeltaMovement(bounceVelocity.x, bounceVelocity.y, bounceVelocity.z);
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource damageSource) {
        return true;
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof Player) {
            float pitch = 0.9F + this.level().getRandom().nextFloat() * 0.1F;
            this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 0.5F, pitch);
            Vector3f lookDirection = new Vector3f(
                    (float) entity.getLookAngle().x,
                    0,
                    (float) entity.getLookAngle().z
            ).normalize();
            Vector3f pushDirection = lookDirection.mul(INITIAL_ROLL_SPEED);
            this.setDeltaMovement(pushDirection.x, this.getDeltaMovement().y, pushDirection.z);
        } else {
            super.push(entity);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            if (player.isShiftKeyDown()) {
                this.discard();

                ItemStack beachBallItem = new ItemStack(ObjectRegistry.BEACH_BALL.get());

                if (!player.getInventory().add(beachBallItem)) {
                    player.drop(beachBallItem, false);
                }

            } else {
                float pitch = 0.8F + this.level().getRandom().nextFloat() * 0.4F;
                this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 1.0F, pitch);
                Vector3f lookDirection = new Vector3f(
                        (float) player.getLookAngle().x,
                        0,
                        (float) player.getLookAngle().z
                ).normalize();

                Vector3f newVelocity = lookDirection.mul(INITIAL_ROLL_SPEED);
                this.setDeltaMovement(newVelocity.x, 0.25f, newVelocity.z);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
