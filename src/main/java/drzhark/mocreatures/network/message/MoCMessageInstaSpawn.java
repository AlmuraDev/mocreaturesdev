/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageInstaSpawn {

    public int entityId;
    public int numberToSpawn;

    public MoCMessageInstaSpawn(int entityId, int numberToSpawn) {
        this.entityId = entityId;
        this.numberToSpawn = numberToSpawn;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.numberToSpawn);
    }

    public MoCMessageInstaSpawn(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.numberToSpawn = buffer.readInt();
    }

    public static void onMessage(MoCMessageInstaSpawn message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if ((MoCreatures.proxy.getProxyMode() == 1 && MoCreatures.proxy.allowInstaSpawn) || MoCreatures.proxy.getProxyMode() == 2) { // make sure the client has admin rights on server!
                MoCTools.spawnNearPlayer(ctx.get().getSender(), message.entityId, message.numberToSpawn);
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("Player " + ctx.get().getSender().getName() + " used MoC instaspawner and got "
                            + message.numberToSpawn + " creatures spawned");
                }
            } else {
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("Player " + ctx.get().getSender().getName()
                            + " tried to use MoC instaspawner, but the allowInstaSpawn setting is set to " + MoCreatures.proxy.allowInstaSpawn);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageInstaSpawn - entityId:%s, numberToSpawn:%s", this.entityId, this.numberToSpawn);
    }
}
