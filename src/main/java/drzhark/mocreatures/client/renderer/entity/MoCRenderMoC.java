/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCEntityModel;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.neutral.MoCEntityWyvern;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

/**
 * The common Mo' Creatures mob renderer: ghost and vanish translucency, the pose adjustments every creature can ask for
 * (pitch, roll, yaw, size), and the pet name and health bar above tamed creatures.
 */
public class MoCRenderMoC<T extends Mob, M extends MoCEntityModel<T>> extends MoCLivingRenderer<T, M> {

    private float prevPitch;
    private float prevRoll;
    private float prevYaw;

    public MoCRenderMoC(EntityRendererProvider.Context context, M model, float shadowSize) {
        super(context, model, shadowSize);
    }

    // ---- translucency ------------------------------------------------------------------------------------------

    /** Whether the entity is drawn translucent (ghosts, vanishing creatures, see-through models). */
    protected boolean shouldHandleTransparency(T entityIn) {
        if (!(entityIn instanceof IMoCEntity entityMoC)) {
            return false;
        }
        return entityMoC.getIsGhost() || hasVanishingEffect(entityIn) || entityMoC.shouldRenderTransparent();
    }

    /** Whether the entity is vanishing; subclasses with a vanish counter override this. */
    protected boolean hasVanishingEffect(T entityIn) {
        return false;
    }

    @Override
    protected float getBodyAlpha(T entity) {
        return shouldHandleTransparency(entity) ? calculateTransparency(entity) : 1.0F;
    }

    @Override
    protected float[] getBodyColor(T entity) {
        return shouldHandleTransparency(entity) ? getTransparencyColor(entity) : super.getBodyColor(entity);
    }

    protected float calculateTransparency(T entityIn) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        if (hasVanishingEffect(entityIn)) {
            return getVanishingTransparency(entityIn);
        }
        if (entityMoC.getIsGhost()) {
            return getGhostTransparency(entityIn);
        }
        if (entityMoC.shouldRenderTransparent()) {
            return getModelTransparency(entityIn);
        }
        return 1.0F;
    }

    protected float getVanishingTransparency(T entityIn) {
        return 1.0F;
    }

    protected float getGhostTransparency(T entityIn) {
        if (entityIn instanceof MoCEntityHorse horse) {
            return horse.tFloat();
        } else if (entityIn instanceof MoCEntityBigCat bigCat) {
            return bigCat.tFloat();
        } else if (entityIn instanceof MoCEntityWyvern wyvern) {
            return wyvern.tFloat();
        }
        return 0.6F;
    }

    protected float getModelTransparency(T entityIn) {
        return 0.6F;
    }

    protected float[] getTransparencyColor(T entityIn) {
        return new float[]{1.0F, 1.0F, 1.0F};
    }

    // ---- pet name and health bar ------------------------------------------------------------------------------

    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        T entity = this.entity(state);
        if (entity != null) {
            this.submitNameAndHealth(entity, state, poseStack, collector, camera);
        }
    }

    /** Draws the pet's name and health bar above a tamed creature that nobody is riding. */
    protected void submitNameAndHealth(T entityIn, MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        IMoCEntity entityMoC = (IMoCEntity) entityIn;
        if (!entityMoC.getIsTamed() || !entityIn.getPassengers().isEmpty() || state.distanceToCameraSq >= 256.0D) {
            return;
        }
        boolean showName = MoCreatures.proxy.getDisplayPetName() && !entityMoC.getPetName().isEmpty();
        boolean showHealth = MoCreatures.proxy.getDisplayPetHealth();
        if (!showName && !showHealth) {
            return;
        }

        MoCPetLabel.submit(poseStack, collector, camera, this.getFont(), state.lightCoords, entityIn, entityMoC.getPetName(),
                showName, showHealth, 0.1F, entityMoC.nameYOffset(), false);
    }

    @Override
    protected boolean shouldShowName(T entity, double distanceToCameraSq) {
        return false;
    }

    // ---- pose ----------------------------------------------------------------------------------------------------

    @Override
    protected void scale(T entityliving, PoseStack matrixStackIn, float f) {
        IMoCEntity mocreature = (IMoCEntity) entityliving;
        super.scale(entityliving, matrixStackIn, f);
        // Ease pitch, roll and yaw towards the creature's offsets for smoother animation
        float interpolationFactor = 0.05F;
        float interpolatedPitch = prevPitch + (mocreature.pitchRotationOffset() - prevPitch) * interpolationFactor;
        float interpolatedRoll = prevRoll + (mocreature.rollRotationOffset() - prevRoll) * interpolationFactor;
        float interpolatedYaw = prevYaw + (mocreature.yawRotationOffset() - prevYaw) * interpolationFactor;
        if (interpolatedPitch != 0) {
            matrixStackIn.rotate(Axis.XN.rotationDegrees(interpolatedPitch));
        }
        if (interpolatedRoll != 0) {
            matrixStackIn.rotate(Axis.ZN.rotationDegrees(interpolatedRoll));
        }
        if (interpolatedYaw != 0) {
            matrixStackIn.rotate(Axis.YN.rotationDegrees(interpolatedYaw));
        }
        prevPitch = interpolatedPitch;
        prevRoll = interpolatedRoll;
        prevYaw = interpolatedYaw;
        adjustPitch(mocreature, matrixStackIn);
        adjustRoll(mocreature, matrixStackIn);
        adjustYaw(mocreature, matrixStackIn);
        stretch(mocreature, matrixStackIn);
    }

    /** Tilts the creature forwards or backwards. */
    protected void adjustPitch(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.pitchRotationOffset();
        if (f != 0) {
            matrixStackIn.rotate(Axis.XN.rotationDegrees(f));
        }
    }

    /** Rolls the creature. */
    protected void adjustRoll(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.rollRotationOffset();
        if (f != 0) {
            matrixStackIn.rotate(Axis.ZN.rotationDegrees(f));
        }
    }

    protected void adjustYaw(IMoCEntity mocreature, PoseStack matrixStackIn) {
        float f = mocreature.yawRotationOffset();
        if (f != 0) {
            matrixStackIn.rotate(Axis.YN.rotationDegrees(f));
        }
    }

    protected void stretch(IMoCEntity mocreature, PoseStack poseStack) {
        float f = mocreature.getSizeFactor();
        if (f != 0) {
            poseStack.scale(f, f, f);
        }
    }

    protected void adjustOffsets(float xOffset, float yOffset, float zOffset, PoseStack matrixStackIn) {
        matrixStackIn.translate(xOffset, yOffset, zOffset);
    }

    @Override
    public Identifier getTextureLocation(T entity) {
        return ((IMoCEntity) entity).getTexture();
    }
}
