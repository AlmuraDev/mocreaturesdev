/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAmbient;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntitySnail extends MoCEntityAmbient {

    private static final EntityDataAccessor<Boolean> IS_HIDING = SynchedEntityData.defineId(MoCEntitySnail.class, EntityDataSerializers.BOOLEAN);

    public MoCEntitySnail(EntityType<? extends MoCEntitySnail> type, Level world) {
        super(type, world);
        //setSize(0.4F, 0.3F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIWanderMoC2(this, 0.8D));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HIDING, Boolean.FALSE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityAmbient.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.10D);
    }

    @Override
    public boolean isMovementCeased() {
        return (getIsHiding());
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(6) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("snail_green.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("snail_yellow.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("snail_red.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("slug_golden.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("slug_black.png");
            default:
                return MoCreatures.proxy.getModelTexture("snail_brown.png");
        }
    }

    public boolean getIsHiding() {
        return this.entityData.get(IS_HIDING);
    }

    public void setIsHiding(boolean flag) {
        this.entityData.set(IS_HIDING, flag);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            LivingEntity entityliving = getBoogey(3D);
            if ((entityliving != null) && entityliving.getEyeHeight() > 0.5F && entityliving.getBbWidth() > 0.5F && hasLineOfSight(entityliving)) {
                if (!getIsHiding()) {
                    setIsHiding(true);
                }
                this.getNavigation().stop();
            } else {
                setIsHiding(false);
            }
            // Slugs won't hide
            if (getIsHiding() && this.getTypeMoC() > 4) {
                setIsHiding(false);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (getIsHiding()) {
            this.yBodyRotO = this.yBodyRot = this.getYRot();
            this.yRotO = this.getYRot();
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_STEP;
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.SNAIL;
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
        // Prevent jumping
    }
}
