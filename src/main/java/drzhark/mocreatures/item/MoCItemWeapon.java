package drzhark.mocreatures.item;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.Item;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ToolMaterial;

import drzhark.mocreatures.MoCreatures;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
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

    public MoCItemWeapon(Item.Properties properties, ToolMaterial material) {
        super(properties.stacksTo(1).durability(material.durability())
                .enchantable(material.enchantmentValue())
                .repairable(material.repairItems())
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3F + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build()));
        this.material = material;
        this.attackDamage = 3F + material.attackDamageBonus();
    }

    public MoCItemWeapon(Item.Properties properties, ToolMaterial material, int damageType) {
        this(properties, material);
        this.specialWeaponType = damageType;
    }

    public float getAttackDamage() {
        return this.material.attackDamageBonus();
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
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
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

        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    // TODO: Cobweb harvesting
    /*@Override
    public boolean canHarvestBlock(BlockState state) {
        return state.is(Blocks.COBWEB);
    }*/


    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, net.minecraft.core.BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide() && state.getDestroySpeed(world, pos) != 0.0F) {
            stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    public String getToolMaterialName() {
        return this.material.toString();
    }



    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (MoCreatures.proxy.weaponEffects) {
            switch (this.specialWeaponType) {
                case 1 -> tooltip.accept(Component.translatable("info.mocreatures.stingdefault1").withStyle(ChatFormatting.BLUE));
                case 2 -> tooltip.accept(Component.translatable("info.mocreatures.stingdefault2").withStyle(ChatFormatting.BLUE));
                case 3 -> tooltip.accept(Component.translatable("info.mocreatures.stingdefault3").withStyle(ChatFormatting.BLUE));
                case 4 -> tooltip.accept(Component.translatable("info.mocreatures.stingdefault4").withStyle(ChatFormatting.BLUE));
                case 5 -> tooltip.accept(Component.translatable("info.mocreatures.stingdefault5").withStyle(ChatFormatting.BLUE));
            }
        }
    }
}
