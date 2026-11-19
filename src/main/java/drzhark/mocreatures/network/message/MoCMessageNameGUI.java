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

public class MoCMessageNameGUI implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageNameGUI> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "name_g_u_i"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageNameGUI> STREAM_CODEC = StreamCodec.ofMember(MoCMessageNameGUI::encode, MoCMessageNameGUI::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageNameGUI> type() {
        return TYPE;
    }

    public int entityId;

    public MoCMessageNameGUI() {}

    public MoCMessageNameGUI(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageNameGUI(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageNameGUI message, IPayloadContext ctx) {
        // Registered client-bound only, so this runs on the client.
        ctx.enqueueWork(() -> MoCClientPacketHandlers.handleNameGUI(message));
    }


    @Override
    public String toString() {
        return "MoCMessageNameGUI - entityId: " + this.entityId;
    }
}
