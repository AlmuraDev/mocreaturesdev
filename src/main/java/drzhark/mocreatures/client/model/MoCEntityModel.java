/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

/**
 * Base for the Mo' Creatures entity models.
 * <p>
 * 26.3 models are driven by a render state and render their root part only. The Mo' Creatures models animate from the
 * entity and draw their parts one by one (some parts conditionally, some with their own alpha), so this class keeps the
 * 1.20 entry points: {@link #setupAnim(MoCRenderState)} forwards to the entity-based
 * {@link #setupAnim(Entity, float, float, float, float, float)}, and
 * {@link #renderToBuffer(PoseStack, VertexConsumer, int, int, float, float, float, float)} is the model's own draw
 * routine, which {@link drzhark.mocreatures.client.renderer.entity.MoCLivingRenderer} calls through custom geometry.
 */
public abstract class MoCEntityModel<T extends Entity> extends EntityModel<MoCRenderState> {

    /** The 1.20 EntityModel fields, refreshed from the render state before each animation pass. */
    public boolean young;
    public boolean riding;
    public float attackTime;

    protected MoCEntityModel(ModelPart root) {
        super(root);
    }

    protected MoCEntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void setupAnim(MoCRenderState state) {
        this.young = state.isBaby;
        this.riding = state.isPassenger;
        this.attackTime = state.attackTime;
        if (state.entity == null) {
            return;
        }
        T entity = (T) state.entity;
        this.prepareMobModel(entity, state.walkAnimationPos, state.walkAnimationSpeed, state.partialTick);
        this.setupAnim(entity, state.walkAnimationPos, state.walkAnimationSpeed, state.ageInTicks, state.yRot, state.xRot);
    }

    /** 1.20 {@code prepareMobModel}: runs before {@link #setupAnim(Entity, float, float, float, float, float)}. */
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
    }

    /** 1.20 {@code setupAnim}, animated from the entity. */
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    /** Draws the model. The default draws the whole root; most models override this to draw their parts themselves. */
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /** Draws one part with a float tint, the way 1.20 {@code ModelPart.render} took it. */
    protected static void renderPart(ModelPart part, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                                     float red, float green, float blue, float alpha) {
        part.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
