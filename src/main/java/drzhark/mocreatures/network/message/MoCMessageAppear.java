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

public class MoCMessageAppear {

    public int entityId;

    public MoCMessageAppear(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageAppear(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageAppear message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof MoCEntityHorse) {
                    ((MoCEntityHorse) entity).MaterializeFX();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageAppear - entityId:%s", this.entityId);
    }
}
