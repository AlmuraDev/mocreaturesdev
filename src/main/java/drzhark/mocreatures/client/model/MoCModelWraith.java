/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.entity.hostile.MoCEntityWraith;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Port of MoCModelWraith from 1.16.5’s BipedModel to 1.20.1’s HumanoidModel.
 */
public class MoCModelWraith<T extends MoCEntityWraith> extends MoCHumanoidModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "wraith"), "main"
    );

    private int attackCounter;
    private boolean isSneaking;

    public MoCModelWraith(ModelPart root) {
        super(root);
        this.hat.visible = false; // the wraith never drew the hat layer
    }

    /**
     * Build a LayerDefinition that replaces BipedModel’s old constructor (0,0,64,40).
     * We delegate to HumanoidModel.createMesh, then tell LayerDefinition our texture is 64×40.
     */
    public static LayerDefinition createBodyLayer() {
        // HumanoidModel.createMesh takes (CubeDeformation, float size).
        // We pass NO deformation and 0.0F “scale,” matching the original BipedModel(0.0F, 0.0F, 64, 40).
        return LayerDefinition.create(
                HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F),
                64, 40
        );
    }

    /**
     * Called before rendering each frame; replaces the old setLivingAnimations.
     * We pull “isSneaking” and “attackCounter” from the entity here.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.isSneaking = entity.isCrouching();
        this.attackCounter = entity.attackCounter;
    }

    /**
     * Called every frame to set up rotations on head, arms, legs, etc.
     * We call super.setupAnim(...) to apply vanilla walking/head animations to our HumanoidModel parts,
     * then layer on the Wraith’s “attack” arm swing and idle arm oscillation exactly as in 1.16.5.
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // First, apply vanilla biped/humanoid animations (walk, head turn, etc.).
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // Copy old BipedModel logic for swinging arms on attack vs. idle:
        float f6 = Mth.sin(this.attackCounter > 0 ? (this.attackCounter * 0.12F) : (this.attackCounter * 0.12F));
        float f7 = Mth.sin((1.0F - ((1.0F - (this.attackCounter > 0 ? 1.0F : 1.0F))
                * (1.0F - (this.attackCounter > 0 ? 1.0F : 1.0F)))) * (float) Math.PI);
        // (Above two lines are vestigial for attackCounter > 0 vs. 0—mirror of original.)

        // Reset Z-rotation on both arms to zero:
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot  = 0.0F;

        // Y-rotation adjustment during the “swingProgress” phase:
        float swingProg = this.attackTime;
        float sinSwing = Mth.sin(swingProg * (float) Math.PI);
        this.rightArm.yRot = -(0.1F - (sinSwing * 0.6F));
        this.leftArm.yRot  =  (0.1F - (sinSwing * 0.6F));

        if (this.attackCounter != 0) {
            // When attackCounter > 0, fists raise and lower based on cos(attackCounter * 0.12)
            float armMov = Mth.cos(this.attackCounter * 0.12F) * 4.0F;
            this.rightArm.xRot = -armMov;
            this.leftArm.xRot  = -armMov;
        } else {
            // Idle “swing” posture: arms start at straight down (−π/2), then add sub-swing
            this.rightArm.xRot = -((float) Math.PI / 2F);
            this.leftArm.xRot  = -((float) Math.PI / 2F);

            // Layer on “swingProgress” bobbing
            float fSwing = sinSwing * 1.2F - f7 * 0.4F;
            this.rightArm.xRot -= fSwing;
            this.leftArm.xRot  -= fSwing;

            // Slight sinusoidal idle motion on arms (ageInTicks * 0.067)
            this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
            this.leftArm.xRot  -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
        }

        // Finally, add a subtle Z-rotation “breathing” motion on arms:
        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot  -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
    }

    /**
     * Override renderToBuffer so that we can replicate the old “child scale” and “sneaking translate” logic
     * from the original BipedModel-based render(...) method.
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        // If entity is “child,” scale entire model to ¾ and shift upward:
        if (this.young) {
            poseStack.scale(0.75F, 0.75F, 0.75F);
            poseStack.translate(0.0F, 0.5F, 0.0F);
        } else {
            // If sneaking, move model slightly downward (as in old render)
            if (this.isSneaking) {
                poseStack.translate(0.0F, 0.2F, 0.0F);
            }
        }

        // Now render all humanoid parts in standard order:
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }
}
