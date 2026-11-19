package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityDuck;
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
public class MoCModelDuck<T extends MoCEntityDuck> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "duck"), "main"
    );

    private final ModelPart head;
    private final ModelPart bill;
    private final ModelPart chin;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public MoCModelDuck(ModelPart root) {
        super(root);
        this.head      = root.getChild("head");
        this.bill      = root.getChild("bill");
        this.chin      = root.getChild("chin");
        this.body      = root.getChild("body");
        this.rightLeg  = root.getChild("rightLeg");
        this.leftLeg   = root.getChild("leftLeg");
        this.rightWing = root.getChild("rightWing");
        this.leftWing  = root.getChild("leftWing");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        /*
         * In the old constructor, var1 = 16. So pivots are calculated with that offset.
         * head: texOffs(0,0), addBox(-2,-6,-2, 4,6,3), pivot(0, 15, -4)
         */
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3),
                PartPose.offset(0.0F, 15.0F, -4.0F)
        );

        // bill: texOffs(14,0), addBox(-2,-4,-4, 4,2,2), pivot(0,15,-4)
        root.addOrReplaceChild("bill",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2),
                PartPose.offset(0.0F, 15.0F, -4.0F)
        );

        // chin: texOffs(14,4), addBox(-1,-2,-3, 2,2,2), pivot(0,15,-4)
        root.addOrReplaceChild("chin",
                CubeListBuilder.create()
                        .texOffs(14, 4)
                        .addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2),
                PartPose.offset(0.0F, 15.0F, -4.0F)
        );

        // body: texOffs(0,9), addBox(-3,-4,-3, 6,8,6), pivot(0,16,0)
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        // rightLeg: texOffs(26,0), addBox(-1,0,-3, 3,5,3), pivot(-2,19,1)
        root.addOrReplaceChild("rightLeg",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-1.0F,  0.0F, -3.0F, 3, 5, 3),
                PartPose.offset(-2.0F, 19.0F,  1.0F)
        );

        // leftLeg: texOffs(26,0), addBox(-1,0,-3, 3,5,3), pivot(1,19,1)
        root.addOrReplaceChild("leftLeg",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-1.0F,  0.0F, -3.0F, 3, 5, 3),
                PartPose.offset(1.0F, 19.0F,  1.0F)
        );

        // rightWing: texOffs(24,13), addBox(0,0,-3, 1,4,6), pivot(-4,13,0)
        root.addOrReplaceChild("rightWing",
                CubeListBuilder.create()
                        .texOffs(24, 13)
                        .addBox(0.0F,  0.0F, -3.0F, 1, 4, 6),
                PartPose.offset(-4.0F, 13.0F,  0.0F)
        );

        // leftWing: texOffs(24,13), addBox(-1,0,-3, 1,4,6), pivot(4,13,0)
        root.addOrReplaceChild("leftWing",
                CubeListBuilder.create()
                        .texOffs(24, 13)
                        .addBox(-1.0F,  0.0F, -3.0F, 1, 4, 6),
                PartPose.offset(4.0F, 13.0F,  0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * This replaces setRotationAngles(...) from 1.16. We rotate:
     *  • head, bill, chin based on headPitch and netHeadYaw
     *  • legs based on walking animation
     *  • wings flapping when not on ground
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {

        // head rotation (in radians)
        this.head.xRot = -headPitch * ((float)Math.PI / 180F);
        this.head.yRot =  netHeadYaw   * ((float)Math.PI / 180F);

        // bill & chin follow the head angles
        this.bill.xRot = this.head.xRot;
        this.bill.yRot = this.head.yRot;
        this.chin.xRot = this.head.xRot;
        this.chin.yRot = this.head.yRot;

        // body sits at 90° (π/2) so duck stands upright
        this.body.xRot = ((float)Math.PI / 2F);

        // legs walk: same cosine pattern as before
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot  = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

        // wing flapping only when not on ground:
        if (!entity.onGround()) {
            // WingRot = cos(ageInTicks * 1.4 + π) * 0.6
            float WingRot = Mth.cos(ageInTicks * 1.4F + (float)Math.PI) * 0.6F;
            this.rightWing.zRot = 0.5F + WingRot;
            this.leftWing.zRot  = -0.5F - WingRot;
        } else {
            this.rightWing.zRot = 0F;
            this.leftWing.zRot  = 0F;
        }
    }

    /**
     * Renders all parts in one call. There is no special transparency here—wings are opaque.
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.bill.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.chin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
