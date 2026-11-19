/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Citadel: sbom_xela
 */
package drzhark.mocreatures.config.biome;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.commons.lang3.tuple.Pair;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.config.biome.BiomeSpawnConfig;
import drzhark.mocreatures.config.biome.SpawnBiomeConfig;
import drzhark.mocreatures.config.biome.SpawnBiomeData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Deprecated(forRemoval = true)
public class BiomeConfig {
	// MoCreatures Animal Spawns
	
	// Bears
	public static final Pair<String, SpawnBiomeData> blackBear = Pair.of("mocreatures:black_bear_spawns", DefaultBiomes.BLACK_BEAR);
	public static final Pair<String, SpawnBiomeData> grizzlyBear = Pair.of("mocreatures:grizzly_bear_spawns", DefaultBiomes.GRIZZLY_BEAR);
	public static final Pair<String, SpawnBiomeData> polarBear = Pair.of("mocreatures:polar_bear_spawns", DefaultBiomes.POLAR_BEAR);
	public static final Pair<String, SpawnBiomeData> pandaBear = Pair.of("mocreatures:panda_bear_spawns", DefaultBiomes.PANDA_BEAR);
	
	// Birds
	public static final Pair<String, SpawnBiomeData> bird = Pair.of("mocreatures:bird_spawns", DefaultBiomes.BIRD);
	public static final Pair<String, SpawnBiomeData> duck = Pair.of("mocreatures:duck_spawns", DefaultBiomes.DUCK);
	public static final Pair<String, SpawnBiomeData> turkey = Pair.of("mocreatures:turkey_spawns", DefaultBiomes.TURKEY);
	
	// Land animals
	public static final Pair<String, SpawnBiomeData> boar = Pair.of("mocreatures:boar_spawns", DefaultBiomes.BOAR);
	public static final Pair<String, SpawnBiomeData> bunny = Pair.of("mocreatures:bunny_spawns", DefaultBiomes.BUNNY);
	public static final Pair<String, SpawnBiomeData> deer = Pair.of("mocreatures:deer_spawns", DefaultBiomes.DEER);
	public static final Pair<String, SpawnBiomeData> goat = Pair.of("mocreatures:goat_spawns", DefaultBiomes.GOAT);
	public static final Pair<String, SpawnBiomeData> kitty = Pair.of("mocreatures:kitty_spawns", DefaultBiomes.KITTY);
	
	// Semi-aquatic creatures
	public static final Pair<String, SpawnBiomeData> crocodile = Pair.of("mocreatures:crocodile_spawns", DefaultBiomes.MOC_CROCODILE);
	public static final Pair<String, SpawnBiomeData> turtle = Pair.of("mocreatures:turtle_spawns", DefaultBiomes.TURTLE);
	
	// Desert/savanna creatures
	public static final Pair<String, SpawnBiomeData> elephant = Pair.of("mocreatures:elephant_spawns", DefaultBiomes.MOC_ELEPHANT);
	public static final Pair<String, SpawnBiomeData> filchLizard = Pair.of("mocreatures:filch_lizard_spawns", DefaultBiomes.FILCH_LIZARD);
	public static final Pair<String, SpawnBiomeData> ostrich = Pair.of("mocreatures:ostrich_spawns", DefaultBiomes.OSTRICH);
	
	// Foxes
	public static final Pair<String, SpawnBiomeData> fox = Pair.of("mocreatures:fox_spawns", DefaultBiomes.MOC_FOX);
	
	// Reptiles
	public static final Pair<String, SpawnBiomeData> komodoDragon = Pair.of("mocreatures:komodo_dragon_spawns", DefaultBiomes.MOC_KOMODO_DRAGON);
	public static final Pair<String, SpawnBiomeData> snake = Pair.of("mocreatures:snake_spawns", DefaultBiomes.SNAKE);
	
	// Big cats
	public static final Pair<String, SpawnBiomeData> leopard = Pair.of("mocreatures:leopard_spawns", DefaultBiomes.LEOPARD);
	public static final Pair<String, SpawnBiomeData> lion = Pair.of("mocreatures:lion_spawns", DefaultBiomes.LION);
	public static final Pair<String, SpawnBiomeData> panther = Pair.of("mocreatures:panther_spawns", DefaultBiomes.PANTHER);
	public static final Pair<String, SpawnBiomeData> tiger = Pair.of("mocreatures:tiger_spawns", DefaultBiomes.MOC_TIGER);
	
