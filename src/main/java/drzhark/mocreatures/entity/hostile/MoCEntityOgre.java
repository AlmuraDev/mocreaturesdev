/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.network.message.MoCMessageExplode;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public class MoCEntityOgre extends MoCEntityMob {

    public int attackCounterLeft;
    public int attackCounterRight;
    public int smashCounter;
    public int armToAnimate; // 1 = left, 2 = right, 3 = both
    public int attackCounter;
    private int movingHead;

    public MoCEntityOgre(EntityType<? extends MoCEntityOgre> type, Level world) {
        super(type, world);
        //setSize(1.8F, 3.05F);
        this.xpReward = 12;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoCEntityOgre.AIOgreAttack(this, 1.25D, false));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityOgre.AIOgreTarget<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new MoCEntityOgre.AIOgreTarget<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(2) + 1);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float damage) {
        if (!super.hurtServer(serverLevel, source, damage)) {
            return false;
        }
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living && attacker != this && !this.hasPassenger(attacker)
                && this.level().getDifficulty() != net.minecraft.world.Difficulty.PEACEFUL
                && this.canAttack(living)
                && !(attacker instanceof Player player && (player.isCreative() || player.isSpectator()))) {
            setTarget(living);
        }
        return true;
    }

    @Override
    public boolean shouldAttackPlayers() {
        return (this.getLightLevelDependentMagicValue() < 0.5F) && super.shouldAttackPlayers();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_OGRE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_OGRE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_OGRE_AMBIENT.get();
    }

    public boolean isFireStarter() {
        return false;
    }

    /**
     * Returns the strength of the blasting power
     */
    public float getDestroyForce() {
        return MoCreatures.proxy.ogreStrength;
    }

    public int getAttackRange() {
        return MoCreatures.proxy.ogreAttackRange;
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            if (this.smashCounter > 0 && ++this.smashCounter > 10) {
                this.smashCounter = 0;
                performDestroyBlastAttack();
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageExplode(this.getId()));
            }

            if ((this.getTarget() != null) && (this.random.nextInt(40) == 0) && this.smashCounter == 0 && this.attackCounter == 0) {
                startDestroyBlast();
            }
        }

        if (this.attackCounter > 0) {
            if (armToAnimate == 3) {
                this.attackCounter++;
            } else {
                this.attackCounter += 2;
            }

            if (this.attackCounter > 10) {
                this.attackCounter = 0;
                this.armToAnimate = 0;
            }
        }
        super.aiStep();
    }

    /**
     * Starts counter to perform the destroyBlast and synchronizes animations with clients
     */
    protected void startDestroyBlast() {
        this.smashCounter = 1;
        MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 3));
    }

    /**
     * Performs the destroy Blast Attack
     */
    public void performDestroyBlastAttack() {
        if (this.deathTime > 0) {
            return;
        }
        MoCTools.destroyBlast(this, this.getX(), this.getY() + 1.0D, this.getZ(), getDestroyForce(), isFireStarter());
    }

    /**
     * Starts attack counters and synchronizes animations with clients
     */
    protected void startArmSwingAttack() {
        if (!this.level().isClientSide()) {
            if (this.smashCounter != 0)
                return;

            boolean leftArmW = (getTypeMoC() == 2 || getTypeMoC() == 4 || getTypeMoC() == 6) && this.random.nextInt(2) == 0;

            this.attackCounter = 1;
            if (leftArmW) {
                this.armToAnimate = 1;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 1));
            } else {
                this.armToAnimate = 2;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 2));
            }
        }
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType != 0) {
            this.attackCounter = 1;
            this.armToAnimate = animationType;
        }

    }

    public int getMovingHead() {
        if (getTypeMoC() == 1) //single headed ogre
        {
            return 1;
        }

        if (this.random.nextInt(60) == 0) {
            this.movingHead = this.random.nextInt(2) + 2; //randomly changes the focus head, returns 2 or 3
        }
        return this.movingHead;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        startArmSwingAttack();
        return super.doHurtTarget(serverLevel, target);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions dimensions = super.getDefaultDimensions(poseIn);
        return dimensions.withEyeHeight((float) (dimensions.height() * 0.91F));
    }

    static class AIOgreAttack extends MeleeAttackGoal {
        public AIOgreAttack(MoCEntityOgre ogre, double speed, boolean useLongMemory) {
            super(ogre, speed, useLongMemory);
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

    static class AIOgreTarget<T extends LivingEntity> extends net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<T> {
        public AIOgreTarget(MoCEntityOgre ogre, Class<T> classTarget, boolean checkSight) {
            super(ogre, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
