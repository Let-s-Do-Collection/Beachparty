package net.satisfy.beachparty.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.beachparty.Beachparty;

@Config(name = Beachparty.MOD_ID)
@Config.Gui.Background("beachparty:textures/block/sandwaves.png")
public class ConfigFabric implements ConfigData {

    @ConfigEntry.Category("worldgen")
    public boolean spawnPalms = true;
    @ConfigEntry.Category("worldgen")
    public boolean spawnSeashells = true;
    @ConfigEntry.Category("worldgen")
    public boolean spawnSandwaves = true;
    @ConfigEntry.Category("worldgen")
    public boolean spawnMessageInABottle = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip
    public boolean destroySandcastle = true;


}
