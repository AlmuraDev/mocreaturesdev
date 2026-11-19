/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 *
 * In 1.20.1:
 *  - ModelRenderer → ModelPart
 *  - setRotationAngles(...) → setupAnim(...)
 *  - rotateAngleX/Y/Z → xRot, yRot, zRot
 *  - render(...) → renderToBuffer(...)
 */

package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntityRat;
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
public class MoCModelRat<T extends MoCEntityRat> extends MoCEntityModel<T> {


    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "rat"),
            "main"
    );

    private final ModelPart head;
    private final ModelPart earR;
    private final ModelPart earL;
    private final ModelPart whiskerR;
    private final ModelPart whiskerL;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart frontL;
    private final ModelPart frontR;
    private final ModelPart rearL;
    private final ModelPart rearR;
    private final ModelPart bodyF;

    private final float radianF = 57.29578F;

    public MoCModelRat(ModelPart root) {
        super(root);
        // All child names must match exactly those defined in createBodyLayer()
        this.head      = root.getChild("head");
        this.earR      = root.getChild("ear_r");
        this.earL      = root.getChild("ear_l");
        this.whiskerR  = root.getChild("whisker_r");
        this.whiskerL  = root.getChild("whisker_l");
        this.body      = root.getChild("body");
        this.tail      = root.getChild("tail");
        this.frontL    = root.getChild("front_l");
        this.frontR    = root.getChild("front_r");
        this.rearL     = root.getChild("rear_l");
        this.rearR     = root.getChild("rear_r");
        this.bodyF     = root.getChild("body_f");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, -1.0F, -6.0F, 3, 4, 6),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        // EARS
        root.addOrReplaceChild("ear_r",
                CubeListBuilder.create()
                        .texOffs(16, 26).addBox(-3.5F, -3.0F, -2.0F, 3, 3, 1),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );
        root.addOrReplaceChild("ear_l",
                CubeListBuilder.create()
                        .texOffs(24, 26).addBox(0.5F, -3.0F, -2.0F, 3, 3, 1),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        // WHISKERS
        root.addOrReplaceChild("whisker_r",
                CubeListBuilder.create()
                        .texOffs(24, 16).addBox(-4.5F, -1.0F, -6.0F, 3, 3, 1),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );
        root.addOrReplaceChild("whisker_l",
                CubeListBuilder.create()
                        .texOffs(24, 20).addBox(1.5F, -1.0F, -6.0F, 3, 3, 1),
                PartPose.offset(0.0F, 19.0F, -6.0F)
        );

        // BODY (rotated 90° on X)
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-4.0F, -3.0F, -3.0F, 8, 8, 8),
                PartPose.offsetAndRotation(0.0F, 20.0F, 2.0F, (float)Math.PI / 2F, 0.0F, 0.0F)
        );

        // TAIL (rotated 90° on X)
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(56, 0).addBox(-1.0F,  0.0F, -1.0F, 2, 18, 2),
                PartPose.offsetAndRotation(0.0F, 20.0F, 7.0F, (float)Math.PI / 2F, 0.0F, 0.0F)
        );

        // FRONT LEGS
        root.addOrReplaceChild("front_l",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-2.0F, 0.0F, -3.0F, 2, 1, 4),
                PartPose.offset(3.0F, 23.0F, -5.0F)
        );
        root.addOrReplaceChild("front_r",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(0.0F, 0.0F, -3.0F, 2, 1, 4),
                PartPose.offset(-3.0F, 23.0F, -5.0F)
        );

        // REAR LEGS
        root.addOrReplaceChild("rear_l",
                CubeListBuilder.create()
                        .texOffs(0, 24).addBox(-2.0F, 0.0F, -4.0F, 2, 1, 5),
                PartPose.offset(4.0F, 23.0F, 4.0F)
        );
        root.addOrReplaceChild("rear_r",
                CubeListBuilder.create()
                        .texOffs(0, 24).addBox(0.0F, 0.0F, -4.0F, 2, 1, 5),
                PartPose.offset(-4.0F, 23.0F, 4.0F)
        );

        // BODY FRONT
        root.addOrReplaceChild("body_f",
                CubeListBuilder.create()
                        .texOffs(32, 16).addBox(-3.0F, -3.0F, -7.0F, 6, 6, 6),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        earR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        earL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        whiskerR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        whiskerL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rearL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rearR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        bodyF.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * In 1.20.1 this replaces setRotationAngles. 
     * Params: entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // HEAD rotation
        head.xRot = -headPitch * ((float)Math.PI / 180F);
        head.yRot = netHeadYaw * ((float)Math.PI / 180F);

        // Sync ears & whiskers with head
        earR.xRot      = head.xRot;
        earR.yRot      = head.yRot;
        earL.xRot      = head.xRot;
        earL.yRot      = head.yRot;
        whiskerR.xRot  = head.xRot;
        whiskerR.yRot  = head.yRot;
        whiskerL.xRot  = head.xRot;
        whiskerL.yRot  = head.yRot;

        // Leg animation: alternating front/back
        float frontLegX  = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        float rearLegX   = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
        float frontLegX2 = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
        float rearLegX2  = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;

        frontL.xRot = frontLegX;
        rearL.xRot  = rearLegX;
        rearR.xRot  = frontLegX2;
        frontR.xRot = rearLegX2;

        // Tail wags proportionally to one of the front legs
        tail.yRot = frontL.xRot * 0.625F;
    }
}
