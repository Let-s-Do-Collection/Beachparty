package net.satisfy.beachparty.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.foundation.client.gui.LetsDoConfigScreen;

@Mod(value = Beachparty.MOD_ID, dist = Dist.CLIENT)
public class BeachpartyNeoForgeClientMod {
    public BeachpartyNeoForgeClientMod(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> new LetsDoConfigScreen(parent, Beachparty.MOD_ID, screen -> new ConfigurationScreen(modContainer, screen)));
    }
}
