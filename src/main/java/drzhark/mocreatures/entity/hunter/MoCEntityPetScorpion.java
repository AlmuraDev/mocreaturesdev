/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;
import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIFollowOwnerPlayer;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;

// Updated imports for 1.20.1
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class MoCEntityPetScorpion extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(MoCEntityPetScorpion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityPetScorpion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_BABIES = SynchedEntityData.defineId(MoCEntityPetScorpion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(MoCEntityPetScorpion.class, EntityDataSerializers.BOOLEAN);
    public int mouthCounter;
    public int armCounter;
    public int transformType;
    private boolean isPoisoning;
    private int poisontimer;
    private int transformCounter;

    public MoCEntityPetScorpion(EntityType<? extends MoCEntityPetScorpion> type, Level world) {
        super(type, world);
        this.poisontimer = 0;
        setAdult(false);
        setMoCAge(20);
        setHasBabies(false);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MoCEntityPetScorpion.AIPetScorpionAttack(this));
        this.goalSelector.addGoal(4, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new EntityAIFleeFromPlayer(this, 1.2D, 4D));
        this.goalSelector.addGoal(6, new EntityAIFollowOwnerPlayer(this, 1.0D, 2F, 10F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, AnimalEntity.class, true));
    }

    // Updated for 1.20.1
    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
            .add(Attributes.FOLLOW_RANGE, 24.0D)
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(1);
        }
    }

    @Override
    public Identifier getTexture() {
        boolean saddle = getIsRideable();

        // Textures based on type for transforming
        if (this.transformCounter != 0 && this.transformType != 0) {
            String newText;
            switch (this.transformType) {
                case 3: // Fire Scorpion
                    newText = saddle ? "scorpion_fire_saddled.png" : "scorpion_fire.png";
                    break;
                case 5: // Undead Scorpion
                    newText = saddle ? "scorpion_undead_saddled.png" : "scorpion_undead.png";
                    break;
                default:
                    newText = saddle ? "scorpion_undead_saddled.png" : "scorpion_undead.png";
                    break;
            }

            // Textures flashing during transformation
            if (this.transformCounter > 60 && (this.transformCounter % 3) == 0) {
                return MoCreatures.proxy.getModelTexture(newText);
            }
        }

        switch (getTypeMoC()) {
            case 1:
                if (!saddle) {
                    return MoCreatures.proxy.getModelTexture("scorpion_dirt.png");
                }
                return MoCreatures.proxy.getModelTexture("scorpion_dirt_saddled.png");
            case 2:
                if (!saddle) {
                    return MoCreatures.proxy.getModelTexture("scorpion_cave.png");
                }
                return MoCreatures.proxy.getModelTexture("scorpion_cave_saddled.png");
            case 3:
                if (!saddle) {
                    return MoCreatures.proxy.getModelTexture("scorpion_fire.png");
                }
                return MoCreatures.proxy.getModelTexture("scorpion_fire_saddled.png");
            case 4:
                if (!saddle) {
                    return MoCreatures.proxy.getModelTexture("scorpion_frost.png");
                }
                return MoCreatures.proxy.getModelTexture("scorpion_frost_saddled.png");
            case 5:
                if (!saddle) {
                    return MoCreatures.proxy.getModelTexture("scorpion_undead.png");
                }
                return MoCreatures.proxy.getModelTexture("scorpion_undead_saddled.png");
            default:
                return MoCreatures.proxy.getModelTexture("scorpion_dirt.png");
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, Boolean.FALSE);
        builder.define(HAS_BABIES, Boolean.FALSE);
        builder.define(IS_SITTING, Boolean.FALSE);
        builder.define(RIDEABLE, Boolean.FALSE);
    }

    @Override
    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    @Override
    public boolean getIsRideable() {
        return this.entityData.get(RIDEABLE);
    }

    public boolean getHasBabies() {
        return getIsAdult() && this.entityData.get(HAS_BABIES);
    }

    public void setHasBabies(boolean flag) {
        this.entityData.set(HAS_BABIES, flag);
    }

    public boolean getIsPoisoning() {
        return this.isPoisoning;
    }

    @Override
    public boolean getIsSitting() {
        return this.entityData.get(IS_SITTING);
    }

    public void setSitting(boolean flag) {
        this.entityData.set(IS_SITTING, flag);
    }

    public void setPoisoning(boolean flag) {
        if (flag && !this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 0));
        }
        this.isPoisoning = flag;
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) // Tail Animation
        {
            setPoisoning(true);
        } else if (animationType == 1) // Attack Animation (Claws)
        {
            this.armCounter = 1;
            swingArm();
        } else if (animationType == 3) // Mouth Movement Animation
        {
            this.mouthCounter = 1;
        } else if (animationType == 5) // Type Transformation (e.g. Undead)
        {
            this.transformCounter = 1;
        }
    }

    @Override
    public boolean onClimbable() {
        return isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return this.entityData.get(CLIMBING);
    }

    public void setBesideClimbableBlock(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
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

        if (this.transformCounter > 0) {
            // Sound plays after this amount of time has passed during transformation
            if (this.transformCounter == 60) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_TRANSFORM.get());
            }

            // Transformation completed
            if (++this.transformCounter > 100) {
                this.transformCounter = 0;

                if (this.transformType != 0) {
                    setTypeMoC(this.transformType);
                }
            }
        }

        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        
        // Position passengers manually since we can't override positionRider
        if (!this.level().isClientSide()) {
            if (!this.getPassengers().isEmpty()) {
                for (Entity passenger : this.getPassengers()) {
                    positionMyRider(passenger);
                }
            }
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    public boolean swingingTail() {
        return getIsPoisoning() && this.poisontimer < 15;
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.5F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        switch (getTypeMoC()) {
            case 1:
                return MoCLootTables.DIRT_SCORPION;
            case 2:
                return MoCLootTables.CAVE_SCORPION;
            case 3:
                return MoCLootTables.FIRE_SCORPION;
            case 4:
                return MoCLootTables.FROST_SCORPION;
            case 5:
                return MoCLootTables.UNDEAD_SCORPION;
            default:
                return MoCLootTables.DIRT_SCORPION;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsAdult() && !getIsRideable()
                && (stack.is(Items.SADDLE) || stack.is(MoCItems.HORSE_SADDLE.get()))) {
            if (!player.isCreative()) stack.shrink(1);
            setRideable(true);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && stack.is(MoCItems.WHIP.get()) && getIsTamed() && (!this.isVehicle())) {
            setSitting(!getIsSitting());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
            return InteractionResult.SUCCESS;
        }

        // Transformations
        if (!stack.isEmpty() && this.getIsTamed() && !this.isVehicle() && !this.isPassenger() && this.transformCounter < 1) {

            // Fire Scorpion (Essence of Fire)
            if (stack.is(MoCItems.ESSENCE_FIRE.get()) && this.getTypeMoC() != 3) {
                if (!player.isCreative()) stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                } else {
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }

                transform(3);
                return InteractionResult.SUCCESS;
            }

            // Undead Scorpion (Essence of Undead)
            if (stack.is(MoCItems.ESSENCE_UNDEAD.get()) && this.getTypeMoC() != 5) {
                if (!player.isCreative()) stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                } else {
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }

                transform(5);
                return InteractionResult.SUCCESS;
            }
        }

        if (!stack.isEmpty() && this.getIsTamed() && !this.isVehicle() && !this.isPassenger() && stack.is(MoCItems.ESSENCE_DARKNESS.get())) {
            if (!player.isCreative()) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            this.setHealth(getMaxHealth());
            if (!this.level().isClientSide()) {
                int i = getTypeMoC() + 40;
                MoCEntityEgg entityegg = MoCEntities.EGG.get().create(this.level(), EntitySpawnReason.BREEDING);
                entityegg.setEggType(i);
                entityegg.snapTo(player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F);
                this.level().addFreshEntity(entityegg);
                entityegg.setDeltaMovement((this.level().getRandom().nextFloat() - this.level().getRandom().nextFloat()) * 0.3F, this.level().getRandom().nextFloat() * 0.05F, (this.level().getRandom().nextFloat() - this.level().getRandom().nextFloat()) * 0.3F);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.getVehicle() == null && this.getMoCAge() < 60 && !getIsAdult()) {
            if (this.startRiding(player)) {
                this.setYRot(player.getYRot());
                if (!this.level().isClientSide() && !getIsTamed()) {
                    MoCTools.tameWithName(player, this);
                }
            }

            return InteractionResult.SUCCESS;
        } else if (this.getVehicle() != null) {
            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
            this.stopRiding();
            this.setDeltaMovement(player.getDeltaMovement().multiply(5D, 0.5D, 5D));
            return InteractionResult.SUCCESS;
        }

        if (getIsRideable() && getIsTamed() && getIsAdult() && (!this.isVehicle())) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            if (!this.level().isClientSide()) {
                player.startRiding(this);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setHasBabies(nbttagcompound.getBooleanOr("Babies", false));
        setRideable(nbttagcompound.getBooleanOr("Saddled", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Babies", getHasBabies());
        nbttagcompound.putBoolean("Saddled", getIsRideable());
    }

    @Override
    public int nameYOffset() {
        int n = (int) (1 - (getMoCAge() * 0.8));
        if (n < -60) {
            n = -60;
        }
        if (getIsAdult()) {
            n = -60;
        }
        if (getIsSitting()) {
            n = (int) (n * 0.8);
        }
        return n;
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    protected boolean isMyHealFood(ItemStack itemstack) {
        return (itemstack.is(MoCItems.RAT_RAW.get()) || itemstack.is(MoCItems.RAT_COOKED.get()));
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    @Override
    public boolean isMovementCeased() {
        return (this.isVehicle()) || getIsSitting();
    }

    @Override
    public void dropMyStuff() {
        MoCTools.dropSaddle(this, this.level());
    }


    @Override
    public float getAdjustedYOffset() {
        return 0.2F;
    }

    @Override
    public int getMoCMaxAge() {
        return 120;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.15D;
    }

    // Custom method to position riders since positionRider is final in Entity
    public void positionMyRider(Entity passenger) {
        double dist = (0.2D);
        double newPosX = this.getX() + (dist * Math.sin(this.yBodyRot / 57.29578F));
        double newPosZ = this.getZ() - (dist * Math.cos(this.yBodyRot / 57.29578F));
        passenger.setPos(newPosX, this.getY() + getPassengersRidingOffset() + 0.1F, newPosZ);
    }

    @Override
    public boolean isNotScared() {
        return getIsTamed();
    }

    public void transform(int tType) {
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), tType));
        }

        // Set what type to transform to based on type integer
        this.transformType = tType;
        if (this.transformType != 0) {
            this.transformCounter = 1;
        }
    }

    @Override
    public boolean isReadyToHunt() {
        return this.getIsAdult() && !this.isMovementCeased();
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityFox) && entity.getBbHeight() <= 1D && entity.getBbWidth() <= 1D;
    }

    public void swingArm() {
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 1));
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if (!(entity instanceof LivingEntity) || entity instanceof Player && getIsTamed()) {
                return false;
            }
            if (entity != this && super.shouldAttackPlayers() && getIsAdult()) {
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        // Claw Attack Sound
        if (this.poisontimer != 1) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SCORPION_CLAW.get());
        }
        
        if (super.doHurtTarget(serverLevel, entity) && entity instanceof LivingEntity) {
            if (!getIsPoisoning() && this.random.nextInt(5) == 0) {
                setPoisoning(true);
                if (getTypeMoC() <= 1) // Dirt Scorpion
                {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 15 * 20, 1)); // 15 seconds
                } else if (getTypeMoC() == 2) // Cave Scorpion
                {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.NAUSEA, 15 * 20, 0)); // 15 seconds
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15 * 20, 0));
                } else if (getTypeMoC() == 3) // Fire Scorpion
                {
                    entity.igniteForSeconds(15);
                } else if (getTypeMoC() == 4) // Frost Scorpion
                {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 25 * 20, 0)); // 25 seconds
                } else if (getTypeMoC() == 5) // Undead Scorpion
                {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15 * 20, 0)); // 15 seconds
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WITHER, 15 * 20, 0));
                }
            } else {
                swingArm();
            }
            return true;
        }
        return false;
    }

    static class AIPetScorpionAttack extends MeleeAttackGoal {
        public AIPetScorpionAttack(MoCEntityPetScorpion scorpion) {
            super(scorpion, 1.0D, true);
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
}
