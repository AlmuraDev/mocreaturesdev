package drzhark.mocreatures.init;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.item.*;
import drzhark.mocreatures.util.MoCArmorMaterial;
import drzhark.mocreatures.util.MoCItemTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class MoCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MoCConstants.MOD_ID);

    /** Item properties carrying the item's registry id, which 26.3 needs before the item is constructed. */
    public static Properties props(Identifier id) {
        return new Properties().setId(ResourceKey.create(Registries.ITEM, id));
    }

    // Misc
    public static final DeferredHolder<Item, Item> HORSE_GUIDE = ITEMS.register("horseguide", id -> new ItemHorseGuide(MoCItems.props(id).stacksTo(1)));
    public static final DeferredHolder<Item, Item> RECORD_SHUFFLE = ITEMS.register("recordshuffle", id -> new MoCItemRecord(15, MoCSoundEvents.ITEM_RECORD_SHUFFLING::get, MoCItems.props(id).stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> HORSE_SADDLE = ITEMS.register("horsesaddle", id -> new MoCItemHorseSaddle(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> SHARK_TEETH = ITEMS.register("sharkteeth", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> HAYSTACK = ITEMS.register("haystack", id -> new MoCItem(MoCItems.props(id).stacksTo(16)));

    public static final DeferredHolder<Item, Item> SUGAR_LUMP = ITEMS.register("sugarlump", id -> new MoCItemFood.Builder(MoCItems.props(id), 1, 0.1F, false, 12)
            .setPotionEffect(new MobEffectInstance(MobEffects.NAUSEA, 80, 0), 0.15F)
            .setPotionEffect(new MobEffectInstance(MobEffects.SPEED, 80, 0), 1.0F).build());

    public static final DeferredHolder<Item, Item> MOC_EGG = ITEMS.register("mocegg", id -> new MoCItemEgg(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> BIGCAT_CLAW = ITEMS.register("bigcatclaw", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> WHIP = ITEMS.register("whip", id -> new MoCItemWhip(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> STAFF_PORTAL = ITEMS.register("staffportal", id -> new ItemStaffPortal(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> MEDALLION = ITEMS.register("medallion", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item>[] KITTYBED = new DeferredHolder[16];
    static {
        for (int i = 0; i < 16; i++) {
            DyeColor dye = DyeColor.byId(i);
            String colorName = dye.getName();
            int finalI = i;
            KITTYBED[i] = ITEMS.register("kittybed_" + colorName, id -> new MoCItemKittyBed(MoCItems.props(id), finalI));
        }
    }
    public static final DeferredHolder<Item, Item> LITTER_BOX = ITEMS.register("kittylitter", id -> new MoCItemLitterBox(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> WOOL_BALL = ITEMS.register("woolball", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> PET_FOOD = ITEMS.register("petfood", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> REPTILE_HIDE = ITEMS.register("reptilehide", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> FUR = ITEMS.register("fur", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ESSENCE_DARKNESS = ITEMS.register("essencedarkness", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ESSENCE_FIRE = ITEMS.register("essencefire", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ESSENCE_UNDEAD = ITEMS.register("essenceundead", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ESSENCE_LIGHT = ITEMS.register("essencelight", id -> new MoCItem(MoCItems.props(id)));

    // Materials & Drops
    public static final DeferredHolder<Item, Item> HEARTDARKNESS = ITEMS.register("heartdarkness", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> HEARTFIRE = ITEMS.register("heartfire", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> HEARTUNDEAD = ITEMS.register("heartundead", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> UNICORNHORN = ITEMS.register("unicornhorn", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> HORSEARMORCRYSTAL = ITEMS.register("horsearmorcrystal", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ANIMALHIDE = ITEMS.register("hide", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> CHITINCAVE = ITEMS.register("chitinblack", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> CHITINFROST = ITEMS.register("chitinfrost", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> CHITINNETHER = ITEMS.register("chitinnether", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> CHITINUNDEAD = ITEMS.register("chitinundead", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> CHITIN = ITEMS.register("chitin", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> TUSKSWOOD = ITEMS.register("tuskswood", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> TUSKSIRON = ITEMS.register("tusksiron", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> TUSKSDIAMOND = ITEMS.register("tusksdiamond", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ELEPHANTHARNESS = ITEMS.register("elephantharness", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ELEPHANTCHEST = ITEMS.register("elephantchest", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ELEPHANTGARMENT = ITEMS.register("elephantgarment", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ELEPHANTHOWDAH = ITEMS.register("elephanthowdah", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> MAMMOTHPLATFORM = ITEMS.register("mammothplatform", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> SCROLLFREEDOM = ITEMS.register("scrolloffreedom", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> SCROLLOFSALE = ITEMS.register("scrollofsale", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> SCROLLOFOWNER = ITEMS.register("scrollofowner", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ANCIENTSILVERSCRAP = ITEMS.register("ancientsilverscrap", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ANCIENTSILVERINGOT = ITEMS.register("ancientsilveringot", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> ANCIENTSILVERNUGGET = ITEMS.register("ancientsilvernugget", id -> new MoCItem(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> FIRESTONECHUNK = ITEMS.register("firestonechunk", id -> new MoCItem(MoCItems.props(id)));

    // Amulets
    public static final DeferredHolder<Item, Item> AMULET_BONE = ITEMS.register("amuletbone", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_BONE_FULL = ITEMS.register("amuletbonefull", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_GHOST = ITEMS.register("amuletghost", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_GHOST_FULL = ITEMS.register("amuletghostfull", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_FAIRY = ITEMS.register("amuletfairy", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_FAIRY_FULL = ITEMS.register("amuletfairyfull", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_PEGASUS = ITEMS.register("amuletpegasus", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> AMULET_PEGASUS_FULL = ITEMS.register("amuletpegasusfull", id -> new MoCItemHorseAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> FISH_NET = ITEMS.register("fishnet", id -> new MoCItemPetAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> FISH_NET_FULL = ITEMS.register("fishnetfull", id -> new MoCItemPetAmulet(MoCItems.props(id)));
    public static final DeferredHolder<Item, Item> PET_AMULET = ITEMS.register("petamulet", id -> new MoCItemPetAmulet(MoCItems.props(id), 1));
    public static final DeferredHolder<Item, Item> PET_AMULET_FULL = ITEMS.register("petamuletfull", id -> new MoCItemPetAmulet(MoCItems.props(id), 1));

    // Food
    public static final DeferredHolder<Item, Item> COOKED_TURKEY = ITEMS.register("turkeycooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 7, 0.8F, true).build());
    public static final DeferredHolder<Item, Item> CRAB_RAW = ITEMS.register("crabraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 2, 0.1F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final DeferredHolder<Item, Item> CRAB_COOKED = ITEMS.register("crabcooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 4, 0.6F, true).build());
    public static final DeferredHolder<Item, Item> DUCK_COOKED = ITEMS.register("duckcooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 6, 0.7F, true).build());
    public static final DeferredHolder<Item, Item> DUCK_RAW = ITEMS.register("duckraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 2, 0.4F, true).build());
    public static final DeferredHolder<Item, Item> MYSTIC_PEAR = ITEMS.register("mysticpear", id -> new MoCItemFood.Builder(MoCItems.props(id), 4, 0.8F, false, 16).setAlwaysEdible().setPotionEffect(new MobEffectInstance(MobEffects.RESISTANCE, 10 * 20, 1), 1.0F).setPotionEffect(new MobEffectInstance(MobEffects.SPEED, 10 * 20, 1), 1.0F).build());
    public static final DeferredHolder<Item, Item> OMELET = ITEMS.register("omelet", id -> new MoCItemFood.Builder(MoCItems.props(id), 3, 0.5F, false).build());
    public static final DeferredHolder<Item, Item> OSTRICH_RAW = ITEMS.register("ostrichraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 3, 0.4F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final DeferredHolder<Item, Item> OSTRICH_COOKED = ITEMS.register("ostrichcooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 7, 0.8F, true).build());
    public static final DeferredHolder<Item, Item> RAT_BURGER = ITEMS.register("ratburger", id -> new MoCItemFood.Builder(MoCItems.props(id), 9, 0.5F, false).build());
    public static final DeferredHolder<Item, Item> RAT_COOKED = ITEMS.register("ratcooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 4, 0.5F, true).build());
    public static final DeferredHolder<Item, Item> RAT_RAW = ITEMS.register("ratraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 2, 0.1F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final DeferredHolder<Item, Item> RAW_TURKEY = ITEMS.register("turkeyraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 3, 0.4F, true).setPotionEffect(new MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.8F).build());
    public static final DeferredHolder<Item, Item> TURTLE_COOKED = ITEMS.register("turtlecooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 6, 0.7F, true).build());
    public static final DeferredHolder<Item, Item> TURTLE_RAW = ITEMS.register("turtleraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 2, 0.2F, true).build());
    public static final DeferredHolder<Item, Item> TURTLE_SOUP = ITEMS.register("turtlesoup", id -> new MoCItemTurtleSoup.Builder(MoCItems.props(id), 8, 0.8F, false).build());
    public static final DeferredHolder<Item, Item> VENISON_COOKED = ITEMS.register("venisoncooked", id -> new MoCItemFood.Builder(MoCItems.props(id), 8, 0.9F, true).build());
    public static final DeferredHolder<Item, Item> VENISON_RAW = ITEMS.register("venisonraw", id -> new MoCItemFood.Builder(MoCItems.props(id), 3, 0.4F, true).build());

    // Weapons
    public static final DeferredHolder<Item, Item> NUNCHAKU = ITEMS.register("nunchaku", id -> new MoCItemSword(MoCItems.props(id), Tiers.IRON));
    public static final DeferredHolder<Item, Item> SAI = ITEMS.register("sai", id -> new MoCItemSword(MoCItems.props(id), Tiers.IRON));
    public static final DeferredHolder<Item, Item> BO = ITEMS.register("bo", id -> new MoCItemSword(MoCItems.props(id), Tiers.IRON));
    public static final DeferredHolder<Item, Item> KATANA = ITEMS.register("katana", id -> new MoCItemSword(MoCItems.props(id), Tiers.IRON));
    public static final DeferredHolder<Item, Item> SHARKSWORD = ITEMS.register("sharksword", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SHARK));
    public static final DeferredHolder<Item, Item> SHARKAXE = ITEMS.register("sharkaxe", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SHARK, 9.5F, 1.0F));
    public static final DeferredHolder<Item, Item> SILVERSWORD = ITEMS.register("silversword", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SILVER));
    public static final DeferredHolder<Item, Item> SILVERAXE = ITEMS.register("silveraxe", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SILVER, 10.0F, 1.1F));
    public static final DeferredHolder<Item, Item> SCORPSWORDCAVE = ITEMS.register("scorpswordcave", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SCORPC, 4));
    public static final DeferredHolder<Item, Item> SCORPAXECAVE = ITEMS.register("scorpaxecave", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SCORPC, 9.5F, 1.0F, 4));
    public static final DeferredHolder<Item, Item> SCORPSWORDFROST = ITEMS.register("scorpswordfrost", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SCORPF, 2));
    public static final DeferredHolder<Item, Item> SCORPAXEFROST = ITEMS.register("scorpaxefrost", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SCORPF, 9.5F, 1.0F, 2));
    public static final DeferredHolder<Item, Item> SCORPSWORDNETHER = ITEMS.register("scorpswordnether", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SCORPN, 3));
    public static final DeferredHolder<Item, Item> SCORPAXENETHER = ITEMS.register("scorpaxenether", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SCORPN, 9.5F, 1.0F, 3));
    public static final DeferredHolder<Item, Item> SCORPSWORDDIRT = ITEMS.register("scorpsworddirt", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SCORPD, 1));
    public static final DeferredHolder<Item, Item> SCORPAXEDIRT = ITEMS.register("scorpaxedirt", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SCORPD, 9.5F, 1.0F, 1));
    public static final DeferredHolder<Item, Item> SCORPSWORDUNDEAD = ITEMS.register("scorpswordundead", id -> new MoCItemSword(MoCItems.props(id), MoCItemTier.SCORPU, 5));
    public static final DeferredHolder<Item, Item> SCORPAXEUNDEAD = ITEMS.register("scorpaxeundead", id -> new MoCItemAxe(MoCItems.props(id), MoCItemTier.SCORPU, 9.5F, 1.0F, 5));
    public static final DeferredHolder<Item, Item> SCORPSTINGCAVE = ITEMS.register("scorpstingcave", id -> new MoCItemWeapon(MoCItems.props(id), MoCItemTier.STING, 4));
    public static final DeferredHolder<Item, Item> SCORPSTINGFROST = ITEMS.register("scorpstingfrost", id -> new MoCItemWeapon(MoCItems.props(id), MoCItemTier.STING, 2));
    public static final DeferredHolder<Item, Item> SCORPSTINGNETHER = ITEMS.register("scorpstingnether", id -> new MoCItemWeapon(MoCItems.props(id), MoCItemTier.STING, 3));
    public static final DeferredHolder<Item, Item> SCORPSTINGDIRT = ITEMS.register("scorpstingdirt", id -> new MoCItemWeapon(MoCItems.props(id), MoCItemTier.STING, 1));
    public static final DeferredHolder<Item, Item> SCORPSTINGUNDEAD = ITEMS.register("scorpstingundead", id -> new MoCItemWeapon(MoCItems.props(id), MoCItemTier.STING, 5));

    // Armor
    public static final DeferredHolder<Item, Item> PLATE_CROC = ITEMS.register("reptileplate", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.CROC, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> HELMET_CROC = ITEMS.register("reptilehelmet", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.CROC, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> LEGS_CROC = ITEMS.register("reptilelegs", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.CROC, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_CROC = ITEMS.register("reptileboots", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.CROC, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SCORP_D = ITEMS.register("scorphelmetdirt", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPD, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> PLATE_SCORP_D = ITEMS.register("scorpplatedirt", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPD, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SCORP_D = ITEMS.register("scorplegsdirt", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPD, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SCORP_D = ITEMS.register("scorpbootsdirt", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPD, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SCORP_F = ITEMS.register("scorphelmetfrost", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPF, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> PLATE_SCORP_F = ITEMS.register("scorpplatefrost", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPF, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SCORP_F = ITEMS.register("scorplegsfrost", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPF, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SCORP_F = ITEMS.register("scorpbootsfrost", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPF, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SCORP_N = ITEMS.register("scorphelmetnether", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPN, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> PLATE_SCORP_N = ITEMS.register("scorpplatenether", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPN, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SCORP_N = ITEMS.register("scorplegsnether", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPN, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SCORP_N = ITEMS.register("scorpbootsnether", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPN, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SCORP_C = ITEMS.register("scorphelmetcave", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPC, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> PLATE_SCORP_C = ITEMS.register("scorpplatecave", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPC, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SCORP_C = ITEMS.register("scorplegscave", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPC, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SCORP_C = ITEMS.register("scorpbootscave", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPC, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SCORP_U = ITEMS.register("scorphelmetundead", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPU, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> PLATE_SCORP_U = ITEMS.register("scorpplateundead", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPU, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SCORP_U = ITEMS.register("scorplegsundead", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPU, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SCORP_U = ITEMS.register("scorpbootsundead", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SCORPU, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_FUR = ITEMS.register("furhelmet", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.FUR, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> CHEST_FUR = ITEMS.register("furchest", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.FUR, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_FUR = ITEMS.register("furlegs", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.FUR, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_FUR = ITEMS.register("furboots", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.FUR, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_HIDE = ITEMS.register("hidehelmet", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.HIDE, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> CHEST_HIDE = ITEMS.register("hidechest", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.HIDE, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_HIDE = ITEMS.register("hidelegs", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.HIDE, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_HIDE = ITEMS.register("hideboots", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.HIDE, ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, Item> HELMET_SILVER = ITEMS.register("ancient_silver_helmet", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SILVER, ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Item> CHEST_SILVER = ITEMS.register("ancient_silver_chestplate", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SILVER, ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, Item> LEGS_SILVER = ITEMS.register("ancient_silver_leggings", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SILVER, ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, Item> BOOTS_SILVER = ITEMS.register("ancient_silver_boots", id -> new MoCItemArmor(MoCItems.props(id), MoCArmorMaterial.SILVER, ArmorItem.Type.BOOTS));
    
}
    


//@SubscribeEvent
//public static void registerCreativeTabs(CreativeModeTabEvent.Register event) {
//    event.registerCreativeModeTab(Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "mocreatures_tab"),
//            builder -> builder.icon(() -> new ItemStack(HORSE_GUIDE.get()))
//                    .title(Component.translatable("itemGroup.mocreatures"))
//                    .displayItems((parameters, output) -> {
//                        output.accept(HORSE_GUIDE.get());
//                        output.accept(SHARK_TEETH.get());
//                        // Add others here
//                    }));
//} Kitty beds (as array or individual entries)