/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelKitty;
import drzhark.mocreatures.entity.neutral.MoCEntityKitty;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import com.mojang.math.Axis;

import org.joml.Matrix4f;

public class MoCRenderKitty extends MoCRenderMoC<MoCEntityKitty, MoCModelKitty<MoCEntityKitty>> {

    public MoCModelKitty kitty;

    public MoCRenderKitty(EntityRendererProvider.Context renderManagerIn, MoCModelKitty modelkitty, float f) {
        super(renderManagerIn, modelkitty, f);
        this.kitty = modelkitty;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityKitty entitykitty) {
        return entitykitty.getTexture();
    }

    /** The kitty's mood icon floats above a tamed kitty when pet icons are on. */
    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        MoCEntityKitty entitykitty = this.entity(state);
        if (entitykitty == null || !entitykitty.getIsTamed() || state.distanceToCameraSq >= 144.0D
                || !MoCreatures.proxy.getDisplayPetIcons() || !entitykitty.getShowEmoteIcon()) {
            return;
        }
        float scale = 0.01666667F * 1.6F;
        poseStack.pushPose();
        poseStack.translate(0.0F, entitykitty.getIsSitting() ? 0.4F : 0.2F, 0.0F);
        poseStack.rotate(Axis.YP.rotationDegrees(-camera.yRot));
        poseStack.scale(-scale, -scale, scale);
        int top = -90;
        int size = 32;
        int left = -size / 2;
        collector.submitCustomGeometry(poseStack, RenderTypes.text(entitykitty.getEmoteIcon()), (pose, vc) -> {
            int light = 0xF000F0;
            vc.addVertex(pose, left, top + size, 0F).setColor(255, 255, 255, 255).setUv(0.0F, 1.0F).setLight(light);
            vc.addVertex(pose, left + size, top + size, 0F).setColor(255, 255, 255, 255).setUv(1.0F, 1.0F).setLight(light);
            vc.addVertex(pose, left + size, top, 0F).setColor(255, 255, 255, 255).setUv(1.0F, 0.0F).setLight(light);
            vc.addVertex(pose, left, top, 0F).setColor(255, 255, 255, 255).setUv(0.0F, 0.0F).setLight(light);
        });
        poseStack.popPose();
    }

    @Override
    protected void prepareModel(MoCEntityKitty entitykitty, MoCModelKitty<MoCEntityKitty> model) {
        model.isSitting = entitykitty.getIsSitting();
        model.isSwinging = entitykitty.getIsSwinging();
        model.swingProgress = entitykitty.attackAnim;
        model.kittystate = entitykitty.getKittyState();
    }

    protected void onMaBack(MoCEntityKitty entitykitty, PoseStack poseStack) {
        poseStack.rotate(Axis.ZN.rotationDegrees(90F));
        if (!entitykitty.level().isClientSide() && (entitykitty.getVehicle() != null)) {
            poseStack.translate(-1.5F, 0.2F, -0.2F);
        } else {
            poseStack.translate(0.1F, 0.2F, -0.2F);
        }
    }

    protected void onTheSide(MoCEntityKitty entityliving, PoseStack poseStack) {
        poseStack.rotate(Axis.ZN.rotationDegrees(90F));
        poseStack.translate(0.2F, 0.0F, -0.2F);
    }

    @Override
    protected void scale(MoCEntityKitty entitykitty, PoseStack poseStack, float f) {
        if (!entitykitty.getIsAdult()) {
            stretch(entitykitty, poseStack);
        }
        if (entitykitty.getKittyState() == 20) {
            onTheSide(entitykitty, poseStack);
        }
        if (entitykitty.climbingTree()) {
            rotateAnimal(entitykitty, poseStack);
        }
        if (entitykitty.upsideDown()) {
            upsideDown(entitykitty, poseStack);
        }
        if (entitykitty.onMaBack()) {
            onMaBack(entitykitty, poseStack);
        }
    }

    protected void rotateAnimal(MoCEntityKitty entitykitty, PoseStack poseStack) {
        poseStack.rotate(Axis.XN.rotationDegrees(90F));
        poseStack.translate(0.0F, 0.5F, 0.0F);
    }

    protected void stretch(MoCEntityKitty entitykitty, PoseStack poseStack) {
        poseStack.scale(entitykitty.getMoCAge() * 0.01F, entitykitty.getMoCAge() * 0.01F,
                entitykitty.getMoCAge() * 0.01F);
    }

    protected void upsideDown(MoCEntityKitty entitykitty, PoseStack poseStack) {
        poseStack.rotate(Axis.ZN.rotationDegrees(180F));
        poseStack.translate(-0.35F, 0F, -0.55F);
    }
}
