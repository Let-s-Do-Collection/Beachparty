package net.satisfy.beachparty.neoforge.core.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.PoiTypeKeys;

public class BeachpartyVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Beachparty.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, Beachparty.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> SANDYMERCHANT_POI = POI_TYPES.register("sandymerchant_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.PALM_BAR.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final DeferredHolder<PoiType, PoiType> SANDCASTLE_POI = POI_TYPES.register(PoiTypeKeys.SANDCASTLE.location().getPath(), () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.SANDCASTLE.get().getStateDefinition().getPossibleStates()), 0, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> SANDYMERCHANT = VILLAGER_PROFESSIONS.register("sandymerchant", () ->
            new VillagerProfession("sandymerchant", x -> x.value() == SANDYMERCHANT_POI.get(), x -> x.value() == SANDYMERCHANT_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
