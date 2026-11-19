/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class MoCEntityRat extends MoCEntityMob {

    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(MoCEntityRat.class,
            EntityDataSerializers.BOOLEAN);

    public MoCEntityRat(EntityType<? extends MoCEntityRat> type, Level world) {
        super(type, world);
        // setSize(0.58F, 0.455F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoCEntityRat.AIRatAttack(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityRat.AIRatTarget<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new MoCEntityRat.AIRatTarget<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, Boolean.FALSE);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();

        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 65) {
                setTypeMoC(1);
            } else if (i <= 98) {
                setTypeMoC(2);
            } else {
                setTypeMoC(3);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("rat_black.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("rat_white.png");
            default:
                return MoCreatures.proxy.getModelTexture("rat_brown.png");
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        ResourceKey<Biome> currentbiome = MoCTools.biomeKind(this.level(), pos);

        try {
            // Simple check based on biome ID - to be updated when BiomeDictionary is
            // resolved
            if (currentbiome.location().getPath().contains("desert")
                    || currentbiome.location().getPath().contains("mesa")) {
                setTypeMoC(1); // only brown rats
            }

            if (currentbiome.location().getPath().contains("snow")
                    || currentbiome.location().getPath().contains("frozen")) {
                setTypeMoC(3); // only white rats
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.EMPTY, SoundEvents.EMPTY);
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if (entity instanceof LivingEntity) {
            setTarget((LivingEntity) entity);
            if (!this.level().isClientSide()) {
                List<MoCEntityRat> list = this.level().getEntitiesOfClass(MoCEntityRat.class,
                        new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0D, this.getY() + 1.0D,
                                this.getZ() + 1.0D).inflate(16D, 4D, 16D));
                for (MoCEntityRat entityrat : list) {
                    if ((entityrat != null) && (entityrat.getTarget() == null)) {
                        entityrat.setTarget((LivingEntity) entity);
                    }
                }
            }
        }
        return super.hurtServer(serverLevel, damagesource, i);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if ((this.random.nextInt(100) == 0) && (this.getLightLevelDependentMagicValue() > 0.5F)) {
            setTarget(null);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCreatures.proxy.legacyRatDeathSound ? MoCSoundEvents.ENTITY_RAT_DEATH_LEGACY.get()
                : MoCSoundEvents.ENTITY_RAT_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_RAT_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_RAT_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.RAT;
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return this.entityData.get(CLIMBING);
    }

    public void setBesideClimbableBlock(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.5F;
    }

    static class AIRatAttack extends MeleeAttackGoal {
        public AIRatAttack(MoCEntityRat rat, double speed, boolean useLongMemory) {
            super(rat, speed, useLongMemory);
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();

            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }

    static class AIRatTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AIRatTarget(MoCEntityRat rat, Class<T> classTarget, boolean checkSight) {
            super(rat, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
