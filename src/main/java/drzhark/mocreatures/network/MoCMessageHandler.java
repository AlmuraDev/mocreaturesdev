/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.network.message.*;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

@SuppressWarnings("removal")
public class MoCMessageHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void init() {
        int id = 0;
        
        // Register all messages in order with unique IDs
        // Note: These are all Server->Client packets that should be handled on the client side
        // Make sure each message class has a static onMessage method!
        INSTANCE.messageBuilder(MoCMessageAnimation.class, id++)
            .encoder(MoCMessageAnimation::encode)
            .decoder(MoCMessageAnimation::new)
            .consumerMainThread((message, context) -> MoCMessageAnimation.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageAppear.class, id++)
            .encoder(MoCMessageAppear::encode)
            .decoder(MoCMessageAppear::new)
            .consumerMainThread((message, context) -> MoCMessageAppear.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageAttachedEntity.class, id++)
            .encoder(MoCMessageAttachedEntity::encode)
            .decoder(MoCMessageAttachedEntity::new)
            .consumerMainThread((message, context) -> MoCMessageAttachedEntity.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageEntityDive.class, id++)
            .encoder(MoCMessageEntityDive::encode)
            .decoder(MoCMessageEntityDive::new)
            .consumerMainThread((message, context) -> MoCMessageEntityDive.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageEntityJump.class, id++)
            .encoder(MoCMessageEntityJump::encode)
            .decoder(MoCMessageEntityJump::new)
            .consumerMainThread((message, context) -> MoCMessageEntityJump.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageExplode.class, id++)
            .encoder(MoCMessageExplode::encode)
            .decoder(MoCMessageExplode::new)
            .consumerMainThread((message, context) -> MoCMessageExplode.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageHealth.class, id++)
            .encoder(MoCMessageHealth::encode)
            .decoder(MoCMessageHealth::new)
            .consumerMainThread((message, context) -> MoCMessageHealth.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageHeart.class, id++)
            .encoder(MoCMessageHeart::encode)
            .decoder(MoCMessageHeart::new)
            .consumerMainThread((message, context) -> MoCMessageHeart.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageInstaSpawn.class, id++)
            .encoder(MoCMessageInstaSpawn::encode)
            .decoder(MoCMessageInstaSpawn::new)
            .consumerMainThread((message, context) -> MoCMessageInstaSpawn.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageNameGUI.class, id++)
            .encoder(MoCMessageNameGUI::encode)
            .decoder(MoCMessageNameGUI::new)
            .consumerMainThread((message, context) -> MoCMessageNameGUI.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageUpdatePetName.class, id++)
            .encoder(MoCMessageUpdatePetName::encode)
            .decoder(MoCMessageUpdatePetName::new)
            .consumerMainThread((message, context) -> MoCMessageUpdatePetName.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageShuffle.class, id++)
            .encoder(MoCMessageShuffle::encode)
            .decoder(MoCMessageShuffle::new)
            .consumerMainThread((message, context) -> MoCMessageShuffle.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageTwoBytes.class, id++)
            .encoder(MoCMessageTwoBytes::encode)
            .decoder(MoCMessageTwoBytes::new)
            .consumerMainThread((message, context) -> MoCMessageTwoBytes.onMessage(message, context))
            .add();
            
        INSTANCE.messageBuilder(MoCMessageVanish.class, id++)
            .encoder(MoCMessageVanish::encode)
            .decoder(MoCMessageVanish::new)
            .consumerMainThread((message, context) -> MoCMessageVanish.onMessage(message, context))
            .add();
    }
}
