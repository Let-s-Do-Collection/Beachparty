package net.satisfy.beachparty.core.block.entity;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.block.BeachGoalBlock;
import net.satisfy.beachparty.core.entity.BeachBallEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;

import java.util.List;

public class BeachGoalBlockEntity extends BlockEntity {
    private static final int PRESENCE_THRESHOLD = 10;
    private boolean hasBeachBall = false;
    private int ballPresenceCounter = 0;
    private static final int[] FW_PALETTE = {
            0xFF0000, 0xFFA500, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0x8A2BE2, 0xFF00FF,
            0xFFFFFF, 0xADD8E6, 0xF4A460, 0xFF7F50, 0x7FFF00, 0xFFD700
    };

    public BeachGoalBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BEACH_GOAL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BeachGoalBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        BeachGoalBlock block = (BeachGoalBlock) state.getBlock();
        VoxelShape detectionShape = block.getDetectionShape(state);
        VoxelShape worldShape = detectionShape.move(pos.getX(), pos.getY(), pos.getZ());
        AABB detectionAABB = worldShape.bounds();
        List<BeachBallEntity> balls = serverLevel.getEntitiesOfClass(BeachBallEntity.class, detectionAABB.inflate(0.1), Entity::isAlive);
        boolean detected = false;
        for (BeachBallEntity ball : balls) {
            VoxelShape ballShape = Shapes.create(ball.getBoundingBox());
            if (Shapes.joinIsNotEmpty(worldShape, ballShape, BooleanOp.AND)) {
                detected = true;
                break;
            }
        }
        if (detected) {
            blockEntity.ballPresenceCounter = Math.min(blockEntity.ballPresenceCounter + 1, PRESENCE_THRESHOLD);
        } else {
            blockEntity.ballPresenceCounter = Math.max(blockEntity.ballPresenceCounter - 1, 0);
        }
        boolean previouslyHadBall = blockEntity.hasBeachBall;
        blockEntity.hasBeachBall = blockEntity.ballPresenceCounter >= PRESENCE_THRESHOLD;
        if (blockEntity.hasBeachBall && !previouslyHadBall) {
            blockEntity.fireRockets(serverLevel, pos);
            level.updateNeighborsAt(pos, state.getBlock());
        } else if (!blockEntity.hasBeachBall && previouslyHadBall) {
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    public boolean hasBeachBall() {
        return hasBeachBall;
    }

    private void fireRockets(ServerLevel world, BlockPos pos) {
        int rocketCount = 3 + world.random.nextInt(3);
        for (int i = 0; i < rocketCount; i++) {
            double dx = (world.random.nextDouble() - 0.5) * 0.18;
            double dz = (world.random.nextDouble() - 0.5) * 0.18;
            double dy = 1.15 + world.random.nextDouble() * 0.25;
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 2.0;
            double z = pos.getZ() + 0.5;

            int flight = 1 + world.random.nextInt(3);
            int explosions = 1 + world.random.nextInt(3);

            ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
            List<FireworkExplosion> list = new java.util.ArrayList<>(explosions);
            for (int e = 0; e < explosions; e++) {
                list.add(randomExplosion(world.random));
            }
            fireworkItem.set(DataComponents.FIREWORKS, new Fireworks(flight, list));

            FireworkRocketEntity rocket = new FireworkRocketEntity(world, fireworkItem, x, y, z, false);
            rocket.setDeltaMovement(dx, dy, dz);
            world.addFreshEntity(rocket);
        }
    }

    private FireworkExplosion randomExplosion(RandomSource random) {
        FireworkExplosion.Shape[] shapes = FireworkExplosion.Shape.values();
        FireworkExplosion.Shape shape = shapes[random.nextInt(shapes.length)];
        IntList colors = randomPaletteColors(random, 1, 4);
        IntList fade = randomPaletteColors(random, 0, 3);
        boolean trail = random.nextBoolean();
        boolean twinkle = random.nextBoolean();
        return new FireworkExplosion(shape, colors, fade, trail, twinkle);
    }

    private IntList randomPaletteColors(RandomSource random, int min, int maxInclusive) {
        int n = min + random.nextInt(maxInclusive - min + 1);
        IntList out = new IntArrayList(n);
        for (int i = 0; i < n; i++) {
            out.add(FW_PALETTE[random.nextInt(FW_PALETTE.length)]);
        }
        return out;
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("HasBeachBall", hasBeachBall);
        compoundTag.putInt("BallPresenceCounter", ballPresenceCounter);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        hasBeachBall = compoundTag.getBoolean("HasBeachBall");
        ballPresenceCounter = compoundTag.getInt("BallPresenceCounter");
    }
}
