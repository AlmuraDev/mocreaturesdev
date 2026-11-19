/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat1;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat2;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelScorpion;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelShark;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Registers all model layers for MoCreatures entities
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCModelRegistry {

    // Model layer locations for all entities
    public static final ModelLayerLocation BUNNY = createModelLayer("bunny");
    public static final ModelLayerLocation BIRD = createModelLayer("bird");
    public static final ModelLayerLocation TURTLE = createModelLayer("turtle");
    public static final ModelLayerLocation MOUSE = createModelLayer("mouse");
    public static final ModelLayerLocation SNAKE = createModelLayer("snake");
    public static final ModelLayerLocation TURKEY = createModelLayer("turkey");
    public static final ModelLayerLocation BUTTERFLY = createModelLayer("butterfly");
    public static final ModelLayerLocation HORSE = createModelLayer("horse");
    public static final ModelLayerLocation HORSE_MOB = createModelLayer("horse_mob");
    public static final ModelLayerLocation BOAR = createModelLayer("boar");
    public static final ModelLayerLocation BEAR = createModelLayer("bear");
    public static final ModelLayerLocation DUCK = createModelLayer("duck");
    public static final ModelLayerLocation DEER = createModelLayer("deer");
    public static final ModelLayerLocation WOLF = createModelLayer("wolf");
    public static final ModelLayerLocation WRAITH = createModelLayer("wraith");
    public static final ModelLayerLocation WEREHUMAN = createModelLayer("werehuman");
    public static final ModelLayerLocation WEREWOLF = createModelLayer("werewolf");
    public static final ModelLayerLocation FILCH_LIZARD = createModelLayer("filch_lizard");
    public static final ModelLayerLocation FOX = createModelLayer("fox");
    public static final ModelLayerLocation SHARK = createModelLayer("shark");
    public static final ModelLayerLocation DOLPHIN = createModelLayer("dolphin");
    public static final ModelLayerLocation FISHY = createModelLayer("fishy");
    public static final ModelLayerLocation EGG = createModelLayer("egg");
    public static final ModelLayerLocation KITTY = createModelLayer("kitty");
    public static final ModelLayerLocation KITTY_BED = createModelLayer("kitty_bed");
    public static final ModelLayerLocation KITTY_BED2 = createModelLayer("kitty_bed2");
    public static final ModelLayerLocation LITTER_BOX = createModelLayer("litter_box");
    public static final ModelLayerLocation RAT = createModelLayer("rat");
    public static final ModelLayerLocation SCORPION = createModelLayer("scorpion");
    public static final ModelLayerLocation PET_SCORPION = createModelLayer("pet_scorpion");
    public static final ModelLayerLocation CROCODILE = createModelLayer("crocodile");
    public static final ModelLayerLocation RAY = createModelLayer("ray");
    public static final ModelLayerLocation JELLYFISH = createModelLayer("jellyfish");
    public static final ModelLayerLocation GOAT = createModelLayer("goat");
    public static final ModelLayerLocation OSTRICH = createModelLayer("ostrich");
    public static final ModelLayerLocation BEE = createModelLayer("bee");
    public static final ModelLayerLocation FLY = createModelLayer("fly");
    public static final ModelLayerLocation DRAGONFLY = createModelLayer("dragonfly");
    public static final ModelLayerLocation FIREFLY = createModelLayer("firefly");
    public static final ModelLayerLocation CRICKET = createModelLayer("cricket");
    public static final ModelLayerLocation GRASSHOPPER = createModelLayer("grasshopper");
    public static final ModelLayerLocation SNAIL = createModelLayer("snail");
    public static final ModelLayerLocation BIG_GOLEM = createModelLayer("big_golem");
    public static final ModelLayerLocation ELEPHANT = createModelLayer("elephant");
    public static final ModelLayerLocation KOMODO = createModelLayer("komodo");
    public static final ModelLayerLocation WYVERN = createModelLayer("wyvern");
    public static final ModelLayerLocation OGRE = createModelLayer("ogre");
    public static final ModelLayerLocation ROACH = createModelLayer("roach");
    public static final ModelLayerLocation MAGGOT = createModelLayer("maggot");
    public static final ModelLayerLocation CRAB = createModelLayer("crab");
    public static final ModelLayerLocation RACCOON = createModelLayer("raccoon");
    public static final ModelLayerLocation MINI_GOLEM = createModelLayer("mini_golem");
    public static final ModelLayerLocation SILVER_SKELETON = createModelLayer("silver_skeleton");
    public static final ModelLayerLocation ANT = createModelLayer("ant");
    public static final ModelLayerLocation MEDIUM_FISH = createModelLayer("medium_fish");
    public static final ModelLayerLocation SMALL_FISH = createModelLayer("small_fish");
    public static final ModelLayerLocation ENT = createModelLayer("ent");
    public static final ModelLayerLocation MOLE = createModelLayer("mole");
    public static final ModelLayerLocation MANTICORE = createModelLayer("manticore");
    public static final ModelLayerLocation MANTICORE_PET = createModelLayer("manticore_pet");
    public static final ModelLayerLocation BIG_CAT = createModelLayer("big_cat");
    
    // Legacy model layers
    public static final ModelLayerLocation LEGACY_BIG_CAT1 = createModelLayer("legacy_big_cat1");
    public static final ModelLayerLocation LEGACY_BIG_CAT2 = createModelLayer("legacy_big_cat2");
    public static final ModelLayerLocation LEGACY_SCORPION = createModelLayer("legacy_scorpion");
    public static final ModelLayerLocation LEGACY_SHARK = createModelLayer("legacy_shark");

    /**
     * Create a model layer location with the MoCreatures namespace
     */
    @SuppressWarnings("removal")
    private static ModelLayerLocation createModelLayer(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name), "main");
    }

    /**
     * Register all layer definitions
     */
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Register all model layer definitions
        event.registerLayerDefinition(BUNNY, MoCModelBunny::createBodyLayer);
        event.registerLayerDefinition(BIRD, MoCModelBird::createBodyLayer);
        event.registerLayerDefinition(TURTLE, MoCModelTurtle::createBodyLayer);
        event.registerLayerDefinition(MOUSE, MoCModelMouse::createBodyLayer);
        event.registerLayerDefinition(SNAKE, MoCModelSnake::createBodyLayer);
        event.registerLayerDefinition(TURKEY, MoCModelTurkey::createBodyLayer);
        event.registerLayerDefinition(BUTTERFLY, MoCModelButterfly::createBodyLayer);
        event.registerLayerDefinition(HORSE, MoCModelHorse::createBodyLayer);
        event.registerLayerDefinition(HORSE_MOB, MoCModelHorseMob::createBodyLayer);
        event.registerLayerDefinition(BOAR, MoCModelBoar::createBodyLayer);
        event.registerLayerDefinition(BEAR, MoCModelBear::createBodyLayer);
        event.registerLayerDefinition(DUCK, MoCModelDuck::createBodyLayer);
        event.registerLayerDefinition(DEER, MoCModelDeer::createBodyLayer);
        event.registerLayerDefinition(WOLF, MoCModelWolf::createBodyLayer);
        event.registerLayerDefinition(WRAITH, MoCModelWraith::createBodyLayer);
        event.registerLayerDefinition(WEREHUMAN, MoCModelWerehuman::createBodyLayer);
        event.registerLayerDefinition(WEREWOLF, MoCModelWerewolf::createBodyLayer);
        event.registerLayerDefinition(FILCH_LIZARD, MoCModelFilchLizard::createBodyLayer);
        event.registerLayerDefinition(FOX, MoCModelFox::createBodyLayer);
        event.registerLayerDefinition(SHARK, MoCModelShark::createBodyLayer);
        event.registerLayerDefinition(DOLPHIN, MoCModelDolphin::createBodyLayer);
        event.registerLayerDefinition(FISHY, MoCModelFishy::createBodyLayer);
        event.registerLayerDefinition(EGG, MoCModelEgg::createBodyLayer);
        event.registerLayerDefinition(KITTY, MoCModelKitty::createBodyLayer);
        event.registerLayerDefinition(KITTY_BED, MoCModelKittyBed::createBodyLayer);
        event.registerLayerDefinition(KITTY_BED2, MoCModelKittyBed2::createBodyLayer);
        event.registerLayerDefinition(LITTER_BOX, MoCModelLitterBox::createBodyLayer);
        event.registerLayerDefinition(RAT, MoCModelRat::createBodyLayer);
        event.registerLayerDefinition(SCORPION, MoCModelScorpion::createBodyLayer);
        event.registerLayerDefinition(PET_SCORPION, MoCModelPetScorpion::createBodyLayer);
        event.registerLayerDefinition(CROCODILE, MoCModelCrocodile::createBodyLayer);
        event.registerLayerDefinition(RAY, MoCModelRay::createBodyLayer);
        event.registerLayerDefinition(JELLYFISH, MoCModelJellyFish::createBodyLayer);
        event.registerLayerDefinition(GOAT, MoCModelGoat::createBodyLayer);
        event.registerLayerDefinition(OSTRICH, MoCModelOstrich::createBodyLayer);
        event.registerLayerDefinition(BEE, MoCModelBee::createBodyLayer);
        event.registerLayerDefinition(FLY, MoCModelFly::createBodyLayer);
        event.registerLayerDefinition(DRAGONFLY, MoCModelDragonfly::createBodyLayer);
        event.registerLayerDefinition(FIREFLY, MoCModelFirefly::createBodyLayer);
        event.registerLayerDefinition(CRICKET, MoCModelCricket::createBodyLayer);
        event.registerLayerDefinition(GRASSHOPPER, MoCModelGrasshopper::createBodyLayer);
        event.registerLayerDefinition(SNAIL, MoCModelSnail::createBodyLayer);
        event.registerLayerDefinition(BIG_GOLEM, MoCModelGolem::createBodyLayer);
        event.registerLayerDefinition(ELEPHANT, MoCModelElephant::createBodyLayer);
        event.registerLayerDefinition(KOMODO, MoCModelKomodo::createBodyLayer);
        event.registerLayerDefinition(WYVERN, MoCModelWyvern::createBodyLayer);
        event.registerLayerDefinition(OGRE, MoCModelOgre::createBodyLayer);
        event.registerLayerDefinition(ROACH, MoCModelRoach::createBodyLayer);
        event.registerLayerDefinition(MAGGOT, MoCModelMaggot::createBodyLayer);
        event.registerLayerDefinition(CRAB, MoCModelCrab::createBodyLayer);
        event.registerLayerDefinition(RACCOON, MoCModelRaccoon::createBodyLayer);
        event.registerLayerDefinition(MINI_GOLEM, MoCModelMiniGolem::createBodyLayer);
        event.registerLayerDefinition(SILVER_SKELETON, MoCModelSilverSkeleton::createBodyLayer);
        event.registerLayerDefinition(ANT, MoCModelAnt::createBodyLayer);
        event.registerLayerDefinition(MEDIUM_FISH, MoCModelMediumFish::createBodyLayer);
        event.registerLayerDefinition(SMALL_FISH, MoCModelSmallFish::createBodyLayer);
        event.registerLayerDefinition(ENT, MoCModelEnt::createBodyLayer);
        event.registerLayerDefinition(MOLE, MoCModelMole::createBodyLayer);
        event.registerLayerDefinition(MANTICORE, MoCModelManticore::createBodyLayer);
        event.registerLayerDefinition(MANTICORE_PET, MoCModelManticorePet::createBodyLayer);
        event.registerLayerDefinition(BIG_CAT, MoCModelBigCat::createBodyLayer);
        
        // Legacy models
        event.registerLayerDefinition(LEGACY_BIG_CAT1, MoCLegacyModelBigCat1::createBodyLayer);
        event.registerLayerDefinition(LEGACY_BIG_CAT2, MoCLegacyModelBigCat2::createBodyLayer);
        event.registerLayerDefinition(LEGACY_SCORPION, MoCLegacyModelScorpion::createBodyLayer);
        event.registerLayerDefinition(LEGACY_SHARK, MoCLegacyModelShark::createBodyLayer);
    }
} 