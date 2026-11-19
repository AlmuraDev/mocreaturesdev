/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityHellRat extends MoCEntityRat {

    private int textCounter;

    public MoCEntityHellRat(EntityType<? extends MoCEntityHellRat> type, Level world) {
        super(type, world);
        //setSize(0.88F, 0.755F);
        //this.isImmuneToFire = true;
        this.xpReward = 7;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityRat.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.325D)
                .add(Attributes.ATTACK_DAMAGE, 4.5D)
                .add(Attributes.ARMOR, 7.0D);
    }

    @Override
    public void selectType() {
        setTypeMoC(4);
    }

    @Override
    public Identifier getTexture() {
        if (this.random.nextInt(2) == 0) {
            this.textCounter++;
        }
        if (this.textCounter < 10) {
            this.textCounter = 10;
        }
        if (this.textCounter > 29) {
            this.textCounter = 10;
        }
        String textNumber = String.valueOf(this.textCounter);
        textNumber = textNumber.substring(0, 1);
        return MoCreatures.proxy.getModelTexture("hell_rat" + textNumber + ".png");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_HELL_RAT_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_HELL_RAT_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_HELL_RAT_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.HELL_RAT;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        boolean flag = super.doHurtTarget(serverLevel, entityIn);

        if (flag && entityIn instanceof LivingEntity) {
            entityIn.igniteForSeconds(5);
        }

        return flag;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + this.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.aiStep();
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.485F));
    }
}
