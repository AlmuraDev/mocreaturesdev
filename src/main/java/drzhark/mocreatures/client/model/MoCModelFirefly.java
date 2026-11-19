package drzhark.mocreatures.client.model;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import drzhark.mocreatures.entity.ambient.MoCEntityFirefly;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

public class MoCModelFirefly<T extends MoCEntityFirefly> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "firefly"), "main"
    );

    private final ModelPart antenna;
    private final ModelPart rearLegs;
    private final ModelPart midLegs;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart abdomen;
    private final ModelPart frontLegs;
    private final ModelPart rightShellOpen;
    private final ModelPart leftShellOpen;
    private final ModelPart thorax;
    private final ModelPart rightShell;
    private final ModelPart leftShell;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    private boolean flying;
    private boolean day;

    public MoCModelFirefly(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        this.head            = root.getChild("head");
        this.antenna         = root.getChild("antenna");
        this.thorax          = root.getChild("thorax");
        this.abdomen         = root.getChild("abdomen");
        this.tail            = root.getChild("tail");
        this.frontLegs       = root.getChild("front_legs");
        this.midLegs         = root.getChild("mid_legs");
        this.rearLegs        = root.getChild("rear_legs");
        this.rightShellOpen  = root.getChild("right_shell_open");
        this.leftShellOpen   = root.getChild("left_shell_open");
        this.rightShell      = root.getChild("right_shell");
        this.leftShell       = root.getChild("left_shell");
        this.leftWing        = root.getChild("left_wing");
        this.rightWing       = root.getChild("right_wing");
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
        // Determine whether it is flying or it is “daytime”
        this.flying = entity.getIsFlying() || entity.getDeltaMovement().y < -0.1D;
        this.day    = entity.level().isBrightOutside();

        float legMov, legMovB, frontLegAdj = 0F;
        if (this.flying) {
            // Wing flapping
            float wingRot = Mth.cos(ageInTicks * 1.8F) * 0.8F;
            this.rightWing.zRot = wingRot;
            this.leftWing.zRot  = -wingRot;

            legMov  = limbSwingAmount * 1.5F;
            legMovB = legMov;
            frontLegAdj = 1.4F;
        } else {
            legMov  = Mth.cos(limbSwing * 1.5F + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        // Legs
        this.frontLegs.xRot = -0.8328009F + frontLegAdj + legMov;
        this.midLegs.xRot   =  1.070744F + legMovB;
        this.rearLegs.xRot  =  1.249201F + legMov;

        // If not flying, legs sway forward/back as it walks
        if (!this.flying) {
            this.frontLegs.yRot = 0F;
            this.midLegs.yRot   = 0F;
            this.rearLegs.yRot  = 0F;
        }

        // Head always rotates toward yaw/pitch
        this.head.xRot      = headPitch * ((float)Math.PI / 180F);
        this.head.yRot      = netHeadYaw * ((float)Math.PI / 180F);
        this.head.zRot      = 0F;
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
        // Antenna, legs, head, abdomen, front legs, thorax, tail
        this.antenna.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rearLegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midLegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.abdomen.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.frontLegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.thorax.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (!this.flying) {
            // When not flying, render closed shells
            this.rightShell.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.leftShell.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            // Flying: open shells + wings with partial alpha
            this.rightShellOpen.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.leftShellOpen.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            poseStack.pushPose();
            float transparency = 0.6F;
            this.leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            poseStack.popPose();
        }

        // Glow/alpha effect: if it is nighttime, wing/tail glow is more transparent; else use additive
        poseStack.pushPose();
        if (!this.day) {
            float alphaGlow = 0.4F;
        } else {
        }
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1F, 0F, -1F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -2F, -2.171231F, 0F, 0F)
        );

        // ANTENNA
        root.addOrReplaceChild("antenna",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1F, 0F, 0F, 2, 1, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -3F, -1.665602F, 0F, 0F)
        );

        // THORAX
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 21F, -1F)
        );

        // ABDOMEN
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-1F, 0F, -1F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22F, 0F, 1.427659F, 0F, 0F)
        );

        // TAIL
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(8, 17)
                        .addBox(-1F, 0.5F, -1F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 21.3F, 1.5F, 1.13023F, 0F, 0F)
        );

        // FRONT LEGS
        root.addOrReplaceChild("front_legs",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, -1.8F, -0.8328009F, 0F, 0F)
        );

        // MID LEGS
        root.addOrReplaceChild("mid_legs",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, -1.2F, 1.070744F, 0F, 0F)
        );

        // REAR LEGS
        root.addOrReplaceChild("rear_legs",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, 0F, 2, 3, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, -0.4F, 1.249201F, 0F, 0F)
        );

        // RIGHT SHELL OPEN
        root.addOrReplaceChild("right_shell_open",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1F, 21F, -2F, 1.22F, 0F, -0.6457718F)
        );

        // LEFT SHELL OPEN
        root.addOrReplaceChild("left_shell_open",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1F, 21F, -2F, 1.22F, 0F, 0.6457718F)
        );

        // RIGHT SHELL (closed)
        root.addOrReplaceChild("right_shell",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1F, 21F, -2F, 0.0174533F, 0F, -0.6457718F)
        );

        // LEFT SHELL (closed)
        root.addOrReplaceChild("left_shell",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1F, 21F, -2F, 0.0174533F, 0F, 0.6457718F)
        );

        // LEFT WING
        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create()
                        .texOffs(15, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1F, 21F, -1F, 0F, 1.047198F, 0F)
        );

        // RIGHT WING
        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create()
                        .texOffs(15, 12)
                        .addBox(-1F, 0F, 0F, 2, 0, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1F, 21F, -1F, 0F, -1.047198F, 0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }
}
