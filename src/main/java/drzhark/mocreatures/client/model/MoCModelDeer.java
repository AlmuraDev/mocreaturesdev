package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityDeer;
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

public class MoCModelDeer<T extends MoCEntityDeer> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "deer"), "main"
    );

    // All formerly ModelRenderer fields become ModelPart:
    private final ModelPart Body;
    private final ModelPart Neck;
    private final ModelPart Head;
    private final ModelPart Leg1;
    private final ModelPart Leg2;
    private final ModelPart Leg3;
    private final ModelPart Leg4;
    private final ModelPart Tail;
    private final ModelPart LEar;
    private final ModelPart REar;
    private final ModelPart LeftAntler;
    private final ModelPart RightAntler;

    public MoCModelDeer(ModelPart root) {
        super(root);
        this.Body       = root.getChild("Body");
        this.Neck       = root.getChild("Neck");
        this.Head       = root.getChild("Head");
        this.Leg1       = root.getChild("Leg1");
        this.Leg2       = root.getChild("Leg2");
        this.Leg3       = root.getChild("Leg3");
        this.Leg4       = root.getChild("Leg4");
        this.Tail       = root.getChild("Tail");
        this.LEar       = root.getChild("LEar");
        this.REar       = root.getChild("REar");
        this.LeftAntler = root.getChild("LeftAntler");
        this.RightAntler= root.getChild("RightAntler");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -6F, -9.5F, 3, 3, 6),
                PartPose.offset(1.0F, 11.5F, -4.5F)
        );

        // Neck (rotated -45° around X)
        root.addOrReplaceChild("Neck",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-2F, -2F, -6F, 4, 4, 6),
                PartPose.offsetAndRotation(1.0F, 11.5F, -4.5F, -0.7853981F, 0F, 0F)
        );

        // Left Ear (rotated +45° around Z)
        root.addOrReplaceChild("LEar",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -7.5F, -5F, 2, 3, 1),
                PartPose.offsetAndRotation(1.0F, 11.5F, -4.5F, 0F, 0F, 0.7853981F)
        );

        // Right Ear (rotated -45° around Z)
        root.addOrReplaceChild("REar",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(2F, -7.5F, -5F, 2, 3, 1),
                PartPose.offsetAndRotation(1.0F, 11.5F, -4.5F, 0F, 0F, -0.7853981F)
        );

        // Left Antler (rotated +12° around Z)
        root.addOrReplaceChild("LeftAntler",
                CubeListBuilder.create()
                        .texOffs(54, 0)
                        .addBox(0F, -14F, -7F, 1, 8, 4),
                PartPose.offsetAndRotation(1.0F, 11.5F, -4.5F, 0F, 0F, 0.2094395F)
        );

        // Right Antler (rotated -12° around Z)
        root.addOrReplaceChild("RightAntler",
                CubeListBuilder.create()
                        .texOffs(54, 0)
                        .addBox(0F, -14F, -7F, 1, 8, 4),
                PartPose.offsetAndRotation(1.0F, 11.5F, -4.5F, 0F, 0F, -0.2094395F)
        );

        // Body
        root.addOrReplaceChild("Body",
                CubeListBuilder.create()
                        .texOffs(24, 12)
                        .addBox(-2F, -3F, -6F, 6, 6, 14),
                PartPose.offset(0.0F, 13F, 0.0F)
        );

        // Leg1 (front right)
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create()
                        .texOffs(9, 20)
                        .addBox(-1F, 0F, -1F, 2, 8, 2),
                PartPose.offset(3F, 16F, -4F)
        );

        // Leg2 (front left)
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-1F, 0F, -1F, 2, 8, 2),
                PartPose.offset(-1F, 16F, -4F)
        );

        // Leg3 (hind right)
        root.addOrReplaceChild("Leg3",
                CubeListBuilder.create()
                        .texOffs(9, 20)
                        .addBox(-1F, 0F, -1F, 2, 8, 2),
                PartPose.offset(3F, 16F, 6F)
        );

        // Leg4 (hind left)
        root.addOrReplaceChild("Leg4",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-1F, 0F, -1F, 2, 8, 2),
                PartPose.offset(-1F, 16F, 6F)
        );

        // Tail (rotated +45° around X)
        root.addOrReplaceChild("Tail",
                CubeListBuilder.create()
                        .texOffs(50, 20)
                        .addBox(-1.5F, -1F, 0F, 3, 2, 4),
                PartPose.offsetAndRotation(1.0F, 11F, 7F, 0.7854F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // Original only animated legs, so we copy that:
        this.Leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.Leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.Leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.Leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LEar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.REar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LeftAntler.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RightAntler.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
