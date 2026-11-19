/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model.legacy;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.client.model.MoCEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntityScorpion;
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
 * Legacy scorpion model ported to 1.20.1.  All ModelRenderer pieces are now ModelPart children,
 * and setRotationAngles(...) logic has been moved into setupAnim(...).
 */
public class MoCLegacyModelScorpion<T extends MoCEntityScorpion> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "legacy_scorpion"), "main"
    );

    // Flags controlled externally (e.g. in renderer):
    public boolean attacking;
    public boolean isSwinging;

    // Model parts:
    private final ModelPart head;
    private final ModelPart rearEnd;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg5;
    private final ModelPart leg6;
    private final ModelPart leg7;
    private final ModelPart leg8;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail7;
    private final ModelPart rArm;
    private final ModelPart lArm;
    private final ModelPart rHand;
    private final ModelPart lHand;
    private final ModelPart rHandB;
    private final ModelPart lHandB;

    public MoCLegacyModelScorpion(ModelPart root) {
        super(root);
        this.head    = root.getChild("Head");
        this.rearEnd = root.getChild("RearEnd");
        this.leg1    = root.getChild("Leg1");
        this.leg2    = root.getChild("Leg2");
        this.leg3    = root.getChild("Leg3");
        this.leg4    = root.getChild("Leg4");
        this.leg5    = root.getChild("Leg5");
        this.leg6    = root.getChild("Leg6");
        this.leg7    = root.getChild("Leg7");
        this.leg8    = root.getChild("Leg8");
        this.tail1   = root.getChild("Tail1");
        this.tail2   = root.getChild("Tail2");
        this.tail3   = root.getChild("Tail3");
        this.tail4   = root.getChild("Tail4");
        this.tail5   = root.getChild("Tail5");
        this.tail7   = root.getChild("Tail7");
        this.rArm    = root.getChild("RArm");
        this.lArm    = root.getChild("LArm");
        this.rHand   = root.getChild("RHand");
        this.lHand   = root.getChild("LHand");
        this.rHandB  = root.getChild("RHandB");
        this.lHandB  = root.getChild("LHandB");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(38, 19)
                        .addBox(-4.0F, -3.0F, -6.0F, 8, 4, 5),
                PartPose.offset(0.0F, 20.0F, -5.0F)
        );

        // REAR END
        root.addOrReplaceChild("RearEnd",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-5.0F, -3.0F, -3.0F, 10, 4, 17),
                PartPose.offsetAndRotation(0.0F, 20.0F, -3.0F, 0.13963F, 0.0F, 0.0F)
        );

        // LEGS
        root.addOrReplaceChild("Leg8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 6)
                        .addBox(-1.0F, -1.0F,  1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(4.0F, 20.0F, -2.0F, 0.0F, 0.57596F, 0.19199F)
        );
        root.addOrReplaceChild("Leg6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 6)
                        .addBox(-1.0F, -1.0F,  1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(4.0F, 20.0F,  1.0F, 0.0F, 0.27925F, 0.19199F)
        );
        root.addOrReplaceChild("Leg4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 6)
                        .addBox(-1.0F, -1.0F,  1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(4.0F, 20.0F,  3.0F, 0.0F, -0.27925F, 0.19199F)
        );
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 6)
                        .addBox(-1.0F, -1.0F,  1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(4.0F, 20.0F,  6.0F, 0.0F, -0.57596F, 0.19199F)
        );
        root.addOrReplaceChild("Leg7",
                CubeListBuilder.create()
                        .texOffs(20, 6)
                        .addBox(-13.0F, -1.0F, 1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(-4.0F, 20.0F, -2.0F, 0.0F, -0.57596F, -0.19199F)
        );
        root.addOrReplaceChild("Leg5",
                CubeListBuilder.create()
                        .texOffs(20, 6)
                        .addBox(-13.0F, -1.0F, 1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(-4.0F, 20.0F,  1.0F, 0.0F, -0.27925F, -0.19199F)
        );
        root.addOrReplaceChild("Leg3",
                CubeListBuilder.create()
                        .texOffs(20, 6)
                        .addBox(-13.0F, -1.0F, 1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(-4.0F, 20.0F,  3.0F, 0.0F,  0.27925F, -0.19199F)
        );
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create()
                        .texOffs(20, 6)
                        .addBox(-13.0F, -1.0F, 1.0F, 14, 2, 2),
                PartPose.offsetAndRotation(-4.0F, 20.0F,  6.0F, 0.0F,  0.57596F, -0.19199F)
        );

        // TAIL SEGMENTS
        root.addOrReplaceChild("Tail1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -8.0F,  8.0F,  6, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -12.0F, 10.0F, 6, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-2.0F, -14.0F,  8.0F, 4, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );
        root.addOrReplaceChild("Tail4",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-2.0F, -17.0F,  6.0F, 4, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );
        root.addOrReplaceChild("Tail5",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-2.0F, -19.0F,  3.0F, 4, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );
        root.addOrReplaceChild("Tail7",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-1.0F, -18.0F,  0.0F, 2, 4, 4),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );

        // ARMS / CLAWS
        root.addOrReplaceChild("RArm",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-11.0F, -1.0F, -1.0F, 11, 2, 4),
                PartPose.offsetAndRotation(-4.0F, 20.0F, -9.0F, 0.0F, -0.27925F, 0.0F)
        );
        root.addOrReplaceChild("LArm",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 0)
                        .addBox(-1.0F, -1.0F, -1.0F, 12, 2, 4),
                PartPose.offsetAndRotation(4.0F, 20.0F, -9.0F, 0.0F,  0.27925F, 0.0F)
        );
        root.addOrReplaceChild("RHand",
                CubeListBuilder.create()
                        .texOffs(44, 9)
                        .addBox(-11.0F, -1.0F, -9.0F, 2, 2, 8),
                PartPose.offsetAndRotation(-4.0F, 20.0F, -9.0F, 0.0F, -0.27925F, 0.0F)
        );
        root.addOrReplaceChild("LHand",
                CubeListBuilder.create()
                        .texOffs(44, 9)
                        .addBox(9.0F, -1.0F, -9.0F, 2, 2, 8),
                PartPose.offsetAndRotation(4.0F, 20.0F, -9.0F, 0.0F,  0.27925F, 0.0F)
        );
        root.addOrReplaceChild("RHandB",
                CubeListBuilder.create()
                        .texOffs(44, 9)
                        .addBox(-8.0F, -1.0F, -9.0F, 2, 2, 8),
                PartPose.offsetAndRotation(-4.0F, 20.0F, -9.0F, 0.0F, -0.27925F, 0.0F)
        );
        root.addOrReplaceChild("LHandB",
                CubeListBuilder.create()
                        .texOffs(44, 9)
                        .addBox(6.0F, -1.0F, -9.0F, 2, 2, 8),
                PartPose.offsetAndRotation(4.0F, 20.0F, -9.0F, 0.0F,  0.27925F, 0.0F)
        );

        // Texture size (choose the same width/height your original texture used):
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

        // Base leg Z rotations:
        final float baseZ    = 0.7853982F;      // 45 degrees
        final float z74      = baseZ * 0.74F;
        this.leg1.zRot = -baseZ;
        this.leg2.zRot =  baseZ;
        this.leg3.zRot = -z74;
        this.leg4.zRot =  z74;
        this.leg5.zRot = -z74;
        this.leg6.zRot =  z74;
        this.leg7.zRot = -baseZ;
        this.leg8.zRot =  baseZ;

        // Base leg Y rotations:
        final float baseY    = 0.3926991F;      // 22.5 degrees
        final float neg     = -0.0F;
        this.leg1.yRot = baseY * 2.0F + neg;
        this.leg2.yRot = -baseY * 2.0F - neg;
        this.leg3.yRot = baseY + neg;
        this.leg4.yRot = -baseY - neg;
        this.leg5.yRot = -baseY + neg;
        this.leg6.yRot = baseY - neg;
        this.leg7.yRot = -baseY * 2.0F + neg;
        this.leg8.yRot = baseY * 2.0F - neg;

        // Leg swinging offsets:
        float swingTimes2 = limbSwing * 0.6662F * 2.0F;
        float f9  = -Mth.cos(swingTimes2 + 0.0F) * 0.4F * limbSwingAmount;
        float f10 = -Mth.cos(swingTimes2 + (float)Math.PI) * 0.4F * limbSwingAmount;
        float f11 = -Mth.cos(swingTimes2 + (float)(Math.PI / 2)) * 0.4F * limbSwingAmount;
        float f12 = -Mth.cos(swingTimes2 + (float)(3 * Math.PI / 2)) * 0.4F * limbSwingAmount;
        float f13 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float f14 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f15 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)(Math.PI / 2)) * 0.4F) * limbSwingAmount;
        float f16 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)(3 * Math.PI / 2)) * 0.4F) * limbSwingAmount;

        this.leg1.yRot += f9;
        this.leg2.yRot -= f9;
        this.leg3.yRot += f10;
        this.leg4.yRot -= f10;
        this.leg5.yRot += f11;
        this.leg6.yRot -= f11;
        this.leg7.yRot += f12;
        this.leg8.yRot -= f12;

        this.leg1.zRot += f13;
        this.leg2.zRot -= f13;
        this.leg3.zRot += f14;
        this.leg4.zRot -= f14;
        this.leg5.zRot += f15;
        this.leg6.zRot -= f15;
        this.leg7.zRot += f16;
        this.leg8.zRot -= f16;

        // Head rotations:
        this.head.yRot = netHeadYaw  * ((float)Math.PI / 180F);
        this.head.xRot = headPitch   * ((float)Math.PI / 180F);

        // Arm/claw rotations locked to Y:
        this.rArm.yRot = -0.27925F;
        this.lArm.yRot =  0.27925F;
        this.rHand.yRot = -0.27925F;
        this.lHand.yRot =  0.27925F;
        this.rHandB.yRot = -0.27925F;
        this.lHandB.yRot =  0.27925F;

        // Tail segment rotations will be adjusted below.

        if (this.attacking) {
            this.rearEnd.xRot = 0.19199F;
            this.tail1.y = 20.0F; this.tail1.z = -2.0F;
            this.tail2.y = 20.0F; this.tail2.z = -6.0F;
            this.tail3.y = 20.0F; this.tail3.z = -8.0F;
            this.tail4.y = 21.0F; this.tail4.z = -10.0F;
            this.tail5.y = 24.0F; this.tail5.z = -11.0F;
            this.tail7.y = 25.0F; this.tail7.z = -12.0F;
        } else {
            this.rearEnd.xRot = 0.13963F;
            this.tail1.y = 20.0F; this.tail1.z =  0.0F;
            this.tail2.y = 20.0F; this.tail2.z =  0.0F;
            this.tail3.y = 20.0F; this.tail3.z =  0.0F;
            this.tail4.y = 20.0F; this.tail4.z =  0.0F;
            this.tail5.y = 20.0F; this.tail5.z =  0.0F;
            this.tail7.y = 20.0F; this.tail7.z =  0.0F;
        }

        if (this.isSwinging) {
            this.rArm.zRot = 0.0F;
            this.lArm.zRot = 0.0F;
            this.rHand.zRot = 0.0F;
            this.lHand.zRot = 0.0F;
            this.rHandB.zRot = 0.0F;
            this.lHandB.zRot = 0.0F;

            float prog = this.attackTime;
            float f18 = prog;
            if (f18 >= 0.6F) {
                f18 = 0.6F - (prog - 0.6F);
            }

            this.rArm.yRot   = -0.27925F - f18;
            this.rHand.yRot  = -0.27925F - f18;
            this.rHandB.yRot = -0.27925F - f18;
            this.lArm.yRot   =  0.27925F + f18;
            this.lHand.yRot  =  0.27925F + f18;
            this.lHandB.yRot =  0.27925F + f18;
        } else {
            float mov = Mth.cos(limbSwing * 0.4F) * 0.3F * limbSwingAmount;
            this.tail1.xRot = mov * 0.8F;
            this.tail2.xRot = mov;
            this.tail3.xRot = mov;
            this.tail4.xRot = mov;
            this.tail5.xRot = mov;
            this.tail7.xRot = mov;

            this.rArm.zRot   = mov;
            this.rHand.zRot  = mov;
            this.rHandB.zRot = mov;
            this.lArm.zRot   = -mov;
            this.lHand.zRot  = -mov;
            this.lHandB.zRot = -mov;
        }
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
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rearEnd.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg8.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg6.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg7.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail7.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rHandB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lHandB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
