/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntityWWolf;
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

public class MoCModelWolf<T extends MoCEntityWWolf> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "wolf"), "main"
    );

    private final ModelPart Head;
    private final ModelPart MouthB;
    private final ModelPart Nose2;
    private final ModelPart Neck;
    private final ModelPart Neck2;
    private final ModelPart LSide;
    private final ModelPart RSide;
    private final ModelPart Nose;
    private final ModelPart Mouth;
    private final ModelPart UTeeth;
    private final ModelPart LTeeth;
    private final ModelPart MouthOpen;
    private final ModelPart REar;
    private final ModelPart LEar;
    private final ModelPart Chest;
    private final ModelPart Body;
    private final ModelPart TailA;
    private final ModelPart TailB;
    private final ModelPart TailC;
    private final ModelPart TailD;
    private final ModelPart Leg1A;
    private final ModelPart Leg1B;
    private final ModelPart Leg1C;
    private final ModelPart Leg2A;
    private final ModelPart Leg2B;
    private final ModelPart Leg2C;
    private final ModelPart Leg3A;
    private final ModelPart Leg3B;
    private final ModelPart Leg3C;
    private final ModelPart Leg3D;
    private final ModelPart Leg4A;
    private final ModelPart Leg4B;
    private final ModelPart Leg4C;
    private final ModelPart Leg4D;

    private boolean openMouth;

    public MoCModelWolf(ModelPart root) {
        super(root);
        this.Head     = root.getChild("Head");
        this.MouthB   = root.getChild("MouthB");
        this.Nose2    = root.getChild("Nose2");
        this.Neck     = root.getChild("Neck");
        this.Neck2    = root.getChild("Neck2");
        this.LSide    = root.getChild("LSide");
        this.RSide    = root.getChild("RSide");
        this.Nose     = root.getChild("Nose");
        this.Mouth    = root.getChild("Mouth");
        this.UTeeth   = root.getChild("UTeeth");
        this.LTeeth   = root.getChild("LTeeth");
        this.MouthOpen= root.getChild("MouthOpen");
        this.REar     = root.getChild("REar");
        this.LEar     = root.getChild("LEar");
        this.Chest    = root.getChild("Chest");
        this.Body     = root.getChild("Body");
        this.TailA    = root.getChild("TailA");
        this.TailB    = root.getChild("TailB");
        this.TailC    = root.getChild("TailC");
        this.TailD    = root.getChild("TailD");
        this.Leg1A    = root.getChild("Leg1A");
        this.Leg1B    = root.getChild("Leg1B");
        this.Leg1C    = root.getChild("Leg1C");
        this.Leg2A    = root.getChild("Leg2A");
        this.Leg2B    = root.getChild("Leg2B");
        this.Leg2C    = root.getChild("Leg2C");
        this.Leg3A    = root.getChild("Leg3A");
        this.Leg3B    = root.getChild("Leg3B");
        this.Leg3C    = root.getChild("Leg3C");
        this.Leg3D    = root.getChild("Leg3D");
        this.Leg4A    = root.getChild("Leg4A");
        this.Leg4B    = root.getChild("Leg4B");
        this.Leg4C    = root.getChild("Leg4C");
        this.Leg4D    = root.getChild("Leg4D");
    }

    /**
     * Builds the LayerDefinition for this wolf model.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition part    = mesh.getRoot();

        // HEAD
        part.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -3F, -6F, 8, 8, 6),
                PartPose.offset(0F, 7F, -10F)
        );

        // MOUTHB
        part.addOrReplaceChild("MouthB",
                CubeListBuilder.create()
                        .texOffs(16, 33)
                        .addBox(-2F, 4F, -7F, 4, 1, 2),
                PartPose.offset(0F, 7F, -10F)
        );

        // NOSE2
        part.addOrReplaceChild("Nose2",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-2F, 2F, -12F, 4, 2, 6),
                PartPose.offset(0F, 7F, -10F)
        );

        // NECK
        part.addOrReplaceChild("Neck",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-3.5F, -3F, -7F, 7, 8, 7),
                PartPose.offsetAndRotation(0F, 10F, -6F, -0.4537856F, 0F, 0F)
        );

        // NECK2
        part.addOrReplaceChild("Neck2",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-1.5F, -2F, -5F, 3, 4, 7),
                PartPose.offsetAndRotation(0F, 14F, -10F, -0.4537856F, 0F, 0F)
        );

        // LSide (left sideburn)
        part.addOrReplaceChild("LSide",
                CubeListBuilder.create()
                        .texOffs(28, 33)
                        .addBox(3F, -0.5F, -2F, 2, 6, 6),
                PartPose.offsetAndRotation(0F, 7F, -10F, -0.2094395F,  0.418879F, -0.0872665F)
        );

        // RSide (right sideburn)
        part.addOrReplaceChild("RSide",
                CubeListBuilder.create()
                        .texOffs(28, 45)
                        .addBox(-5F, -0.5F, -2F, 2, 6, 6),
                PartPose.offsetAndRotation(0F, 7F, -10F, -0.2094395F, -0.418879F,  0.0872665F)
        );

        // NOSE
        part.addOrReplaceChild("Nose",
                CubeListBuilder.create()
                        .texOffs(44, 33)
                        .addBox(-1.5F, -1.8F, -12.4F, 3, 2, 7),
                PartPose.offsetAndRotation(0F, 7F, -10F, 0.2792527F, 0F, 0F)
        );

        // MOUTH (closed)
        part.addOrReplaceChild("Mouth",
                CubeListBuilder.create()
                        .texOffs(1, 34)
                        .addBox(-2F, 4F, -11.5F, 4, 1, 5),
                PartPose.offset(0F, 7F, -10F)
        );

        // UTEETH (upper teeth, shown when open mouth)
        part.addOrReplaceChild("UTeeth",
                CubeListBuilder.create()
                        .texOffs(46, 18)
                        .addBox(-2F, 4F, -12F, 4, 2, 5),
                PartPose.offset(0F, 7F, -10F)
        );

        // LTEETH (lower teeth, shown when open mouth)
        part.addOrReplaceChild("LTeeth",
                CubeListBuilder.create()
                        .texOffs(20, 109)
                        .addBox(-1.5F, -12.9F, 1.2F, 3, 5, 2),
                PartPose.offsetAndRotation(0F, 7F, -10F, 145F / 57.29578F, 0F, 0F)
        );

        // MOUTHOPEN (open mouth)
        part.addOrReplaceChild("MouthOpen",
                CubeListBuilder.create()
                        .texOffs(42, 69)
                        .addBox(-1.5F, -12.9F, -0.81F, 3, 9, 2),
                PartPose.offsetAndRotation(0F, 7F, -10F, 145F / 57.29578F, 0F, 0F)
        );

        // REAR (right ear)
        part.addOrReplaceChild("REar",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-3.5F, -7F, -1.5F, 3, 5, 1),
                PartPose.offsetAndRotation(0F, 7F, -10F, 0F, 0F, -0.1745329F)
        );

        // LEAR (left ear)
        part.addOrReplaceChild("LEar",
                CubeListBuilder.create()
                        .texOffs(13, 14)
                        .addBox(0.5F, -7F, -1.5F, 3, 5, 1),
                PartPose.offsetAndRotation(0F, 7F, -10F, 0F, 0F,  0.1745329F)
        );

        // CHEST
        part.addOrReplaceChild("Chest",
                CubeListBuilder.create()
                        .texOffs(20, 15)
                        .addBox(-4F, -11F, -12F, 8, 8, 10),
                PartPose.offsetAndRotation(0F, 5F, 2F, 1.570796F, 0F, 0F)
        );

        // BODY
        part.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-3F, -8F, -9F, 6, 16, 8),
                PartPose.offsetAndRotation(0F, 6.5F, 2F, 1.570796F, 0F, 0F)
        );

        // TAILA
        part.addOrReplaceChild("TailA",
                CubeListBuilder.create()
                        .texOffs(52, 42)
                        .addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
                PartPose.offsetAndRotation(0F, 8.5F, 9F, 1.064651F, 0F, 0F)
        );

        // TAILB
        part.addOrReplaceChild("TailB",
                CubeListBuilder.create()
                        .texOffs(48, 49)
                        .addBox(-2F, 3F, -1F, 4, 6, 4),
                PartPose.offsetAndRotation(0F, 8.5F, 9F, 0.7504916F, 0F, 0F)
        );

        // TAILC
        part.addOrReplaceChild("TailC",
                CubeListBuilder.create()
                        .texOffs(48, 59)
                        .addBox(-2F, 7.8F, -4.1F, 4, 6, 4),
                PartPose.offsetAndRotation(0F, 8.5F, 9F, 1.099557F, 0F, 0F)
        );

        // TAILD
        part.addOrReplaceChild("TailD",
                CubeListBuilder.create()
                        .texOffs(52, 69)
                        .addBox(-1.5F, 9.8F, -3.6F, 3, 5, 3),
                PartPose.offsetAndRotation(0F, 8.5F, 9F, 1.099557F, 0F, 0F)
        );

        // LEG1A (front right upper)
        part.addOrReplaceChild("Leg1A",
                CubeListBuilder.create()
                        .texOffs(28, 57)
                        .addBox(0.01F, -4F, -2.5F, 2, 8, 4),
                PartPose.offsetAndRotation(4F, 12.5F, -5.5F, 0.2617994F, 0F, 0F)
        );

        // LEG1B (front right mid)
        part.addOrReplaceChild("Leg1B",
                CubeListBuilder.create()
                        .texOffs(28, 69)
                        .addBox(0F, 3.2F, 0.5F, 2, 8, 2),
                PartPose.offsetAndRotation(4F, 12.5F, -5.5F, -0.1745329F, 0F, 0F)
        );

        // LEG1C (front right lower)
        part.addOrReplaceChild("Leg1C",
                CubeListBuilder.create()
                        .texOffs(28, 79)
                        .addBox(-0.5066667F, 9.5F, -2.5F, 3, 2, 3),
                PartPose.offset(4F, 12.5F, -5.5F)
        );

        // LEG2A (front left upper)
        part.addOrReplaceChild("Leg2A",
                CubeListBuilder.create()
                        .texOffs(28, 84)
                        .addBox(-2.01F, -4F, -2.5F, 2, 8, 4),
                PartPose.offsetAndRotation(-4F, 12.5F, -5.5F, 0.2617994F, 0F, 0F)
        );

        // LEG2B (front left mid)
        part.addOrReplaceChild("Leg2B",
                CubeListBuilder.create()
                        .texOffs(28, 96)
                        .addBox(-2F, 3.2F, 0.5F, 2, 8, 2),
                PartPose.offsetAndRotation(-4F, 12.5F, -5.5F, -0.1745329F, 0F, 0F)
        );

        // LEG2C (front left lower)
        part.addOrReplaceChild("Leg2C",
                CubeListBuilder.create()
                        .texOffs(28, 106)
                        .addBox(-2.506667F, 9.5F, -2.5F, 3, 2, 3),
                PartPose.offset(-4F, 12.5F, -5.5F)
        );

        // LEG3A (rear right upper)
        part.addOrReplaceChild("Leg3A",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(0F, -3.8F, -3.5F, 2, 7, 5),
                PartPose.offsetAndRotation(3F, 12.5F, 7F, -0.3665191F, 0F, 0F)
        );

        // LEG3B (rear right mid)
        part.addOrReplaceChild("Leg3B",
                CubeListBuilder.create()
                        .texOffs(0, 76)
                        .addBox(-0.1F, 1.9F, -1.8F, 2, 2, 5),
                PartPose.offsetAndRotation(3F, 12.5F, 7F, -0.7330383F, 0F, 0F)
        );

        // LEG3C (rear right lower)
        part.addOrReplaceChild("Leg3C",
                CubeListBuilder.create()
                        .texOffs(0, 83)
                        .addBox(0F, 3.2F, 0F, 2, 8, 2),
                PartPose.offsetAndRotation(3F, 12.5F, 7F, -0.1745329F, 0F, 0F)
        );

        // LEG3D (rear right foot)
        part.addOrReplaceChild("Leg3D",
                CubeListBuilder.create()
                        .texOffs(0, 93)
                        .addBox(-0.5066667F, 9.5F, -3F, 3, 2, 3),
                PartPose.offset(3F, 12.5F, 7F)
        );

        // LEG4A (rear left upper)
        part.addOrReplaceChild("Leg4A",
                CubeListBuilder.create()
                        .texOffs(14, 64)
                        .addBox(-2F, -3.8F, -3.5F, 2, 7, 5),
                PartPose.offsetAndRotation(-3F, 12.5F, 7F, -0.3665191F, 0F, 0F)
        );

        // LEG4B (rear left mid)
        part.addOrReplaceChild("Leg4B",
                CubeListBuilder.create()
                        .texOffs(14, 76)
                        .addBox(-1.9F, 1.9F, -1.8F, 2, 2, 5),
                PartPose.offsetAndRotation(-3F, 12.5F, 7F, -0.7330383F, 0F, 0F)
        );

        // LEG4C (rear left lower)
        part.addOrReplaceChild("Leg4C",
                CubeListBuilder.create()
                        .texOffs(14, 83)
                        .addBox(-2F, 3.2F, 0F, 2, 8, 2),
                PartPose.offsetAndRotation(-3F, 12.5F, 7F, -0.1745329F, 0F, 0F)
        );

        // LEG4D (rear left foot)
        part.addOrReplaceChild("Leg4D",
                CubeListBuilder.create()
                        .texOffs(14, 93)
                        .addBox(-2.506667F, 9.5F, -3F, 3, 2, 3),
                PartPose.offset(-3F, 12.5F, 7F)
        );

        // Texture size = 64×128
        return LayerDefinition.create(mesh, 64, 128);
    }

    /**
     * In 1.20.1, prepareMobModel replaces setLivingAnimations.
     * Here we pull the “openMouth” flag from the entity.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.openMouth = entity.mouthCounter != 0;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // HEAD rotations
        this.Head.xRot = headPitch  / 57.29578F;
        this.Head.yRot = netHeadYaw / 57.29578F;

        // Compute leg swings
        float LLegX = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
        float RLegX = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;

        // Sync mouth & nose parts to head rotation
        this.Mouth.xRot    = this.Head.xRot;
        this.Mouth.yRot    = this.Head.yRot;
        this.MouthB.xRot   = this.Head.xRot;
        this.MouthB.yRot   = this.Head.yRot;
        this.Nose2.xRot    = this.Head.xRot;
        this.Nose2.yRot    = this.Head.yRot;
        this.UTeeth.xRot   = this.Head.xRot;
        this.UTeeth.yRot   = this.Head.yRot;

        this.MouthOpen.xRot = (145F / 57.29578F) + this.Head.xRot;
        this.MouthOpen.yRot = this.Head.yRot;
        this.LTeeth.xRot    = (145F / 57.29578F) + this.Head.xRot;
        this.LTeeth.yRot    = this.Head.yRot;
        this.Nose.xRot      = (16F  / 57.29578F) + this.Head.xRot;
        this.Nose.yRot      = this.Head.yRot;

        // Sideburns
        this.LSide.xRot = (-12F / 57.29578F) + this.Head.xRot;
        this.LSide.yRot = ( 24F / 57.29578F) + this.Head.yRot;
        this.RSide.xRot = (-12F / 57.29578F) + this.Head.xRot;
        this.RSide.yRot = (-24F / 57.29578F) + this.Head.yRot;

        // Ears follow head
        this.REar.xRot = this.Head.xRot;
        this.REar.yRot = this.Head.yRot;
        this.LEar.xRot = this.Head.xRot;
        this.LEar.yRot = this.Head.yRot;

        // Front legs (Leg1 = right, Leg2 = left)
        this.Leg1A.xRot = (15F / 57.29578F) + LLegX;
        this.Leg1B.xRot = (-10F / 57.29578F) + LLegX;
        this.Leg1C.xRot = LLegX;

        this.Leg2A.xRot = (15F / 57.29578F) + RLegX;
        this.Leg2B.xRot = (-10F / 57.29578F) + RLegX;
        this.Leg2C.xRot = RLegX;

        // Rear legs (Leg3 = right, Leg4 = left)
        this.Leg3A.xRot = (-21F / 57.29578F) + RLegX;
        this.Leg3B.xRot = (-42F / 57.29578F) + RLegX;
        this.Leg3C.xRot = (-10F / 57.29578F) + RLegX;
        this.Leg3D.xRot = RLegX;

        this.Leg4A.xRot = (-21F / 57.29578F) + LLegX;
        this.Leg4B.xRot = (-42F / 57.29578F) + LLegX;
        this.Leg4C.xRot = (-10F / 57.29578F) + LLegX;
        this.Leg4D.xRot = LLegX;

        // Tail movement
        float tailMov = -1.3089F + (limbSwingAmount * 1.5F);
        if (entity.tailCounter != 0) {
            this.TailA.yRot = Mth.cos(ageInTicks * 0.5F);
            tailMov = 0F;
        } else {
            this.TailA.yRot = 0F;
        }
        this.TailA.xRot = (61F  / 57.29F) - tailMov;
        this.TailB.xRot = (43F  / 57.29F) - tailMov;
        this.TailC.xRot = (63F  / 57.29F) - tailMov;
        this.TailD.xRot = (63F  / 57.29F) - tailMov;

        this.TailB.yRot = this.TailA.yRot;
        this.TailC.yRot = this.TailA.yRot;
        this.TailD.yRot = this.TailA.yRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Nose2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Neck2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LSide.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RSide.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (this.openMouth) {
            this.MouthOpen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.UTeeth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LTeeth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.Mouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.MouthB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        this.REar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.TailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.Leg4A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4D.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.Leg3B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.Leg3D.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.Leg1A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
