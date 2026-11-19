package drzhark.mocreatures.item;
import net.minecraft.world.item.ToolMaterial;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MoCItemWeapon extends MoCItem {

    private final ToolMaterial material;
    private final float attackDamage;
    private int specialWeaponType = 0;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public MoCItemWeapon(Item.Properties properties, ToolMaterial material) {
        super(properties.stacksTo(1).durability(material.durability()));
        this.material = material;
        this.attackDamage = 3F + material.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public MoCItemWeapon(Item.Properties properties, ToolMaterial material, int damageType) {
        this(properties, material);
        this.specialWeaponType = damageType;
    }

    public float getAttackDamage() {
        return this.material.getAttackDamageBonus();
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            //Material material = state.getMaterial();
            //return (material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && material != Material.LEAVES && material != Material.VEGETABLE) ? 1.0F : 1.5F;
            return 1.5F;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (MoCreatures.proxy.weaponEffects) {
            int timer = 10; // seconds
            switch (this.specialWeaponType) {
                case 1 -> target.addEffect(new MobEffectInstance(MobEffects.POISON, timer * 20, 1));
                case 2 -> target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, timer * 20, 0));
                case 3 -> target.igniteForSeconds(timer);
                case 4 -> {
                    if (target instanceof Player)
                        target.addEffect(new MobEffectInstance(MobEffects.NAUSEA, timer * 20, 0));
                    else
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, timer * 20, 0));
                }
                case 5 -> {
                    if (target instanceof Player)
                        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, timer * 20, 0));
                    else
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, timer * 20, 0));
                }
            }
        }

        stack.hurtAndBreak(1, attacker, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    // TODO: Cobweb harvesting
    /*@Override
    public boolean canHarvestBlock(BlockState state) {
        return state.is(Blocks.COBWEB);
    }*/

    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, net.minecraft.core.BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide() && state.getDestroySpeed(world, pos) != 0.0F) {
            stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public String getToolMaterialName() {
        return this.material.toString();
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.material.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (MoCreatures.proxy.weaponEffects) {
            switch (this.specialWeaponType) {
                case 1 -> tooltip.add(Component.translatable("info.mocreatures.stingdefault1").withStyle(ChatFormatting.BLUE));
                case 2 -> tooltip.add(Component.translatable("info.mocreatures.stingdefault2").withStyle(ChatFormatting.BLUE));
                case 3 -> tooltip.add(Component.translatable("info.mocreatures.stingdefault3").withStyle(ChatFormatting.BLUE));
                case 4 -> tooltip.add(Component.translatable("info.mocreatures.stingdefault4").withStyle(ChatFormatting.BLUE));
                case 5 -> tooltip.add(Component.translatable("info.mocreatures.stingdefault5").withStyle(ChatFormatting.BLUE));
            }
        }
    }
}
