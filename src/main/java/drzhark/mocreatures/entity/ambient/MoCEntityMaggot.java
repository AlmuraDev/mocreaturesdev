/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.entity.MoCEntityAmbient;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityMaggot extends MoCEntityAmbient {

    public MoCEntityMaggot(EntityType<? extends MoCEntityMaggot> type, Level world) {
        super(type, world);
        //setSize(0.2F, 0.2F);
        this.texture = "maggot.png";
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAIWanderMoC2(this, 0.8D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityAmbient.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D);
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
        return MoCLootTables.MAGGOT;
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
        // Override to prevent jumping
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.45F;
    }
}
