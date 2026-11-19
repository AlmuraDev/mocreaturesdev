/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.model.EntityModel;

@OnlyIn(Dist.CLIENT)
public class MoCModelKittyBed2<T extends MoCEntityKittyBed> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "kitty_bed2"), "main"
    );

    private final ModelPart sheet;

    public MoCModelKittyBed2(ModelPart root) {
        super(root);
        this.sheet = root.getChild("sheet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh     = new MeshDefinition();
        PartDefinition root     = mesh.getRoot();

        // Sheet panel: size 16×3×14, pivot at (-8,21,-7)
        root.addOrReplaceChild("sheet",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(0F, 0F, 0F, 16, 3, 14),
                PartPose.offset(-8F, 21F, -7F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        // No animations for the sheet
    }

    @Override
    public void renderToBuffer(PoseStack poseStack,
                               VertexConsumer buffer,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {
        this.sheet.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
