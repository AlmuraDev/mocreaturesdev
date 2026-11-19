/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;
import net.minecraft.world.entity.animal.pig.PigSoundVariants;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MoCEntityBoar extends MoCEntityAnimal {

    public MoCEntityBoar(EntityType<? extends MoCEntityBoar> type, Level world) {
        super(type, world);
        //setSize(0.9F, 0.9F);
        setAdult(this.random.nextInt(4) != 0);
        // TODO: Make hitboxes adjust depending on size
        //setAge(this.random.nextInt(15) + 45);
        setMoCAge(60);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIFleeFromPlayer(this, 1.0D, 4D));
        this.goalSelector.addGoal(3, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, AnimalEntity.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityAnimal.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public Identifier getTexture() {
        if (getIsAdult()) {
            return MoCreatures.proxy.getModelTexture("boar.png");
        }
        return MoCreatures.proxy.getModelTexture("boar_baby.png");
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if (entity != null && this.isPassengerOfSameVehicle(entity)) {
                return true;
            }
            if (entity != this && entity instanceof LivingEntity && super.shouldAttackPlayers() && getIsAdult()) {
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNotScared() {
        return getIsAdult();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds().hurtSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds().deathSound().value();
    }

    // TODO: Add unique sound event
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PIG_STEP.value(), 0.15F, 1.0F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BOAR;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityBoar) && super.canAttackTarget(entity);
    }

    @Override
    public boolean isReadyToHunt() {
        return this.getIsAdult() && !this.isMovementCeased();
    }

    @Override
    public float getSizeFactor() {
        if (getIsAdult()) {
            return 1F;
        }
        return getMoCAge() * 0.01F;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.75F));
    }
}
