package net.satisfy.beachparty.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void onWaterUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = world.getBlockState(pos);
        if (context.getClickedFace() != Direction.DOWN
                && (state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.GRAVEL)
                && Objects.requireNonNull(stack.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER)) {
            world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
            assert player != null;
            player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;
                for (int i = 0; i < 5; ++i) {
                    serverWorld.sendParticles(ParticleTypes.SPLASH, pos.getX() + world.random.nextDouble(), pos.getY() + 1, pos.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }
            world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            world.setBlockAndUpdate(pos, state.getBlock() == Blocks.GRAVEL ? Blocks.SAND.defaultBlockState() : ObjectRegistry.SANDWAVES.get().defaultBlockState());
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            return;
        }
        if (context.getClickedFace() != Direction.DOWN
                && state.getBlock() == Blocks.HAY_BLOCK
                && Objects.requireNonNull(stack.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER)) {
            world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
            assert player != null;
            player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;
                for (int i = 0; i < 5; ++i) {
                    serverWorld.sendParticles(ParticleTypes.SPLASH, pos.getX() + world.random.nextDouble(), pos.getY() + 1, pos.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }
            world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            world.setBlockAndUpdate(pos, ObjectRegistry.WET_HAY_BLOCK.get().defaultBlockState());
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}
