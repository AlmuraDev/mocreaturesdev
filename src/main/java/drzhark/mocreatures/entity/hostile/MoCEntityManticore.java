/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class MoCEntityManticore extends MoCEntityMob {

    public int mouthCounter;
    public int tailCounter;
    public int wingFlapCounter;
    private boolean isPoisoning;
    private int poisontimer;

    public MoCEntityManticore(EntityType<? extends MoCEntityManticore> type, Level world) {
        super(type, world);
        //setSize(1.35F, 1.45F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MoCEntityManticore.AIManticoreAttack(this, 1.0D, false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityManticore.AIManticoreTarget<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new MoCEntityManticore.AIManticoreTarget<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.9F);
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    public boolean getIsRideable() {
        return false;
    }

    @Override
    public int maxFlyingHeight() {
        return 10;
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double dist = (-0.1D);
            double newPosX = this.getX() + (dist * Math.sin(this.getYRot() / 57.29578F));
            double newPosZ = this.getZ() - (dist * Math.cos(this.getYRot() / 57.29578F));
            double newPosY = this.getY() + this.getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y);
            
            moveFunction.accept(passenger, newPosX, newPosY, newPosZ);
            passenger.setYRot(this.getYRot());
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.1D;
    }

    protected void openMouth() {
        this.mouthCounter = 1;
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    public boolean isOnAir() {
        return this.level().isEmptyBlock(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2D), Mth.floor(this.getZ())));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // TODO: Fix broken mouth movement
        if (this.level().isClientSide()) {
            if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
                this.mouthCounter = 0;
            }

            if (this.random.nextInt(250) == 0) {
                moveTail();
            }

            if (this.tailCounter > 0 && ++this.tailCounter > 10 && this.random.nextInt(15) == 0) {
                this.tailCounter = 0;
            }
        }

        if (isFlyer()) {
            if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
                this.wingFlapCounter = 0;
            }

            // TODO: Particles while flying?
            /*if (this.wingFlapCounter != 0 && this.wingFlapCounter % 5 == 0 && this.level().isClientSide()) {
                MoCreatures.proxy.StarFX(this);
            }*/

            if (!this.level().isClientSide() && this.wingFlapCounter == 5) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_WINGFLAP.get());
            }
        }

        if (getIsPoisoning()) {
            this.poisontimer++;
            if (this.poisontimer == 1) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SCORPION_STING.get());
            }

            if (this.poisontimer > 50) {
                this.poisontimer = 0;
                setPoisoning(false);
            }
        }

        if (!this.level().isClientSide()) {
            if (!this.level().isClientSide() && isFlyer() && isOnAir()) {
                float myFlyingSpeed = MoCTools.getMyMovementSpeed(this);
                int wingFlapFreq = (int) (25 - (myFlyingSpeed * 10));
                if (!this.isVehicle() || wingFlapFreq < 5) {
                    wingFlapFreq = 5;
                }

                if (this.random.nextInt(wingFlapFreq) == 0) {
                    wingFlap();
                }
            }
        }

        if (!this.isVehicle() && this.random.nextInt(200) == 0) {
            MoCTools.findMobRider(this);
        }
    }

    @Override
    public void makeEntityJump() {
        wingFlap();
        super.makeEntityJump();
    }

    public void wingFlap() {
        if (this.level().isClientSide()) {
            return;
        }

        if (this.isFlyer() && this.wingFlapCounter == 0) {
            this.wingFlapCounter = 1;
            if (!this.level().isClientSide()) {
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 3));
            }
        }
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) // Sting Attack
        {
            setPoisoning(true);
        } else if (animationType == 3) // Flapping Wings
        {
            this.wingFlapCounter = 1;
        }
    }

    public boolean getIsPoisoning() {
        return this.isPoisoning;
    }

    public void setPoisoning(boolean flag) {
        if (flag && !this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 0));
        }

        this.isPoisoning = flag;
    }

    public boolean swingingTail() {
        return getIsPoisoning() && this.poisontimer < 15;
    }

    @Override
    protected SoundEvent getDeathSound() {
        openMouth(); // Mouth Animation
        return MoCSoundEvents.ENTITY_LION_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth(); // Mouth Animation
        return MoCSoundEvents.ENTITY_LION_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth(); // Mouth Animation
        return MoCSoundEvents.ENTITY_LION_AMBIENT.get();
    }

    @Override
    public float getSizeFactor() {
        return 1.4F;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.945F));
    }

    static class AIManticoreAttack extends MeleeAttackGoal {
        public AIManticoreAttack(MoCEntityManticore manticore, double speed, boolean useLongMemory) {
            super(manticore, speed, useLongMemory);
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();

            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected boolean canPerformAttack(LivingEntity attackTarget) {
            // 1.20 attack reach: 4.0F + attackTarget.getBbWidth()
            return this.isTimeToAttack() && this.mob.distanceToSqr(attackTarget) <= (4.0F + attackTarget.getBbWidth()) && this.mob.getSensing().hasLineOfSight(attackTarget);
        }
    }

    static class AIManticoreTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AIManticoreTarget(MoCEntityManticore manticore, Class<T> classTarget, boolean checkSight) {
            super(manticore, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
