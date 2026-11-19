/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat1;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat2;
import drzhark.mocreatures.client.renderer.entity.MoCLivingRenderer;
import drzhark.mocreatures.client.renderer.entity.MoCPetLabel;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.hunter.MoCEntityLion;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/** The old-style big cats (config option), with their mane or ruff drawn by a second model. */
public class MoCLegacyRenderBigCat extends MoCLivingRenderer<MoCEntityBigCat, MoCLegacyModelBigCat2<MoCEntityBigCat>> {

    public MoCLegacyRenderBigCat(EntityRendererProvider.Context renderManagerIn, MoCLegacyModelBigCat2 modelbigcat2, MoCLegacyModelBigCat1 modelbigcat1, float f) {
        super(renderManagerIn, modelbigcat2, f);
        this.addLayer(new LayerMoCBigCat(this, modelbigcat1));
    }

    @Override
    public Identifier getTextureLocation(MoCEntityBigCat entitybigcat) {
        return entitybigcat.getTexture();
    }

    @Override
    public void submit(MoCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        MoCEntityBigCat entitybigcat = this.entity(state);
        if (entitybigcat != null && entitybigcat.getIsTamed() && state.distanceToCameraSq < 256.0D) {
            boolean displayName = MoCreatures.proxy.getDisplayPetName() && !entitybigcat.getPetName().isEmpty();
            boolean displayHealth = MoCreatures.proxy.getDisplayPetHealth();
            MoCPetLabel.submit(poseStack, collector, camera, this.getFont(), state.lightCoords, entitybigcat, entitybigcat.getPetName(),
                    displayName, displayHealth, 0.1F, -60, true);
        }
    }

    @Override
    protected void prepareModel(MoCEntityBigCat entitybigcat, MoCLegacyModelBigCat2<MoCEntityBigCat> model) {
        model.sitting = entitybigcat.getIsSitting();
        model.tamed = entitybigcat.getIsTamed();
    }

    @Override
    protected void scale(MoCEntityBigCat entitybigcat, PoseStack poseStack, float f) {
        stretch(entitybigcat, poseStack);
    }

    protected void stretch(MoCEntityBigCat entitybigcat, PoseStack poseStack) {
        float f = entitybigcat.getMoCAge() * 0.01F;
        if (entitybigcat.getIsAdult()) {
            f = 1.0F;
        }
        poseStack.scale(f, f, f);
    }

    /** The mane (male lions) or ruff, posed like the body. */
    private class LayerMoCBigCat extends RenderLayer<MoCRenderState, MoCLegacyModelBigCat2<MoCEntityBigCat>> {
        private final MoCLegacyRenderBigCat mocRenderer;
        private final MoCLegacyModelBigCat1<MoCEntityBigCat> mocModel;

        public LayerMoCBigCat(MoCLegacyRenderBigCat render, MoCLegacyModelBigCat1<MoCEntityBigCat> maneModel) {
            super(render);
            this.mocRenderer = render;
            this.mocModel = maneModel;
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            if (!(state.entity instanceof MoCEntityBigCat entitybigcat)) {
                return;
            }
            Identifier texture;
            if (entitybigcat instanceof MoCEntityLion && entitybigcat.hasMane()) {
                texture = entitybigcat.getTypeMoC() == 7
                        ? MoCreatures.proxy.getModelTexture("big_cat_white_lion_legacy_layer.png")
                        : MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_male.png");
            } else {
                texture = MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_female.png");
            }
            MoCLegacyModelBigCat2<MoCEntityBigCat> body = this.getParentModel();
            MoCLegacyModelBigCat1<MoCEntityBigCat> mane = this.mocModel;
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture), (pose, buffer) -> {
                this.mocRenderer.prepareModel(entitybigcat, body);
                body.setupAnim(state);
                mane.copyPoseFrom(body);
                mane.renderToBuffer(poseFrom(pose), buffer, lightCoords, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            });
        }
    }
}
