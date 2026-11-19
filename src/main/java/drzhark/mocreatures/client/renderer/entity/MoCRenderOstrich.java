/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelOstrich;
import drzhark.mocreatures.entity.neutral.MoCEntityOstrich;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderOstrich extends MoCRenderMoC<MoCEntityOstrich, MoCModelOstrich<MoCEntityOstrich>> {

    public MoCRenderOstrich(EntityRendererProvider.Context renderManagerIn, MoCModelOstrich modelbase, float f) {
        super(renderManagerIn, modelbase, 0.5F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityOstrich entityostrich) {
        return entityostrich.getTexture();
    }

    protected void adjustHeight(MoCEntityOstrich entityliving, float FHeight, PoseStack poseStack) {
        poseStack.translate(0.0F, FHeight, 0.0F);
    }

    @Override
    protected void scale(MoCEntityOstrich entityliving, PoseStack poseStack, float f) {
        MoCEntityOstrich entityostrich = entityliving;
        if (entityostrich.getTypeMoC() == 1) {
            stretch(entityostrich, poseStack);
        }

        super.scale(entityliving, poseStack, f);

    }

    protected void stretch(MoCEntityOstrich entityostrich, PoseStack poseStack) {

        float f = entityostrich.getMoCAge() * 0.01F;
        poseStack.scale(f, f, f);
    }
}
