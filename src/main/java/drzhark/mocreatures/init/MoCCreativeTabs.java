package drzhark.mocreatures.init;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.item.ItemHorseGuide;
import drzhark.mocreatures.item.MoCItemEgg;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoCConstants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOC_TAB = CREATIVE_MODE_TABS.register("mocreatures_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mocreatures_tab"))
                    .icon(() -> MoCItems.AMULET_FAIRY_FULL.get() != null ? 
                           new ItemStack(MoCItems.AMULET_FAIRY_FULL.get()) : 
                           ItemStack.EMPTY)
                    .displayItems((params, output) -> {
                        // Add block items first
                        for (DeferredHolder<Block, ? extends Block> blockEntry : MoCBlocks.BLOCKS.getEntries()) {
                            try {
                                if (blockEntry.isBound()) {
                                    // Find the corresponding item for this block
                                    String blockPath = blockEntry.getId().getPath();
                                    MoCBlocks.ITEMS.getEntries().stream()
                                        .filter(itemObj -> itemObj.getId().getPath().equals(blockPath))
                                        .findFirst()
                                        .ifPresent(itemObj -> output.accept(itemObj.get()));
                                }
                            } catch (Exception e) {
                                // Skip this entry if there's an error
                            }
                        }
                        
                        // Add standalone items (not block items)
                        for (DeferredHolder<Item, ? extends Item> itemEntry : MoCItems.ITEMS.getEntries()) {
                            try {
                                if (itemEntry.isBound()) {
                                    Item item = itemEntry.get();

                                    // Special handling for the horse guide item to add the display stack
                                    if (item == MoCItems.HORSE_GUIDE.get()) {
                                        output.accept(ItemHorseGuide.createDisplayStack());
                                        continue; 
                                    }
                                    
                                    // Special handling for the egg item to add all variants
                                    if (item instanceof MoCItemEgg) {
                                        MoCItemEgg eggItem = (MoCItemEgg) item;
                                        eggItem.fillItemCategory(output);
                                    } else {
                                        output.accept(item);
                                    }
                                }
                            } catch (Exception e) {
                                // Skip this entry if there's an error
                            }
                        }
                    })
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
