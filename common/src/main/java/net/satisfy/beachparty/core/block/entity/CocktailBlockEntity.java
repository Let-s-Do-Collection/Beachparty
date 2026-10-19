package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;

public class CocktailBlockEntity extends BlockEntity {
    private int shownStage = -1;
    private float fromLevel;
    private float changeTime;

    public CocktailBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.COCKTAIL_BLOCK_ENTITY.get(), pos, state);
    }

    public int getShownStage() {
        return shownStage;
    }

    public float getFromLevel() {
        return fromLevel;
    }

    public float getChangeTime() {
        return changeTime;
    }

    public void show(int stage, float fromLevel, float changeTime) {
        this.shownStage = stage;
        this.fromLevel = fromLevel;
        this.changeTime = changeTime;
    }
}
