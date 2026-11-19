/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.util.RandomSource;

public abstract class MoCEntityAmbient extends PathfinderMob implements IMoCEntity {

    // Synched data parameters
    private static final EntityDataAccessor<Boolean> ADULT = SynchedEntityData.defineId(MoCEntityAmbient.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(MoCEntityAmbient.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(MoCEntityAmbient.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> NAME_STR = SynchedEntityData.defineId(MoCEntityAmbient.class, EntityDataSerializers.STRING);

    protected String texture;
    protected boolean riderIsDisconnecting;

    protected MoCEntityAmbient(EntityType<? extends MoCEntityAmbient> type, Level world) {
        super(type, world);
        // If the default navigation type needs adjustment, override createNavigation()
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getName() {
        String entityString = this.getType().getDescriptionId(); // translation key like "entity.mocreatures.myentity"
        if (!MoCreatures.proxy.verboseEntityNames || entityString == null) {
            return super.getName();
        }
        // Build verbose key: "entity.<registry_name>.verbose.name"
        String registryName = this.getType().getDescriptionId(); // e.g., "myentity"
        String translationKey = "entity." + registryName + ".verbose.name";
        String translated = I18n.get(translationKey);
        if (!translationKey.equals(translated)) {
            return Component.translatable(translationKey);
        }
        return super.getName();
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture(this.texture);
    }

    /**
     * Register default attributes for all ambient MoC entities. Subclasses should
     * add additional attributes (e.g., movement speed, health) by calling
     * super.createAttributes().add(...).
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                // Example defaults; subclasses should override or extend:
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    /**
     * Called when the Mob is first spawned (e.g., world generation, spawn egg).
     * Equivalent to 1.16.5's onInitialSpawn.
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        selectType();
        return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    /**
     * Put your code to choose a texture / the mob type in here. Will be called
     * by default MoCEntity constructors (via finalizeSpawn).
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
    public boolean renderName() {
        return MoCreatures.proxy.getDisplayPetName()
                && (getPetName() != null && !getPetName().isEmpty())
                && (!this.isVehicle()) // not being ridden
                && (this.getVehicle() == null);
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
    }

    @Override
    public boolean getIsTamed() {
        return false;
    }

    @Override
    public int getOwnerPetId() {
        return 0;
    }

    @Override
    public void setOwnerPetId(int petId) {
        // no-op for ambient
    }

    @Override
    public UUID getOwnerId() {
        return null;
    }

    /**
     * Called in custom spawn rule to ensure correct biome spawning.
     */
    @Override
    public boolean checkSpawningBiome() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (isMovementCeased()) {
                this.getNavigation().stop();
            }
            // The PathNavigation tick is invoked automatically by Mob.tick()
        }
        super.tick();
    }

    /** Used to drop armor, inventory, saddles, etc. */
    public void dropMyStuff() {
        // Override in subclasses as needed
    }

    /** Used to heal the animal */
    protected boolean isMyHealFood(ItemStack itemstack) {
        return false;
    }

    public void faceLocation(int x, int y, int z, float maxTurn) {
        double dx = x + 0.5D - this.getX();
        double dz = z + 0.5D - this.getZ();
        double dy = y + 0.5D - this.getY();
        double horizontalDist = Mth.sqrt((float) (dx * dx + dz * dz));
        float targetYaw = (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90F;
        float targetPitch = (float) (-(Mth.atan2(dy, horizontalDist) * (180D / Math.PI)));
        this.setXRot(-updateRotation(this.getXRot(), targetPitch, maxTurn));
        this.setYRot(updateRotation(this.getYRot(), targetYaw, maxTurn));
    }

    /** Arguments: current rotation, intended rotation, max increment. */
    private float updateRotation(float current, float intended, float maxIncrement) {
        float diff;
        for (diff = intended - current; diff < -180.0F; diff += 360.0F) {
            ;
        }
        while (diff >= 180.0F) {
            diff -= 360.0F;
        }
        if (diff > maxIncrement) {
            diff = maxIncrement;
        }
        if (diff < -maxIncrement) {
            diff = -maxIncrement;
        }
        return current + diff;
    }

    public void getMyOwnPath(Entity target, float speed) {
        // New 1.20 pathfinding: create a Path and then moveTo it
        PathNavigation nav = this.getNavigation();
        net.minecraft.world.level.pathfinder.Path path = nav.createPath(target, 0);
        if (path != null) {
            nav.moveTo(path, speed);
        }
    }

    /**
     * Vanilla-style ambient spawn rules with light level check integration
     */
    public static boolean checkMobSpawnRules(EntityType<MoCEntityAmbient> type, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        // Always allow spawn eggs
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        
        // Use vanilla ambient spawn rules (can see sky)
        boolean willSpawn = world.canSeeSky(pos);
        
        // Add light level check from BiomeSpawnConfig if this is a Level (not just LevelAccessor)
        if (willSpawn && world.getLevel() instanceof Level) {
            willSpawn = drzhark.mocreatures.config.biome.BiomeSpawnConfig.checkLightLevelForEntity((Level) world.getLevel(), pos, type);
        }
        
        if (MoCreatures.proxy.debug && willSpawn) {
            BlockState blockState = world.getBlockState(pos);
            MoCreatures.LOGGER.info("Ambient: {} at: {} State: {} biome: {}", type.getDescription(), pos, blockState, MoCTools.biomeName((Level) world.getLevel(), pos));
        }
        
        return willSpawn;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag entityData = MoCTools.getEntityData(this);
        entityData.putBoolean("Adult", getIsAdult());
        entityData.putInt("Edad", getMoCAge());
        entityData.putString("Name", getPetName());
        entityData.putInt("TypeInt", getTypeMoC());
        compound.put("MoCData", entityData);
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag entityData = MoCTools.getEntityData(this);
        if (compound.contains("MoCData", 10)) {
            CompoundTag mocTag = compound.getCompound("MoCData");
            setAdult(mocTag.getBoolean("Adult"));
            setMoCAge(mocTag.getInt("Edad"));
            setPetName(mocTag.getString("Name"));
            setTypeMoC(mocTag.getInt("TypeInt"));
        }
    }

    /** Sets a flag that will make the Entity "jump" in the next onGround update */
    @Override
    public void makeEntityJump() {
        // Default no-op; override if your mob needs special behavior
    }

    /** Boolean used for flying mounts */
    public boolean isFlyer() {
        return false;
    }

    @Override
    public void makeEntityDive() {
        // Called each tick to apply movement; can be used to make entity "dive" or "fly"
    }

    @Override
    public int nameYOffset() {
        return -80;
    }

    /** Used to synchronize animations between server and client */
    @Override
    public void performAnimation(int attackType) {
        // Override in subclasses to trigger packet-based animations
    }

    /** Used to follow the player carrying the item */
    @SuppressWarnings("unused")
    public boolean isMyFavoriteFood(ItemStack stack) {
        return false;
    }

    public boolean isOnAir() {
        BlockPos below1 = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2D),
                Mth.floor(this.getZ()));
        BlockPos below2 = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 1.2D),
                Mth.floor(this.getZ()));
        return this.level().isEmptyBlock(below1) && this.level().isEmptyBlock(below2);
    }

    @Override
    public float getSizeFactor() {
        return 1.0F;
    }

    @Override
    public float getAdjustedYOffset() {
        return 0F;
    }

    public boolean getIsRideable() {
        return false;
    }

    public void setRideable(boolean b) {
        // Override if your mob becomes rideable
    }

    @Override
    public void setArmorType(int i) {
        // Ambient mobs don’t use armor by default
    }

    /**
     * Finds an entity within a horizontal radius d that matches entitiesToInclude
     */
    protected LivingEntity getBoogey(double d) {
        LivingEntity result = null;
        AABB box = this.getBoundingBox().inflate(d, 4.0D, d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity entity : list) {
            if (entitiesToInclude(entity)) {
                result = (LivingEntity) entity;
                break;
            }
        }
        return result;
    }

    /**
     * Used in getBoogey to specify what kind of entity to look for
     */
    public boolean entitiesToInclude(Entity entity) {
        return (entity instanceof LivingEntity)
                && (entity.getBbWidth() >= 0.5D || entity.getBbHeight() >= 0.5D);
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

    protected boolean canBeTrappedInNet() {
        return (this instanceof IMoCTameable) && getIsTamed();
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        if (!this.level().isClientSide() && !MoCTools.isThisPlayerAnOP(player) && this.getIsTamed()
                && !player.getUUID().equals(this.getOwnerId())) {
            return false;
        }
        return super.canBeLeashed(player);
    }

    @Override
    public boolean getIsSitting() {
        return false;
    }

    @Override
    public boolean isNotScared() {
        return false;
    }

    @Override
    public boolean isMovementCeased() {
        return getIsSitting();
    }

    @Override
    public boolean shouldAttackPlayers() {
        return this.level().getDifficulty() != net.minecraft.world.Difficulty.PEACEFUL;
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    /** The distance the entity will float under the surface. 0F = surface, 1.0F = 1 block under */
    @Override
    public double getDivingDepth() {
        return 0.5D;
    }

    @Override
    public boolean isDiving() {
        return false;
    }

    @Override
    public void forceEntityJump() {
        this.jumpFromGround();
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false; // No fall damage for ambient
    }

    @Override
    public int minFlyingHeight() {
        return 2;
    }

    /** Maximum flyer height when moving autonomously */
    public int maxFlyingHeight() {
        return 4;
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (!getIsFlying()) {
            super.travel(movementInput);
        } else {
            moveEntityWithHeadingFlying(movementInput);
        }
    }

    public void moveEntityWithHeadingFlying(Vec3 vec) {
        if (!this.level().isClientSide()) {
            // Move relative partial for flying
            this.moveRelative(0.1F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9D, 0.9D, 0.9D));
        } else {
            super.travel(vec);
        }
    }

    @Override
    public boolean getIsFlying() {
        return false;
    }

    @Override
    public boolean getIsGhost() {
        return false;
    }

    @Override
    public void setLeashedTo(Entity leashHolder, boolean sendAttachNotification) {
        if (this.getIsTamed() && leashHolder instanceof Player) {
            Player player = (Player) leashHolder;
            if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null
                    && !player.getUUID().equals(this.getOwnerId())
                    && !MoCTools.isThisPlayerAnOP(player)) {
                return;
            }
        }
        super.setLeashedTo(leashHolder, sendAttachNotification);
    }
}
