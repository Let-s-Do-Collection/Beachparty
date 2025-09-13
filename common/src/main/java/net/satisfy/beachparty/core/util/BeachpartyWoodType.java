package net.satisfy.beachparty.core.util;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BeachpartyWoodType {
    public static final WoodType PALM = WoodType.register(new WoodType(BeachpartyIdentifier.identifier("palm").toString(), BlockSetType.OAK));
}