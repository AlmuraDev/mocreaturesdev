/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelSnake;
import drzhark.mocreatures.entity.hunter.MoCEntitySnake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderSnake extends MoCRenderMoC<MoCEntitySnake, MoCModelSnake<MoCEntitySnake>> {

    public MoCRenderSnake(EntityRendererProvider.Context renderManagerIn, MoCModelSnake modelbase, float f) {
        super(renderManagerIn, modelbase, 0.0F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntitySnake par1Entity) {
        return par1Entity.getTexture();
    }

    protected void adjustHeight(MoCEntitySnake entitysnake, float FHeight, PoseStack poseStack) {
        poseStack.translate(0.0F, FHeight, 0.0F);
    }

    @Override
    protected void scale(MoCEntitySnake entitysnake, PoseStack poseStack, float f) {
        stretch(entitysnake, poseStack);

        /*
         * if(mod_mocreatures.mc.isMultiplayerWorld() &&
         * (entitysnake.pickedUp())) { poseStack.translate(0.0F, 1.4F, 0.0F); }
         */

        if (entitysnake.pickedUp())// && entitysnake.getSizeF() < 0.6F)
        {
            float xOff = (entitysnake.getSizeF() - 1.0F);
            if (xOff > 0.0F) {
                xOff = 0.0F;
            }
            if (entitysnake.level().isClientSide()) {
                poseStack.translate(xOff, 0.0F, 0F);
            } else {
                poseStack.translate(xOff, 0F, 0.0F);
                //-0.5 puts it in the right shoulder
            }
            /*
             * //if(small) //works for small snakes poseStack.rotate(Axis.XP.rotationDegrees(20F));
             * if(mod_mocreatures.mc.isMultiplayerWorld()) {
             * poseStack.translate(-0.5F, 1.4F, 0F); } else {
             * poseStack.translate(0.7F, 0F, 1.2F); }
             */
        }

        if (entitysnake.isEyeInFluid(FluidTags.WATER)) {
            adjustHeight(entitysnake, -0.25F, poseStack);
        }

        super.scale(entitysnake, poseStack, f);
    }

    protected void stretch(MoCEntitySnake entitysnake, PoseStack poseStack) {
        float f = entitysnake.getSizeF();
        poseStack.scale(f, f, f);
    }

    /*
     * @Override protected void preRenderCallback(MobEntity entityliving, PoseStack poseStack,
     * float f) { MoCEntitySnake entitysnake = (MoCEntitySnake) entityliving;
     * //tempSnake.textPos = entitysnake.type - 1; if (entitysnake.type <4) {
     * tempSnake.textPos = 0; }else { tempSnake.textPos = 1; }
     * super.scale(entityliving, PoseStack poseStack, f); } private MoCModelSnake
     * tempSnake;
     */
}
