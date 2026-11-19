/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelCricket;
import drzhark.mocreatures.entity.ambient.MoCEntityCricket;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderCricket extends MoCRenderMoC<MoCEntityCricket, MoCModelCricket<MoCEntityCricket>> {

    public MoCRenderCricket(EntityRendererProvider.Context renderManagerIn, MoCModelCricket modelbase) {
        super(renderManagerIn, modelbase, 0.0F);
    }

    @Override
    protected void scale(MoCEntityCricket entitycricket, PoseStack poseStack, float par2) {
        rotateCricket(entitycricket, poseStack);
    }

    protected void rotateCricket(MoCEntityCricket entitycricket, PoseStack poseStack) {
        if (!entitycricket.onGround()) {
            if (entitycricket.getDeltaMovement().y > 0.5D) {
                poseStack.rotate(Axis.XN.rotationDegrees(35F));
            } else if (entitycricket.getDeltaMovement().y < -0.5D) {
                poseStack.rotate(Axis.XN.rotationDegrees(-35F));
            } else {
                poseStack.rotate(Axis.XN.rotationDegrees((float) (entitycricket.getDeltaMovement().y * 70D)));
            }
        }
    }

    @Override
    public Identifier getTextureLocation(MoCEntityCricket par1Entity) {
        return par1Entity.getTexture();
    }
}
