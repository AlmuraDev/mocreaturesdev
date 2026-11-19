package drzhark.mocreatures.client;

import drzhark.mocreatures.event.MoCEventHooksClient;
import net.neoforged.neoforge.common.NeoForge;

public class ClientOnlyEventRegistrar {
    public static void registerClientEvents() {
        NeoForge.EVENT_BUS.register(new MoCEventHooksClient());
        NeoForge.EVENT_BUS.register(new MoCKeyHandler());
    }
}