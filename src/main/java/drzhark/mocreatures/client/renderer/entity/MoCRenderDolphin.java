/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelDolphin;
import drzhark.mocreatures.entity.aquatic.MoCEntityDolphin;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderDolphin extends MoCLivingRenderer<MoCEntityDolphin, MoCModelDolphin<MoCEntityDolphin>> {

    public MoCRenderDolphin(EntityRendererProvider.Context renderManagerIn, MoCModelDolphin modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @SuppressWarnings("removal")
    @Override
    public void render(MoCEntityDolphin entitydolphin, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        super.render(entitydolphin, entityYaw, partialTicks, poseStack, buffer, packedLightIn);
        boolean flag = MoCreatures.proxy.getDisplayPetName() && !(entitydolphin.getPetName().isEmpty());
        boolean flag1 = MoCreatures.proxy.getDisplayPetHealth();
        if (entitydolphin.getIsTamed()) {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f4 = entitydolphin.distanceTo(this.entityRenderDispatcher.camera.getEntity());
            if (f4 < 16F) {
                String s = entitydolphin.getPetName();
                float f5 = 0.1F;
                Font font = this.getFont();
                
                poseStack.pushPose();
                poseStack.translate(0.0F, f5, 0.0F);
                poseStack.rotate(Axis.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot()));
                poseStack.scale(-f3, -f3, f3);

                byte byte0 = -50;
                if (flag1) {
                    if (!flag) {
                        byte0 += 8;
                    }
                    
                    // Render health bar
                    Matrix4f matrix4f = poseStack.last().pose();
                    float f6 = entitydolphin.getHealth();
                    float f7 = entitydolphin.getMaxHealth();
                    float f8 = f6 / f7;
                    float f9 = 40F * f8;
                    
                    // Red background
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(Identifier.parse("textures/misc/white.png")));
                    vertexconsumer.vertex(matrix4f, -20F + f9, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F + f9, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    
                    // Green health
                    vertexconsumer = buffer.getBuffer(RenderTypes.text(Identifier.parse("textures/misc/white.png")));
                    vertexconsumer.vertex(matrix4f, -20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f9 - 20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f9 - 20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                }
                
                if (flag) {
                    // Render pet name
                    int textWidth = font.width(s);
                    float textX = -textWidth / 2.0f;
                    float textY = byte0;
                    
                    // Name background
                    Matrix4f matrix4f = poseStack.last().pose();
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
    protected void scale(MoCEntityDolphin entitydolphin, PoseStack poseStack, float partialTickTime) {
        stretch(entitydolphin, poseStack);
    }

    protected void stretch(MoCEntityDolphin entitydolphin, PoseStack poseStack) {
        poseStack.scale(entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityDolphin entitydolphin) {
        return entitydolphin.getTexture();
    }
}
