package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityBear;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MoCModelBear<T extends MoCEntityBear> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "bear"), "main"
    );

    // All parts, named exactly as before (stand, biped, and sit variants mixed together)
    private final ModelPart root;

    // "On all fours" parts:
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart mouthOpen;
    private final ModelPart lEar;
    private final ModelPart rEar;
    private final ModelPart snout;
    private final ModelPart neck;
    private final ModelPart abdomen;
    private final ModelPart torso;
    private final ModelPart tail;
    private final ModelPart legFL1, legFL2, legFL3;
    private final ModelPart legFR1, legFR2, legFR3;
    private final ModelPart legRL1, legRL2, legRL3;
    private final ModelPart legRR1, legRR2, legRR3;

    // "Standing up" (biped) parts:
    private final ModelPart bHead;
    private final ModelPart bSnout;
    private final ModelPart bMouth;
    private final ModelPart bMouthOpen;
    private final ModelPart bNeck;
    private final ModelPart bLEar;
    private final ModelPart bREar;
    private final ModelPart bTorso;
    private final ModelPart bAbdomen;
    private final ModelPart bTail;
    private final ModelPart bLegFL1, bLegFL2, bLegFL3;
    private final ModelPart bLegFR1, bLegFR2, bLegFR3;
    private final ModelPart bLegRL1, bLegRL2, bLegRL3;
    private final ModelPart bLegRR1, bLegRR2, bLegRR3;

    // "Sitting" parts:
    private final ModelPart cHead;
    private final ModelPart cSnout;
    private final ModelPart cMouth;
    private final ModelPart cMouthOpen;
    private final ModelPart cLEar;
    private final ModelPart cREar;
    private final ModelPart cNeck;
    private final ModelPart cTorso;
    private final ModelPart cAbdomen;
    private final ModelPart cTail;
    private final ModelPart cLegFL1, cLegFL2, cLegFL3;
    private final ModelPart cLegFR1, cLegFR2, cLegFR3;
    private final ModelPart cLegRL1, cLegRL2, cLegRL3;
    private final ModelPart cLegRR1, cLegRR2, cLegRR3;

    // Saddle/Bag parts (both "on all fours" and "sitting" variants):
    private final ModelPart saddle, saddleBack, saddleFront, bag;
    private final ModelPart saddleSitted, saddleBackSitted, saddleFrontSitted, bagSitted;

    // State trackers:
    private int bearState;
    private float attackSwing;
    private MoCEntityBear entityBear;

    public MoCModelBear(ModelPart root) {
        super(root);
        this.root = root;

        // "On all fours" children
        this.head       = root.getChild("head");
        this.mouth      = root.getChild("mouth");
        this.mouthOpen  = root.getChild("mouth_open");
        this.lEar       = root.getChild("l_ear");
        this.rEar       = root.getChild("r_ear");
        this.snout      = root.getChild("snout");
        this.neck       = root.getChild("neck");
        this.abdomen    = root.getChild("abdomen");
        this.torso      = root.getChild("torso");
        this.tail       = root.getChild("tail");
        this.legFL1     = root.getChild("leg_fl1");
        this.legFL2     = root.getChild("leg_fl2");
        this.legFL3     = root.getChild("leg_fl3");
        this.legFR1     = root.getChild("leg_fr1");
        this.legFR2     = root.getChild("leg_fr2");
        this.legFR3     = root.getChild("leg_fr3");
        this.legRL1     = root.getChild("leg_rl1");
        this.legRL2     = root.getChild("leg_rl2");
        this.legRL3     = root.getChild("leg_rl3");
        this.legRR1     = root.getChild("leg_rr1");
        this.legRR2     = root.getChild("leg_rr2");
        this.legRR3     = root.getChild("leg_rr3");

        // "Standing up" children
        this.bHead      = root.getChild("b_head");
        this.bSnout     = root.getChild("b_snout");
        this.bMouth     = root.getChild("b_mouth");
        this.bMouthOpen = root.getChild("b_mouth_open");
        this.bNeck      = root.getChild("b_neck");
        this.bLEar      = root.getChild("b_l_ear");
        this.bREar      = root.getChild("b_r_ear");
        this.bTorso     = root.getChild("b_torso");
        this.bAbdomen   = root.getChild("b_abdomen");
        this.bTail      = root.getChild("b_tail");
        this.bLegFL1    = root.getChild("b_leg_fl1");
        this.bLegFL2    = root.getChild("b_leg_fl2");
        this.bLegFL3    = root.getChild("b_leg_fl3");
        this.bLegFR1    = root.getChild("b_leg_fr1");
        this.bLegFR2    = root.getChild("b_leg_fr2");
        this.bLegFR3    = root.getChild("b_leg_fr3");
        this.bLegRL1    = root.getChild("b_leg_rl1");
        this.bLegRL2    = root.getChild("b_leg_rl2");
        this.bLegRL3    = root.getChild("b_leg_rl3");
        this.bLegRR1    = root.getChild("b_leg_rr1");
        this.bLegRR2    = root.getChild("b_leg_rr2");
        this.bLegRR3    = root.getChild("b_leg_rr3");

        // "Sitting" children
        this.cHead      = root.getChild("c_head");
        this.cSnout     = root.getChild("c_snout");
        this.cMouth     = root.getChild("c_mouth");
        this.cMouthOpen = root.getChild("c_mouth_open");
        this.cLEar      = root.getChild("c_l_ear");
        this.cREar      = root.getChild("c_r_ear");
        this.cNeck      = root.getChild("c_neck");
        this.cTorso     = root.getChild("c_torso");
        this.cAbdomen   = root.getChild("c_abdomen");
        this.cTail      = root.getChild("c_tail");
        this.cLegFL1    = root.getChild("c_leg_fl1");
        this.cLegFL2    = root.getChild("c_leg_fl2");
        this.cLegFL3    = root.getChild("c_leg_fl3");
        this.cLegFR1    = root.getChild("c_leg_fr1");
        this.cLegFR2    = root.getChild("c_leg_fr2");
        this.cLegFR3    = root.getChild("c_leg_fr3");
        this.cLegRL1    = root.getChild("c_leg_rl1");
        this.cLegRL2    = root.getChild("c_leg_rl2");
        this.cLegRL3    = root.getChild("c_leg_rl3");
        this.cLegRR1    = root.getChild("c_leg_rr1");
        this.cLegRR2    = root.getChild("c_leg_rr2");
        this.cLegRR3    = root.getChild("c_leg_rr3");

        // Saddle & Bag (both poses)
        this.saddle            = root.getChild("saddle");
        this.saddleBack        = root.getChild("saddle_back");
        this.saddleFront       = root.getChild("saddle_front");
        this.bag               = root.getChild("bag");
        this.saddleSitted      = root.getChild("saddle_sitted");
        this.saddleBackSitted  = root.getChild("saddle_back_sitted");
        this.saddleFrontSitted = root.getChild("saddle_front_sitted");
        this.bagSitted         = root.getChild("bag_sitted");
    }

    /**
     * Builds the entire mesh hierarchy.  Mirrors your old addBox/ setRotation calls exactly.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ----------------------------------------------------------------------
        // "On all fours" parts
        // ----------------------------------------------------------------------

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(-4F, 0F, -4F, 8, 8, 5),
                PartPose.offsetAndRotation(0F, 6F, -10F, 0.1502636F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "mouth",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 6F, -6.8F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, 6F, -10F, -0.0068161F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "mouth_open",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 4F, -9.5F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, 6F, -10F, 0.534236F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "l_ear",
                CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(2F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, 6F, -10F, 0.1502636F, -0.3490659F, 0.1396263F)
        );

        root.addOrReplaceChild(
                "r_ear",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-5F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, 6F, -10F, 0.1502636F, 0.3490659F, -0.1396263F)
        );

        root.addOrReplaceChild(
                "snout",
                CubeListBuilder.create()
                        .texOffs(23, 13)
                        .addBox(-2F, 3F, -8F, 4, 3, 5),
                PartPose.offsetAndRotation(0F, 6F, -10F, 0.1502636F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-3.5F, 0F, -7F, 7, 7, 7),
                PartPose.offsetAndRotation(0F, 5F, -5F, 0.2617994F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create()
                        .texOffs(13, 62)
                        .addBox(-4.5F, 0F, 0F, 9, 11, 10),
                PartPose.offsetAndRotation(0F, 5F, 5F, -0.4363323F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "torso",
                CubeListBuilder.create()
                        .texOffs(12, 42)
                        .addBox(-5F, 0F, 0F, 10, 10, 10),
                PartPose.offset(0F, 5F, -5F)
        );

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                        .texOffs(26, 83)
                        .addBox(-1.5F, 0F, 0F, 3, 3, 3),
                PartPose.offsetAndRotation(0F, 8.466666F, 12F, 0.4363323F, 0F, 0F)
        );

        root.addOrReplaceChild(
                "leg_fl1",
                CubeListBuilder.create()
                        .texOffs(40, 22)
                        .addBox(-2.5F, 0F, -2.5F, 5, 8, 5),
                PartPose.offsetAndRotation(4F, 10F, -4F, 0.2617994F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "leg_fl2",
                CubeListBuilder.create()
                        .texOffs(46, 35)
                        .addBox(-2F, 7F, 0F, 4, 6, 4),
                PartPose.offset(4F, 10F, -4F)
        );
        root.addOrReplaceChild(
                "leg_fl3",
                CubeListBuilder.create()
                        .texOffs(46, 45)
                        .addBox(-2F, 12F, -1F, 4, 2, 5),
                PartPose.offset(4F, 10F, -4F)
        );

        root.addOrReplaceChild(
                "leg_fr1",
                CubeListBuilder.create()
                        .texOffs(4, 22)
                        .addBox(-2.5F, 0F, -2.5F, 5, 8, 5),
                PartPose.offsetAndRotation(-4F, 10F, -4F, 0.2617994F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "leg_fr2",
                CubeListBuilder.create()
                        .texOffs(2, 35)
                        .addBox(-2F, 7F, 0F, 4, 6, 4),
                PartPose.offset(-4F, 10F, -4F)
        );
        root.addOrReplaceChild(
                "leg_fr3",
                CubeListBuilder.create()
                        .texOffs(0, 45)
                        .addBox(-2F, 12F, -1F, 4, 2, 5),
                PartPose.offset(-4F, 10F, -4F)
        );

        root.addOrReplaceChild(
                "leg_rl1",
                CubeListBuilder.create()
                        .texOffs(34, 83)
                        .addBox(-1.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(3.5F, 11F, 9F, -0.1745329F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "leg_rl2",
                CubeListBuilder.create()
                        .texOffs(41, 97)
                        .addBox(-2F, 6F, -1F, 4, 6, 4),
                PartPose.offset(3.5F, 11F, 9F)
        );
        root.addOrReplaceChild(
                "leg_rl3",
                CubeListBuilder.create()
                        .texOffs(44, 107)
                        .addBox(-2F, 11F, -2F, 4, 2, 5),
                PartPose.offset(3.5F, 11F, 9F)
        );

        root.addOrReplaceChild(
                "leg_rr1",
                CubeListBuilder.create()
                        .texOffs(10, 83)
                        .addBox(-2.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(-3.5F, 11F, 9F, -0.1745329F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "leg_rr2",
                CubeListBuilder.create()
                        .texOffs(7, 97)
                        .addBox(-2F, 6F, -1F, 4, 6, 4),
                PartPose.offset(-3.5F, 11F, 9F)
        );
        root.addOrReplaceChild(
                "leg_rr3",
                CubeListBuilder.create()
                        .texOffs(2, 107)
                        .addBox(-2F, 11F, -2F, 4, 2, 5),
                PartPose.offset(-3.5F, 11F, 9F)
        );

        // ----------------------------------------------------------------------
        // "Standing up" (biped) parts
        // ----------------------------------------------------------------------

        root.addOrReplaceChild(
                "b_head",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(-4F, 0F, -4F, 8, 8, 5),
                PartPose.offsetAndRotation(0F, -12F, 5F, -0.0242694F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_snout",
                CubeListBuilder.create()
                        .texOffs(23, 13)
                        .addBox(-2F, 2.5F, -8.5F, 4, 3, 5),
                PartPose.offsetAndRotation(0F, -12F, 5F, -0.0242694F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_mouth",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 5.5F, -8.0F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, -12F, 5F, -0.08726F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_mouth_open",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 3.5F, -11F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, -12F, 5F, 0.5235988F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_neck",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-3.5F, 0F, -7F, 7, 6, 7),
                PartPose.offsetAndRotation(0F, -3F, 11F, -1.336881F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_l_ear",
                CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(2F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, -12F, 5F, -0.0242694F, -0.3490659F, 0.1396263F)
        );
        root.addOrReplaceChild(
                "b_r_ear",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-5F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, -12F, 5F, -0.0242694F, 0.3490659F, -0.1396263F)
        );
        root.addOrReplaceChild(
                "b_torso",
                CubeListBuilder.create()
                        .texOffs(12, 42)
                        .addBox(-5F, 0F, 0F, 10, 10, 10),
                PartPose.offsetAndRotation(0F, -3.5F, 12.3F, -1.396263F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_abdomen",
                CubeListBuilder.create()
                        .texOffs(13, 62)
                        .addBox(-4.5F, 0F, 0F, 9, 11, 10),
                PartPose.offsetAndRotation(0F, 6F, 14F, -1.570796F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_tail",
                CubeListBuilder.create()
                        .texOffs(26, 83)
                        .addBox(-1.5F, 0F, 0F, 3, 3, 3),
                PartPose.offsetAndRotation(0F, 12.46667F, 12.6F, 0.3619751F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_fl1",
                CubeListBuilder.create()
                        .texOffs(40, 22)
                        .addBox(-2.5F, 0F, -2.5F, 5, 8, 5),
                PartPose.offsetAndRotation(5F, -1F, 6F, 0.2617994F, 0F, -0.2617994F)
        );
        root.addOrReplaceChild(
                "b_leg_fl2",
                CubeListBuilder.create()
                        .texOffs(46, 35)
                        .addBox(0F, 5F, 3F, 4, 6, 4),
                PartPose.offsetAndRotation(5F, -1F, 6F, -0.5576792F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_fl3",
                CubeListBuilder.create()
                        .texOffs(46, 45)
                        .addBox(0.1F, -7F, -14F, 4, 2, 5),
                PartPose.offsetAndRotation(5F, -1F, 6F, 2.007645F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_fr1",
                CubeListBuilder.create()
                        .texOffs(4, 22)
                        .addBox(-2.5F, 0F, -2.5F, 5, 8, 5),
                PartPose.offsetAndRotation(-5F, -1F, 6F, 0.2617994F, 0F, 0.2617994F)
        );
        root.addOrReplaceChild(
                "b_leg_fr2",
                CubeListBuilder.create()
                        .texOffs(2, 35)
                        .addBox(-4F, 5F, 3F, 4, 6, 4),
                PartPose.offsetAndRotation(-5F, -1F, 6F, -0.5576792F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_fr3",
                CubeListBuilder.create()
                        .texOffs(0, 45)
                        .addBox(-4.1F, -7F, -14F, 4, 2, 5),
                PartPose.offsetAndRotation(-5F, -1F, 6F, 2.007129F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rl1",
                CubeListBuilder.create()
                        .texOffs(34, 83)
                        .addBox(-1.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(3F, 11F, 9F, -0.5235988F, -0.2617994F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rl2",
                CubeListBuilder.create()
                        .texOffs(41, 97)
                        .addBox(-1.3F, 6F, -3F, 4, 6, 4),
                PartPose.offsetAndRotation(3F, 11F, 9F, 0F, -0.2617994F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rl3",
                CubeListBuilder.create()
                        .texOffs(44, 107)
                        .addBox(-1.2F, 11F, -4F, 4, 2, 5),
                PartPose.offsetAndRotation(3F, 11F, 9F, 0F, -0.2617994F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rr1",
                CubeListBuilder.create()
                        .texOffs(10, 83)
                        .addBox(-2.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(-3F, 11F, 9F, -0.1745329F, 0.2617994F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rr2",
                CubeListBuilder.create()
                        .texOffs(7, 97)
                        .addBox(-2.4F, 6F, -1F, 4, 6, 4),
                PartPose.offsetAndRotation(-3F, 11F, 9F, 0F, 0.2617994F, 0F)
        );
        root.addOrReplaceChild(
                "b_leg_rr3",
                CubeListBuilder.create()
                        .texOffs(2, 107)
                        .addBox(-2.5F, 11F, -2F, 4, 2, 5),
                PartPose.offsetAndRotation(-3F, 11F, 9F, 0F, 0.2617994F, 0F)
        );

        // ----------------------------------------------------------------------
        // "Sitting" parts
        // ----------------------------------------------------------------------

        root.addOrReplaceChild(
                "c_head",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(-4F, 0F, -4F, 8, 8, 5),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, 0.1502636F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_snout",
                CubeListBuilder.create()
                        .texOffs(23, 13)
                        .addBox(-2F, 3F, -8.5F, 4, 3, 5),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, 0.1502636F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_mouth",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 6F, -7F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, -0.0068161F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_mouth_open",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(-1.5F, 5.5F, -9F, 3, 2, 5),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, 0.3665191F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_l_ear",
                CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(2F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, 0.1502636F, -0.3490659F, 0.1396263F)
        );
        root.addOrReplaceChild(
                "c_r_ear",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-5F, -2F, -2F, 3, 3, 1),
                PartPose.offsetAndRotation(0F, 3F, -3.5F, 0.1502636F, 0.3490659F, -0.1396263F)
        );
        root.addOrReplaceChild(
                "c_neck",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-3.5F, 0F, -7F, 7, 7, 7),
                PartPose.offsetAndRotation(0F, 5.8F, 3.4F, -0.3316126F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_torso",
                CubeListBuilder.create()
                        .texOffs(12, 42)
                        .addBox(-5F, 0F, 0F, 10, 10, 10),
                PartPose.offsetAndRotation(0F, 5.8F, 3.4F, -0.9712912F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_abdomen",
                CubeListBuilder.create()
                        .texOffs(13, 62)
                        .addBox(-4.5F, 0F, 0F, 9, 11, 10),
                PartPose.offsetAndRotation(0F, 14F, 9F, -1.570796F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_tail",
                CubeListBuilder.create()
                        .texOffs(26, 83)
                        .addBox(-1.5F, 0F, 0F, 3, 3, 3),
                PartPose.offsetAndRotation(0F, 21.46667F, 8F, 0.4363323F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_leg_fl1",
                CubeListBuilder.create()
                        .texOffs(40, 22)
                        .addBox(-2.5F, 0F, -1.5F, 5, 8, 5),
                PartPose.offsetAndRotation(4F, 10F, 0F, -0.2617994F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_leg_fl2",
                CubeListBuilder.create()
                        .texOffs(46, 35)
                        .addBox(-2F, 0F, -1.2F, 4, 6, 4),
                PartPose.offsetAndRotation(4F, 17F, -2F, -0.3490659F, 0F, 0.2617994F)
        );
        root.addOrReplaceChild(
                "c_leg_fl3",
                CubeListBuilder.create()
                        .texOffs(46, 45)
                        .addBox(-2F, 0F, -3F, 4, 2, 5),
                PartPose.offsetAndRotation(2.5F, 22F, -4F, 0F, 0.1745329F, 0F)
        );
        root.addOrReplaceChild(
                "c_leg_fr1",
                CubeListBuilder.create()
                        .texOffs(4, 22)
                        .addBox(-2.5F, 0F, -1.5F, 5, 8, 5),
                PartPose.offsetAndRotation(-4F, 10F, 0F, -0.2617994F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "c_leg_fr2",
                CubeListBuilder.create()
                        .texOffs(2, 35)
                        .addBox(-2F, 0F, -1.2F, 4, 6, 4),
                PartPose.offsetAndRotation(-4F, 17F, -2F, -0.3490659F, 0F, -0.2617994F)
        );
        root.addOrReplaceChild(
                "c_leg_fr3",
                CubeListBuilder.create()
                        .texOffs(0, 45)
                        .addBox(-2F, 0F, -3F, 4, 2, 5),
                PartPose.offsetAndRotation(-2.5F, 22F, -4F, 0F, -0.1745329F, 0F)
        );
        root.addOrReplaceChild(
                "c_leg_rl1",
                CubeListBuilder.create()
                        .texOffs(34, 83)
                        .addBox(-1.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(3F, 21F, 5F, -1.396263F, -0.3490659F, 0.3490659F)
        );
        root.addOrReplaceChild(
                "c_leg_rl2",
                CubeListBuilder.create()
                        .texOffs(41, 97)
                        .addBox(-2F, 0F, -2F, 4, 6, 4),
                PartPose.offsetAndRotation(5.2F, 22.5F, -1F, -1.570796F, 0F, 0.3490659F)
        );
        root.addOrReplaceChild(
                "c_leg_rl3",
                CubeListBuilder.create()
                        .texOffs(44, 107)
                        .addBox(-2F, 0F, -3F, 4, 2, 5),
                PartPose.offsetAndRotation(5.5F, 22F, -6F, -1.375609F, 0F, 0.3490659F)
        );
        root.addOrReplaceChild(
                "c_leg_rr1",
                CubeListBuilder.create()
                        .texOffs(10, 83)
                        .addBox(-2.5F, 0F, -2.5F, 4, 8, 6),
                PartPose.offsetAndRotation(-3F, 21F, 5F, -1.396263F, 0.3490659F, -0.3490659F)
        );
        root.addOrReplaceChild(
                "c_leg_rr2",
                CubeListBuilder.create()
                        .texOffs(7, 97)
                        .addBox(-2F, 0F, -2F, 4, 6, 4),
                PartPose.offsetAndRotation(-5.2F, 22.5F, -1F, -1.570796F, 0F, -0.3490659F)
        );
        root.addOrReplaceChild(
                "c_leg_rr3",
                CubeListBuilder.create()
                        .texOffs(2, 107)
                        .addBox(-2F, 0F, -3F, 4, 2, 5),
                PartPose.offsetAndRotation(-5.5F, 22F, -6F, -1.375609F, 0F, -0.3490659F)
        );

        // ----------------------------------------------------------------------
        // Saddle & Bag (all fours)
        // ----------------------------------------------------------------------

        root.addOrReplaceChild(
                "saddle",
                CubeListBuilder.create()
                        .texOffs(36, 114)
                        .addBox(-4F, -0.5F, -3F, 8, 2, 6),
                PartPose.offset(0F, 4F, -2F)
        );
        root.addOrReplaceChild(
                "saddle_back",
                CubeListBuilder.create()
                        .texOffs(20, 108)
                        .addBox(-4F, -0.2F, 2.9F, 8, 2, 4),
                PartPose.offsetAndRotation(0F, 4F, -2F, 0.10088F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "saddle_front",
                CubeListBuilder.create()
                        .texOffs(36, 122)
                        .addBox(-2.5F, -1F, -3F, 5, 2, 3),
                PartPose.offsetAndRotation(0F, 4F, -2F, -0.1850049F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "bag",
                CubeListBuilder.create()
                        .texOffs(0, 114)
                        .addBox(-5F, -3F, -2.5F, 10, 2, 5),
                PartPose.offsetAndRotation(0F, 7F, 7F, -0.4363323F, 0F, 0F)
        );

        // ----------------------------------------------------------------------
        // Saddle & Bag ("sitting" pose)
        // ----------------------------------------------------------------------

        root.addOrReplaceChild(
                "bag_sitted",
                CubeListBuilder.create()
                        .texOffs(0, 114)
                        .addBox(-5F, -3F, -2.5F, 10, 2, 5),
                PartPose.offsetAndRotation(0F, 17F, 8F, -1.570796F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "saddle_sitted",
                CubeListBuilder.create()
                        .texOffs(36, 114)
                        .addBox(-4F, -0.5F, -3F, 8, 2, 6),
                PartPose.offsetAndRotation(0F, 7.5F, 6.5F, -0.9686577F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "saddle_back_sitted",
                CubeListBuilder.create()
                        .texOffs(20, 108)
                        .addBox(-4F, -0.3F, 2.9F, 8, 2, 4),
                PartPose.offsetAndRotation(0F, 7.5F, 6.5F, -0.9162979F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "saddle_front_sitted",
                CubeListBuilder.create()
                        .texOffs(36, 122)
                        .addBox(-2.5F, -1F, -3F, 5, 2, 3),
                PartPose.offsetAndRotation(0F, 7.5F, 6.5F, -1.151917F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 128, 128);
    }

    /**
     * Called from the renderer to capture bearState & attackSwing
     */
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.entityBear = entityIn;
        this.bearState = entityBear.getBearState();
        this.attackSwing = entityBear.getAttackSwing();
    }

    @Override
    public void setupAnim(
            @NotNull T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Store the entity reference
        this.entityBear = entity;
        this.bearState = entity.getBearState();
        this.attackSwing = entity.getAttackSwing();

        // Calculate common leg swings:
        float lLegRotX = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
        float rLegRotX = Mth.cos((limbSwing * 0.6662F) + (float) Math.PI) * 0.8F * limbSwingAmount;
        float xAngle   = headPitch * Mth.DEG_TO_RAD;
        float yAngle   = netHeadYaw * Mth.DEG_TO_RAD;

        // First, default‐hide everything. We'll enable visibility on‐the‐fly below.
        setAllVisible(false);

        if (this.bearState == 0) {
            // ─────────────────────────────────────
            // On All Fours
            // ─────────────────────────────────────
            boolean openMouth = (entity.mouthCounter != 0);
            boolean chested   = entity.getIsChested();
            boolean saddled   = entity.getIsRideable();

            // Head + look angles:
            this.head.xRot      = 0.1502636F + xAngle;
            this.head.yRot      = yAngle;
            this.snout.xRot     = 0.1502636F + xAngle;
            this.snout.yRot     = yAngle;
            this.mouth.xRot     = -0.0068161F + xAngle;
            this.mouth.yRot     = yAngle;
            this.mouthOpen.xRot = 0.534236F + xAngle;
            this.mouthOpen.yRot = yAngle;
            this.lEar.xRot      = 0.1502636F + xAngle;
            this.lEar.yRot      = -0.3490659F + yAngle;
            this.rEar.xRot      = 0.1502636F + xAngle;
            this.rEar.yRot      = 0.3490659F + yAngle;

            // Leg swings:
            this.legFL1.xRot = 0.2617994F + lLegRotX;
            this.legFL2.xRot = lLegRotX;
            this.legFL3.xRot = lLegRotX;

            this.legRR1.xRot = -0.1745329F + lLegRotX;
            this.legRR2.xRot = lLegRotX;
            this.legRR3.xRot = lLegRotX;

            this.legFR1.xRot = 0.2617994F + rLegRotX;
            this.legFR2.xRot = rLegRotX;
            this.legFR3.xRot = rLegRotX;

            this.legRL1.xRot = -0.1745329F + rLegRotX;
            this.legRL2.xRot = rLegRotX;
            this.legRL3.xRot = rLegRotX;

            // Tail wag (subtle)
            this.tail.zRot = lLegRotX * 0.2F;

            // Make visible:
            this.head.visible      = true;
            (openMouth ? this.mouthOpen : this.mouth).visible = true;
            this.lEar.visible      = true;
            this.rEar.visible      = true;
            this.snout.visible     = true;
            this.neck.visible      = true;
            this.abdomen.visible   = true;
            this.torso.visible     = true;
            this.tail.visible      = true;

            this.legFR1.visible  = true;
            this.legFR2.visible  = true;
            this.legFR3.visible  = true;
            this.legFL1.visible  = true;
            this.legFL2.visible  = true;
            this.legFL3.visible  = true;
            this.legRL1.visible  = true;
            this.legRL2.visible  = true;
            this.legRL3.visible  = true;
            this.legRR1.visible  = true;
            this.legRR2.visible  = true;
            this.legRR3.visible  = true;

            if (saddled) {
                this.saddle.visible      = true;
                this.saddleBack.visible  = true;
                this.saddleFront.visible = true;
            }
            if (chested) {
                this.bag.visible = true;
            }

        } else if (this.bearState == 1) {
            // ─────────────────────────────────────
            // Standing Up (Biped)
            // ─────────────────────────────────────
            boolean openMouth = (entity.mouthCounter != 0);

            // Head + look:
            this.bHead.xRot      = -0.0242694F - xAngle;
            this.bHead.yRot      = yAngle;
            this.bSnout.xRot     = -0.0242694F - xAngle;
            this.bSnout.yRot     = yAngle;
            this.bMouth.xRot     = -0.08726F - xAngle;
            this.bMouth.yRot     = yAngle;
            this.bMouthOpen.xRot = 0.5235988F - xAngle;
            this.bMouthOpen.yRot = yAngle;
            this.bLEar.xRot      = -0.0242694F - xAngle;
            this.bLEar.yRot      = -0.3490659F + yAngle;
            this.bREar.xRot      = -0.0242694F - xAngle;
            this.bREar.yRot      = 0.3490659F + yAngle;

            // "Breathing" subtle Z‐axis swings on front legs:
            float breathing = Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bLegFR1.zRot = 0.2617994F + breathing;
            this.bLegFR2.zRot = breathing;
            this.bLegFR3.zRot = breathing;
            this.bLegFL1.zRot = -0.2617994F - breathing;
            this.bLegFL2.zRot = -breathing;
            this.bLegFL3.zRot = -breathing;

            // Attack swing layering on front legs:
            this.bLegFL1.xRot = 0.2617994F + attackSwing;
            this.bLegFL2.xRot = -0.5576792F + attackSwing;
            this.bLegFL3.xRot = 2.007645F + attackSwing;
            this.bLegFR1.xRot = 0.2617994F + attackSwing;
            this.bLegFR2.xRot = -0.5576792F + attackSwing;
            this.bLegFR3.xRot = 2.007645F + attackSwing;

            // Rear‐leg walking animation:
            this.bLegRR1.xRot = -0.1745329F + lLegRotX;
            this.bLegRR2.xRot = lLegRotX;
            this.bLegRR3.xRot = lLegRotX;
            this.bLegRL1.xRot = -0.5235988F + rLegRotX;
            this.bLegRL2.xRot = rLegRotX;
            this.bLegRL3.xRot = rLegRotX;

            // Make visible:
            this.bHead.visible      = true;
            (openMouth ? this.bMouthOpen : this.bMouth).visible = true;
            this.bSnout.visible     = true;
            this.bLEar.visible      = true;
            this.bREar.visible      = true;
            this.bNeck.visible      = true;
            this.bTorso.visible     = true;
            this.bAbdomen.visible   = true;
            this.bTail.visible      = true;
            this.bLegFL1.visible    = true;
            this.bLegFL2.visible    = true;
            this.bLegFL3.visible    = true;
            this.bLegFR1.visible    = true;
            this.bLegFR2.visible    = true;
            this.bLegFR3.visible    = true;
            this.bLegRL1.visible    = true;
            this.bLegRL2.visible    = true;
            this.bLegRL3.visible    = true;
            this.bLegRR1.visible    = true;
            this.bLegRR2.visible    = true;
            this.bLegRR3.visible    = true;

        } else if (this.bearState == 2) {
            // ─────────────────────────────────────
            // Sitting
            // ─────────────────────────────────────
            boolean openMouth = (entity.mouthCounter != 0);
            boolean chested   = entity.getIsChested();
            boolean saddled   = entity.getIsRideable();

            // Head + look:
            this.cHead.xRot      = 0.1502636F + xAngle;
            this.cHead.yRot      = yAngle;
            this.cSnout.xRot     = 0.1502636F + xAngle;
            this.cSnout.yRot     = yAngle;
            this.cMouth.xRot     = -0.0068161F + xAngle;
            this.cMouth.yRot     = yAngle;
            this.cMouthOpen.xRot = 0.3665191F + xAngle;
            this.cMouthOpen.yRot = yAngle;
            this.cLEar.xRot      = 0.1502636F + xAngle;
            this.cLEar.yRot      = -0.3490659F + yAngle;
            this.cREar.xRot      = 0.1502636F + xAngle;
            this.cREar.yRot      = 0.3490659F + yAngle;

            // Make visible:
            this.cHead.visible      = true;
            (openMouth ? this.cMouthOpen : this.cMouth).visible = true;
            this.cSnout.visible     = true;
            this.cLEar.visible      = true;
            this.cREar.visible      = true;
            this.cNeck.visible      = true;
            this.cTorso.visible     = true;
            this.cAbdomen.visible   = true;
            this.cTail.visible      = true;
            this.cLegFL1.visible    = true;
            this.cLegFL2.visible    = true;
            this.cLegFL3.visible    = true;
            this.cLegFR1.visible    = true;
            this.cLegFR2.visible    = true;
            this.cLegFR3.visible    = true;
            this.cLegRL1.visible    = true;
            this.cLegRL2.visible    = true;
            this.cLegRL3.visible    = true;
            this.cLegRR1.visible    = true;
            this.cLegRR2.visible    = true;
            this.cLegRR3.visible    = true;

            if (saddled) {
                this.saddleSitted.visible       = true;
                this.saddleBackSitted.visible   = true;
                this.saddleFrontSitted.visible  = true;
            }
            if (chested) {
                this.bagSitted.visible = true;
            }
        }
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        // Simply render every child of root; invisible parts will skip themselves.
        this.root.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * Helper to set all ModelParts' visible=false at the start of each frame.
     */
    private void setAllVisible(boolean visible) {
        head.visible             = visible;
        mouth.visible            = visible;
        mouthOpen.visible        = visible;
        lEar.visible             = visible;
        rEar.visible             = visible;
        snout.visible            = visible;
        neck.visible             = visible;
        abdomen.visible          = visible;
        torso.visible            = visible;
        tail.visible             = visible;
        legFL1.visible           = visible;
        legFL2.visible           = visible;
        legFL3.visible           = visible;
        legFR1.visible           = visible;
        legFR2.visible           = visible;
        legFR3.visible           = visible;
        legRL1.visible           = visible;
        legRL2.visible           = visible;
        legRL3.visible           = visible;
        legRR1.visible           = visible;
        legRR2.visible           = visible;
        legRR3.visible           = visible;

        bHead.visible            = visible;
        bSnout.visible           = visible;
        bMouth.visible           = visible;
        bMouthOpen.visible       = visible;
        bNeck.visible            = visible;
        bLEar.visible            = visible;
        bREar.visible            = visible;
        bTorso.visible           = visible;
        bAbdomen.visible         = visible;
        bTail.visible            = visible;
        bLegFL1.visible          = visible;
        bLegFL2.visible          = visible;
        bLegFL3.visible          = visible;
        bLegFR1.visible          = visible;
        bLegFR2.visible          = visible;
        bLegFR3.visible          = visible;
        bLegRL1.visible          = visible;
        bLegRL2.visible          = visible;
        bLegRL3.visible          = visible;
        bLegRR1.visible          = visible;
        bLegRR2.visible          = visible;
        bLegRR3.visible          = visible;

        cHead.visible            = visible;
        cSnout.visible           = visible;
        cMouth.visible           = visible;
        cMouthOpen.visible       = visible;
        cLEar.visible            = visible;
        cREar.visible            = visible;
        cNeck.visible            = visible;
        cTorso.visible           = visible;
        cAbdomen.visible         = visible;
        cTail.visible            = visible;
        cLegFL1.visible          = visible;
        cLegFL2.visible          = visible;
        cLegFL3.visible          = visible;
        cLegFR1.visible          = visible;
        cLegFR2.visible          = visible;
        cLegFR3.visible          = visible;
        cLegRL1.visible          = visible;
        cLegRL2.visible          = visible;
        cLegRL3.visible          = visible;
        cLegRR1.visible          = visible;
        cLegRR2.visible          = visible;
        cLegRR3.visible          = visible;

        saddle.visible            = visible;
        saddleBack.visible        = visible;
        saddleFront.visible       = visible;
        bag.visible               = visible;
        saddleSitted.visible      = visible;
        saddleBackSitted.visible  = visible;
        saddleFrontSitted.visible = visible;
        bagSitted.visible         = visible;
    }
}
