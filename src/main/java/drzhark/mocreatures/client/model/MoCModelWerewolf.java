/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntityWerewolf;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelWerewolf<T extends MoCEntityWerewolf> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "werewolf"), "main"
    );

    private final ModelPart Head;
    private final ModelPart Nose;
    private final ModelPart Snout;
    private final ModelPart TeethU;
    private final ModelPart TeethL;
    private final ModelPart Mouth;
    private final ModelPart LEar;
    private final ModelPart REar;
    private final ModelPart Neck;
    private final ModelPart Neck2;
    private final ModelPart SideburnL;
    private final ModelPart SideburnR;
    private final ModelPart Chest;
    private final ModelPart Abdomen;
    private final ModelPart TailA;
    private final ModelPart TailB;
    private final ModelPart TailC;
    private final ModelPart TailD;
    private final ModelPart RLegA;
    private final ModelPart RFoot;
    private final ModelPart RLegB;
    private final ModelPart RLegC;
    private final ModelPart LLegA;
    private final ModelPart LFoot;
    private final ModelPart LLegB;
    private final ModelPart LLegC;
    private final ModelPart RArmA;
    private final ModelPart RArmB;
    private final ModelPart RArmC;
    private final ModelPart RHand;
    private final ModelPart LArmA;
    private final ModelPart LArmB;
    private final ModelPart LArmC;
    private final ModelPart LHand;
    private final ModelPart RFinger1;
    private final ModelPart RFinger2;
    private final ModelPart RFinger3;
    private final ModelPart RFinger4;
    private final ModelPart RFinger5;
    private final ModelPart LFinger1;
    private final ModelPart LFinger2;
    private final ModelPart LFinger3;
    private final ModelPart LFinger4;
    private final ModelPart LFinger5;

    // This flag controls “hunched” posture
    public boolean hunched;

    public MoCModelWerewolf(ModelPart root) {
        super(root);
        this.Head      = root.getChild("Head");
        this.Nose      = root.getChild("Nose");
        this.Snout     = root.getChild("Snout");
        this.TeethU    = root.getChild("TeethU");
        this.TeethL    = root.getChild("TeethL");
        this.Mouth     = root.getChild("Mouth");
        this.LEar      = root.getChild("LEar");
        this.REar      = root.getChild("REar");
        this.Neck      = root.getChild("Neck");
        this.Neck2     = root.getChild("Neck2");
        this.SideburnL = root.getChild("SideburnL");
        this.SideburnR = root.getChild("SideburnR");
        this.Chest     = root.getChild("Chest");
        this.Abdomen   = root.getChild("Abdomen");
        this.TailA     = root.getChild("TailA");
        this.TailB     = root.getChild("TailB");
        this.TailC     = root.getChild("TailC");
        this.TailD     = root.getChild("TailD");
        this.RLegA     = root.getChild("RLegA");
        this.RFoot     = root.getChild("RFoot");
        this.RLegB     = root.getChild("RLegB");
        this.RLegC     = root.getChild("RLegC");
        this.LLegA     = root.getChild("LLegA");
        this.LFoot     = root.getChild("LFoot");
        this.LLegB     = root.getChild("LLegB");
        this.LLegC     = root.getChild("LLegC");
        this.RArmA     = root.getChild("RArmA");
        this.RArmB     = root.getChild("RArmB");
        this.RArmC     = root.getChild("RArmC");
        this.RHand     = root.getChild("RHand");
        this.LArmA     = root.getChild("LArmA");
        this.LArmB     = root.getChild("LArmB");
        this.LArmC     = root.getChild("LArmC");
        this.LHand     = root.getChild("LHand");
        this.RFinger1  = root.getChild("RFinger1");
        this.RFinger2  = root.getChild("RFinger2");
        this.RFinger3  = root.getChild("RFinger3");
        this.RFinger4  = root.getChild("RFinger4");
        this.RFinger5  = root.getChild("RFinger5");
        this.LFinger1  = root.getChild("LFinger1");
        this.LFinger2  = root.getChild("LFinger2");
        this.LFinger3  = root.getChild("LFinger3");
        this.LFinger4  = root.getChild("LFinger4");
        this.LFinger5  = root.getChild("LFinger5");
    }

    /**
     * Builds the LayerDefinition for this werewolf model.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition part    = mesh.getRoot();

        // HEAD
        part.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -3F, -6F, 8, 8, 6),
                PartPose.offset(0F, -8F, -6F)
        );

        // NOSE (initial X‐rotation: 0.2792527F)
        part.addOrReplaceChild("Nose",
                CubeListBuilder.create()
                        .texOffs(44, 33)
                        .addBox(-1.5F, -1.7F, -12.3F, 3, 2, 7),
                PartPose.offsetAndRotation(0F, -8F, -6F, 0.2792527F, 0F, 0F)
        );

        // SNOUT
        part.addOrReplaceChild("Snout",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-2F, 2F, -12F, 4, 2, 6),
                PartPose.offset(0F, -8F, -6F)
        );

        // TEETHU
        part.addOrReplaceChild("TeethU",
                CubeListBuilder.create()
                        .texOffs(46, 18)
                        .addBox(-2F, 4.01F, -12F, 4, 2, 5),
                PartPose.offset(0F, -8F, -6F)
        );

        // TEETHL (initial X‐rotation: 2.530727F)
        part.addOrReplaceChild("TeethL",
                CubeListBuilder.create()
                        .texOffs(20, 109)
                        .addBox(-1.5F, -12.5F, 2.01F, 3, 5, 2),
                PartPose.offsetAndRotation(0F, -8F, -6F, 2.530727F, 0F, 0F)
        );

        // MOUTH (initial X‐rotation: 2.530727F)
        part.addOrReplaceChild("Mouth",
                CubeListBuilder.create()
                        .texOffs(42, 69)
                        .addBox(-1.5F, -12.5F, 0F, 3, 9, 2),
                PartPose.offsetAndRotation(0F, -8F, -6F, 2.530727F, 0F, 0F)
        );

        // LEAR (initial Z‐rotation: 0.1745329F)
        part.addOrReplaceChild("LEar",
                CubeListBuilder.create()
                        .texOffs(13, 14)
                        .addBox(0.5F, -7.5F, -1F, 3, 5, 1),
                PartPose.offsetAndRotation(0F, -8F, -6F, 0F, 0F, 0.1745329F)
        );

        // REAR (initial Z‐rotation: -0.1745329F)
        part.addOrReplaceChild("REar",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-3.5F, -7.5F, -1F, 3, 5, 1),
                PartPose.offsetAndRotation(0F, -8F, -6F, 0F, 0F, -0.1745329F)
        );

        // NECK (initial X‐rotation: -0.6025001F)
        part.addOrReplaceChild("Neck",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-3.5F, -3F, -7F, 7, 8, 7),
                PartPose.offsetAndRotation(0F, -5F, -2F, -0.6025001F, 0F, 0F)
        );

        // NECK2 (initial X‐rotation: -0.4537856F)
        part.addOrReplaceChild("Neck2",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-1.5F, -2F, -5F, 3, 4, 7),
                PartPose.offsetAndRotation(0F, -1F, -6F, -0.4537856F, 0F, 0F)
        );

        // SIDEBURNL (initial X: -0.2094395F, Y: 0.418879F, Z: -0.0872665F)
        part.addOrReplaceChild("SideburnL",
                CubeListBuilder.create()
                        .texOffs(28, 33)
                        .addBox(3F, 0F, -2F, 2, 6, 6),
                PartPose.offsetAndRotation(0F, -8F, -6F, -0.2094395F, 0.418879F, -0.0872665F)
        );

        // SIDEBURNR (initial X: -0.2094395F, Y: -0.418879F, Z: 0.0872665F)
        part.addOrReplaceChild("SideburnR",
                CubeListBuilder.create()
                        .texOffs(28, 45)
                        .addBox(-5F, 0F, -2F, 2, 6, 6),
                PartPose.offsetAndRotation(0F, -8F, -6F, -0.2094395F, -0.418879F, 0.0872665F)
        );

        // CHEST (initial X: 0.641331F)
        part.addOrReplaceChild("Chest",
                CubeListBuilder.create()
                        .texOffs(20, 15)
                        .addBox(-4F, 0F, -7F, 8, 8, 10),
                PartPose.offsetAndRotation(0F, -6F, -2.5F, 0.641331F, 0F, 0F)
        );

        // ABDOMEN (initial X: 0.2695449F)
        part.addOrReplaceChild("Abdomen",
                CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-3F, -8F, -8F, 6, 14, 8),
                PartPose.offsetAndRotation(0F, 4.5F, 5F, 0.2695449F, 0F, 0F)
        );

        // TAILA (initial X: 1.064651F)
        part.addOrReplaceChild("TailA",
                CubeListBuilder.create()
                        .texOffs(52, 42)
                        .addBox(-1.5F, -1F, -2F, 3, 4, 3),
                PartPose.offsetAndRotation(0F, 9.5F, 6F, 1.064651F, 0F, 0F)
        );

        // TAILB (initial X: 0.7504916F)
        part.addOrReplaceChild("TailB",
                CubeListBuilder.create()
                        .texOffs(48, 49)
                        .addBox(-2F, 2F, -2F, 4, 6, 4),
                PartPose.offsetAndRotation(0F, 9.5F, 6F, 0.7504916F, 0F, 0F)
        );

        // TAILC (initial X: 1.099557F, Y‐offset 6.8F, Z‐offset -4.6F relative)
        part.addOrReplaceChild("TailC",
                CubeListBuilder.create()
                        .texOffs(48, 59)
                        .addBox(-2F, 6.8F, -4.6F, 4, 6, 4),
                PartPose.offsetAndRotation(0F, 9.5F, 6F, 1.099557F, 0F, 0F)
        );

        // TAILD (initial X: 1.099557F, offsets 9.8F, -4.1F)
        part.addOrReplaceChild("TailD",
                CubeListBuilder.create()
                        .texOffs(52, 69)
                        .addBox(-1.5F, 9.8F, -4.1F, 3, 5, 3),
                PartPose.offsetAndRotation(0F, 9.5F, 6F, 1.099557F, 0F, 0F)
        );

        // RIGHT LEG A (initial X: -0.8126625F)
        part.addOrReplaceChild("RLegA",
                CubeListBuilder.create()
                        .texOffs(12, 64)
                        .addBox(-2.5F, -1.5F, -3.5F, 3, 8, 5),
                PartPose.offsetAndRotation(-3F, 9.5F, 3F, -0.8126625F, 0F, 0F)
        );

        // R FOOT (no initial rotation)
        part.addOrReplaceChild("RFoot",
                CubeListBuilder.create()
                        .texOffs(14, 93)
                        .addBox(-2.506667F, 12.5F, -5F, 3, 2, 3),
                PartPose.offset(-3F, 9.5F, 3F)
        );

        // RIGHT LEG B (initial X: -0.8445741F)
        part.addOrReplaceChild("RLegB",
                CubeListBuilder.create()
                        .texOffs(14, 76)
                        .addBox(-1.9F, 4.2F, 0.5F, 2, 2, 5),
                PartPose.offsetAndRotation(-3F, 9.5F, 3F, -0.8445741F, 0F, 0F)
        );

        // RIGHT LEG C (initial X: -0.2860688F)
        part.addOrReplaceChild("RLegC",
                CubeListBuilder.create()
                        .texOffs(14, 83)
                        .addBox(-2F, 6.2F, 0.5F, 2, 8, 2),
                PartPose.offsetAndRotation(-3F, 9.5F, 3F, -0.2860688F, 0F, 0F)
        );

        // LEFT LEG B (initial X: -0.8445741F)
        part.addOrReplaceChild("LLegB",
                CubeListBuilder.create()
                        .texOffs(0, 76)
                        .addBox(-0.1F, 4.2F, 0.5F, 2, 2, 5),
                PartPose.offsetAndRotation(3F, 9.5F, 3F, -0.8445741F, 0F, 0F)
        );

        // L FOOT
        part.addOrReplaceChild("LFoot",
                CubeListBuilder.create()
                        .texOffs(0, 93)
                        .addBox(-0.5066667F, 12.5F, -5F, 3, 2, 3),
                PartPose.offset(3F, 9.5F, 3F)
        );

        // LEFT LEG C (initial X: -0.2860688F)
        part.addOrReplaceChild("LLegC",
                CubeListBuilder.create()
                        .texOffs(0, 83)
                        .addBox(0F, 6.2F, 0.5F, 2, 8, 2),
                PartPose.offsetAndRotation(3F, 9.5F, 3F, -0.2860688F, 0F, 0F)
        );

        // LEFT LEG A (initial X: -0.8126625F)
        part.addOrReplaceChild("LLegA",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-0.5F, -1.5F, -3.5F, 3, 8, 5),
                PartPose.offsetAndRotation(3F, 9.5F, 3F, -0.8126625F, 0F, 0F)
        );

        // RIGHT ARM B (initial X: 0.2617994F, Z: 0.3490659F)
        part.addOrReplaceChild("RArmB",
                CubeListBuilder.create()
                        .texOffs(48, 77)
                        .addBox(-3.5F, 1F, -1.5F, 4, 8, 4),
                PartPose.offsetAndRotation(-4F, -4F, -2F, 0.2617994F, 0F, 0.3490659F)
        );

        // RIGHT ARM C (initial X: -0.3490659F)
        part.addOrReplaceChild("RArmC",
                CubeListBuilder.create()
                        .texOffs(48, 112)
                        .addBox(-6F, 5F, 3F, 4, 7, 4),
                PartPose.offsetAndRotation(-4F, -4F, -2F, -0.3490659F, 0F, 0F)
        );

        // LEFT ARM B (initial X: 0.2617994F, Z: -0.3490659F)
        part.addOrReplaceChild("LArmB",
                CubeListBuilder.create()
                        .texOffs(48, 89)
                        .addBox(-0.5F, 1F, -1.5F, 4, 8, 4),
                PartPose.offsetAndRotation(4F, -4F, -2F, 0.2617994F, 0F, -0.3490659F)
        );

        // RIGHT HAND (no initial rotation)
        part.addOrReplaceChild("RHand",
                CubeListBuilder.create()
                        .texOffs(32, 118)
                        .addBox(-6F, 12.5F, -1.5F, 4, 3, 4),
                PartPose.offset(-4F, -4F, -2F)
        );

        // RIGHT ARM A (initial X: 0.6320364F)
        part.addOrReplaceChild("RArmA",
                CubeListBuilder.create()
                        .texOffs(0, 108)
                        .addBox(-5F, -3F, -2F, 5, 5, 5),
                PartPose.offsetAndRotation(-4F, -4F, -2F, 0.6320364F, 0F, 0F)
        );

        // LEFT ARM A (initial X: 0.6320364F)
        part.addOrReplaceChild("LArmA",
                CubeListBuilder.create()
                        .texOffs(0, 98)
                        .addBox(0F, -3F, -2F, 5, 5, 5),
                PartPose.offsetAndRotation(4F, -4F, -2F, 0.6320364F, 0F, 0F)
        );

        // LEFT ARM C (initial X: -0.3490659F)
        part.addOrReplaceChild("LArmC",
                CubeListBuilder.create()
                        .texOffs(48, 101)
                        .addBox(2F, 5F, 3F, 4, 7, 4),
                PartPose.offsetAndRotation(4F, -4F, -2F, -0.3490659F, 0F, 0F)
        );

        // LEFT HAND (no initial rotation)
        part.addOrReplaceChild("LHand",
                CubeListBuilder.create()
                        .texOffs(32, 111)
                        .addBox(2F, 12.5F, -1.5F, 4, 3, 4),
                PartPose.offset(4F, -4F, -2F)
        );

        // RIGHT FINGER 1 (attach under “RHand” region, no initial rotation)
        part.addOrReplaceChild("RFinger1",
                CubeListBuilder.create()
                        .texOffs(8, 120)
                        .addBox(-3F, 15.5F, 1F, 1, 3, 1),
                PartPose.offset(-4F, -4F, -2F)
        );

        // RIGHT FINGER 2
        part.addOrReplaceChild("RFinger2",
                CubeListBuilder.create()
                        .texOffs(12, 124)
                        .addBox(-3.5F, 15.5F, -1.5F, 1, 3, 1),
                PartPose.offset(-4F, -4F, -2F)
        );

        // RIGHT FINGER 3
        part.addOrReplaceChild("RFinger3",
                CubeListBuilder.create()
                        .texOffs(12, 119)
                        .addBox(-4.8F, 15.5F, -1.5F, 1, 4, 1),
                PartPose.offset(-4F, -4F, -2F)
        );

        // RIGHT FINGER 4
        part.addOrReplaceChild("RFinger4",
                CubeListBuilder.create()
                        .texOffs(16, 119)
                        .addBox(-6F, 15.5F, -0.5F, 1, 4, 1),
                PartPose.offset(-4F, -4F, -2F)
        );

        // RIGHT FINGER 5
        part.addOrReplaceChild("RFinger5",
                CubeListBuilder.create()
                        .texOffs(16, 124)
                        .addBox(-6F, 15.5F, 1F, 1, 3, 1),
                PartPose.offset(-4F, -4F, -2F)
        );

        // LEFT FINGER 1
        part.addOrReplaceChild("LFinger1",
                CubeListBuilder.create()
                        .texOffs(8, 124)
                        .addBox(2F, 15.5F, 1F, 1, 3, 1),
                PartPose.offset(4F, -4F, -2F)
        );

        // LEFT FINGER 2
        part.addOrReplaceChild("LFinger2",
                CubeListBuilder.create()
                        .texOffs(0, 124)
                        .addBox(2.5F, 15.5F, -1.5F, 1, 3, 1),
                PartPose.offset(4F, -4F, -2F)
        );

        // LEFT FINGER 3
        part.addOrReplaceChild("LFinger3",
                CubeListBuilder.create()
                        .texOffs(0, 119)
                        .addBox(3.8F, 15.5F, -1.5F, 1, 4, 1),
                PartPose.offset(4F, -4F, -2F)
        );

        // LEFT FINGER 4
        part.addOrReplaceChild("LFinger4",
                CubeListBuilder.create()
                        .texOffs(4, 119)
                        .addBox(5F, 15.5F, -0.5F, 1, 4, 1),
                PartPose.offset(4F, -4F, -2F)
        );

        // LEFT FINGER 5
        part.addOrReplaceChild("LFinger5",
                CubeListBuilder.create()
                        .texOffs(4, 124)
                        .addBox(5F, 15.5F, 1F, 1, 3, 1),
                PartPose.offset(4F, -4F, -2F)
        );

        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Nose.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Snout.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethU.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TeethL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Mouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.REar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Neck2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SideburnL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.SideburnR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TailD.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RLegC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LLegC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RArmA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RArmB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RArmC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LArmA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LArmB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LArmC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LHand.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFinger1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFinger2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFinger3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFinger4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RFinger5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFinger1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFinger2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFinger3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFinger4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LFinger5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float radianF      = 57.29578F;
        float RLegXRot     = Mth.cos((limbSwing * 0.6662F) + (float) Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot     = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;

        // HEAD yaw
        this.Head.yRot = netHeadYaw / radianF;

        if (!this.hunched) {
            // Normal posture
            this.Head.setPos(0F, -8F, -6F);
            this.Head.xRot = headPitch / radianF;

            this.Neck.xRot        = -34F / radianF;
            this.Neck.setPos(0F, -5F, -2F);

            this.Neck2.setPos(0F, -1F, -6F);

            this.Chest.setPos(0F, -6F, -2.5F);
            this.Chest.xRot       = 36F / radianF;

            this.Abdomen.xRot     = 15F / radianF;
            this.LLegA.setPos(3F, 9.5F, 3F);

            this.RArmA.setPos(-4F, -4F, -2F);
            this.RArmA.xRot       = 0.6320364F;
            this.LArmA.setPos(4F, -4F, -2F);
            this.LArmA.xRot       = 0.6320364F;

            this.TailA.setPos(0F, 9.5F, 6F);
            this.TailA.xRot       = 1.064651F;
        } else {
            // Hunched posture
            this.Head.setPos(0F, 0F, -11F);
            this.Head.xRot       = (15F + headPitch) / radianF;

            this.Neck.xRot        = -10F / radianF;
            this.Neck.setPos(0F, 2F, -6F);

            this.Neck2.setPos(0F, 9F, -9F);

            this.Chest.setPos(0F, 1F, -7.5F);
            this.Chest.xRot       = 60F / radianF;

            this.Abdomen.xRot     = 75F / radianF;
            this.LLegA.setPos(3F, 9.5F, 7F);

            this.RArmA.setPos(-4F, 4.5F, -6F);
            this.RArmA.xRot       = 0.6320364F;
            this.LArmA.setPos(4F, 4.5F, -6F);
            this.LArmA.xRot       = 0.6320364F;

            this.TailA.setPos(0F, 7.5F, 10F);
            this.TailA.xRot       = 1.064651F;
        }

        // Propagate Head’s Y/X to facial parts
        this.Nose.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.Snout.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.TeethU.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.TeethL.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.Mouth.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.LEar.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.REar.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.SideburnL.setPos(this.Head.x, this.Head.y, this.Head.z);
        this.SideburnR.setPos(this.Head.x, this.Head.y, this.Head.z);

        this.Nose.yRot   = this.Head.yRot;
        this.Snout.yRot  = this.Head.yRot;
        this.TeethU.yRot = this.Head.yRot;
        this.TeethL.yRot = this.Head.yRot;
        this.Mouth.yRot  = this.Head.yRot;
        this.LEar.yRot   = this.Head.yRot;
        this.REar.yRot   = this.Head.yRot;

        this.Nose.xRot   = 0.2792527F + this.Head.xRot;
        this.Snout.xRot  = this.Head.xRot;
        this.TeethU.xRot = this.Head.xRot;
        this.TeethL.xRot = this.Head.xRot + 2.530727F;
        this.Mouth.xRot  = this.Head.xRot + 2.530727F;
        this.LEar.xRot   = this.Head.xRot;
        this.REar.xRot   = this.Head.xRot;

        this.SideburnL.xRot = -0.2094395F + this.Head.xRot;
        this.SideburnL.yRot =  0.418879F + this.Head.yRot;
        this.SideburnR.xRot = -0.2094395F + this.Head.xRot;
        this.SideburnR.yRot = -0.418879F + this.Head.yRot;

        // LEGS: Right
        this.RLegA.xRot = -0.8126625F + RLegXRot;
        this.RLegB.xRot = -0.8445741F + RLegXRot;
        this.RLegC.xRot = -0.2860688F + RLegXRot;
        this.RFoot.xRot = RLegXRot;

        // LEGS: Left
        this.LLegA.xRot = -0.8126625F + LLegXRot;
        this.LLegB.xRot = -0.8445741F + LLegXRot;
        this.LLegC.xRot = -0.2860688F + LLegXRot;
        this.LFoot.xRot = LLegXRot;

        // ARMS: oscillate Z slightly
        float armZOsc = (float) (Math.cos(ageInTicks * 0.09F) * 0.05F);
        this.RArmA.zRot = -armZOsc + 0.05F;
        this.LArmA.zRot =  armZOsc - 0.05F;

        // ARMS: match to opposite leg X‐rotation
        this.RArmA.xRot = LLegXRot;
        this.LArmA.xRot = RLegXRot;

        // ARMS: propagate to B and C and hands/fingers
        this.RArmB.zRot  = 0.3490659F + this.RArmA.zRot;
        this.LArmB.zRot  = -0.3490659F + this.LArmA.zRot;
        this.RArmB.xRot  = 0.2617994F + this.RArmA.xRot;
        this.LArmB.xRot  = 0.2617994F + this.LArmA.xRot;

        this.RArmC.zRot = this.RArmA.zRot;
        this.LArmC.zRot = this.LArmA.zRot;
        this.RArmC.xRot = -0.3490659F + this.RArmA.xRot;
        this.LArmC.xRot = -0.3490659F + this.LArmA.xRot;

        this.RHand.zRot = this.RArmA.zRot;
        this.LHand.zRot = this.LArmA.zRot;
        this.RHand.xRot = this.RArmA.xRot;
        this.LHand.xRot = this.LArmA.xRot;

        // FINGERS: match to corresponding hand X/Z
        for (ModelPart finger : new ModelPart[] {RFinger1, RFinger2, RFinger3, RFinger4, RFinger5}) {
            finger.xRot = this.RArmA.xRot;
            finger.zRot = this.RArmA.zRot;
            finger.setPos(this.RArmA.x, this.RArmA.y, this.RArmA.z);
        }
        for (ModelPart finger : new ModelPart[] {LFinger1, LFinger2, LFinger3, LFinger4, LFinger5}) {
            finger.xRot = this.LArmA.xRot;
            finger.zRot = this.LArmA.zRot;
            finger.setPos(this.LArmA.x, this.LArmA.y, this.LArmA.z);
        }

        // SYNC other parts’ positions for symmetry:
        // TailB, TailC, TailD follow TailA’s position each frame
        this.TailB.setPos(this.TailA.x, this.TailA.y, this.TailA.z);
        this.TailC.setPos(this.TailA.x, this.TailA.y, this.TailA.z);
        this.TailD.setPos(this.TailA.x, this.TailA.y, this.TailA.z);

        // Arms and fingers for Right side already set; ensure Left matches where needed
        this.RArmB.setPos(this.RArmA.x, this.RArmA.y, this.RArmA.z);
        this.RArmC.setPos(this.RArmA.x, this.RArmA.y, this.RArmA.z);
        this.RHand.setPos(this.RArmA.x, this.RArmA.y, this.RArmA.z);
        this.RLegB.setPos(this.RLegA.x, this.RLegA.y, this.RLegA.z);
        this.RLegC.setPos(this.RLegA.x, this.RLegA.y, this.RLegA.z);
        this.RFoot.setPos(this.RLegA.x, this.RLegA.y, this.RLegA.z);

        this.LArmB.setPos(this.LArmA.x, this.LArmA.y, this.LArmA.z);
        this.LArmC.setPos(this.LArmA.x, this.LArmA.y, this.LArmA.z);
        this.LHand.setPos(this.LArmA.x, this.LArmA.y, this.LArmA.z);
        this.LLegB.setPos(this.LLegA.x, this.LLegA.y, this.LLegA.z);
        this.LLegC.setPos(this.LLegA.x, this.LLegA.y, this.LLegA.z);
        this.LFoot.setPos(this.LLegA.x, this.LLegA.y, this.LLegA.z);
    }
}
