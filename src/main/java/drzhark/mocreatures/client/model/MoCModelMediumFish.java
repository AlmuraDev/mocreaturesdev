/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import drzhark.mocreatures.entity.aquatic.MoCEntityMediumFish;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.16.5 → 1.20.1. All ModelRenderer fields are now ModelParts,
 * built via createBodyLayer(). Animations have been moved into setupAnim(...),
 * and render(...) is now renderToBuffer(...).
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelMediumFish<T extends MoCEntityMediumFish> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "medium_fish"),
            "main"
    );

    // ----------------------------------------------------------------
    // 2) All ModelRenderer fields become ModelPart fields
    // ----------------------------------------------------------------
    private final ModelPart Head;
    private final ModelPart LowerHead;
    private final ModelPart Nose;
    private final ModelPart MouthBottom;
    private final ModelPart MouthBottomB;
    private final ModelPart Body;
    private final ModelPart BackUp;
    private final ModelPart BackDown;
    private final ModelPart Tail;
    private final ModelPart TailFin;
    private final ModelPart RightPectoralFin;
    private final ModelPart LeftPectoralFin;
    private final ModelPart UpperFin;
    private final ModelPart LowerFin;
    private final ModelPart RightLowerFin;
    private final ModelPart LeftLowerFin;

    // Store entity for offsets during render
    private MoCEntityMediumFish mediumFish;

    public MoCModelMediumFish(ModelPart root) {
        super(root);
        // Grab each child by the names used in createBodyLayer()
        this.Head             = root.getChild("Head");
        this.LowerHead        = root.getChild("LowerHead");
        this.Nose             = root.getChild("Nose");
        this.MouthBottom      = root.getChild("MouthBottom");
        this.MouthBottomB     = root.getChild("MouthBottomB");
        this.Body             = root.getChild("Body");
        this.BackUp           = root.getChild("BackUp");
        this.BackDown         = root.getChild("BackDown");
        this.Tail             = root.getChild("Tail");
        this.TailFin          = root.getChild("TailFin");
        this.RightPectoralFin = root.getChild("RightPectoralFin");
        this.LeftPectoralFin  = root.getChild("LeftPectoralFin");
        this.UpperFin         = root.getChild("UpperFin");
        this.LowerFin         = root.getChild("LowerFin");
        this.RightLowerFin    = root.getChild("RightLowerFin");
        this.LeftLowerFin     = root.getChild("LeftLowerFin");
    }

    /**
     * Build the LayerDefinition (MeshDefinition → PartDefinition).
     * Each child here matches one of the old ModelRenderer fields.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //----------------------------------------------------------------
        // Head (texture 0,10; box(-5,0,-1.5; 5x3x3); pivot(-8,6,0); rotate Z=-0.4461433)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-5.0F, 0.0F, -1.5F, 5, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -8.0F, 6.0F, 0.0F,   // offset
                        0.0F, 0.0F, -0.4461433F  // xRot, yRot, zRot
                )
        );

        //----------------------------------------------------------------
        // LowerHead (texture 0,16; box(-4,-3,-1.5; 4x3x3); pivot(-8,12,0); rotate Z=0.3346075)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LowerHead",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-4.0F, -3.0F, -1.5F, 4, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -8.0F, 12.0F, 0.0F,
                        0.0F, 0.0F, 0.3346075F
                )
        );

        //----------------------------------------------------------------
        // Nose (texture 14,17; box(-1,-1,-1; 1x3x2); pivot(-11,8.2,0); rotate Z=1.412787)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Nose",
                CubeListBuilder.create()
                        .texOffs(14, 17)
                        .addBox(-1.0F, -1.0F, -1.0F, 1, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -11.0F, 8.2F, 0.0F,
                        0.0F, 0.0F, 1.412787F
                )
        );

        //----------------------------------------------------------------
        // MouthBottom (texture 16,10; box(-2,-0.4,-1; 2x1x2); pivot(-11.5,10,0); rotate Z=0.3346075)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "MouthBottom",
                CubeListBuilder.create()
                        .texOffs(16, 10)
                        .addBox(-2.0F, -0.4F, -1.0F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -11.5F, 10.0F, 0.0F,
                        0.0F, 0.0F, 0.3346075F
                )
        );

        //----------------------------------------------------------------
        // MouthBottomB (texture 16,13; box(-1.5,-2.4,-0.5; 1x1x1); pivot(-11.5,10,0); rotate Z=-0.7132579)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "MouthBottomB",
                CubeListBuilder.create()
                        .texOffs(16, 13)
                        .addBox(-1.5F, -2.4F, -0.5F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -11.5F, 10.0F, 0.0F,
                        0.0F, 0.0F, -0.7132579F
                )
        );

        //----------------------------------------------------------------
        // Body (texture 0,0; box(0,-3,-2; 9x6x4); pivot(-8,9,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -3.0F, -2.0F, 9, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-8.0F, 9.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // BackUp (texture 26,0; box(0,0,-1.5; 8x3x3); pivot(1,6,0); rotate Z=0.1858931)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "BackUp",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(0.0F, 0.0F, -1.5F, 8, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.0F, 6.0F, 0.0F,
                        0.0F, 0.0F, 0.1858931F
                )
        );

        //----------------------------------------------------------------
        // BackDown (texture 26,6; box(0,-3,-1.5; 8x3x3); pivot(1,12,0); rotate Z=-0.1919862)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "BackDown",
                CubeListBuilder.create()
                        .texOffs(26, 6)
                        .addBox(0.0F, -3.0F, -1.5F, 8, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.0F, 12.0F, 0.0F,
                        0.0F, 0.0F, -0.1919862F
                )
        );

        //----------------------------------------------------------------
        // Tail (texture 48,0; box(0,-1.5,-1; 4x3x2); pivot(8,9,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(0.0F, -1.5F, -1.0F, 4, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(8.0F, 9.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // TailFin (texture 48,5; box(3,-5.3,0; 5x11x0); pivot(8,9,0))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "TailFin",
                CubeListBuilder.create()
                        .texOffs(48, 5)
                        .addBox(3.0F, -5.3F, 0.0F, 5, 11, 0, new CubeDeformation(0.0F)),
                PartPose.offset(8.0F, 9.0F, 0.0F)
        );

        //----------------------------------------------------------------
        // RightPectoralFin (texture 28,12; box(0,-2,0; 5x4x0); pivot(-6.5,10,2); rotate Y=-0.8726646, Z=0.185895)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightPectoralFin",
                CubeListBuilder.create()
                        .texOffs(28, 12)
                        .addBox(0.0F, -2.0F, 0.0F, 5, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -6.5F, 10.0F, 2.0F,
                        0.0F, -0.8726646F, 0.185895F
                )
        );

        //----------------------------------------------------------------
        // LeftPectoralFin (texture 38,12; box(0,-2,0; 5x4x0); pivot(-6.5,10,-2); rotate Y=0.8726646, Z=0.1858931)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftPectoralFin",
                CubeListBuilder.create()
                        .texOffs(38, 12)
                        .addBox(0.0F, -2.0F, 0.0F, 5, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -6.5F, 10.0F, -2.0F,
                        0.0F, 0.8726646F, 0.1858931F
                )
        );

        //----------------------------------------------------------------
        // UpperFin (texture 0,22; box(0,-4,0; 15x4x0); pivot(-7,6,0); rotate Z=0.1047198)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "UpperFin",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(0.0F, -4.0F, 0.0F, 15, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -7.0F, 6.0F, 0.0F,
                        0.0F, 0.0F, 0.1047198F
                )
        );

        //----------------------------------------------------------------
        // LowerFin (texture 46,20; box(0,0,0; 9x4x0); pivot(0,12,0); rotate Z=-0.1858931)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LowerFin",
                CubeListBuilder.create()
                        .texOffs(46, 20)
                        .addBox(0.0F, 0.0F, 0.0F, 9, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 12.0F, 0.0F,
                        0.0F, 0.0F, -0.1858931F
                )
        );

        //----------------------------------------------------------------
        // RightLowerFin (texture 28,16; box(0,0,0; 9x4x0); pivot(-7,12,1); rotate X=0.5235988)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RightLowerFin",
                CubeListBuilder.create()
                        .texOffs(28, 16)
                        .addBox(0.0F, 0.0F, 0.0F, 9, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -7.0F, 12.0F, 1.0F,
                        0.5235988F, 0.0F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // LeftLowerFin (texture 46,16; box(0,0,0; 9x4x0); pivot(-7,12,-1); rotate X=-0.5235988)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LeftLowerFin",
                CubeListBuilder.create()
                        .texOffs(46, 16)
                        .addBox(0.0F, 0.0F, 0.0F, 9, 4, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -7.0F, 12.0F, -1.0F,
                        -0.5235988F, 0.0F, 0.0F
                )
        );

        // Finally, return a LayerDefinition with texture size 64×32
        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * Capture the entity instance each frame. In 1.16.5 this was setLivingAnimations(...).
     * In 1.20.1 we override prepareMobModel(...).
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.mediumFish = entity;
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
        // tail movement: tail sway based on limbSwing & limbSwingAmount
        float tailMov = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.6F;
        float finMov  = Mth.cos(ageInTicks * 0.2F) * 0.4F;
        float mouthMov= Mth.cos(ageInTicks * 0.3F) * 0.2F;

        // apply to tail and tail fin
        this.Tail.yRot     = tailMov;
        this.TailFin.yRot  = tailMov;

        // pectoral fins
        this.LeftPectoralFin.yRot  = 0.8726646F + finMov;
        this.RightPectoralFin.yRot = -0.8726646F - finMov;

        // mouth movement
        this.MouthBottom.zRot    = 0.3346075F + mouthMov;
        this.MouthBottomB.zRot   = -0.7132579F + mouthMov;
    }

    /**
     * Render all parts, applying entity‐specific offsets and a 90° Y rotation.
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
        // Get the adjusted offsets from the entity (same as old getAdjustedX/Y/ZOffset)
        float yOffset = this.mediumFish.getAdjustedYOffset();
        float xOffset = this.mediumFish.getAdjustedXOffset();
        float zOffset = this.mediumFish.getAdjustedZOffset();

        poseStack.pushPose();
        // translate
        poseStack.translate(xOffset, yOffset, zOffset);
        // rotate 90° around Y
        poseStack.rotate(Axis.YP.rotationDegrees(90.0F));

        // Render each part
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LowerHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.MouthBottom.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.MouthBottomB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.BackUp.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.BackDown.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightPectoralFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftPectoralFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UpperFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LowerFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightLowerFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftLowerFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }
}
