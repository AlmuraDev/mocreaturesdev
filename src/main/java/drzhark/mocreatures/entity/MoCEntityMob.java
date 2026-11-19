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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIMoverHelperMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import drzhark.mocreatures.entity.item.MoCEntityLitterBox;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageHealth;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;               // was CompoundNBT
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;     // same
import net.minecraft.server.level.ServerLevel;       // replaces IServerWorld :contentReference[oaicite:1]{index=1}
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource; // was net.minecraft.util.DamageSource
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;     // replaces AttributeModifierMap.MutableAttribute :contentReference[oaicite:4]{index=4}
import net.minecraft.world.entity.ai.attributes.Attributes;            // same
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Monster;                       // replaces MonsterEntity :contentReference[oaicite:5]{index=5}
import net.minecraft.world.entity.ai.navigation.PathNavigation;          // replaces PathNavigator :contentReference[oaicite:9]{index=9}
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;     // replaces PathNavigateFlyer
import net.minecraft.world.level.Level;                                   // replaces World
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;                                     // replaces Vector3d :contentReference[oaicite:14]{index=14}
import net.minecraft.network.syncher.EntityDataAccessor;                   // replaces DataParameter :contentReference[oaicite:15]{index=15}
import net.minecraft.network.syncher.EntityDataSerializers;                // replaces DataSerializers
import net.minecraft.network.syncher.SynchedEntityData;                    // replaces EntityDataManager
import net.minecraft.world.Difficulty;                                     // same
import net.minecraft.world.DifficultyInstance;                             // same


import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

/**
 * Updated for Minecraft 1.20.1 (Forge/Mojang mappings).
 */
public abstract class MoCEntityMob extends Monster implements IMoCEntity {

    protected static final EntityDataAccessor<Boolean> ADULT = SynchedEntityData.defineId(MoCEntityMob.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TYPE   = SynchedEntityData.defineId(MoCEntityMob.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> AGE    = SynchedEntityData.defineId(MoCEntityMob.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<String> NAME_STR = SynchedEntityData.defineId(MoCEntityMob.class, EntityDataSerializers.STRING);
    protected boolean divePending;
    protected String texture;
    protected PathNavigation navigatorWater;
    protected PathNavigation navigatorFlyer;
    protected EntityAIWanderMoC2 wander;

    protected MoCEntityMob(EntityType<? extends MoCEntityMob> type, Level world) {
        super(type, world);
        this.texture = "blank.jpg";
        this.moveControl = new EntityAIMoverHelperMoC(this); // custom MoveControl
        this.navigatorWater = new WaterBoundPathNavigation(this, world);                 // was SwimmerPathNavigator :contentReference[oaicite:16]{index=16}
        this.navigatorFlyer = new FlyingPathNavigation(this, world);               // custom Flyer nav
        this.wander = new EntityAIWanderMoC2(this, 1.0D, 80);
        this.goalSelector.addGoal(4, this.wander);
    }

    @Override
    public Component getName() {
        String entityString = this.getType().getDescriptionId();
        if (!MoCreatures.proxy.verboseEntityNames || entityString == null) {
            return super.getName();
        }
        String translationKey = "entity." + entityString + ".verbose.name";
        return Language.getInstance().has(translationKey)
                ? Component.translatable(translationKey)
                : super.getName();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.7F)
                .add(Attributes.ATTACK_DAMAGE, 2D)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                   EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        selectType();
        return super.finalizeSpawn(world, difficulty, reason, spawnData);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture(this.texture);
    }

    /**
     * Put your code to choose a texture / the mob type in here. Will be called by default MocEntity constructors.
     */
    @Override
    public void selectType() {
        setTypeMoC(1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ADULT, false);
        builder.define(TYPE, 0);
        builder.define(AGE, 45);
        builder.define(NAME_STR, "");
    }

    @Override
    public int getTypeMoC() {
        return this.entityData.get(TYPE);
    }

    @Override
    public void setTypeMoC(int i) {
        this.entityData.set(TYPE, i);
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
    public boolean getIsTamed() {
        return false;
    }

    @Override
    public String getPetName() {
        return this.entityData.get(NAME_STR);
    }

    @Override
    public void setPetName(String name) {
        this.entityData.set(NAME_STR, name == null ? "" : name);
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

    @Nullable
    public UUID getOwnerId() {
        return null;
    }

    @Override
    public int getOwnerPetId() {
        return 0;
    }

    @Override
    public void setOwnerPetId(int petId) {
    }

    public static boolean getCanSpawnHere(EntityType<? extends MoCEntityMob> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        return checkMobSpawnRules(type, world, reason, pos, world.getRandom());
    }

    /** Explicit JSON light limits override vanilla darkness, but never difficulty/surface rules. */
    public static boolean checkMobSpawnRules(EntityType<? extends MoCEntityMob> type, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        boolean override = drzhark.mocreatures.config.biome.BiomeSpawnConfig.hasLightLevelOverride(type);
        if (!override) {
            return Monster.checkMonsterSpawnRules(type, world, reason, pos, random);
        }
        return Monster.checkAnyLightMonsterSpawnRules(type, world, reason, pos, random)
                && drzhark.mocreatures.config.biome.BiomeSpawnConfig.checkLightLevelForEntity(world.getLevel(), pos, type);
    }

    public boolean entitiesToIgnore(Entity entity) {
        return (!(entity instanceof Mob))
                || (entity instanceof Monster)
                || (entity instanceof MoCEntityEgg)
                || entity instanceof MoCEntityKittyBed
                || entity instanceof MoCEntityLitterBox
                || (this.getIsTamed() && entity instanceof IMoCEntity && ((IMoCEntity) entity).getIsTamed())
                || (entity instanceof Wolf && !MoCreatures.proxy.attackWolves)
                || (entity instanceof MoCEntityHorse && !MoCreatures.proxy.attackHorses);
    }

    @Override
    public boolean checkSpawningBiome() {
        return true;
    }

    @SuppressWarnings("deprecated")
    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (getIsTamed() && this.random.nextInt(200) == 0) {
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHealth(this.getId(), this.getHealth()));
            }

            if (this.isDaylightSensitive() && this.level().isBrightOutside()) {
                float brightness = this.getLightLevelDependentMagicValue();
                if (brightness > 0.5F
                        && this.level().canSeeSky(new BlockPos(
                        Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ())))
                        && this.random.nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F)
                {
                    this.igniteForSeconds(8);
                }
            }

            if (getMoCAge() == 0) setMoCAge(getMaxAge() - 10); // fixes tiny creatures spawned by error
            if (!getIsAdult() && this.random.nextInt(300) == 0) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= getMaxAge()) {
                    setAdult(true);
                }
            }

