/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromEntityMoC;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class MoCEntityDeer extends MoCEntityTameableAnimal {

    private int readyToJumpTimer;

    public MoCEntityDeer(EntityType<? extends MoCEntityDeer> type, Level world) {
        super(type, world);
        setMoCAge(75);
        //setSize(0.9F, 1.425F);
        setAdult(true);
        setTamed(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EntityAIFleeFromEntityMoC(this, entity -> !(entity instanceof MoCEntityDeer) && (entity.getBbHeight() > 0.8F || entity.getBbWidth() > 0.8F), 6.0F, this.getMyAISpeed(), this.getMyAISpeed() * 1.2D));
        this.goalSelector.addGoal(2, new PanicGoal(this, this.getMyAISpeed() * 1.2D));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, getMyAISpeed()));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, getMyAISpeed()));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes().add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 20) {
                setTypeMoC(1);
            } else if (i <= 70) {
                setTypeMoC(2);
            } else {
                setAdult(false);
                setTypeMoC(3);
            }
        }
    }

    @Override
    public Identifier getTexture() {

        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("deer_doe.png");
            case 3:
                setAdult(false);
                return MoCreatures.proxy.getModelTexture("deer_fawn.png");
            default:
                return MoCreatures.proxy.getModelTexture("deer_stag.png");
        }
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean entitiesToInclude(Entity entity) {
        return !(entity instanceof MoCEntityDeer) && super.entitiesToInclude(entity);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_DEER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_DEER_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!getIsAdult()) {
            return MoCSoundEvents.ENTITY_DEER_AMBIENT_BABY.get();
        } else {
            return MoCSoundEvents.ENTITY_DEER_AMBIENT.get();
        }
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.DEER;
    }

    public double getMyAISpeed() {
        /*if (getTypeMoC() == 1) {
            return 1.1D;
        } else if (getTypeMoC() == 2) {
            return 1.3D;
        }*/
        return 1.1D;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    public int getMoCMaxAge() {
        return 130;
    }

    @Override
    public void setAdult(boolean flag) {
        if (!this.level().isClientSide()) {
            setTypeMoC(this.random.nextInt(1));
        }
        super.setAdult(flag);
    }

    @Override
    public boolean getIsAdult() {
        return this.getTypeMoC() != 3 && super.getIsAdult();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {

            if (this.onGround() && --this.readyToJumpTimer <= 0) {
                if (MoCTools.getMyMovementSpeed(this) > 0.17F) {
                    float velX = (float) (0.5F * Math.cos((MoCTools.realAngle(this.getYRot() - 90F)) / 57.29578F));
                    float velZ = (float) (0.5F * Math.sin((MoCTools.realAngle(this.getYRot() - 90F)) / 57.29578F));
                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, 0.5D, this.getDeltaMovement().z).subtract(velX, 0.0D, velZ));
                    this.readyToJumpTimer = this.random.nextInt(10) + 20;
                }
            }
        }
    }

    @Override
    public float pitchRotationOffset() {
        if (!this.onGround() && MoCTools.getMyMovementSpeed(this) > 0.08F) {
            if (this.getDeltaMovement().y > 0.5D) {
                return 25F;
            }
            if (this.getDeltaMovement().y < -0.5D) {
                return -25F;
            }
            return (float) (this.getDeltaMovement().y * 70D);
        }
        return 0F;
    }

    @Override
    public float getSizeFactor() {
        if (getTypeMoC() == 1) {
            return 1.6F;
        }
        if (getTypeMoC() == 2) {
            return 1.3F;
        }
        return getMoCAge() * 0.01F;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.945F;
    }
}
