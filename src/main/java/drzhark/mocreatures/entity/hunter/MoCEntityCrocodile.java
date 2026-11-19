/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIHunt;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MoCEntityCrocodile extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> IS_RESTING = SynchedEntityData.defineId(MoCEntityCrocodile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING_PREY = SynchedEntityData.defineId(MoCEntityCrocodile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BITING = SynchedEntityData.defineId(MoCEntityCrocodile.class, EntityDataSerializers.BOOLEAN);
    public float biteProgress;
    public float spin;
    public int spinInt;
    private boolean waterbound;

    public MoCEntityCrocodile(EntityType<? extends MoCEntityCrocodile> type, Level world) {
        super(type, world);
        this.texture = "crocodile.png";
        //setSize(0.9F, 0.5F);
        setAdult(true);
        // TODO: Make hitboxes adjust depending on size
        //setAge(50 + this.random.nextInt(50));
        setMoCAge(80);
        setTamed(false);
        this.xpReward = 5;
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new EntityAIFleeFromPlayer(this, 0.8D, 4D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new EntityAIWanderMoC2(this, 0.9D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, Animal.class, true));
        this.targetSelector.addGoal(3, new EntityAIHunt<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_RESTING, Boolean.FALSE);
        builder.define(EATING_PREY, Boolean.FALSE);
        builder.define(IS_BITING, Boolean.FALSE);
    }

    public boolean getIsBiting() {
        return this.entityData.get(IS_BITING);
    }

    public boolean getIsSitting() {
        return this.entityData.get(IS_RESTING);
    }

    public void setIsSitting(boolean flag) {
        this.entityData.set(IS_RESTING, flag);
    }

    public boolean getHasCaughtPrey() {
        return this.entityData.get(EATING_PREY);
    }

    public void setHasCaughtPrey(boolean flag) {
        this.entityData.set(EATING_PREY, flag);
    }

    public void setBiting(boolean flag) {
        this.entityData.set(IS_BITING, flag);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    @Override
    public void jumpFromGround() {
        if (isSwimming()) {
            if (getHasCaughtPrey()) {
                return;
            }

            this.setDeltaMovement(this.getDeltaMovement().x, 0.3D, this.getDeltaMovement().z);
            this.needsSync = true;
        } else if (this.getTarget() != null || getHasCaughtPrey()) {
            super.jumpFromGround();
        }
    }

    @Override
    public boolean isMovementCeased() {
        return getIsSitting();
    }

    @Override
    public void aiStep() {
        if (getIsSitting()) {
            this.setXRot(-5F);
            if (!isSwimming() && this.biteProgress < 0.3F && this.random.nextInt(5) == 0) {
                this.biteProgress += 0.005F;
            }
            if (this.getTarget() != null) {
                setIsSitting(false);
            }
            if (!this.level().isClientSide() && this.getTarget() != null || isSwimming() || getHasCaughtPrey() || this.random.nextInt(500) == 0)// areEyesInFluid(FluidTags.WATER)
            {
                setIsSitting(false);
                this.biteProgress = 0;
            }

        } else {
            if (!this.level().isClientSide() && (this.random.nextInt(500) == 0) && this.getTarget() == null && !getHasCaughtPrey() && !isSwimming()) {
                setIsSitting(true);
                this.getNavigation().stop();
            }
        }

        if (this.random.nextInt(500) == 0 && !getHasCaughtPrey() && !getIsSitting()) {
            crocBite();
        }

        //TODO replace with move to water AI
        if (!this.level().isClientSide() && this.random.nextInt(500) == 0 && !this.waterbound && !getIsSitting() && !isSwimming()) {
            MoCTools.moveToWater(this);
        }

        if (this.waterbound) {
            if (!this.isEyeInFluid(FluidTags.WATER)) {
                MoCTools.moveToWater(this);
            } else {
                this.waterbound = false;
            }
        }

        if (getHasCaughtPrey()) {
            if (this.isVehicle()) {
                setTarget(null);

                this.biteProgress = 0.4F;
                setIsSitting(false);

                if (!this.isEyeInFluid(FluidTags.WATER)) {
                    this.waterbound = true;
                    if (this.getFirstPassenger() instanceof LivingEntity && ((LivingEntity) this.getFirstPassenger()).getHealth() > 0) {
                        ((LivingEntity) this.getFirstPassenger()).deathTime = 0;
                    }

                    if (!this.level().isClientSide() && this.random.nextInt(50) == 0) {
                        this.getFirstPassenger().hurtOrSimulate(this.damageSources().mobAttack(this), 2);
                    }
                }
            } else {
                setHasCaughtPrey(false);
                this.biteProgress = 0F;
                this.waterbound = false;
            }

            if (isSpinning()) {
                this.spinInt += 3;
                if ((this.spinInt % 20) == 0) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_CROCODILE_ROLL.get());
                }
                if (this.spinInt > 80) {
                    this.spinInt = 0;
                    this.getFirstPassenger().hurtOrSimulate(this.damageSources().mobAttack(this), 4); //TODO ADJUST
                }

                //TODO 4FIX
                //the following if to be removed from SMP
                //if (!this.level().isClientSide() && this.isVehicle() && this.getVehicle() instanceof Player) {
                //MoCreatures.mc.gameSettings.thirdPersonView = 1;
                //}
            }
        }

        super.aiStep();
    }

    @Override
    public boolean isNotScared() {
        return true;
    }

    public void crocBite() {
        if (!getIsBiting()) {
            setBiting(true);
            this.biteProgress = 0.0F;
        }
    }

    @Override
    public void tick() {
        if (getIsBiting() && !getHasCaughtPrey())// && biteProgress <0.3)
        {
            this.biteProgress += 0.1F;
            if (this.biteProgress == 0.4F) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_CROCODILE_JAWSNAP.get());
            }
            if (this.biteProgress > 0.6F) {
                setBiting(false);
                this.biteProgress = 0.0F;
            }
        }

        super.tick();
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        if (getHasCaughtPrey()) {
            return false;
        }

        //TODO FIX!!!!
        /*if (!this.level().isClientSide() && entityIn.getVehicle() == null && this.random.nextInt(3) == 0) {
            entityIn.startRiding(this);
            setHasCaughtPrey(true);
            this.waterbound = true;
            return false;
        }*/

        if (super.doHurtTarget(serverLevel, entityIn)) {
            if (!this.level().isClientSide() && !getHasCaughtPrey()) {
                crocBite();
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (this.isVehicle()) {
            Entity entity = damagesource.getEntity();
            if (entity != null && this.getFirstPassenger() == entity) {
                if (this.random.nextInt(2) != 0) {
                    return false;
                } else {
                    unMount();
                }
            }
        }
        
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (this.isVehicle() && this.getFirstPassenger() == entity) {
                if ((entity != this) && entity instanceof LivingEntity && super.shouldAttackPlayers()) {
                    setTarget((LivingEntity) entity);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityCrocodile);
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (!this.isVehicle()) {
            return;
        }
        int direction;

        double dist = getMoCAge() * 0.01F + passenger.getBbWidth() - 0.4D;
        double newPosX = this.getX() - (dist * Math.cos((MoCTools.realAngle(this.getYRot() - 90F)) / 57.29578F));
        double newPosZ = this.getZ() - (dist * Math.sin((MoCTools.realAngle(this.getYRot() - 90F)) / 57.29578F));
        moveFunction.accept(passenger, newPosX, this.getY() + getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y), newPosZ);

        if (this.spinInt > 40) {
            direction = -1;
        } else {
            direction = 1;
        }

        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).yBodyRot = this.getYRot() * direction;
            ((LivingEntity) passenger).yHeadRot = this.getYRot() * direction;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.35D;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_CROCODILE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_CROCODILE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getIsSitting()) {
            return MoCSoundEvents.ENTITY_CROCODILE_RESTING.get();
        }
        return MoCSoundEvents.ENTITY_CROCODILE_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.CROCODILE;
    }

    public boolean isSpinning() {
        return getHasCaughtPrey() && (this.isVehicle()) && (this.isSwimming());
    }

    @Override
    public void die(DamageSource damagesource) {
        unMount();
        MoCTools.checkForTwistedEntities(this.level());
        super.die(damagesource);
    }

    public void unMount() {
        if (this.isVehicle()) {
            if (this.getFirstPassenger() instanceof LivingEntity && ((LivingEntity) this.getFirstPassenger()).getHealth() > 0) {
                ((LivingEntity) this.getFirstPassenger()).deathTime = 0;
            }

            this.ejectPassengers();
            setHasCaughtPrey(false);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    public boolean isAmphibian() {
        return true;
    }

    @Override
    public boolean isSwimming() {
        return this.isInWater();
    }

    @Override
    public boolean isReadyToHunt() {
        return this.isNotScared() && !this.isMovementCeased() && !this.isVehicle() && !this.getHasCaughtPrey();
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.8F));
    }
}
