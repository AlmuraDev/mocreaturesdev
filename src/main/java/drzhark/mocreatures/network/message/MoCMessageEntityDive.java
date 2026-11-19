/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageEntityDive {

    public MoCMessageEntityDive() {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public MoCMessageEntityDive(FriendlyByteBuf buffer) {
    }

    public static void onMessage(MoCMessageEntityDive message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null && ctx.get().getSender().getVehicle() instanceof IMoCEntity) {
                ((IMoCEntity) ctx.get().getSender().getVehicle()).makeEntityDive();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
