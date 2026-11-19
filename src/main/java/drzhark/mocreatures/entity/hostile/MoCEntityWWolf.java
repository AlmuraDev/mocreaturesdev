/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.entity.EntitySpawnReason;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.entity.hunter.MoCEntityBear;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MoCEntityWWolf extends MoCEntityMob {

    public int mouthCounter;
    public int tailCounter;

    public MoCEntityWWolf(EntityType<? extends MoCEntityWWolf> type, Level world) {
        super(type, world);
        //setSize(0.8F, 1.1F);
        setAdult(true);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoCEntityWWolf.AIWolfAttack(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityWWolf.AIWolfTarget<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new MoCEntityWWolf.AIWolfTarget<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(4) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("wild_wolf_black.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("wild_wolf_timber.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("wild_wolf_dark.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("wild_wolf_bright.png");
            default:
                return MoCreatures.proxy.getModelTexture("wild_wolf_classic.png");
        }
    }

    private void openMouth() {
        this.mouthCounter = 1;
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.random.nextInt(200) == 0) {
            moveTail();
        }

        if (this.mouthCounter > 0 && ++this.mouthCounter > 15) {
            this.mouthCounter = 0;
        }

        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(getBoundingBox().minY);
        int k = Mth.floor(this.getZ());

        ResourceKey<Biome> biome = MoCTools.biomeKind(this.level(), new BlockPos(i, j, k));
        
        // Simple check based on biome path instead of BiomeDictionary
        String biomePath = biome.identifier().getPath();
        if (biomePath.contains("snow") || biomePath.contains("frozen") || biomePath.contains("ice") || biomePath.contains("cold")) {
            setTypeMoC(3);
        }
        
        selectType();
        return true;
    }

    public static boolean getCanSpawnHere(EntityType<? extends MoCEntityMob> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        return MoCEntityMob.getCanSpawnHere(type, world, reason, pos, randomIn) && world.canSeeSky(pos);
    }

    //TODO move this
    public LivingEntity getClosestTarget(Entity entity, double d) {
        double d1 = -1D;
        LivingEntity entityliving = null;
        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(d), 
            e -> e instanceof LivingEntity && e != entity && e != entity.getVehicle() && !(e instanceof Player) 
                && !(e instanceof Monster) && !(e instanceof MoCEntityBigCat) && !(e instanceof MoCEntityBear) 
                && !(e instanceof Cow) && !((e instanceof Wolf) && !(MoCreatures.proxy.attackWolves))
                && !((e instanceof MoCEntityHorse) && !(MoCreatures.proxy.attackHorses)));
                
        for (Entity entity1 : list) {
            if (!(entity1 instanceof LivingEntity)) {
                continue;
            }
            
            double d2 = entity1.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
            if (((d < 0.0D) || (d2 < (d * d))) && ((d1 == -1D) || (d2 < d1)) && ((LivingEntity) entity1).hasLineOfSight(entity)) {
                d1 = d2;
                entityliving = (LivingEntity) entity1;
            }
        }

        return entityliving;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_WOLF_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        return MoCSoundEvents.ENTITY_WOLF_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        return MoCSoundEvents.ENTITY_WOLF_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.WILD_WOLF;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double dist = 0.1D;
            double newPosX = this.getX() + (dist * Math.sin(this.getYRot() / 57.29578F));
            double newPosZ = this.getZ() - (dist * Math.cos(this.getYRot() / 57.29578F));
            double newPosY = this.getY() + this.getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y);
            
            moveFunction.accept(passenger, newPosX, newPosY, newPosZ);
            passenger.setYRot(this.getYRot());
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.1D;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && !this.isVehicle() && this.random.nextInt(100) == 0) {
            List<Monster> list = this.level().getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(4D, 2D, 4D), 
                e -> e.getVehicle() == null && (e instanceof Skeleton || e instanceof Zombie || e instanceof MoCEntitySilverSkeleton));
                
            for (Monster monster : list) {
                monster.startRiding(this);
                break;
            }
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.945F));
    }

    static class AIWolfAttack extends MeleeAttackGoal {
        public AIWolfAttack(MoCEntityWWolf wolf, double speed, boolean useLongMemory) {
            super(wolf, speed, useLongMemory);
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
        protected boolean canPerformAttack(LivingEntity attackTarget) {
            // 1.20 attack reach: 4.0F + attackTarget.getBbWidth()
            return this.isTimeToAttack() && this.mob.distanceToSqr(attackTarget) <= (4.0F + attackTarget.getBbWidth()) && this.mob.getSensing().hasLineOfSight(attackTarget);
        }
    }

    static class AIWolfTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AIWolfTarget(MoCEntityWWolf wolf, Class<T> classTarget, boolean checkSight) {
            super(wolf, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f < 0.5F && super.canUse();
        }
    }
}
