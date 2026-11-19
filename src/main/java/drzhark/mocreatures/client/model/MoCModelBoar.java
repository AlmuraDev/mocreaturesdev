package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.neutral.MoCEntityBoar;
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
public class MoCModelBoar<T extends MoCEntityBoar> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "boar"), "main");

    private final ModelPart head;
    private final ModelPart trout;
    private final ModelPart tusks;
    private final ModelPart jaw;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart headMane;
    private final ModelPart body;
    private final ModelPart bodyMane;
    private final ModelPart tail;
    private final ModelPart upperLegRight;
    private final ModelPart lowerLegRight;
    private final ModelPart upperLegLeft;
    private final ModelPart lowerLegLeft;
    private final ModelPart upperHindLegRight;
    private final ModelPart lowerHindLegRight;
    private final ModelPart upperHindLegLeft;
    private final ModelPart lowerHindLegLeft;

    public MoCModelBoar(ModelPart root) {
        super(root);
        this.head               = root.getChild("head");
        this.trout              = root.getChild("trout");
        this.tusks              = root.getChild("tusks");
        this.jaw                = root.getChild("jaw");
        this.leftEar            = root.getChild("left_ear");
        this.rightEar           = root.getChild("right_ear");
        this.headMane           = root.getChild("head_mane");
        this.body               = root.getChild("body");
        this.bodyMane           = root.getChild("body_mane");
        this.tail               = root.getChild("tail");
        this.upperLegRight      = root.getChild("upper_leg_right");
        this.lowerLegRight      = root.getChild("lower_leg_right");
        this.upperLegLeft       = root.getChild("upper_leg_left");
        this.lowerLegLeft       = root.getChild("lower_leg_left");
        this.upperHindLegRight  = root.getChild("upper_hind_leg_right");
        this.lowerHindLegRight  = root.getChild("lower_hind_leg_right");
        this.upperHindLegLeft   = root.getChild("upper_hind_leg_left");
        this.lowerHindLegLeft   = root.getChild("lower_hind_leg_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3F, 0F, -5F, 6, 6, 5),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.2617994F, 0F, 0F)
        );

        // Trout (snout extension)
        root.addOrReplaceChild("trout",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1.5F, 1.5F, -9.5F, 3, 3, 5),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.3490659F, 0F, 0F)
        );

        // Tusks
        root.addOrReplaceChild("tusks",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-2F, 3F, -8F, 4, 2, 1),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.3490659F, 0F, 0F)
        );

        // Jaw
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                        .texOffs(0, 19)
                        .addBox(-1F, 4.9F, -8.5F, 2, 1, 4),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.2617994F, 0F, 0F)
        );

        // Left Ear
        root.addOrReplaceChild("left_ear",
                CubeListBuilder.create()
                        .texOffs(16, 11)
                        .addBox(1F, -4F, -2F, 2, 4, 2),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.6981317F, 0F, 0.3490659F)
        );

        // Right Ear
        root.addOrReplaceChild("right_ear",
                CubeListBuilder.create()
                        .texOffs(16, 17)
                        .addBox(-3F, -4F, -2F, 2, 4, 2),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.6981317F, 0F, -0.3490659F)
        );

        // Head Mane
        root.addOrReplaceChild("head_mane",
                CubeListBuilder.create()
                        .texOffs(23, 0)
                        .addBox(-1F, -2F, -5F, 2, 2, 5),
                PartPose.offsetAndRotation(0F, 11F, -5F, 0.4363323F, 0F, 0F)
        );

        // Body
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(-3.5F, 0F, 0F, 7, 8, 13),
                PartPose.offsetAndRotation(0F, 11F, -5F, -0.0872665F, 0F, 0F)
        );

        // Body Mane
        root.addOrReplaceChild("body_mane",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(-1F, -2F, -1F, 2, 2, 9),
                PartPose.offsetAndRotation(0F, 11.3F, -4F, -0.2617994F, 0F, 0F)
        );

        // Tail
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(60, 38)
                        .addBox(-0.5F, 0F, 0F, 1, 5, 1),
                PartPose.offsetAndRotation(0F, 13F, 7.5F, 0.0872665F, 0F, 0F)
        );

        // Upper Leg Right
        root.addOrReplaceChild("upper_leg_right",
                CubeListBuilder.create()
                        .texOffs(32, 21)
                        .addBox(-1F, -2F, -2F, 1, 5, 3),
                PartPose.offsetAndRotation(-3.5F, 16F, -2.5F, 0.1745329F, 0F, 0F)
        );

        // Lower Leg Right
        root.addOrReplaceChild("lower_leg_right",
                CubeListBuilder.create()
                        .texOffs(32, 29)
                        .addBox(-0.5F, 2F, -1F, 2, 6, 2),
                PartPose.offset( -3.5F, 16F, -2.5F)
        );

        // Upper Leg Left
        root.addOrReplaceChild("upper_leg_left",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(0F, -2F, -2F, 1, 5, 3),
                PartPose.offsetAndRotation(3.5F, 16F, -2.5F, 0.1745329F, 0F, 0F)
        );

        // Lower Leg Left
        root.addOrReplaceChild("lower_leg_left",
                CubeListBuilder.create()
                        .texOffs(24, 29)
                        .addBox(-1.5F, 2F, -1F, 2, 6, 2),
                PartPose.offset(3.5F, 16F, -2.5F)
        );

        // Upper Hind Leg Right
        root.addOrReplaceChild("upper_hind_leg_right",
                CubeListBuilder.create()
                        .texOffs(44, 21)
                        .addBox(-1.5F, -2F, -2F, 1, 5, 4),
                PartPose.offsetAndRotation(-3F, 16F, 5.5F, -0.2617994F, 0F, 0F)
        );

        // Lower Hind Leg Right
        root.addOrReplaceChild("lower_hind_leg_right",
                CubeListBuilder.create()
                        .texOffs(46, 30)
                        .addBox(-1F, 2F, 0F, 2, 6, 2),
                PartPose.offset(-3F, 16F, 5.5F)
        );

        // Upper Hind Leg Left
        root.addOrReplaceChild("upper_hind_leg_left",
                CubeListBuilder.create()
                        .texOffs(54, 21)
                        .addBox(0.5F, -2F, -2F, 1, 5, 4),
                PartPose.offsetAndRotation(3F, 16F, 5.5F, -0.2617994F, 0F, 0F)
        );

        // Lower Hind Leg Left
        root.addOrReplaceChild("lower_hind_leg_left",
                CubeListBuilder.create()
                        .texOffs(56, 30)
                        .addBox(-1F, 2F, 0F, 2, 6, 2),
                PartPose.offset(3F, 16F, 5.5F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        trout.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tusks.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        jaw.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        headMane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        bodyMane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        upperLegRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lowerLegRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        upperLegLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lowerLegLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        upperHindLegRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lowerHindLegRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        upperHindLegLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lowerHindLegLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Convert head pitch/yaw into radians
        float xAngle = headPitch * Mth.DEG_TO_RAD;
        float yAngle = netHeadYaw * Mth.DEG_TO_RAD;

        // Head + associated parts follow headPitch/yaw
        head.xRot      = 0.2617994F + xAngle;
        head.yRot      = yAngle;
        headMane.xRot  = 0.4363323F + xAngle;
        headMane.yRot  = yAngle;
        trout.xRot     = 0.3490659F + xAngle;
        trout.yRot     = yAngle;
        jaw.xRot       = 0.2617994F + xAngle;
        jaw.yRot       = yAngle;
        tusks.xRot     = 0.3490659F + xAngle;
        tusks.yRot     = yAngle;
        leftEar.xRot   = 0.6981317F + xAngle;
        leftEar.yRot   = yAngle;
        rightEar.xRot  = 0.6981317F + xAngle;
        rightEar.yRot  = yAngle;

        // Leg swing:   cos(limbSwing * 0.6662) * 1.4 * limbSwingAmount
        float leftLegRot  = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        float rightLegRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 1.4F * limbSwingAmount;

        // Front legs
        upperLegLeft.xRot      = leftLegRot;
        lowerLegLeft.xRot      = leftLegRot;
        upperHindLegRight.xRot = leftLegRot;
        lowerHindLegRight.xRot = leftLegRot;

        upperLegRight.xRot      = rightLegRot;
        lowerLegRight.xRot      = rightLegRot;
        upperHindLegLeft.xRot   = rightLegRot;
        lowerHindLegLeft.xRot   = rightLegRot;

        // Tail wags proportional to front leg movement
        tail.zRot = leftLegRot * 0.2F;
    }
}
