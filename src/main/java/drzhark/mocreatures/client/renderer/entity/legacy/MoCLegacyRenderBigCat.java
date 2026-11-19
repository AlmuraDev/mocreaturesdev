/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity.legacy;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import drzhark.mocreatures.client.renderer.entity.MoCLivingRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat1;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat2;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.hunter.MoCEntityLion;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCLegacyRenderBigCat extends MoCLivingRenderer<MoCEntityBigCat, MoCLegacyModelBigCat2<MoCEntityBigCat>> {

    private static final Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");
    public MoCLegacyModelBigCat2 bigcat1;

    public MoCLegacyRenderBigCat(EntityRendererProvider.Context renderManagerIn, MoCLegacyModelBigCat2 modelbigcat2, MoCLegacyModelBigCat1 modelbigcat1, float f) {
        super(renderManagerIn, modelbigcat2, f);
        this.addLayer(new LayerMoCBigCat(this, modelbigcat1));
        this.bigcat1 = modelbigcat2;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityBigCat entitybigcat) {
        return entitybigcat.getTexture();
    }

    @Override
    public void render(MoCEntityBigCat entitybigcat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        super.render(entitybigcat, entityYaw, partialTicks, poseStack, buffer, packedLightIn);
        boolean displayName = MoCreatures.proxy.getDisplayPetName() && !(entitybigcat.getPetName()).isEmpty();
        boolean displayHealth = MoCreatures.proxy.getDisplayPetHealth();

        if (entitybigcat.getIsTamed()) {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f5 = entitybigcat.distanceTo(this.entityRenderDispatcher.camera.getEntity());
            if (f5 < 16F) {
                String s = entitybigcat.getPetName();
                float f7 = 0.1F;
                Font font = this.getFont();
                poseStack.pushPose();
                poseStack.translate(0.0F, f7, 0.0F);
                poseStack.rotate(Axis.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot()));
                poseStack.scale(-f3, -f3, f3);

                byte byte0 = -60;
                if (displayHealth) {
                    if (!displayName) {
                        byte0 += 8;
                    }
                    
                    // Render health bar
                    Matrix4f matrix4f = poseStack.last().pose();
                    float f8 = entitybigcat.getHealth();
                    float f9 = entitybigcat.getMaxHealth();
                    float f10 = f8 / f9;
                    float f11 = 40F * f10;
                    
                    // Red background
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix4f, -20F + f11, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F + f11, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    
                    // Green health
                    vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix4f, -20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f11 - 20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f11 - 20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                }
                
                if (displayName) {
                    // Render pet name
                    int textWidth = font.width(s);
                    float textX = -textWidth / 2.0f;
                    float textY = byte0;
                    
                    // Simplify - just draw a translucent quad with gui render type
                    Matrix4f matrix4f = poseStack.last().pose();
                    
                    // Use gui render type instead of textBackground
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.gui());
                    int left = (int)(textX - 1);
                    int top = (int)(textY - 1);
                    int right = (int)(textX + textWidth + 1);
                    int bottom = (int)(textY + 8);
                    
                    // Draw quad with minimal attributes
                    vertexconsumer.vertex(matrix4f, left, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, left, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    
                    // Render text
                    font.drawInBatch(s, textX, textY, 0x20ffffff, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, 0, packedLightIn);
                    font.drawInBatch(s, textX, textY, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLightIn);
                }

                poseStack.popPose();
            }
        }
    }

    @Override
    protected void scale(MoCEntityBigCat entitybigcat, PoseStack poseStack, float f) {
        this.bigcat1.sitting = entitybigcat.getIsSitting();
        this.bigcat1.tamed = entitybigcat.getIsTamed();
        stretch(entitybigcat, poseStack);
    }

    protected void stretch(MoCEntityBigCat entitybigcat, PoseStack poseStack) {
        float f = entitybigcat.getMoCAge() * 0.01F;
        if (entitybigcat.getIsAdult()) {
            f = 1.0F;
        }
        poseStack.scale(f, f, f);
    }

    // Render mane
    private class LayerMoCBigCat extends RenderLayer<MoCEntityBigCat, MoCLegacyModelBigCat2<MoCEntityBigCat>> {

        private final MoCLegacyRenderBigCat mocRenderer;
        private final MoCLegacyModelBigCat1<MoCEntityBigCat> mocModel;

        public LayerMoCBigCat(MoCLegacyRenderBigCat render, MoCLegacyModelBigCat1<MoCEntityBigCat> maneModel) {
            super(render);
            this.mocRenderer = render;
            this.mocModel = maneModel;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, MoCEntityBigCat entitybigcat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            Identifier resourcelocation;
            if (entitybigcat instanceof MoCEntityLion && entitybigcat.hasMane()) {
                if (entitybigcat.getTypeMoC() == 7) {
                    resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_white_lion_legacy_layer.png");
                } else {
                    resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_male.png");
                }
            } else {
                resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_female.png");
            }
            this.mocModel.copyPoseFrom(this.getParentModel());
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.entityCutoutNoCull(resourcelocation));
            this.mocModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
