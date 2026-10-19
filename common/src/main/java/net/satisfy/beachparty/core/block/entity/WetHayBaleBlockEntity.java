package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.jetbrains.annotations.NotNull;

public class WetHayBaleBlockEntity extends BlockEntity {
    public static final TagKey<Biome> HOT_BIOME = TagKey.create(Registries.BIOME, BeachpartyIdentifier.identifier("hot_biome"));
    private static final int UNSET = -1;
    private int timer = UNSET;
    private boolean isProtected = false;
    private Boolean hot;

    public WetHayBaleBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.WET_HAY_BALE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(ServerLevel level, BlockPos pos, BlockState state, WetHayBaleBlockEntity be) {
        if (be.isProtected) {
            return;
        }
        if (level.dimensionType().ultraWarm()) {
            level.setBlock(pos, ObjectRegistry.THATCH.get().defaultBlockState(), 3);
            return;
        }
        if (!level.canSeeSky(pos.above()) || level.getFluidState(pos).is(net.minecraft.world.level.material.Fluids.WATER)) {
            return;
        }
        boolean hot = be.isHot(level);
        if (be.timer == UNSET) {
            be.timer = initialTimer(level.getRandom(), hot);
        }
        be.timer--;
        if (be.timer <= 0) {
            level.setBlock(pos, hot ? ObjectRegistry.THATCH.get().defaultBlockState() : net.minecraft.world.level.block.Blocks.HAY_BLOCK.defaultBlockState(), 3);
        }
    }

    private boolean isHot(Level level) {
        if (hot == null) {
            hot = level.getBiome(getBlockPos()).is(HOT_BIOME);
        }
        return hot;
    }

    public boolean isDrying() {
        return level != null && !isProtected && level.canSeeSky(worldPosition.above()) && !level.getFluidState(worldPosition).is(net.minecraft.world.level.material.Fluids.WATER);
    }

    public boolean isHot() {
        return level != null && isHot(level);
    }

    private static int initialTimer(RandomSource random, boolean hot) {
        return (hot ? 1200 : 600) + random.nextInt(601);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        timer = tag.contains("Timer") ? tag.getInt("Timer") : UNSET;
        isProtected = tag.getBoolean("Protected");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("Timer", timer);
        tag.putBoolean("Protected", isProtected);
    }

    public void preventDrying() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CLAY.defaultBlockState()),
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.0, getBlockPos().getZ() + 0.5,
                    10, 0.2, 0.2, 0.2, 0.1);
            serverLevel.playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        isProtected = true;
        sync();
    }

    public void removeProtection() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.0, getBlockPos().getZ() + 0.5,
                    10, 0.2, 0.2, 0.2, 0.1);
            serverLevel.playSound(null, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        isProtected = false;
        timer = UNSET;
        sync();
    }

    public boolean isProtected() {
        return isProtected;
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }
}
