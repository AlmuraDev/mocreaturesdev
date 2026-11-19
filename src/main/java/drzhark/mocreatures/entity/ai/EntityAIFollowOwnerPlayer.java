/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.UUID;

public class EntityAIFollowOwnerPlayer extends Goal {

    private final Mob thePet;
    private final double speed;
    private final PathNavigation petPathfinder;
    private final Level level;
    float maxDist;
    float minDist;
    private Player theOwner;
    private int delayCounter;

    public EntityAIFollowOwnerPlayer(Mob thePetIn, double speedIn, float minDistIn, float maxDistIn) {
        this.thePet = thePetIn;
        this.level = thePetIn.level();
        this.speed = speedIn;
        this.petPathfinder = thePetIn.getNavigation();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

        //if (!(thePetIn.getNavigator() instanceof PathNavigateGround)) {
        //System.out.println("exiting due to first illegal argument");
        //    throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        //}
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (((IMoCEntity) this.thePet).getIsSitting()) {
            return false;
        }

        if (!((MoCEntityAnimal) this.thePet).getIsFollowingOwnerPlayer()) {
            return false;
        }

        UUID ownerUniqueId = ((IMoCTameable) this.thePet).getOwnerId();
        if (ownerUniqueId == null) {
            return false;
        }

        Player entityplayer = EntityAITools.getIMoCTameableOwner((IMoCTameable) this.thePet);

        if (entityplayer == null) {
            return false;
        } else if (this.thePet.distanceToSqr(entityplayer) < this.minDist * this.minDist
                || this.thePet.distanceToSqr(entityplayer) > this.maxDist * this.maxDist) {
            return false;
        } else {
            this.theOwner = entityplayer;
            return true;
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return !this.petPathfinder.isDone() && this.thePet.distanceToSqr(this.theOwner) > this.maxDist * this.maxDist
                && !((IMoCEntity) this.thePet).getIsSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.delayCounter = 0;
        //this.flag = ((PathNavigateGround) this.thePet.getNavigator()).getAvoidsWater();
        //((PathNavigateGround) this.thePet.getNavigator()).setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        this.theOwner = null;
        this.petPathfinder.stop();
        //((PathNavigateGround) this.thePet.getNavigator()).setAvoidsWater(true); //TODO
    }

    private boolean isEmptyBlock(BlockPos pos) {
        BlockState iblockstate = this.level.getBlockState(pos);
        return iblockstate.isAir() || !iblockstate.canOcclude();  // New 1.20.1 API way to check
    }

    @Override
    public void tick() {
        this.thePet.getLookControl().setLookAt(this.theOwner, 10.0F, (float) this.thePet.getMaxHeadXRot());

        if (!((IMoCEntity) this.thePet).getIsSitting()) {
            if (--this.delayCounter <= 0) {
                this.delayCounter = 10;

                if (!this.petPathfinder.moveTo(this.theOwner, this.speed)) {
                    if (!this.thePet.isLeashed()) {
                        if (this.thePet.distanceToSqr(this.theOwner) >= 144.0D) {
                            int i = Mth.floor(this.theOwner.getX()) - 2;
                            int j = Mth.floor(this.theOwner.getZ()) - 2;
                            int k = Mth.floor(this.theOwner.getBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    final BlockPos pos = new BlockPos(i + l, k - 1, j + i1);
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP) && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                        this.thePet.snapTo((double)(i + l) + 0.5D, k, (double)(j + i1) + 0.5D, this.thePet.getYRot(), this.thePet.getXRot());
                                        this.petPathfinder.stop();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
