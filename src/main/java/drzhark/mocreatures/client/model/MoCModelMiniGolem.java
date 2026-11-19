/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.hostile.MoCEntityMiniGolem;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

/**
 * Ported from 1.16.5 → 1.20.1. All ModelRenderer fields are now ModelParts,
 * built via createBodyLayer(). Animations moved to setupAnim(...), rendering
 * moved to renderToBuffer(...), and entity flags captured in prepareMobModel(...).
 */
public class MoCModelMiniGolem<T extends MoCEntityMiniGolem> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "mini_golem"),
            "main"
    );

    private static final float RADIAN_CONV = 57.29578F;
    
    private final ModelPart Head;
    private final ModelPart HeadRed;
    private final ModelPart Body;
    private final ModelPart BodyRed;
    private final ModelPart LeftShoulder;
    private final ModelPart LeftArm;
    private final ModelPart LeftArmRingA;
    private final ModelPart LeftArmRingB;
    private final ModelPart RightShoulder;
    private final ModelPart RightArm;
    private final ModelPart RightArmRingA;
    private final ModelPart RightArmRingB;
    private final ModelPart RightLeg;
    private final ModelPart RightFoot;
    private final ModelPart LeftLeg;
    private final ModelPart LeftFoot;

    // Entity‐state flag from setLivingAnimations(...)
    private boolean angry;

    public MoCModelMiniGolem(ModelPart root) {
        super(root);
        this.Head            = root.getChild("Head");
        this.HeadRed         = root.getChild("HeadRed");
        this.Body            = root.getChild("Body");
        this.BodyRed         = root.getChild("BodyRed");
        this.LeftShoulder    = root.getChild("LeftShoulder");
        this.LeftArm         = root.getChild("LeftArm");
        this.LeftArmRingA    = root.getChild("LeftArmRingA");
        this.LeftArmRingB    = root.getChild("LeftArmRingB");
        this.RightShoulder   = root.getChild("RightShoulder");
        this.RightArm        = root.getChild("RightArm");
        this.RightArmRingA   = root.getChild("RightArmRingA");
        this.RightArmRingB   = root.getChild("RightArmRingB");
        this.RightLeg        = root.getChild("RightLeg");
        this.RightFoot       = root.getChild("RightFoot");
        this.LeftLeg         = root.getChild("LeftLeg");
        this.LeftFoot        = root.getChild("LeftFoot");
    }

    /**
     * Build the LayerDefinition (MeshDefinition → PartDefinition).
     * Each child here corresponds to one of the old ModelRenderer fields.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //----------------------------------------------------------------
        // Head (texture 30,0; box(-3, -3, -3; 6×3×6); pivot(0,8,0); rotateY=-0.7853982)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        0.0F, -0.7853982F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // HeadRed (texture 30,29; same shape; pivot(0,8,0); rotateY=-0.7853982)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "HeadRed",
                CubeListBuilder.create()
                        .texOffs(30, 29)
                        .addBox(-3.0F, -3.0F, -3.0F, 6, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        0.0F, -0.7853982F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // Body (texture 0,0; box(-5, -10, -5; 10×10×10); pivot(0,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // BodyRed (texture 0,28; same shape; pivot(0,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "BodyRed",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftShoulder (texture 0,4; box(0, -1, -1; 1×2×2); pivot(5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftShoulder",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(0.0F, -1.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftArm (texture 0,48; box(1, -2, -2; 4×12×4); pivot(5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftArm",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(1.0F, -2.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftArmRingA (texture 20,20; box(0.5, 1, -2.5; 5×3×5); pivot(5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftArmRingA",
                CubeListBuilder.create()
                        .texOffs(20, 20)
                        .addBox(0.5F, 1.0F, -2.5F, 5, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftArmRingB (texture 20,20; box(0.5, 5, -2.5; 5×3×5); pivot(5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftArmRingB",
                CubeListBuilder.create()
                        .texOffs(20, 20)
                        .addBox(0.5F, 5.0F, -2.5F, 5, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightShoulder (texture 0,0; box(-1, -1, -1; 1×2×2); pivot(-5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightShoulder",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightArm (texture 16,48; box(-5, -2, -2; 4×12×4); pivot(-5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-5.0F, -2.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightArmRingA (texture 0,20; box(-5.5, 1, -2.5; 5×3×5); pivot(-5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightArmRingA",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-5.5F, 1.0F, -2.5F, 5, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightArmRingB (texture 0,20; box(-5.5, 5, -2.5; 5×3×5); pivot(-5,11,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightArmRingB",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-5.5F, 5.0F, -2.5F, 5, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 11.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightLeg (texture 40,9; box(-2.5, 0, -2; 4×6×4); pivot(-2,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create()
                        .texOffs(40, 9)
                        .addBox(-2.5F, 0.0F, -2.0F, 4, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 18.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightFoot (texture 15,22; box(-2.5, 5, -3; 4×1×1); pivot(-2,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightFoot",
                CubeListBuilder.create()
                        .texOffs(15, 22)
                        .addBox(-2.5F, 5.0F, -3.0F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 18.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftLeg (texture 40,19; box(-1.5, 0, -2; 4×6×4); pivot(2,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create()
                        .texOffs(40, 19)
                        .addBox(-1.5F, 0.0F, -2.0F, 4, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 18.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // LeftFoot (texture 15,20; box(-1.5, 5, -3; 4×1×1); pivot(2,18,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftFoot",
                CubeListBuilder.create()
                        .texOffs(15, 20)
                        .addBox(-1.5F, 5.0F, -3.0F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 18.0F, 0.0F)
        );

        // Return a LayerDefinition with texture size 64×64
        return LayerDefinition.create(mesh, 64, 64);
    }

    /**
     * In 1.16.5 this was setLivingAnimations(...).
     * In 1.20.1, override prepareMobModel(...) instead.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.angry = entity.isAggressive();
    }

    /**
     * Copy the old setRotationAngles(...) logic here. Now called setupAnim(...)
     */
    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Yaw converted to radians
        float hRotY     = netHeadYaw / RADIAN_CONV;
        // Leg swings
        float RLegXRot  = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot  = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;

        // Apply to legs & feet
        this.RightLeg.xRot   = RLegXRot;
        this.RightFoot.xRot  = RLegXRot;
        this.LeftLeg.xRot    = LLegXRot;
        this.LeftFoot.xRot   = LLegXRot;

        // Head yaw plus offset
        this.Head.yRot    = -0.7853982F + hRotY;
        this.HeadRed.yRot = -0.7853982F + hRotY;

        // Shoulders: if holding rock, arms down; else animate
        if (entity.getHasRock()) {
            this.LeftShoulder.zRot  = 0.0F;
            this.LeftShoulder.xRot  = - (float)Math.PI; // -180° in radians
            this.RightShoulder.zRot = 0.0F;
            this.RightShoulder.xRot = - (float)Math.PI;
        } else {
            this.LeftShoulder.zRot  = (Mth.cos(ageInTicks * 0.09F) * 0.05F) - 0.05F;
            this.LeftShoulder.xRot  = RLegXRot;
            this.RightShoulder.zRot = - (Mth.cos(ageInTicks * 0.09F) * 0.05F) + 0.05F;
            this.RightShoulder.xRot = LLegXRot;
        }

        // Copy shoulder rotations into the entire left arm stack
        this.LeftArm.xRot       = this.LeftArmRingA.xRot       = this.LeftArmRingB.xRot       = this.LeftShoulder.xRot;
        this.LeftArm.zRot       = this.LeftArmRingA.zRot       = this.LeftArmRingB.zRot       = this.LeftShoulder.zRot;

        // Copy shoulder rotations into the entire right arm stack
        this.RightArm.xRot      = this.RightArmRingA.xRot      = this.RightArmRingB.xRot      = this.RightShoulder.xRot;
        this.RightArm.zRot      = this.RightArmRingA.zRot      = this.RightArmRingB.zRot      = this.RightShoulder.zRot;
    }

    /**
     * Render everything, toggling Head/Body vs. HeadRed/BodyRed based on angry flag.
     */
    @Override
    public void renderToBuffer(
            PoseStack        poseStack,
            VertexConsumer   buffer,
            int              packedLight,
            int              packedOverlay,
            float            red,
            float            green,
            float            blue,
            float            alpha
    ) {
        // First, choose which head/body to render
        if (this.angry) {
            this.HeadRed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.BodyRed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Then all the other parts
        this.LeftShoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftArmRingA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftArmRingB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightShoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightArmRingA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightArmRingB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
