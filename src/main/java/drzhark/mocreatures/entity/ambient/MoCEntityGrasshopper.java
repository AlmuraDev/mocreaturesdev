/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
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

public class MoCEntityGrasshopper extends MoCEntityInsect {

    private int jumpCounter;
    private int soundCounter;

    public MoCEntityGrasshopper(EntityType<? extends MoCEntityGrasshopper> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityInsect.createAttributes().add(Attributes.ARMOR, 1.0D).add(Attributes.FLYING_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            int i = this.random.nextInt(100);
            if (i <= 50) {
                setTypeMoC(1);
            } else {
                setTypeMoC(2);
            }
        }
    }

    @Override
    public Identifier getTexture() {
        if (getTypeMoC() == 1) {
            return MoCreatures.proxy.getModelTexture("grasshopper_bright_green.png");
        } else {
            return MoCreatures.proxy.getModelTexture("grasshopper_olive_green.png");
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (!this.level().isClientSide()) {
            if (getIsFlying() || !this.onGround()) {
                Player ep = this.level().getNearestPlayer(this, 5D);
                if (ep != null && --this.soundCounter == -1) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GRASSHOPPER_FLY.get());
                    this.soundCounter = 10;
                }
            }

            if (this.jumpCounter > 0 && ++this.jumpCounter > 30) {
                this.jumpCounter = 0;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (onGround() && ((getDeltaMovement().x > 0.05D) || (getDeltaMovement().z > 0.05D) || (getDeltaMovement().x < -0.05D) || (getDeltaMovement().z < -0.05D)))
                if (this.jumpCounter == 0) {
                    this.setDeltaMovement(this.getDeltaMovement().x * 5D, 0.45D, this.getDeltaMovement().z * 5D);
                    this.jumpCounter = 1;
                }
            }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (level().isBrightOutside()) {
            // TODO: Add grasshopper daytime ambient sound
            return level().getRandom().nextDouble() <= 0.1D ? MoCSoundEvents.ENTITY_GRASSHOPPER_CHIRP.get() : null;
        } else {
            return level().getRandom().nextDouble() <= 0.1D ? MoCSoundEvents.ENTITY_GRASSHOPPER_CHIRP.get() : null;
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
        return MoCLootTables.GRASSHOPPER;
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
        return 0.15F;
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.15F;
    }
    
    @Override
    public boolean shouldRenderTransparent() {
        return true; // Grasshoppers have transparent wings when flying
    }
}
