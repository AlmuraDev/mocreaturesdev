/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MoCMessageAttachedEntity {

    public int sourceEntityId;
    public int targetEntityId;

    public MoCMessageAttachedEntity(int sourceEntityId, int targetEntityId) {
        this.sourceEntityId = sourceEntityId;
        this.targetEntityId = targetEntityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.sourceEntityId);
        buffer.writeInt(this.targetEntityId);
    }

    public MoCMessageAttachedEntity(FriendlyByteBuf buffer) {
        this.sourceEntityId = buffer.readInt();
        this.targetEntityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageAttachedEntity message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity sourceEntity = minecraft.level.getEntity(message.sourceEntityId);
                Entity targetEntity = minecraft.level.getEntity(message.targetEntityId);

                if (sourceEntity != null && targetEntity != null) {
                    sourceEntity.startRiding(targetEntity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageAttachedEntity - sourceEntityId:%s, targetEntityId:%s", this.sourceEntityId, this.targetEntityId);
    }
}
