/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityFox;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelFox<T extends MoCEntityFox> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "fox"), "main"
    );

    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart ears;
    private final ModelPart tail;

    public MoCModelFox(ModelPart root) {
        super(root);
        this.body  = root.getChild("body");
        this.leg1  = root.getChild("leg1");
        this.leg2  = root.getChild("leg2");
        this.leg3  = root.getChild("leg3");
        this.leg4  = root.getChild("leg4");
        this.head  = root.getChild("head");
        this.snout = root.getChild("snout");
        this.ears  = root.getChild("ears");
        this.tail  = root.getChild("tail");
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Head rotation
        this.head.yRot  = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot  = headPitch * ((float)Math.PI / 180F);
        this.snout.yRot = this.head.yRot;
        this.snout.xRot = this.head.xRot;
        this.ears.yRot  = this.head.yRot;
        this.ears.xRot  = this.head.xRot;

        // Leg animation: front/back opposite phase, left/right opposite phase
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.snout.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ears.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * Creates the mesh + texture size for MoCModelFox.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition root    = mesh.getRoot();
        byte legHeight = 8;

        // BODY
        // Original: addBox(1, 0, -) size (6,6,12), pivot (-4,10,-6)
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1F, 0F, 0F, 6, 6, 12, new CubeDeformation(0.0F)),
                PartPose.offset(-4F, 10F, -6F)
        );

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-2F, -3F, -4F, 6, 6, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-1F, 11F, -6F)
        );

        // SNOUT
        root.addOrReplaceChild("snout",
                CubeListBuilder.create()
                        .texOffs(20, 20)
                        .addBox(0F, 1F, -7F, 2, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-1F, 11F, -6F)
        );

        // EARS
        root.addOrReplaceChild("ears",
                CubeListBuilder.create()
                        .texOffs(50, 20)
                        .addBox(-2F, -6F, -2F, 6, 4, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-1F, 11F, -6F)
        );

        // TAIL
        // Original: addBox(-4,-5,-2, 3,3,8), pivot (2.5,15,5), rotateX = -0.5235988
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(32, 20)
                        .addBox(-4F, -5F, -2F, 3, 3, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 15F, 5F, -0.5235988F, 0F, 0F)
        );

        // LEG1
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -2F, 3, legHeight, 3, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 24 - legHeight, 5F)
        );

        // LEG2
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -2F, 3, legHeight, 3, new CubeDeformation(0.0F)),
                PartPose.offset(1F, 24 - legHeight, 5F)
        );

        // LEG3
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -2F, 3, legHeight, 3, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 24 - legHeight, -4F)
        );

        // LEG4
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -2F, 3, legHeight, 3, new CubeDeformation(0.0F)),
                PartPose.offset(1F, 24 - legHeight, -4F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }
}
