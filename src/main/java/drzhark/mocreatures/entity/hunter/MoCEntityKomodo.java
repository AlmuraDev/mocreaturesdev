/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIHunt;
import drzhark.mocreatures.entity.ai.EntityAIPanicMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class MoCEntityKomodo extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityKomodo.class, EntityDataSerializers.BOOLEAN);
    public int tailCounter;
    public int tongueCounter;
    public int mouthCounter;
    private int sitCounter;

    public MoCEntityKomodo(EntityType<? extends MoCEntityKomodo> type, Level world) {
        super(type, world);
        this.texture = "komodo_dragon.png";
        setTamed(false);
        setAdult(true);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
        // Note: stepHeight is now called maxUpStep and is accessed differently in 1.20.1

        // TODO: Make hitboxes adjust depending on size
        /*if (this.random.nextInt(6) == 0) {
            setAge(30 + this.random.nextInt(40));
        } else {
            setAge(90 + this.random.nextInt(20));
        }*/
        setMoCAge(90);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.1D));
        this.goalSelector.addGoal(3, new EntityAIFleeFromPlayer(this, 1.1D, 4D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new EntityAIWanderMoC2(this, 0.9D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(2, new EntityAIHunt<>(this, AnimalEntity.class, true));
        this.targetSelector.addGoal(3, new EntityAIHunt<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
            .add(Attributes.FOLLOW_RANGE, 24.0D)
            .add(Attributes.MAX_HEALTH, 25.0D)
            .add(Attributes.ARMOR, 5.0D)
            .add(Attributes.ATTACK_DAMAGE, 4.5D)
            .add(Attributes.MOVEMENT_SPEED, 0.18D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RIDEABLE, Boolean.FALSE);
        // rideable: 0 nothing, 1 saddle
    }

    @Override
    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    @Override
    public boolean getIsRideable() {
        return this.entityData.get(RIDEABLE);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    @Override
    protected SoundEvent getDeathSound() {
        openmouth();
        return MoCSoundEvents.ENTITY_SNAKE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openmouth();
        return MoCSoundEvents.ENTITY_SNAKE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openmouth();
        return MoCSoundEvents.ENTITY_SNAKE_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.KOMODO_DRAGON;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 500;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.sitCounter > 0 && (this.isVehicle() || ++this.sitCounter > 150)) {
            this.sitCounter = 0;
        }
        if (!this.level().isClientSide()) {
            if (!this.isSwimming() && !this.isVehicle() && this.sitCounter == 0 && this.random.nextInt(500) == 0) { //TODO
                sit();
            }

        } else //animation counters, not needed on server
        {
            if (this.tailCounter > 0 && ++this.tailCounter > 60) {
                this.tailCounter = 0;
            }

            if (this.random.nextInt(100) == 0) {
                this.tailCounter = 1;
            }

            if (this.random.nextInt(100) == 0) {
                this.tongueCounter = 1;
            }

            if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
                this.mouthCounter = 0;
            }

            if (this.tongueCounter > 0 && ++this.tongueCounter > 20) {
                this.tongueCounter = 0;
            }
        }
    }

    private void openmouth() {
        this.mouthCounter = 1;
    }

    private void sit() {
        this.sitCounter = 1;
        if (!this.level().isClientSide()) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new MoCMessageAnimation(this.getId(), 0));
        }
        this.getNavigation().stop();
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) //sitting animation
        {
            this.sitCounter = 1;
            this.getNavigation().stop();
        }
    }

    @Override
    public float getSizeFactor() {
        if (!getIsAdult()) {
            return getMoCAge() * 0.01F;
        }
        return 1.2F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && (getMoCAge() > 90 || getIsAdult()) && !getIsRideable()
                && (stack.getItem() instanceof SaddleItem || stack.getItem() == MoCItems.HORSE_SADDLE.get())) {
            if (!player.isCreative()) stack.shrink(1);
            setRideable(true);
            return InteractionResult.SUCCESS;
        }

        if (getIsRideable() && getIsTamed() && getMoCAge() > 90 && (!this.isVehicle())) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isMovementCeased() {
        return this.getIsSitting() || (this.isVehicle());
    }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    @Override
    public int nameYOffset() {
        if (getIsAdult()) {
            return (-50);
        }
        return (-50 + (getMoCAge() / 2));
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Saddle", getIsRideable());
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setRideable(nbttagcompound.getBooleanOr("Saddle", false));
    }

    @Override
    public double getPassengersRidingOffset() {
        double yOff = 0.15F;
        if (getIsAdult()) {
            return yOff + (this.getBbHeight());
        }
        return this.getBbHeight() * ((double) 120 / getMoCAge());
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if ((entity != null && getIsTamed() && entity instanceof Player) || !(entity instanceof LivingEntity)) {
                return false;
            }

            if ((this.isVehicle()) && (entity == this.getVehicle())) {
                return false;
            }

            if ((entity != this) && (super.shouldAttackPlayers())) {
                setTarget((LivingEntity) entity);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == MoCItems.RAT_RAW.get() || stack.getItem() == MoCItems.RAW_TURKEY.get());
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public void dropMyStuff() {
        if (!this.level().isClientSide()) {
            dropArmor();
            MoCTools.dropSaddle(this, this.level());
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityKomodo) && super.canAttackTarget(entity);
    }

    @Override
    public int getMoCMaxAge() {
        return 120;
    }

    @Override
    public boolean getIsSitting() {
        return this.sitCounter != 0;
    }

    @Override
    public boolean isNotScared() {
        return getMoCAge() > 70;
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity entityLivingBaseIn, Entity entityIn) {
        ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 150, 0));
        super.doEnchantDamageEffects(entityLivingBaseIn, entityIn);
    }

    @Override
    public boolean isReadyToHunt() {
        return this.isNotScared() && !this.isMovementCeased() && !this.isVehicle();
    }

    @Override
    public boolean isAmphibian() {
        return true;
    }

    @Override
    public boolean isSwimming() {
        return this.isInWater();
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return !this.isMovementCeased() ? this.getBbHeight() * 0.7F : this.getBbHeight() * 0.365F;
    }
}
