/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityKomodo;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.client.model.EntityModel;

/**
 * Port of MoCModelKomodo (1.16.5) to Minecraft 1.20.1.
 */
public class MoCModelKomodo<T extends MoCEntityKomodo> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "komodo"), "main"
    );

    private final float radianF = 57.29578F;

    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart nose;
    private final ModelPart mouth;
    private final ModelPart tongue;

    private final ModelPart chest;
    private final ModelPart abdomen;

    private final ModelPart tail;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;

    private final ModelPart legFrontLeft;
    private final ModelPart legFrontLeft1;
    private final ModelPart legFrontLeft2;
    private final ModelPart legFrontLeft3;

    private final ModelPart legBackLeft;
    private final ModelPart legBackLeft1;
    private final ModelPart legBackLeft2;
    private final ModelPart legBackLeft3;

    private final ModelPart legFrontRight;
    private final ModelPart legFrontRight1;
    private final ModelPart legFrontRight2;
    private final ModelPart legFrontRight3;

    private final ModelPart legBackRight;
    private final ModelPart legBackRight1;
    private final ModelPart legBackRight2;
    private final ModelPart legBackRight3;

    private final ModelPart saddleA;
    private final ModelPart saddleB;
    private final ModelPart saddleC;

    private boolean isRideable;

    public MoCModelKomodo(ModelPart root) {
        super(root);
        this.head            = root.getChild("Head");
        this.neck            = head.getChild("Neck");
        this.nose            = neck.getChild("Nose");
        this.mouth           = neck.getChild("Mouth");
        this.tongue          = mouth.getChild("Tongue");

        this.chest           = root.getChild("Chest");
        this.abdomen         = root.getChild("Abdomen");

        this.tail            = root.getChild("Tail");
        this.tail1           = tail.getChild("Tail1");
        this.tail2           = tail1.getChild("Tail2");
        this.tail3           = tail2.getChild("Tail3");
        this.tail4           = tail3.getChild("Tail4");

        this.legFrontLeft    = root.getChild("LegFrontLeft");
        this.legFrontLeft1   = legFrontLeft.getChild("LegFrontLeft1");
        this.legFrontLeft2   = legFrontLeft1.getChild("LegFrontLeft2");
        this.legFrontLeft3   = legFrontLeft2.getChild("LegFrontLeft3");

        this.legBackLeft     = root.getChild("LegBackLeft");
        this.legBackLeft1    = legBackLeft.getChild("LegBackLeft1");
        this.legBackLeft2    = legBackLeft1.getChild("LegBackLeft2");
        this.legBackLeft3    = legBackLeft2.getChild("LegBackLeft3");

        this.legFrontRight   = root.getChild("LegFrontRight");
        this.legFrontRight1  = legFrontRight.getChild("LegFrontRight1");
        this.legFrontRight2  = legFrontRight1.getChild("LegFrontRight2");
        this.legFrontRight3  = legFrontRight2.getChild("LegFrontRight3");

        this.legBackRight    = root.getChild("LegBackRight");
        this.legBackRight1   = legBackRight.getChild("LegBackRight1");
        this.legBackRight2   = legBackRight1.getChild("LegBackRight2");
        this.legBackRight3   = legBackRight2.getChild("LegBackRight3");

        this.saddleA         = root.getChild("SaddleA");
        this.saddleB         = root.getChild("SaddleB");
        this.saddleC         = root.getChild("SaddleC");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh     = new MeshDefinition();
        PartDefinition root     = mesh.getRoot();

        // Head and children
        PartDefinition head = root.addOrReplaceChild("Head",
                CubeListBuilder.create(),
                PartPose.offset(0F, 13F, -8F)
        );
        PartDefinition neck = head.addOrReplaceChild("Neck",
                CubeListBuilder.create()
                        .texOffs(22, 34)
                        .addBox(-2F, 0F, -6F, 4, 5, 6),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition nose = neck.addOrReplaceChild("Nose",
                CubeListBuilder.create()
                        .texOffs(24, 45)
                        .addBox(-1.5F, -1F, -6.5F, 3, 2, 6),
                PartPose.offset(0F, 1F, -5F)
        );
        PartDefinition mouth = neck.addOrReplaceChild("Mouth",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1F, -0.3F, -5F, 2, 1, 6),
                PartPose.offset(0F, 3F, -5.8F)
        );
        mouth.addOrReplaceChild("Tongue",
                CubeListBuilder.create()
                        .texOffs(48, 44)
                        .addBox(-1.5F, 0F, -5F, 3, 0, 5),
                PartPose.offset(0F, -0.4F, -4.7F)
        );

        // Chest
        root.addOrReplaceChild("Chest",
                CubeListBuilder.create()
                        .texOffs(36, 2)
                        .addBox(-3F, 0F, -8F, 6, 6, 7),
                PartPose.offset(0F, 13F, 0F)
        );

        // Abdomen
        root.addOrReplaceChild("Abdomen",
                CubeListBuilder.create()
                        .texOffs(36, 49)
                        .addBox(-3F, 0F, -1F, 6, 7, 8),
                PartPose.offset(0F, 13F, 0F)
        );

        // Tail hierarchy
        PartDefinition tail = root.addOrReplaceChild("Tail",
                CubeListBuilder.create(),
                PartPose.offset(0F, 13F, 7F)
        );
        PartDefinition tail1 = tail.addOrReplaceChild("Tail1",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2F, 0F, 0F, 4, 5, 8),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition tail2 = tail1.addOrReplaceChild("Tail2",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-1.5F, 0F, 0F, 3, 4, 8),
                PartPose.offset(0F, 0.1F, 7.7F)
        );
        PartDefinition tail3 = tail2.addOrReplaceChild("Tail3",
                CubeListBuilder.create()
                        .texOffs(0, 46)
                        .addBox(-1F, 0F, 0F, 2, 3, 8),
                PartPose.offset(0F, 0.1F, 7.3F)
        );
        tail3.addOrReplaceChild("Tail4",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-0.5F, 0F, 0F, 1, 2, 8),
                PartPose.offset(0F, 0.1F, 7F)
        );

        // Front-left leg
        PartDefinition legFrontLeft = root.addOrReplaceChild("LegFrontLeft",
                CubeListBuilder.create(),
                PartPose.offset(2F, 17F, -7F)
        );
        PartDefinition legFrontLeft1 = legFrontLeft.addOrReplaceChild("LegFrontLeft1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, -1F, -1.5F, 4, 3, 3),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition legFrontLeft2 = legFrontLeft1.addOrReplaceChild("LegFrontLeft2",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
                PartPose.offset(3F, 0.5F, 0F)
        );
        legFrontLeft2.addOrReplaceChild("LegFrontLeft3",
                CubeListBuilder.create()
                        .texOffs(16, 58)
                        .addBox(-1.5F, 0F, -3.5F, 3, 1, 5),
                PartPose.offsetAndRotation(0F, 4F, 0F, 0F, -10F * ((float)Math.PI / 180F), 0F)
        );

        // Back-left leg
        PartDefinition legBackLeft = root.addOrReplaceChild("LegBackLeft",
                CubeListBuilder.create(),
                PartPose.offset(2F, 17F, 6F)
        );
        PartDefinition legBackLeft1 = legBackLeft.addOrReplaceChild("LegBackLeft1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, -1F, -1.5F, 4, 3, 3),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition legBackLeft2 = legBackLeft1.addOrReplaceChild("LegBackLeft2",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
                PartPose.offset(3F, 0.5F, 0F)
        );
        legBackLeft2.addOrReplaceChild("LegBackLeft3",
                CubeListBuilder.create()
                        .texOffs(16, 58)
                        .addBox(-1.5F, 0F, -3.5F, 3, 1, 5),
                PartPose.offsetAndRotation(0F, 4F, 0F, 0F, -10F * ((float)Math.PI / 180F), 0F)
        );

        // Front-right leg
        PartDefinition legFrontRight = root.addOrReplaceChild("LegFrontRight",
                CubeListBuilder.create(),
                PartPose.offset(-2F, 17F, -7F)
        );
        PartDefinition legFrontRight1 = legFrontRight.addOrReplaceChild("LegFrontRight1",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-4F, -1F, -1.5F, 4, 3, 3),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition legFrontRight2 = legFrontRight1.addOrReplaceChild("LegFrontRight2",
                CubeListBuilder.create()
                        .texOffs(22, 7)
                        .addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
                PartPose.offset(-3F, 0.5F, 0F)
        );
        legFrontRight2.addOrReplaceChild("LegFrontRight3",
                CubeListBuilder.create()
                        .texOffs(0, 58)
                        .addBox(-1.5F, 0F, -3.5F, 3, 1, 5),
                PartPose.offsetAndRotation(0F, 4F, 0F, 0F, 10F * ((float)Math.PI / 180F), 0F)
        );

        // Back-right leg
        PartDefinition legBackRight = root.addOrReplaceChild("LegBackRight",
                CubeListBuilder.create(),
                PartPose.offset(-2F, 17F, 6F)
        );
        PartDefinition legBackRight1 = legBackRight.addOrReplaceChild("LegBackRight1",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-4F, -1F, -1.5F, 4, 3, 3),
                PartPose.offset(0F, 0F, 0F)
        );
        PartDefinition legBackRight2 = legBackRight1.addOrReplaceChild("LegBackRight2",
                CubeListBuilder.create()
                        .texOffs(22, 7)
                        .addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
                PartPose.offset(-3F, 0.5F, 0F)
        );
        legBackRight2.addOrReplaceChild("LegBackRight3",
                CubeListBuilder.create()
                        .texOffs(0, 58)
                        .addBox(-1.5F, 0F, -3.5F, 3, 1, 5),
                PartPose.offsetAndRotation(0F, 4F, 0F, 0F, 10F * ((float)Math.PI / 180F), 0F)
        );

        // Saddle pieces
        root.addOrReplaceChild("SaddleA",
                CubeListBuilder.create()
                        .texOffs(36, 28)
                        .addBox(-2.5F, 0.5F, -4F, 5, 1, 8),
                PartPose.offset(0F, 12F, 0F)
        );
        root.addOrReplaceChild("SaddleC",
                CubeListBuilder.create()
                        .texOffs(36, 37)
                        .addBox(-2.5F, 0F, 2F, 5, 1, 2),
                PartPose.offset(0F, 12F, 0F)
        );
        root.addOrReplaceChild("SaddleB",
                CubeListBuilder.create()
                        .texOffs(54, 37)
                        .addBox(-1.5F, 0F, -4F, 3, 1, 2),
                PartPose.offset(0F, 12F, 0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        boolean mouth     = (entityIn.mouthCounter != 0);
        boolean sitting   = entityIn.getIsSitting();
        boolean swimming  = entityIn.isSwimming();
        boolean moveTail  = (entityIn.tailCounter != 0);
        boolean tongue    = (entityIn.tongueCounter != 0);

        float tailXRot    = Mth.cos(limbSwing * 0.4F) * 0.2F * limbSwingAmount;
        float LLegXRot    = Mth.cos(limbSwing * 1.2F) * 1.2F * limbSwingAmount;
        float RLegXRot    = Mth.cos((limbSwing * 1.2F) + (float)Math.PI) * 1.2F * limbSwingAmount;

        if (netHeadYaw > 60F) netHeadYaw = 60F;
        if (netHeadYaw < -60F) netHeadYaw = -60F;

        float adjustY = 0F;
        if (swimming) {
            adjustY = 4F;
            tail1.xRot = (0F / radianF) - tailXRot;
            legFrontLeft1.zRot = 0F / radianF;
            legFrontLeft2.zRot = -65F / radianF;
            legFrontLeft1.yRot = -80F / radianF;

            legBackLeft1.zRot = 0F / radianF;
            legBackLeft2.zRot = -65F / radianF;
            legBackLeft1.yRot = -80F / radianF;

            legFrontRight1.zRot = 0F / radianF;
            legFrontRight2.zRot = 65F / radianF;
            legFrontRight1.yRot = 80F / radianF;

            legBackRight1.zRot = 0F / radianF;
            legBackRight2.zRot = 65F / radianF;
            legBackRight1.yRot = 80F / radianF;
        } else if (sitting) {
            adjustY = 4F;
            tail1.xRot = (-5F / radianF) - tailXRot;
            legFrontLeft1.zRot = -30F / radianF;
            legFrontLeft2.zRot = 0F / radianF;
            legFrontLeft1.yRot = 0F;

            legBackLeft1.zRot = 0F / radianF;
            legBackLeft2.zRot = -65F / radianF;
            legBackLeft1.yRot = -40F / radianF;

            legFrontRight1.zRot = 30F / radianF;
            legFrontRight2.zRot = 0F / radianF;
            legFrontRight1.yRot = 0F;

            legBackRight1.zRot = 0F / radianF;
            legBackRight2.zRot = 65F / radianF;
            legBackRight1.yRot = 40F / radianF;
        } else {
            tail1.xRot = (-15F / radianF) - tailXRot;
            legFrontLeft1.zRot = 30F / radianF;
            legFrontLeft2.zRot = -30F / radianF;
            legFrontLeft1.yRot = LLegXRot;
            legFrontLeft2.xRot = -LLegXRot;

            legBackLeft1.zRot = 30F / radianF;
            legBackLeft2.zRot = -30F / radianF;
            legBackLeft1.yRot = RLegXRot;
            legBackLeft2.xRot = -RLegXRot;

            legFrontRight1.zRot = -30F / radianF;
            legFrontRight2.zRot = 30F / radianF;
            legFrontRight1.yRot = -RLegXRot;
            legFrontRight2.xRot = -RLegXRot;

            legBackRight1.zRot = -30F / radianF;
            legBackRight2.zRot = 30F / radianF;
            legBackRight1.yRot = -LLegXRot;
            legBackRight2.xRot = -LLegXRot;
        }

        // Adjust Y pivots
        tail.y            = adjustY + 13F;
        head.y            = adjustY + 13F;
        chest.y           = adjustY + 13F;
        legFrontLeft.y    = adjustY + 17F;
        legBackLeft.y     = adjustY + 17F;
        legFrontRight.y   = adjustY + 17F;
        legBackRight.y    = adjustY + 17F;
        abdomen.y        = adjustY + 13F;
        saddleA.y         = adjustY + 12F;
        saddleB.y         = adjustY + 12F;
        saddleC.y         = adjustY + 12F;

        // Tongue and mouth animation
        float tongueF = 0F;
        if (!mouth && tongue) {
            tongueF = (Mth.cos(ageInTicks * 3F) / 10F);
            this.tongue.z = -4.7F;
        } else {
            this.tongue.z = 0.3F;
        }

        float mouthF = 0F;
        if (mouth) {
            mouthF = 35F / radianF;
            this.tongue.z = -0.8F;
        }

        neck.xRot  = 11F / radianF + (headPitch * 0.33F / radianF);
        nose.xRot  = 10.6F / radianF + (headPitch * 0.66F / radianF);
        this.mouth.xRot = mouthF + (-3F / radianF) + (headPitch * 0.66F / radianF);
        this.tongue.xRot= tongueF;

        neck.yRot  = (netHeadYaw * 0.33F / radianF);
        nose.yRot  = (netHeadYaw * 0.66F / radianF);
        this.mouth.yRot = (netHeadYaw * 0.66F / radianF);

        tail2.xRot = (-17F / radianF) + tailXRot;
        tail3.xRot = (13F / radianF) + tailXRot;
        tail4.xRot = (11F / radianF) + tailXRot;

        float t = limbSwing / 2F;
        if (moveTail) {
            t = ageInTicks / 4F;
        }
        float A = 0.35F;
        float w = 0.6F;
        float k = 0.6F;
        int i = 0;
        float tailLat;

        tailLat = A * Mth.sin(w * t - k * (i++));
        tail1.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * (i++));
        tail2.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * (i++));
        tail3.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * (i++));
        tail4.yRot = tailLat;

        this.isRideable = entityIn.getIsRideable();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack,
                               VertexConsumer buffer,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {
        tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        legFrontLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        legBackLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        legFrontRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        legBackRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (isRideable) {
            saddleA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            saddleC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            saddleB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
}
 