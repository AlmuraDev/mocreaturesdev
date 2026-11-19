/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;
import drzhark.mocreatures.client.MoCClientPacketHandlers;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageAnimation implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageAnimation> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "animation"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageAnimation> STREAM_CODEC = StreamCodec.ofMember(MoCMessageAnimation::encode, MoCMessageAnimation::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageAnimation> type() {
        return TYPE;
    }


    public int entityId;
    public int animationType;

    public MoCMessageAnimation(int entityId, int animationType) {
        this.entityId = entityId;
        this.animationType = animationType;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.animationType);
    }

    public MoCMessageAnimation(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.animationType = buffer.readInt();
    }

    public static void onMessage(MoCMessageAnimation message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleAnimation(message));
            }

    @Override
    public String toString() {
        return String.format("MoCMessageAnimation - entityId:%s, animationType:%s", this.entityId, this.animationType);
    }
}
