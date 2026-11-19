package drzhark.mocreatures.client.model;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.neutral.MoCEntityWyvern;
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
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelWyvern<T extends MoCEntityWyvern> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "wyvern"), "main"
    );

    private final ModelPart Tail;
    private final ModelPart back1;
    private final ModelPart tail1;
    private final ModelPart back2;
    private final ModelPart tail2;
    private final ModelPart back3;
    private final ModelPart tail3;
    private final ModelPart back4;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart chest;
    private final ModelPart neckplate3;
    private final ModelPart neck3;
    private final ModelPart MainHead;
    private final ModelPart neck2;
    private final ModelPart neckplate2;
    private final ModelPart neck1;
    private final ModelPart neckplate1;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart headplate;
    private final ModelPart beak;
    private final ModelPart righteyesock;
    private final ModelPart lefteyesock;
    private final ModelPart Jaw;
    private final ModelPart leftupjaw;
    private final ModelPart rightupjaw;
    private final ModelPart mouthrod;
    private final ModelPart helmetstrap1;
    private final ModelPart helmetstrap2;
    private final ModelPart controlrope1;
    private final ModelPart controlrope2;
    private final ModelPart rightearskin;
    private final ModelPart leftearskin;
    private final ModelPart rightspine1;
    private final ModelPart rightspine2;
    private final ModelPart rightspine3;
    private final ModelPart leftspine1;
    private final ModelPart leftspine2;
    private final ModelPart leftspine3;
    private final ModelPart ironhelmethorn1;
    private final ModelPart ironhelmethorn2;
    private final ModelPart ironhelmet;
    private final ModelPart ironhelmetsnout;
    private final ModelPart goldhelmethorn1;
    private final ModelPart goldhelmethorn2;
    private final ModelPart goldhelmet;
    private final ModelPart goldhelmetsnout;
    private final ModelPart diamondhelmet;
    private final ModelPart diamondhelmethorn2;
    private final ModelPart diamondhelmethorn1;
    private final ModelPart diamondhelmetsnout;
    private final ModelPart torso;
    private final ModelPart saddle;
    private final ModelPart rightshoulder;
    private final ModelPart leftshoulder;
    private final ModelPart LeftWing;
    private final ModelPart leftuparm;
    private final ModelPart leftlowarm;
    private final ModelPart leftfing1a;
    private final ModelPart leftfing1b;
    private final ModelPart leftfing2a;
    private final ModelPart leftfing2b;
    private final ModelPart leftfing3a;
    private final ModelPart leftfing3b;
    private final ModelPart leftwingflap1;
    private final ModelPart leftwingflap2;
    private final ModelPart leftwingflap3;
    private final ModelPart RightWing;
    private final ModelPart rightuparm;
    private final ModelPart rightlowarm;
    private final ModelPart rightfing1a;
    private final ModelPart rightfing1b;
    private final ModelPart rightwingflap1;
    private final ModelPart rightfing2a;
    private final ModelPart rightfing2b;
    private final ModelPart rightwingflap2;
    private final ModelPart rightfing3a;
    private final ModelPart rightfing3b;
    private final ModelPart rightwingflap3;
    private final ModelPart leftupleg;
    private final ModelPart leftmidleg;
    private final ModelPart leftlowleg;
    private final ModelPart leftfoot;
    private final ModelPart lefttoe1;
    private final ModelPart lefttoe3;
    private final ModelPart lefttoe2;
    private final ModelPart leftclaw1;
    private final ModelPart leftclaw2;
    private final ModelPart leftclaw3;
    private final ModelPart ironleftlegarmor;
    private final ModelPart goldleftlegarmor;
    private final ModelPart diamondleftlegarmor;
    private final ModelPart rightupleg;
    private final ModelPart rightmidleg;
    private final ModelPart rightlowleg;
    private final ModelPart rightfoot;
    private final ModelPart righttoe1;
    private final ModelPart righttoe3;
    private final ModelPart righttoe2;
    private final ModelPart rightclaw1;
    private final ModelPart rightclaw2;
    private final ModelPart rightclaw3;
    private final ModelPart ironrightlegarmor;
    private final ModelPart goldrightlegarmor;
    private final ModelPart diamondrightlegarmor;
    private final ModelPart storage;
    private final ModelPart chestbelt;
    private final ModelPart stomachbelt;
    private final ModelPart ironchestarmor;
    private final ModelPart ironrightshoulderpad;
    private final ModelPart ironleftshoulderpad;
    private final ModelPart goldleftshoulder;
    private final ModelPart goldchestarmor;
    private final ModelPart goldrightshoulder;
    private final ModelPart diamondleftshoulder;
    private final ModelPart diamondrightshoulder;
    private final ModelPart diamondchestarmor;

    // State flags, matching your setLivingAnimations(…):
    private int armor;
    private boolean isRidden;
    private boolean isChested;
    private boolean isSaddled;
    private boolean flapwings;
    private boolean onAir;
    private boolean diving;
    private boolean isSitting;
    private boolean isGhost;
    private int openMouth;
    private float yOffset;
    private float transparency;

    public MoCModelWyvern(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        this.Tail                   = root.getChild("Tail");
        this.back1                  = Tail.getChild("back1");
        this.tail1                  = Tail.getChild("tail1");
        this.back2                  = tail1.getChild("back2");
        this.tail2                  = tail1.getChild("tail2");
        this.back3                  = tail2.getChild("back3");
        this.tail3                  = tail2.getChild("tail3");
        this.back4                  = tail3.getChild("back4");
        this.tail4                  = tail3.getChild("tail4");
        this.tail5                  = tail4.getChild("tail5");

        this.chest                  = root.getChild("chest");
        this.neckplate3             = root.getChild("neckplate3");
        this.neck3                  = root.getChild("neck3");

        this.MainHead               = root.getChild("MainHead");
        this.neck2                  = MainHead.getChild("neck2");
        this.neckplate2             = neck2.getChild("neckplate2");
        this.neck1                  = neck2.getChild("neck1");
        this.neckplate1             = neck1.getChild("neckplate1");
        this.head                   = neck1.getChild("head");
        this.snout                  = head.getChild("snout");
        this.headplate              = head.getChild("headplate");
        this.beak                   = snout.getChild("beak");
        this.righteyesock           = head.getChild("righteyesock");
        this.lefteyesock            = head.getChild("lefteyesock");
        this.Jaw                    = head.getChild("Jaw");
        this.leftupjaw              = head.getChild("leftupjaw");
        this.rightupjaw             = head.getChild("rightupjaw");
        this.mouthrod               = head.getChild("mouthrod");
        this.helmetstrap1           = head.getChild("helmetstrap1");
        this.helmetstrap2           = head.getChild("helmetstrap2");
        this.controlrope1           = head.getChild("mouthrod").getChild("controlrope1");
        this.controlrope2           = head.getChild("mouthrod").getChild("controlrope2");
        this.rightearskin           = head.getChild("rightearskin");
        this.leftearskin            = head.getChild("leftearskin");
        this.rightspine1            = rightearskin.getChild("rightspine1");
        this.rightspine2            = rightearskin.getChild("rightspine2");
        this.rightspine3            = rightearskin.getChild("rightspine3");
        this.leftspine1             = leftearskin.getChild("leftspine1");
        this.leftspine2             = leftearskin.getChild("leftspine2");
        this.leftspine3             = leftearskin.getChild("leftspine3");
        this.ironhelmethorn1        = leftspine1.getChild("ironhelmethorn1");
        this.ironhelmethorn2        = rightspine1.getChild("ironhelmethorn2");
        this.ironhelmet             = head.getChild("ironhelmet");
        this.ironhelmetsnout        = snout.getChild("ironhelmetsnout");
        this.goldhelmethorn1        = leftspine1.getChild("goldhelmethorn1");
        this.goldhelmethorn2        = rightspine1.getChild("goldhelmethorn2");
        this.goldhelmet             = head.getChild("goldhelmet");
        this.goldhelmetsnout        = snout.getChild("goldhelmetsnout");
        this.diamondhelmet          = head.getChild("diamondhelmet");
        this.diamondhelmethorn2     = rightspine1.getChild("diamondhelmethorn2");
        this.diamondhelmethorn1     = leftspine1.getChild("diamondhelmethorn1");
        this.diamondhelmetsnout     = snout.getChild("diamondhelmetsnout");

        this.torso                  = root.getChild("torso");
        this.saddle                 = root.getChild("saddle");
        this.rightshoulder          = root.getChild("rightshoulder");
        this.leftshoulder           = root.getChild("leftshoulder");

        this.LeftWing               = root.getChild("LeftWing");
        this.leftuparm              = LeftWing.getChild("leftuparm");
        this.leftlowarm             = leftuparm.getChild("leftlowarm");
        this.leftfing1a             = leftlowarm.getChild("leftfing1a");
        this.leftfing1b             = leftfing1a.getChild("leftfing1b");
        this.leftfing2a             = leftlowarm.getChild("leftfing2a");
        this.leftfing2b             = leftfing2a.getChild("leftfing2b");
        this.leftfing3a             = leftlowarm.getChild("leftfing3a");
        this.leftfing3b             = leftfing3a.getChild("leftfing3b");
        this.leftwingflap1          = leftfing1a.getChild("leftwingflap1");
        this.leftwingflap2          = leftfing2a.getChild("leftwingflap2");
        this.leftwingflap3          = leftfing3a.getChild("leftwingflap3");

        this.RightWing              = root.getChild("RightWing");
        this.rightuparm             = RightWing.getChild("rightuparm");
        this.rightlowarm            = rightuparm.getChild("rightlowarm");
        this.rightfing1a            = rightlowarm.getChild("rightfing1a");
        this.rightfing1b            = rightfing1a.getChild("rightfing1b");
        this.rightwingflap1         = rightfing1a.getChild("rightwingflap1");
        this.rightfing2a            = rightlowarm.getChild("rightfing2a");
        this.rightfing2b            = rightfing2a.getChild("rightfing2b");
        this.rightwingflap2         = rightfing2a.getChild("rightwingflap2");
        this.rightfing3a            = rightlowarm.getChild("rightfing3a");
        this.rightfing3b            = rightfing3a.getChild("rightfing3b");
        this.rightwingflap3         = rightfing3a.getChild("rightwingflap3");

        this.leftupleg              = root.getChild("leftupleg");
        this.leftmidleg             = leftupleg.getChild("leftmidleg");
        this.leftlowleg             = leftmidleg.getChild("leftlowleg");
        this.leftfoot               = leftlowleg.getChild("leftfoot");
        this.lefttoe1               = leftfoot.getChild("lefttoe1");
        this.lefttoe3               = leftfoot.getChild("lefttoe3");
        this.lefttoe2               = leftfoot.getChild("lefttoe2");
        this.leftclaw1              = lefttoe1.getChild("leftclaw1");
        this.leftclaw2              = lefttoe2.getChild("leftclaw2");
        this.leftclaw3              = lefttoe3.getChild("leftclaw3");
        this.ironleftlegarmor       = leftlowleg.getChild("ironleftlegarmor");
        this.goldleftlegarmor       = leftlowleg.getChild("goldleftlegarmor");
        this.diamondleftlegarmor    = leftlowleg.getChild("diamondleftlegarmor");

        this.rightupleg             = root.getChild("rightupleg");
        this.rightmidleg            = rightupleg.getChild("rightmidleg");
        this.rightlowleg            = rightmidleg.getChild("rightlowleg");
        this.rightfoot              = rightlowleg.getChild("rightfoot");
        this.righttoe1              = rightfoot.getChild("righttoe1");
        this.righttoe3              = rightfoot.getChild("righttoe3");
        this.righttoe2              = rightfoot.getChild("righttoe2");
        this.rightclaw1             = righttoe1.getChild("rightclaw1");
        this.rightclaw2             = righttoe2.getChild("rightclaw2");
        this.rightclaw3             = righttoe3.getChild("rightclaw3");
        this.ironrightlegarmor      = rightlowleg.getChild("ironrightlegarmor");
        this.goldrightlegarmor      = rightlowleg.getChild("goldrightlegarmor");
        this.diamondrightlegarmor   = rightlowleg.getChild("diamondrightlegarmor");

        this.storage                = root.getChild("storage");
        this.chestbelt              = root.getChild("chestbelt");
        this.stomachbelt            = root.getChild("stomachbelt");
        this.ironchestarmor         = root.getChild("ironchestarmor");
        this.ironrightshoulderpad   = root.getChild("ironrightshoulderpad");
        this.ironleftshoulderpad    = root.getChild("ironleftshoulderpad");
        this.goldleftshoulder       = root.getChild("goldleftshoulder");
        this.goldchestarmor         = root.getChild("goldchestarmor");
        this.goldrightshoulder      = root.getChild("goldrightshoulder");
        this.diamondleftshoulder    = root.getChild("diamondleftshoulder");
        this.diamondrightshoulder   = root.getChild("diamondrightshoulder");
        this.diamondchestarmor      = root.getChild("diamondchestarmor");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        //
        // ─── TAIL HIERARCHY ───────────────────────────────────────────────────────────
        //
        PartDefinition Tail = root.addOrReplaceChild("Tail",
                CubeListBuilder.create(),
                PartPose.offset(0F, 0F, 0F)
        );
        // back1
        Tail.addOrReplaceChild("back1",
                CubeListBuilder.create()
                        .texOffs(92, 0)
                        .addBox(-3F, -2F, -12F, 6, 2, 12),
                PartPose.offset(0F, 0F, 0F)
        );
        // tail1
        PartDefinition tail1 = Tail.addOrReplaceChild("tail1",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-4F, 0F, 0F, 8, 8, 10),
                PartPose.offset(0F, 0F, 0F)
        );
        // back2
        tail1.addOrReplaceChild("back2",
                CubeListBuilder.create()
                        .texOffs(100, 14)
                        .addBox(-2F, -2F, 0F, 4, 2, 10),
                PartPose.offset(0F, 0F, 0F)
        );
        // tail2
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-3F, 0F, 0F, 6, 6, 9),
                PartPose.offset(0F, 0F, 10F)
        );
        // back3
        tail2.addOrReplaceChild("back3",
                CubeListBuilder.create()
                        .texOffs(104, 26)
                        .addBox(-1.5F, -2F, 0F, 3, 2, 9),
                PartPose.offset(0F, 0F, 0F)
        );
        // tail3
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create()
                        .texOffs(0, 55)
                        .addBox(-2F, 0F, 0F, 4, 5, 8),
                PartPose.offset(0F, 0F, 8F)
        );
        // back4
        tail3.addOrReplaceChild("back4",
                CubeListBuilder.create()
                        .texOffs(108, 37)
                        .addBox(-1F, -2F, 0F, 2, 2, 8),
                PartPose.offset(0F, 0F, 0F)
        );
        // tail4
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create()
                        .texOffs(0, 68)
                        .addBox(-1F, 0F, 0F, 2, 5, 7),
                PartPose.offset(0F, -1F, 7F)
        );
        // tail5
        tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create()
                        .texOffs(0, 80)
                        .addBox(-0.5F, 0F, 0F, 1, 3, 7),
                PartPose.offset(0F, 1F, 6F)
        );

        //
        // ─── CHEST & NECK & HEAD ────────────────────────────────────────────────────────
        //
        // chest
        root.addOrReplaceChild("chest",
                CubeListBuilder.create()
                        .texOffs(44, 0)
                        .addBox(-4.5F, 2.7F, -13F, 9, 10, 4),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2602503F, 0F, 0F)
        );

        // neckplate3
        PartDefinition neckplate3 = root.addOrReplaceChild("neckplate3",
                CubeListBuilder.create()
                        .texOffs(112, 64)
                        .addBox(-2F, -2F, -2F, 4, 2, 4),
                PartPose.offsetAndRotation(0F, 0F, -12F, -0.669215F, 0F, 0F)
        );
        // neck3
        PartDefinition neck3 = root.addOrReplaceChild("neck3",
                CubeListBuilder.create()
                        .texOffs(100, 113)
                        .addBox(-3F, 0F, -2F, 6, 7, 8),
                PartPose.offsetAndRotation(0F, 0F, -12F, -0.669215F, 0F, 0F)
        );

        // MainHead (root for all head pieces)
        PartDefinition MainHead = root.addOrReplaceChild("MainHead",
                CubeListBuilder.create(),
                PartPose.offset(0F, 3F, -15F)
        );

        // neck2 (child of MainHead)
        PartDefinition neck2 = MainHead.addOrReplaceChild("neck2",
                CubeListBuilder.create()
                        .texOffs(102, 99)
                        .addBox(-2.5F, -3F, -8F, 5, 6, 8),
                PartPose.offset(0F, 0F, 0F)
        );
        // neckplate2 (child of neck2)
        neck2.addOrReplaceChild("neckplate2",
                CubeListBuilder.create()
                        .texOffs(106, 54)
                        .addBox(-1.5F, -2F, -8F, 3, 2, 8),
                PartPose.offset(0F, -3F, 0F)
        );
        // neck1 (child of neck2)
        PartDefinition neck1 = neck2.addOrReplaceChild("neck1",
                CubeListBuilder.create()
                        .texOffs(104, 85)
                        .addBox(-2F, -3F, -8F, 4, 6, 8),
                PartPose.offset(0F, -0.5F, -5.5F)
        );
        // neckplate1 (child of neck1)
        neck1.addOrReplaceChild("neckplate1",
                CubeListBuilder.create()
                        .texOffs(80, 108)
                        .addBox(-1F, -2F, -8F, 2, 2, 8),
                PartPose.offset(0F, -3F, 0F)
        );

        // head (child of neck1)
        PartDefinition head = neck1.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(98, 70)
                        .addBox(-3.5F, -3.5F, -8F, 7, 7, 8),
                PartPose.offset(0F, 0F, -7F)
        );
        // snout (child of head)
        head.addOrReplaceChild("snout",
                CubeListBuilder.create()
                        .texOffs(72, 70)
                        .addBox(-2F, -1.5F, -9F, 4, 3, 9),
                PartPose.offsetAndRotation(0F, -1.5F, -8F, 2F / 57.29578F, 0F, 0F)
        );
        // headplate (child of head)
        head.addOrReplaceChild("headplate",
                CubeListBuilder.create()
                        .texOffs(80, 118)
                        .addBox(-1F, -1F, -4F, 2, 2, 8),
                PartPose.offsetAndRotation(0F, -3F, -1F, 10F / 57.29578F, 0F, 0F)
        );
        // beak (child of snout)
        head.getChild("snout").addOrReplaceChild("beak",
                CubeListBuilder.create()
                        .texOffs(60, 85)
                        .addBox(-1.5F, -2.5F, -1.5F, 3, 5, 3),
                PartPose.offsetAndRotation(0F, 0.8F, -8.0F, -6F / 57.29578F, 45F / 57.29578F, -6F / 57.29578F)
        );
        // righteyesock (child of head)
        head.addOrReplaceChild("righteyesock",
                CubeListBuilder.create()
                        .texOffs(70, 108)
                        .addBox(0F, 0F, 0F, 1, 2, 4),
                PartPose.offset(-3.5F, -2.5F, -8F)
        );
        // lefteyesock (child of head)
        head.addOrReplaceChild("lefteyesock",
                CubeListBuilder.create()
                        .texOffs(70, 114)
                        .addBox(0F, 0F, 0F, 1, 2, 4),
                PartPose.offset(2.5F, -2.5F, -8F)
        );
        // Jaw (child of head)
        head.addOrReplaceChild("Jaw",
                CubeListBuilder.create()
                        .texOffs(72, 82)
                        .addBox(-2F, -1F, -9F, 4, 2, 9),
                PartPose.offsetAndRotation(0F, 2.5F, -7.5F, -10F / 57.29578F, 0F, 0F)
        );
        // leftupjaw (child of head)
        head.addOrReplaceChild("leftupjaw",
                CubeListBuilder.create()
                        .texOffs(42, 93)
                        .addBox(-1F, -1F, -6.5F, 2, 2, 13),
                PartPose.offsetAndRotation(2F, 0F, -10.5F, -10F / 57.29578F, 10F / 57.29578F, 0F)
        );
        // rightupjaw (child of head)
        head.addOrReplaceChild("rightupjaw",
                CubeListBuilder.create()
                        .texOffs(72, 93)
                        .addBox(-1F, -1F, -6.5F, 2, 2, 13),
                PartPose.offsetAndRotation(-2F, 0F, -10.5F, -10F / 57.29578F, -10F / 57.29578F, 0F)
        );
        // mouthrod (child of head)
        head.addOrReplaceChild("mouthrod",
                CubeListBuilder.create()
                        .texOffs(104, 50)
                        .addBox(-5F, -1F, -1F, 10, 2, 2),
                PartPose.offset(0F, 1F, -8F)
        );
        // helmetstrap1 (child of head)
        head.addOrReplaceChild("helmetstrap1",
                CubeListBuilder.create()
                        .texOffs(32, 146)
                        .addBox(-4F, -2F, 0F, 8, 4, 1),
                PartPose.offset(0F, 2F, -7.5F)
        );
        // helmetstrap2 (child of head)
        head.addOrReplaceChild("helmetstrap2",
                CubeListBuilder.create()
                        .texOffs(32, 141)
                        .addBox(-4F, -2F, 0F, 8, 4, 1),
                PartPose.offset(0F, 2F, -3.5F)
        );

        // controlrope1 (child of mouthrod)
        head.getChild("mouthrod").addOrReplaceChild("controlrope1",
                CubeListBuilder.create()
                        .texOffs(66, 43)
                        .addBox(0F, -2F, 0F, 0, 4, 23),
                PartPose.offset(4.5F, 1F, 0F)
        );
        // controlrope2
        head.getChild("mouthrod").addOrReplaceChild("controlrope2",
                CubeListBuilder.create()
                        .texOffs(66, 43)
                        .addBox(0F, -2F, 0F, 0, 4, 23),
                PartPose.offset(-4.5F, 1F, 0F)
        );

        // rightearskin (child of head)
        PartDefinition rightearskin = head.addOrReplaceChild("rightearskin",
                CubeListBuilder.create()
                        .texOffs(112, 201)
                        .addBox(0F, -4F, 0F, 0, 8, 8),
                PartPose.offset(-3F, -0.5F, 0F)
        );
        // leftearskin
        PartDefinition leftearskin = head.addOrReplaceChild("leftearskin",
                CubeListBuilder.create()
                        .texOffs(96, 201)
                        .addBox(0F, -4F, 0F, 0, 8, 8),
                PartPose.offset(3F, -0.5F, 0F)
        );
        // rightspine1 (child of rightearskin)
        rightearskin.addOrReplaceChild("rightspine1",
                CubeListBuilder.create()
                        .texOffs(50, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offsetAndRotation(0F, -2F, 0F, 15F / 57.29578F, 0F, 0F)
        );
        // rightspine2 (child of rightearskin)
        rightearskin.addOrReplaceChild("rightspine2",
                CubeListBuilder.create()
                        .texOffs(50, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offset(0F, 0F, 0F)
        );
        // rightspine3 (child of rightearskin)
        rightearskin.addOrReplaceChild("rightspine3",
                CubeListBuilder.create()
                        .texOffs(50, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offsetAndRotation(0F, 2F, 0F, -15F / 57.29578F, 0F, 0F)
        );
        // leftspine1 (child of leftearskin)
        leftearskin.addOrReplaceChild("leftspine1",
                CubeListBuilder.create()
                        .texOffs(68, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offsetAndRotation(0F, -2F, 0F, 15F / 57.29578F, 0F, 0F)
        );
        // leftspine2 (child of leftearskin)
        leftearskin.addOrReplaceChild("leftspine2",
                CubeListBuilder.create()
                        .texOffs(68, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offset(0F, 0F, 0F)
        );
        // leftspine3 (child of leftearskin)
        leftearskin.addOrReplaceChild("leftspine3",
                CubeListBuilder.create()
                        .texOffs(68, 141)
                        .addBox(-0.5F, -1F, 0F, 1, 2, 8),
                PartPose.offsetAndRotation(0F, 2F, 0F, -15F / 57.29578F, 0F, 0F)
        );

        // ironhelmethorn1 (child of leftspine1)
        leftearskin.getChild("leftspine1").addOrReplaceChild("ironhelmethorn1",
                CubeListBuilder.create()
                        .texOffs(106, 139)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(-0.5F, 0F, 0.1F)
        );
        // ironhelmethorn2 (child of rightspine1)
        rightearskin.getChild("rightspine1").addOrReplaceChild("ironhelmethorn2",
                CubeListBuilder.create()
                        .texOffs(106, 128)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(0.5F, 0F, 0.1F)
        );
        // ironhelmet (child of head)
        head.addOrReplaceChild("ironhelmet",
                CubeListBuilder.create()
                        .texOffs(32, 128)
                        .addBox(-4F, -4F, -9F, 8, 4, 9),
                PartPose.offset(0F, 0F, 0F)
        );
        // ironhelmetsnout (child of snout)
        head.getChild("snout").addOrReplaceChild("ironhelmetsnout",
                CubeListBuilder.create()
                        .texOffs(0, 144)
                        .addBox(-2.5F, -2F, -7F, 5, 2, 7),
                PartPose.offset(0F, 0F, -1F)
        );
        // goldhelmethorn1 (child of leftspine1)
        leftearskin.getChild("leftspine1").addOrReplaceChild("goldhelmethorn1",
                CubeListBuilder.create()
                        .texOffs(106, 161)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(-0.5F, 0F, 0.1F)
        );
        // goldhelmethorn2 (child of rightspine1)
        rightearskin.getChild("rightspine1").addOrReplaceChild("goldhelmethorn2",
                CubeListBuilder.create()
                        .texOffs(106, 150)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(0.5F, 0F, 0.1F)
        );
        // goldhelmet (child of head)
        head.addOrReplaceChild("goldhelmet",
                CubeListBuilder.create()
                        .texOffs(94, 226)
                        .addBox(-4F, -4F, -9F, 8, 4, 9),
                PartPose.offset(0F, 0F, 0F)
        );
        // goldhelmetsnout (child of snout)
        head.getChild("snout").addOrReplaceChild("goldhelmetsnout",
                CubeListBuilder.create()
                        .texOffs(71, 235)
                        .addBox(-2.5F, -2F, -7F, 5, 2, 7),
                PartPose.offset(0F, 0F, -1F)
        );
        // diamondhelmet (child of head)
        head.addOrReplaceChild("diamondhelmet",
                CubeListBuilder.create()
                        .texOffs(23, 226)
                        .addBox(-4F, -4F, -9F, 8, 4, 9),
                PartPose.offset(0F, 0F, 0F)
        );
        // diamondhelmethorn2 (child of rightspine1)
        rightearskin.getChild("rightspine1").addOrReplaceChild("diamondhelmethorn2",
                CubeListBuilder.create()
                        .texOffs(49, 234)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(0.5F, 0F, 0.1F)
        );
        // diamondhelmethorn1 (child of leftspine1)
        leftearskin.getChild("leftspine1").addOrReplaceChild("diamondhelmethorn1",
                CubeListBuilder.create()
                        .texOffs(49, 245)
                        .addBox(-1.5F, -1.5F, 0F, 3, 3, 8),
                PartPose.offset(-0.5F, 0F, 0.1F)
        );
        // diamondhelmetsnout (child of snout)
        head.getChild("snout").addOrReplaceChild("diamondhelmetsnout",
                CubeListBuilder.create()
                        .texOffs(0, 235)
                        .addBox(-2.5F, -2F, -7F, 5, 2, 7),
                PartPose.offset(0F, 0F, -1F)
        );

        // torso
        root.addOrReplaceChild("torso",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5F, 0F, -12F, 10, 10, 12),
                PartPose.offset(0F, 0F, 0F)
        );
        // saddle
        root.addOrReplaceChild("saddle",
                CubeListBuilder.create()
                        .texOffs(38, 70)
                        .addBox(-3.5F, -2.5F, -8F, 7, 3, 10),
                PartPose.offset(0F, 0F, 0F)
        );
        // rightshoulder
        root.addOrReplaceChild("rightshoulder",
                CubeListBuilder.create()
                        .texOffs(42, 83)
                        .addBox(-6F, 1F, -12.5F, 4, 5, 5),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // leftshoulder
        root.addOrReplaceChild("leftshoulder",
                CubeListBuilder.create()
                        .texOffs(24, 83)
                        .addBox(2F, 1F, -12.5F, 4, 5, 5),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );

        //
        // ─── LEFT WING ─────────────────────────────────────────────────────────────────
        //
        PartDefinition LeftWing = root.addOrReplaceChild("LeftWing",
                CubeListBuilder.create(),
                PartPose.offset(4F, 1F, -11F)
        );
        // leftuparm
        PartDefinition leftuparm = LeftWing.addOrReplaceChild("leftuparm",
                CubeListBuilder.create()
                        .texOffs(44, 14)
                        .addBox(0F, -2F, -2F, 10, 4, 4),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, -10F / 57.29578F, 0F)
        );
        // leftlowarm (child of leftuparm)
        PartDefinition leftlowarm = leftuparm.addOrReplaceChild("leftlowarm",
                CubeListBuilder.create()
                        .texOffs(72, 14)
                        .addBox(0F, -2F, -2F, 10, 4, 4),
                PartPose.offsetAndRotation(9F, 0F, 0F, 0F, 10F / 57.29578F, 0F)
        );
        // leftfing1a (child of leftlowarm)
        PartDefinition leftfing1a = leftlowarm.addOrReplaceChild("leftfing1a",
                CubeListBuilder.create()
                        .texOffs(52, 30)
                        .addBox(0F, 0F, -1F, 2, 15, 2),
                PartPose.offsetAndRotation(9F, 1F, 0F, 90F / 57.29578F, 70F / 57.29578F, 0F)
        );
        // leftfing1b (child of leftfing1a)
        leftfing1a.addOrReplaceChild("leftfing1b",
                CubeListBuilder.create()
                        .texOffs(52, 47)
                        .addBox(0F, 0F, -1F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, 35F / 57.29578F)
        );
        // leftfing2a (child of leftlowarm)
        PartDefinition leftfing2a = leftlowarm.addOrReplaceChild("leftfing2a",
                CubeListBuilder.create()
                        .texOffs(44, 30)
                        .addBox(-1F, 0F, 0F, 2, 15, 2),
                PartPose.offsetAndRotation(9F, 1F, 0F, 90F / 57.29578F, 35F / 57.29578F, 0F)
        );
        // leftfing2b
        leftfing2a.addOrReplaceChild("leftfing2b",
                CubeListBuilder.create()
                        .texOffs(44, 47)
                        .addBox(-1F, 0F, 0F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, 30F / 57.29578F)
        );
        // leftfing3a (child of leftlowarm)
        PartDefinition leftfing3a = leftlowarm.addOrReplaceChild("leftfing3a",
                CubeListBuilder.create()
                        .texOffs(36, 30)
                        .addBox(-1F, 0F, 1F, 2, 15, 2),
                PartPose.offsetAndRotation(9F, 1F, 0F, 90F / 57.29578F, -5F / 57.29578F, 0F)
        );
        // leftfing3b
        leftfing3a.addOrReplaceChild("leftfing3b",
                CubeListBuilder.create()
                        .texOffs(36, 47)
                        .addBox(-1F, 0F, 1F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, 30F / 57.29578F)
        );
        // leftwingflap1 (child of leftfing1a)
        leftfing1a.addOrReplaceChild("leftwingflap1",
                CubeListBuilder.create()
                        .texOffs(74, 153)
                        .addBox(3.5F, -3F, 0.95F, 14, 24, 0),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 70F / 57.29578F)
        );
        // leftwingflap2 (child of leftfing2a)
        leftfing2a.addOrReplaceChild("leftwingflap2",
                CubeListBuilder.create()
                        .texOffs(36, 153)
                        .addBox(-7F, 1.05F, 1.05F, 19, 24, 0),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 40F / 57.29578F)
        );
        // leftwingflap3 (child of leftfing3a)
        leftfing3a.addOrReplaceChild("leftwingflap3",
                CubeListBuilder.create()
                        .texOffs(0, 153)
                        .addBox(-17.5F, 1F, 1.1F, 18, 24, 0),
                PartPose.offset(0F, 0F, 0F)
        );

        //
        // ─── RIGHT WING ────────────────────────────────────────────────────────────────
        //
        PartDefinition RightWing = root.addOrReplaceChild("RightWing",
                CubeListBuilder.create(),
                PartPose.offset(-4F, 1F, -11F)
        );
        // rightuparm
        PartDefinition rightuparm = RightWing.addOrReplaceChild("rightuparm",
                CubeListBuilder.create()
                        .texOffs(44, 22)
                        .addBox(-10F, -2F, -2F, 10, 4, 4),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 10F / 57.29578F, 0F)
        );
        // rightlowarm
        PartDefinition rightlowarm = rightuparm.addOrReplaceChild("rightlowarm",
                CubeListBuilder.create()
                        .texOffs(72, 22)
                        .addBox(-10F, -2F, -2F, 10, 4, 4),
                PartPose.offsetAndRotation(-9F, 0F, 0F, 0F, -10F / 57.29578F, 0F)
        );
        // rightfing1a
        PartDefinition rightfing1a = rightlowarm.addOrReplaceChild("rightfing1a",
                CubeListBuilder.create()
                        .texOffs(36, 30)
                        .addBox(-1F, 0F, -1F, 2, 15, 2),
                PartPose.offsetAndRotation(-9F, 1F, -1F, 90F / 57.29578F, -70F / 57.29578F, 0F)
        );
        // rightfing1b
        rightfing1a.addOrReplaceChild("rightfing1b",
                CubeListBuilder.create()
                        .texOffs(36, 47)
                        .addBox(-1F, 0F, -1F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, -35F / 57.29578F)
        );
        // rightwingflap1
        rightfing1a.addOrReplaceChild("rightwingflap1",
                CubeListBuilder.create()
                        .texOffs(74, 177)
                        .addBox(-17.5F, -3F, 0.95F, 14, 24, 0),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, -70F / 57.29578F)
        );
        // rightfing2a
        PartDefinition rightfing2a = rightlowarm.addOrReplaceChild("rightfing2a",
                CubeListBuilder.create()
                        .texOffs(44, 30)
                        .addBox(-1F, 0F, 0F, 2, 15, 2),
                PartPose.offsetAndRotation(-9F, 1F, 0F, 90F / 57.29578F, -35F / 57.29578F, 0F)
        );
        // rightfing2b
        rightfing2a.addOrReplaceChild("rightfing2b",
                CubeListBuilder.create()
                        .texOffs(44, 47)
                        .addBox(-1F, 0F, 0F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, -30F / 57.29578F)
        );
        // rightwingflap2
        rightfing2a.addOrReplaceChild("rightwingflap2",
                CubeListBuilder.create()
                        .texOffs(36, 177)
                        .addBox(-19F, 1.05F, 1.05F, 19, 24, 0),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, -40F / 57.29578F)
        );
        // rightfing3a
        PartDefinition rightfing3a = rightlowarm.addOrReplaceChild("rightfing3a",
                CubeListBuilder.create()
                        .texOffs(52, 30)
                        .addBox(-1F, 0F, 1F, 2, 15, 2),
                PartPose.offsetAndRotation(-9F, 1F, 0F, 90F / 57.29578F, 5F / 57.29578F, 0F)
        );
        // rightfing3b
        rightfing3a.addOrReplaceChild("rightfing3b",
                CubeListBuilder.create()
                        .texOffs(52, 47)
                        .addBox(-1F, 0F, 1F, 2, 10, 2),
                PartPose.offsetAndRotation(0F, 14F, 0F, 0F, 0F, -30F / 57.29578F)
        );
        // rightwingflap3
        rightfing3a.addOrReplaceChild("rightwingflap3",
                CubeListBuilder.create()
                        .texOffs(0, 177)
                        .addBox(-0.5F, 1F, 1.1F, 18, 24, 0),
                PartPose.offset(0F, 0F, 0F)
        );

        //
        // ─── LEFT LEG ──────────────────────────────────────────────────────────────────
        //
        PartDefinition leftupleg = root.addOrReplaceChild("leftupleg",
                CubeListBuilder.create()
                        .texOffs(0, 111)
                        .addBox(-2F, -3F, -3F, 4, 10, 7),
                PartPose.offsetAndRotation(5F, 6F, -5F, -25F / 57.29578F, 0F, 0F)
        );
        // leftmidleg
        PartDefinition leftmidleg = leftupleg.addOrReplaceChild("leftmidleg",
                CubeListBuilder.create()
                        .texOffs(0, 102)
                        .addBox(-1.5F, -2F, 0F, 3, 4, 5),
                PartPose.offset(0F, 5F, 4F)
        );
        // leftlowleg
        PartDefinition leftlowleg = leftmidleg.addOrReplaceChild("leftlowleg",
                CubeListBuilder.create()
                        .texOffs(0, 91)
                        .addBox(-1.5F, 0F, -1.5F, 3, 8, 3),
                PartPose.offset(0F, 2F, 3.5F)
        );
        // leftfoot
        PartDefinition leftfoot = leftlowleg.addOrReplaceChild("leftfoot",
                CubeListBuilder.create()
                        .texOffs(44, 121)
                        .addBox(-2F, -1F, -3F, 4, 3, 4),
                PartPose.offsetAndRotation(0F, 7F, 0.5F, 25F / 57.29578F, 0F, 0F)
        );
        // lefttoe1
        leftfoot.addOrReplaceChild("lefttoe1",
                CubeListBuilder.create()
                        .texOffs(96, 35)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3),
                PartPose.offset(-1.5F, 1F, -3F)
        );
        // lefttoe3
        leftfoot.addOrReplaceChild("lefttoe3",
                CubeListBuilder.create()
                        .texOffs(96, 30)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3),
                PartPose.offset(1.5F, 1F, -3F)
        );
        // lefttoe2
        leftfoot.addOrReplaceChild("lefttoe2",
                CubeListBuilder.create()
                        .texOffs(84, 30)
                        .addBox(-1F, -1.5F, -4F, 2, 3, 4),
                PartPose.offset(0F, 0.5F, -3F)
        );
        // leftclaw1 (child of lefttoe1)
        leftfoot.getChild("lefttoe1").addOrReplaceChild("leftclaw1",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(0.5F, -0.5F, -2.5F, -25F / 57.29578F, 0F, 0F)
        );
        // leftclaw2 (child of lefttoe2)
        leftfoot.getChild("lefttoe2").addOrReplaceChild("leftclaw2",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, -1F, -3.5F, -25F / 57.29578F, 0F, 0F)
        );
        // leftclaw3 (child of lefttoe3)
        leftfoot.getChild("lefttoe3").addOrReplaceChild("leftclaw3",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(-0.5F, -0.5F, -2.5F, -25F / 57.29578F, 0F, 0F)
        );
        // ironleftlegarmor (child of leftlowleg)
        leftlowleg.addOrReplaceChild("ironleftlegarmor",
                CubeListBuilder.create()
                        .texOffs(39, 97)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );
        // goldleftlegarmor
        leftlowleg.addOrReplaceChild("goldleftlegarmor",
                CubeListBuilder.create()
                        .texOffs(112, 181)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );
        // diamondleftlegarmor
        leftlowleg.addOrReplaceChild("diamondleftlegarmor",
                CubeListBuilder.create()
                        .texOffs(43, 215)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );

        //
        // ─── RIGHT LEG ─────────────────────────────────────────────────────────────────
        //
        PartDefinition rightupleg = root.addOrReplaceChild("rightupleg",
                CubeListBuilder.create()
                        .texOffs(0, 111)
                        .addBox(-2F, -3F, -3F, 4, 10, 7),
                PartPose.offsetAndRotation(-5F, 6F, -5F, -25F / 57.29578F, 0F, 0F)
        );
        // rightmidleg
        PartDefinition rightmidleg = rightupleg.addOrReplaceChild("rightmidleg",
                CubeListBuilder.create()
                        .texOffs(0, 102)
                        .addBox(-1.5F, -2F, 0F, 3, 4, 5),
                PartPose.offset(0F, 5F, 4F)
        );
        // rightlowleg
        PartDefinition rightlowleg = rightmidleg.addOrReplaceChild("rightlowleg",
                CubeListBuilder.create()
                        .texOffs(0, 91)
                        .addBox(-1.5F, 0F, -1.5F, 3, 8, 3),
                PartPose.offset(0F, 2F, 3.5F)
        );
        // rightfoot
        PartDefinition rightfoot = rightlowleg.addOrReplaceChild("rightfoot",
                CubeListBuilder.create()
                        .texOffs(44, 121)
                        .addBox(-2F, -1F, -3F, 4, 3, 4),
                PartPose.offsetAndRotation(0F, 7F, 0.5F, 25F / 57.29578F, 0F, 0F)
        );
        // righttoe1
        rightfoot.addOrReplaceChild("righttoe1",
                CubeListBuilder.create()
                        .texOffs(96, 35)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3),
                PartPose.offset(-1.5F, 1F, -3F)
        );
        // righttoe3
        rightfoot.addOrReplaceChild("righttoe3",
                CubeListBuilder.create()
                        .texOffs(96, 30)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3),
                PartPose.offset(1.5F, 1F, -3F)
        );
        // righttoe2
        rightfoot.addOrReplaceChild("righttoe2",
                CubeListBuilder.create()
                        .texOffs(84, 30)
                        .addBox(-1F, -1.5F, -4F, 2, 3, 4),
                PartPose.offset(0F, 0.5F, -3F)
        );
        // rightclaw1
        rightfoot.getChild("righttoe1").addOrReplaceChild("rightclaw1",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(0.5F, -0.5F, -2.5F, -25F / 57.29578F, 0F, 0F)
        );
        // rightclaw2
        rightfoot.getChild("righttoe2").addOrReplaceChild("rightclaw2",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, -1F, -3.5F, -25F / 57.29578F, 0F, 0F)
        );
        // rightclaw3
        rightfoot.getChild("righttoe3").addOrReplaceChild("rightclaw3",
                CubeListBuilder.create()
                        .texOffs(100, 26)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(-0.5F, -0.5F, -2.5F, -25F / 57.29578F, 0F, 0F)
        );
        // ironrightlegarmor
        rightlowleg.addOrReplaceChild("ironrightlegarmor",
                CubeListBuilder.create()
                        .texOffs(39, 97)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );
        // goldrightlegarmor
        rightlowleg.addOrReplaceChild("goldrightlegarmor",
                CubeListBuilder.create()
                        .texOffs(112, 181)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );
        // diamondrightlegarmor
        rightlowleg.addOrReplaceChild("diamondrightlegarmor",
                CubeListBuilder.create()
                        .texOffs(43, 215)
                        .addBox(-2F, -2.5F, -2F, 4, 5, 4),
                PartPose.offset(0F, 2.5F, 0F)
        );

        //
        // ─── STORAGE & BELTS & CHEST ARMOR & SHOULDERS ────────────────────────────────────
        //
        root.addOrReplaceChild("storage",
                CubeListBuilder.create()
                        .texOffs(28, 59)
                        .addBox(-5F, -4.5F, 1.5F, 10, 5, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2268928F, 0F, 0F)
        );
        // chestbelt
        root.addOrReplaceChild("chestbelt",
                CubeListBuilder.create()
                        .texOffs(0, 201)
                        .addBox(-5.5F, -0.5F, -9F, 11, 11, 2),
                PartPose.offset(0F, 0F, 0F)
        );
        // stomachbelt
        root.addOrReplaceChild("stomachbelt",
                CubeListBuilder.create()
                        .texOffs(0, 201)
                        .addBox(-5.5F, -0.5F, -3F, 11, 11, 2),
                PartPose.offset(0F, 0F, 0F)
        );
        // ironchestarmor
        root.addOrReplaceChild("ironchestarmor",
                CubeListBuilder.create()
                        .texOffs(0, 128)
                        .addBox(-5.5F, 2.2F, -13.5F, 11, 11, 5),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2602503F, 0F, 0F)
        );
        // ironrightshoulderpad
        root.addOrReplaceChild("ironrightshoulderpad",
                CubeListBuilder.create()
                        .texOffs(74, 201)
                        .addBox(-6.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // ironleftshoulderpad
        root.addOrReplaceChild("ironleftshoulderpad",
                CubeListBuilder.create()
                        .texOffs(26, 201)
                        .addBox(1.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // goldleftshoulder
        root.addOrReplaceChild("goldleftshoulder",
                CubeListBuilder.create()
                        .texOffs(71, 244)
                        .addBox(1.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // goldchestarmor
        root.addOrReplaceChild("goldchestarmor",
                CubeListBuilder.create()
                        .texOffs(71, 219)
                        .addBox(-5.5F, 2.2F, -13.5F, 11, 11, 5),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2602503F, 0F, 0F)
        );
        // goldrightshoulder
        root.addOrReplaceChild("goldrightshoulder",
                CubeListBuilder.create()
                        .texOffs(93, 244)
                        .addBox(-6.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // diamondleftshoulder
        root.addOrReplaceChild("diamondleftshoulder",
                CubeListBuilder.create()
                        .texOffs(0, 244)
                        .addBox(1.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // diamondrightshoulder
        root.addOrReplaceChild("diamondrightshoulder",
                CubeListBuilder.create()
                        .texOffs(22, 244)
                        .addBox(-6.5F, 0.5F, -13F, 5, 6, 6),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2617994F, 0F, 0F)
        );
        // diamondchestarmor
        root.addOrReplaceChild("diamondchestarmor",
                CubeListBuilder.create()
                        .texOffs(0, 219)
                        .addBox(-5.5F, 2.2F, -13.5F, 11, 11, 5),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.2602503F, 0F, 0F)
        );

        // Finally return a  LayerDefinition telling Minecraft “my texture is 128×256”:
        return LayerDefinition.create(mesh, 128, 256);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        this.armor = entity.getArmorType();
        this.isRidden = (entity.isVehicle() && entity.getControllingPassenger() instanceof Player);
        this.isChested = entity.getIsChested();
        this.isSaddled = entity.getIsRideable();
        this.flapwings = (entity.wingFlapCounter != 0);
        this.onAir = entity.isOnAir() || entity.getIsFlying();
        this.diving = (entity.diveCounter != 0);
        this.isSitting = entity.getIsSitting();
        this.isGhost = entity.getIsGhost();
        this.openMouth = entity.mouthCounter;
        this.yOffset = entity.getAdjustedYOffset();
        this.transparency = entity.tFloat();
        //
        // Copy all the “setRotationAngles(...)” logic here, replacing
        // ModelRenderer.rotateAngleX/Y/Z with ModelPart.xRot/yRot/zRot,
        // and ModelRenderer.rotationPointX/Y/Z with ModelPart.setPos(x,y,z) if needed.
        //

        // 1) Leg swings (exactly as before):
        float RLegXRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;

        // 2) Constrain netHeadYaw to ±60°
        netHeadYaw = MoCTools.realAngle(netHeadYaw);
        float maxYaw = 60F;
        if (netHeadYaw > maxYaw)  netHeadYaw = maxYaw;
        if (netHeadYaw < -maxYaw) netHeadYaw = -maxYaw;

        // 3) Neck rotations from headPitch / netHeadYaw:
        this.neck2.xRot = -66F / 57.29578F + (headPitch * (1F/3F) / 57.29578F);
        this.neck1.xRot = 30F  / 57.29578F + (headPitch * (2F/3F) / 57.29578F);
        this.head.xRot  = 45F  / 57.29578F;

        this.neck2.yRot = (netHeadYaw * (2F/3F)) / 57.29578F;
        this.neck1.yRot = (netHeadYaw * (1F/3F)) / 57.29578F;
        this.head.yRot  = 0F;
        this.head.zRot  = 0F;

        // 4) If ridden, lock neck yaw, and if not onAir, let neck pitch follow leg:
        if (isRidden) {
            this.neck1.yRot = 0F;
            this.neck2.yRot = 0F;
            if (onAir) {
                this.neck1.xRot = 0F;
                this.neck2.xRot = 0F;
            } else {
                this.neck2.xRot = -66F / 57.29578F + (RLegXRot * (1F/60F));
                this.neck1.xRot = 30F  / 57.29578F + (RLegXRot * (2F/60F));
            }
        }

        // 5) Tail wagging (wave through tail1→tail5):
        float TailXRot = Mth.cos(limbSwing * 0.4F) * 0.2F * limbSwingAmount;
        this.tail1.xRot = (-19F / 57.29578F);
        this.tail2.xRot = (-16F / 57.29578F);
        this.tail3.xRot = (  7F / 57.29578F);
        this.tail4.xRot = ( 11F / 57.29578F);
        this.tail5.xRot = (  8F / 57.29578F);

        float t = limbSwing / 2F;
        float A = 0.15F;
        float w = 0.9F;
        float k = 0.6F;
        int i = 0;
        float tailLat;
        tailLat = A * Mth.sin(w * t - k * i++);
        this.tail1.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * i++);
        this.tail2.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * i++);
        this.tail3.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * i++);
        this.tail4.yRot = tailLat;
        tailLat = A * Mth.sin(w * t - k * i++);
        this.tail5.yRot = tailLat;

        // 6) Wing flapping or cruising:
        float WingSpread;
        if (flapwings && !isGhost) {
            WingSpread = Mth.cos((ageInTicks * 0.3F) + (float)Math.PI) * 1.2F;
        } else {
            WingSpread = Mth.cos(limbSwing * 0.5F) * 0.1F;
        }

        if (onAir || isGhost) {
            float speedMov = limbSwingAmount * 0.5F;
            if (isGhost) speedMov = 0.5F;

            // Left wing onAir:
            this.leftuparm.zRot    = (WingSpread * 2F / 3F);
            this.rightuparm.zRot   = (-WingSpread * 2F / 3F);
            this.leftlowarm.zRot   = (WingSpread * 0.1F);
            this.leftfing1a.zRot   = (WingSpread);
            this.leftfing2a.zRot   = (WingSpread * 0.8F);
            this.rightlowarm.zRot  = (-WingSpread * 0.1F);
            this.rightfing1a.zRot  = (-WingSpread);
            this.rightfing2a.zRot  = (-WingSpread * 0.8F);

            this.leftuparm.yRot    = -10F / 57.29578F - (WingSpread / 2F);
            this.leftlowarm.yRot   = 15F / 57.29578F + (WingSpread / 2F);
            this.leftfing1a.yRot   = 70F / 57.29578F;
            this.leftfing2a.yRot   = 35F / 57.29578F;
            this.leftfing3a.yRot   = -5F / 57.29578F;

            this.rightuparm.yRot   = 10F / 57.29578F + (WingSpread / 2F);
            this.rightlowarm.yRot  = -15F / 57.29578F - (WingSpread / 2F);
            this.rightfing1a.yRot  = -70F / 57.29578F;
            this.rightfing2a.yRot  = -35F / 57.29578F;
            this.rightfing3a.yRot  = 5F / 57.29578F;

            // Legs tucked when onAir:
            this.leftupleg.xRot   = speedMov;
            this.leftmidleg.xRot  = speedMov;
            this.leftfoot.xRot    = 25F / 57.29578F;
            this.lefttoe1.xRot    = speedMov;
            this.lefttoe2.xRot    = speedMov;
            this.lefttoe3.xRot    = speedMov;
            this.rightfoot.xRot   = 25F / 57.29578F;
            this.rightupleg.xRot  = speedMov;
            this.rightmidleg.xRot = speedMov;
            this.righttoe1.xRot   = speedMov;
            this.righttoe2.xRot   = speedMov;
            this.righttoe3.xRot   = speedMov;

        } else {
            // Wings folded at rest:
            this.leftlowarm.zRot    = 0F;
            this.leftfing1a.zRot    = 0F;
            this.leftfing2a.zRot    = 0F;
            this.rightlowarm.zRot   = 0F;
            this.rightfing1a.zRot   = 0F;
            this.rightfing2a.zRot   = 0F;

            this.leftuparm.zRot     = 30F / 57.29578F;
            this.leftuparm.yRot     = -60F / 57.29578F + (LLegXRot / 5F);
            this.leftlowarm.yRot    = 105F / 57.29578F;
            this.leftfing1a.yRot    = -20F / 57.29578F;
            this.leftfing2a.yRot    = -26F / 57.29578F;
            this.leftfing3a.yRot    = -32F / 57.29578F;

            this.rightuparm.yRot    = 60F / 57.29578F - (RLegXRot / 5F);
            this.rightuparm.zRot    = -30F / 57.29578F;
            this.rightlowarm.yRot   = -105F / 57.29578F;
            this.rightfing1a.yRot   = 16F / 57.29578F;
            this.rightfing2a.yRot   = 26F / 57.29578F;
            this.rightfing3a.yRot   = 32F / 57.29578F;

            // Leg walking/rest animation:
            this.leftupleg.xRot     = -25F / 57.29578F + LLegXRot;
            this.rightupleg.xRot    = -25F / 57.29578F + RLegXRot;
            this.leftmidleg.xRot    = 0F;
            this.leftlowleg.xRot    = 0F;
            this.leftfoot.xRot      = 25F / 57.29578F - LLegXRot;
            this.lefttoe1.xRot      = LLegXRot;
            this.lefttoe2.xRot      = LLegXRot;
            this.lefttoe3.xRot      = LLegXRot;
            this.rightmidleg.xRot   = 0F;
            this.rightlowleg.xRot   = 0F;
            this.rightfoot.xRot     = 25F / 57.29578F - RLegXRot;
            this.righttoe1.xRot     = RLegXRot;
            this.righttoe2.xRot     = RLegXRot;
            this.righttoe3.xRot     = RLegXRot;
        }

        // 7) Sitting pose overrides:
        if (isSitting) {
            this.leftupleg.xRot  = 45F / 57.29578F + LLegXRot;
            this.rightupleg.xRot = 45F / 57.29578F + RLegXRot;
            this.leftmidleg.xRot = 30F / 57.29578F;
            this.rightmidleg.xRot = 30F / 57.29578F;
            this.neck2.xRot = -36F / 57.29578F + (headPitch * (1F/3F) / 57.29578F);
            this.neck1.xRot = 30F  / 57.29578F + (headPitch * (2F/3F) / 57.29578F);
        }

        // 8) Diving pose overrides:
        if (diving) {
            this.leftuparm.zRot    = -40F / 57.29578F;
            this.rightuparm.zRot   = 40F / 57.29578F;
            this.leftlowarm.zRot   = 0F;
            this.leftfing1a.zRot   = 0F;
            this.leftfing2a.zRot   = 0F;
            this.rightlowarm.zRot  = 0F;
            this.rightfing1a.zRot  = 0F;
            this.rightfing2a.zRot  = 0F;

            this.leftuparm.yRot    = -50F / 57.29578F;
            this.leftlowarm.yRot   = 30F / 57.29578F;
            this.leftfing1a.yRot   = 50F / 57.29578F;
            this.leftfing2a.yRot   = 30F / 57.29578F;
            this.leftfing3a.yRot   = 10F / 57.29578F;

            this.rightuparm.yRot   = 50F / 57.29578F;
            this.rightlowarm.yRot  = -30F / 57.29578F;
            this.rightfing1a.yRot  = -50F / 57.29578F;
            this.rightfing2a.yRot  = -30F / 57.29578F;
            this.rightfing3a.yRot  = -10F / 57.29578F;
        }

        // 9) Mouth opening:
        if (openMouth != 0) {
            float mouthMov = Mth.cos((openMouth - 15) * 0.11F) * 0.8F;
            this.Jaw.xRot          = (-10F / 57.29578F) + mouthMov;
            this.leftearskin.yRot  = mouthMov;
            this.rightearskin.yRot = -mouthMov;
        } else {
            this.Jaw.xRot          = -10F / 57.29578F;
            this.leftearskin.yRot  = 0F;
            this.rightearskin.yRot = 0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        // Apply the entity’s vertical offset:
        poseStack.translate(0F, yOffset, 0F);

        // If ghost, enable translucent blending:
        if (isGhost) {
        }

        // Render spine+tail+chest in order:
        this.back1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Wings:
        this.LeftWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Shoulders:
        this.rightshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Neck “plates”:
        this.neckplate3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.neck3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Torso:
        this.torso.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Chested storage (if chest flag set):
        if (isChested) {
            this.storage.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Saddle + straps + belts (if isSaddled):
        if (isSaddled) {
            this.saddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.mouthrod.visible      = true;
            this.helmetstrap1.visible  = true;
            this.helmetstrap2.visible  = true;
            this.chestbelt.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.stomachbelt.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            if (isRidden) {
                this.controlrope1.visible = true;
                this.controlrope2.visible = true;
            } else {
                this.controlrope1.visible = false;
                this.controlrope2.visible = false;
            }
        } else {
            this.mouthrod.visible      = false;
            this.helmetstrap1.visible  = false;
            this.helmetstrap2.visible  = false;
        }

        // Hide all armor by default:
        this.ironhelmethorn1.visible       = false;
        this.ironhelmethorn2.visible       = false;
        this.ironhelmet.visible            = false;
        this.ironhelmetsnout.visible       = false;
        this.ironrightlegarmor.visible     = false;
        this.ironleftlegarmor.visible      = false;
        this.ironchestarmor.visible        = false;
        this.ironrightshoulderpad.visible  = false;
        this.ironleftshoulderpad.visible   = false;
        this.goldleftshoulder.visible      = false;
        this.goldchestarmor.visible        = false;
        this.goldrightshoulder.visible     = false;
        this.goldleftlegarmor.visible      = false;
        this.goldrightlegarmor.visible     = false;
        this.goldhelmethorn1.visible       = false;
        this.goldhelmethorn2.visible       = false;
        this.goldhelmet.visible            = false;
        this.goldhelmetsnout.visible       = false;
        this.diamondleftshoulder.visible   = false;
        this.diamondrightshoulder.visible  = false;
        this.diamondchestarmor.visible     = false;
        this.diamondleftlegarmor.visible   = false;
        this.diamondrightlegarmor.visible  = false;
        this.diamondhelmet.visible         = false;
        this.diamondhelmethorn2.visible    = false;
        this.diamondhelmethorn1.visible    = false;
        this.diamondhelmetsnout.visible    = false;

        // Show whichever armor set is selected:
        switch (armor) {
            case 1:
                this.ironhelmethorn1.visible       = true;
                this.ironhelmethorn2.visible       = true;
                this.ironhelmet.visible            = true;
                this.ironhelmetsnout.visible       = true;
                this.ironrightlegarmor.visible     = true;
                this.ironleftlegarmor.visible      = true;
                this.ironchestarmor.visible        = true;
                this.ironrightshoulderpad.visible  = true;
                this.ironleftshoulderpad.visible   = true;
                break;
            case 2:
                this.goldleftshoulder.visible      = true;
                this.goldchestarmor.visible        = true;
                this.goldrightshoulder.visible     = true;
                this.goldleftlegarmor.visible      = true;
                this.goldrightlegarmor.visible     = true;
                this.goldhelmethorn1.visible       = true;
                this.goldhelmethorn2.visible       = true;
                this.goldhelmet.visible            = true;
                this.goldhelmetsnout.visible       = true;
                break;
            case 3:
                this.diamondleftshoulder.visible   = true;
                this.diamondrightshoulder.visible  = true;
                this.diamondchestarmor.visible     = true;
                this.diamondleftlegarmor.visible   = true;
                this.diamondrightlegarmor.visible  = true;
                this.diamondhelmet.visible         = true;
                this.diamondhelmethorn2.visible    = true;
                this.diamondhelmethorn1.visible    = true;
                this.diamondhelmetsnout.visible    = true;
                break;
        }

        // Render head & legs last so they appear on top:
        this.MainHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftupleg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightupleg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Render chest armor & shoulder pads *after* legs/head so they overlay properly:
        this.ironchestarmor.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ironrightshoulderpad.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ironleftshoulderpad.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.goldleftshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.goldchestarmor.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.goldrightshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.diamondleftshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.diamondrightshoulder.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.diamondchestarmor.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // If ghost, disable blending so future renders aren’t translucent:
        if (isGhost) {
        }
        poseStack.popPose();
    }
}
