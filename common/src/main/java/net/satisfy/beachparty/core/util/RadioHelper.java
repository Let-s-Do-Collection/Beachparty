package net.satisfy.beachparty.core.util;

import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.satisfy.beachparty.core.registry.SoundEventRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class RadioHelper {

    private static final Map<BlockPos, List<SimpleSoundInstance>> soundInstances = new HashMap<>();

    public static void setPlaying(BlockPos pos, int channel, boolean play, int delay) {
        if (play) {
            playSound(pos, channel, delay);
        } else {
            stopSounds(pos);
        }
    }

    public static void tune(BlockPos pos, int channel) {
        Minecraft.getInstance().getSoundManager().play(
                new SimpleSoundInstance(SoundEventRegistry.RADIO_TUNE.get(), SoundSource.RECORDS, 1.0f, 1.0f, RandomSource.create(), pos)
        );
        stopSounds(pos);
        if (!soundInstances.containsKey(pos)) {
            addSounds(pos);
        }
        playSound(pos, channel, 2 * 20);
    }

    private static void playSound(BlockPos pos, int channel, int delay) {
        stopSounds(pos);

        if (!soundInstances.containsKey(pos)) {
            addSounds(pos);
        }

        SimpleSoundInstance soundInstance = getSound(pos, channel);
        if (soundInstance != null) {
            Minecraft.getInstance().getSoundManager().playDelayed(soundInstance, delay);
        }
    }

    private static void stopSounds(BlockPos pos) {
        List<SimpleSoundInstance> sounds = soundInstances.remove(pos);
        if (sounds != null) {
            for (SimpleSoundInstance sound : sounds) {
                Minecraft.getInstance().getSoundManager().stop(sound);
            }
        }
    }

    private static void addSounds(BlockPos blockPos) {
        List<SimpleSoundInstance> soundList = new ArrayList<>();
        for (RegistrySupplier<SoundEvent> sound : SoundEventRegistry.RADIO_SOUNDS) {
            soundList.add(new SimpleSoundInstance(sound.get().getLocation(), SoundSource.RECORDS, 0.6f, 1.0f, RandomSource.create(), true, 0, SoundInstance.Attenuation.LINEAR, blockPos.getX(), blockPos.getY(), blockPos.getZ(), false
            ));
        }
        soundInstances.put(blockPos, soundList);
    }

    public static SimpleSoundInstance getSound(BlockPos pos, int channel) {
        List<SimpleSoundInstance> list = soundInstances.get(pos);
        if (list == null || list.isEmpty()) return null;
        if (channel < 0 || channel >= list.size()) return null;
        return list.get(channel);
    }
}
