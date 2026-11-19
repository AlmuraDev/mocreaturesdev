/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.entity.ai.EntityAIFleeFromEntityMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAquatic;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MoCEntityMediumFish extends MoCEntityTameableAquatic {

    public static final String[] fishNames = { "Salmon", "Cod", "Bass" };

    public MoCEntityMediumFish(EntityType<? extends MoCEntityMediumFish> type, Level world) {
        super(type, world);
        // setSize(0.7f, 0.45f);
        // TODO: Make hitboxes adjust depending on size
        // setAge(30 + this.random.nextInt(70));
        setMoCAge(100);

        this.moveControl = new FishMoveControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    public static MoCEntityMediumFish createEntity(Level world, int type) {
        if (type == 1) {
            return (MoCEntityMediumFish) MoCEntities.SALMON.get().create(world);
        }
        if (type == 2) {
            return (MoCEntityMediumFish) MoCEntities.COD.get().create(world);
        }
        if (type == 3) {
            return (MoCEntityMediumFish) MoCEntities.BASS.get().create(world);
        }
        return (MoCEntityMediumFish) MoCEntities.SALMON.get().create(world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FishSwimGoal(this));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4,
                new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        // this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 1.0D, 50));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAquatic.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 7.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(fishNames.length) + 1);
        }
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
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (getIsTamed() && this.random.nextInt(100) == 0 && getHealth() < getMaxHealth()) {
                this.setHealth(getMaxHealth());
            }
        }
        if (!this.isEyeInFluid(FluidTags.WATER)) {
            this.yBodyRot = this.getYRot();
            this.setXRot(this.getXRot());
        }
    }

    @Override
    public float getSizeFactor() {
        return getMoCAge() * 0.0081F;
    }

    @Override
    public float getAdjustedYOffset() {
        if (!this.isEyeInFluid(FluidTags.WATER)) {
            return 1F;
        }
        return 0.5F;
    }

    @Override
    protected boolean isFisheable() {
        return !getIsTamed();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float yawRotationOffset() {
        if (!this.isEyeInFluid(FluidTags.WATER)) {
            return 90F;
        }
        return 90F + super.yawRotationOffset();
    }

    @Override
    public float rollRotationOffset() {
        if (!isInWater() && this.onGround()) {
            return -90F;
        }
        return 0F;
    }

    @Override
    public int nameYOffset() {
        return -30;
    }

    @Override
    public float getAdjustedZOffset() {
        if (!isInWater()) {
            return 0.2F;
        }
        return 0F;
    }

    @Override
    protected boolean canBeTrappedInNet() {
        return true;
    }

    @Override
    protected boolean usesNewAI() {
        return true;
    }

    @Override
    public float getSpeed() {
        return 0.5F;
    }

    @Override
    public boolean isMovementCeased() {
        return !isInWater();
    }

    @Override
    protected double minDivingDepth() {
        return 0.5D;
    }

    @Override
    protected double maxDivingDepth() {
        return 4.0D;
    }

    @Override
    public int getMaxAge() {
        return 120;
    }

    @Override
    public boolean isNotScared() {
        return getIsTamed();
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.775F;
    }

    static class FishMoveControl extends MoveControl {
        private final MoCEntityMediumFish fish;

        FishMoveControl(MoCEntityMediumFish p_27501_) {
            super(p_27501_);
            this.fish = p_27501_;
        }

        public void tick() {
            if (this.fish.isEyeInFluid(FluidTags.WATER)) {
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.fish.getNavigation().isDone()) {
                float f = (float) (this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), f));
                double d0 = this.wantedX - this.fish.getX();
                double d1 = this.wantedY - this.fish.getY();
                double d2 = this.wantedZ - this.fish.getZ();
                if (d1 != 0.0D) {
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D,
                            (double) this.fish.getSpeed() * (d1 / d3) * 0.1D, 0.0D));
                }

                if (d0 != 0.0D || d2 != 0.0D) {
                    float f1 = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    this.fish.setYRot(this.rotlerp(this.fish.getYRot(), f1, 90.0F));
                    this.fish.yBodyRot = this.fish.getYRot();
                }

            } else {
                this.fish.setSpeed(0.0F);
            }
        }
    }

    static class FishSwimGoal extends RandomSwimmingGoal {
        private final MoCEntityMediumFish fish;

        public FishSwimGoal(MoCEntityMediumFish p_27505_) {
            super(p_27505_, 1.0D, 40);
            this.fish = p_27505_;
        }

        public boolean canUse() {
            return super.canUse();
        }
    }
}