	// Big cat hybrids
	public static final Pair<String, SpawnBiomeData> liger = Pair.of("mocreatures:liger_spawns", DefaultBiomes.LIGER);
	public static final Pair<String, SpawnBiomeData> lither = Pair.of("mocreatures:lither_spawns", DefaultBiomes.LITHER);
	public static final Pair<String, SpawnBiomeData> panthger = Pair.of("mocreatures:panthger_spawns", DefaultBiomes.PANTHGER);
	public static final Pair<String, SpawnBiomeData> panthard = Pair.of("mocreatures:panthard_spawns", DefaultBiomes.PANTHARD);
	public static final Pair<String, SpawnBiomeData> leoger = Pair.of("mocreatures:leoger_spawns", DefaultBiomes.LEOGER);
	
	// Small mammals
	public static final Pair<String, SpawnBiomeData> mouse = Pair.of("mocreatures:mouse_spawns", DefaultBiomes.MOUSE);
	public static final Pair<String, SpawnBiomeData> mole = Pair.of("mocreatures:mole_spawns", DefaultBiomes.MOLE);
	public static final Pair<String, SpawnBiomeData> raccoon = Pair.of("mocreatures:raccoon_spawns", DefaultBiomes.MOC_RACCOON);
	
	// Horses
	public static final Pair<String, SpawnBiomeData> wildHorse = Pair.of("mocreatures:wild_horse_spawns", DefaultBiomes.WILD_HORSE);
	
	// Magical creatures
	public static final Pair<String, SpawnBiomeData> ent = Pair.of("mocreatures:ent_spawns", DefaultBiomes.ENT);
	public static final Pair<String, SpawnBiomeData> wyvern = Pair.of("mocreatures:wyvern_spawns", DefaultBiomes.WYVERN);
	
	// MoCreatures Monster Spawns
	
	// Ogres
	public static final Pair<String, SpawnBiomeData> greenOgre = Pair.of("mocreatures:green_ogre_spawns", DefaultBiomes.GREEN_OGRE);
	public static final Pair<String, SpawnBiomeData> caveOgre = Pair.of("mocreatures:cave_ogre_spawns", DefaultBiomes.CAVE_OGRE);
	public static final Pair<String, SpawnBiomeData> fireOgre = Pair.of("mocreatures:fire_ogre_spawns", DefaultBiomes.FIRE_OGRE);
	
	// Scorpions
	public static final Pair<String, SpawnBiomeData> caveScorpion = Pair.of("mocreatures:cave_scorpion_spawns", DefaultBiomes.CAVE_SCORPION);
	public static final Pair<String, SpawnBiomeData> dirtScorpion = Pair.of("mocreatures:dirt_scorpion_spawns", DefaultBiomes.DIRT_SCORPION);
	public static final Pair<String, SpawnBiomeData> fireScorpion = Pair.of("mocreatures:fire_scorpion_spawns", DefaultBiomes.FIRE_SCORPION);
	public static final Pair<String, SpawnBiomeData> frostScorpion = Pair.of("mocreatures:frost_scorpion_spawns", DefaultBiomes.FROST_SCORPION);
	public static final Pair<String, SpawnBiomeData> undeadScorpion = Pair.of("mocreatures:undead_scorpion_spawns", DefaultBiomes.UNDEAD_SCORPION);
	
	// Golems
	public static final Pair<String, SpawnBiomeData> bigGolem = Pair.of("mocreatures:big_golem_spawns", DefaultBiomes.BIG_GOLEM);
	public static final Pair<String, SpawnBiomeData> miniGolem = Pair.of("mocreatures:mini_golem_spawns", DefaultBiomes.MINI_GOLEM);
	
