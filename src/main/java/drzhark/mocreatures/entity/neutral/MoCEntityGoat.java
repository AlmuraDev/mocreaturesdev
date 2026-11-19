/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIPanicMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;

public class MoCEntityGoat extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> IS_CHARGING = SynchedEntityData.defineId(MoCEntityGoat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_UPSET = SynchedEntityData.defineId(MoCEntityGoat.class, EntityDataSerializers.BOOLEAN);
    public int movecount;
    private boolean hungry;
    private boolean swingLeg;
    private boolean swingEar;
    private boolean swingTail;
    private boolean bleat;
    private boolean eating;
    private int bleatcount;
    private int attacking;
    private int chargecount;
    private int tailcount; // 90 to -45
    private int earcount; // 20 to 40 default = 30
    private int eatcount;

    public MoCEntityGoat(EntityType<? extends MoCEntityGoat> type, Level world) {
        super(type, world);
        // TODO: Separate hitbox for female goats
        //setSize(0.8F, 0.9F);
        setAdult(true);
        setMoCAge(70);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.0D));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ARMOR, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGING, Boolean.FALSE);
        builder.define(IS_UPSET, Boolean.FALSE);
    }

    public boolean getUpset() {
        return this.entityData.get(IS_UPSET);
    }

    public void setUpset(boolean flag) {
        this.entityData.set(IS_UPSET, flag);
    }

    public boolean getCharging() {
        return this.entityData.get(IS_CHARGING);
    }

    public void setCharging(boolean flag) {
        this.entityData.set(IS_CHARGING, flag);
    }

    @Override
    public void selectType() {
        /*
         * type 1 = baby type 2 = female type 3 = female 2 type 4 = female 3
         * type 5 = male 1 type 6 = male 2 type 7 = male 3
         */
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 15) {
                setTypeMoC(1);
                setMoCAge(50);
            } else if (i <= 30) {
                setTypeMoC(2);
                setMoCAge(70);
            } else if (i <= 45) {
                setTypeMoC(3);
                setMoCAge(70);
            } else if (i <= 60) {
                setTypeMoC(4);
                setMoCAge(70);
            } else if (i <= 75) {
                setTypeMoC(5);
                setMoCAge(90);
            } else if (i <= 90) {
                setTypeMoC(6);
                setMoCAge(90);
            } else {
                setTypeMoC(7);
                setMoCAge(90);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("goat_brown_light.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("goat_brown_spotted.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("goat_gray_spotted.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("goat_gray.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("goat_brown.png");
            default:
                return MoCreatures.proxy.getModelTexture("goat_white.png");
        }
    }

    public void calm() {
        setTarget(null);
        setUpset(false);
        setCharging(false);
        this.attacking = 0;
        this.chargecount = 0;
    }

    @Override
    public void jumpFromGround() {
        if (getTypeMoC() == 1) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.41D, this.getDeltaMovement().z);
        } else if (getTypeMoC() < 5) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.45D, this.getDeltaMovement().z);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.5D, this.getDeltaMovement().z);
        }

        if (this.hasEffect(MobEffects.JUMP_BOOST)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (this.getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F, 0.0D));
        }
        if (this.isSprinting()) {
            float f = this.getYRot() * 0.01745329F;
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(f) * -0.2F, 0.0D, Mth.cos(f) * 0.2F));
        }
        this.hasImpulse = true;
    }

    @Override
    public void tick() {
        super.tick();
        
        if (getBleating()) {
            this.bleatcount++;
            if (this.bleatcount > 15) {
                this.bleatcount = 0;
                setBleating(false);
            }
        }

        if (getSwingLeg()) {
            this.movecount += 5;
            if (this.movecount == 30) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOAT_DIGG.get());
            }

            if (this.movecount > 100) {
                setSwingLeg(false);
                this.movecount = 0;
            }
        }

        if (getSwingEar()) {
            this.earcount += 5;
            if (this.earcount > 40) {
                setSwingEar(false);
                this.earcount = 0;
            }
        }

        if (getSwingTail()) {
            this.tailcount += 15;
            if (this.tailcount > 135) {
                setSwingTail(false);
                this.tailcount = 0;
            }
        }

        if (getEating()) {
            this.eatcount += 1;
            if (this.eatcount == 2) {
                Player entityplayer1 = this.level().getNearestPlayer(this, 3D);
                if (entityplayer1 != null) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOAT_EATING.get());
                }
            }
            if (this.eatcount > 25) {
                setEating(false);
                this.eatcount = 0;
            }
        }
    }
    
    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level().isClientSide()) {
            if (this.random.nextInt(100) == 0) {
                setSwingEar(true);
            }

            if (this.random.nextInt(80) == 0) {
                setSwingTail(true);
            }

            if (this.random.nextInt(50) == 0) {
                setEating(true);
            }
        }
        
        if ((this.hungry) && (this.random.nextInt(20) == 0)) {
            this.hungry = false;
        }

        if (!this.level().isClientSide() && (getMoCAge() < 90 || getTypeMoC() > 4 && getMoCAge() < 100) && this.random.nextInt(500) == 0) {
            setMoCAge(getMoCAge() + 1);
            if (getTypeMoC() == 1 && getMoCAge() > 70) {
                int i = this.random.nextInt(6) + 2;
                setTypeMoC(i);
            }
        }

        if (getUpset()) {
            this.attacking += (this.random.nextInt(4)) + 2;
            if (this.attacking > 75) {
                this.attacking = 75;
            }

            if (this.random.nextInt(200) == 0 || getTarget() == null) {
                calm();
            }

            if (!getCharging() && this.random.nextInt(35) == 0) {
                swingLeg();
            }

            if (!getCharging()) {
                this.getNavigation().stop();
            }

            if (getTarget() != null) {
                lookAt(getTarget(), 10F, 10F);
                if (this.random.nextInt(80) == 0) {
                    setCharging(true);
                }
            }
        }

        if (getCharging()) {
            this.chargecount++;
            if (this.chargecount > 120) {
                this.chargecount = 0;
            }
            if (getTarget() == null) {
                calm();
            }
        }

        if (!getUpset() && !getCharging()) {
            Player player = this.level().getNearestPlayer(this, 24D);
            if (player != null) {// Behaviour that happens only close to player :)

                // is there food around? only check with player near
                ItemEntity entityitem = getClosestEntityItem(this, 10D);
                if (entityitem != null) {
                    float f = entityitem.distanceTo(this);
                    if (f > 2.0F) {
                        int i = Mth.floor(entityitem.getX());
                        int j = Mth.floor(entityitem.getY());
                        int k = Mth.floor(entityitem.getZ());
                        faceLocation(i, j, k, 30F);

                        setPathToEntity(entityitem, f);
                        return;
                    }
                    if (f < 2.0F && this.deathTime == 0 && this.random.nextInt(50) == 0) {
                        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOAT_EATING.get());
                        setEating(true);

                        entityitem.remove(Entity.RemovalReason.DISCARDED);
                        return;
                    }
                }

                // find other goat to play!
                if (getTypeMoC() > 4 && this.random.nextInt(200) == 0) {
                    MoCEntityGoat entitytarget = (MoCEntityGoat) getClosestEntityLiving(this, 14D);
                    if (entitytarget != null) {
                        setUpset(true);
                        setTarget(entitytarget);
                        entitytarget.setUpset(true);
                        entitytarget.setTarget(this);
                    }
                }
            }// end of close to player behavior
        }// end of !upset !charging
    }

    @Override
    public boolean isMyFavoriteFood(ItemStack stack) {
        return !stack.isEmpty() && MoCTools.isItemEdible(stack.getItem());
    }

    @Override
    public int getAmbientSoundInterval() {
        if (this.hungry) {
            return 80;
        }

        return 200;
    }

    @Override
    public boolean entitiesToIgnore(Entity entity) {
        return ((!(entity instanceof MoCEntityGoat)) || ((((MoCEntityGoat) entity).getTypeMoC() < 5)));
    }

    @Override
    public boolean isMovementCeased() {
        return getUpset() && !getCharging();
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        this.attacking = 30;
        if (entityIn instanceof MoCEntityGoat) {
            MoCTools.bigSmack(this, entityIn, 0.4F);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOAT_SMACK.get());
            if (this.random.nextInt(3) == 0) {
                calm();
                ((MoCEntityGoat) entityIn).calm();
            }
            return false;
        }
        MoCTools.bigSmack(this, entityIn, 0.8F);
        if (this.random.nextInt(3) == 0) {
            calm();
        }
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    @Override
    public boolean isNotScared() {
        return getTypeMoC() > 4;
    }

    private void swingLeg() {
        if (!getSwingLeg()) {
            setSwingLeg(true);
            this.movecount = 0;
        }
    }

    public boolean getSwingLeg() {
        return this.swingLeg;
    }

    public void setSwingLeg(boolean flag) {
        this.swingLeg = flag;
    }

    public boolean getSwingEar() {
        return this.swingEar;
    }

    public void setSwingEar(boolean flag) {
        this.swingEar = flag;
    }

    public boolean getSwingTail() {
        return this.swingTail;
    }

    public void setSwingTail(boolean flag) {
        this.swingTail = flag;
    }

    public boolean getEating() {
        return this.eating;
    }

    public void setEating(boolean flag) {
        this.eating = flag;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (entity != this && entity instanceof LivingEntity && super.shouldAttackPlayers() && getTypeMoC() > 4) {
                setTarget((LivingEntity) entity);
                setUpset(true);
            }
            return true;
        } else {
            return false;
        }
    }

    public int legMovement() {
        if (!getSwingLeg()) {
            return 0;
        }

        if (this.movecount < 21) {
            return this.movecount * -1;
        }
        if (this.movecount < 70) {
            return this.movecount - 40;
        }
        return -this.movecount + 100;
    }

    public int earMovement() {
        // 20 to 40 default = 30
        if (!getSwingEar()) {
            return 0;
        }
        if (this.earcount < 11) {
            return this.earcount + 30;
        }
        if (this.earcount < 31) {
            return -this.earcount + 50;
        }
        return this.earcount - 10;
    }

    public int tailMovement() {
        // 90 to -45
        if (!getSwingTail()) {
            return 90;
        }

        return this.tailcount - 45;
    }

    public int mouthMovement() {
        if (!getEating()) {
            return 0;
        }
        if (this.eatcount < 6) {
            return this.eatcount;
        }
        if (this.eatcount < 16) {
            return -this.eatcount + 10;
        }
        return this.eatcount - 20;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.is(Items.BUCKET)) {
            if (getTypeMoC() > 4) {
                setUpset(true);
                setTarget(player);
                return InteractionResult.FAIL;
            }
            if (getTypeMoC() == 1) {
                return InteractionResult.FAIL;
            }

            if (!player.getAbilities().instabuild) stack.shrink(1);
            player.getInventory().add(new ItemStack(Items.MILK_BUCKET));
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && !stack.isEmpty() && (MoCTools.isItemEdible(stack.getItem()))) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            this.setHealth(getMaxHealth());
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOAT_EATING.get());
            return InteractionResult.SUCCESS;
        }

        if (!getIsTamed() && !stack.isEmpty() && MoCTools.isItemEdible(stack.getItem())) {
            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public boolean getBleating() {
        return this.bleat && (getAttacking() == 0);
    }

    public void setBleating(boolean flag) {
        this.bleat = flag;
    }

    public int getAttacking() {
        return this.attacking;
    }

    public void setAttacking(int flag) {
        this.attacking = flag;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_GOAT_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        setBleating(true);
        if (getTypeMoC() == 1) {
            return MoCSoundEvents.ENTITY_GOAT_AMBIENT_BABY.get();
        }
        if (getTypeMoC() > 2 && getTypeMoC() < 5) {
            return MoCSoundEvents.ENTITY_GOAT_AMBIENT_FEMALE.get();
        }

        return MoCSoundEvents.ENTITY_GOAT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_GOAT_DEATH.get();
    }

    // TODO: Add unique step sound
    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.GOAT;
    }

    public int getMoCMaxAge() {
        return 50; //so the update is not handled on MoCEntityAnimal
    }

    @Override
    public float getSpeed() {
        return 0.15F;
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.945F;
    }
}
