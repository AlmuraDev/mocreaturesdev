/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.MoCEntityAquatic;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.util.Mth;

public class EntityAIMoverHelperMoC extends MoveControl {

    protected MoveControl.Operation operation = MoveControl.Operation.WAIT;
    PathfinderMob theCreature;

    public EntityAIMoverHelperMoC(Mob entityliving) {
        super(entityliving);
        this.theCreature = (PathfinderMob) entityliving;
    }

    public boolean isUpdating() {
        return this.operation == MoveControl.Operation.MOVE_TO;
    }

    /**
     * Sets the speed and location to move to
     */
    @Override
    public void setWantedPosition(double x, double y, double z, double speedIn) {
        this.wantedX = x;
        this.wantedY = y;
        this.wantedZ = z;
        this.speedModifier = speedIn;
        this.operation = MoveControl.Operation.MOVE_TO;
    }

    public void strafe(float forward, float strafe) {
        this.operation = MoveControl.Operation.STRAFE;
        this.strafeForwards = forward;
        this.strafeRight = strafe;
        this.speedModifier = 0.25D;
    }

    public void onUpdateMoveOnGround() {
        if (this.operation == MoveControl.Operation.STRAFE) {
            float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float f1 = (float) this.speedModifier * f;
            float f2 = this.strafeForwards;
            float f3 = this.strafeRight;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = Mth.sin(this.mob.getYRot() * 0.017453292F);
            float f6 = Mth.cos(this.mob.getYRot() * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigation pathnavigate = this.mob.getNavigation();

            if (pathnavigate != null) {
                NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();

                if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double) f7), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + (double) f8)) != BlockPathTypes.WALKABLE) {
                    this.strafeForwards = 1.0F;
                    this.strafeRight = 0.0F;
                    f1 = f;
                }
            }

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - this.mob.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D) {
                this.mob.setZza(0.0F);
                return;
            }

            float f9 = (float) (Mth.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 20.0F));
            this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

            if (d2 > (double) this.mob.getStepHeight() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.mob.getBbWidth())) {
                this.mob.getJumpControl().jump();
            }
        } else {
            this.mob.setZza(0.0F);
        }
    }

    /**
     * Limits the given angle to a upper and lower limit.
     */
    protected float rotlerp(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f = Mth.wrapDegrees(p_75639_2_ - p_75639_1_);

        if (f > p_75639_3_) {
            f = p_75639_3_;
        }

        if (f < -p_75639_3_) {
            f = -p_75639_3_;
        }

        float f1 = p_75639_1_ + f;

        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }

        return f1;
    }

    @Override
    public void tick() {
        boolean isFlyer = ((IMoCEntity) theCreature).isFlyer();
        boolean isSwimmer = this.theCreature.isInWater();
        float fLimitAngle = 90F;
        if (!isFlyer && !isSwimmer) {
            onUpdateMoveOnGround();
            return;
        }

        /*
         * Flying specific movement code
         */
        if (isFlyer && !theCreature.isVehicle()) {
            this.flyingMovementUpdate();
        }

        /*
         * Water movement code
         */
        if (isSwimmer) {
            this.swimmerMovementUpdate();
            fLimitAngle = 30F;
        }
        if (this.operation == MoveControl.Operation.MOVE_TO && !this.theCreature.getNavigation().isDone()) {
            double d0 = this.wantedX - this.theCreature.getX();
            double d1 = this.wantedY - this.theCreature.getY();
            double d2 = this.wantedZ - this.theCreature.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            d3 = Mth.sqrt((float)d3);
            if (d3 < 0.5) {
                this.mob.setZza(0.0F);
                this.theCreature.getNavigation().stop();
                return;
            }
            //System.out.println("distance to objective = " + d3 + "objective: X = " + this.wantedX + ", Y = " + this.wantedY + ", Z = " + this.wantedZ);
            d1 /= d3;
            float f = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            this.theCreature.setYRot(this.rotlerp(this.theCreature.getYRot(), f, fLimitAngle));
            this.theCreature.yBodyRot = this.theCreature.getYRot();
            float f1 = (float) (this.speedModifier * this.theCreature.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.theCreature.setSpeed(this.theCreature.getSpeed() + (f1 - this.theCreature.getSpeed()) * 0.125F);
            double d4 = Math.sin((double) (this.theCreature.tickCount + this.theCreature.getId()) * 0.75D) * 0.01D;
            double d5 = Math.cos(this.theCreature.getYRot() * (float) Math.PI / 180.0F);
            double d6 = Math.sin(this.theCreature.getYRot() * (float) Math.PI / 180.0F);

            double targetDepth = MoCTools.waterSurfaceAtGivenEntity(this.theCreature) - this.theCreature.getY();
            double yMotion = 0.0D;

            if (targetDepth > ((IMoCEntity) this.theCreature).getDivingDepth()) {
                yMotion = 0.01D; // gently ascend
            } else if (targetDepth < ((IMoCEntity) this.theCreature).getDivingDepth() - 0.2D) {
                yMotion = -0.005D; // descend slightly
            }

            this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().add(d4 * d5, yMotion, d4 * d6));
        }
    }

    /**
     * Makes flying creatures reach the proper flying height
     */
    private void flyingMovementUpdate() {

        //Flying alone
        if (((IMoCEntity) theCreature).getIsFlying()) {
            int distY = MoCTools.distanceToFloor(this.theCreature);
            if (distY <= ((IMoCEntity) theCreature).minFlyingHeight()
                    && (this.theCreature.horizontalCollision || this.theCreature.level().getRandom().nextInt(100) == 0)) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().add(0.0F, 0.02D, 0.0F));
            }
            if (distY > ((IMoCEntity) theCreature).maxFlyingHeight() || this.theCreature.level().getRandom().nextInt(150) == 0) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().subtract(0.0F, 0.02D, 0.0F));
            }

        } else {
            if (this.theCreature.getDeltaMovement().y < 0) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().multiply(1.0F, 0.6D, 1.0F));
            }
        }

    }

    /**
     * Makes creatures in the water float to the right depth
     */
    private void swimmerMovementUpdate() {
        if (theCreature.isVehicle()) {
            return;
        }

        double distToSurface = (MoCTools.waterSurfaceAtGivenEntity(theCreature) - theCreature.getY());
        if (distToSurface > ((IMoCEntity) theCreature).getDivingDepth()) {
            if (theCreature.getDeltaMovement().y < 0) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().x(), 0, this.theCreature.getDeltaMovement().z());
            }
            this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().add(0.0F, 0.001D + (distToSurface * 0.01), 0.0F));
        }

        if (!theCreature.getNavigation().isDone() && theCreature.horizontalCollision) {
            if (theCreature instanceof MoCEntityAquatic) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().x(), 0.05D, this.theCreature.getDeltaMovement().z());
            } else {
                ((IMoCEntity) theCreature).forceEntityJump();
            }
        }

        if ((this.theCreature.getTarget() != null && ((this.theCreature.getTarget().getY() < (this.wantedX - 0.5D)) && this.theCreature
                .distanceTo(this.theCreature.getTarget()) < 10F))) {
            if (this.theCreature.getDeltaMovement().y < -0.1) {
                this.theCreature.setDeltaMovement(this.theCreature.getDeltaMovement().x(), -0.1, this.theCreature.getDeltaMovement().z());
            }
        }
    }
}
