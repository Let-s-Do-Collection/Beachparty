package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.beachparty.core.block.BeachGoalBlock;
import net.satisfy.beachparty.core.entity.BeachBallEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;

import java.util.List;

public class BeachGoalBlockEntity extends BlockEntity {
    private boolean hasBeachBall = false;

    public BeachGoalBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BEACH_GOAL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BeachGoalBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            BeachGoalBlock block = (BeachGoalBlock) state.getBlock();
            VoxelShape detectionShape = block.getDetectionShape(state);
            AABB detectionAABB = detectionShape.bounds().move(pos);

            List<BeachBallEntity> entities = serverLevel.getEntitiesOfClass(BeachBallEntity.class, detectionAABB, Entity::isAlive);
            boolean previouslyHadBeachBall = blockEntity.hasBeachBall;
            blockEntity.hasBeachBall = !entities.isEmpty();

            if (blockEntity.hasBeachBall && !previouslyHadBeachBall) {
                for (BeachBallEntity ignored : entities) {
                    blockEntity.fireRocket(serverLevel, pos, new int[]{0xADD8E6}, -0.05);
                    blockEntity.fireRocket(serverLevel, pos, new int[]{0xFFFFFF}, 0);
                    blockEntity.fireRocket(serverLevel, pos, new int[]{0xF4A460}, 0.05);
                }
                level.updateNeighborsAt(pos, state.getBlock());
            } else if (!blockEntity.hasBeachBall && previouslyHadBeachBall) {
                level.updateNeighborsAt(pos, state.getBlock());
            }
        }
    }

    public boolean hasBeachBall() {
        return hasBeachBall;
    }

    private void fireRocket(ServerLevel world, BlockPos pos, int[] colors, double dx) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
        CompoundTag fireworkTag = new CompoundTag();

        CompoundTag explosion = new CompoundTag();
        explosion.putIntArray("Colors", colors);
        explosion.putByte("Type", (byte)1);

        ListTag explosionsList = new ListTag();
        explosionsList.add(explosion);

        CompoundTag fireworks = new CompoundTag();
        fireworks.putByte("Flight", (byte)1);
        fireworks.put("Explosions", explosionsList);

        fireworkTag.put("Fireworks", fireworks);
        fireworkItem.setTag(fireworkTag);

        FireworkRocketEntity rocket = new FireworkRocketEntity(world, x, y, z, fireworkItem);
        rocket.setDeltaMovement(dx, 0.5, 0);
        world.addFreshEntity(rocket);
    }


    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("HasBeachBall", hasBeachBall);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        hasBeachBall = tag.getBoolean("HasBeachBall");
    }
}
