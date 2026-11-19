/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCRecipes {

    @SubscribeEvent
    public static void registerFuels(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();

        if (item == MoCBlocks.wyvwoodSapling.get().asItem()) {
            event.setBurnTime(100); // 0.5 items
        } else if (item == MoCItems.FIRESTONECHUNK.get()) {
            event.setBurnTime(2400); // 12 items
        } else if (item == MoCItems.HEARTFIRE.get()) {
            event.setBurnTime(3200); // 16 items
        } else if (item == MoCItems.SHARKAXE.get() || item == MoCItems.SHARKSWORD.get()) {
            event.setBurnTime(200); // 1 item
        }
    }
}
