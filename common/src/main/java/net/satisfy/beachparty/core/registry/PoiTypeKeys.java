package net.satisfy.beachparty.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class PoiTypeKeys {
    public static final ResourceKey<PoiType> SANDCASTLE = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, BeachpartyIdentifier.identifier("sandcastle"));
}
