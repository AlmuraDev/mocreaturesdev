/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.util;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * This is an example/reference class showing how to update common patterns from 1.16.5 to 1.20.1.
 * Use this as a reference when updating the rest of your mod.
 */
public class MoCClassUpdater {

    /**
     * Helper method to get the level (world) from an entity in a non-deprecated way.
     * In 1.20.1, direct field access like entity.level is discouraged/deprecated.
     * 
     * @param entity The entity
     * @return The level (world) the entity is in
     */
    public static Level getLevel(Entity entity) {
        return entity.level(); // This is the preferred 1.20.1 way
    }
    
    /*
     * ENTITY DATA HANDLING
     * 
     * Old (1.16.5):
     * private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(MyCatClass.class, DataSerializers.BOOLEAN);
     * this.dataManager.register(SITTING, false);
     * boolean isSitting = this.dataManager.get(SITTING);
     * this.dataManager.set(SITTING, true);
     * 
     * New (1.20.1):
     * private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(MyCatClass.class, EntityDataSerializers.BOOLEAN);
     * this.entityData.define(SITTING, false);
     * boolean isSitting = this.entityData.get(SITTING);
     * this.entityData.set(SITTING, true);
     */
    
    /*
     * ENTITY METHODS
     * 
     * Old (1.16.5):
     * @Override
     * protected void registerData() { ... }
     * entity.getNavigator().clearPath();
     * entity.getLookController().setLookPositionWithEntity()
     * entity.getVerticalFaceSpeed()
     * entity.setAttackTarget(target);
     * entity.getAttackTarget()
     * entity.getLeashed()
     * entity.getDistanceSq(other)
     * entity.getPosX(), entity.getPosY(), entity.getPosZ()
     * entity.setPosition()
     * entity.setLocationAndAngles()
     * entity.rotationYaw, entity.rotationPitch 
     * entity.isBeingRidden()
     * entity.getRidingEntity()
     * entity.getEntityId()
     * entity.getHeldItem(hand)
     * entity.setHeldItem(hand, stack)
     * entity.remove()
     * entity.world (field)
     * 
     * New (1.20.1):
     * @Override 
     * protected void defineSynchedData(SynchedEntityData.Builder builder) { ... }
     * entity.getNavigation().stop();
     * entity.getLookControl().setLookAt()
     * entity.getMaxHeadXRot()
     * entity.setTarget(target);
     * entity.getTarget()
     * entity.isLeashed()
     * entity.distanceToSqr(other)
     * entity.getX(), entity.getY(), entity.getZ()
     * entity.setPos()
     * entity.moveTo()
     * entity.getYRot(), entity.getXRot()
     * entity.isPassenger()
     * entity.getVehicle()
     * entity.getId()
     * entity.getItemInHand(hand)
     * entity.setItemInHand(hand, stack)
     * entity.remove(RemovalReason.KILLED)
     * entity.level() (method)
     */
    
    /*
     * WORLD METHODS
     * 
     * Old (1.16.5):
     * world.isRemote
     * world.addEntity(entity)
     * world.getClosestPlayer(entity, distance)
     * world.getPlayers()
     * world.getBlockState(pos)
     * world.playSound(...)
     * 
     * New (1.20.1):
     * level.isClientSide()
     * level.addFreshEntity(entity) 
     * level.getNearestPlayer(entity, distance)
     * level.players()
     * level.getBlockState(pos)
     * level.playSound(...)
     */
    
    /*
     * PLAYER METHODS
     * 
     * Old (1.16.5):
     * player.abilities.isCreativeMode
     * player.isSneaking()
     * player.getUniqueID()
     * 
     * New (1.20.1):
     * player.isCreative()
     * player.isCrouching()
     * player.getUUID()
     */
    
    /*
     * BLOCKSTATE METHODS
     * 
     * Old (1.16.5):
     * blockstate.isSolidSide(world, pos, Direction.DOWN)
     * blockstate.getMaterial() == Material.AIR
     * 
     * New (1.20.1):
     * blockstate.isFaceSturdy(level, pos, Direction.UP)
     * blockstate.isAir() or blockstate.getMaterial() == Material.AIR
     */
    
    /*
     * GOAL/AI METHODS
     * 
     * Old (1.16.5):
     * this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE))
     * shouldExecute()
     * shouldContinueExecuting() 
     * startExecuting()
     * resetTask()
     * navigator.tryMoveToEntityLiving()
     * navigator.noPath()
     * 
     * New (1.20.1):
     * this.setFlags(EnumSet.of(Goal.Flag.MOVE))
     * canUse()
     * canContinueToUse()
     * start() 
     * stop()
     * navigation.moveTo()
     * navigation.isDone()
     */
    
    /*
     * MATH METHODS
     * 
     * Old (1.16.5):
     * MathHelper.floor()
     * 
     * New (1.20.1):
     * Mth.floor()
     */
    
    /*
     * NBT METHODS
     * 
     * Old (1.16.5):
     * CompoundNBT
     * ListNBT
     * ItemStack.read(nbt)
     * 
     * New (1.20.1):
     * CompoundTag
     * ListTag
     * ItemStack.of(nbt)
     */
    
    /*
     * HAND AND INTERACTION
     * 
     * Old (1.16.5):
     * ActionResultType
     * Hand
     * 
     * New (1.20.1):
     * InteractionResult
     * InteractionHand
     */
} 