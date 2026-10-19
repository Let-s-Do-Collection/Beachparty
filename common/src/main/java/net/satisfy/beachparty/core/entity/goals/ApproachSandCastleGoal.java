package net.satisfy.beachparty.core.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.block.SandBucketBlock;
import net.satisfy.beachparty.core.registry.PoiTypeKeys;

import java.util.EnumSet;

public class ApproachSandCastleGoal extends Goal {
    private static final int SEARCH_RADIUS = 24;
    private static final int SEARCH_INTERVAL = 60;
    private final Mob mob;
    private BlockPos targetPos;
    private int cooldown;

    public ApproachSandCastleGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        cooldown = SEARCH_INTERVAL;
        if (!(mob.level() instanceof ServerLevel level)) return false;
        targetPos = level.getPoiManager().findClosest(type -> type.is(PoiTypeKeys.SANDCASTLE), pos -> isIntactSandCastle(level.getBlockState(pos)), mob.blockPosition(), SEARCH_RADIUS, PoiManager.Occupancy.ANY).orElse(null);
        return targetPos != null;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
    }

    @Override
    public boolean canContinueToUse() {
        return targetPos != null && !mob.getNavigation().isDone() && isIntactSandCastle(mob.level().getBlockState(targetPos));
    }

    @Override
    public void stop() {
        targetPos = null;
        mob.getNavigation().stop();
    }

    private static boolean isIntactSandCastle(BlockState state) {
        return state.getBlock() instanceof SandBucketBlock.SandCastleBlock && !state.getValue(SandBucketBlock.SandCastleBlock.PETRIFIED);
    }
}
