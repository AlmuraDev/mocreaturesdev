/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 *
 * Ported to Minecraft 1.20.1:
 *  - ModelRenderer → ModelPart
 *  - setRotationAngles(...) → setupAnim(...)
 *  - rotateAngleX/Y/Z → xRot, yRot, zRot
 *  - render(...) → renderToBuffer(...)
 *  - Added createBodyLayer(...) to register the LayerDefinition
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityRoach;
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
public class MoCModelRoach<T extends MoCEntityRoach> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "roach"),
            "main"
    );

    private static final float RADIAN = 57.29578F;

    private final ModelPart head;
    private final ModelPart lAntenna;
    private final ModelPart lAntennaB;
    private final ModelPart rAntenna;
    private final ModelPart rAntennaB;

    private final ModelPart thorax;
    private final ModelPart frontLegs;
    private final ModelPart midLegs;
    private final ModelPart rearLegs;
    private final ModelPart abdomen;
    private final ModelPart tailL;
    private final ModelPart tailR;

    private final ModelPart lShellClosed;
    private final ModelPart rShellClosed;
    private final ModelPart lShellOpen;
    private final ModelPart rShellOpen;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    private boolean flying;

    public MoCModelRoach(ModelPart root) {
        super(root);
        this.head         = root.getChild("head");
        this.lAntenna     = head.getChild("l_antenna");
        this.lAntennaB    = lAntenna.getChild("l_antenna_b");
        this.rAntenna     = head.getChild("r_antenna");
        this.rAntennaB    = rAntenna.getChild("r_antenna_b");

        this.thorax       = root.getChild("thorax");
        this.frontLegs    = root.getChild("front_legs");
        this.midLegs      = root.getChild("mid_legs");
        this.rearLegs     = root.getChild("rear_legs");
        this.abdomen      = root.getChild("abdomen");
        this.tailL        = root.getChild("tail_l");
        this.tailR        = root.getChild("tail_r");

        this.lShellClosed = root.getChild("l_shell_closed");
        this.rShellClosed = root.getChild("r_shell_closed");
        this.lShellOpen   = root.getChild("l_shell_open");
        this.rShellOpen   = root.getChild("r_shell_open");
        this.leftWing     = root.getChild("left_wing");
        this.rightWing    = root.getChild("right_wing");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1, 1, 2),
                PartPose.offset(0.0F, 23.0F, -2.0F)
        );
        // Initial rotation: rotateX = –2.171231 rad
        head.addOrReplaceChild("l_antenna",
                CubeListBuilder.create()
                        .texOffs(3, 21).addBox(0.0F, 0.0F, 0.0F, 4, 0, 1),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -2.171231F - (90F / RADIAN), 0.4363323F, 0.0F)
                // Equivalent to setRotation(-90°/rad, +0.4363323, 0)
        );
        PartDefinition lAntenna = head.getChild("l_antenna");
        lAntenna.addOrReplaceChild("l_antenna_b",
                CubeListBuilder.create()
                        .texOffs(4, 21).addBox(0.0F, 0.0F, 1.0F, 3, 0, 1),
                PartPose.offsetAndRotation(2.5F, 0.0F, -0.5F, 0.0F, 0.7853982F, 0.0F)
                // rotateY = +45°
        );

        head.addOrReplaceChild("r_antenna",
                CubeListBuilder.create()
                        .texOffs(3, 19).addBox(-4.5F, 0.0F, 0.0F, 4, 0, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.171231F - (90F / RADIAN), -0.4363323F, 0.0F)
                // rotateX = –2.171231 rad, rotateY = –0.4363323
        );
        PartDefinition rAntenna = head.getChild("r_antenna");
        rAntenna.addOrReplaceChild("r_antenna_b",
                CubeListBuilder.create()
                        .texOffs(4, 19).addBox(-4.0F, 0.0F, 1.0F, 3, 0, 1),
                PartPose.offsetAndRotation(-2.5F, 0.0F, 0.5F, 0.0F, -0.7853982F, 0.0F)
                // rotateY = –45°
        );

        // THORAX
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create()
                        .texOffs(0, 3).addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2),
                PartPose.offset(0.0F, 22.0F, -1.0F)
        );

        // FRONT LEGS (rotateX = –1.115358 rad)
        root.addOrReplaceChild("front_legs",
                CubeListBuilder.create()
                        .texOffs(0, 11).addBox(-2.0F, 0.0F, 0.0F, 4, 2, 0),
                PartPose.offsetAndRotation(0.0F, 23.0F, -1.8F, -1.115358F, 0.0F, 0.0F)
        );

        // MID LEGS (rotateX = +1.264073 rad)
        root.addOrReplaceChild("mid_legs",
                CubeListBuilder.create()
                        .texOffs(0, 13).addBox(-2.5F, 0.0F, 0.0F, 5, 2, 0),
                PartPose.offsetAndRotation(0.0F, 23.0F, -1.2F, 1.264073F, 0.0F, 0.0F)
        );

        // REAR LEGS (rotateX = +1.368173 rad)
        root.addOrReplaceChild("rear_legs",
                CubeListBuilder.create()
                        .texOffs(0, 15).addBox(-2.0F, 0.0F, 0.0F, 4, 4, 0),
                PartPose.offsetAndRotation(0.0F, 23.0F, -0.4F, 1.368173F, 0.0F, 0.0F)
        );

        // ABDOMEN (rotateX = +1.427659 rad)
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create()
                        .texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 1),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 1.427659F, 0.0F, 0.0F)
        );

        // TAIL L (rotateX = +1.554066F, rotateY = +0.6457718F)
        root.addOrReplaceChild("tail_l",
                CubeListBuilder.create()
                        .texOffs(2, 29).addBox(-0.5F, 0.0F, 0.0F, 1, 2, 0),
                PartPose.offsetAndRotation(0.0F, 23.0F, 3.6F, 1.554066F, 0.6457718F, 0.0F)
        );

        // TAIL R (rotateX = +1.554066F, rotateY = –0.6457718F)
        root.addOrReplaceChild("tail_r",
                CubeListBuilder.create()
                        .texOffs(0, 29).addBox(-0.5F, 0.0F, 0.0F, 1, 2, 0),
                PartPose.offsetAndRotation(0.0F, 23.0F, 3.6F, 1.554066F, -0.6457718F, 0.0F)
        );

        // LEFT SHELL CLOSED (rotateX = –0.1487144F, rotateY = –0.0872665F, rotateZ = +0.1919862F)
        root.addOrReplaceChild("l_shell_closed",
                CubeListBuilder.create()
                        .texOffs(4, 23).addBox(0.0F, 0.0F, 0.0F, 2, 0, 6),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, -0.1487144F, -0.0872665F, 0.1919862F)
        );

        // RIGHT SHELL CLOSED (rotateX = –0.1487144F, rotateY = +0.0872665F, rotateZ = –0.1919862F)
        root.addOrReplaceChild("r_shell_closed",
                CubeListBuilder.create()
                        .texOffs(0, 23).addBox(-2.0F, 0.0F, 0.0F, 2, 0, 6),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, -0.1487144F, 0.0872665F, -0.1919862F)
        );

        // LEFT SHELL OPEN (rotateX = +1.117011F, rotateY = –0.0872665F, rotateZ = +1.047198F)
        root.addOrReplaceChild("l_shell_open",
                CubeListBuilder.create()
                        .texOffs(4, 23).addBox(0.0F, 0.0F, 0.0F, 2, 0, 6),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, 1.117011F, -0.0872665F, 1.047198F)
        );

        // RIGHT SHELL OPEN (rotateX = +1.117011F, rotateY = +0.0872665F, rotateZ = –1.047198F)
        root.addOrReplaceChild("r_shell_open",
                CubeListBuilder.create()
                        .texOffs(0, 23).addBox(-2.0F, 0.0F, 0.0F, 2, 0, 6),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, 1.117011F, 0.0872665F, -1.047198F)
        );

        // LEFT WING (rotateY = –1.047198F, rotateZ = –0.4363323F)
        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create()
                        .texOffs(11, 21).addBox(0.0F, 1.0F, -1.0F, 6, 0, 2),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, 0.0F, -1.047198F, -0.4363323F)
        );

        // RIGHT WING (rotateY = +1.047198F, rotateZ = +0.4363323F)
        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create()
                        .texOffs(11, 19).addBox(-6.0F, 1.0F, -1.0F, 6, 0, 2),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.5F, 0.0F, 1.047198F, 0.4363323F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack,
                               VertexConsumer vertexConsumer,
                               int packedLight,
                               int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Render opaque parts first
        renderOpaqueParts(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        
        // Render transparent parts with blending if needed
        if (shouldRenderPartialTransparency()) {
            poseStack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.clearColor(getTransparencyColor()[0], getTransparencyColor()[1], getTransparencyColor()[2], getTransparencyValue());
            
            renderTransparentParts(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            
            RenderSystem.disableBlend();
            poseStack.popPose();
            // Reset shader color to opaque white
            RenderSystem.clearColor(1F, 1F, 1F, 1F);
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Always render head, legs, thorax, abdomen, tails
        head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        midLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rearLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tailL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tailR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (!flying) {
            lShellClosed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rShellClosed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            lShellOpen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rShellOpen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render wings with transparency
        leftWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.6F; // 60% transparency for wings
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return this.flying; // Only when flying
    }

    @Override
    public void setupAnim(T entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

        // Determine flying state
        this.flying = entity.getIsFlying() || entity.getDeltaMovement().y() < -0.1D;

        // HEAD rotation (base –2.171231F plus headPitch)
        head.xRot = -2.171231F + (headPitch / RADIAN);

        // ANTENNAWAVE: 5°/RAD + (limbSwingAmount × 1.5)
        float antMov = (5F / RADIAN) + (limbSwingAmount * 1.5F);
        lAntenna.zRot = -antMov;
        rAntenna.zRot = +antMov;

        float legMov, legMovB, frontLegAdj = 0F;

        if (this.flying) {
            // Wing flap by ageInTicks
            float wingRot = Mth.cos(ageInTicks * 2.0F) * 0.7F;
            rightWing.yRot = 1.047198F + wingRot;
            leftWing.yRot  = -1.047198F - wingRot;

            legMov  = limbSwingAmount * 1.5F;
            legMovB = legMov;
            frontLegAdj = 1.4F;
        } else {
            legMov  = Mth.cos((limbSwing * 1.5F) + (float)Math.PI) * 0.6F * limbSwingAmount;
            legMovB = Mth.cos(limbSwing * 1.5F) * 0.8F * limbSwingAmount;
        }

        // FRONT_LEGS: base rotating –1.115358F plus movement & optional adjustment
        frontLegs.xRot = -1.115358F + frontLegAdj + legMov;

        // MID_LEGS: base +1.264073F + legMovB
        midLegs.xRot   = 1.264073F + legMovB;

        // REAR_LEGS: base +1.368173F – adjustment + legMov
        rearLegs.xRot  = 1.368173F - frontLegAdj + legMov;
    }
}
