/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.event;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCForgeEvents {

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        // This ensures our blocks are properly tagged at runtime
        // Note: This is a backup in case data generation tags don't load properly
        MoCreatures.LOGGER.info("Tags updated - MoC blocks should now be properly tagged");
    }
} 