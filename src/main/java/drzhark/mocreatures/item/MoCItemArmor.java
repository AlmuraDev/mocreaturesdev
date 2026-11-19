package drzhark.mocreatures.item;
import net.minecraft.world.item.equipment.ArmorType;

import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class MoCItemArmor extends Item {

    public MoCItemArmor(Item.Properties properties, ArmorMaterial materialIn, ArmorType type) {
        super(properties.humanoidArmor(materialIn, type));
    }

    @SuppressWarnings("removal")
    @Override
    public void onArmorTick(ItemStack itemStack, Level world, Player player) {
        if (!world.isClientSide() && this.getEquipmentSlot() == EquipmentSlot.FEET && player.tickCount % 40 == 0) {
            MoCTools.updatePlayerArmorEffects(player);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if ((this == MoCItems.HELMET_SCORP_D.get()) || (this == MoCItems.PLATE_SCORP_D.get())
                || (this == MoCItems.LEGS_SCORP_D.get()) || (this == MoCItems.BOOTS_SCORP_D.get())) {
            tooltip.add(Component.translatable("info.mocreatures.setbonus").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.mocreatures.setbonusscorp1").withStyle(ChatFormatting.BLUE));
        }

        if ((this == MoCItems.HELMET_SCORP_F.get()) || (this == MoCItems.PLATE_SCORP_F.get())
                || (this == MoCItems.LEGS_SCORP_F.get()) || (this == MoCItems.BOOTS_SCORP_F.get())) {
            tooltip.add(Component.translatable("info.mocreatures.setbonus").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.mocreatures.setbonusscorp2").withStyle(ChatFormatting.BLUE));
        }

        if ((this == MoCItems.HELMET_SCORP_N.get()) || (this == MoCItems.PLATE_SCORP_N.get())
                || (this == MoCItems.LEGS_SCORP_N.get()) || (this == MoCItems.BOOTS_SCORP_N.get())) {
            tooltip.add(Component.translatable("info.mocreatures.setbonus").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.mocreatures.setbonusscorp3").withStyle(ChatFormatting.BLUE));
        }

        if ((this == MoCItems.HELMET_SCORP_C.get()) || (this == MoCItems.PLATE_SCORP_C.get())
                || (this == MoCItems.LEGS_SCORP_C.get()) || (this == MoCItems.BOOTS_SCORP_C.get())) {
            tooltip.add(Component.translatable("info.mocreatures.setbonus").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.mocreatures.setbonusscorp4").withStyle(ChatFormatting.BLUE));
        }

        if ((this == MoCItems.HELMET_SCORP_U.get()) || (this == MoCItems.PLATE_SCORP_U.get())
                || (this == MoCItems.LEGS_SCORP_U.get()) || (this == MoCItems.BOOTS_SCORP_U.get())) {
            tooltip.add(Component.translatable("info.mocreatures.setbonus").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.mocreatures.setbonusscorp5").withStyle(ChatFormatting.BLUE));
        }
    }
}
