package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.aquatic.MoCEntityDolphin;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MoCModelDolphin<T extends MoCEntityDolphin> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "dolphin"), "main"
    );

    private final ModelPart Body;
    private final ModelPart UHead;
    private final ModelPart DHead;
    private final ModelPart PTail;
    private final ModelPart UpperFin;
    private final ModelPart LTailFin;
    private final ModelPart RTailFin;
    private final ModelPart LeftFin;
    private final ModelPart RightFin;

    public MoCModelDolphin(ModelPart root) {
        super(root);
        this.Body      = root.getChild("Body");
        this.UHead     = root.getChild("UHead");
        this.DHead     = root.getChild("DHead");
        this.PTail     = root.getChild("PTail");
        this.UpperFin  = root.getChild("UpperFin");
        this.LTailFin  = root.getChild("LTailFin");
        this.RTailFin  = root.getChild("RTailFin");
        this.LeftFin   = root.getChild("LeftFin");
        this.RightFin  = root.getChild("RightFin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body: texOffs(4,6), addBox(0,0,0,6,8,18), pivot(-3,17,-4)
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(4, 6)
                        .addBox(0.0F, 0.0F, 0.0F, 6, 8, 18),
                PartPose.offset(-3.0F, 17.0F, -4.0F)
        );

        // UHead: texOffs(0,0), addBox(0,0,0,5,7,8), pivot(-2.5,18,-10.5)
        root.addOrReplaceChild("UHead",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 5, 7, 8),
                PartPose.offset(-2.5F, 18.0F, -10.5F)
        );

        // DHead: texOffs(50,0), addBox(0,0,0,3,3,4), pivot(-1.5,21.5,-14.5)
        root.addOrReplaceChild("DHead",
                CubeListBuilder.create()
                        .texOffs(50, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3, 3, 4),
                PartPose.offset(-1.5F, 21.5F, -14.5F)
        );

        // PTail: texOffs(34,9), addBox(0,0,0,5,5,10), pivot(-2.5,19,14)
        root.addOrReplaceChild("PTail",
                CubeListBuilder.create()
                        .texOffs(34, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 5, 5, 10),
                PartPose.offset(-2.5F, 19.0F, 14.0F)
        );

        // UpperFin: texOffs(4,12), addBox(0,0,0,1,4,8), pivot(-0.5,18,2), rotateX = +0.7853981
        root.addOrReplaceChild("UpperFin",
                CubeListBuilder.create()
                        .texOffs(4, 12)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 4, 8),
                PartPose.offsetAndRotation(-0.5F, 18.0F, 2.0F, 0.7853981F, 0.0F, 0.0F)
        );

        // LTailFin: texOffs(34,0), addBox(0,0,0,4,1,8, 0.3), pivot(-1,21.5,24), rotateY = +0.7853981
        root.addOrReplaceChild("LTailFin",
                CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4, 1, 8, new CubeDeformation(0.3F)),
                PartPose.offsetAndRotation(-1.0F, 21.5F, 24.0F, 0.0F, 0.7853981F, 0.0F)
        );

        // RTailFin: texOffs(34,0), addBox(0,0,0,4,1,8,0.3), pivot(-2,21.5,21), rotateY = -0.7853981
        root.addOrReplaceChild("RTailFin",
                CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4, 1, 8, new CubeDeformation(0.3F)),
                PartPose.offsetAndRotation(-2.0F, 21.5F, 21.0F, 0.0F, -0.7853981F, 0.0F)
        );

        // LeftFin: texOffs(14,0), addBox(0,0,0,8,1,4), pivot(3,24,-1), rotateY=-0.5235988, rotateZ=+0.5235988
        root.addOrReplaceChild("LeftFin",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8, 1, 4),
                PartPose.offsetAndRotation(3.0F, 24.0F, -1.0F, 0.0F, -0.5235988F, 0.5235988F)
        );

        // RightFin: texOffs(14,0), addBox(0,0,0,8,1,4), pivot(-9,27.5,3), rotateY=+0.5235988, rotateZ=-0.5235988
        root.addOrReplaceChild("RightFin",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8, 1, 4),
                PartPose.offsetAndRotation(-9.0F, 27.5F, 3.0F, 0.0F, 0.5235988F, -0.5235988F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // In your old setRotationAngles, only the two tail‐fins were animated:
        this.RTailFin.xRot = Mth.cos(limbSwing * 0.4F) * limbSwingAmount;
        this.LTailFin.xRot = Mth.cos(limbSwing * 0.4F) * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.PTail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.DHead.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.UpperFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LTailFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RTailFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
