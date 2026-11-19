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
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageHeart implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageHeart> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "heart"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageHeart> STREAM_CODEC = StreamCodec.ofMember(MoCMessageHeart::encode, MoCMessageHeart::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageHeart> type() {
        return TYPE;
    }


    public int entityId;

    public MoCMessageHeart() {
    }

    public MoCMessageHeart(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageHeart(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageHeart message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleHeart(message));
            }

    @Override
    public String toString() {
        return String.format("MoCMessageHeart - entityId:%s", this.entityId);
    }
}
