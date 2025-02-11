package net.satisfy.beachparty.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.alchemy.PotionUtils;
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

//TODO
@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void onSand(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player playerEntity = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockState blockState = world.getBlockState(blockPos);
        if (context.getClickedFace() != Direction.DOWN && (blockState.getBlock() == Blocks.SAND || blockState.getBlock() == Blocks.GRAVEL) && PotionUtils.getPotion(itemStack) == Potions.WATER) {
            world.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
            assert playerEntity != null;
            playerEntity.setItemInHand(context.getHand(), ItemUtils.createFilledResult(itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
            playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;

                for (int i = 0; i < 5; ++i) {
                    serverWorld.sendParticles(ParticleTypes.SPLASH, (double) blockPos.getX() + world.random.nextDouble(), blockPos.getY() + 1, (double) blockPos.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }

            world.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            world.setBlockAndUpdate(blockPos, blockState.getBlock() == Blocks.GRAVEL ? Blocks.SAND.defaultBlockState() : ObjectRegistry.SANDWAVES.get().defaultBlockState());
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}
