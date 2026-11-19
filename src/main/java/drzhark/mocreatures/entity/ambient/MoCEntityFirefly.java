/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.MoCEntityInsect;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityFirefly extends MoCEntityInsect {

    private int soundCount;

    public MoCEntityFirefly(EntityType<? extends MoCEntityFirefly> type, Level world) {
        super(type, world);
        this.texture = "firefly.png";
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityInsect.registerAttributes()
                .add(Attributes.ARMOR, 1.0D);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            Player player = this.level().getNearestPlayer(this, 5D);
            if (player != null && getIsFlying() && --this.soundCount == -1) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GRASSHOPPER_FLY.get());
                this.soundCount = 20;
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_GRASSHOPPER_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_GRASSHOPPER_HURT.get();
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FIREFLY;
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public float getSpeed() {
        if (getIsFlying()) {
            return 0.12F;
        }
        return 0.10F;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (0.15F));
    }
}
