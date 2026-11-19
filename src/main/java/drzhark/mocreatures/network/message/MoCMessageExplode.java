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
import drzhark.mocreatures.entity.hostile.MoCEntityOgre;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageExplode implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageExplode> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "explode"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageExplode> STREAM_CODEC = StreamCodec.ofMember(MoCMessageExplode::encode, MoCMessageExplode::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageExplode> type() {
        return TYPE;
    }


    public int entityId;

    public MoCMessageExplode() {
    }

    public MoCMessageExplode(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageExplode(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageExplode message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleExplode(message));
            }

    @Override
    public String toString() {
        return String.format("MoCMessageExplode - entityId:%s", this.entityId);
    }
}
