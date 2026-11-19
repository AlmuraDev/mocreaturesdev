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
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageShuffle implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageShuffle> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "shuffle"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageShuffle> STREAM_CODEC = StreamCodec.ofMember(MoCMessageShuffle::encode, MoCMessageShuffle::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageShuffle> type() {
        return TYPE;
    }


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

    public static void onMessage(MoCMessageShuffle message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleShuffle(message));
            }

    @Override
    public String toString() {
        return String.format("MoCMessageShuffle - entityId:%s, flag:%s", this.entityId, this.flag);
    }
}
