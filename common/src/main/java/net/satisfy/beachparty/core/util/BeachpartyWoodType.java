package net.satisfy.beachparty.core.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.foundation.wood.BoatWood;

public class BeachpartyWoodType {
    public static final ResourceLocation PALM_ID = BeachpartyIdentifier.identifier("palm");
    public static final ResourceLocation FLOATY_ID = BeachpartyIdentifier.identifier("floaty");
    public static final WoodType PALM = WoodType.register(new WoodType(PALM_ID.toString(), BlockSetType.OAK));
    public static final BoatWood PALM_BOAT = BoatWood.register(PALM_ID, () -> ObjectRegistry.PALM_BOAT.get(), () -> ObjectRegistry.PALM_CHEST_BOAT.get());
    public static final BoatWood FLOATY_BOAT = BoatWood.register(FLOATY_ID, () -> ObjectRegistry.FLOATY_BOAT.get(), () -> ObjectRegistry.FLOATY_CHEST_BOAT.get());
}
