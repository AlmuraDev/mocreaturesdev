/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import drzhark.mocreatures.entity.aquatic.MoCEntityJellyFish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.16.5 MoCModelJellyFish to Minecraft 1.20.1.
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelJellyFish<T extends MoCEntityJellyFish> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "jellyfish"), "main"
    );

    protected final ModelPart Top;
    protected final ModelPart Head;
    protected final ModelPart HeadSmall;
    protected final ModelPart Body;
    protected final ModelPart BodyCenter;
    protected final ModelPart BodyBottom;
    protected final ModelPart Side1;
    protected final ModelPart Side2;
    protected final ModelPart Side3;
    protected final ModelPart Side4;
    protected final ModelPart LegSmall1;
    protected final ModelPart LegC1;
    protected final ModelPart LegC2;
    protected final ModelPart LegC3;
    protected final ModelPart Leg1;
    protected final ModelPart Leg2;
    protected final ModelPart Leg3;
    protected final ModelPart Leg4;
    protected final ModelPart Leg5;
    protected final ModelPart Leg6;
    protected final ModelPart Leg7;
    protected final ModelPart Leg8;
    protected final ModelPart Leg9;

    private boolean glowing;
    private boolean outOfWater;
    private float limbSwingAmount;

    public MoCModelJellyFish(ModelPart root) {
        super(root);
        this.Top         = root.getChild("Top");
        this.Head        = root.getChild("Head");
        this.HeadSmall   = root.getChild("HeadSmall");
        this.Body        = root.getChild("Body");
        this.BodyCenter  = root.getChild("BodyCenter");
        this.BodyBottom  = root.getChild("BodyBottom");
        this.Side1       = root.getChild("Side1");
        this.Side2       = root.getChild("Side2");
        this.Side3       = root.getChild("Side3");
        this.Side4       = root.getChild("Side4");
        this.LegSmall1   = root.getChild("LegSmall1");
        this.LegC1       = root.getChild("LegC1");
        this.LegC2       = root.getChild("LegC2");
        this.LegC3       = root.getChild("LegC3");
        this.Leg1        = root.getChild("Leg1");
        this.Leg2        = root.getChild("Leg2");
        this.Leg3        = root.getChild("Leg3");
        this.Leg4        = root.getChild("Leg4");
        this.Leg5        = root.getChild("Leg5");
        this.Leg6        = root.getChild("Leg6");
        this.Leg7        = root.getChild("Leg7");
        this.Leg8        = root.getChild("Leg8");
        this.Leg9        = root.getChild("Leg9");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh     = new MeshDefinition();
        PartDefinition root     = mesh.getRoot();

        // Top dome
        root.addOrReplaceChild("Top",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-2.5F, 0F, -2.5F, 5, 1, 5),
                PartPose.offset(0F, 11F, 0F));

        // Large head ring
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, 0F, -4F, 8, 2, 8),
                PartPose.offset(0F, 12F, 0F));

        // Smaller head segment
        root.addOrReplaceChild("HeadSmall",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(-2F, 0F, -2F, 4, 3, 4),
                PartPose.offset(0F, 12.5F, 0F));

        // Bulbous body
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(36, 0)
                        .addBox(-3.5F, 0F, -3.5F, 7, 7, 7),
                PartPose.offset(0F, 13.8F, 0F));

        // Central column
        root.addOrReplaceChild("BodyCenter",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offset(0F, 15.5F, 0F));

        // Bottom plate
        root.addOrReplaceChild("BodyBottom",
                CubeListBuilder.create()
                        .texOffs(20, 10)
                        .addBox(-2F, 0F, -2F, 4, 2, 4),
                PartPose.offset(0F, 18.3F, 0F));

        // Side panels
        root.addOrReplaceChild("Side1",
                CubeListBuilder.create()
                        .texOffs(20, 10)
                        .addBox(-2F, 5F, 0F, 4, 2, 4),
                PartPose.offsetAndRotation(0F, 12.5F, 0F, -0.7679449F, 0F, 0F));

        root.addOrReplaceChild("Side2",
                CubeListBuilder.create()
                        .texOffs(20, 10)
                        .addBox(-4F, 5F, -2F, 4, 2, 4),
                PartPose.offsetAndRotation(0F, 12.5F, 0F, 0F, 0F, -0.7679449F));

        root.addOrReplaceChild("Side3",
                CubeListBuilder.create()
                        .texOffs(20, 10)
                        .addBox(0F, 5F, -2F, 4, 2, 4),
                PartPose.offsetAndRotation(0F, 12.5F, 0F, 0F, 0F, 0.7679449F));

        root.addOrReplaceChild("Side4",
                CubeListBuilder.create()
                        .texOffs(20, 10)
                        .addBox(-2F, 5F, -4F, 4, 2, 4),
                PartPose.offsetAndRotation(0F, 12.5F, 0F, 0.7679449F, 0F, 0F));

        // Small central leg
        root.addOrReplaceChild("LegSmall1",
                CubeListBuilder.create()
                        .texOffs(60, 2)
                        .addBox(-1F, 0F, -1F, 1, 3, 1),
                PartPose.offset(0F, 18.5F, 0F));

        // Curved legs around center
        root.addOrReplaceChild("LegC1",
                CubeListBuilder.create()
                        .texOffs(15, 10)
                        .addBox(-1F, 0F, -1F, 1, 4, 1),
                PartPose.offsetAndRotation(-0.5F, 15.5F, -0.5F, -0.2602503F, 0F, 0.1487144F));

        root.addOrReplaceChild("LegC2",
                CubeListBuilder.create()
                        .texOffs(15, 10)
                        .addBox(-1F, 0F, 0F, 1, 4, 1),
                PartPose.offsetAndRotation(0.5F, 15.5F, -0.5F, 0.1487144F, 1.747395F, 0F));

        root.addOrReplaceChild("LegC3",
                CubeListBuilder.create()
                        .texOffs(15, 10)
                        .addBox(-1F, 0F, 0F, 1, 4, 1),
                PartPose.offsetAndRotation(-0.5F, 15.5F, 0.5F, 0.1115358F, 0.3717861F, 0.2230717F));

        // Outer legs
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offset(0F, 20F, 2.5F));

        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offset(0F, 20F, -2.5F));

        root.addOrReplaceChild("Leg3",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offset(2.5F, 20F, 0F));

        root.addOrReplaceChild("Leg4",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offset(-2.5F, 20F, 0F));

        // Diagonal legs at 45°
        root.addOrReplaceChild("Leg5",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offsetAndRotation(2F, 20F, 2F, 0F, 0.7853982F, 0F));

        root.addOrReplaceChild("Leg6",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offsetAndRotation(2F, 20F, -2F, 0F, 0.7853982F, 0F));

        root.addOrReplaceChild("Leg7",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offsetAndRotation(-2F, 20F, -2F, 0F, 0.7853982F, 0F));

        root.addOrReplaceChild("Leg8",
                CubeListBuilder.create()
                        .texOffs(60, 0)
                        .addBox(0F, 0F, 0F, 1, 5, 1),
                PartPose.offset(0F, 18.5F, 0F));

        root.addOrReplaceChild("Leg9",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-0.5F, 0F, -0.5F, 1, 4, 1),
                PartPose.offsetAndRotation(-2F, 20F, 2F, 0F, 0.7853982F, 0F));

        return LayerDefinition.create(mesh, 64, 16);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.glowing          = entityIn.isGlowing();
        this.outOfWater       = !entityIn.isInWater();
        this.limbSwingAmount  = limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
                               float red, float green, float blue, float alpha) {
        // Apply jellyfish-specific transformations
        matrixStackIn.pushPose();
        
        // Apply water-based positioning and rotation
        // Note: The renderer will apply scale(1.0F, -1.0F, 1.0F) and translate(0.0D, -1.501F, 0.0D)
        // So we need to account for that in our positioning
        if (outOfWater) {
            // When out of water, adjust for the renderer's transformations to place on ground
            matrixStackIn.translate(0F, -2.201F, 0F);
        } else {
            // When in water, adjust for the renderer's transformations
            matrixStackIn.translate(0F, -0.2F, 0F);
            matrixStackIn.rotate(Axis.XP.rotationDegrees(this.limbSwingAmount * -60F));
        }
        
        // Render opaque parts first (none for jellyfish)
        renderOpaqueParts(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        
        // Render transparent parts with blending
        if (shouldRenderPartialTransparency()) {
            renderTransparentParts(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, getTransparencyValue());
        }
        
        matrixStackIn.popPose();
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Jellyfish has no opaque parts - everything is transparent
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render all jellyfish parts with transparency
        this.Top.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.HeadSmall.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.BodyCenter.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.BodyBottom.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Side1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Side2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Side3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Side4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LegSmall1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LegC1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LegC2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LegC3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg6.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg7.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg8.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg9.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.7F; // 70% transparency for jellyfish
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return true; // Jellyfish is always transparent
    }
}
