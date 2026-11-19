/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelGoat;
import drzhark.mocreatures.entity.neutral.MoCEntityGoat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
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
public class MoCRenderGoat extends MoCLivingRenderer<MoCEntityGoat, MoCModelGoat<MoCEntityGoat>> {

    private static final Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");
    private final MoCModelGoat tempGoat;
    float depth = 0F;

    public MoCRenderGoat(EntityRendererProvider.Context renderManagerIn, MoCModelGoat modelbase, float f) {
        super(renderManagerIn, modelbase, f);
        this.tempGoat = modelbase;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityGoat entitygoat) {
        return entitygoat.getTexture();
    }

    @Override
    protected void scale(MoCEntityGoat entitygoat, PoseStack poseStack, float partialTick) {
        poseStack.translate(0.0F, this.depth, 0.0F);
        stretch(entitygoat, poseStack);
    }

    @Override
    public void render(MoCEntityGoat entitygoat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        this.tempGoat.typeInt = entitygoat.getTypeMoC();
        this.tempGoat.age = entitygoat.getMoCAge() * 0.01F;
        this.tempGoat.bleat = entitygoat.getBleating();
        this.tempGoat.attacking = entitygoat.getAttacking();
        this.tempGoat.legMov = entitygoat.legMovement();
        this.tempGoat.earMov = entitygoat.earMovement();
        this.tempGoat.tailMov = entitygoat.tailMovement();
        this.tempGoat.eatMov = entitygoat.mouthMovement();
        super.render(entitygoat, entityYaw, partialTicks, poseStack, buffer, packedLightIn);
        boolean displayName = MoCreatures.proxy.getDisplayPetName() && !(entitygoat.getPetName()).isEmpty();
        boolean displayHealth = MoCreatures.proxy.getDisplayPetHealth();
        if (entitygoat.getIsTamed()) {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f4 = entitygoat.distanceTo(this.entityRenderDispatcher.camera.getEntity());
            if (f4 < 16F) {
                String s = entitygoat.getPetName();
                float f5 = 0.1F;
                Font font = this.getFont();
                
                poseStack.pushPose();
                poseStack.translate(0.0F, f5, 0.0F);
                poseStack.rotate(Axis.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot()));
                poseStack.scale(-f3, -f3, f3);

                byte byte0 = (byte) (-15 + (-40 * entitygoat.getMoCAge() * 0.01F));
                if (displayHealth) {
                    if (!displayName) {
                        byte0 += 8;
                    }
                    
                    // Render health bar
                    Matrix4f matrix4f = poseStack.last().pose();
                    float f6 = entitygoat.getHealth();
                    float f7 = entitygoat.getMaxHealth();
                    float f8 = f6 / f7;
                    float f9 = 40F * f8;
                    
                    // Red background
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix4f, -20F + f9, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F + f9, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -6 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, 20F, -10 + byte0, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    
                    // Green health
                    vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix4f, -20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, -20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f9 - 20F, -6 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix4f, f9 - 20F, -10 + byte0, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLightIn).normal(0, 1, 0).endVertex();
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

    protected void stretch(MoCEntityGoat entitygoat, PoseStack poseStack) {
        float scale = entitygoat.getMoCAge() * 0.01F;
        poseStack.scale(scale, scale, scale);
    }
}
