package net.satisfy.beachparty.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.satisfy.beachparty.Beachparty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeashellItem extends BlockItem {
    public SeashellItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> resultHolder = super.use(world, player, hand);
        ItemStack stack = resultHolder.getObject();
        player.swing(hand);
        if (!world.isClientSide) {
            world.playSound(player, player.blockPosition().above(),
                    SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 1, 1);
            final MinecraftServer minecraftServer = player.level().getServer();
            if (minecraftServer != null && player.level() instanceof ServerLevel server) {
                LootParams lootContext = new LootParams.Builder(server)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .create(LootContextParamSets.GIFT);
                LootTable treasure = minecraftServer.getLootData().getLootTable(new ResourceLocation(Beachparty.MOD_ID, "gameplay/seashell"));
                treasure.getRandomItems(lootContext).forEach(player::addItem);
            }
        }
        stack.shrink(1);
        return resultHolder;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(style -> style.withColor(TextColor.fromRgb(0xD4B483))));
    }
}
