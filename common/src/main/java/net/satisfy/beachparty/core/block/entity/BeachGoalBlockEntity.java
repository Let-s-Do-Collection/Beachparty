package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
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
import org.joml.Vector3d;

import java.util.List;

public class BeachGoalBlockEntity extends BlockEntity {
    private static final int PRESENCE_THRESHOLD = 10;
    private boolean hasBeachBall = false;
    private int ballPresenceCounter = 0;

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
        int rocketCount = world.random.nextInt(5) + 3;
        int[][] colorOptions = new int[][]{{0xADD8E6}, {0xFFFFFF}, {0xF4A460}};
        for (int i = 0; i < rocketCount; i++) {
            double dx = (world.random.nextDouble() - 0.5) * 0.2;
            double dz = (world.random.nextDouble() - 0.5) * 0.2;
            double dy = 1.2 + (world.random.nextDouble() - 0.5) * 0.1;
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 2.0;
            double z = pos.getZ() + 0.5;
            ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
            CustomData fireworkTag = CustomData.of(new CompoundTag());
            CustomData explosion = CustomData.of(new CompoundTag());
            int[] colors = colorOptions[world.random.nextInt(colorOptions.length)];
            explosion.copyTag().putIntArray("Colors", colors);
            explosion.copyTag().putByte("Type", (byte) 1);
            ListTag explosionsList = new ListTag();
            explosionsList.add(explosion.copyTag());
            CustomData fireworks = CustomData.of(new CompoundTag());
            fireworks.copyTag().putByte("Flight", (byte) 1);
            fireworks.copyTag().put("Explosions", explosionsList);
            fireworkTag.copyTag().put("Fireworks", fireworks.copyTag());
            fireworkItem.set(DataComponents.CUSTOM_DATA, fireworkTag);
            FireworkRocketEntity rocket = new FireworkRocketEntity(world, x, y, z, fireworkItem);
            Vector3d velocity = new Vector3d(dx, dy, dz);
            rocket.setDeltaMovement(velocity.x, velocity.y, velocity.z);
            world.addFreshEntity(rocket);
        }
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
