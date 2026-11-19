/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.event;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.compat.CompatScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MoCConstants.MOD_ID)
public class MoCEventHooksClient {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void displayCompatScreen(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen && CompatScreen.showScreen && ModList.get().isLoaded("customspawner")) {
            event.setNewScreen(new CompatScreen());
            CompatScreen.showScreen = false;
        }
    }
}
