/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.util;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.Map;

/**
 * Mo' Creatures armour materials. 26.3 armour materials are records; the worn look comes from an equipment asset
 * (assets/mocreatures/equipment/&lt;name&gt;.json) and repair items from an item tag
 * (data/mocreatures/tags/item/*_armor_materials.json).
 */
public final class MoCArmorMaterial {

    public static final ArmorMaterial CROC = new ArmorMaterial(10, defense(1, 3, 4, 1), 17, SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.0F,
            repairTag("croc_armor_materials"), asset("croc"));
    public static final ArmorMaterial FUR = new ArmorMaterial(4, defense(1, 2, 2, 1), 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            repairTag("fur_armor_materials"), asset("fur"));
    public static final ArmorMaterial HIDE = new ArmorMaterial(8, defense(1, 3, 3, 1), 18, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            repairTag("hide_armor_materials"), asset("hide"));
    public static final ArmorMaterial SCORPD = new ArmorMaterial(18, defense(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F,
            repairTag("scorpd_armor_materials"), asset("scorpd"));
    public static final ArmorMaterial SCORPF = new ArmorMaterial(18, defense(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F,
            repairTag("scorpf_armor_materials"), asset("scorpf"));
    public static final ArmorMaterial SCORPN = new ArmorMaterial(18, defense(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F,
            repairTag("scorpn_armor_materials"), asset("scorpn"));
    public static final ArmorMaterial SCORPC = new ArmorMaterial(18, defense(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F,
            repairTag("scorpc_armor_materials"), asset("scorpc"));
    public static final ArmorMaterial SCORPU = new ArmorMaterial(18, defense(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F,
            repairTag("scorpu_armor_materials"), asset("scorpu"));
    public static final ArmorMaterial SILVER = new ArmorMaterial(15, defense(2, 6, 5, 2), 22, SoundEvents.ARMOR_EQUIP_GOLD, 1.5F, 0.0F,
            repairTag("silver_armor_materials"), asset("silver"));

    private MoCArmorMaterial() {
    }

    private static Map<ArmorType, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        Map<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.BOOTS, boots);
        map.put(ArmorType.LEGGINGS, leggings);
        map.put(ArmorType.CHESTPLATE, chestplate);
        map.put(ArmorType.HELMET, helmet);
        map.put(ArmorType.BODY, chestplate);
        return map;
    }

    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name));
    }

    private static ResourceKey<EquipmentAsset> asset(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, name));
    }
}
