/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.passive.MoCEntityMouse;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

/**
 * Ported from 1.16.5 → 1.20.1. All ModelRenderer fields have become ModelParts,
 * built via createBodyLayer(). Animations moved to setupAnim(...), rendering
 * moved to renderToBuffer(...).
 */
public class MoCModelMouse<T extends MoCEntityMouse> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "mouse"),
            "main"
    );

    private static final float RADIAN_CONV = 57.29578F;
    
    private final ModelPart Head;
    private final ModelPart EarR;
    private final ModelPart EarL;
    private final ModelPart WhiskerR;
    private final ModelPart WhiskerL;
    private final ModelPart Tail;
    private final ModelPart FrontL;
    private final ModelPart FrontR;
    private final ModelPart RearL;
    private final ModelPart RearR;
    private final ModelPart BodyF;

    public MoCModelMouse(ModelPart root) {
        super(root);
        this.Head      = root.getChild("Head");
        this.EarR      = root.getChild("EarR");
        this.EarL      = root.getChild("EarL");
        this.WhiskerR  = root.getChild("WhiskerR");
        this.WhiskerL  = root.getChild("WhiskerL");
        this.Tail      = root.getChild("Tail");
        this.FrontL    = root.getChild("FrontL");
        this.FrontR    = root.getChild("FrontR");
        this.RearL     = root.getChild("RearL");
        this.RearR     = root.getChild("RearR");
        this.BodyF     = root.getChild("BodyF");
    }

    /**
     * Build the LayerDefinition (MeshDefinition → PartDefinition).
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //----------------------------------------------------------------
        // Head (texture 0,0; box(-1.5, -1.0, -6.0; 3×4×6); pivot(0,19,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, -6.0F, 3, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // EarR (texture 16,26; box(-3.5, -3.0, -1.0; 3×3×1); pivot(0,19,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "EarR",
                CubeListBuilder.create()
                        .texOffs(16, 26)
                        .addBox(-3.5F, -3.0F, -1.0F, 3, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // EarL (texture 24,26; box(0.5, -3.0, -1.0; 3×3×1); pivot(0,19,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "EarL",
                CubeListBuilder.create()
                        .texOffs(24, 26)
                        .addBox(0.5F, -3.0F, -1.0F, 3, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // WhiskerR (texture 20,20; box(-4.5, -1.0, -7.0; 3×3×1); pivot(0,19,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "WhiskerR",
                CubeListBuilder.create()
                        .texOffs(20, 20)
                        .addBox(-4.5F, -1.0F, -7.0F, 3, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // WhiskerL (texture 24,20; box(1.5, -1.0, -6.0; 3×3×1); pivot(0,19,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "WhiskerL",
                CubeListBuilder.create()
                        .texOffs(24, 20)
                        .addBox(1.5F, -1.0F, -6.0F, 3, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // Tail (texture 56,0; box(-0.5, 0, -1; 1×14×1); pivot(0,20,6); rotateX=1.570796)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create()
                        .texOffs(56, 0)
                        .addBox(-0.5F, 0.0F, -1.0F, 1, 14, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 20.0F, 6.0F,
                        1.570796F, 0.0F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // FrontL (texture 0,18; box(-2, 0, -3; 2×1×4); pivot(3,23,-4))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "FrontL",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-2.0F, 0.0F, -3.0F, 2, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offset(3.0F, 23.0F, -4.0F)
        );

        //----------------------------------------------------------------
        // FrontR (texture 0,18; box(0, 0, -3; 2×1×4); pivot(-3,23,-4))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "FrontR",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(0.0F, 0.0F, -3.0F, 2, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-3.0F, 23.0F, -4.0F)
        );

        //----------------------------------------------------------------
        // RearL (texture 0,18; box(-2, 0, -4; 2×1×4); pivot(3,23,5))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RearL",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-2.0F, 0.0F, -4.0F, 2, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offset(3.0F, 23.0F, 5.0F)
        );

        //----------------------------------------------------------------
        // RearR (texture 0,18; box(0, 0, -4; 2×1×4); pivot(-3,23,5))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RearR",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(0.0F, 0.0F, -4.0F, 2, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-3.0F, 23.0F, 5.0F)
        );

        //----------------------------------------------------------------
        // BodyF (texture 20,0; box(-3, -3, -7; 6×6×12); pivot(0,20,1))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "BodyF",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(-3.0F, -3.0F, -7.0F, 6, 6, 12, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 20.0F, 1.0F)
        );

        // Return a LayerDefinition with texture size 64×32
        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * No setLivingAnimations(...) here; all logic lives in setupAnim(...) for head/limbs.
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
        // Head pitch & yaw
        this.Head.xRot = - (headPitch / RADIAN_CONV);
        this.Head.yRot = netHeadYaw / RADIAN_CONV;
        this.EarR.xRot = this.Head.xRot;
        this.EarR.yRot = this.Head.yRot;
        this.EarL.xRot = this.Head.xRot;
        this.EarL.yRot = this.Head.yRot;
        this.WhiskerR.xRot = this.Head.xRot;
        this.WhiskerR.yRot = this.Head.yRot;
        this.WhiskerL.xRot = this.Head.xRot;
        this.WhiskerL.yRot = this.Head.yRot;

        // Limb swings
        float frontLRot = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        float rearLRot  = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
        float rearRRot  = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        float frontRRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;

        this.FrontL.xRot = frontLRot;
        this.RearL.xRot  = rearLRot;
        this.RearR.xRot  = rearRRot;
        this.FrontR.xRot = frontRRot;

        // Tail sway based on FrontL rotation
        this.Tail.yRot = this.FrontL.xRot * 0.625F;
    }

    /**
     * Render all parts. In the original, no entity-specific offsets were applied.
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
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.EarR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.EarL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.WhiskerR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.WhiskerL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.FrontL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.FrontR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RearL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RearR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.BodyF.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
