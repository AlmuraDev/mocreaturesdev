/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntitySilverSkeleton extends MoCEntityMob {

    public int attackCounterLeft;
    public int attackCounterRight;

    public MoCEntitySilverSkeleton(EntityType<? extends MoCEntitySilverSkeleton> type, Level world) {
        super(type, world);
        this.texture = "silver_skeleton.png";
        //setSize(0.6F, 2.125F);
        this.xpReward = 5 + this.random.nextInt(4);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoCEntitySilverSkeleton.AISkeletonAttack(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntitySilverSkeleton.AISkeletonTarget<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new MoCEntitySilverSkeleton.AISkeletonTarget<>(this, IronGolem.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ARMOR, 11.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            setSprinting(this.getTarget() != null);
        }

        if (this.attackCounterLeft > 0 && ++this.attackCounterLeft > 10) {
            this.attackCounterLeft = 0;
        }

        if (this.attackCounterRight > 0 && ++this.attackCounterRight > 10) {
            this.attackCounterRight = 0;
        }

        super.aiStep();
    }

    @Override
    public void performAnimation(int animationType) {

        if (animationType == 1) //left arm
        {
            this.attackCounterLeft = 1;
        }
        if (animationType == 2) //right arm
        {
            this.attackCounterRight = 1;
        }
    }

    /**
     * Starts attack counters and synchronizes animations with clients
     */
    private void startAttackAnimation() {
        if (!this.level().isClientSide()) {
            boolean leftArmW = this.random.nextInt(2) == 0;

            if (leftArmW) {
                this.attackCounterLeft = 1;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 1));
            } else {
                this.attackCounterRight = 1;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 2));
            }
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        startAttackAnimation();
        return super.doHurtTarget(serverLevel, entityIn);
    }

    @Override
    public float getSpeed() {
        if (isSprinting()) {
            return 0.35F;
        }
        return 0.2F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }


    // TODO: Add unique step sound
    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.SILVER_SKELETON;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.905F));
    }

    static class AISkeletonAttack extends MeleeAttackGoal {
        public AISkeletonAttack(MoCEntitySilverSkeleton skeleton, double speed, boolean useLongMemory) {
            super(skeleton, speed, useLongMemory);
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

    static class AISkeletonTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AISkeletonTarget(MoCEntitySilverSkeleton skeleton, Class<T> classTarget, boolean checkSight) {
            super(skeleton, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
