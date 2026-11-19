/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

@SuppressWarnings("deprecation")
public class MoCEntityHorseMob extends MoCEntityMob {

    public int mouthCounter;
    public int textCounter;
    public int standCounter;
    public int tailCounter;
    public int eatingCounter;
    public int wingFlapCounter;
    private boolean isImmuneToFire;

    public MoCEntityHorseMob(EntityType<? extends MoCEntityHorseMob> type, Level world) {
        super(type, world);
        //setSize(1.3964844F, 1.6F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void selectType() {
        if (this.level().dimensionType().ultraWarm()) {
            setTypeMoC(38);
            this.isImmuneToFire = true;
        } else {
            if (getTypeMoC() == 0) {
                int j = this.random.nextInt(100);
                if (j <= (40)) {
                    setTypeMoC(23); //undead
                } else if (j <= (80)) {
                    setTypeMoC(26); //skeleton horse
                } else {
                    setTypeMoC(32); //bat
                }
            }
        }
    }

    /**
     * Overridden for the dynamic nightmare texture. *
     * 23 Undead
     * 24 Undead Unicorn
     * 25 Undead Pegasus
     * 26 skeleton
     * 27 skeleton unicorn
     * 28 skeleton pegasus
     * 30 bug horse
     * 32 Bat Horse
     * 38 nightmare
     */
    @Override
    public Identifier getTexture() {

        switch (getTypeMoC()) {
            case 23://undead horse

                if (!MoCreatures.proxy.getAnimateTextures()) {
                    return MoCreatures.proxy.getModelTexture("horseundead.png");
                }
                String baseTex = "horseundead";
                int max = 79;

                if (this.random.nextInt(3) == 0) {
                    this.textCounter++;
                }
                if (this.textCounter < 10) {
                    this.textCounter = 10;
                }
                if (this.textCounter > max) {
                    this.textCounter = 10;
                }

                String iteratorTex = String.valueOf(this.textCounter);
                iteratorTex = iteratorTex.substring(0, 1);
                String decayTex = String.valueOf(getMoCAge() / 100);
                decayTex = decayTex.substring(0, 1);
                return MoCreatures.proxy.getModelTexture(baseTex + decayTex + iteratorTex + ".png");

            case 26:
                return MoCreatures.proxy.getModelTexture("horseskeleton.png");

            case 32:
                return MoCreatures.proxy.getModelTexture("horsebat.png");

            case 38:
                if (!MoCreatures.proxy.getAnimateTextures()) {
                    return MoCreatures.proxy.getModelTexture("horsenightmare1.png");
                }
                this.textCounter++;
                if (this.textCounter < 10) {
                    this.textCounter = 10;
                }
                if (this.textCounter > 59) {
                    this.textCounter = 10;
                }
                String NTA = "horsenightmare";
                String NTB = String.valueOf(this.textCounter);
                NTB = NTB.substring(0, 1);
                String NTC = ".png";

                return MoCreatures.proxy.getModelTexture(NTA + NTB + NTC);

            default:
                return MoCreatures.proxy.getModelTexture("horseundead.png");
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        openMouth();
        return MoCSoundEvents.ENTITY_HORSE_DEATH_UNDEAD.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        stand();
        return MoCSoundEvents.ENTITY_HORSE_HURT_UNDEAD.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        if (this.random.nextInt(10) == 0) {
            stand();
        }
        return MoCSoundEvents.ENTITY_HORSE_AMBIENT_UNDEAD.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (!blockIn.liquid()) {
            SoundType soundtype = blockIn.getBlock().getSoundType(blockIn, level(), pos, this);

            if (this.level().getBlockState(pos.above()).getBlock() == Blocks.SNOW) {
                soundtype = Blocks.SNOW.getSoundType(this.level().getBlockState(pos.above()), level(), pos, this);
            } else if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound(SoundEvents.HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
        }
    }

    public boolean isOnAir() {
        return this.level().isEmptyBlock(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2D), Mth.floor(this.getZ())));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
            this.mouthCounter = 0;
        }

