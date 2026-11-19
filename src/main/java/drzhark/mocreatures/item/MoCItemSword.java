package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCreatures;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MoCItemSword extends SwordItem {

    private final int specialWeaponType;

    public MoCItemSword(Item.Properties properties, Tier material) {
        super(material, 3, -2.4F, properties);
        this.specialWeaponType = 0;
    }

    public MoCItemSword(Item.Properties properties, Tier material, int damageType) {
        super(material, 3, -2.4F, properties);
        this.specialWeaponType = damageType;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (MoCreatures.proxy.weaponEffects) {
            int timer = 10 * 20; // duration in ticks

            switch (this.specialWeaponType) {
                case 1: // Poison 2
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, timer, 1));
                    break;
                case 2: // Slowness
                    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, timer, 0));
                    break;
                case 3: // Fire
                    target.igniteForSeconds(10);
                    break;
                case 4: // Weakness (Nausea for players)
                    target.addEffect(new MobEffectInstance(
                            target instanceof Player ? MobEffects.NAUSEA : MobEffects.WEAKNESS, timer, 0));
                    break;
                case 5: // Wither (Blindness for players)
                    target.addEffect(new MobEffectInstance(
                            target instanceof Player ? MobEffects.BLINDNESS : MobEffects.WITHER, timer, 0));
                    break;
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (MoCreatures.proxy.weaponEffects) {
            String translationKey = null;
            switch (this.specialWeaponType) {
                case 1: translationKey = "info.mocreatures.stingdefault1"; break;
                case 2: translationKey = "info.mocreatures.stingdefault2"; break;
                case 3: translationKey = "info.mocreatures.stingdefault3"; break;
                case 4: translationKey = "info.mocreatures.stingdefault4"; break;
                case 5: translationKey = "info.mocreatures.stingdefault5"; break;
            }
            if (translationKey != null) {
                tooltip.add(Component.translatable(translationKey).withStyle(ChatFormatting.BLUE));
            }
        }
    }
}
