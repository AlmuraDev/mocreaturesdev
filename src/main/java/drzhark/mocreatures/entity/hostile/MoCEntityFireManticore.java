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
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityFireManticore extends MoCEntityManticore {

    public MoCEntityFireManticore(EntityType<? extends MoCEntityFireManticore> type, Level world) {
        super(type, world);
        //this.isImmuneToFire = true; - field doesn't exist in 1.20.1
        this.xpReward = 10;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityManticore.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 7.5D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("manticore_fire.png");
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (!getIsPoisoning() && this.random.nextInt(5) == 0 && target instanceof LivingEntity) {
            setPoisoning(true);
            target.igniteForSeconds(15);
        } else {
            openMouth();
        }
        super.doEnchantDamageEffects(attacker, target);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FIRE_MANTICORE;
    }
}
