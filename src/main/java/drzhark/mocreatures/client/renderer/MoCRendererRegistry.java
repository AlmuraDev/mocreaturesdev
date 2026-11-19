/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer;

import net.neoforged.fml.common.EventBusSubscriber;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.client.model.*;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat1;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat2;
import drzhark.mocreatures.client.renderer.entity.*;
import drzhark.mocreatures.client.renderer.entity.legacy.MoCLegacyRenderBigCat;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.MoCreatures;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Handles entity renderer registration using Forge events system
 */
@EventBusSubscriber(modid = MoCConstants.MOD_ID, value = Dist.CLIENT)
public class MoCRendererRegistry {

    /**
     * Register entity renderers
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Passive creatures
        event.registerEntityRenderer(MoCEntities.BUNNY.get(), ctx ->
            new MoCRenderBunny(ctx, new MoCModelBunny<>(ctx.bakeLayer(MoCModelRegistry.BUNNY)), 0.3F));
        event.registerEntityRenderer(MoCEntities.BIRD.get(), ctx ->
            new MoCRenderBird(ctx, new MoCModelBird<>(ctx.bakeLayer(MoCModelRegistry.BIRD)), 0.3F));
        event.registerEntityRenderer(MoCEntities.TURTLE.get(), ctx ->
            new MoCRenderTurtle(ctx, new MoCModelTurtle<>(ctx.bakeLayer(MoCModelRegistry.TURTLE)), 0.4F));
        event.registerEntityRenderer(MoCEntities.MOUSE.get(), ctx ->
            new MoCRenderMouse(ctx, new MoCModelMouse<>(ctx.bakeLayer(MoCModelRegistry.MOUSE)), 0.1F));
        event.registerEntityRenderer(MoCEntities.SNAKE.get(), ctx ->
            new MoCRenderSnake(ctx, new MoCModelSnake<>(ctx.bakeLayer(MoCModelRegistry.SNAKE)), 0.0F));
        event.registerEntityRenderer(MoCEntities.TURKEY.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelTurkey<>(ctx.bakeLayer(MoCModelRegistry.TURKEY)), 0.4F));
        event.registerEntityRenderer(MoCEntities.BUTTERFLY.get(), ctx ->
            new MoCRenderButterfly(ctx, new MoCModelButterfly<>(ctx.bakeLayer(MoCModelRegistry.BUTTERFLY))));
        event.registerEntityRenderer(MoCEntities.WILDHORSE.get(), ctx ->
            new MoCRenderHorse(ctx, new MoCModelHorse<>(ctx.bakeLayer(MoCModelRegistry.HORSE))));
        event.registerEntityRenderer(MoCEntities.HORSE_MOB.get(), ctx -> 
            new MoCRenderHorseMob(ctx, new MoCModelHorseMob<>(ctx.bakeLayer(MoCModelRegistry.HORSE_MOB))));
        event.registerEntityRenderer(MoCEntities.BOAR.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelBoar<>(ctx.bakeLayer(MoCModelRegistry.BOAR)), 0.5F));
        event.registerEntityRenderer(MoCEntities.BLACK_BEAR.get(), ctx -> 
            new MoCRenderMoC(ctx, new MoCModelBear<>(ctx.bakeLayer(MoCModelRegistry.BEAR)), 0.7F));
        event.registerEntityRenderer(MoCEntities.GRIZZLY_BEAR.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelBear<>(ctx.bakeLayer(MoCModelRegistry.BEAR)), 0.7F));
        event.registerEntityRenderer(MoCEntities.PANDA_BEAR.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelBear<>(ctx.bakeLayer(MoCModelRegistry.BEAR)), 0.7F));
        event.registerEntityRenderer(MoCEntities.POLAR_BEAR.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelBear<>(ctx.bakeLayer(MoCModelRegistry.BEAR)), 0.7F));
        event.registerEntityRenderer(MoCEntities.DUCK.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelDuck<>(ctx.bakeLayer(MoCModelRegistry.DUCK)), 0.3F));
        event.registerEntityRenderer(MoCEntities.DEER.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelDeer<>(ctx.bakeLayer(MoCModelRegistry.DEER)), 0.5F));
        event.registerEntityRenderer(MoCEntities.WWOLF.get(), ctx ->
            new MoCRenderWWolf(ctx, new MoCModelWolf<>(ctx.bakeLayer(MoCModelRegistry.WOLF)), 0.7F));
        event.registerEntityRenderer(MoCEntities.WRAITH.get(), ctx ->
            new MoCRenderWraith(ctx, new MoCModelWraith<>(ctx.bakeLayer(MoCModelRegistry.WRAITH)), 0.5F));
        event.registerEntityRenderer(MoCEntities.FLAME_WRAITH.get(), ctx ->
            new MoCRenderWraith(ctx, new MoCModelWraith<>(ctx.bakeLayer(MoCModelRegistry.WRAITH)), 0.5F));
        event.registerEntityRenderer(MoCEntities.WEREWOLF.get(), ctx ->
            new MoCRenderWerewolf(ctx, 
                    new MoCModelWerehuman<>(ctx.bakeLayer(MoCModelRegistry.WEREHUMAN)), 
                    new MoCModelWerewolf<>(ctx.bakeLayer(MoCModelRegistry.WEREWOLF)), 0.7F));
        event.registerEntityRenderer(MoCEntities.FILCH_LIZARD.get(), ctx ->
            new MoCRenderFilchLizard(ctx, new MoCModelFilchLizard<>(ctx.bakeLayer(MoCModelRegistry.FILCH_LIZARD)), 0.5F));
        event.registerEntityRenderer(MoCEntities.FOX.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelFox<>(ctx.bakeLayer(MoCModelRegistry.FOX)), 0.5F));
        event.registerEntityRenderer(MoCEntities.SHARK.get(), ctx ->
            new MoCRenderShark(ctx, new MoCModelShark<>(ctx.bakeLayer(MoCModelRegistry.SHARK)), 0.6F));
        event.registerEntityRenderer(MoCEntities.DOLPHIN.get(), ctx ->
            new MoCRenderDolphin(ctx, new MoCModelDolphin<>(ctx.bakeLayer(MoCModelRegistry.DOLPHIN)), 0.6F));
        event.registerEntityRenderer(MoCEntities.FISHY.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelFishy<>(ctx.bakeLayer(MoCModelRegistry.FISHY)), 0.1F));
        event.registerEntityRenderer(MoCEntities.EGG.get(), ctx ->
            new MoCRenderEgg(ctx, new MoCModelEgg<>(ctx.bakeLayer(MoCModelRegistry.EGG)), 0.0F));
        event.registerEntityRenderer(MoCEntities.KITTY.get(), ctx ->
            new MoCRenderKitty(ctx, new MoCModelKitty<>(ctx.bakeLayer(MoCModelRegistry.KITTY)), 0.4F));
        event.registerEntityRenderer(MoCEntities.KITTY_BED.get(), ctx ->
            new MoCRenderKittyBed(ctx, 
                    new MoCModelKittyBed<>(ctx.bakeLayer(MoCModelRegistry.KITTY_BED)), 
                    new MoCModelKittyBed2<>(ctx.bakeLayer(MoCModelRegistry.KITTY_BED2)), 0.3F));
        event.registerEntityRenderer(MoCEntities.LITTERBOX.get(), ctx ->
            new MoCRenderLitterBox(ctx, new MoCModelLitterBox<>(ctx.bakeLayer(MoCModelRegistry.LITTER_BOX)), 0.3F));
        event.registerEntityRenderer(MoCEntities.RAT.get(), ctx ->
            new MoCRenderRat(ctx, new MoCModelRat<>(ctx.bakeLayer(MoCModelRegistry.RAT)), 0.2F));
        event.registerEntityRenderer(MoCEntities.HELL_RAT.get(), ctx -> 
            new MoCRenderHellRat(ctx, new MoCModelRat<>(ctx.bakeLayer(MoCModelRegistry.RAT)), 0.4F));
        event.registerEntityRenderer(MoCEntities.CAVE_SCORPION.get(), ctx ->
            new MoCRenderScorpion(ctx, new MoCModelScorpion<>(ctx.bakeLayer(MoCModelRegistry.SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.DIRT_SCORPION.get(), ctx ->
            new MoCRenderScorpion(ctx, new MoCModelScorpion<>(ctx.bakeLayer(MoCModelRegistry.SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.FIRE_SCORPION.get(), ctx ->
            new MoCRenderScorpion(ctx, new MoCModelScorpion<>(ctx.bakeLayer(MoCModelRegistry.SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.FROST_SCORPION.get(), ctx ->
            new MoCRenderScorpion(ctx, new MoCModelScorpion<>(ctx.bakeLayer(MoCModelRegistry.SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.UNDEAD_SCORPION.get(), ctx ->
            new MoCRenderScorpion(ctx, new MoCModelScorpion<>(ctx.bakeLayer(MoCModelRegistry.SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.CROCODILE.get(), ctx ->
            new MoCRenderCrocodile(ctx, new MoCModelCrocodile<>(ctx.bakeLayer(MoCModelRegistry.CROCODILE)), 0.5F));

        // Big Cats - use legacy models conditionally
        if (MoCreatures.proxy.legacyBigCatModels) {
            event.registerEntityRenderer(MoCEntities.LION.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.TIGER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LEOPARD.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LEOGER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LIARD.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LIGER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LITHER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHARD.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHGER.get(), ctx ->
                new MoCLegacyRenderBigCat(ctx, new MoCLegacyModelBigCat2<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT2)), new MoCLegacyModelBigCat1<>(ctx.bakeLayer(MoCModelRegistry.LEGACY_BIG_CAT1)), 0.5F));
        } else {
            event.registerEntityRenderer(MoCEntities.LION.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.TIGER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LEOPARD.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LEOGER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LIARD.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LIGER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.LITHER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHARD.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
            event.registerEntityRenderer(MoCEntities.PANTHGER.get(), ctx -> 
                new MoCRenderMoC(ctx, new MoCModelBigCat<>(ctx.bakeLayer(MoCModelRegistry.BIG_CAT)), 0.5F));
        }

        // Use MoCRenderMoC for all other entities to avoid missing renderer classes
        
        // Fish renderers
        event.registerEntityRenderer(MoCEntities.COD.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMediumFish<>(ctx.bakeLayer(MoCModelRegistry.MEDIUM_FISH)), 0.2F));
        event.registerEntityRenderer(MoCEntities.SALMON.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMediumFish<>(ctx.bakeLayer(MoCModelRegistry.MEDIUM_FISH)), 0.2F));
        event.registerEntityRenderer(MoCEntities.BASS.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMediumFish<>(ctx.bakeLayer(MoCModelRegistry.MEDIUM_FISH)), 0.2F));
        
        // Small fish
        event.registerEntityRenderer(MoCEntities.ANCHOVY.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.ANGELFISH.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.ANGLER.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.CLOWNFISH.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.GOLDFISH.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.HIPPOTANG.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.MANDERIN.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));
        event.registerEntityRenderer(MoCEntities.PIRANHA.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSmallFish<>(ctx.bakeLayer(MoCModelRegistry.SMALL_FISH)), 0.1F));

        // Aquatic creatures
        event.registerEntityRenderer(MoCEntities.MANTA_RAY.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelRay<>(ctx.bakeLayer(MoCModelRegistry.RAY)), 0.4F));
        event.registerEntityRenderer(MoCEntities.STING_RAY.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelRay<>(ctx.bakeLayer(MoCModelRegistry.RAY)), 0.4F));
        event.registerEntityRenderer(MoCEntities.JELLYFISH.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelJellyFish<>(ctx.bakeLayer(MoCModelRegistry.JELLYFISH)), 0.1F));
        
        // Animals
        event.registerEntityRenderer(MoCEntities.GOAT.get(), ctx ->
            new MoCRenderGoat(ctx, new MoCModelGoat<>(ctx.bakeLayer(MoCModelRegistry.GOAT)), 0.3F));
        event.registerEntityRenderer(MoCEntities.OSTRICH.get(), ctx ->
            new MoCRenderOstrich(ctx, new MoCModelOstrich<>(ctx.bakeLayer(MoCModelRegistry.OSTRICH)), 0.5F));

        // Insects - use MoCRenderInsect where available
        event.registerEntityRenderer(MoCEntities.BEE.get(), ctx ->
            new MoCRenderInsect(ctx, new MoCModelBee<>(ctx.bakeLayer(MoCModelRegistry.BEE))));
        event.registerEntityRenderer(MoCEntities.FLY.get(), ctx ->
            new MoCRenderInsect(ctx, new MoCModelFly<>(ctx.bakeLayer(MoCModelRegistry.FLY))));
        event.registerEntityRenderer(MoCEntities.DRAGONFLY.get(), ctx ->
            new MoCRenderInsect(ctx, new MoCModelDragonfly<>(ctx.bakeLayer(MoCModelRegistry.DRAGONFLY))));
        event.registerEntityRenderer(MoCEntities.FIREFLY.get(), ctx ->
            new MoCRenderFirefly(ctx, new MoCModelFirefly<>(ctx.bakeLayer(MoCModelRegistry.FIREFLY))));
        event.registerEntityRenderer(MoCEntities.CRICKET.get(), ctx ->
            new MoCRenderCricket(ctx, new MoCModelCricket<>(ctx.bakeLayer(MoCModelRegistry.CRICKET))));
        event.registerEntityRenderer(MoCEntities.GRASSHOPPER.get(), ctx ->
            new MoCRenderGrasshopper(ctx, new MoCModelGrasshopper<>(ctx.bakeLayer(MoCModelRegistry.GRASSHOPPER))));
        event.registerEntityRenderer(MoCEntities.SNAIL.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSnail<>(ctx.bakeLayer(MoCModelRegistry.SNAIL)), 0.0F));
        event.registerEntityRenderer(MoCEntities.ROACH.get(), ctx ->
            new MoCRenderInsect(ctx, new MoCModelRoach<>(ctx.bakeLayer(MoCModelRegistry.ROACH))));

        // Use MoCRenderMoC for remaining entities
        event.registerEntityRenderer(MoCEntities.BIG_GOLEM.get(), ctx ->
            new MoCRenderGolem(ctx, new MoCModelGolem<>(ctx.bakeLayer(MoCModelRegistry.BIG_GOLEM)), 0.5F));
        event.registerEntityRenderer(MoCEntities.TROCK.get(), ctx ->
            new MoCRenderTRock(ctx));
        event.registerEntityRenderer(MoCEntities.PET_SCORPION.get(), ctx ->
            new MoCRenderPetScorpion(ctx, new MoCModelPetScorpion<>(ctx.bakeLayer(MoCModelRegistry.PET_SCORPION)), 0.4F));
        event.registerEntityRenderer(MoCEntities.ELEPHANT.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelElephant<>(ctx.bakeLayer(MoCModelRegistry.ELEPHANT)), 0.7F));
        event.registerEntityRenderer(MoCEntities.KOMODO_DRAGON.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelKomodo<>(ctx.bakeLayer(MoCModelRegistry.KOMODO)), 0.3F));
        event.registerEntityRenderer(MoCEntities.WYVERN.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelWyvern<>(ctx.bakeLayer(MoCModelRegistry.WYVERN)), 0.5F));
        event.registerEntityRenderer(MoCEntities.OGRE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelOgre<>(ctx.bakeLayer(MoCModelRegistry.OGRE)), 0.6F));
        event.registerEntityRenderer(MoCEntities.GREEN_OGRE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelOgre<>(ctx.bakeLayer(MoCModelRegistry.OGRE)), 0.6F));
        event.registerEntityRenderer(MoCEntities.CAVE_OGRE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelOgre<>(ctx.bakeLayer(MoCModelRegistry.OGRE)), 0.6F));
        event.registerEntityRenderer(MoCEntities.FIRE_OGRE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelOgre<>(ctx.bakeLayer(MoCModelRegistry.OGRE)), 0.6F));
        event.registerEntityRenderer(MoCEntities.MAGGOT.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMaggot<>(ctx.bakeLayer(MoCModelRegistry.MAGGOT)), 0F));
        event.registerEntityRenderer(MoCEntities.CRAB.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelCrab<>(ctx.bakeLayer(MoCModelRegistry.CRAB)), 0.2F));
        event.registerEntityRenderer(MoCEntities.RACCOON.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelRaccoon<>(ctx.bakeLayer(MoCModelRegistry.RACCOON)), 0.4F));
        event.registerEntityRenderer(MoCEntities.MINI_GOLEM.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMiniGolem<>(ctx.bakeLayer(MoCModelRegistry.MINI_GOLEM)), 0.5F));
        event.registerEntityRenderer(MoCEntities.SILVER_SKELETON.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelSilverSkeleton<>(ctx.bakeLayer(MoCModelRegistry.SILVER_SKELETON)), 0.6F));
        event.registerEntityRenderer(MoCEntities.ANT.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelAnt<>(ctx.bakeLayer(MoCModelRegistry.ANT)), 0F));
        event.registerEntityRenderer(MoCEntities.ENT.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelEnt<>(ctx.bakeLayer(MoCModelRegistry.ENT)), 0.5F));
        event.registerEntityRenderer(MoCEntities.MOLE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelMole<>(ctx.bakeLayer(MoCModelRegistry.MOLE)), 0F));

        // Manticores, including the legacy generic summon ID.
        event.registerEntityRenderer(MoCEntities.MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.DARK_MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.FIRE_MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.FROST_MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.PLAIN_MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.TOXIC_MANTICORE.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticore<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE)), 0.7F));
        event.registerEntityRenderer(MoCEntities.MANTICORE_PET.get(), ctx ->
            new MoCRenderMoC(ctx, new MoCModelManticorePet<>(ctx.bakeLayer(MoCModelRegistry.MANTICORE_PET)), 0.7F));
    }
} 