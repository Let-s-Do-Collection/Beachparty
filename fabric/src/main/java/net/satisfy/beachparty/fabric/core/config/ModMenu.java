package net.satisfy.beachparty.fabric.core.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.foundation.client.gui.LetsDoConfigScreen;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new LetsDoConfigScreen(parent, Beachparty.MOD_ID, screen -> AutoConfig.getConfigScreen(BeachpartyFabricConfig.class, screen).get());
    }
}
