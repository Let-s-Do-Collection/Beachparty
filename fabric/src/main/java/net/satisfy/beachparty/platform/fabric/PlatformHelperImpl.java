package net.satisfy.beachparty.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.fabric.config.ConfigFabric;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class PlatformHelperImpl {
    static ConfigFabric config = AutoConfig.getConfigHolder(ConfigFabric.class).getConfig();
    public static void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (config.worldgen.destroySandcastle) {
            world.setBlock(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState(), 3);
        }
    }


}
