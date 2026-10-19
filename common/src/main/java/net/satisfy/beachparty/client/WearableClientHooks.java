package net.satisfy.beachparty.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class WearableClientHooks {
    public static boolean isJumping(Player player) {
        return player instanceof LocalPlayer && Minecraft.getInstance().options.keyJump.isDown();
    }
}
