/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.worldgen.feature.FirestoneClusterFeature;
import drzhark.mocreatures.worldgen.feature.WyvernNestFeature;
import drzhark.mocreatures.worldgen.structure.WyvernIslandFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCFeatures {

    /** Feature types: the codecs the worldgen/feature JSON files refer to with their "type". */
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES = DeferredRegister.create(Registries.FEATURE_TYPE, MoCConstants.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FirestoneClusterFeature>> FIRESTONE_CLUSTER =
            FEATURES.register("firestone_cluster", () -> FirestoneClusterFeature.CODEC);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WyvernIslandFeature>> WYVERN_ISLAND =
            FEATURES.register("wyvern_island", () -> WyvernIslandFeature.CODEC);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WyvernNestFeature>> WYVERN_NEST =
            FEATURES.register("wyvern_nest", () -> WyvernNestFeature.CODEC);

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