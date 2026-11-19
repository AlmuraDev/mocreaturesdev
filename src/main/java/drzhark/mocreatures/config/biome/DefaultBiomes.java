/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Citadel: sbom_xela
 */
package drzhark.mocreatures.config.biome;

public class DefaultBiomes {

    public static final SpawnBiomeData EMPTY = new SpawnBiomeData();

    // Test biome data - using tags that definitely work in Alex's Mobs
    public static final SpawnBiomeData TEST_BASIC_TAGS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_overworld", 0);
            
    public static final SpawnBiomeData TEST_OCEAN_TAGS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0);

    // MoCreatures Biome Data
    
    // Bears
    public static final SpawnBiomeData BLACK_BEAR = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 0);
            
    public static final SpawnBiomeData GRIZZLY_BEAR = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0);
            
    public static final SpawnBiomeData POLAR_BEAR = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 0);
            
    public static final SpawnBiomeData PANDA_BEAR = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);
    
    // Birds
    public static final SpawnBiomeData BIRD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 5);
    
    // Land animals
    public static final SpawnBiomeData BOAR = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1);
            
    public static final SpawnBiomeData BUNNY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 5);

    public static final SpawnBiomeData DEER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 2);

    public static final SpawnBiomeData DUCK = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 1);
        
    // Semi-aquatic creatures
    public static final SpawnBiomeData MOC_CROCODILE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0);

    public static final SpawnBiomeData TURTLE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 1);
        
    // Desert/savanna creatures
    public static final SpawnBiomeData MOC_ELEPHANT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 3);

    public static final SpawnBiomeData FILCH_LIZARD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 3);
            
    public static final SpawnBiomeData OSTRICH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_badlands", 1);
        
    // Foxes
    public static final SpawnBiomeData MOC_FOX = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 2);

    // Hoofed animals
    public static final SpawnBiomeData GOAT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 1);

    // Village animals
    public static final SpawnBiomeData KITTY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 1);

    // Reptiles
    public static final SpawnBiomeData MOC_KOMODO_DRAGON = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 1);

    // Big cats
    public static final SpawnBiomeData LEOPARD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 2);
            
    public static final SpawnBiomeData LION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2);
            
    public static final SpawnBiomeData PANTHER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);
            
    public static final SpawnBiomeData MOC_TIGER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);

    // Big cat hybrids
    public static final SpawnBiomeData LIGER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 1);
            
    public static final SpawnBiomeData LITHER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);

    public static final SpawnBiomeData PANTHGER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);
            
    public static final SpawnBiomeData PANTHARD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0);

    public static final SpawnBiomeData LEOGER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 1);
        
    // Small mammals
    public static final SpawnBiomeData MOUSE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 3);
        
    public static final SpawnBiomeData MOLE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2);
            
    public static final SpawnBiomeData MOC_RACCOON = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0);
            
    // Snakes
    public static final SpawnBiomeData SNAKE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 5)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 6)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 7)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 8);
                
    // Farm animals
    public static final SpawnBiomeData TURKEY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1);

    // Horses
    public static final SpawnBiomeData WILD_HORSE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 1);
            
    // Magical creatures
    public static final SpawnBiomeData ENT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0);
            
    // Wyvern dimension
    public static final SpawnBiomeData WYVERN = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 0);

    // Monsters
    public static final SpawnBiomeData GREEN_OGRE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 4);

    public static final SpawnBiomeData CAVE_OGRE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 0);

    public static final SpawnBiomeData FIRE_OGRE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0);

    // Scorpions
    public static final SpawnBiomeData CAVE_SCORPION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dry/overworld", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_hot/overworld", 5)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 6)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 7);
                
    public static final SpawnBiomeData DIRT_SCORPION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dry/overworld", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_hot/overworld", 3);
            
    public static final SpawnBiomeData FIRE_SCORPION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0);
            
    public static final SpawnBiomeData FROST_SCORPION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 0);
            
    public static final SpawnBiomeData UNDEAD_SCORPION = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 1);

    // Golems
    public static final SpawnBiomeData BIG_GOLEM = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_hill", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 5);
                
    public static final SpawnBiomeData MINI_GOLEM = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 4);

    // Manticores
    public static final SpawnBiomeData DARK_MANTICORE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 5);

    public static final SpawnBiomeData FIRE_MANTICORE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0);

    public static final SpawnBiomeData FROST_MANTICORE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 0);
            
    public static final SpawnBiomeData PLAIN_MANTICORE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2);
            
    public static final SpawnBiomeData TOXIC_MANTICORE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 1);

    // Werewolves and variants
    public static final SpawnBiomeData WEREWOLF = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 1);

    public static final SpawnBiomeData WWOLF = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 2);

    // Rats
    public static final SpawnBiomeData RAT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 3);
            
    public static final SpawnBiomeData HELL_RAT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0);

    // Undead
    public static final SpawnBiomeData SILVER_SKELETON = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sandy", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_snowy", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 5);

    // Wraiths
    public static final SpawnBiomeData WRAITH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_tree/coniferous", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dense_vegetation/overworld", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 4);
                
    public static final SpawnBiomeData FLAME_WRAITH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0);

    // Nether mounts
    public static final SpawnBiomeData HORSE_MOB = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_nether", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 4);

    // Aquatic creatures
    public static final SpawnBiomeData DOLPHIN = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0);
            
    public static final SpawnBiomeData SHARK = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0);

    public static final SpawnBiomeData MANTA_RAY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0);

    // River/swamp creatures
    public static final SpawnBiomeData BASS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 3);
            
    public static final SpawnBiomeData STING_RAY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 1);

    // Fish
    public static final SpawnBiomeData ANCHOVY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 2);
            
    public static final SpawnBiomeData ANGELFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 2);

    public static final SpawnBiomeData ANGLER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1);

    public static final SpawnBiomeData CLOWNFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1);

    public static final SpawnBiomeData GOLDFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 0);

    public static final SpawnBiomeData HIPPOTANG = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1);
            
    public static final SpawnBiomeData MANDERIN = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1);

    // Medium fish
    public static final SpawnBiomeData COD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 1);

    public static final SpawnBiomeData SALMON = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_aquatic", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 5);

    // Predatory fish
    public static final SpawnBiomeData PIRANHA = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 2);

    // Other aquatic creatures
    public static final SpawnBiomeData JELLYFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0);

    // General fish
    public static final SpawnBiomeData FISHY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_aquatic", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 5);

    // Ambient/Insects
    public static final SpawnBiomeData ANT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_badlands", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 4)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_hot/overworld", 5)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dry/overworld", 6)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 7)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_sparse_vegetation/overworld", 8)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_mountain", 9);

    // Fireflies
    public static final SpawnBiomeData FIREFLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 4);

    // Crabs
    public static final SpawnBiomeData CRAB = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_beach", 0);

    // Snails
    public static final SpawnBiomeData SNAIL = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 2);

    // Flying insects
    public static final SpawnBiomeData DRAGONFLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 4);

    public static final SpawnBiomeData BUTTERFLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 4);

    public static final SpawnBiomeData BEE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_lush", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 4);

    public static final SpawnBiomeData MOC_FLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 2);

    // Cricket and Grasshopper
    public static final SpawnBiomeData CRICKET = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 3);

    public static final SpawnBiomeData GRASSHOPPER = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_forest", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_jungle", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_plains", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_savanna", 3)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "mocreatures:is_wyvern_lair", 4);

    // Maggots
    public static final SpawnBiomeData MAGGOT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_swamp", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 2);

    // Roaches
    public static final SpawnBiomeData ROACH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_dead", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_spooky", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "c:is_hot/overworld", 2);
}