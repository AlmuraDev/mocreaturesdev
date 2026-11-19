/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MoCModelAbstractBigCat<T extends Entity> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "abstract_big_cat"),
            "main"
    );

    // Pivot‐scale helper constant
    private static final float RADIAN_CONV = 57.29578F;
    private static final float INTERPOLATION_FACTOR = 0.1F;

    // State flags (exactly as before)
    protected boolean hasMane;
    protected boolean isRidden;
    protected boolean isSaddled;
    protected boolean flapwings;
    protected boolean onAir;
    protected boolean isFlyer;
    protected boolean floating;
    protected boolean poisoning;
    protected boolean isTamed;
    protected boolean movingTail;
    protected int openMouthCounter;
    protected boolean hasSaberTeeth;
    protected boolean hasChest;
    protected boolean hasStinger;
    protected boolean isGhost;
    protected boolean isMovingVertically;
    protected boolean isChested;
    protected boolean diving;
    protected boolean isSitting;

    // For interpolation in setupAnim:
    private float prevTailSwingYaw;
    private float prevMouthAngle;

    // The entity instance (if needed)
    protected T bigcat;
    
    private final ModelPart Chest;
    private final ModelPart NeckBase;
    private final ModelPart Collar;
    private final ModelPart HeadBack;
    private final ModelPart NeckHarness;
    private final ModelPart HarnessStick;
    private final ModelPart LeftHarness;
    private final ModelPart RightHarness;
    private final ModelPart Head;
    private final ModelPart Nose;
    private final ModelPart RightUpperLip;
    private final ModelPart LeftUpperLip;
    private final ModelPart UpperTeeth;
    private final ModelPart LeftFang;
    private final ModelPart RightFang;
    private final ModelPart InsideMouth;
    private final ModelPart LowerJaw;
    private final ModelPart LowerJawTeeth;
    private final ModelPart ChinHair;
    private final ModelPart LeftChinBeard;
    private final ModelPart RightChinBeard;
    private final ModelPart ForeheadHair;
    private final ModelPart Mane;
    private final ModelPart RightEar;
    private final ModelPart LeftEar;
    private final ModelPart NeckHair;
    private final ModelPart InnerWing;
    private final ModelPart MidWing;
    private final ModelPart OuterWing;
    private final ModelPart InnerWingR;
    private final ModelPart MidWingR;
    private final ModelPart OuterWingR;
    private final ModelPart Abdomen;
    private final ModelPart Ass;
    private final ModelPart TailRoot;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Tail4;
    private final ModelPart TailTip;
    private final ModelPart TailTusk;
    private final ModelPart Saddle;
    private final ModelPart SaddleFront;
    private final ModelPart SaddleBack;
    private final ModelPart LeftFootHarness;
    private final ModelPart LeftFootRing;
    private final ModelPart RightFootHarness;
    private final ModelPart RightFootRing;
    private final ModelPart StorageChest;
    private final ModelPart STailRoot;
    private final ModelPart STail2;
    private final ModelPart STail3;
    private final ModelPart STail4;
    private final ModelPart STail5;
    private final ModelPart StingerLump;
    private final ModelPart Stinger;
    private final ModelPart LeftUpperLeg;
    private final ModelPart LeftLowerLeg;
    private final ModelPart LeftFrontFoot;
    private final ModelPart LeftClaw1;
    private final ModelPart LeftClaw2;
    private final ModelPart LeftClaw3;
    private final ModelPart RightUpperLeg;
    private final ModelPart RightLowerLeg;
    private final ModelPart RightFrontFoot;
    private final ModelPart RightClaw1;
    private final ModelPart RightClaw2;
    private final ModelPart RightClaw3;
    private final ModelPart LeftHindUpperLeg;
    private final ModelPart LeftAnkle;
    private final ModelPart LeftHindLowerLeg;
    private final ModelPart LeftHindFoot;
    private final ModelPart RightHindUpperLeg;
    private final ModelPart RightAnkle;
    private final ModelPart RightHindLowerLeg;
    private final ModelPart RightHindFoot;

    public MoCModelAbstractBigCat(ModelPart root) {
        super(root);
        // Grab every child by the exact same name used in createBodyLayer():
        this.Chest            = root.getChild("Chest");
        this.NeckBase         = this.Chest.getChild("NeckBase");
        this.Collar           = this.NeckBase.getChild("Collar");
        this.HeadBack         = this.NeckBase.getChild("HeadBack");
        this.NeckHarness      = this.HeadBack.getChild("NeckHarness");
        this.HarnessStick     = this.HeadBack.getChild("HarnessStick");
        this.LeftHarness      = root.getChild("LeftHarness");
        this.RightHarness     = root.getChild("RightHarness");
        this.Head             = this.HeadBack.getChild("Head");
        this.Nose             = this.Head.getChild("Nose");
        this.RightUpperLip    = this.Head.getChild("RightUpperLip");
        this.LeftUpperLip     = this.Head.getChild("LeftUpperLip");
        this.UpperTeeth       = this.Head.getChild("UpperTeeth");
        this.LeftFang         = this.Head.getChild("LeftFang");
        this.RightFang        = this.Head.getChild("RightFang");
        this.InsideMouth      = this.Head.getChild("InsideMouth");
        this.LowerJaw         = this.Head.getChild("LowerJaw");
        this.LowerJawTeeth    = this.LowerJaw.getChild("LowerJawTeeth");
        this.ChinHair         = this.LowerJaw.getChild("ChinHair");
        this.LeftChinBeard    = this.Head.getChild("LeftChinBeard");
        this.RightChinBeard   = this.Head.getChild("RightChinBeard");
        this.ForeheadHair     = this.Head.getChild("ForeheadHair");
        this.Mane             = this.Head.getChild("Mane");
        this.RightEar         = this.Head.getChild("RightEar");
        this.LeftEar          = this.Head.getChild("LeftEar");
        this.NeckHair         = this.NeckBase.getChild("NeckHair");
        this.InnerWing        = root.getChild("InnerWing");
        this.MidWing          = root.getChild("MidWing");
        this.OuterWing        = root.getChild("OuterWing");
        this.InnerWingR       = root.getChild("InnerWingR");
        this.MidWingR         = root.getChild("MidWingR");
        this.OuterWingR       = root.getChild("OuterWingR");
        this.Abdomen          = this.Chest.getChild("Abdomen");
        this.Ass              = this.Abdomen.getChild("Ass");
        this.TailRoot         = this.Abdomen.getChild("TailRoot");
        this.Tail2            = this.TailRoot.getChild("Tail2");
        this.Tail3            = this.Tail2.getChild("Tail3");
        this.Tail4            = this.Tail3.getChild("Tail4");
        this.TailTip          = this.Tail4.getChild("TailTip");
        this.TailTusk         = this.Tail4.getChild("TailTusk");
        this.Saddle           = this.Chest.getChild("Saddle");
        this.SaddleFront      = this.Saddle.getChild("SaddleFront");
        this.SaddleBack       = this.Saddle.getChild("SaddleBack");
        this.LeftFootHarness  = this.Saddle.getChild("LeftFootHarness");
        this.LeftFootRing     = this.LeftFootHarness.getChild("LeftFootRing");
        this.RightFootHarness = this.Saddle.getChild("RightFootHarness");
        this.RightFootRing    = this.RightFootHarness.getChild("RightFootRing");
        this.StorageChest     = this.Abdomen.getChild("StorageChest");
        this.STailRoot        = root.getChild("STailRoot");
        this.STail2           = root.getChild("STail2");
        this.STail3           = root.getChild("STail3");
        this.STail4           = root.getChild("STail4");
        this.STail5           = root.getChild("STail5");
        this.StingerLump      = root.getChild("StingerLump");
        this.Stinger          = root.getChild("Stinger");
        this.LeftUpperLeg     = this.Chest.getChild("LeftUpperLeg");
        this.LeftLowerLeg     = this.LeftUpperLeg.getChild("LeftLowerLeg");
        this.LeftFrontFoot    = this.LeftLowerLeg.getChild("LeftFrontFoot");
        this.LeftClaw1        = this.LeftFrontFoot.getChild("LeftClaw1");
        this.LeftClaw2        = this.LeftFrontFoot.getChild("LeftClaw2");
        this.LeftClaw3        = this.LeftFrontFoot.getChild("LeftClaw3");
        this.RightUpperLeg    = this.Chest.getChild("RightUpperLeg");
        this.RightLowerLeg    = this.RightUpperLeg.getChild("RightLowerLeg");
        this.RightFrontFoot   = this.RightLowerLeg.getChild("RightFrontFoot");
        this.RightClaw1       = this.RightFrontFoot.getChild("RightClaw1");
        this.RightClaw2       = this.RightFrontFoot.getChild("RightClaw2");
        this.RightClaw3       = this.RightFrontFoot.getChild("RightClaw3");
        this.LeftHindUpperLeg = this.Abdomen.getChild("LeftHindUpperLeg");
        this.LeftAnkle        = this.LeftHindUpperLeg.getChild("LeftAnkle");
        this.LeftHindLowerLeg = this.LeftAnkle.getChild("LeftHindLowerLeg");
        this.LeftHindFoot     = this.LeftHindLowerLeg.getChild("LeftHindFoot");
        this.RightHindUpperLeg= this.Abdomen.getChild("RightHindUpperLeg");
        this.RightAnkle       = this.RightHindUpperLeg.getChild("RightAnkle");
        this.RightHindLowerLeg= this.RightAnkle.getChild("RightHindLowerLeg");
        this.RightHindFoot    = this.RightHindLowerLeg.getChild("RightHindFoot");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ---------------------------------------
        // Chest (texture at 0,18; box(-3.5,0,-8; 7x8x9); pivot(0,8,0))
        // ---------------------------------------
        PartDefinition chest = root.addOrReplaceChild(
                "Chest",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-3.5F, 0.0F, -8.0F, 7, 8, 9, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, 0.0F)
        );

        // ---------------------------------------
        // NeckBase (0,7; box(-2.5,0,-2.5; 5x6x5); pivot(0,-0.5,-8); rotateAngleX=-14°)
        // ---------------------------------------
        PartDefinition neckBase = chest.addOrReplaceChild(
                "NeckBase",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-2.5F, 0.0F, -2.5F, 5, 6, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -0.5F, -8.0F,
                        -14F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Collar (18,0; box(-2.5,0,0; 5x4x1); pivot(0,6,-2); rotateAngleX=+20°)
        // ---------------------------------------
        neckBase.addOrReplaceChild(
                "Collar",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(-2.5F, 0.0F, 0.0F, 5, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 6.0F, -2.0F,
                        20F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // HeadBack (0,0; box(-2.51,-2.5,-1; 5x5x2); pivot(0,2.7,-2.9); rotateAngleX=+14°)
        // ---------------------------------------
        PartDefinition headBack = neckBase.addOrReplaceChild(
                "HeadBack",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.51F, -2.5F, -1.0F, 5, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 2.7F, -2.9F,
                        14F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // NeckHarness (85,32; box(-3,-3,-2; 6x6x2); pivot(0,0,0.95))
        // (child of HeadBack, no extra rotation)
        // ---------------------------------------
        headBack.addOrReplaceChild(
                "NeckHarness",
                CubeListBuilder.create()
                        .texOffs(85, 32)
                        .addBox(-3.0F, -3.0F, -2.0F, 6, 6, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.95F)
        );

        // ---------------------------------------
        // HarnessStick (85,42; box(-3.5,-0.5,-0.5; 7x1x1); pivot(0,-1.8,0.5); rotateAngleX=+45°)
        // ---------------------------------------
        headBack.addOrReplaceChild(
                "HarnessStick",
                CubeListBuilder.create()
                        .texOffs(85, 42)
                        .addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -1.8F, 0.5F,
                        45F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftHarness (85,32; box(3.2,-0.6,1.5; 0x1x9); pivot(0,8.6,-13); rotateAngleX=+25°)
        // ---------------------------------------
        root.addOrReplaceChild(
                "LeftHarness",
                CubeListBuilder.create()
                        .texOffs(85, 32)
                        .addBox(3.2F, -0.6F, 1.5F, 0, 1, 9, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.6F, -13.0F,
                        25F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightHarness (85,31; box(-3.2,-0.6,1.5; 0x1x9); pivot(0,8.6,-13); rotateAngleX=+25°)
        // ---------------------------------------
        root.addOrReplaceChild(
                "RightHarness",
                CubeListBuilder.create()
                        .texOffs(85, 31)
                        .addBox(-3.2F, -0.6F, 1.5F, 0, 1, 9, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.6F, -13.0F,
                        25F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Head (32,0; box(-3.5,-3,-2; 7x6x4); pivot(0,0.2,-2.2)) — child of HeadBack
        // ---------------------------------------
        PartDefinition head = headBack.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-3.5F, -3.0F, -2.0F, 7, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.2F, -2.2F)
        );

        // ---------------------------------------
        // Nose (46,19; box(-1.5,-1,-2; 3x2x4); pivot(0,0,-3); rotateAngleX=+27°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "Nose",
                CubeListBuilder.create()
                        .texOffs(46, 19)
                        .addBox(-1.5F, -1.0F, -2.0F, 3, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 0.0F, -3.0F,
                        27F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightUpperLip (34,19; box(-1,-1,-2; 2x2x4); pivot(-1.25,1,-2.8); rotate(X=10°, Y=2°, Z=-15°))
        // ---------------------------------------
        head.addOrReplaceChild(
                "RightUpperLip",
                CubeListBuilder.create()
                        .texOffs(34, 19)
                        .addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -1.25F, 1.0F, -2.8F,
                        10F / RADIAN_CONV, 2F / RADIAN_CONV, -15F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // LeftUpperLip (34,25; box(-1,-1,-2; 2x2x4); pivot(1.25,1,-2.8); rotate(X=10°, Y=-2°, Z=15°))
        // ---------------------------------------
        head.addOrReplaceChild(
                "LeftUpperLip",
                CubeListBuilder.create()
                        .texOffs(34, 25)
                        .addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.25F, 1.0F, -2.8F,
                        10F / RADIAN_CONV, -2F / RADIAN_CONV, 15F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // UpperTeeth (20,7; box(-1.5,-1,-1.5; 3x2x3); pivot(0,2,-2.5); rotateAngleX=+15°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "UpperTeeth",
                CubeListBuilder.create()
                        .texOffs(20, 7)
                        .addBox(-1.5F, -1.0F, -1.5F, 3, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 2.0F, -2.5F,
                        15F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftFang (44,10; box(-0.5,-1.5,-0.5; 1x3x1); pivot(1.2,2.8,-3.4); rotateAngleX=+15°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "LeftFang",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(-0.5F, -1.5F, -0.5F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.2F, 2.8F, -3.4F,
                        15F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightFang (48,10; box(-0.5,-1.5,-0.5; 1x3x1); pivot(-1.2,2.8,-3.4); rotateAngleX=+15°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "RightFang",
                CubeListBuilder.create()
                        .texOffs(48, 10)
                        .addBox(-0.5F, -1.5F, -0.5F, 1, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -1.2F, 2.8F, -3.4F,
                        15F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // InsideMouth (50,0; box(-1.5,-1,-1; 3x2x2); pivot(0,2,-1))
        // ---------------------------------------
        head.addOrReplaceChild(
                "InsideMouth",
                CubeListBuilder.create()
                        .texOffs(50, 0)
                        .addBox(-1.5F, -1.0F, -1.0F, 3, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, -1.0F)
        );

        // ---------------------------------------
        // LowerJaw (46,25; box(-1.5,-1,-4; 3x2x4); pivot(0,2.1,0))
        // ---------------------------------------
        PartDefinition lowerJaw = head.addOrReplaceChild(
                "LowerJaw",
                CubeListBuilder.create()
                        .texOffs(46, 25)
                        .addBox(-1.5F, -1.0F, -4.0F, 3, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.1F, 0.0F)
        );

        // ---------------------------------------
        // LowerJawTeeth (20,12; box(-1,0,-1; 2x1x2); pivot(0,-1.8,-2.7); mirror=true)
        // ---------------------------------------
        lowerJaw.addOrReplaceChild(
                "LowerJawTeeth",
                CubeListBuilder.create()
                        .texOffs(20, 12)
                        .mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -1.8F, -2.7F)
        );

        // ---------------------------------------
        // ChinHair (76,7; box(-2.5,0,-2; 5x6x4); pivot(0,0,1))
        // ---------------------------------------
        lowerJaw.addOrReplaceChild(
                "ChinHair",
                CubeListBuilder.create()
                        .texOffs(76, 7)
                        .addBox(-2.5F, 0.0F, -2.0F, 5, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 1.0F)
        );

        // ---------------------------------------
        // LeftChinBeard (48,10; box(-1,-2.5,-2; 2x5x4); pivot(3.6,0,0.25); rotateAngleY=+30°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "LeftChinBeard",
                CubeListBuilder.create()
                        .texOffs(48, 10)
                        .addBox(-1.0F, -2.5F, -2.0F, 2, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        3.6F, 0.0F, 0.25F,
                        0.0F, 30F / RADIAN_CONV, 0.0F
                )
        );

        // ---------------------------------------
        // RightChinBeard (36,10; box(-1,-2.5,-2; 2x5x4); pivot(-3.6,0,0.25); rotateAngleY=-30°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "RightChinBeard",
                CubeListBuilder.create()
                        .texOffs(36, 10)
                        .addBox(-1.0F, -2.5F, -2.0F, 2, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -3.6F, 0.0F, 0.25F,
                        0.0F, -30F / RADIAN_CONV, 0.0F
                )
        );

        // ---------------------------------------
        // ForeheadHair (88,0; box(-1.5,-1.5,-1.5; 3x3x3); pivot(0,-3.2,0); rotateAngleX=+10°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "ForeheadHair",
                CubeListBuilder.create()
                        .texOffs(88, 0)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -3.2F, 0.0F,
                        10F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Mane (94,0; box(-5.5,-5.5,-3; 11x11x6); pivot(0,0.7,3.7); rotateAngleX=-5°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "Mane",
                CubeListBuilder.create()
                        .texOffs(94, 0)
                        .addBox(-5.5F, -5.5F, -3.0F, 11, 11, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 0.7F, 3.7F,
                        -5F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightEar (54,7; box(-1,-1,-0.5; 2x2x1); pivot(-2.7,-3.5,1); rotateAngleZ=-15°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "RightEar",
                CubeListBuilder.create()
                        .texOffs(54, 7)
                        .addBox(-1.0F, -1.0F, -0.5F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -2.7F, -3.5F, 1.0F,
                        0.0F, 0.0F, -15F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // LeftEar (54,4; box(-1,-1,-0.5; 2x2x1); pivot(2.7,-3.5,1); rotateAngleZ=+15°)
        // ---------------------------------------
        head.addOrReplaceChild(
                "LeftEar",
                CubeListBuilder.create()
                        .texOffs(54, 4)
                        .addBox(-1.0F, -1.0F, -0.5F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        2.7F, -3.5F, 1.0F,
                        0.0F, 0.0F, 15F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // NeckHair (108,17; box(-2,-1,-3; 4x2x6); pivot(0,-0.5,3); rotateAngleX=-10.6°)
        // ---------------------------------------
        neckBase.addOrReplaceChild(
                "NeckHair",
                CubeListBuilder.create()
                        .texOffs(108, 17)
                        .addBox(-2.0F, -1.0F, -3.0F, 4, 2, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -0.5F, 3.0F,
                        -10.6F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ----------------------------------------------------------------
        // Wings (these are children of root, not Chest/Head)
        // ----------------------------------------------------------------
        root.addOrReplaceChild(
                "InnerWing",
                CubeListBuilder.create()
                        .texOffs(26, 115)
                        .addBox(0.0F, 0.0F, 0.0F, 7, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        4.0F, 9.0F, -7.0F,
                        0.0F, -20F / RADIAN_CONV, 0.0F
                )
        );

        root.addOrReplaceChild(
                "MidWing",
                CubeListBuilder.create()
                        .texOffs(36, 89)
                        .addBox(1.0F, 0.1F, 1.0F, 12, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        4.0F, 9.0F, -7.0F,
                        0.0F, 5F / RADIAN_CONV, 0.0F
                )
        );

        root.addOrReplaceChild(
                "OuterWing",
                CubeListBuilder.create()
                        .texOffs(62, 115)
                        .addBox(0.0F, 0.0F, 0.0F, 22, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        16.0F, 9.0F, -7.0F,
                        0.0F, -18F / RADIAN_CONV, 0.0F
                )
        );

        root.addOrReplaceChild(
                "InnerWingR",
                CubeListBuilder.create()
                        .texOffs(26, 102)
                        .addBox(-7.0F, 0.0F, 0.0F, 7, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -4.0F, 9.0F, -7.0F,
                        0.0F, 20F / RADIAN_CONV, 0.0F
                )
        );

        root.addOrReplaceChild(
                "MidWingR",
                CubeListBuilder.create()
                        .texOffs(82, 89)
                        .addBox(-13.0F, 0.1F, 1.0F, 12, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -4.0F, 9.0F, -7.0F,
                        0.0F, -5F / RADIAN_CONV, 0.0F
                )
        );

        root.addOrReplaceChild(
                "OuterWingR",
                CubeListBuilder.create()
                        .texOffs(62, 102)
                        .addBox(-22.0F, 0.0F, 0.0F, 22, 2, 11, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -16.0F, 9.0F, -7.0F,
                        0.0F, 18F / RADIAN_CONV, 0.0F
                )
        );

        // ---------------------------------------
        // Abdomen (0,35; box(-3,0,0; 6x7x7); pivot(0,0,0); rotateAngleX≈-3°)
        // ---------------------------------------
        PartDefinition abdomen = chest.addOrReplaceChild(
                "Abdomen",
                CubeListBuilder.create()
                        .texOffs(0, 35)
                        .addBox(-3.0F, 0.0F, 0.0F, 6, 7, 7, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 0.0F, 0.0F,
                        -0.0523599F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Ass (0,49; box(-2.5,0,0; 5x5x3); pivot(0,0,7); rotateAngleX≈-20°)
        // ---------------------------------------
        abdomen.addOrReplaceChild(
                "Ass",
                CubeListBuilder.create()
                        .texOffs(0, 49)
                        .addBox(-2.5F, 0.0F, 0.0F, 5, 5, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 0.0F, 7.0F,
                        -20F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // TailRoot (96,83; box(-1,0,-1; 2x4x2); pivot(0,1,7); rotateAngleX≈+87°)
        // ---------------------------------------
        PartDefinition tailRoot = abdomen.addOrReplaceChild(
                "TailRoot",
                CubeListBuilder.create()
                        .texOffs(96, 83)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 1.0F, 7.0F,
                        87F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Tail2 (96,75; box(-1,0,-1; 2x6x2); pivot(-0.01,3.5,0); rotateAngleX=-30°)
        // ---------------------------------------
        PartDefinition tail2 = tailRoot.addOrReplaceChild(
                "Tail2",
                CubeListBuilder.create()
                        .texOffs(96, 75)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -0.01F, 3.5F, 0.0F,
                        -30F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Tail3 (96,67; box(-1,0,-1; 2x6x2); pivot(0.01,5.5,0); rotateAngleX=-17°)
        // ---------------------------------------
        PartDefinition tail3 = tail2.addOrReplaceChild(
                "Tail3",
                CubeListBuilder.create()
                        .texOffs(96, 67)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.01F, 5.5F, 0.0F,
                        -17F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Tail4 (96,61; box(-1,0,-1; 2x4x2); pivot(-0.01,5.5,0); rotateAngleX=+21°)
        // ---------------------------------------
        PartDefinition tail4 = tail3.addOrReplaceChild(
                "Tail4",
                CubeListBuilder.create()
                        .texOffs(96, 61)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -0.01F, 5.5F, 0.0F,
                        21F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // TailTip (96,55; box(-1,0,-1; 2x4x2); pivot(0.01,3.5,0); rotateAngleX=+21°)
        // ---------------------------------------
        tail4.addOrReplaceChild(
                "TailTip",
                CubeListBuilder.create()
                        .texOffs(96, 55)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.01F, 3.5F, 0.0F,
                        21F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // TailTusk (96,49; box(-1.5,0,-1.5; 3x3x3); pivot(0,3.5,0); rotateAngleX=+21°)
        // ---------------------------------------
        tail4.addOrReplaceChild(
                "TailTusk",
                CubeListBuilder.create()
                        .texOffs(96, 49)
                        .addBox(-1.5F, 0.0F, -1.5F, 3, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 3.5F, 0.0F,
                        21F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Saddle (79,18; box(-4,-1,-3; 8x2x6); pivot(0,0.5,-1))
        // ---------------------------------------
        PartDefinition saddle = chest.addOrReplaceChild(
                "Saddle",
                CubeListBuilder.create()
                        .texOffs(79, 18)
                        .addBox(-4.0F, -1.0F, -3.0F, 8, 2, 6, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.5F, -1.0F)
        );

        // ---------------------------------------
        // SaddleFront (101,26; box(-2.5,-1,-1.5; 5x2x3); pivot(0,-1.0,-1.5); rotateAngleX=-10.6°)
        // ---------------------------------------
        saddle.addOrReplaceChild(
                "SaddleFront",
                CubeListBuilder.create()
                        .texOffs(101, 26)
                        .addBox(-2.5F, -1.0F, -1.5F, 5, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -1.0F, -1.5F,
                        -10.6F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // SaddleBack (77,26; box(-4,-2,-2; 8x2x4); pivot(0,0.7,4); rotateAngleX≈+12.78°)
        // ---------------------------------------
        saddle.addOrReplaceChild(
                "SaddleBack",
                CubeListBuilder.create()
                        .texOffs(77, 26)
                        .addBox(-4.0F, -2.0F, -2.0F, 8, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 0.7F, 4.0F,
                        12.78F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftFootHarness (81,18; box(-0.5,0,-0.5; 1x5x1); pivot(4,0,0.5))
        // ---------------------------------------
        PartDefinition leftFootHarness = saddle.addOrReplaceChild(
                "LeftFootHarness",
                CubeListBuilder.create()
                        .texOffs(81, 18)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, 0.0F, 0.5F)
        );

        // ---------------------------------------
        // LeftFootRing (107,31; box(0,0,0; 1x2x2); pivot(-0.5,5,-1))
        // ---------------------------------------
        leftFootHarness.addOrReplaceChild(
                "LeftFootRing",
                CubeListBuilder.create()
                        .texOffs(107, 31)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 5.0F, -1.0F)
        );

        // ---------------------------------------
        // RightFootHarness (101,18; box(-0.5,0,-0.5; 1x5x1); pivot(-4,0,0.5))
        // ---------------------------------------
        PartDefinition rightFootHarness = saddle.addOrReplaceChild(
                "RightFootHarness",
                CubeListBuilder.create()
                        .texOffs(101, 18)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, 0.0F, 0.5F)
        );

        // ---------------------------------------
        // RightFootRing (101,31; box(0,0,0; 1x2x2); pivot(-0.5,5,-1))
        // ---------------------------------------
        rightFootHarness.addOrReplaceChild(
                "RightFootRing",
                CubeListBuilder.create()
                        .texOffs(101, 31)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 5.0F, -1.0F)
        );

        // ---------------------------------------
        // StorageChest (32,59; box(-5,-2,-2.5; 10x4x5); pivot(0,-2,5.5); rotateAngleX=-90°)
        // ---------------------------------------
        abdomen.addOrReplaceChild(
                "StorageChest",
                CubeListBuilder.create()
                        .texOffs(32, 59)
                        .addBox(-5.0F, -2.0F, -2.5F, 10, 4, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, -2.0F, 5.5F,
                        -90F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // STailRoot (104,79; box(-3,4,5; 6x4x6); pivot(0,8,0); rotateAngleX≈+33°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "STailRoot",
                CubeListBuilder.create()
                        .texOffs(104, 79)
                        .mirror()
                        .addBox(-3.0F, 4.0F, 5.0F, 6, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        0.5796765F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // STail2 (106,69; box(-2.5,7.5,7.3; 5x4x6); pivot(0,8,0); rotateAngleX≈+54.5°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "STail2",
                CubeListBuilder.create()
                        .texOffs(106, 69)
                        .mirror()
                        .addBox(-2.5F, 7.5F, 7.3F, 5, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        0.9514626F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // STail3 (108,60; box(-2,13.5,3.3; 4x3x6); pivot(0,8,0); rotateAngleX≈+95.1°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "STail3",
                CubeListBuilder.create()
                        .texOffs(108, 60)
                        .mirror()
                        .addBox(-2.0F, 13.5F, 3.3F, 4, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        1.660128F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // STail4 (108,51; box(-2,15.2,-5.3; 4x3x6); pivot(0,8,0); rotateAngleX≈+141.8°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "STail4",
                CubeListBuilder.create()
                        .texOffs(108, 51)
                        .mirror()
                        .addBox(-2.0F, 15.2F, -5.3F, 4, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        2.478058F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // STail5 (108,42; box(-2,12.9,-9; 4x3x6); pivot(0,8,0); rotateAngleX≈+173.9°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "STail5",
                CubeListBuilder.create()
                        .texOffs(108, 42)
                        .mirror()
                        .addBox(-2.0F, 12.9F, -9.0F, 4, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        3.035737F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // StingerLump (112,34; box(-1.5,7.9,6; 3x3x5); pivot(0,8,0); rotateAngleX≈+116.4°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "StingerLump",
                CubeListBuilder.create()
                        .texOffs(112, 34)
                        .mirror()
                        .addBox(-1.5F, 7.9F, 6.0F, 3, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        2.031914F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // Stinger (118,29; box(-0.5,1.9,8; 1x1x4); pivot(0,8,0); rotateAngleX≈+69.5°; mirror=true)
        // ---------------------------------------
        root.addOrReplaceChild(
                "Stinger",
                CubeListBuilder.create()
                        .texOffs(118, 29)
                        .mirror()
                        .addBox(-0.5F, 1.9F, 8.0F, 1, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 8.0F, 0.0F,
                        1.213985F, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftUpperLeg (0,96; box(-1.5,0,-2; 3x7x4); pivot(3.99,3,-7); rotateAngleX=+15°)
        // ---------------------------------------
        PartDefinition leftUpperLeg = chest.addOrReplaceChild(
                "LeftUpperLeg",
                CubeListBuilder.create()
                        .texOffs(0, 96)
                        .addBox(-1.5F, 0.0F, -2.0F, 3, 7, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        3.99F, 3.0F, -7.0F,
                        15F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftLowerLeg (0,107; box(-1.5,0,-1.5; 3x6x3); pivot(-0.01,6.5,0.2); rotateAngleX=-21.5°)
        // ---------------------------------------
        PartDefinition leftLowerLeg = leftUpperLeg.addOrReplaceChild(
                "LeftLowerLeg",
                CubeListBuilder.create()
                        .texOffs(0, 107)
                        .addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -0.01F, 6.5F, 0.2F,
                        -21.5F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftFrontFoot (0,116; box(-2,0,-2; 4x2x4); pivot(0,5,-1); rotateAngleX=+6.5°)
        // ---------------------------------------
        PartDefinition leftFrontFoot = leftLowerLeg.addOrReplaceChild(
                "LeftFrontFoot",
                CubeListBuilder.create()
                        .texOffs(0, 116)
                        .addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 5.0F, -1.0F,
                        6.5F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // Left claw sets (all 16,125; box(-0.5,0,-0.5; 1x1x2))
        leftFrontFoot.addOrReplaceChild(
                "LeftClaw1",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -1.3F, 1.2F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, -1F / RADIAN_CONV
                )
        );
        leftFrontFoot.addOrReplaceChild(
                "LeftClaw2",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 1.1F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, 0.0F
                )
        );
        leftFrontFoot.addOrReplaceChild(
                "LeftClaw3",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.3F, 1.2F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, 1F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // RightUpperLeg (14,96; box(-1.5,0,-2; 3x7x4); pivot(-3.99,3,-7); rotateAngleX=+15°)
        // ---------------------------------------
        PartDefinition rightUpperLeg = chest.addOrReplaceChild(
                "RightUpperLeg",
                CubeListBuilder.create()
                        .texOffs(14, 96)
                        .addBox(-1.5F, 0.0F, -2.0F, 3, 7, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -3.99F, 3.0F, -7.0F,
                        15F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightLowerLeg (12,107; box(-1.5,0,-1.5; 3x6x3); pivot(0.01,6.5,0.2); rotateAngleX=-21.5°)
        // ---------------------------------------
        PartDefinition rightLowerLeg = rightUpperLeg.addOrReplaceChild(
                "RightLowerLeg",
                CubeListBuilder.create()
                        .texOffs(12, 107)
                        .addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.01F, 6.5F, 0.2F,
                        -21.5F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightFrontFoot (0,122; box(-2,0,-2; 4x2x4); pivot(0,5,-1); rotateAngleX=+6.5°)
        // ---------------------------------------
        PartDefinition rightFrontFoot = rightLowerLeg.addOrReplaceChild(
                "RightFrontFoot",
                CubeListBuilder.create()
                        .texOffs(0, 122)
                        .addBox(-2.0F, 0.0F, -2.0F, 4, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 5.0F, -1.0F,
                        6.5F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // Right claw sets
        rightFrontFoot.addOrReplaceChild(
                "RightClaw1",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -1.3F, 1.2F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, -1F / RADIAN_CONV
                )
        );
        rightFrontFoot.addOrReplaceChild(
                "RightClaw2",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 1.1F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, 0.0F
                )
        );
        rightFrontFoot.addOrReplaceChild(
                "RightClaw3",
                CubeListBuilder.create()
                        .texOffs(16, 125)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        1.3F, 1.2F, -3.0F,
                        45F / RADIAN_CONV, 0.0F, 1F / RADIAN_CONV
                )
        );

        // ---------------------------------------
        // LeftHindUpperLeg (0,67; box(-2,-1,-1.5; 3x8x5); pivot(3,3,6.8); rotateAngleX=-25°)
        // ---------------------------------------
        PartDefinition leftHindUpperLeg = abdomen.addOrReplaceChild(
                "LeftHindUpperLeg",
                CubeListBuilder.create()
                        .texOffs(0, 67)
                        .addBox(-2.0F, -1.0F, -1.5F, 3, 8, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        3.0F, 3.0F, 6.8F,
                        -25F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // LeftAnkle (0,80; box(-1,0,-1.5; 2x3x3); pivot(-0.5,4,5))
        // ---------------------------------------
        PartDefinition leftAnkle = leftHindUpperLeg.addOrReplaceChild(
                "LeftAnkle",
                CubeListBuilder.create()
                        .texOffs(0, 80)
                        .addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offset( -0.5F, 4.0F, 5.0F )
        );

        // ---------------------------------------
        // LeftHindLowerLeg (0,86; box(-1,0,-1; 2x3x2); pivot(0,3,0.5))
        // ---------------------------------------
        PartDefinition leftHindLowerLeg = leftAnkle.addOrReplaceChild(
                "LeftHindLowerLeg",
                CubeListBuilder.create()
                        .texOffs(0, 86)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset( 0.0F, 3.0F, 0.5F )
        );

        // ---------------------------------------
        // LeftHindFoot (0,91; box(-1.5,0,-1.5; 3x2x3); pivot(0,2.6,-0.8); rotateAngleX=+27°)
        // ---------------------------------------
        leftHindLowerLeg.addOrReplaceChild(
                "LeftHindFoot",
                CubeListBuilder.create()
                        .texOffs(0, 91)
                        .addBox(-1.5F, 0.0F, -1.5F, 3, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 2.6F, -0.8F,
                        27F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightHindUpperLeg (16,67; box(-2,-1,-1.5; 3x8x5); pivot(-2,3,6.8); rotateAngleX=-25°)
        // ---------------------------------------
        PartDefinition rightHindUpperLeg = abdomen.addOrReplaceChild(
                "RightHindUpperLeg",
                CubeListBuilder.create()
                        .texOffs(16, 67)
                        .addBox(-2.0F, -1.0F, -1.5F, 3, 8, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -2.0F, 3.0F, 6.8F,
                        -25F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // ---------------------------------------
        // RightAnkle (10,80; box(-1,0,-1.5; 2x3x3); pivot(-0.5,4,5))
        // ---------------------------------------
        PartDefinition rightAnkle = rightHindUpperLeg.addOrReplaceChild(
                "RightAnkle",
                CubeListBuilder.create()
                        .texOffs(10, 80)
                        .addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 4.0F, 5.0F)
        );

        // ---------------------------------------
        // RightHindLowerLeg (8,86; box(-1,0,-1; 2x3x2); pivot(0,3,0.5))
        // ---------------------------------------
        PartDefinition rightHindLowerLeg = rightAnkle.addOrReplaceChild(
                "RightHindLowerLeg",
                CubeListBuilder.create()
                        .texOffs(8, 86)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, 0.5F)
        );

        // ---------------------------------------
        // RightHindFoot (12,91; box(-1.5,0,-1.5; 3x2x3); pivot(0,2.6,-0.8); rotateAngleX=+27°)
        // ---------------------------------------
        rightHindLowerLeg.addOrReplaceChild(
                "RightHindFoot",
                CubeListBuilder.create()
                        .texOffs(12, 91)
                        .addBox(-1.5F, 0.0F, -1.5F, 3, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        0.0F, 2.6F, -0.8F,
                        27F / RADIAN_CONV, 0.0F, 0.0F
                )
        );

        // Finally, return a LayerDefinition with texture size 128×128
        return LayerDefinition.create(mesh, 128, 128);
    }

    // ------------------------------------------------------------
    // Helper methods to toggle visibility (showModel → visible)
    // ------------------------------------------------------------
    private void renderTeeth(boolean flag) {
        this.LeftFang.visible  = flag;
        this.RightFang.visible = flag;
    }

    private void renderCollar(boolean flag) {
        this.Collar.visible = flag;
    }

    private void renderSaddle(boolean flag) {
        this.NeckHarness.visible  = flag;
        this.HarnessStick.visible = flag;
        this.Saddle.visible       = flag;
    }

    private void renderMane(boolean flag) {
        this.Mane.visible          = flag;
        this.LeftChinBeard.visible = flag;
        this.RightChinBeard.visible= flag;
        this.ForeheadHair.visible  = flag;
        this.NeckHair.visible      = flag;
        this.ChinHair.visible      = flag;
    }

    private void renderChest(boolean flag) {
        this.StorageChest.visible = flag;
    }

    // ------------------------------------------------------------
    // renderToBuffer → exactly reproduce the old render(...) logic
    // ------------------------------------------------------------
    @Override
    public void renderToBuffer(
            PoseStack        poseStack,
            VertexConsumer   buffer,
            int              packedLight,
            int              packedOverlay,
            float            red,
            float            green,
            float            blue,
            float            alpha
    ) {
        // 1) toggle on/off various sub‐parts
        renderSaddle(this.isSaddled);
        renderMane(this.hasMane);
        renderCollar(this.isTamed);
        renderTeeth(this.hasSaberTeeth);
        renderChest(this.hasChest);

        poseStack.pushPose();

        if (this.isGhost) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            // In 1.16.5: RenderSystem.color4f(0.8F,0.8F,0.8F, updateGhostTransparency());
            // updateGhostTransparency() always returned 1.0F before, so it’s effectively opaque.
            RenderSystem.clearColor(0.8F, 0.8F, 0.8F, 1.0F);
        }

        // 2) Draw the main Chest (all its children go automatically)
        this.Chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // 3) Wings if flying
        if (this.isFlyer) {
            this.InnerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.MidWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.OuterWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.InnerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.MidWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.OuterWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // 4) Stinger parts if hasStinger
        if (this.hasStinger) {
            this.STailRoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.STail2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.STail3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.STail4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.STail5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.StingerLump.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Stinger.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // 5) Harness rings if ridden
        if (this.isSaddled && this.isRidden) {
            this.LeftHarness.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RightHarness.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        if (this.isGhost) {
            RenderSystem.disableBlend();
            RenderSystem.clearColor(1F, 1F, 1F, 1F);
        }
        poseStack.popPose();
    }

    /**
     * In 1.16.5 this was setRotationAngles(...). Now it’s setupAnim(...).
     * Copy the exact same logic, but use ModelPart.xRot, yRot, zRot instead of rotateAngleX/Y/Z.
     */
    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Copy all local flags:
        this.bigcat = entity;

        // Interpolation factor for smoother animations already declared above
        // (we’ll reuse INTERPOLATION_FACTOR = 0.1F).

        // Calculate leg rotations (RLegXRot, LLegXRot, gallopRLegXRot, gallopLLegXRot)
        float RLegXRot        = Mth.cos((limbSwing * 0.8F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot        = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
        float gallopRLegXRot  = Mth.cos((limbSwing * 0.6F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float gallopLLegXRot  = Mth.cos(limbSwing * 0.6F) * 0.8F * limbSwingAmount;
        float stingYOffset;
        float stingZOffset;

        // Tail swinging interpolation
        float targetTailAngle = this.movingTail
                ? Mth.cos(ageInTicks * 0.3F)
                : 0.0F;
        float interpolatedTailSwingYaw = this.prevTailSwingYaw + (targetTailAngle - this.prevTailSwingYaw) * INTERPOLATION_FACTOR;
        this.Tail2.yRot = interpolatedTailSwingYaw;
        this.prevTailSwingYaw = interpolatedTailSwingYaw;

        if (this.isSitting) {
            stingYOffset = 17F;
            stingZOffset = -3F;
            this.Chest.y = 14F;
            this.Abdomen.xRot   = -10F / RADIAN_CONV;
            this.Chest.xRot     = -45F / RADIAN_CONV;
            this.RightUpperLeg.xRot = 35F / RADIAN_CONV;
            this.RightLowerLeg.xRot = 5F / RADIAN_CONV;
            this.LeftUpperLeg.xRot  = 35F / RADIAN_CONV;
            this.LeftLowerLeg.xRot  = 5F / RADIAN_CONV;
            this.NeckBase.xRot   = 20F / RADIAN_CONV;
            this.RightHindUpperLeg.y = 1F;
            this.RightHindUpperLeg.xRot = -50F / RADIAN_CONV;
            this.LeftHindUpperLeg.y  = 1F;
            this.LeftHindUpperLeg.xRot  = -50F / RADIAN_CONV;
            this.RightHindFoot.xRot = 90F / RADIAN_CONV;
            this.LeftHindFoot.xRot  = 90F / RADIAN_CONV;
            this.TailRoot.xRot = 100F / RADIAN_CONV;
            this.Tail2.xRot    = 35F / RADIAN_CONV;
            this.Tail3.xRot    = 10F / RADIAN_CONV;
            this.NeckHair.y = 2F;
            this.Collar.xRot     = 0F;
            this.Collar.y = 7F;
            this.Collar.z = -4F;
        } else {
            stingYOffset = 8F;
            stingZOffset = 0F;
            this.Chest.y = 8F;
            this.Abdomen.xRot = 0F;
            this.Chest.xRot   = 0F;
            this.NeckBase.xRot= -14F / RADIAN_CONV;
            this.TailRoot.xRot = 87F / RADIAN_CONV;
            this.Tail2.xRot    = -30F / RADIAN_CONV;
            this.Tail3.xRot    = -17F / RADIAN_CONV;
            this.RightLowerLeg.xRot = -21.5F / RADIAN_CONV;
            this.LeftLowerLeg.xRot  = -21.5F / RADIAN_CONV;
            this.RightHindUpperLeg.y = 3F;
            this.LeftHindUpperLeg.y  = 3F;
            this.RightHindFoot.xRot = 27F / RADIAN_CONV;
            this.LeftHindFoot.xRot  = 27F / RADIAN_CONV;
            this.Collar.z = -2F;
            this.NeckHair.y= -0.5F;
            if (this.hasMane) {
                this.Collar.y = 9F;
            } else {
                this.Collar.y = 6F;
            }
            this.Collar.xRot = (20F / RADIAN_CONV) + Mth.cos(limbSwing * 0.8F) * 0.5F * limbSwingAmount;

            boolean galloping = (limbSwingAmount >= 0.97F);
            if (this.onAir || this.isGhost) {
                if (this.isGhost || (this.isFlyer && limbSwingAmount > 0)) {
                    float speedMov = (limbSwingAmount * 0.5F);
                    this.RightUpperLeg.xRot   = (45F / RADIAN_CONV) + speedMov;
                    this.LeftUpperLeg.xRot    = (45F / RADIAN_CONV) + speedMov;
                    this.RightHindUpperLeg.xRot = (10F / RADIAN_CONV) + speedMov;
                    this.LeftHindUpperLeg.xRot  = (10F / RADIAN_CONV) + speedMov;
                } else if (this.isMovingVertically) {
                    this.RightUpperLeg.xRot   = (-35F / RADIAN_CONV);
                    this.LeftUpperLeg.xRot    = (-35F / RADIAN_CONV);
                    this.RightHindUpperLeg.xRot = (35F / RADIAN_CONV);
                    this.LeftHindUpperLeg.xRot  = (35F / RADIAN_CONV);
                }
            } else {
                if (galloping) {
                    this.RightUpperLeg.xRot = (15F / RADIAN_CONV) + gallopRLegXRot;
                    this.LeftUpperLeg.xRot  = (15F / RADIAN_CONV) + gallopRLegXRot;
                    this.RightHindUpperLeg.xRot = (-25F / RADIAN_CONV) + gallopLLegXRot;
                    this.LeftHindUpperLeg.xRot   = (-25F / RADIAN_CONV) + gallopLLegXRot;
                    this.Abdomen.yRot = 0F;
                } else {
                    this.RightUpperLeg.xRot = (15F / RADIAN_CONV) + RLegXRot;
                    this.LeftHindUpperLeg.xRot  = (-25F / RADIAN_CONV) + RLegXRot;
                    this.LeftUpperLeg.xRot = (15F / RADIAN_CONV) + LLegXRot;
                    this.RightHindUpperLeg.xRot  = (-25F / RADIAN_CONV) + LLegXRot;
                    if (!this.hasStinger) {
                        this.Abdomen.yRot = Mth.cos(limbSwing * 0.3F) * 0.25F * limbSwingAmount;
                    }
                }
            }

            if (this.isRidden) {
                this.LeftFootHarness.xRot  = -60F / RADIAN_CONV;
                this.RightFootHarness.xRot = -60F / RADIAN_CONV;
            } else {
                this.LeftFootHarness.xRot  = RLegXRot / 3F;
                this.RightFootHarness.xRot = RLegXRot / 3F;
                this.LeftFootHarness.zRot  = RLegXRot / 5F;
                this.RightFootHarness.zRot = -RLegXRot / 5F;
            }

            float TailXRot = Mth.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;
            this.TailRoot.xRot = (87F / RADIAN_CONV) + TailXRot;
            this.Tail2.xRot    = (-30F / RADIAN_CONV) + TailXRot;
            this.Tail3.xRot    = (-17F / RADIAN_CONV) + TailXRot;
            this.Tail4.xRot    = (21F / RADIAN_CONV) + TailXRot;
            this.TailTip.xRot  = (21F / RADIAN_CONV) + TailXRot;
            this.TailTusk.xRot = (21F / RADIAN_CONV) + TailXRot;
        }

        // Head rotations
        float headXRot = headPitch / RADIAN_CONV;
        this.HeadBack.xRot = (14F / RADIAN_CONV) + headXRot;
        this.HeadBack.yRot = netHeadYaw / RADIAN_CONV;

        // Mouth interpolation
        float targetMouthAngle;
        if (this.openMouthCounter != 0) {
            if (this.openMouthCounter < 10) {
                targetMouthAngle = 22F + (this.openMouthCounter * 3F);
            } else if (this.openMouthCounter > 20) {
                targetMouthAngle = 22F + (90F - (this.openMouthCounter * 3F));
            } else {
                targetMouthAngle = 55F;
            }
        } else {
            targetMouthAngle = 0F;
        }
        float interpolatedMouthAngle = this.prevMouthAngle + (targetMouthAngle - this.prevMouthAngle) * INTERPOLATION_FACTOR;
        this.LowerJaw.xRot = interpolatedMouthAngle / RADIAN_CONV;
        this.prevMouthAngle = interpolatedMouthAngle;

        // Harness arms if saddled
        if (this.isSaddled) {
            this.LeftHarness.xRot  = 25F / RADIAN_CONV + this.HeadBack.xRot;
            this.LeftHarness.yRot  = this.HeadBack.yRot;
            this.RightHarness.xRot = 25F / RADIAN_CONV + this.HeadBack.xRot;
            this.RightHarness.yRot = this.HeadBack.yRot;
        }

        // Wing flapping/cruising
        if (this.isFlyer) {
            float wingRot;
            if (this.flapwings) {
                wingRot = Mth.cos((ageInTicks * 0.3F) + (float)Math.PI) * 1.2F;
            } else {
                wingRot = Mth.cos(limbSwing * 0.5F) * 0.1F;
            }

            if (this.floating) {
                this.OuterWing.yRot  = -0.3228859F + (wingRot / 2F);
                this.OuterWingR.yRot =  0.3228859F - (wingRot / 2F);
            } else {
                wingRot = 60F / RADIAN_CONV; // ~ +1.0472F radians
                this.OuterWing.yRot  = -90F / RADIAN_CONV;  // ~ -1.5708F
                this.OuterWingR.yRot =  90F / RADIAN_CONV;  // ~ +1.5708F
            }

            this.InnerWingR.y = this.InnerWing.y;
            this.InnerWingR.z = this.InnerWing.z;
            this.OuterWing.x = this.InnerWing.x + (Mth.cos(wingRot) * 12F);
            this.OuterWingR.x = this.InnerWingR.x - (Mth.cos(wingRot) * 12F);

            this.MidWing.y  = this.InnerWing.y;
            this.MidWingR.y = this.InnerWing.y;
            this.OuterWing.y  = this.InnerWing.y + (Mth.sin(wingRot) * 12F);
            this.OuterWingR.y = this.InnerWing.y + (Mth.sin(wingRot) * 12F);

            this.MidWing.z   = this.InnerWing.z;
            this.MidWingR.z  = this.InnerWing.z;
            this.OuterWing.z = this.InnerWing.z;
            this.OuterWingR.z= this.InnerWing.z;

            this.MidWing.zRot   = wingRot;
            this.InnerWing.zRot = wingRot;
            this.OuterWing.zRot = wingRot;

            this.InnerWingR.zRot = -wingRot;
            this.MidWingR.zRot   = -wingRot;
            this.OuterWingR.zRot = -wingRot;

            if (this.hasStinger) {
                if (!this.poisoning) {
                    this.STailRoot.xRot = 33F / RADIAN_CONV;
                    this.STailRoot.y = stingYOffset;
                    this.STailRoot.z = stingZOffset;

                    this.STail2.xRot = 54.5F / RADIAN_CONV;
                    this.STail2.y = stingYOffset;
                    this.STail2.z = stingZOffset;

                    this.STail3.xRot = 95.1F / RADIAN_CONV;
                    this.STail3.y = stingYOffset;
                    this.STail3.z = stingZOffset;

                    this.STail4.xRot = 141.8F / RADIAN_CONV;
                    this.STail4.y = stingYOffset;
                    this.STail4.z = stingZOffset;

                    this.STail5.xRot = 173.9F / RADIAN_CONV;
                    this.STail5.y = stingYOffset;
                    this.STail5.z = stingZOffset;

                    this.StingerLump.xRot = 116.4F / RADIAN_CONV;
                    this.StingerLump.y = stingYOffset;
                    this.StingerLump.z = stingZOffset;

                    this.Stinger.xRot = 69.5F / RADIAN_CONV;
                    this.Stinger.y = stingYOffset;
                    this.Stinger.z = stingZOffset;
                } else if (!this.isSitting) {
                    this.STailRoot.xRot = 95.2F / RADIAN_CONV;
                    this.STailRoot.y = 14.5F;
                    this.STailRoot.z = 2.0F;

                    this.STail2.xRot = 128.5F / RADIAN_CONV;
                    this.STail2.y = 15.0F;
                    this.STail2.z = 4.0F;

                    this.STail3.xRot = 169F / RADIAN_CONV;
                    this.STail3.y = 14.0F;
                    this.STail3.z = 3.8F;

                    this.STail4.xRot = 177F / RADIAN_CONV;
                    this.STail4.y = 13.5F;
                    this.STail4.z = -8.5F;

                    this.STail5.xRot = 180F / RADIAN_CONV;
                    this.STail5.y = 11.5F;
                    this.STail5.z = -17F;

                    this.StingerLump.xRot = 35.4F / RADIAN_CONV;
                    this.StingerLump.y = -4F;
                    this.StingerLump.z = -28F;

                    this.Stinger.xRot = 25.5F / RADIAN_CONV;
                    this.Stinger.y = 4F;
                    this.Stinger.z = -29F;
                }
            }
        }
    }

    /**
     * Keep updateGhostTransparency if you need it in subclasses; originally returned 1.0F.
     */
    public float updateGhostTransparency() {
        return 1.0F;
    }
}
