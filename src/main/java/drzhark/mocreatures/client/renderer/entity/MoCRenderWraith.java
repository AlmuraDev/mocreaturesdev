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
    protected float getBodyAlpha(MoCEntityWraith wraith) {
        return wraith instanceof MoCEntityFlameWraith ? 0.4F : 0.6F;
    }

    @Override
    protected float[] getBodyColor(MoCEntityWraith wraith) {
        return wraith instanceof MoCEntityFlameWraith ? new float[]{1.0F, 0.6F, 0.6F} : new float[]{0.8F, 0.8F, 0.8F};
    }

    @Override
    public Identifier getTextureLocation(MoCEntityWraith wraith) {
        return wraith.getTexture();
    }
}
