package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.*;

@SuppressWarnings("deprecation")
public class PalmGlassPaneBlock extends IronBarsBlock {
    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 3);

    public PalmGlassPaneBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            updateAllConnected(world, pos);
        }
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        if (!world.isClientSide()) {
            updateAllConnected(world, pos);
        }
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!world.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockPos up = pos.above();
            BlockPos down = pos.below();
            if (world.getBlockState(up).getBlock() == this) {
                updateAllConnected(world, up);
            }
            if (world.getBlockState(down).getBlock() == this) {
                updateAllConnected(world, down);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }

    private void updateAllConnected(Level world, BlockPos start) {
        Queue<Vector3i> queue = new LinkedList<>();
        Set<Vector3i> visited = new HashSet<>();
        Vector3i origin = new Vector3i(start.getX(), start.getY(), start.getZ());
        queue.add(origin);
        visited.add(origin);
        while (!queue.isEmpty()) {
            Vector3i current = queue.poll();
            for (Direction d : Direction.values()) {
                Vector3i next = new Vector3i(current.x + d.getStepX(), current.y + d.getStepY(), current.z + d.getStepZ());
                BlockPos bp = new BlockPos(next.x, next.y, next.z);
                if (world.getBlockState(bp).getBlock() == this && !visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        Map<Long, List<Vector3i>> columns = new HashMap<>();
        for (Vector3i v : visited) {
            long key = ((long) v.x << 32) ^ (v.z & 0xffffffffL);
            columns.computeIfAbsent(key, k -> new ArrayList<>()).add(v);
        }
        for (List<Vector3i> col : columns.values()) {
            col.sort(Comparator.comparingInt(o -> o.y));
            for (int i = 0; i < col.size(); i++) {
                Vector3i v = col.get(i);
                BlockPos bp = new BlockPos(v.x, v.y, v.z);
                BlockState s = world.getBlockState(bp);
                int size = col.size();
                int part;
                if (size == 1) {
                    part = 0;
                } else if (i == 0) {
                    part = 1;
                } else if (i == size - 1) {
                    part = 3;
                } else {
                    part = 2;
                }
                world.setBlock(bp, s.setValue(PART, part), 3);
            }
        }
    }
}
