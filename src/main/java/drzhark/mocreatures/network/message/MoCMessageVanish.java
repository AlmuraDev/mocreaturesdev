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

public class MoCMessageVanish implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageVanish> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "vanish"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageVanish> STREAM_CODEC = StreamCodec.ofMember(MoCMessageVanish::encode, MoCMessageVanish::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageVanish> type() {
        return TYPE;
    }


    public int entityId;

    public MoCMessageVanish() {
    }

    public MoCMessageVanish(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageVanish(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageVanish message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                ((MoCEntityHorse) ent).setVanishC((byte) 1);
            }
        });
            }

    @Override
    public String toString() {
        return String.format("MoCMessageVanish - entityId:%s", this.entityId);
    }
}
