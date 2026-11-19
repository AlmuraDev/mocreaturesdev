package drzhark.mocreatures.world;
import net.minecraft.util.valueproviders.UniformInt;

import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.config.biome.BiomeConfig;
import drzhark.mocreatures.config.biome.BiomeSpawnConfig;
import drzhark.mocreatures.config.biome.MoCConfig;
import drzhark.mocreatures.config.biome.SpawnBiomeData;
import drzhark.mocreatures.entity.MoCEntityData;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.world.MoCSpawnRegistryCache;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;

import java.util.Map;

@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCWorldRegistry {

    public static void modifyStructure(Holder<Structure> structure, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (structure.is(BuiltinStructures.VILLAGE_DESERT) || structure.is(BuiltinStructures.VILLAGE_PLAINS) || structure.is(BuiltinStructures.VILLAGE_SAVANNA) || structure.is(BuiltinStructures.VILLAGE_SNOWY) || structure.is(BuiltinStructures.VILLAGE_TAIGA)) {
            // Get kitty from cache to avoid chunk deadlocks
            EntityType<?> kitty = MoCSpawnRegistryCache.ENTITIES.get("kitty");
            if (kitty != null) {
                // Get kitty spawn data from entity map
                MoCEntityData kittyData = getEntityData(kitty);
                if (kittyData != null && kittyData.getCanSpawn() && kittyData.getFrequency() > 0) {
                    int spawnWeight = Math.max(1, kittyData.getFrequency() / 4); // Reduced weight for village spawns
                    builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.CREATURE)
                        .addSpawn(new MobSpawnSettings.SpawnerData(kitty, UniformInt.of(1, 3)), spawnWeight);
                }
            }
        }
    }

    /**
     * Helper method to get entity data from MoCreatures.entityMap
     */
    private static MoCEntityData getEntityData(EntityType<?> entityType) {
        if (MoCreatures.entityMap == null) {
            MoCreatures.LOGGER.warn("MoCWorldRegistry: entityMap is null! Spawn system not initialized.");
            return null;
        }
        return MoCreatures.entityMap.get(entityType);
    }



    private static Identifier getBiomeName(Holder<Biome> biome) {
        return biome.unwrap().map((resourceKey) -> resourceKey.identifier(), (noKey) -> null);
    }

    public static boolean testBiome(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome) {
        boolean result = false;
        try {
            result = BiomeConfig.test(entry, biome, getBiomeName(biome));
        } catch (Exception e) {
            MoCreatures.LOGGER.warn("could not test biome config for " + entry.getLeft() + ", defaulting to no spawns for mob");
            result = false;
        }
        return result;
    }

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        // Early exit if cache is not initialized
        if (MoCSpawnRegistryCache.ENTITIES == null || MoCSpawnRegistryCache.ENTITIES.isEmpty()) {
            return;
        }
        
        Identifier biomeRL = getBiomeName(biome);
        if (biomeRL == null) {
            return;
        }
        
        String biomeName = biomeRL.toString();
        int spawnsAdded = 0;
        
        // Debug: Check if this is a wyvern lair biome
        boolean isWyvernLair = biomeName.contains("wyvernlair");
        if (isWyvernLair && MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Processing Wyvern Lair biome: {}", biomeName);
        }
        
        // Limit the number of spawn attempts to prevent excessive processing
        int maxSpawnChecks = 150; // Increased to accommodate all entities in cache (123+ entities)
        int spawnChecks = 0;
        
        // Only process entities that are actually in the cache and have valid spawn data
        for (Map.Entry<String, EntityType<?>> entry : MoCSpawnRegistryCache.ENTITIES.entrySet()) {
            String creatureName = entry.getKey();
            EntityType<?> entityType = entry.getValue();
            
            if (entityType == null) continue;
            
            // Quick pre-check: only do expensive processing if entity has spawn data
            BiomeSpawnConfig.CreatureSpawnData spawnData = BiomeSpawnConfig.getSpawnData(creatureName);
            if (spawnData == null || !spawnData.enabled || spawnData.weight <= 0) {
                continue;
            }
            
            // Safety check to prevent infinite loops during world generation
            if (++spawnChecks > maxSpawnChecks) {
                MoCreatures.LOGGER.warn("Reached maximum spawn checks ({}) for biome {}, stopping to prevent server freeze", maxSpawnChecks, biomeName);
                break;
            }
            
            // Debug: Log for specific entities in wyvern lair
            boolean isWyvernEntity = creatureName.equals("bunny") || creatureName.equals("snake") || 
                                   creatureName.equals("filch_lizard") || creatureName.equals("wyvern") ||
                                   creatureName.equals("firefly") || creatureName.equals("dragonfly") || 
                                   creatureName.equals("grasshopper");
            
            if (isWyvernLair && isWyvernEntity && MoCreatures.proxy.debug) {
                MoCreatures.LOGGER.info("  Checking entity: {}", creatureName);
            }
            
            try {
                // Only test biome if we have valid spawn data (already checked above)
                if (!BiomeSpawnConfig.testBiome(creatureName, biome, biomeRL)) {
                    if (isWyvernLair && isWyvernEntity && MoCreatures.proxy.debug) {
                        MoCreatures.LOGGER.info("    Biome test failed for entity: {}", creatureName);
                    }
                    continue;
                }
                
                if (isWyvernLair && isWyvernEntity && MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.info("    SUCCESS: Adding {} to spawn list with weight {}, category {}", 
                        creatureName, spawnData.weight, spawnData.category);
                }
                
                // Convert category string to MobCategory
                MobCategory category;
                try {
                    category = MobCategory.valueOf(spawnData.category);
                } catch (IllegalArgumentException e) {
                    category = MobCategory.CREATURE; // Default without logging during world gen
                }
                
                // Add the spawn
                builder.getMobSpawnSettings().addSpawn(entityType, category, (int) (spawnData.weight * MoCreatures.proxy.spawnMultiplier), UniformInt.of(spawnData.minCount, spawnData.maxCount));
                
                spawnsAdded++;
                
            } catch (Exception e) {
                if (isWyvernLair && isWyvernEntity && MoCreatures.proxy.debug) {
                    MoCreatures.LOGGER.error("    Exception processing entity {}: {}", creatureName, e.getMessage());
                }
                MoCreatures.LOGGER.debug("Error processing entity {} for biome {}: {}", creatureName, biomeName, e.getMessage());
            }
        }
        
        if (isWyvernLair && MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Finished processing Wyvern Lair biome: {} - Added {} spawns total", biomeName, spawnsAdded);
        }
        
        // Early return if we're using the new system
        if (spawnsAdded > 0) {
            return;
        }
    }
    
    // Legacy method - remove the old long implementation
    public static void addBiomeSpawnsOld(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        String biomeName = getBiomeName(biome) != null ? getBiomeName(biome).toString() : "unknown";
        MoCreatures.LOGGER.debug("MoCWorldRegistry: Adding spawns for biome {}", biomeName);
        
        // MoCreatures Animal Spawns
        
        // Bears - kept one example of old format
        if (testBiome(BiomeConfig.blackBear, biome) && MoCConfig.blackBearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BLACK_BEAR.get(), MobCategory.CREATURE, MoCConfig.blackBearSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.grizzlyBear, biome) && MoCConfig.grizzlyBearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.GRIZZLY_BEAR.get(), MobCategory.CREATURE, MoCConfig.grizzlyBearSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.polarBear, biome) && MoCConfig.polarBearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.POLAR_BEAR.get(), MobCategory.CREATURE, MoCConfig.polarBearSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.pandaBear, biome) && MoCConfig.pandaBearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PANDA_BEAR.get(), MobCategory.CREATURE, MoCConfig.pandaBearSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Birds
        if (testBiome(BiomeConfig.bird, biome) && MoCConfig.birdSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BIRD.get(), MobCategory.CREATURE, MoCConfig.birdSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.duck, biome) && MoCConfig.duckSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DUCK.get(), MobCategory.CREATURE, MoCConfig.duckSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.turkey, biome) && MoCConfig.turkeySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.TURKEY.get(), MobCategory.CREATURE, MoCConfig.turkeySpawnWeight, UniformInt.of(1, 2));
        }
        
        // Land animals
        if (testBiome(BiomeConfig.boar, biome) && MoCConfig.boarSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BOAR.get(), MobCategory.CREATURE, MoCConfig.boarSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.bunny, biome) && MoCConfig.bunnySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BUNNY.get(), MobCategory.CREATURE, MoCConfig.bunnySpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.deer, biome) && MoCConfig.deerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DEER.get(), MobCategory.CREATURE, MoCConfig.deerSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.goat, biome) && MoCConfig.goatSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.GOAT.get(), MobCategory.CREATURE, MoCConfig.goatSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.kitty, biome) && MoCConfig.kittySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.KITTY.get(), MobCategory.CREATURE, MoCConfig.kittySpawnWeight, UniformInt.of(1, 3));
        }
        
        // Semi-aquatic creatures
        if (testBiome(BiomeConfig.crocodile, biome) && MoCConfig.crocodileSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CROCODILE.get(), MobCategory.CREATURE, MoCConfig.crocodileSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.turtle, biome) && MoCConfig.turtleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.TURTLE.get(), MobCategory.CREATURE, MoCConfig.turtleSpawnWeight, UniformInt.of(1, 3));
        }
        
        // Desert/savanna creatures
        if (testBiome(BiomeConfig.elephant, biome) && MoCConfig.elephantSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ELEPHANT.get(), MobCategory.CREATURE, MoCConfig.elephantSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.filchLizard, biome) && MoCConfig.filchLizardSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FILCH_LIZARD.get(), MobCategory.CREATURE, MoCConfig.filchLizardSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.ostrich, biome) && MoCConfig.ostrichSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.OSTRICH.get(), MobCategory.CREATURE, MoCConfig.ostrichSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Foxes
        if (testBiome(BiomeConfig.fox, biome) && MoCConfig.foxSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FOX.get(), MobCategory.CREATURE, MoCConfig.foxSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Reptiles
        if (testBiome(BiomeConfig.komodoDragon, biome) && MoCConfig.komodoDragonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.KOMODO_DRAGON.get(), MobCategory.CREATURE, MoCConfig.komodoDragonSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.snake, biome) && MoCConfig.snakeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.SNAKE.get(), MobCategory.CREATURE, MoCConfig.snakeSpawnWeight, UniformInt.of(1, 3));
        }
        
        // Big cats
        if (testBiome(BiomeConfig.leopard, biome) && MoCConfig.leopardSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.LEOPARD.get(), MobCategory.CREATURE, MoCConfig.leopardSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.lion, biome) && MoCConfig.lionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.LION.get(), MobCategory.CREATURE, MoCConfig.lionSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.panther, biome) && MoCConfig.pantherSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PANTHER.get(), MobCategory.CREATURE, MoCConfig.pantherSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.tiger, biome) && MoCConfig.tigerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.TIGER.get(), MobCategory.CREATURE, MoCConfig.tigerSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Big cat hybrids
        if (testBiome(BiomeConfig.liger, biome) && MoCConfig.ligerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.LIGER.get(), MobCategory.CREATURE, MoCConfig.ligerSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.lither, biome) && MoCConfig.litherSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.LITHER.get(), MobCategory.CREATURE, MoCConfig.litherSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.panthger, biome) && MoCConfig.panthgerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PANTHGER.get(), MobCategory.CREATURE, MoCConfig.panthgerSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.panthard, biome) && MoCConfig.panthardSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PANTHARD.get(), MobCategory.CREATURE, MoCConfig.panthardSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.leoger, biome) && MoCConfig.leogerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.LEOGER.get(), MobCategory.CREATURE, MoCConfig.leogerSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Small mammals
        if (testBiome(BiomeConfig.mouse, biome) && MoCConfig.mouseSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MOUSE.get(), MobCategory.CREATURE, MoCConfig.mouseSpawnWeight, UniformInt.of(2, 6));
        }
        if (testBiome(BiomeConfig.mole, biome) && MoCConfig.moleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MOLE.get(), MobCategory.CREATURE, MoCConfig.moleSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.raccoon, biome) && MoCConfig.raccoonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.RACCOON.get(), MobCategory.CREATURE, MoCConfig.raccoonSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Horses
        if (testBiome(BiomeConfig.wildHorse, biome) && MoCConfig.wildHorseSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.WILDHORSE.get(), MobCategory.CREATURE, MoCConfig.wildHorseSpawnWeight, UniformInt.of(2, 4));
        }
        
        // Magical creatures
        if (testBiome(BiomeConfig.ent, biome) && MoCConfig.entSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ENT.get(), MobCategory.CREATURE, MoCConfig.entSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.wyvern, biome) && MoCConfig.wyvernSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.WYVERN.get(), MobCategory.CREATURE, MoCConfig.wyvernSpawnWeight, UniformInt.of(1, 1));
        }
        
        // MoCreatures Monster Spawns
        
        // Ogres
        if (testBiome(BiomeConfig.greenOgre, biome) && MoCConfig.greenOgreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.GREEN_OGRE.get(), MobCategory.MONSTER, MoCConfig.greenOgreSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.caveOgre, biome) && MoCConfig.caveOgreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CAVE_OGRE.get(), MobCategory.MONSTER, MoCConfig.caveOgreSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.fireOgre, biome) && MoCConfig.fireOgreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FIRE_OGRE.get(), MobCategory.MONSTER, MoCConfig.fireOgreSpawnWeight, UniformInt.of(1, 1));
        }
        
        // Scorpions
        if (testBiome(BiomeConfig.caveScorpion, biome) && MoCConfig.caveScorpionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CAVE_SCORPION.get(), MobCategory.MONSTER, MoCConfig.caveScorpionSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.dirtScorpion, biome) && MoCConfig.dirtScorpionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DIRT_SCORPION.get(), MobCategory.MONSTER, MoCConfig.dirtScorpionSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.fireScorpion, biome) && MoCConfig.fireScorpionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FIRE_SCORPION.get(), MobCategory.MONSTER, MoCConfig.fireScorpionSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.frostScorpion, biome) && MoCConfig.frostScorpionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FROST_SCORPION.get(), MobCategory.MONSTER, MoCConfig.frostScorpionSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.undeadScorpion, biome) && MoCConfig.undeadScorpionSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.UNDEAD_SCORPION.get(), MobCategory.MONSTER, MoCConfig.undeadScorpionSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Golems
        if (testBiome(BiomeConfig.bigGolem, biome) && MoCConfig.bigGolemSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BIG_GOLEM.get(), MobCategory.MONSTER, MoCConfig.bigGolemSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.miniGolem, biome) && MoCConfig.miniGolemSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MINI_GOLEM.get(), MobCategory.MONSTER, MoCConfig.miniGolemSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Manticores
        if (testBiome(BiomeConfig.darkManticore, biome) && MoCConfig.darkManticoreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DARK_MANTICORE.get(), MobCategory.MONSTER, MoCConfig.darkManticoreSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.fireManticore, biome) && MoCConfig.fireManticoreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FIRE_MANTICORE.get(), MobCategory.MONSTER, MoCConfig.fireManticoreSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.frostManticore, biome) && MoCConfig.frostManticoreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FROST_MANTICORE.get(), MobCategory.MONSTER, MoCConfig.frostManticoreSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.plainManticore, biome) && MoCConfig.plainManticoreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PLAIN_MANTICORE.get(), MobCategory.MONSTER, MoCConfig.plainManticoreSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.toxicManticore, biome) && MoCConfig.toxicManticoreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.TOXIC_MANTICORE.get(), MobCategory.MONSTER, MoCConfig.toxicManticoreSpawnWeight, UniformInt.of(1, 1));
        }
        
        // Werewolves and variants
        if (testBiome(BiomeConfig.werewolf, biome) && MoCConfig.werewolfSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.WEREWOLF.get(), MobCategory.MONSTER, MoCConfig.werewolfSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.wwolf, biome) && MoCConfig.wwolfSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.WWOLF.get(), MobCategory.MONSTER, MoCConfig.wwolfSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Rats
        if (testBiome(BiomeConfig.rat, biome) && MoCConfig.ratSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.RAT.get(), MobCategory.MONSTER, MoCConfig.ratSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.hellRat, biome) && MoCConfig.hellRatSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.HELL_RAT.get(), MobCategory.MONSTER, MoCConfig.hellRatSpawnWeight, UniformInt.of(1, 3));
        }
        
        // Undead
        if (testBiome(BiomeConfig.silverSkeleton, biome) && MoCConfig.silverSkeletonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.SILVER_SKELETON.get(), MobCategory.MONSTER, MoCConfig.silverSkeletonSpawnWeight, UniformInt.of(1, 2));
        }
        
        // Wraiths
        if (testBiome(BiomeConfig.wraith, biome) && MoCConfig.wraithSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.WRAITH.get(), MobCategory.MONSTER, MoCConfig.wraithSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.flameWraith, biome) && MoCConfig.flameWraithSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FLAME_WRAITH.get(), MobCategory.MONSTER, MoCConfig.flameWraithSpawnWeight, UniformInt.of(1, 1));
        }
        
        // Nether mounts
        if (testBiome(BiomeConfig.horseMob, biome) && MoCConfig.horseMobSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.HORSE_MOB.get(), MobCategory.MONSTER, MoCConfig.horseMobSpawnWeight, UniformInt.of(1, 2));
        }
        
        // MoCreatures Aquatic Spawns
        
        // Ocean creatures
        if (testBiome(BiomeConfig.dolphin, biome) && MoCConfig.dolphinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DOLPHIN.get(), MobCategory.WATER_CREATURE, MoCConfig.dolphinSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.shark, biome) && MoCConfig.sharkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.SHARK.get(), MobCategory.WATER_CREATURE, MoCConfig.sharkSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.mantaRay, biome) && MoCConfig.mantaRaySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MANTA_RAY.get(), MobCategory.WATER_CREATURE, MoCConfig.mantaRaySpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.jellyfish, biome) && MoCConfig.jellyfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.JELLYFISH.get(), MobCategory.WATER_CREATURE, MoCConfig.jellyfishSpawnWeight, UniformInt.of(1, 3));
        }
        
        // River/swamp creatures
        if (testBiome(BiomeConfig.bass, biome) && MoCConfig.bassSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BASS.get(), MobCategory.WATER_CREATURE, MoCConfig.bassSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.stingRay, biome) && MoCConfig.stingRaySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.STING_RAY.get(), MobCategory.WATER_CREATURE, MoCConfig.stingRaySpawnWeight, UniformInt.of(1, 2));
        }
        
        // Small fish
        if (testBiome(BiomeConfig.anchovy, biome) && MoCConfig.anchovySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ANCHOVY.get(), MobCategory.WATER_AMBIENT, MoCConfig.anchovySpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.angelfish, biome) && MoCConfig.angelfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ANGELFISH.get(), MobCategory.WATER_AMBIENT, MoCConfig.angelfishSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.angler, biome) && MoCConfig.anglerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ANGLER.get(), MobCategory.WATER_AMBIENT, MoCConfig.anglerSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.clownfish, biome) && MoCConfig.clownfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CLOWNFISH.get(), MobCategory.WATER_AMBIENT, MoCConfig.clownfishSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.goldfish, biome) && MoCConfig.goldfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.GOLDFISH.get(), MobCategory.WATER_AMBIENT, MoCConfig.goldfishSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.hippotang, biome) && MoCConfig.hippotangSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.HIPPOTANG.get(), MobCategory.WATER_AMBIENT, MoCConfig.hippotangSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.manderin, biome) && MoCConfig.manderinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MANDERIN.get(), MobCategory.WATER_AMBIENT, MoCConfig.manderinSpawnWeight, UniformInt.of(2, 4));
        }
        
        // Medium fish
        if (testBiome(BiomeConfig.cod, biome) && MoCConfig.codSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.COD.get(), MobCategory.WATER_CREATURE, MoCConfig.codSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.salmon, biome) && MoCConfig.salmonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.SALMON.get(), MobCategory.WATER_CREATURE, MoCConfig.salmonSpawnWeight, UniformInt.of(3, 6));
        }
        
        // Predatory fish
        if (testBiome(BiomeConfig.piranha, biome) && MoCConfig.piranhaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.PIRANHA.get(), MobCategory.WATER_CREATURE, MoCConfig.piranhaSpawnWeight, UniformInt.of(3, 6));
        }
        
        // General fish
        if (testBiome(BiomeConfig.fishy, biome) && MoCConfig.fishySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FISHY.get(), MobCategory.WATER_AMBIENT, MoCConfig.fishySpawnWeight, UniformInt.of(4, 8));
        }
        
        // MoCreatures Ambient Spawns
        
        // Insects
        if (testBiome(BiomeConfig.ant, biome) && MoCConfig.antSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ANT.get(), MobCategory.AMBIENT, MoCConfig.antSpawnWeight, UniformInt.of(4, 8));
        }
        if (testBiome(BiomeConfig.bee, biome) && MoCConfig.beeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BEE.get(), MobCategory.AMBIENT, MoCConfig.beeSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.butterfly, biome) && MoCConfig.butterflySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.BUTTERFLY.get(), MobCategory.AMBIENT, MoCConfig.butterflySpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.cricket, biome) && MoCConfig.cricketSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CRICKET.get(), MobCategory.AMBIENT, MoCConfig.cricketSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.dragonfly, biome) && MoCConfig.dragonflySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.DRAGONFLY.get(), MobCategory.AMBIENT, MoCConfig.dragonflySpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.firefly, biome) && MoCConfig.fireflySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FIREFLY.get(), MobCategory.AMBIENT, MoCConfig.fireflySpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.fly, biome) && MoCConfig.flySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.FLY.get(), MobCategory.AMBIENT, MoCConfig.flySpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.grasshopper, biome) && MoCConfig.grasshopperSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.GRASSHOPPER.get(), MobCategory.AMBIENT, MoCConfig.grasshopperSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.maggot, biome) && MoCConfig.maggotSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.MAGGOT.get(), MobCategory.AMBIENT, MoCConfig.maggotSpawnWeight, UniformInt.of(2, 5));
        }
        if (testBiome(BiomeConfig.roach, biome) && MoCConfig.roachSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.ROACH.get(), MobCategory.AMBIENT, MoCConfig.roachSpawnWeight, UniformInt.of(2, 5));
        }
        
        // Beach/aquatic ambient
        if (testBiome(BiomeConfig.crab, biome) && MoCConfig.crabSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.CRAB.get(), MobCategory.AMBIENT, MoCConfig.crabSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.snail, biome) && MoCConfig.snailSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MoCEntities.SNAIL.get(), MobCategory.AMBIENT, MoCConfig.snailSpawnWeight, UniformInt.of(2, 4));
        }
    }
}