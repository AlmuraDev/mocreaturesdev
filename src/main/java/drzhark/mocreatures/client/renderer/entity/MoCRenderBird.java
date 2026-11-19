/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelBird;
import drzhark.mocreatures.entity.passive.MoCEntityBird;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MoCRenderBird extends MoCRenderMoC<MoCEntityBird, MoCModelBird<MoCEntityBird>> {

    public MoCRenderBird(EntityRendererProvider.Context renderManagerIn, MoCModelBird modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityBird par1Entity) {
        return par1Entity.getTexture();
    }

    @Override
    protected float getBob(MoCEntityBird entitybird, float partialTicks) {
        float f1 = entitybird.winge + ((entitybird.wingb - entitybird.winge) * partialTicks);
        float f2 = entitybird.wingd + ((entitybird.wingc - entitybird.wingd) * partialTicks);
        return (Mth.sin(f1) + 1.0F) * f2;
    }

    @Override
    protected void scale(MoCEntityBird entitybird, PoseStack poseStack, float f) {
        if (!entitybird.level().isClientSide() && (entitybird.getVehicle() != null)) {
            poseStack.translate(0.0F, 1.3F, 0.0F);
        }
    }
}
