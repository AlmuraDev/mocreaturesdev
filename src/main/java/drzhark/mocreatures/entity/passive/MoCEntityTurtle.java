/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowOwnerPlayer;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class MoCEntityTurtle extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> IS_UPSIDE_DOWN = SynchedEntityData.defineId(MoCEntityTurtle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HIDING = SynchedEntityData.defineId(MoCEntityTurtle.class, EntityDataSerializers.BOOLEAN);
    private boolean isSwinging;
    private boolean twistright;
    private int flopcounter;

    public MoCEntityTurtle(EntityType<? extends MoCEntityTurtle> type, Level world) {
        super(type, world);
        //setSize(0.6F, 0.425F);
        setAdult(false);
        // TODO: Make hitboxes adjust depending on size
        //setAge(60 + this.random.nextInt(50));
        setMoCAge(90);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIFollowOwnerPlayer(this, 0.8D, 2F, 10F));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 0.8D, 50));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes().add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.ARMOR, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.15D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_UPSIDE_DOWN, Boolean.FALSE);
        // rideable: 0 nothing, 1 saddle
        builder.define(IS_HIDING, Boolean.FALSE);
        // rideable: 0 nothing, 1 saddle
    }

    @Override
    public Identifier getTexture() {
        String tempText = "turtle.png";

        if (MoCreatures.proxy.easterEggs) {
            if (getPetName().equals("Donatello") || getPetName().equals("donatello")) {
                tempText = "turtle_donatello.png";
            }

            if (getPetName().equals("Leonardo") || getPetName().equals("leonardo")) {
                tempText = "turtle_leonardo.png";
            }

            if (getPetName().equals("raphael") || getPetName().equals("Raphael")) {
                tempText = "turtle_raphael.png";
            }

            if (getPetName().equals("Michelangelo") || getPetName().equals("michelangelo")) {
                tempText = "turtle_michelangelo.png";
            }
        }

        return MoCreatures.proxy.getModelTexture(tempText);
    }

    public boolean getIsHiding() {
        return this.entityData.get(IS_HIDING);
    }

    public void setIsHiding(boolean flag) {
        this.entityData.set(IS_HIDING, flag);
    }

    public boolean getIsUpsideDown() {
        return this.entityData.get(IS_UPSIDE_DOWN);
    }

    public void setIsUpsideDown(boolean flag) {
        this.flopcounter = 0;
        this.attackAnim = 0.0F;
        this.entityData.set(IS_UPSIDE_DOWN, flag);
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getVehicle() instanceof Player) {
            if (this.getVehicle().isCrouching()) {
                return -0.25D + ((300D - this.getMoCAge()) / 500D);
            }
            return (300D - this.getMoCAge()) / 500D;
        }

        return super.getPassengersRidingOffset();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        if (getIsTamed()) {
            if (getIsUpsideDown()) {
                flipflop(false);
                return InteractionResult.SUCCESS;
            }
            if (this.getVehicle() == null) {
                if (this.startRiding(player)) {
                    this.setYRot(player.getYRot());
                }
            }
            return InteractionResult.SUCCESS;
        }

        flipflop(!getIsUpsideDown());

        return super.mobInteract(player, hand);
    }

    @Override
    public void jumpFromGround() {
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.3D, this.getDeltaMovement().z);
            if (isSprinting()) {
                float f = this.getYRot() * 0.01745329F;
                this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(f) * -0.2F, 0.0D, Mth.cos(f) * 0.2F));
            }
            this.hasImpulse = true;
        }
    }

    @Override
    public boolean isNotScared() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (!getIsUpsideDown() && !getIsTamed()) {
                LivingEntity entityliving = getBoogey(4D);
                if ((entityliving != null) && this.hasLineOfSight(entityliving)) {
                    if (!getIsHiding() && !isInWater()) {
                        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_TURTLE_ANGRY.get());
                        setIsHiding(true);
                    }

                    this.getNavigation().stop();
                } else {

                    setIsHiding(false);
                    if (!this.getNavigation().isInProgress() && this.random.nextInt(50) == 0) {
                        ItemEntity entityitem = this.getClosestItem(this, 10D, Ingredient.of(Items.MELON, Items.SUGAR_CANE));
                        if (entityitem != null) {
                            float f = entityitem.distanceTo(this);
                            if (f > 2.0F) {
                                setPathToEntity(entityitem, f);
                            }
                            if (f < 2.0F && this.deathTime == 0) {
                                entityitem.remove(RemovalReason.DISCARDED);
                                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_TURTLE_EATING.get());
                                Player entityplayer = this.level().getNearestPlayer(this, 24D);
                                if (entityplayer != null) {
                                    MoCTools.tameWithName(entityplayer, this);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if (this.getVehicle() != null) {
            return false;
        }
        if (entity == null) {
            return super.hurtServer(serverLevel, damagesource, i);
        }
        if (getIsHiding()) {
            if (this.random.nextInt(10) == 0) {
                flipflop(true);
            }
            return false;
        } else {
            boolean flag = super.hurtServer(serverLevel, damagesource, i);
            if (this.random.nextInt(3) == 0) {
                flipflop(true);
            }
            return flag;
        }
    }

    public void flipflop(boolean flip) {
        setIsUpsideDown(flip);
        setIsHiding(false);
        this.getNavigation().stop();
    }

    @Override
    public boolean entitiesToIgnore(Entity entity) {
        return (entity instanceof MoCEntityTurtle) || ((entity.getBbHeight() <= this.getBbHeight()) && (entity.getBbWidth() <= this.getBbWidth()))
                || super.entitiesToIgnore(entity);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if ((this.getVehicle() != null) && (this.getVehicle() instanceof Player)) {
            Player entityplayer = (Player) this.getVehicle();
            this.setYRot(entityplayer.getYRot());
        }

        if (getIsSwinging()) {
            this.attackAnim += 0.2F;
            if (this.attackAnim > 8F) {
                setSwinging(false);
            }
        } else {
            this.attackAnim = 0.0F;
        }

        if (getIsUpsideDown()) {
            this.flopcounter++;
            this.getNavigation().stop();
            if (this.flopcounter > 50 && this.random.nextInt(30) == 0) {
                setIsUpsideDown(false);
            }
        }
    }

    public boolean getIsSwinging() {
        return this.isSwinging;
    }

    public void setSwinging(boolean flag) {
        this.isSwinging = flag;
    }

    @Override
    public boolean isMovementCeased() {
        return getIsUpsideDown() || getIsHiding();
    }

    public int getFlipDirection() {
        if (this.twistright) {
            return 1;
        }
        this.twistright = !this.twistright;
        return -1;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("UpsideDown", getIsUpsideDown());
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        // Older turtles were marked adult at age 90 and could never reach max age.
        if (getMoCAge() < getMoCMaxAge()) setAdult(false);
        setIsUpsideDown(nbttagcompound.getBooleanOr("UpsideDown", false));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_TURTLE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_TURTLE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_TURTLE_DEATH.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.TURTLE;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(serverLevel, serverLevel, recentlyHitIn);

        // chance to drop turtle helmet
        if (!this.level().isClientSide() && !this.isBaby() && this.random.nextInt(10) == 0) {
            ItemStack helmet = new ItemStack(Items.TURTLE_HELMET);
            this.spawnAtLocation(helmet);
        }

        // teenage mutant ninja turtles drop weapons
        if (isTMNT() && !this.level().isClientSide()) {
            if (getPetName().equalsIgnoreCase("Donatello")) {
                ItemStack weapon = new ItemStack(MoCItems.BO.get());
                weapon.setHoverName(Component.translatable("mocreatures.ninjaturtle.bo"));
                this.spawnAtLocation(weapon);
            }

            if (getPetName().equalsIgnoreCase("Leonardo")) {
                ItemStack weapon = new ItemStack(MoCItems.KATANA.get());
                weapon.setHoverName(Component.translatable("mocreatures.ninjaturtle.katana"));
                this.spawnAtLocation(weapon);
            }

            if (getPetName().equalsIgnoreCase("Raphael")) {
                ItemStack weapon = new ItemStack(MoCItems.SAI.get());
                weapon.setHoverName(Component.translatable("mocreatures.ninjaturtle.sai"));
                this.spawnAtLocation(weapon);
            }

            if (getPetName().equalsIgnoreCase("Michelangelo")) {
                ItemStack weapon = new ItemStack(MoCItems.NUNCHAKU.get());
                weapon.setHoverName(Component.translatable("mocreatures.ninjaturtle.nunchaku"));
                this.spawnAtLocation(weapon);
            }
        }
    }

    public boolean isTMNT() {
        if (!getIsTamed() || !MoCreatures.proxy.easterEggs) {
            return false;
        }

        return (getPetName().equalsIgnoreCase("Donatello") || getPetName().equalsIgnoreCase("Leonardo")
                || getPetName().equalsIgnoreCase("Raphael") || getPetName().equalsIgnoreCase("Michelangelo"));
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == Items.SUGAR_CANE || stack.getItem() == Items.MELON);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public int nameYOffset() {
        return -10 - (getMoCAge() / 5);
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    public boolean isAmphibian() {
        return true;
    }

    @Override
    public float getSpeed() {
        if (isInWater() && !isMovementCeased()) {
            return 0.25F;
        }
        return 0.12F;
    }

    @Override
    protected double minDivingDepth() {
        return (getMoCAge() + 8D) / 340D;
    }

    @Override
    protected double maxDivingDepth() {
        return (this.getMoCAge() / 100D);
    }

    @Override
    public int getMoCMaxAge() {
        return 120;
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.525F;
    }
}
