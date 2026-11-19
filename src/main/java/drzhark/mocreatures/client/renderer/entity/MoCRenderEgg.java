/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelEgg;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderEgg extends MoCLivingRenderer<MoCEntityEgg, MoCModelEgg<MoCEntityEgg>> {

    public MoCRenderEgg(EntityRendererProvider.Context renderManagerIn, MoCModelEgg modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    protected void scale(MoCEntityEgg entityegg, PoseStack poseStack, float f) {
        stretch(entityegg, poseStack);
        super.scale(entityegg, poseStack, f);

    }

    protected void stretch(MoCEntityEgg entityegg, PoseStack poseStack) {
        float f = entityegg.getSize() * 0.01F;
        poseStack.scale(f, f, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityEgg entityegg) {
        return entityegg.getTexture();
    }
}
