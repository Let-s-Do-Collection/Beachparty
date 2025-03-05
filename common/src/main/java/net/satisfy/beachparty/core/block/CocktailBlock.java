package net.satisfy.beachparty.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CocktailBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final VoxelShape SWEETBERRIES_COCKTAIL_SHAPE = Shapes.or(
            Shapes.box(0.375, 0.0, 0.375, 0.625, 0.3125, 0.625),
            Shapes.box(0.3125, 0.3125, 0.3125, 0.6875, 0.75, 0.6875)
    );
    public static final VoxelShape MELON_COCKTAIL_SHAPE = Shapes.or(
            Shapes.box(0.375, 0.0, 0.375, 0.625, 0.0625, 0.625),
            Shapes.box(0.4375, 0.0625, 0.4375, 0.5625, 0.375, 0.5625),
            Shapes.box(0.3125, 0.375, 0.3125, 0.6875, 0.75, 0.6875)
    );
    public static final VoxelShape COCONUT_COCKTAIL_SHAPE = Shapes.box(0.25, 0.0, 0.25, 0.75, 0.5, 0.75);
    public static final VoxelShape PUMPKIN_COCKTAIL_SHAPE = Shapes.box(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);
    public static final VoxelShape HONEY_COCKTAIL_SHAPE = Shapes.or(
            Shapes.box(0.375, 0.0, 0.375, 0.625, 0.0625, 0.625),
            Shapes.box(0.4375, 0.0625, 0.4375, 0.5625, 0.25, 0.5625),
            Shapes.box(0.3125, 0.25, 0.3125, 0.6875, 0.8125, 0.6875)
    );
    public static final VoxelShape COCOA_COCKTAIL_SHAPE = Shapes.or(
            Shapes.box(0.375, 0.0, 0.375, 0.625, 0.0625, 0.625),
            Shapes.box(0.4375, 0.0625, 0.4375, 0.5625, 0.375, 0.5625),
            Shapes.box(0.3125, 0.375, 0.3125, 0.6875, 0.75, 0.6875)
    );
    private final MobEffect effect;
    private final int effectDuration;
    private final Supplier<VoxelShape> shapeSupplier;

    public CocktailBlock(BlockBehaviour.Properties settings, MobEffect effect, int effectDuration, Supplier<VoxelShape> shapeSupplier) {
        super(settings);
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.shapeSupplier = shapeSupplier;
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(STAGE, 3));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapeSupplier.get();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            int stage = state.getValue(STAGE);
            if (stage > 1) {
                player.addEffect(new MobEffectInstance(effect, effectDuration, 0));
                world.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.setBlock(pos, state.setValue(STAGE, stage - 1), 3);
            } else if (stage == 1) {
                world.setBlock(pos, state.setValue(STAGE, 0), 3);
            } else {
                world.destroyBlock(pos, false);
            }
        }
        return InteractionResult.SUCCESS;
    }

}
