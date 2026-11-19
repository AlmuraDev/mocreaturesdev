/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCItems;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Random;

public class MoCEntityWerewolf extends MoCEntityMob {

    private static final EntityDataAccessor<Boolean> IS_HUMAN = SynchedEntityData.defineId(MoCEntityWerewolf.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HUNCHED = SynchedEntityData.defineId(MoCEntityWerewolf.class, EntityDataSerializers.BOOLEAN);
    private boolean transforming;
    private int tcounter;
    private int textCounter;
    private boolean isImmuneToFire;

    public MoCEntityWerewolf(EntityType<? extends MoCEntityWerewolf> type, Level world) {
        super(type, world);
        // TODO: Change hitbox depending on form
        //setSize(0.7F, 2.0F);
        this.transforming = false;
        this.tcounter = 0;
        setHumanForm(true);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.5D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HUMAN, Boolean.FALSE);
        builder.define(IS_HUNCHED, Boolean.FALSE);
    }

    @Override
    public void setHealth(float par1) {
        if (this.getIsHumanForm() && par1 > 15F) {
            par1 = 15F;
        }
        super.setHealth(par1);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int k = this.random.nextInt(100);
            if (k <= 28) {
                setTypeMoC(1);
            } else if (k <= 56) {
                setTypeMoC(2);
            } else if (k <= 85) {
                setTypeMoC(3);
            } else {
                setTypeMoC(4);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        if (this.getIsHumanForm()) {
            return MoCreatures.proxy.getModelTexture("wereblank.png");
        }

        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("werewolf_black.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("werewolf_white.png");
            case 4:
                if (!MoCreatures.proxy.getAnimateTextures()) {
                    return MoCreatures.proxy.getModelTexture("werewolf_fire1.png");
                }
                this.textCounter++;
                if (this.textCounter < 10) {
                    this.textCounter = 10;
                }
                if (this.textCounter > 39) {
                    this.textCounter = 10;
                }
                String NTA = "werewolf_fire";
                String NTB = String.valueOf(this.textCounter);
                NTB = NTB.substring(0, 1);
                String NTC = ".png";

                return MoCreatures.proxy.getModelTexture(NTA + NTB + NTC);
            default:
                return MoCreatures.proxy.getModelTexture("werewolf_brown.png");
        }
    }

    public boolean getIsHumanForm() {
        return this.entityData.get(IS_HUMAN);
    }

    public void setHumanForm(boolean flag) {
        this.entityData.set(IS_HUMAN, flag);
    }

    public boolean getIsHunched() {
        return this.entityData.get(IS_HUNCHED);
    }

    public void setHunched(boolean flag) {
        this.entityData.set(IS_HUNCHED, flag);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        if (getIsHumanForm()) {
            setTarget(null);
            return false;
        }
        if (this.getTypeMoC() == 4 && entityIn instanceof LivingEntity) {
            entityIn.igniteForSeconds(10);
        }
        return super.doHurtTarget(serverLevel, serverLevel, entityIn);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if (!getIsHumanForm() && (entity instanceof Player)) {
            Player entityplayer = (Player) entity;
            ItemStack stack = entityplayer.getMainHandItem();
            if (!stack.isEmpty()) {
                if (stack.getItem() == MoCItems.SILVERSWORD.get()) {
                    i = 10F;
                } else if (stack.getItem() instanceof SwordItem) {
                    String swordMaterial = ((SwordItem) stack.getItem()).getTier().toString();
                    String swordName = stack.getItem().getDescriptionId();
                    if (swordMaterial.toLowerCase().contains("silver") || swordName.toLowerCase().contains("silver")) {
                        i = ((SwordItem) stack.getItem()).getDamage() + 6F;
                    } else {
                        i = ((SwordItem) stack.getItem()).getDamage() + 2F;
                    }
                } else if (stack.getItem() instanceof TieredItem) {
                    String swordMaterial = ((TieredItem) stack.getItem()).getTier().toString();
                    String swordName = stack.getItem().getDescriptionId();
                    if (swordMaterial.toLowerCase().contains("silver") || swordName.toLowerCase().contains("silver")) {
                        i = ((TieredItem) stack.getItem()).getTier().getAttackDamageBonus() * 3F;
                    } else {
                        i = ((TieredItem) stack.getItem()).getTier().getAttackDamageBonus() * 0.5F;
                    }
                } else if (stack.getItem().getDescriptionId().toLowerCase().contains("silver")) {
                    i = 6F;
                } else {
                    i = Math.min(i * 0.5F, 4F);
                }
            }
        }
        return super.hurtServer(serverLevel, damagesource, i);
    }

    @Override
    public boolean shouldAttackPlayers() {
        return !getIsHumanForm() && super.shouldAttackPlayers();
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (getIsHumanForm()) {
            return MoCreatures.proxy.legacyWerehumanSounds ? MoCSoundEvents.ENTITY_WEREWOLF_DEATH_HUMAN.get() : SoundEvents.GENERIC_HURT;
        } else {
            return MoCSoundEvents.ENTITY_WEREWOLF_DEATH.get();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (getIsHumanForm()) {
            if (!this.transforming)
                return MoCreatures.proxy.legacyWerehumanSounds ? MoCSoundEvents.ENTITY_WEREWOLF_HURT_HUMAN.get() : SoundEvents.GENERIC_HURT;
            return null;
        } else {
            return MoCSoundEvents.ENTITY_WEREWOLF_HURT.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (getIsHumanForm()) {
            return null;
        } else {
            return MoCSoundEvents.ENTITY_WEREWOLF_AMBIENT.get();
        }
    }

    @Override
    protected Identifier getDefaultLootTable() {
        if (getIsHumanForm()) {
            return MoCLootTables.WEREHUMAN;
        }
        return MoCLootTables.WEREWOLF;
    }

    public boolean IsNight() {
        return !this.level().isBrightOutside();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (((IsNight() && getIsHumanForm()) || (!IsNight() && !getIsHumanForm())) && (this.random.nextInt(250) == 0)) {
                this.transforming = true;
            }
            if (getIsHumanForm() && (this.getTarget() != null)) {
                setTarget(null);
            }
            if (this.getTarget() != null && !getIsHumanForm()) {
                boolean hunch = (this.distanceToSqr(this.getTarget()) > 12D);
                setHunched(hunch);
            }

            if (this.transforming && (this.random.nextInt(3) == 0)) {
                this.tcounter++;
                if ((this.tcounter % 2) == 0) {
                    this.setPos(this.getX() + 0.3D, this.getY() + (double) this.tcounter / 30, this.getZ());
                    hurtOrSimulate(this.damageSources().mobAttack(this), 1);
                }
                if ((this.tcounter % 2) != 0) {
                    this.setPos(this.getX() - 0.3D, this.getY(), this.getZ());
                }
                if (this.tcounter == 10) {
                    MoCTools.playCustomSound(this, MoCreatures.proxy.legacyWerehumanSounds ? MoCSoundEvents.ENTITY_WEREWOLF_TRANSFORM_HUMAN.get() : MoCSoundEvents.ENTITY_WEREWOLF_TRANSFORM.get());
                }
                if (this.tcounter > 30) {
                    Transform();
                    this.tcounter = 0;
                    this.transforming = false;
                }
            }
            //so entity doesn't despawn that often
            if (this.random.nextInt(300) == 0) {
                this.noActionTime -= 100 * this.level().getDifficulty().getId();
                if (this.noActionTime < 0) {
                    this.noActionTime = 0;
                }
            }
        }
    }

    public static boolean getCanSpawnHere(EntityType<? extends MoCEntityMob> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        return MoCEntityMob.getCanSpawnHere(type, world, reason, pos, randomIn) && world.canSeeSky(new BlockPos(pos));
    }

    // PATCHED Transform method:
    private void Transform() {
        if (this.deathTime > 0) return;

        int i = Mth.floor(this.getX());
        int j = Mth.floor(getBoundingBox().minY) + 1;
        int k = Mth.floor(this.getZ());
        float f = 0.1F;
        for (int l = 0; l < 30; l++) {
            double d = i + this.level().getRandom().nextFloat();
            double d1 = j + this.level().getRandom().nextFloat();
            double d2 = k + this.level().getRandom().nextFloat();
            double d3 = d - i;
            double d4 = d1 - j;
            double d5 = d2 - k;
            double d6 = Mth.sqrt((float)((d3 * d3) + (d4 * d4) + (d5 * d5)));
            d3 /= d6;
            d4 /= d6;
            d5 /= d6;
            double d7 = 0.5D / ((d6 / f) + 0.1D);
            d7 *= (this.level().getRandom().nextFloat() * this.level().getRandom().nextFloat()) + 0.3F;
            d3 *= d7;
            d4 *= d7;
            d5 *= d7;
            this.level().addParticle(ParticleTypes.POOF, (d + (i * 1.0D)) / 2D, (d1 + (j * 1.0D)) / 2D, (d2 + (k * 1.0D)) / 2D, d3, d4, d5);
        }

        if (getIsHumanForm()) {
            setHumanForm(false);
            this.setHealth(40);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        } else {
            setHumanForm(true);
            this.setHealth(15);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        }

        this.refreshDimensions(); // forces size/hitbox update
        this.transforming = false;
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setHumanForm(nbttagcompound.getBooleanOr("HumanForm", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("HumanForm", getIsHumanForm());
    }

    @Override
    public float getSpeed() {
        if (getIsHumanForm()) {
            return 0.1F;
        }
        if (getIsHunched()) {
            return 0.35F;
        }
        return 0.2F;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        if (getIsHumanForm()) {
            return EntityDimensions.fixed(0.6F, 1.8F); // player size
        } else {
            return EntityDimensions.fixed(1.2F, 2.4F); // beast form
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return getIsHumanForm() ? 1.62F : 2.0F; // match model scale
    }

    @Override
    public void setTypeMoC(int i) {
        this.isImmuneToFire = i == 4;
        super.setTypeMoC(i);
    }

    @Override
    public boolean fireImmune() {
        return this.isImmuneToFire ? true : super.fireImmune();
    }
}
