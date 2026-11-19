/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.network.message.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers the Mo' Creatures packets and sends them from the server.
 * Sending to the server lives in {@link drzhark.mocreatures.client.MoCClientPackets}, which only the client loads.
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public final class MoCMessageHandler {

    private static final String PROTOCOL_VERSION = "1";

    private MoCMessageHandler() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        // Server to client
        registrar.playToClient(MoCMessageAnimation.TYPE, MoCMessageAnimation.STREAM_CODEC, MoCMessageAnimation::onMessage);
        registrar.playToClient(MoCMessageAppear.TYPE, MoCMessageAppear.STREAM_CODEC, MoCMessageAppear::onMessage);
        registrar.playToClient(MoCMessageAttachedEntity.TYPE, MoCMessageAttachedEntity.STREAM_CODEC, MoCMessageAttachedEntity::onMessage);
        registrar.playToClient(MoCMessageExplode.TYPE, MoCMessageExplode.STREAM_CODEC, MoCMessageExplode::onMessage);
        registrar.playToClient(MoCMessageHealth.TYPE, MoCMessageHealth.STREAM_CODEC, MoCMessageHealth::onMessage);
        registrar.playToClient(MoCMessageHeart.TYPE, MoCMessageHeart.STREAM_CODEC, MoCMessageHeart::onMessage);
        registrar.playToClient(MoCMessageNameGUI.TYPE, MoCMessageNameGUI.STREAM_CODEC, MoCMessageNameGUI::onMessage);
        registrar.playToClient(MoCMessageShuffle.TYPE, MoCMessageShuffle.STREAM_CODEC, MoCMessageShuffle::onMessage);
        registrar.playToClient(MoCMessageTwoBytes.TYPE, MoCMessageTwoBytes.STREAM_CODEC, MoCMessageTwoBytes::onMessage);
        registrar.playToClient(MoCMessageVanish.TYPE, MoCMessageVanish.STREAM_CODEC, MoCMessageVanish::onMessage);

        // Client to server
        registrar.playToServer(MoCMessageEntityDive.TYPE, MoCMessageEntityDive.STREAM_CODEC, MoCMessageEntityDive::onMessage);
        registrar.playToServer(MoCMessageEntityJump.TYPE, MoCMessageEntityJump.STREAM_CODEC, MoCMessageEntityJump::onMessage);
        registrar.playToServer(MoCMessageInstaSpawn.TYPE, MoCMessageInstaSpawn.STREAM_CODEC, MoCMessageInstaSpawn::onMessage);
        registrar.playToServer(MoCMessageUpdatePetName.TYPE, MoCMessageUpdatePetName.STREAM_CODEC, MoCMessageUpdatePetName::onMessage);
    }

    /** Sends to every player within {@code radius} of the point. Does nothing on the client. */
    public static void sendToNear(Level level, double x, double y, double z, double radius, CustomPacketPayload message) {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, radius, message);
        }
    }

    /** Sends to every player tracking the entity. Does nothing on the client. */
    public static void sendToTracking(Entity entity, CustomPacketPayload message) {
        if (!entity.level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntity(entity, message);
        }
    }

    /** Sends to one player. Does nothing unless the player is on the server. */
    public static void sendToPlayer(Player player, CustomPacketPayload message) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, message);
        }
    }

    public static void sendToAll(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
