/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageEntityJump {

    public MoCMessageEntityJump() {
    }

    public void encode(FriendlyByteBuf buffer) {
        // No data to encode
    }

    public MoCMessageEntityJump(FriendlyByteBuf buffer) {
        // No data to decode
    }

    public static void onMessage(MoCMessageEntityJump message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // This is a server-side message handler
            if (ctx.get().getSender() != null) {
                Entity vehicle = ctx.get().getSender().getVehicle();
                if (vehicle instanceof IMoCEntity) {
                    ((IMoCEntity) vehicle).makeEntityJump();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return "MoCMessageEntityJump";
    }
}
