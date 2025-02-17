package net.satisfy.beachparty.core.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.Beachparty;

public class BeachpartyWoodType {
    public static final WoodType PALM = WoodType.register(new WoodType(new ResourceLocation(Beachparty.MOD_ID, "palm").toString(), BlockSetType.OAK));
}