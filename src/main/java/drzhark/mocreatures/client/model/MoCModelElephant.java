package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.neutral.MoCEntityElephant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * 1.20.1 port of the old MoCModelElephant (1.16) to ModelPart/LayerDefinition.
 */
public class MoCModelElephant<T extends MoCEntityElephant> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "elephant"), "main"
    );

    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart headBump;
    private final ModelPart chin;
    private final ModelPart lowerLip;
    private final ModelPart back;
    private final ModelPart leftSmallEar;
    private final ModelPart leftBigEar;
    private final ModelPart rightSmallEar;
    private final ModelPart rightBigEar;
    private final ModelPart hump;
    private final ModelPart body;
    private final ModelPart skirt;
    private final ModelPart rightTuskA;
    private final ModelPart rightTuskB;
    private final ModelPart rightTuskC;
    private final ModelPart rightTuskD;
    private final ModelPart leftTuskA;
    private final ModelPart leftTuskB;
    private final ModelPart leftTuskC;
    private final ModelPart leftTuskD;
    private final ModelPart trunkA;
    private final ModelPart trunkB;
    private final ModelPart trunkC;
    private final ModelPart trunkD;
    private final ModelPart trunkE;
    private final ModelPart frontRightUpperLeg;
    private final ModelPart frontRightLowerLeg;
    private final ModelPart frontLeftUpperLeg;
    private final ModelPart frontLeftLowerLeg;
    private final ModelPart backRightUpperLeg;
    private final ModelPart backRightLowerLeg;
    private final ModelPart backLeftUpperLeg;
    private final ModelPart backLeftLowerLeg;
    private final ModelPart tailRoot;
    private final ModelPart tail;
    private final ModelPart tailPlush;
    private final ModelPart storageRightBedroll;
    private final ModelPart storageLeftBedroll;
    private final ModelPart storageFrontRightChest;
    private final ModelPart storageBackRightChest;
    private final ModelPart storageFrontLeftChest;
    private final ModelPart storageBackLeftChest;
    private final ModelPart storageRightBlankets;
    private final ModelPart storageLeftBlankets;
    private final ModelPart harnessBlanket;
    private final ModelPart harnessUpperBelt;
    private final ModelPart harnessLowerBelt;
    private final ModelPart cabinPillow;
    private final ModelPart cabinLeftRail;
    private final ModelPart cabin;
    private final ModelPart cabinRightRail;
    private final ModelPart cabinBackRail;
    private final ModelPart cabinRoof;
    private final ModelPart fortNeckBeam;
    private final ModelPart fortBackBeam;
    private final ModelPart tuskLD1;
    private final ModelPart tuskLD2;
    private final ModelPart tuskLD3;
    private final ModelPart tuskLD4;
    private final ModelPart tuskLD5;
    private final ModelPart tuskRD1;
    private final ModelPart tuskRD2;
    private final ModelPart tuskRD3;
    private final ModelPart tuskRD4;
    private final ModelPart tuskRD5;
    private final ModelPart tuskLI1;
    private final ModelPart tuskLI2;
    private final ModelPart tuskLI3;
    private final ModelPart tuskLI4;
    private final ModelPart tuskLI5;
    private final ModelPart tuskRI1;
    private final ModelPart tuskRI2;
    private final ModelPart tuskRI3;
    private final ModelPart tuskRI4;
    private final ModelPart tuskRI5;
    private final ModelPart tuskLW1;
    private final ModelPart tuskLW2;
    private final ModelPart tuskLW3;
    private final ModelPart tuskLW4;
    private final ModelPart tuskLW5;
    private final ModelPart tuskRW1;
    private final ModelPart tuskRW2;
    private final ModelPart tuskRW3;
    private final ModelPart tuskRW4;
    private final ModelPart tuskRW5;
    private final ModelPart fortFloor1;
    private final ModelPart fortFloor2;
    private final ModelPart fortFloor3;
    private final ModelPart fortBackWall;
    private final ModelPart fortBackLeftWall;
    private final ModelPart fortBackRightWall;
    private final ModelPart storageUpLeft;
    private final ModelPart storageUpRight;

    // runtime state:
    private MoCEntityElephant elephant;
    private float radianF = 57.29578F;
    private int tusks;
    private boolean isSitting;
    private int tailCounter;
    private int earCounter;
    private int trunkCounter;

    public MoCModelElephant(ModelPart root) {
        super(root);
        this.head                  = root.getChild("head");
        this.neck                  = root.getChild("neck");
        this.headBump              = root.getChild("headBump");
        this.chin                  = root.getChild("chin");
        this.lowerLip              = root.getChild("lowerLip");
        this.back                  = root.getChild("back");
        this.leftSmallEar          = root.getChild("leftSmallEar");
        this.leftBigEar            = root.getChild("leftBigEar");
        this.rightSmallEar         = root.getChild("rightSmallEar");
        this.rightBigEar           = root.getChild("rightBigEar");
        this.hump                  = root.getChild("hump");
        this.body                  = root.getChild("body");
        this.skirt                 = root.getChild("skirt");
        this.rightTuskA            = root.getChild("rightTuskA");
        this.rightTuskB            = root.getChild("rightTuskB");
        this.rightTuskC            = root.getChild("rightTuskC");
        this.rightTuskD            = root.getChild("rightTuskD");
        this.leftTuskA             = root.getChild("leftTuskA");
        this.leftTuskB             = root.getChild("leftTuskB");
        this.leftTuskC             = root.getChild("leftTuskC");
        this.leftTuskD             = root.getChild("leftTuskD");
        this.trunkA                = root.getChild("trunkA");
        this.trunkB                = root.getChild("trunkB");
        this.trunkC                = root.getChild("trunkC");
        this.trunkD                = root.getChild("trunkD");
        this.trunkE                = root.getChild("trunkE");
        this.frontRightUpperLeg    = root.getChild("frontRightUpperLeg");
        this.frontRightLowerLeg    = root.getChild("frontRightLowerLeg");
        this.frontLeftUpperLeg     = root.getChild("frontLeftUpperLeg");
        this.frontLeftLowerLeg     = root.getChild("frontLeftLowerLeg");
        this.backRightUpperLeg     = root.getChild("backRightUpperLeg");
        this.backRightLowerLeg     = root.getChild("backRightLowerLeg");
        this.backLeftUpperLeg      = root.getChild("backLeftUpperLeg");
        this.backLeftLowerLeg      = root.getChild("backLeftLowerLeg");
        this.tailRoot              = root.getChild("tailRoot");
        this.tail                  = root.getChild("tail");
        this.tailPlush             = root.getChild("tailPlush");
        this.storageRightBedroll   = root.getChild("storageRightBedroll");
        this.storageLeftBedroll    = root.getChild("storageLeftBedroll");
        this.storageFrontRightChest= root.getChild("storageFrontRightChest");
        this.storageBackRightChest = root.getChild("storageBackRightChest");
        this.storageFrontLeftChest = root.getChild("storageFrontLeftChest");
        this.storageBackLeftChest  = root.getChild("storageBackLeftChest");
        this.storageRightBlankets  = root.getChild("storageRightBlankets");
        this.storageLeftBlankets   = root.getChild("storageLeftBlankets");
        this.harnessBlanket        = root.getChild("harnessBlanket");
        this.harnessUpperBelt      = root.getChild("harnessUpperBelt");
        this.harnessLowerBelt      = root.getChild("harnessLowerBelt");
        this.cabinPillow           = root.getChild("cabinPillow");
        this.cabinLeftRail         = root.getChild("cabinLeftRail");
        this.cabin                 = root.getChild("cabin");
        this.cabinRightRail        = root.getChild("cabinRightRail");
        this.cabinBackRail         = root.getChild("cabinBackRail");
        this.cabinRoof             = root.getChild("cabinRoof");
        this.fortNeckBeam          = root.getChild("fortNeckBeam");
        this.fortBackBeam          = root.getChild("fortBackBeam");
        this.tuskLD1               = root.getChild("tuskLD1");
        this.tuskLD2               = root.getChild("tuskLD2");
        this.tuskLD3               = root.getChild("tuskLD3");
        this.tuskLD4               = root.getChild("tuskLD4");
        this.tuskLD5               = root.getChild("tuskLD5");
        this.tuskRD1               = root.getChild("tuskRD1");
        this.tuskRD2               = root.getChild("tuskRD2");
        this.tuskRD3               = root.getChild("tuskRD3");
        this.tuskRD4               = root.getChild("tuskRD4");
        this.tuskRD5               = root.getChild("tuskRD5");
        this.tuskLI1               = root.getChild("tuskLI1");
        this.tuskLI2               = root.getChild("tuskLI2");
        this.tuskLI3               = root.getChild("tuskLI3");
        this.tuskLI4               = root.getChild("tuskLI4");
        this.tuskLI5               = root.getChild("tuskLI5");
        this.tuskRI1               = root.getChild("tuskRI1");
        this.tuskRI2               = root.getChild("tuskRI2");
        this.tuskRI3               = root.getChild("tuskRI3");
        this.tuskRI4               = root.getChild("tuskRI4");
        this.tuskRI5               = root.getChild("tuskRI5");
        this.tuskLW1               = root.getChild("tuskLW1");
        this.tuskLW2               = root.getChild("tuskLW2");
        this.tuskLW3               = root.getChild("tuskLW3");
        this.tuskLW4               = root.getChild("tuskLW4");
        this.tuskLW5               = root.getChild("tuskLW5");
        this.tuskRW1               = root.getChild("tuskRW1");
        this.tuskRW2               = root.getChild("tuskRW2");
        this.tuskRW3               = root.getChild("tuskRW3");
        this.tuskRW4               = root.getChild("tuskRW4");
        this.tuskRW5               = root.getChild("tuskRW5");
        this.fortFloor1            = root.getChild("fortFloor1");
        this.fortFloor2            = root.getChild("fortFloor2");
        this.fortFloor3            = root.getChild("fortFloor3");
        this.fortBackWall          = root.getChild("fortBackWall");
        this.fortBackLeftWall      = root.getChild("fortBackLeftWall");
        this.fortBackRightWall     = root.getChild("fortBackRightWall");
        this.storageUpLeft         = root.getChild("storageUpLeft");
        this.storageUpRight        = root.getChild("storageUpRight");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(60, 0)
                        .addBox(-5.5F, -6F, -8F, 11, 15, 10),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, 0F, 0F)
        );

        // NECK
        root.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(46, 48)
                        .addBox(-4.95F, -6F, -8F, 10, 14, 8),
                PartPose.offsetAndRotation(0F, -8F, -10F, -0.2617994F, 0F, 0F)
        );

        // HEAD BUMP
        root.addOrReplaceChild("headBump",
                CubeListBuilder.create()
                        .texOffs(104, 41)
                        .addBox(-3F, -9F, -6F, 6, 3, 6),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, 0F, 0F)
        );

        // CHIN
        root.addOrReplaceChild("chin",
                CubeListBuilder.create()
                        .texOffs(86, 56)
                        .addBox(-1.5F, -6F, -10.7F, 3, 5, 4),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 2.054118F, 0F, 0F)
        );

        // LOWER LIP
        root.addOrReplaceChild("lowerLip",
                CubeListBuilder.create()
                        .texOffs(80, 65)
                        .addBox(-2F, -2F, -14F, 4, 2, 6),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 1.570796F, 0F, 0F)
        );

        // BACK
        root.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-5F, -10F, -10F, 10, 2, 26),
                PartPose.offset(0F, -4F, -3F)
        );

        // LEFT SMALL EAR
        root.addOrReplaceChild("leftSmallEar",
                CubeListBuilder.create()
                        .texOffs(102, 0)
                        .addBox(2F, -8F, -5F, 8, 10, 1),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, -0.5235988F, 0.5235988F)
        );

        // LEFT BIG EAR
        root.addOrReplaceChild("leftBigEar",
                CubeListBuilder.create()
                        .texOffs(102, 0)
                        .addBox(2F, -8F, -5F, 12, 14, 1),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, -0.5235988F, 0.5235988F)
        );

        // RIGHT SMALL EAR
        root.addOrReplaceChild("rightSmallEar",
                CubeListBuilder.create()
                        .texOffs(106, 15)
                        .addBox(-10F, -8F, -5F, 8, 10, 1),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, 0.5235988F, -0.5235988F)
        );

        // RIGHT BIG EAR
        root.addOrReplaceChild("rightBigEar",
                CubeListBuilder.create()
                        .texOffs(102, 15)
                        .addBox(-14F, -8F, -5F, 12, 14, 1),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.1745329F, 0.5235988F, -0.5235988F)
        );

        // HUMP
        root.addOrReplaceChild("hump",
                CubeListBuilder.create()
                        .texOffs(88, 30)
                        .addBox(-6F, -2F, -3F, 12, 3, 8),
                PartPose.offset(0F, -13F, -5.5F)
        );

        // BODY
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8F, -10F, -10F, 16, 20, 28),
                PartPose.offset(0F, -2F, -3F)
        );

        // SKIRT
        root.addOrReplaceChild("skirt",
                CubeListBuilder.create()
                        .texOffs(28, 94)
                        .addBox(-8F, -10F, -6F, 16, 28, 6),
                PartPose.offsetAndRotation(0F, 8F, -3F, 1.570796F, 0F, 0F)
        );

        // RIGHT TUSK A
        root.addOrReplaceChild("rightTuskA",
                CubeListBuilder.create()
                        .texOffs(2, 60)
                        .addBox(-3.8F, -3.5F, -19F, 2, 2, 10),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 1.22173F, 0F, 0.1745329F)
        );

        // RIGHT TUSK B
        root.addOrReplaceChild("rightTuskB",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.8F, 6.2F, -24.2F, 2, 2, 7),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 0.6981317F, 0F, 0.1745329F)
        );

        // RIGHT TUSK C
        root.addOrReplaceChild("rightTuskC",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-3.8F, 17.1F, -21.9F, 2, 2, 5),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // RIGHT TUSK D
        root.addOrReplaceChild("rightTuskD",
                CubeListBuilder.create()
                        .texOffs(14, 18)
                        .addBox(-3.8F, 25.5F, -14.5F, 2, 2, 5),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // LEFT TUSK A
        root.addOrReplaceChild("leftTuskA",
                CubeListBuilder.create()
                        .texOffs(2, 48)
                        .addBox(1.8F, -3.5F, -19F, 2, 2, 10),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 1.22173F, 0F, -0.1745329F)
        );

        // LEFT TUSK B
        root.addOrReplaceChild("leftTuskB",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(1.8F, 6.2F, -24.2F, 2, 2, 7),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 0.6981317F, 0F, -0.1745329F)
        );

        // LEFT TUSK C
        root.addOrReplaceChild("leftTuskC",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(1.8F, 17.1F, -21.9F, 2, 2, 5),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // LEFT TUSK D
        root.addOrReplaceChild("leftTuskD",
                CubeListBuilder.create()
                        .texOffs(14, 18)
                        .addBox(1.8F, 25.5F, -14.5F, 2, 2, 5),
                PartPose.offsetAndRotation(0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TRUNK A
        root.addOrReplaceChild("trunkA",
                CubeListBuilder.create()
                        .texOffs(0, 76)
                        .addBox(-4F, -2.5F, -18F, 8, 7, 10),
                PartPose.offsetAndRotation(0F, -3F, -22.46667F, 1.570796F, 0F, 0F)
        );

        // TRUNK B
        root.addOrReplaceChild("trunkB",
                CubeListBuilder.create()
                        .texOffs(0, 93)
                        .addBox(-3F, -2.5F, -7F, 6, 5, 7),
                PartPose.offsetAndRotation(0F, 6.5F, -22.5F, 1.658063F, 0F, 0F)
        );

        // TRUNK C
        root.addOrReplaceChild("trunkC",
                CubeListBuilder.create()
                        .texOffs(0, 105)
                        .addBox(-2.5F, -2F, -4F, 5, 4, 5),
                PartPose.offsetAndRotation(0F, 13F, -22.0F, 1.919862F, 0F, 0F)
        );

        // TRUNK D
        root.addOrReplaceChild("trunkD",
                CubeListBuilder.create()
                        .texOffs(0, 114)
                        .addBox(-2F, -1.5F, -5F, 4, 3, 5),
                PartPose.offsetAndRotation(0F, 16F, -21.5F, 2.216568F, 0F, 0F)
        );

        // TRUNK E
        root.addOrReplaceChild("trunkE",
                CubeListBuilder.create()
                        .texOffs(0, 122)
                        .addBox(-1.5F, -1F, -4F, 3, 2, 4),
                PartPose.offsetAndRotation(0F, 19.5F, -19F, 2.530727F, 0F, 0F)
        );

        // FRONT RIGHT UPPER LEG
        root.addOrReplaceChild("frontRightUpperLeg",
                CubeListBuilder.create()
                        .texOffs(100, 109)
                        .addBox(-3.5F, 0F, -3.5F, 7, 12, 7),
                PartPose.offset( -4.6F, 4F, -9.6F)
        );

        // FRONT RIGHT LOWER LEG
        root.addOrReplaceChild("frontRightLowerLeg",
                CubeListBuilder.create()
                        .texOffs(100, 73)
                        .addBox(-3.5F, 0F, -3.5F, 7, 10, 7),
                PartPose.offset( -4.6F, 14F, -9.6F)
        );

        // FRONT LEFT UPPER LEG
        root.addOrReplaceChild("frontLeftUpperLeg",
                CubeListBuilder.create()
                        .texOffs(100, 90)
                        .addBox(-3.5F, 0F, -3.5F, 7, 12, 7),
                PartPose.offset( 4.6F, 4F, -9.6F)
        );

        // FRONT LEFT LOWER LEG
        root.addOrReplaceChild("frontLeftLowerLeg",
                CubeListBuilder.create()
                        .texOffs(72, 73)
                        .addBox(-3.5F, 0F, -3.5F, 7, 10, 7),
                PartPose.offset( 4.6F, 14F, -9.6F)
        );

        // BACK RIGHT UPPER LEG
        root.addOrReplaceChild("backRightUpperLeg",
                CubeListBuilder.create()
                        .texOffs(72, 109)
                        .addBox(-3.5F, 0F, -3.5F, 7, 12, 7),
                PartPose.offset( -4.6F, 4F, 11.6F)
        );

        // BACK RIGHT LOWER LEG
        root.addOrReplaceChild("backRightLowerLeg",
                CubeListBuilder.create()
                        .texOffs(100, 56)
                        .addBox(-3.5F, 0F, -3.5F, 7, 10, 7),
                PartPose.offset( -4.6F, 14F, 11.6F)
        );

        // BACK LEFT UPPER LEG
        root.addOrReplaceChild("backLeftUpperLeg",
                CubeListBuilder.create()
                        .texOffs(72, 90)
                        .addBox(-3.5F, 0F, -3.5F, 7, 12, 7),
                PartPose.offset( 4.6F, 4F, 11.6F)
        );

        // BACK LEFT LOWER LEG
        root.addOrReplaceChild("backLeftLowerLeg",
                CubeListBuilder.create()
                        .texOffs(44, 77)
                        .addBox(-3.5F, 0F, -3.5F, 7, 10, 7),
                PartPose.offset( 4.6F, 14F, 11.6F)
        );

        // TAIL ROOT
        root.addOrReplaceChild("tailRoot",
                CubeListBuilder.create()
                        .texOffs(20, 105)
                        .addBox(-1F, 0F, -2F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, -8F, 15F, 0.296706F, 0F, 0F)
        );

        // TAIL
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(20, 117)
                        .addBox(-1F, 9.7F, -0.2F, 2, 6, 2),
                PartPose.offsetAndRotation(0F, -8F, 15F, 0.1134464F, 0F, 0F)
        );

        // TAIL PLUSH
        root.addOrReplaceChild("tailPlush",
                CubeListBuilder.create()
                        .texOffs(26, 76)
                        .addBox(-1.5F, 15.5F, -0.7F, 3, 6, 3),
                PartPose.offsetAndRotation(0F, -8F, 15F, 0.1134464F, 0F, 0F)
        );

        // STORAGE RIGHT BEDROLL
        root.addOrReplaceChild("storageRightBedroll",
                CubeListBuilder.create()
                        .texOffs(90, 231)
                        .addBox(-2.5F, 8F, -8F, 3, 3, 16),
                PartPose.offsetAndRotation(-9F, -10.2F, 1F, 0F, 0F, 0.418879F)
        );

        // STORAGE LEFT BEDROLL
        root.addOrReplaceChild("storageLeftBedroll",
                CubeListBuilder.create()
                        .texOffs(90, 231)
                        .addBox(-0.5F, 8F, -8F, 3, 3, 16),
                PartPose.offsetAndRotation(9F, -10.2F, 1F, 0F, 0F, -0.418879F)
        );

        // STORAGE FRONT RIGHT CHEST
        root.addOrReplaceChild("storageFrontRightChest",
                CubeListBuilder.create()
                        .texOffs(76, 208)
                        .addBox(-3.5F, 0F, -5F, 5, 8, 10),
                PartPose.offsetAndRotation(-11F, -1.2F, -4.5F, 0F, 0F, -0.2617994F)
        );

        // STORAGE BACK RIGHT CHEST
        root.addOrReplaceChild("storageBackRightChest",
                CubeListBuilder.create()
                        .texOffs(76, 208)
                        .addBox(-3.5F, 0F, -5F, 5, 8, 10),
                PartPose.offsetAndRotation(-11F, -1.2F, 6.5F, 0F, 0F, -0.2617994F)
        );

        // STORAGE FRONT LEFT CHEST
        root.addOrReplaceChild("storageFrontLeftChest",
                CubeListBuilder.create()
                        .texOffs(76, 226)
                        .addBox(-1.5F, 0F, -5F, 5, 8, 10),
                PartPose.offsetAndRotation(11F, -1.2F, -4.5F, 0F, 0F, 0.2617994F)
        );

        // STORAGE BACK LEFT CHEST
        root.addOrReplaceChild("storageBackLeftChest",
                CubeListBuilder.create()
                        .texOffs(76, 226)
                        .addBox(-1.5F, 0F, -5F, 5, 8, 10),
                PartPose.offsetAndRotation(11F, -1.2F, 6.5F, 0F, 0F, 0.2617994F)
        );

        // STORAGE RIGHT BLANKETS
        root.addOrReplaceChild("storageRightBlankets",
                CubeListBuilder.create()
                        .texOffs(0, 228)
                        .addBox(-4.5F, -1F, -7F, 5, 10, 14),
                PartPose.offset( -9F, -10.2F, 1F)
        );

        // STORAGE LEFT BLANKETS
        root.addOrReplaceChild("storageLeftBlankets",
                CubeListBuilder.create()
                        .texOffs(38, 228)
                        .addBox(-0.5F, -1F, -7F, 5, 10, 14),
                PartPose.offset( 9F, -10.2F, 1F)
        );

        // HARNESS BLANKET
        root.addOrReplaceChild("harnessBlanket",
                CubeListBuilder.create()
                        .texOffs(0, 196)
                        .addBox(-8.5F, -2F, -3F, 17, 14, 18),
                PartPose.offset( 0F, -13.2F, -3.5F)
        );

        // HARNESS UPPER BELT
        root.addOrReplaceChild("harnessUpperBelt",
                CubeListBuilder.create()
                        .texOffs(70, 196)
                        .addBox(-8.5F, 0.5F, -2F, 17, 10, 2),
                PartPose.offset( 0F, -2F, -2.5F)
        );

        // HARNESS LOWER BELT
        root.addOrReplaceChild("harnessLowerBelt",
                CubeListBuilder.create()
                        .texOffs(70, 196)
                        .addBox(-8.5F, 0.5F, -2.5F, 17, 10, 2),
                PartPose.offset( 0F, -2F, 7F)
        );

        // CABIN PILLOW
        root.addOrReplaceChild("cabinPillow",
                CubeListBuilder.create()
                        .texOffs(76, 146)
                        .addBox(-6.5F, 0F, -6.5F, 13, 4, 13),
                PartPose.offset( 0F, -16F, 2F)
        );

        // CABIN LEFT RAIL
        root.addOrReplaceChild("cabinLeftRail",
                CubeListBuilder.create()
                        .texOffs(56, 147)
                        .addBox(-7F, 0F, 7F, 14, 1, 1),
                PartPose.offsetAndRotation(0F, -23F, 1.5F, 0F, 1.570796F, 0F)
        );

        // CABIN
        root.addOrReplaceChild("cabin",
                CubeListBuilder.create()
                        .texOffs(0, 128)
                        .addBox(-7F, 0F, -7F, 14, 20, 14),
                PartPose.offset( 0F, -35F, 2F)
        );

        // CABIN RIGHT RAIL
        root.addOrReplaceChild("cabinRightRail",
                CubeListBuilder.create()
                        .texOffs(56, 147)
                        .addBox(-7F, 0F, 7F, 14, 1, 1),
                PartPose.offsetAndRotation(0F, -23F, 1.5F, 0F, -1.570796F, 0F)
        );

        // CABIN BACK RAIL
        root.addOrReplaceChild("cabinBackRail",
                CubeListBuilder.create()
                        .texOffs(56, 147)
                        .addBox(-7F, 0F, 7F, 14, 1, 1),
                PartPose.offset( 0F, -23F, 1.5F)
        );

        // CABIN ROOF
        root.addOrReplaceChild("cabinRoof",
                CubeListBuilder.create()
                        .texOffs(56, 128)
                        .addBox(-7.5F, 0F, -7.5F, 15, 4, 15),
                PartPose.offset( 0F, -34F, 2F)
        );

        // FORT NECK BEAM
        root.addOrReplaceChild("fortNeckBeam",
                CubeListBuilder.create()
                        .texOffs(26, 180)
                        .addBox(-12F, 0F, -20.5F, 24, 4, 4),
                PartPose.offset( 0F, -16F, 10F)
        );

        // FORT BACK BEAM
        root.addOrReplaceChild("fortBackBeam",
                CubeListBuilder.create()
                        .texOffs(26, 180)
                        .addBox(-12F, 0F, 0F, 24, 4, 4),
                PartPose.offset( 0F, -16F, 10F)
        );

        // TUSK LD1
        root.addOrReplaceChild("tuskLD1",
                CubeListBuilder.create()
                        .texOffs(108, 207)
                        .addBox(1.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, -0.1745329F)
        );

        // TUSK LD2
        root.addOrReplaceChild("tuskLD2",
                CubeListBuilder.create()
                        .texOffs(112, 199)
                        .addBox(1.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LD3
        root.addOrReplaceChild("tuskLD3",
                CubeListBuilder.create()
                        .texOffs(110, 190)
                        .addBox(1.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK LD4
        root.addOrReplaceChild("tuskLD4",
                CubeListBuilder.create()
                        .texOffs(86, 175)
                        .addBox(2.7F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LD5
        root.addOrReplaceChild("tuskLD5",
                CubeListBuilder.create()
                        .texOffs(112, 225)
                        .addBox(2.7F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK RD1
        root.addOrReplaceChild("tuskRD1",
                CubeListBuilder.create()
                        .texOffs(108, 207)
                        .addBox(-4.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, 0.1745329F)
        );

        // TUSK RD2
        root.addOrReplaceChild("tuskRD2",
                CubeListBuilder.create()
                        .texOffs(112, 199)
                        .addBox(-4.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RD3
        root.addOrReplaceChild("tuskRD3",
                CubeListBuilder.create()
                        .texOffs(110, 190)
                        .addBox(-4.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // TUSK RD4
        root.addOrReplaceChild("tuskRD4",
                CubeListBuilder.create()
                        .texOffs(86, 163)
                        .addBox(-2.8F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RD5
        root.addOrReplaceChild("tuskRD5",
                CubeListBuilder.create()
                        .texOffs(112, 232)
                        .addBox(-2.8F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // TUSK LI1
        root.addOrReplaceChild("tuskLI1",
                CubeListBuilder.create()
                        .texOffs(108, 180)
                        .addBox(1.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, -0.1745329F)
        );

        // TUSK LI2
        root.addOrReplaceChild("tuskLI2",
                CubeListBuilder.create()
                        .texOffs(112, 172)
                        .addBox(1.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LI3
        root.addOrReplaceChild("tuskLI3",
                CubeListBuilder.create()
                        .texOffs(110, 163)
                        .addBox(1.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK LI4
        root.addOrReplaceChild("tuskLI4",
                CubeListBuilder.create()
                        .texOffs(96, 175)
                        .addBox(2.7F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LI5
        root.addOrReplaceChild("tuskLI5",
                CubeListBuilder.create()
                        .texOffs(112, 209)
                        .addBox(2.7F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK RI1
        root.addOrReplaceChild("tuskRI1",
                CubeListBuilder.create()
                        .texOffs(108, 180)
                        .addBox(-4.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, 0.1745329F)
        );

        // TUSK RI2
        root.addOrReplaceChild("tuskRI2",
                CubeListBuilder.create()
                        .texOffs(112, 172)
                        .addBox(-4.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RI3
        root.addOrReplaceChild("tuskRI3",
                CubeListBuilder.create()
                        .texOffs(110, 163)
                        .addBox(-4.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // TUSK RI4
        root.addOrReplaceChild("tuskRI4",
                CubeListBuilder.create()
                        .texOffs(96, 163)
                        .addBox(-2.8F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RI5
        root.addOrReplaceChild("tuskRI5",
                CubeListBuilder.create()
                        .texOffs(112, 216)
                        .addBox(-2.8F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // TUSK LW1
        root.addOrReplaceChild("tuskLW1",
                CubeListBuilder.create()
                        .texOffs(56, 166)
                        .addBox(1.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, -0.1745329F)
        );

        // TUSK LW2
        root.addOrReplaceChild("tuskLW2",
                CubeListBuilder.create()
                        .texOffs(60, 158)
                        .addBox(1.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LW3
        root.addOrReplaceChild("tuskLW3",
                CubeListBuilder.create()
                        .texOffs(58, 149)
                        .addBox(1.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK LW4
        root.addOrReplaceChild("tuskLW4",
                CubeListBuilder.create()
                        .texOffs(46, 164)
                        .addBox(2.7F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, -0.1745329F)
        );

        // TUSK LW5
        root.addOrReplaceChild("tuskLW5",
                CubeListBuilder.create()
                        .texOffs(52, 192)
                        .addBox(2.7F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, -0.1745329F)
        );

        // TUSK RW1
        root.addOrReplaceChild("tuskRW1",
                CubeListBuilder.create()
                        .texOffs(56, 166)
                        .addBox(-4.3F, 5.5F, -24.2F, 3, 3, 7),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.6981317F, 0F, 0.1745329F)
        );

        // TUSK RW2
        root.addOrReplaceChild("tuskRW2",
                CubeListBuilder.create()
                        .texOffs(60, 158)
                        .addBox(-4.29F, 16.5F, -21.9F, 3, 3, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RW3
        root.addOrReplaceChild("tuskRW3",
                CubeListBuilder.create()
                        .texOffs(58, 149)
                        .addBox(-4.3F, 24.9F, -15.5F, 3, 3, 6),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // TUSK RW4
        root.addOrReplaceChild("tuskRW4",
                CubeListBuilder.create()
                        .texOffs(46, 157)
                        .addBox(-2.8F, 14.5F, -21.9F, 0, 7, 5),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, 0.1745329F, 0F, 0.1745329F)
        );

        // TUSK RW5
        root.addOrReplaceChild("tuskRW5",
                CubeListBuilder.create()
                        .texOffs(52, 199)
                        .addBox(-2.8F, 22.9F, -17.5F, 0, 7, 8),
                PartPose.offsetAndRotation( 0F, -10F, -16.5F, -0.3490659F, 0F, 0.1745329F)
        );

        // FORT FLOOR 1
        root.addOrReplaceChild("fortFloor1",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(-0.5F, -20F, -6F, 1, 8, 12),
                PartPose.offsetAndRotation(0F, -16F, 10F, 1.570796F, 0F, 1.570796F)
        );

        // FORT FLOOR 2
        root.addOrReplaceChild("fortFloor2",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(-0.5F, -12F, -6F, 1, 8, 12),
                PartPose.offsetAndRotation(0F, -16F, 10F, 1.570796F, 0F, 1.570796F)
        );

        // FORT FLOOR 3
        root.addOrReplaceChild("fortFloor3",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(-0.5F, -4F, -6F, 1, 8, 12),
                PartPose.offsetAndRotation(0F, -16F, 10F, 1.570796F, 0F, 1.570796F)
        );

        // FORT BACK WALL
        root.addOrReplaceChild("fortBackWall",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(-5F, -6.2F, -6F, 1, 8, 12),
                PartPose.offsetAndRotation(0F, -16F, 10F, 0F, 1.570796F, 0F)
        );

        // FORT BACK LEFT WALL
        root.addOrReplaceChild("fortBackLeftWall",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(6F, -6F, -7F, 1, 8, 12),
                PartPose.offset(0F, -16F, 10F)
        );

        // FORT BACK RIGHT WALL
        root.addOrReplaceChild("fortBackRightWall",
                CubeListBuilder.create()
                        .texOffs(0, 176)
                        .addBox(-7F, -6F, -7F, 1, 8, 12),
                PartPose.offset(0F, -16F, 10F)
        );

        // STORAGE UP LEFT
        root.addOrReplaceChild("storageUpLeft",
                CubeListBuilder.create()
                        .texOffs(76, 226)
                        .addBox(6.5F, 1F, -14F, 5, 8, 10),
                PartPose.offsetAndRotation(0F, -16F, 10F, 0F, 0F, -0.3839724F)
        );

        // STORAGE UP RIGHT
        root.addOrReplaceChild("storageUpRight",
                CubeListBuilder.create()
                        .texOffs(76, 208)
                        .addBox(-11.5F, 1F, -14F, 5, 8, 10),
                PartPose.offsetAndRotation(0F, -16F, 10F, 0F, 0F, 0.3839724F)
        );

        return LayerDefinition.create(mesh, 128, 256);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.elephant      = entity;
        this.tusks         = elephant.getTusks();
        this.tailCounter   = elephant.tailCounter;
        this.earCounter    = elephant.earCounter;
        this.trunkCounter  = elephant.trunkCounter;
        this.isSitting     = (elephant.sitCounter != 0);
        
        // Call the setRotationAngles to apply all animations
        this.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }


    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        int type    = elephant.getTypeMoC();
        int harness = elephant.getArmorType();
        int storage = elephant.getStorage();

        // render tusks based on tusk type:
        if (tusks == 0) {
            leftTuskB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rightTuskB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            if (elephant.getIsAdult() || elephant.getMoCAge() > 70) {
                leftTuskC.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                rightTuskC.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (elephant.getIsAdult() || elephant.getMoCAge() > 90) {
                leftTuskD.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                rightTuskD.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        } else if (tusks == 1) {
            tuskLW1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLW2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLW3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLW4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLW5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRW1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRW2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRW3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRW4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRW5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else if (tusks == 2) {
            tuskLI1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLI2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLI3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLI4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLI5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRI1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRI2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRI3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRI4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRI5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else if (tusks == 3) {
            tuskLD1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLD2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLD3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLD4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskLD5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRD1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRD2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRD3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRD4.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            tuskRD5.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // ears: big if African (type=1), small otherwise
        if (type == 1) {
            leftBigEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rightBigEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            leftSmallEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            rightSmallEar.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // mammoths (type 3 or 4)
        if (type == 3 || type == 4) {
            headBump.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            skirt.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // harness
        if (harness >= 1) {
            harnessBlanket.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            harnessUpperBelt.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            harnessLowerBelt.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            if (type == 5) {
                skirt.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // cabin or fortress
        if (harness == 3) {
            if (type == 5) {
                cabinPillow.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                cabinLeftRail.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                cabin.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                cabinRightRail.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                cabinBackRail.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                cabinRoof.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (type == 4) {
                fortBackRightWall.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortBackLeftWall.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortBackWall.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortFloor1.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortFloor2.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortFloor3.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortNeckBeam.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                fortBackBeam.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // storage chests + blankets
        if (storage >= 1) {
            storageRightBedroll.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageFrontRightChest.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageBackRightChest.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageRightBlankets.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        if (storage >= 2) {
            storageLeftBlankets.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageLeftBedroll.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageFrontLeftChest.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            storageBackLeftChest.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        if (storage >= 3) {
            storageUpLeft.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        if (storage >= 4) {
            storageUpRight.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // HEAD, NECK, CHIN, LOWER LIP, BACK, HUMP, BODY
        head.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        neck.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        chin.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lowerLip.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        back.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        hump.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // TUSK A (always shown)
        rightTuskA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftTuskA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // TRUNK segments
        trunkA.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        trunkB.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        trunkC.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        trunkD.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        trunkE.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // LEGS
        frontRightUpperLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontRightLowerLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontLeftUpperLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        frontLeftLowerLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        backRightUpperLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        backRightLowerLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        backLeftUpperLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        backLeftLowerLeg.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // TAIL
        tailRoot.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tail.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tailPlush.render(matrixStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * Exactly the same logic as the old setRotationAngles(...) in 1.16,
     * but all ModelRenderer.setRotationPoint() calls have already been
     * baked into PartPose.offset in createBodyLayer().
     */
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float headPitch) {

        // Convert pitch/yaw to radians, clamp yaw to ±20°
        if (headPitch < 0) headPitch = 0;
        float headXRadians = headPitch / radianF;
        float headY = Mth.clamp(netHeadYaw, -20F, 20F) / radianF;

        // leg swinging
        float rLegX = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float lLegX = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;

        // adjust overall vertical offset if sitting
        float yOffset = isSitting ? 8F : 0F;
        adjustY(yOffset);

        // trunk oscillation
        float trunkOsc = 0F;
        if (trunkCounter != 0) {
            headXRadians = 0F;
            trunkOsc = Mth.cos(trunkCounter * 0.2F) * 12F;
        }
        if (isSitting) {
            headXRadians = 0F;
            trunkOsc = 25F;
        }

        // HEAD orientation + bump, chin, lower lip align
        head.xRot = -10F / radianF + headXRadians;
        head.yRot = headY;
        headBump.xRot = head.xRot;
        headBump.yRot = headY;
        chin.yRot = headY;
        chin.xRot = 113F / radianF + headXRadians;
        lowerLip.yRot = headY;
        lowerLip.xRot = 85F / radianF + headXRadians;

        // tusk A always rotates along with head
        rightTuskA.yRot = headY;
        rightTuskA.xRot = 70F / radianF + headXRadians;
        leftTuskA.yRot = headY;
        leftTuskA.xRot = 70F / radianF + headXRadians;

        // Ear "flapping" / random wiggle
        float earFlap = 0F;
        if (earCounter != 0) {
            earFlap = Mth.cos(earCounter * 0.5F) * 0.35F;
        }
        // Big ears if African(type=1), else small ears
        rightBigEar.yRot    = 30F / radianF + headY + earFlap;
        rightSmallEar.yRot  = 30F / radianF + headY + earFlap;
        leftBigEar.yRot     = -30F / radianF + headY - earFlap;
        leftSmallEar.yRot   = -30F / radianF + headY - earFlap;

        rightBigEar.xRot    = -10F / radianF + headXRadians;
        rightSmallEar.xRot  = -10F / radianF + headXRadians;
        leftBigEar.xRot     = -10F / radianF + headXRadians;
        leftSmallEar.xRot   = -10F / radianF + headXRadians;

        // TRUNK chaining: each segment's origin is "attached" to previous.
        // We must recalculate trunkB,C,D,E pivot points so they move with trunkA's rotation.

        trunkA.yRot = headY;
        float trunkARot = (90F - trunkOsc);
        if (trunkARot < 85) trunkARot = 85;
        trunkA.xRot = trunkARot / radianF + headXRadians;
        // Apply position adjustment to follow head
        repositionChildFromParent(trunkA, head);

        // Reposition trunkB relative to trunkA's new pivot/rotation
        repositionChildFromParent(trunkB, trunkA);
        trunkB.yRot = headY;
        trunkB.xRot = (95F - trunkOsc * 1.5F) / radianF + headXRadians;

        repositionChildFromParent(trunkC, trunkB);
        trunkC.yRot = headY;
        trunkC.xRot = (110F - trunkOsc * 3F) / radianF + headXRadians;

        repositionChildFromParent(trunkD, trunkC);
        trunkD.yRot = headY;
        trunkD.xRot = (127F - trunkOsc * 4.5F) / radianF + headXRadians;

        repositionChildFromParent(trunkE, trunkD);
        trunkE.yRot = headY;
        trunkE.xRot = (145F - trunkOsc * 6F) / radianF + headXRadians;

        // LEGS: if sitting, rotate all upper legs to -30°, lower legs to +90°
        if (isSitting) {
            frontRightUpperLeg.xRot = -30F / radianF;
            frontLeftUpperLeg.xRot  = -30F / radianF;
            backRightUpperLeg.xRot   = -30F / radianF;
            backLeftUpperLeg.xRot    = -30F / radianF;

            frontRightLowerLeg.xRot = 90F / radianF;
            frontLeftLowerLeg.xRot  = 90F / radianF;
            backRightLowerLeg.xRot  = 90F / radianF;
            backLeftLowerLeg.xRot   = 90F / radianF;
        } else {
            frontRightUpperLeg.xRot = rLegX;
            frontLeftUpperLeg.xRot  = lLegX;
            backRightUpperLeg.xRot  = lLegX;
            backLeftUpperLeg.xRot   = rLegX;

            // compute D‐joint angles based on upper‐leg angles
            float lDeg = (lLegX * (180F / (float)Math.PI));
            float rDeg = (rLegX * (180F / (float)Math.PI));
            if (lDeg > 0F) lDeg *= 2F;
            if (rDeg > 0F) rDeg *= 2F;

            frontLeftLowerLeg.xRot   = lDeg / radianF;
            frontRightLowerLeg.xRot  = rDeg / radianF;
            backLeftLowerLeg.xRot    = rDeg / radianF;
            backRightLowerLeg.xRot   = lDeg / radianF;
        }
        // adjust leg "attach" pivots: child's pivot must match parent's moved position
        repositionLegChild(frontRightLowerLeg, frontRightUpperLeg);
        repositionLegChild(frontLeftLowerLeg, frontLeftUpperLeg);
        repositionLegChild(backRightLowerLeg, backRightUpperLeg);
        repositionLegChild(backLeftLowerLeg, backLeftUpperLeg);

        // TUSKS (type‐dependent) all swivel with head
        switch (tusks) {
            case 0:
                leftTuskB.yRot   = headY;
                leftTuskC.yRot   = headY;
                leftTuskD.yRot   = headY;
                rightTuskB.yRot  = headY;
                rightTuskC.yRot  = headY;
                rightTuskD.yRot  = headY;

                leftTuskB.xRot   =  40F / radianF + headXRadians;
                leftTuskC.xRot   =  10F / radianF + headXRadians;
                leftTuskD.xRot   = -20F / radianF + headXRadians;
                rightTuskB.xRot  =  40F / radianF + headXRadians;
                rightTuskC.xRot  =  10F / radianF + headXRadians;
                rightTuskD.xRot  = -20F / radianF + headXRadians;
                break;
            case 1:
                for (ModelPart m : new ModelPart[]{tuskLW1, tuskLW2, tuskLW3, tuskLW4, tuskLW5,
                        tuskRW1, tuskRW2, tuskRW3, tuskRW4, tuskRW5}) {
                    m.yRot = headY;
                    m.xRot = 40F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLW2, tuskLW4, tuskRW2, tuskRW4}) {
                    m.xRot = 10F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLW3, tuskRW3}) {
                    m.xRot = -20F / radianF + headXRadians;
                }
                break;
            case 2:
                for (ModelPart m : new ModelPart[]{tuskLI1, tuskLI2, tuskLI3, tuskLI4, tuskLI5,
                        tuskRI1, tuskRI2, tuskRI3, tuskRI4, tuskRI5}) {
                    m.yRot = headY;
                    m.xRot = 40F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLI2, tuskLI4, tuskRI2, tuskRI4}) {
                    m.xRot = 10F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLI3, tuskRI3}) {
                    m.xRot = -20F / radianF + headXRadians;
                }
                break;
            case 3:
                for (ModelPart m : new ModelPart[]{tuskLD1, tuskLD2, tuskLD3, tuskLD4, tuskLD5,
                        tuskRD1, tuskRD2, tuskRD3, tuskRD4, tuskRD5}) {
                    m.yRot = headY;
                    m.xRot = 40F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLD2, tuskLD4, tuskRD2, tuskRD4}) {
                    m.xRot = 10F / radianF + headXRadians;
                }
                for (ModelPart m : new ModelPart[]{tuskLD3, tuskRD3}) {
                    m.xRot = -20F / radianF + headXRadians;
                }
                break;
        }

        // CHEST animations (slightly bounce with legs)
        storageLeftBedroll.xRot      = lLegX / 10F;
        storageFrontLeftChest.xRot   = lLegX / 5F;
        storageBackLeftChest.xRot    = lLegX / 5F;
        storageRightBedroll.xRot     = rLegX / 10F;
        storageFrontRightChest.xRot  = rLegX / 5F;
        storageBackRightChest.xRot   = rLegX / 5F;

        fortNeckBeam.zRot            = lLegX / 50F;
        fortBackBeam.zRot            = lLegX / 50F;
        fortBackRightWall.zRot       = lLegX / 50F;
        fortBackLeftWall.zRot        = lLegX / 50F;
        fortBackWall.xRot            = -lLegX / 50F;

        // TAIL swish
        float tailSwing = limbSwingAmount * 0.9F;
        if (tailSwing < 0) tailSwing = 0;
        if (tailCounter != 0) {
            tailRoot.yRot = Mth.cos(ageInTicks * 0.4F) * 1.3F;
            tailSwing = 30F / radianF;
        } else {
            tailRoot.yRot = 0;
        }
        tailRoot.xRot   = 17F / radianF + tailSwing;
        tail.xRot       = 6.5F / radianF + tailSwing;
        tailPlush.xRot  = tail.xRot;
        tailPlush.yRot  = tailRoot.yRot;
        tail.yRot       = tailPlush.yRot;
    }


    /**
     * Adjust all pivot Ys by the same offset when sitting (shifts entire model up/down).
     */
    private void adjustY(float offsetY) {
        head.y           = -10F + offsetY;
        neck.y           = -8F + offsetY;
        headBump.y       = -10F + offsetY;
        chin.y           = -10F + offsetY;
        lowerLip.y       = -10F + offsetY;
        back.y           = -4F + offsetY;
        leftSmallEar.y   = -10F + offsetY;
        leftBigEar.y     = -10F + offsetY;
        rightSmallEar.y  = -10F + offsetY;
        rightBigEar.y    = -10F + offsetY;
        hump.y           = -13F + offsetY;
        body.y           = -2F + offsetY;
        skirt.y          = 8F + offsetY;
        rightTuskA.y     = -10F + offsetY;
        rightTuskB.y     = -10F + offsetY;
        rightTuskC.y     = -10F + offsetY;
        rightTuskD.y     = -10F + offsetY;
        leftTuskA.y      = -10F + offsetY;
        leftTuskB.y      = -10F + offsetY;
        leftTuskC.y      = -10F + offsetY;
        leftTuskD.y      = -10F + offsetY;
        // Keep trunkA in sync with the head position
        trunkA.y         = -3F + offsetY;  // The base Y position is adjusted with offsetY
        trunkB.y         = 6.5F + offsetY;
        trunkC.y         = 13F + offsetY;
        trunkD.y         = 16F + offsetY;
        trunkE.y         = 19.5F + offsetY;
        frontRightUpperLeg.y = 4F + offsetY;
        frontRightLowerLeg.y = 14F + offsetY;
        frontLeftUpperLeg.y  = 4F + offsetY;
        frontLeftLowerLeg.y  = 14F + offsetY;
        backRightUpperLeg.y  = 4F + offsetY;
        backRightLowerLeg.y  = 14F + offsetY;
        backLeftUpperLeg.y   = 4F + offsetY;
        backLeftLowerLeg.y   = 14F + offsetY;
        tailRoot.y         = -8F + offsetY;
        tail.y             = -8F + offsetY;
        tailPlush.y         = -8F + offsetY;
        storageRightBedroll.y   = -10.2F + offsetY;
        storageLeftBedroll.y    = -10.2F + offsetY;
        storageFrontRightChest.y= -1.2F + offsetY;
        storageBackRightChest.y = -1.2F + offsetY;
        storageFrontLeftChest.y = -1.2F + offsetY;
        storageBackLeftChest.y  = -1.2F + offsetY;
        storageRightBlankets.y = -10.2F + offsetY;
        storageLeftBlankets.y  = -10.2F + offsetY;
        harnessBlanket.y       = -13.2F + offsetY;
        harnessUpperBelt.y     = -2F + offsetY;
        harnessLowerBelt.y     = -2F + offsetY;
        cabinPillow.y          = -16F + offsetY;
        cabinLeftRail.y        = -23F + offsetY;
        cabin.y                = -35F + offsetY;
        cabinRightRail.y       = -23F + offsetY;
        cabinBackRail.y        = -23F + offsetY;
        cabinRoof.y            = -34F + offsetY;
        fortNeckBeam.y         = -16F + offsetY;
        fortBackBeam.y         = -16F + offsetY;
        fortFloor1.y           = -16F + offsetY;
        fortFloor2.y           = -16F + offsetY;
        fortFloor3.y           = -16F + offsetY;
        fortBackWall.y         = -16F + offsetY;
        fortBackLeftWall.y     = -16F + offsetY;
        fortBackRightWall.y    = -16F + offsetY;
        storageUpLeft.y        = -16F + offsetY;
        storageUpRight.y       = -16F + offsetY;
        tuskLD1.y              = -10F + offsetY;
        tuskLD2.y              = -10F + offsetY;
        tuskLD3.y              = -10F + offsetY;
        tuskLD4.y              = -10F + offsetY;
        tuskLD5.y              = -10F + offsetY;
        tuskRD1.y              = -10F + offsetY;
        tuskRD2.y              = -10F + offsetY;
        tuskRD3.y              = -10F + offsetY;
        tuskRD4.y              = -10F + offsetY;
        tuskRD5.y              = -10F + offsetY;
        tuskLI1.y              = -10F + offsetY;
        tuskLI2.y              = -10F + offsetY;
        tuskLI3.y              = -10F + offsetY;
        tuskLI4.y              = -10F + offsetY;
        tuskLI5.y              = -10F + offsetY;
        tuskRI1.y              = -10F + offsetY;
        tuskRI2.y              = -10F + offsetY;
        tuskRI3.y              = -10F + offsetY;
        tuskRI4.y              = -10F + offsetY;
        tuskRI5.y              = -10F + offsetY;
        tuskLW1.y              = -10F + offsetY;
        tuskLW2.y              = -10F + offsetY;
        tuskLW3.y              = -10F + offsetY;
        tuskLW4.y              = -10F + offsetY;
        tuskLW5.y              = -10F + offsetY;
        tuskRW1.y              = -10F + offsetY;
        tuskRW2.y              = -10F + offsetY;
        tuskRW3.y              = -10F + offsetY;
        tuskRW4.y              = -10F + offsetY;
        tuskRW5.y              = -10F + offsetY;
    }

    /**
     * When a child part is "attached" to a rotating parent, we must recalculate
     * the child's pivot so that it "follows" the parent.  This helper mimics
     * the old ModelRenderer chaining, but with ModelPart coordinates.
     */
    private void repositionChildFromParent(ModelPart child, ModelPart parent) {
        // original Y distance from parent → child
        float dy = child.y - parent.y;
        float dz = child.z - parent.z;

        // child's new Y = parentY + sin(parentXrot)*dy
        // child's new Z = parentZ - cos(parentYrot)*(cos(parentXrot)*dy)
        child.y = parent.y + Mth.sin(parent.xRot) * dy;
        child.z = parent.z - Mth.cos(parent.yRot) * (Mth.cos(parent.xRot) * dy);
        // child.xRot, child.yRot, child.zRot remain as set in setRotationAngles
    }

    /**
     * Same as old "AdjustXRotationPoints" for legs:
     * replots the child "lower leg" pivot so that it sits at the bottom of the rotated "upper leg."
     */
    private void repositionLegChild(ModelPart lower, ModelPart upper) {
        float dY = Math.abs(lower.y - upper.y);
        lower.z = upper.z + Mth.sin(upper.xRot) * dY;
        lower.y = upper.y + Mth.cos(upper.xRot) * dY;
    }
}
