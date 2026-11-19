/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageAttachedEntity implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageAttachedEntity> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "attached_entity"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageAttachedEntity> STREAM_CODEC = StreamCodec.ofMember(MoCMessageAttachedEntity::encode, MoCMessageAttachedEntity::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageAttachedEntity> type() {
        return TYPE;
    }


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

    public static void onMessage(MoCMessageAttachedEntity message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity sourceEntity = minecraft.level.getEntity(message.sourceEntityId);
                Entity targetEntity = minecraft.level.getEntity(message.targetEntityId);

                if (sourceEntity != null && targetEntity != null) {
                    sourceEntity.startRiding(targetEntity);
                }
            }
        });
            }

    @Override
    public String toString() {
        return String.format("MoCMessageAttachedEntity - sourceEntityId:%s, targetEntityId:%s", this.sourceEntityId, this.targetEntityId);
    }
}
