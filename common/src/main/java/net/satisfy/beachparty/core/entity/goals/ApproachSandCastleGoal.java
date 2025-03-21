package net.satisfy.beachparty.core.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.SandBucketBlock;
import net.satisfy.beachparty.core.util.SandCastleManager;

public class ApproachSandCastleGoal extends Goal {
    private final Mob mob;
    private BlockPos targetPos;
    private int cooldown;
    private int recalculateCooldown;
    private boolean isMoving;

    public ApproachSandCastleGoal(Mob mob) {
        this.mob = mob;
        this.cooldown = 0;
        this.recalculateCooldown = 0;
        this.isMoving = false;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        cooldown = 60;  

        if (recalculateCooldown > 0) {
            recalculateCooldown--;
        } else {
            recalculateCooldown = 200;  
            BlockPos mobPos = this.mob.blockPosition();
            BlockPos newTargetPos = SandCastleManager.getNearestSandCastle(mobPos);

            if (newTargetPos == null || newTargetPos.equals(targetPos)) {
                return false;
            }

            targetPos = newTargetPos;
            isMoving = false;  
        }

        return targetPos != null;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            moveToTarget();
        }
    }

    private void moveToTarget() {
        this.mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        isMoving = true;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetPos == null) return false;
        Level level = this.mob.level();
        BlockState state = level.getBlockState(targetPos);

        if (!(state.getBlock() instanceof SandBucketBlock.SandCastleBlock)) return false;

        double distanceSquared = this.mob.distanceToSqr(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        return distanceSquared <= 16.0;
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        Level level = this.mob.level();
        BlockState state = level.getBlockState(targetPos);

        if (!(state.getBlock() instanceof SandBucketBlock.SandCastleBlock)) {
            stop();
            return;
        }

        double distanceSquared = this.mob.distanceToSqr(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        if (distanceSquared > 2.25) {
            if (!isMoving) {
                moveToTarget();
            }
        } else {
            isMoving = false;
        }
    }

    @Override
    public void stop() {
        targetPos = null;
        isMoving = false;
    }
}
