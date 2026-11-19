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
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageTwoBytes implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageTwoBytes> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "two_bytes"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageTwoBytes> STREAM_CODEC = StreamCodec.ofMember(MoCMessageTwoBytes::encode, MoCMessageTwoBytes::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageTwoBytes> type() {
        return TYPE;
    }


    public int entityId;
    public byte slot;
    public byte value;

    public MoCMessageTwoBytes() {
    }

    public MoCMessageTwoBytes(int entityId, byte slot, byte value) {
        this.entityId = entityId;
        this.slot = slot;
        this.value = value;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeByte(this.slot);
        buffer.writeByte(this.value);
    }

    public MoCMessageTwoBytes(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.slot = buffer.readByte();
        this.value = buffer.readByte();
    }

    public static void onMessage(MoCMessageTwoBytes message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleTwoBytes(message));
            }

    @Override
    public String toString() {
        return String.format("MoCMessageTwoBytes - entityId:%s, slot:%s, value:%s", this.entityId, this.slot, this.value);
    }
}
