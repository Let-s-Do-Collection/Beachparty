package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.List;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Beachparty.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> RADIO_CLICK = soundEvent("radio_click");
    public static final RegistrySupplier<SoundEvent> RADIO_TUNE = soundEvent("radio_tune");

    public static final RegistrySupplier<SoundEvent> OVER_THE_RAINBOW_SE = soundEvent("over_the_rainbow");
    public static final RegistrySupplier<SoundEvent> BEACHPARTY_SE = soundEvent("beachparty");
    public static final RegistrySupplier<SoundEvent> CARIBBEAN_BEACH_SE = soundEvent("caribbean_beach");
    public static final RegistrySupplier<SoundEvent> PRIDELANDS_SE = soundEvent("pridelands");
    public static final RegistrySupplier<SoundEvent> VOCALISTA_SE = soundEvent("vocalista");
    public static final RegistrySupplier<SoundEvent> WILD_VEINS_SE = soundEvent("wild_veins");

    public static final ResourceKey<JukeboxSong> OVER_THE_RAINBOW = song("over_the_rainbow");
    public static final ResourceKey<JukeboxSong> BEACHPARTY = song("beachparty");
    public static final ResourceKey<JukeboxSong> CARIBBEAN_BEACH = song("caribbean_beach");
    public static final ResourceKey<JukeboxSong> PRIDELANDS = song("pridelands");
    public static final ResourceKey<JukeboxSong> VOCALISTA = song("vocalista");
    public static final ResourceKey<JukeboxSong> WILD_VEINS = song("wild_veins");

    public static final List<ResourceKey<JukeboxSong>> RADIO_SONGS =
            List.of(BEACHPARTY, CARIBBEAN_BEACH, PRIDELANDS, VOCALISTA, WILD_VEINS, OVER_THE_RAINBOW);

    private static RegistrySupplier<SoundEvent> soundEvent(String id) {
        ResourceLocation rl = BeachpartyIdentifier.identifier(id);
        return SOUND_EVENTS.register(rl, () -> SoundEvent.createVariableRangeEvent(rl));
    }

    private static ResourceKey<JukeboxSong> song(String id) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, BeachpartyIdentifier.identifier(id));
    }

    public static void init() {
        SOUND_EVENTS.register();
    }
}
