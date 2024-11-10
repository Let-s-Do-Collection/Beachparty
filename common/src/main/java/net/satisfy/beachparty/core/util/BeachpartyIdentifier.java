package net.satisfy.beachparty.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;

public class BeachpartyIdentifier extends ResourceLocation {
    public BeachpartyIdentifier(String path) {
        super(Beachparty.MOD_ID, path);
    }
}