	// Manticores
	public static final Pair<String, SpawnBiomeData> darkManticore = Pair.of("mocreatures:dark_manticore_spawns", DefaultBiomes.DARK_MANTICORE);
	public static final Pair<String, SpawnBiomeData> fireManticore = Pair.of("mocreatures:fire_manticore_spawns", DefaultBiomes.FIRE_MANTICORE);
	public static final Pair<String, SpawnBiomeData> frostManticore = Pair.of("mocreatures:frost_manticore_spawns", DefaultBiomes.FROST_MANTICORE);
	public static final Pair<String, SpawnBiomeData> plainManticore = Pair.of("mocreatures:plain_manticore_spawns", DefaultBiomes.PLAIN_MANTICORE);
	public static final Pair<String, SpawnBiomeData> toxicManticore = Pair.of("mocreatures:toxic_manticore_spawns", DefaultBiomes.TOXIC_MANTICORE);
	
	// Werewolves and variants
	public static final Pair<String, SpawnBiomeData> werewolf = Pair.of("mocreatures:werewolf_spawns", DefaultBiomes.WEREWOLF);
	public static final Pair<String, SpawnBiomeData> wwolf = Pair.of("mocreatures:wwolf_spawns", DefaultBiomes.WWOLF);
	
	// Rats
	public static final Pair<String, SpawnBiomeData> rat = Pair.of("mocreatures:rat_spawns", DefaultBiomes.RAT);
	public static final Pair<String, SpawnBiomeData> hellRat = Pair.of("mocreatures:hell_rat_spawns", DefaultBiomes.HELL_RAT);
	
	// Undead
	public static final Pair<String, SpawnBiomeData> silverSkeleton = Pair.of("mocreatures:silver_skeleton_spawns", DefaultBiomes.SILVER_SKELETON);
	
	// Wraiths
	public static final Pair<String, SpawnBiomeData> wraith = Pair.of("mocreatures:wraith_spawns", DefaultBiomes.WRAITH);
	public static final Pair<String, SpawnBiomeData> flameWraith = Pair.of("mocreatures:flame_wraith_spawns", DefaultBiomes.FLAME_WRAITH);
	
	// Nether mounts
	public static final Pair<String, SpawnBiomeData> horseMob = Pair.of("mocreatures:horse_mob_spawns", DefaultBiomes.HORSE_MOB);
	
	// MoCreatures Aquatic Spawns
	
	// Ocean creatures
	public static final Pair<String, SpawnBiomeData> dolphin = Pair.of("mocreatures:dolphin_spawns", DefaultBiomes.DOLPHIN);
	public static final Pair<String, SpawnBiomeData> shark = Pair.of("mocreatures:shark_spawns", DefaultBiomes.SHARK);
	public static final Pair<String, SpawnBiomeData> mantaRay = Pair.of("mocreatures:manta_ray_spawns", DefaultBiomes.MANTA_RAY);
	public static final Pair<String, SpawnBiomeData> jellyfish = Pair.of("mocreatures:jellyfish_spawns", DefaultBiomes.JELLYFISH);
	
	// River/swamp creatures
	public static final Pair<String, SpawnBiomeData> bass = Pair.of("mocreatures:bass_spawns", DefaultBiomes.BASS);
	public static final Pair<String, SpawnBiomeData> stingRay = Pair.of("mocreatures:sting_ray_spawns", DefaultBiomes.STING_RAY);
	
	// Small fish
	public static final Pair<String, SpawnBiomeData> anchovy = Pair.of("mocreatures:anchovy_spawns", DefaultBiomes.ANCHOVY);
	public static final Pair<String, SpawnBiomeData> angelfish = Pair.of("mocreatures:angelfish_spawns", DefaultBiomes.ANGELFISH);
	public static final Pair<String, SpawnBiomeData> angler = Pair.of("mocreatures:angler_spawns", DefaultBiomes.ANGLER);
	public static final Pair<String, SpawnBiomeData> clownfish = Pair.of("mocreatures:clownfish_spawns", DefaultBiomes.CLOWNFISH);
	public static final Pair<String, SpawnBiomeData> goldfish = Pair.of("mocreatures:goldfish_spawns", DefaultBiomes.GOLDFISH);
	public static final Pair<String, SpawnBiomeData> hippotang = Pair.of("mocreatures:hippotang_spawns", DefaultBiomes.HIPPOTANG);
	public static final Pair<String, SpawnBiomeData> manderin = Pair.of("mocreatures:manderin_spawns", DefaultBiomes.MANDERIN);
	
	// Medium fish
	public static final Pair<String, SpawnBiomeData> cod = Pair.of("mocreatures:cod_spawns", DefaultBiomes.COD);
	public static final Pair<String, SpawnBiomeData> salmon = Pair.of("mocreatures:salmon_spawns", DefaultBiomes.SALMON);
	
