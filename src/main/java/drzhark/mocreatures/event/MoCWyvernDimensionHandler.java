package drzhark.mocreatures.event;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Event handler specifically for managing the Wyvern dimension mob spawning
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCWyvernDimensionHandler {

    // Use suppliers to delay initialization until the entities are actually registered
    private static final Supplier<List<EntityType<?>>> ALLOWED_ENTITIES_SUPPLIER = () -> Arrays.asList(
        MoCEntities.WYVERN.get(),
        MoCEntities.BUNNY.get(),
        MoCEntities.SNAKE.get(),
        MoCEntities.FILCH_LIZARD.get(),
        MoCEntities.DRAGONFLY.get(),
        MoCEntities.FIREFLY.get(),
        MoCEntities.GRASSHOPPER.get()
    );
    
    // Lazy initialization - will only be called when needed
    private static List<EntityType<?>> getAllowedEntities() {
        return ALLOWED_ENTITIES_SUPPLIER.get();
    }

    /**
     * Handle mob spawning - use lower priority to allow other systems to work first
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        
        // Check if we're in the Wyvern dimension
        if (entity.level() != null && 
            entity.level().dimension().identifier().toString().equals("mocreatures:wyvernlairworld")) {
            
            if (MoCreatures.proxy.debug) {
                MoCreatures.LOGGER.info("MoCWyvernDimensionHandler: Checking spawn for {} in Wyvern dimension", 
                    entity.getType().getDescriptionId());
            }
            
            // If this is an explicitly allowed entity type, force allow it
            if (getAllowedEntities().contains(entity.getType())) {
                if (MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("MoCWyvernDimensionHandler: Allowing {} to spawn in Wyvern dimension", 
                        entity.getType().getDescriptionId());
                }
                event.setResult(Event.Result.ALLOW);
                return;
            }
            
            // If this is a player, allow it
            if (entity instanceof Player) {
                event.setResult(Event.Result.DEFAULT);
                return;
            }
            
            // For other entities, log but don't interfere - let JSON modifiers handle it
            if (MoCreatures.proxy.debug) {
                MoCreatures.LOGGER.info("MoCWyvernDimensionHandler: Non-allowed entity {} attempted to spawn, letting JSON modifiers handle it", 
                    entity.getType().getDescriptionId());
            }
            event.setResult(Event.Result.DEFAULT);
        }
    }

    // @SubscribeEvent
    // public static void onServerAboutToStart(ServerAboutToStartEvent event) {
    //     MoCreatures.LOGGER.info("Adding wyvern island features to biomes directly");
        
    //     var biomeRegistry = event.getServer().registryAccess().registryOrThrow(Registries.BIOME);
        
    //     ResourceKey<?>[] biomeKeys = {
    //         ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlairlands")),
    //         ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlairlandsforest")),
    //         ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlair_mountains")),
    //         ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "wyvernlair_desertlands"))
    //     };
        
    //     for (ResourceKey<?> biomeKey : biomeKeys) {
    //         MoCreatures.LOGGER.info("Attempting to add wyvern islands to biome: {}", biomeKey.identifier());
            
    //         if (biomeRegistry.containsKey(ResourceKey.create(Registries.BIOME, biomeKey.identifier()))) {
    //             MoCreatures.LOGGER.info("Found biome {} in registry", biomeKey.identifier());
    //         } else {
    //             MoCreatures.LOGGER.error("Could not find biome {} in registry", biomeKey.identifier());
    //         }
    //     }
        
    //     MoCreatures.LOGGER.info("Wyvern island feature registration complete");
        
    //     // Log allowed entities for debugging
    //     MoCreatures.LOGGER.info("Allowed entities in Wyvern dimension:");
    //     for (EntityType<?> entityType : getAllowedEntities()) {
    //         MoCreatures.LOGGER.info("  - {}", EntityType.getKey(entityType));
    //     }
    // }
} 