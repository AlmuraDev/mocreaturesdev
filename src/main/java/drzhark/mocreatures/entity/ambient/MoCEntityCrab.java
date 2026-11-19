/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowOwnerPlayer;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityCrab extends MoCEntityTameableAnimal {

    public MoCEntityCrab(EntityType<? extends MoCEntityCrab> type, Level world) {
        super(type, world);
        //setSize(0.45F, 0.3F);
        setMoCAge(50 + this.random.nextInt(50));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIFollowOwnerPlayer(this, 0.8D, 6F, 5F));
        this.goalSelector.addGoal(4, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 1.5D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(5) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("crab_blue.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("crab_spotted.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("crab_green.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("crab_russet.png");
            default:
                return MoCreatures.proxy.getModelTexture("crab_red.png");
        }
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return 1 + this.level().getRandom().nextInt(3);
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.CRAB;
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }

    public boolean climbing() {
        return !this.onGround() && onClimbable();
    }

    @Override
    public void jumpFromGround() {
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity instanceof Player && this.getTarget() == null && !(entity.level().getDifficulty() == Difficulty.PEACEFUL)) {
            entity.hurtOrSimulate(this.damageSources().mobAttack(this), 1.5F);
        }

        super.doPush(entity);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        this.playSound(MoCSoundEvents.ENTITY_GOAT_SMACK.get(), 1.0F, 2.0F);
        return super.doHurtTarget(serverLevel, entity);
    }

    @Override
    public float getSizeFactor() {
        return 0.7F * getMoCAge() * 0.01F;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isFleeing() {
        return MoCTools.getMyMovementSpeed(this) > 0.09F;
    }


    @Override
    protected boolean canBeTrappedInNet() {
        return true;
    }

    @Override
    public int nameYOffset() {
        return -20;
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }
}
