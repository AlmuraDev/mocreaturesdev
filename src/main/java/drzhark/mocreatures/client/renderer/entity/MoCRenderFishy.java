/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelFishy;
import drzhark.mocreatures.entity.aquatic.MoCEntityFishy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class MoCRenderFishy extends MoCLivingRenderer<MoCEntityFishy, MoCModelFishy<MoCEntityFishy>> {

    public MoCRenderFishy(EntityRendererProvider.Context renderManagerIn, MoCModelFishy modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }


    @Override
    public void extractRenderState(MoCEntityFishy entityfishy, MoCRenderState state, float partialTicks) {
        if (entityfishy.getTypeMoC() == 0) {
            entityfishy.selectType();
        }
        super.extractRenderState(entityfishy, state, partialTicks);
    }

    @Override
    protected void scale(MoCEntityFishy entityfishy, PoseStack poseStack, float f) {
        stretch(entityfishy, poseStack);
        poseStack.translate(0.0F, 0.3F, 0.0F);
    }

    protected void stretch(MoCEntityFishy entityfishy, PoseStack poseStack) {
        poseStack.scale(entityfishy.getMoCAge() * 0.01F, entityfishy.getMoCAge() * 0.01F, entityfishy.getMoCAge() * 0.01F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityFishy entityfishy) {
        return entityfishy.getTexture();
    }
}
