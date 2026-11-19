/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

import javax.annotation.Nullable;

public class MoCEntityMouse extends MoCEntityAnimal {
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(MoCEntityMouse.class, EntityDataSerializers.BOOLEAN);

    public MoCEntityMouse(EntityType<? extends MoCEntityMouse> type, Level world) {
        super(type, world);
        //setSize(0.45F, 0.3F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EntityAIFleeFromPlayer(this, 1.2D, 4D));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityAnimal.createAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, Boolean.FALSE);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();

        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(3) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("mouse_brown.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("mouse_white.png");
            default:
                return MoCreatures.proxy.getModelTexture("mouse_beige.png");
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        
        try {
            // In 1.20.1, we should use biome checks compatible with the MoCTools methods actually available
            String biomeName = this.level().getBiome(pos).unwrapKey().orElseThrow().location().getPath();
            
            if (biomeName.contains("mesa")) {
                setTypeMoC(2); // only brown mice
            }

            if (biomeName.contains("snow")) {
                setTypeMoC(3); // only white mice
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    public boolean getIsPicked() {
        return this.getVehicle() != null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_MOUSE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_MOUSE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_MOUSE_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.MOUSE;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        if (this.getVehicle() instanceof Player && this.getVehicle() == MoCreatures.proxy.getPlayer() && this.level().isClientSide()) {
            return (super.getMyRidingOffset() - 0.7F);
        }

        if ((this.getVehicle() instanceof Player) && this.level().isClientSide()) {
            return (super.getMyRidingOffset() - 0.1F);
        } else {
            return super.getMyRidingOffset();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.getVehicle() == null) {
            if (this.startRiding(player)) {
                this.setYRot(player.getYRot());
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
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
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.onGround() && (this.getVehicle() != null)) {
            this.setYRot(this.getVehicle().getYRot());
        }
    }

    public boolean upsideDown() {
        return getIsPicked();
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }
    
    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.EMPTY, SoundEvents.EMPTY);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.575F;
    }
}
