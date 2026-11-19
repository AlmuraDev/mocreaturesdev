/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model.legacy;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.client.model.MoCEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
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

public class MoCLegacyModelBigCat2<T extends MoCEntityBigCat> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "legacy_big_cat2"), "main"
    );

    // Public flags (set externally, e.g. in EntityRenderer or via setLivingAnimations):
    public boolean sitting;
    public boolean tamed;

    // All of the pieces we need:
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart ears;
    private final ModelPart collar;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public MoCLegacyModelBigCat2(ModelPart root) {
        super(root);
        // Grab each child by the exact name we gave it in createBodyLayer():
        this.ears   = root.getChild("ears");
        this.head   = root.getChild("head");
        this.snout  = root.getChild("snout");
        this.collar = root.getChild("collar");
        this.body   = root.getChild("body");
        this.tail   = root.getChild("tail");
        this.leg1   = root.getChild("leg1");
        this.leg2   = root.getChild("leg2");
        this.leg3   = root.getChild("leg3");
        this.leg4   = root.getChild("leg4");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        /*
         * EARS
         *   this.ears = new ModelRenderer(this, 16, 25);
         *   this.ears.addBox(-4F, -7F, -3F, 8, 4, 1);
         *   this.ears.setRotationPoint(0F, 4F, -8F);
         */
        root.addOrReplaceChild("ears",
                CubeListBuilder.create()
                        .texOffs(16, 25)
                        .addBox(
                                /* x = */ -4.0F,
                                /* y = */ -7.0F,
                                /* z = */ -3.0F,
                                /* dx = */ 8,
                                /* dy = */ 4,
                                /* dz = */ 1
                        ),
                PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        /*
         * HEAD
         *   this.head = new ModelRenderer(this, 0, 0);
         *   this.head.addBox(-4F, -4F, -6F, 8, 8, 6);
         *   this.head.setRotationPoint(0F, 4F, -8F);
         */
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                /* x = */ -4.0F,
                                /* y = */ -4.0F,
                                /* z = */ -6.0F,
                                /* dx = */ 8,
                                /* dy = */ 8,
                                /* dz = */ 6
                        ),
                PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        /*
         * SNOUT
         *   this.snout = new ModelRenderer(this, 14, 14);
         *   this.snout.addBox(-2F, 0F, -9F, 4, 4, 6);
         *   this.snout.setRotationPoint(0F, 4F, -8F);
         */
        root.addOrReplaceChild("snout",
                CubeListBuilder.create()
                        .texOffs(14, 14)
                        .addBox(
                                /* x = */ -2.0F,
                                /* y = */  0.0F,
                                /* z = */ -9.0F,
                                /* dx = */ 4,
                                /* dy = */ 4,
                                /* dz = */ 6
                        ),
                PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        /*
         * COLLAR
         *   this.collar = new ModelRenderer(this, 24, 0);
         *   this.collar.addBox(-2.5F, 4F, -3F, 5, 4, 1);
         *   this.collar.setRotationPoint(0F, 4F, -8F);
         */
        root.addOrReplaceChild("collar",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(
                                /* x = */ -2.5F,
                                /* y = */  4.0F,
                                /* z = */ -3.0F,
                                /* dx = */ 5,
                                /* dy = */ 4,
                                /* dz = */ 1
                        ),
                PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        /*
         * BODY
         *   this.body = new ModelRenderer(this, 28, 0);
         *   this.body.addBox(-5F, -10F, -7F, 10, 18, 8);
         *   this.body.setRotationPoint(0F, 5F, 2F);
         */
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(
                                /* x = */ -5.0F,
                                /* y = */ -10.0F,
                                /* z = */ -7.0F,
                                /* dx = */ 10,
                                /* dy = */ 18,
                                /* dz = */ 8
                        ),
                PartPose.offset(0.0F, 5.0F, 2.0F)
        );

        /*
         * TAIL
         *   this.tail = new ModelRenderer(this, 26, 15);
         *   this.tail.addBox(-5F, -5F, -2F, 3, 3, 14);
         *   this.tail.setRotationPoint(3.5F, 9.3F, 9F);
         *   this.tail.rotateAngleX = -0.5235988F;
         */
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(26, 15)
                        .addBox(
                                /* x = */ -5.0F,
                                /* y = */ -5.0F,
                                /* z = */ -2.0F,
                                /* dx = */ 3,
                                /* dy = */ 3,
                                /* dz = */ 14
                        ),
                PartPose.offsetAndRotation(
                        /* pivotX = */ 3.5F,
                        /* pivotY = */ 9.3F,
                        /* pivotZ = */ 9.0F,
                        /* rotX   = */ -0.5235988F,
                        /* rotY   = */  0.0F,
                        /* rotZ   = */  0.0F
                )
        );

        /*
         * LEG1 (front left)
         *   this.leg1 = new ModelRenderer(this, 0, 16);
         *   this.leg1.addBox(-2F, 0F, -2F, 4, 12, 4);
         *   this.leg1.setRotationPoint(-3F, 12F, 7F);
         */
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                /* x = */ -2.0F,
                                /* y = */  0.0F,
                                /* z = */ -2.0F,
                                /* dx = */ 4,
                                /* dy = */ 12,
                                /* dz = */ 4
                        ),
                PartPose.offset(-3.0F, 12.0F,  7.0F)
        );

        /*
         * LEG2 (front right)
         *   this.leg2 = new ModelRenderer(this, 0, 16);
         *   this.leg2.addBox(-2F, 0F, -2F, 4, 12, 4);
         *   this.leg2.setRotationPoint(3F, 12F, 7F);
         */
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                /* x = */ -2.0F,
                                /* y = */  0.0F,
                                /* z = */ -2.0F,
                                /* dx = */ 4,
                                /* dy = */ 12,
                                /* dz = */ 4
                        ),
                PartPose.offset( 3.0F, 12.0F,  7.0F)
        );

        /*
         * LEG3 (back left)
         *   this.leg3 = new ModelRenderer(this, 0, 16);
         *   this.leg3.addBox(-2F, 0F, -2F, 4, 12, 4);
         *   this.leg3.setRotationPoint(-3F, 12F, -5F);
         */
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                /* x = */ -2.0F,
                                /* y = */  0.0F,
                                /* z = */ -2.0F,
                                /* dx = */ 4,
                                /* dy = */ 12,
                                /* dz = */ 4
                        ),
                PartPose.offset(-3.0F, 12.0F, -5.0F)
        );

        /*
         * LEG4 (back right)
         *   this.leg4 = new ModelRenderer(this, 0, 16);
         *   this.leg4.addBox(-2F, 0F, -2F, 4, 12, 4);
         *   this.leg4.setRotationPoint(3F, 12F, -5F);
         */
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                /* x = */ -2.0F,
                                /* y = */  0.0F,
                                /* z = */ -2.0F,
                                /* dx = */ 4,
                                /* dy = */ 12,
                                /* dz = */ 4
                        ),
                PartPose.offset( 3.0F, 12.0F, -5.0F)
        );

        // The old code never set texture height beyond about “(28,0)” etc., so
        // a 64×32 sheet (or 64×64 if you prefer) will cover all offsets used above.
        // Adjust these two numbers if your texture is a different size.
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {

        // 1) Head rotation:
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        // 2) Legs swinging normally:
        float swingFactor = 0.6662F;
        float frontLeftLegRot  = Mth.cos(limbSwing * swingFactor) * 1.4F * limbSwingAmount;
        float frontRightLegRot = Mth.cos(limbSwing * swingFactor + (float)Math.PI) * 1.4F * limbSwingAmount;
        float backLeftLegRot   = frontRightLegRot; // same as leg2
        float backRightLegRot  = frontLeftLegRot;  // same as leg1

        this.leg1.xRot = frontLeftLegRot;   // front-left
        this.leg2.xRot = frontRightLegRot;  // front-right
        this.leg3.xRot = frontRightLegRot;  // back-left
        this.leg4.xRot = frontLeftLegRot;   // back-right

        // 3) Sync snout, ears, collar to head's rotation:
        this.snout.yRot   = this.head.yRot;
        this.snout.xRot   = this.head.xRot;
        this.ears.yRot    = this.head.yRot;
        this.ears.xRot    = this.head.xRot;
        this.collar.yRot  = this.head.yRot;
        this.collar.xRot  = this.head.xRot;

        // 4) Handle “sitting” vs. “not sitting” pivot positions & tail wag:
        if (!this.sitting) {
            // — Body (upright):
            this.body.x = 0.0F;
            this.body.y = 5.0F;
            this.body.z = 2.0F;
            this.body.xRot = 1.570796F;   // 90°

            // — Legs: reset to original pivots
            this.leg1.x = -3.0F; this.leg1.z =  7.0F;
            this.leg2.x =  3.0F; this.leg2.z =  7.0F;
            this.leg3.x = -3.0F; this.leg3.z = -5.0F;
            this.leg4.x =  3.0F; this.leg4.z = -5.0F;
            this.leg1.y = this.leg2.y = this.leg3.y = this.leg4.y = 12.0F;

            // — Tail: original pivot & base rotation
            this.tail.x = 3.5F;   this.tail.y =  9.3F;   this.tail.z = 9.0F;
            this.tail.xRot = -0.5235988F; // –30°
            // + wagging left/right based on limbSwing:
            this.tail.yRot = Mth.cos(limbSwing * swingFactor) * 0.7F * limbSwingAmount;
        } else {
            // — Sitting posture adjustments:
            this.body.x = 0.0F;
            this.body.y = 12.0F;
            this.body.z = 1.0F;
            this.body.xRot = 0.8726646F;  // ≈ 50°

            // — Legs fold under:
            this.leg1.x = -5.0F; this.leg1.z =  0.0F;
            this.leg2.x =  5.0F; this.leg2.z =  0.0F;
            this.leg3.x = -2.0F; this.leg3.z = -8.0F;
            this.leg4.x =  2.0F; this.leg4.z = -8.0F;
            this.leg1.y = this.leg2.y = this.leg3.y = this.leg4.y = 12.0F;

            // — Tail lowered:
            this.tail.x = 3.5F;   this.tail.y = 22.0F;   this.tail.z = 8.0F;
            this.tail.xRot = -0.1745329F; // ≈ –10°
            this.tail.yRot = 0.0F;        // no wag
        }
    }

    public void copyManePose(ModelPart maneHead, ModelPart maneBody) {
        maneHead.loadPose(this.head.storePose());
        maneBody.loadPose(this.body.storePose());
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Render in roughly the same order you did originally:
        this.snout.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ears.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Only draw the collar if tamed == true:
        if (this.tamed) {
            this.collar.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
}
