/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelBear;
import drzhark.mocreatures.entity.hunter.MoCEntityBear;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderBear extends MoCRenderMoC<MoCEntityBear, MoCModelBear<MoCEntityBear>> {

    public MoCRenderBear(EntityRendererProvider.Context renderManagerIn, MoCModelBear modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    protected void scale(MoCEntityBear entitybear, PoseStack poseStack, float f) {
        stretch(poseStack, entitybear);
        super.scale(entitybear, poseStack, f);

    }

    protected void stretch(PoseStack poseStack, MoCEntityBear entitybear) {
        float sizeFactor = entitybear.getMoCAge() * 0.01F;
        if (entitybear.getIsAdult()) {
            sizeFactor = 1.0F;
        }
        sizeFactor *= entitybear.getBearSize();
        poseStack.scale(sizeFactor, sizeFactor, sizeFactor);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityBear entitybear) {
        return entitybear.getTexture();
    }
}
