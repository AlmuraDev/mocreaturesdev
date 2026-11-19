/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIPanicMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAquatic;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import drzhark.mocreatures.util.MoCTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.tags.TagKey;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.util.RandomSource;

public class MoCEntityDolphin extends MoCEntityTameableAquatic {

    private static final EntityDataAccessor<Boolean> IS_HUNGRY = SynchedEntityData.defineId(MoCEntityDolphin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EATEN = SynchedEntityData.defineId(MoCEntityDolphin.class, EntityDataSerializers.BOOLEAN);
    public int gestationtime;

    public MoCEntityDolphin(EntityType<? extends MoCEntityDolphin> type, Level world) {
        super(type, world);
        //setSize(1.3F, 0.605F);
        setAdult(true);
        // TODO: Make hitboxes adjust depending on size
        //setAge(60 + this.random.nextInt(100));
        setMoCAge(120);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIPanicMoC(this, 1.3D));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 1.0D, 30));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAquatic.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.5D)
            .add(Attributes.ATTACK_DAMAGE, 4.5D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 35) {
                setTypeMoC(1);
            } else if (i <= 60) {
                setTypeMoC(2);
            } else if (i <= 85) {
                setTypeMoC(3);
            } else if (i <= 96) {
                setTypeMoC(4);
            } else if (i <= 98) {
                setTypeMoC(5);
            } else {
                setTypeMoC(6);
            }
        }
    }

    @Override
    public Identifier getTexture() {

        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("dolphin_green.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("dolphin_purple.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("dolphin_black.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("dolphin_pink.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("dolphin_white.png");
            default:
                return MoCreatures.proxy.getModelTexture("dolphin_blue.png");
        }
    }

    @Override
    public int getMaxTemper() {

        switch (getTypeMoC()) {
            case 1:
                return 50;
            case 3:
                return 150;
            case 4:
                return 200;
            case 5:
                return 250;
            case 6:
                return 300;
            default:
                return 100;
        }
    }

    public int getInitialTemper() {
        switch (getTypeMoC()) {
            case 2:
                return 100;
            case 3:
                return 150;
            case 4:
                return 200;
            case 5:
                return 250;
            case 6:
                return 300;
            default:
                return 50;
        }
    }

    @Override
    public double getCustomSpeed() {
        switch (getTypeMoC()) {
            case 2:
                return 2.0D;
            case 3:
                return 2.5D;
            case 4:
                return 3.D;
            case 5:
                return 3.5D;
            case 6:
                return 4.D;
            default:
                return 1.5D;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HUNGRY, Boolean.FALSE);
        builder.define(HAS_EATEN, Boolean.FALSE);
    }

    public boolean getIsHungry() {
        return (this.entityData.get(IS_HUNGRY));
    }

    public void setIsHungry(boolean flag) {
        this.entityData.set(IS_HUNGRY, flag);
    }

    public boolean getHasEaten() {
        return (this.entityData.get(HAS_EATEN));
    }

    public void setHasEaten(boolean flag) {
        this.entityData.set(HAS_EATEN, flag);
    }

    //TODO
    /*@Override
    protected void attackEntity(Entity entity, float f) {
        if (attackTime <= 0 && (f < 3.5D) && (entity.getBoundingBox().maxY > getBoundingBox().minY)
                && (entity.getBoundingBox().minY < getBoundingBox().maxY) && (getAge() >= 100)) {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        }
    }*/

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i) && (this.level().getDifficulty().getId() > 0)) {
            Entity entity = damagesource.getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity entityliving = (LivingEntity) entity;
                if (this.isVehicle() && !this.getPassengers().isEmpty() && entity == this.getPassengers().get(0)) {
                    return true;
                }
                if (entity != this && this.getMoCAge() >= 100) {
                    setTarget(entityliving);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    private int Genetics(MoCEntityDolphin entitydolphin, MoCEntityDolphin entitydolphin1) {
        if (entitydolphin.getTypeMoC() == entitydolphin1.getTypeMoC()) {
            return entitydolphin.getTypeMoC();
        }
        int i = entitydolphin.getTypeMoC() + entitydolphin1.getTypeMoC();
        boolean flag = this.random.nextInt(3) == 0;
        boolean flag1 = this.random.nextInt(10) == 0;
        if ((i < 5) && flag) {
            return i;
        }
        if (((i == 5) || (i == 6)) && flag1) {
            return i;
        } else {
            return 0;
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_DOLPHIN_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_DOLPHIN_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_DOLPHIN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getAngrySound() {
        return MoCSoundEvents.ENTITY_DOLPHIN_UPSET.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {        
        return MoCLootTables.DOLPHIN;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && isInTag(stack.getItem(), MoCTags.Items.RAW_FISHES)) {
            if (!player.isCreative()) stack.shrink(1);
            if (!this.level().isClientSide()) {
                setTemper(getTemper() + 25);
                if (getTemper() >= getMaxTemper() && !getIsTamed()) {
                    // Tame the dolphin when temper reaches maximum
                    MoCTools.tameWithName(player, this);
                } else if (getTemper() > getMaxTemper()) {
                    setTemper(getMaxTemper() - 1);
                }

                if ((getHealth() + 15) > getMaxHealth()) {
                    this.setHealth(getMaxHealth());
                } else {
                    this.setHealth(getHealth() + 15);
                }

                if (!getIsAdult()) {
                    setMoCAge(getMoCAge() + 1);
                }
            }

            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());

            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && isInTag(stack.getItem(), MoCTags.Items.COOKED_FISHES) && getIsTamed() && getIsAdult()) {
            if (!player.isCreative()) stack.shrink(1);
            if ((getHealth() + 25) > getMaxHealth()) {
                this.setHealth(getMaxHealth());
            } else {
                this.setHealth(getHealth() + 25);
            }
            setHasEaten(true);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            return InteractionResult.SUCCESS;
        }
        if (!this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }
    
    // Helper method to check if item is in a tag
    private boolean isInTag(Item item, TagKey<Item> tag) {
        return item.builtInRegistryHolder().is(tag);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {

            //TODO
            /*if (!getIsHungry() && (this.random.nextInt(100) == 0)) {
                setIsHungry(true);
            }*/
            // fixes growth
            if (!getIsAdult() && (random.nextInt(50) == 0)) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= 150) {
                    setAdult(true);
                }
            }
            //TODO
            if ((!this.isVehicle()) && (this.deathTime == 0) && (!getIsTamed() || getIsHungry())) {
                ItemEntity entityitem = getClosestFish(this, 12D);
                if (entityitem != null) {
                    moveToNextEntity(entityitem);
                    ItemEntity entityitem1 = getClosestFish(this, 2D);
                    if ((this.random.nextInt(20) == 0) && (entityitem1 != null) && (this.deathTime == 0)) {

                        entityitem1.remove(Entity.RemovalReason.DISCARDED);
                        setTemper(getTemper() + 25);
                        if (getTemper() > getMaxTemper()) {
                            setTemper(getMaxTemper() - 1);
                        }
                        this.setHealth(getMaxHealth());
                    }
                }
            }
            if (!ReadyforParenting(this)) {
                return;
            }
            int i = 0;
            List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(8D, 2D, 8D), entity -> entity != this);
            for (Entity entity : list) {
                if (entity instanceof MoCEntityDolphin) {
                    i++;
                }
            }

            if (i > 1) {
                return;
            }
            List<Entity> list1 = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(4D, 2D, 4D), entity -> entity != this);
            for (Entity entity1 : list1) {
                if (!(entity1 instanceof MoCEntityDolphin) || (entity1 == this)) {
                    continue;
                }
                MoCEntityDolphin entitydolphin = (MoCEntityDolphin) entity1;
                if (!ReadyforParenting(this) || !ReadyforParenting(entitydolphin)) {
                    continue;
                }
                if (this.random.nextInt(100) == 0) {
                    this.gestationtime++;
                }
                if (this.gestationtime % 3 == 0) {
                    MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHeart(this.getId()));
                }
                if (this.gestationtime <= 50) {
                    continue;
                }
                
                // Create a baby dolphin
                EntityType<?> dolphinType = MoCEntities.DOLPHIN.get();
                if (dolphinType != null) {
                    Entity babyEntity = dolphinType.create(this.level(), EntitySpawnReason.BREEDING);
                    if (babyEntity instanceof MoCEntityDolphin) {
                        MoCEntityDolphin babydolphin = (MoCEntityDolphin) babyEntity;
                        babydolphin.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                        if (this.level().addFreshEntity(babydolphin)) {
                            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                            setHasEaten(false);
                            entitydolphin.setHasEaten(false);
                            this.gestationtime = 0;
                            entitydolphin.gestationtime = 0;
                            int l = Genetics(this, entitydolphin);
                            babydolphin.setMoCAge(35);
                            babydolphin.setAdult(false);
                            babydolphin.setOwnerId(this.getOwnerId());
                            babydolphin.setTamed(true);
                            UUID ownerId = this.getOwnerId();
                            Player entityplayer = null;
                            if (ownerId != null) {
                                entityplayer = this.level().getPlayerByUUID(this.getOwnerId());
                            }
                            if (entityplayer != null) {
                                MoCTools.tameWithName(entityplayer, babydolphin);
                            }
                            babydolphin.setTypeInt(l);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean ReadyforParenting(MoCEntityDolphin entitydolphin) {
        LivingEntity passenger = (LivingEntity) this.getControllingPassenger();
        return !entitydolphin.isVehicle() && (passenger == null) && entitydolphin.getIsTamed()
                && entitydolphin.getHasEaten() && entitydolphin.getIsAdult();
    }



    /*@Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }*/

    @Override
    protected boolean usesNewAI() {
        return true;
    }

    @Override
    public float getSpeed() {
        return 0.15F;
    }

    @Override
    public boolean isMovementCeased() {
        return !isInWater();
    }

    @Override
    protected double minDivingDepth() {
        return 0.4D;
    }

    @Override
    protected double maxDivingDepth() {
        return 4.0D;
    }

    @Override
    public int getMaxAge() {
        return 160;
    }

    // We need to use positionRider for proper passenger positioning in 1.20.1
    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double dist = (0.8D);
            double newPosX = this.getX() + (dist * Math.sin(this.yBodyRot / 57.29578F));
            double newPosZ = this.getZ() - (dist * Math.cos(this.yBodyRot / 57.29578F));
            moveFunction.accept(passenger, newPosX, this.getY() + getMountedYOffset() + (-passenger.getVehicleAttachmentPoint(this).y), newPosZ);
        }
    }

    public double getMountedYOffset() {
        return this.getMoCAge() * 0.01F * (this.getBbHeight() * 0.3D);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.315F));
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof Player && this.getIsTamed();
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isVehicle()) {
            Entity entity = this.getPassengers().get(0);
            if (entity instanceof LivingEntity) {
                return (LivingEntity) entity;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        
        // Server-side bucking and taming logic for untamed dolphins with riders
        if (!this.level().isClientSide() && this.isVehicle() && !this.getIsTamed()) {
            Entity passenger = this.getControllingPassenger();
            if (passenger != null && passenger instanceof Player) {
                Player player = (Player) passenger;
                
                // Check if rider is underwater - if not, more likely to buck them off
                boolean riderUnderwater = passenger.isEyeInFluid(FluidTags.WATER);
                int buckingChance = riderUnderwater ? 50 : 15; // Much more likely to buck if rider isn't underwater
                
                // Use deterministic random for consistent behavior
                long seed = this.getId() + this.tickCount;
                RandomSource syncRandom = RandomSource.create(seed);
                
                if (syncRandom.nextInt(buckingChance) == 0) {
                    // Server-side dismount with proper position sync to prevent rubberband
                    // 1. Calculate a safe dismount position near the entity
                    double offsetX = syncRandom.nextGaussian() * 0.5D;
                    double offsetZ = syncRandom.nextGaussian() * 0.5D;
                    double dismountX = this.getX() + offsetX;
                    double dismountY = this.getY();
                    double dismountZ = this.getZ() + offsetZ;
                    
                    // 2. Force dismount on server side
                    passenger.stopRiding();
                    
                    // 3. Immediately set the passenger's position to prevent client desync
                    passenger.teleportTo(dismountX, dismountY, dismountZ);
                    
                    // 4. Clear entity state and force updates
                    this.ejectPassengers();
                    this.refreshDimensions();
                    this.jumpFromGround();
                    
                    // 5. Force position sync by marking both entities as dirty
                    passenger.setPos(dismountX, dismountY, dismountZ);
                    this.setPos(this.getX(), this.getY(), this.getZ());
                    
                    MoCreatures.LOGGER.info("DOLPHIN_TICK: {} successfully bucked rider off! (rider underwater: {})", 
                        this.getType().getDescription().getString(), riderUnderwater);
                }
                
                // Pure RNG taming chance while riding (server-side only for state changes)
                // 1 in 400 chance per tick to tame while riding (about 20 seconds on average)
                // Use same deterministic random for consistency
                if (syncRandom.nextInt(400) == 0) {
                    MoCTools.tameWithName(player, this);
                    MoCreatures.LOGGER.info("DOLPHIN_TICK: {} RNG taming successful while being ridden!", 
                        this.getType().getDescription().getString());
                }
            }
        }
    }
}
