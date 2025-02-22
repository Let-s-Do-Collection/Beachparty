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
    public static final RegistrySupplier<SoundEvent> OVER_THE_RAINBOW = create("over_the_rainbow");
    public static final RegistrySupplier<SoundEvent> BEACHPARTY = create("beachparty");
    public static final RegistrySupplier<SoundEvent> CARIBBEAN_BEACH = create("caribbean_beach");
    public static final RegistrySupplier<SoundEvent> PRIDELANDS = create("pridelands");
    public static final RegistrySupplier<SoundEvent> VOCALISTA = create("vocalista");
    public static final RegistrySupplier<SoundEvent> WILD_VEINS = create("wild_veins");

    public static final List<RegistrySupplier<SoundEvent>> RADIO_SOUNDS = List.of(BEACHPARTY, CARIBBEAN_BEACH, PRIDELANDS, VOCALISTA, WILD_VEINS, OVER_THE_RAINBOW);

    private static RegistrySupplier<SoundEvent> create(String name) {
        final ResourceLocation id = new BeachpartyIdentifier(name);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
    }
}
