/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.bus.api.SubscribeEvent;
import com.mojang.blaze3d.platform.InputConstants;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCKeyHandler {

    public static KeyMapping diveBinding = new KeyMapping("key.mocreatures.dive", 
        KeyConflictContext.IN_GAME, 
        InputConstants.getKey(InputConstants.KEY_Z, -1), 
        "key.categories.mocreatures");

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(diveBinding);
    }
}
