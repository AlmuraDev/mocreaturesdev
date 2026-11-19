/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/** Client-only: sends Mo' Creatures packets to the server. Only screens, key handlers and other client code call this. */
public final class MoCClientPackets {

    private MoCClientPackets() {
    }

    public static void sendToServer(CustomPacketPayload message) {
        ClientPacketDistributor.sendToServer(message);
    }
}
