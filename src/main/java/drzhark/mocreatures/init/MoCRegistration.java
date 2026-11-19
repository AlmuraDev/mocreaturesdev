package drzhark.mocreatures.init;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Handles registration that should happen after the registries are built
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCRegistration {
    
    /**
     * Register creative tabs
     */
    @SubscribeEvent
    public static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            // Add spawn eggs to creative tab when they're registered
            MoCSpawnEggs.SPAWN_EGGS.getEntries().forEach(egg -> {
                event.accept(egg.get());
            });
        }
    }
    
    /**
     * Register client-specific setup
     */
    public static void clientSetup() {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            // Client-specific setup that needs to happen after registration
            MoCreatures.LOGGER.info("Mo'Creatures client setup complete");
        }
    }
} 