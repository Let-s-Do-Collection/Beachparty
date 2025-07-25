package net.satisfy.beachparty.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class JukeboxSongRegistry {

    public static final ResourceKey<JukeboxSong> OVER_THE_RAINBOW = create("over_the_rainbow");
    public static final ResourceKey<JukeboxSong> BEACHPARTY = create("beachparty");
    public static final ResourceKey<JukeboxSong> CARIBBEAN_BEACH = create("caribbean_beach");
    public static final ResourceKey<JukeboxSong> PRIDELANDS = create("pridelands");
    public static final ResourceKey<JukeboxSong> VOCALISTA = create("vocalista");
    public static final ResourceKey<JukeboxSong> WILD_VEINS = create("wild_veins");

    private static ResourceKey<JukeboxSong> create(String string) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, BeachpartyIdentifier.identifier(string));
    }

}