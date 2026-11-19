/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntitySilverSkeleton;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.16.5 Forge → 1.20.1 Forge.
 * All ModelRenderer → ModelPart; setRotationAngles(...) → setupAnim(...); render(...) → renderToBuffer(...).
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelSilverSkeleton<T extends MoCEntitySilverSkeleton> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "silver_skeleton"), "main"
    );

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart back;
    private final ModelPart rightArm;
    private final ModelPart rightHand;
    private final ModelPart rightSwordA;
    private final ModelPart rightSwordB;
    private final ModelPart rightSwordC;
    private final ModelPart leftArm;
    private final ModelPart leftHand;
    private final ModelPart leftSwordA;
    private final ModelPart leftSwordB;
    private final ModelPart leftSwordC;
    private final ModelPart rightThigh;
    private final ModelPart rightKnee;
    private final ModelPart rightLeg;
    private final ModelPart rightFoot;
    private final ModelPart leftThigh;
    private final ModelPart leftKnee;
    private final ModelPart leftLeg;
    private final ModelPart leftFoot;

    // Temporary state passed in during setLivingAnimations()
    private float limbSwingAmountStored;
    private boolean sprintingStored;
    private int leftAttackStored;
    private int rightAttackStored;
    private boolean ridingStored;

    public MoCModelSilverSkeleton(ModelPart root) {
        super(root);
        this.head         = root.getChild("head");
        this.body         = root.getChild("body");
        this.back         = this.body.getChild("back");
        this.rightArm     = root.getChild("rightArm");
        this.rightHand    = root.getChild("rightHand");
        this.rightSwordA  = root.getChild("rightSwordA");
        this.rightSwordB  = root.getChild("rightSwordB");
        this.rightSwordC  = root.getChild("rightSwordC");
        this.leftArm      = root.getChild("leftArm");
        this.leftHand     = root.getChild("leftHand");
        this.leftSwordA   = root.getChild("leftSwordA");
        this.leftSwordB   = root.getChild("leftSwordB");
        this.leftSwordC   = root.getChild("leftSwordC");
        this.rightThigh   = root.getChild("rightThigh");
        this.rightKnee    = root.getChild("rightKnee");
        this.rightLeg     = this.rightThigh.getChild("rightLeg");
        this.rightFoot    = this.rightLeg.getChild("rightFoot");
        this.leftThigh    = root.getChild("leftThigh");
        this.leftKnee     = root.getChild("leftKnee");
        this.leftLeg      = this.leftThigh.getChild("leftLeg");
        this.leftFoot     = this.leftLeg.getChild("leftFoot");
    }

    /**
     * Defines every part of the skeleton: cubes, offsets, rotations, etc.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // --- Head ---
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8, CubeDeformation.NONE),
                PartPose.offset(0F, -2F, 0F)
        );

        // --- Body & Back ---
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-4F, 0F, -2F, 8, 12, 4, CubeDeformation.NONE),
                PartPose.offset(0F, -2F, 0F)
        );

        PartDefinition back = body.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(44, 54).addBox(-4F, -4F, 0.5F, 8, 8, 2, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0F, 2F, 2F, -0.1570796F, 0F, 0F)
        );

        // --- Right Arm, Hand, and Swords ---
        PartDefinition rightArm = root.addOrReplaceChild("rightArm",
                CubeListBuilder.create()
                        .texOffs(48, 31).addBox(-3F, -2.5F, -2.5F, 4, 11, 4, CubeDeformation.NONE),
                PartPose.offset(-5F, 1F, 0F)
        );

        PartDefinition rightHand = root.addOrReplaceChild("rightHand",
                CubeListBuilder.create()
                        .texOffs(24, 16).addBox(-2.5F, -2F, -2F, 3, 12, 3, CubeDeformation.NONE),
                PartPose.offset(-5F, 1F, 0F)
        );

        PartDefinition rightSwordA = root.addOrReplaceChild("rightSwordA",
                CubeListBuilder.create()
                        .texOffs(52, 46).addBox(-1.5F, 8.5F, -3F, 1, 1, 5, CubeDeformation.NONE),
                PartPose.offset(-5F, 1F, 0F)
        );
        PartDefinition rightSwordB = root.addOrReplaceChild("rightSwordB",
                CubeListBuilder.create()
                        .texOffs(48, 50).addBox(-1.5F, 7.5F, -4F, 1, 3, 1, CubeDeformation.NONE),
                PartPose.offset(-5F, 1F, 0F)
        );
        PartDefinition rightSwordC = root.addOrReplaceChild("rightSwordC",
                CubeListBuilder.create()
                        .texOffs(28, 28).addBox(-1F, 7.5F, -14F, 0, 3, 10, CubeDeformation.NONE),
                PartPose.offset(-5F, 1F, 0F)
        );

        // --- Left Arm, Hand, and Swords ---
        PartDefinition leftArm = root.addOrReplaceChild("leftArm",
                CubeListBuilder.create()
                        .texOffs(48, 16).addBox(-1F, -2.5F, -2.5F, 4, 11, 4, CubeDeformation.NONE),
                PartPose.offset(5F, 1F, 0F)
        );
        PartDefinition leftHand = root.addOrReplaceChild("leftHand",
                CubeListBuilder.create()
                        .texOffs(36, 16).addBox(-0.5F, -2F, -2F, 3, 12, 3, CubeDeformation.NONE),
                PartPose.offset(5F, 1F, 0F)
        );
        PartDefinition leftSwordA = root.addOrReplaceChild("leftSwordA",
                CubeListBuilder.create()
                        .texOffs(52, 46).addBox(0.5F, 8.5F, -3F, 1, 1, 5, CubeDeformation.NONE),
                PartPose.offset(5F, 1F, 0F)
        );
        PartDefinition leftSwordB = root.addOrReplaceChild("leftSwordB",
                CubeListBuilder.create()
                        .texOffs(48, 46).addBox(0.5F, 7.5F, -4F, 1, 3, 1, CubeDeformation.NONE),
                PartPose.offset(5F, 1F, 0F)
        );
        PartDefinition leftSwordC = root.addOrReplaceChild("leftSwordC",
                CubeListBuilder.create()
                        .texOffs(28, 31).addBox(1F, 7.5F, -14F, 0, 3, 10, CubeDeformation.NONE),
                PartPose.offset(5F, 1F, 0F)
        );

        // --- Right Leg (Thigh, Knee, Leg, Foot) ---
        PartDefinition rightThigh = root.addOrReplaceChild("rightThigh",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.5F, 0F, -1.5F, 3, 6, 3, CubeDeformation.NONE),
                PartPose.offset(-2F, 10.5F, 0F)
        );
        PartDefinition rightKnee = root.addOrReplaceChild("rightKnee",
                CubeListBuilder.create()
                        .texOffs(0, 46).addBox(-2F, 1F, -2F, 4, 4, 4, CubeDeformation.NONE),
                PartPose.offset(-2F, 10.5F, 0F)
        );
        PartDefinition rightLeg = rightThigh.addOrReplaceChild("rightLeg",
                CubeListBuilder.create()
                        .texOffs(0, 25).addBox(-1.5F, 0F, -1.5F, 3, 6, 3, CubeDeformation.NONE),
                PartPose.offset(0F, 6F, 0F)
        );
        PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot",
                CubeListBuilder.create()
                        .texOffs(0, 54).addBox(-2F, 0F, -2F, 4, 6, 4, CubeDeformation.NONE),
                PartPose.offset(0F, 2F, 0F)
        );

        // --- Left Leg (Thigh, Knee, Leg, Foot) ---
        PartDefinition leftThigh = root.addOrReplaceChild("leftThigh",
                CubeListBuilder.create()
                        .texOffs(12, 16).addBox(-1.5F, 0F, -1.5F, 3, 6, 3, CubeDeformation.NONE),
                PartPose.offset(2F, 10.5F, 0F)
        );
        PartDefinition leftKnee = root.addOrReplaceChild("leftKnee",
                CubeListBuilder.create()
                        .texOffs(16, 46).addBox(-2F, 1F, -2F, 4, 4, 4, CubeDeformation.NONE),
                PartPose.offset(2F, 10.5F, 0F)
        );
        PartDefinition leftLeg = leftThigh.addOrReplaceChild("leftLeg",
                CubeListBuilder.create()
                        .texOffs(12, 25).addBox(-1.5F, 0F, -1.5F, 3, 6, 3, CubeDeformation.NONE),
                PartPose.offset(0F, 6F, 0F)
        );
        PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot",
                CubeListBuilder.create()
                        .texOffs(16, 54).addBox(-2F, 0F, -2F, 4, 6, 4, CubeDeformation.NONE),
                PartPose.offset(0F, 2F, 0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    /**
     * Called first—stores sprinting/attack/riding flags so we can use them in setupAnim().
     */
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.limbSwingAmountStored = limbSwingAmount;
        this.sprintingStored      = entityIn.isSprinting();
        this.leftAttackStored     = entityIn.attackCounterLeft;
        this.rightAttackStored    = entityIn.attackCounterRight;
        this.ridingStored         = entityIn.getVehicle() != null;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float radianF = 57.29578F;
        float hRotY = netHeadYaw / radianF;
        float hRotX = headPitch / radianF;

        // --- Head rotation ---
        this.head.xRot = hRotX;
        this.head.yRot = hRotY;

        // --- Arms swinging and attacks ---
        float RLegXRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;

        // Left arm:
        if (leftAttackStored == 0) {
            // Idle/waving animation:
            this.leftArm.zRot = (Mth.cos(ageInTicks * 0.09F) * 0.05F) - 0.05F;
            this.leftArm.xRot = RLegXRot;
        } else {
            // Attack animation: swing by cos(leftAttackStored)
            this.leftArm.xRot = -(Mth.cos(leftAttackStored * 0.18F) * 3F);
        }

        // Right arm:
        if (rightAttackStored == 0) {
            this.rightArm.zRot = -(Mth.cos(ageInTicks * 0.09F) * 0.05F) + 0.05F;
            this.rightArm.xRot = LLegXRot;
        } else {
            this.rightArm.xRot = -(Mth.cos(rightAttackStored * 0.18F) * 3F);
        }

        // Synchronize hands & swords to the parent arm’s rotation:
        this.leftHand.xRot = this.leftSwordA.xRot = this.leftSwordB.xRot = this.leftSwordC.xRot = this.leftArm.xRot;
        this.leftHand.zRot = this.leftSwordA.zRot = this.leftSwordB.zRot = this.leftSwordC.zRot = this.leftArm.zRot;

        this.rightHand.xRot = this.rightSwordA.xRot = this.rightSwordB.xRot = this.rightSwordC.xRot = this.rightArm.xRot;
        this.rightHand.zRot = this.rightSwordA.zRot = this.rightSwordB.zRot = this.rightSwordC.zRot = this.rightArm.zRot;

        // --- Legs & Knees / Riding logic ---
        if (ridingStored) {
            // Sitting pose when riding
            this.rightLeg.xRot    = 0F;
            this.rightThigh.xRot  = -60F / radianF;
            this.rightThigh.yRot  =  20F / radianF;
            this.rightKnee.yRot   =  20F / radianF;
            this.rightKnee.xRot   = -60F / radianF;

            this.leftLeg.xRot     = 0F;
            this.leftThigh.yRot   = -20F / radianF;
            this.leftKnee.yRot    = -20F / radianF;
            this.leftThigh.xRot   = -60F / radianF;
            this.leftKnee.xRot    = -60F / radianF;
        } else {
            // Normal walking animation:
            this.rightThigh.yRot = 0F;
            this.rightKnee.yRot  = 0F;
            this.leftThigh.yRot  = 0F;
            this.leftKnee.yRot   = 0F;

            this.rightThigh.xRot = RLegXRot;
            this.leftThigh.xRot  = LLegXRot;
            this.rightKnee.xRot  = this.rightThigh.xRot;
            this.leftKnee.xRot   = this.leftThigh.xRot;

            // Add a little “kick” when moving quickly:
            float RLegXRot2 = Mth.cos(((limbSwing + 0.1F) * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
            float LLegXRot2 = Mth.cos((limbSwing + 0.1F) * 0.6662F) * 0.8F * limbSwingAmount;
            float RLegXRotB = RLegXRot;
            float LLegXRotB = LLegXRot;
            if (limbSwingAmount > 0.15F) {
                if (RLegXRot > RLegXRot2) {
                    RLegXRotB = RLegXRot + (25F / radianF);
                }
                if (LLegXRot > LLegXRot2) {
                    LLegXRotB = LLegXRot + (25F / radianF);
                }
            }
            this.rightLeg.xRot = LLegXRotB;
            this.leftLeg.xRot  = RLegXRotB;
        }

        // --- Sprinting tilt on body/head if sprinting fast enough ---
        if (sprintingStored && limbSwingAmountStored > 0.3F) {
            this.body.xRot = -limbSwingAmountStored * 20F / radianF;
        } else {
            this.body.xRot = 0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if (ridingStored) {
            // Lift model slightly when riding
            poseStack.translate(0.0D, 0.5D, 0.0D);
        }
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightSwordA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightSwordB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightSwordC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftSwordA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftSwordB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftSwordC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightThigh.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightKnee.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftThigh.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftKnee.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        poseStack.popPose();
    }
}
