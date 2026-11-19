/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelFilchLizard;
import drzhark.mocreatures.entity.passive.MoCEntityFilchLizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
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

    private class LayerHeldItemCustom extends RenderLayer<MoCEntityFilchLizard, MoCModelFilchLizard<MoCEntityFilchLizard>> {
        protected final MoCRenderFilchLizard livingEntityRenderer;

        public LayerHeldItemCustom(MoCRenderFilchLizard livingEntityRendererIn) {
            super(livingEntityRendererIn);
            this.livingEntityRenderer = livingEntityRendererIn;
        }

        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, MoCEntityFilchLizard entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemStack = entity.getMainHandItem();
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                if (this.livingEntityRenderer.getModel().young) {
                    poseStack.translate(0.0F, 0.625F, 0.0F);
                    poseStack.rotate(Axis.XN.rotationDegrees(-20.0F));
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                }
                if (!entity.getMainHandItem().isEmpty()) {
                    this.renderHeldItemLizard(poseStack, entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, buffer, packedLightIn);
                }
                poseStack.popPose();
            }
        }

        public void renderHeldItemLizard(PoseStack poseStack, LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, MultiBufferSource buffer, int packedLightIn) {
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                if (entity.isCrouching()) {
                    poseStack.translate(0.0F, 0.2F, 0.0F);
                }
                poseStack.rotate(Axis.YP.rotationDegrees(90.0F));
                poseStack.rotate(Axis.ZP.rotationDegrees(180.0F));
                poseStack.rotate(Axis.ZP.rotationDegrees(20.0F));
                poseStack.translate(-0.55F, -1.0F, -0.05F);
                ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
                renderer.renderItem(entity, itemStack, displayContext, false, poseStack, buffer, packedLightIn);
                poseStack.popPose();
            }
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
