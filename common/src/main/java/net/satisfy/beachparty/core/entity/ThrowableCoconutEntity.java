package net.satisfy.beachparty.core.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class ThrowableCoconutEntity extends ThrowableItemProjectile {

    public ThrowableCoconutEntity(Level world, LivingEntity owner) {
        super(EntityTypeRegistry.COCONUT.get(), owner, world);
    }

    public ThrowableCoconutEntity(EntityType<? extends ThrowableCoconutEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ObjectRegistry.COCONUT.get();
    }

    private ParticleOptions getParticleParameters() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ObjectRegistry.COCONUT.get()));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            Level level = this.level();
            for (int i = 0; i < 30; i++) {
                double ox = this.random.nextGaussian() * 0.02D;
                double oy = this.random.nextGaussian() * 0.02D;
                double oz = this.random.nextGaussian() * 0.02D;
                level.addParticle(getParticleParameters(), this.getX() + ox, this.getY() + oy, this.getZ() + oz, 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        int damage = 2;
        target.hurt(target.damageSources().thrown(this, this.getOwner()), (float) damage);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        if (!level.isClientSide) {
            level.broadcastEntityEvent(this, (byte) 3);
            this.playSound(SoundEvents.WOOD_FALL, 1.0F, 1.0F);
            MinecraftServer server = level.getServer();
            if (server != null && level instanceof ServerLevel serverLevel) {
                LootParams lootParams = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .create(LootContextParamSets.GIFT);
                LootTable lootTable = server.reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, BeachpartyIdentifier.identifier("gameplay/throwable_coconut")));
                lootTable.getRandomItems(lootParams).forEach(this::spawnAtLocation);
            }
            this.discard();
        }
    }

}
