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
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MoCMessageInstaSpawn implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MoCMessageInstaSpawn> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "insta_spawn"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageInstaSpawn> STREAM_CODEC = StreamCodec.ofMember(MoCMessageInstaSpawn::encode, MoCMessageInstaSpawn::new);

    @Override
    public CustomPacketPayload.Type<MoCMessageInstaSpawn> type() {
        return TYPE;
    }


    public int entityId;
    public int numberToSpawn;

    public MoCMessageInstaSpawn(int entityId, int numberToSpawn) {
        this.entityId = entityId;
        this.numberToSpawn = numberToSpawn;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.numberToSpawn);
    }

    public MoCMessageInstaSpawn(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.numberToSpawn = buffer.readInt();
    }

    public static void onMessage(MoCMessageInstaSpawn message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if ((MoCreatures.proxy.getProxyMode() == 1 && MoCreatures.proxy.allowInstaSpawn) || MoCreatures.proxy.getProxyMode() == 2) { // make sure the client has admin rights on server!
                MoCTools.spawnNearPlayer(((ServerPlayer) ctx.player()), message.entityId, message.numberToSpawn);
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("Player " + ((ServerPlayer) ctx.player()).getName() + " used MoC instaspawner and got "
                            + message.numberToSpawn + " creatures spawned");
                }
            } else {
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("Player " + ((ServerPlayer) ctx.player()).getName()
                            + " tried to use MoC instaspawner, but the allowInstaSpawn setting is set to " + MoCreatures.proxy.allowInstaSpawn);
                }
            }
        });
            }

    @Override
    public String toString() {
        return String.format("MoCMessageInstaSpawn - entityId:%s, numberToSpawn:%s", this.entityId, this.numberToSpawn);
    }
}
