package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityBunny;
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

public class MoCModelBunny<T extends MoCEntityBunny> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "bunny"), "main");

    private final ModelPart part1;
    private final ModelPart part2;
    private final ModelPart part3;
    private final ModelPart part4;
    private final ModelPart part5;
    private final ModelPart part6;
    private final ModelPart part7;
    private final ModelPart part8;
    private final ModelPart part9;
    private final ModelPart part10;
    private final ModelPart part11;

    public MoCModelBunny(ModelPart root) {
        super(root);
        this.part1  = root.getChild("part1");
        this.part8  = root.getChild("part8");
        this.part9  = root.getChild("part9");
        this.part10 = root.getChild("part10");
        this.part11 = root.getChild("part11");
        this.part2  = root.getChild("part2");
        this.part3  = root.getChild("part3");
        this.part4  = root.getChild("part4");
        this.part5  = root.getChild("part5");
        this.part6  = root.getChild("part6");
        this.part7  = root.getChild("part7");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // part1 (head)
        root.addOrReplaceChild("part1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2F, -1F, -4F, 4, 4, 6),
                PartPose.offset(0F, 15F, -4F)
        );

        // part8 (left ear)
        root.addOrReplaceChild("part8",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-2F, -5F, -3F, 1, 4, 2),
                PartPose.offset(0F, 15F, -4F)
        );

        // part9 (right ear)
        root.addOrReplaceChild("part9",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(1F, -5F, -3F, 1, 4, 2),
                PartPose.offset(0F, 15F, -4F)
        );

        // part10 (left cheek)
        root.addOrReplaceChild("part10",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(-4F, 0F, -3F, 2, 3, 2),
                PartPose.offset(0F, 15F, -4F)
        );

        // part11 (right cheek)
        root.addOrReplaceChild("part11",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(2F, 0F, -3F, 2, 3, 2),
                PartPose.offset(0F, 15F, -4F)
        );

        // part2 (torso)
        root.addOrReplaceChild("part2",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-3F, -4F, -3F, 6, 8, 6),
                PartPose.offset(0F, 16F, 0F)
        );

        // part3 (belly)
        root.addOrReplaceChild("part3",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-2F, 4F, -2F, 4, 3, 4),
                PartPose.offset(0F, 16F, 0F)
        );

        // part4 (front right leg)
        root.addOrReplaceChild("part4",
                CubeListBuilder.create()
                        .texOffs(24, 16)
                        .addBox(-2F, 0F, -1F, 2, 2, 2),
                PartPose.offset(3F, 19F, -3F)
        );

        // part5 (front left leg)
        root.addOrReplaceChild("part5",
                CubeListBuilder.create()
                        .texOffs(24, 16)
                        .addBox(0F, 0F, -1F, 2, 2, 2),
                PartPose.offset(-3F, 19F, -3F)
        );

        // part6 (hind right leg)
        root.addOrReplaceChild("part6",
                CubeListBuilder.create()
                        .texOffs(16, 24)
                        .addBox(-2F, 0F, -4F, 2, 2, 4),
                PartPose.offset(3F, 19F, 4F)
        );

        // part7 (hind left leg)
        root.addOrReplaceChild("part7",
                CubeListBuilder.create()
                        .texOffs(16, 24)
                        .addBox(0F, 0F, -4F, 2, 2, 4),
                PartPose.offset(-3F, 19F, 4F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        part1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part8.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part9.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part10.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part11.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part6.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        part7.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Head rotation
        float headX    = -headPitch * Mth.DEG_TO_RAD;
        float headY    = netHeadYaw * Mth.DEG_TO_RAD;

        part1.xRot   = headX;
        part1.yRot   = headY;
        part8.xRot   = headX;
        part8.yRot   = headY;
        part9.xRot   = headX;
        part9.yRot   = headY;
        part10.xRot  = headX;
        part10.yRot  = headY;
        part11.xRot  = headX;
        part11.yRot  = headY;

        // Torso and belly always lie flat
        part2.xRot = (float) (Math.PI / 2.0F);
        part3.xRot = (float) (Math.PI / 2.0F);

        // Leg movement (if not riding)
        if (entityIn.getVehicle() == null) {
            float frontLeg  = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
            float hindLeg   = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 1.2F * limbSwingAmount;

            part4.xRot = frontLeg;  // front right
            part5.xRot = frontLeg;  // front left
            part6.xRot = hindLeg;   // hind right
            part7.xRot = hindLeg;   // hind left
        }
    }
}
