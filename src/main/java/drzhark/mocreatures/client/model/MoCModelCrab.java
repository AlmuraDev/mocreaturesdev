package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityCrab;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelCrab<T extends MoCEntityCrab> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "crab"), "main");

    private final ModelPart shell;
    private final ModelPart shell_right;
    private final ModelPart shell_left;
    private final ModelPart shell_back;
    private final ModelPart left_eye;
    private final ModelPart left_eye_base;
    private final ModelPart right_eye_base;
    private final ModelPart right_eye;

    private final ModelPart right_arm_a;
    private final ModelPart right_arm_b;
    private final ModelPart right_arm_c;
    private final ModelPart right_arm_d;

    private final ModelPart left_arm_a;
    private final ModelPart left_arm_b;
    private final ModelPart left_arm_c;
    private final ModelPart left_arm_d;

    private final ModelPart right_leg1_a;
    private final ModelPart right_leg1_b;
    private final ModelPart right_leg2_a;
    private final ModelPart right_leg2_b;
    private final ModelPart right_leg3_a;
    private final ModelPart right_leg3_b;
    private final ModelPart right_leg4_a;
    private final ModelPart right_leg4_b;
    private final ModelPart right_leg4_c;

    private final ModelPart left_leg1_a;
    private final ModelPart left_leg1_b;
    private final ModelPart left_leg2_a;
    private final ModelPart left_leg2_b;
    private final ModelPart left_leg3_a;
    private final ModelPart left_leg3_b;
    private final ModelPart left_leg4_a;
    private final ModelPart left_leg4_b;
    private final ModelPart left_leg4_c;

    public MoCModelCrab(ModelPart root) {
        super(root);
        this.shell          = root.getChild("shell");
        this.shell_right    = root.getChild("shell_right");
        this.shell_left     = root.getChild("shell_left");
        this.shell_back     = root.getChild("shell_back");
        this.left_eye       = root.getChild("left_eye");
        this.left_eye_base  = root.getChild("left_eye_base");
        this.right_eye_base = root.getChild("right_eye_base");
        this.right_eye      = root.getChild("right_eye");

        this.right_arm_a    = root.getChild("right_arm_a");
        this.right_arm_b    = this.right_arm_a.getChild("right_arm_b");
        this.right_arm_c    = this.right_arm_b.getChild("right_arm_c");
        this.right_arm_d    = this.right_arm_b.getChild("right_arm_d");

        this.left_arm_a     = root.getChild("left_arm_a");
        this.left_arm_b     = this.left_arm_a.getChild("left_arm_b");
        this.left_arm_c     = this.left_arm_b.getChild("left_arm_c");
        this.left_arm_d     = this.left_arm_b.getChild("left_arm_d");

        this.right_leg1_a   = root.getChild("right_leg1_a");
        this.right_leg1_b   = this.right_leg1_a.getChild("right_leg1_b");
        this.right_leg2_a   = root.getChild("right_leg2_a");
        this.right_leg2_b   = this.right_leg2_a.getChild("right_leg2_b");
        this.right_leg3_a   = root.getChild("right_leg3_a");
        this.right_leg3_b   = this.right_leg3_a.getChild("right_leg3_b");
        this.right_leg4_a   = root.getChild("right_leg4_a");
        this.right_leg4_b   = this.right_leg4_a.getChild("right_leg4_b");
        this.right_leg4_c   = this.right_leg4_b.getChild("right_leg4_c");

        this.left_leg1_a    = root.getChild("left_leg1_a");
        this.left_leg1_b    = this.left_leg1_a.getChild("left_leg1_b");
        this.left_leg2_a    = root.getChild("left_leg2_a");
        this.left_leg2_b    = this.left_leg2_a.getChild("left_leg2_b");
        this.left_leg3_a    = root.getChild("left_leg3_a");
        this.left_leg3_b    = this.left_leg3_a.getChild("left_leg3_b");
        this.left_leg4_a    = root.getChild("left_leg4_a");
        this.left_leg4_b    = this.left_leg4_a.getChild("left_leg4_b");
        this.left_leg4_c    = this.left_leg4_b.getChild("left_leg4_c");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Shell (main carapace)
        root.addOrReplaceChild("shell",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5F, 0F, -4F, 10, 4, 8),
                PartPose.offset(0F, 16F, 0F)
        );

        // Shell Right Flare
        root.addOrReplaceChild("shell_right",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(4.6F, -2F, -4F, 3, 3, 8),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0.418879F)
        );

        // Shell Left Flare
        root.addOrReplaceChild("shell_left",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-7.6F, -2F, -4F, 3, 3, 8),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, -0.418879F)
        );

        // Shell Back Rim
        root.addOrReplaceChild("shell_back",
                CubeListBuilder.create()
                        .texOffs(10, 42)
                        .addBox(-5F, -1.6F, 3.6F, 10, 3, 3),
                PartPose.offsetAndRotation(0F, 16F, 0F, -0.418879F, 0F, 0F)
        );

        // Left Eye (upper stalk)
        root.addOrReplaceChild("left_eye",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(1F, -2F, -4.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0.1745329F)
        );

        // Left Eye Base
        root.addOrReplaceChild("left_eye_base",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(1F, 1F, -5F, 2, 3, 1),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0.2094395F)
        );

        // Right Eye Base
        root.addOrReplaceChild("right_eye_base",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-3F, 1F, -5F, 2, 3, 1),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, -0.2094395F)
        );

        // Right Eye (upper stalk)
        root.addOrReplaceChild("right_eye",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2F, -2F, -4.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, -0.1745329F)
        );

        // Right Arm A (first segment of right claw)
        PartDefinition rightArmA = root.addOrReplaceChild("right_arm_a",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-4F, -1F, -1F, 4, 2, 2),
                PartPose.offsetAndRotation(-4F, 19F, -4F, 0F, -0.5235988F, 0F)
        );
        // Right Arm B (second segment)
        PartDefinition rightArmB = rightArmA.addOrReplaceChild("right_arm_b",
                CubeListBuilder.create()
                        .texOffs(22, 12)
                        .addBox(-4F, -1.5F, -1F, 4, 3, 2),
                PartPose.offsetAndRotation(-4F, 0F, 0F, 0F, -2.094395F, 0F)
        );
        // Right Arm C (pincer claw)
        rightArmB.addOrReplaceChild("right_arm_c",
                CubeListBuilder.create()
                        .texOffs(22, 17)
                        .addBox(-3F, -1.5F, -1F, 3, 1, 2),
                PartPose.offset( -4F, 0F, 0F )
        );
        // Right Arm D (tip of pincer)
        rightArmB.addOrReplaceChild("right_arm_d",
                CubeListBuilder.create()
                        .texOffs(16, 12)
                        .addBox(-2F, 0.5F, -0.5F, 2, 1, 1),
                PartPose.offset( -4F, 0F, 0F )
        );

        // Left Arm A
        PartDefinition leftArmA = root.addOrReplaceChild("left_arm_a",
                CubeListBuilder.create()
                        .texOffs(0, 38)
                        .addBox(0F, -1F, -1F, 4, 2, 2),
                PartPose.offsetAndRotation(4F, 19F, -4F, 0F, 0.5235988F, 0F)
        );
        // Left Arm B
        PartDefinition leftArmB = leftArmA.addOrReplaceChild("left_arm_b",
                CubeListBuilder.create()
                        .texOffs(22, 20)
                        .addBox(0F, -1.5F, -1F, 4, 3, 2),
                PartPose.offsetAndRotation(4F, 0F, 0F, 0F, 2.094395F, 0F)
        );
        // Left Arm C
        leftArmB.addOrReplaceChild("left_arm_c",
                CubeListBuilder.create()
                        .texOffs(22, 25)
                        .addBox(0F, -1.5F, -1F, 3, 1, 2),
                PartPose.offset(4F, 0F, 0F)
        );
        // Left Arm D
        leftArmB.addOrReplaceChild("left_arm_d",
                CubeListBuilder.create()
                        .texOffs(16, 23)
                        .addBox(0F, 0.5F, -0.5F, 2, 1, 1),
                PartPose.offset(4F, 0F, 0F)
        );

        // Right Leg 1A (closest to body)
        PartDefinition rightLeg1A = root.addOrReplaceChild("right_leg1_a",
                CubeListBuilder.create()
                        .texOffs(0, 42)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-5F, 19.5F, -2.5F, 0F, -0.1745329F, -0.418879F)
        );
        // Right Leg 1B
        rightLeg1A.addOrReplaceChild("right_leg1_b",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-4F, 0F, 0F, 0F, 0F, -0.5235988F)
        );

        // Right Leg 2A
        PartDefinition rightLeg2A = root.addOrReplaceChild("right_leg2_a",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-5F, 19.5F, 0F, 0F, 0.0872665F, -0.418879F)
        );
        // Right Leg 2B
        rightLeg2A.addOrReplaceChild("right_leg2_b",
                CubeListBuilder.create()
                        .texOffs(0, 50)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-4F, 0F, 0F, 0F, 0F, -0.5235988F)
        );

        // Right Leg 3A
        PartDefinition rightLeg3A = root.addOrReplaceChild("right_leg3_a",
                CubeListBuilder.create()
                        .texOffs(0, 46)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-5F, 19.5F, 2.5F, 0F, 0.6981317F, -0.418879F)
        );
        // Right Leg 3B
        rightLeg3A.addOrReplaceChild("right_leg3_b",
                CubeListBuilder.create()
                        .texOffs(0, 52)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-4F, 0F, 0F, 0F, 0F, -0.5235988F)
        );

        // Right Leg 4A
        PartDefinition rightLeg4A = root.addOrReplaceChild("right_leg4_a",
                CubeListBuilder.create()
                        .texOffs(12, 34)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(-3F, 19.5F, 3.5F, 0F, 0.6108652F, -0.418879F)
        );
        // Right Leg 4B
        PartDefinition rightLeg4B = rightLeg4A.addOrReplaceChild("right_leg4_b",
                CubeListBuilder.create()
                        .texOffs(12, 36)
                        .addBox(-3F, -0.5F, -1F, 3, 1, 2),
                PartPose.offsetAndRotation(-4F, 0F, 0F, 0F, 1.308997F, -0.418879F)
        );
        // Right Leg 4C
        rightLeg4B.addOrReplaceChild("right_leg4_c",
                CubeListBuilder.create()
                        .texOffs(12, 39)
                        .addBox(-3F, -0.5F, -1F, 3, 1, 2),
                PartPose.offsetAndRotation(-3F, 0F, 0F, 0F, 0.8726646F, -0.418879F)
        );

        // Left Leg 1A
        PartDefinition leftLeg1A = root.addOrReplaceChild("left_leg1_a",
                CubeListBuilder.create()
                        .texOffs(0, 54)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(5F, 19.5F, -2.5F, 0F, 0.1745329F, 0.418879F)
        );
        // Left Leg 1B
        leftLeg1A.addOrReplaceChild("left_leg1_b",
                CubeListBuilder.create()
                        .texOffs(0, 56)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(4F, 0F, 0F, 0F, 0F, 0.5235988F)
        );

        // Left Leg 2A
        PartDefinition leftLeg2A = root.addOrReplaceChild("left_leg2_a",
                CubeListBuilder.create()
                        .texOffs(0, 62)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(5F, 19.5F, 0F, 0F, -0.0872665F, 0.418879F)
        );
        // Left Leg 2B
        leftLeg2A.addOrReplaceChild("left_leg2_b",
                CubeListBuilder.create()
                        .texOffs(10, 62)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(4F, 0F, 0F, 0F, 0F, 0.5235988F)
        );

        // Left Leg 3A
        PartDefinition leftLeg3A = root.addOrReplaceChild("left_leg3_a",
                CubeListBuilder.create()
                        .texOffs(0, 58)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(5F, 19.5F, 2.5F, 0F, -0.6981317F, 0.418879F)
        );
        // Left Leg 3B
        leftLeg3A.addOrReplaceChild("left_leg3_b",
                CubeListBuilder.create()
                        .texOffs(0, 60)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(4F, 0F, 0F, 0F, 0F, 0.5235988F)
        );

        // Left Leg 4A
        PartDefinition leftLeg4A = root.addOrReplaceChild("left_leg4_a",
                CubeListBuilder.create()
                        .texOffs(22, 34)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offsetAndRotation(2F, 19.5F, 3.5F, 0F, -0.6108652F, 0.418879F)
        );
        // Left Leg 4B
        PartDefinition leftLeg4B = leftLeg4A.addOrReplaceChild("left_leg4_b",
                CubeListBuilder.create()
                        .texOffs(22, 36)
                        .addBox(0F, -0.5F, -1F, 3, 1, 2),
                PartPose.offsetAndRotation(4F, 0F, 0F, 0F, -1.308997F, 0.418879F)
        );
        // Left Leg 4C
        leftLeg4B.addOrReplaceChild("left_leg4_c",
                CubeListBuilder.create()
                        .texOffs(22, 39)
                        .addBox(0F, -0.5F, -1F, 3, 1, 2),
                PartPose.offsetAndRotation(3F, 0F, 0F, 0F, -0.8726646F, 0.418879F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing, float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // Handle “fleeing” animation for claws
        if (entityIn.isFleeing()) {
            // Both claws point downward (–90° on X)
            this.left_arm_a.xRot  = -90F / 57.29578F;
            this.right_arm_a.xRot = -90F / 57.29578F;
        } else {
            this.left_arm_a.xRot  = 0F;
            this.right_arm_a.xRot = 0F;
        }

        // If nearly stationary, do random “claw wave” animations
        if (limbSwingAmount < 0.1F) {
            // Base positions for both arms
            this.right_arm_a.yRot = -30F / 57.29578F;
            this.right_arm_b.yRot = -120F / 57.29578F;

            // Left claw “random flick”
            float lHand = 0F;
            float f2a = ageInTicks % 100F;
            if (f2a > 0F && f2a < 10F) {
                lHand = (f2a * 2F) / 57.29578F;
            }
            this.left_arm_a.yRot = 30F / 57.29578F + lHand;
            this.left_arm_b.yRot = 120F / 57.29578F + lHand;

            // Right claw “random flick”
            float rHand = 0F;
            float f2b = ageInTicks % 75F;
            if (f2b > 30F && f2b < 40F) {
                rHand = (f2b - 29F) * 2F / 57.29578F;
            }
            this.right_arm_a.yRot = -30F / 57.29578F - rHand;
            this.right_arm_b.yRot = -120F / 57.29578F - rHand;
        }

        // Leg‐swing math
        float f9  = -Mth.cos(limbSwing * 5F) * limbSwingAmount * 2F;
        float f10 = -Mth.cos(limbSwing * 5F + (float)Math.PI) * limbSwingAmount * 2F;
        float f11 = -Mth.cos(limbSwing * 5F + (float)(Math.PI * 0.5F)) * limbSwingAmount * 2F;
        float f12 = -Mth.cos(limbSwing * 5F + (float)(Math.PI * 1.5F)) * limbSwingAmount * 2F;
        float f13 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount * 5F;
        float f14 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f15 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)(Math.PI * 0.5F)) * 0.4F) * limbSwingAmount;
        float f16 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)(Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;

        // Right Leg 1A adjustments
        this.right_leg1_a.yRot = -0.1745329F;
        this.right_leg1_a.zRot = -0.418879F;
        this.right_leg1_a.yRot += f9;
        this.right_leg1_a.zRot += f13;

        this.right_leg1_b.zRot = -0.5235988F;
        this.right_leg1_b.zRot -= f13;

        // Right Leg 2A
        this.right_leg2_a.yRot = 0.0872665F;
        this.right_leg2_a.zRot = -0.418879F;
        this.right_leg2_a.yRot += f10;
        this.right_leg2_a.zRot += f14;

        this.right_leg2_b.zRot = -0.5235988F;
        this.right_leg2_b.zRot -= f14;

        // Right Leg 3A
        this.right_leg3_a.yRot = 0.6981317F;
        this.right_leg3_a.zRot = -0.418879F;
        this.right_leg3_a.yRot += f11;
        this.right_leg3_a.zRot += f15;

        this.right_leg3_b.zRot = -0.5235988F;
        this.right_leg3_b.zRot -= f15;

        // Right Leg 4A
        this.right_leg4_a.yRot = 0.6108652F;
        this.right_leg4_a.zRot = -0.418879F;
        this.right_leg4_a.yRot += f12;
        this.right_leg4_a.zRot += f16;

        // Left Leg 1A
        this.left_leg1_a.yRot = 0.1745329F;
        this.left_leg1_a.zRot = 0.418879F;
        this.left_leg1_a.yRot -= f9;
        this.left_leg1_a.zRot -= f13;

        this.left_leg1_b.zRot = 0.5235988F;
        this.left_leg1_b.zRot += f13;

        // Left Leg 2A
        this.left_leg2_a.yRot = -0.0872665F;
        this.left_leg2_a.zRot = 0.418879F;
        this.left_leg2_a.yRot -= f10;
        this.left_leg2_a.zRot -= f14;

        this.left_leg2_b.zRot = 0.5235988F;
        this.left_leg2_b.zRot += f14;

        // Left Leg 3A
        this.left_leg3_a.yRot = -0.6981317F;
        this.left_leg3_a.zRot = 0.418879F;
        this.left_leg3_a.yRot -= f11;
        this.left_leg3_a.zRot -= f15;

        this.left_leg3_b.zRot = 0.5235988F;
        this.left_leg3_b.zRot += f15;

        // Left Leg 4A
        this.left_leg4_a.yRot = -0.6108652F;
        this.left_leg4_a.zRot = 0.418879F;
        this.left_leg4_a.yRot -= f12;
        this.left_leg4_a.zRot -= f16;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Draw the crab in approximately the same order as before:
        this.shell.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.shell_right.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.shell_left.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.shell_back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.left_eye.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_eye_base.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_eye_base.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_eye.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.right_arm_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_arm_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.left_leg1_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_leg2_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_leg3_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_leg4_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.right_leg1_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_leg2_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_leg3_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_leg4_a.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
