/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIFollowOwnerPlayer;
import drzhark.mocreatures.entity.ai.EntityAIHunt;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.entity.tameable.MoCPetData;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.util.MoCTags;

// Updated imports for 1.20.1
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;

public class MoCEntityBigCat extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityBigCat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_AMULET = SynchedEntityData.defineId(MoCEntityBigCat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(MoCEntityBigCat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GHOST = SynchedEntityData.defineId(MoCEntityBigCat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(MoCEntityBigCat.class, EntityDataSerializers.BOOLEAN);
    public int mouthCounter;
    public int tailCounter;
    public int wingFlapCounter;
    public MoCAnimalChest localchest;
    public ItemStack localstack;
    protected String chestName = "BigCatChest";
    private int tCounter;
    private float fTransparency;

    public MoCEntityBigCat(EntityType<? extends MoCEntityBigCat> type, Level world) {
        super(type, world);
        //setSize(1.4F, 1.3F);
        boolean isAdult = this.random.nextInt(4) != 0;
        setAdult(isAdult);
        // Set appropriate age based on adult status to prevent mane synchronization issues
        if (isAdult) {
            setMoCAge(45); // Close to adult age for faster growth
        } else {
            setMoCAge(20); // Much younger for babies to prevent mane issues
        }
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
        xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new EntityAIFollowOwnerPlayer(this, 1D, 2F, 10F));
        this.goalSelector.addGoal(2, new EntityAIWanderMoC2(this, 0.8D, 30));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(3, new EntityAIHunt<>(this, AnimalEntity.class, true));
        this.targetSelector.addGoal(3, new EntityAIHunt<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
            .add(Attributes.ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public void selectType() {
        if (getIsAdult()) {
            setMoCAge(getMoCMaxAge());
        }
    }

    @Override
    public double getCustomSpeed() {
        return 2D;
    }

    /**
     * Initializes datawatchers for entity. Each datawatcher is used to sync
     * server data to client.
     */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RIDEABLE, Boolean.FALSE);
        builder.define(SITTING, Boolean.FALSE);
        builder.define(GHOST, Boolean.FALSE);
        builder.define(HAS_AMULET, Boolean.FALSE);
        builder.define(CHESTED, Boolean.FALSE);
    }

    public boolean getHasAmulet() {
        return this.entityData.get(HAS_AMULET);
    }

    public void setHasAmulet(boolean flag) {
        this.entityData.set(HAS_AMULET, flag);
    }

    @Override
    public boolean getIsSitting() {
        return this.entityData.get(SITTING);
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

    @Override
    public boolean getIsGhost() {
        return this.entityData.get(GHOST);
    }

    public void setIsGhost(boolean flag) {
        this.entityData.set(GHOST, flag);
    }

    public void setSitting(boolean flag) {
        this.entityData.set(SITTING, flag);
    }

    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return xpReward;
    }

    // Method used for receiving damage from another source
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if ((this.isPassenger()) && (entity == this.getVehicle())) {
            return false;
        }

        if (super.hurtServer(serverLevel, damagesource, i)) {
            if (entity != null && getIsTamed() && entity instanceof Player) {
                return false;
            }
            if (entity != this && entity instanceof LivingEntity && (this.level().getDifficulty() != Difficulty.PEACEFUL)) {
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        openMouth();
        if (getIsAdult()) {
            return MoCSoundEvents.ENTITY_LION_DEATH.get();
        } else {
            return MoCSoundEvents.ENTITY_LION_DEATH_BABY.get();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        if (getIsAdult()) {
            return MoCSoundEvents.ENTITY_LION_HURT.get();
        } else {
            return MoCSoundEvents.ENTITY_LION_HURT_BABY.get();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        if (getIsAdult()) {
            return MoCSoundEvents.ENTITY_LION_AMBIENT.get();
        } else {
            return MoCSoundEvents.ENTITY_LION_AMBIENT_BABY.get();
        }
    }

    @Override
    public void die(DamageSource damagesource) {
        if (!this.level().isClientSide()) {
            if (getHasAmulet()) {
                MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.MEDALLION.get(), 1));
                setHasAmulet(false);
            }

            if (getIsTamed() && !getIsGhost() && this.random.nextInt(4) == 0) {
                this.spawnGhost();
            }
        }
        super.die(damagesource);
    }

    public void spawnGhost() {
        try {
            EntityType<?> entityType = this.getType();
            Mob templiving = (Mob)entityType.create(this.level(), EntitySpawnReason.BREEDING);
            if (templiving instanceof MoCEntityBigCat) {
                MoCEntityBigCat ghost = (MoCEntityBigCat) templiving;
                ghost.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(ghost);
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_MAGIC_APPEAR.get());
                ghost.setOwnerId(this.getOwnerId());
                ghost.setTamed(true);
                Player entityplayer = this.level().getNearestPlayer(this, 24D);
                if (entityplayer != null) {
                    MoCTools.tameWithName(entityplayer, ghost);
                }

                ghost.setAdult(false);
                ghost.setMoCAge(this.getMoCAge());
                ghost.setTypeMoC(this.getTypeMoC());
                ghost.selectType();
                ghost.setIsGhost(true);

            }
        } catch (Exception ignored) {
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            setSprinting(this.getTarget() != null);
        }

        if (this.level().isClientSide()) //animation counters
        {
            if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
                this.mouthCounter = 0;
            }

            if (this.random.nextInt(250) == 0) {
                moveTail();
            }

            if (this.tailCounter > 0 && ++this.tailCounter > 10 && this.random.nextInt(15) == 0) {
                this.tailCounter = 0;
            }
        } else //server stuff
        {
            if (getIsGhost() && getMoCAge() > 0 && getMoCAge() < 10 && this.random.nextInt(5) == 0) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() == 9) {
                    setMoCAge(getMoCMaxAge());
                    setAdult(true);
                }
            }

            if (!getIsGhost() && getMoCAge() < 10) {
                this.remove(Entity.RemovalReason.KILLED);
            }
            /*if (getHasEaten() && rand.nextInt(300) == 0)
            {
                setEaten(false);
            }*/
        }

        if (!this.level().isClientSide() && isFlyer() && isOnAir()) {
            float myFlyingSpeed = MoCTools.getMyMovementSpeed(this);
            int wingFlapFreq = (int) (25 - (myFlyingSpeed * 10));
            if (!this.isPassenger() || wingFlapFreq < 5) {
                wingFlapFreq = 5;
            }
            if (this.random.nextInt(wingFlapFreq) == 0) {
                wingFlap();
            }
        }

        if (isFlyer()) {
            if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
                this.wingFlapCounter = 0;
            }
            if (!this.level().isClientSide() && this.wingFlapCounter == 5) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_WINGFLAP.get());
            }
        }

        if ((this.random.nextInt(300) == 0) && (this.getHealth() <= getMaxHealth()) && (this.deathTime == 0) && !this.level().isClientSide()) {
            this.setHealth(getHealth() + 1);
        }

        if ((this.deathTime == 0) && !isMovementCeased()) {
            ItemEntity entityitem = getClosestItem(this, 12D, Ingredient.of(Items.PORKCHOP), Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(MoCTags.Items.RAW_FISHES)));
            if (entityitem != null) {
                float f = entityitem.distanceTo(this);
                if (f > 2.0F) {
                    setPathToEntity(entityitem, f);
                }
                if (f < 2.0F && this.deathTime == 0) {
                    entityitem.remove(Entity.RemovalReason.DISCARDED);
                    this.setHealth(getMaxHealth());
                    setHasEaten(true);
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
                }
            }
        }
    }

    @Override
    public boolean readytoBreed() {
        return !this.getIsGhost() && super.readytoBreed();
    }

    public void wingFlap() {
        if (this.level().isClientSide()) {
            return;
        }

        if (this.wingFlapCounter == 0) {
            this.wingFlapCounter = 1;
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 3));
        }
    }

    @Override
    public boolean isNotScared() {
        return getIsAdult() || getMoCAge() > 80;
    }

    @Override
    public boolean isReadyToHunt() {
        return getIsAdult() && !this.isMovementCeased();
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        double dist = getSizeFactor() * (0.1D);
        double newPosX = this.getX() + (dist * Math.sin(this.yBodyRot / 57.29578F));
        double newPosZ = this.getZ() - (dist * Math.cos(this.yBodyRot / 57.29578F));
        passenger.setPos(newPosX, this.getY() + getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y), newPosZ);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Saddle", getIsRideable());
        nbttagcompound.putBoolean("Sitting", getIsSitting());
        nbttagcompound.putBoolean("Chested", getIsChested());
        nbttagcompound.putBoolean("Ghost", getIsGhost());
        nbttagcompound.putBoolean("Amulet", getHasAmulet());
        if (getIsChested() && this.localchest != null) {
            this.localchest.write(nbttagcompound);
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localchest.getContainerSize(); i++) {
                // grab the current item stack
                this.localstack = this.localchest.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items", nbttaglist);
        }

    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setRideable(nbttagcompound.getBooleanOr("Saddle", false));
        setSitting(nbttagcompound.getBooleanOr("Sitting", false));
        setIsChested(nbttagcompound.getBooleanOr("Chested", false));
        setIsGhost(nbttagcompound.getBooleanOr("Ghost", false));
        setHasAmulet(nbttagcompound.getBooleanOr("Amulet", false));
        if (getIsChested()) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items");
            this.localchest = new MoCAnimalChest("BigCatChest", MoCAnimalChest.Size.small);
            this.localchest.read(nbttagcompound);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localchest.getContainerSize()) {
                    this.localchest.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }

    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && !getIsTamed() && getHasEaten() && !getIsAdult() && (stack.getItem() == MoCItems.MEDALLION.get())) {
            if (!this.level().isClientSide()) {
                setHasAmulet(true);
                MoCTools.tameWithName(player, this);
            }
            if (!player.isCreative()) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && !getHasAmulet() && (stack.getItem() == MoCItems.MEDALLION.get())) {
            if (!this.level().isClientSide()) {
                setHasAmulet(true);
            }
            if (!player.isCreative()) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && (stack.getItem() == MoCItems.WHIP.get())) {
            setSitting(!getIsSitting());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
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
        if (!stack.isEmpty() && getIsTamed() && !getIsRideable() && (getMoCAge() > 80)
                && (stack.is(Items.SADDLE) || stack.getItem() == MoCItems.HORSE_SADDLE.get())) {
            if (!player.isCreative()) stack.shrink(1);
            setRideable(true);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.getIsGhost() && this.getIsTamed() && stack.getItem() == MoCItems.AMULET_GHOST.get()) {

            player.setItemInHand(hand, ItemStack.EMPTY);
            if (!this.level().isClientSide()) {
                MoCPetData petData = MoCreatures.instance.mapData.getPetData(this.getOwnerId());
                if (petData != null) {
                    petData.setInAmulet(this.getOwnerPetId(), true);
                }
                this.dropMyStuff();
                MoCTools.dropAmulet(this, 3, player);
                this.remove(Entity.RemovalReason.DISCARDED);
            }

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
                this.localchest = new MoCAnimalChest(this.chestName, MoCAnimalChest.Size.small);
            }
            if (!this.level().isClientSide()) {
                player.openMenu((MenuProvider)this.localchest);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public float getSizeFactor() {
        return getMoCAge() * 0.01F;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource damageSource) {
        if (isFlyer()) {
            return false;
        }
        float i = (float) (Math.ceil(distance - 3F) / 2F);
        if (!this.level().isClientSide() && (i > 0)) {
            i /= 2;
            if (i > 1F) {
                hurtOrSimulate(damageSource, i);
            }
            if ((this.isVehicle()) && (i > 1F)) {
                for (Entity entity : this.getPassengers()) {
                    entity.hurtOrSimulate(damageSource, i);
                }
            }
            BlockPos blockpos = BlockPos.containing(this.getX(), this.getY() - 0.2D - this.xRotO, this.getZ());
            BlockState blockstate = this.level().getBlockState(blockpos);
            Block block = blockstate.getBlock();

            if (!blockstate.isAir() && !this.isSilent()) {
                SoundType soundtype = blockstate.getSoundType();
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), soundtype.getStepSound(), this.getSoundSource(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
            return true;
        }
        return false;
    }

    private void openMouth() {
        this.mouthCounter = 1;
    }

    public boolean hasMane() {
        return false;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    public boolean hasSaberTeeth() {
        return false;
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) //tail animation
        {
            //setPoisoning(true);
        } else if (animationType == 3) //wing flap
        {
            this.wingFlapCounter = 1;
        }
    }

    @Override
    public void makeEntityJump() {
        if (this.isFlyer()) {
            wingFlap();
        }
        super.makeEntityJump();
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

    public boolean getHasStinger() {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        double Yfactor = ((0.0833D * this.getMoCAge()) - 2.5D) / 10D;
        return this.getBbHeight() * Yfactor;
    }

    public float tFloat() {

        if (++this.tCounter > 30) {
            this.tCounter = 0;
            this.fTransparency = (this.random.nextFloat() * (0.4F - 0.2F) + 0.15F);
        }

        if (this.getMoCAge() < 10) {
            return 0F;
        }
        return this.fTransparency;
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
    public float getSpeed() {
        if (isSprinting()) {
            return 0.37F;
        }
        return 0.18F;
    }

    @Override
    protected boolean canBeTrappedInNet() {
        // Ghost variants must stay in Ghost Amulets
        return !this.getIsGhost() && super.canBeTrappedInNet();
    }
}

//would be nice
//lying down
//manticore sounds, drops
//cheetahs
//hand swing when attacking
//more hybrids
//jaguars
//lynx / bobcats