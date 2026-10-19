package net.satisfy.beachparty.core.block.entity;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.core.block.BeachGoalBlock;
import net.satisfy.beachparty.core.entity.BeachBallEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;

import java.util.List;

public class BeachGoalBlockEntity extends BlockEntity {
    public static final int EVENT_FIREWORK = 1;
    private static final int BURSTS = 3;
    private static final int BURST_INTERVAL = 6;
    private static final double LAUNCH_HEIGHT = 1.75;
    private static final double ROCKET_SPEED = 0.4;
    private static final int GOAL_COOLDOWN = 40;
    private static final int[] PALETTE = {0xFF6F61, 0xFFD23F, 0x3BCEAC, 0x0EAD69, 0x4CC9F0, 0xFFFFFF, 0xF78C6B};

    private boolean hasBeachBall = false;
    private int celebrationTicks = 0;
    private int cooldown = 0;

    public BeachGoalBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BEACH_GOAL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BeachGoalBlockEntity goal) {
        if (!(level instanceof ServerLevel serverLevel) || state.getValue(BeachGoalBlock.PART) != BeachGoalBlock.Part.BOTTOM_LEFT) {
            return;
        }
        if (goal.cooldown > 0) {
            goal.cooldown--;
        }
        if (goal.celebrationTicks > 0) {
            goal.flyRockets(serverLevel, pos, state);
        }

        Direction facing = state.getValue(BeachGoalBlock.FACING);
        AABB area = BeachGoalBlock.goalArea(pos, facing);
        List<BeachBallEntity> balls = serverLevel.getEntitiesOfClass(BeachBallEntity.class, area, Entity::isAlive);
        BeachBallEntity scored = null;
        for (BeachBallEntity ball : balls) {
            if (area.contains(ball.getBoundingBox().getCenter())) {
                scored = ball;
                break;
            }
        }

        boolean inside = scored != null;
        if (inside == goal.hasBeachBall) {
            return;
        }
        goal.hasBeachBall = inside;
        goal.setChanged();
        if (inside && goal.cooldown == 0) {
            scored.catchInNet();
            goal.cooldown = GOAL_COOLDOWN;
            goal.celebrationTicks = celebrationLength();
            level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.6F, 1.3F);
        }
        for (BlockPos part : BeachGoalBlock.partPositions(pos, facing)) {
            level.updateNeighborsAt(part, state.getBlock());
        }
    }

    private void flyRockets(ServerLevel level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(BeachGoalBlock.FACING);
        int elapsed = celebrationLength() - celebrationTicks;
        for (int burst = 0; burst < BURSTS; burst++) {
            int age = elapsed - burst * BURST_INTERVAL;
            int flight = flightTicks(burst);
            if (age < 0 || age > flight) {
                continue;
            }
            if (age == flight) {
                level.blockEvent(pos, state.getBlock(), EVENT_FIREWORK, burst);
                continue;
            }
            Vec3 at = rocketPosition(pos, facing, burst, age);
            if (age == 0) {
                level.playSound(null, at.x, at.y, at.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.BLOCKS, 1.5F, 1.0F);
            }
            level.sendParticles(ParticleTypes.FIREWORK, at.x, at.y, at.z, 2, 0.03, 0.1, 0.03, 0.01);
        }
        celebrationTicks--;
    }

    public static void spawnFirework(Level level, BlockPos pos, Direction facing, int burst) {
        RandomSource random = level.getRandom();
        Vec3 at = rocketPosition(pos, facing, burst, flightTicks(burst));
        FireworkExplosion.Shape shape = burst == 1 ? FireworkExplosion.Shape.STAR : FireworkExplosion.Shape.SMALL_BALL;
        FireworkExplosion explosion = new FireworkExplosion(shape, colors(random, 2), colors(random, 1), true, burst == 1);
        level.createFireworks(at.x, at.y, at.z, 0, 0, 0, List.of(explosion));
    }

    private static Vec3 rocketPosition(BlockPos pos, Direction facing, int burst, int age) {
        double localX = switch (burst) {
            case 0 -> 0.1875;
            case 2 -> 1.8125;
            default -> 1.0;
        };
        double drift = (burst - 1) * 0.04 * age;
        return BeachGoalBlock.toWorld(pos, facing, localX + drift, LAUNCH_HEIGHT + ROCKET_SPEED * age, 0.25);
    }

    private static int flightTicks(int burst) {
        return burst == 1 ? 20 : 14;
    }

    private static int celebrationLength() {
        int length = 0;
        for (int burst = 0; burst < BURSTS; burst++) {
            length = Math.max(length, burst * BURST_INTERVAL + flightTicks(burst) + 1);
        }
        return length;
    }

    private static IntList colors(RandomSource random, int count) {
        IntList out = new IntArrayList(count);
        for (int i = 0; i < count; i++) {
            out.add(PALETTE[random.nextInt(PALETTE.length)]);
        }
        return out;
    }

    public boolean hasBeachBall() {
        return hasBeachBall;
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("HasBeachBall", hasBeachBall);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        hasBeachBall = compoundTag.getBoolean("HasBeachBall");
    }
}
