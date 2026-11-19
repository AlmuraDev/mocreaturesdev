/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.client.model.MoCModelGolem;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class MoCRenderGolem extends MoCRenderMoC<MoCEntityGolem, MoCModelGolem<MoCEntityGolem>> {

    public MoCRenderGolem(EntityRendererProvider.Context renderManagerIn, MoCModelGolem modelbase, float f) {
        super(renderManagerIn, modelbase, f);
        this.addLayer(new LayerMoCGolem(this));
    }

    @Override
    public Identifier getTextureLocation(MoCEntityGolem par1Entity) {
        return par1Entity.getTexture();
    }

    // Simple layer renderer that applies an energy swirl effect
    /** The energy swirl over a golem that has an effect texture. */
    private class LayerMoCGolem extends RenderLayer<MoCRenderState, MoCModelGolem<MoCEntityGolem>> {
        private final MoCRenderGolem mocRenderer;

        public LayerMoCGolem(MoCRenderGolem render) {
            super(render);
            this.mocRenderer = render;
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            MoCEntityGolem entity = (MoCEntityGolem) state.entity;
            Identifier effectTexture = entity == null ? null : entity.getEffectTexture();
            if (effectTexture != null) {
                float f = state.ageInTicks;
                this.mocRenderer.submitModelPass(poseStack, collector, state, RenderTypes.energySwirl(effectTexture, f * 0.01F, f * 0.01F), 0.5F, 0.5F, 0.5F, 1.0F);
            }
        }
    }
}
