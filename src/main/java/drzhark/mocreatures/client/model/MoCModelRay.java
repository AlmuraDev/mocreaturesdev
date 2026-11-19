/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 *
 * Ported to Minecraft 1.20.1:
 *  - ModelRenderer → ModelPart
 *  - setRotationAngles(...) → setupAnim(...)
 *  - rotateAngleX/Y/Z → xRot, yRot, zRot
 *  - render(...) → renderToBuffer(...)
 *  - Initial rotations are applied via PartPose.offsetAndRotation(...)
 */

package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.aquatic.MoCEntityRay;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelRay<T extends MoCEntityRay> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "ray"),
            "main"
    );

    private final ModelPart tail;
    private final ModelPart body;
    private final ModelPart right;
    private final ModelPart left;
    private final ModelPart bodyU;
    private final ModelPart rWingA;
    private final ModelPart rWingB;
    private final ModelPart rWingC;
    private final ModelPart rWingD;
    private final ModelPart rWingE;
    private final ModelPart rWingF;
    private final ModelPart rWingG;
    private final ModelPart lWingA;
    private final ModelPart lWingB;
    private final ModelPart lWingC;
    private final ModelPart lWingD;
    private final ModelPart lWingE;
    private final ModelPart lWingF;
    private final ModelPart lWingG;
    private final ModelPart bodyTail;
    private final ModelPart lEye;
    private final ModelPart rEye;

    private boolean isMantaRay;
    private boolean attacking;

    public MoCModelRay(ModelPart root) {
        super(root);
        this.tail      = root.getChild("tail");
        this.body      = root.getChild("body");
        this.right     = root.getChild("right");
        this.left      = root.getChild("left");
        this.bodyU     = root.getChild("body_u");
        this.bodyTail  = root.getChild("body_tail");

        this.rWingA    = root.getChild("r_wing_a");
        this.rWingB    = root.getChild("r_wing_b");
        this.rWingC    = root.getChild("r_wing_c");
        this.rWingD    = root.getChild("r_wing_d");
        this.rWingE    = root.getChild("r_wing_e");
        this.rWingF    = root.getChild("r_wing_f");
        this.rWingG    = root.getChild("r_wing_g");

        this.lWingA    = root.getChild("l_wing_a");
        this.lWingB    = root.getChild("l_wing_b");
        this.lWingC    = root.getChild("l_wing_c");
        this.lWingD    = root.getChild("l_wing_d");
        this.lWingE    = root.getChild("l_wing_e");
        this.lWingF    = root.getChild("l_wing_f");
        this.lWingG    = root.getChild("l_wing_g");

        this.lEye      = root.getChild("l_eye");
        this.rEye      = root.getChild("r_eye");
    }

    /**
     * Call MoCModelRay.createBodyLayer() when registering the layer in your client setup.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // BODY (main)
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(26, 0).addBox(-4.0F, -1.0F,  0.0F, 8, 2, 11),
                PartPose.offset(0.0F, 22.0F, -5.0F)
        );

        // RIGHT “fin” detail (only for manta)
        root.addOrReplaceChild("right",
                CubeListBuilder.create()
                        .texOffs(10, 26).addBox(-0.5F, -1.0F, -4.0F, 1, 2, 4),
                PartPose.offset(-3.0F, 22.0F, -4.8F)
        );

        // LEFT “fin” detail (only for manta)
        root.addOrReplaceChild("left",
                CubeListBuilder.create()
                        .texOffs(0, 26).addBox(-0.5F, -1.0F, -4.0F, 1, 2, 4),
                PartPose.offset(3.0F, 22.0F, -4.8F)
        );

        // BODY_U (upper “hump”)
        root.addOrReplaceChild("body_u",
                CubeListBuilder.create()
                        .texOffs(0, 11).addBox(-3.0F, -1.0F,  0.0F, 6, 1,  8),
                PartPose.offset(0.0F, 21.0F, -4.0F)
        );

        // TAIL (long spike)
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(30, 15).addBox(-0.5F, -0.5F,  1.0F, 1, 1, 16),
                PartPose.offset(0.0F, 22.0F,  8.0F)
        );

        // BODY_TAIL (base of tail, rotated 1 radian around Y)
        root.addOrReplaceChild("body_tail",
                CubeListBuilder.create()
                        .texOffs(0, 20).addBox(-1.8F, -0.5F, -3.2F, 5, 1, 5),
                PartPose.offsetAndRotation(0.0F, 22.0F,  7.0F, 0.0F, 1.0F, 0.0F)
        );

        // RIGHT Wing segments (series of smaller “ribs”)
        root.addOrReplaceChild("r_wing_a",
                CubeListBuilder.create()
                        .texOffs(0,  0).addBox(-3.0F, -0.5F, -5.0F, 3, 1, 10),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_b",
                CubeListBuilder.create()
                        .texOffs(2,  2).addBox(-6.0F, -0.5F, -4.0F, 3, 1,  8),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_c",
                CubeListBuilder.create()
                        .texOffs(5,  4).addBox(-8.0F, -0.5F, -3.0F, 2, 1,  6),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_d",
                CubeListBuilder.create()
                        .texOffs(6,  5).addBox(-10.0F, -0.5F, -2.5F, 2, 1, 5),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_e",
                CubeListBuilder.create()
                        .texOffs(7,  6).addBox(-12.0F, -0.5F, -2.0F, 2, 1, 4),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_f",
                CubeListBuilder.create()
                        .texOffs(8,  7).addBox(-14.0F, -0.5F, -1.5F, 2, 1, 3),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("r_wing_g",
                CubeListBuilder.create()
                        .texOffs(9,  8).addBox(-16.0F, -0.5F, -1.0F, 2, 1, 2),
                PartPose.offset( -4.0F, 22.0F, 1.0F)
        );

        // LEFT Wing segments (mirrored)
        root.addOrReplaceChild("l_wing_a",
                CubeListBuilder.create()
                        .texOffs(0,  0).mirror().addBox(0.0F, -0.5F, -5.0F, 3, 1, 10),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_b",
                CubeListBuilder.create()
                        .texOffs(2,  2).mirror().addBox(3.0F, -0.5F, -4.0F, 3, 1,  8),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_c",
                CubeListBuilder.create()
                        .texOffs(5,  4).mirror().addBox(6.0F, -0.5F, -3.0F, 2, 1,  6),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_d",
                CubeListBuilder.create()
                        .texOffs(6,  5).mirror().addBox(8.0F, -0.5F, -2.5F, 2, 1, 5),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_e",
                CubeListBuilder.create()
                        .texOffs(7,  6).mirror().addBox(10.0F, -0.5F, -2.0F, 2, 1, 4),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_f",
                CubeListBuilder.create()
                        .texOffs(8,  7).mirror().addBox(12.0F, -0.5F, -1.5F, 2, 1, 3),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );
        root.addOrReplaceChild("l_wing_g",
                CubeListBuilder.create()
                        .texOffs(9,  8).mirror().addBox(14.0F, -0.5F, -1.0F, 2, 1, 2),
                PartPose.offset(4.0F, 22.0F, 1.0F)
        );

        // LEFT eye (only for non-manta)
        root.addOrReplaceChild("l_eye",
                CubeListBuilder.create()
                        .texOffs(0,  0).addBox(-3.0F, -2.0F,  1.0F, 1, 1, 2),
                PartPose.offset(0.0F, 21.0F, -4.0F)
        );

        // RIGHT eye (only for non-manta)
        root.addOrReplaceChild("r_eye",
                CubeListBuilder.create()
                        .texOffs(0,  3).addBox( 2.0F, -2.0F,  1.0F, 1, 1, 2),
                PartPose.offset(0.0F, 21.0F, -4.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack stack,
                               VertexConsumer builder,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {

        // Always draw these (body + “core” parts + tail)
        tail.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        bodyU.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        bodyTail.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Wings part A & B are drawn in both manta & non‐manta (they form the broad base)
        rWingA.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rWingB.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lWingA.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lWingB.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (this.isMantaRay) {
            // Draw manta‐only detail: the “fins” (right & left), and the entire wing chain C→G
            right.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            left.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            rWingC.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rWingD.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rWingE.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rWingF.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rWingG.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            lWingC.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            lWingD.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            lWingE.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            lWingF.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            lWingG.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            // Draw non‐manta “eyes” only
            rEye.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            lEye.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }

    @Override
    public void setupAnim(T entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

        // Update two flags from entity state
        this.attacking  = entity.isPoisoning();
        this.isMantaRay = entity.isMantaRay();

        // Tail wag (side‐to‐side) and optional “attack posture” (tail up)
        float wag = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
        tail.yRot = wag;
        tail.xRot = (this.attacking ? 0.5F : 0.0F);

        // Wings flap in a sequential ripple from A→B→C→...→G
        // Start with A/B linked to limbSwing:
        float rotF = Mth.cos(limbSwing * 0.6662F) * 1.5F * limbSwingAmount;
        float f6 = 20F;

        // A
        rWingA.zRot =  rotF;
        lWingA.zRot = -rotF;

        // B
        rotF += rotF / f6;
        rWingB.zRot =  rotF;
        lWingB.zRot = -rotF;

        // C
        rotF += rotF / f6;
        rWingC.zRot =  rotF;
        lWingC.zRot = -rotF;

        // D
        rotF += rotF / f6;
        rWingD.zRot =  rotF;
        lWingD.zRot = -rotF;

        // E
        rotF += rotF / f6;
        rWingE.zRot =  rotF;
        lWingE.zRot = -rotF;

        // F
        rotF += rotF / f6;
        rWingF.zRot =  rotF;
        lWingF.zRot = -rotF;

        // G (last, smallest ripple)
        rotF += rotF / f6;
        rWingG.zRot =  rotF;
        lWingG.zRot = -rotF;

        // No head or other rotations needed for this model
    }
}
