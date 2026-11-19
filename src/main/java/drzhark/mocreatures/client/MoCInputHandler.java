/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import drzhark.mocreatures.client.MoCClientPackets;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageEntityDive;
import drzhark.mocreatures.network.message.MoCMessageEntityJump;
import drzhark.mocreatures.proxy.MoCProxyClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCInputHandler {

    @SubscribeEvent
    public static void onInput(ClientTickEvent.Post event) {
        Player player = MoCProxyClient.mc().player;
        if (player == null) return;

        boolean kbJump = MoCProxyClient.mc().options.keyJump.isDown();
        boolean kbDive = MoCKeyHandler.diveBinding.isDown();

        if (kbJump && player.getVehicle() != null && player.getVehicle() instanceof IMoCEntity) {
            // jump code needs to be executed client/server simultaneously to take
            ((IMoCEntity) player.getVehicle()).makeEntityJump();
            MoCClientPackets.sendToServer(new MoCMessageEntityJump());
        }

        if (kbDive && player.getVehicle() != null && player.getVehicle() instanceof IMoCEntity) {
            // dive code needs to be executed client/server simultaneously to take
            ((IMoCEntity) player.getVehicle()).makeEntityDive();
            MoCClientPackets.sendToServer(new MoCMessageEntityDive());
        }
    }
} 