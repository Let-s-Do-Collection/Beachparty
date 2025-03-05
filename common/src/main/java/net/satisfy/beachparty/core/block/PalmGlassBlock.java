package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class PalmGlassBlock extends Block {
    public static final EnumProperty<VerticalPart> PART = EnumProperty.create("part", VerticalPart.class);

    public PalmGlassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, VerticalPart.NONE));
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide()) {
            updateVertical(level, pos);
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide()) {
            updateVertical(level, pos);
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockPos up = pos.above();
            BlockPos down = pos.below();
            if (level.getBlockState(up).getBlock() == this) {
                updateVertical(level, up);
            }
            if (level.getBlockState(down).getBlock() == this) {
                updateVertical(level, down);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    private void updateVertical(Level level, BlockPos pos) {
        BlockPos bottom = pos;
        while (level.getBlockState(bottom.below()).getBlock() == this) {
            bottom = bottom.below();
        }
        BlockPos top = pos;
        while (level.getBlockState(top.above()).getBlock() == this) {
            top = top.above();
        }
        List<Vector3i> positions = new ArrayList<>();
        Vector3i current = new Vector3i(bottom.getX(), bottom.getY(), bottom.getZ());
        Vector3i end = new Vector3i(top.getX(), top.getY(), top.getZ());
        positions.add(new Vector3i(current.x, current.y, current.z));
        while (!current.equals(end)) {
            current = new Vector3i(current.x, current.y + 1, current.z);
            positions.add(new Vector3i(current.x, current.y, current.z));
        }
        if (positions.size() == 1) {
            Vector3i v = positions.get(0);
            BlockPos bp = new BlockPos(v.x, v.y, v.z);
            level.setBlock(bp, level.getBlockState(bp).setValue(PART, VerticalPart.NONE), 3);
        } else {
            for (int i = 0; i < positions.size(); i++) {
                Vector3i v = positions.get(i);
                BlockPos bp = new BlockPos(v.x, v.y, v.z);
                BlockState s = level.getBlockState(bp);
                if (i == 0) {
                    s = s.setValue(PART, VerticalPart.BOTTOM);
                } else if (i == positions.size() - 1) {
                    s = s.setValue(PART, VerticalPart.TOP);
                } else {
                    s = s.setValue(PART, VerticalPart.MIDDLE);
                }
                level.setBlock(bp, s, 3);
            }
        }
    }

    public enum VerticalPart implements StringRepresentable {
        NONE("none"),
        BOTTOM("bottom"),
        MIDDLE("middle"),
        TOP("top");

        private final String name;

        VerticalPart(String name) {
            this.name = name;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
