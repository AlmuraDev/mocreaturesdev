/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import drzhark.mocreatures.client.model.MoCModelWraith;
import drzhark.mocreatures.entity.hostile.MoCEntityFlameWraith;
import drzhark.mocreatures.entity.hostile.MoCEntityWraith;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderWraith extends MoCLivingRenderer<MoCEntityWraith, MoCModelWraith<MoCEntityWraith>> {

    public MoCRenderWraith(EntityRendererProvider.Context renderManagerIn, MoCModelWraith modelbiped, float f) {
        super(renderManagerIn, modelbiped, f);
    }

    @Override
    public void render(MoCEntityWraith wraith, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, (double)(-1.501F), 0.0D);
    
        // Correctly handle entity rotations exactly like MobRenderer does
        float bodyRot = Mth.rotLerp(partialTicks, wraith.yBodyRotO, wraith.yBodyRot);
        float headRot = Mth.rotLerp(partialTicks, wraith.yHeadRotO, wraith.yHeadRot);
        float headRotDelta = headRot - bodyRot;
    
        float pitch = Mth.lerp(partialTicks, wraith.xRotO, wraith.getXRot());
    
        // apply entity orientation
        this.setupRotations(wraith, poseStack, wraith.tickCount + partialTicks, bodyRot, partialTicks);
    
        // Get your translucent render type
        RenderType renderType = RenderTypes.entityTranslucent(getTextureLocation(wraith));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
    
        // Setup animation (important!)
        model.prepareMobModel(wraith, 0, 0, partialTicks);
        model.setupAnim(wraith, 0, 0, wraith.tickCount + partialTicks, headRotDelta, pitch);
    
        float transparency = wraith instanceof MoCEntityFlameWraith ? 0.4F : 0.6F;
        float red = wraith instanceof MoCEntityFlameWraith ? 1.0F : 0.8F;
        float green = wraith instanceof MoCEntityFlameWraith ? 0.6F : 0.8F;
        float blue = wraith instanceof MoCEntityFlameWraith ? 0.6F : 0.8F;
    
        // rendering with transparency, correctly posed
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, transparency);
    
        poseStack.popPose();
    }
    


    @Override
    public Identifier getTextureLocation(MoCEntityWraith wraith) {
        return wraith.getTexture();
    }
}
