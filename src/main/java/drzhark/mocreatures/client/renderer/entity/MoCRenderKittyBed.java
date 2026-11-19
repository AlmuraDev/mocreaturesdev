/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelKittyBed;
import drzhark.mocreatures.client.model.MoCModelKittyBed2;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class MoCRenderKittyBed extends MoCLivingRenderer<MoCEntityKittyBed, MoCModelKittyBed<MoCEntityKittyBed>> {

    public MoCModelKittyBed kittybed;
    private int mycolor;

    public MoCRenderKittyBed(EntityRendererProvider.Context renderManagerIn, MoCModelKittyBed modelkittybed, MoCModelKittyBed2 modelkittybed2, float f) {
        super(renderManagerIn, modelkittybed, f);
        this.kittybed = modelkittybed;
        this.addLayer(new LayerMoCKittyBed(this, modelkittybed2));
    }

    @Override
    protected void scale(MoCEntityKittyBed entitykittybed, PoseStack poseStack, float f) {
        this.mycolor = entitykittybed.getSheetColor();
        this.kittybed.hasMilk = entitykittybed.getHasMilk();
        this.kittybed.hasFood = entitykittybed.getHasFood();
        this.kittybed.pickedUp = entitykittybed.getPickedUp();
        this.kittybed.milklevel = entitykittybed.milkLevel;
    }

    @Override
    public Identifier getTextureLocation(MoCEntityKittyBed entitykittybed) {
        return entitykittybed.getTexture();
    }

    private static class LayerMoCKittyBed extends RenderLayer<MoCEntityKittyBed, MoCModelKittyBed<MoCEntityKittyBed>> {

        private final MoCRenderKittyBed mocRenderer;
        private final MoCModelKittyBed2 mocModel;

        public LayerMoCKittyBed(MoCRenderKittyBed render, MoCModelKittyBed2 model) {
            super(render);
            this.mocRenderer = render;
            this.mocModel = model;
        }

        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, MoCEntityKittyBed entitykittybed, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            int j = this.mocRenderer.mycolor;
            
            float[] rgb = DyeColor.byId(j).getTextureDiffuseColors();
            
            // Render colored overlay model
            this.getParentModel().copyPropertiesTo(this.mocModel);
            this.mocModel.renderToBuffer(poseStack, buffer.getBuffer(
                    RenderTypes.entityCutoutNoCull(this.getTextureLocation(entitykittybed))),
                    packedLightIn, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
        }
    }
}
