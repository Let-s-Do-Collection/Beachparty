package net.satisfy.beachparty.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.satisfy.beachparty.core.registry.SoundEventRegistry;

import java.util.List;

public class RadioBlockEntity extends BlockEntity {
    private static final List<ResourceKey<JukeboxSong>> TRACKS = SoundEventRegistry.RADIO_SONGS;

    private int currentIndex = -1;
    private int ticks;
    private long tickCount;
    private long startTick;
    public boolean isPlaying;
    private boolean syncedWithClient = false;

    public RadioBlockEntity(BlockPos pos, BlockState state) {
        super(net.satisfy.beachparty.core.registry.EntityTypeRegistry.RADIO_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, RadioBlockEntity entity) {
        if (!entity.isPlaying || !(level instanceof ServerLevel server)) return;

        if (!entity.syncedWithClient) {
            entity.resendSoundToClient(server);
            entity.syncedWithClient = true;
        }

        entity.ticks++;
        entity.tickCount++;

        if (server.getGameTime() >= entity.startTick + 20 * 60) {
            entity.stop();
            return;
        }

        if (level.getRandom().nextFloat() < 0.05f) {
            double baseX = pos.getX() + 0.5;
            double baseY = pos.getY() + 1.0;
            double baseZ = pos.getZ() + 0.5;

            double offsetX = (level.getRandom().nextDouble() - 0.5) * 0.6;
            double offsetZ = (level.getRandom().nextDouble() - 0.5) * 0.6;
            float color = level.getRandom().nextFloat();

            server.sendParticles(ParticleTypes.NOTE, baseX + offsetX, baseY, baseZ + offsetZ, 0, color, 0.0, 0.0, 1.0);
        }
    }

    public boolean toggleOrNext() {
        if (!isPlaying) {
            start();
        } else {
            next(level);
        }
        return true;
    }

    public void start() {
        currentIndex = 0;
        play(level);
    }

    public void next(Level level) {
        currentIndex = (currentIndex + 1) % TRACKS.size();
        play(level);
    }

    public void stop() {
        if (!(level instanceof ServerLevel server)) return;
        server.levelEvent(1011, worldPosition, 0);
        server.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, worldPosition, Context.of(getBlockState()));
        isPlaying = false;
        ticks = 0;
        tickCount = 0;
        syncedWithClient = false;
    }

    private void play(Level level) {
        stop();
        if (!(level instanceof ServerLevel server)) return;
        if (currentIndex < 0 || currentIndex >= TRACKS.size()) return;

        isPlaying = true;
        ticks = 0;
        startTick = server.getGameTime();

        server.levelEvent(null, 1010, worldPosition, getJukeboxSongId(server));
        server.gameEvent(GameEvent.JUKEBOX_PLAY, worldPosition, Context.of(getBlockState()));
        syncedWithClient = true;
    }

    private void resendSoundToClient(ServerLevel server) {
        server.levelEvent(null, 1010, worldPosition, getJukeboxSongId(server));
    }

    private int getJukeboxSongId(ServerLevel server) {
        Registry<JukeboxSong> registry = server.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG);
        return registry.getId(registry.get(TRACKS.get(currentIndex)));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.currentIndex = compoundTag.getInt("CurrentIndex");
        this.ticks = compoundTag.getInt("Ticks");
        this.tickCount = compoundTag.getLong("TickCount");
        this.startTick = compoundTag.getLong("StartTick");
        this.isPlaying = compoundTag.getBoolean("IsPlaying");
        this.syncedWithClient = false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("CurrentIndex", currentIndex);
        tag.putInt("Ticks", ticks);
        tag.putLong("TickCount", tickCount);
        tag.putLong("StartTick", startTick);
        tag.putBoolean("IsPlaying", isPlaying);
    }
}
