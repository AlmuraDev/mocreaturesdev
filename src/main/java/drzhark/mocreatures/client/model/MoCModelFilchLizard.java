package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityFilchLizard;
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
public class MoCModelFilchLizard<T extends MoCEntityFilchLizard> extends MoCEntityModel<T> {
    
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "filchlizard"), "main"
    );

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart foldHead;
    private final ModelPart foldFilch1;
    private final ModelPart foldFilch2;

    // These six are children under “head”
    private final ModelPart filch1;
    private final ModelPart filch2;
    private final ModelPart filch3;
    private final ModelPart filch4;
    private final ModelPart filch5;
    private final ModelPart filch6;

    private boolean heldItem;

    public MoCModelFilchLizard(ModelPart root) {
        super(root);
        this.body        = root.getChild("body");
        this.tail        = root.getChild("tail");
        this.leg1        = root.getChild("leg1");
        this.leg2        = root.getChild("leg2");
        this.leg3        = root.getChild("leg3");
        this.leg4        = root.getChild("leg4");
        this.foldHead    = root.getChild("fold_head");
        this.foldFilch1  = root.getChild("fold_filch1");
        this.foldFilch2  = root.getChild("fold_filch2");

        this.head       = root.getChild("head");
        this.filch1     = this.head.getChild("filch1");
        this.filch2     = this.head.getChild("filch2");
        this.filch3     = this.head.getChild("filch3");
        this.filch4     = this.head.getChild("filch4");
        this.filch5     = this.head.getChild("filch5");
        this.filch6     = this.head.getChild("filch6");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Mimic original setLivingAnimations: check if Filch Lizard is holding something
        this.heldItem = !entity.getMainHandItem().isEmpty();

        if (this.heldItem) {
            // “Held” pose
            this.leg1.setPos(-2F, 13F, -1F);
            this.leg1.xRot = 0F;
            this.leg1.yRot = 1.047198F;
            this.leg1.zRot = 0.6981317F;

            this.leg2.setPos(2F, 13F, -1F);
            this.leg2.xRot = 0F;
            this.leg2.yRot = -1.047198F;
            this.leg2.zRot = -0.6981317F;

            this.leg3.setPos(2F, 20F, 5F);
            this.leg3.xRot = 0F;
            this.leg3.yRot = 0F;
            this.leg3.zRot = 1.396263F;

            this.leg4.setPos(-2F, 20F, 5F);
            this.leg4.xRot = 0F;
            this.leg4.yRot = 0F;
            this.leg4.zRot = -1.396263F;

            this.body.setPos(0F, 16F, 2F);
            this.body.xRot = -0.9948377F;
            this.body.yRot = 0F;
            this.body.zRot = 0F;

            this.tail.setPos(0F, 20F, 6F);
            this.tail.xRot = 0.6806784F;
            this.tail.yRot = 0F;
            this.tail.zRot = 0F;

            // Head follows netHeadYaw/headPitch
            this.head.xRot = headPitch * ((float)Math.PI / 180F);
            this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.head.zRot = 0F;
        } else {
            // “Default (not held)” pose
            this.leg1.setPos(2F, 22F, -4F);
            this.leg1.xRot = 0F;
            this.leg1.yRot = 0F;
            this.leg1.zRot = 0.3839724F; // ≈ 22°

            this.leg2.setPos(-2F, 22F, -4F);
            this.leg2.xRot = 0F;
            this.leg2.yRot = 0F;
            this.leg2.zRot = -0.3839724F;

            this.leg3.setPos(2F, 22F, 5F);
            this.leg3.xRot = 0F;
            this.leg3.yRot = 0F;
            this.leg3.zRot = 0.3839724F;

            this.leg4.setPos(-2F, 22F, 5F);
            this.leg4.xRot = 0F;
            this.leg4.yRot = 0F;
            this.leg4.zRot = -0.3839724F;

            this.body.setPos(0F, 21F, 0F);
            this.body.xRot = 0F;
            this.body.yRot = 0F;
            this.body.zRot = 0F;

            this.tail.setPos(0F, 21F, 6F);
            this.tail.xRot = 0F;
            this.tail.yRot = 0F;
            this.tail.zRot = 0F;

            // Legs 1 & 2 swing in opposite phase
            this.leg1.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
            this.leg2.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;

            // Folded‐head orientation
            this.foldHead.xRot = headPitch * ((float)Math.PI / 180F);
            this.foldHead.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.foldHead.zRot = 0F;
        }

        // Common animations for legs 3 & 4 and tail rotation
        this.leg3.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
        this.leg4.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;

        this.tail.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.2F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // If holding an item: render “head” (which has Filch children); otherwise, render folded head + folds
        if (this.heldItem) {
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.foldHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.foldFilch1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.foldFilch2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }

    /**
     * Defines and textures all child parts.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ─── BODY ──────────────────────────────────────────────────────────────────
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-2F, -1.5F, -6F, 4, 3, 12, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 21F, 0F)
        );

        // ─── TAIL ──────────────────────────────────────────────────────────────────
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(32, 9)
                        .addBox(-1F, -0.5F, 0F, 2, 2, 10, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 21F, 6F)
        );

        // ─── LEGS ──────────────────────────────────────────────────────────────────
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(2F, 22F, -4F)
        );
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create()
                        .texOffs(16, 3)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 22F, -4F)
        );
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(0F, -0.5F, -0.5F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(2F, 22F, 5F)
        );
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create()
                        .texOffs(16, 3)
                        .addBox(-4F, -0.5F, -0.5F, 4, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 22F, 5F)
        );

        // ─── FOLDED FILCHS (used when not holding an item) ─────────────────────────
        root.addOrReplaceChild("fold_filch1",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(1F, -1.5F, 0F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 21F, -6F, 0F, 0.0349066F, 0F)
        );
        root.addOrReplaceChild("fold_filch2",
                CubeListBuilder.create()
                        .texOffs(14, 22)
                        .addBox(-2F, -1.5F, 0F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 21F, -6F, 0F, -0.0349066F, 0F)
        );
        root.addOrReplaceChild("fold_head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2F, -0.5F, -4F, 4, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 21F, -6F)
        );

        // ─── HEAD (with Filch children, used when holding an item) ─────────────────
        PartDefinition headPart = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2F, -2.5F, -4F, 4, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 12F, -1F)
        );
        headPart.addOrReplaceChild("filch1",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(0F, -2.5F, 2.5F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.3665191F, 1.570796F, -0.296706F)
        );
        headPart.addOrReplaceChild("filch2",
                CubeListBuilder.create()
                        .texOffs(14, 22)
                        .addBox(-1F, -2.5F, 2.5F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.3665191F, -1.570796F, 0.296706F)
        );
        headPart.addOrReplaceChild("filch3",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-0.5F, -2.5F, 2F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 1.570796F, -0.2617994F)
        );
        headPart.addOrReplaceChild("filch4",
                CubeListBuilder.create()
                        .texOffs(14, 22)
                        .addBox(-0.5F, -2.5F, 2F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, -1.570796F, 0.2617994F)
        );
        headPart.addOrReplaceChild("filch5",
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-1F, -2.5F, 1.5F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.3839724F, 1.570796F, -0.2617994F)
        );
        headPart.addOrReplaceChild("filch6",
                CubeListBuilder.create()
                        .texOffs(14, 22)
                        .addBox(0F, -2.5F, 1.5F, 1, 3, 6, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.4014257F, -1.570796F, 0.2617994F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }
}
