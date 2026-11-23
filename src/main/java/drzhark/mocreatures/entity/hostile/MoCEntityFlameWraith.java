/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.attribute.EnvironmentAttributes;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class MoCEntityFlameWraith extends MoCEntityWraith implements Enemy {

    protected int burningTime;

    public MoCEntityFlameWraith(EntityType<? extends MoCEntityFlameWraith> type, Level world) {
        super(type, world);
        this.texture = MoCreatures.proxy.alphaWraithEyes ? "wraith_flame_alpha.png" : "wraith_flame.png";
        //this.isImmuneToFire = true;
        this.burningTime = 30;
        this.xpReward = 7;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityWraith.createAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FLAME_WRAITH;
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            if (this.level().isBrightOutside()) {
                float f = this.getLightLevelDependentMagicValue();
                if ((f > 0.5F) && this.level().canSeeSky(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ()))) && ((this.random.nextFloat() * 30F) < ((f - 0.4F) * 2.0F))) {
                    this.setHealth(getHealth() - 2);
                }
            }
        } else {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + this.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.aiStep();
    }

    //TODO TEST
    /*@Override
    public float getMoveSpeed() {
        return 1.1F;
    }*/

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (!this.level().isClientSide() && !this.level().environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.blockPosition())) {
            target.igniteForSeconds(this.burningTime);
        }
        super.doEnchantDamageEffects(attacker, target);
    }
}
