/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity;
import net.minecraft.locale.Language;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIMoverHelperMoC;
import drzhark.mocreatures.entity.ai.PathNavigateFlyer;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import drzhark.mocreatures.entity.item.MoCEntityLitterBox;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.util.RandomSource;

public abstract class MoCEntityAnimal extends Animal implements IMoCEntity {

    private static final EntityDataAccessor<Boolean> ADULT = SynchedEntityData.defineId(MoCEntityAnimal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(MoCEntityAnimal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(MoCEntityAnimal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> NAME_STR = SynchedEntityData.defineId(MoCEntityAnimal.class, EntityDataSerializers.STRING);

    protected boolean divePending;
    protected boolean jumpPending;
    protected int temper;
    protected boolean isEntityJumping;
    protected boolean riderIsDisconnecting;
    protected String texture;
    protected boolean isTameable;
    protected PathNavigation navigatorWater;
    protected PathNavigation navigatorFlyer;
    private int huntingCounter;
    private int followPlayerCounter;
    private double divingDepth;
    private boolean randomAttributesUpdated;

    protected MoCEntityAnimal(EntityType<? extends MoCEntityAnimal> type, Level world) {
        super(type, world);
        this.riderIsDisconnecting = false;
        this.isTameable = false;
        this.texture = "blank.jpg";
        this.navigatorWater = new WaterBoundPathNavigation(this, world);
        this.moveControl = new EntityAIMoverHelperMoC(this);
        this.navigatorFlyer = new PathNavigateFlyer(this, world);
    }

    @Override
    public Component getName() {
        String entityString = this.getType().getDescriptionId();
        if (!MoCreatures.proxy.verboseEntityNames || entityString == null) {
            return super.getName();
        }
        String registryName = this.getType().getDescriptionId();
        String translationKey = "entity." + registryName + ".verbose.name";
        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey);
        }
        return super.getName();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        selectType();
        return super.finalizeSpawn(world, difficulty, reason, spawnData);
    }

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

    public boolean isMale() {
        return (getTypeMoC() == 1);
    }

    @Override
    public boolean renderName() {
        return MoCreatures.proxy.getDisplayPetName()
                && (getPetName() != null && !getPetName().isEmpty())
                && (!this.isVehicle())
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
        if (getMoCAge() >= getMoCMaxAge()) {
            setAdult(true);
        }
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

    public boolean getIsJumping() {
        return this.isEntityJumping;
    }

    public void setIsJumping(boolean flag) {
        this.isEntityJumping = flag;
    }

    @Override
    public boolean checkSpawningBiome() {
        return true;
    }

    protected LivingEntity getClosestEntityLiving(Entity entity, double d) {
        double closestDistSq = -1D;
        LivingEntity closest = null;
        AABB box = this.getBoundingBox().inflate(d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity candidate : list) {
            if (entitiesToIgnore(candidate)) {
                continue;
            }
            double distSq = candidate.distanceToSqr(entity);
            if ((d < 0.0D || distSq < d * d)
                    && (closestDistSq == -1D || distSq < closestDistSq)
                    && ((LivingEntity) candidate).hasLineOfSight(entity)) {
                closestDistSq = distSq;
                closest = (LivingEntity) candidate;
            }
        }
        return closest;
    }

    public LivingEntity getClosestTarget(Entity entity, double d) {
        double closestDistSq = -1D;
        LivingEntity closest = null;
        AABB box = this.getBoundingBox().inflate(d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity candidate : list) {
            if (!(candidate instanceof LivingEntity)
                    || candidate == entity
                    || candidate == entity.getVehicle()
                    || candidate instanceof Player
                    || candidate instanceof Monster
                    || this.getBbHeight() <= candidate.getBbHeight()
                    || this.getBbWidth() <= candidate.getBbWidth()) {
                continue;
            }
            double distSq = candidate.distanceToSqr(entity);
            if ((d < 0.0D || distSq < d * d)
                    && (closestDistSq == -1D || distSq < closestDistSq)
                    && ((LivingEntity) candidate).hasLineOfSight(entity)) {
                closestDistSq = distSq;
                closest = (LivingEntity) candidate;
            }
        }
        return closest;
    }

    public boolean entitiesToIgnore(Entity entity) {
        return !(entity instanceof Mob)
                || entity instanceof Monster
                || entity instanceof MoCEntityKittyBed
                || entity instanceof MoCEntityLitterBox
                || (this.getIsTamed() && entity instanceof IMoCEntity && ((IMoCEntity) entity).getIsTamed())
                || (entity instanceof Wolf && !MoCreatures.proxy.attackWolves)
                || (entity instanceof MoCEntityHorse && !MoCreatures.proxy.attackHorses)
                || entity.getBbWidth() >= this.getBbWidth()
                || entity.getBbHeight() >= this.getBbHeight()
                || entity instanceof MoCEntityEgg
                || (entity instanceof IMoCEntity && !MoCreatures.proxy.enableHunters);
    }

    protected LivingEntity getBoogey(double d) {
        LivingEntity found = null;
        AABB box = this.getBoundingBox().inflate(d, 4.0D, d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity candidate : list) {
            if (entitiesToInclude(candidate)) {
                found = (LivingEntity) candidate;
                break;
            }
        }
        return found;
    }

    public boolean entitiesToInclude(Entity entity) {
        return (entity.getClass() != this.getClass())
                && (entity instanceof LivingEntity)
                && (entity.getBbWidth() >= 0.5D || entity.getBbHeight() >= 0.5D);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (rideableEntity() && this.isVehicle()) {
                riding();
            }
            if (isMovementCeased()) {
                this.getNavigation().stop();
            }
            if (getMoCAge() == 0) {
                setMoCAge(getMoCMaxAge() - 10);
            }
            if (!getIsAdult() && (this.random.nextInt(300) == 0) && getMoCAge() <= getMoCMaxAge()) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= getMoCMaxAge()) {
                    setAdult(true);
                }
            }
            if (MoCreatures.proxy.enableHunters && isReadyToHunt() && !getIsHunting() && this.random.nextInt(500) == 0) {
                setIsHunting(true);
            } else if (!getIsHunting() && isReadyToFollowOwnerPlayer() && !getIsFollowingOwnerPlayer() && this.random.nextInt(60) == 0) {
                setIsFollowingOwnerPlayer(true);
            }
            if (getIsHunting() && ++this.huntingCounter > 50) {
                setIsHunting(false);
            }
            if (getIsFollowingOwnerPlayer() && ++this.followPlayerCounter > 50) {
                setIsFollowingOwnerPlayer(false);
            }
        }
        if (this.isInWater() && isAmphibian() && (this.random.nextInt(500) == 0 || !this.randomAttributesUpdated)) {
            this.setNewDivingDepth();
            this.randomAttributesUpdated = true;
        }
        if (canRidePlayer() && this.isPassenger()) {
                MoCTools.dismountSneakingPlayer(this);
        }
        super.tick();
    }

    public int getMoCMaxAge() {
        return 100;
    }

    @Override
    public boolean isNotScared() {
        return false;
    }

    public boolean swimmerEntity() {
        return false;
    }

    public boolean isSwimming() {
        return this.isEyeInFluid(FluidTags.WATER);
    }

    public void dropMyStuff() {
        // Override in subclasses if needed
    }

    protected boolean isMyHealFood(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return isAmphibian();
    }

    public ItemEntity getClosestItem(Entity entity, double d, Ingredient... items) {
        double closestDistSq = -1D;
        ItemEntity found = null;
        AABB box = this.getBoundingBox().inflate(d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity cand : list) {
            if (!(cand instanceof ItemEntity)) {
                continue;
            }
            ItemEntity itemEnt = (ItemEntity) cand;
            if (items.length > 0 && Arrays.stream(items).noneMatch(ing -> ing.test(itemEnt.getItem()))) {
                continue;
            }
            double distSq = itemEnt.distanceToSqr(entity);
            if ((d < 0.0D || distSq < d * d) && (closestDistSq == -1D || distSq < closestDistSq)) {
                closestDistSq = distSq;
                found = itemEnt;
            }
        }
        return found;
    }

    public ItemEntity getClosestEntityItem(Entity entity, double d) {
        double closestDistSq = -1D;
        ItemEntity found = null;
        AABB box = this.getBoundingBox().inflate(d);
        List<Entity> list = this.level().getEntities(this, box);
        for (Entity cand : list) {
            if (!(cand instanceof ItemEntity)) {
                continue;
            }
            ItemEntity itemEnt = (ItemEntity) cand;
            double distSq = itemEnt.distanceToSqr(entity);
            if ((d < 0.0D || distSq < d * d) && (closestDistSq == -1D || distSq < closestDistSq)) {
                closestDistSq = distSq;
                found = itemEnt;
            }
        }
        return found;
    }

    /** Faces this entity toward a world-space point.
 *  @param x X-coord
 *  @param y Y-coord
 *  @param z Z-coord
 *  @param maxTurn maximum yaw change per call, in degrees
 */
