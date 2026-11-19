/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCModelGoat;
import drzhark.mocreatures.entity.neutral.MoCEntityGoat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import com.mojang.math.Axis;

public class MoCRenderGoat extends MoCLivingRenderer<MoCEntityGoat, MoCModelGoat<MoCEntityGoat>> {

    private static final Identifier WHITE_TEXTURE = Identifier.parse("textures/misc/white.png");
    private final MoCModelGoat tempGoat;
    float depth = 0F;

    public MoCRenderGoat(EntityRendererProvider.Context renderManagerIn, MoCModelGoat modelbase, float f) {
        super(renderManagerIn, modelbase, f);
        this.tempGoat = modelbase;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityGoat entitygoat) {
        return entitygoat.getTexture();
    }

    @Override
    protected void scale(MoCEntityGoat entitygoat, PoseStack poseStack, float partialTick) {
        poseStack.translate(0.0F, this.depth, 0.0F);
        stretch(entitygoat, poseStack);
    }

    @Override
    protected void prepareModel(MoCEntityGoat entitygoat, MoCModelGoat<MoCEntityGoat> model) {
        model.typeInt = entitygoat.getTypeMoC();
        model.age = entitygoat.getMoCAge() * 0.01F;
        model.bleat = entitygoat.getBleating();
        model.attacking = entitygoat.getAttacking();
        model.legMov = entitygoat.legMovement();
        model.earMov = entitygoat.earMovement();
        model.tailMov = entitygoat.tailMovement();
        model.eatMov = entitygoat.mouthMovement();
    }

    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        MoCEntityGoat entitygoat = this.entity(state);
        if (entitygoat != null && entitygoat.getIsTamed() && state.distanceToCameraSq < 256.0D) {
            boolean showName = MoCreatures.proxy.getDisplayPetName() && !entitygoat.getPetName().isEmpty();
            boolean showHealth = MoCreatures.proxy.getDisplayPetHealth();
            MoCPetLabel.submit(poseStack, collector, camera, this.getFont(), state.lightCoords, entitygoat, entitygoat.getPetName(),
                    showName, showHealth, 0.1F, (byte) (-15 + (-40 * entitygoat.getMoCAge() * 0.01F)), true);
        }
    }

    protected void stretch(MoCEntityGoat entitygoat, PoseStack poseStack) {
        float scale = entitygoat.getMoCAge() * 0.01F;
        poseStack.scale(scale, scale, scale);
    }
}
