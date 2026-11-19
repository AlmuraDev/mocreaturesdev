package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityCrocodile;
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

public class MoCModelCrocodile<T extends MoCEntityCrocodile> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "crocodile"), "main"
    );

    // All former ModelRenderer fields become ModelPart:
    private final ModelPart LJaw;
    private final ModelPart TailA;
    private final ModelPart TailB;
    private final ModelPart TailC;
    private final ModelPart UJaw;
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart Leg1;
    private final ModelPart Leg3;
    private final ModelPart Leg2;
    private final ModelPart Leg4;
    private final ModelPart TailD;
    private final ModelPart Leg1A;
    private final ModelPart Leg2A;
    private final ModelPart Leg3A;
    private final ModelPart Leg4A;
    private final ModelPart UJaw2;
    private final ModelPart LJaw2;
    private final ModelPart TeethA;
    private final ModelPart TeethB;
    private final ModelPart TeethC;
    private final ModelPart TeethD;
    private final ModelPart TeethF;
    private final ModelPart Spike0;
    private final ModelPart Spike1;
    private final ModelPart Spike2;
    private final ModelPart Spike3;
    private final ModelPart Spike4;
    private final ModelPart Spike5;
    private final ModelPart Spike6;
    private final ModelPart Spike7;
    private final ModelPart Spike8;
    private final ModelPart Spike9;
    private final ModelPart Spike10;
    private final ModelPart Spike11;
    private final ModelPart SpikeBack0;
    private final ModelPart SpikeBack1;
    private final ModelPart SpikeBack2;
    private final ModelPart SpikeBack3;
    private final ModelPart SpikeBack4;
    private final ModelPart SpikeBack5;
    private final ModelPart SpikeEye;
    private final ModelPart SpikeEye1;
    private final ModelPart TeethA1;
    private final ModelPart TeethB1;
    private final ModelPart TeethC1;
    private final ModelPart TeethD1;

    // These two get set from the entity each frame:
    public float biteProgress;
    public boolean swimming;
    public boolean resting;

    public MoCModelCrocodile(ModelPart root) {
        super(root);
        this.LJaw      = root.getChild("LJaw");
        this.TailA     = root.getChild("TailA");
        this.TailB     = root.getChild("TailB");
        this.TailC     = root.getChild("TailC");
        this.UJaw      = root.getChild("UJaw");
        this.Head      = root.getChild("Head");
        this.Body      = root.getChild("Body");
        this.Leg1      = root.getChild("Leg1");
        this.Leg3      = root.getChild("Leg3");
        this.Leg2      = root.getChild("Leg2");
        this.Leg4      = root.getChild("Leg4");
        this.TailD     = root.getChild("TailD");
        this.Leg1A     = root.getChild("Leg1A");
        this.Leg2A     = root.getChild("Leg2A");
        this.Leg3A     = root.getChild("Leg3A");
        this.Leg4A     = root.getChild("Leg4A");
        this.UJaw2     = root.getChild("UJaw2");
        this.LJaw2     = root.getChild("LJaw2");
        this.TeethA    = root.getChild("TeethA");
        this.TeethB    = root.getChild("TeethB");
        this.TeethC    = root.getChild("TeethC");
        this.TeethD    = root.getChild("TeethD");
        this.TeethF    = root.getChild("TeethF");
        this.Spike0    = root.getChild("Spike0");
        this.Spike1    = root.getChild("Spike1");
        this.Spike2    = root.getChild("Spike2");
        this.Spike3    = root.getChild("Spike3");
        this.Spike4    = root.getChild("Spike4");
        this.Spike5    = root.getChild("Spike5");
        this.Spike6    = root.getChild("Spike6");
        this.Spike7    = root.getChild("Spike7");
        this.Spike8    = root.getChild("Spike8");
        this.Spike9    = root.getChild("Spike9");
        this.Spike10   = root.getChild("Spike10");
        this.Spike11   = root.getChild("Spike11");
        this.SpikeBack0 = root.getChild("SpikeBack0");
        this.SpikeBack1 = root.getChild("SpikeBack1");
        this.SpikeBack2 = root.getChild("SpikeBack2");
        this.SpikeBack3 = root.getChild("SpikeBack3");
        this.SpikeBack4 = root.getChild("SpikeBack4");
        this.SpikeBack5 = root.getChild("SpikeBack5");
        this.SpikeEye   = root.getChild("SpikeEye");
        this.SpikeEye1  = root.getChild("SpikeEye1");
        this.TeethA1   = root.getChild("TeethA1");
        this.TeethB1   = root.getChild("TeethB1");
        this.TeethC1   = root.getChild("TeethC1");
        this.TeethD1   = root.getChild("TeethD1");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Lower Jaw
        root.addOrReplaceChild("LJaw",
                CubeListBuilder.create()
                        .texOffs(42, 0)
                        .addBox(-2.5F,  1F, -12F,  5, 2, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        // Tail A
        root.addOrReplaceChild("TailA",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -0.5F,  0F,  8, 4, 8),
                PartPose.offset(0F, 17F, 12F)
        );

        // Tail B
        root.addOrReplaceChild("TailB",
                CubeListBuilder.create()
                        .texOffs(2, 0)
                        .addBox(-3F,  0F,  8F,  6, 3, 8),
                PartPose.offset(0F, 17F, 12F)
        );

        // Tail C
        root.addOrReplaceChild("TailC",
                CubeListBuilder.create()
                        .texOffs(6, 2)
                        .addBox(-2F,  0.5F, 16F,  4, 2, 6),
                PartPose.offset(0F, 17F, 12F)
        );

        // Tail D
        root.addOrReplaceChild("TailD",
                CubeListBuilder.create()
                        .texOffs(7, 2)
                        .addBox(-1.5F, 1F, 22F,  3, 1, 6),
                PartPose.offset(0F, 17F, 12F)
        );

        // Upper Jaw
        root.addOrReplaceChild("UJaw",
                CubeListBuilder.create()
                        .texOffs(44, 8)
                        .addBox(-2F, -1F, -12F,  4, 2, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        // Head
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3F, -2F, -6F,  6, 5, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        // Body
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(4, 7)
                        .addBox(0F,  0F,  0F,  10, 5, 20),
                PartPose.offset(-5F, 16F, -8F)
        );

        // Leg 1 (front right)
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create()
                        .texOffs(49, 21)
                        .addBox(1F,  2F, -3F,  3, 2, 4),
                PartPose.offset(5F, 19F, -3F)
        );

        // Leg 3 (hind right)
        root.addOrReplaceChild("Leg3",
                CubeListBuilder.create()
                        .texOffs(48, 20)
                        .addBox(1F,  2F, -3F,  3, 2, 5),
                PartPose.offset(5F, 19F, 9F)
        );

        // Leg 2 (front left)
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create()
                        .texOffs(49, 21)
                        .addBox(-4F, 2F, -3F,  3, 2, 4),
                PartPose.offset(-5F, 19F, -3F)
        );

        // Leg 4 (hind left)
        root.addOrReplaceChild("Leg4",
                CubeListBuilder.create()
                        .texOffs(48, 20)
                        .addBox(-4F, 2F, -3F,  3, 2, 5),
                PartPose.offset(-5F, 19F, 9F)
        );

        // Lower segment Leg1A (front right)
        root.addOrReplaceChild("Leg1A",
                CubeListBuilder.create()
                        .texOffs(7, 9)
                        .addBox(0F, -1F, -2F,  3, 3, 3),
                PartPose.offset(5F, 19F, -3F)
        );

        // Lower segment Leg2A (front left)
        root.addOrReplaceChild("Leg2A",
                CubeListBuilder.create()
                        .texOffs(7, 9)
                        .addBox(-3F, -1F, -2F,  3, 3, 3),
                PartPose.offset(-5F, 19F, -3F)
        );

        // Lower segment Leg3A (hind right)
        root.addOrReplaceChild("Leg3A",
                CubeListBuilder.create()
                        .texOffs(6, 8)
                        .addBox(0F, -1F, -2F,  3, 3, 4),
                PartPose.offset(5F, 19F, 9F)
        );

        // Lower segment Leg4A (hind left)
        root.addOrReplaceChild("Leg4A",
                CubeListBuilder.create()
                        .texOffs(6, 8)
                        .addBox(-3F, -1F, -2F,  3, 3, 4),
                PartPose.offset(-5F, 19F, 9F)
        );

        // Upper Jaw II
        root.addOrReplaceChild("UJaw2",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(-1.5F, -1F, -16F,  3, 2, 4),
                PartPose.offset(0F, 18F, -8F)
        );

        // Lower Jaw II
        root.addOrReplaceChild("LJaw2",
                CubeListBuilder.create()
                        .texOffs(24, 1)
                        .addBox(-2F, 1F, -16F,  4, 2, 4),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth A
        root.addOrReplaceChild("TeethA",
                CubeListBuilder.create()
                        .texOffs(8, 11)
                        .addBox(1.6F, 0F, -16F,  0, 1, 4),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth B
        root.addOrReplaceChild("TeethB",
                CubeListBuilder.create()
                        .texOffs(8, 11)
                        .addBox(-1.6F, 0F, -16F,  0, 1, 4),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth C
        root.addOrReplaceChild("TeethC",
                CubeListBuilder.create()
                        .texOffs(6, 9)
                        .addBox(2.1F, 0F, -12F,  0, 1, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth D
        root.addOrReplaceChild("TeethD",
                CubeListBuilder.create()
                        .texOffs(6, 9)
                        .addBox(-2.1F, 0F, -12F,  0, 1, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth F
        root.addOrReplaceChild("TeethF",
                CubeListBuilder.create()
                        .texOffs(19, 21)
                        .addBox(-1F, 0F, -16.1F,  2, 1, 0),
                PartPose.offset(0F, 18F, -8F)
        );

        // Spikes on tail (Spike0..Spike11)
        root.addOrReplaceChild("Spike0",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-1F, -1F, 23F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike1",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(1F, -1F, 23F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike2",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-1.5F, -1.5F, 17F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike3",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(1.5F, -1.5F, 17F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike4",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-2F, -2F, 12F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike5",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(2F, -2F, 12F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike6",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-2.5F, -2F, 8F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike7",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(2.5F, -2F, 8F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike8",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-3F, -2.5F, 4F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike9",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(3F, -2.5F, 4F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike10",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(3.5F, -2.5F, 0F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );
        root.addOrReplaceChild("Spike11",
                CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-3.5F, -2.5F, 0F,  0, 2, 4),
                PartPose.offset(0F, 17F, 12F)
        );

        // Spikes on back (SpikeBack0..5)
        root.addOrReplaceChild("SpikeBack0",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(0F, 14F, 3F)
        );
        root.addOrReplaceChild("SpikeBack1",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(0F, 14F, -6F)
        );
        root.addOrReplaceChild("SpikeBack2",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(4F, 14F, -8F)
        );
        root.addOrReplaceChild("SpikeBack3",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(-4F, 14F, -8F)
        );
        root.addOrReplaceChild("SpikeBack4",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(-4F, 14F, 1F)
        );
        root.addOrReplaceChild("SpikeBack5",
                CubeListBuilder.create()
                        .texOffs(44, 10)
                        .addBox(0F, 0F, 0F,  0, 2, 8),
                PartPose.offset(4F, 14F, 1F)
        );

        // Spikes near eyes
        root.addOrReplaceChild("SpikeEye",
                CubeListBuilder.create()
                        .texOffs(44, 14)
                        .addBox(-3F, -3F, -6F,  0, 1, 2),
                PartPose.offset(0F, 18F, -8F)
        );
        root.addOrReplaceChild("SpikeEye1",
                CubeListBuilder.create()
                        .texOffs(44, 14)
                        .addBox(3F, -3F, -6F,  0, 1, 2),
                PartPose.offset(0F, 18F, -8F)
        );

        // Teeth on upper jaw
        root.addOrReplaceChild("TeethA1",
                CubeListBuilder.create()
                        .texOffs(52, 12)
                        .addBox(1.4F, 1F, -16.4F,  0, 1, 4),
                PartPose.offset(0F, 18F, -8F)
        );
        root.addOrReplaceChild("TeethB1",
                CubeListBuilder.create()
                        .texOffs(52, 12)
                        .addBox(-1.4F, 1F, -16.4F,  0, 1, 4),
                PartPose.offset(0F, 18F, -8F)
        );
        root.addOrReplaceChild("TeethC1",
                CubeListBuilder.create()
                        .texOffs(50, 10)
                        .addBox(1.9F, 1F, -12.5F,  0, 1, 6),
                PartPose.offset(0F, 18F, -8F)
        );
        root.addOrReplaceChild("TeethD1",
                CubeListBuilder.create()
                        .texOffs(50, 10)
                        .addBox(-1.9F, 1F, -12.5F,  0, 1, 6),
                PartPose.offset(0F, 18F, -8F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // Copy exactly what your old setRotationAngles(...) did:
        this.biteProgress = entity.biteProgress;    // assume your entity exposes this
        this.swimming     = entity.isSwimming();    // assume a boolean getter
        this.resting      = entity.getIsSitting();     // assume a boolean getter

        // Head yaw/pitch:
        this.Head.xRot = headPitch / 57.29578F;
        this.Head.yRot = netHeadYaw / 57.29578F;

        // SpikeEye mirrors head rotation:
        this.SpikeEye.xRot  = this.Head.xRot;
        this.SpikeEye.yRot  = this.Head.yRot;
        this.SpikeEye1.xRot = this.Head.xRot;
        this.SpikeEye1.yRot = this.Head.yRot;

        // Lower/Upper jaws rotate around Y to match head yaw:
        this.LJaw.yRot  = this.Head.yRot;
        this.LJaw2.yRot = this.Head.yRot;
        this.UJaw.yRot  = this.Head.yRot;
        this.UJaw2.yRot = this.Head.yRot;

        // Handle swimming state:
        if (this.swimming) {
            // Move legs underneath body and flip them backward:
            this.Leg1.setPos(9F, 18F, 0F);
            this.Leg1.yRot = (float) Math.PI;
            this.Leg2.setPos(-9F, 18F, 0F);
            this.Leg2.yRot = (float) Math.PI;
            this.Leg3.setPos(8F, 18F, 12F);
            this.Leg3.yRot = (float) Math.PI;
            this.Leg4.setPos(-8F, 18F, 12F);
            this.Leg4.yRot = (float) Math.PI;

            // Flip the lower segments straight down:
            this.Leg1A.setPos(5F, 19F, -3F);
            this.Leg1A.xRot = (float) (Math.PI / 2);
            this.Leg2A.setPos(-5F, 19F, -3F);
            this.Leg2A.xRot = (float) (Math.PI / 2);
            this.Leg3A.setPos(5F, 19F, 9F);
            this.Leg3A.xRot = (float) (Math.PI / 2);
            this.Leg4A.setPos(-5F, 19F, 9F);
            this.Leg4A.xRot = (float) (Math.PI / 2);

            // Zero out Z-rotations so claws dangle straight:
            this.Leg1.zRot = 0F;
            this.Leg1A.zRot = 0F;
            this.Leg2.zRot = 0F;
            this.Leg2A.zRot = 0F;
            this.Leg3.zRot = 0F;
            this.Leg3A.zRot = 0F;
            this.Leg4.zRot = 0F;
            this.Leg4A.zRot = 0F;

        } else if (this.resting) {
            // Tuck legs under the body at diagonal angles:
            this.Leg1.setPos(6F, 17F, -6F);
            this.Leg1.yRot = -0.7854F;
            this.Leg2.setPos(-6F, 17F, -6F);
            this.Leg2.yRot =  0.7854F;
            this.Leg3.setPos(7F, 17F, 7F);
            this.Leg3.yRot = -0.7854F;
            this.Leg4.setPos(-7F, 17F, 7F);
            this.Leg4.yRot =  0.7854F;

            this.Leg1A.setPos(5F, 17F, -3F);
            this.Leg1A.xRot = 0F;
            this.Leg2A.setPos(-5F, 17F, -3F);
            this.Leg2A.xRot = 0F;
            this.Leg3A.setPos(5F, 17F, 9F);
            this.Leg3A.xRot = 0F;
            this.Leg4A.setPos(-5F, 17F, 9F);
            this.Leg4A.xRot = 0F;

            // Ensure claws point roughly level:
            this.Leg1.zRot = 0F;
            this.Leg1A.zRot = 0F;
            this.Leg2.zRot = 0F;
            this.Leg2A.zRot = 0F;
            this.Leg3.zRot = 0F;
            this.Leg3A.zRot = 0F;
            this.Leg4.zRot = 0F;
            this.Leg4A.zRot = 0F;

        } else {
            // Normal walking stance:
            this.Leg1.setPos(5F, 19F, -3F);
            this.Leg2.setPos(-5F, 19F, -3F);
            this.Leg3.setPos(5F, 19F, 9F);
            this.Leg4.setPos(-5F, 19F, 9F);
            this.Leg1.yRot = 0F;
            this.Leg2.yRot = 0F;
            this.Leg3.yRot = 0F;
            this.Leg4.yRot = 0F;

            this.Leg1A.setPos(5F, 19F, -3F);
            this.Leg2A.setPos(-5F, 19F, -3F);
            this.Leg3A.setPos(5F, 19F, 9F);
            this.Leg4A.setPos(-5F, 19F, 9F);

            // Flip legs in/out in a walking gait:
            this.Leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.Leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.Leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.Leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

            // Keep Y-rot zero so claws face forward:
            this.Leg1.yRot = 0F;
            this.Leg2.yRot = 0F;
            this.Leg3.yRot = 0F;
            this.Leg4.yRot = 0F;

            // Sync lower claws to upper segment:
            this.Leg1A.xRot = this.Leg1.xRot;
            this.Leg2A.xRot = this.Leg2.xRot;
            this.Leg3A.xRot = this.Leg3.xRot;
            this.Leg4A.xRot = this.Leg4.xRot;

            // Make legs angle in/out slightly (latitude motion)
            float latrot = Mth.cos(limbSwing / 1.919107651F) * 0.261799387799149F * limbSwingAmount * 5F;
            this.Leg1.zRot =  latrot;
            this.Leg1A.zRot = latrot;
            this.Leg3.zRot =  latrot;
            this.Leg3A.zRot = latrot;
            this.Leg2.zRot = -latrot;
            this.Leg2A.zRot = -latrot;
            this.Leg4.zRot = -latrot;
            this.Leg4A.zRot = -latrot;
        }

        // Tail swaying:
        float tailYaw = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
        this.TailA.yRot = tailYaw;
        this.TailB.yRot = tailYaw;
        this.TailC.yRot = tailYaw;
        this.TailD.yRot = tailYaw;

        // Rotate all tail-spikes in sync
        this.Spike0.yRot  = tailYaw;
        this.Spike1.yRot  = tailYaw;
        this.Spike2.yRot  = tailYaw;
        this.Spike3.yRot  = tailYaw;
        this.Spike4.yRot  = tailYaw;
        this.Spike5.yRot  = tailYaw;
        this.Spike6.yRot  = tailYaw;
        this.Spike7.yRot  = tailYaw;
        this.Spike8.yRot  = tailYaw;
        this.Spike9.yRot  = tailYaw;
        this.Spike10.yRot = tailYaw;
        this.Spike11.yRot = tailYaw;

        // Handle bite animation: UJaw and LJaw open/close around head X‐rotation
        float f = this.biteProgress;
        float f2 = f >= 0.5F ? (0.5F - (f - 0.5F)) : f;
        this.UJaw.xRot  = this.Head.xRot - f2;
        this.UJaw2.xRot = this.UJaw.xRot;
        this.LJaw.xRot  = this.Head.xRot + (f2 / 2F);
        this.LJaw2.xRot = this.LJaw.xRot;

        // Sync all lower teeth to lower jaw:
        this.TeethA.xRot = this.LJaw.xRot;
        this.TeethB.xRot = this.LJaw.xRot;
        this.TeethC.xRot = this.LJaw.xRot;
        this.TeethD.xRot = this.LJaw.xRot;
        this.TeethF.xRot = this.LJaw.xRot;
        this.TeethA.yRot = this.LJaw.yRot;
        this.TeethB.yRot = this.LJaw.yRot;
        this.TeethC.yRot = this.LJaw.yRot;
        this.TeethD.yRot = this.LJaw.yRot;
        this.TeethF.yRot = this.LJaw.yRot;

        // Sync upper teeth to upper jaw:
        this.TeethA1.xRot = this.UJaw.xRot;
        this.TeethB1.xRot = this.UJaw.xRot;
        this.TeethC1.xRot = this.UJaw.xRot;
        this.TeethD1.xRot = this.UJaw.xRot;
        this.TeethA1.yRot = this.UJaw.yRot;
        this.TeethB1.yRot = this.UJaw.yRot;
        this.TeethC1.yRot = this.UJaw.yRot;
        this.TeethD1.yRot = this.UJaw.yRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.LJaw.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UJaw.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UJaw2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LJaw2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethF.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike0.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike6.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike7.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike8.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike9.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike10.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Spike11.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack0.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeBack5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeEye.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SpikeEye1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethA1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethB1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethC1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethD1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
