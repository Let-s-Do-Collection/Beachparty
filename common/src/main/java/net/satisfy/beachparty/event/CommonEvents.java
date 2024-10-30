package net.satisfy.beachparty.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.block.furniture.RadioBlock;
import net.satisfy.beachparty.effect.NeverMeltEffect;
import net.satisfy.beachparty.registry.MobEffectRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
        PlayerEvent.PLAYER_JOIN.register(CommonEvents::onPlayerJoin);
        PlayerEvent.ATTACK_ENTITY.register(CommonEvents::onPlayerAttack);
    }

    public static void onModifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext ctx, boolean b) {
        LoottableInjector.InjectLoot(id, ctx);
    }

    private static void onPlayerJoin(ServerPlayer player) {
        ServerLevel world = player.serverLevel();
        for (BlockPos pos : RadioBlock.getAllRadioBlocks()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof RadioBlock && state.getValue(RadioBlock.ON)) {
                RadioBlock.sendPacket(state, world, pos, true);
            }
        }
    }

    private static EventResult onPlayerAttack(Player player, Level level, Entity entity, InteractionHand hand, @Nullable EntityHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);

        if (itemInHand.getItem() == ObjectRegistry.POOL_NOODLE.get()) {
            Vec3 knockbackDirection = new Vec3(
                    entity.getX() - player.getX(),
                    0.2,
                    entity.getZ() - player.getZ()
            ).normalize().scale(1.5);

            entity.push(knockbackDirection.x, knockbackDirection.y, knockbackDirection.z);

            return EventResult.interruptTrue();
        }

        if (player.hasEffect(MobEffectRegistry.NEVERMELT.get())) {
            MobEffectInstance effect = player.getEffect(MobEffectRegistry.NEVERMELT.get());
            if (effect != null && effect.getEffect() instanceof NeverMeltEffect neverMeltEffect) {
                neverMeltEffect.onPlayerHurt(player, level.damageSources().generic(), 0);
            }
        }

        return EventResult.pass();
    }

    public static class LoottableInjector {
        public static void InjectLoot(ResourceLocation id, LootEvent.LootTableModificationContext context) {
            String prefix = "minecraft:chests/";
            String name = id.toString();

            if (name.startsWith(prefix)) {
                String file = name.substring(name.indexOf(prefix) + prefix.length());
                switch (file) {
                    case "desert_pyramid", "buried_treasure", "shipwreck_supply", "shipwreck_treasure",
                         "simple_dungeon", "underwater_ruin_big", "underwater_ruin_small", "woodland_mansion",
                         "village/village_cartographer", "village/plains_house", "village_savanna_house" ->
                            context.addPool(getPool(file));
                    default -> {
                    }
                }
            }
        }

        public static LootPool getPool(String entryName) {
            return LootPool.lootPool().add(getPoolEntry(entryName)).build();
        }

        @SuppressWarnings("rawtypes")
        private static LootPoolEntryContainer.Builder getPoolEntry(String name) {
            ResourceLocation table = new ResourceLocation(Beachparty.MOD_ID, "chests/" + name);
            return LootTableReference.lootTableReference(table);
        }
    }
}