        if (this.standCounter > 0 && ++this.standCounter > 20) {
            this.standCounter = 0;
        }

        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }

        if (this.eatingCounter > 0 && ++this.eatingCounter > 50) {
            this.eatingCounter = 0;
        }

        if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
            this.wingFlapCounter = 0;
            //TODO flap sound!
        }
    }

    @Override
    public boolean isFlyer() {
        return this.getTypeMoC() == 25 //undead pegasus
                || this.getTypeMoC() == 32 // bat horse
                || this.getTypeMoC() == 28; // skeleton pegasus
    }

    /**
     * Has a unicorn? to render it and buckle entities!
     */
    public boolean isUnicorned() {
        return this.getTypeMoC() == 24 || this.getTypeMoC() == 27 || this.getTypeMoC() == 32;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (isOnAir() && isFlyer() && this.random.nextInt(5) == 0) {
            this.wingFlapCounter = 1;
        }

        if (this.random.nextInt(200) == 0) {
            moveTail();
        }

        if (!isOnAir() && (!this.isVehicle()) && this.random.nextInt(250) == 0) {
            stand();
        }

        if (this.level().isClientSide() && getTypeMoC() == 38 && this.random.nextInt(50) == 0) {
            LavaFX();
        }

        if (this.level().isClientSide() && getTypeMoC() == 23 && this.random.nextInt(50) == 0) {
            UndeadFX();
        }

        if (!this.level().isClientSide()) {
            if (isFlyer() && this.random.nextInt(500) == 0) {
                wingFlap();
            }

            if (!isOnAir() && (!this.isVehicle()) && this.random.nextInt(300) == 0) {
                setEating();
            }

            if (!this.isVehicle() && this.random.nextInt(100) == 0) {
                MoCTools.findMobRider(this);
            }
        }
    }

    private void openMouth() {
        this.mouthCounter = 1;
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    private void setEating() {
        this.eatingCounter = 1;
    }

    private void stand() {
        this.standCounter = 1;
    }

    public void wingFlap() {
        this.wingFlapCounter = 1;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(serverLevel, serverLevel, recentlyHitIn);
        boolean flag = (this.random.nextInt(100) < MoCreatures.proxy.rareItemDropChance);
        if (this.getTypeMoC() == 32 && MoCreatures.proxy.rareItemDropChance < 25) {
            flag = (this.random.nextInt(100) < 25);
        }

        Item drop = Items.LEATHER;

        if (flag && (this.getTypeMoC() == 36 || (this.getTypeMoC() >= 50 && this.getTypeMoC() < 60))) //unicorn
        {
            drop = MoCItems.UNICORNHORN.get();
        }

        if (this.getTypeMoC() == 38 && flag && this.level().dimensionType().ultraWarm()) //nightmare
        {
            drop = MoCItems.HEARTFIRE.get();
        }
        if (this.getTypeMoC() == 32 && flag) //bat horse
        {
            drop = MoCItems.HEARTDARKNESS.get();
        }
        if (this.getTypeMoC() == 26)//skely
        {
            drop = Items.BONE;
        }
        if ((this.getTypeMoC() == 23 || this.getTypeMoC() == 24 || this.getTypeMoC() == 25)) {
            if (flag) {
                drop = MoCItems.HEARTUNDEAD.get();
            }
            else {
                drop = Items.ROTTEN_FLESH;
                }
        }

        if (this.getTypeMoC() == 21 || this.getTypeMoC() == 22) {
            drop = Items.GHAST_TEAR;
        }

        int i = this.random.nextInt(3);

        if (looting > 0)
        {
            i += this.random.nextInt(looting + 1);
        }

        this.spawnAtLocation(new ItemStack(drop, i));
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        if (entityIn instanceof Player && !shouldAttackPlayers()) {
            return false;
        }
        if (this.onGround() && !isOnAir()) {
            stand();
        }
        openMouth();
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_HORSE_ANGRY_UNDEAD.get());
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);

        if ((this.getTypeMoC() == 23) || (this.getTypeMoC() == 24) || (this.getTypeMoC() == 25)) {
            MoCTools.spawnSlimes(this.level(), this);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.1D;
    }

    public boolean checkSpawnRules(LevelAccessor worldIn, EntitySpawnReason spawnReasonIn) {
        if (this.getY() < 50D && !level().dimensionType().ultraWarm()) {
            setTypeMoC(32);
        }
        return super.checkSpawnRules(worldIn, spawnReasonIn);
    }

    public void UndeadFX() {
        MoCreatures.proxy.UndeadFX(this);
    }

    public void LavaFX() {
        MoCreatures.proxy.LavaFX(this);
    }

    /**
     * Get this Entity's MobType
     */
    @Override
    public MobType getMobType() {
        if (getTypeMoC() == 23 || getTypeMoC() == 24 || getTypeMoC() == 25) {
            return MobType.UNDEAD;
        }
        return super.getMobType();
    }

    @Override
    protected boolean isDaylightSensitive() {
        return true;
    }

    @Override
    public int maxFlyingHeight() {
        return 10;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double dist = (0.4D);
            double newPosX = this.getX() + (dist * Math.sin(this.getYRot() / 57.29578F));
            double newPosZ = this.getZ() - (dist * Math.cos(this.getYRot() / 57.29578F));
            double newPosY = this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset();
            
            moveFunction.accept(passenger, newPosX, newPosY, newPosZ);
            passenger.setYRot(this.getYRot());
        }
    }

    // Adjusted to avoid most of the roof suffocation for now
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.9F;
    }

    @Override
    public boolean fireImmune() {
        return this.isImmuneToFire ? true : super.fireImmune();
    }
}
