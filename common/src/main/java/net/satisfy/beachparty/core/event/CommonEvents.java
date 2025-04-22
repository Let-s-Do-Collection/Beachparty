package net.satisfy.beachparty.core.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.satisfy.beachparty.core.block.BeachParasolBlock;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
        PlayerEvent.ATTACK_ENTITY.register(CommonEvents::onPlayerAttack);
        EntityEvent.LIVING_HURT.register(CommonEvents::onLivingHurt);
    }

    public static void onModifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext ctx, boolean b) {
        LoottableInjector.InjectLoot(id, ctx);
    }

    private static EventResult onPlayerAttack(Player player, Level level, Entity entity, InteractionHand hand, @Nullable EntityHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);

        if (itemInHand.getItem() == ObjectRegistry.POOL_NOODLE.get()) {
            Vec3 knockbackDirection = new Vec3(entity.getX() - player.getX(), 0.6, entity.getZ() - player.getZ()).normalize().scale(1.5);

            entity.push(knockbackDirection.x, knockbackDirection.y, knockbackDirection.z);

            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PARROT_IMITATE_SLIME, SoundSource.PLAYERS, 1.0F, 1.5F);

            return EventResult.interruptTrue();
        }
        return EventResult.pass();
    }

    private static EventResult onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (entity.level().isClientSide()) return EventResult.pass();

        if (isFireDamage(source) && isNearBeachParasol(entity)) {
            float reducedDamage = amount * 0.96f;
            entity.setHealth(entity.getHealth() + (amount - reducedDamage));
            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    private static boolean isFireDamage(DamageSource source) {
        return source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA);
    }

    private static boolean isNearBeachParasol(LivingEntity entity) {
        Level level = entity.level();
        BlockPos entityPos = entity.blockPosition();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = entityPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    if (blockState.getBlock() instanceof BeachParasolBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static class LoottableInjector {
        public static void InjectLoot(ResourceLocation id, LootEvent.LootTableModificationContext context) {
            String prefix = "minecraft:chests/";
            String name = id.toString();

            if (name.startsWith(prefix)) {
                String file = name.substring(name.indexOf(prefix) + prefix.length());
                switch (file) {
                    case "desert_pyramid", "buried_treasure", "shipwreck_supply", "shipwreck_treasure",
                         "simple_dungeon", "underwater_ruin_big", "underwater_ruin_small", "woodland_mansion" ->
                            context.addPool(getPool(file));
                    default -> {
                    }
                }
            }
        }

        public static LootPool getPool(String entryName) {
            return LootPool.lootPool().add(getPoolEntry(entryName)).build();
        }

        private static LootPoolEntryContainer.Builder<?> getPoolEntry(String name) {
            ResourceLocation table = new ResourceLocation(Beachparty.MOD_ID, "chests/" + name);
            return LootTableReference.lootTableReference(table);
        }
    }
}
