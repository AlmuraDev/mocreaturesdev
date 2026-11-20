/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity;
import net.minecraft.locale.Language;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIMoverHelperMoC;
import drzhark.mocreatures.entity.tameable.IMoCTameable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook; // in 1.20.1, FishingBobberEntity is now FishingHook
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Updated for Minecraft 1.20.1 (Forge/Yarn).  Most of the "old" 1.16.5 methods
 * and classes have been swapped out for their 1.20.1 equivalents.
 */
public abstract class MoCEntityAquatic extends WaterAnimal implements IMoCEntity {

    // === Data parameters (formerly EntityDataManager + DataSerializers) ===
    private static final EntityDataAccessor<Boolean> ADULT =
            SynchedEntityData.defineId(MoCEntityAquatic.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TYPE =
            SynchedEntityData.defineId(MoCEntityAquatic.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AGE =
            SynchedEntityData.defineId(MoCEntityAquatic.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> NAME_STR =
            SynchedEntityData.defineId(MoCEntityAquatic.class, EntityDataSerializers.STRING);

    protected boolean fishHooked;
    protected boolean divePending;
    protected boolean jumpPending;
    protected boolean isEntityJumping;
    protected int outOfWater;
    protected boolean riderIsDisconnecting;
    protected float moveSpeed;
    protected String texture;
    protected PathNavigation navigatorWater;
    protected int temper;
    private boolean diving;
    private int divingCount;
    private int mountCount;
    private boolean updateDivingDepth = false;
    private double divingDepth;

    public MoCEntityAquatic(EntityType<? extends MoCEntityAquatic> type, Level level) {
        super(type, level);
        this.outOfWater = 0;
        setTemper(50);
        this.setNewDivingDepth();
        this.riderIsDisconnecting = false;
        this.texture = "blank.jpg";
        this.navigatorWater = new WaterBoundPathNavigation(this, level);
        //this.moveControl = new EntityAIMoverHelperMoC(this);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 5, 0.01F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
protected PathNavigation createNavigation(Level level) {
    return new WaterBoundPathNavigation(this, level);
}

// 1.20.1 – stop water from steering the mob
@Override
public boolean isPushedByFluid(FluidType type) {
    return false;
}


    // === ATTRIBUTE REGISTRATION ===
    /**
     * In 1.20.1, attributes are registered via a static method returning
     * AttributeSupplier.Builder.  Forge will pick this up from your
     * DeferredRegister<EntityType<?>> setup.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.MAX_HEALTH, 6.0D);
    }

    // === SYNCHED DATA DEFINITION ===
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ADULT, false);
        builder.define(TYPE, 0);
        builder.define(AGE, 45);
        builder.define(NAME_STR, "");
    }

    // === NAMING / VERBOSE NAMES (CLIENT‐ONLY) ===
    @Override
    public Component getName() {
        String entityKey = this.getType().getDescriptionId();
        if (!MoCreatures.proxy.verboseEntityNames || entityKey == null) {
            return super.getName();
        }
        String translationKey = "entity." + entityKey + ".verbose.name";
        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey);
        }
        return super.getName();
    }

    // === SPAWN / TYPE SELECTION ===
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectType();
        return super.finalizeSpawn(world, difficulty, reason, spawnData);
    }

    @Override
    public void selectType() {
        setTypeMoC(1);
    }

    // === DATA ACCESSORS (formerly getTypeMoC(), setTypeMoC(), etc.) ===
    @Override
    public int getTypeMoC() {
        return this.entityData.get(TYPE);
    }

    @Override
    public void setTypeMoC(int i) {
        this.entityData.set(TYPE, i);
    }

    @Override
    public int getOwnerPetId() {
        return -1;
    }

    @Override
    public void setOwnerPetId(int i) { }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return null;
    }

    @Override
    public boolean getIsTamed() {
        return false;
    }

    @Override
    public boolean getIsAdult() {
        return this.entityData.get(ADULT);
    }

    @Override
    public void setAdult(boolean flag) {
        this.entityData.set(ADULT, flag);
    }

    @Override
    public String getPetName() {
        return this.entityData.get(NAME_STR);
    }

    @Override
    public void setPetName(String name) {
        this.entityData.set(NAME_STR, name);
    }

    @Override
    public int getMoCAge() {
        return this.entityData.get(AGE);
    }

    @Override
    public void setMoCAge(int i) {
        this.entityData.set(AGE, i);
        if (getMoCAge() >= getMaxAge()) {
            setAdult(true);
        }
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int i) {
        this.temper = i;
    }

    public int getMaxTemper() {
        return 100;
    }

    // === HELPER FOR SMOOTH ROTATION ===
    public float clampAngle(float current, float target, float maxDelta) {
        float delta = target - current;
        while (delta < -180F) {
            delta += 360F;
        }
        while (delta >= 180F) {
            delta -= 360F;
        }
        if (delta > maxDelta) {
            delta = maxDelta;
        }
        if (delta < -maxDelta) {
            delta = -maxDelta;
        }
        return current + delta;
    }

    public void faceItem(double x, double y, double z, float maxAngleDiff) {
        double dx = x - this.getX();
        double dy = z - this.getZ();
        double dz = y - this.getY();
        double horizontalDist = Math.sqrt(dx * dx + dy * dy);
        float yawTarget = (float) (Math.atan2(dy, dx) * (180D / Math.PI)) - 90F;
        float pitchTarget = (float) (Math.atan2(dz, horizontalDist) * (180D / Math.PI));
        this.setXRot(-clampAngle(this.getXRot(), pitchTarget, maxAngleDiff));
        this.setYRot(clampAngle(this.getYRot(), yawTarget, maxAngleDiff));
    }

    // === BIOME & STEP SOUND OVERRIDES ===
    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason reason) {
        // Always allow spawn eggs to work
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        // Check if the entity is in water
        BlockPos pos = this.blockPosition();
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        // no step sound for aquatic
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    // === FINDS CLOSEST FISH ITEM ENTITY IN WATER ===
    /**
     * Find the nearest ItemEntity within `radius` that is tagged as a fish and is in water.
     */
    public ItemEntity getClosestFish(Entity seeker, double radius) {
        double closestSq = -1D;
        ItemEntity closest = null;

        // Build an AABB around this entity with the given radius
        AABB searchBox = this.getBoundingBox().inflate(radius);

        // Get all ItemEntity instances in the box
        List<ItemEntity> itemsInRange = this.level().getEntitiesOfClass(
                ItemEntity.class,
                searchBox,
                // Filter lambda: only keep those whose ItemStack is in the FISHES tag and that are in water
                itemEntity -> {
                    ItemStack stack = itemEntity.getItem();
                    return stack.is(ItemTags.FISHES) && itemEntity.isInWater();
                }
        );

        // Iterate to find the absolute closest one
        for (ItemEntity fishEntity : itemsInRange) {
            double sqDist = fishEntity.distanceToSqr(seeker);
            if ((radius < 0.0D || sqDist < (radius * radius)) &&
                    (closest == null || sqDist < closestSq)) {
                closestSq = sqDist;
                closest = fishEntity;
            }
        }

        return closest;
    }


    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    // === JUMP & JUMP FLAGS ===
    public double getCustomJump() {
        return 0.4D;
    }

    public boolean getIsJumping() {
        return this.isEntityJumping;
    }

    public void setIsJumping(boolean flag) {
        this.isEntityJumping = flag;
    }

    @Override
    public void jumpFromGround() {
        this.jumpPending = true;
    }

    protected void moveToNextEntity(Entity target) {
        if (target != null) {
            int i = Mth.floor(target.getX());
            int j = Mth.floor(target.getY());
            int k = Mth.floor(target.getZ());
            faceItem(i, j, k, 30F);

            Vec3 motion = this.getDeltaMovement();
            if (this.getX() < i) {
                double dx = target.getX() - this.getX();
                if (dx > 0.5D) {
                    motion = motion.add(0.05D, 0.0D, 0.0D);
                }
            } else {
                double dx = this.getX() - target.getX();
                if (dx > 0.5D) {
                    motion = motion.subtract(0.05D, 0.0D, 0.0D);
                }
            }
            if (this.getZ() < k) {
                double dz = target.getZ() - this.getZ();
                if (dz > 0.5D) {
                    motion = motion.add(0.0D, 0.0D, 0.05D);
                }
            } else {
                double dz = this.getZ() - target.getZ();
                if (dz > 0.5D) {
                    motion = motion.subtract(0.0D, 0.0D, 0.05D);
                }
            }
            this.setDeltaMovement(motion);
        }
    }

    public double getCustomSpeed() {
        return 1.5D;
    }

    @Override
    public boolean isDiving() {
        return this.diving;
    }

    @Override
    public void makeEntityJump() {
        if (this.isVehicle() && this.isInWater()) {
            this.jumpPending = true;
        }
    }

    public void riding() {
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player) {
            Player rider = (Player) this.getControllingPassenger();
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D));
            for (Entity ent : list) {
                if (ent.isRemoved()) {
                    continue;
                }
                ent.playerTouch(rider);
                if (!(ent instanceof Monster)) {
                    continue;
                }
                float dist = (float) this.distanceTo(ent);
                if (dist < 2.0F && this.random.nextInt(10) == 0) {
                    ent.hurtOrSimulate(
                            ent.level().damageSources().mobAttack((LivingEntity) ent),
                            (float) ((Monster) ent).getAttributeValue(Attributes.ATTACK_DAMAGE)
                    );
                }
            }
            if (rider.isSecondaryUseActive()) {
                this.makeEntityDive();
            }
        }
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            if (this.isVehicle() && this.getControllingPassenger() instanceof Player) {
                riding();
                this.mountCount = 1;
            }

            if (this.mountCount > 0 && ++this.mountCount > 50) {
                this.mountCount = 0;
            }
            if (getMoCAge() == 0) {
                setMoCAge(getMaxAge() - 10); // fixes tiny creatures spawned by error
            }
            if (!getIsAdult() && this.random.nextInt(300) == 0) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= getMaxAge()) {
                    setAdult(true);
                }
            }

