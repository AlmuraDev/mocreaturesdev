/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import drzhark.mocreatures.client.model.MoCModelWolf;
import drzhark.mocreatures.entity.hostile.MoCEntityWWolf;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderWWolf extends MoCLivingRenderer<MoCEntityWWolf, MoCModelWolf<MoCEntityWWolf>> {

    public MoCRenderWWolf(EntityRendererProvider.Context renderManagerIn, MoCModelWolf modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityWWolf par1Entity) {
        return par1Entity.getTexture();
    }
}
