/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowHerd;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityPiranha extends MoCEntitySmallFish {

    public MoCEntityPiranha(EntityType<? extends MoCEntityPiranha> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new EntityAIFollowHerd(this, 0.6D, 4D, 20D, 1));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntitySmallFish.createAttributes()
            .add(Attributes.MAX_HEALTH, 5.0D)
            .add(Attributes.ATTACK_DAMAGE, 3.5D);
    }

    @Override
    public void selectType() {
        setTypeMoC(1);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("smallfish_piranha.png");
    }

    protected Entity findPlayerToAttack() {
        if ((this.level().getDifficulty().getId() > 0)) {
            Player entityplayer = this.level().getNearestPlayer(this, 12D);
            if ((entityplayer != null) && entityplayer.isInWater() && !getIsTamed()) {
                return entityplayer;
            }
        }
        return null;
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return 3;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i) && (this.level().getDifficulty().getId() > 0)) {
            Entity entity = damagesource.getEntity();
            if (entity instanceof LivingEntity) {
                if (this.isVehicle() && entity == this.getPassengers().get(0)) {
                    return true;
                }
                if (entity != this) {
                    this.setTarget((LivingEntity) entity);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNotScared() {
        return true;
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.PIRANHA;
    }
}
