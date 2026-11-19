package drzhark.mocreatures.util;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public enum MoCArmorMaterial implements ArmorMaterial {

    CROC("croc", 10, baseStats(1, 3, 4, 1), 17, SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.0F, () -> Ingredient.of(MoCItems.REPTILE_HIDE.get())),
    FUR("fur", 4, baseStats(1, 2, 2, 1), 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(MoCItems.FUR.get())),
    HIDE("hide", 8, baseStats(1, 3, 3, 1), 18, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(MoCItems.ANIMALHIDE.get())),
    SCORPD("scorpd", 18, baseStats(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F, () -> Ingredient.of(MoCItems.CHITIN.get())),
    SCORPF("scorpf", 18, baseStats(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F, () -> Ingredient.of(MoCItems.CHITINFROST.get())),
    SCORPN("scorpn", 18, baseStats(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F, () -> Ingredient.of(MoCItems.CHITINNETHER.get())),
    SCORPC("scorpc", 18, baseStats(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F, () -> Ingredient.of(MoCItems.CHITINCAVE.get())),
    SCORPU("scorpu", 18, baseStats(2, 6, 7, 2), 16, SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0.0F, () -> Ingredient.of(MoCItems.CHITINUNDEAD.get())),
    SILVER("silver", 15, baseStats(2, 6, 5, 2), 22, SoundEvents.ARMOR_EQUIP_GOLD, 1.5F, 0.0F, () -> Ingredient.of(MoCItems.ANCIENTSILVERINGOT.get()));

    private static final int[] BASE_DURABILITIES = new int[]{13, 15, 16, 11};

    private final String name;
    private final int durabilityMultiplier;
    private final Map<Type, Integer> protection;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    MoCArmorMaterial(String name, int durabilityMultiplier, Map<Type, Integer> protection, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protection = protection;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurabilityForType(Type type) {
        return BASE_DURABILITIES[type.getSlot().getIndex()] * durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(Type type) {
        return protection.getOrDefault(type, 0);
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return MoCConstants.MOD_ID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    private static Map<Type, Integer> baseStats(int boots, int leggings, int chestplate, int helmet) {
        Map<Type, Integer> map = new EnumMap<>(Type.class);
        map.put(Type.BOOTS, boots);
        map.put(Type.LEGGINGS, leggings);
        map.put(Type.CHESTPLATE, chestplate);
        map.put(Type.HELMET, helmet);
        return map;
    }
}
