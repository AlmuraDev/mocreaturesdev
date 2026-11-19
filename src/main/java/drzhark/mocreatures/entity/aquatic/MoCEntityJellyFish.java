/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAquatic;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class MoCEntityJellyFish extends MoCEntityTameableAquatic {

    private static final EntityDataAccessor<Boolean> GLOWS = SynchedEntityData.defineId(MoCEntityJellyFish.class, EntityDataSerializers.BOOLEAN);
    private int poisoncounter;

    public MoCEntityJellyFish(EntityType<? extends MoCEntityJellyFish> type, Level world) {
        super(type, world);
        //setSize(0.45F, 0.575F);
        // TODO: Make hitboxes adjust depending on size
        //setAge(50 + (this.random.nextInt(50)));
        setMoCAge(100);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 0.15D, 120));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAquatic.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 6.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.15D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(5) + 1);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GLOWS, Boolean.FALSE);
    }

    public boolean isGlowing() {
        return (this.entityData.get(GLOWS));
    }

    public void setGlowing(boolean flag) {
        this.entityData.set(GLOWS, flag);
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("jellyfish_purple_gray.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("jellyfish_blue_dark.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("jellyfish_green.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("jellyfish_orange_red.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("jellyfish_orange_yellow.png");
            case 7:
                return MoCreatures.proxy.getModelTexture("jellyfish_blue_speckled.png");
            case 8:
                return MoCreatures.proxy.getModelTexture("jellyfish_white.png");
            case 9:
                return MoCreatures.proxy.getModelTexture("jellyfish_purple.png");
            case 10:
                return MoCreatures.proxy.getModelTexture("jellyfish_orange_light.png");
            case 11:
                return MoCreatures.proxy.getModelTexture("jellyfish_red.png");
            case 12:
                return MoCreatures.proxy.getModelTexture("jellyfish_blue_light.png");
            default:
                return MoCreatures.proxy.getModelTexture("jellyfish_orange_dark.png");
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (this.random.nextInt(200) == 0) {
                setGlowing(!this.level().isBrightOutside());
            }

            if (!getIsTamed() && ++this.poisoncounter > 250 && (this.shouldAttackPlayers()) && this.random.nextInt(30) == 0) {
                if (MoCTools.findNearPlayerAndPoison(this, true)) {
                    this.poisoncounter = 0;
                }
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_ATTACK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_ATTACK;
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.JELLYFISH;
    }

    @Override
    public float pitchRotationOffset() {
        if (!this.isInWater()) {
            return 90F;
        }
        return 0F;
    }

    @Override
    public int nameYOffset() {
        return (int) (getMoCAge() * -1 / 2.3);
    }

    @Override
    public float getSizeFactor() {
        float myMoveSpeed = MoCTools.getMyMovementSpeed(this);
        float pulseSpeed = 0.08F;
        if (myMoveSpeed > 0F)
            pulseSpeed = 0.5F;
        float pulseSize = Mth.cos(this.tickCount * pulseSpeed) * 0.2F;
        return getMoCAge() * 0.01F + (pulseSize / 5);
    }

    @Override
    protected boolean canBeTrappedInNet() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.85F;
    }
    
    @Override
    public boolean shouldRenderTransparent() {
        return true; // Jellyfish is always transparent
    }
}
