/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageAppear implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageAppear> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "appear"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageAppear> STREAM_CODEC = StreamCodec.ofMember(MoCMessageAppear::encode, MoCMessageAppear::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageAppear> type() {
        return TYPE;
    }


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

    public static void onMessage(MoCMessageAppear message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof MoCEntityHorse) {
                    ((MoCEntityHorse) entity).MaterializeFX();
                }
            }
        });
            }

    @Override
    public String toString() {
        return String.format("MoCMessageAppear - entityId:%s", this.entityId);
    }
}
