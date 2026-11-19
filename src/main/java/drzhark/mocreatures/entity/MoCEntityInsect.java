/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import drzhark.mocreatures.MoCTools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Updated for Minecraft 1.20.1 (Forge/Mojang mappings).
 */
public abstract class MoCEntityInsect extends MoCEntityAmbient {

    private int climbCounter;

    /**
     * In 1.20.1, constructors take a Level, not a World.
     */
    protected MoCEntityInsect(EntityType<? extends MoCEntityInsect> type, Level world) {
        super(type, world);
        // Replace flying controller:
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    /**
     * In 1.20.1, AttributeModifiers use AttributeSupplier.Builder instead of AttributeModifierMap.
     */
    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityAmbient.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.6D);
    }

    /**
     * Override createNavigation(Level) instead of createNavigator(World).
     * Use FlightPathNavigation rather than FlyingPathNavigator.
     */
    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, worldIn);
        nav.setCanPassDoors(true);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new WaterAvoidingRandomFlyingGoal(this, 0.8D));
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.2F;
    }

    @Override
    public boolean getIsFlying() {
        return this.isOnAir() && !this.onClimbable();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().add(1.0D, 0.6D, 1.0D));
        }

        if (!this.level().isClientSide()) {
            if (this.random.nextInt(50) == 0) {
                int[] ai = MoCTools.returnNearestBlockCoord(
                        this,
                        this.isAttractedToLight() ? Blocks.TORCH : Blocks.TALL_GRASS,
                        8D
                );
                if (ai[0] > -1000) {
                    // In 1.20.1: getNavigation().moveTo(x, y, z, speed)
                    this.getNavigation().moveTo(ai[0], ai[1], ai[2], 1.0D);
                }
            }
        } else {
            if (this.climbCounter > 0 && ++this.climbCounter > 8) {
                this.climbCounter = 0;
            }
        }
    }

    /**
     * Is this insect attracted to light?
     */
    public boolean isAttractedToLight() {
        return false;
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 1) { // climbing animation
            this.climbCounter = 1;
        }
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision; // collidedHorizontally → horizontalCollision
    }

    public boolean climbing() {
        return this.climbCounter != 0;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        // Insects don’t take fall damage—no changes needed here.
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
}
