/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.init;

import net.minecraft.core.registries.Registries;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MoCSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_DEFERRED = DeferredRegister.create(Registries.SOUND_EVENT, MoCConstants.MOD_ID);
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAR_AMBIENT = createSoundEvent("beargrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAR_DEATH = createSoundEvent("beardeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAR_HURT = createSoundEvent("bearhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEE_AMBIENT = createSoundEvent("bee");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEE_HURT = createSoundEvent("beehurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEE_UPSET = createSoundEvent("beeupset");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_BLACK = createSoundEvent("birdblack");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_BLUE = createSoundEvent("birdblue");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_GREEN = createSoundEvent("birdgreen");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_RED = createSoundEvent("birdred");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_YELLOW = createSoundEvent("birdyellow");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_AMBIENT_WHITE = createSoundEvent("birdwhite");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_DEATH = createSoundEvent("birddying"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_HURT = createSoundEvent("birdhurt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRICKET_AMBIENT = createSoundEvent("cricketambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRICKET_CHIRP = createSoundEvent("cricketchirp");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRICKET_HURT = createSoundEvent("crickethurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_AMBIENT = createSoundEvent("crocgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_DEATH = createSoundEvent("crocdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_HURT = createSoundEvent("crochurt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_JAWSNAP = createSoundEvent("crocjawsnap");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_RESTING = createSoundEvent("crocresting");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CROCODILE_ROLL = createSoundEvent("crocroll");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DEER_AMBIENT_BABY = createSoundEvent("deerbgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DEER_AMBIENT = createSoundEvent("deerfgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DEER_DEATH = createSoundEvent("deerdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DEER_HURT = createSoundEvent("deerhurt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DOLPHIN_AMBIENT = createSoundEvent("dolphin");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DOLPHIN_DEATH = createSoundEvent("dolphindying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DOLPHIN_HURT = createSoundEvent("dolphinhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DOLPHIN_UPSET = createSoundEvent("dolphinupset");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DUCK_AMBIENT = createSoundEvent("duck");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DUCK_DEATH = createSoundEvent("duckdying"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DUCK_HURT = createSoundEvent("duckhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGONFLY_AMBIENT = createSoundEvent("dragonfly");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGONFLY_HURT = createSoundEvent("dragonflyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ENT_AMBIENT = createSoundEvent("entgrunt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ENT_DEATH = createSoundEvent("entdying"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ENT_HURT = createSoundEvent("enthurt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ELEPHANT_AMBIENT_BABY = createSoundEvent("elephantcalf");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ELEPHANT_AMBIENT = createSoundEvent("elephantgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ELEPHANT_DEATH = createSoundEvent("elephantdying"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ELEPHANT_HURT = createSoundEvent("elephanthurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FLY_AMBIENT = createSoundEvent("fly");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FLY_HURT = createSoundEvent("flyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FOX_AMBIENT = createSoundEvent("foxcall");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FOX_DEATH = createSoundEvent("foxdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FOX_HURT = createSoundEvent("foxhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_ARMOR_ON = createSoundEvent("armorput");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_ARMOR_OFF = createSoundEvent("armoroff");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_DESTROY = createSoundEvent("destroy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_DRINKING = createSoundEvent("drinking");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_EATING = createSoundEvent("eating");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_MAGIC_APPEAR = createSoundEvent("appearmagic");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_ROPING = createSoundEvent("roping");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_TRANSFORM = createSoundEvent("transform");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_TUD = createSoundEvent("tud"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_VANISH = createSoundEvent("vanish");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_WHIP = createSoundEvent("whip");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GENERIC_WINGFLAP = createSoundEvent("wingflap");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_AMBIENT = createSoundEvent("goatgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_AMBIENT_BABY = createSoundEvent("goatkid");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_AMBIENT_FEMALE = createSoundEvent("goatfemale");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_DEATH = createSoundEvent("goatdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_HURT = createSoundEvent("goathurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_DIGG = createSoundEvent("goatdigg");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_EATING = createSoundEvent("goateating");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOAT_SMACK = createSoundEvent("goatsmack");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_AMBIENT = createSoundEvent("golemgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_ATTACH = createSoundEvent("golemattach");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_DYING = createSoundEvent("golemdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_EXPLODE = createSoundEvent("golemexplode");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_HURT = createSoundEvent("golemhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_SHOOT = createSoundEvent("golemshoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GOLEM_WALK = createSoundEvent("golemwalk");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GRASSHOPPER_AMBIENT = createSoundEvent("grasshopperambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GRASSHOPPER_CHIRP = createSoundEvent("grasshopperchirp");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GRASSHOPPER_FLY = createSoundEvent("grasshopperfly");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GRASSHOPPER_HURT = createSoundEvent("grasshopperhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HELL_RAT_AMBIENT = createSoundEvent("hellratgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HELL_RAT_DEATH = createSoundEvent("hellratdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HELL_RAT_HURT = createSoundEvent("hellrathurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_MAD = createSoundEvent("horsemad");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_AMBIENT = createSoundEvent("horsegrunt");
    //public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_AMBIENT_DONKEY = createSoundEvent("horsegrunt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_AMBIENT_GHOST = createSoundEvent("horsegruntghost");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_AMBIENT_UNDEAD = createSoundEvent("horsegruntundead");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_AMBIENT_ZEBRA = createSoundEvent("zebragrunt");
    //public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_ANGRY = createSoundEvent("horsemad");
    //public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_ANGRY_DONKEY = createSoundEvent("donkeyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_ANGRY_GHOST = createSoundEvent("horsemadghost");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_ANGRY_UNDEAD = createSoundEvent("horsemadundead");
    //public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_ANGRY_ZEBRA = createSoundEvent("zebrahurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_DEATH = createSoundEvent("horsedying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_DEATH_DONKEY = createSoundEvent("donkeydying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_DEATH_GHOST = createSoundEvent("horsedyingghost");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_DEATH_UNDEAD = createSoundEvent("horsedyingundead");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_HURT = createSoundEvent("horsehurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_HURT_DONKEY = createSoundEvent("donkeyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_HURT_GHOST = createSoundEvent("horsehurtghost");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_HURT_UNDEAD = createSoundEvent("horsehurtundead");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HORSE_HURT_ZEBRA = createSoundEvent("zebrahurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_AMBIENT = createSoundEvent("kittygrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_AMBIENT_BABY = createSoundEvent("kittengrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_ANGRY = createSoundEvent("kittyupset");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_DEATH = createSoundEvent("kittydying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_DEATH_BABY = createSoundEvent("kittendying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_DRINKING = createSoundEvent("kittyeatingm");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_EATING = createSoundEvent("kittyfood");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_HUNGRY = createSoundEvent("kittyeatingf");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_HURT = createSoundEvent("kittyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_HURT_BABY = createSoundEvent("kittenhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_LITTER = createSoundEvent("kittylitter"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_PURR = createSoundEvent("kittypurr");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTY_TRAPPED = createSoundEvent("kittytrapped");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTYBED_POURINGFOOD = createSoundEvent("pouringfood");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KITTYBED_POURINGMILK = createSoundEvent("pouringmilk");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_AMBIENT = createSoundEvent("liongrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_AMBIENT_BABY = createSoundEvent("cubgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_DEATH = createSoundEvent("liondeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_DEATH_BABY = createSoundEvent("cubdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_HURT = createSoundEvent("lionhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_LION_HURT_BABY = createSoundEvent("cubhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOUSE_AMBIENT = createSoundEvent("micegrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOUSE_DEATH = createSoundEvent("micedying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOUSE_HURT = createSoundEvent("micehurt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OGRE_AMBIENT = createSoundEvent("ogre");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OGRE_DEATH = createSoundEvent("ogredying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OGRE_HURT = createSoundEvent("ogrehurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OSTRICH_AMBIENT = createSoundEvent("ostrichgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OSTRICH_AMBIENT_BABY = createSoundEvent("ostrichchick");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OSTRICH_DEATH = createSoundEvent("ostrichdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_OSTRICH_HURT = createSoundEvent("ostrichhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RABBIT_DEATH = createSoundEvent("rabbitdeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RABBIT_HURT = createSoundEvent("rabbithurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RABBIT_LIFT = createSoundEvent("rabbitlift");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RACCOON_AMBIENT = createSoundEvent("raccoongrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RACCOON_DEATH = createSoundEvent("raccoondying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RACCOON_HURT = createSoundEvent("raccoonhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_DEATH_LEGACY = createSoundEvent("ratdyinglegacy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_AMBIENT = createSoundEvent("ratgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_DEATH = createSoundEvent("ratdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_HURT = createSoundEvent("rathurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_AMBIENT = createSoundEvent("scorpiongrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_CLAW = createSoundEvent("scorpionclaw");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_DEATH = createSoundEvent("scorpiondying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_HURT = createSoundEvent("scorpionhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_STING = createSoundEvent("scorpionsting");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_AMBIENT = createSoundEvent("snakehiss");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_ANGRY = createSoundEvent("snakeupset");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_DEATH = createSoundEvent("snakedying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_HURT = createSoundEvent("snakehurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_RATTLE = createSoundEvent("snakerattle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_SNAP = createSoundEvent("snakesnap");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_SWIM = createSoundEvent("snakeswim");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_AMBIENT = createSoundEvent("turkey");
    //public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_DEATH = createSoundEvent("turkeyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_HURT = createSoundEvent("turkeyhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURTLE_AMBIENT = createSoundEvent("turtlegrunt"); // TODO
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURTLE_ANGRY = createSoundEvent("turtlehissing");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURTLE_DEATH = createSoundEvent("turtledying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURTLE_EATING = createSoundEvent("turtleeating");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURTLE_HURT = createSoundEvent("turtlehurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_DEATH_HUMAN = createSoundEvent("werehumandying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_HURT_HUMAN = createSoundEvent("werehumanhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_AMBIENT = createSoundEvent("werewolfgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_DEATH = createSoundEvent("werewolfdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_HURT = createSoundEvent("werewolfhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_TRANSFORM = createSoundEvent("weretransform");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WEREWOLF_TRANSFORM_HUMAN = createSoundEvent("werehumantransform");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WOLF_AMBIENT = createSoundEvent("wolfgrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WOLF_DEATH = createSoundEvent("wolfdeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WOLF_HURT = createSoundEvent("wolfhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_AMBIENT = createSoundEvent("wraith");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_DEATH = createSoundEvent("wraithdying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_HURT = createSoundEvent("wraithhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_AMBIENT_LEGACY = createSoundEvent("wraithlegacy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_DEATH_LEGACY = createSoundEvent("wraithdyinglegacy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WRAITH_HURT_LEGACY = createSoundEvent("wraithhurtlegacy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WYVERN_AMBIENT = createSoundEvent("wyverngrunt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WYVERN_DEATH = createSoundEvent("wyverndying");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WYVERN_HURT = createSoundEvent("wyvernhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WYVERN_WINGFLAP = createSoundEvent("wyvernwingflap");
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_RECORD_SHUFFLING = createSoundEvent("shuffling");

    /**
     * Create a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name without the testmod3 prefix
     * @return The SoundEvent
     */
    @SuppressWarnings("removal")
    private static DeferredHolder<SoundEvent, SoundEvent> createSoundEvent(final String soundName) {
        return SOUND_DEFERRED.register(soundName, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, soundName))
        );
    }

}