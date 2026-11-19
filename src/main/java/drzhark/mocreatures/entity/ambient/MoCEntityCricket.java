/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAmbient;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityCricket extends MoCEntityAmbient {

    private int jumpCounter;
    private int soundCounter;

    public MoCEntityCricket(EntityType<? extends MoCEntityCricket> type, Level world) {
        super(type, world);
        //setSize(0.4F, 0.3F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIWanderMoC2(this, 1.2D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityAmbient.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ARMOR, 1.0D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 50) {
                setTypeMoC(1);
            } else {
                setTypeMoC(2);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        if (getTypeMoC() == 1) {
            return MoCreatures.proxy.getModelTexture("cricket_light_brown.png");
        } else {
            return MoCreatures.proxy.getModelTexture("cricket_brown.png");
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (!this.level().isClientSide()) {
            if (this.jumpCounter > 0 && ++this.jumpCounter > 30) {
                this.jumpCounter = 0;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    
        if (!this.level().isClientSide()) {
            if (onGround() && ((getDeltaMovement().x > 0.05D) || (getDeltaMovement().z > 0.05D) || (getDeltaMovement().x < -0.05D) || (getDeltaMovement().z < -0.05D)))
                if (this.jumpCounter == 0) {
                    this.setDeltaMovement(this.getDeltaMovement().x * 5D, 0.45D, this.getDeltaMovement().z * 5D);
                    this.jumpCounter = 1;
                }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!level().isBrightOutside()) {
            return level().getRandom().nextDouble() <= 0.1D ? MoCSoundEvents.ENTITY_CRICKET_AMBIENT.get() : null;
        } else {
            return level().getRandom().nextDouble() <= 0.1D ? MoCSoundEvents.ENTITY_CRICKET_CHIRP.get() : null;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_CRICKET_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_CRICKET_HURT.get();
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.CRICKET;
    }

    @Override
    public float getSpeed() {
        if (getIsFlying()) {
            return 0.12F;
        }
        return 0.15F;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (0.15F));
    }
}
