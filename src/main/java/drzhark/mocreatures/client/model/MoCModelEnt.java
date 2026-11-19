package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.neutral.MoCEntityEnt;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelEnt<T extends MoCEntityEnt> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "ent"), "main");

    //private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart LShoulder;
    private final ModelPart LArm;
    private final ModelPart LWrist;
    private final ModelPart LHand;
    private final ModelPart LFingers;
    private final ModelPart RShoulder;
    private final ModelPart RArm;
    private final ModelPart RWrist;
    private final ModelPart RHand;
    private final ModelPart RFingers;
    private final ModelPart LLeg;
    private final ModelPart LThigh;
    private final ModelPart LKnee;
    private final ModelPart LAnkle;
    private final ModelPart LFoot;
    private final ModelPart RLeg;
    private final ModelPart RThigh;
    private final ModelPart RKnee;
    private final ModelPart RAnkle;
    private final ModelPart RFoot;
    private final ModelPart Neck;
    private final ModelPart Face;
    private final ModelPart Head;
    private final ModelPart Nose;
    private final ModelPart Mouth;
    private final ModelPart TreeBase;
    private final ModelPart Leave1;
    private final ModelPart Leave2;
    private final ModelPart Leave3;
    private final ModelPart Leave4;
    private final ModelPart Leave5;
    private final ModelPart Leave6;
    private final ModelPart Leave7;
    private final ModelPart Leave8;
    private final ModelPart Leave9;
    private final ModelPart Leave10;
    private final ModelPart Leave11;
    private final ModelPart Leave12;
    private final ModelPart Leave13;
    private final ModelPart Leave14;
    private final ModelPart Leave15;
    private final ModelPart Leave16;

    public MoCModelEnt(ModelPart root) {
        super(root);
        //this.root       = root;
        this.Body       = root.getChild("Body");
        this.LShoulder  = root.getChild("LShoulder");
        this.LArm       = root.getChild("LArm");
        this.LWrist     = root.getChild("LWrist");
        this.LHand      = root.getChild("LHand");
        this.LFingers   = root.getChild("LFingers");
        this.RShoulder  = root.getChild("RShoulder");
        this.RArm       = root.getChild("RArm");
        this.RWrist     = root.getChild("RWrist");
        this.RHand      = root.getChild("RHand");
        this.RFingers   = root.getChild("RFingers");
        this.LLeg       = root.getChild("LLeg");
        this.LThigh     = root.getChild("LThigh");
        this.LKnee      = root.getChild("LKnee");
        this.LAnkle     = root.getChild("LAnkle");
        this.LFoot      = root.getChild("LFoot");
        this.RLeg       = root.getChild("RLeg");
        this.RThigh     = root.getChild("RThigh");
        this.RKnee      = root.getChild("RKnee");
        this.RAnkle     = root.getChild("RAnkle");
        this.RFoot      = root.getChild("RFoot");
        this.Neck       = root.getChild("Neck");
        this.Face       = root.getChild("Face");
        this.Head       = root.getChild("Head");
        this.Nose       = root.getChild("Nose");
        this.Mouth      = root.getChild("Mouth");
        this.TreeBase   = root.getChild("TreeBase");
        this.Leave1     = root.getChild("Leave1");
        this.Leave2     = root.getChild("Leave2");
        this.Leave3     = root.getChild("Leave3");
        this.Leave4     = root.getChild("Leave4");
        this.Leave5     = root.getChild("Leave5");
        this.Leave6     = root.getChild("Leave6");
        this.Leave7     = root.getChild("Leave7");
        this.Leave8     = root.getChild("Leave8");
        this.Leave9     = root.getChild("Leave9");
        this.Leave10    = root.getChild("Leave10");
        this.Leave11    = root.getChild("Leave11");
        this.Leave12    = root.getChild("Leave12");
        this.Leave13    = root.getChild("Leave13");
        this.Leave14    = root.getChild("Leave14");
        this.Leave15    = root.getChild("Leave15");
        this.Leave16    = root.getChild("Leave16");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition parts   = mesh.getRoot();

        // Body
        parts.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(68, 36)
                        .addBox(-7.5F, -12.5F, -4.5F, 15, 25, 9),
                PartPose.offset(0F, -31F, 0F)
        );

        // LShoulder
        parts.addOrReplaceChild("LShoulder",
                CubeListBuilder.create()
                        .texOffs(48, 108)
                        .addBox(6F, -14F, -4.8F, 9, 7, 7),
                PartPose.offsetAndRotation(0F, -31F, 0F, 0F, 0F, -0.1745329F)
        );

        // LArm
        parts.addOrReplaceChild("LArm",
                CubeListBuilder.create()
                        .texOffs(80, 108)
                        .addBox(0F, -4F, -5F, 6, 24, 6),
                PartPose.offsetAndRotation(10F, -42F, 1F, 0F, 0F, -0.1745329F)
        );

        // LWrist
        parts.addOrReplaceChild("LWrist",
                CubeListBuilder.create()
                        .texOffs(0, 169)
                        .addBox(2F, 17F, -6F, 8, 15, 8),
                PartPose.offset(10F, -42F, 1F)
        );

        // LHand
        parts.addOrReplaceChild("LHand",
                CubeListBuilder.create()
                        .texOffs(88, 241)
                        .addBox(1F, 28F, -7F, 10, 5, 10),
                PartPose.offset(10F, -42F, 1F)
        );

        // LFingers
        parts.addOrReplaceChild("LFingers",
                CubeListBuilder.create()
                        .texOffs(88, 176)
                        .addBox(1F, 33F, -7F, 10, 15, 10),
                PartPose.offset(10F, -42F, 1F)
        );

        // RShoulder
        parts.addOrReplaceChild("RShoulder",
                CubeListBuilder.create()
                        .texOffs(48, 122)
                        .addBox(-15F, -14F, -4.8F, 9, 7, 7),
                PartPose.offsetAndRotation(0F, -31F, 0F, 0F, 0F, 0.1745329F)
        );

        // RArm
        parts.addOrReplaceChild("RArm",
                CubeListBuilder.create()
                        .texOffs(104, 108)
                        .addBox(-6F, -4F, -5F, 6, 24, 6),
                PartPose.offsetAndRotation(-10F, -42F, 1F, 0F, 0F, 0.1745329F)
        );

        // RWrist
        parts.addOrReplaceChild("RWrist",
                CubeListBuilder.create()
                        .texOffs(32, 169)
                        .addBox(-10F, 17F, -6F, 8, 15, 8),
                PartPose.offset(-10F, -42F, 1F)
        );

        // RHand
        parts.addOrReplaceChild("RHand",
                CubeListBuilder.create()
                        .texOffs(88, 226)
                        .addBox(-11F, 28F, -7F, 10, 5, 10),
                PartPose.offset(-10F, -42F, 1F)
        );

        // RFingers
        parts.addOrReplaceChild("RFingers",
                CubeListBuilder.create()
                        .texOffs(88, 201)
                        .addBox(-11F, 33F, -7F, 10, 15, 10),
                PartPose.offset(-10F, -42F, 1F)
        );

        // LLeg
        parts.addOrReplaceChild("LLeg",
                CubeListBuilder.create()
                        .texOffs(0, 90)
                        .addBox(3F, 0F, -3F, 6, 20, 6),
                PartPose.offset(0F, -21F, 0F)
        );

        // LThigh
        parts.addOrReplaceChild("LThigh",
                CubeListBuilder.create()
                        .texOffs(24, 64)
                        .addBox(2.5F, 4F, -3.5F, 7, 12, 7),
                PartPose.offset(0F, -21F, 0F)
        );

        // LKnee
        parts.addOrReplaceChild("LKnee",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(2F, 20F, -4F, 8, 24, 8),
                PartPose.offset(0F, -21F, 0F)
        );

        // LAnkle
        parts.addOrReplaceChild("LAnkle",
                CubeListBuilder.create()
                        .texOffs(32, 29)
                        .addBox(1.5F, 25F, -4.5F, 9, 20, 9),
                PartPose.offset(0F, -21F, 0F)
        );

        // LFoot
        parts.addOrReplaceChild("LFoot",
                CubeListBuilder.create()
                        .texOffs(0, 206)
                        .addBox(1.5F, 38F, -23.5F, 9, 5, 9),
                PartPose.offsetAndRotation(0F, -21F, 0F, 0.2617994F, 0F, 0F)
        );

        // RLeg
        parts.addOrReplaceChild("RLeg",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-9F, 0F, -3F, 6, 20, 6),
                PartPose.offset(0F, -21F, 0F)
        );

        // RThigh
        parts.addOrReplaceChild("RThigh",
                CubeListBuilder.create()
                        .texOffs(24, 83)
                        .addBox(-9.5F, 4F, -3.5F, 7, 12, 7),
                PartPose.offset(0F, -21F, 0F)
        );

        // RKnee
        parts.addOrReplaceChild("RKnee",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-10F, 20F, -4F, 8, 24, 8),
                PartPose.offset(0F, -21F, 0F)
        );

        // RAnkle
        parts.addOrReplaceChild("RAnkle",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-10.5F, 25F, -4.5F, 9, 20, 9),
                PartPose.offset(0F, -21F, 0F)
        );

        // RFoot
        parts.addOrReplaceChild("RFoot",
                CubeListBuilder.create()
                        .texOffs(0, 192)
                        .addBox(-10.5F, 38F, -23.5F, 9, 5, 9),
                PartPose.offsetAndRotation(0F, -21F, 0F, 0.2617994F, 0F, 0F)
        );

        // Neck
        parts.addOrReplaceChild("Neck",
                CubeListBuilder.create()
                        .texOffs(52, 90)
                        .addBox(-4F, -8F, -5.8F, 8, 10, 8),
                PartPose.offsetAndRotation(0F, -44F, 0F, 0.5235988F, 0F, 0F)
        );

        // Face
        parts.addOrReplaceChild("Face",
                CubeListBuilder.create()
                        .texOffs(52, 70)
                        .addBox(-4.5F, -11F, -9F, 9, 7, 8),
                PartPose.offset(0F, -44F, 0F)
        );

        // Head
        parts.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(84, 88)
                        .addBox(-6F, -20.5F, -9.5F, 12, 10, 10),
                PartPose.offset(0F, -44F, 0F)
        );

        // Nose
        parts.addOrReplaceChild("Nose",
                CubeListBuilder.create()
                        .texOffs(82, 88)
                        .addBox(-1.5F, -12F, -12F, 3, 7, 3),
                PartPose.offsetAndRotation(0F, -44F, 0F, -0.122173F, 0F, 0F)
        );

        // Mouth
        parts.addOrReplaceChild("Mouth",
                CubeListBuilder.create()
                        .texOffs(77, 36)
                        .addBox(-3F, -8F, -6.8F, 6, 2, 1),
                PartPose.offsetAndRotation(0F, -44F, 0F, 0.5235988F, 0F, 0F)
        );

        // TreeBase
        parts.addOrReplaceChild("TreeBase",
                CubeListBuilder.create()
                        .texOffs(0, 136)
                        .addBox(-10F, -31.5F, -11.5F, 20, 13, 20),
                PartPose.offset(0F, -44F, 0F)
        );

        // Leaves (all use the same texture offset 0,224)
        parts.addOrReplaceChild("Leave1",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -45F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave2",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -45F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave3",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -45F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave4",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -45F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave5",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -45F, -33F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave6",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -45F, -33F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave7",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(16F, -45F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave8",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(16F, -45F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave9",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -45F, 15F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave10",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -45F, 15F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave11",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-32F, -45F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave12",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-32F, -45F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave13",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -61F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave14",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -61F, -17F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave15",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(0F, -61F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );
        parts.addOrReplaceChild("Leave16",
                CubeListBuilder.create()
                        .texOffs(0, 224)
                        .addBox(-16F, -61F, -1F, 16, 16, 16),
                PartPose.offset(0F, -44F, 0F)
        );

        return LayerDefinition.create(mesh, 128, 256);
    }

    @Override
    public void renderToBuffer(
            PoseStack pose,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float r,
            float g,
            float b,
            float a
    ) {
        this.Body.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LShoulder.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LArm.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LWrist.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LHand.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LFingers.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RShoulder.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RArm.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RWrist.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RHand.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RFingers.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LLeg.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LThigh.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LKnee.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LAnkle.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.LFoot.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RLeg.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RThigh.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RKnee.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RAnkle.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.RFoot.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Neck.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Face.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Head.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Nose.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Mouth.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.TreeBase.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave1.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave2.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave3.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave4.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave5.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave6.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave7.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave8.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave9.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave10.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave11.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave12.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave13.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave14.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave15.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.Leave16.render(pose, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
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
        float rad = 57.29578F;

        // Arm swings
        float RArmXRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        float LArmXRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        // Leg swings
        float RLegXRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.0F * limbSwingAmount;

        // Wrist waggle animation
        this.LWrist.xRot = LArmXRot;
        this.LWrist.zRot = (Mth.cos(ageInTicks * 0.09F) * 0.05F) - 0.05F;
        this.RWrist.xRot = RArmXRot;
        this.RWrist.zRot = -(Mth.cos(ageInTicks * 0.09F) * 0.05F) + 0.05F;

        // Propagate to hand/fingers/arm
        this.LArm.xRot     = this.LWrist.xRot;
        this.LHand.xRot    = this.LWrist.xRot;
        this.LFingers.xRot = this.LWrist.xRot;
        this.LArm.zRot     = (-10F / rad) + this.LWrist.zRot;
        this.LHand.zRot    = this.LWrist.zRot;
        this.LFingers.zRot = this.LWrist.zRot;

        this.RArm.xRot     = this.RWrist.xRot;
        this.RHand.xRot    = this.RWrist.xRot;
        this.RFingers.xRot = this.RWrist.xRot;
        this.RArm.zRot     = ( 10F / rad) + this.RWrist.zRot;
        this.RHand.zRot    = this.RWrist.zRot;
        this.RFingers.zRot = this.RWrist.zRot;

        // Legs
        this.RLeg.xRot   = RLegXRot;
        this.LLeg.xRot   = LLegXRot;
        this.LThigh.xRot = this.LKnee.xRot = this.LAnkle.xRot = this.LLeg.xRot;
        this.RThigh.xRot = this.RKnee.xRot = this.RAnkle.xRot = this.RLeg.xRot;
        this.LFoot.xRot  = (15F / rad) + this.LLeg.xRot;
        this.RFoot.xRot  = (15F / rad) + this.RLeg.xRot;

        // Head yaw
        float headYRot = netHeadYaw / rad;
        this.Neck.yRot = headYRot;
        this.Face.yRot = headYRot;
        this.Head.yRot = headYRot;
        this.Nose.yRot = headYRot;
        this.Mouth.yRot = headYRot;
        this.TreeBase.yRot = headYRot;
        this.Leave1.yRot = headYRot;
        this.Leave2.yRot = headYRot;
        this.Leave3.yRot = headYRot;
        this.Leave4.yRot = headYRot;
        this.Leave5.yRot = headYRot;
        this.Leave6.yRot = headYRot;
        this.Leave7.yRot = headYRot;
        this.Leave8.yRot = headYRot;
        this.Leave9.yRot = headYRot;
        this.Leave10.yRot = headYRot;
        this.Leave11.yRot = headYRot;
        this.Leave12.yRot = headYRot;
        this.Leave13.yRot = headYRot;
        this.Leave14.yRot = headYRot;
        this.Leave15.yRot = headYRot;
        this.Leave16.yRot = headYRot;
    }
}
