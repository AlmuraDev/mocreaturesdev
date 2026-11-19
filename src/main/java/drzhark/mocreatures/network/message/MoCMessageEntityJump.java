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
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class MoCMessageEntityJump implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageEntityJump> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "entity_jump"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageEntityJump> STREAM_CODEC = StreamCodec.ofMember(MoCMessageEntityJump::encode, MoCMessageEntityJump::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageEntityJump> type() {
        return TYPE;
    }


    public MoCMessageEntityJump() {
    }

    public void encode(FriendlyByteBuf buffer) {
        // No data to encode
    }

    public MoCMessageEntityJump(FriendlyByteBuf buffer) {
        // No data to decode
    }

    public static void onMessage(MoCMessageEntityJump message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // This is a server-side message handler
            if (((ServerPlayer) ctx.player()) != null) {
                Entity vehicle = ((ServerPlayer) ctx.player()).getVehicle();
                if (vehicle instanceof IMoCEntity) {
                    ((IMoCEntity) vehicle).makeEntityJump();
                }
            }
        });
            }

    @Override
    public String toString() {
        return "MoCMessageEntityJump";
    }
}
