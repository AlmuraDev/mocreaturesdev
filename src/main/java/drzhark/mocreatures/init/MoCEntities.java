/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.resources.ResourceKey;

import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.*;
import drzhark.mocreatures.entity.ambient.*;
import drzhark.mocreatures.entity.aquatic.*;
import drzhark.mocreatures.entity.hostile.*;
import drzhark.mocreatures.entity.hunter.MoCEntitySnake;
import drzhark.mocreatures.entity.hunter.*;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import drzhark.mocreatures.entity.item.MoCEntityLitterBox;
import drzhark.mocreatures.entity.item.MoCEntityThrowableRock;
import drzhark.mocreatures.entity.neutral.MoCEntityBoar;
import drzhark.mocreatures.entity.neutral.*;
import drzhark.mocreatures.entity.passive.*;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.*;
import java.util.function.Supplier;

/**
 * Registry for Mo' Creatures entities
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID)
public class MoCEntities {

    /**
     * Registry for entity types
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
            .create(BuiltInRegistries.ENTITY_TYPE, MoCConstants.MOD_ID);

    /**
     * Map of entity attributes to be registered later
     */
    private static final Map<DeferredHolder<EntityType<?>, ? extends EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> ENTITY_ATTRIBUTES = new HashMap<>();

    /**
     * Helper method to register an entity with proper attributes
     */
    @SuppressWarnings("removal")
    private static <T extends LivingEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(
            String name,
            EntityType.EntityFactory<T> factory,
            MobCategory category,
            float width,
            float height,
            Supplier<AttributeSupplier.Builder> attributes,
            int primaryColor,
            int secondaryColor) {

        DeferredHolder<EntityType<?>, EntityType<T>> entityType = ENTITY_TYPES.register(name.toLowerCase(),
                () -> EntityType.Builder.of(factory, category)
                        .sized(width, height)
                        .clientTrackingRange(80)
                        .updateInterval(3)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name.toLowerCase()))));

        // Store attributes for later registration
        ENTITY_ATTRIBUTES.put(entityType, attributes);

        return entityType;
    }

    /**
     * Helper method to register an entity without spawn egg
     */
    @SuppressWarnings("removal")
    private static <T extends LivingEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(
            String name,
            EntityType.EntityFactory<T> factory,
            MobCategory category,
            float width,
            float height,
            Supplier<AttributeSupplier.Builder> attributes) {

        DeferredHolder<EntityType<?>, EntityType<T>> entityType = ENTITY_TYPES.register(name.toLowerCase(),
                () -> EntityType.Builder.of(factory, category)
                        .sized(width, height)
                        .clientTrackingRange(80)
                        .updateInterval(3)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name.toLowerCase()))));

        // Store attributes
        ENTITY_ATTRIBUTES.put(entityType, attributes);

        return entityType;
    }

    /**
     * Animal entities
     */
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBird>> BIRD = registerEntity("bird", MoCEntityBird::new,
            MobCategory.CREATURE, 0.5F, 0.9F, MoCEntityBird::createAttributes, 37109, 4609629);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBlackBear>> BLACK_BEAR = registerEntity("blackbear",
            MoCEntityBlackBear::new, MobCategory.CREATURE, 0.85F, 1.175F, MoCEntityBlackBear::createAttributes, 986897,
            8609347);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBoar>> BOAR = registerEntity("boar", MoCEntityBoar::new,
            MobCategory.CREATURE, 0.9F, 0.9F, MoCEntityBoar::createAttributes, 2037783, 4995892);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBunny>> BUNNY = registerEntity("bunny", MoCEntityBunny::new,
            MobCategory.CREATURE, 0.5F, 0.5F, MoCEntityBunny::createAttributes, 8741934, 14527570);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCrocodile>> CROCODILE = registerEntity("crocodile",
            MoCEntityCrocodile::new, MobCategory.CREATURE, 0.9F, 0.5F, MoCEntityCrocodile::createAttributes, 2698525,
            10720356);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDuck>> DUCK = registerEntity("duck", MoCEntityDuck::new,
            MobCategory.CREATURE, 0.4F, 0.7F, MoCEntityDuck::createAttributes, 3161353, 14011565);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDeer>> DEER = registerEntity("deer", MoCEntityDeer::new,
            MobCategory.CREATURE, 0.9F, 1.425F, MoCEntityDeer::createAttributes, 11572843, 13752020);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityElephant>> ELEPHANT = registerEntity("elephant",
            MoCEntityElephant::new, MobCategory.CREATURE, 1.1F, 3F, MoCEntityElephant::createAttributes, 4274216,
            9337176);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityEnt>> ENT = registerEntity("ent", MoCEntityEnt::new,
            MobCategory.CREATURE, 1.4F, 7F, MoCEntityEnt::createAttributes, 9794886, 5800509);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFilchLizard>> FILCH_LIZARD = registerEntity("filchlizard",
            MoCEntityFilchLizard::new, MobCategory.CREATURE, 0.6f, 0.5f, MoCEntityFilchLizard::createAttributes,
            9930060, 5580310);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFox>> FOX = registerEntity("fox", MoCEntityFox::new,
            MobCategory.CREATURE, 0.7F, 0.85F, MoCEntityFox::createAttributes, 15966491, 4009236);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGoat>> GOAT = registerEntity("goat", MoCEntityGoat::new,
            MobCategory.CREATURE, 0.8F, 0.9F, MoCEntityGoat::createAttributes, 15262682, 4404517);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGrizzlyBear>> GRIZZLY_BEAR = registerEntity("grizzlybear",
            MoCEntityGrizzlyBear::new, MobCategory.CREATURE, 1.125F, 1.57F, MoCEntityGrizzlyBear::createAttributes,
            3547151, 11371099);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityKitty>> KITTY = registerEntity("kitty", MoCEntityKitty::new,
            MobCategory.CREATURE, 0.8F, 0.8F, MoCEntityKitty::createAttributes, 16707009, 14861419);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityKomodo>> KOMODO_DRAGON = registerEntity("komododragon",
            MoCEntityKomodo::new, MobCategory.CREATURE, 1.25F, 0.9F, MoCEntityKomodo::createAttributes, 8615512,
            3025185);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLeoger>> LEOGER = registerEntity("leoger",
            MoCEntityLeoger::new, MobCategory.CREATURE, 1.3F, 1.3815F, MoCEntityLeoger::createAttributes, 13274957,
            6638124);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLeopard>> LEOPARD = registerEntity("leopard",
            MoCEntityLeopard::new, MobCategory.CREATURE, 1.165F, 1.01F, MoCEntityLeopard::createAttributes, 13478009,
            3682085);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLiard>> LIARD = registerEntity("liard", MoCEntityLiard::new,
            MobCategory.CREATURE, 1.175F, 1.065F, MoCEntityLiard::createAttributes, 11965543, 8215850);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLion>> LION = registerEntity("lion", MoCEntityLion::new,
            MobCategory.CREATURE, 1.25F, 1.275F, MoCEntityLion::createAttributes, 11503958, 2234383);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLiger>> LIGER = registerEntity("liger", MoCEntityLiger::new,
            MobCategory.CREATURE, 1.35F, 1.43525F, MoCEntityLiger::createAttributes, 13347170, 9068088);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLither>> LITHER = registerEntity("lither",
            MoCEntityLither::new, MobCategory.CREATURE, 1.175F, 1.17F, MoCEntityLither::createAttributes, 2234897,
            7821878);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityManticorePet>> MANTICORE_PET = registerEntity("manticorepet",
            MoCEntityManticorePet::new, MobCategory.CREATURE, 1.4F, 1.3F, MoCEntityManticorePet::createAttributes);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMole>> MOLE = registerEntity("mole", MoCEntityMole::new,
            MobCategory.CREATURE, 1F, 0.5F, MoCEntityMole::createAttributes, 263173, 10646113);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMouse>> MOUSE = registerEntity("mouse", MoCEntityMouse::new,
            MobCategory.CREATURE, 0.45F, 0.3F, MoCEntityMouse::createAttributes, 7428164, 15510186);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityOstrich>> OSTRICH = registerEntity("ostrich",
            MoCEntityOstrich::new, MobCategory.CREATURE, 0.8F, 2.225F, MoCEntityOstrich::createAttributes, 12884106,
            10646377);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPandaBear>> PANDA_BEAR = registerEntity("pandabear",
            MoCEntityPandaBear::new, MobCategory.CREATURE, 0.8F, 1.05F, MoCEntityPandaBear::createAttributes, 13354393,
            789516);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPanthard>> PANTHARD = registerEntity("panthard",
            MoCEntityPanthard::new, MobCategory.CREATURE, 1.14F, 1.063175F, MoCEntityPanthard::createAttributes, 591108,
            9005068);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPanther>> PANTHER = registerEntity("panther",
            MoCEntityPanther::new, MobCategory.CREATURE, 1.175F, 1.065F, MoCEntityPanther::createAttributes, 1709584,
            16768078);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPanthger>> PANTHGER = registerEntity("panthger",
            MoCEntityPanthger::new, MobCategory.CREATURE, 1.225F, 1.2225F, MoCEntityPanthger::createAttributes, 2826517,
            14348086);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPetScorpion>> PET_SCORPION = registerEntity("petscorpion",
            MoCEntityPetScorpion::new, MobCategory.CREATURE, 1.4F, 0.9F, MoCEntityPetScorpion::createAttributes);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPolarBear>> POLAR_BEAR = registerEntity("wildpolarbear",
            MoCEntityPolarBear::new, MobCategory.CREATURE, 1.5F, 1.834F, MoCEntityPolarBear::createAttributes, 15131867,
            11380879);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityRaccoon>> RACCOON = registerEntity("raccoon",
            MoCEntityRaccoon::new, MobCategory.CREATURE, 0.6F, 0.525F, MoCEntityRaccoon::createAttributes, 6115913,
            1578001);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntitySnake>> SNAKE = registerEntity("snake", MoCEntitySnake::new,
            MobCategory.CREATURE, 1.4F, 0.5F, MoCEntitySnake::createAttributes, 670976, 11309312);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityTiger>> TIGER = registerEntity("tiger", MoCEntityTiger::new,
            MobCategory.CREATURE, 1.25F, 1.275F, MoCEntityTiger::createAttributes, 12476160, 2956299);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityTurtle>> TURTLE = registerEntity("turtle",
            MoCEntityTurtle::new, MobCategory.CREATURE, 0.6F, 0.425F, MoCEntityTurtle::createAttributes, 6505237,
            10524955);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityTurkey>> TURKEY = registerEntity("turkey",
            MoCEntityTurkey::new, MobCategory.CREATURE, 0.6F, 0.9F, MoCEntityTurkey::createAttributes, 12268098,
            6991322);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityHorse>> WILDHORSE = registerEntity("wildhorse",
            MoCEntityHorse::new, MobCategory.CREATURE, 1.3964844F, 1.6F, MoCEntityHorse::createAttributes, 9204829,
            11379712);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityWyvern>> WYVERN = registerEntity("wyvern",
            MoCEntityWyvern::new, MobCategory.CREATURE, 1.45F, 1.55F, MoCEntityWyvern::registerAttributes, 11440923,
            15526339);

    /**
     * Monster entities
     */
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCaveOgre>> CAVE_OGRE = registerEntity("caveogre",
            MoCEntityCaveOgre::new, MobCategory.MONSTER, 1.8F, 3.05F, MoCEntityCaveOgre::createAttributes, 5079480,
            12581631);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFireOgre>> FIRE_OGRE = registerEntity("fireogre",
            MoCEntityFireOgre::new, MobCategory.MONSTER, 1.8F, 3.05F, MoCEntityFireOgre::createAttributes, 9440256,
            14336256);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFlameWraith>> FLAME_WRAITH = registerEntity("flamewraith",
            MoCEntityFlameWraith::new, MobCategory.MONSTER, 0.6F, 1.3F, MoCEntityFlameWraith::createAttributes,
            16777216, 16744064);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGolem>> BIG_GOLEM = registerEntity("biggolem",
            MoCEntityGolem::new, MobCategory.MONSTER, 1.8F, 4.3F, MoCEntityGolem::createAttributes, 4868682, 52411);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGreenOgre>> GREEN_OGRE = registerEntity("greenogre",
            MoCEntityGreenOgre::new, MobCategory.MONSTER, 1.8F, 3.05F, MoCEntityGreenOgre::createAttributes, 3355443,
            6553856);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityHorseMob>> HORSE_MOB = registerEntity("horsemob",
            MoCEntityHorseMob::new, MobCategory.MONSTER, 1.3964844F, 1.6F, MoCEntityHorseMob::createAttributes, 2763306,
            10579012);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityHellRat>> HELL_RAT = registerEntity("hellrat",
            MoCEntityHellRat::new, MobCategory.MONSTER, 0.7F, 0.65F, MoCEntityHellRat::createAttributes, 5064201,
            10354944);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityManticore>> MANTICORE = registerEntity("manticore",
            MoCEntityManticore::new, MobCategory.MONSTER, 1.4F, 1.3F, MoCEntityManticore::createAttributes, 7954176,
            3355443);
    // Manticore variants
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDarkManticore>> DARK_MANTICORE = registerEntity(
            "darkmanticore", MoCEntityDarkManticore::new, MobCategory.MONSTER, 1.35F, 1.45F,
            MoCEntityDarkManticore::createAttributes, 3289650, 657930);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFireManticore>> FIRE_MANTICORE = registerEntity(
            "firemanticore", MoCEntityFireManticore::new, MobCategory.MONSTER, 1.35F, 1.45F,
            MoCEntityFireManticore::createAttributes, 7148552, 2819585);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFrostManticore>> FROST_MANTICORE = registerEntity(
            "frostmanticore", MoCEntityFrostManticore::new, MobCategory.MONSTER, 1.35F, 1.45F,
            MoCEntityFrostManticore::createAttributes, 3559006, 2041389);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPlainManticore>> PLAIN_MANTICORE = registerEntity(
            "plainmanticore", MoCEntityPlainManticore::new, MobCategory.MONSTER, 1.35F, 1.45F,
            MoCEntityPlainManticore::createAttributes, 7623465, 5510656);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityToxicManticore>> TOXIC_MANTICORE = registerEntity(
            "toxicmanticore", MoCEntityToxicManticore::new, MobCategory.MONSTER, 1.35F, 1.45F,
            MoCEntityToxicManticore::createAttributes, 6252034, 3365689);

    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMiniGolem>> MINI_GOLEM = registerEntity("minigolem",
            MoCEntityMiniGolem::new, MobCategory.MONSTER, 0.6F, 1.3F, MoCEntityMiniGolem::createAttributes, 4734789,
            12763842);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityOgre>> OGRE = registerEntity("ogre", MoCEntityOgre::new,
            MobCategory.MONSTER, 1.8F, 3.05F, MoCEntityOgre::createAttributes, 9204851, 6513507);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityRat>> RAT = registerEntity("rat", MoCEntityRat::new,
            MobCategory.MONSTER, 0.58F, 0.455F, MoCEntityRat::createAttributes, 3685435, 15838633);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCaveScorpion>> CAVE_SCORPION = registerEntity("cavescorpion",
            MoCEntityCaveScorpion::new, MobCategory.MONSTER, 1.4F, 0.9F, MoCEntityCaveScorpion::createAttributes,
            3289650, 855309);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDirtScorpion>> DIRT_SCORPION = registerEntity("dirtscorpion",
            MoCEntityDirtScorpion::new, MobCategory.MONSTER, 1.4F, 0.9F, MoCEntityDirtScorpion::createAttributes,
            6838816, 855309);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFireScorpion>> FIRE_SCORPION = registerEntity("firescorpion",
            MoCEntityFireScorpion::new, MobCategory.MONSTER, 1.4F, 0.9F, MoCEntityFireScorpion::createAttributes,
            16711680, 855309);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFrostScorpion>> FROST_SCORPION = registerEntity(
            "frostscorpion", MoCEntityFrostScorpion::new, MobCategory.MONSTER, 1.4F, 0.9F,
            MoCEntityFrostScorpion::createAttributes, 12632256, 855309);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityUndeadScorpion>> UNDEAD_SCORPION = registerEntity(
            "undeadscorpion", MoCEntityUndeadScorpion::new, MobCategory.MONSTER, 1.4F, 0.9F,
            MoCEntityUndeadScorpion::createAttributes, 1710618, 855309);

    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntitySilverSkeleton>> SILVER_SKELETON = registerEntity(
            "silverskeleton", MoCEntitySilverSkeleton::new, MobCategory.MONSTER, 0.6F, 1.95F,
            MoCEntitySilverSkeleton::createAttributes, 13882323, 15658734);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityWerewolf>> WEREWOLF = registerEntity("werewolf",
            MoCEntityWerewolf::new, MobCategory.MONSTER, 0.6F, 1.8F, MoCEntityWerewolf::createAttributes, 7631459,
            14408667);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityWraith>> WRAITH = registerEntity("wraith",
            MoCEntityWraith::new, MobCategory.MONSTER, 0.6F, 1.3F, MoCEntityWraith::createAttributes, 2764582,
            11579568);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityWWolf>> WWOLF = registerEntity("wwolf", MoCEntityWWolf::new,
            MobCategory.MONSTER, 0.8F, 0.8F, MoCEntityWWolf::createAttributes, 4868682, 9996056);

    /**
     * Aquatic entities
     */
    // Small fish
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityAnchovy>> ANCHOVY = registerEntity("anchovy",
            MoCEntityAnchovy::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes, 7039838,
            12763545);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityAngelFish>> ANGELFISH = registerEntity("angelfish",
            MoCEntityAngelFish::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes,
            12040119, 15970609);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityAngler>> ANGLER = registerEntity("angler",
            MoCEntityAngler::new, MobCategory.WATER_CREATURE, 0.5F, 1.5F, MoCEntityAquatic::createAttributes, 2961195,
            11972077);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityClownFish>> CLOWNFISH = registerEntity("clownfish",
            MoCEntityClownFish::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes,
            16439491, 15425029);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGoldFish>> GOLDFISH = registerEntity("goldfish",
            MoCEntityGoldFish::new, MobCategory.WATER_CREATURE, 0.3F, 0.2F, MoCEntityAquatic::createAttributes,
            16750848, 16776960);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityHippoTang>> HIPPOTANG = registerEntity("hippotang",
            MoCEntityHippoTang::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes,
            4280267, 12893441);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityManderin>> MANDERIN = registerEntity("manderin",
            MoCEntityManderin::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes,
            14764801, 5935359);

    // Medium fish
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBass>> BASS = registerEntity("bass", MoCEntityBass::new,
            MobCategory.WATER_CREATURE, 0.7F, 0.3F, MoCEntityAquatic::createAttributes, 5854242, 10066177);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCod>> COD = registerEntity("cod", MoCEntityCod::new,
            MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes, 8355712, 12169216);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntitySalmon>> SALMON = registerEntity("salmon",
            MoCEntitySalmon::new, MobCategory.WATER_CREATURE, 0.7F, 0.4F, MoCEntityAquatic::createAttributes, 10489616,
            951424);

    // Large aquatic
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDolphin>> DOLPHIN = registerEntity("dolphin",
            MoCEntityDolphin::new, MobCategory.WATER_CREATURE, 1.3F, 0.605F, MoCEntityAquatic::createAttributes,
            4086148, 11251396);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMantaRay>> MANTA_RAY = registerEntity("mantaray",
            MoCEntityMantaRay::new, MobCategory.WATER_CREATURE, 2.0F, 0.5F, MoCEntityAquatic::createAttributes, 592137,
            2434341);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityRay>> RAY = registerEntity("ray", MoCEntityRay::new,
            MobCategory.WATER_CREATURE, 1.45F, 0.4F, MoCEntityAquatic::createAttributes, 6770509, 13947080);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityShark>> SHARK = registerEntity("shark", MoCEntityShark::new,
            MobCategory.WATER_CREATURE, 1.65F, 0.9F, MoCEntityAquatic::createAttributes, 3817558, 11580358);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityStingRay>> STING_RAY = registerEntity("stingray",
            MoCEntityStingRay::new, MobCategory.WATER_CREATURE, 1.2F, 0.3F, MoCEntityAquatic::createAttributes, 6770509,
            13947080);

    // Other aquatic
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFishy>> FISHY = registerEntity("fishy", MoCEntityFishy::new,
            MobCategory.WATER_CREATURE, 0.3F, 0.3F, MoCEntityAquatic::createAttributes, 16684800, 13382400);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityJellyFish>> JELLYFISH = registerEntity("jellyfish",
            MoCEntityJellyFish::new, MobCategory.WATER_CREATURE, 0.9F, 0.9F, MoCEntityAquatic::createAttributes,
            3618615, 10066177);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMediumFish>> MEDIUM_FISH = registerEntity("mediumfish",
            MoCEntityMediumFish::new, MobCategory.WATER_CREATURE, 0.5F, 0.3F, MoCEntityAquatic::createAttributes,
            8355712, 12169216);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityPiranha>> PIRANHA = registerEntity("piranha",
            MoCEntityPiranha::new, MobCategory.WATER_CREATURE, 0.3F, 0.3F, MoCEntityAquatic::createAttributes, 16684800,
            13107200);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntitySmallFish>> SMALL_FISH = registerEntity("smallfish",
            MoCEntitySmallFish::new, MobCategory.WATER_CREATURE, 0.3F, 0.3F, MoCEntityAquatic::createAttributes,
            16684800, 13382400);

    /**
     * Ambient entities
     */
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityAnt>> ANT = registerEntity("ant", MoCEntityAnt::new,
            MobCategory.AMBIENT, 0.3F, 0.2F, MoCEntityAnt::createAttributes, 5915945, 2693905);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityBee>> BEE = registerEntity("bee", MoCEntityBee::new,
            MobCategory.AMBIENT, 0.4F, 0.3F, MoCEntityBee::createAttributes, 15912747, 526604);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityButterfly>> BUTTERFLY = registerEntity("butterfly",
            MoCEntityButterfly::new, MobCategory.AMBIENT, 0.5F, 0.3F, MoCEntityButterfly::createAttributes, 15912747,
            526604);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCrab>> CRAB = registerEntity("crab", MoCEntityCrab::new,
            MobCategory.AMBIENT, 0.45F, 0.3F, MoCEntityCrab::registerAttributes, 11880978, 15514213);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityCricket>> CRICKET = registerEntity("cricket",
            MoCEntityCricket::new, MobCategory.AMBIENT, 0.3F, 0.3F, MoCEntityCricket::createAttributes, 1644825,
            6513507);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityDragonfly>> DRAGONFLY = registerEntity("dragonfly",
            MoCEntityDragonfly::new, MobCategory.AMBIENT, 0.5F, 0.3F, MoCEntityDragonfly::createAttributes, 2302755,
            8224125);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFirefly>> FIREFLY = registerEntity("firefly",
            MoCEntityFirefly::new, MobCategory.AMBIENT, 0.3F, 0.3F, MoCEntityFirefly::createAttributes, 15921152,
            1315840);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityFly>> FLY = registerEntity("fly", MoCEntityFly::new,
            MobCategory.AMBIENT, 0.2F, 0.2F, MoCEntityFly::createAttributes, 1644825, 6513507);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityGrasshopper>> GRASSHOPPER = registerEntity("grasshopper",
            MoCEntityGrasshopper::new, MobCategory.AMBIENT, 0.4F, 0.3F, MoCEntityGrasshopper::createAttributes, 7830593,
            3747075);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityMaggot>> MAGGOT = registerEntity("maggot",
            MoCEntityMaggot::new, MobCategory.AMBIENT, 0.3F, 0.3F, MoCEntityMaggot::createAttributes, 14737632,
            16777215);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityRoach>> ROACH = registerEntity("roach", MoCEntityRoach::new,
            MobCategory.AMBIENT, 0.3F, 0.3F, MoCEntityRoach::createAttributes, 1644825, 6513507);
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntitySnail>> SNAIL = registerEntity("snail", MoCEntitySnail::new,
            MobCategory.AMBIENT, 0.3F, 0.3F, MoCEntitySnail::createAttributes, 8618883, 15658734);

    /**
     * Other entities (misc/items)
     */
    @SuppressWarnings("removal")
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityEgg>> EGG = ENTITY_TYPES.register("egg",
            () -> EntityType.Builder.<MoCEntityEgg>of(MoCEntityEgg::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(64)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "egg"))));

    // Store the egg attributes for registration
    static {
        ENTITY_ATTRIBUTES.put(EGG, MoCEntityEgg::createAttributes);
    }

    @SuppressWarnings("removal")
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityKittyBed>> KITTY_BED = ENTITY_TYPES.register("kittybed",
            () -> EntityType.Builder.<MoCEntityKittyBed>of(MoCEntityKittyBed::new, MobCategory.MISC)
                    .sized(1.0F, 0.15F)
                    .clientTrackingRange(64)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "kittybed"))));

    @SuppressWarnings("removal")
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityLitterBox>> LITTERBOX = ENTITY_TYPES.register("litterbox",
            () -> EntityType.Builder.<MoCEntityLitterBox>of(MoCEntityLitterBox::new, MobCategory.MISC)
                    .sized(1.0F, 0.15F)
                    .clientTrackingRange(64)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "litterbox"))));

    @SuppressWarnings("removal")
    public static final DeferredHolder<EntityType<?>, EntityType<MoCEntityThrowableRock>> TROCK = ENTITY_TYPES.register("trock",
            () -> EntityType.Builder.<MoCEntityThrowableRock>of(MoCEntityThrowableRock::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(64)
                    .updateInterval(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "trock"))));

    /**
     * Register entity attributes
     */
    /** Where and how each creature may spawn naturally. */
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Handle spawn placement here
        // ===== ANIMALS =====
        // All animals use MoCEntityAnimal::getCanSpawnHere except Kitty and Wyvern

        // Bears
        event.register(BLACK_BEAR.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GRIZZLY_BEAR.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(POLAR_BEAR.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PANDA_BEAR.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Birds - NO_RESTRICTIONS
        event.register(BIRD.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DUCK.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(OSTRICH.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TURKEY.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Herbivores
        event.register(BOAR.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BUNNY.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DEER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ELEPHANT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GOAT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(WILDHORSE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Big Cats
        event.register(LEOPARD.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(LION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PANTHER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TIGER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Hybrids
        event.register(LEOGER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(LIARD.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(LIGER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(LITHER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PANTHARD.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PANTHGER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Other Animals
        event.register(CROCODILE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ENT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FILCH_LIZARD.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FOX.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(KOMODO_DRAGON.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MOLE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MOUSE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(RACCOON.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SNAKE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TURTLE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Special Animals with their own spawn methods (matching 1.16.5 MoCEntities)
        event.register(KITTY.get(), SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        MoCEntityAnimal::checkMoCAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(WYVERN.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    // Use Wyvern's specific spawn method
                    return MoCEntityWyvern.getCanSpawnHere(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // ===== MONSTERS =====
        // Most use MoCEntityMob::getCanSpawnHere, some have specific methods

        // Ogres - Cave Ogre has specific method
        event.register(CAVE_OGRE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(FIRE_OGRE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GREEN_OGRE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Golems - Big Golem has specific method (MoCEntityGolem::getCanSpawnHere in
        // 1.16.5)
        event.register(BIG_GOLEM.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MINI_GOLEM.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Horse Mob
        event.register(HORSE_MOB.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Rats
        event.register(HELL_RAT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(RAT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Manticores - Dark Manticore has specific method
        // (MoCEntityDarkManticore::getCanSpawnHere in 1.16.5)
        event.register(DARK_MANTICORE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FIRE_MANTICORE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FROST_MANTICORE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PLAIN_MANTICORE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(TOXIC_MANTICORE.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Scorpions - Cave Scorpion has specific method
        // (MoCEntityCaveScorpion::getCanSpawnHere in 1.16.5)
        event.register(CAVE_SCORPION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DIRT_SCORPION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FIRE_SCORPION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FROST_SCORPION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(UNDEAD_SCORPION.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Other Monsters
        event.register(SILVER_SKELETON.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FLAME_WRAITH.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(WRAITH.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Werewolf has specific method (MoCEntityWerewolf::getCanSpawnHere in 1.16.5)
        event.register(WEREWOLF.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // WWolf has specific method (MoCEntityWWolf::getCanSpawnHere in 1.16.5)
        event.register(WWOLF.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnType, pos, random) -> {
                    if (spawnType == EntitySpawnReason.SPAWN_ITEM_USE)
                        return true;
                    return MoCEntityMob.checkMobSpawnRules(entityType, world, spawnType, pos, random);
                }, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // ===== AQUATIC =====
        // All use MoCEntityAquatic::getCanSpawnHere

        // Large Aquatic
        event.register(DOLPHIN.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SHARK.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MANTA_RAY.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(STING_RAY.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(JELLYFISH.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Fish
        event.register(ANCHOVY.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ANGELFISH.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ANGLER.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BASS.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CLOWNFISH.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(COD.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FISHY.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GOLDFISH.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(HIPPOTANG.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MANDERIN.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PIRANHA.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SALMON.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAquatic::checkMoCWaterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // ===== AMBIENT =====
        // All use MoCEntityAmbient::getCanSpawnHere

        // Flying insects
        event.register(BEE.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(BUTTERFLY.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(DRAGONFLY.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FIREFLY.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(FLY.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Ground insects
        event.register(ANT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(CRICKET.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(GRASSHOPPER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MAGGOT.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ROACH.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SNAIL.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MoCEntityAmbient::checkMoCAmbientSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Beach/Water ambient
        // The crab is a tameable animal, not an ambient creature; it has always used vanilla's mob rule
        event.register(CRAB.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        // Add the kitty bed and litter box entities to the attributes map
        ENTITY_ATTRIBUTES.put(KITTY_BED, MoCEntityKittyBed::createAttributes);
        ENTITY_ATTRIBUTES.put(LITTERBOX, MoCEntityLitterBox::createAttributes);

    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        ENTITY_ATTRIBUTES.forEach((entityType, attributeSupplier) -> {
            try {
                // Only register attributes if the entity type is present
                if (entityType.isBound()) {
                    AttributeSupplier attributes = attributeSupplier.get().build();
                    event.put(entityType.get(), attributes);

                    // Debug log for wyvern
                    if (entityType.equals(WYVERN)) {
                        MoCreatures.LOGGER.info("Registered Wyvern attributes: {}", attributes);
                    }
                } else {
                    MoCreatures.LOGGER.warn("Skipping attributes for entity that's not present: {}",
                            entityType.getId());
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.error("Error registering attributes for entity: {}", entityType.getId(), e);
            }
        });

        MoCreatures.LOGGER.info("Registered entity attributes for {} entities", ENTITY_ATTRIBUTES.size());
    }
}
