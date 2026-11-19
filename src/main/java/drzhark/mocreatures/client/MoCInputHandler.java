/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client;

import drzhark.mocreatures.client.MoCClientPackets;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageEntityDive;
import drzhark.mocreatures.network.message.MoCMessageEntityJump;
import drzhark.mocreatures.proxy.MoCProxyClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCInputHandler {

    @SubscribeEvent
    public static void onInput(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        boolean kbJump = MoCProxyClient.mc.options.keyJump.isDown();
        boolean kbDive = MoCKeyHandler.diveBinding.isDown();

        if (kbJump && e.player.getVehicle() != null && e.player.getVehicle() instanceof IMoCEntity) {
            // jump code needs to be executed client/server simultaneously to take
            ((IMoCEntity) e.player.getVehicle()).makeEntityJump();
            MoCClientPackets.sendToServer(new MoCMessageEntityJump());
        }

        if (kbDive && e.player.getVehicle() != null && e.player.getVehicle() instanceof IMoCEntity) {
            // dive code needs to be executed client/server simultaneously to take
            ((IMoCEntity) e.player.getVehicle()).makeEntityDive();
            MoCClientPackets.sendToServer(new MoCMessageEntityDive());
        }
    }
} 