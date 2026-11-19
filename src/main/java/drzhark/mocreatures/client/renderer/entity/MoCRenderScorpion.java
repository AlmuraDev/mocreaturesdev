/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelScorpion;
import drzhark.mocreatures.entity.hostile.MoCEntityScorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;

public class MoCRenderScorpion extends MoCRenderMoC<MoCEntityScorpion, MoCModelScorpion<MoCEntityScorpion>> {

    public MoCRenderScorpion(EntityRendererProvider.Context renderManagerIn, MoCModelScorpion modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    protected float getFlipDegrees(MoCEntityScorpion entityscorpion) {
        return 180.0F;
    }

    @Override
    protected void scale(MoCEntityScorpion entityscorpion, PoseStack poseStack, float f) {
        /* TODO: Fix rider rotation
        if (entityscorpion.onClimbable()) {
            rotateAnimal(entityscorpion);
        }
        */

        if (!entityscorpion.getIsAdult()) {
            stretch(entityscorpion, poseStack);
        } else {
            adjustHeight(entityscorpion, poseStack);
        }
    }

    protected void adjustHeight(MoCEntityScorpion entityscorpion, PoseStack poseStack) {
        poseStack.translate(0.0F, -0.1F, 0.0F);
    }

    protected void rotateAnimal(PoseStack poseStack, MoCEntityScorpion entityscorpion) {
        poseStack.rotate(Axis.XN.rotationDegrees(90.0F));
        poseStack.translate(0.0F, 1.0F, 0.0F);
    }

    protected void stretch(MoCEntityScorpion entityscorpion, PoseStack poseStack) {

        float f = 1.1F;
        if (!entityscorpion.getIsAdult()) {
            f = entityscorpion.getMoCAge() * 0.01F;
        }
        poseStack.scale(f, f, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityScorpion entityscorpion) {
        return entityscorpion.getTexture();
    }
}
