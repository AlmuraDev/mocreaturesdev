/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class MoCEntityWraith extends MoCEntityMob//MoCEntityFlyerMob
{
    public int attackCounter;

    public MoCEntityWraith(EntityType<? extends MoCEntityWraith> type, Level world) {
        super(type, world);
        this.verticalCollision = false;
        //setSize(0.6F, 2.0F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes().add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i < 5 && MoCreatures.proxy.easterEggs) {
                setTypeMoC(2);
                setCustomName(Component.literal("Scratch"));
            } else {
                setTypeMoC(1);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        if (getTypeMoC() == 2) {
            return MoCreatures.proxy.getModelTexture("wraith_scratch.png");
        } else {
            return MoCreatures.proxy.getModelTexture(MoCreatures.proxy.alphaWraithEyes ? "wraith_alpha.png" : "wraith.png");
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCreatures.proxy.legacyWraithSounds ? MoCSoundEvents.ENTITY_WRAITH_DEATH_LEGACY.get() : MoCSoundEvents.ENTITY_WRAITH_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCreatures.proxy.legacyWraithSounds ? MoCSoundEvents.ENTITY_WRAITH_HURT_LEGACY.get() : MoCSoundEvents.ENTITY_WRAITH_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCreatures.proxy.legacyWraithSounds ? MoCSoundEvents.ENTITY_WRAITH_AMBIENT_LEGACY.get() : MoCSoundEvents.ENTITY_WRAITH_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.WRAITH;
    }


    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public int maxFlyingHeight() {
        return 10;
    }

    @Override
    public int minFlyingHeight() {
        return 3;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        startArmSwingAttack();
        return super.doHurtTarget(serverLevel, entityIn);
    }

    /**
     * Starts attack counters and synchronizes animations with clients
     */
    private void startArmSwingAttack() {
        if (!this.level().isClientSide()) {
            this.attackCounter = 1;
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 1));
        }
    }

    @Override
    public void aiStep() {
        if (this.attackCounter > 0) {
            this.attackCounter += 2;
            if (this.attackCounter > 10) this.attackCounter = 0;
        }

        super.aiStep();
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 1) {
            this.attackCounter = 1;
        }
    }

    @Override
    protected boolean isDaylightSensitive() {
        return true;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.86F));
    }
}
