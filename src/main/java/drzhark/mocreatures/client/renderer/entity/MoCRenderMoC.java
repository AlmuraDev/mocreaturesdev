/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import drzhark.mocreatures.client.model.MoCEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.neutral.MoCEntityWyvern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import net.minecraft.network.chat.Component;

public class MoCRenderMoC<T extends Mob, M extends MoCEntityModel<T>> extends MoCLivingRenderer<T, M> {

    private float prevPitch;
    private float prevRoll;
    private float prevYaw;

    public MoCRenderMoC(EntityRendererProvider.Context context, M model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        
        // Check if we need to handle transparency
        if (shouldHandleTransparency(entityIn)) {
            renderWithTransparency(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            renderMoC(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
    
    /**
     * Checks if the entity should be rendered with transparency handling
     */
    protected boolean shouldHandleTransparency(T entityIn) {
        if (!(entityIn instanceof IMoCEntity)) {
            return false;
        }
        
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        return entityMoC.getIsGhost() || hasVanishingEffect(entityIn) || entityMoC.shouldRenderTransparent();
    }
    
    /**
     * Checks if the entity has a vanishing effect
     */
    protected boolean hasVanishingEffect(T entityIn) {
        // Override in subclasses for entities with vanish counters
        return false;
    }
    
    /**
     * Renders the entity with transparency handling
     */
    protected void renderWithTransparency(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, (double) (-1.501F), 0.0D);

        // Apply scaling and transformations
        this.scale(entityIn, poseStack, partialTicks);

        // Calculate transparency
        float transparency = calculateTransparency(entityIn);
        
        // Choose appropriate render type
        RenderType renderType = transparency < 1.0F
                ? RenderTypes.entityTranslucent(getTextureLocation(entityIn))
                : RenderTypes.entityCutout(getTextureLocation(entityIn));

        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

        // Setup rotations and animations
        float bodyRot = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float headRot = Mth.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float headRotDelta = headRot - bodyRot;
        float pitch = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());

        setupRotations(entityIn, poseStack, entityIn.tickCount + partialTicks, bodyRot, partialTicks);
        model.prepareMobModel(entityIn, 0, 0, partialTicks);
        model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, headRotDelta, pitch);

        // Get color tint
        float[] color = getTransparencyColor(entityIn);

        // Render with transparency
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 
                color[0], color[1], color[2], transparency);

        poseStack.popPose();

        // Render name and health if needed
        renderNameAndHealth(entityIn, poseStack, buffer, packedLight);
    }
    
    /**
     * Calculates transparency value for the entity
     */
    protected float calculateTransparency(T entityIn) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        
        // Check for vanishing effect first
        if (hasVanishingEffect(entityIn)) {
            return getVanishingTransparency(entityIn);
        }
        
        // Check for ghost state
        if (entityMoC.getIsGhost()) {
            return getGhostTransparency(entityIn);
        }
        
        // Check for model transparency (partial/full model transparency)
        if (entityMoC.shouldRenderTransparent()) {
            return getModelTransparency(entityIn);
        }
        
