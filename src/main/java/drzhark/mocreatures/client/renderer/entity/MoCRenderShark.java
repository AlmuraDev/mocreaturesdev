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
import drzhark.mocreatures.client.model.MoCModelShark;
import drzhark.mocreatures.entity.aquatic.MoCEntityShark;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import com.mojang.math.Axis;

public class MoCRenderShark extends MoCLivingRenderer<MoCEntityShark, MoCModelShark<MoCEntityShark>> {

    public MoCRenderShark(EntityRendererProvider.Context renderManagerIn, MoCModelShark modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        MoCEntityShark entityshark = this.entity(state);
        if (entityshark != null && entityshark.getIsTamed() && state.distanceToCameraSq < 256.0D) {
            boolean showName = MoCreatures.proxy.getDisplayPetName() && !entityshark.getPetName().isEmpty();
            boolean showHealth = MoCreatures.proxy.getDisplayPetHealth();
            MoCPetLabel.submit(poseStack, collector, camera, this.getFont(), state.lightCoords, entityshark, entityshark.getPetName(),
                    showName, showHealth, 0.1F, -50, true);
        }
    }

    @Override
    protected void scale(MoCEntityShark entityshark, PoseStack poseStack, float partialTickTime) {
        stretch(entityshark, poseStack);
    }

    protected void stretch(MoCEntityShark entityshark, PoseStack poseStack) {
        poseStack.scale((entityshark.getMoCAge() * 0.01F) / 2, (entityshark.getMoCAge() * 0.01F) / 2, (entityshark.getMoCAge() * 0.01F) / 2);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityShark entityshark) {
        return entityshark.getTexture();
    }
}
