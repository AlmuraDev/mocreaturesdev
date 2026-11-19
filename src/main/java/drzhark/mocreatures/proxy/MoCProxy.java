/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.proxy;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.config.MoCConfigCategory;
import drzhark.mocreatures.config.MoCConfiguration;
import drzhark.mocreatures.config.MoCProperty;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.MoCEntityData;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.tags.TagKey;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

@SuppressWarnings("removal")
public class MoCProxy {

    protected static final String CATEGORY_MOC_GENERAL_SETTINGS = "global-settings";
    protected static final String CATEGORY_MOC_CREATURE_GENERAL_SETTINGS = "creature-general-settings";
    protected static final String CATEGORY_MOC_MONSTER_GENERAL_SETTINGS = "monster-general-settings";
    protected static final String CATEGORY_MOC_WATER_CREATURE_GENERAL_SETTINGS = "water-mob-general-settings";
    protected static final String CATEGORY_MOC_AMBIENT_GENERAL_SETTINGS = "ambient-general-settings";
    protected static final String CATEGORY_MOC_ID_SETTINGS = "custom-id-settings";
    private static final String CATEGORY_OWNERSHIP_SETTINGS = "ownership-settings";
    public static String ARMOR_TEXTURE = "textures/armor/";
    public static String BLOCK_TEXTURE = "textures/blocks/";
    public static String ITEM_TEXTURE = "textures/items/";
    public static String MODEL_TEXTURE = "textures/models/";
    public static String GUI_TEXTURE = "textures/gui/";
    public static String MISC_TEXTURE = "textures/misc/";

    // CONFIG VARIABLES
    public boolean allowInstaSpawn;
    public boolean alphaWraithEyes;
    public boolean alwaysNamePets;
    public boolean animateTextures;
    public boolean armorSetEffects;
    public boolean attackHorses;
    public boolean attackWolves;
    public boolean attackDolphins;
    public boolean debug;
    public boolean destroyDrops;
    public boolean displayPetHealth;
    public boolean displayPetIcons;
    public boolean displayPetName;
    public boolean easterEggs;
    public boolean easyHorseBreeding;
    public boolean elephantBulldozer;
    public boolean originalHorseBreeding;
    public boolean enableHunters;
    public boolean enableOwnership;
    public boolean enableResetOwnership;
    public boolean foggyWyvernLair;
    public boolean golemDestroyBlocks;
    public boolean legacyBigCatModels;
    public boolean legacyBunnyTextures;
    public boolean legacyRatDeathSound;
    public boolean legacyWerehumanSounds;
    public boolean legacyWraithSounds;
    public boolean legacyWyvernLairSky;
    public boolean staticBed;
    public boolean staticLitter;
    public boolean verboseEntityNames;
    public boolean weaponEffects;
    public boolean worldInitDone;
    public double spawnMultiplier;
    public float ogreCaveStrength;
    public float ogreFireStrength;
    public float ogreStrength;
    public int filchLizardSpawnItemChance;
    public int kittyVillageChance;
    public int maxOPTamed;
    public int maxTamed;
    public int motherWyvernEggDropChance;
    public int ostrichEggDropChance;
    public int particleFX;
    public int rareItemDropChance;
    public ResourceKey<Level> wyvernDimension;
    public int wyvernEggDropChance;
    public short ogreAttackRange;

    public MoCConfiguration mocSettingsConfig;
    public MoCConfiguration mocEntityConfig;


    /** Opens a written book screen; only the client proxy does anything. */
    public void openBook(net.minecraft.world.item.ItemStack book) {
    }

    public void resetAllData() {
        this.readGlobalConfigValues();
    }

    //----------------CONFIG INITIALIZATION
    public void configInit() {
        this.mocSettingsConfig = new MoCConfiguration(new File(FMLPaths.CONFIGDIR.get().toString(), "MoCreatures" + File.separator + "MoCSettings.cfg"));
        //this.mocEntityConfig = new MoCConfiguration(new File(FMLPaths.CONFIGDIR.get().toString(), "MoCreatures" + File.separator + "MoCreatures.cfg"));
        this.mocSettingsConfig.load();
        //this.mocEntityConfig.load();
        this.readGlobalConfigValues();
        if (this.debug) {
            MoCreatures.LOGGER.info("Initializing MoCreatures Config File at " + FMLPaths.CONFIGDIR.get().toString() + "MoCSettings.cfg");
        }
    }

    //-----------------THE FOLLOWING ARE CLIENT SIDE ONLY, NOT TO BE USED IN SERVER AS THEY AFFECT ONLY DISPLAY / SOUNDS

    public void UndeadFX(Entity entity) {
    }

    public void StarFX(MoCEntityHorse moCEntityHorse) {
    }

    public void LavaFX(Entity entity) {
    }

    public void VanishFX(MoCEntityHorse entity) {
    }

    public void MaterializeFX(MoCEntityHorse entity) {
    }

    public void VacuumFX(MoCEntityGolem entity) {
    }

    public void hammerFX(Player entityplayer) {
    }

    public void teleportFX(Player entity) {
    }

    public boolean getAnimateTextures() {
        return false;
    }

    public boolean getDisplayPetName() {
        return this.displayPetName;
    }

    public boolean getDisplayPetIcons() {
        return this.displayPetIcons;
    }

