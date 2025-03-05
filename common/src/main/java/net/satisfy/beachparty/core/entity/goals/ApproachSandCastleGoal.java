package net.satisfy.beachparty.core.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.SandBucketBlock;
import org.joml.Vector3d;

public class ApproachSandCastleGoal extends Goal {
    private final Mob mob;
    private BlockPos targetPos;

    public ApproachSandCastleGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        Level level = this.mob.level();
        BlockPos mobPos = this.mob.blockPosition();
        int radius = 12;
        for (BlockPos pos : BlockPos.betweenClosed(mobPos.offset(-radius, -radius, -radius), mobPos.offset(radius, radius, radius))) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof SandBucketBlock.SandCastleBlock) {
                targetPos = pos;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetPos == null) return false;
        Level level = this.mob.level();
        BlockState state = level.getBlockState(targetPos);
        if (!(state.getBlock() instanceof SandBucketBlock.SandCastleBlock)) return false;
        Vector3d mobPos = new Vector3d(this.mob.getX(), this.mob.getY(), this.mob.getZ());
        Vector3d targetVector = new Vector3d(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        return mobPos.distance(targetVector) > 2;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            this.mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetPos != null) {
            this.mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void stop() {
        targetPos = null;
    }
}
