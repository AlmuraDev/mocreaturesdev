package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCreatures;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MoCItemAxe extends AxeItem {

    private final int specialWeaponType;

    public MoCItemAxe(Item.Properties properties, Tier material, float damage, float speed) {
        super(material, damage - 1.0F, speed - 4.0F, properties);
        this.specialWeaponType = 0;
    }

    public MoCItemAxe(Item.Properties properties, Tier material, float damage, float speed, int damageType) {
        super(material, damage - 1.0F, speed - 4.0F, properties);
        this.specialWeaponType = damageType;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (MoCreatures.proxy.weaponEffects) {
            int timer = 15; // seconds
            switch (this.specialWeaponType) {
                case 1: // Poison 2
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, timer * 20, 1));
                    break;
                case 2: // Slowness
                    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, timer * 20, 0));
                    break;
                case 3: // Fire
                    target.igniteForSeconds(timer);
                    break;
                case 4: // Weakness or Nausea
                    if (target instanceof Player) {
                        target.addEffect(new MobEffectInstance(MobEffects.NAUSEA, timer * 20, 0));
                    } else {
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, timer * 20, 0));
                    }
                    break;
                case 5: // Wither or Blindness
                    if (target instanceof Player) {
                        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, timer * 20, 0));
                    } else {
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, timer * 20, 0));
                    }
                    break;
                default:
                    break;
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (MoCreatures.proxy.weaponEffects) {
            String key = switch (this.specialWeaponType) {
                case 1 -> "info.mocreatures.stingaxe1";
                case 2 -> "info.mocreatures.stingaxe2";
                case 3 -> "info.mocreatures.stingaxe3";
                case 4 -> "info.mocreatures.stingaxe4";
                case 5 -> "info.mocreatures.stingaxe5";
                default -> null;
            };
            if (key != null) {
                tooltip.add(Component.translatable(key).withStyle(ChatFormatting.BLUE));
            }
        }
    }
}