	// Predatory fish
	public static final Pair<String, SpawnBiomeData> piranha = Pair.of("mocreatures:piranha_spawns", DefaultBiomes.PIRANHA);
	
	// General fish
	public static final Pair<String, SpawnBiomeData> fishy = Pair.of("mocreatures:fishy_spawns", DefaultBiomes.FISHY);
	
	// MoCreatures Ambient Spawns
	
	// Insects
	public static final Pair<String, SpawnBiomeData> ant = Pair.of("mocreatures:ant_spawns", DefaultBiomes.ANT);
	public static final Pair<String, SpawnBiomeData> bee = Pair.of("mocreatures:bee_spawns", DefaultBiomes.BEE);
	public static final Pair<String, SpawnBiomeData> butterfly = Pair.of("mocreatures:butterfly_spawns", DefaultBiomes.BUTTERFLY);
	public static final Pair<String, SpawnBiomeData> cricket = Pair.of("mocreatures:cricket_spawns", DefaultBiomes.CRICKET);
	public static final Pair<String, SpawnBiomeData> dragonfly = Pair.of("mocreatures:dragonfly_spawns", DefaultBiomes.DRAGONFLY);
	public static final Pair<String, SpawnBiomeData> firefly = Pair.of("mocreatures:firefly_spawns", DefaultBiomes.FIREFLY);
	public static final Pair<String, SpawnBiomeData> fly = Pair.of("mocreatures:fly_spawns", DefaultBiomes.MOC_FLY);
	public static final Pair<String, SpawnBiomeData> grasshopper = Pair.of("mocreatures:grasshopper_spawns", DefaultBiomes.GRASSHOPPER);
	public static final Pair<String, SpawnBiomeData> maggot = Pair.of("mocreatures:maggot_spawns", DefaultBiomes.MAGGOT);
	public static final Pair<String, SpawnBiomeData> roach = Pair.of("mocreatures:roach_spawns", DefaultBiomes.ROACH);
	
	// Beach/aquatic ambient
	public static final Pair<String, SpawnBiomeData> crab = Pair.of("mocreatures:crab_spawns", DefaultBiomes.CRAB);
	public static final Pair<String, SpawnBiomeData> snail = Pair.of("mocreatures:snail_spawns", DefaultBiomes.SNAIL);

	private static boolean init = false;
	private static final Map<String, SpawnBiomeData> biomeConfigValues = new HashMap<>();

    public static void init() {
        try {
            MoCreatures.LOGGER.info("Initializing BiomeConfig...");
            
            // Initialize the new JSON-based config system
            BiomeSpawnConfig.init();
            
            // Legacy initialization for backward compatibility
            for (Field f : BiomeConfig.class.getDeclaredFields()) {
                Object obj = f.get(null);
               if(obj instanceof Pair){
				   String id = (String)((Pair) obj).getLeft();
				   SpawnBiomeData data = (SpawnBiomeData)((Pair) obj).getRight();
				   biomeConfigValues.put(id, SpawnBiomeConfig.create(Identifier.parse(id), data));
				   MoCreatures.LOGGER.debug("Registered biome config: {}", id);
               }
            }
            MoCreatures.LOGGER.info("BiomeConfig initialized with {} entries", biomeConfigValues.size());
        }catch (Exception e){
            MoCreatures.LOGGER.error("Encountered error building Mo' Creatures biome config .json files", e);
        }
		init = true;
    }

    public static boolean test(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome, Identifier name){
    	if(!init){
    		MoCreatures.LOGGER.warn("BiomeConfig test called before initialization!");
    		return false;
		}
		SpawnBiomeData data = biomeConfigValues.get(entry.getKey());
		if (data == null) {
			MoCreatures.LOGGER.warn("No biome config data found for: {}", entry.getKey());
			return false;
		}
		boolean result = data.matches(biome, name);
		if (result) {
			MoCreatures.LOGGER.info("Biome {} matches config {}", name, entry.getKey());
		}
		return result;
	}

	public static boolean test(Pair<String, SpawnBiomeData> spawns, Holder<Biome> biome) {
		return test(spawns, biome, BuiltInRegistries.BIOME.getKey(biome.value()));
	}
}