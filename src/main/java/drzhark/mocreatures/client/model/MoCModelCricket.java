package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityCricket;
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


public class MoCModelCricket<T extends MoCEntityCricket> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "cricket"), "main"
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

    private boolean flying;

    public MoCModelCricket(ModelPart root) {
        super(root);
        this.head       = root.getChild("head");
        this.antenna    = root.getChild("antenna");
        this.antennaB   = root.getChild("antennaB");
        this.thorax     = root.getChild("thorax");
        this.abdomen    = root.getChild("abdomen");
        this.tailA      = root.getChild("tailA");
        this.tailB      = root.getChild("tailB");
        this.frontLegs  = root.getChild("frontLegs");
        this.midLegs    = root.getChild("midLegs");
        this.thighLeft  = root.getChild("thighLeft");
        this.thighLeftB = root.getChild("thighLeftB");
        this.thighRight = root.getChild("thighRight");
        this.thighRightB= root.getChild("thighRightB");
        this.legLeft    = root.getChild("legLeft");
        this.legLeftB   = root.getChild("legLeftB");
        this.legRight   = root.getChild("legRight");
        this.legRightB  = root.getChild("legRightB");
    }

    /**
     * Builds exactly the same cubes/rotations as your old ModelRenderer-based constructor.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-0.5F,  0F, -1F,  1, 1, 2),
                PartPose.offsetAndRotation(0F, 22.5F, -2F, -2.171231F, 0F, 0F)
        );

        // ANTENNA (first segment)
        root.addOrReplaceChild("antenna",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1F,  0F,  0F,  2, 2, 0),
                PartPose.offsetAndRotation(0F, 22.5F, -3F, -2.736346F, 0F, 0F)
        );

        // ANTENNAB (second segment)
        root.addOrReplaceChild("antennaB",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F,  0F,  0F,  2, 2, 0),
                PartPose.offsetAndRotation(0F, 20.7F, -3.8F, 2.88506F, 0F, 0F)
        );

        // THORAX
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F,  0F, -1F,  2, 2, 2),
                PartPose.offsetAndRotation(0F, 21F, -1F, 0F, 0F, 0F)
        );

        // ABDOMEN
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-1F,  0F, -1F,  2, 3, 2),
                PartPose.offsetAndRotation(0F, 22F, 0F, 1.427659F, 0F, 0F)
        );

        // TAIL A
        root.addOrReplaceChild("tailA",
                CubeListBuilder.create()
                        .texOffs(4, 9)
                        .addBox(-1F,  0F,  0F,  2, 3, 0),
                PartPose.offsetAndRotation(0F, 22F, 2.8F, 1.308687F, 0F, 0F)
        );

        // TAIL B
        root.addOrReplaceChild("tailB",
                CubeListBuilder.create()
                        .texOffs(4, 7)
                        .addBox(-1F,  0F,  0F,  2, 2, 0),
                PartPose.offsetAndRotation(0F, 23F, 2.8F, 1.665602F, 0F, 0F)
        );

        // FRONT LEGS
        root.addOrReplaceChild("frontLegs",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1F,  0F,  0F,  2, 2, 0),
                PartPose.offsetAndRotation(0F, 23F, -1.8F, -0.8328009F, 0F, 0F)
        );

        // MID LEGS
        root.addOrReplaceChild("midLegs",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-2F,  0F,  0F,  4, 2, 0),
                PartPose.offsetAndRotation(0F, 23F, -1.2F, 1.070744F, 0F, 0F)
        );

        // THIGH LEFT (upper portion)
        root.addOrReplaceChild("thighLeft",
                CubeListBuilder.create()
                        .texOffs(8, 5)
                        .addBox(0F,  -3F,  0F,  1, 3, 1),
                PartPose.offsetAndRotation(0.5F, 23F, 0F, -0.4886922F, 0.2617994F, 0F)
        );

        // THIGH LEFT B (folded variant)
        root.addOrReplaceChild("thighLeftB",
                CubeListBuilder.create()
                        .texOffs(8, 5)
                        .addBox(0F,  -3F,  0F,  1, 3, 1),
                PartPose.offsetAndRotation(0.5F, 22.5F, 0F, -1.762782F, 0F, 0F)
        );

        // THIGH RIGHT (upper portion)
        root.addOrReplaceChild("thighRight",
                CubeListBuilder.create()
                        .texOffs(12, 5)
                        .addBox(-1F, -3F,  0F,  1, 3, 1),
                PartPose.offsetAndRotation(-0.5F, 23F, 0F, -0.4886922F, -0.2617994F, 0F)
        );

        // THIGH RIGHT B (folded variant)
        root.addOrReplaceChild("thighRightB",
                CubeListBuilder.create()
                        .texOffs(12, 5)
                        .addBox(-1F, -3F,  0F,  1, 3, 1),
                PartPose.offsetAndRotation(-0.5F, 22.5F, 0F, -1.762782F, 0F, 0F)
        );

        // LEG LEFT (lower portion)
        root.addOrReplaceChild("legLeft",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(0F,  0F, -1F,  0, 3, 2),
                PartPose.offsetAndRotation(2F, 21F, 2.5F, 0F, 0F, 0F)
        );

        // LEG LEFT B (folded)
        root.addOrReplaceChild("legLeftB",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F,  0F, -1F,  0, 3, 2),
                PartPose.offsetAndRotation(1.5F, 23F, 2.9F, 1.249201F, 0F, 0F)
        );

        // LEG RIGHT (lower portion)
        root.addOrReplaceChild("legRight",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F,  0F, -1F,  0, 3, 2),
                PartPose.offsetAndRotation(-2F, 21F, 2.5F, 0F, 0F, 0F)
        );

        // LEG RIGHT B (folded)
        root.addOrReplaceChild("legRightB",
                CubeListBuilder.create()
                        .texOffs(4, 15)
                        .addBox(0F,  0F, -1F,  0, 3, 2),
                PartPose.offsetAndRotation(-1.5F, 23F, 2.9F, 1.249201F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // exactly the same logic you had:
        this.flying = entity.getIsFlying() || entity.getDeltaMovement().y < -0.1D;

        float legMov;
        float legMovB;
        float frontLegAdj = 0F;

        if (this.flying) {
            legMov   = limbSwingAmount * 1.5F;
            legMovB  = legMov;
            frontLegAdj = 1.4F;
        } else {
            legMov   = Mth.cos((limbSwing * 1.5F) + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB  = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        // ANTENNA B (animates with leg movement)
        this.antennaB.xRot = 2.88506F - legMov;

        // FRONT LEGS
        this.frontLegs.xRot = -0.8328009F + frontLegAdj + legMov;
        this.midLegs.xRot   = 1.070744F + legMovB;

        // (The folded variants are only visible during render(), so no other changes here)
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
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
            this.thighLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.thighLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            // (I had a translucent‐wing‐like blend around here; removed because Crickets don’t have wings)
            // If I ever want to reintroduce blending:
            poseStack.pushPose();
            poseStack.popPose();
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
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
            this.thighLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.thighLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.thighRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legLeftB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.legRightB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Crickets don't have wings, so no transparent parts to render
        // This method is required by the interface but not used for crickets
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.6F; // Default transparency value (not used for crickets)
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return false; // Crickets don't have transparent parts
    }
}
