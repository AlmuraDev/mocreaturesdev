/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.worldgen.feature.FirestoneClusterFeature;
import drzhark.mocreatures.worldgen.feature.WyvernNestFeature;
import drzhark.mocreatures.worldgen.structure.WyvernIslandFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MoCConstants.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> FIRESTONE_CLUSTER = 
            FEATURES.register("firestone_cluster", 
                    () -> new FirestoneClusterFeature(NoneFeatureConfiguration.CODEC));
    
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> WYVERN_ISLAND = 
            FEATURES.register("wyvern_island", 
                    () -> new WyvernIslandFeature(NoneFeatureConfiguration.CODEC));
    
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> WYVERN_NEST = 
            FEATURES.register("wyvern_nest", 
                    () -> new WyvernNestFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        MoCreatures.LOGGER.info("Registering MoCreatures features");
        FEATURES.register(eventBus);
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MoCreatures.LOGGER.info("MoCreatures features initialized");
            MoCreatures.LOGGER.info("Registered features: " + FEATURES.getEntries().size());
        });
    }
}