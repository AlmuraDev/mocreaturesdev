/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelHorse;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderHorse extends MoCRenderMoC<MoCEntityHorse, MoCModelHorse<MoCEntityHorse>> {

    public MoCRenderHorse(EntityRendererProvider.Context context, MoCModelHorse<MoCEntityHorse> modelbase) {
        super(context, modelbase, 0.5F);
    }

    @Override
    public void render(MoCEntityHorse entityHorse, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        if (entityHorse.getIsGhost() || entityHorse.getVanishC() != 0) {
            poseStack.pushPose();

            poseStack.scale(1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0D, (double) (-1.501F), 0.0D);

            // Perform your scaling and transformations as usual
            this.scale(entityHorse, poseStack, partialTicks);

            // Determine transparency conditionally
            float transparency;

            if (entityHorse.getVanishC() != 0) {
                transparency = 1.0F - (((float) (entityHorse.getVanishC())) / 100);
            } else {
                transparency = entityHorse.tFloat();
            }

            // Explicitly select translucent or opaque render type based on transparency
            RenderType renderType = transparency < 1.0F
                    ? RenderTypes.entityTranslucent(getTextureLocation(entityHorse))
                    : RenderTypes.entityCutout(getTextureLocation(entityHorse));

            // Get VertexConsumer explicitly from original buffer
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

            // Setup rotations, animations, etc., as usual
            float bodyRot = Mth.rotLerp(partialTicks, entityHorse.yBodyRotO, entityHorse.yBodyRot);
            float headRot = Mth.rotLerp(partialTicks, entityHorse.yHeadRotO, entityHorse.yHeadRot);
            float headRotDelta = headRot - bodyRot;
            float pitch = Mth.lerp(partialTicks, entityHorse.xRotO, entityHorse.getXRot());

            setupRotations(entityHorse, poseStack, entityHorse.tickCount + partialTicks, bodyRot, partialTicks);
            model.prepareMobModel(entityHorse, 0, 0, partialTicks);
            model.setupAnim(entityHorse, 0, 0, entityHorse.tickCount + partialTicks, headRotDelta, pitch);

            // Render the model explicitly using transparency
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                    transparency);

            poseStack.popPose();

            boolean showName = MoCreatures.proxy.getDisplayPetName() && !(entityHorse.getPetName().isEmpty())
                    && (entityHorse.getPassengers().isEmpty());
            boolean showHealth = MoCreatures.proxy.getDisplayPetHealth() && (entityHorse.getPassengers().isEmpty());

            if (entityHorse.getIsTamed() && (entityHorse.getPassengers().isEmpty())) {
                float f2 = 1.6F;
                float f3 = 0.01666667F * f2;
                float f5 = (float) this.entityRenderDispatcher.distanceToSqr(entityHorse);

                if (f5 < 256F) {
                    String s = "";
                    s = s + entityHorse.getPetName();
                    float f7 = 0.1F;

                    poseStack.pushPose();
                    poseStack.translate(0.0F, f7, 0.0F);
                    poseStack.rotate(this.entityRenderDispatcher.cameraOrientation());
                    poseStack.scale(-f3, -f3, f3);
                    int yOff = entityHorse.nameYOffset();

                    if (showHealth) {
                        if (!showName) {
                            yOff += 8;
                        }

                        Matrix4f matrix = poseStack.last().pose();

                        // Health bar background (red)
                        float health = entityHorse.getHealth();
                        float maxHealth = entityHorse.getMaxHealth();
                        float healthRatio = health / maxHealth;
                        float barWidth = 40F * healthRatio;

                        // Use static white texture for health bars
                        Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");

                        // Red background (empty health)
                        VertexConsumer vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                        vertexconsumer.vertex(matrix, -20F + barWidth, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F)
                                .uv(0, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, -20F + barWidth, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F)
                                .uv(0, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, 20F, -6 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 1)
                                .overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, 20F, -10 + yOff, 0.0F).color(0.7F, 0.0F, 0.0F, 1.0F).uv(1, 0)
                                .overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();

                        // Green health (filled health)
                        vertexconsumer = buffer.getBuffer(RenderTypes.text(WHITE_TEXTURE));
                        vertexconsumer.vertex(matrix, -20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 0)
                                .overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, -20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F).uv(0, 1)
                                .overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, barWidth - 20F, -6 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F)
                                .uv(1, 1).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                        vertexconsumer.vertex(matrix, barWidth - 20F, -10 + yOff, 0.01F).color(0.0F, 0.7F, 0.0F, 1.0F)
                                .uv(1, 0).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
                    }

                    if (showName) {
                        Matrix4f matrix4f = poseStack.last().pose();
                        Font font = this.getFont();

                        // Get text dimensions
                        float textWidth = font.width(Component.literal(s));
                        float textX = -textWidth / 2.0f;

                        // Draw name background using gui render type
                        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.gui());
                        int left = (int) (textX - 1);
                        int top = (int) (yOff - 1);
                        int right = (int) (textX + textWidth + 1);
                        int bottom = (int) (yOff + 8);

                        // Draw quad with minimal attributes
                        vertexconsumer.vertex(matrix4f, left, top, 0.0F).color(0, 0, 0, 64).endVertex();
                        vertexconsumer.vertex(matrix4f, left, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                        vertexconsumer.vertex(matrix4f, right, bottom, 0.0F).color(0, 0, 0, 64).endVertex();
                        vertexconsumer.vertex(matrix4f, right, top, 0.0F).color(0, 0, 0, 64).endVertex();

                        // Render text
                        font.drawInBatch(Component.literal(s), textX, yOff, 0x20ffffff, false, matrix4f, buffer,
                                Font.DisplayMode.SEE_THROUGH, 0, packedLight);
                        font.drawInBatch(Component.literal(s), textX, yOff, -1, false, matrix4f, buffer,
                                Font.DisplayMode.NORMAL, 0, packedLight);
                    }

                    poseStack.popPose();
                }
            }
        } else {
            this.renderMoC(entityHorse, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public Identifier getTextureLocation(MoCEntityHorse entityhorse) {
        return entityhorse.getTexture();
    }

    protected void adjustHeight(MoCEntityHorse entityhorse, float FHeight, PoseStack poseStack) {
        poseStack.translate(0.0F, FHeight, 0.0F);
    }

    @Override
    protected void scale(MoCEntityHorse entityhorse, PoseStack poseStack, float partialTicks) {
        if (!entityhorse.getIsAdult() || entityhorse.getTypeMoC() > 64) {
            stretch(entityhorse, poseStack);
        }
        if (entityhorse.getIsGhost()) {
            adjustHeight(entityhorse, -0.3F + (entityhorse.tFloat() / 5F), poseStack);
        }
        super.scale(entityhorse, poseStack, partialTicks);
    }

    protected void stretch(MoCEntityHorse entityhorse, PoseStack poseStack) {
        float sizeFactor = entityhorse.getMoCAge() * 0.01F;
        if (entityhorse.getIsAdult()) {
            sizeFactor = 1.0F;
        }
        if (entityhorse.getTypeMoC() > 64) // donkey
        {
            sizeFactor *= 0.9F;
        }
        poseStack.scale(sizeFactor, sizeFactor, sizeFactor);
    }
    
    @Override
    protected boolean hasVanishingEffect(MoCEntityHorse entityIn) {
        return entityIn.getVanishC() != 0;
    }
    
    @Override
    protected float getVanishingTransparency(MoCEntityHorse entityIn) {
        return 1.0F - (((float) (entityIn.getVanishC())) / 100);
    }
    
    @Override
    protected float getGhostTransparency(MoCEntityHorse entityIn) {
        return entityIn.tFloat();
    }
}
