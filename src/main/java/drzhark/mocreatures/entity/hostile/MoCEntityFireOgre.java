/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityFireOgre extends MoCEntityOgre {

    public MoCEntityFireOgre(EntityType<? extends MoCEntityFireOgre> type, Level world) {
        super(type, world);
        //this.isImmuneToFire = true; - this field doesn't exist in 1.20.1
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityOgre.createAttributes()
                .add(Attributes.MAX_HEALTH, 65.0D)
                .add(Attributes.ARMOR, 9.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.5D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("ogre_fire.png");
    }

    @Override
    public boolean isFireStarter() {
        return true;
    }

    @Override
    public float getDestroyForce() {
        return MoCreatures.proxy.ogreFireStrength;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isInWaterOrRain()) {
            this.hurtOrSimulate(this.damageSources().drown(), 1.0F);
        }
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FIRE_OGRE;
    }
}
