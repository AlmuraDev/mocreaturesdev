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

public class MoCMessageShuffle {

    public int entityId;
    public boolean flag;

    public MoCMessageShuffle() {
    }

    public MoCMessageShuffle(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.flag);
    }

    public MoCMessageShuffle(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.flag = buffer.readBoolean();
    }

    public static void onMessage(MoCMessageShuffle message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                if (message.flag) {
                    //((MoCEntityHorse) ent).shuffle();
                } else {
                    ((MoCEntityHorse) ent).shuffleCounter = 0;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString() {
        return String.format("MoCMessageShuffle - entityId:%s, flag:%s", this.entityId, this.flag);
    }
}
