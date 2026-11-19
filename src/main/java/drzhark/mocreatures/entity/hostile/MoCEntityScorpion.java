/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.entity.hunter.MoCEntityPetScorpion;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public class MoCEntityScorpion extends MoCEntityMob {

    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(MoCEntityScorpion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_BABIES = SynchedEntityData.defineId(MoCEntityScorpion.class, EntityDataSerializers.BOOLEAN);
    public int mouthCounter;
    public int armCounter;
    public int getTypeMoC; // Baby Type
    private boolean isPoisoning;
    private int poisontimer;

    public MoCEntityScorpion(EntityType<? extends MoCEntityScorpion> type, Level world, int typeMoc) {
        super(type, world);
        //setSize(1.4F, 0.9F);
        setAdult(true);
        setMoCAge(20);
        this.poisontimer = 0;
        this.getTypeMoC = typeMoc;

        // Fire and Undead Scorpions won't spawn with babies
        if (!this.level().isClientSide() && getTypeMoC != 3 && getTypeMoC != 5) {
            setHasBabies(this.getIsAdult() && this.random.nextInt(4) == 0);
        }
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MoCEntityScorpion.AIScorpionAttack(this, 1.0D, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityScorpion.AIScorpionTarget<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new MoCEntityScorpion.AIScorpionTarget<>(this, IronGolem.class, true));
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("scorpion_dirt.png");
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, Boolean.FALSE);
        builder.define(HAS_BABIES, Boolean.FALSE);
    }

    public boolean getHasBabies() {
        return this.entityData.get(HAS_BABIES);
    }

    public void setHasBabies(boolean flag) {
        this.entityData.set(HAS_BABIES, flag);
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

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) // Sting Attack
        {
            setPoisoning(true);
        } else if (animationType == 1) // Attack Animation (Claws)
        {
            this.armCounter = 1;
            swingArm();
        } else if (animationType == 3) // Mouth Movement Animation
        {
            this.mouthCounter = 1;
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return this.entityData.get(CLIMBING);
    }

    public void setBesideClimbableBlock(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        // Claw Attack Sound
        if (this.poisontimer != 1) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SCORPION_CLAW.get());
        }
        return super.doHurtTarget(serverLevel, entity);
    }

    @Override
    public void aiStep() {

        if (!this.onGround() && (this.getVehicle() != null)) {
            this.setYRot(this.getVehicle().getYRot());
        }

        if (this.mouthCounter != 0 && this.mouthCounter++ > 50) {
            this.mouthCounter = 0;
        }

        if (this.armCounter != 0 && this.armCounter++ > 24) {
            this.armCounter = 0;
        }

        if (!this.level().isClientSide() && !this.isVehicle() && this.getIsAdult() && !this.getHasBabies() && this.random.nextInt(100) == 0) {
            MoCTools.findMobRider(this);
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

        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide()) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (entity != this && entity instanceof LivingEntity && this.shouldAttackPlayers()) {
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean entitiesToIgnore(Entity entity) {
        return ((super.entitiesToIgnore(entity)) || (this.getIsTamed() && entity instanceof MoCEntityPetScorpion && ((MoCEntityPetScorpion) entity).getIsTamed()));
    }

    public void swingArm() {
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 1));
        }
    }

    public boolean swingingTail() {
        return getIsPoisoning() && this.poisontimer < 15;
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);

        if (!this.level().isClientSide() && getIsAdult() && getHasBabies()) {
            int k = this.random.nextInt(5);
            for (int i = 0; i < k; i++) {
                Entity entityscorpy = MoCEntities.PET_SCORPION.get().create(this.level(), EntitySpawnReason.BREEDING);
                if (entityscorpy instanceof MoCEntityPetScorpion petScorpion) {
                    petScorpion.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    petScorpion.setAdult(false);
                    petScorpion.setMoCAge(20);
                    petScorpion.setTypeMoC(getTypeMoC);
                    this.level().addFreshEntity(petScorpion);
                    MoCTools.playCustomSound(petScorpion, SoundEvents.SLIME_SQUISH);
                }
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_SCORPION_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_SCORPION_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // Mouth Movement Animation
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 3));
        }
        return MoCSoundEvents.ENTITY_SCORPION_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setHasBabies(nbttagcompound.getBooleanOr("Babies", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Babies", getHasBabies());
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }


    @Override
    public float getAdjustedYOffset() {
        return 30F;
    }

    @Override
    protected int getMaxAge() {
        return 120;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.15D;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double dist = 0.2D;
            double offsetX = this.getX() + (dist * Math.sin(this.getYRot() / 57.29578F));
            double offsetZ = this.getZ() - (dist * Math.cos(this.getYRot() / 57.29578F));
            double offsetY = this.getY() + this.getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y);
            
            moveFunction.accept(passenger, offsetX, offsetY, offsetZ);
            passenger.setYRot(this.getYRot());
        }
    }

    static class AIScorpionAttack extends MeleeAttackGoal {
        public AIScorpionAttack(MoCEntityScorpion scorpion, double speed, boolean useLongMemory) {
            super(scorpion, speed, useLongMemory);
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

    static class AIScorpionTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AIScorpionTarget(MoCEntityScorpion scorpion, Class<T> classTarget, boolean checkSight) {
            super(scorpion, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
