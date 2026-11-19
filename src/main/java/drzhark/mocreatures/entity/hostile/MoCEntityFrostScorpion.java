/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import drzhark.mocreatures.MoCreatures;
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

public class MoCEntityFrostScorpion extends MoCEntityScorpion {

    public MoCEntityFrostScorpion(EntityType<? extends MoCEntityFrostScorpion> type, Level world) {
        super(type, world, 4);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityScorpion.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D)
                .add(Attributes.ARMOR, 5.0D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("scorpion_frost.png");
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (!getIsPoisoning() && this.random.nextInt(5) == 0 && target instanceof LivingEntity livingTarget) {
            setPoisoning(true);
            livingTarget.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 25 * 20, 0)); // 25 seconds
        } else {
            swingArm();
        }
        super.doEnchantDamageEffects(attacker, target);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FROST_SCORPION;
    }
}
