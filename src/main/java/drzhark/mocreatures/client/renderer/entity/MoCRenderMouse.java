/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import drzhark.mocreatures.client.model.MoCModelMouse;
import drzhark.mocreatures.entity.passive.MoCEntityMouse;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class MoCRenderMouse extends MoCRenderMoC<MoCEntityMouse, MoCModelMouse<MoCEntityMouse>> {

    public MoCRenderMouse(EntityRendererProvider.Context context, MoCModelMouse<MoCEntityMouse> modelbase, float f) {
        super(context, modelbase, f);
    }

    @Override
    protected void scale(MoCEntityMouse entitymouse, PoseStack poseStack, float partialTickTime) {
        stretch(poseStack);
        // When mice are picked up
        if (entitymouse.upsideDown()) {
            upsideDown(poseStack);
        }

        if (entitymouse.onClimbable()) {
            rotateAnimal(poseStack);
        }
    }

    protected void rotateAnimal(PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(90.0F));
        poseStack.translate(0.0F, 0.4F, 0.0F);
    }

    protected void stretch(PoseStack poseStack) {
        float f = 0.6F;
        poseStack.scale(f, f, f);
    }

    protected void upsideDown(PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(-90.0F));
        poseStack.translate(-0.55F, 0.0F, 0.0F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityMouse entitymouse) {
        return entitymouse.getTexture();
    }
}
