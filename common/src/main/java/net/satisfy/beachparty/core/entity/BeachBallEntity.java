package net.satisfy.beachparty.core.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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
    private static final float FLOAT_HEIGHT = 0.3f;
    private int waterTicks = 0;

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
        this.setDeltaMovement(this.getDeltaMovement().multiply(FRICTION, 1.0, FRICTION));
        if (this.isInWater()) {
            waterTicks++;
            double waterSurfaceY = this.blockPosition().getY() + 1;
            double targetY = waterSurfaceY + FLOAT_HEIGHT;
            double currentY = this.getY();
            if (waterTicks < 10) {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.05, this.getDeltaMovement().z);
            } else {
                double error = targetY - currentY;
                double newYVelocity = error * 0.05;
                if (Math.abs(error) < 0.02) {
                    newYVelocity = 0;
                }
                this.setDeltaMovement(this.getDeltaMovement().x, newYVelocity, this.getDeltaMovement().z);
            }
        } else {
            waterTicks = 0;
        }
        if (this.horizontalCollision) {
            Vector3f bounceVelocity = new Vector3f((float)-this.getDeltaMovement().x * BOUNCE_FACTOR, (float)this.getDeltaMovement().y, (float)-this.getDeltaMovement().z * BOUNCE_FACTOR);
            this.setDeltaMovement(bounceVelocity.x, bounceVelocity.y, bounceVelocity.z);
        }
        if (this.getDeltaMovement().lengthSqr() > 0.003) {
            spawnKickParticles();
        }
    }

    private void spawnKickParticles() {
        ParticleOptions particle = this.getCustomName() != null && "MissLilitu".equalsIgnoreCase(this.getCustomName().getString())
                ? ParticleTypes.CHERRY_LEAVES : ParticleTypes.CRIT;
        for (int i = 0; i < 1; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.2;
            double offsetY = (this.random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.2;
            this.level().addParticle(particle, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0, 0, 0);
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return true;
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof Player) {
            float pitch = 0.75F + this.level().getRandom().nextFloat() * 0.1F;
            this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 0.25F, pitch);
            if (this.getCustomName() != null && "Steve".equalsIgnoreCase(this.getCustomName().getString())) {
                if (!this.level().isClientSide) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.VILLAGER_HURT, this.getSoundSource(), 0.25F, 1.0F);
                }
            }
            Vector3f lookDirection = new Vector3f((float) entity.getLookAngle().x, 0, (float) entity.getLookAngle().z).normalize();
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
                if (this.getCustomName() != null) {
                    String name = this.getCustomName().getString();
                    if ("Steve".equalsIgnoreCase(name)) {
                        this.playSound(SoundEvents.VILLAGER_HURT, 0.25F, pitch);
                    } else if ("MissLilitu".equalsIgnoreCase(name)) {
                        this.playSound(SoundEvents.FLOWERING_AZALEA_BREAK, 0.25F, pitch);
                    } else {
                        this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 0.25F, pitch);
                    }
                } else {
                    this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 0.25F, pitch);
                }
                boolean invertMovement = this.getCustomName() != null && "CR-055".equalsIgnoreCase(this.getCustomName().getString());
                Vector3f lookDirection = new Vector3f(
                        invertMovement ? (float) -player.getLookAngle().x : (float) player.getLookAngle().x,
                        0,
                        invertMovement ? (float) -player.getLookAngle().z : (float) player.getLookAngle().z
                ).normalize();
                Vector3f newVelocity = lookDirection.mul(INITIAL_ROLL_SPEED);
                this.setDeltaMovement(newVelocity.x, 0.25f, newVelocity.z);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
