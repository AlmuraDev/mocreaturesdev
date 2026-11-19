/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelFilchLizard;
import drzhark.mocreatures.entity.passive.MoCEntityFilchLizard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;

// Courtesy of Daveyx0, permission given
public class MoCRenderFilchLizard extends MoCLivingRenderer<MoCEntityFilchLizard, MoCModelFilchLizard<MoCEntityFilchLizard>> {

    public MoCRenderFilchLizard(EntityRendererProvider.Context renderManagerIn, MoCModelFilchLizard modelBase, float f) {
        super(renderManagerIn, modelBase, f);
        this.addLayer(new LayerHeldItemCustom(this));
    }

    @Override
    public Identifier getTextureLocation(MoCEntityFilchLizard entity) {
        return entity.getTexture();
    }

    @Override
    public void extractRenderState(MoCEntityFilchLizard entity, MoCRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        this.itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
    }

    /** The item the lizard has stolen, held in its mouth. */
    private class LayerHeldItemCustom extends RenderLayer<MoCRenderState, MoCModelFilchLizard<MoCEntityFilchLizard>> {
        protected final MoCRenderFilchLizard livingEntityRenderer;

        public LayerHeldItemCustom(MoCRenderFilchLizard livingEntityRendererIn) {
            super(livingEntityRendererIn);
            this.livingEntityRenderer = livingEntityRendererIn;
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            if (state.heldItem.isEmpty()) {
                return;
            }
            poseStack.pushPose();
            if (state.isBaby) {
                poseStack.translate(0.0F, 0.625F, 0.0F);
                poseStack.rotate(Axis.XN.rotationDegrees(-20.0F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }
            if (state.entity != null && state.entity.isCrouching()) {
                poseStack.translate(0.0F, 0.2F, 0.0F);
            }
            poseStack.rotate(Axis.YP.rotationDegrees(90.0F));
            poseStack.rotate(Axis.ZP.rotationDegrees(180.0F));
            poseStack.rotate(Axis.ZP.rotationDegrees(20.0F));
            poseStack.translate(-0.55F, -1.0F, -0.05F);
            state.heldItem.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
