package net.satisfy.beachparty.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.Beachparty;

public class BeachpartyIdentifier {

    public static ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(Beachparty.MOD_ID, path);
    }
}
