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
import drzhark.mocreatures.client.model.MoCModelDolphin;
import drzhark.mocreatures.entity.aquatic.MoCEntityDolphin;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderDolphin extends MoCLivingRenderer<MoCEntityDolphin, MoCModelDolphin<MoCEntityDolphin>> {

    public MoCRenderDolphin(EntityRendererProvider.Context renderManagerIn, MoCModelDolphin modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        MoCEntityDolphin entitydolphin = this.entity(state);
        if (entitydolphin != null && entitydolphin.getIsTamed() && state.distanceToCameraSq < 256.0D) {
            boolean showName = MoCreatures.proxy.getDisplayPetName() && !entitydolphin.getPetName().isEmpty();
            boolean showHealth = MoCreatures.proxy.getDisplayPetHealth();
            MoCPetLabel.submit(poseStack, collector, camera, this.getFont(), state.lightCoords, entitydolphin, entitydolphin.getPetName(),
                    showName, showHealth, 0.1F, -50, true);
        }
    }

    @Override
    protected void scale(MoCEntityDolphin entitydolphin, PoseStack poseStack, float partialTickTime) {
        stretch(entitydolphin, poseStack);
    }

    protected void stretch(MoCEntityDolphin entitydolphin, PoseStack poseStack) {
        poseStack.scale(entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F);
    }

    @Override
    public Identifier getTextureLocation(MoCEntityDolphin entitydolphin) {
        return entitydolphin.getTexture();
    }
}
