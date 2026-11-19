/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelKittyBed;
import drzhark.mocreatures.client.model.MoCModelKittyBed2;
import drzhark.mocreatures.client.renderer.state.MoCRenderState;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;

public class MoCRenderKittyBed extends MoCLivingRenderer<MoCEntityKittyBed, MoCModelKittyBed<MoCEntityKittyBed>> {

    public MoCRenderKittyBed(EntityRendererProvider.Context renderManagerIn, MoCModelKittyBed modelkittybed, MoCModelKittyBed2 modelkittybed2, float f) {
        super(renderManagerIn, modelkittybed, f);
        this.addLayer(new LayerMoCKittyBed(this, modelkittybed2));
    }

    @Override
    protected void prepareModel(MoCEntityKittyBed entitykittybed, MoCModelKittyBed<MoCEntityKittyBed> model) {
        model.hasMilk = entitykittybed.getHasMilk();
        model.hasFood = entitykittybed.getHasFood();
        model.pickedUp = entitykittybed.getPickedUp();
        model.milklevel = entitykittybed.milkLevel;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityKittyBed entitykittybed) {
        return entitykittybed.getTexture();
    }

    /** The bed's sheet, drawn with the second model in the sheet's dye colour. */
    private static class LayerMoCKittyBed extends RenderLayer<MoCRenderState, MoCModelKittyBed<MoCEntityKittyBed>> {
        private final MoCRenderKittyBed mocRenderer;
        private final MoCModelKittyBed2 mocModel;

        public LayerMoCKittyBed(MoCRenderKittyBed render, MoCModelKittyBed2 model) {
            super(render);
            this.mocRenderer = render;
            this.mocModel = model;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, MoCRenderState state, float yRot, float xRot) {
            if (!(state.entity instanceof MoCEntityKittyBed entitykittybed)) {
                return;
            }
            int color = DyeColor.byId(entitykittybed.getSheetColor()).getTextureDiffuseColor();
            float red = ARGB.redFloat(color);
            float green = ARGB.greenFloat(color);
            float blue = ARGB.blueFloat(color);
            MoCModelKittyBed2<MoCEntityKittyBed> model = this.mocModel;
            Identifier texture = this.mocRenderer.getTextureLocation(entitykittybed);
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture), (pose, buffer) -> {
                model.setupAnim(state);
                model.renderToBuffer(poseFrom(pose), buffer, lightCoords, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
            });
        }
    }
}
