package net.satisfy.beachparty.core.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.beachparty.core.networking.packet.TuneRadioS2CPacket;
import net.satisfy.beachparty.core.networking.packet.TurnRadioS2CPacket;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;

public class BeachpartyMessages {
    public static final ResourceLocation TURN_RADIO_S2C = new BeachpartyIdentifier("turn_radio");
    public static final ResourceLocation TUNE_RADIO_S2C = new BeachpartyIdentifier("tune_radio");

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TURN_RADIO_S2C, new TurnRadioS2CPacket());
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TUNE_RADIO_S2C, new TuneRadioS2CPacket());
    }
}
