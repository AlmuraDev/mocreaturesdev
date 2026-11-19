/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 *
 * NOTE: In 1.20.1, ModelRenderer → ModelPart, and
 *       setRotationAngles(...) → setupAnim(...).
 * 
 */

package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityRaccoon;
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
public class MoCModelRaccoon<T extends MoCEntityRaccoon> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "raccoon"),
            "main"
    );

    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart leftSideburn;
    private final ModelPart rightSideburn;
    private final ModelPart neck;
    private final ModelPart body;
    private final ModelPart tailA;
    private final ModelPart tailB;
    private final ModelPart rightFrontLegA;
    private final ModelPart rightFrontLegB;
    private final ModelPart rightFrontFoot;
    private final ModelPart leftFrontLegA;
    private final ModelPart leftFrontLegB;
    private final ModelPart leftFrontFoot;
    private final ModelPart rightRearLegA;
    private final ModelPart rightRearLegB;
    private final ModelPart rightRearFoot;
    private final ModelPart leftRearLegA;
    private final ModelPart leftRearLegB;
    private final ModelPart leftRearFoot;

    private final float radianF = 57.29578F;

    public MoCModelRaccoon(ModelPart root) {
        super(root);
        this.head             = root.getChild("head");
        this.snout            = root.getChild("snout");
        this.rightEar         = root.getChild("right_ear");
        this.leftEar          = root.getChild("left_ear");
        this.leftSideburn     = head.getChild("left_sideburn");
        this.rightSideburn    = head.getChild("right_sideburn");
        this.neck             = root.getChild("neck");
        this.body             = root.getChild("body");
        this.tailA            = root.getChild("tail_a");
        this.tailB            = root.getChild("tail_b");
        this.rightFrontLegA   = root.getChild("right_front_leg_a");
        this.rightFrontLegB   = root.getChild("right_front_leg_b");
        this.rightFrontFoot   = root.getChild("right_front_foot");
        this.leftFrontLegA    = root.getChild("left_front_leg_a");
        this.leftFrontLegB    = root.getChild("left_front_leg_b");
        this.leftFrontFoot    = root.getChild("left_front_foot");
        this.rightRearLegA    = root.getChild("right_rear_leg_a");
        this.rightRearLegB    = root.getChild("right_rear_leg_b");
        this.rightRearFoot    = root.getChild("right_rear_foot");
        this.leftRearLegA     = root.getChild("left_rear_leg_a");
        this.leftRearLegB     = root.getChild("left_rear_leg_b");
        this.leftRearFoot     = root.getChild("left_rear_foot");
    }

    /**
     * LayerDefinition builder that must match the names used in the constructor above.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(38, 21).addBox(-4.0F, -3.5F, -6.5F, 8, 6, 5),
                PartPose.offset(0.0F, 17.0F, -4.0F));

        head.addOrReplaceChild("right_sideburn",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 3, 4, 4),
                PartPose.offsetAndRotation(-2.5F, 0.5F, -3.2F, 0.0F, -0.5235988F, 0.0F));

        head.addOrReplaceChild("left_sideburn",
                CubeListBuilder.create()
                        .texOffs(0, 40).addBox(0.0F, -2.0F, -2.0F, 3, 4, 4),
                PartPose.offsetAndRotation(2.5F, 0.5F, -3.2F, 0.0F, 0.5235988F, 0.0F));

        // SNOUT
        root.addOrReplaceChild("snout",
                CubeListBuilder.create()
                        .texOffs(24, 25).addBox(-1.5F, -0.5F, -10.5F, 3, 3, 4),
                PartPose.offset(0.0F, 17.0F, -4.0F));

        // EARS
        root.addOrReplaceChild("right_ear",
                CubeListBuilder.create()
                        .texOffs(24, 22).addBox(-4.0F, -5.5F, -3.5F, 3, 2, 1),
                PartPose.offset(0.0F, 17.0F, -4.0F));

        root.addOrReplaceChild("left_ear",
                CubeListBuilder.create()
                        .texOffs(24, 18).addBox(1.0F, -5.5F, -3.5F, 3, 2, 1),
                PartPose.offset(0.0F, 17.0F, -4.0F));

        // NECK
        root.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(46, 4).addBox(-2.5F, -2.0F, -3.0F, 5, 4, 3),
                PartPose.offsetAndRotation(0.0F, 17.0F, -4.0F, -0.4461433F, 0.0F, 0.0F));

        // BODY
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6, 6, 12),
                PartPose.offset(0.0F, 15.0F, -2.0F));

        // TAIL
        root.addOrReplaceChild("tail_a",
                CubeListBuilder.create()
                        .texOffs(0, 3).addBox(-1.5F, -6.0F, -1.5F, 3, 6, 3),
                PartPose.offsetAndRotation(0.0F, 16.5F, 6.5F, -2.024582F, 0.0F, 0.0F));

        root.addOrReplaceChild("tail_b",
                CubeListBuilder.create()
                        .texOffs(24, 3).addBox(-1.5F, -11.0F, 0.3F, 3, 6, 3),
                PartPose.offsetAndRotation(0.0F, 16.5F, 6.5F, -1.689974F, 0.0F, 0.0F));

        // RIGHT FRONT LEG
        root.addOrReplaceChild("right_front_leg_a",
                CubeListBuilder.create()
                        .texOffs(36, 0).addBox(-4.0F, -1.0F, -1.0F, 2, 5, 3),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, 0.5205006F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_front_leg_b",
                CubeListBuilder.create()
                        .texOffs(46, 11).addBox(-3.5F, 1.0F, 2.0F, 2, 4, 2),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, -0.3717861F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_front_foot",
                CubeListBuilder.create()
                        .texOffs(46, 0).addBox(-4.0F, 5.0F, -1.0F, 3, 1, 3),
                PartPose.offset(0.0F, 18.0F, -4.0F));

        // LEFT FRONT LEG
        root.addOrReplaceChild("left_front_leg_a",
                CubeListBuilder.create()
                        .texOffs(36, 8).addBox(2.0F, -1.0F, -1.0F, 2, 5, 3),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, 0.5205006F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_front_leg_b",
                CubeListBuilder.create()
                        .texOffs(54, 11).addBox(1.5F, 1.0F, 2.0F, 2, 4, 2),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, -0.3717861F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_front_foot",
                CubeListBuilder.create()
                        .texOffs(46, 0).addBox(1.0F, 5.0F, -1.0F, 3, 1, 3),
                PartPose.offset(0.0F, 18.0F, -4.0F));

        // RIGHT REAR LEG
        root.addOrReplaceChild("right_rear_leg_a",
                CubeListBuilder.create()
                        .texOffs(12, 18).addBox(-5.0F, -2.0F, -3.0F, 2, 5, 4),
                PartPose.offsetAndRotation(0.0F, 18.0F, 4.0F, 0.9294653F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_rear_leg_b",
                CubeListBuilder.create()
                        .texOffs(0, 27).addBox(-4.5F, 2.0F, -5.0F, 2, 2, 3),
                PartPose.offsetAndRotation(0.0F, 18.0F, 4.0F, 0.9294653F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_rear_foot",
                CubeListBuilder.create()
                        .texOffs(46, 0).addBox(-5.0F, 5.0F, -2.0F, 3, 1, 3),
                PartPose.offset(0.0F, 18.0F, 4.0F));

        // LEFT REAR LEG
        root.addOrReplaceChild("left_rear_leg_a",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(3.0F, -2.0F, -3.0F, 2, 5, 4),
                PartPose.offsetAndRotation(0.0F, 18.0F, 4.0F, 0.9294653F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_rear_leg_b",
                CubeListBuilder.create()
                        .texOffs(10, 27).addBox(2.5F, 2.0F, -5.0F, 2, 2, 3),
                PartPose.offsetAndRotation(0.0F, 18.0F, 4.0F, 0.9294653F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_rear_foot",
                CubeListBuilder.create()
                        .texOffs(46, 0).addBox(2.0F, 5.0F, -2.0F, 3, 1, 3),
                PartPose.offset(0.0F, 18.0F, 4.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        snout.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        neck.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tailA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tailB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        rightFrontLegA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightFrontLegB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightFrontFoot.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        leftFrontLegA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftFrontLegB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftFrontFoot.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        rightRearLegA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightRearLegB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightRearFoot.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        leftRearLegA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftRearLegB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftRearFoot.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * In 1.20.1 this replaces setRotationAngles. limbSwing / limbSwingAmount / ageInTicks / netHeadYaw / headPitch.
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot   = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot   = headPitch * ((float)Math.PI / 180F);
        snout.yRot  = head.yRot;
        snout.xRot  = head.xRot;
        rightEar.xRot = head.xRot;
        rightEar.yRot = head.yRot;
        leftEar.xRot  = head.xRot;
        leftEar.yRot  = head.yRot;

        float RLegXRot = Mth.cos(limbSwing + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing)            * 0.8F * limbSwingAmount;

        // Front legs: offset + some base rotation
        this.rightFrontLegA.xRot = (30F / radianF) + RLegXRot;
        this.leftFrontLegA.xRot  = (30F / radianF) + LLegXRot;
        this.rightRearLegA.xRot  = (53F / radianF) + LLegXRot;
        this.leftRearLegA.xRot   = (53F / radianF) + RLegXRot;

        this.rightFrontLegB.xRot = (-21F / radianF) + RLegXRot;
        this.rightFrontFoot.xRot = RLegXRot;
        this.leftFrontLegB.xRot  = (-21F / radianF) + LLegXRot;
        this.leftFrontFoot.xRot  = LLegXRot;

        this.rightRearLegB.xRot  = (53F / radianF) + LLegXRot;
        this.rightRearFoot.xRot  = LLegXRot;
        this.leftRearLegB.xRot   = (53F / radianF) + RLegXRot;
        this.leftRearFoot.xRot   = RLegXRot;

        this.tailA.yRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
        this.tailB.yRot = this.tailA.yRot;
    }
}
