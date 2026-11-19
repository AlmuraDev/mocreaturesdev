package drzhark.mocreatures.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.UUID;

public class MoCItemCrabClaw extends MoCItem {

    protected static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID TOUGHNESS_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    public final int armor;
    public final int enchantability;
    public final float reach;
    public final float toughness;

    private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public MoCItemCrabClaw(Item.Properties properties, int enchantability, float toughness, int armor, float reach) {
        super(properties);
        this.armor = armor;
        this.enchantability = enchantability;
        this.reach = reach;
        this.toughness = toughness;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Item offhand = attacker.getOffhandItem().getItem();
        if (offhand instanceof MoCItemCrabClaw) {
            stack.hurtAndBreak(1, attacker, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F && entity.getOffhandItem().getItem() instanceof MoCItemCrabClaw) {
            stack.hurtAndBreak(1, entity, (e) -> {
                e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == MoCItems.ANIMALHIDE.get() || super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (attributeModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ARMOR, new AttributeModifier(TOUGHNESS_MODIFIER, "Crab claw armor", armor, AttributeModifier.Operation.ADDITION));
            builder.put(NeoForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Crab claw reach", reach, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(TOUGHNESS_MODIFIER, "Crab claw toughness", toughness, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
        }
        return slot == EquipmentSlot.OFFHAND ? attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }
}

