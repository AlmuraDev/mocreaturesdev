/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MoCMessageEntityDive implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageEntityDive> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "entity_dive"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageEntityDive> STREAM_CODEC = StreamCodec.ofMember(MoCMessageEntityDive::encode, MoCMessageEntityDive::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageEntityDive> type() {
        return TYPE;
    }


    public MoCMessageEntityDive() {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public MoCMessageEntityDive(FriendlyByteBuf buffer) {
    }

    public static void onMessage(MoCMessageEntityDive message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (((ServerPlayer) ctx.player()) != null && ((ServerPlayer) ctx.player()).getVehicle() instanceof IMoCEntity) {
                ((IMoCEntity) ((ServerPlayer) ctx.player()).getVehicle()).makeEntityDive();
            }
        });
            }
}
