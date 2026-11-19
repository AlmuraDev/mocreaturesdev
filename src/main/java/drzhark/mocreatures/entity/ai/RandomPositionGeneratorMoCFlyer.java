/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;

public class RandomPositionGeneratorMoCFlyer {
    /**
     * used to store a direction when the user passes a point to move towards or away from. WARNING: NEVER THREAD SAFE.
     * MULTIPLE findTowards and findAway calls, will share this var
     */
    private static Vec3 staticVector = Vec3.ZERO;

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks
     */
    @Nullable
    public static Vec3 findRandomTarget(PathfinderMob entitycreatureIn, int xz, int y) {
        /*
          searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
          of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(entitycreatureIn, xz, y, null);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the direction of the point par3
     */
    @Nullable
    public static Vec3 findRandomTargetBlockTowards(PathfinderMob entitycreatureIn, int xz, int y, Vec3 targetVec3) {
        staticVector = targetVec3.subtract(entitycreatureIn.getX(), entitycreatureIn.getY(), entitycreatureIn.getZ());
        /*
          searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
          of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the reverse direction of the point par3
     */
    @Nullable
    public static Vec3 findRandomTargetBlockAwayFrom(PathfinderMob entitycreatureIn, int xz, int y, Vec3 targetVec3) {
        staticVector = (new Vec3(entitycreatureIn.getX(), entitycreatureIn.getY(), entitycreatureIn.getZ())).subtract(targetVec3);
        /*
          searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
          of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    /**
     * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction of
     * par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
     */
    @Nullable
    private static Vec3 findRandomTargetBlock(PathfinderMob entitycreatureIn, int xz, int y, @Nullable Vec3 targetVec3) {
        PathNavigation pathnavigate = entitycreatureIn.getNavigation();
        RandomSource random = entitycreatureIn.getRandom();
        boolean flag = false;
        int i = 0;
        int j = 0;
        int k = 0;
        float f = -99999.0F;
        boolean flag1;

        if (entitycreatureIn.hasHome()) {
            double d0 = entitycreatureIn.getHomePosition().distToCenterSqr(
                    Mth.floor(entitycreatureIn.getX()), 
                    Mth.floor(entitycreatureIn.getY()), 
                    Mth.floor(entitycreatureIn.getZ())) + 4.0D;
            double d1 = entitycreatureIn.getHomeRadius() + (float) xz;
            flag1 = d0 < d1 * d1;
        } else {
            flag1 = false;
        }

        for (int j1 = 0; j1 < 10; ++j1) {
            int l = random.nextInt(2 * xz + 1) - xz;
            int k1 = random.nextInt(2 * y + 1) - y;
            int i1 = random.nextInt(2 * xz + 1) - xz;

            if (targetVec3 == null || (double) l * targetVec3.x + (double) i1 * targetVec3.z >= 0.0D) {
                if (entitycreatureIn.hasHome() && xz > 1) {
                    BlockPos blockpos = entitycreatureIn.getHomePosition();

                    if (entitycreatureIn.getX() > (double) blockpos.getX()) {
                        l -= random.nextInt(xz / 2);
                    } else {
                        l += random.nextInt(xz / 2);
                    }

                    if (entitycreatureIn.getZ() > (double) blockpos.getZ()) {
                        i1 -= random.nextInt(xz / 2);
                    } else {
                        i1 += random.nextInt(xz / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos(Mth.floor((double) l + entitycreatureIn.getX()), Mth.floor((double) k1 + entitycreatureIn.getY()), Mth.floor((double) i1 + entitycreatureIn.getZ()));

                if ((!flag1 || entitycreatureIn.isWithinHome(blockpos1)))// && pathnavigate.canEntityStandOnPos(blockpos1))
                {
                    float f1 = entitycreatureIn.getWalkTargetValue(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        i = l;
                        j = k1;
                        k = i1;
                        flag = true;
                    }
                }
            }
        }

        if (flag) {
            return new Vec3((double) i + entitycreatureIn.getX(), (double) j + entitycreatureIn.getY(), (double) k + entitycreatureIn.getZ());
        } else {
            return null;
        }
    }
}