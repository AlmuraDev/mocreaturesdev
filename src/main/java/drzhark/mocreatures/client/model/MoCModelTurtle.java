/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityTurtle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MoCModelTurtle<T extends MoCEntityTurtle> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "turtle"), "main"
    );

    private final ModelPart Shell;
    private final ModelPart ShellUp;
    private final ModelPart ShellTop;
    private final ModelPart Belly;
    private final ModelPart Leg1;
    private final ModelPart Leg2;
    private final ModelPart Leg3;
    private final ModelPart Leg4;
    private final ModelPart Head;
    private final ModelPart Tail;
    
    public boolean isHiding;
    public boolean upsidedown;
    public float swingProgress;
    public boolean turtleHat;
    public boolean TMNT;
    public boolean isSwimming;

    public MoCModelTurtle(ModelPart root) {
        super(root);
        this.Shell = root.getChild("Shell");
        this.ShellUp = root.getChild("ShellUp");
        this.ShellTop = root.getChild("ShellTop");
        this.Belly = root.getChild("Belly");
        this.Leg1 = root.getChild("Leg1");
        this.Leg2 = root.getChild("Leg2");
        this.Leg3 = root.getChild("Leg3");
        this.Leg4 = root.getChild("Leg4");
        this.Head = root.getChild("Head");
        this.Tail = root.getChild("Tail");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        // Shell (base)
        part.addOrReplaceChild("Shell",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(0F, 0F, 0F, 9, 1, 9),
                PartPose.offset(-4.5F, 19F, -4.5F)
        );

        // ShellUp (middle)
        part.addOrReplaceChild("ShellUp",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(0F, 0F, 0F, 8, 2, 8),
                PartPose.offset(-4F, 17F, -4F)
        );

        // ShellTop (top of shell, only shown if not TMNT)
        part.addOrReplaceChild("ShellTop",
                CubeListBuilder.create()
                        .texOffs(40, 10)
                        .addBox(0F, 0F, 0F, 6, 1, 6),
                PartPose.offset(-3F, 16F, -3F)
        );

        // Belly
        part.addOrReplaceChild("Belly",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0F, 0F, 0F, 8, 1, 8),
                PartPose.offset(-4F, 20F, -4F)
        );

        // Leg1 (front right)
        part.addOrReplaceChild("Leg1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offset(3.5F, 20F, -3.5F)
        );

        // Leg2 (front left)
        part.addOrReplaceChild("Leg2",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offset(-3.5F, 20F, -3.5F)
        );

        // Leg3 (rear right)
        part.addOrReplaceChild("Leg3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offset(3.5F, 20F, 3.5F)
        );

        // Leg4 (rear left)
        part.addOrReplaceChild("Leg4",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offset(-3.5F, 20F, 3.5F)
        );

        // Head
        part.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(-1.5F, -1F, -4F, 3, 2, 4),
                PartPose.offset(0F, 20F, -4.5F)
        );

        // Tail
        part.addOrReplaceChild("Tail",
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(-1F, -1F, 0F, 2, 1, 3),
                PartPose.offset(0F, 21F, 4F)
        );

        // Texture size: width=64, height=32 (same as in 1.16.5)
        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * Replaces the old setLivingAnimations. Use this to pull boolean flags from the entity before animating.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.TMNT = entity.isTMNT();
        this.turtleHat = entity.getVehicle() != null;
        this.isSwimming = entity.isInWater();
    }

    /**
     * Replaces the old setRotationAngles. Called every tick to apply rotations/offsets to each ModelPart.
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // ==== Upside-down logic ====
        if (this.upsidedown) {
            float f25 = this.swingProgress;
            float f26 = f25;
            if (f25 >= 0.8F) {
                f26 = (0.6F - (f25 - 0.8F));
            }
            if (f26 > 1.6F) {
                f26 = 1.6F;
            }
            if (f26 < -1.6F) {
                f26 = -1.6F;
            }
            this.Leg1.xRot = -f26;
            this.Leg2.xRot =  f26;
            this.Leg3.xRot =  f26;
            this.Leg4.xRot = -f26;
            this.Tail.yRot = -f26;

        } else if (this.turtleHat) {
            // Riding/hatch logic: all legs flat
            this.Leg1.xRot = 0F;
            this.Leg2.xRot = 0F;
            this.Leg3.xRot = 0F;
            this.Leg4.xRot = 0F;
            this.Tail.yRot = 0F;

        } else if (this.isSwimming) {
            // Swimming animation
            float swimmLegs = Mth.cos(limbSwing * 0.5F) * 6.0F * limbSwingAmount;
            this.Leg1.xRot = -1.2F;
            this.Leg1.yRot = -1.2F + swimmLegs;
            this.Leg2.xRot = -1.2F;
            this.Leg2.yRot =  1.2F - swimmLegs;
            this.Leg3.xRot =  1.2F;
            this.Leg4.xRot =  1.2F;
            this.Tail.yRot = 0F;

        } else {
            // Walking animation
            this.Leg1.xRot = Mth.cos(limbSwing * 2.0F) * 2.0F * limbSwingAmount;
            this.Leg2.xRot = Mth.cos((limbSwing * 2.0F) + (float)Math.PI) * 2.0F * limbSwingAmount;
            this.Leg3.xRot = Mth.cos((limbSwing * 2.0F) + (float)Math.PI) * 2.0F * limbSwingAmount;
            this.Leg4.xRot = Mth.cos(limbSwing * 2.0F) * 2.0F * limbSwingAmount;
            this.Tail.yRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;

            this.Leg1.yRot = 0F;
            this.Leg2.yRot = 0F;
        }

        // ==== Hiding vs. Normal posture ====
        if (this.isHiding && !this.isSwimming) {
            // Legs pulled in
            this.Head.xRot = 0F;
            this.Head.yRot = 0F;

            this.Leg1.setPos(2.9F, 18.5F, -2.9F);
            this.Leg2.setPos(-2.9F, 18.5F, -2.9F);
            this.Leg3.setPos(2.9F, 18.5F, 2.9F);
            this.Leg4.setPos(-2.9F, 18.5F, 2.9F);

            this.Head.setPos(0F, 19.5F, -1F);
            this.Tail.setPos(0F, 21F, 2F);

        } else {
            // Normal posture: head/legs/tail in standard places
            this.Head.xRot = headPitch * ((float)Math.PI / 180F);
            this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);

            this.Leg1.setPos(3.5F, 20F, -3.5F);
            this.Leg2.setPos(-3.5F, 20F, -3.5F);
            this.Leg3.setPos(3.5F, 20F, 3.5F);
            this.Leg4.setPos(-3.5F, 20F, 3.5F);

            this.Head.setPos(0F, 20F, -4.5F);
            this.Tail.setPos(0F, 21F, 4F);
        }
    }

    /**
     * Renders all the parts. Called by your renderer’s render() method.
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.Shell.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ShellUp.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        if (!this.TMNT) {
            this.ShellTop.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        this.Belly.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
