package net.satisfy.beachparty.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SandBucketItem extends BlockItem {
    public SandBucketItem(Block block, Properties settings) {
        super(block, settings);
    }

    public static ItemStack getEmptiedStack(ItemStack stack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(ObjectRegistry.SAND_BUCKET_EMPTY.get()) : stack;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {
            InteractionResult actionResult = this.place(new BlockPlaceContext(context));
            if (!actionResult.consumesAction() && this.getDefaultInstance().has(DataComponents.FOOD)) {
                InteractionResult actionResult2 = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
                return actionResult2 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : actionResult2;
            }
            return actionResult;
        } else {
            return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos offsetPos = blockPos.relative(direction);
            if (world.mayInteract(user, blockPos)) {
                if (this == ObjectRegistry.SAND_BUCKET_EMPTY.get() && world.getBlockState(blockPos).is(BlockTags.SAND)) {
                    world.destroyBlock(blockPos, false);
                    ItemStack sandBucket = new ItemStack(ObjectRegistry.SAND_BUCKET_FILLED.get());
                    ItemStack returnStack = exchangeStack(itemStack, user, sandBucket);
                    return InteractionResultHolder.sidedSuccess(returnStack, world.isClientSide());
                } else if (this == ObjectRegistry.SAND_BUCKET_FILLED.get() && user.mayUseItemAt(offsetPos, direction, itemStack)) {
                    if (world.getBlockState(offsetPos).isAir() && ObjectRegistry.SANDCASTLE.get().defaultBlockState().canSurvive(world, offsetPos)) {
                        world.setBlock(offsetPos, ObjectRegistry.SANDCASTLE.get().defaultBlockState(), 3);
                        ItemStack returnStack = exchangeStack(itemStack, user, getEmptiedStack(itemStack, user));
                        return InteractionResultHolder.sidedSuccess(returnStack, world.isClientSide());
                    }
                }
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private ItemStack exchangeStack(ItemStack handStack, Player player, ItemStack possibleReturnStack) {
        ItemStack returnStack = !player.getAbilities().instabuild ? possibleReturnStack : handStack;
        if (player.isCreative()) {
            if (!player.getInventory().contains(possibleReturnStack)) {
                player.getInventory().add(possibleReturnStack);
            }
            return handStack;
        }
        Inventory inventory = player.getInventory();
        int slot = inventory.findSlotMatchingItem(handStack);
        handStack.shrink(1);
        if (player.getInventory().getItem(slot).isEmpty()) {
            if (!inventory.add(slot, returnStack)) {
                player.drop(returnStack, false);
            }
        } else {
            if (!inventory.add(returnStack)) {
                player.drop(returnStack, false);
            }
        }
        return possibleReturnStack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }
}