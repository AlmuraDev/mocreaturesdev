/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAquatic;
import drzhark.mocreatures.entity.ai.EntityAIHunt;
import drzhark.mocreatures.entity.ai.EntityAITargetNonTamedMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAquatic;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MoCEntityShark extends MoCEntityTameableAquatic {

    public MoCEntityShark(EntityType<? extends MoCEntityShark> type, Level world) {
        super(type, world);
        this.texture = "shark.png";
        //setSize(1.65F, 0.9F);
        setAdult(true);
        // TODO: Make hitboxes adjust depending on size
        //setAge(60 + this.random.nextInt(100));
        setMoCAge(160);
        
        this.moveControl = new SharkMoveControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SharkSwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // Target players in water (but chase even if they're in boats - just don't attack)
        // Reduced targetChance to 5 for more frequent targeting checks
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 5, true, false, (player) -> {
            if (player == null) return false;
            // Target players in water, or within reasonable distance even if not in water
            return !getIsTamed() && isReadyToHunt() && (player.isInWater() || distanceTo(player) < 16.0F);
        }));
        
        // Target squids - more persistent targeting
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Squid.class, 5, true, false, (squid) -> {
            return squid != null && !getIsTamed() && isReadyToHunt();
        }));
        
        // Target dolphins only if config allows
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MoCEntityDolphin.class, 5, true, false, (dolphin) -> {
            return dolphin != null && !getIsTamed() && isReadyToHunt() && MoCreatures.proxy.attackDolphins;
        }));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAquatic.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 30.0D)
            .add(Attributes.MOVEMENT_SPEED, 1.0D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return 5;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        // Keep body rotation aligned when not in water
        if (!this.isEyeInFluid(FluidTags.WATER)) {
            this.yBodyRot = this.getYRot();
            this.setXRot(this.getXRot());
        }

        // Age progression for sharks
        if (!this.level().isClientSide()) {
            if (!getIsAdult() && (this.random.nextInt(50) == 0)) {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= getMaxAge()) {
                    setAdult(true);
                }
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i) && (this.level().getDifficulty().getId() > 0)) {
            Entity entity = damagesource.getEntity();
            if (entity != null && this.isVehicle() && entity == this.getPassengers().get(0)) {
                return true;
            }
            if (entity != this && entity instanceof LivingEntity) {
                setTarget((LivingEntity) entity);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        // Don't attack players in boats, but we can still chase them
        if (entityIn instanceof Player player && player.getVehicle() instanceof Boat) {
            return false;
        }
        
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.SHARK;
    }

    // This method enables hunting behavior - sharks will hunt when adult and difficulty > peaceful
    public boolean isReadyToHunt() {
        return getIsAdult() && this.level().getDifficulty().getId() > 0;
    }

    @Override
    public boolean isNotScared() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.61F;
    }

    @Override
    public void travel(Vec3 p_27490_) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, p_27490_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(p_27490_);
        }
    }

    @Override
    protected boolean usesNewAI() {
        return true;
    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public boolean isMovementCeased() {
        return !isInWater();
    }

    @Override
    protected double minDivingDepth() {
        return 1D;
    }

    @Override
    protected double maxDivingDepth() {
        return 6.0D;
    }

    @Override
    public int getMaxAge() {
        return 200;
    }

    static class SharkMoveControl extends MoveControl {
        private final MoCEntityShark shark;

        SharkMoveControl(MoCEntityShark shark) {
            super(shark);
            this.shark = shark;
        }

        public void tick() {
            if (this.shark.isEyeInFluid(FluidTags.WATER)) {
                this.shark.setDeltaMovement(this.shark.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.shark.getNavigation().isDone()) {
                float f = (float) (this.speedModifier * this.shark.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.shark.setSpeed(Mth.lerp(0.125F, this.shark.getSpeed(), f));
                double d0 = this.wantedX - this.shark.getX();
                double d1 = this.wantedY - this.shark.getY();
                double d2 = this.wantedZ - this.shark.getZ();
                if (d1 != 0.0D) {
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    this.shark.setDeltaMovement(this.shark.getDeltaMovement().add(0.0D,
                            (double) this.shark.getSpeed() * (d1 / d3) * 0.1D, 0.0D));
                }

                if (d0 != 0.0D || d2 != 0.0D) {
                    float f1 = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    this.shark.setYRot(this.rotlerp(this.shark.getYRot(), f1, 90.0F));
                    this.shark.yBodyRot = this.shark.getYRot();
                }

            } else {
                this.shark.setSpeed(0.0F);
            }
        }
    }

    static class SharkSwimGoal extends RandomSwimmingGoal {
        private final MoCEntityShark shark;

        public SharkSwimGoal(MoCEntityShark shark) {
            super(shark, 1.0D, 40);
            this.shark = shark;
        }

        public boolean canUse() {
            return super.canUse();
        }
    }
}
