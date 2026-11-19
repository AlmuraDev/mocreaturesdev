/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.*;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

import javax.annotation.Nullable;
import java.util.List;

public class MoCEntityBunny extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> HAS_EATEN = SynchedEntityData.defineId(MoCEntityBunny.class, EntityDataSerializers.BOOLEAN);
    private int bunnyReproduceTickerA;
    private int bunnyReproduceTickerB;
    private int jumpTimer;

    public MoCEntityBunny(EntityType<? extends MoCEntityBunny> type, Level world) {
        super(type, world);
        setAdult(true);
        setTamed(false);
        setMoCAge(50 + this.random.nextInt(15));
        if (this.random.nextInt(4) == 0) {
            setAdult(false);
        }
        //setSize(0.5F, 0.5F);
        this.bunnyReproduceTickerA = this.random.nextInt(64);
        this.bunnyReproduceTickerB = 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EntityAIFollowOwnerPlayer(this, 0.8D, 6F, 5F));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.0D));
        this.goalSelector.addGoal(3, new EntityAIFleeFromPlayer(this, 1.0D, 4D));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes().add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ARMOR, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EATEN, Boolean.FALSE);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        if (worldIn.getLevel().dimension() == MoCreatures.proxy.wyvernDimension) this.setPersistenceRequired();
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !getIsTamed() && this.level().dimension() != MoCreatures.proxy.wyvernDimension;
    }

    public boolean getHasEaten() {
        return this.entityData.get(HAS_EATEN);
    }

    public void setHasEaten(boolean flag) {
        this.entityData.set(HAS_EATEN, flag);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();

        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(5) + 1);
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos pos = new BlockPos(i, j, k);

        try {
            // In 1.20.1, check biome directly
            String biomeName = this.level().getBiome(pos).unwrapKey().orElseThrow().location().getPath();
            if (biomeName.contains("snow")) {
                setTypeMoC(3); //snow-white bunnies!
                return true;
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.legacyBunnyTextures ? MoCreatures.proxy.getModelTexture("bunny_beige.png") : MoCreatures.proxy.getModelTexture("bunny_beige_detailed.png");
            case 3:
                return MoCreatures.proxy.legacyBunnyTextures ? MoCreatures.proxy.getModelTexture("bunny_white.png") : MoCreatures.proxy.getModelTexture("bunny_white_detailed.png");
            case 4:
                return MoCreatures.proxy.legacyBunnyTextures ? MoCreatures.proxy.getModelTexture("bunny_black.png") : MoCreatures.proxy.getModelTexture("bunny_black_detailed.png");
            case 5:
                return MoCreatures.proxy.legacyBunnyTextures ? MoCreatures.proxy.getModelTexture("bunny_spotted.png") : MoCreatures.proxy.getModelTexture("bunny_spotted_detailed.png");
            default:
                return MoCreatures.proxy.legacyBunnyTextures ? MoCreatures.proxy.getModelTexture("bunny_golden.png") : MoCreatures.proxy.getModelTexture("bunny_golden_detailed.png");
        }
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_RABBIT_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_RABBIT_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BUNNY;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && (stack.getItem() == Items.GOLDEN_CARROT) && !getHasEaten()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            setHasEaten(true);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            return InteractionResult.SUCCESS;
        }
        if (this.getVehicle() == null) {
            if (this.startRiding(player)) {
                this.setYRot(player.getYRot());
                if (!getIsTamed() && !this.level().isClientSide()) {
                    MoCTools.tameWithName(player, this);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getVehicle() != null) {
            this.setYRot(this.getVehicle().getYRot());
        }

        if (!this.level().isClientSide()) {
            // Add the jumping logic that was missing
            if (--this.jumpTimer <= 0 && this.onGround() && ((this.getDeltaMovement().x > 0.05D) || (this.getDeltaMovement().z > 0.05D) || (this.getDeltaMovement().x < -0.05D) || (this.getDeltaMovement().z < -0.05D))) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.3D, this.getDeltaMovement().z);
                this.jumpTimer = 15;
            }
            
            // Original 1.16.5 reproduction logic
            if (!getIsTamed() || !getIsAdult() || !getHasEaten() || (this.getVehicle() != null)) {
                // Not ready for reproduction
            } else {
                if (this.bunnyReproduceTickerA < 1023) {
                    this.bunnyReproduceTickerA++;
                } else if (this.bunnyReproduceTickerB < 127) {
                    this.bunnyReproduceTickerB++;
                } else {
                    List<Entity> list1 = this.level().getEntities(this, this.getBoundingBox().inflate(4D, 2D, 4D));
                    for (Entity entity1 : list1) {
                        if (!(entity1 instanceof MoCEntityBunny) || (entity1 == this)) {
                            continue;
                        }
                        MoCEntityBunny entitybunny = (MoCEntityBunny) entity1;
                        if ((entitybunny.getVehicle() != null) || (entitybunny.bunnyReproduceTickerA < 1023) || !entitybunny.getIsAdult() || !entitybunny.getHasEaten()) {
                            continue;
                        }
                        MoCEntityBunny entitybunny1 = MoCEntities.BUNNY.get().create(this.level());
                        if (entitybunny1 != null) {
                            entitybunny1.setPos(this.getX(), this.getY(), this.getZ());
                            entitybunny1.setAdult(false);
                            int babytype = this.getTypeMoC();
                            if (this.random.nextInt(2) == 0) {
                                babytype = entitybunny.getTypeMoC();
                            }
                            entitybunny1.setTypeMoC(babytype);
                            this.level().addFreshEntity(entitybunny1);
                            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                            proceed();
                            entitybunny.proceed();
                            break;
                        }
                    }
                }
            }
        }
    }

    public void proceed() {
        setHasEaten(false);
        this.bunnyReproduceTickerB = 0;
        this.bunnyReproduceTickerA = this.random.nextInt(64);
    }

    @Override
    public int nameYOffset() {
        return -40;
    }
    
    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return (stack.getItem() == Items.CARROT);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (this.isInvulnerableTo(damagesource)) {
            return false;
        }
        
        // Add protection for bunny-hats (bunnies that are riding)
        if (this.getVehicle() != null) {
            return false;
        }
        
        return super.hurtServer(serverLevel, damagesource, i);
    }

    @Override
    public boolean isNotScared() {
        return getIsTamed();
    }

    @Override
    public double getMyRidingOffset() {
        if (this.getVehicle() instanceof Player) {
            return this.getVehicle().isCrouching() ? 0.25 : 0.5F;
        }

        return super.getMyRidingOffset();
    }

    @Override
    public float getAdjustedYOffset() {
        return 0.2F;
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.675F;
    }
}
