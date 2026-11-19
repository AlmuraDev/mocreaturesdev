/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import drzhark.mocreatures.entity.ambient.MoCEntityGrasshopper;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelGrasshopper<T extends MoCEntityGrasshopper> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "grasshopper"), "main"
    );

    private final ModelPart head;
    private final ModelPart antenna;
    private final ModelPart antennaB;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart tailA;
    private final ModelPart tailB;
    private final ModelPart frontLegs;
    private final ModelPart midLegs;
    private final ModelPart thighLeft;
    private final ModelPart thighLeftB;
    private final ModelPart thighRight;
    private final ModelPart thighRightB;
    private final ModelPart legLeft;
    private final ModelPart legLeftB;
    private final ModelPart legRight;
    private final ModelPart legRightB;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart foldedWings;

    private boolean flying;

    public MoCModelGrasshopper(ModelPart root) {
        super(root);
        this.head         = root.getChild("head");
        this.antenna      = root.getChild("antenna");
        this.antennaB     = root.getChild("antennaB");
        this.thorax       = root.getChild("thorax");
        this.abdomen      = root.getChild("abdomen");
        this.tailA        = root.getChild("tailA");
        this.tailB        = root.getChild("tailB");
        this.frontLegs    = root.getChild("frontLegs");
        this.midLegs      = root.getChild("midLegs");
        this.thighLeft    = root.getChild("thighLeft");
        this.thighLeftB   = root.getChild("thighLeftB");
        this.thighRight   = root.getChild("thighRight");
        this.thighRightB  = root.getChild("thighRightB");
        this.legLeft      = root.getChild("legLeft");
        this.legLeftB     = root.getChild("legLeftB");
        this.legRight     = root.getChild("legRight");
        this.legRightB    = root.getChild("legRightB");
        this.leftWing     = root.getChild("leftWing");
        this.rightWing    = root.getChild("rightWing");
        this.foldedWings  = root.getChild("foldedWings");
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
        // Determine flying state
        this.flying = entity.getIsFlying() || entity.getDeltaMovement().y < -0.1D;

        float legMov, legMovB, frontLegAdj = 0F;
        if (this.flying) {
            float wingRot = Mth.cos(ageInTicks * 2.0F) * 0.7F;
            this.rightWing.zRot = wingRot;
            this.leftWing.zRot  = -wingRot;
            legMov  = limbSwingAmount * 1.5F;
            legMovB = legMov;
            frontLegAdj = 1.4F;
        } else {
            legMov  = Mth.cos(limbSwing * 1.5F + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        // AntennaB swings opposite to legMov
        this.antennaB.xRot = 2.88506F - legMov;

        // Front and mid legs movement
        this.frontLegs.xRot = -0.8328009F + frontLegAdj + legMov;
        this.midLegs.xRot   =  1.070744F + legMovB;
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
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.clearColor(getTransparencyColor()[0], getTransparencyColor()[1], getTransparencyColor()[2], getTransparencyValue());
            
            renderTransparentParts(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            
            RenderSystem.disableBlend();
            poseStack.popPose();
        } else {
            // Render folded wings as opaque when not flying
            this.foldedWings.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render head and body
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.antenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.antennaB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.frontLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (!this.flying) {
            // Grounded pose
            this.thighLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            // Flying pose
            this.thighLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render wings with transparency
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

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition root    = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-0.5F, 0F, -1F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -2F, -2.171231F, 0F, 0F)
        );

        // ANTENNA
        root.addOrReplaceChild("antenna",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22.5F, -3F, -2.736346F, 0F, 0F)
        );

        // ANTENNA B
        root.addOrReplaceChild("antennaB",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.7F, -3.8F, 2.88506F, 0F, 0F)
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
                        .addBox(-1F, 0F, -1F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22F, 0F, 1.427659F, 0F, 0F)
        );

        // TAIL A
        root.addOrReplaceChild("tailA",
                CubeListBuilder.create()
                        .texOffs(4, 9)
                        .addBox(-1F, 0F, 0F, 2, 3, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 22F, 2.8F, 1.308687F, 0F, 0F)
        );

        // TAIL B
        root.addOrReplaceChild("tailB",
                CubeListBuilder.create()
                        .texOffs(4, 7)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, 2.8F, 1.665602F, 0F, 0F)
        );

        // FRONT LEGS
        root.addOrReplaceChild("frontLegs",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1F, 0F, 0F, 2, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, -1.8F, -0.8328009F, 0F, 0F)
        );

        // MID LEGS
        root.addOrReplaceChild("midLegs",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-2F, 0F, 0F, 4, 2, 0, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 23F, -1.2F, 1.070744F, 0F, 0F)
        );

        // THIGH LEFT
        root.addOrReplaceChild("thighLeft",
                CubeListBuilder.create()
                        .texOffs(8, 5)
                        .addBox(0F, -3F, 0F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, 23F, 0F, -0.4886922F, 0.2617994F, 0F)
        );

        // THIGH LEFT B (flying)
        root.addOrReplaceChild("thighLeftB",
                CubeListBuilder.create()
                        .texOffs(8, 5)
                        .addBox(0F, -3F, 0F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, 22.5F, 0F, -1.762782F, 0F, 0F)
        );

        // THIGH RIGHT
        root.addOrReplaceChild("thighRight",
                CubeListBuilder.create()
                        .texOffs(12, 5)
                        .addBox(-1F, -3F, 0F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, 23F, 0F, -0.4886922F, -0.2617994F, 0F)
        );

        // THIGH RIGHT B (flying)
        root.addOrReplaceChild("thighRightB",
                CubeListBuilder.create()
                        .texOffs(12, 5)
                        .addBox(-1F, -3F, 0F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, 22.5F, 0F, -1.762782F, 0F, 0F)
        );

        // LEG LEFT
        root.addOrReplaceChild("legLeft",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(0F, 0F, -1F, 0, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(2F, 21F, 2.5F)
        );

        // LEG LEFT B (flying)
        root.addOrReplaceChild("legLeftB",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F, 0F, -1F, 0, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 23F, 2.9F, 1.249201F, 0F, 0F)
        );

        // LEG RIGHT
        root.addOrReplaceChild("legRight",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F, 0F, -1F, 0, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 21F, 2.5F)
        );

        // LEG RIGHT B (flying)
        root.addOrReplaceChild("legRightB",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F, 0F, -1F, 0, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 23F, 2.9F, 1.249201F, 0F, 0F)
        );

        // LEFT WING
        root.addOrReplaceChild("leftWing",
                CubeListBuilder.create()
                        .texOffs(0, 30)
                        .addBox(0F, 0F, -1F, 6, 0, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.9F, -1F, 0F, -0.1919862F, 0F)
        );

        // RIGHT WING
        root.addOrReplaceChild("rightWing",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-6F, 0F, -1F, 6, 0, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.9F, -1F, 0F, 0.1919862F, 0F)
        );

        // FOLDED WINGS
        root.addOrReplaceChild("foldedWings",
                CubeListBuilder.create()
                        .texOffs(0, 26)
                        .addBox(0F, 0F, -1F, 6, 0, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 20.9F, -2F, 0F, -1.570796F, 0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }
}
