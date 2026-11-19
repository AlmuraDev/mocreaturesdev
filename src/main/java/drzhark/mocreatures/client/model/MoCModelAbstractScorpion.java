package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public abstract class MoCModelAbstractScorpion<T extends net.minecraft.world.entity.Entity> extends MoCEntityModel<T> {
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "scorpion"), "main");

    // Fields correspond to all the pieces from the old 1.16 ModelRenderer:
    private final ModelPart head;
    private final ModelPart mouthL;
    private final ModelPart mouthR;
    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart sting1;
    private final ModelPart sting2;
    private final ModelPart lArm1;
    private final ModelPart lArm2;
    private final ModelPart lArm3;
    private final ModelPart lArm4;
    private final ModelPart rArm1;
    private final ModelPart rArm2;
    private final ModelPart rArm3;
    private final ModelPart rArm4;
    private final ModelPart leg1A;
    private final ModelPart leg1B;
    private final ModelPart leg1C;
    private final ModelPart leg2A;
    private final ModelPart leg2B;
    private final ModelPart leg2C;
    private final ModelPart leg3A;
    private final ModelPart leg3B;
    private final ModelPart leg3C;
    private final ModelPart leg4A;
    private final ModelPart leg4B;
    private final ModelPart leg4C;
    private final ModelPart leg5A;
    private final ModelPart leg5B;
    private final ModelPart leg5C;
    private final ModelPart leg6A;
    private final ModelPart leg6B;
    private final ModelPart leg6C;
    private final ModelPart leg7A;
    private final ModelPart leg7B;
    private final ModelPart leg7C;
    private final ModelPart leg8A;
    private final ModelPart leg8B;
    private final ModelPart leg8C;
    private final ModelPart baby1;
    private final ModelPart baby2;
    private final ModelPart baby3;
    private final ModelPart baby4;
    private final ModelPart baby5;

    // State variables (same as before):
    protected boolean poisoning;
    protected boolean isAdult;
    protected boolean isTalking;
    protected boolean babies;
    protected int attacking;
    protected boolean sitting;

    public MoCModelAbstractScorpion(ModelPart root) {
        super(root);
        this.head     = root.getChild("head");
        this.mouthL   = root.getChild("mouth_l");
        this.mouthR   = root.getChild("mouth_r");
        this.body     = root.getChild("body");
        this.tail1    = root.getChild("tail1");
        this.tail2    = root.getChild("tail2");
        this.tail3    = root.getChild("tail3");
        this.tail4    = root.getChild("tail4");
        this.tail5    = root.getChild("tail5");
        this.sting1   = root.getChild("sting1");
        this.sting2   = root.getChild("sting2");
        this.lArm1    = root.getChild("l_arm1");
        this.lArm2    = root.getChild("l_arm2");
        this.lArm3    = root.getChild("l_arm3");
        this.lArm4    = root.getChild("l_arm4");
        this.rArm1    = root.getChild("r_arm1");
        this.rArm2    = root.getChild("r_arm2");
        this.rArm3    = root.getChild("r_arm3");
        this.rArm4    = root.getChild("r_arm4");
        this.leg1A    = root.getChild("leg1a");
        this.leg1B    = root.getChild("leg1b");
        this.leg1C    = root.getChild("leg1c");
        this.leg2A    = root.getChild("leg2a");
        this.leg2B    = root.getChild("leg2b");
        this.leg2C    = root.getChild("leg2c");
        this.leg3A    = root.getChild("leg3a");
        this.leg3B    = root.getChild("leg3b");
        this.leg3C    = root.getChild("leg3c");
        this.leg4A    = root.getChild("leg4a");
        this.leg4B    = root.getChild("leg4b");
        this.leg4C    = root.getChild("leg4c");
        this.leg5A    = root.getChild("leg5a");
        this.leg5B    = root.getChild("leg5b");
        this.leg5C    = root.getChild("leg5c");
        this.leg6A    = root.getChild("leg6a");
        this.leg6B    = root.getChild("leg6b");
        this.leg6C    = root.getChild("leg6c");
        this.leg7A    = root.getChild("leg7a");
        this.leg7B    = root.getChild("leg7b");
        this.leg7C    = root.getChild("leg7c");
        this.leg8A    = root.getChild("leg8a");
        this.leg8B    = root.getChild("leg8b");
        this.leg8C    = root.getChild("leg8c");
        this.baby1    = root.getChild("baby1");
        this.baby2    = root.getChild("baby2");
        this.baby3    = root.getChild("baby3");
        this.baby4    = root.getChild("baby4");
        this.baby5    = root.getChild("baby5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        /*
         * HEAD
         */
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */ -5F, /* y */ 0F, /* z */ 0F,
                                /* dx */ 10, /* dy */ 5, /* dz */ 13,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(0F, 14F, -9F)
        );

        PartDefinition mouthL = root.addOrReplaceChild("mouth_l",
                CubeListBuilder.create()
                        .texOffs(18, 58)
                        .addBox(
                                /* x */ -3F, /* y */ -2F, /* z */ -1F,
                                /* dx */ 4,  /* dy */ 4,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(3F, 17F, -9F,
                        /* xRot */ 0F,
                        /* yRot */ -0.3839724F,
                        /* zRot */ 0F
                )
        );

        PartDefinition mouthR = root.addOrReplaceChild("mouth_r",
                CubeListBuilder.create()
                        .texOffs(30, 58)
                        .addBox(
                                /* x */ -1F, /* y */ -2F, /* z */ -1F,
                                /* dx */ 4,  /* dy */ 4,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-3F, 17F, -9F,
                        /* xRot */ 0F,
                        /* yRot */ 0.3839724F,
                        /* zRot */ 0F
                )
        );

        /*
         * BODY
         */
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(
                                /* x */ -4F, /* y */ -2F, /* z */ 0F,
                                /* dx */ 8,  /* dy */ 4,  /* dz */ 10,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 17F, 3F,
                        /* xRot */ 0.0872665F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        /*
         * TAIL SEGMENTS
         */
        PartDefinition tail1 = root.addOrReplaceChild("tail1",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(
                                /* x */ -3F, /* y */ -2F, /* z */ 0F,
                                /* dx */ 6,  /* dy */ 4,  /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 16F, 12F,
                        /* xRot */ 0.6108652F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        PartDefinition tail2 = root.addOrReplaceChild("tail2",
                CubeListBuilder.create()
                        .texOffs(0, 42)
                        .addBox(
                                /* x */ -2F, /* y */ -2F, /* z */ 0F,
                                /* dx */ 4,  /* dy */ 4,  /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 13F, 16.5F,
                        /* xRot */ 1.134464F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        PartDefinition tail3 = root.addOrReplaceChild("tail3",
                CubeListBuilder.create()
                        .texOffs(0, 52)
                        .addBox(
                                /* x */ -1.5F, /* y */ -1.5F, /* z */ 0F,
                                /* dx */ 3,    /* dy */ 3,    /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 8F, 18.5F,
                        /* xRot */ 1.692143F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        PartDefinition tail4 = root.addOrReplaceChild("tail4",
                CubeListBuilder.create()
                        .texOffs(24, 32)
                        .addBox(
                                /* x */ -1.5F, /* y */ -1.5F, /* z */ 0F,
                                /* dx */ 3,    /* dy */ 3,    /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 3F, 18F,
                        /* xRot */ 2.510073F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        PartDefinition tail5 = root.addOrReplaceChild("tail5",
                CubeListBuilder.create()
                        .texOffs(24, 41)
                        .addBox(
                                /* x */ -1.5F, /* y */ -1.5F, /* z */ 0F,
                                /* dx */ 3,    /* dy */ 3,    /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, -0.2F, 14F,
                        /* xRot */ 3.067752F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        /*
         * STING
         */
        PartDefinition sting1 = root.addOrReplaceChild("sting1",
                CubeListBuilder.create()
                        .texOffs(30, 50)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -1.5F,
                                /* dx */ 3,    /* dy */ 5, /* dz */ 3,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, -1F, 7F,
                        /* xRot */ 0.4089647F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        PartDefinition sting2 = root.addOrReplaceChild("sting2",
                CubeListBuilder.create()
                        .texOffs(26, 50)
                        .addBox(
                                /* x */ -0.5F, /* y */ 0F, /* z */ 0.5F,
                                /* dx */ 1,    /* dy */ 4, /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(0F, 2.6F, 8.8F,
                        /* xRot */ -0.2230717F, /* yRot */ 0F, /* zRot */ 0F
                )
        );

        /*
         * LEFT ARMS (CLAW)
         */
        PartDefinition lArm1 = root.addOrReplaceChild("l_arm1",
                CubeListBuilder.create()
                        .texOffs(26, 18)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -8F,
                        /* xRot */ -0.3490659F, /* yRot */ 0F, /* zRot */ 0.8726646F
                )
        );

        PartDefinition lArm2 = root.addOrReplaceChild("l_arm2",
                CubeListBuilder.create()
                        .texOffs(42, 55)
                        .addBox(
                                /* x */ -1.5F, /* y */ -1.5F, /* z */ -6F,
                                /* dx */ 3,    /* dy */ 3,    /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(10F, 14F, -6F,
                        /* xRot */ 0.1745329F, /* yRot */ -0.3490659F, /* zRot */ -0.2617994F
                )
        );

        PartDefinition lArm3 = root.addOrReplaceChild("l_arm3",
                CubeListBuilder.create()
                        .texOffs(42, 39)
                        .addBox(
                                /* x */ -0.5F, /* y */ -0.5F, /* z */ -7F,
                                /* dx */ 2,    /* dy */ 1,    /* dz */ 7,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(12F, 15F, -11F,
                        /* xRot */ 0.2617994F, /* yRot */ 0.1570796F, /* zRot */ -0.1570796F
                )
        );

        PartDefinition lArm4 = root.addOrReplaceChild("l_arm4",
                CubeListBuilder.create()
                        .texOffs(42, 31)
                        .addBox(
                                /* x */ -1.5F, /* y */ -0.5F, /* z */ -6F,
                                /* dx */ 1,    /* dy */ 1,    /* dz */ 7,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(11F, 15F, -11F,
                        /* xRot */ 0.2617994F, /* yRot */ 0F, /* zRot */ -0.1570796F
                )
        );

        /*
         * RIGHT ARMS (CLAW)
         */
        PartDefinition rArm1 = root.addOrReplaceChild("r_arm1",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -8F,
                        /* xRot */ -0.3490659F, /* yRot */ 0F, /* zRot */ -0.8726646F
                )
        );

        PartDefinition rArm2 = root.addOrReplaceChild("r_arm2",
                CubeListBuilder.create()
                        .texOffs(42, 55)
                        .addBox(
                                /* x */ -1.5F, /* y */ -1.5F, /* z */ -6F,
                                /* dx */ 3,    /* dy */ 3,    /* dz */ 6,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-10F, 14F, -6F,
                        /* xRot */ 0.1745329F, /* yRot */ 0.3490659F, /* zRot */ 0.2617994F
                )
        );

        PartDefinition rArm3 = root.addOrReplaceChild("r_arm3",
                CubeListBuilder.create()
                        .texOffs(42, 47)
                        .addBox(
                                /* x */ -1.5F, /* y */ -0.5F, /* z */ -7F,
                                /* dx */ 2,    /* dy */ 1,    /* dz */ 7,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-12F, 15F, -11F,
                        /* xRot */ 0.2617994F, /* yRot */ -0.1570796F, /* zRot */ 0.1570796F
                )
        );

        PartDefinition rArm4 = root.addOrReplaceChild("r_arm4",
                CubeListBuilder.create()
                        .texOffs(42, 31)
                        .addBox(
                                /* x */ 0.5F, /* y */ -0.5F, /* z */ -6F,
                                /* dx */ 1,   /* dy */ 1,    /* dz */ 7,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-11F, 15F, -11F,
                        /* xRot */ 0.2617994F, /* yRot */ 0F, /* zRot */ 0.1570796F
                )
        );

        /*
         * LEGS 1–4 (RIGHT SIDE)
         */
        PartDefinition leg1A = root.addOrReplaceChild("leg1a",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -5F,
                        -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 75F * ((float)Math.PI / 180F))
        );

        PartDefinition leg1B = root.addOrReplaceChild("leg1b",
                CubeListBuilder.create()
                        .texOffs(50, 0)
                        .addBox(
                                /* x */ 2F, /* y */ -8F, /* z */ -1F,
                                /* dx */ 5, /* dy */ 2,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -5F,
                        -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg1C = root.addOrReplaceChild("leg1c",
                CubeListBuilder.create()
                        .texOffs(52, 16)
                        .addBox(
                                /* x */ 4.5F, /* y */ -9F, /* z */ -0.7F,
                                /* dx */ 5,   /* dy */ 1,   /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -5F,
                        /* xDeg */ -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 75F * ((float)Math.PI / 180F))
        );

        PartDefinition leg2A = root.addOrReplaceChild("leg2a",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg2B = root.addOrReplaceChild("leg2b",
                CubeListBuilder.create()
                        .texOffs(50, 4)
                        .addBox(
                                /* x */ 1F,  /* y */ -8F, /* z */ -1F,
                                /* dx */ 5,  /* dy */ 2,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg2C = root.addOrReplaceChild("leg2c",
                CubeListBuilder.create()
                        .texOffs(50, 18)
                        .addBox(
                                /* x */ 4F, /* y */ -8.5F, /* z */ -1F,
                                /* dx */ 6, /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg3A = root.addOrReplaceChild("leg3a",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg3B = root.addOrReplaceChild("leg3b",
                CubeListBuilder.create()
                        .texOffs(48, 8)
                        .addBox(
                                /* x */ 1F, /* y */ -8F, /* z */ -1F,
                                /* dx */ 6, /* dy */ 2,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg3C = root.addOrReplaceChild("leg3c",
                CubeListBuilder.create()
                        .texOffs(50, 20)
                        .addBox(
                                /* x */ 4.5F, /* y */ -8.2F, /* z */ -1.3F,
                                /* dx */ 6,   /* dy */ 1,   /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg4A = root.addOrReplaceChild("leg4a",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg4B = root.addOrReplaceChild("leg4b",
                CubeListBuilder.create()
                        .texOffs(46, 12)
                        .addBox(
                                /* x */ 0.5F, /* y */ -8.5F, /* z */ -1F,
                                /* dx */ 7,   /* dy */ 2,    /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg4C = root.addOrReplaceChild("leg4c",
                CubeListBuilder.create()
                        .texOffs(48, 22)
                        .addBox(
                                /* x */ 3.5F, /* y */ -8.5F, /* z */ -1.5F,
                                /* dx */ 7,   /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ 70F * ((float)Math.PI / 180F))
        );

        /*
         * LEGS 5–8 (LEFT SIDE)
         */
        PartDefinition leg5A = root.addOrReplaceChild("leg5a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -5F,
                        /* xDeg */ -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -75F * ((float)Math.PI / 180F))
        );

        PartDefinition leg5B = root.addOrReplaceChild("leg5b",
                CubeListBuilder.create()
                        .texOffs(50, 0)
                        .addBox(
                                /* x */ -7F, /* y */ -8F, /* z */ -1F,
                                /* dx */ 5,   /* dy */ 2,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -5F,
                        /* xDeg */ -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg5C = root.addOrReplaceChild("leg5c",
                CubeListBuilder.create()
                        .texOffs(52, 16)
                        .addBox(
                                /* x */ -9.5F, /* y */ -9F, /* z */ -0.7F,
                                /* dx */ 5,    /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -5F,
                        /* xDeg */ -10F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -75F * ((float)Math.PI / 180F))
        );

        PartDefinition leg6A = root.addOrReplaceChild("leg6a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg6B = root.addOrReplaceChild("leg6b",
                CubeListBuilder.create()
                        .texOffs(50, 4)
                        .addBox(
                                /* x */ -6F,   /* y */ -8F, /* z */ -1F,
                                /* dx */ 5,    /* dy */ 2,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg6C = root.addOrReplaceChild("leg6c",
                CubeListBuilder.create()
                        .texOffs(50, 18)
                        .addBox(
                                /* x */ -10F, /* y */ -8.5F, /* z */ -1F,
                                /* dx */ 6,   /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 18F, -2F,
                        /* xDeg */ -30F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg7A = root.addOrReplaceChild("leg7a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg7B = root.addOrReplaceChild("leg7b",
                CubeListBuilder.create()
                        .texOffs(48, 8)
                        .addBox(
                                /* x */ -7F,   /* y */ -8.5F, /* z */ -1F,
                                /* dx */ 6,    /* dy */ 2,    /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg7C = root.addOrReplaceChild("leg7c",
                CubeListBuilder.create()
                        .texOffs(50, 20)
                        .addBox(
                                /* x */ -10.5F, /* y */ -8.7F, /* z */ -1.3F,
                                /* dx */ 6,     /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17.5F, 1F,
                        /* xDeg */ -45F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg8A = root.addOrReplaceChild("leg8a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x */ -1F, /* y */ -7F, /* z */ -1F,
                                /* dx */ 2,  /* dy */ 7,  /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -70F * ((float)Math.PI / 180F))
        );

        PartDefinition leg8B = root.addOrReplaceChild("leg8b",
                CubeListBuilder.create()
                        .texOffs(46, 12)
                        .addBox(
                                /* x */ -7.5F, /* y */ -8.5F, /* z */ -1F,
                                /* dx */ 7,     /* dy */ 2,    /* dz */ 2,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -60F * ((float)Math.PI / 180F))
        );

        PartDefinition leg8C = root.addOrReplaceChild("leg8c",
                CubeListBuilder.create()
                        .texOffs(48, 22)
                        .addBox(
                                /* x */ -10.5F, /* y */ -8.5F, /* z */ -1.5F,
                                /* dx */ 7,      /* dy */ 1,    /* dz */ 1,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(-5F, 17F, 4F,
                        /* xDeg */ -60F * ((float)Math.PI / 180F),
                        /* yDeg */ 0F,
                        /* zDeg */ -70F * ((float)Math.PI / 180F))
        );

        /*
         * BABIES (five little scorpions)
         */
        PartDefinition baby1 = root.addOrReplaceChild("baby1",
                CubeListBuilder.create()
                        .texOffs(48, 24)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -2.5F,
                                /* dx */ 3,    /* dy */ 2, /* dz */ 5,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(0F, 12F, 0F)
        );

        PartDefinition baby2 = root.addOrReplaceChild("baby2",
                CubeListBuilder.create()
                        .texOffs(48, 24)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -2.5F,
                                /* dx */ 3,    /* dy */ 2, /* dz */ 5,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        /* x */ -5F, /* y */ 13.4F, /* z */ -1F,
                        /* xRot */ 0.4461433F, /* yRot */ 2.490967F, /* zRot */ 0.5205006F
                )
        );

        PartDefinition baby3 = root.addOrReplaceChild("baby3",
                CubeListBuilder.create()
                        .texOffs(48, 24)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -2.5F,
                                /* dx */ 3,    /* dy */ 2, /* dz */ 5,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        /* x */ -2F, /* y */ 13F, /* z */ 4F,
                        /* xRot */ 0F, /* yRot */ 0.8551081F, /* zRot */ 0F
                )
        );

        PartDefinition baby4 = root.addOrReplaceChild("baby4",
                CubeListBuilder.create()
                        .texOffs(48, 24)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -2.5F,
                                /* dx */ 3,    /* dy */ 2, /* dz */ 5,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        /* x */ 4F, /* y */ 13F, /* z */ 2F,
                        /* xRot */ 0F, /* yRot */ 2.714039F, /* zRot */ -0.3717861F
                )
        );

        PartDefinition baby5 = root.addOrReplaceChild("baby5",
                CubeListBuilder.create()
                        .texOffs(48, 24)
                        .addBox(
                                /* x */ -1.5F, /* y */ 0F, /* z */ -2.5F,
                                /* dx */ 3,    /* dy */ 2, /* dz */ 5,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        /* x */ 1F, /* y */ 13F, /* z */ 8F,
                        /* xRot */ 0F, /* yRot */ -1.189716F, /* zRot */ 0F
                )
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        this.head.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.mouthL.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.mouthR.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.body.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.tail1.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.tail2.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.tail3.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.tail4.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.tail5.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.sting1.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.sting2.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.lArm1.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.lArm2.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.lArm3.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.lArm4.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.rArm1.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.rArm2.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.rArm3.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.rArm4.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg1A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg1B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg1C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg2A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg2B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg2C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg3A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg3B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg3C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg4A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg4B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg4C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg5A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg5B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg5C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg6A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg6B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg6C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg7A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg7B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg7C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg8A.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg8B.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        this.leg8C.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));

        if (babies && isAdult) {
            this.baby1.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
            this.baby2.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
            this.baby3.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
            this.baby4.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
            this.baby5.render(stack, builder, packedLight, packedOverlay, ARGB.colorFromFloat(a, r, g, b));
        }
    }

    private float radianF = 57.29578F; // same as 1/Mth.DEG_TO_RAD

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // EXACTLY copied logic from your old setRotationAngles, but adapted to ModelPart:
        if (!poisoning) {
            this.body.xRot    = 5F  / this.radianF;
            this.tail1.xRot   = 35F / this.radianF;
            this.tail1.y     = 16F;
            this.tail1.z     = 12F;

            this.tail2.xRot   = 65F / this.radianF;
            this.tail2.y     = 13F;
            this.tail2.z     = 16.5F;

            this.tail3.xRot   = 90F / this.radianF;
            this.tail3.y     = 8F;
            this.tail3.z     = 18.5F;

            this.tail4.xRot   = 143F / this.radianF;
            this.tail4.y     = 3F;
            this.tail4.z     = 18F;

            this.tail5.xRot   = 175F / this.radianF;
            this.tail5.y     = -0.2F;
            this.tail5.z     = 14F;

            this.sting1.xRot  = 24F  / this.radianF;
            this.sting1.y     = -1F;
            this.sting1.z     = 7F;

            this.sting2.xRot  = -12F / this.radianF;
            this.sting2.y     = 2.6F;
            this.sting2.z     = 8.8F;
        } else {
            this.body.xRot    = 50F   / this.radianF;
            this.tail1.xRot   = 100F  / this.radianF;
            this.tail1.y     = 9F;
            this.tail1.z     = 10F;

            this.tail2.xRot   = 160F  / this.radianF;
            this.tail2.y     = 3F;
            this.tail2.z     = 9.5F;

            this.tail3.xRot   = -170F / this.radianF;
            this.tail3.y     = 1F;
            this.tail3.z     = 3.5F;

            this.tail4.xRot   = -156F / this.radianF;
            this.tail4.y     = 1.8F;
            this.tail4.z     = -2F;

            this.tail5.xRot   = -154F / this.radianF;
            this.tail5.y     = 3.8F;
            this.tail5.z     = -7F;

            this.sting1.xRot  = -57F  / this.radianF;
            this.sting1.y     = 6F;
            this.sting1.z     = -12F;

            this.sting2.xRot  = -93.7F/ this.radianF;
            this.sting2.y     = 8F;
            this.sting2.z     = -15.2F;
        }

        /*
         * Mouth animation
         */
        float mouthRot = 0F;
        if (isTalking) {
            mouthRot = Mth.cos(ageInTicks * 1.1F) * 0.2F;
        }
        this.mouthR.yRot = (22F / this.radianF) + mouthRot;
        this.mouthL.yRot = (-22F / this.radianF) - mouthRot;

        // Reset L/R arms to default:
        this.lArm1.xRot = -20F / this.radianF;
        this.lArm2.setPos(10F, 14F, -6F);
        this.lArm3.setPos(12F, 15F, -11F);
        this.lArm4.setPos(11F, 15F, -11F);
        this.lArm4.yRot = 0F;

        this.rArm1.xRot = -20F / this.radianF;
        this.rArm2.setPos(-10F, 14F, -6F);
        this.rArm3.setPos(-12F, 15F, -11F);
        this.rArm4.setPos(-11F, 15F, -11F);
        this.rArm4.yRot = 0F;

        /*
         * Random hand animations
         */
        if (attacking == 0) {
            float lHand = 0F;
            float f2a = ageInTicks % 100F;
            if (f2a > 0F && f2a < 20F) {
                lHand = f2a / this.radianF;
            }
            this.lArm3.yRot = (9F / this.radianF) - lHand;
            this.lArm4.yRot = lHand;

            float rHand = 0F;
            float f2b = ageInTicks % 75F;
            if (f2b > 30F && f2b < 50F) {
                rHand = (f2b - 29F) / this.radianF;
            }
            this.rArm3.yRot = (-9F / this.radianF) + rHand;
            this.rArm4.yRot = -rHand;
        } else {
            // Attacking sequence:
            if (attacking > 0 && attacking < 5) {
                // Left arm forward, open
                this.lArm1.xRot    = 50F / this.radianF;
                this.lArm2.setPos(8F, 15F, -13F);
                this.lArm3.setPos(10F, 16F, -18F);
                this.lArm4.setPos(9F, 16F, -18F);
                this.lArm4.yRot    = 40F / this.radianF;
            }
            if (attacking >= 5 && attacking < 10) {
                // Left arm forward, closed
                this.lArm1.xRot    = 70F / this.radianF;
                this.lArm2.setPos(7F, 16F, -14F);
                this.lArm3.setPos(9F, 17F, -19F);
                this.lArm4.setPos(8F, 17F, -19F);
                this.lArm4.yRot    = 0F;
            }
            if (attacking >= 10 && attacking < 15) {
                // Right arm forward, open
                this.rArm1.xRot    = 50F / this.radianF;
                this.rArm2.setPos(-8F, 15F, -13F);
                this.rArm3.setPos(-10F, 16F, -18F);
                this.rArm4.setPos(-9F, 16F, -18F);
                this.rArm4.yRot    = -40F / this.radianF;
            }
            if (attacking >= 15 && attacking < 20) {
                // Right arm forward, closed
                this.rArm1.xRot    = 70F / this.radianF;
                this.rArm2.setPos(-7F, 16F, -14F);
                this.rArm3.setPos(-9F, 17F, -19F);
                this.rArm4.setPos(-8F, 17F, -19F);
                this.rArm4.yRot    = 0F;
            }
        }

        /*
         * Babies animation
         */
        if (babies && isAdult) {
            float fmov = ageInTicks % 100F;
            float fb1 = 0F;
            float fb2 = 142F / this.radianF;
            float fb3 = 49F  / this.radianF;
            float fb4 = 155F / this.radianF;
            float fb5 = -68F / this.radianF;

            if (fmov > 0F && fmov < 20F) {
                fb2 -= Mth.cos(ageInTicks * 0.8F) * 0.3F;
                fb3 -= Mth.cos(ageInTicks * 0.6F) * 0.2F;
                fb1 += Mth.cos(ageInTicks * 0.4F) * 0.4F;
                fb5 += Mth.cos(ageInTicks * 0.7F) * 0.5F;
            }
            if (fmov > 30F && fmov < 50F) {
                fb4 -= Mth.cos(ageInTicks * 0.8F) * 0.4F;
                fb1 += Mth.cos(ageInTicks * 0.7F) * 0.1F;
                fb3 -= Mth.cos(ageInTicks * 0.6F) * 0.2F;
            }
            if (fmov > 80F) {
                fb5 += Mth.cos(ageInTicks * 0.2F) * 0.4F;
                fb2 -= Mth.cos(ageInTicks * 0.6F) * 0.3F;
                fb4 -= Mth.cos(ageInTicks * 0.4F) * 0.2F;
            }

            this.baby1.yRot = fb1;
            this.baby2.yRot = fb2;
            this.baby3.yRot = fb3;
            this.baby4.yRot = fb4;
            this.baby5.yRot = fb5;
        }

        /*
         * LEG ANIMATIONS
         */
        float f9  = -Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F)     * 0.4F * limbSwingAmount;
        float f10 = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F * limbSwingAmount;
        float f11 = -Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI/2F))  * 0.4F * limbSwingAmount;
        float f12 = -Mth.cos(limbSwing * 0.6662F * 2.0F + (3F*(float)Math.PI/2F)) * 0.4F * limbSwingAmount;
        float f13 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F))      * 0.4F * limbSwingAmount;
        float f14 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI)) * 0.4F * limbSwingAmount;
        float f15 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI/2F))) * 0.4F * limbSwingAmount;
        float f16 = Math.abs(Mth.sin(limbSwing * 0.6662F + (3F*(float)Math.PI/2F))) * 0.4F * limbSwingAmount;

        if (sitting) {
            // If sitting, use fixed “sit” poses:
            this.leg1A.xRot = -10F / this.radianF;
            this.leg1A.zRot =  35F / this.radianF;
            this.leg1B.zRot =  20F / this.radianF;
            this.leg1C.zRot =  35F / this.radianF;

            this.leg2A.xRot = -30F / this.radianF;
            this.leg2A.zRot =  35F / this.radianF;
            this.leg2B.zRot =  20F / this.radianF;
            this.leg2C.zRot =  35F / this.radianF;

            this.leg3A.xRot = -45F / this.radianF;
            this.leg3A.zRot =  35F / this.radianF;
            this.leg3B.zRot =  20F / this.radianF;
            this.leg3C.zRot =  35F / this.radianF;

            this.leg4A.xRot = -60F / this.radianF;
            this.leg4A.zRot =  35F / this.radianF;
            this.leg4B.zRot =  20F / this.radianF;
            this.leg4C.zRot =  35F / this.radianF;

            this.leg5A.xRot = -10F / this.radianF;
            this.leg5A.zRot = -35F / this.radianF;
            this.leg5B.zRot = -20F / this.radianF;
            this.leg5C.zRot = -35F / this.radianF;

            this.leg6A.xRot = -30F / this.radianF;
            this.leg6A.zRot = -35F / this.radianF;
            this.leg6B.zRot = -20F / this.radianF;
            this.leg6C.zRot = -35F / this.radianF;

            this.leg7A.xRot = -45F / this.radianF;
            this.leg7A.zRot = -35F / this.radianF;
            this.leg7B.zRot = -20F / this.radianF;
            this.leg7C.zRot = -35F / this.radianF;

            this.leg8A.xRot = -60F / this.radianF;
            this.leg8A.zRot = -35F / this.radianF;
            this.leg8B.zRot = -20F / this.radianF;
            this.leg8C.zRot = -35F / this.radianF;
        } else {
            // Otherwise “walking” stance:
            // LEG1 (right) 
            this.leg1A.xRot = -10F / this.radianF;
            this.leg1A.zRot =  75F / this.radianF;
            this.leg1B.zRot =  60F / this.radianF;
            this.leg1C.zRot =  75F / this.radianF;

            this.leg1A.xRot += f9;
            this.leg1B.xRot = this.leg1A.xRot;
            this.leg1C.xRot = this.leg1A.xRot;

            this.leg1A.zRot += f13;
            this.leg1B.zRot += f13;
            this.leg1C.zRot += f13;

            // LEG2 (right)
            this.leg2A.xRot = -30F / this.radianF;
            this.leg2A.zRot =  70F / this.radianF;
            this.leg2B.zRot =  60F / this.radianF;
            this.leg2C.zRot =  70F / this.radianF;

            this.leg2A.xRot += f10;
            this.leg2B.xRot = this.leg2A.xRot;
            this.leg2C.xRot = this.leg2A.xRot;

            this.leg2A.zRot += f14;
            this.leg2B.zRot += f14;
            this.leg2C.zRot += f14;

            // LEG3 (right)
            this.leg3A.xRot = -45F / this.radianF;
            this.leg3A.zRot =  70F / this.radianF;
            this.leg3B.zRot =  60F / this.radianF;
            this.leg3C.zRot =  70F / this.radianF;

            this.leg3A.xRot += f11;
            this.leg3B.xRot = this.leg3A.xRot;
            this.leg3C.xRot = this.leg3A.xRot;

            this.leg3A.zRot += f15;
            this.leg3B.zRot += f15;
            this.leg3C.zRot += f15;

            // LEG4 (right)
            this.leg4A.xRot = -60F / this.radianF;
            this.leg4A.zRot =  70F / this.radianF;
            this.leg4B.zRot =  60F / this.radianF;
            this.leg4C.zRot =  70F / this.radianF;

            this.leg4A.xRot += f12;
            this.leg4B.xRot = this.leg4A.xRot;
            this.leg4C.xRot = this.leg4A.xRot;

            this.leg4A.zRot += f16;
            this.leg4B.zRot += f16;
            this.leg4C.zRot += f16;

            // LEG5 (left)
            this.leg5A.xRot = -10F / this.radianF;
            this.leg5A.zRot = -75F / this.radianF;
            this.leg5B.zRot = -60F / this.radianF;
            this.leg5C.zRot = -75F / this.radianF;

            this.leg5A.xRot -= f9;
            this.leg5B.xRot = this.leg5A.xRot;
            this.leg5C.xRot = this.leg5A.xRot;

            this.leg5A.zRot -= f13;
            this.leg5B.zRot -= f13;
            this.leg5C.zRot -= f13;

            // LEG6 (left)
            this.leg6A.xRot = -30F / this.radianF;
            this.leg6A.zRot = -70F / this.radianF;
            this.leg6B.zRot = -60F / this.radianF;
            this.leg6C.zRot = -70F / this.radianF;

            this.leg6A.xRot -= f10;
            this.leg6B.xRot = this.leg6A.xRot;
            this.leg6C.xRot = this.leg6A.xRot;

            this.leg6A.zRot -= f14;
            this.leg6B.zRot -= f14;
            this.leg6C.zRot -= f14;

            // LEG7 (left)
            this.leg7A.xRot = -45F / this.radianF;
            this.leg7A.zRot = -70F / this.radianF;
            this.leg7B.zRot = -60F / this.radianF;
            this.leg7C.zRot = -70F / this.radianF;

            this.leg7A.xRot -= f11;
            this.leg7B.xRot = this.leg7A.xRot;
            this.leg7C.xRot = this.leg7A.xRot;

            this.leg7A.zRot -= f15;
            this.leg7B.zRot -= f15;
            this.leg7C.zRot -= f15;

            // LEG8 (left)
            this.leg8A.xRot = -60F / this.radianF;
            this.leg8A.zRot = -70F / this.radianF;
            this.leg8B.zRot = -60F / this.radianF;
            this.leg8C.zRot = -70F / this.radianF;

            this.leg8A.xRot -= f12;
            this.leg8B.xRot = this.leg8A.xRot;
            this.leg8C.xRot = this.leg8A.xRot;

            this.leg8A.zRot -= f16;
            this.leg8B.zRot -= f16;
            this.leg8C.zRot -= f16;
        }
    }
}
