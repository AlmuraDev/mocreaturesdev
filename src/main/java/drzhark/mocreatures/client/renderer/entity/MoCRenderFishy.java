/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelFishy;
import drzhark.mocreatures.entity.aquatic.MoCEntityFishy;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderFishy extends MoCLivingRenderer<MoCEntityFishy, MoCModelFishy<MoCEntityFishy>> {

    public MoCRenderFishy(EntityRendererProvider.Context renderManagerIn, MoCModelFishy modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    public void render(MoCEntityFishy entityfishy, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        if (entityfishy.getTypeMoC() == 0) { // && !MoCreatures.mc.isMultiplayerWorld())
            entityfishy.selectType();
        }
        super.render(entityfishy, entityYaw, partialTicks, poseStack, buffer, packedLightIn);
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
