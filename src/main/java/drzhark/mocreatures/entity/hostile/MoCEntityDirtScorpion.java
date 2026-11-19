/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityDirtScorpion extends MoCEntityScorpion {

    public MoCEntityDirtScorpion(EntityType<? extends MoCEntityDirtScorpion> type, Level world) {
        super(type, world, 1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityScorpion.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 3.0D);
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (!getIsPoisoning() && this.random.nextInt(5) == 0 && target instanceof LivingEntity livingTarget) {
            setPoisoning(true);
            livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 15 * 20, 1)); // 15 seconds
        } else {
            swingArm();
        }
        super.doEnchantDamageEffects(attacker, target);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.DIRT_SCORPION;
    }
}
