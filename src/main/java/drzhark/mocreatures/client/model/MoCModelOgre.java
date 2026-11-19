/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.hostile.MoCEntityOgre;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

/**
 * Ported from 1.16.5 → 1.20.1. All ModelRenderer fields are now ModelParts,
 * built via createBodyLayer(). Animations moved to setupAnim(...), rendering
 * moved to renderToBuffer(...). Entity data captured in prepareMobModel(...).
 */
public class MoCModelOgre<T extends MoCEntityOgre> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "ogre"),
            "main"
    );

    private static final float RADIAN_CONV = 57.29578F;
    
    private final ModelPart Head;
    private final ModelPart Brow;
    private final ModelPart NoseBridge;
    private final ModelPart Nose;
    private final ModelPart RgtTusk;
    private final ModelPart RgtTooth;
    private final ModelPart LftTooth;
    private final ModelPart LftTusk;
    private final ModelPart Lip;
    private final ModelPart RgtEar;
    private final ModelPart RgtRing;
    private final ModelPart RgtRingHole;
    private final ModelPart LftEar;
    private final ModelPart LftRing;
    private final ModelPart LftRingHole;
    private final ModelPart HairRope;
    private final ModelPart Hair1;
    private final ModelPart Hair2;
    private final ModelPart Hair3;
    private final ModelPart DiamondHorn;
    private final ModelPart RgtHorn;
    private final ModelPart RgtHornTip;
    private final ModelPart LftHorn;
    private final ModelPart LftHornTip;
    private final ModelPart RgtShoulder;
    private final ModelPart LftShoulder;
    private final ModelPart NeckRest;
    private final ModelPart Chest;
    private final ModelPart Stomach;
    private final ModelPart ButtCover;
    private final ModelPart LoinCloth;
    private final ModelPart RgtThigh;
    private final ModelPart RgtLeg;        // Child of RgtThigh
    private final ModelPart RgtKnee;       // Child of RgtLeg
    private final ModelPart RgtToes;       // Child of RgtLeg
    private final ModelPart RgtBigToe;     // Child of RgtLeg
    private final ModelPart LftThigh;
    private final ModelPart LftLeg;        // Child of LftThigh
    private final ModelPart LftKnee;       // Child of LftLeg
    private final ModelPart LftToes;       // Child of LftLeg
    private final ModelPart LftBigToe;     // Child of LftLeg
    private final ModelPart LftArm;        // Child of LftShoulder
    private final ModelPart LftElbow;      // Child of LftHand
    private final ModelPart LftHand;       // Child of LftArm
    private final ModelPart LftWeaponRoot; // Child of LftHand
    private final ModelPart LftWeaponEnd;  // Child of LftWeaponRoot
    private final ModelPart LftWeaponLump; // Child of LftWeaponRoot
    private final ModelPart LftWeaponBetween; // Child of LftWeaponLump
    private final ModelPart LftWeaponTip;  // Child of LftWeaponBetween
    private final ModelPart LftHammerNeck; // Child of LftWeaponTip
    private final ModelPart LftHammerHeadSupport; // Child of LftWeaponTip
    private final ModelPart LftHammerHead; // Child of LftHammerHeadSupport
    private final ModelPart LftSpike;      // Child of LftWeaponTip
    private final ModelPart LftSpike1;     // Child of LftWeaponTip
    private final ModelPart LftSpike2;     // Child of LftWeaponTip
    private final ModelPart LftSpike3;     // Child of LftWeaponTip
    private final ModelPart LftSpike4;     // Child of LftWeaponTip

    private final ModelPart RgtArm;        // Child of RgtShoulder
    private final ModelPart RgtElbow;      // Child of RgtHand
    private final ModelPart RgtHand;       // Child of RgtArm
    private final ModelPart RgtWeaponRoot; // Child of RgtHand
    private final ModelPart RgtWeaponEnd;  // Child of RgtWeaponRoot
    private final ModelPart RgtWeaponLump; // Child of RgtWeaponRoot
    private final ModelPart RgtWeaponBetween; // Child of RgtWeaponLump
    private final ModelPart RgtWeaponTip;  // Child of RgtWeaponBetween
    private final ModelPart RgtHammerNeck; // Child of RgtWeaponTip
    private final ModelPart RgtHammerHeadSupport; // Child of RgtWeaponTip
    private final ModelPart RgtHammerHead; // Child of RgtHammerHeadSupport
    private final ModelPart RgtSpike;      // Child of RgtWeaponTip
    private final ModelPart RgtSpike1;     // Child of RgtWeaponTip
    private final ModelPart RgtSpike2;     // Child of RgtWeaponTip
    private final ModelPart RgtSpike3;     // Child of RgtWeaponTip
    private final ModelPart RgtSpike4;     // Child of RgtWeaponTip

    private final ModelPart Head3RgtEar;
    private final ModelPart Head3LftEar;
    private final ModelPart Head3Eyelid;
    private final ModelPart Head3Nose;
    private final ModelPart Head3;
    private final ModelPart Head3Brow;
    private final ModelPart Head3Hair;
    private final ModelPart Head3Lip;
    private final ModelPart Head3RgtTusk;
    private final ModelPart Head3RgtTooth;
    private final ModelPart Head3LftTooth;
    private final ModelPart Head3LftTusk;
    private final ModelPart Head3RingHole;
    private final ModelPart Head3Ring;

    private final ModelPart Head2Chin;
    private final ModelPart Head2;
    private final ModelPart Head2Lip;
    private final ModelPart Head2LftTusk;
    private final ModelPart Head2RgtTusk;
    private final ModelPart Head2Nose;
    private final ModelPart Head2NoseBridge;
    private final ModelPart Head2Brow;
    private final ModelPart Head2RgtHorn;
    private final ModelPart Head2LftHorn;
    private final ModelPart Head2DiamondHorn;

    // Entity data captured in prepareMobModel(...)
    private int type;
    private int attackCounter;
    private int headMoving;
    private int armToAnimate;

    public MoCModelOgre(ModelPart root) {
        super(root);
        // Attach each ModelPart by name
        this.Head                   = root.getChild("Head");
        this.Brow                   = root.getChild("Brow");
        this.NoseBridge             = root.getChild("NoseBridge");
        this.Nose                   = root.getChild("Nose");
        this.RgtTusk                = root.getChild("RgtTusk");
        this.RgtTooth               = root.getChild("RgtTooth");
        this.LftTooth               = root.getChild("LftTooth");
        this.LftTusk                = root.getChild("LftTusk");
        this.Lip                    = root.getChild("Lip");
        this.RgtEar                 = root.getChild("RgtEar");
        this.RgtRing                = root.getChild("RgtRing");
        this.RgtRingHole            = root.getChild("RgtRingHole");
        this.LftEar                 = root.getChild("LftEar");
        this.LftRing                = root.getChild("LftRing");
        this.LftRingHole            = root.getChild("LftRingHole");
        this.HairRope               = root.getChild("HairRope");
        this.Hair1                  = root.getChild("Hair1");
        this.Hair2                  = root.getChild("Hair2");
        this.Hair3                  = root.getChild("Hair3");
        this.DiamondHorn            = root.getChild("DiamondHorn");
        this.RgtHorn                = root.getChild("RgtHorn");
        this.RgtHornTip             = root.getChild("RgtHornTip");
        this.LftHorn                = root.getChild("LftHorn");
        this.LftHornTip             = root.getChild("LftHornTip");

        this.NeckRest               = root.getChild("NeckRest");
        this.Chest                  = root.getChild("Chest");
        this.Stomach                = root.getChild("Stomach");
        this.ButtCover              = root.getChild("ButtCover");
        this.LoinCloth              = root.getChild("LoinCloth");

        this.RgtThigh               = root.getChild("RgtThigh");
        this.RgtLeg                 = RgtThigh.getChild("RgtLeg");
        this.RgtKnee                = RgtLeg.getChild("RgtKnee");
        this.RgtToes                = RgtLeg.getChild("RgtToes");
        this.RgtBigToe              = RgtLeg.getChild("RgtBigToe");

        this.LftThigh               = root.getChild("LftThigh");
        this.LftLeg                 = LftThigh.getChild("LftLeg");
        this.LftKnee                = LftLeg.getChild("LftKnee");
        this.LftToes                = LftLeg.getChild("LftToes");
        this.LftBigToe              = LftLeg.getChild("LftBigToe");

        this.LftShoulder            = root.getChild("LftShoulder");
        this.LftArm                 = LftShoulder.getChild("LftArm");
        this.LftHand                = LftArm.getChild("LftHand");
        this.LftElbow               = LftHand.getChild("LftElbow");
        this.LftWeaponRoot          = LftHand.getChild("LftWeaponRoot");
        this.LftWeaponEnd           = LftWeaponRoot.getChild("LftWeaponEnd");
        this.LftWeaponLump          = LftWeaponRoot.getChild("LftWeaponLump");
        this.LftWeaponBetween       = LftWeaponLump.getChild("LftWeaponBetween");
        this.LftWeaponTip           = LftWeaponBetween.getChild("LftWeaponTip");
        this.LftHammerNeck          = LftWeaponTip.getChild("LftHammerNeck");
        this.LftHammerHeadSupport   = LftWeaponTip.getChild("LftHammerHeadSupport");
        this.LftHammerHead          = LftHammerHeadSupport.getChild("LftHammerHead");
        this.LftSpike               = LftWeaponTip.getChild("LftSpike");
        this.LftSpike1              = LftWeaponTip.getChild("LftSpike1");
        this.LftSpike2              = LftWeaponTip.getChild("LftSpike2");
        this.LftSpike3              = LftWeaponTip.getChild("LftSpike3");
        this.LftSpike4              = LftWeaponTip.getChild("LftSpike4");

        this.RgtShoulder            = root.getChild("RgtShoulder");
        this.RgtArm                 = RgtShoulder.getChild("RgtArm");
        this.RgtHand                = RgtArm.getChild("RgtHand");
        this.RgtElbow               = RgtHand.getChild("RgtElbow");
        this.RgtWeaponRoot          = RgtHand.getChild("RgtWeaponRoot");
        this.RgtWeaponEnd           = RgtWeaponRoot.getChild("RgtWeaponEnd");
        this.RgtWeaponLump          = RgtWeaponRoot.getChild("RgtWeaponLump");
        this.RgtWeaponBetween       = RgtWeaponLump.getChild("RgtWeaponBetween");
        this.RgtWeaponTip           = RgtWeaponBetween.getChild("RgtWeaponTip");
        this.RgtHammerNeck          = RgtWeaponTip.getChild("RgtHammerNeck");
        this.RgtHammerHeadSupport   = RgtWeaponTip.getChild("RgtHammerHeadSupport");
        this.RgtHammerHead          = RgtHammerHeadSupport.getChild("RgtHammerHead");
        this.RgtSpike               = RgtWeaponTip.getChild("RgtSpike");
        this.RgtSpike1              = RgtWeaponTip.getChild("RgtSpike1");
        this.RgtSpike2              = RgtWeaponTip.getChild("RgtSpike2");
        this.RgtSpike3              = RgtWeaponTip.getChild("RgtSpike3");
        this.RgtSpike4              = RgtWeaponTip.getChild("RgtSpike4");

        this.Head3RgtEar            = root.getChild("Head3RgtEar");
        this.Head3LftEar            = root.getChild("Head3LftEar");
        this.Head3Eyelid            = root.getChild("Head3Eyelid");
        this.Head3Nose              = root.getChild("Head3Nose");
        this.Head3                  = root.getChild("Head3");
        this.Head3Brow              = root.getChild("Head3Brow");
        this.Head3Hair              = root.getChild("Head3Hair");
        this.Head3Lip               = root.getChild("Head3Lip");
        this.Head3RgtTusk           = root.getChild("Head3RgtTusk");
        this.Head3RgtTooth          = root.getChild("Head3RgtTooth");
        this.Head3LftTooth          = root.getChild("Head3LftTooth");
        this.Head3LftTusk           = root.getChild("Head3LftTusk");
        this.Head3RingHole          = root.getChild("Head3RingHole");
        this.Head3Ring              = root.getChild("Head3Ring");

        this.Head2Chin              = root.getChild("Head2Chin");
        this.Head2                  = root.getChild("Head2");
        this.Head2Lip               = root.getChild("Head2Lip");
        this.Head2LftTusk           = root.getChild("Head2LftTusk");
        this.Head2RgtTusk           = root.getChild("Head2RgtTusk");
        this.Head2Nose              = root.getChild("Head2Nose");
        this.Head2NoseBridge        = root.getChild("Head2NoseBridge");
        this.Head2Brow              = root.getChild("Head2Brow");
        this.Head2RgtHorn           = root.getChild("Head2RgtHorn");
        this.Head2LftHorn           = root.getChild("Head2LftHorn");
        this.Head2DiamondHorn       = root.getChild("Head2DiamondHorn");
    }

    /**
     * Build the LayerDefinition (MeshDefinition → PartDefinition).
     * Each child corresponds to an old ModelRenderer field.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD PIECES
        root.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Brow",
                CubeListBuilder.create()
                        .texOffs(68, 7)
                        .addBox(-5.0F, -10.5F, -8.0F, 10, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, -0.0872665F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "NoseBridge",
                CubeListBuilder.create()
                        .texOffs(80, 4)
                        .addBox(-1.0F, -7.0F, -8.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, -0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Nose",
                CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(-2.0F, -7.0F, -7.0F, 4, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0872665F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtTusk",
                CubeListBuilder.create()
                        .texOffs(60, 4)
                        .addBox(-3.5F, -6.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtTooth",
                CubeListBuilder.create()
                        .texOffs(64, 4)
                        .addBox(-1.5F, -5.0F, -6.5F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftTooth",
                CubeListBuilder.create()
                        .texOffs(72, 4)
                        .addBox(0.5F, -5.0F, -6.5F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftTusk",
                CubeListBuilder.create()
                        .texOffs(76, 4)
                        .addBox(2.5F, -6.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Lip",
                CubeListBuilder.create()
                        .texOffs(60, 0)
                        .addBox(-4.0F, -4.0F, -7.0F, 8, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtEar",
                CubeListBuilder.create()
                        .texOffs(60, 12)
                        .addBox(-9.0F, -9.0F, -1.0F, 3, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtRing",
                CubeListBuilder.create()
                        .texOffs(32, 58)
                        .addBox(-8.0F, -6.0F, -2.0F, 1, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtRingHole",
                CubeListBuilder.create()
                        .texOffs(26, 50)
                        .addBox(-8.0F, -5.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftEar",
                CubeListBuilder.create()
                        .texOffs(70, 12)
                        .addBox(6.0F, -9.0F, -1.0F, 3, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftRing",
                CubeListBuilder.create()
                        .texOffs(32, 58)
                        .addBox(7.0F, -6.0F, -2.0F, 1, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftRingHole",
                CubeListBuilder.create()
                        .texOffs(26, 50)
                        .addBox(7.0F, -5.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "HairRope",
                CubeListBuilder.create()
                        .texOffs(82, 83)
                        .addBox(-2.0F, -8.0F, 9.0F, 4, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.6108652F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Hair1",
                CubeListBuilder.create()
                        .texOffs(78, 107)
                        .addBox(-3.0F, -9.0F, 13.0F, 6, 8, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.6108652F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Hair2",
                CubeListBuilder.create()
                        .texOffs(60, 107)
                        .addBox(-3.0F, -6.5F, 11.6F, 6, 8, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.2617994F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Hair3",
                CubeListBuilder.create()
                        .texOffs(42, 107)
                        .addBox(-3.0F, -2.4F, 11.4F, 6, 8, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "DiamondHorn",
                CubeListBuilder.create()
                        .texOffs(120, 31)
                        .addBox(-1.0F, -17.0F, -6.0F, 2, 6, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0872665F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtHorn",
                CubeListBuilder.create()
                        .texOffs(46, 6)
                        .addBox(-6.0F, -12.0F, -11.0F, 2, 2, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "RgtHornTip",
                CubeListBuilder.create()
                        .texOffs(44, 13)
                        .addBox(-6.0F, -15.0F, -11.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftHorn",
                CubeListBuilder.create()
                        .texOffs(46, 6)
                        .addBox(4.0F, -12.0F, -11.0F, 2, 2, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "LftHornTip",
                CubeListBuilder.create()
                        .texOffs(52, 13)
                        .addBox(4.0F, -15.0F, -11.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 0.0F)
        );

        // BODY PIECES
        root.addOrReplaceChild(
                "NeckRest",
                CubeListBuilder.create()
                        .texOffs(39, 20)
                        .addBox(-7.0F, -19.0F, -3.0F, 14, 3, 11, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 5.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Chest",
                CubeListBuilder.create()
                        .texOffs(32, 34)
                        .addBox(-9.5F, -17.8F, -7.3F, 19, 11, 13, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Stomach",
                CubeListBuilder.create()
                        .texOffs(28, 58)
                        .addBox(-11.0F, -8.0F, -6.0F, 22, 11, 14, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 5.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "ButtCover",
                CubeListBuilder.create()
                        .texOffs(32, 118)
                        .addBox(-4.0F, 0.0F, 0.0F, 8, 8, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, 6.0F)
        );

        root.addOrReplaceChild(
                "LoinCloth",
                CubeListBuilder.create()
                        .texOffs(32, 118)
                        .addBox(-4.0F, 0.0F, -2.0F, 8, 8, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, -4.0F)
        );

        // RIGHT LEG BRANCH
        PartDefinition rgtThigh = root.addOrReplaceChild(
                "RgtThigh",
                CubeListBuilder.create()
                        .texOffs(0, 83)
                        .addBox(-10.0F, 0.0F, -5.0F, 10, 11, 10, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 4.0F, 1.0F)
        );

        PartDefinition rgtLeg = rgtThigh.addOrReplaceChild(
                "RgtLeg",
                CubeListBuilder.create()
                        .texOffs(0, 104)
                        .addBox(-4.0F, -1.0F, -4.0F, 8, 11, 8, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 10.0F, 0.0F)
        );

        rgtLeg.addOrReplaceChild(
                "RgtKnee",
                CubeListBuilder.create()
                        .texOffs(0, 88)
                        .addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, -4.25F)
        );

        rgtLeg.addOrReplaceChild(
                "RgtToes",
                CubeListBuilder.create()
                        .texOffs(0, 123)
                        .addBox(-2.5F, -1.0F, -3.0F, 5, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(-1.5F, 9.0F, -3.5F)
        );

        rgtLeg.addOrReplaceChild(
                "RgtBigToe",
                CubeListBuilder.create()
                        .texOffs(20, 123)
                        .addBox(-1.5F, -1.0F, -3.0F, 3, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(2.5F, 9.0F, -4.0F)
        );

        // LEFT LEG BRANCH
        PartDefinition lftThigh = root.addOrReplaceChild(
                "LftThigh",
                CubeListBuilder.create()
                        .texOffs(88, 83)
                        .addBox(0.0F, 0.0F, -5.0F, 10, 11, 10, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 4.0F, 1.0F)
        );

        PartDefinition lftLeg = lftThigh.addOrReplaceChild(
                "LftLeg",
                CubeListBuilder.create()
                        .texOffs(96, 104)
                        .addBox(-4.0F, -1.0F, -4.0F, 8, 11, 8, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 10.0F, 0.0F)
        );

        lftLeg.addOrReplaceChild(
                "LftKnee",
                CubeListBuilder.create()
                        .texOffs(118, 88)
                        .addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, -4.25F)
        );

        lftLeg.addOrReplaceChild(
                "LftToes",
                CubeListBuilder.create()
                        .texOffs(112, 123)
                        .addBox(-2.5F, -1.0F, -3.0F, 5, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(1.5F, 9.0F, -3.5F)
        );

        lftLeg.addOrReplaceChild(
                "LftBigToe",
                CubeListBuilder.create()
                        .texOffs(96, 123)
                        .addBox(-1.5F, -1.0F, -3.0F, 3, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 9.0F, -4.0F)
        );

        // LEFT ARM BRANCH
        PartDefinition lftShoulder = root.addOrReplaceChild(
                "LftShoulder",
                CubeListBuilder.create()
                        .texOffs(96, 31)
                        .addBox(0.0F, -3.0F, -4.0F, 8, 7, 8, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -10.0F, 2.0F)
        );

        PartDefinition lftArm = lftShoulder.addOrReplaceChild(
                "LftArm",
                CubeListBuilder.create()
                        .texOffs(100, 66)
                        .addBox(0.0F, 0.0F, -4.0F, 6, 9, 8, new CubeDeformation(0.0F)),
                PartPose.offset(6.0F, -1.0F, 1.0F)
        );

        PartDefinition lftHand = lftArm.addOrReplaceChild(
                "LftHand",
                CubeListBuilder.create()
                        .texOffs(96, 46)
                        .addBox(-4.0F, 0.0F, -4.0F, 8, 12, 8, new CubeDeformation(0.0F)),
                PartPose.offset(3.0F, 8.0F, -1.0F)
        );

        lftHand.addOrReplaceChild(
                "LftElbow",
                CubeListBuilder.create()
                        .texOffs(86, 64)
                        .addBox(-2.0F, -1.5F, -0.5F, 4, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, 4.0F)
        );

        PartDefinition lftWeaponRoot = lftHand.addOrReplaceChild(
                "LftWeaponRoot",
                CubeListBuilder.create()
                        .texOffs(24, 104)
                        .addBox(-1.5F, -1.5F, -4.0F, 3, 3, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 8.5F, -4.0F)
        );

        PartDefinition lftWeaponEnd = lftWeaponRoot.addOrReplaceChild(
                "LftWeaponEnd",
                CubeListBuilder.create()
                        .texOffs(74, 90)
                        .addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 8.0F)
        );

        PartDefinition lftWeaponLump = lftWeaponRoot.addOrReplaceChild(
                "LftWeaponLump",
                CubeListBuilder.create()
                        .texOffs(30, 83)
                        .addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -4.0F)
        );

        PartDefinition lftWeaponBetween = lftWeaponLump.addOrReplaceChild(
                "LftWeaponBetween",
                CubeListBuilder.create()
                        .texOffs(83, 42)
                        .addBox(-1.5F, -1.5F, -2.0F, 3, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -4.0F)
        );

        PartDefinition lftWeaponTip = lftWeaponBetween.addOrReplaceChild(
                "LftWeaponTip",
                CubeListBuilder.create()
                        .texOffs(60, 118)
                        .addBox(-2.5F, -2.5F, -5.0F, 5, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -2.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftHammerNeck",
                CubeListBuilder.create()
                        .texOffs(32, 39)
                        .addBox(-0.5F, -4.0F, -4.0F, 1, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -2.5F, -1.0F)
        );

        PartDefinition lftHammerHeadSupport = lftWeaponTip.addOrReplaceChild(
                "LftHammerHeadSupport",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, -2.0F, 2, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, -3.0F)
        );

        lftHammerHeadSupport.addOrReplaceChild(
                "LftHammerHead",
                CubeListBuilder.create()
                        .texOffs(32, 3)
                        .addBox(-2.0F, 0.0F, -2.5F, 4, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, 0.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftSpike",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, -1.0F, -3.0F, 2, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -5.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftSpike1",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 0.0F, -3.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftSpike2",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(3.0F, -1.0F, -1.0F, 3, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 0.0F, -3.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftSpike3",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, -3.0F)
        );

        lftWeaponTip.addOrReplaceChild(
                "LftSpike4",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -2.5F, -3.0F)
        );

        // RIGHT ARM BRANCH
        PartDefinition rgtShoulder = root.addOrReplaceChild(
                "RgtShoulder",
                CubeListBuilder.create()
                        .texOffs(0, 31)
                        .addBox(0.0F, -3.0F, -4.0F, 8, 7, 8, new CubeDeformation(0.0F)),
                PartPose.offset(-15.0F, -10.0F, 2.0F)
        );

        PartDefinition rgtArm = rgtShoulder.addOrReplaceChild(
                "RgtArm",
                CubeListBuilder.create()
                        .texOffs(0, 66)
                        .addBox(0.0F, 0.0F, -4.0F, 6, 9, 8, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, -1.0F, 1.0F)
        );

        PartDefinition rgtHand = rgtArm.addOrReplaceChild(
                "RgtHand",
                CubeListBuilder.create()
                        .texOffs(0, 46)
                        .addBox(-4.0F, 0.0F, -4.0F, 8, 12, 8, new CubeDeformation(0.0F)),
                PartPose.offset(3.0F, 8.0F, -1.0F)
        );

        rgtHand.addOrReplaceChild(
                "RgtElbow",
                CubeListBuilder.create()
                        .texOffs(86, 64)
                        .addBox(-2.0F, -1.5F, -0.5F, 4, 3, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, 4.0F)
        );

        PartDefinition rgtWeaponRoot = rgtHand.addOrReplaceChild(
                "RgtWeaponRoot",
                CubeListBuilder.create()
                        .texOffs(24, 104)
                        .addBox(-1.5F, -1.5F, -4.0F, 3, 3, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 8.5F, -4.0F)
        );

        PartDefinition rgtWeaponEnd = rgtWeaponRoot.addOrReplaceChild(
                "RgtWeaponEnd",
                CubeListBuilder.create()
                        .texOffs(74, 90)
                        .addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 8.0F)
        );

        PartDefinition rgtWeaponLump = rgtWeaponRoot.addOrReplaceChild(
                "RgtWeaponLump",
                CubeListBuilder.create()
                        .texOffs(30, 83)
                        .addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -4.0F)
        );

        PartDefinition rgtWeaponBetween = rgtWeaponLump.addOrReplaceChild(
                "RgtWeaponBetween",
                CubeListBuilder.create()
                        .texOffs(83, 42)
                        .addBox(-1.5F, -1.5F, -2.0F, 3, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -4.0F)
        );

        PartDefinition rgtWeaponTip = rgtWeaponBetween.addOrReplaceChild(
                "RgtWeaponTip",
                CubeListBuilder.create()
                        .texOffs(60, 118)
                        .addBox(-2.5F, -2.5F, -5.0F, 5, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -2.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtHammerNeck",
                CubeListBuilder.create()
                        .texOffs(32, 39)
                        .addBox(-0.5F, -4.0F, -4.0F, 1, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -2.5F, -1.0F)
        );

        PartDefinition rgtHammerHeadSupport = rgtWeaponTip.addOrReplaceChild(
                "RgtHammerHeadSupport",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, -2.0F, 2, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, -3.0F)
        );

        rgtHammerHeadSupport.addOrReplaceChild(
                "RgtHammerHead",
                CubeListBuilder.create()
                        .texOffs(32, 3)
                        .addBox(-2.0F, 0.0F, -2.5F, 4, 3, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, 0.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtSpike",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, -1.0F, -3.0F, 2, 2, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -5.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtSpike1",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 0.0F, -3.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtSpike2",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(3.0F, -1.0F, -1.0F, 3, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-0.5F, 0.0F, -3.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtSpike3",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.5F, -3.0F)
        );

        rgtWeaponTip.addOrReplaceChild(
                "RgtSpike4",
                CubeListBuilder.create()
                        .texOffs(52, 118)
                        .addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -2.5F, -3.0F)
        );

        // HEAD VARIANTS
        root.addOrReplaceChild(
                "Head3RgtEar",
                CubeListBuilder.create()
                        .texOffs(110, 24)
                        .addBox(-8.0F, -9.0F, -1.0F, 3, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3LftEar",
                CubeListBuilder.create()
                        .texOffs(100, 24)
                        .addBox(5.0F, -9.0F, -1.0F, 3, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Eyelid",
                CubeListBuilder.create()
                        .texOffs(46, 3)
                        .addBox(-3.0F, -8.0F, -4.5F, 6, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.2617994F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Nose",
                CubeListBuilder.create()
                        .texOffs(60, 9)
                        .addBox(-1.5F, -8.5F, -3.5F, 3, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.4886922F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3",
                CubeListBuilder.create()
                        .texOffs(42, 83)
                        .addBox(-5.0F, -12.0F, -6.0F, 10, 12, 12, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Brow",
                CubeListBuilder.create()
                        .texOffs(46, 0)
                        .addBox(-3.0F, -9.0F, -8.5F, 6, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, -0.2617994F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Hair",
                CubeListBuilder.create()
                        .texOffs(80, 118)
                        .addBox(-2.0F, -17.0F, -5.0F, 4, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, -0.6108652F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Lip",
                CubeListBuilder.create()
                        .texOffs(22, 68)
                        .addBox(-4.0F, -4.0F, -7.0F, 8, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3RgtTusk",
                CubeListBuilder.create()
                        .texOffs(83, 34)
                        .addBox(-3.5F, -6.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3RgtTooth",
                CubeListBuilder.create()
                        .texOffs(87, 34)
                        .addBox(-1.5F, -5.0F, -6.5F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3LftTooth",
                CubeListBuilder.create()
                        .texOffs(96, 34)
                        .addBox(0.5F, -5.0F, -6.5F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3LftTusk",
                CubeListBuilder.create()
                        .texOffs(100, 34)
                        .addBox(2.5F, -6.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3RingHole",
                CubeListBuilder.create()
                        .texOffs(26, 50)
                        .addBox(6.0F, -5.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head3Ring",
                CubeListBuilder.create()
                        .texOffs(32, 58)
                        .addBox(6.0F, -6.0F, -2.0F, 1, 4, 4, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -13.0F, 0.0F)
        );

        // HEAD2 VARIANT
        root.addOrReplaceChild(
                "Head2Chin",
                CubeListBuilder.create()
                        .texOffs(21, 24)
                        .addBox(-3.0F, -5.0F, -8.0F, 6, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 0.2617994F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -12.0F, -6.0F, 10, 12, 12, new CubeDeformation(0.0F)),
                PartPose.offset(-7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2Lip",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-4.0F, -5.0F, -8.0F, 8, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-7.0F, -13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2LftTusk",
                CubeListBuilder.create()
                        .texOffs(46, 28)
                        .addBox(2.5F, -8.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2RgtTusk",
                CubeListBuilder.create()
                        .texOffs(39, 28)
                        .addBox(-3.5F, -8.0F, -6.5F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2Nose",
                CubeListBuilder.create()
                        .texOffs(116, 0)
                        .addBox(-2.0F, -7.0F, -7.0F, 4, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 0.0872665F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2NoseBridge",
                CubeListBuilder.create()
                        .texOffs(116, 4)
                        .addBox(-1.0F, -7.0F, -8.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, -0.1745329F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2Brow",
                CubeListBuilder.create()
                        .texOffs(80, 24)
                        .addBox(-4.0F, -10.5F, -8.0F, 8, 3, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, -0.0872665F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2RgtHorn",
                CubeListBuilder.create()
                        .texOffs(24, 30)
                        .addBox(-4.0F, -8.0F, -15.0F, 2, 2, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, -0.5235988F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2LftHorn",
                CubeListBuilder.create()
                        .texOffs(24, 30)
                        .addBox(2.0F, -8.0F, -15.0F, 2, 2, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, -0.5235988F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "Head2DiamondHorn",
                CubeListBuilder.create()
                        .texOffs(120, 46)
                        .addBox(-1.0F, -17.0F, -6.0F, 2, 6, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 0.0872665F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 128, 128);
    }

    /**
     * Capture entity-specific data from setLivingAnimations(...).
     * In 1.20.1, override prepareMobModel(...) instead.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.type           = entity.getTypeMoC();
        this.attackCounter  = entity.attackCounter;
        this.headMoving     = entity.getMovingHead();
        this.armToAnimate   = entity.armToAnimate;
    }

    /**
     * Copy old setRotationAngles(...) logic here. Now called setupAnim(...)
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
        float hRotY = netHeadYaw / RADIAN_CONV;
        float hRotX = headPitch / RADIAN_CONV;

        // Leg swing factors
        float RLegXRot  = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot  = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
        float ClothRot  = Mth.cos(limbSwing * 0.9F) * 0.6F * limbSwingAmount;

        float RLegXRotB = RLegXRot;
        float LLegXRotB = LLegXRot;

        // Adjusted swings for pronounced step
        float RLegXRot2 = Mth.cos(((limbSwing + 0.1F) * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot2 = Mth.cos((limbSwing + 0.1F) * 0.6662F) * 0.8F * limbSwingAmount;
        if (limbSwingAmount > 0.15F) {
            if (RLegXRot > RLegXRot2) {
                RLegXRotB = RLegXRot + (25.0F / RADIAN_CONV);
            }
            if (LLegXRot > LLegXRot2) {
                LLegXRotB = LLegXRot + (25.0F / RADIAN_CONV);
            }
        }

        // Apply to thighs and lower legs
        this.RgtThigh.xRot = RLegXRot;
        this.LftThigh.xRot = LLegXRot;
        this.RgtLeg.xRot   = RLegXRotB;
        this.LftLeg.xRot   = LLegXRotB;

        // Cloth and butt sway
        this.LoinCloth.xRot = ClothRot;
        this.ButtCover.xRot = ClothRot;

        // Arm attack movement
        float armMov = - (Mth.cos(attackCounter * 0.18F) * 3.0F);

        // LEFT ARM
        if (this.armToAnimate == 1 || this.armToAnimate == 3) {
            this.LftShoulder.xRot = armMov;
            this.LftHand.xRot     = -45.0F / RADIAN_CONV;
        } else {
            this.LftShoulder.zRot = (Mth.cos(ageInTicks * 0.09F) * 0.05F) - 0.05F;
            this.LftShoulder.xRot = RLegXRot;
            this.LftHand.xRot     = 0.0F;
        }

        // RIGHT ARM
        if (this.armToAnimate == 2 || this.armToAnimate == 3) {
            this.RgtShoulder.xRot = armMov;
            this.RgtHand.xRot     = -45.0F / RADIAN_CONV;
        } else {
            this.RgtShoulder.zRot = - (Mth.cos(ageInTicks * 0.09F) * 0.05F) + 0.05F;
            this.RgtShoulder.xRot = LLegXRot;
            this.RgtHand.xRot     = 0.0F;
        }

        // HEAD VARIANTS: Head2 or Head3 rotation
        if (this.headMoving == 2) {
            this.Head2.xRot = hRotX;
            this.Head2.yRot = hRotY;
        }
        if (this.headMoving == 3) {
            this.Head3.xRot = hRotX;
            this.Head3.yRot = hRotY;
        }

        // MAIN HEAD piece if type = 1,3,5
        if (type == 1 || type == 3 || type == 5) {
            this.Head.xRot = hRotX;
            this.Head.yRot = hRotY;

            // Sync sub‐parts to head
            this.Brow.xRot            = this.Head.xRot;
            this.NoseBridge.xRot      = this.Head.xRot;
            this.Nose.xRot            = this.Head.xRot;
            this.RgtTusk.xRot         = this.Head.xRot;
            this.RgtTooth.xRot        = this.Head.xRot;
            this.LftTooth.xRot        = this.Head.xRot;
            this.LftTusk.xRot         = this.Head.xRot;
            this.Lip.xRot             = this.Head.xRot;
            this.RgtEar.xRot          = this.Head.xRot;
            this.RgtRing.xRot         = this.Head.xRot;
            this.RgtRingHole.xRot     = this.Head.xRot;
            this.LftEar.xRot          = this.Head.xRot;
            this.LftRing.xRot         = this.Head.xRot;
            this.LftRingHole.xRot     = this.Head.xRot;
            this.HairRope.xRot        = 0.6108652F + this.Head.xRot;
            this.Hair1.xRot           = 0.6108652F + this.Head.xRot;
            this.Hair2.xRot           = 0.2617994F + this.Head.xRot;
            this.Hair3.xRot           = this.Head.xRot;
            this.DiamondHorn.xRot     = 0.0872665F + this.Head.xRot;
            this.RgtHorn.xRot         = this.Head.xRot;
            this.RgtHornTip.xRot      = this.Head.xRot;
            this.LftHorn.xRot         = this.Head.xRot;
            this.LftHornTip.xRot      = this.Head.xRot;

            this.Brow.yRot            = this.Head.yRot;
            this.NoseBridge.yRot      = this.Head.yRot;
            this.Nose.yRot            = this.Head.yRot;
            this.RgtTusk.yRot         = this.Head.yRot;
            this.RgtTooth.yRot        = this.Head.yRot;
            this.LftTooth.yRot        = this.Head.yRot;
            this.LftTusk.yRot         = this.Head.yRot;
            this.Lip.yRot             = this.Head.yRot;
            this.RgtEar.yRot          = this.Head.yRot;
            this.RgtRing.yRot         = this.Head.yRot;
            this.RgtRingHole.yRot     = this.Head.yRot;
            this.LftEar.yRot          = this.Head.yRot;
            this.LftRing.yRot         = this.Head.yRot;
            this.LftRingHole.yRot     = this.Head.yRot;
            this.HairRope.yRot        = this.Head.yRot;
            this.Hair1.yRot           = this.Head.yRot;
            this.Hair2.yRot           = this.Head.yRot;
            this.Hair3.yRot           = this.Head.yRot;
            this.DiamondHorn.yRot     = this.Head.yRot;
            this.RgtHorn.yRot         = this.Head.yRot;
            this.RgtHornTip.yRot      = this.Head.yRot;
            this.LftHorn.yRot         = this.Head.yRot;
            this.LftHornTip.yRot      = this.Head.yRot;
        } else {
            // HEAD3 sub‐parts
            this.Head3RgtEar.xRot   = this.Head3.xRot;
            this.Head3LftEar.xRot   = this.Head3.xRot;
            this.Head3Eyelid.xRot   = 0.2617994F + this.Head3.xRot;
            this.Head3Nose.xRot     = 0.4886922F + this.Head3.xRot;
            this.Head3Brow.xRot     = -0.2617994F + this.Head3.xRot;
            this.Head3Hair.xRot     = -0.6108652F + this.Head3.xRot;
            this.Head3Lip.xRot      = 0.1745329F + this.Head3.xRot;
            this.Head3RgtTusk.xRot  = 0.1745329F + this.Head3.xRot;
            this.Head3RgtTooth.xRot = 0.1745329F + this.Head3.xRot;
            this.Head3LftTooth.xRot = 0.1745329F + this.Head3.xRot;
            this.Head3LftTusk.xRot  = 0.1745329F + this.Head3.xRot;
            this.Head3RingHole.xRot = this.Head3.xRot;
            this.Head3Ring.xRot     = this.Head3.xRot;

            this.Head3RgtEar.yRot   = this.Head3.yRot;
            this.Head3LftEar.yRot   = this.Head3.yRot;
            this.Head3Eyelid.yRot   = this.Head3.yRot;
            this.Head3Nose.yRot     = this.Head3.yRot;
            this.Head3Brow.yRot     = this.Head3.yRot;
            this.Head3Hair.yRot     = this.Head3.yRot;
            this.Head3Lip.yRot      = this.Head3.yRot;
            this.Head3RgtTusk.yRot  = this.Head3.yRot;
            this.Head3RgtTooth.yRot = this.Head3.yRot;
            this.Head3LftTooth.yRot = this.Head3.yRot;
            this.Head3LftTusk.yRot  = this.Head3.yRot;
            this.Head3RingHole.yRot = this.Head3.yRot;
            this.Head3Ring.yRot     = this.Head3.yRot;

            // HEAD2 sub‐parts
            this.Head2Chin.xRot       = 0.2617994F + this.Head2.xRot;
            this.Head2Lip.xRot        = this.Head2.xRot;
            this.Head2LftTusk.xRot    = 0.1745329F + this.Head2.xRot;
            this.Head2RgtTusk.xRot    = 0.1745329F + this.Head2.xRot;
            this.Head2Nose.xRot       = 0.0872665F + this.Head2.xRot;
            this.Head2NoseBridge.xRot = -0.1745329F + this.Head2.xRot;
            this.Head2Brow.xRot       = -0.0872665F + this.Head2.xRot;
            this.Head2RgtHorn.xRot    = -0.5235988F + this.Head2.xRot;
            this.Head2LftHorn.xRot    = -0.5235988F + this.Head2.xRot;
            this.Head2DiamondHorn.xRot= 0.0872665F + this.Head2.xRot;

            this.Head2Chin.yRot       = this.Head2.yRot;
            this.Head2Lip.yRot        = this.Head2.yRot;
            this.Head2LftTusk.yRot    = this.Head2.yRot;
            this.Head2RgtTusk.yRot    = this.Head2.yRot;
            this.Head2Nose.yRot       = this.Head2.yRot;
            this.Head2NoseBridge.yRot = this.Head2.yRot;
            this.Head2Brow.yRot       = this.Head2.yRot;
            this.Head2RgtHorn.yRot    = this.Head2.yRot;
            this.Head2LftHorn.yRot    = this.Head2.yRot;
            this.Head2DiamondHorn.yRot= this.Head2.yRot;
        }
    }

    /**
     * Render all parts. Toggle visibility based on type where needed.
     */
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
        if (type == 1) {
            // Render primary head
            this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Brow.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.NoseBridge.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtTooth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftTooth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Lip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtRing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtRingHole.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftRing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftRingHole.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.HairRope.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Hair1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Hair2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Hair3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.DiamondHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RgtHornTip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LftHornTip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            // Render HEAD3 variant
            this.Head3RgtEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3LftEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Eyelid.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Brow.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Hair.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Lip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3RgtTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3RgtTooth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3LftTooth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3LftTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3RingHole.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head3Ring.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            // Render HEAD2 variant
            this.Head2Chin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2Lip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2LftTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2RgtTusk.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2NoseBridge.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2Brow.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2RgtHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2LftHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Head2DiamondHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Render body, limbs, arms
        this.NeckRest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Stomach.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ButtCover.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LoinCloth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.RgtThigh.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LftThigh.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.RgtShoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LftShoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