            // Conditional navigation tick for aquatic entities in water
            // Only tick when in water to prevent memory leaks on land
            if (this.isInWater() && this.tickCount % 2 == 0) {
                this.getNavigation().tick();
            }

            // Update diving depth after movement - reduced frequency to prevent memory leaks
            if (!this.getNavigation().isDone()) {
                // Only update diving depth every few ticks instead of every tick
                if (this.tickCount % 3 == 0 && !this.updateDivingDepth) {
                    float targetDepth = MoCTools.distanceToSurface(
                            this.moveControl.getWantedX(),
                            this.moveControl.getWantedY(),
                            this.moveControl.getWantedZ(),
                            this.level()
                    );
                    setNewDivingDepth(targetDepth);
                    this.updateDivingDepth = true;
                }
                // Reset the flag periodically to prevent it from getting stuck
                else if (this.tickCount % 10 == 0) {
                    this.updateDivingDepth = false;
                }
            } else {
                this.updateDivingDepth = false;
            }

            if (isMovementCeased() || this.random.nextInt(200) == 0) {
                this.getNavigation().stop();
            }

            if (isFisheable() && !this.fishHooked && this.random.nextInt(30) == 0) {
                getFished();
            }

            if (this.fishHooked && this.random.nextInt(200) == 0) {
                this.fishHooked = false;
                List<FishingHook> hooks = this.level().getEntitiesOfClass(
                        FishingHook.class,
                        this.getBoundingBox().inflate(2.0D),
                        hook -> hook.getHookedIn() == this
                );
                for (FishingHook hook : hooks) {
                    // TODO: Call hook.onHit (Reflection might be needed, as onEntityHit is private).
                    // hook.onEntityHit(new EntityRayTraceResult(this));
                }
                // Clear the list reference to help GC
                hooks.clear();
            }
        }

        this.moveSpeed = 0.7F;

        if (isSwimming()) {
            this.outOfWater = 0;
            this.setAirSupply(800);
        } else {
            this.outOfWater++;
            Vec3 motion = this.getDeltaMovement().subtract(0.0, 0.1, 0.0);
            this.setDeltaMovement(motion);

            if (this.onGround() && this.outOfWater % 20 == 0) {
                // Flop around if out of water
                Vec3 randomMotion = new Vec3(
                        (this.random.nextDouble() - 0.5D) * 0.5D,
                        0.4D,
                        (this.random.nextDouble() - 0.5D) * 0.5D
                );
                this.setDeltaMovement(randomMotion);
                this.setOnGround(false);
            }

            if (this.outOfWater > 20) {
                this.getNavigation().stop();
            }
            if (this.outOfWater > 300 && (this.outOfWater % 40) == 0) {
                Vec3 randMotion = this.getDeltaMovement().add(
                        (this.random.nextDouble() * 0.2D - 0.1D),
                        0.3D,
                        (this.random.nextDouble() * 0.2D - 0.1D)
                );
                this.setDeltaMovement(randMotion);
                this.hurtOrSimulate(this.damageSources().drown(), 1.0F);
            }
        }

        if (!this.diving) {
            if (!this.isVehicle() && this.getTarget() == null && !this.getNavigation().isDone() && this.random.nextInt(500) == 0) {
                this.diving = true;
            }
        } else {
            this.divingCount++;
            if (this.divingCount > 100 || this.isVehicle()) {
                this.diving = false;
                this.divingCount = 0;
            }
        }

        super.aiStep();
    }

    public boolean isSwimming() {
        return this.isEyeInFluid(FluidTags.WATER);
    }

    // === NBT READ/WRITE (1.20.1) ===
    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Adult", getIsAdult());
        compound.putInt("Edad", getMoCAge());
        compound.putString("Name", getPetName());
        compound.putInt("TypeInt", getTypeMoC());
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        setAdult(compound.getBooleanOr("Adult", false));
        setMoCAge(compound.getIntOr("Edad", 0));
        setPetName(compound.getStringOr("Name", ""));
        setTypeMoC(compound.getIntOr("TypeInt", 0));
    }

    public void setTypeInt(int i) {
        setTypeMoC(i);
        selectType();
    }

    @Override
    public void performAnimation(int attackType) {
    }

    @Override
    public void checkDespawn() {
        if (this.isPersistenceRequired() || this.requiresCustomPersistence()
                || (this instanceof IMoCTameable pet && pet.getIsTamed())) {
            this.noActionTime = 0;
            return;
        }
        Player player = this.level().getNearestPlayer(this, -1.0D);
        if (player != null) {
            double d0 = player.distanceToSqr(this);
            int instantDist = this.getType().getCategory().getDespawnDistance();
            int j = instantDist * instantDist;
            if (d0 > (double) j && this.removeWhenFarAway(d0)) {
                this.playerTouch(player); // or simply remove
                this.remove(RemovalReason.DISCARDED);
            }
            int randomDist = this.getType().getCategory().getDespawnDistance();
            int l = randomDist * randomDist;
            if (this.tickCount > 1800 && this.random.nextInt(800) == 0 && d0 > (double) l && this.removeWhenFarAway(d0)) {
                this.remove(RemovalReason.DISCARDED);
            } else if (d0 < (double) l) {
                this.noActionTime = 0;
            }
        }
    }

    @Override
    public int nameYOffset() {
        return 0;
    }

    @Override
    public boolean renderName() {
        return MoCreatures.proxy.getDisplayPetName()
                && !getPetName().isEmpty()
                && !this.isVehicle();
    }

    @Override
    public void makeEntityDive() {
        this.divePending = true;
    }

    @Override
    public float getSizeFactor() {
        return 1.0F;
    }

    @Override
    public float getAdjustedYOffset() {
        return 0F;
    }

    /**
     * Spawn rule for all Mo' Creatures water mobs, with the light level check. Named apart from WaterAnimal's
     * checkSurfaceWaterAnimalSpawnRules on purpose: with the same name, MoCEntities' method references resolved to
     * the vanilla one, so the vanilla sea-level rule ran instead of this one.
     */
    public static boolean checkMoCWaterSpawnRules(EntityType<? extends MoCEntityAquatic> type, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        // Always allow spawn eggs
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        
        // Any water at any height and depth, as in 1.12.2 (the IN_WATER placement already keeps solid blocks off the spot
        // above). Vanilla's rule also needs the spot to be within 13 blocks below sea level with water above and below it,
        // which keeps fish out of lakes above sea level and out of lakes less than 3 deep (both common in TerrainControl worlds).
        boolean willSpawn = world.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER);

        // Add light level check from BiomeSpawnConfig if this is a Level (not just LevelAccessor)
        if (willSpawn && world.getLevel() instanceof Level) {
            willSpawn = drzhark.mocreatures.config.biome.BiomeSpawnConfig.checkLightLevelForEntity((Level) world.getLevel(), pos, type);
        }

        if (MoCreatures.proxy.debug && willSpawn) {
            MoCreatures.LOGGER.info(
                    "Aquatic: {} at {} State: {} biome: {}",
                    type.getDescription(),
                    pos,
                    world.getBlockState(pos),
                    MoCTools.biomeName((Level) world.getLevel(), pos)
            );
        }
        
        return willSpawn;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (usesNewAI()) {
            return super.hurtServer(serverLevel, source, amount);
        }

        if (isNotScared()) {
            LivingEntity oldTarget = this.getTarget();
            setTarget(oldTarget);
            return super.hurtServer(serverLevel, source, amount);
        }

        return super.hurtServer(serverLevel, source, amount);
    }

    protected boolean canBeTrappedInNet() {
        return (this instanceof IMoCTameable) && getIsTamed();
    }

    protected void dropMyStuff() { }

    protected boolean isMyHealFood(ItemStack stack) {
        return false;
    }

    @Override
    public void setArmorType(int i) {
    }

    @Override
    public float pitchRotationOffset() {
        return 0F;
    }

    @Override
    public float rollRotationOffset() {
        return 0F;
    }

    // === FISHINGHOOK INTERACTION ===
    private void getFished() {
        Player nearest = this.level().getNearestPlayer(this, 18D);
        if (nearest != null) {
            FishingHook fishHook = nearest.fishing;
            if (fishHook != null && fishHook.getHookedIn() == null) {
                float dist = fishHook.distanceTo(this);
                if (dist > 1F) {
                    MoCTools.setPathToEntity(this, fishHook, dist);
                } else {
                    // TODO: call fishHook.onHit(new EntityRayTraceResult(this));
                    this.fishHooked = true;
                }
            }
        }
    }

    protected boolean isFisheable() {
        return false;
    }

    @Override
    public float getAdjustedZOffset() {
        return 0F;
    }

    @Override
    public float getAdjustedXOffset() {
        return 0F;
    }

    @Override
    public boolean isNotScared() {
        return false;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean getIsSitting() {
        return false;
    }

    @Override
    public boolean shouldAttackPlayers() {
        return !getIsTamed() && this.level().getDifficulty() != Difficulty.PEACEFUL;
    }

    // === TRAVEL / MOVEMENT ===
    @Override
    public void travel(Vec3 movementInput) {
        // MoCreatures.LOGGER.info("TRAVEL: {} called - isVehicle: {}, inWater: {}, movementInput: {}", 
        //     this.getType().getDescription().getString(), 
        //     this.isVehicle(), 
        //     this.isInWater(),
        //     movementInput);
            
        if (this.isVehicle()) {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof LivingEntity) {
                // MoCreatures.LOGGER.info("TRAVEL: {} has rider {}, calling moveWithRider", 
                //     this.getType().getDescription().getString(), 
                //     passenger.getType().getDescription().getString());
                this.moveWithRider(movementInput, (LivingEntity) passenger);
                return;
            }
        }
        
        // Non-ridden movement
        if (this.isInWater()) {
            // MoCreatures.LOGGER.info("TRAVEL: {} non-ridden water movement", this.getType().getDescription().getString());
            this.moveRelative(0.1F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8999999761581421D));

            if (this.getTarget() == null && this.getNavigation().isDone()) {
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.005, 0.0));
            }

            this.calculateEntityAnimation(true); // sets limbSwing, etc.
        } else {
            // MoCreatures.LOGGER.info("TRAVEL: {} non-ridden land movement", this.getType().getDescription().getString());
            super.travel(movementInput);
        }
    }

    /**
     * Riding code when ridden by a player or other entity.
     * Based on MoCEntityAnimal pattern but adapted for aquatic creatures.
     */
    public void moveWithRider(Vec3 movementInput, LivingEntity passenger) {
        if (!this.isVehicle() || passenger == null) {
            // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} not vehicle or no passenger", this.getType().getDescription().getString());
            return;
        }
        
        // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} with passenger {}, tamed: {}, swimming: {}", 
        //     this.getType().getDescription().getString(),
        //     passenger.getType().getDescription().getString(),
        //     this.getIsTamed(),
        //     this.isSwimming());
            
        // Untamed behavior
        if (!getIsTamed()) {
            // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} calling untamed behavior", this.getType().getDescription().getString());
            this.moveEntityWithRiderUntamed(movementInput, passenger);
            return;
        }
        
        // Tamed behavior - align rotation with rider
        // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} tamed movement - inputs: forward={}, strafe={}, jump={}, dive={}", 
        //     this.getType().getDescription().getString(),
        //     passenger.zza,
        //     passenger.xxa,
        //     this.jumpPending,
        //     this.divePending);
            
        this.setYRot(passenger.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(passenger.getXRot() * 0.5F);
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.yBodyRot;
        
        // Convert rider inputs to movement vector (reduced speed for aquatic)
        movementInput = new Vec3(
            passenger.xxa * 0.2F,  // reduced strafe speed
            movementInput.y,
            passenger.zza * 0.4F   // reduced forward speed
        );
        
        // Prevent sinking when idle in water
        if (this.getDeltaMovement().y < 0D && isSwimming() && movementInput.z == 0) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0D, this.getDeltaMovement().z);
        }

        // Handle jumping
        if (this.jumpPending) {
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, getCustomJump(), 0.0));
                // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} jumping in water", this.getType().getDescription().getString());
            }
            this.jumpPending = false;
        }
        
        // Handle diving
        if (this.divePending) {
            this.divePending = false;
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.3, 0.0));
            // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} diving", this.getType().getDescription().getString());
        }
        
        // Aquatic-specific movement
        if (this.isInWater()) {
            this.moveRelative(0.1F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85D)); // slight drag in water
            // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} water movement applied", this.getType().getDescription().getString());
        } else {
            // Land movement (for when dolphin is out of water)
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            super.travel(movementInput);
            // MoCreatures.LOGGER.info("MOVE_WITH_RIDER: {} land movement applied", this.getType().getDescription().getString());
        }
    }

    public void moveEntityWithRiderUntamed(Vec3 movementInput, LivingEntity passenger) {
        // MoCreatures.LOGGER.info("MOVE_WITH_RIDER_UNTAMED: {} bucking behavior - temper: {}/{}, inWater: {}", 
        //     this.getType().getDescription().getString(),
        //     this.getTemper(),
        //     this.getMaxTemper(),
        //     this.isSwimming());
            
        // Subtle erratic movement - less frequent and gentler
        if (this.random.nextInt(20) == 0) {
            double deltaX = this.random.nextGaussian() / 80D; // Much weaker
            double deltaZ = this.random.nextGaussian() / 80D; // Much weaker
            
            // Apply movement more gently by adding to existing movement instead of replacing
            Vec3 currentMovement = this.getDeltaMovement();
            this.setDeltaMovement(
                currentMovement.x + deltaX, 
                currentMovement.y, 
                currentMovement.z + deltaZ
            );
            
            // Gentler rotation - only slight direction changes
            if (Math.abs(deltaX) > 0.001 || Math.abs(deltaZ) > 0.001) {
                float currentYaw = this.getYRot();
                float targetYaw = (float)(Math.atan2(deltaZ, deltaX) * (180D / Math.PI)) - 90F;
                // Only rotate slightly towards the new direction (10% blend)
                float newYaw = currentYaw + (targetYaw - currentYaw) * 0.1F;
                this.setYRot(newYaw);
                this.yRotO = newYaw;
                this.yBodyRot = newYaw;
                this.yHeadRot = newYaw;
            }
        }
        
        this.move(MoverType.SELF, this.getDeltaMovement());
        
        // Bucking logic - needs to run on both sides for consistent behavior
        // but state changes only happen server-side
        // boolean riderUnderwater = passenger.isEyeInFluid(FluidTags.WATER);
        // int buckingChance = riderUnderwater ? 50 : 15; // Much more likely to buck if rider isn't underwater
        
        // // Use a deterministic random based on entity ID and tick count for sync
        // long seed = this.getId() + this.tickCount;
        // Random syncRandom = new Random(seed);
        
        // if (syncRandom.nextInt(buckingChance) == 0) {
        //     if (!this.level().isClientSide()) {
        //         // Server-side dismount with proper position sync to prevent rubberband
        //         // 1. Calculate a safe dismount position near the entity
        //         double offsetX = syncRandom.nextGaussian() * 0.5D;
        //         double offsetZ = syncRandom.nextGaussian() * 0.5D;
        //         double dismountX = this.getX() + offsetX;
        //         double dismountY = this.getY();
        //         double dismountZ = this.getZ() + offsetZ;
                
        //         // 2. Force dismount on server side
        //         passenger.stopRiding();
                
        //         // 3. Immediately set the passenger's position to prevent client desync
        //         passenger.teleportTo(dismountX, dismountY, dismountZ);
                
        //         // 4. Clear entity state and force updates
        //         this.ejectPassengers();
        //         this.refreshDimensions();
        //         this.jumpFromGround();
                
        //         // 5. Force position sync by marking both entities as dirty
        //         passenger.setPos(dismountX, dismountY, dismountZ);
        //         this.setPos(this.getX(), this.getY(), this.getZ());
                
        //         MoCreatures.LOGGER.info("MOVE_WITH_RIDER_UNTAMED: {} successfully bucked rider off! (rider underwater: {})", 
        //             this.getType().getDescription().getString(), riderUnderwater);
        //     } else {
        //         // Client-side: just visual effects, no state changes
        //         this.jumpFromGround();
        //     }
        // }
        
        // // Pure RNG taming chance while riding (server-side only for state changes)
        // if (!this.level().isClientSide() && this instanceof IMoCTameable && passenger instanceof Player) {
        //     // 1 in 400 chance per tick to tame while riding (about 20 seconds on average)
        //     // Use same deterministic random for consistency
        //     if (syncRandom.nextInt(400) == 0) {
        //         MoCTools.tameWithName((Player) passenger, (IMoCTameable) this);
        //     }
        // }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Override
    protected void handleAirSupply(ServerLevel serverLevel, int air) {
        if (this.isAlive() && !this.isInWater()) {
            this.setAirSupply(air - 1);
            if (this.getAirSupply() == -30) {
                this.setAirSupply(0);
                this.hurtOrSimulate(this.damageSources().drown(), 1.0F);
                Vec3 kick = this.getDeltaMovement().add(
                        this.random.nextDouble() / 10D,
                        0.0,
                        this.random.nextDouble() / 10D
                );
                this.setDeltaMovement(kick);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    protected boolean usesNewAI() {
        return false;
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.isInWater()) {
            return this.navigatorWater;
        }
        return super.getNavigation();
    }

    /**
     * Distance the entity will float under the surface. 0.0 = at surface, 1.0 = one block below.
     */
    @Override
    public double getDivingDepth() {
        return this.divingDepth;
    }

    protected void setNewDivingDepth(double setDepth) {
        if (setDepth != 0.0D) {
            if (setDepth > maxDivingDepth()) {
                setDepth = maxDivingDepth();
            }
            if (setDepth < minDivingDepth()) {
                setDepth = minDivingDepth();
            }
            this.divingDepth = setDepth;
        } else {
            this.divingDepth = this.random.nextDouble() * (maxDivingDepth() - minDivingDepth()) + minDivingDepth();
        }
    }

    protected void setNewDivingDepth() {
        setNewDivingDepth(0.0D);
    }

    protected double minDivingDepth() {
        return 0.20D;
    }

    protected double maxDivingDepth() {
        return 3.0D;
    }

    @Override
    public void forceEntityJump() {
        this.makeEntityJump();
    }

    @Override
    public float yawRotationOffset() {
        double wobble = 0F;
        if (this.getDeltaMovement().x != 0D || this.getDeltaMovement().z != 0D) {
            wobble = Math.sin((this.tickCount) * 0.5D) * 8D;
        }
        return (float) wobble;
    }

    public int getMaxAge() {
        return 100;
    }


    /** 1.20 riding height: passengers sit this far above the creature's feet. Creatures override it. */
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.75D;
    }

    /** 1.20 offset of this creature when it rides something (for example a bunny on a player's head). */
    public double getMyRidingOffset() {
        return 0.0D;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0D, this.getPassengersRidingOffset(), 0.0D);
    }

    @Override
    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        return super.getVehicleAttachmentPoint(vehicle).subtract(0.0D, this.getMyRidingOffset(), 0.0D);
    }

    /**
     * The creature's loot table, which may depend on its variant; null drops nothing. 26.3 ties a mob's loot table to its
     * entity type, so the base class drops from this table itself.
     */
    protected @Nullable Identifier getDefaultLootTable() {
        return null;
    }

    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled) {
        Identifier table = this.getDefaultLootTable();
        if (table != null) {
            this.dropFromLootTable(level, source, playerKilled, ResourceKey.create(Registries.LOOT_TABLE, table));
        }
    }

    /**
     * Mo' Creatures on-hit hook, kept from 1.20's {@code doEnchantDamageEffects}: runs after a successful melee hit.
     * Creatures override it for their own hit effects (poison, fire, knockback) and call super for the enchantment effects.
     */
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            EnchantmentHelper.doPostAttackEffects(serverLevel, target, this.damageSources().mobAttack(attacker));
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        }
        if (!target.isInWater()) {
            return false;
        }
        boolean flag = ((LivingEntity) target).hurtOrSimulate(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (flag) {
            this.doEnchantDamageEffects(this, target);
        }
        return flag;
    }

    @Override
    public void setLeashedTo(Entity entity, boolean sendAttachNotification) {
        if (this.getIsTamed() && entity instanceof Player) {
            Player player = (Player) entity;
            if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null &&
                    !player.getUUID().equals(this.getOwnerId()) &&
                    !MoCTools.isThisPlayerAnOP(player)) {
                return;
            }
        }
        super.setLeashedTo(entity, sendAttachNotification);
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return !this.isVehicle();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity passenger ? passenger : null;
    }

    @Override
    public boolean getIsGhost() {
        return false;
    }

    @Override
    public int maxFlyingHeight() {
        return 1;
    }

    @Override
    public int minFlyingHeight() {
        return 1;
    }

    public boolean isFlyer() {
        return false;
    }

    @Override
    public boolean getIsFlying() {
        return false;
    }

    protected SoundEvent getAngrySound() {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture(this.texture);
    }

    @Override
    public boolean isMovementCeased() {
        return ((!isSwimming() && !this.isPassenger()) || this.isPassenger() || this.getIsSitting());
    }

    @Override
    public boolean checkSpawningBiome() {
        return true;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        // Clean up navigation and AI to prevent memory leaks
        if (this.getNavigation() != null) {
            this.getNavigation().stop();
        }
        
        // Clear any references that might cause memory leaks
        this.fishHooked = false;
        this.updateDivingDepth = false;
        
        super.remove(reason);
    }
}
