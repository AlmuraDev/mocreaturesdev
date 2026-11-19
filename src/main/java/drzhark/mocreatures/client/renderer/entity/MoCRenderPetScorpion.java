/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelPetScorpion;
import drzhark.mocreatures.entity.hunter.MoCEntityPetScorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;

public class MoCRenderPetScorpion extends MoCRenderMoC<MoCEntityPetScorpion, MoCModelPetScorpion<MoCEntityPetScorpion>> {

    public MoCRenderPetScorpion(EntityRendererProvider.Context renderManagerIn, MoCModelPetScorpion modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    protected float getFlipDegrees(MoCEntityPetScorpion entityscorpion) {
        return 180.0F;
    }

    @Override
    protected void scale(MoCEntityPetScorpion entityscorpion, PoseStack poseStack, float f) {
        /* TODO: Fix rider rotation
        if (entityscorpion.onClimbable()) {
            rotateAnimal(entityscorpion);
        }
        */

        if (entityscorpion.getIsSitting()) {
            float factorY = 0.4F * (entityscorpion.getMoCAge() / 100.0F);
            poseStack.translate(0.0F, factorY, 0.0F);
        }

        if (!entityscorpion.getIsAdult()) {
            stretch(entityscorpion, poseStack);
            if (entityscorpion.getVehicle() != null) {
                upsideDown(entityscorpion, poseStack);
            }
        } else {
            adjustHeight(entityscorpion, poseStack);
        }
    }

    protected void upsideDown(MoCEntityPetScorpion entityscorpion, PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(-90.0F));
        poseStack.translate(-1.5F, -0.5F, -2.5F);
    }

    protected void adjustHeight(MoCEntityPetScorpion entityscorpion, PoseStack poseStack) {
        poseStack.translate(0.0F, -0.1F, 0.0F);
    }

    protected void rotateAnimal(MoCEntityPetScorpion entityscorpion, PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(90.0F));
        poseStack.translate(0.0F, 1.0F, 0.0F);
    }

    protected void stretch(MoCEntityPetScorpion entityscorpion, PoseStack poseStack) {

        float f = 1.1F;
        if (!entityscorpion.getIsAdult()) {
            f = entityscorpion.getMoCAge() * 0.01F;
        }
        poseStack.scale(f, f, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityPetScorpion entityscorpion) {
        return entityscorpion.getTexture();
    }
}
