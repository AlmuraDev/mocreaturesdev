/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIPanicMoC;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import drzhark.mocreatures.entity.item.MoCEntityLitterBox;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.util.MoCTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MoCEntityKitty extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HUNGRY = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EMO = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> KITTY_STATE = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TEMPER = SynchedEntityData.defineId(MoCEntityKitty.class, EntityDataSerializers.INT);
    private final int[] treeCoord = {-1, -1, -1};
    private int kittyTimer;
    private int madTimer;
    private boolean foundTree;
    private boolean isSwinging;
    private boolean onTree;
    private ItemEntity itemAttackTarget;

    public MoCEntityKitty(EntityType<? extends MoCEntityKitty> type, Level world) {
        super(type, world);
        //setSize(0.8F, 0.8F);
        setAdult(true);
        setMoCAge(40);
        setKittyState(1);
        this.kittyTimer = 0;
        this.madTimer = this.random.nextInt(5);
        this.foundTree = false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.0D));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(11) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("kitty_gray.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("kitty_black.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("kitty_calico.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("kitty_tuxedo.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("kitty_white_black.png");
            case 7:
                return MoCreatures.proxy.getModelTexture("kitty_white.png");
            case 8:
                return MoCreatures.proxy.getModelTexture("kitty_orange_tabby.png");
            case 9:
                return MoCreatures.proxy.getModelTexture("kitty_cream_dark.png");
            case 10:
                return MoCreatures.proxy.getModelTexture("kitty_gray_tabby.png");
            case 11:
                return MoCreatures.proxy.getModelTexture("kitty_yellow_tabby.png");
            default:
                return MoCreatures.proxy.getModelTexture("kitty_cream.png");
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SITTING, Boolean.FALSE);
        builder.define(HUNGRY, Boolean.FALSE);
        builder.define(EMO, Boolean.FALSE);
        builder.define(CLIMBING, Boolean.FALSE);
        builder.define(KITTY_STATE, 0);
        builder.define(TEMPER, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        RandomSource random = worldIn.getRandom();
        switch (random.nextInt(3)) {
            case 0: // NEUTRAL
                setTemper(0);
                break;
            case 1: // DOCILE
                setTemper(1);
                break;
            case 2: // DEFIANT
                setTemper(2);
                break;
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public int getKittyState() {
        return this.entityData.get(KITTY_STATE);
    }

    public void setKittyState(int i) {
        this.entityData.set(KITTY_STATE, i);
    }

    public int getTemper() {
        return this.entityData.get(TEMPER);
    }

    public void setTemper(int i) {
        this.entityData.set(TEMPER, i);
    }

    public boolean getIsSitting() {
        return this.entityData.get(SITTING);
    }

    public boolean getIsHungry() {
        return this.entityData.get(HUNGRY);
    }

    public boolean getShowEmoteIcon() {
        return this.entityData.get(EMO);
    }

    public void setShowEmoteIcon(boolean flag) {
        this.entityData.set(EMO, flag);
    }

    public boolean getIsSwinging() {
        return this.isSwinging;
    }

    public boolean getOnTree() {
        return this.onTree;
    }

    public void setOnTree(boolean var1) {
        this.onTree = var1;
    }

    public void setSitting(boolean flag) {
        this.entityData.set(SITTING, flag);
    }

    public void setHungry(boolean flag) {
        this.entityData.set(HUNGRY, flag);
    }

    public void setSwinging(boolean var1) {
        this.isSwinging = var1;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.8F;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        if (getKittyState() != 18 && getKittyState() != 10) {
            swingArm();
        }
        if ((getKittyState() == 13 && entityIn instanceof Player) || (getKittyState() == 8 && entityIn instanceof ItemEntity) || (getKittyState() == 18 && entityIn instanceof MoCEntityKitty) || getKittyState() == 10) {
            return false;
        }
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if (entity != this && entity instanceof LivingEntity) {
                LivingEntity entity1 = (LivingEntity) entity;
                if (getKittyState() == 10) {
                    List<MoCEntityKitty> list = this.level().getEntitiesOfClass(MoCEntityKitty.class, this.getBoundingBox().inflate(16D, 6D, 16D));
                    for (MoCEntityKitty entity2 : list) {
                        if (entity2.getKittyState() == 21) {
                            entity2.setTarget(entity1);
                            return true;
                        }
                    }
                    return true;
                }
                if (entity1 instanceof Player && super.shouldAttackPlayers() && (getTemper() == 2 || (getTemper() == 0 && this.random.nextInt(2) < 1))) {
                    if (getKittyState() < 2) {
                        setTarget(entity1);
                    } else if (getKittyState() == 19 || getKittyState() == 20 || getKittyState() == 21) {
                        setTarget(entity1);
                        setSitting(false);
                    } else if (getKittyState() > 1 && getKittyState() != 10 && getKittyState() != 19 && getKittyState() != 20 && getKittyState() != 21) {
                        setKittyState(13);
                        setSitting(false);
                    }
                    return true;
                }
                setTarget(entity1);
            }
            return true;
        } else {
            return false;
        }
    }

    public void changeKittyState(int i) {
        setKittyState(i);
        setSitting(false);
        this.kittyTimer = 0;
        setOnTree(false);
        this.foundTree = false;
        setTarget(null);
        this.itemAttackTarget = null;
    }

    public boolean climbingTree() {
        return getKittyState() == 16 && onClimbable();
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected Entity findPlayerToAttack() {
        if (this.level().getDifficulty().getId() > net.minecraft.world.Difficulty.NORMAL.getId() && getKittyState() != 8 && getKittyState() != 10 && getKittyState() != 15 && getKittyState() != 18 && getKittyState() != 19 && !isMovementCeased() && getIsHungry()) {
            return getClosestTarget(this, 10D);
        } else {
            return null;
        }
    }

    public Identifier getEmoteIcon() {
        switch (getKittyState()) {
            case -1:
                return MoCreatures.proxy.getMiscTexture("emoticon_blank.png"); // Blank
            case 3:
                return MoCreatures.proxy.getMiscTexture("emoticon_3.png"); // Food
            case 4:
                return MoCreatures.proxy.getMiscTexture("emoticon_4.png"); // Happy
            case 5:
                return MoCreatures.proxy.getMiscTexture("emoticon_5.png"); // Litter Box
            case 7:
                return MoCreatures.proxy.getMiscTexture("emoticon_7.png"); // Very Happy
            case 8:
                return MoCreatures.proxy.getMiscTexture("emoticon_8.png"); // Very, Very Happy
            case 9:
            case 18:
                return MoCreatures.proxy.getMiscTexture("emoticon_9.png"); // In Love
            case 10:
            case 21:
                return MoCreatures.proxy.getMiscTexture("emoticon_10.png"); // Pleased
            case 11:
                return MoCreatures.proxy.getMiscTexture("emoticon_11.png"); // Wondering
            case 12:
                return MoCreatures.proxy.getMiscTexture("emoticon_12.png"); // Sleeping
            case 13:
                return MoCreatures.proxy.getMiscTexture("emoticon_13.png"); // Angry
            case 16:
                return MoCreatures.proxy.getMiscTexture("emoticon_16.png"); // Tree
            case 17:
                return MoCreatures.proxy.getMiscTexture("emoticon_17.png"); // Scared
            case 19:
            case 20:
                return MoCreatures.proxy.getMiscTexture("emoticon_19.png"); // In Labor
            default:
                return MoCreatures.proxy.getMiscTexture("emoticon_1.png"); // Neutral
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getKittyState() == 10 ? MoCSoundEvents.ENTITY_KITTY_DEATH_BABY.get() : MoCSoundEvents.ENTITY_KITTY_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getKittyState() == 10 ? MoCSoundEvents.ENTITY_KITTY_HURT_BABY.get() : MoCSoundEvents.ENTITY_KITTY_HURT.get();
    }

    @Override
    public Identifier getDefaultLootTable() {
        return getIsAdult() ? MoCLootTables.KITTY : null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        switch (getKittyState()) {
            case 3:
                return MoCSoundEvents.ENTITY_KITTY_HUNGRY.get();
            case 4:
                if (this.getVehicle() != null) {
                    MoCEntityKittyBed kittyBed = (MoCEntityKittyBed) this.getVehicle();
                    if (kittyBed != null && !kittyBed.getHasMilk()) {
                        return MoCSoundEvents.ENTITY_KITTY_DRINKING.get();
                    }
                    if (kittyBed != null && !kittyBed.getHasFood()) {
                        return MoCSoundEvents.ENTITY_KITTY_EATING.get();
                    }
                }
                return null;
            case 6:
                return MoCSoundEvents.ENTITY_KITTY_LITTER.get();
            case 10:
                return MoCSoundEvents.ENTITY_KITTY_AMBIENT_BABY.get();
            case 12:
            case 18:
                return MoCSoundEvents.ENTITY_KITTY_PURR.get();
            case 13:
                return MoCSoundEvents.ENTITY_KITTY_ANGRY.get();
            case 17:
                return MoCSoundEvents.ENTITY_KITTY_TRAPPED.get();
            default:
                return MoCSoundEvents.ENTITY_KITTY_AMBIENT.get();
        }
    }

    public Mob getKittyStuff(Entity entity, double d, boolean flag) {
        double d1 = -1D;
        Mob obj = null;
        List<? extends Entity> list;
        if (flag) {
            list = this.level().getEntitiesOfClass(MoCEntityLitterBox.class, this.getBoundingBox().inflate(d));
        } else {
            list = this.level().getEntitiesOfClass(MoCEntityKittyBed.class, this.getBoundingBox().inflate(d));
        }
        for (Entity entity1 : list) {
            if (flag) {
                if (!(entity1 instanceof MoCEntityLitterBox)) {
                    continue;
                }
                MoCEntityLitterBox entitylitterbox = (MoCEntityLitterBox) entity1;
                if (entitylitterbox.getUsedLitter()) {
                    continue;
                }
                double d2 = entity1.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
                if (((d < 0.0D) || (d2 < (d * d))) && ((d1 == -1.0D) || (d2 < d1)) && entitylitterbox.hasLineOfSight(entity)) {
                    d1 = d2;
                    obj = entitylitterbox;
                }
                continue;
            }
            if (!(entity1 instanceof MoCEntityKittyBed)) {
                continue;
            }
            MoCEntityKittyBed kittyBed = (MoCEntityKittyBed) entity1;
            double d3 = entity1.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
            if (((d < 0.0D) || (d3 < (d * d))) && ((d1 == -1.0D) || (d3 < d1)) && kittyBed.hasLineOfSight(entity)) {
                d1 = d3;
                obj = kittyBed;
            }
        }
        return obj;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getVehicle() instanceof Player && this.level().isClientSide()) {
            if (getKittyState() == 10) {
                return super.getPassengersRidingOffset() + 0.4F;
            }
            if (upsideDown()) {
                return super.getPassengersRidingOffset() - 0.1F;
            }
            if (onMaBack()) {
                return super.getPassengersRidingOffset() + 0.1F;
            }
        }
        return super.getPassengersRidingOffset();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Only process one hand to prevent double interactions
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.FAIL;
        }
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }
        final ItemStack stack = player.getItemInHand(hand);
        if (getKittyState() == 2 && !stack.isEmpty() && stack.getItem() == MoCItems.MEDALLION.get()) {
            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }
            if (getIsTamed()) {
                if (!player.isCreative()) stack.shrink(1);
                changeKittyState(3);
                this.setHealth(getMaxHealth());
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (getKittyState() == 7 && !stack.isEmpty() && (stack.getItem() == Items.CAKE || stack.is(ItemTags.FISHES))) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_KITTY_EATING.get());
            this.setHealth(getMaxHealth());
            changeKittyState(9);
            return InteractionResult.SUCCESS;
        }
        if (getKittyState() == 11 && !stack.isEmpty() && stack.getItem() == MoCItems.WOOL_BALL.get()) {
            if (!player.isCreative()) stack.shrink(1);
            setKittyState(8);
            if (!this.level().isClientSide()) {
                ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY() + 1.0D, this.getZ(), new ItemStack(MoCItems.WOOL_BALL.get(), 1));
                entityitem.setPickUpDelay(30);
                entityitem.setNoPickUpDelay();
                this.level().addFreshEntity(entityitem);
                entityitem.setDeltaMovement(entityitem.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.3F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.3F));
                this.itemAttackTarget = entityitem;
            }
            return InteractionResult.SUCCESS;
        }
        if (getKittyState() == 13 && !stack.isEmpty() && stack.is(ItemTags.FISHES)) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_KITTY_EATING.get());
            this.setHealth(getMaxHealth());
            changeKittyState(7);
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getKittyState() > 2 && stack.getItem() == MoCItems.MEDALLION.get() || stack.getItem() == Items.BOOK) {
            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getKittyState() > 2 && pickable() && stack.getItem() == Items.LEAD) {
            if (this.startRiding(player)) {
                changeKittyState(14);
            }
            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getKittyState() > 2 && whipable() && stack.getItem() == MoCItems.WHIP.get()) {
            setSitting(!getIsSitting());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
            return InteractionResult.SUCCESS;
        }
        if (stack.isEmpty() && getKittyState() > 2 && pickable()) {
            if (this.startRiding(player)) {
                changeKittyState(15);
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.isEmpty() && getKittyState() == 15) {
            changeKittyState(7);
            return InteractionResult.SUCCESS;
        }
        if (getKittyState() == 14 && this.getVehicle() != null) {
            changeKittyState(7);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isMovementCeased() {
        return getIsSitting() || getKittyState() == 6 || (getKittyState() == 16 && getOnTree()) || getKittyState() == 12 || getKittyState() == 17 || getKittyState() == 14 || getKittyState() == 20 || getKittyState() == 23;
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
    public void aiStep() {
        if (!this.level().isClientSide()) {
            if (!getIsAdult() && getKittyState() != 10) {
                setKittyState(10);
            }
            if (getKittyState() != 12) {
                super.aiStep();
            }
            if (this.random.nextInt(200) < 1) {
                setShowEmoteIcon(!getShowEmoteIcon());
            }
            if (!getIsAdult() && this.random.nextInt(200) < 1) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= 100) {
                    setAdult(true);
                }
            }
            if (!getIsHungry() && !getIsSitting() && this.random.nextInt(100) < 1) {
                setHungry(true);
            }
            switch (getKittyState()) {
                case -1:
                case 23:
                    break;
                case 0:
                    changeKittyState(1);
                    break;
                case 1: // Untamed
                    if (this.random.nextInt(20) < 1) {
                        changeKittyState(2);
                        break;
                    }
                    if (!getIsHungry() || this.random.nextInt(10) != 0) {
                        break;
                    }
                    ItemEntity entityItem = getClosestItem(this, 10D, Ingredient.of(MoCTags.Items.COOKED_FISHES), Ingredient.of(MoCTags.Items.COOKED_FISHES));
                    if (entityItem != null) {
                        float f = this.distanceTo(entityItem);
                        if (f > 2.0F) {
                            setPathToEntity(entityItem, f);
                        }
                        if (f < 2.0F && this.deathTime < 1) {
                            entityItem.discard();
                            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_KITTY_EATING.get());
                            setHungry(false);
                        }
                    }
                    break;
                case 2: // Scared
                    LivingEntity living1 = getBoogey(6D);
                    if (living1 != null) {
                        MoCTools.runLikeHell(this, living1);
                    }
                    if (this.random.nextInt(200) < 1) {
                        changeKittyState(1);
                        break;
                    }
                    break;
                case 3: // Looking for kitty bed to rest
                    this.kittyTimer++;
                    if (this.kittyTimer > 500) {
                        if (this.random.nextInt(200) < 1) {
                            changeKittyState(13);
                            break;
                        }
                        if (this.random.nextInt(500) < 1) {
                            changeKittyState(7);
                            break;
                        }
                    }
                    if (this.random.nextInt(20) != 0) {
                        break;
                    }
                    MoCEntityKittyBed kittyBed = (MoCEntityKittyBed) getKittyStuff(this, 18D, false);
                    if (kittyBed == null) {
                        break;
                    }
                    if (kittyBed.isVehicle()) {
                        break;
                    }
                    if (!kittyBed.getHasMilk() && !kittyBed.getHasFood()) {
                        break;
                    }
                    float f5 = this.distanceTo(kittyBed);
                    if (f5 > 2.0F) {
                        setPathToEntity(kittyBed, f5);
                    }
                    if (f5 < 2.0F && this.startRiding(kittyBed)) {
                        changeKittyState(4);
                        setSitting(true);
                    }
                    break;
                case 4: // Sitting in kitty bed
                    if (this.getVehicle() != null) {
                        MoCEntityKittyBed kittyBed1 = (MoCEntityKittyBed) this.getVehicle();
                        if (kittyBed1 != null && !kittyBed1.getHasMilk() && !kittyBed1.getHasFood()) {
                            this.setHealth(getMaxHealth());
                            this.stopRiding();
                            changeKittyState(5);
                        }
                    } else {
                        this.setHealth(getMaxHealth());
                        this.stopRiding();
                        changeKittyState(5);
                    }
                    if (this.random.nextInt(2500) < 1) {
                        this.setHealth(getMaxHealth());
                        this.stopRiding();
                        changeKittyState(7);
                    }
                    break;
                case 5: // Looking for litter box
                    this.kittyTimer++;
                    if ((this.kittyTimer > 2000) && (this.random.nextInt(1000) < 1)) {
                        changeKittyState(13);
                        break;
                    }
                    if (this.random.nextInt(20) != 0) {
                        break;
                    }
                    MoCEntityLitterBox litterBox = (MoCEntityLitterBox) getKittyStuff(this, 18D, true);
                    if ((litterBox == null) || (litterBox.isVehicle()) || litterBox.getUsedLitter()) {
                        break;
                    }
                    float f6 = this.distanceTo(litterBox);
                    if (f6 > 2.0F) {
                        setPathToEntity(litterBox, f6);
                    }
                    if (f6 < 2.0F && this.startRiding(litterBox)) {
                        changeKittyState(6);
                    }
                    break;
                case 6: // Doing business in litter box
                    this.kittyTimer++;
                    if (this.kittyTimer <= 300) {
                        if (this.random.nextInt(40) < 1) {
                            MoCTools.playCustomSound(this, SoundEvents.SAND_BREAK);
                        }
                        break;
                    }
                    // TODO: Custom sound
                    MoCTools.playCustomSound(this, SoundEvents.SLIME_BLOCK_BREAK);
                    MoCEntityLitterBox litterBox1 = (MoCEntityLitterBox) this.getVehicle();
                    if (litterBox1 != null) {
                        litterBox1.setUsedLitter(true);
                        litterBox1.litterTime = 0;
                    }
                    this.stopRiding();
                    changeKittyState(7);
                    break;
                case 7: // Idling
                    if (getIsSitting()) {
                        break;
                    }
                    if (this.random.nextInt(20) < 1) {
                        Player player = this.level().getNearestPlayer(this, 12D);
                        if (player != null) {
                            ItemStack stack = player.getInventory().getSelected();
                            if (!stack.isEmpty() && stack.getItem() == MoCItems.WOOL_BALL.get()) {
                                changeKittyState(11);
                                break;
                            }
                        }
                    }
                    // When wet
                    if (this.isInWaterOrBubble() && this.random.nextInt(500) < 1) {
                        changeKittyState(13);
                        break;
                    }
                    // When nighttime
                    if (!this.level().isBrightOutside() && this.random.nextInt(500) < 1) {
                        MoCEntityKittyBed kittyBed1 = (MoCEntityKittyBed) getKittyStuff(this, 18D, false);
                        if (kittyBed1 == null || kittyBed1.isVehicle()) {
                            changeKittyState(12);
                        } else {
                            float f9 = this.distanceTo(kittyBed1);
                            if (f9 > 2.0F) {
                                setPathToEntity(kittyBed1, f9);
                            } else if (this.startRiding(kittyBed1)) {
                                changeKittyState(12);
                            }
                        }
                        break;
                    }
                    // When injured or random
                    if (getHealth() < getMaxHealth() || this.random.nextInt(3000) < 1) {
                        changeKittyState(3);
                        break;
                    }
                    // When outside
                    if (this.level().canSeeSky(this.blockPosition()) && this.random.nextInt(4000) < 1) {
                        changeKittyState(16);
                    }
                    break;
                case 8: // Playing with wool ball
                    if (this.random.nextInt(getTemper() == 2 ? 300 : 200) < 1) {
                        if (this.isInWaterOrBubble()) {
                            changeKittyState(13);
                        } else {
                            changeKittyState(7);
                        }
                        break;
                    }
                    if (this.itemAttackTarget != null) {
                        float f1 = this.distanceTo(itemAttackTarget);
                        if (f1 < 1.5F) {
                            swingArm();
                            if (this.random.nextInt(10) < 1) {
                                float force = 0.2F;
                                if (getTemper() == 2) force = 0.3F;
                                if (getTypeMoC() == 10) force = 0.1F;
                                MoCTools.bigSmack(this, itemAttackTarget, force);
                            }
                        } else {
                            setPathToEntity(itemAttackTarget, f1);
                        }
                    }
                    break;
                case 9: // Looking for mate
                    this.kittyTimer++;
                    if (this.random.nextInt(50) < 1) {
                        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(16D, 6D, 16D), EntitySelector.NO_SPECTATORS.and(e -> e != this));
                        int j = 0;
                        do {
                            if (j >= list.size()) {
                                break;
                            }
                            Entity entity = list.get(j);
                            if (entity instanceof MoCEntityKitty && ((MoCEntityKitty) entity).getKittyState() == 9) {
                                changeKittyState(18);
                                setTarget((LivingEntity) entity);
                                ((MoCEntityKitty) entity).changeKittyState(18);
                                ((MoCEntityKitty) entity).setTarget(this);
                                break;
                            }
                            j++;
                        } while (true);
                    }
                    if (this.kittyTimer > 2000) {
                        changeKittyState(7);
                    }
                    break;
                case 10: // Baby state
                    if (getIsAdult()) {
                        changeKittyState(7);
                        break;
                    }
                    if (this.random.nextInt(50) < 1) {
                        List<Entity> list1 = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(16D, 6D, 16D), EntitySelector.NO_SPECTATORS.and(e -> e != this));
                        for (Entity entity1 : list1) {
                            if (!(entity1 instanceof MoCEntityKitty) || ((MoCEntityKitty) entity1).getKittyState() != 21) {
                                continue;
                            }
                            float f9 = this.distanceTo(entity1);
                            if (f9 > 12F) {
                                setTarget((LivingEntity) entity1);
                            }
                        }
                    }
                    if ((this.itemAttackTarget == null || getTarget() == null) && this.random.nextInt(100) < 1) {
                        int i = this.random.nextInt(10);
                        if (i < 7) {
                            this.itemAttackTarget = getClosestItem(this, 10D);
                        } else {
                            this.setTarget(this.level().getNearestPlayer(this, 18D));
                        }
                    }
                    if ((this.getTarget() != null || this.itemAttackTarget != null) && this.random.nextInt(400) < 1) {
                        setTarget(null);
                        this.itemAttackTarget = null;
                    }
                    if (this.itemAttackTarget != null) {
                        float f2 = this.distanceTo(this.itemAttackTarget);
                        if (f2 < 1.5F) {
                            swingArm();
                            if (this.random.nextInt(10) < 1) {
                                MoCTools.bigSmack(this, this.itemAttackTarget, 0.2F);
                            }
                        }
                    }
                    if (getTarget() instanceof MoCEntityKitty && this.random.nextInt(20) < 1) {
                        float f3 = this.distanceTo(getTarget());
                        if (f3 < 2.0F) {
                            swingArm();
                            this.getNavigation().stop();
                        }
                    }
                    if (!(getTarget() instanceof Player)) {
                        break;
                    }
                    float f4 = this.distanceTo(getTarget());
                    if ((f4 < 2.0F) && (this.random.nextInt(20) < 1)) {
                        swingArm();
                    }
                    break;
                case 11: // Looking for player holding wool ball
                    Player player1 = this.level().getNearestPlayer(this, 18D);
                    if ((player1 == null) || (this.random.nextInt(10) != 0)) {
                        break;
                    }
                    ItemStack stack1 = player1.getInventory().getSelected();
                    if (stack1.getItem() != MoCItems.WOOL_BALL.get()) {
                        changeKittyState(7);
                        break;
                    }
                    float f8 = this.distanceTo(player1);
                    if (f8 > 5F) {
                        getPathOrWalkableBlock(player1, f8);
                    }
                    break;
                case 12: // Lying down to sleep at night
                    this.kittyTimer++;
                    if (this.level().isBrightOutside() || (this.kittyTimer > 500 && this.random.nextInt(500) < 1)) {
                        this.stopRiding();
                        changeKittyState(7);
                        break;
                    }
                    setSitting(true);
                    if (this.random.nextInt(100) < 1 || !this.onGround()) {
                        super.aiStep();
                    }
                    break;
                case 13: // Aggressive behavior
                    if (getTemper() == 1 || (getTemper() == 0 && this.random.nextInt(2) < 1)) {
                        changeKittyState(7);
                    }
                    setHungry(false);
                    setTarget(this.level().getNearestPlayer(this, 18D));
                    if (getTarget() != null) {
                        float f7 = this.distanceTo(getTarget());
                        if (f7 < 1.5F) {
                            swingArm();
                            if (this.random.nextInt(20) < 1) {
                                this.madTimer--;
                                getTarget().hurtOrSimulate(this.level().damageSources().mobAttack(this), 1);
                                if (this.madTimer < 1) {
                                    changeKittyState(7);
                                    this.madTimer = this.random.nextInt(5);
                                }
                            }
                        }
                        if (this.random.nextInt(500) < 1) {
                            changeKittyState(7);
                        }
                    } else {
                        changeKittyState(7);
                    }
                    break;
                case 14: // Held by rope
                    if (this.onGround()) {
                        changeKittyState(13);
                        break;
                    }
                    if (this.random.nextInt(50) < 1) {
                        swingArm();
                    }
                    if (this.getVehicle() == null) {
                        break;
                    }
                    this.setYRot(this.getVehicle().getYRot() + 90F);
                    Player player2 = (Player) this.getVehicle();
                    if (player2 == null) {
                        changeKittyState(13);
                        break;
                    }
                    ItemStack stack2 = player2.getInventory().getSelected();
                    if (stack2.getItem() != Items.LEAD) {
                        changeKittyState(13);
                    }
                    break;
                case 15: // Picked up by player
                    if (this.onGround()) {
                        changeKittyState(7);
                    }
                    if (this.getVehicle() != null) {
                        this.setYRot(this.getVehicle().getYRot() + 90F);
                    }
                    break;
                case 16: // Looking for nearby tree
                    kittyTimer++;
                    if (kittyTimer > 500) {
                        if (!getOnTree()) {
                            changeKittyState(7);
                        } else {
                            setKittyState(17);
                        }
                    }
                    if (!getOnTree()) {
                        if (!foundTree && random.nextInt(50) < 1) {
                            BlockPos treeTop = MoCTools.getTreeTop(level(), this, 18);
                            if (treeTop != null) {
                                treeCoord[0] = treeTop.getX();
                                treeCoord[1] = treeTop.getY();
                                treeCoord[2] = treeTop.getZ();
                                foundTree = true;
                            }
                        }
                        Path pathEntity = this.getNavigation().createPath(treeCoord[0], treeCoord[1], treeCoord[2], 0);
                        if (pathEntity != null) {
                            this.getNavigation().moveTo(pathEntity, 1.5D);
                        }
                        double dX = treeCoord[0] + 0.5D - this.getX();
                        double dY = treeCoord[1] + 0.5D - (this.getY() + this.getEyeHeight());
                        double dZ = treeCoord[2] + 0.5D - this.getZ();
                        double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                        // Climbing the tree
                        this.setDeltaMovement((dX / distance) * 0.05D, this.getDeltaMovement().y, (dZ / distance) * 0.05D);
                        // Climbing through leaves
                        if (!onGround() && this.horizontalCollision) {
                            // Disable collision checks while climbing through leaves
                            noPhysics = level().getBlockState(this.blockPosition()).is(net.minecraft.tags.BlockTags.LEAVES) || level().getBlockState(this.blockPosition().above()).is(net.minecraft.tags.BlockTags.LEAVES);
                        }
                        // Reached the top of the tree
                        else if (!level().getBlockState(this.blockPosition()).isSuffocating(level(), this.blockPosition())) {
                            // Re-enable collision checks after climbing through leaves
                            noPhysics = false;
                            this.pushEntities();
                            // Check if the block below is leaves
                            BlockPos posBelow = this.blockPosition().below();
                            if (level().getBlockState(posBelow).is(net.minecraft.tags.BlockTags.LEAVES)) {
                                setOnTree(true);
                            }
                        }
                    }
                    break;
                case 17: // Stuck on tree, looking for player nearby
                    if (getTemper() == 2 || (getTemper() == 0 && this.random.nextInt(2) < 1)) {
                        changeKittyState(7);
                    }
                    Player player3 = this.level().getNearestPlayer(this, 2D);
                    if (player3 != null) {
                        changeKittyState(7);
                    }
                    break;
                case 18: // Mating with another kitty
                    if (!(getTarget() instanceof MoCEntityKitty)) {
                        changeKittyState(9);
                        break;
                    }
                    MoCEntityKitty kitty = (MoCEntityKitty) getTarget();
                    if (kitty != null && kitty.getKittyState() == 18) {
                        if (this.random.nextInt(50) < 1) {
                            swingArm();
                        }
                        float f10 = this.distanceTo(kitty);
                        if (f10 < 5F) {
                            this.kittyTimer++;
                        }
                        if (this.kittyTimer > 500 && this.random.nextInt(50) < 1) {
                            ((MoCEntityKitty) getTarget()).changeKittyState(7);
                            changeKittyState(19);
                        }
                    } else {
                        changeKittyState(9);
                    }
                    break;
                case 19: // Looking for kitty bed to give birth
                    if (this.random.nextInt(20) != 0) {
                        break;
                    }
                    MoCEntityKittyBed kittyBed2 = (MoCEntityKittyBed) getKittyStuff(this, 18D, false);
                    if (kittyBed2 == null || kittyBed2.isVehicle()) {
                        break;
                    }
                    float f11 = this.distanceTo(kittyBed2);
                    if (f11 > 2.0F) {
                        setPathToEntity(kittyBed2, f11);
                    }
                    if (f11 < 2.0F && this.startRiding(kittyBed2)) {
                        changeKittyState(20);
                    }
                    break;
                case 20: // Giving birth in kitty bed
                    if (this.getVehicle() == null) {
                        changeKittyState(19);
                        break;
                    }
                    this.setYRot(180F);
                    this.kittyTimer++;
                    if (this.kittyTimer <= 1000) {
                        break;
                    }
                    int i2 = this.random.nextInt(3) + 1;
                    for (int l2 = 0; l2 < i2; l2++) {
                        MoCEntityKitty kitty1 = MoCEntities.KITTY.get().create(this.level());
                        int babyType = this.getTypeMoC();
                        if (this.random.nextInt(2) < 1) {
                            babyType = (this.random.nextInt(8) + 1);
                        }
                        kitty1.setTypeMoC(babyType);
                        kitty1.setPos(this.getX(), this.getY(), this.getZ());
                        this.level().addFreshEntity(kitty1);
                        MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                        kitty1.setAdult(false);
                        kitty1.changeKittyState(10);
                    }
                    this.stopRiding();
                    changeKittyState(21);
                    break;
                case 21: // Defending kittens
                    this.kittyTimer++;
                    if (this.kittyTimer > 2000) {
                        List<Entity> list2 = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(24D, 8D, 24D), EntitySelector.NO_SPECTATORS.and(e -> e != this));
                        int i3 = 0;
                        for (Entity entity2 : list2) {
                            if (entity2 instanceof MoCEntityKitty && ((MoCEntityKitty) entity2).getKittyState() == 10) {
                                i3++;
                            }
                        }
                        if (i3 < 1) {
                            changeKittyState(7);
                            break;
                        }
                        this.kittyTimer = 1000;
                    }
                    if (getTarget() instanceof Player && this.random.nextInt(300) < 1) {
                        setTarget(null);
                    }
                    break;
                default:
                    changeKittyState(7);
                    break;
            }
        } else {
            super.aiStep();
        }
        // Dismount player on both sides to prevent desyncs
        if (this.isPassenger()) MoCTools.dismountSneakingPlayer(this);
    }

    public boolean onMaBack() {
        return getKittyState() == 15;
    }

    @Override
    public void tick() {
        super.tick();
        if (getIsSwinging()) {
            this.attackAnim += 0.2F;
            if (this.attackAnim > 2.0F) {
                setSwinging(false);
                this.attackAnim = 0.0F;
            }
        }
        if (!this.level().isClientSide() && getKittyState() == 16) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    private boolean pickable() {
        return getKittyState() != 13 && getKittyState() != 14 && getKittyState() != 15 && getKittyState() != 19 && getKittyState() != 20 && getKittyState() != 21;
    }

    @Override
    public boolean renderName() {
        return getKittyState() != 14 && getKittyState() != 15 && getKittyState() > 1 && super.renderName();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return getKittyState() > 2 || super.requiresCustomPersistence();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return getKittyState() <= 2 && super.removeWhenFarAway(distance);
    }

    public void swingArm() {
        // To synchronize, uses the packet handler to invoke the same method in the clients
        if (!this.level().isClientSide()) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new MoCMessageAnimation(this.getId(), 0));
        }
        if (!getIsSwinging()) {
            setSwinging(true);
            this.attackAnim = 0.0F;
        }
    }

    @Override
    public void performAnimation(int i) {
        swingArm();
    }

    public boolean upsideDown() {
        return getKittyState() == 14;
    }

    public boolean whipable() {
        return getKittyState() != 13;
    }

    public static boolean getCanSpawnHere(EntityType<MoCEntityAnimal> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        if (MoCreatures.proxy.kittyVillageChance <= 0)
            return MoCEntityAnimal.getCanSpawnHere(type, world, reason, pos, randomIn);
        BlockPos villagePos = world.findNearestMapStructure(net.minecraft.tags.StructureTags.VILLAGE, pos, 100, true);
        if (villagePos != null) {
            if (pos.distSqr(villagePos) <= 128 * 128) return MoCEntityAnimal.getCanSpawnHere(type, world, reason, pos, randomIn);
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Sitting", getIsSitting());
        tag.putInt("KittyState", getKittyState());
        tag.putInt("Temper", getTemper());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBooleanOr("Sitting", false));
        setKittyState(tag.getIntOr("KittyState", 0));
        setTemper(tag.getIntOr("Temper", 0));
    }


    @Override
    public void dropMyStuff() {
                    if (!this.level().isClientSide() && getIsTamed()) {
            MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.MEDALLION.get(), 1));
        }
    }

    @Override
    public void die(DamageSource damagesource) {
        dropMyStuff();
        super.die(damagesource);
    }

    @Override
    public boolean swimmerEntity() {
        return true;
    }

    @Override
    public int nameYOffset() {
        if (this.getIsSitting()) return -30;
        return -40;
    }
}

