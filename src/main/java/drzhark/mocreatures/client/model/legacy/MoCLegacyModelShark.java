/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model.legacy;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.client.model.MoCEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.aquatic.MoCEntityShark;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Legacy shark model ported to 1.20.1. All ModelRenderer pieces are now ModelPart children,
 * and tail‐fin animation logic moves into setupAnim().
 */
public class MoCLegacyModelShark<T extends MoCEntityShark> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "legacy_shark"), "main"
    );

    private final ModelPart Body;
    private final ModelPart UHead;
    private final ModelPart DHead;
    private final ModelPart RHead;
    private final ModelPart LHead;
    private final ModelPart PTail;
    private final ModelPart UpperFin;
    private final ModelPart UpperTailFin;
    private final ModelPart LowerTailFin;
    private final ModelPart LeftFin;
    private final ModelPart RightFin;

    public MoCLegacyModelShark(ModelPart root) {
        super(root);
        this.Body         = root.getChild("Body");
        this.UHead        = root.getChild("UHead");
        this.DHead        = root.getChild("DHead");
        this.RHead        = root.getChild("RHead");
        this.LHead        = root.getChild("LHead");
        this.PTail        = root.getChild("PTail");
        this.UpperFin     = root.getChild("UpperFin");
        this.UpperTailFin = root.getChild("UpperTailFin");
        this.LowerTailFin = root.getChild("LowerTailFin");
        this.LeftFin      = root.getChild("LeftFin");
        this.RightFin     = root.getChild("RightFin");
    }

    /**
     * Builds a LayerDefinition matching the old ModelRenderer.addBox(...) + setRotationPoint(...) + rotateAngle settings.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // BODY
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(6, 6)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  6,
                                /* dy */  8,
                                /* dz */  18
                        ),
                PartPose.offset(-3.0F, 17.0F, -9.0F)
        );

        // UPPER HEAD
        root.addOrReplaceChild("UHead",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  5,
                                /* dy */  2,
                                /* dz */  8
                        ),
                PartPose.offsetAndRotation(-2.5F, 21.0F, -15.5F, 0.5235988F, 0.0F, 0.0F)
        );

        // LOWER HEAD
        root.addOrReplaceChild("DHead",
                CubeListBuilder.create()
                        .texOffs(44, 0)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  5,
                                /* dy */  2,
                                /* dz */  5
                        ),
                PartPose.offsetAndRotation(-2.5F, 21.5F, -12.5F, -0.261799F, 0.0F, 0.0F)
        );

        // RIGHT HEAD
        root.addOrReplaceChild("RHead",
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  1,
                                /* dy */  6,
                                /* dz */  6
                        ),
                PartPose.offsetAndRotation(-2.45F, 21.3F, -12.85F, 0.7853981F, 0.0F, 0.0F)
        );

        // LEFT HEAD
        root.addOrReplaceChild("LHead",
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  1,
                                /* dy */  6,
                                /* dz */  6
                        ),
                PartPose.offsetAndRotation(1.45F, 21.3F, -12.8F, 0.7853981F, 0.0F, 0.0F)
        );

        // POSTERIOR TAIL
        root.addOrReplaceChild("PTail",
                CubeListBuilder.create()
                        .texOffs(36, 8)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  4,
                                /* dy */  6,
                                /* dz */ 10
                        ),
                PartPose.offset(-2.0F, 18.0F, 9.0F)
        );

        // UPPER FIN
        root.addOrReplaceChild("UpperFin",
                CubeListBuilder.create()
                        .texOffs(6, 12)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  1,
                                /* dy */  4,
                                /* dz */  8
                        ),
                PartPose.offsetAndRotation(-0.5F, 17.0F, 0.0F, 0.7853981F, 0.0F, 0.0F)
        );

        // UPPER TAIL FIN
        root.addOrReplaceChild("UpperTailFin",
                CubeListBuilder.create()
                        .texOffs(6, 12)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  1,
                                /* dy */  4,
                                /* dz */  8
                        ),
                PartPose.offsetAndRotation(-0.5F, 18.0F, 17.0F, 0.5235988F, 0.0F, 0.0F)
        );

        // LOWER TAIL FIN
        root.addOrReplaceChild("LowerTailFin",
                CubeListBuilder.create()
                        .texOffs(8, 14)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  1,
                                /* dy */  4,
                                /* dz */  6
                        ),
                PartPose.offsetAndRotation(-0.5F, 21.0F, 19.0F, -0.7853981F, 0.0F, 0.0F)
        );

        // LEFT FIN
        root.addOrReplaceChild("LeftFin",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  8,
                                /* dy */  1,
                                /* dz */  4
                        ),
                PartPose.offsetAndRotation(3.0F, 24.0F, -4.0F, 0.0F, -0.5235988F, 0.5235988F)
        );

        // RIGHT FIN
        root.addOrReplaceChild("RightFin",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(
                                /* x */   0.0F,
                                /* y */   0.0F,
                                /* z */   0.0F,
                                /* dx */  8,
                                /* dy */  1,
                                /* dz */  4
                        ),
                PartPose.offsetAndRotation(-9.0F, 27.5F, 0.0F, 0.0F,  0.5235988F, -0.5235988F)
        );

        // Use a 64×32 texture sheet (adjust if your PNG differs)
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        // Animate tail fins by yaw movement
        float yawMotion = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.UpperTailFin.yRot = yawMotion;
        this.LowerTailFin.yRot = yawMotion;

        // (No other dynamic rotations in the original code)
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
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.PTail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.DHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UpperFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UpperTailFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LowerTailFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
