package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityBird;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;

public class MoCModelBird<T extends MoCEntityBird> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "bird"), "main");

    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart body;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart rWing;
    private final ModelPart lWing;
    private final ModelPart tail;

    public MoCModelBird(ModelPart root) {
        super(root);
        this.head     = root.getChild("head");
        this.beak     = root.getChild("beak");
        this.body     = root.getChild("body");
        this.leftLeg  = root.getChild("leftleg");
        this.rightLeg = root.getChild("rightleg");
        this.rWing    = root.getChild("rwing");
        this.lWing    = root.getChild("lwing");
        this.tail     = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // head (no initial rotation)
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -3F, -2F, 3, 3, 3),
                PartPose.offset(0F, 15F, -4F)
        );

        // beak (attached at same pivot as head)
        root.addOrReplaceChild("beak",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-0.5F, -1.5F, -3F, 1, 1, 2),
                PartPose.offset(0F, 15F, -4F)
        );

        // body, rotated ~60° (1.047198 radians) around X
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-2F, -4F, -3F, 4, 8, 4),
                PartPose.offsetAndRotation(0F, 16F, 0F, 1.047198F, 0F, 0F)
        );

        // left leg
        root.addOrReplaceChild("leftleg",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-1F, 0F, -4F, 3, 4, 3),
                PartPose.offset(-2F, 19F, 1F)
        );

        // right leg
        root.addOrReplaceChild("rightleg",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-1F, 0F, -4F, 3, 4, 3),
                PartPose.offset(1F, 19F, 1F)
        );

        // right wing
        root.addOrReplaceChild("rwing",
                CubeListBuilder.create()
                        .texOffs(24, 13)
                        .addBox(-1F, 0F, -3F, 1, 5, 5),
                PartPose.offset(-2F, 14F, 0F)
        );

        // left wing
        root.addOrReplaceChild("lwing",
                CubeListBuilder.create()
                        .texOffs(24, 13)
                        .addBox(0F, 0F, -3F, 1, 5, 5),
                PartPose.offset(2F, 14F, 0F)
        );

        // tail, rotated ~15° (0.261799 radians) around X
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-6F, 5F, 2F, 4, 1, 4),
                PartPose.offsetAndRotation(4F, 13F, 0F, 0.261799F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        beak.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        leftLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        rWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        lWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Head pitch / yaw. Original did: head.rotateAngleX = -(headPitch/2)/57.29578
        head.xRot = -(headPitch * 0.5F) * Mth.DEG_TO_RAD;
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        // beak follows head's Y rotation
        beak.yRot = head.yRot;

        // If flying (in air and not riding), legs tuck straight out at ~1.4 rad:
        if (entityIn.isOnAir() && entityIn.getVehicle() == null) {
            leftLeg.xRot = 1.4F;
            rightLeg.xRot = 1.4F;
        } else {
            // Otherwise trot animation:
            leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
            rightLeg.xRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * limbSwingAmount;
        }

        // Wings flap continuously:
        float wingRot = ageInTicks;
        rWing.zRot = wingRot;
        lWing.zRot = -wingRot;
    }
}
