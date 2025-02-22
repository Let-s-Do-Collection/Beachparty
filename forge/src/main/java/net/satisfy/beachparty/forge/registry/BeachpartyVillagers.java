package net.satisfy.beachparty.forge.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

public class BeachpartyVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Beachparty.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Beachparty.MOD_ID);

    public static final RegistryObject<PoiType> SANDYMERCHANT_POI = POI_TYPES.register("sandymerchant_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.PALM_BAR.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> SANDYMERCHANT = VILLAGER_PROFESSIONS.register("sandymerchant", () ->
            new VillagerProfession("sandymerchant", x -> x.get() == SANDYMERCHANT_POI.get(), x -> x.get() == SANDYMERCHANT_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
