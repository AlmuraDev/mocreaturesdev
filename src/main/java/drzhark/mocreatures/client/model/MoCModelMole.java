/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.passive.MoCEntityMole;

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
 * moved to renderToBuffer(...), and entity offsets captured in prepareMobModel(...).
 */
public class MoCModelMole<T extends MoCEntityMole> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "mole"),
            "main"
    );
    
    private final ModelPart Nose;
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart Back;
    private final ModelPart Tail;
    private final ModelPart LLeg;
    private final ModelPart LFingers;
    private final ModelPart RLeg;
    private final ModelPart RFingers;
    private final ModelPart LRearLeg;
    private final ModelPart RRearLeg;

    // Captured offset from setLivingAnimations(...)
    private float yOffset;

    private final float RADIAN_CONV = 57.29578F;

    public MoCModelMole(ModelPart root) {
        super(root);
        this.Nose       = root.getChild("Nose");
        this.Head       = root.getChild("Head");
        this.Body       = root.getChild("Body");
        this.Back       = root.getChild("Back");
        this.Tail       = root.getChild("Tail");
        this.LLeg       = root.getChild("LLeg");
        this.LFingers   = root.getChild("LFingers");
        this.RLeg       = root.getChild("RLeg");
        this.RFingers   = root.getChild("RFingers");
        this.LRearLeg   = root.getChild("LRearLeg");
        this.RRearLeg   = root.getChild("RRearLeg");
    }

    /**
     * Build the LayerDefinition (MeshDefinition → PartDefinition).
     * Each child here corresponds to one of the old ModelRenderer fields.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root  = mesh.getRoot();

        //----------------------------------------------------------------
        // Nose (texture 0,25; box(-1, 0, -4; 2×2×3); pivot(0,20,-6); rotateX=0.2617994)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Nose",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-1.0F, 0.0F, -4.0F, 2, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 20.0F, -6.0F,
                        0.2617994F, 0.0F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // Head (texture 0,18; box(-3, -2, -2; 6×4×3); pivot(0,20,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-3.0F, -2.0F, -2.0F, 6, 4, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 20.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // Body (texture 0,0; box(-5, 0, 0; 10×6×10); pivot(0,17,-6))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, 0.0F, 10, 6, 10, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 17.0F, -6.0F)
        );

        //----------------------------------------------------------------
        // Back (texture 18,16; box(-4, -3, 0; 8×5×4); pivot(0,21,4))
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Back",
                CubeListBuilder.create()
                        .texOffs(18, 16)
                        .addBox(-4.0F, -3.0F, 0.0F, 8, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 21.0F, 4.0F)
        );

        //----------------------------------------------------------------
        // Tail (texture 52,8; box(-0.5, 0, 1; 1×1×5); pivot(0,21,6); rotateX=-0.3490659)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create()
                        .texOffs(52, 8)
                        .addBox(-0.5F, 0.0F, 1.0F, 1, 1, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 21.0F, 6.0F,
                        -0.3490659F, 0.0F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // LLeg (texture 10,25; box(0, -2, -1; 6×4×2); pivot(4,21,-4); rotateZ=0.2268928)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LLeg",
                CubeListBuilder.create()
                        .texOffs(10, 25)
                        .addBox(0.0F, -2.0F, -1.0F, 6, 4, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        4.0F, 21.0F, -4.0F,
                        0.0F, 0.0F, 0.2268928F
                )
        );

        //----------------------------------------------------------------
        // LFingers (texture 44,8; box(5, -2, 1; 1×4×1); pivot(4,21,-4); rotateZ=0.2268928)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LFingers",
                CubeListBuilder.create()
                        .texOffs(44, 8)
                        .addBox(5.0F, -2.0F, 1.0F, 1, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        4.0F, 21.0F, -4.0F,
                        0.0F, 0.0F, 0.2268928F
                )
        );

        //----------------------------------------------------------------
        // RLeg (texture 26,25; box(-6, -2, -1; 6×4×2); pivot(-4,21,-4); rotateZ=-0.2268928)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RLeg",
                CubeListBuilder.create()
                        .texOffs(26, 25)
                        .addBox(-6.0F, -2.0F, -1.0F, 6, 4, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -4.0F, 21.0F, -4.0F,
                        0.0F, 0.0F, -0.2268928F
                )
        );

        //----------------------------------------------------------------
        // RFingers (texture 48,8; box(-6, -2, 1; 1×4×1); pivot(-4,21,-4); rotateZ=-0.2268928)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RFingers",
                CubeListBuilder.create()
                        .texOffs(48, 8)
                        .addBox(-6.0F, -2.0F, 1.0F, 1, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -4.0F, 21.0F, -4.0F,
                        0.0F, 0.0F, -0.2268928F
                )
        );

        //----------------------------------------------------------------
        // LRearLeg (texture 36,0; box(0, -2, -1; 2×3×5); pivot(3,22,5); rotateX=-0.2792527, Y=0.5235988)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "LRearLeg",
                CubeListBuilder.create()
                        .texOffs(36, 0)
                        .addBox(0.0F, -2.0F, -1.0F, 2, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        3.0F, 22.0F, 5.0F,
                        -0.2792527F, 0.5235988F, 0.0F
                )
        );

        //----------------------------------------------------------------
        // RRearLeg (texture 50,0; box(-2, -2, -1; 2×3×5); pivot(-3,22,5); rotateX=-0.2792527, Y=-0.5235988)
        //----------------------------------------------------------------
        root.addOrReplaceChild(
                "RRearLeg",
                CubeListBuilder.create()
                        .texOffs(50, 0)
                        .addBox(-2.0F, -2.0F, -1.0F, 2, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -3.0F, 22.0F, 5.0F,
                        -0.2792527F, -0.5235988F, 0.0F
                )
        );

        // Return a LayerDefinition with texture size 64×32
        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * In 1.16.5 this was setLivingAnimations(...).
     * In 1.20.1, override prepareMobModel(...) instead.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.yOffset = entity.getAdjustedYOffset();
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
        // Head pitch & yaw
        this.Head.yRot  = netHeadYaw / RADIAN_CONV;
        this.Head.xRot  = headPitch / RADIAN_CONV;
        this.Nose.xRot  = 0.2617994F + this.Head.xRot;
        this.Nose.yRot  = this.Head.yRot;

        // Leg swing
        float RLegXRot = Mth.cos((limbSwing) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing) * 0.8F * limbSwingAmount;

        this.RLeg.yRot       = RLegXRot;
        this.RFingers.yRot   = this.RLeg.yRot;
        this.LLeg.yRot       = LLegXRot;
        this.LFingers.yRot   = this.LLeg.yRot;
        this.RRearLeg.yRot   = -0.5235988F + LLegXRot;
        this.LRearLeg.yRot   =  0.5235988F + RLegXRot;

        // Tail sway based on leg movement
        this.Tail.zRot = this.LLeg.xRot * 0.625F;
    }

    /**
     * Render all parts, applying the Y offset before drawing.
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
        poseStack.pushPose();
        poseStack.translate(0.0F, this.yOffset, 0.0F);

        this.Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFingers.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFingers.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LRearLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RRearLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }
}
