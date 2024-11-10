package net.satisfy.beachparty.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

import java.util.List;

public class SoundEventRegistry {

    public static Registrar<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Beachparty.MOD_ID, Registries.SOUND_EVENT).getRegistrar();

    public static final RegistrySupplier<SoundEvent> RADIO_CLICK = create("radio_click");
    public static final RegistrySupplier<SoundEvent> RADIO_TUNE = create("radio_tune");
    public static final RegistrySupplier<SoundEvent> RADIO_REGGEA = create("radio_reggea");
    public static final RegistrySupplier<SoundEvent> RADIO_HAWAII = create("radio_hawaii");
    public static final RegistrySupplier<SoundEvent> RADIO_TROPICAL = create("radio_tropical");
    public static final RegistrySupplier<SoundEvent> RADIO_BEACHPARTY = create("radio_beachparty");

    public static final List<RegistrySupplier<SoundEvent>> RADIO_SOUNDS = List.of(RADIO_REGGEA, RADIO_HAWAII, RADIO_TROPICAL, RADIO_BEACHPARTY);

    private static RegistrySupplier<SoundEvent> create(String name) {
        final ResourceLocation id = new BeachpartyIdentifier(name);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
        Beachparty.LOGGER.debug("Register " + SoundEventRegistry.class);
    }
}
