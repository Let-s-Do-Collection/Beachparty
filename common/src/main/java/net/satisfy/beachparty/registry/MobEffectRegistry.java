package net.satisfy.beachparty.registry;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.effect.AquaFloatEffect;
import net.satisfy.beachparty.effect.NeverMeltEffect;
import net.satisfy.beachparty.effect.OceanWalkEffect;
import net.satisfy.beachparty.effect.TideRushEffect;
import net.satisfy.beachparty.util.BeachpartyIdentifier;

import java.util.function.Supplier;

public class MobEffectRegistry {
    public static final RegistrySupplier<MobEffect> NEVERMELT;
    public static final RegistrySupplier<MobEffect> TIDE_RUSH;
    public static final RegistrySupplier<MobEffect> AQUA_FLOAT;
    public static final RegistrySupplier<MobEffect> OCEAN_WALK;
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Beachparty.MOD_ID, Registries.MOB_EFFECT);
    private static final Registrar<MobEffect> MOB_EFFECTS_REGISTRAR = MOB_EFFECTS.getRegistrar();

    static {
        NEVERMELT = registerEffect("never_melt", NeverMeltEffect::new);
        TIDE_RUSH = registerEffect("tide_rush", TideRushEffect::new);
        AQUA_FLOAT = registerEffect("aqua_float", AquaFloatEffect::new);
        OCEAN_WALK = registerEffect("ocean_walk", OceanWalkEffect::new);
    }

    private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect) {
        if (Platform.isForge()) {
            return MOB_EFFECTS.register(name, effect);
        }
        return MOB_EFFECTS_REGISTRAR.register(new BeachpartyIdentifier(name), effect);
    }

    public static void init() {
        Beachparty.LOGGER.debug("Registering MobEffects for " + Beachparty.MOD_ID);
        MOB_EFFECTS.register();
    }
}
