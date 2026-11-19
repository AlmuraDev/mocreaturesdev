/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.neutral.MoCEntityOstrich;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

/**
 * Ported from 1.16.5 to 1.20.1. All ModelRenderer fields are now ModelPart
 * children of a single root, built via createBodyLayer(). Animations moved to
 * setupAnim(...), rendering to renderToBuffer(...), and entity data captured
 * in prepareMobModel(...).
 */
public class MoCModelOstrich<T extends MoCEntityOstrich> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "ostrich"),
            "main"
    );

    private static final float RADIAN_CONV = 57.29578F;

    // --- ModelPart fields (formerly ModelRenderer) ---
    private final ModelPart UBeak;
    private final ModelPart UBeak2;
    private final ModelPart UBeakb;
    private final ModelPart UBeak2b;
    private final ModelPart LBeak;
    private final ModelPart LBeakb;
    private final ModelPart LBeak2;
    private final ModelPart LBeak2b;
    private final ModelPart Body;
    private final ModelPart Tail;
    private final ModelPart LLegA;
    private final ModelPart LLegB;
    private final ModelPart LLegC;
    private final ModelPart LFoot;
    private final ModelPart RLegA;
    private final ModelPart RLegB;
    private final ModelPart RLegC;
    private final ModelPart RFoot;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart LWingB;
    private final ModelPart LWingC;
    private final ModelPart LWingD;
    private final ModelPart LWingE;
    private final ModelPart RWingB;
    private final ModelPart RWingC;
    private final ModelPart RWingD;
    private final ModelPart RWingE;
    private final ModelPart SaddleA;
    private final ModelPart SaddleB;
    private final ModelPart SaddleL;
    private final ModelPart SaddleR;
    private final ModelPart SaddleL2;
    private final ModelPart SaddleR2;
    private final ModelPart SaddleC;
    private final ModelPart NeckLFeather;
    private final ModelPart NeckUFeather;
    private final ModelPart NeckD;
    private final ModelPart Saddlebag;
    private final ModelPart Flagpole;
    private final ModelPart FlagBlack;
    private final ModelPart FlagDarkGrey;
    private final ModelPart FlagYellow;
    private final ModelPart FlagBrown;
    private final ModelPart FlagGreen;
    private final ModelPart FlagCyan;
    private final ModelPart FlagLightBlue;
    private final ModelPart FlagDarkBlue;
    private final ModelPart FlagPurple;
    private final ModelPart FlagDarkPurple;
    private final ModelPart FlagDarkGreen;
    private final ModelPart FlagLightRed;
    private final ModelPart FlagRed;
    private final ModelPart FlagWhite;
    private final ModelPart FlagGrey;
    private final ModelPart FlagOrange;
    private final ModelPart NeckU;
    private final ModelPart NeckL;
    private final ModelPart NeckHarness;
    private final ModelPart NeckHarness2;
    private final ModelPart NeckHarnessRight;
    private final ModelPart NeckHarnessLeft;
    private final ModelPart Head;
    private final ModelPart UniHorn;
    private final ModelPart HelmetLeather;
    private final ModelPart HelmetIron;
    private final ModelPart HelmetGold;
    private final ModelPart HelmetDiamond;
    private final ModelPart HelmetHide;
    private final ModelPart HelmetNeckHide;
    private final ModelPart HelmetHideEar1;
    private final ModelPart HelmetHideEar2;
    private final ModelPart HelmetFur;
    private final ModelPart HelmetNeckFur;
    private final ModelPart HelmetFurEar1;
    private final ModelPart HelmetFurEar2;
    private final ModelPart HelmetReptile;
    private final ModelPart HelmetReptileEar1;
    private final ModelPart HelmetReptileEar2;
    private final ModelPart HelmetGreenChitin;
    private final ModelPart HelmetYellowChitin;
    private final ModelPart HelmetBlueChitin;
    private final ModelPart HelmetBlackChitin;
    private final ModelPart HelmetRedChitin;
    private final ModelPart Tailpart1;
    private final ModelPart Tailpart2;
    private final ModelPart Tailpart3;
    private final ModelPart Tailpart4;
    private final ModelPart Tailpart5;

    // Entity state captured in prepareMobModel(...)
    private byte typeI;
    private boolean openMouth;
    private boolean isSaddled;
    private boolean bagged;
    private boolean rider;
    private int helmet;
    private int flagColor;

    public MoCModelOstrich(ModelPart root) {
        super(root);
        // Bind each ModelPart by name:
        this.UBeak           = root.getChild("UBeak");
        this.UBeak2          = root.getChild("UBeak2");
        this.UBeakb          = root.getChild("UBeakb");
        this.UBeak2b         = root.getChild("UBeak2b");
        this.LBeak           = root.getChild("LBeak");
        this.LBeakb          = root.getChild("LBeakb");
        this.LBeak2          = root.getChild("LBeak2");
        this.LBeak2b         = root.getChild("LBeak2b");
        this.Body            = root.getChild("Body");
        this.Tail            = root.getChild("Tail");
        this.LLegA           = root.getChild("LLegA");
        this.LLegB           = root.getChild("LLegB");
        this.LLegC           = root.getChild("LLegC");
        this.LFoot           = root.getChild("LFoot");
        this.RLegA           = root.getChild("RLegA");
        this.RLegB           = root.getChild("RLegB");
        this.RLegC           = root.getChild("RLegC");
        this.RFoot           = root.getChild("RFoot");
        this.Tail1           = root.getChild("Tail1");
        this.Tail2           = root.getChild("Tail2");
        this.Tail3           = root.getChild("Tail3");
        this.LWingB          = root.getChild("LWingB");
        this.LWingC          = root.getChild("LWingC");
        this.LWingD          = root.getChild("LWingD");
        this.LWingE          = root.getChild("LWingE");
        this.RWingB          = root.getChild("RWingB");
        this.RWingC          = root.getChild("RWingC");
        this.RWingD          = root.getChild("RWingD");
        this.RWingE          = root.getChild("RWingE");
        this.SaddleA         = root.getChild("SaddleA");
        this.SaddleB         = root.getChild("SaddleB");
        this.SaddleL         = root.getChild("SaddleL");
        this.SaddleR         = root.getChild("SaddleR");
        this.SaddleL2        = root.getChild("SaddleL2");
        this.SaddleR2        = root.getChild("SaddleR2");
        this.SaddleC         = root.getChild("SaddleC");
        this.NeckLFeather    = root.getChild("NeckLFeather");
        this.NeckUFeather    = root.getChild("NeckUFeather");
        this.NeckD           = root.getChild("NeckD");
        this.Saddlebag       = root.getChild("Saddlebag");
        this.Flagpole        = root.getChild("Flagpole");
        this.FlagBlack       = root.getChild("FlagBlack");
        this.FlagDarkGrey    = root.getChild("FlagDarkGrey");
        this.FlagYellow      = root.getChild("FlagYellow");
        this.FlagBrown       = root.getChild("FlagBrown");
        this.FlagGreen       = root.getChild("FlagGreen");
        this.FlagCyan        = root.getChild("FlagCyan");
        this.FlagLightBlue   = root.getChild("FlagLightBlue");
        this.FlagDarkBlue    = root.getChild("FlagDarkBlue");
        this.FlagPurple      = root.getChild("FlagPurple");
        this.FlagDarkPurple  = root.getChild("FlagDarkPurple");
        this.FlagDarkGreen   = root.getChild("FlagDarkGreen");
        this.FlagLightRed    = root.getChild("FlagLightRed");
        this.FlagRed         = root.getChild("FlagRed");
        this.FlagWhite       = root.getChild("FlagWhite");
        this.FlagGrey        = root.getChild("FlagGrey");
        this.FlagOrange      = root.getChild("FlagOrange");
        this.NeckU           = root.getChild("NeckU");
        this.NeckL           = root.getChild("NeckL");
        this.NeckHarness     = root.getChild("NeckHarness");
        this.NeckHarness2    = root.getChild("NeckHarness2");
        this.NeckHarnessRight= root.getChild("NeckHarnessRight");
        this.NeckHarnessLeft = root.getChild("NeckHarnessLeft");
        this.Head            = root.getChild("Head");
        this.UniHorn         = root.getChild("UniHorn");
        this.HelmetLeather   = root.getChild("HelmetLeather");
        this.HelmetIron      = root.getChild("HelmetIron");
        this.HelmetGold      = root.getChild("HelmetGold");
        this.HelmetDiamond   = root.getChild("HelmetDiamond");
        this.HelmetHide      = root.getChild("HelmetHide");
        this.HelmetNeckHide  = root.getChild("HelmetNeckHide");
        this.HelmetHideEar1  = root.getChild("HelmetHideEar1");
        this.HelmetHideEar2  = root.getChild("HelmetHideEar2");
        this.HelmetFur       = root.getChild("HelmetFur");
        this.HelmetNeckFur   = root.getChild("HelmetNeckFur");
        this.HelmetFurEar1   = root.getChild("HelmetFurEar1");
        this.HelmetFurEar2   = root.getChild("HelmetFurEar2");
        this.HelmetReptile   = root.getChild("HelmetReptile");
        this.HelmetReptileEar1 = root.getChild("HelmetReptileEar1");
        this.HelmetReptileEar2 = root.getChild("HelmetReptileEar2");
        this.HelmetGreenChitin = root.getChild("HelmetGreenChitin");
        this.HelmetYellowChitin= root.getChild("HelmetYellowChitin");
        this.HelmetBlueChitin  = root.getChild("HelmetBlueChitin");
        this.HelmetBlackChitin = root.getChild("HelmetBlackChitin");
        this.HelmetRedChitin   = root.getChild("HelmetRedChitin");
        this.Tailpart1        = root.getChild("Tailpart1");
        this.Tailpart2        = root.getChild("Tailpart2");
        this.Tailpart3        = root.getChild("Tailpart3");
        this.Tailpart4        = root.getChild("Tailpart4");
        this.Tailpart5        = root.getChild("Tailpart5");
    }

    /**
     * Build the LayerDefinition: all ModelPart children with correct positions,
     * rotations, and texture offsets. Corresponds line-by-line to the old
     * ModelRenderer.addBox(...) and setRotation(...) calls.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // UBeak
        root.addOrReplaceChild("UBeak",
                CubeListBuilder.create()
                        .texOffs(12, 16)
                        .addBox(-1.5F, -15.0F, -5.5F, 3, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // UBeak2
        root.addOrReplaceChild("UBeak2",
                CubeListBuilder.create()
                        .texOffs(20, 16)
                        .addBox(-1.0F, -15.0F, -7.5F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // UBeakb (lower rotation angle)
        root.addOrReplaceChild("UBeakb",
                CubeListBuilder.create()
                        .texOffs(12, 16)
                        .addBox(-1.5F, -15.0F, -6.5F, 3, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, -0.0698132F, 0.0F, 0.0F)
        );

        // UBeak2b
        root.addOrReplaceChild("UBeak2b",
                CubeListBuilder.create()
                        .texOffs(20, 16)
                        .addBox(-1.0F, -15.0F, -8.5F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, -0.0698132F, 0.0F, 0.0F)
        );

        // LBeak
        root.addOrReplaceChild("LBeak",
                CubeListBuilder.create()
                        .texOffs(12, 22)
                        .addBox(-1.5F, -14.0F, -5.5F, 3, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // LBeakb
        root.addOrReplaceChild("LBeakb",
                CubeListBuilder.create()
                        .texOffs(12, 22)
                        .addBox(-1.5F, -14.0F, -3.9F, 3, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.122173F, 0.0F, 0.0F)
        );

        // LBeak2
        root.addOrReplaceChild("LBeak2",
                CubeListBuilder.create()
                        .texOffs(20, 22)
                        .addBox(-1.0F, -14.0F, -7.5F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // LBeak2b
        root.addOrReplaceChild("LBeak2b",
                CubeListBuilder.create()
                        .texOffs(20, 22)
                        .addBox(-1.0F, -14.0F, -5.9F, 2, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.122173F, 0.0F, 0.0F)
        );

        // Body
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(0, 38)
                        .addBox(-4.0F, 1.0F, 0.0F, 8, 10, 16, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, -6.0F)
        );

        // Left Leg A
        root.addOrReplaceChild("LLegA",
                CubeListBuilder.create()
                        .texOffs(50, 28)
                        .addBox(-2.0F, -1.0F, -2.5F, 4, 6, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Left Leg B
        root.addOrReplaceChild("LLegB",
                CubeListBuilder.create()
                        .texOffs(50, 39)
                        .addBox(-1.5F, 5.0F, -1.5F, 3, 4, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Left Leg C
        root.addOrReplaceChild("LLegC",
                CubeListBuilder.create()
                        .texOffs(8, 38)
                        .addBox(-1.0F, 8.0F, 2.5F, 2, 10, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 5.0F, 4.0F, -0.2617994F, 0.0F, 0.0F)
        );

        // Left Foot
        root.addOrReplaceChild("LFoot",
                CubeListBuilder.create()
                        .texOffs(32, 42)
                        .addBox(-1.0F, 17.0F, -9.0F, 2, 1, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Right Leg A
        root.addOrReplaceChild("RLegA",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(-2.0F, -1.0F, -2.5F, 4, 6, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Right Leg B
        root.addOrReplaceChild("RLegB",
                CubeListBuilder.create()
                        .texOffs(18, 27)
                        .addBox(-1.5F, 5.0F, -1.5F, 3, 4, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Right Leg C
        root.addOrReplaceChild("RLegC",
                CubeListBuilder.create()
                        .texOffs(0, 38)
                        .addBox(-1.0F, 8.0F, 2.5F, 2, 10, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 5.0F, 4.0F, -0.2617994F, 0.0F, 0.0F)
        );

        // Right Foot
        root.addOrReplaceChild("RFoot",
                CubeListBuilder.create()
                        .texOffs(32, 48)
                        .addBox(-1.0F, 17.0F, -9.0F, 2, 1, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 5.0F, 4.0F, 0.1745329F, 0.0F, 0.0F)
        );

        // Tail1
        root.addOrReplaceChild("Tail1",
                CubeListBuilder.create()
                        .texOffs(44, 18)
                        .addBox(-0.5F, -2.0F, -2.0F, 1, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 15.0F, 0.3490659F, 0.0F, 0.0F)
        );

        // Tail2
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create()
                        .texOffs(58, 18)
                        .addBox(-2.6F, -2.0F, -2.0F, 1, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 15.0F, 0.3490659F, -0.2617994F, 0.0F)
        );

        // Tail3
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create()
                        .texOffs(30, 18)
                        .addBox(1.6F, -2.0F, -2.0F, 1, 4, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 15.0F, 0.3490659F, 0.2617994F, 0.0F)
        );

        // Left Wing B
        root.addOrReplaceChild("LWingB",
                CubeListBuilder.create()
                        .texOffs(68, 46)
                        .addBox(-0.5F, -3.0F, 0.0F, 1, 4, 14, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 4.0F, -3.0F, 0.0872665F, 0.0872665F, 0.0F)
        );

        // Left Wing C
        root.addOrReplaceChild("LWingC",
                CubeListBuilder.create()
                        .texOffs(98, 46)
                        .addBox(-1.0F, 0.0F, 0.0F, 1, 4, 14, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 4.0F, -3.0F, 0.0F, 0.0872665F, 0.0F)
        );

        // Left Wing D
        root.addOrReplaceChild("LWingD",
                CubeListBuilder.create()
                        .texOffs(26, 84)
                        .addBox(0.0F, -1.0F, -1.0F, 15, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 3.0F, -3.0F, 0.0F, 0.0F, -0.3490659F)
        );

        // Left Wing E
        root.addOrReplaceChild("LWingE",
                CubeListBuilder.create()
                        .texOffs(0, 103)
                        .addBox(0.0F, 0.0F, 1.0F, 15, 0, 15, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 3.0F, -3.0F, 0.0F, 0.0F, -0.3490659F)
        );

        // Right Wing B
        root.addOrReplaceChild("RWingB",
                CubeListBuilder.create()
                        .texOffs(68, 0)
                        .addBox(-0.5F, -3.0F, 0.0F, 1, 4, 14, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 4.0F, -3.0F, 0.0872665F, -0.0872665F, 0.0F)
        );

        // Right Wing C
        root.addOrReplaceChild("RWingC",
                CubeListBuilder.create()
                        .texOffs(98, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 4, 14, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 4.0F, -3.0F, 0.0F, -0.0872665F, 0.0F)
        );

        // Right Wing D
        root.addOrReplaceChild("RWingD",
                CubeListBuilder.create()
                        .texOffs(26, 80)
                        .addBox(-15.0F, -1.0F, -1.0F, 15, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -3.0F, 0.0F, 0.0F, 0.3490659F)
        );

        // Right Wing E
        root.addOrReplaceChild("RWingE",
                CubeListBuilder.create()
                        .texOffs(0, 88)
                        .addBox(-15.0F, 0.0F, 1.0F, 15, 0, 15, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 3.0F, -3.0F, 0.0F, 0.0F, 0.3490659F)
        );

        // Saddle A
        root.addOrReplaceChild("SaddleA",
                CubeListBuilder.create()
                        .texOffs(72, 18)
                        .addBox(-4.0F, 0.5F, -3.0F, 8, 1, 8, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        // Saddle B
        root.addOrReplaceChild("SaddleB",
                CubeListBuilder.create()
                        .texOffs(72, 27)
                        .addBox(-1.5F, 0.0F, -3.0F, 3, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        // Saddle L
        root.addOrReplaceChild("SaddleL",
                CubeListBuilder.create()
                        .texOffs(72, 30)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, 1.0F, 0.0F)
        );

        // Saddle R
        root.addOrReplaceChild("SaddleR",
                CubeListBuilder.create()
                        .texOffs(84, 30)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, 1.0F, 0.0F)
        );

        // Saddle L2
        root.addOrReplaceChild("SaddleL2",
                CubeListBuilder.create()
                        .texOffs(76, 30)
                        .addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, 1.0F, 0.0F)
        );

        // Saddle R2
        root.addOrReplaceChild("SaddleR2",
                CubeListBuilder.create()
                        .texOffs(88, 30)
                        .addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, 1.0F, 0.0F)
        );

        // Saddle C
        root.addOrReplaceChild("SaddleC",
                CubeListBuilder.create()
                        .texOffs(84, 27)
                        .addBox(-4.0F, 0.0F, 3.0F, 8, 1, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        // Neck Left Feather
        root.addOrReplaceChild("NeckLFeather",
                CubeListBuilder.create()
                        .texOffs(8, 73)
                        .addBox(0.0F, -8.0F, -0.5F, 0, 7, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.2007129F, 0.0F, 0.0F)
        );

        // Neck Upper Feather
        root.addOrReplaceChild("NeckUFeather",
                CubeListBuilder.create()
                        .texOffs(0, 73)
                        .addBox(0.0F, -16.0F, -2.0F, 0, 9, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // Neck D
        root.addOrReplaceChild("NeckD",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.5F, -4.0F, -2.0F, 3, 8, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.4363323F, 0.0F, 0.0F)
        );

        // Saddlebag
        root.addOrReplaceChild("Saddlebag",
                CubeListBuilder.create()
                        .texOffs(32, 7)
                        .addBox(-4.5F, -3.0F, 5.0F, 9, 4, 7, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2602503F, 0.0F, 0.0F)
        );

        // Flagpole
        root.addOrReplaceChild("Flagpole",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-0.5F, -15.0F, -0.5F, 1, 17, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, -0.2602503F, 0.0F, 0.0F)
        );

        // Flag variants: Black
        root.addOrReplaceChild("FlagBlack",
                CubeListBuilder.create()
                        .texOffs(108, 8)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Dark Grey
        root.addOrReplaceChild("FlagDarkGrey",
                CubeListBuilder.create()
                        .texOffs(108, 12)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Yellow
        root.addOrReplaceChild("FlagYellow",
                CubeListBuilder.create()
                        .texOffs(48, 46)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Brown
        root.addOrReplaceChild("FlagBrown",
                CubeListBuilder.create()
                        .texOffs(48, 42)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Green
        root.addOrReplaceChild("FlagGreen",
                CubeListBuilder.create()
                        .texOffs(48, 38)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Cyan
        root.addOrReplaceChild("FlagCyan",
                CubeListBuilder.create()
                        .texOffs(48, 50)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Light Blue
        root.addOrReplaceChild("FlagLightBlue",
                CubeListBuilder.create()
                        .texOffs(68, 32)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Dark Blue
        root.addOrReplaceChild("FlagDarkBlue",
                CubeListBuilder.create()
                        .texOffs(68, 28)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Purple
        root.addOrReplaceChild("FlagPurple",
                CubeListBuilder.create()
                        .texOffs(88, 32)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Dark Purple
        root.addOrReplaceChild("FlagDarkPurple",
                CubeListBuilder.create()
                        .texOffs(88, 28)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Dark Green
        root.addOrReplaceChild("FlagDarkGreen",
                CubeListBuilder.create()
                        .texOffs(108, 32)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Light Red
        root.addOrReplaceChild("FlagLightRed",
                CubeListBuilder.create()
                        .texOffs(108, 28)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Red
        root.addOrReplaceChild("FlagRed",
                CubeListBuilder.create()
                        .texOffs(108, 24)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // White
        root.addOrReplaceChild("FlagWhite",
                CubeListBuilder.create()
                        .texOffs(108, 20)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Grey
        root.addOrReplaceChild("FlagGrey",
                CubeListBuilder.create()
                        .texOffs(108, 16)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );
        // Orange
        root.addOrReplaceChild("FlagOrange",
                CubeListBuilder.create()
                        .texOffs(88, 24)
                        .addBox(0.0F, -2.1F, 0.0F, 0, 4, 10, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.2602503F, 0.0F, 0.0F)
        );

        // NeckU
        root.addOrReplaceChild("NeckU",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(-1.0F, -12.0F, -4.0F, 2, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // NeckL
        root.addOrReplaceChild("NeckL",
                CubeListBuilder.create()
                        .texOffs(20, 7)
                        .addBox(-1.0F, -8.0F, -2.5F, 2, 5, 2, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.2007129F, 0.0F, 0.0F)
        );

        // Neck Harness top strap
        root.addOrReplaceChild("NeckHarness",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-2.0F, -3.0F, -2.5F, 4, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.4363323F, 0.0F, 0.0F)
        );

        // Neck Harness horizontal bar
        root.addOrReplaceChild("NeckHarness2",
                CubeListBuilder.create()
                        .texOffs(84, 55)
                        .addBox(-3.0F, -2.5F, -2.0F, 6, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // Neck Harness Right Strap
        root.addOrReplaceChild("NeckHarnessRight",
                CubeListBuilder.create()
                        .texOffs(84, 45)
                        .addBox(-2.3F, -3.5F, -0.5F, 0, 3, 12, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.8983798F, 0.0F, 0.0F)
        );

        // Neck Harness Left Strap
        root.addOrReplaceChild("NeckHarnessLeft",
                CubeListBuilder.create()
                        .texOffs(84, 45)
                        .addBox(2.3F, -3.5F, -0.5F, 0, 3, 12, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.8983798F, 0.0F, 0.0F)
        );

        // Head
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -16.0F, -4.5F, 3, 4, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // UniHorn
        root.addOrReplaceChild("UniHorn",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-0.5F, -21.0F, 0.5F, 1, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.3171542F, 0.0F, 0.0F)
        );

        // Helmets
        root.addOrReplaceChild("HelmetLeather",
                CubeListBuilder.create()
                        .texOffs(66, 0)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetIron",
                CubeListBuilder.create()
                        .texOffs(84, 46)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetGold",
                CubeListBuilder.create()
                        .texOffs(112, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetDiamond",
                CubeListBuilder.create()
                        .texOffs(96, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetHide",
                CubeListBuilder.create()
                        .texOffs(96, 5)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetNeckHide",
                CubeListBuilder.create()
                        .texOffs(58, 0)
                        .addBox(-1.5F, -12.0F, -4.5F, 3, 1, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetHideEar1",
                CubeListBuilder.create()
                        .texOffs(84, 9)
                        .addBox(-2.5F, -18.0F, -3.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetHideEar2",
                CubeListBuilder.create()
                        .texOffs(90, 9)
                        .addBox(0.5F, -18.0F, -3.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetFur",
                CubeListBuilder.create()
                        .texOffs(84, 0)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetNeckFur",
                CubeListBuilder.create()
                        .texOffs(96, 0)
                        .addBox(-1.5F, -12.0F, -4.5F, 3, 1, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetFurEar1",
                CubeListBuilder.create()
                        .texOffs(66, 9)
                        .addBox(-2.5F, -18.0F, -3.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetFurEar2",
                CubeListBuilder.create()
                        .texOffs(76, 9)
                        .addBox(0.5F, -17.0F, -3.0F, 2, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetReptile",
                CubeListBuilder.create()
                        .texOffs(64, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetReptileEar2",
                CubeListBuilder.create()
                        .texOffs(114, 45)
                        .addBox(2.5F, -16.5F, -2.0F, 0, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.0F, 0.6108652F, 0.0F)
        );
        root.addOrReplaceChild("HelmetReptileEar1",
                CubeListBuilder.create()
                        .texOffs(114, 50)
                        .addBox(-2.5F, -16.5F, -2.0F, 0, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, 0.0F, -0.6108652F, 0.0F)
        );
        root.addOrReplaceChild("HelmetGreenChitin",
                CubeListBuilder.create()
                        .texOffs(80, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetYellowChitin",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetBlueChitin",
                CubeListBuilder.create()
                        .texOffs(16, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetBlackChitin",
                CubeListBuilder.create()
                        .texOffs(32, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );
        root.addOrReplaceChild("HelmetRedChitin",
                CubeListBuilder.create()
                        .texOffs(48, 64)
                        .addBox(-2.0F, -16.5F, -5.0F, 4, 5, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 3.0F, -6.0F)
        );

        // Tail (main)
        root.addOrReplaceChild("Tail",
                CubeListBuilder.create()
                        .texOffs(30, 28)
                        .addBox(-2.5F, -1.0F, 0.0F, 5, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 10.0F)
        );

        // Tailpart1
        root.addOrReplaceChild("Tailpart1",
                CubeListBuilder.create()
                        .texOffs(30, 28)
                        .addBox(-2.5F, -2.2F, 5.0F, 5, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 10.0F, -0.2974289F, 0.0F, 0.0F)
        );

        // Tailpart2
        root.addOrReplaceChild("Tailpart2",
                CubeListBuilder.create()
                        .texOffs(60, 73)
                        .addBox(-2.5F, -4.3F, 9.0F, 5, 5, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 10.0F, -0.5205006F, 0.0F, 0.0F)
        );

        // Tailpart3
        root.addOrReplaceChild("Tailpart3",
                CubeListBuilder.create()
                        .texOffs(60, 86)
                        .addBox(-2.0F, 1.0F, 16.0F, 4, 4, 7, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 10.0F, -0.2230717F, 0.0F, 0.0F)
        );

        // Tailpart4
        root.addOrReplaceChild("Tailpart4",
                CubeListBuilder.create()
                        .texOffs(60, 97)
                        .addBox(-1.5F, 8.0F, 20.6F, 3, 3, 7, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 10.0F, 0.0743572F, 0.0F, 0.0F)
        );

        // Tailpart5
        root.addOrReplaceChild("Tailpart5",
                CubeListBuilder.create()
                        .texOffs(60, 107)
                        .addBox(-1.0F, 16.5F, 22.9F, 2, 2, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, 10.0F, 0.4089647F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 128, 128);
    }

    /**
     * Capture entity state for animation use.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.typeI       = (byte) entity.getTypeMoC();
        this.openMouth   = entity.mouthCounter != 0;
        this.isSaddled   = entity.getIsRideable();
        this.bagged      = entity.getIsChested();
        this.rider       = entity.isPassenger();
        this.helmet      = entity.getHelmet();
        this.flagColor   = entity.getFlagColorRaw();
    }

    /**
     * Copy original setRotationAngles(...) logic here in setupAnim(...).
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
        boolean isHiding    = entity.getHiding();
        boolean wingFlap    = entity.wingCounter != 0;
        int jumpCounter     = entity.jumpCounter;
        boolean floating    = entity.isFlyer() && entity.isOnAir();

        float LLegXRot = Mth.cos(limbSwing * 0.4F) * 1.1F * limbSwingAmount;
        float RLegXRot = Mth.cos((limbSwing * 0.4F) + (float)Math.PI) * 1.1F * limbSwingAmount;

        // HEAD ROTATION
        if (isHiding) {
            this.Head.y = 9.0F;
            this.Head.xRot = 2.61799F;
            this.Head.yRot = 0.0F;
        } else {
            this.Head.y = 3.0F;
            this.Head.xRot = (RLegXRot / 20F) + (-headPitch / (180F / (float)Math.PI));
            this.Head.yRot = netHeadYaw / (180F / (float)Math.PI);
        }
        if (rider) {
            if (floating) {
                this.Head.xRot = 0.0F;
            } else {
                this.Head.xRot = (RLegXRot / 20F);
            }
        }

        // Sync beak and neck parts to head's position/rotation
        this.UBeak.y = this.Head.y;
        this.UBeakb.y = this.Head.y;
        this.UBeak2.y = this.Head.y;
        this.UBeak2b.y = this.Head.y;
        this.LBeak.y = this.Head.y;
        this.LBeakb.y = this.Head.y;
        this.LBeak2.y = this.Head.y;
        this.LBeak2b.y = this.Head.y;
        this.NeckU.y = this.Head.y;
        this.NeckD.y = this.Head.y;
        this.NeckL.y = this.Head.y;
        this.UniHorn.y = this.Head.y;

        // HEAD rotation sync
        this.UBeak.xRot    = this.Head.xRot;
        this.UBeak.yRot    = this.Head.yRot;
        this.UBeak2.xRot   = this.Head.xRot;
        this.UBeak2.yRot   = this.Head.yRot;
        this.LBeak.xRot    = this.Head.xRot;
        this.LBeak.yRot    = this.Head.yRot;
        this.LBeak2.xRot   = this.Head.xRot;
        this.LBeak2.yRot   = this.Head.yRot;
        this.NeckU.xRot    = this.Head.xRot;
        this.NeckU.yRot    = this.Head.yRot;
        this.NeckD.xRot    = 0.4363323F + this.Head.xRot;
        this.NeckD.yRot    = this.Head.yRot;
        this.NeckL.xRot    = (11.5F / RADIAN_CONV) + this.Head.xRot;
        this.NeckL.yRot    = this.Head.yRot;

        // OPEN vs CLOSED beak rotation offsets
        this.UBeakb.xRot   = -0.0698132F + this.Head.xRot;
        this.UBeakb.yRot   = this.Head.yRot;
        this.UBeak2b.xRot  = -0.0698132F + this.Head.xRot;
        this.UBeak2b.yRot  = this.Head.yRot;
        this.LBeakb.xRot   = (7F / RADIAN_CONV) + this.Head.xRot;
        this.LBeakb.yRot   = this.Head.yRot;
        this.LBeak2b.xRot  = (7F / RADIAN_CONV) + this.Head.xRot;
        this.LBeak2b.yRot  = this.Head.yRot;

        // Neck feathers and horn sync
        this.NeckUFeather.y   = this.Head.y;
        this.NeckLFeather.y   = this.Head.y;
        this.UniHorn.y        = this.Head.y;
        this.NeckUFeather.xRot= this.Head.xRot;
        this.NeckUFeather.yRot= this.Head.yRot;
        this.NeckLFeather.xRot= (11.5F / RADIAN_CONV) + this.Head.xRot;
        this.NeckLFeather.yRot= this.Head.yRot;
        this.UniHorn.xRot     = (18F / RADIAN_CONV) + this.Head.xRot;
        this.UniHorn.yRot     = this.Head.yRot;

        // Neck harness sync
        this.NeckHarness.y       = this.Head.y;
        this.NeckHarness2.y      = this.Head.y;
        this.NeckHarnessLeft.y   = this.Head.y;
        this.NeckHarnessRight.y  = this.Head.y;
        this.NeckHarness.xRot    = (25F / RADIAN_CONV) + this.Head.xRot;
        this.NeckHarness.yRot    = this.Head.yRot;
        this.NeckHarness2.xRot   = this.Head.xRot;
        this.NeckHarness2.yRot   = this.Head.yRot;
        this.NeckHarnessLeft.xRot= (50F / RADIAN_CONV) + this.Head.xRot;
        this.NeckHarnessLeft.yRot= this.Head.yRot;
        this.NeckHarnessRight.xRot= (50F / RADIAN_CONV) + this.Head.xRot;
        this.NeckHarnessRight.yRot= this.Head.yRot;

        // Helmets sync to head
        switch (this.helmet) {
            case 1 -> {
                this.HelmetLeather.y    = this.Head.y;
                this.HelmetLeather.xRot = this.Head.xRot;
                this.HelmetLeather.yRot = this.Head.yRot;
            }
            case 2 -> {
                this.HelmetIron.y       = this.Head.y;
                this.HelmetIron.xRot    = this.Head.xRot;
                this.HelmetIron.yRot    = this.Head.yRot;
            }
            case 3 -> {
                this.HelmetGold.y       = this.Head.y;
                this.HelmetGold.xRot    = this.Head.xRot;
                this.HelmetGold.yRot    = this.Head.yRot;
            }
            case 4 -> {
                this.HelmetDiamond.y    = this.Head.y;
                this.HelmetDiamond.xRot = this.Head.xRot;
                this.HelmetDiamond.yRot = this.Head.yRot;
            }
            case 5 -> {
                this.HelmetHide.y      = this.Head.y;
                this.HelmetHide.xRot   = this.Head.xRot;
                this.HelmetHide.yRot   = this.Head.yRot;
                this.HelmetNeckHide.y  = this.Head.y;
                this.HelmetNeckHide.xRot = this.Head.xRot;
                this.HelmetNeckHide.yRot = this.Head.yRot;
                this.HelmetHideEar1.y  = this.Head.y;
                this.HelmetHideEar1.xRot = this.Head.xRot;
                this.HelmetHideEar1.yRot = this.Head.yRot;
                this.HelmetHideEar2.y  = this.Head.y;
                this.HelmetHideEar2.xRot = this.Head.xRot;
                this.HelmetHideEar2.yRot = this.Head.yRot;
            }
            case 6 -> {
                this.HelmetFur.y        = this.Head.y;
                this.HelmetFur.xRot     = this.Head.xRot;
                this.HelmetFur.yRot     = this.Head.yRot;
                this.HelmetNeckFur.y    = this.Head.y;
                this.HelmetNeckFur.xRot = this.Head.xRot;
                this.HelmetNeckFur.yRot = this.Head.yRot;
                this.HelmetFurEar1.y    = this.Head.y;
                this.HelmetFurEar1.xRot = this.Head.xRot;
                this.HelmetFurEar1.yRot = this.Head.yRot;
                this.HelmetFurEar2.y    = this.Head.y;
                this.HelmetFurEar2.xRot = this.Head.xRot;
                this.HelmetFurEar2.yRot = this.Head.yRot;
            }
            case 7 -> {
                this.HelmetReptile.y      = this.Head.y;
                this.HelmetReptile.xRot   = this.Head.xRot;
                this.HelmetReptile.yRot   = this.Head.yRot;
                this.HelmetReptileEar1.y  = this.Head.y;
                this.HelmetReptileEar1.xRot = this.Head.xRot;
                this.HelmetReptileEar1.yRot = (-35F / RADIAN_CONV) + this.Head.yRot;
                this.HelmetReptileEar2.y  = this.Head.y;
                this.HelmetReptileEar2.xRot = this.Head.xRot;
                this.HelmetReptileEar2.yRot = (35F / RADIAN_CONV) + this.Head.yRot;
            }
            case 8 -> {
                this.HelmetGreenChitin.y  = this.Head.y;
                this.HelmetGreenChitin.xRot= this.Head.xRot;
                this.HelmetGreenChitin.yRot= this.Head.yRot;
            }
            case 9 -> {
                this.HelmetYellowChitin.y   = this.Head.y;
                this.HelmetYellowChitin.xRot= this.Head.xRot;
                this.HelmetYellowChitin.yRot= this.Head.yRot;
            }
            case 10 -> {
                this.HelmetBlueChitin.y    = this.Head.y;
                this.HelmetBlueChitin.xRot = this.Head.xRot;
                this.HelmetBlueChitin.yRot = this.Head.yRot;
            }
            case 11 -> {
                this.HelmetBlackChitin.y    = this.Head.y;
                this.HelmetBlackChitin.xRot = this.Head.xRot;
                this.HelmetBlackChitin.yRot = this.Head.yRot;
            }
            case 12 -> {
                this.HelmetRedChitin.y      = this.Head.y;
                this.HelmetRedChitin.xRot   = this.Head.xRot;
                this.HelmetRedChitin.yRot   = this.Head.yRot;
            }
        }

        // FLAG sway
        float flagF = Mth.cos(limbSwing * 0.8F) * 0.1F * limbSwingAmount;
        switch (this.flagColor) {
            case 0 -> this.FlagWhite.yRot     = flagF;
            case 1 -> this.FlagOrange.yRot    = flagF;
            case 2 -> this.FlagPurple.yRot    = flagF;
            case 3 -> this.FlagLightBlue.yRot = flagF;
            case 4 -> this.FlagYellow.yRot    = flagF;
            case 5 -> this.FlagGreen.yRot     = flagF;
            case 6 -> this.FlagLightRed.yRot  = flagF;
            case 7 -> this.FlagDarkGrey.yRot  = flagF;
            case 8 -> this.FlagGrey.yRot      = flagF;
            case 9 -> this.FlagCyan.yRot      = flagF;
            case 10-> this.FlagDarkPurple.yRot= flagF;
            case 11-> this.FlagDarkBlue.yRot  = flagF;
            case 12-> this.FlagBrown.yRot     = flagF;
            case 13-> this.FlagDarkGreen.yRot = flagF;
            case 14-> this.FlagRed.yRot       = flagF;
            case 15-> this.FlagBlack.yRot     = flagF;
        }

        // LEG animation
        if ((this.typeI == 5 || this.typeI == 6) && floating) {
            // Flying posture
            this.LLegC.y = 8.0F;
            this.LLegC.z = 17.0F;
            this.RLegC.y = 8.0F;
            this.RLegC.z = 17.0F;
            this.LFoot.y = -5.0F;
            this.LFoot.z = -3.0F;
            this.RFoot.y = -5.0F;
            this.RFoot.z = -3.0F;

            this.LLegA.xRot = 40.0F / RADIAN_CONV;
            this.LLegB.xRot = this.LLegA.xRot;
            this.LLegC.xRot = -85.0F / RADIAN_CONV;
            this.LFoot.xRot = 25.0F / RADIAN_CONV;

            this.RLegA.xRot = 40.0F / RADIAN_CONV;
            this.RLegB.xRot = this.RLegA.xRot;
            this.RLegC.xRot = -85.0F / RADIAN_CONV;
            this.RFoot.xRot = 25.0F / RADIAN_CONV;
        } else {
            // Walking posture
            this.LLegC.y = 5.0F;
            this.LLegC.z = 4.0F;
            this.RLegC.y = 5.0F;
            this.RLegC.z = 4.0F;
            this.LFoot.y = 5.0F;
            this.LFoot.z = 4.0F;
            this.RFoot.y = 5.0F;
            this.RFoot.z = 4.0F;

            this.LLegA.xRot = 0.1745329F + LLegXRot;
            this.LLegB.xRot = this.LLegA.xRot;
            this.LLegC.xRot = -0.2617994F + LLegXRot;
            this.LFoot.xRot = this.LLegA.xRot;

            this.RLegA.xRot = 0.1745329F + RLegXRot;
            this.RLegB.xRot = this.RLegA.xRot;
            this.RLegC.xRot = -0.2617994F + RLegXRot;
            this.RFoot.xRot = this.RLegA.xRot;
        }

        // WING animation
        float wingF;
        if (this.typeI == 5 || this.typeI == 6) {
            if (jumpCounter != 0) {
                wingF = (-40.0F / RADIAN_CONV) + Mth.cos(jumpCounter * 0.3F) * 1.3F;
            } else if (rider && floating) {
                wingF = Mth.cos(ageInTicks * 0.8F) * 0.2F;
            } else {
                wingF = Mth.cos(limbSwing * 0.3F) * limbSwingAmount;
            }
            this.LWingD.zRot = (-20.0F / RADIAN_CONV) - wingF;
            this.LWingE.zRot = (-20.0F / RADIAN_CONV) - wingF;
            this.RWingD.zRot = (20.0F / RADIAN_CONV) + wingF;
            this.RWingE.zRot = (20.0F / RADIAN_CONV) + wingF;
        } else {
            wingF = (10.0F / RADIAN_CONV) + Mth.cos(limbSwing * 0.6F) * 0.2F * limbSwingAmount;
            if (wingFlap) {
                wingF += (50.0F / RADIAN_CONV);
            }
            this.LWingB.yRot = 0.0872665F + wingF;
            this.LWingC.yRot = 0.0872665F + wingF;
            this.RWingB.yRot = -0.0872665F - wingF;
            this.RWingC.yRot = -0.0872665F - wingF;

            this.LWingB.xRot = 0.0872665F + (RLegXRot / 10F);
            this.LWingC.xRot = (RLegXRot / 10F);
            this.RWingB.xRot = 0.0872665F + (RLegXRot / 10F);
            this.RWingC.xRot = (RLegXRot / 10F);
        }

        // SADDLE adjustment
        if (rider) {
            this.SaddleL.xRot = -60.0F / RADIAN_CONV;
            this.SaddleL2.xRot= this.SaddleL.xRot;
            this.SaddleR.xRot = -60.0F / RADIAN_CONV;
            this.SaddleR2.xRot= this.SaddleR.xRot;
            this.SaddleL.zRot = -40.0F / RADIAN_CONV;
            this.SaddleL2.zRot= this.SaddleL.zRot;
            this.SaddleR.zRot = 40.0F / RADIAN_CONV;
            this.SaddleR2.zRot= this.SaddleR.zRot;
        } else {
            this.SaddleL.xRot = RLegXRot / 3F;
            this.SaddleL2.xRot= RLegXRot / 3F;
            this.SaddleR.xRot = RLegXRot / 3F;
            this.SaddleR2.xRot= RLegXRot / 3F;
            this.SaddleL.zRot = RLegXRot / 5F;
            this.SaddleL2.zRot= RLegXRot / 5F;
            this.SaddleR.zRot = -RLegXRot / 5F;
            this.SaddleR2.zRot= -RLegXRot / 5F;
        }

        // DARKNESS OSTRICH tail wave (typeI == 6)
        if (this.typeI == 6) {
            float f6 = 15.0F;
            float rotF = Mth.cos(limbSwing * 0.5F) * 0.3F * limbSwingAmount;
            this.Tail.yRot = rotF;
            rotF += (rotF / f6);
            this.Tailpart1.yRot = rotF;
            rotF += (rotF / f6);
            this.Tailpart2.yRot = rotF;
            rotF += (rotF / f6);
            this.Tailpart3.yRot = rotF;
            rotF += (rotF / f6);
            this.Tailpart4.yRot = rotF;
            rotF += (rotF / f6);
            this.Tailpart5.yRot = rotF;
        }
    }

    /**
     * Render all parts. Toggle visibility based on type, openMouth, saddled, bagged, helmet.
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
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.NeckU.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.NeckD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.NeckL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // UniHorn for type 8
        if (this.typeI == 8) {
            this.UniHorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Wings
        if (this.typeI == 5 || this.typeI == 6) {
            this.LWingD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LWingE.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RWingD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RWingE.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.NeckUFeather.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.NeckLFeather.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.LWingB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LWingC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RWingB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.RWingC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Tail segments for darkness ostrich
        if (this.typeI == 6) {
            this.Tailpart1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tailpart2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tailpart3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tailpart4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tailpart5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.Tail1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tail2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Tail3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Beak open/closed
        if (openMouth) {
            this.UBeakb.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.UBeak2b.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LBeakb.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LBeak2b.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.UBeak.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.UBeak2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LBeak.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.LBeak2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Saddle elements
        if (isSaddled) {
            this.SaddleA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleL2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.SaddleR2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.NeckHarness.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.NeckHarness2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            if (rider) {
                this.NeckHarnessLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.NeckHarnessRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // Saddlebag and flag
        if (bagged) {
            this.Saddlebag.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.Flagpole.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            switch (this.flagColor) {
                case 0 -> this.FlagWhite.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 1 -> this.FlagOrange.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 2 -> this.FlagPurple.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 3 -> this.FlagLightBlue.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 4 -> this.FlagYellow.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 5 -> this.FlagGreen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 6 -> this.FlagLightRed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 7 -> this.FlagDarkGrey.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 8 -> this.FlagGrey.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 9 -> this.FlagCyan.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 10-> this.FlagDarkPurple.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 11-> this.FlagDarkBlue.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 12-> this.FlagBrown.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 13-> this.FlagDarkGreen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 14-> this.FlagRed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                case 15-> this.FlagBlack.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // Render selected helmet
        switch (this.helmet) {
            case 1 -> this.HelmetLeather.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 2 -> this.HelmetIron.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 3 -> this.HelmetGold.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 4 -> this.HelmetDiamond.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 5 -> {
                this.HelmetHide.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetNeckHide.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetHideEar1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetHideEar2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            case 6 -> {
                this.HelmetFur.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetNeckFur.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetFurEar1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetFurEar2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            case 7 -> {
                this.HelmetReptile.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetReptileEar1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.HelmetReptileEar2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            case 8 -> this.HelmetGreenChitin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 9 -> this.HelmetYellowChitin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 10-> this.HelmetBlueChitin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 11-> this.HelmetBlackChitin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            case 12-> this.HelmetRedChitin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
}
