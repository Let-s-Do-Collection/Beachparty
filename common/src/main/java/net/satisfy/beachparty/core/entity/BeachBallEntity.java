package net.satisfy.beachparty.core.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BeachBallEntity extends Entity {
    private static final double GRAVITY = 0.035;
    private static final double AIR_DRAG = 0.985;
    private static final double WATER_DRAG = 0.92;
    private static final double WATER_DAMPING = 0.8;
    private static final double BUOYANCY = 0.22;
    private static final double GROUND_BOUNCE = 0.6;
    private static final double WALL_BOUNCE = 0.65;
    private static final double MIN_BOUNCE_SPEED = 0.08;
    private static final double MIN_ROLL_SPEED = 0.004;
    private static final float RADIUS = 0.25f;
    private static final int TOUCH_COOLDOWN = 8;
    private static final double SETTLE_SPEED = 0.02;
    private static final float SETTLE_RATE = 0.2f;

    private int touchCooldown = 0;
    private final Quaternionf orientation = new Quaternionf();
    private final Quaternionf orientationO = new Quaternionf();
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;

    public BeachBallEntity(EntityType<? extends BeachBallEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void tick() {
        super.tick();
        tickLerp();
        if (touchCooldown > 0) {
            touchCooldown--;
        }
        if (this.isControlledByLocalInstance()) {
            simulate();
        }
        if (this.level().isClientSide) {
            updateOrientation();
            if (isNamed("MissLilitu") && this.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                this.level().addParticle(ParticleTypes.CHERRY_LEAVES, this.getX(), this.getY() + RADIUS, this.getZ(), 0, 0, 0);
            }
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
        if (lerpSteps > 0) {
            this.lerpPositionAndRotationStep(lerpSteps, lerpX, lerpY, lerpZ, this.getYRot(), this.getXRot());
            lerpSteps--;
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        lerpSteps = steps;
    }

    @Override
    public double lerpTargetX() {
        return lerpSteps > 0 ? lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return lerpSteps > 0 ? lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return lerpSteps > 0 ? lerpZ : this.getZ();
    }

    private void simulate() {
        Vec3 motion = this.getDeltaMovement();
        double water = this.getFluidHeight(FluidTags.WATER);
        if (water > 0) {
            double lift = Math.min(water, this.getBbHeight()) * BUOYANCY;
            motion = new Vec3(motion.x * WATER_DRAG, motion.y * WATER_DAMPING + lift - GRAVITY, motion.z * WATER_DRAG);
        } else {
            motion = new Vec3(motion.x * AIR_DRAG, (motion.y - GRAVITY) * AIR_DRAG, motion.z * AIR_DRAG);
        }

        Vec3 start = this.position();
        this.move(MoverType.SELF, motion);
        Vec3 moved = this.position().subtract(start);

        double vx = motion.x;
        double vy = motion.y;
        double vz = motion.z;
        if (this.horizontalCollision) {
            if (Math.abs(moved.x) < Math.abs(motion.x) - 1.0E-4) {
                vx = -motion.x * WALL_BOUNCE;
            }
            if (Math.abs(moved.z) < Math.abs(motion.z) - 1.0E-4) {
                vz = -motion.z * WALL_BOUNCE;
            }
            if (motion.horizontalDistanceSqr() > MIN_BOUNCE_SPEED * MIN_BOUNCE_SPEED) {
                playBounceSound(motion.horizontalDistance());
            }
        }
        if (this.verticalCollision) {
            if (motion.y < 0) {
                double impact = -motion.y;
                if (impact > MIN_BOUNCE_SPEED) {
                    vy = impact * GROUND_BOUNCE;
                    onGroundImpact(impact);
                } else {
                    vy = 0;
                }
            } else {
                vy = -motion.y * 0.3;
            }
        }
        if (this.onGround() && vy <= 0) {
            double friction = rollingFriction();
            vx *= friction;
            vz *= friction;
        }
        if (vx * vx + vz * vz < MIN_ROLL_SPEED * MIN_ROLL_SPEED) {
            vx = 0;
            vz = 0;
        }
        this.setDeltaMovement(vx, vy, vz);
    }

    private double rollingFriction() {
        BlockState ground = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement());
        return Mth.clamp(0.9 + (ground.getBlock().getFriction() - 0.6) * 0.3, 0.8, 0.985);
    }

    private void onGroundImpact(double impact) {
        playBounceSound(impact);
        if (impact > 0.2 && this.level() instanceof ServerLevel serverLevel) {
            BlockState ground = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement());
            if (!ground.isAir()) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), this.getX(), this.getY(), this.getZ(), 6, 0.15, 0.0, 0.15, 0.05);
            }
        }
    }

    private void playBounceSound(double speed) {
        if (this.level().isClientSide) {
            return;
        }
        float volume = (float) Mth.clamp(speed * 0.6, 0.05, 0.3);
        float pitch = 1.1F + this.random.nextFloat() * 0.2F;
        this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE.value(), volume, pitch);
    }

    private void updateOrientation() {
        orientationO.set(orientation);
        double dx = this.getX() - this.xo;
        double dz = this.getZ() - this.zo;
        double distance = Math.sqrt(dx * dx + dz * dz);
        if (distance > 1.0E-4) {
            Vector3f axis = new Vector3f((float) (dz / distance), 0, (float) (-dx / distance));
            orientation.premul(new Quaternionf().rotationAxis((float) (distance / RADIUS), axis)).normalize();
        }
        if (distance < SETTLE_SPEED && Math.abs(this.getY() - this.yo) < 1.0E-3) {
            orientation.slerp(nearestFlatOrientation(), SETTLE_RATE).normalize();
        }
    }

    private Quaternionf nearestFlatOrientation() {
        Matrix3f rotation = new Matrix3f().set(orientation);
        Vector3f x = snapToAxis(rotation.getColumn(0, new Vector3f()));
        Vector3f column = rotation.getColumn(1, new Vector3f());
        column.sub(new Vector3f(x).mul(column.dot(x)));
        Vector3f y = snapToAxis(column);
        Vector3f z = new Vector3f(x).cross(y);
        return new Quaternionf().setFromNormalized(new Matrix3f(x, y, z));
    }

    private static Vector3f snapToAxis(Vector3f v) {
        float ax = Math.abs(v.x);
        float ay = Math.abs(v.y);
        float az = Math.abs(v.z);
        if (ax >= ay && ax >= az) {
            return new Vector3f(Math.signum(v.x), 0, 0);
        }
        if (ay >= az) {
            return new Vector3f(0, Math.signum(v.y), 0);
        }
        return new Vector3f(0, 0, Math.signum(v.z));
    }

    public Quaternionf getOrientation(float partialTick) {
        return new Quaternionf(orientationO).slerp(orientation, partialTick);
    }

    public static float getRadius() {
        return RADIUS;
    }

    public void kick(Vec3 direction, double horizontalSpeed, double verticalSpeed, Entity kicker) {
        Vec3 flat = new Vec3(direction.x, 0, direction.z);
        if (flat.lengthSqr() < 1.0E-6) {
            flat = this.position().subtract(kicker.position()).multiply(1, 0, 1);
        }
        if (flat.lengthSqr() < 1.0E-6) {
            flat = new Vec3(this.random.nextDouble() - 0.5, 0, this.random.nextDouble() - 0.5);
        }
        flat = flat.normalize();
        if (isNamed("CR-055")) {
            flat = flat.reverse();
        }
        double vy = Math.max(this.getDeltaMovement().y, verticalSpeed);
        this.setDeltaMovement(flat.x * horizontalSpeed, vy, flat.z * horizontalSpeed);
        this.hurtMarked = true;
        touchCooldown = TOUCH_COOLDOWN;
        playKickSound(horizontalSpeed);
    }

    public void catchInNet() {
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.15, 0.4, 0.15));
        this.hurtMarked = true;
    }

    private void playKickSound(double strength) {
        float pitch = (float) Mth.clamp(1.15 - strength * 0.4, 0.7, 1.2) + this.random.nextFloat() * 0.1F;
        SoundEvent sound = SoundEvents.ARMOR_EQUIP_TURTLE.value();
        if (isNamed("Steve")) {
            sound = SoundEvents.VILLAGER_HURT;
        } else if (isNamed("MissLilitu")) {
            sound = SoundEvents.FLOWERING_AZALEA_BREAK;
        }
        this.playSound(sound, 0.4F, pitch);
    }

    private boolean isNamed(String name) {
        return this.getCustomName() != null && name.equalsIgnoreCase(this.getCustomName().getString());
    }

    @Override
    public void push(@NotNull Entity entity) {
        if (entity instanceof Player player && !this.level().isClientSide) {
            if (touchCooldown > 0) {
                return;
            }
            Vec3 away = this.position().subtract(player.position());
            if (player.isShiftKeyDown()) {
                kick(away, 0.2, 0.0, player);
            } else if (player.isSprinting()) {
                kick(away, 0.75, 0.3, player);
            } else {
                kick(away, 0.42, 0.15, player);
            }
        } else if (!(entity instanceof Player)) {
            super.push(entity);
        }
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.NAME_TAG) && held.has(DataComponents.CUSTOM_NAME)) {
            if (!this.level().isClientSide) {
                this.setCustomName(held.getHoverName());
                held.consume(1, player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (!this.level().isClientSide) {
            if (player.isShiftKeyDown()) {
                this.discard();
                ItemStack beachBallItem = new ItemStack(ObjectRegistry.BEACH_BALL.get());
                if (this.hasCustomName()) {
                    beachBallItem.set(DataComponents.CUSTOM_NAME, this.getCustomName());
                }
                if (!player.getInventory().add(beachBallItem)) {
                    player.drop(beachBallItem, false);
                }
            } else {
                Vec3 look = player.getLookAngle();
                kick(look, 0.7, 0.3 + Math.max(0, look.y) * 0.5, player);
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity attacker) {
        if (!this.level().isClientSide && attacker instanceof Player player) {
            Vec3 look = player.getLookAngle();
            kick(look, 0.95, 0.35 + Math.max(0, look.y) * 0.6, player);
        }
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.level().isClientSide && source.getDirectEntity() instanceof Projectile projectile) {
            kick(projectile.getDeltaMovement(), 0.6, 0.25, projectile);
        }
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ObjectRegistry.BEACH_BALL.get());
    }
}
