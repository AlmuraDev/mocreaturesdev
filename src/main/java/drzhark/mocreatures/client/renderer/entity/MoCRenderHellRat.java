/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelRat;
import drzhark.mocreatures.entity.hostile.MoCEntityHellRat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderHellRat extends MoCRenderRat<MoCEntityHellRat, MoCModelRat<MoCEntityHellRat>> {

    public MoCRenderHellRat(EntityRendererProvider.Context renderManagerIn, MoCModelRat modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    protected void stretch(MoCEntityHellRat entityhellrat, PoseStack poseStack) {
        float f = 1.3F;
        poseStack.scale(f, f, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityHellRat entityhellrat) {
        return entityhellrat.getTexture();
    }
}