    public boolean getDisplayPetHealth() {
        return this.displayPetHealth;
    }

    public int getParticleFX() {
        return 0;
    }

    public Identifier getArmorTexture(String texture) {
        return null;
    }

    public Identifier getBlockTexture(String texture) {
        return null;
    }

    public Identifier getItemTexture(String texture) {
        return null;
    }

    public Identifier getModelTexture(String texture) {
        return null;
    }

    public Identifier getGuiTexture(String texture) {
        return null;
    }

    public Identifier getMiscTexture(String texture) {
        return null;
    }

    public Player getPlayer() {
        return null;
    }

    // Client side only
    public void printMessageToPlayer(String msg) {
    }

    public List<TagKey<Biome>> parseBiomeTypes(String[] biomeNames) {
        List<TagKey<Biome>> biomeTypes = new ArrayList<>();
        for (String biomeName : biomeNames) {
            if (biomeName == null || biomeName.trim().isEmpty()) {
                continue;
            }
            
            try {
                String cleanBiomeName = biomeName.trim();
                
                // Handle the malformed format from config: "TagKeyminecraft:worldgen/biome/minecraft:is_forest"
                if (cleanBiomeName.startsWith("TagKey") && !cleanBiomeName.startsWith("TagKey[")) {
                    // This is the corrupted format - extract the actual tag ID
                    if (cleanBiomeName.contains("/minecraft:")) {
                        // Extract everything after the last "/"
                        int lastSlash = cleanBiomeName.lastIndexOf("/");
                        if (lastSlash != -1 && lastSlash < cleanBiomeName.length() - 1) {
                            cleanBiomeName = cleanBiomeName.substring(lastSlash + 1);
                        }
                    } else if (cleanBiomeName.contains("/forge:")) {
                        // Extract everything after the last "/"
                        int lastSlash = cleanBiomeName.lastIndexOf("/");
                        if (lastSlash != -1 && lastSlash < cleanBiomeName.length() - 1) {
                            cleanBiomeName = cleanBiomeName.substring(lastSlash + 1);
                        }
                    }
                }
                // Handle the proper TagKey format: "TagKey[minecraft:worldgen/biome / minecraft:is_forest]"
                else if (cleanBiomeName.startsWith("TagKey[") && cleanBiomeName.endsWith("]")) {
                    // Extract the actual resource location from TagKey format
                    String inner = cleanBiomeName.substring(7, cleanBiomeName.length() - 1); // Remove "TagKey[" and "]"
                    String[] parts = inner.split(" / ");
                    if (parts.length >= 2) {
                        cleanBiomeName = parts[1]; // Take the second part which is the actual tag ID
                    } else {
                        cleanBiomeName = inner; // Fallback to the whole inner content
                    }
                }
                // Handle direct format: "minecraft:is_forest"
                // (cleanBiomeName is already correct)
                
                // Validate that we have a proper resource location format
                if (!cleanBiomeName.contains(":")) {
                    cleanBiomeName = "minecraft:" + cleanBiomeName; // Default to minecraft namespace
                }
                
                TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, Identifier.parse(drzhark.mocreatures.config.biome.MoCBiomeTagNames.modernize(cleanBiomeName)));
                biomeTypes.add(biomeTag);
                
                if (MoCreatures.isDebug()) {
                    MoCreatures.LOGGER.debug("Successfully parsed biome tag: {} -> {}", biomeName, cleanBiomeName);
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.warn("Failed to parse biome tag '{}': {}", biomeName, e.getMessage());
                // Try to create a fallback tag if the original fails
                try {
                    String fallback = biomeName.replaceAll("[^a-z0-9_/:-]", "").toLowerCase();
                    if (fallback.contains(":") && Identifier.tryParse(fallback) != null) {
                        TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, Identifier.parse(drzhark.mocreatures.config.biome.MoCBiomeTagNames.modernize(fallback)));
                        biomeTypes.add(biomeTag);
                        MoCreatures.LOGGER.info("Using fallback biome tag: {} -> {}", biomeName, fallback);
                    }
                } catch (Exception fallbackException) {
                    MoCreatures.LOGGER.error("Complete failure parsing biome tag: {}", biomeName);
                }
            }
        }
        return biomeTypes;
    }

    /**
     * Parse a config array string properly, preserving spaces in TagKey format.
     * Handles formats like: "[TagKey[minecraft:worldgen/biome / minecraft:is_forest], TagKey[...]]"
     */
    private String[] parseConfigArray(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return new String[0];
        }
        
        // Remove outer brackets if present
        String trimmed = rawValue.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        
        // Split by comma, but be careful with TagKey format which contains commas in spaces
        List<String> result = new ArrayList<>();
        int depth = 0;
        int start = 0;
        
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
            } else if (c == ',' && depth == 0) {
                // Found a top-level comma - this is a separator
                result.add(trimmed.substring(start, i).trim());
                start = i + 1;
            }
        }
        
        // Add the last element
        if (start < trimmed.length()) {
            result.add(trimmed.substring(start).trim());
        }
        
        return result.toArray(new String[0]);
    }

    /**
     * Initialize the mocEntityMap with MoCEntityData entries from spawn configuration
     * This must be called before readMocConfigValues() to populate the entity map
     */
    public void initializeMocEntityMap() {
        if (MoCreatures.mocEntityMap == null) {
            MoCreatures.mocEntityMap = new Object2ObjectLinkedOpenHashMap<>();
        }
        if (MoCreatures.entityMap == null) {
            MoCreatures.entityMap = new it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap<>();
        }
        
        // Clear existing entries
        MoCreatures.mocEntityMap.clear();
        MoCreatures.entityMap.clear();
        
        // Populate with hardcoded spawn data from the deprecated config
        populateEntityMapWithDefaults();
        
        MoCreatures.LOGGER.info("Initialized {} entities in mocEntityMap", MoCreatures.mocEntityMap.size());
        MoCreatures.LOGGER.info("Initialized {} entities in entityMap", MoCreatures.entityMap.size());
        
        // Debug: Show some sample entity data
        if (!MoCreatures.mocEntityMap.isEmpty()) {
            MoCEntityData sampleData = MoCreatures.mocEntityMap.values().iterator().next();
            MoCreatures.LOGGER.info("Sample entity data - Name: {}, CanSpawn: {}, Frequency: {}, MinSpawn: {}, MaxSpawn: {}", 
                sampleData.getEntityName(), sampleData.getCanSpawn(), sampleData.getFrequency(), 
                sampleData.getMinSpawn(), sampleData.getMaxSpawn());
        }
    }

    /**
     * Populate entity map with default spawn data extracted from MoCSpawnConfig_DEPRECATED.java
     */
    private void populateEntityMapWithDefaults() {
        ResourceKey<Level>[] overworldDimensions = new ResourceKey[] { Level.OVERWORLD };
        ResourceKey<Level>[] netherDimensions = new ResourceKey[] { Level.NETHER };
        ResourceKey<Level>[] wyvernDimensions = new ResourceKey[] { wyvernDimension };
        
        // Helper method to create TagKey from string
        java.util.function.Function<String, TagKey<Biome>> createBiomeTag = (tagName) -> {
            try {
                return TagKey.create(net.minecraft.core.registries.Registries.BIOME, Identifier.parse(drzhark.mocreatures.config.biome.MoCBiomeTagNames.modernize(tagName)));
            } catch (Exception e) {
                MoCreatures.LOGGER.warn("Failed to create biome tag: {}", tagName);
                return null;
            }
        };
        
        // Bears
        addEntityData("BlackBear", drzhark.mocreatures.init.MoCEntities.BLACK_BEAR.get(), MobCategory.CREATURE, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_tree/coniferous")}, new TagKey[0]);
        addEntityData("GrizzlyBear", drzhark.mocreatures.init.MoCEntities.GRIZZLY_BEAR.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        addEntityData("PolarBear", drzhark.mocreatures.init.MoCEntities.POLAR_BEAR.get(), MobCategory.CREATURE, 8, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_snowy")}, new TagKey[0]);
        addEntityData("PandaBear", drzhark.mocreatures.init.MoCEntities.PANDA_BEAR.get(), MobCategory.CREATURE, 7, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        
        // Birds
        addEntityData("Bird", drzhark.mocreatures.init.MoCEntities.BIRD.get(), MobCategory.CREATURE, 16, 2, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("Duck", drzhark.mocreatures.init.MoCEntities.DUCK.get(), MobCategory.CREATURE, 12, 2, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_river"), createBiomeTag.apply("c:is_lush")}, new TagKey[0]);
        addEntityData("Turkey", drzhark.mocreatures.init.MoCEntities.TURKEY.get(), MobCategory.CREATURE, 12, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        
        // Common land animals
        addEntityData("Boar", drzhark.mocreatures.init.MoCEntities.BOAR.get(), MobCategory.CREATURE, 12, 2, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        addEntityData("Bunny", drzhark.mocreatures.init.MoCEntities.BUNNY.get(), MobCategory.CREATURE, 12, 2, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_snowy"),
                createBiomeTag.apply("c:is_tree/coniferous"), createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        addEntityData("Deer", drzhark.mocreatures.init.MoCEntities.DEER.get(), MobCategory.CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_tree/coniferous")}, new TagKey[0]);
        addEntityData("Goat", drzhark.mocreatures.init.MoCEntities.GOAT.get(), MobCategory.CREATURE, 12, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("Kitty", drzhark.mocreatures.init.MoCEntities.KITTY.get(), MobCategory.CREATURE, 8, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        
        // Semi-aquatic creatures  
        addEntityData("Crocodile", drzhark.mocreatures.init.MoCEntities.CROCODILE.get(), MobCategory.CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp")}, new TagKey[0]);
        addEntityData("Turtle", drzhark.mocreatures.init.MoCEntities.TURTLE.get(), MobCategory.CREATURE, 12, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_river")}, new TagKey[0]);
        
        // Desert/savanna creatures
        addEntityData("Elephant", drzhark.mocreatures.init.MoCEntities.ELEPHANT.get(), MobCategory.CREATURE, 6, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("minecraft:is_savanna"), createBiomeTag.apply("c:is_snowy")}, 
                new TagKey[]{createBiomeTag.apply("minecraft:is_badlands")});
        addEntityData("FilchLizard", drzhark.mocreatures.init.MoCEntities.FILCH_LIZARD.get(), MobCategory.CREATURE, 6, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_savanna"), createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        addEntityData("Ostrich", drzhark.mocreatures.init.MoCEntities.OSTRICH.get(), MobCategory.CREATURE, 7, 1, 1, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_savanna"), createBiomeTag.apply("c:is_sandy")}, 
                new TagKey[]{createBiomeTag.apply("minecraft:is_badlands")});
        
        // Foxes
        addEntityData("Fox", drzhark.mocreatures.init.MoCEntities.FOX.get(), MobCategory.CREATURE, 10, 1, 1, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_snowy"), createBiomeTag.apply("c:is_tree/coniferous")}, new TagKey[0]);
        
        // Reptiles
        addEntityData("KomodoDragon", drzhark.mocreatures.init.MoCEntities.KOMODO_DRAGON.get(), MobCategory.CREATURE, 12, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        addEntityData("Snake", drzhark.mocreatures.init.MoCEntities.SNAKE.get(), MobCategory.CREATURE, 14, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("c:is_lush"), 
                createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        
        // Big cats
        addEntityData("Leopard", drzhark.mocreatures.init.MoCEntities.LEOPARD.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_snowy"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        addEntityData("Lion", drzhark.mocreatures.init.MoCEntities.LION.get(), MobCategory.CREATURE, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_savanna"), createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_badlands")}, new TagKey[0]);
        addEntityData("Panther", drzhark.mocreatures.init.MoCEntities.PANTHER.get(), MobCategory.CREATURE, 6, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        addEntityData("Tiger", drzhark.mocreatures.init.MoCEntities.TIGER.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        
        // Big cat hybrids
        addEntityData("Liger", drzhark.mocreatures.init.MoCEntities.LIGER.get(), MobCategory.CREATURE, 8, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        addEntityData("Lither", drzhark.mocreatures.init.MoCEntities.LITHER.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        addEntityData("Panthger", drzhark.mocreatures.init.MoCEntities.PANTHGER.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        addEntityData("Panthard", drzhark.mocreatures.init.MoCEntities.PANTHARD.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        addEntityData("Leoger", drzhark.mocreatures.init.MoCEntities.LEOGER.get(), MobCategory.CREATURE, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        
        // Small mammals
        addEntityData("Mouse", drzhark.mocreatures.init.MoCEntities.MOUSE.get(), MobCategory.CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("Mole", drzhark.mocreatures.init.MoCEntities.MOLE.get(), MobCategory.CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        addEntityData("Raccoon", drzhark.mocreatures.init.MoCEntities.RACCOON.get(), MobCategory.CREATURE, 12, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        
        // Horses
        addEntityData("WildHorse", drzhark.mocreatures.init.MoCEntities.WILDHORSE.get(), MobCategory.CREATURE, 12, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        
        // Magical creatures
        addEntityData("Ent", drzhark.mocreatures.init.MoCEntities.ENT.get(), MobCategory.CREATURE, 5, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        addEntityData("Wyvern", drzhark.mocreatures.init.MoCEntities.WYVERN.get(), MobCategory.CREATURE, 12, 1, 3, wyvernDimensions,
                new TagKey[]{createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        
        // Monsters
        addEntityData("GreenOgre", drzhark.mocreatures.init.MoCEntities.GREEN_OGRE.get(), MobCategory.MONSTER, 8, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("c:is_lush"), 
                createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        addEntityData("CaveOgre", drzhark.mocreatures.init.MoCEntities.CAVE_OGRE.get(), MobCategory.MONSTER, 5, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("FireOgre", drzhark.mocreatures.init.MoCEntities.FIRE_OGRE.get(), MobCategory.MONSTER, 6, 1, 2, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether")}, new TagKey[0]);
        
        // More monsters
        addEntityData("CaveScorpion", drzhark.mocreatures.init.MoCEntities.CAVE_SCORPION.get(), MobCategory.MONSTER, 4, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("c:is_snowy"), 
                createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_dry"), createBiomeTag.apply("c:is_hot"), 
                createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        addEntityData("DirtScorpion", drzhark.mocreatures.init.MoCEntities.DIRT_SCORPION.get(), MobCategory.MONSTER, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_dry"), createBiomeTag.apply("c:is_hot")}, new TagKey[0]);
        addEntityData("FireScorpion", drzhark.mocreatures.init.MoCEntities.FIRE_SCORPION.get(), MobCategory.MONSTER, 6, 1, 3, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether")}, new TagKey[0]);
        addEntityData("FrostScorpion", drzhark.mocreatures.init.MoCEntities.FROST_SCORPION.get(), MobCategory.MONSTER, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_snowy")}, new TagKey[0]);
        addEntityData("UndeadScorpion", drzhark.mocreatures.init.MoCEntities.UNDEAD_SCORPION.get(), MobCategory.MONSTER, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        
        addEntityData("BigGolem", drzhark.mocreatures.init.MoCEntities.BIG_GOLEM.get(), MobCategory.MONSTER, 3, 1, 1, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("c:is_hill"), createBiomeTag.apply("minecraft:is_badlands"), 
                createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_dead")}, new TagKey[0]);
        addEntityData("MiniGolem", drzhark.mocreatures.init.MoCEntities.MINI_GOLEM.get(), MobCategory.MONSTER, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_mountain"), 
                createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_dead")}, new TagKey[0]);
        
        addEntityData("DarkManticore", drzhark.mocreatures.init.MoCEntities.DARK_MANTICORE.get(), MobCategory.MONSTER, 5, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("c:is_snowy"), createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        addEntityData("FireManticore", drzhark.mocreatures.init.MoCEntities.FIRE_MANTICORE.get(), MobCategory.MONSTER, 8, 1, 3, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether")}, new TagKey[0]);
        addEntityData("FrostManticore", drzhark.mocreatures.init.MoCEntities.FROST_MANTICORE.get(), MobCategory.MONSTER, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_snowy")}, new TagKey[0]);
        addEntityData("PlainManticore", drzhark.mocreatures.init.MoCEntities.PLAIN_MANTICORE.get(), MobCategory.MONSTER, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("c:is_mountain"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        addEntityData("ToxicManticore", drzhark.mocreatures.init.MoCEntities.TOXIC_MANTICORE.get(), MobCategory.MONSTER, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        
        addEntityData("Werewolf", drzhark.mocreatures.init.MoCEntities.WEREWOLF.get(), MobCategory.MONSTER, 8, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_tree/coniferous"), createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        addEntityData("WWolf", drzhark.mocreatures.init.MoCEntities.WWOLF.get(), MobCategory.MONSTER, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_snowy"), createBiomeTag.apply("c:is_dead")}, new TagKey[0]);
        
        addEntityData("Rat", drzhark.mocreatures.init.MoCEntities.RAT.get(), MobCategory.MONSTER, 7, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_badlands"), createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("HellRat", drzhark.mocreatures.init.MoCEntities.HELL_RAT.get(), MobCategory.MONSTER, 6, 1, 4, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether")}, new TagKey[0]);
        
        addEntityData("SilverSkeleton", drzhark.mocreatures.init.MoCEntities.SILVER_SKELETON.get(), MobCategory.MONSTER, 6, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_sandy"), createBiomeTag.apply("c:is_snowy"), createBiomeTag.apply("minecraft:is_badlands"), 
                createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        
        addEntityData("Wraith", drzhark.mocreatures.init.MoCEntities.WRAITH.get(), MobCategory.MONSTER, 6, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_tree/coniferous"), createBiomeTag.apply("c:is_dead"), 
                createBiomeTag.apply("c:is_dense_vegetation"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        addEntityData("FlameWraith", drzhark.mocreatures.init.MoCEntities.FLAME_WRAITH.get(), MobCategory.MONSTER, 5, 1, 2, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether")}, new TagKey[0]);
        
        addEntityData("HorseMob", drzhark.mocreatures.init.MoCEntities.HORSE_MOB.get(), MobCategory.MONSTER, 8, 1, 3, netherDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_nether"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_savanna"), 
                createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        
        // Aquatic creatures
        addEntityData("Dolphin", drzhark.mocreatures.init.MoCEntities.DOLPHIN.get(), MobCategory.WATER_CREATURE, 6, 2, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("Shark", drzhark.mocreatures.init.MoCEntities.SHARK.get(), MobCategory.WATER_CREATURE, 6, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("MantaRay", drzhark.mocreatures.init.MoCEntities.MANTA_RAY.get(), MobCategory.WATER_CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("JellyFish", drzhark.mocreatures.init.MoCEntities.JELLYFISH.get(), MobCategory.WATER_CREATURE, 8, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        
        addEntityData("Bass", drzhark.mocreatures.init.MoCEntities.BASS.get(), MobCategory.WATER_CREATURE, 10, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_river"), createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        addEntityData("StingRay", drzhark.mocreatures.init.MoCEntities.STING_RAY.get(), MobCategory.WATER_CREATURE, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_river")}, new TagKey[0]);
        
        // Small fish
        addEntityData("Anchovy", drzhark.mocreatures.init.MoCEntities.ANCHOVY.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean"), createBiomeTag.apply("minecraft:is_river")}, new TagKey[0]);
        addEntityData("AngelFish", drzhark.mocreatures.init.MoCEntities.ANGELFISH.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_river"), createBiomeTag.apply("minecraft:is_jungle")}, new TagKey[0]);
        addEntityData("Angler", drzhark.mocreatures.init.MoCEntities.ANGLER.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("ClownFish", drzhark.mocreatures.init.MoCEntities.CLOWNFISH.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("GoldFish", drzhark.mocreatures.init.MoCEntities.GOLDFISH.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_river")}, new TagKey[0]);
        addEntityData("HippoTang", drzhark.mocreatures.init.MoCEntities.HIPPOTANG.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("Manderin", drzhark.mocreatures.init.MoCEntities.MANDERIN.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        
        addEntityData("Cod", drzhark.mocreatures.init.MoCEntities.COD.get(), MobCategory.WATER_CREATURE, 10, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("minecraft:is_ocean")}, new TagKey[0]);
        addEntityData("Salmon", drzhark.mocreatures.init.MoCEntities.SALMON.get(), MobCategory.WATER_CREATURE, 10, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("c:is_aquatic"), createBiomeTag.apply("minecraft:is_ocean"), 
                createBiomeTag.apply("minecraft:is_river"), createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        addEntityData("Piranha", drzhark.mocreatures.init.MoCEntities.PIRANHA.get(), MobCategory.WATER_CREATURE, 4, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_lush")}, new TagKey[0]);
        addEntityData("Fishy", drzhark.mocreatures.init.MoCEntities.FISHY.get(), MobCategory.WATER_AMBIENT, 12, 1, 6, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach"), createBiomeTag.apply("c:is_aquatic"), createBiomeTag.apply("minecraft:is_ocean"), 
                createBiomeTag.apply("minecraft:is_river"), createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("c:is_plains")}, new TagKey[0]);
        
        // Ambient creatures
        addEntityData("Ant", drzhark.mocreatures.init.MoCEntities.ANT.get(), MobCategory.AMBIENT, 12, 1, 4, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("minecraft:is_badlands"), 
                createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("c:is_hot"), 
                createBiomeTag.apply("c:is_dry"), createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("c:is_sparse_vegetation"), createBiomeTag.apply("c:is_mountain")}, new TagKey[0]);
        addEntityData("Bee", drzhark.mocreatures.init.MoCEntities.BEE.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        addEntityData("Butterfly", drzhark.mocreatures.init.MoCEntities.BUTTERFLY.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("minecraft:is_savanna")}, new TagKey[0]);
        addEntityData("Cricket", drzhark.mocreatures.init.MoCEntities.CRICKET.get(), MobCategory.AMBIENT, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("c:is_swamp")}, new TagKey[0]);
        addEntityData("Dragonfly", drzhark.mocreatures.init.MoCEntities.DRAGONFLY.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), 
                createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        addEntityData("Firefly", drzhark.mocreatures.init.MoCEntities.FIREFLY.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), 
                createBiomeTag.apply("c:is_lush"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        addEntityData("Fly", drzhark.mocreatures.init.MoCEntities.FLY.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("c:is_plains"), createBiomeTag.apply("minecraft:is_forest")}, new TagKey[0]);
        addEntityData("Grasshopper", drzhark.mocreatures.init.MoCEntities.GRASSHOPPER.get(), MobCategory.AMBIENT, 10, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_forest"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_plains"), 
                createBiomeTag.apply("minecraft:is_savanna"), createBiomeTag.apply("mocreatures:is_wyvern_lair")}, new TagKey[0]);
        addEntityData("Maggot", drzhark.mocreatures.init.MoCEntities.MAGGOT.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky")}, new TagKey[0]);
        addEntityData("Roach", drzhark.mocreatures.init.MoCEntities.ROACH.get(), MobCategory.AMBIENT, 6, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_dead"), createBiomeTag.apply("c:is_spooky"), createBiomeTag.apply("c:is_hot")}, new TagKey[0]);
        
        addEntityData("Crab", drzhark.mocreatures.init.MoCEntities.CRAB.get(), MobCategory.AMBIENT, 6, 1, 2, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("minecraft:is_beach")}, new TagKey[0]);
        addEntityData("Snail", drzhark.mocreatures.init.MoCEntities.SNAIL.get(), MobCategory.AMBIENT, 8, 1, 3, overworldDimensions,
                new TagKey[]{createBiomeTag.apply("c:is_swamp"), createBiomeTag.apply("minecraft:is_jungle"), createBiomeTag.apply("c:is_lush")}, new TagKey[0]);
    }

    /**
     * Helper method to add entity data to both maps
     */
    private void addEntityData(String name, net.minecraft.world.entity.EntityType<?> entityType, MobCategory category, 
            int weight, int minCount, int maxCount, ResourceKey<Level>[] dimensions, TagKey<Biome>... biomeTags) {
        addEntityData(name, entityType, category, weight, minCount, maxCount, dimensions, biomeTags, new TagKey[0]);
    }
    
    /**
     * Helper method to add entity data to both maps with blocked biome tags
     */
    private void addEntityData(String name, net.minecraft.world.entity.EntityType<?> entityType, MobCategory category, 
            int weight, int minCount, int maxCount, ResourceKey<Level>[] dimensions, TagKey<Biome>[] biomeTags, TagKey<Biome>[] blockedBiomeTags) {
        
        // Filter out null biome tags
        List<TagKey<Biome>> validBiomeTags = new ArrayList<>();
        for (TagKey<Biome> tag : biomeTags) {
            if (tag != null) {
                validBiomeTags.add(tag);
            }
        }
        
        // Filter out null blocked biome tags
        List<TagKey<Biome>> validBlockedBiomeTags = new ArrayList<>();
        for (TagKey<Biome> tag : blockedBiomeTags) {
            if (tag != null) {
                validBlockedBiomeTags.add(tag);
            }
        }
        
        // Create SpawnerData
        net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData spawnerData = 
            new net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData(entityType, net.minecraft.util.valueproviders.UniformInt.of(minCount, maxCount));
        
        // Create MoCEntityData
        drzhark.mocreatures.entity.MoCEntityData entityData = new drzhark.mocreatures.entity.MoCEntityData(
            name, 4, dimensions, category, spawnerData, weight, validBiomeTags);
        
        // Set blocked biome tags
        entityData.setBlockedBiomeTags(validBlockedBiomeTags);
        
        // Add to both maps
        MoCreatures.mocEntityMap.put(name, entityData);
        MoCreatures.entityMap.put(entityType, entityData);
    }

    // deprecated
    public void readMocConfigValues() {
        // First ensure the entity map is initialized
        if (MoCreatures.mocEntityMap == null || MoCreatures.mocEntityMap.isEmpty()) {
            initializeMocEntityMap();
        }
        
        if (MoCreatures.mocEntityMap != null && !MoCreatures.mocEntityMap.isEmpty()) {
            for (MoCEntityData entityData : MoCreatures.mocEntityMap.values()) {
                MoCConfigCategory cat = this.mocEntityConfig.getCategory(entityData.getEntityName().toLowerCase());
                if (!cat.containsKey("biomeTypes")) {
                    cat.put("biomeTypes", new MoCProperty("biomeTypes", Arrays.toString(entityData.getBiomeTags().toArray()), MoCProperty.Type.STRING));
                } else {
                    // Properly parse the biome types from config without corrupting the format
                    String rawValue = cat.get("biomeTypes").value;
                    String[] biomeStrings = parseConfigArray(rawValue);
                    entityData.setBiomeTags(parseBiomeTypes(biomeStrings));
                }
                if (!cat.containsKey("blockedBiomeTypes")) {
                    cat.put("blockedBiomeTypes", new MoCProperty("blockedBiomeTypes", Arrays.toString(entityData.getBlockedBiomeTags().toArray()), MoCProperty.Type.STRING));
                } else {
                    // Properly parse the blocked biome types from config without corrupting the format
                    String rawValue = cat.get("blockedBiomeTypes").value;
                    String[] biomeStrings = parseConfigArray(rawValue);
                    entityData.setBlockedBiomeTags(parseBiomeTypes(biomeStrings));
                }
                if (!cat.containsKey("canSpawn")) {
                    cat.put("canSpawn", new MoCProperty("canSpawn", Boolean.toString(entityData.getCanSpawn()), MoCProperty.Type.STRING));
                } else {
                    entityData.setCanSpawn(Boolean.parseBoolean(cat.get("canSpawn").value));
                }
                if (!cat.containsKey("frequency")) {
                    cat.put("frequency", new MoCProperty("frequency", Integer.toString(entityData.getFrequency()), MoCProperty.Type.INTEGER));
                } else {
                    entityData.setFrequency(Integer.parseInt(cat.get("frequency").value));
                }
                if (!cat.containsKey("maxSpawn")) {
                    cat.put("maxSpawn", new MoCProperty("maxSpawn", Integer.toString(entityData.getMaxSpawn()), MoCProperty.Type.INTEGER));
                } else {
                    entityData.setMaxSpawn(Integer.parseInt(cat.get("maxSpawn").value));
                }
                if (!cat.containsKey("minSpawn")) {
                    cat.put("minSpawn", new MoCProperty("minSpawn", Integer.toString(entityData.getMinSpawn()), MoCProperty.Type.INTEGER));
                } else {
                    entityData.setMinSpawn(Integer.parseInt(cat.get("minSpawn").value));
                }
            }
        }
        this.mocEntityConfig.save();
    }

    /**
     * Reads values from file
     */
    public void readGlobalConfigValues() {
        // Client side only
        this.animateTextures = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "AnimateTextures", true, "Enables animated textures.").getBoolean(true);
        this.displayPetHealth = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "DisplayPetHealth", true, "Shows the health of pets.").getBoolean(true);
        this.displayPetIcons = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "DisplayPetIcons", true, "Shows the emotes of pets.").getBoolean(true);
        this.displayPetName = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "DisplayPetName", true, "Shows the name of pets.").getBoolean(true);

        // General
        this.alphaWraithEyes = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "AlphaWraithEyes", false, "Enables different eye colors for wraiths and flame wraiths like in alpha versions.").getBoolean(false);
        this.alwaysNamePets = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "AlwaysNamePets", true, "Displays a GUI to name a pet when taming.").getBoolean(true);
        this.armorSetEffects = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "ArmorSetEffects", true, "Applies potion effects when wearing full scorpion armor sets.").getBoolean(true);
        this.attackHorses = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "AttackHorses", false, "When enabled, tamed horses can be attacked.").getBoolean(false);
        this.attackWolves = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "AttackWolves", false, "When enabled, tamed wolves can be attacked.").getBoolean(false);
        this.attackDolphins = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "AttackDolphins", false, "When enabled, sharks will attack dolphins.").getBoolean(false);
        this.debug = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "DebugMode", false, "When enabled, enables debugging logs.").getBoolean(false);
        this.destroyDrops = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "DestroyDrops", true, "When enabled, if tamed entities find drops, they will destroy them.").getBoolean(true);
        this.easterEggs = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "EasterEggs", false, "Spawns certain entities on Easter.").getBoolean(false);
        this.easyHorseBreeding = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "EasyHorseBreeding", false, "Makes breeding horses easier with one click.").getBoolean(false);
        this.originalHorseBreeding = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "OriginalHorseBreeding", false, "If set to false, horses take about 15 seconds to breed. If set to true, horses must be left for half a Minecraft day (about 10 minutes) after being fed the proper items. Pegasus and unicorns take about a whole Minecraft day.").getBoolean(false);
        this.elephantBulldozer = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "ElephantBulldozer", true, "When enabled, elephants will destroy all non-solid blocks.").getBoolean(true);
        this.enableHunters = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "EnableHunters", true, "When disabled, hunters will not attack your pets.").getBoolean(true);
        this.enableOwnership = this.mocSettingsConfig.get(CATEGORY_OWNERSHIP_SETTINGS, "EnableOwnership", true, "Enables ownership for other players.").getBoolean(true);
        this.enableResetOwnership = this.mocSettingsConfig.get(CATEGORY_OWNERSHIP_SETTINGS, "EnableResetOwnership", true, "Reset ownership when loading animal from chunk.").getBoolean(true);
        this.filchLizardSpawnItemChance = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "FilchLizardSpawnItemChance", 50, "Percentage chance of a filch lizard to steal a dropped item.").getInt(50);
        this.foggyWyvernLair = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "FoggyWyvernLair", true, "Enables fog in the Wyvern Lair dimension.").getBoolean(true);
        this.golemDestroyBlocks = this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "GolemDestroyBlocks", true, "When enabled, golems will destroy blocks.").getBoolean(true);
        this.kittyVillageChance = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "KittyVillageChance", 25, "Percentage chance of a kitty to spawn in a village.").getInt(25);
        this.legacyBigCatModels = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "LegacyBigCatModels", false, "Use the old big cat model when true.").getBoolean(false);
        this.legacyBunnyTextures = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "LegacyBunnyTextures", false, "Use the old bunny textures when true.").getBoolean(false);
        this.legacyRatDeathSound = this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "LegacyRatDeathSound", false, "Use the old rat death sound when true.").getBoolean(false);
        this.legacyWerehumanSounds = this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "LegacyWerehumanSounds", false, "Use the old werehuman sounds when true.").getBoolean(false);
        this.legacyWraithSounds = this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "LegacyWraithSounds", false, "Use the old wraith sounds when true.").getBoolean(false);
        this.legacyWyvernLairSky = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "LegacyWyvernLairSky", false, "Use the old wyvern lair sky when true.").getBoolean(false);
        this.maxOPTamed = this.mocSettingsConfig.get(CATEGORY_OWNERSHIP_SETTINGS, "MaxOPTamed", 15, "Max amount of pets an OP player can own.").getInt(15);
        this.maxTamed = this.mocSettingsConfig.get(CATEGORY_OWNERSHIP_SETTINGS, "MaxTamed", 10, "Max amount of pets a player can own.").getInt(10);
        this.motherWyvernEggDropChance = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "MotherWyvernEggDropChance", 10, "Percentage chance of a mother wyvern to drop an egg when killed.").getInt(10);
        this.ogreAttackRange = (short) this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "OgreAttackRange", 12, "Maximum block distance for ogres to destroy.").getInt(12);
        this.ogreCaveStrength = (float) this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "OgreCaveStrength", 2.5D, "Strength value for cave ogres.").getDouble(2.5D);
        this.ogreFireStrength = (float) this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "OgreFireStrength", 3.0D, "Strength value for fire ogres.").getDouble(3.0D);
        this.ogreStrength = (float) this.mocSettingsConfig.get(CATEGORY_MOC_MONSTER_GENERAL_SETTINGS, "OgreStrength", 2.0D, "Strength value for ogres.").getDouble(2.0D);
        this.ostrichEggDropChance = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "OstrichEggDropChance", 10, "Percentage chance of a ostrich to drop an egg when killed.").getInt(10);
        this.particleFX = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "ParticleFX", 3, "Particle FX. 0 = off, 1 = minimal, 2 = normal, 3 = maximal.").getInt(3);
        this.rareItemDropChance = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "RareItemDropChance", 25, "Percentage chance of dropping a rare item.").getInt(25);
        this.spawnMultiplier = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "SpawnMultiplier", 1.0D, "Multiplier for spawn frequency.").getDouble(1.0D);
        MoCreatures.LOGGER.info("Spawn multiplier set to: {}", this.spawnMultiplier);
        this.staticBed = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "StaticBed", false, "When enabled, kitty beds cannot be pushed.").getBoolean(false);
        this.staticLitter = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "StaticLitter", false, "When enabled, litter boxes cannot be pushed.").getBoolean(false);
        this.verboseEntityNames = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "VerboseEntityNames", false, "Enables displaying a verbose name of type on pets.").getBoolean(false);
        this.weaponEffects = this.mocSettingsConfig.get(CATEGORY_MOC_GENERAL_SETTINGS, "WeaponEffects", true, "Applies weapon effects when attacking with silver weapons.").getBoolean(true);
        this.wyvernEggDropChance = this.mocSettingsConfig.get(CATEGORY_MOC_CREATURE_GENERAL_SETTINGS, "WyvernEggDropChance", 5, "Percentage chance of a wyvern to drop an egg when killed.").getInt(5);
        this.wyvernDimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse("mocreatures:wyvernlairworld"));

        // Custom ID settings
        this.allowInstaSpawn = this.mocSettingsConfig.get(CATEGORY_MOC_ID_SETTINGS, "AllowInstaSpawn", false, "Used for debugging purposes.").getBoolean(false);

        if (this.debug) {
            MoCreatures.LOGGER.info("Settings loaded.");
        }
        this.mocSettingsConfig.save();
    }

    public void registerRenderers() {
    }

    public void registerRenderInformation() {
    }

    public int getProxyMode() {
        return 0;
    }

    public void setName(Player player, IMoCEntity mocanimal) {
    }
}