        return 1.0F; // Fully opaque
    }
    
    /**
     * Gets transparency for vanishing effect
     */
    protected float getVanishingTransparency(T entityIn) {
        // Override in subclasses
        return 1.0F;
    }
    
    /**
     * Gets transparency for ghost state
     */
    protected float getGhostTransparency(T entityIn) {
        // Default ghost transparency - override in subclasses for custom behavior
        if (entityIn instanceof MoCEntityHorse) {
            return ((MoCEntityHorse) entityIn).tFloat();
        } else if (entityIn instanceof MoCEntityBigCat) {
            return ((MoCEntityBigCat) entityIn).tFloat();
        } else if (entityIn instanceof MoCEntityWyvern) {
            return ((MoCEntityWyvern) entityIn).tFloat();
        }
        
        // Default ghost transparency
        return 0.6F;
    }
    
    /**
     * Gets transparency for model-based transparency (partial/full model)
     */
    protected float getModelTransparency(T entityIn) {
        // Default model transparency - override in subclasses for custom behavior
        return 0.6F; // Default model transparency
    }
    
    /**
     * Gets the color tint for transparency
     */
    protected float[] getTransparencyColor(T entityIn) {
        // Default color (white)
        return new float[]{1.0F, 1.0F, 1.0F};
    }
    
    /**
     * Renders name and health display
     */
    protected void renderNameAndHealth(T entityIn, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        boolean showName = MoCreatures.proxy.getDisplayPetName() && !(entityMoC.getPetName().isEmpty()) && (entityIn.getPassengers().isEmpty());
        boolean showHealth = MoCreatures.proxy.getDisplayPetHealth() && (entityIn.getPassengers().isEmpty());
        
        if (entityMoC.getIsTamed() && (entityIn.getPassengers().isEmpty())) {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f5 = (float) this.entityRenderDispatcher.distanceToSqr(entityIn);
            
            if (f5 < 256F) {
                String s = "";
                s = s + entityMoC.getPetName();
                float f7 = 0.1F;
                
                poseStack.pushPose();
                poseStack.translate(0.0F, f7, 0.0F);
                poseStack.rotate(this.entityRenderDispatcher.cameraOrientation());
                poseStack.scale(-f3, -f3, f3);
                int yOff = entityMoC.nameYOffset();
                
                if (showHealth) {
                    if (!showName) {
                        yOff += 8;
                    }
                    
                    Matrix4f matrix = poseStack.last().pose();
                    
                    // Health bar background (red)
                    float health = entityIn.getHealth();
                    float maxHealth = entityIn.getMaxHealth();
                    float healthRatio = health / maxHealth;
                    float barWidth = 40F * healthRatio;
                    
                    // Use static white texture for health bars
                    Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");
                    
                    // Red background (empty health)
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix, -20F + barWidth, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, -20F + barWidth, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, 20F, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, 20F, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    
                    // Green health (filled health)
                    vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix, -20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, -20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, barWidth - 20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, barWidth - 20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                }
                
                if (showName) {
                    Matrix4f matrix4f = poseStack.last().pose();
                    Font font = this.getFont();
                    
                    // Get text dimensions
                    float textWidth = font.width(Component.literal(s));
                    float textX = -textWidth / 2.0f;
                    
                    // Draw name background using gui render type
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.gui());
                    int left = (int)(textX - 1);
                    int top = (int)(yOff - 1);
                    int right = (int)(textX + textWidth + 1);
                    int bottom = (int)(yOff + 8);
                    
                    // Draw quad with minimal attributes
                    vertexconsumer.vertex(matrix4f, left, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, left, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    
                    // Render text
                    font.drawInBatch(Component.literal(s), textX, yOff, 0x20ffffff, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, 0, packedLight);
                    font.drawInBatch(Component.literal(s), textX, yOff, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
                }
                
                poseStack.popPose();
            }
        }
    }

    public void renderMoC(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLight);
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        boolean showName = MoCreatures.proxy.getDisplayPetName() && !(entityMoC.getPetName().isEmpty()) && (entityIn.getPassengers().isEmpty());
        boolean showHealth = MoCreatures.proxy.getDisplayPetHealth() && (entityIn.getPassengers().isEmpty());
        
        if (entityMoC.getIsTamed() && (entityIn.getPassengers().isEmpty())) {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f5 = (float) this.entityRenderDispatcher.distanceToSqr(entityIn);
            
            if (f5 < 256F) {
                String s = "";
                s = s + entityMoC.getPetName();
                float f7 = 0.1F;
                
                poseStack.pushPose();
                poseStack.translate(0.0F, f7, 0.0F);
                poseStack.rotate(this.entityRenderDispatcher.cameraOrientation());
                poseStack.scale(-f3, -f3, f3);
                int yOff = entityMoC.nameYOffset();
                
                if (showHealth) {
                    if (!showName) {
                        yOff += 8;
                    }
                    
                    Matrix4f matrix = poseStack.last().pose();
                    
                    // Health bar background (red)
                    float health = entityIn.getHealth();
                    float maxHealth = entityIn.getMaxHealth();
                    float healthRatio = health / maxHealth;
                    float barWidth = 40F * healthRatio;
                    
                    // Use static white texture for health bars
                    Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");
                    
                    // Red background (empty health)
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix, -20F + barWidth, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, -20F + barWidth, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, 20F, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, 20F, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    
                    // Green health (filled health)
                    vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                    vertexconsumer.vertex(matrix, -20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, -20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, barWidth - 20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    vertexconsumer.vertex(matrix, barWidth - 20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                }
                
                if (showName) {
                    Matrix4f matrix4f = poseStack.last().pose();
                    Font font = this.getFont();
                    
                    // Get text dimensions
                    float textWidth = font.width(Component.literal(s));
                    float textX = -textWidth / 2.0f;
                    
                    // Draw name background using gui render type
                    VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.gui());
                    int left = (int)(textX - 1);
                    int top = (int)(yOff - 1);
                    int right = (int)(textX + textWidth + 1);
                    int bottom = (int)(yOff + 8);
                    
                    // Draw quad with minimal attributes
                    vertexconsumer.vertex(matrix4f, left, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, left, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                    vertexconsumer.vertex(matrix4f, right, top, 0.0F).color(0, 0, 0, 64).endVertex();
                    
                    // Render text
                    font.drawInBatch(Component.literal(s), textX, yOff, 0x20ffffff, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, 0, packedLight);
                    font.drawInBatch(Component.literal(s), textX, yOff, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
                }
                
                poseStack.popPose();
            }
        }
    }

    @Override
    protected void scale(T entityliving, PoseStack matrixStackIn, float f) {
        IMoCEntity mocreature = (IMoCEntity) entityliving;
        super.scale(entityliving, matrixStackIn, f);
        // Interpolation factor for smoother animations
        float interpolationFactor = 0.05F;
        // Interpolate pitch, roll, and yaw
        float interpolatedPitch = prevPitch + (mocreature.pitchRotationOffset() - prevPitch) * interpolationFactor;
        float interpolatedRoll = prevRoll + (mocreature.rollRotationOffset() - prevRoll) * interpolationFactor;
        float interpolatedYaw = prevYaw + (mocreature.yawRotationOffset() - prevYaw) * interpolationFactor;
        // Apply the interpolated transformations
        if (interpolatedPitch != 0) {
            matrixStackIn.rotate(Axis.XN.rotationDegrees(interpolatedPitch));
        }
        if (interpolatedRoll != 0) {
            matrixStackIn.rotate(Axis.ZN.rotationDegrees(interpolatedRoll));
        }
        if (interpolatedYaw != 0) {
            matrixStackIn.rotate(Axis.YN.rotationDegrees(interpolatedYaw));
        }
        // Save the current values for the next frame's interpolation
        prevPitch = interpolatedPitch;
        prevRoll = interpolatedRoll;
        prevYaw = interpolatedYaw;
        adjustPitch(mocreature, matrixStackIn);
        adjustRoll(mocreature, matrixStackIn);
        adjustYaw(mocreature, matrixStackIn);
        stretch(mocreature, matrixStackIn);
    }

    @Override
    public boolean shouldShowName(T entity) {
        return false;
    }

    /**
     * Tilts the creature to the front / back
     */
    protected void adjustPitch(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.pitchRotationOffset();

        if (f != 0) {
            matrixStackIn.rotate(Axis.XN.rotationDegrees(f));
        }
    }

    /**
     * Rolls creature
     */
    protected void adjustRoll(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.rollRotationOffset();

        if (f != 0) {
            matrixStackIn.rotate(Axis.ZN.rotationDegrees(f));
        }
    }

    protected void adjustYaw(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.yawRotationOffset();
        if (f != 0) {
            matrixStackIn.rotate(Axis.YN.rotationDegrees(f));
        }
    }

    protected void stretch(IMoCEntity mocreature, PoseStack poseStack) {
        float f = mocreature.getSizeFactor();
        if (f != 0) {
            poseStack.scale(f, f, f);
        }
    }

    protected void adjustOffsets(float xOffset, float yOffset, float zOffset, PoseStack matrixStackIn) {
        matrixStackIn.translate(xOffset, yOffset, zOffset);
    }

    @Override
    public Identifier getTextureLocation(Mob entity) {
        return ((IMoCEntity) entity).getTexture();
    }
}