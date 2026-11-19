/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageVanish {

    public int entityId;

    public MoCMessageVanish() {
    }

    public MoCMessageVanish(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageVanish(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageVanish message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                ((MoCEntityHorse) ent).setVanishC((byte) 1);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageVanish - entityId:%s", this.entityId);
    }
}
