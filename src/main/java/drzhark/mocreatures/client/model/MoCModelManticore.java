/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import drzhark.mocreatures.entity.hostile.MoCEntityManticore;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelManticore<T extends MoCEntityManticore> extends MoCModelAbstractBigCat<T> {

    public MoCModelManticore(ModelPart root) {
        super(root);
    }

    /**
     * In 1.20.1 we override prepareMobModel() to do exactly what setLivingAnimations did in 1.16.5.
     */
    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);

        // Copy over all of your flag-setting logic:
        this.isFlyer           = entityIn.isFlyer();
        this.isSaddled         = entityIn.getIsRideable();
        this.flapwings         = true;
        this.floating          = this.isFlyer && entityIn.isOnAir() && !entityIn.onGround();
        this.poisoning         = entityIn.swingingTail();
        this.isRidden          = entityIn.isPassenger();
        this.hasMane           = true;
        this.hasSaberTeeth     = true;
        this.onAir             = entityIn.isOnAir();
        this.hasStinger        = true;
        this.isMovingVertically = (entityIn.getDeltaMovement().y != 0) && !entityIn.onGround();
        this.hasChest          = false;
        this.isTamed           = false;
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        // No additional per-limb rotations here, unless you want to tweak Manticore’s pose further.
    }
}
