/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class MoCEntityMole extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Integer> MOLE_STATE = SynchedEntityData.defineId(MoCEntityMole.class, EntityDataSerializers.INT);

    public MoCEntityMole(EntityType<? extends MoCEntityMole> type, Level world) {
        super(type, world);
        //setSize(1F, 0.5F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes().add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("mole.png");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOLE_STATE, 0); // state - 0 outside / 1 digging / 2 underground / 3 pick-a-boo

    }

    public boolean isOnDirt() {
        Block block =
                this.level().getBlockState(
                        new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getBoundingBox().minY - 0.5D), Mth
                                .floor(this.getZ()))).getBlock();
        return isDiggableBlock(block);
    }

    private boolean isDiggableBlock(Block block) {
        BlockState state = block.defaultBlockState();
        return state.is(net.minecraft.tags.BlockTags.DIRT) || 
               state.is(net.minecraft.tags.BlockTags.SAND) || 
               block == Blocks.GRAVEL;
    }

    /**
     * Moves entity forward underground
     */
    @SuppressWarnings("unused")
    private void digForward() {
        double coordY = this.getY();
        double coordZ = this.getZ();
        double coordX = this.getX();
        int x = 1;
        double newPosY = coordY - Math.cos((this.getXRot() - 90F) / 57.29578F) * x;
        double newPosX =
                coordX + Math.cos((MoCTools.realAngle(this.getYRot() - 90F) / 57.29578F)) * (Math.sin((this.getXRot() - 90F) / 57.29578F) * x);
        double newPosZ =
                coordZ + Math.sin((MoCTools.realAngle(this.getYRot() - 90F) / 57.29578F)) * (Math.sin((this.getXRot() - 90F) / 57.29578F) * x);
        Block block =
                this.level().getBlockState(
                                new BlockPos(Mth.floor(newPosX), Mth.floor(newPosY), Mth.floor(newPosZ)))
                        .getBlock();
        if (isDiggableBlock(block)) {
            this.setPos(newPosX, newPosY, newPosZ);
        }
    }

    /**
     * obtains State
     *
     * @return 0 outside / 1 digging / 2 underground / 3 pick-a-boo
     */
    public int getState() {
        return this.entityData.get(MOLE_STATE);
    }

    /**
     * Changes the state
     * 0 outside / 1 digging / 2 underground / 3 pick-a-boo
     */
    public void setState(int i) {
        this.entityData.set(MOLE_STATE, i);
    }

    @Override
    public float pitchRotationOffset() {

        int i = getState();
        switch (i) {
            case 1:
                return -45F;
            case 3:
                return 60F;
            default:
                return 0F;
        }
    }

    @Override
    public float getAdjustedYOffset() {
        int i = getState();
        switch (i) {
            case 1:
                return 0.3F;
            case 2:
                return 1F;
            case 3:
                return 0.1F;
            default:
                return 0F;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (this.random.nextInt(10) == 0 && getState() == 1) {
                setState(2);
            }

            if (getState() != 2 && getState() != 1 && isOnDirt()) {
                LivingEntity entityliving = getBoogey(4D);
                if ((entityliving != null) && this.hasLineOfSight(entityliving)) {
                    setState(1);
                    this.getNavigation().stop();
                }
            }

            //if underground and no enemies: pick a boo
            if (this.random.nextInt(20) == 0 && getState() == 2 && (getBoogey(4D) == null)) {
                setState(3);
                this.getNavigation().stop();
            }

            //if not on dirt, get out!
            if (getState() != 0 && !isOnDirt()) {
                setState(0);
            }

            if (this.random.nextInt(30) == 0 && getState() == 3) {
                setState(2);
            }

            /*
             * if (getState() == 2) { if (rand.nextInt(50) == 0) digForward(); }
             */

            //digging fx
            setSprinting(getState() == 1 || getState() == 2);
        }
    }

    @Override
    public boolean isMovementCeased() {
        return getState() == 1 || getState() == 3;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (getState() != 2) {
            return super.hurtServer(serverLevel, damagesource, i);
        }
        return false;
    }

    @Override
    public boolean isPushable() {
        return (getState() != 2);
    }

    @Override
    protected void doPush(Entity entity) {
        if (getState() != 2) {
            super.doPush(entity);
        }
    }

    @Override
    public boolean isInWall() {
        if (getState() == 2) {
            return false;
        }
        return super.isInWall();
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource source) {
        if (getState() == 2) {
            return true;
        }
        return super.isInvulnerableTo(serverLevel, source);
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
        return null;
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.MOLE;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.65F));
    }
}
