/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.MoCEntityAmbient;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityAnt extends MoCEntityAmbient {

    private static final EntityDataAccessor<Boolean> FOUND_FOOD = SynchedEntityData.defineId(MoCEntityAnt.class, EntityDataSerializers.BOOLEAN);

    public MoCEntityAnt(EntityType<? extends MoCEntityAnt> type, Level world) {
        super(type, world);
        //setSize(0.3F, 0.2F);
        this.texture = "ant.png";
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIWanderMoC2(this, 1.2D));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FOUND_FOOD, Boolean.FALSE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityAmbient.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.ARMOR, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D);
    }

    public boolean getHasFood() {
        return this.entityData.get(FOUND_FOOD);
    }

    public void setHasFood(boolean flag) {
        this.entityData.set(FOUND_FOOD, flag);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (!this.level().isClientSide()) {
            if (!getHasFood()) {
                ItemEntity entityitem = MoCTools.getClosestFood(this, 8D);
                if (entityitem == null || entityitem.isRemoved()) {
                    return;
                }
                if (entityitem.getVehicle() == null) {
                    float f = entityitem.distanceTo(this);
                    if (f > 1.0F) {
                        int i = Mth.floor(entityitem.getX());
                        int j = Mth.floor(entityitem.getY());
                        int k = Mth.floor(entityitem.getZ());
                        faceLocation(i, j, k, 30F);

                        getMyOwnPath(entityitem, f);
                        return;
                    }
                    if (f < 1.0F) {
                        exchangeItem(entityitem);
                        setHasFood(true);
                        return;
                    }
                }
            }
        }

        if (getHasFood()) {
            if (!this.isPassenger()) {
                ItemEntity entityitem = MoCTools.getClosestFood(this, 2D);
                if (entityitem != null && entityitem.getVehicle() == null) {
                    entityitem.startRiding(this);
                    return;
                }

                if (!this.isPassenger()) {
                    setHasFood(false);
                }
            }
        }
    }

    private void exchangeItem(ItemEntity entityitem) {
        ItemEntity cargo = new ItemEntity(this.level(), this.getX(), this.getY() + 0.2D, this.getZ(), entityitem.getItem());
        entityitem.discard();
        if (!this.level().isClientSide()) {
            this.level().addFreshEntity(cargo);
        }
    }

    @Override
    public boolean isMyFavoriteFood(ItemStack stack) {
        return !stack.isEmpty() && MoCTools.isItemEdible(stack.getItem());
    }

    @Override
    public float getSpeed() {
        if (getHasFood()) {
            return 0.1F;
        }
        return 0.15F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.ANT;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (0.1F));
    }
}
