/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityFly;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MoCModelFly<T extends MoCEntityFly> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "fly"), "main"
    );

    private final ModelPart frontLegs;
    private final ModelPart rearLegs;
    private final ModelPart midLegs;
    private final ModelPart foldedWings;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart abdomen;
    private final ModelPart rightWing;
    private final ModelPart thorax;
    private final ModelPart leftWing;

    private boolean flying;

    public MoCModelFly(ModelPart root) {
        super(root);
        this.frontLegs   = root.getChild("front_legs");
        this.rearLegs    = root.getChild("rear_legs");
        this.midLegs     = root.getChild("mid_legs");
        this.foldedWings = root.getChild("folded_wings");
        this.head        = root.getChild("head");
        this.tail        = root.getChild("tail");
        this.abdomen     = root.getChild("abdomen");
        this.rightWing   = root.getChild("right_wing");
        this.thorax      = root.getChild("thorax");
        this.leftWing    = root.getChild("left_wing");
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Determine “flying” state
        this.flying = entity.getIsFlying() || entity.getDeltaMovement().y < -0.1D;

        // Wing flapping
        float wingRot = Mth.cos(ageInTicks * 3.0F) * 0.7F;
        this.rightWing.zRot = wingRot;
        this.leftWing.zRot  = -wingRot;

        float legMov, legMovB;
        if (this.flying) {
            legMov  = limbSwingAmount * 1.5F;
            legMovB = legMov;
        } else {
            legMov  = Mth.cos(limbSwing * 1.5F + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        this.frontLegs.xRot = 0.1487144F + legMov;
        this.midLegs.xRot   = 0.5948578F + legMovB;
        this.rearLegs.xRot  = 1.070744F + legMov;

        // Head/tail/abdomen/thorax stay static except in initial pose, so no per-tick rotation here.
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        // Render opaque parts first
        renderOpaqueParts(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        
        // Render transparent parts with blending if needed
        if (shouldRenderPartialTransparency()) {
            poseStack.pushPose();
            
            renderTransparentParts(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            
            poseStack.popPose();
        } else {
            // Render folded wings as opaque when not flying
            this.foldedWings.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Draw legs and body parts
        this.frontLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rearLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render open wings with transparency
        this.leftWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.6F; // 60% transparency for wings
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return this.flying; // Only when flying
    }

    /**
     * Defines geometry + texture size for MoCModelFly.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition root    = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1F, 0F, -1F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 21.5F, -2F, -2.171231F, 0F, 0F)
        );

        // THORAX
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 20.5F, -1F)
        );

        // ABDOMEN
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-1F, 0F, -1F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 21.5F, 0F, 1.427659F, 0F, 0F)
        );

        // TAIL
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(10, 2)
                        .addBox(-1F, 0F, -1F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, 21.2F, 1.5F, 1.427659F, 0F, 0F)
        );

        // FRONT LEGS
        root.addOrReplaceChild("front_legs",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -1.8F, 0.1487144F, 0F, 0F)
        );

        // REAR LEGS
        root.addOrReplaceChild("rear_legs",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -0.4F, 1.070744F, 0F, 0F)
        );

        // MID LEGS
        root.addOrReplaceChild("mid_legs",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -1.2F, 0.5948578F, 0F, 0F)
        );

        // LEFT WING (open)
        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create()
                        .texOffs(4, 4)
                        .addBox(-1F, 0F, 0.5F, 2, 0, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.4F, -1F, 0F, 1.047198F, 0F)
        );

        // RIGHT WING (open)
        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create()
                        .texOffs(4, 4)
                        .addBox(-1F, 0F, 0.5F, 2, 0, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.4F, -1F, 0F, -1.047198F, 0F)
        );

        // FOLDED WINGS
        root.addOrReplaceChild("folded_wings",
                CubeListBuilder.create()
                        .texOffs(4, 4)
                        .addBox(-1F, 0F, 0F, 2, 0, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.5F, -2F, 0.0872665F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }
}
