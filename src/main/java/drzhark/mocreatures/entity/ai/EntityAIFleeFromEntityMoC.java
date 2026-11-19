/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.MoCEntityAquatic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * Updated for Minecraft 1.20.1 (Forge/Mojang mappings).
 */
public class EntityAIFleeFromEntityMoC extends Goal {

    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    private final Predicate<Entity> avoidTargetSelector;

    /** The mob this AI is controlling */
    protected Mob entity;

    /** Only pick targets that can be seen and are alive */
    public final Predicate<Entity> canBeSeenSelector = entityIn ->
            entityIn.isAlive() && this.entity.getSensing().hasLineOfSight(entityIn);

    protected Entity closestLivingEntity;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public EntityAIFleeFromEntityMoC(Mob mob, Predicate<Entity> targetSelector,
                                     float searchDistance, double farSpeedIn, double nearSpeedIn) {
        this.entity = mob;
        this.avoidTargetSelector = targetSelector;
        this.avoidDistance = searchDistance;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (this.entity instanceof IMoCEntity && ((IMoCEntity) this.entity).isNotScared()) {
            return false;
        }
        if (this.entity instanceof MoCEntityAquatic && !this.entity.isInWater()) {
            return false;
        }

        // Build an AABB inflated by avoidDistance horizontally and 3.0 vertically
        AABB searchBox = this.entity.getBoundingBox()
                .inflate(this.avoidDistance, 3.0D, this.avoidDistance);

        // Gather all entities of any class in that AABB, excluding spectators
        List<Entity> list = this.entity.level().getEntitiesOfClass(
                Entity.class,
                searchBox,
                e -> EntitySelector.NO_SPECTATORS.test(e)
                        && this.canBeSeenSelector.test(e)
                        && this.avoidTargetSelector.test(e)
        );

        if (list.isEmpty()) {
            return false;
        }

        this.closestLivingEntity = list.get(0);

        // Find a random position away from the closest entity
        BlockPos awayPos = RandomPos.generateRandomDirectionWithinRadians(
                this.entity.getRandom(),
                0.0D,
                16,                                   // horizontal search radius
                7,                                    // vertical search range
                (int) Math.floor(this.entity.getY()),// centerYOffset
                this.entity.getX() - this.closestLivingEntity.getX(),
                this.entity.getZ() - this.closestLivingEntity.getZ(),
                Math.PI                               // allow up to 180° deviation
        );

        if (awayPos == null) {
            return false;
        }

        // Convert that BlockPos to a Vec3 at its bottom‐center:
        Vec3 vec3 = Vec3.atBottomCenterOf(awayPos);
        // If the chosen “away” point ends up being closer to the threat than the mob, refuse:
        if (this.closestLivingEntity.distanceToSqr(vec3.x, vec3.y, vec3.z)
                < this.closestLivingEntity.distanceToSqr(this.entity)) {
            return false;
        }

        this.randPosX = vec3.x;
        this.randPosY = vec3.y;
        this.randPosZ = vec3.z;
        return true;
    }

    /**
     * Execute a one-shot task or start executing a continuous task
     */
    @Override
    public void start() {
        // Move towards the chosen flee position at nearSpeed
        this.entity.getNavigation().moveTo(this.randPosX, this.randPosY, this.randPosZ, this.nearSpeed);
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }

    /**
     * Updates the task each tick
     */
    @Override
    public void tick() {
        if (this.entity.distanceToSqr(this.closestLivingEntity) < 8.0D) {
            this.entity.getNavigation().setSpeedModifier(this.nearSpeed);
        } else {
            this.entity.getNavigation().setSpeedModifier(this.farSpeed);
        }
    }
}
