/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.ai.*;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class MoCEntityBear extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Integer> BEAR_STATE = SynchedEntityData.defineId(MoCEntityBear.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityBear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(MoCEntityBear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GHOST = SynchedEntityData.defineId(MoCEntityBear.class, EntityDataSerializers.BOOLEAN);
    public int mouthCounter;
    public MoCAnimalChest localchest;
    public ItemStack localstack;
    private int attackCounter;
    private int standingCounter;

    public MoCEntityBear(EntityType<? extends MoCEntityBear> type, Level world) {
        super(type, world);
        //setSize(1.2F, 1.5F);
        setMoCAge(55);
        setAdult(this.random.nextInt(4) != 0);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.0D));
        this.goalSelector.addGoal(3, new EntityAIFollowOwnerPlayer(this, 1D, 2F, 10F));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, AnimalEntity.class, true));
        this.targetSelector.addGoal(3, new EntityAIHunt<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
            .add(Attributes.ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    /**
     * Initializes datawatchers for entity. Each datawatcher is used to sync
     * server data to client.
     */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEAR_STATE, 0);
        builder.define(RIDEABLE, Boolean.FALSE);
        builder.define(CHESTED, Boolean.FALSE);
        builder.define(GHOST, Boolean.FALSE);
    }

    /**
     * 0 - bear is on fours 1 - standing 2 - sitting
     */
    public int getBearState() {
        return this.entityData.get(BEAR_STATE);
    }

    public void setBearState(int i) {
        this.entityData.set(BEAR_STATE, i);
    }

    @Override
    public boolean getIsRideable() {
        return this.entityData.get(RIDEABLE);
    }

    public boolean getIsChested() {
        return this.entityData.get(CHESTED);
    }

    public void setIsChested(boolean flag) {
        this.entityData.set(CHESTED, flag);
    }

    public boolean getIsGhost() {
        return this.entityData.get(GHOST);
    }

    public void setIsGhost(boolean flag) {
        this.entityData.set(GHOST, flag);
    }

    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    @Override
    public void selectType() {
        if (getIsAdult()) {
            setMoCAge(getMoCMaxAge());
        }
    }

    /**
     * Returns the factor size for the bear, polars are bigger and pandas
     * smaller
     */
    public float getBearSize() {
        return 1.0F;
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return xpReward;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        startAttack();
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    /**
     * Checks if entity is sitting.
     */
    @Override
    public boolean isMovementCeased() {
        return getBearState() == 2;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if (entity != null && this.isVehicle() && this.isPassengerOfSameVehicle(entity)) {
                return true;
            }
            if (entity != this && entity instanceof LivingEntity && super.shouldAttackPlayers()) {
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNotScared() {
        return getIsAdult();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.mouthCounter > 0 && ++this.mouthCounter > 20) {
            this.mouthCounter = 0;
        }
        if (this.attackCounter > 0 && ++this.attackCounter > 9) {
            this.attackCounter = 0;
        }
        if (!this.level().isClientSide() && !getIsAdult() && getMoCAge() < 80 && (this.random.nextInt(300) == 0)) {
            setBearState(2);
        }
        /*
         * Sitting non tamed bears will resume on fours stance every now and then
         */
        if (!this.level().isClientSide() && getBearState() == 2 && !getIsTamed() && this.random.nextInt(800) == 0) {
            setBearState(0);
        }
        if (!this.level().isClientSide() && getBearState() == 2 && !getIsTamed() && !this.getNavigation().isDone()) {
            setBearState(0);
        }
        if (!this.level().isClientSide() && this.standingCounter > 0 && ++this.standingCounter > 100) {
            this.standingCounter = 0;
            setBearState(0);
        }
        /*
         * Standing if close to a vulnerable player
         */
        if (!this.level().isClientSide() && !getIsTamed() && getIsStanding()
                && getBearState() != 2 && getIsAdult() && (this.random.nextInt(200) == 0) && shouldAttackPlayers()) {
            Player entityplayer1 = this.level().getNearestPlayer(this, 4D);
            if ((entityplayer1 != null && this.hasLineOfSight(entityplayer1) && !entityplayer1.getAbilities().invulnerable)) {
                this.setStand();
                setBearState(1);
            }
        }
        //TODO move to AI
        if (!this.level().isClientSide() && getTypeMoC() == 3 && (this.deathTime == 0) && getBearState() != 2) {
            ItemEntity entityitem = getClosestItem(this, 12D, Ingredient.of(Items.SUGAR_CANE, Items.SUGAR));
            if (entityitem != null) {

                float f = entityitem.distanceTo(this);
                if (f > 2.0F) {
                    setPathToEntity(entityitem, f);
                }
                if (f < 2.0F && this.deathTime == 0) {
                    entityitem.remove(Entity.RemovalReason.DISCARDED);
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
                    this.setHealth(getMaxHealth());
                }

            }
        }
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityBear) && entity.getBbHeight() <= 1D && entity.getBbWidth() <= 1D;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_BEAR_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        return MoCSoundEvents.ENTITY_BEAR_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        return MoCSoundEvents.ENTITY_BEAR_AMBIENT.get();
    }

    // TODO: Add unique sound event
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    private void openMouth() {
        this.mouthCounter = 1;
    }

    public float getAttackSwing() {
        if (attackCounter == 0)
            return 0;
        return 1.5F + ((attackCounter / 10F) - 10F) * 5F;
    }

    private void startAttack() {
        if (!this.level().isClientSide() && this.attackCounter == 0 && getBearState() == 1) {
            MoCMessageHandler.INSTANCE.send(
                PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), 
                new MoCMessageAnimation(this.getId(), 0)
            );
            this.attackCounter = 1;
        }
    }

    @Override
    public void performAnimation(int i) {
        this.attackCounter = 1;
    }

    protected void eatingAnimal() {
        openMouth();
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
    }

    @Override
    public double getCustomSpeed() {
        if (getBearState() == 2) {
            return 0D;
        }
        return super.getCustomSpeed();
    }

    @Override
    public boolean isReadyToHunt() {
        return this.getIsAdult() && !this.isMovementCeased();
    }

    public boolean getIsStanding() {
        return this.standingCounter != 0;
    }

    public void setStand() {
        this.standingCounter = 1;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && !getIsRideable() && (getMoCAge() > 80)
                && (stack.getItem() instanceof SaddleItem || stack.getItem() == MoCItems.HORSE_SADDLE.get())) {
            if (!player.isCreative()) stack.shrink(1);
            setRideable(true);
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getIsTamed() && MoCTools.isItemEdibleForCarnivores(stack.getItem())) {
            if (!player.isCreative()) stack.shrink(1);
            this.setHealth(getMaxHealth());
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            setIsHunting(false);
            setHasEaten(true);
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && !getIsChested() && (stack.getItem() == Item.byBlock(Blocks.CHEST))) {
            if (!player.isCreative()) stack.shrink(1);
            setIsChested(true);
            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
            return InteractionResult.SUCCESS;
        }
        if (getIsChested() && player.isCrouching()) {
            if (this.localchest == null) {
                this.localchest = new MoCAnimalChest("BigBearChest", MoCAnimalChest.Size.small);
            }
            if (!this.level().isClientSide()) {
                player.openMenu((MenuProvider) this.localchest);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public double getPassengersRidingOffset() {
        double Yfactor = ((0.086D * this.getMoCAge()) - 2.5D) / 10D;
        return this.getBbHeight() * Yfactor;
    }

    @Override
    public int nameYOffset() {
        return (int) (((0.445D * this.getMoCAge()) + 15D) * -1);
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    @Override
    public float getSizeFactor() {
        return getMoCAge() * 0.01F;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        double dist = getSizeFactor() * (0.1D);
        double newPosX = this.getX() + (dist * Math.sin(this.yBodyRot / 57.29578F));
        double newPosZ = this.getZ() - (dist * Math.cos(this.yBodyRot / 57.29578F));
        passenger.setPos(newPosX, this.getY() + getPassengersRidingOffset() + passenger.getMyRidingOffset(), newPosZ);
    }

    @Override
    public void dropMyStuff() {
        if (!this.level().isClientSide()) {
            dropArmor();
            MoCTools.dropSaddle(this, this.level());

            if (getIsChested()) {
                MoCTools.dropInventory(this, this.localchest);
                MoCTools.dropCustomItem(this, this.level(), new ItemStack(Blocks.CHEST, 1));
                setIsChested(false);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Saddle", getIsRideable());
        nbttagcompound.putBoolean("Chested", getIsChested());
        nbttagcompound.putBoolean("Ghost", getIsGhost());
        nbttagcompound.putInt("BearState", getBearState());
        if (getIsChested() && this.localchest != null) {
            this.localchest.write(nbttagcompound);
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localchest.getContainerSize(); i++) {
                // grab the current item stack
                this.localstack = this.localchest.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    this.localstack.save(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            nbttagcompound.put("Items", nbttaglist);
        }

    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setRideable(nbttagcompound.getBooleanOr("Saddle", false));
        setIsChested(nbttagcompound.getBooleanOr("Chested", false));
        setIsGhost(nbttagcompound.getBooleanOr("Ghost", false));
        setBearState(nbttagcompound.getIntOr("BearState", 0));
        if (getIsChested()) {
            ListTag nbttaglist = nbttagcompound.getList("Items", 10);
            this.localchest = new MoCAnimalChest("BigBearChest", MoCAnimalChest.Size.small);
            this.localchest.read(nbttagcompound);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompound(i);
                int j = nbttagcompound1.getByte("Slot") & 0xff;
                if (j < this.localchest.getContainerSize()) {
                    this.localchest.setItem(j, ItemStack.of(nbttagcompound1));
                }
            }
        }
    }
}
