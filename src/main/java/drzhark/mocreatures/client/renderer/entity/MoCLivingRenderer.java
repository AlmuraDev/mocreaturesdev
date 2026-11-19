/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.IPartialTransparencyModel;
import drzhark.mocreatures.client.model.MoCEntityModel;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import org.jspecify.annotations.Nullable;

/**
 * Base for the Mo' Creatures mob renderers.
 * <p>
 * It keeps the 1.20 renderer hooks the creature renderers override ({@code scale}, {@code setupRotations} and
 * {@code getTextureLocation} taking the entity) and draws the body through the model's own
 * {@link MoCEntityModel#renderToBuffer(PoseStack, com.mojang.blaze3d.vertex.VertexConsumer, int, int, float, float, float, float)}
 * routine, submitted as custom geometry. Vanilla's model submission is turned off ({@link #getRenderType} returns null);
 * the body is drawn by the first layer instead, so it gets the same pose as vanilla would give it and the other layers
 * still draw on top.
 */
public abstract class MoCLivingRenderer<T extends Mob, M extends MoCEntityModel<T>> extends MobRenderer<T, MoCRenderState, M> {

    /** The state being drawn; the legacy hooks get it from here so their signatures can stay as they were. */
    protected @Nullable MoCRenderState currentState;
    private float currentEntityScale = 1.0F;

    protected MoCLivingRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
        super(context, model, shadowSize);
        this.addLayer(new BodyLayer());
    }

    @Override
    public MoCRenderState createRenderState() {
        return new MoCRenderState();
    }

    @Override
    public void extractRenderState(T entity, MoCRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.isPassenger = entity.isPassenger();
        state.attackTime = entity.getSwingAnimation(partialTicks);
        state.ageInTicks = this.getBob(entity, partialTicks);
    }

    /** 1.20 {@code getBob}: the animation clock the model gets as ageInTicks. */
    protected float getBob(T entity, float partialTicks) {
        return entity.tickCount + partialTicks;
    }

    @Override
    protected final float getFlipDegrees() {
        T entity = this.currentState != null ? this.entity(this.currentState) : null;
        return entity != null ? this.getFlipDegrees(entity) : super.getFlipDegrees();
    }

    /** 1.20 {@code getFlipDegrees}: how far the body turns over when the creature dies. */
    protected float getFlipDegrees(T entity) {
        return 90.0F;
    }

    @SuppressWarnings("unchecked")
    protected T entity(MoCRenderState state) {
        return (T) state.entity;
    }

    // ---- texture ---------------------------------------------------------------------------------------------------

    @Override
    public final Identifier getTextureLocation(MoCRenderState state) {
        return this.getTextureLocation(this.entity(state));
    }

    public abstract Identifier getTextureLocation(T entity);

    // ---- pose hooks, forwarded to the 1.20 signatures ------------------------------------------------------------------

    @Override
    protected final void scale(MoCRenderState state, PoseStack poseStack) {
        this.currentState = state;
        this.scale(this.entity(state), poseStack, state.partialTick);
    }

    /** 1.20 {@code scale}. */
    protected void scale(T entity, PoseStack poseStack, float partialTickTime) {
    }

    @Override
    protected final void setupRotations(MoCRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        this.currentState = state;
        this.currentEntityScale = entityScale;
        this.setupRotations(this.entity(state), poseStack, state.ageInTicks, bodyRot, state.partialTick);
    }

    /** 1.20 {@code setupRotations}; calling super runs vanilla's rotations. */
    protected void setupRotations(T entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(this.currentState, poseStack, rotationYaw, this.currentEntityScale);
    }

    // ---- body ----------------------------------------------------------------------------------------------------

    @Override
    protected @Nullable RenderType getRenderType(MoCRenderState state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        return null;
    }

    /**
     * Copies per-entity values into the model right before it is drawn. Models are shared by every entity of a type and
     * drawn later in the frame, so anything a renderer used to set on the model while rendering belongs here.
     */
    protected void prepareModel(T entity, M model) {
    }

    /** Opacity of the whole body; below 1 the body is drawn translucent. */
    protected float getBodyAlpha(T entity) {
        return 1.0F;
    }

    /** Red, green and blue tint of the body. */
    protected float[] getBodyColor(T entity) {
        return new float[]{1.0F, 1.0F, 1.0F};
    }

    /** Render type for an opaque body; models that need translucency for their own parts can widen this. */
    protected RenderType getOpaqueRenderType(Identifier texture) {
        return this.model.renderType(texture);
    }

    protected void submitBody(PoseStack poseStack, SubmitNodeCollector collector, MoCRenderState state) {
        T entity = this.entity(state);
        if (entity == null) {
            return;
        }
        boolean bodyVisible = this.isBodyVisible(state);
        boolean forceTransparent = !bodyVisible && !state.isInvisibleToPlayer;
        if (!bodyVisible && !forceTransparent) {
            return;
        }
        Identifier texture = this.getTextureLocation(state);
        float alpha = forceTransparent ? 0.15F : this.getBodyAlpha(entity);
        float[] color = this.getBodyColor(entity);
        int light = state.lightCoords;
        int overlay = getOverlayCoords(state, this.getWhiteOverlayProgress(state));
        M model = this.model;

        if (model instanceof IPartialTransparencyModel<?> partial && partial.shouldRenderPartialTransparency() && !forceTransparent) {
            float partAlpha = partial.getTransparencyValue();
            float[] partColor = partial.getTransparencyColor();
            collector.submitCustomGeometry(poseStack, this.getOpaqueRenderType(texture), (pose, buffer) -> {
                PoseStack stack = poseFrom(pose);
                this.prepareModel(entity, model);
                model.setupAnim(state);
                partial.renderOpaqueParts(stack, buffer, light, overlay, color[0], color[1], color[2], alpha);
            });
            collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(texture), (pose, buffer) -> {
                PoseStack stack = poseFrom(pose);
                this.prepareModel(entity, model);
                model.setupAnim(state);
                partial.renderTransparentParts(stack, buffer, light, overlay, partColor[0], partColor[1], partColor[2], partAlpha);
            });
            return;
        }

        RenderType renderType = alpha < 1.0F ? RenderTypes.entityTranslucent(texture) : this.getOpaqueRenderType(texture);
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            PoseStack stack = poseFrom(pose);
            this.prepareModel(entity, model);
            model.setupAnim(state);
            model.renderToBuffer(stack, buffer, light, overlay, color[0], color[1], color[2], alpha);
        });
    }

    /** Draws the model again with another render type and tint, for glow and effect layers. */
    public void submitModelPass(PoseStack poseStack, SubmitNodeCollector collector, MoCRenderState state, RenderType renderType,
                                float red, float green, float blue, float alpha) {
        T entity = this.entity(state);
        if (entity == null) {
            return;
        }
        int light = state.lightCoords;
        int overlay = getOverlayCoords(state, this.getWhiteOverlayProgress(state));
        M model = this.model;
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            PoseStack stack = poseFrom(pose);
            this.prepareModel(entity, model);
            model.setupAnim(state);
            model.renderToBuffer(stack, buffer, light, overlay, red, green, blue, alpha);
        });
    }

    protected static PoseStack poseFrom(PoseStack.Pose pose) {
        PoseStack stack = new PoseStack();
        stack.last().set(pose);
        return stack;
    }

    /** Draws the body as the first layer, inside the pose vanilla sets up for the model. */
    private final class BodyLayer extends RenderLayer<MoCRenderState, M> {
        BodyLayer() {
            super(MoCLivingRenderer.this);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            MoCLivingRenderer.this.submitBody(poseStack, collector, state);
        }
    }
}
