/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelFirefly;
import drzhark.mocreatures.entity.ambient.MoCEntityFirefly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderFirefly extends MoCRenderInsect<MoCEntityFirefly, MoCModelFirefly<MoCEntityFirefly>> {

    public MoCRenderFirefly(EntityRendererProvider.Context renderManagerIn, MoCModelFirefly modelbase) {
        super(renderManagerIn, modelbase);
        this.addLayer(new LayerMoCFirefly(this));
    }

    @Override
    protected void scale(MoCEntityFirefly entityfirefly, PoseStack poseStack, float par2) {
        if (entityfirefly.getIsFlying()) {
            rotateFirefly(entityfirefly, poseStack);
        } else if (entityfirefly.climbing()) {
            rotateAnimal(entityfirefly, poseStack);
        }
    }

    protected void rotateFirefly(MoCEntityFirefly entityfirefly, PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(40F));
    }

    @Override
    public Identifier getTextureLocation(MoCEntityFirefly entityfirefly) {
        return entityfirefly.getTexture();
    }

    private class LayerMoCFirefly extends RenderLayer<MoCEntityFirefly, MoCModelFirefly<MoCEntityFirefly>> {
        private final MoCRenderFirefly mocRenderer;

        public LayerMoCFirefly(MoCRenderFirefly renderer) {
            super(renderer);
            this.mocRenderer = renderer;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, MoCEntityFirefly entity, 
                           float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, 
                           float netHeadYaw, float headPitch) {
            this.setTailBrightness(poseStack, entity, partialTicks);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderTypes.entityCutoutNoCull(
                    MoCreatures.proxy.getModelTexture("firefly_glow.png")));
            
            // Use the parent model directly - it will have already been set up with the right properties
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLightIn, 
                    OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        protected void setTailBrightness(PoseStack poseStack, MoCEntityFirefly entityliving, float partialTicks) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