            if (getIsFlying() && this.getNavigation().isDone() && !isMovementCeased() && this.getTarget() == null && this.random.nextInt(20) == 0) {
                this.wander.makeUpdate();
            }
        }

        this.getNavigation().tick(); // was getNavigator().tick() :contentReference[oaicite:17]{index=17}
        super.tick();
    }

    protected int getMaxAge() {
        return 100;
    }

    protected boolean isDaylightSensitive() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (!this.level().isClientSide() && getIsTamed()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHealth(this.getId(), this.getHealth()));
        }
        return super.hurtServer(serverLevel, source, amount);
    }

    /** Boolean used to select pathfinding behavior */
    public boolean isFlyer() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Adult", getIsAdult());
        tag.putInt("Edad", getMoCAge());
        tag.putString("Name", getPetName());
        tag.putInt("TypeInt", getTypeMoC());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        setAdult(tag.getBooleanOr("Adult", false));
        setMoCAge(tag.getIntOr("Edad", 0));
        setPetName(tag.getStringOr("Name", ""));
        setTypeMoC(tag.getIntOr("TypeInt", 0));
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        if (!isFlyer()) {
            return super.causeFallDamage(distance, damageMultiplier, source);
        }
        return false;
    }

    @Override
    public boolean onClimbable() {
        return isFlyer() ? false : super.onClimbable();
    }

    @Override
    public void travel(Vec3 delta) {
        if (!isFlyer()) {
            super.travel(delta);
            return;
        }
        this.navigateFlying(delta);
    }

    protected void navigateFlying(Vec3 delta) {
        if (this.isAlive() && !this.level().isClientSide()) {
            this.moveRelative(0.1F, delta);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(delta);
        }
    }

    /**
     * Used to synchronize the attack animation between server and client
     */
    @Override
    public void performAnimation(int attackType) {
    }

    @Override
    public int nameYOffset() {
        return 0;
    }

    @Override
    public boolean renderName() {
        return MoCreatures.proxy.getDisplayPetName()
                && !getPetName().isEmpty()
                && !this.isPassenger()
                && this.getVehicle() == null;
    }

    @Override
    public void makeEntityJump() {
        // TODO
    }

    @Override
    public void makeEntityDive() {
        this.divePending = true;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return getIsTamed() || super.requiresCustomPersistence();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !getIsTamed() && super.removeWhenFarAway(distance);
    }

    @Override
    public float getSizeFactor() {
        return 1.0F;
    }

    @Override
    public float getAdjustedYOffset() {
        return 0F;
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

    @Override
    public float yawRotationOffset() {
        return 0F;
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
    public boolean canAttackTarget(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean getIsSitting() {
        return false;
    }

    @Override
    public boolean isNotScared() {
        return true;
    }

    @Override
    public boolean isMovementCeased() {
        return false;
    }

    @Override
    public boolean shouldAttackPlayers() {
        return this.level().getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public double getDivingDepth() {
        return 0;
    }

    @Override
    public boolean isDiving() {
        return false;
    }

    @Override
    public void forceEntityJump() {
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
        boolean flag = target.hurtOrSimulate(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (flag) {
            this.doEnchantDamageEffects(this, target);
        }
        return flag;
    }

    @Override
    public int maxFlyingHeight() {
        return 5;
    }

    @Override
    public int minFlyingHeight() {
        return 1;
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.isInWater() && this.isAmphibian()) {
            return this.navigatorWater;
        }
        if (this.isFlyer()) {
            return this.navigatorFlyer;
        }
        return super.getNavigation();
    }

    public boolean isAmphibian() {
        return false;
    }

    @Override
    public boolean getIsFlying() {
        return isFlyer();
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public boolean getIsGhost() {
        return false;
    }
}
