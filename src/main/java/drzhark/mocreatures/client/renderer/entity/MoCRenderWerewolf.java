/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.model.MoCEntityModel;
import drzhark.mocreatures.client.model.MoCModelWerehuman;
import drzhark.mocreatures.client.model.MoCModelWerewolf;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import drzhark.mocreatures.entity.hostile.MoCEntityWerewolf;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class MoCRenderWerewolf<M extends MoCEntityModel<MoCEntityWerewolf>> extends MoCLivingRenderer<MoCEntityWerewolf, M> {

    public MoCRenderWerewolf(EntityRendererProvider.Context context, MoCModelWerehuman<MoCEntityWerewolf> modelwerehuman, M modelbase, float f) {
        super(context, modelbase, f);
        this.addLayer(new LayerMoCWereHuman(this, modelwerehuman));
    }

    @Override
    protected void prepareModel(MoCEntityWerewolf entity, M model) {
        if (model instanceof MoCModelWerewolf<?> werewolf) {
            werewolf.hunched = entity.getIsHunched();
        }
    }

    @Override
    public Identifier getTextureLocation(MoCEntityWerewolf entity) {
        return entity.getTexture();
    }

    /** In human form the werewolf is drawn with the humanoid model and its own skin. */
    private class LayerMoCWereHuman extends RenderLayer<MoCRenderState, M> {
        private final MoCModelWerehuman<MoCEntityWerewolf> humanModel;

        public LayerMoCWereHuman(MoCRenderWerewolf<M> renderer, MoCModelWerehuman<MoCEntityWerewolf> humanModel) {
            super(renderer);
            this.humanModel = humanModel;
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            if (!(state.entity instanceof MoCEntityWerewolf entity) || !entity.getIsHumanForm()) {
                return;
            }
            Identifier texture = switch (entity.getTypeMoC()) {
                case 1 -> MoCreatures.proxy.getModelTexture("werehuman_dude.png");
                case 2 -> MoCreatures.proxy.getModelTexture("werehuman_classic.png");
                case 4 -> MoCreatures.proxy.getModelTexture("werehuman_woman.png");
                default -> MoCreatures.proxy.getModelTexture("werehuman_oldie.png");
            };
            MoCModelWerehuman<MoCEntityWerewolf> model = this.humanModel;
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture), (pose, buffer) -> {
                model.setupAnim(state);
                model.renderToBuffer(poseFrom(pose), buffer, lightCoords, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            });
        }
    }
}
