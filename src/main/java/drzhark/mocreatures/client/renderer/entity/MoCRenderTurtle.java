/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelTurtle;
import drzhark.mocreatures.entity.passive.MoCEntityTurtle;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderTurtle extends MoCRenderMoC<MoCEntityTurtle, MoCModelTurtle<MoCEntityTurtle>> {

    public MoCModelTurtle turtly;

    public MoCRenderTurtle(EntityRendererProvider.Context renderManagerIn, MoCModelTurtle modelbase, float f) {
        super(renderManagerIn, modelbase, f);
        this.turtly = modelbase;
    }

    @Override
    protected void scale(MoCEntityTurtle entityturtle, PoseStack poseStack, float f) {
        this.turtly.upsidedown = entityturtle.getIsUpsideDown();
        this.turtly.swingProgress = entityturtle.attackAnim;
        this.turtly.isHiding = entityturtle.getIsHiding();

        if (!entityturtle.level().isClientSide() && (entityturtle.getVehicle() != null)) {
            poseStack.translate(0.0F, 1.3F, 0.0F);
        }
        if (entityturtle.getIsHiding()) {
            adjustHeight(entityturtle, 0.15F * entityturtle.getMoCAge() * 0.01F, poseStack);
        } else if (!entityturtle.getIsHiding() && !entityturtle.getIsUpsideDown() && !entityturtle.isEyeInFluid(FluidTags.WATER)) {
            adjustHeight(entityturtle, 0.05F * entityturtle.getMoCAge() * 0.01F, poseStack);
        }
        if (entityturtle.getIsUpsideDown()) {
            rotateAnimal(entityturtle, poseStack);
        }

        stretch(entityturtle, poseStack);
    }

    protected void rotateAnimal(MoCEntityTurtle entityturtle, PoseStack poseStack) {
        //poseStack.rotate(Axis.XN.rotationDegrees(180F)); //head up 180
        //poseStack.rotate(Axis.YN.rotationDegrees(180F)); //head around 180

        float f = entityturtle.attackAnim * 10F * entityturtle.getFlipDirection();
        float f2 = entityturtle.attackAnim / 30 * entityturtle.getFlipDirection();
        poseStack.rotate(Axis.ZN.rotationDegrees(180F + f));
        poseStack.translate(0.0F - f2, 0.5F * entityturtle.getMoCAge() * 0.01F, 0.0F);
    }

    protected void adjustHeight(MoCEntityTurtle entityturtle, float height, PoseStack poseStack) {
        poseStack.translate(0.0F, height, 0.0F);
    }

    protected void stretch(MoCEntityTurtle entityturtle, PoseStack poseStack) {
        float f = entityturtle.getMoCAge() * 0.01F;
        poseStack.scale(f, f, f);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityTurtle entityturtle) {
        return entityturtle.getTexture();
    }
}