public void faceLocation(double x, double y, double z, float maxTurn) {
    double dx = x - getX();
    double dz = z - getZ();
    double dy = y - (getY() + getEyeHeight());

    double horiz = Math.sqrt(dx * dx + dz * dz);

    // Desired angles
    float targetYaw   = (float)(Mth.atan2(dz, dx) * (180D / Math.PI)) - 90F;
    float targetPitch = (float)-(Mth.atan2(dy, horiz) * (180D / Math.PI));

    // Smoothly interpolate toward the target
    this.setYRot(Mth.approachDegrees(this.getYRot(), targetYaw,  maxTurn));
    this.setXRot(Mth.approachDegrees(this.getXRot(), targetPitch, maxTurn));

    // Keep head/body in sync so render & AI see the same direction
    this.yHeadRot = this.getYRot();
    this.yBodyRot = this.getYRot();
}


    public void setPathToEntity(Entity entity, float speed) {
        PathNavigation nav = this.getNavigation();
        net.minecraft.world.level.pathfinder.Path path = nav.createPath(entity, 0);
        if (path != null) {
            nav.moveTo(path, 1D);
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

    protected void getPathOrWalkableBlock(Entity entity, float f) {
        PathNavigation nav = this.getNavigation();
        net.minecraft.world.level.pathfinder.Path path = nav.createPath(entity.blockPosition(), 0);
        if (path == null && f > 8F) {
            int cx = Mth.floor(entity.getX()) - 2;
            int cz = Mth.floor(entity.getZ()) - 2;
            int cy = Mth.floor(entity.getBoundingBox().minY);
            for (int dx = 0; dx <= 4; dx++) {
                for (int dz = 0; dz <= 4; dz++) {
                    BlockPos pos = new BlockPos(cx + dx, cy - 1, cz + dz);
                    BlockState bsBelow = this.level().getBlockState(pos);
                    BlockState bsAt = this.level().getBlockState(pos.above());
                    BlockState bsAbove = this.level().getBlockState(pos.above(2));
                    if (((dx < 1) || (dz < 1) || (dx > 3) || (dz > 3))
                            && bsBelow.isCollisionShapeFullBlock(this.level(), pos)
                            && !bsAt.isCollisionShapeFullBlock(this.level(), pos.above())
                            && !bsAbove.isCollisionShapeFullBlock(this.level(), pos.above(2))) {
                        this.snapTo(cx + dx + 0.5D, cy, cz + dz + 0.5D, this.getYRot(), this.getXRot());
                        return;
                    }
                }
            }
        } else {
            nav.moveTo(path, 1D);
        }
    }

    public static boolean getCanSpawnHere(EntityType<MoCEntityAnimal> type, LevelAccessor world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        BlockState blockBelow = world.getBlockState(pos.below());
        boolean willSpawn = world.getBrightness(LightLayer.SKY, pos) > 8 && blockBelow.isValidSpawn(world, pos, type);
        
        // Add light level check from BiomeSpawnConfig if this is a Level (not just LevelAccessor)
        if (willSpawn && world instanceof Level) {
            willSpawn = drzhark.mocreatures.config.biome.BiomeSpawnConfig.checkLightLevelForEntity((Level) world, pos, type);
        }
        
        if (MoCreatures.proxy.debug && willSpawn) {
            MoCreatures.LOGGER.info("Animal: " + type.getDescription() + " at: " + pos + " State: " + world.getBlockState(pos) + " biome: " + MoCTools.biomeName((Level) world, pos));
        }
        return willSpawn;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag data = MoCTools.getEntityData(this);
        data.putBoolean("Adult", getIsAdult());
        data.putInt("Edad", getMoCAge());
        data.putString("Name", getPetName());
        data.putInt("TypeInt", getTypeMoC());
        MoCTools.writeTag(compound, "MoCData", data);
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        if (MoCTools.has(compound, "MoCData")) {
            CompoundTag data = MoCTools.readCompound(compound, "MoCData");
            setAdult(data.getBooleanOr("Adult", false));
            setMoCAge(data.getIntOr("Edad", 0));
            setPetName(data.getStringOr("Name", ""));
            setTypeMoC(data.getIntOr("TypeInt", 0));
        }
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isVehicle()) {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof LivingEntity) {
                this.moveWithRider(vec, (LivingEntity) passenger);
            }
            return;
        }
        if ((isAmphibian() && this.isInWater()) || (isFlyer() && getIsFlying())) {
            this.moveRelative(0.1F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9D, 0.9D, 0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.005D, 0));
            }
        } else {
            super.travel(vec);
        }
    }

    public void moveWithRider(Vec3 vec, LivingEntity passenger) {
        if (!this.isVehicle()) {
            return;
        }
        if (!getIsTamed()) {
            this.moveEntityWithRiderUntamed(vec, passenger);
            return;
        }
        boolean flySelfPropelled = selfPropelledFlyer() && isOnAir();
        boolean flyingMount = isFlyer() && this.isVehicle() && getIsTamed() && !this.onGround() && isOnAir();
        this.setYRot(passenger.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(passenger.getXRot() * 0.5F);
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.yBodyRot;
        if (!selfPropelledFlyer() || (selfPropelledFlyer() && !isOnAir())) {
            vec = new Vec3(passenger.xxa * 0.5F * getCustomSpeed(), vec.y, passenger.zza * getCustomSpeed());
        }
        if (this.jumpPending && isFlyer()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, flyerThrust(), 0.0D));
            this.jumpPending = false;
            if (flySelfPropelled) {
                float velX = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
                float velZ = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
                this.setDeltaMovement(this.getDeltaMovement().add(-0.5F * velX, 0.0D, 0.5F * velZ));
            }
        } else if (this.jumpPending && !getIsJumping()) {
            this.setDeltaMovement(this.getDeltaMovement().x, getCustomJump() * 2, this.getDeltaMovement().z);
            setIsJumping(true);
            this.jumpPending = false;
        }
        if (this.divePending) {
            this.divePending = false;
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.3D, 0.0D));
        }
        if (flyingMount) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.moveRelative(flyerFriction() / 10F, vec);
            this.setDeltaMovement(this.getDeltaMovement().multiply(flyerFriction(), myFallSpeed(), flyerFriction()).subtract(0.0D, 0.055D, 0.0D));
        } else {
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            super.travel(vec);
        }
        if (this.onGround()) {
            setIsJumping(false);
            this.divePending = false;
            this.jumpPending = false;
        }
    }

    public void moveEntityWithRiderUntamed(Vec3 vec, LivingEntity passenger) {
        if (!this.level().isClientSide()) {
            if (this.random.nextInt(10) == 0) {
                this.setDeltaMovement(this.random.nextGaussian() / 30D, this.getDeltaMovement().y, this.random.nextGaussian() / 10D);
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.random.nextInt(50) == 0) {
                passenger.stopRiding();
                this.jumpFromGround();
            }
            if (this instanceof IMoCTameable && passenger instanceof Player) {
                int chance = (getMaxTemper() - getTemper());
                if (chance <= 0) chance = 1;
                if (this.random.nextInt(chance * 8) == 0) {
                    MoCTools.tameWithName((Player) passenger, (IMoCTameable) this);
                }
            }
        }
    }

    public int maxFlyingHeight() {
        return 5;
    }

    protected double myFallSpeed() {
        return 0.6D;
    }

    protected double flyerThrust() {
        return 0.3D;
    }

    protected float flyerFriction() {
        return 0.91F;
    }

    protected boolean selfPropelledFlyer() {
        return false;
    }

    @Override
    public void makeEntityJump() {
        this.jumpPending = true;
    }

    public boolean isFlyer() {
        return false;
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

    public double getCustomSpeed() {
        return 0.6D;
    }

    public double getCustomJump() {
        return 0.4D;
    }

    protected SoundEvent getAngrySound() {
        return null;
    }

    public boolean rideableEntity() {
        return false;
    }

    @Override
    public int nameYOffset() {
        return -80;
    }

    @SuppressWarnings("unused")
    protected Entity findPlayerToAttack() {
        return null;
    }

    @SuppressWarnings("unused")
    public boolean isFlyingAlone() {
        return false;
    }

    @Override
    public void performAnimation(int attackType) {
        // Subclasses override for custom animations
    }

    @SuppressWarnings("unused")
    public boolean isMyFavoriteFood(ItemStack stack) {
        return false;
    }

    @Override
    public void makeEntityDive() {
        this.divePending = true;
    }

    public boolean isOnAir() {
        BlockPos pos1 = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2D), Mth.floor(this.getZ()));
        BlockPos pos2 = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 1.2D), Mth.floor(this.getZ()));
        return this.level().isEmptyBlock(pos1) && this.level().isEmptyBlock(pos2);
    }

    @Override
    public float getSizeFactor() {
        return 1.0F;
    }

    @Override
    public float getAdjustedYOffset() {
        return 0F;
    }

    protected boolean canBeTrappedInNet() {
        return (this instanceof IMoCTameable) && getIsTamed();
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide()) {
            dropMyStuff();
        }
        super.die(source);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (isNotScared()) {
            LivingEntity tempTarget = this.getTarget();
            boolean flag = super.hurtServer(serverLevel, source, amount);
            this.setTarget(tempTarget);
            return flag;
        }
        return super.hurtServer(serverLevel, source, amount);
    }


    /** 26.3 makes this abstract; 1.20's default was wheat. Creatures override it. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.WHEAT);
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
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        boolean flag = entityIn.hurtOrSimulate(
                this.level().damageSources().mobAttack(this),
                (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE)
        );
        if (flag) {
            this.doEnchantDamageEffects(this, entityIn);
        }
        return flag;
    }

    public boolean isReadyToHunt() {
        return false;
    }

    public boolean isReadyToFollowOwnerPlayer() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed();
    }

    /** 26.3 no longer tells canBeLeashed who holds the lead, so a tamed pet refuses a lead from anyone but its owner (or an op) here. */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!this.level().isClientSide() && player.getItemInHand(hand).is(Items.LEAD) && this.getIsTamed()
                && !MoCTools.isThisPlayerAnOP(player) && !player.getUUID().equals(this.getOwnerId())) {
            return InteractionResult.PASS;
        }
        return super.interact(player, hand, location);
    }

    @Override
    public boolean getIsSitting() {
        return false;
    }

    @Override
    public boolean isMovementCeased() {
        return getIsSitting() || this.isVehicle();
    }

    public boolean getIsHunting() {
        return this.huntingCounter != 0;
    }

    public void setIsHunting(boolean flag) {
        if (flag) {
            this.huntingCounter = this.random.nextInt(30) + 1;
        } else {
            this.huntingCounter = 0;
        }
    }

    public boolean getIsFollowingOwnerPlayer() {
        return this.followPlayerCounter != 0;
    }

    public void setIsFollowingOwnerPlayer(boolean flag) {
        if (flag) {
            this.followPlayerCounter = this.random.nextInt(30) + 1;
        } else {
            this.followPlayerCounter = 0;
        }
    }

    @Override
    public boolean shouldAttackPlayers() {
        return !getIsTamed() && this.level().getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean killedEntity(ServerLevel world, LivingEntity entityLivingIn, DamageSource killingDamage) {
        if (!(entityLivingIn instanceof Player)) {
            MoCTools.destroyDrops(this, 3D);
        }

        return true;
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.isInWater() && isAmphibian()) {
            return this.navigatorWater;
        }
        if (this.isFlyer() && getIsFlying()) {
            return this.navigatorFlyer;
        }
        return super.getNavigation();
    }

    public boolean isAmphibian() {
        return false;
    }

    @Override
    public boolean isDiving() {
        return false;
    }

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
        return 0.2D;
    }

    protected double maxDivingDepth() {
        return 1.0D;
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
    }

    @Override
    public int minFlyingHeight() {
        return 1;
    }

    @Override
    public boolean getIsFlying() {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        
        Entity passenger = this.getPassengers().get(0);
        return passenger instanceof LivingEntity ? (LivingEntity) passenger : null;
    }

    public boolean canRidePlayer() {
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
            if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null && !player.getUUID().equals(this.getOwnerId()) && !MoCTools.isThisPlayerAnOP(player)) {
                return;
            }
        }
        super.setLeashedTo(leashHolder, sendAttachNotification);
    }

    @Override
    public boolean isInWall() {
        return !this.isPassenger() && super.isInWall();
    }

    public void forceEntityJump() {
        this.jumpFromGround();
    }

    public boolean getIsRideable() {
        return false;
    }

    public void setRideable(boolean flag) {
        // Default implementation does nothing
    }

    public int getArmorType() {
        return 0;
    }

    public void dropArmor() {
        // Default implementation does nothing
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
    public void setArmorType(int i) {
    }


    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture(this.texture);
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return this.getBbHeight() >= entity.getBbHeight() && this.getBbWidth() >= entity.getBbWidth();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason reason) {
        // Always allow spawn eggs to work
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        
        // Prevent land animals from spawning in water
        if (world.getFluidState(this.blockPosition()).is(FluidTags.WATER)) {
            return false;
        }
        
        return super.checkSpawnRules(world, reason);
    }

    /**
     * Vanilla-style animal spawn rules with light level check integration
     */
    public static boolean checkAnimalSpawnRules(EntityType<MoCEntityAnimal> type, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        // Always allow spawn eggs
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            return true;
        }
        
        // Use vanilla animal spawn rules
        boolean willSpawn = Animal.checkAnimalSpawnRules(type, world, reason, pos, random);
        
        // Add light level check from BiomeSpawnConfig if this is a Level (not just LevelAccessor)
        if (willSpawn && world.getLevel() instanceof Level) {
            willSpawn = drzhark.mocreatures.config.biome.BiomeSpawnConfig.checkLightLevelForEntity((Level) world.getLevel(), pos, type);
        }
        
        if (MoCreatures.proxy.debug && willSpawn) {
            MoCreatures.LOGGER.info("Animal: " + type.getDescription() + " at: " + pos + " State: " + world.getBlockState(pos) + " biome: " + MoCTools.biomeName((Level) world.getLevel(), pos));
        }
        
        return willSpawn;
    }
}
