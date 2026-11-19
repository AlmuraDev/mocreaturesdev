/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelBunny;
import drzhark.mocreatures.entity.passive.MoCEntityBunny;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;

public class MoCRenderBunny extends MoCRenderMoC<MoCEntityBunny, MoCModelBunny<MoCEntityBunny>> {

    public MoCRenderBunny(EntityRendererProvider.Context context, MoCModelBunny modelbase, float f) {
        super(context, modelbase, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityBunny entitybunny) {
        return entitybunny.getTexture();
    }

    @Override
    protected void scale(MoCEntityBunny entitybunny, PoseStack poseStack, float f) {
        if (!entitybunny.getIsAdult()) {
            stretch(entitybunny, poseStack);
        }
        rotBunny(entitybunny, poseStack);
        adjustOffsets(entitybunny.getAdjustedXOffset(), entitybunny.getAdjustedYOffset(), entitybunny.getAdjustedZOffset(), poseStack);
    }

    protected void rotBunny(MoCEntityBunny entitybunny, PoseStack poseStack) {
        if (!entitybunny.onGround() && (entitybunny.getVehicle() == null)) {
            if (entitybunny.getDeltaMovement().y > 0.5D) {
                poseStack.rotate(Axis.XN.rotationDegrees(35F));
            } else if (entitybunny.getDeltaMovement().y < -0.5D) {
                poseStack.rotate(Axis.XN.rotationDegrees(-35F));
            } else {
                poseStack.rotate(Axis.XN.rotationDegrees((float) (entitybunny.getDeltaMovement().y * 70D)));
            }
        }
    }

    protected void stretch(MoCEntityBunny entitybunny, PoseStack poseStack) {
        float f = entitybunny.getMoCAge() * 0.01F;
        poseStack.scale(f, f, f);
    }
}
