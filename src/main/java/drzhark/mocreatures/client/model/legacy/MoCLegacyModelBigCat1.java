/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model.legacy;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.client.model.MoCEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCLegacyModelBigCat1<T extends MoCEntityBigCat> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "legacy_big_cat1"), "main");

    private final ModelPart head;
    private final ModelPart body;

    public MoCLegacyModelBigCat1(ModelPart root) {
        super(root);
        // Grab the two children we defined in createBodyLayer():
        this.head = root.getChild("head");
        this.body = root.getChild("body");
    }

    /**
     * Exactly match old ModelRenderer(...) calls:
     *   head.addBox(-7F, -8F, -2F, 14, 14, 8);
     *   head.setRotationPoint(0F, 4F, -8F);
     *   body.addBox(-6F, -11F, -8F, 12, 10, 10);
     *   body.setRotationPoint(0F, 5F, 2F);
     *
     * In 1.20.1, we do that inside a PartDefinition tree and return a LayerDefinition.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        PartDefinition headPart = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(
                                /* x */       -7.0F,
                                /* y */       -8.0F,
                                /* z */       -2.0F,
                                /* dx */      14,
                                /* dy */      14,
                                /* dz */      8
                        ),
                // setRotationPoint(0F, 4F, -8F):
                PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        // BODY
        PartDefinition bodyPart = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(
                                /* x */       -6.0F,
                                /* y */       -11.0F,
                                /* z */       -8.0F,
                                /* dx */      12,
                                /* dy */      10,
                                /* dz */      10
                        ),
                // setRotationPoint(0F, 5F, 2F):
                PartPose.offset(0.0F, 5.0F, 2.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    public void copyPoseFrom(MoCLegacyModelBigCat2<T> model) {
        model.copyManePose(this.head, this.body);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Simply render head and body; no legs were defined:
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
