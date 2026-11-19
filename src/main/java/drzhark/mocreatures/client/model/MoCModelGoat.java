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
import drzhark.mocreatures.entity.neutral.MoCEntityGoat;
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

/**
 * Port of MoCModelGoat (1.16.5) to 1.20.1. Uses ModelPart instead of ModelRenderer,
 * and moves animations into setupAnim.
 * 
 * I think the goatee isn't attached to the chin, but I'm not sure.
 */
public class MoCModelGoat<T extends MoCEntityGoat> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "goat"), "main"
    );

    // Dynamic state fields (populated externally by entity before render)
    public int typeInt;
    public int attacking;
    public float age;
    public boolean bleat;
    public int legMov;
    public int earMov;
    public int tailMov;
    public int eatMov;
    private float prevTailAngleX;
    private float prevLEarAngleX;
    private float prevREarAngleX;
    private float prevMouthAngleX;

    // Model parts
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart lEar;
    private final ModelPart rEar;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart tongue;
    private final ModelPart mouth;
    private final ModelPart rHorn1;
    private final ModelPart rHorn2;
    private final ModelPart rHorn3;
    private final ModelPart rHorn4;
    private final ModelPart rHorn5;
    private final ModelPart lHorn1;
    private final ModelPart lHorn2;
    private final ModelPart lHorn3;
    private final ModelPart lHorn4;
    private final ModelPart lHorn5;
    private final ModelPart goatie;
    private final ModelPart neck;
    private final ModelPart tits;

    public MoCModelGoat(ModelPart root) {
        super(root);
        this.leg1   = root.getChild("leg1");
        this.leg2   = root.getChild("leg2");
        this.leg3   = root.getChild("leg3");
        this.leg4   = root.getChild("leg4");
        this.body   = root.getChild("body");
        this.tail   = root.getChild("tail");
        this.lEar   = root.getChild("l_ear");
        this.rEar   = root.getChild("r_ear");
        this.head   = root.getChild("head");
        this.nose   = root.getChild("nose");
        this.tongue = root.getChild("tongue");
        this.mouth  = root.getChild("mouth");
        this.rHorn1 = root.getChild("r_horn1");
        this.rHorn2 = root.getChild("r_horn2");
        this.rHorn3 = root.getChild("r_horn3");
        this.rHorn4 = root.getChild("r_horn4");
        this.rHorn5 = root.getChild("r_horn5");
        this.lHorn1 = root.getChild("l_horn1");
        this.lHorn2 = root.getChild("l_horn2");
        this.lHorn3 = root.getChild("l_horn3");
        this.lHorn4 = root.getChild("l_horn4");
        this.lHorn5 = root.getChild("l_horn5");
        this.goatie = root.getChild("goatie");
        this.neck   = root.getChild("neck");
        this.tits   = root.getChild("tits");
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
        // Leg movement (traditional quadruped gait)
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        // Head and neck orientation
        float headYawClamped = netHeadYaw;
        if (headYawClamped > 20F) headYawClamped = 20F;
        if (headYawClamped < -20F) headYawClamped = -20F;

        float headYawRad   = headYawClamped * ((float)Math.PI / 180F);
        float headPitchRad = headPitch * ((float)Math.PI / 180F);
        float baseAngle    = (30F * ((float)Math.PI / 180F)) + headPitchRad; // 30° + pitched

        this.head.yRot = headYawRad;
        this.neck.xRot = -30F * ((float)Math.PI / 180F); // fixed at –30°

        // Default head angle
        this.head.xRot = baseAngle;

        // Bleating overrides head pitch
        if (this.bleat) {
            this.head.xRot = -15F * ((float)Math.PI / 180F); // –15°
        }

        // Attacking overrides head & neck
        if (this.attacking != 0) {
            float attackRad = this.attacking * ((float)Math.PI / 180F);
            this.head.xRot = attackRad;
            this.neck.xRot = ((1.33F * this.attacking) - 70F) * ((float)Math.PI / 180F);
            if (this.legMov != 0) {
                this.leg1.xRot = this.legMov * ((float)Math.PI / 180F);
            }
        }

        // Sync child parts to head/neck rotations
        this.nose.xRot   = this.head.xRot;
        this.nose.yRot   = this.head.yRot;
        this.tongue.xRot = this.head.xRot;
        this.tongue.yRot = this.head.yRot;
        this.goatie.xRot = this.head.xRot;
        this.goatie.yRot = this.head.yRot;
        this.rHorn1.xRot = this.head.xRot; this.rHorn1.yRot = this.head.yRot;
        this.lHorn1.xRot = this.head.xRot; this.lHorn1.yRot = this.head.yRot;
        this.rHorn2.xRot = this.head.xRot; this.rHorn2.yRot = this.head.yRot;
        this.lHorn2.xRot = this.head.xRot; this.lHorn2.yRot = this.head.yRot;
        this.rHorn3.xRot = this.head.xRot; this.rHorn3.yRot = this.head.yRot;
        this.lHorn3.xRot = this.head.xRot; this.lHorn3.yRot = this.head.yRot;
        this.rHorn4.xRot = this.head.xRot; this.rHorn4.yRot = this.head.yRot;
        this.lHorn4.xRot = this.head.xRot; this.lHorn4.yRot = this.head.yRot;
        this.rHorn5.xRot = this.head.xRot; this.rHorn5.yRot = this.head.yRot;
        this.lHorn5.xRot = this.head.xRot; this.lHorn5.yRot = this.head.yRot;
        this.mouth.yRot  = this.head.yRot;

        this.lEar.yRot = this.head.yRot;
        this.rEar.yRot = this.head.yRot;

        // Interpolation factor for smooth tail/ear/mouth movement
        float interp = 0.1F;

        // Tail movement
        float targetTailX = this.tailMov * ((float)Math.PI / 180F);
        float interpTail = this.prevTailAngleX + (targetTailX - this.prevTailAngleX) * interp;
        this.tail.xRot = interpTail;

        // Ears movement (unless bleating or attacking)
        float targetEarX = (!this.bleat && this.attacking == 0)
                ? baseAngle + (this.earMov * ((float)Math.PI / 180F))
                : this.head.xRot;
        float interpLEar = this.prevLEarAngleX + (targetEarX - this.prevLEarAngleX) * interp;
        float interpREar = this.prevREarAngleX + (targetEarX - this.prevREarAngleX) * interp;
        this.lEar.xRot = interpLEar;
        this.rEar.xRot = interpREar;

        // Mouth movement (bleat or attack)
        float targetMouthX = this.bleat
                ? 0F
                : (this.attacking != 0
                ? this.attacking * ((float)Math.PI / 180F)
                : baseAngle);
        float interpMouth = this.prevMouthAngleX + (targetMouthX - this.prevMouthAngleX) * interp;
        this.mouth.xRot = interpMouth;

        // Save for next frame
        this.prevTailAngleX  = interpTail;
        this.prevLEarAngleX  = interpLEar;
        this.prevREarAngleX  = interpREar;
        this.prevMouthAngleX = interpMouth;
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
        // Draw legs and body
        this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Tits for certain goat types (1 < typeInt < 5)
        if (this.typeInt > 1 && this.typeInt < 5) {
            this.tits.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        poseStack.pushPose();
        // If attacking, apply a small translation
        if (this.attacking != 0) {
            float yOff = (this.attacking / 150F) - (1F / 5F);
            float zOff = (this.attacking / 450F) - (1F / 15F);
            poseStack.translate(0.0F, yOff, -zOff);
        }

        // Ears, head, nose
        this.lEar.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rEar.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.nose.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Horns for typeInt > 1
        if (this.typeInt > 1) {
            if (this.age > 0.7F) {
                this.rHorn1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lHorn1.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (this.age > 0.8F) {
                this.rHorn2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lHorn2.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // Extra horns for typeInt > 4
        if (this.typeInt > 4) {
            if (this.age > 0.8F) {
                this.rHorn3.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lHorn3.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (this.age > 0.85F) {
                this.rHorn4.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lHorn4.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (this.age > 0.9F) {
                this.rHorn5.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lHorn5.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // Eating translation if applicable
        if (this.eatMov != 0 && !this.bleat) {
            poseStack.translate(this.eatMov / 100F, 0.0F, 0.0F);
        }

        // Goatie for highest horn types
        if (this.typeInt > 4 && this.age > 0.9F) {
            this.goatie.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Tongue and mouth
        this.tongue.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.mouth.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }

    /**
     * Defines the geometry (MeshDefinition) and texture size (64×32) for this model.
     * Register this with EntityRenderersEvent.RegisterLayerDefinitions.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition root    = mesh.getRoot();

        // LEG1
        root.addOrReplaceChild("leg1",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-1F, 0F, -1F, 2, 7, 2, new CubeDeformation(0.0F)),
                PartPose.offset(2F, 17F, -6F)
        );

        // LEG2
        root.addOrReplaceChild("leg2",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-1F, 0F, -1F, 2, 7, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 17F, -6F)
        );

        // LEG3
        root.addOrReplaceChild("leg3",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-1F, 0F, -1F, 2, 7, 2, new CubeDeformation(0.0F)),
                PartPose.offset(-2F, 17F, 6F)
        );

        // LEG4
        root.addOrReplaceChild("leg4",
                CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-1F, 0F, -1F, 2, 7, 2, new CubeDeformation(0.0F)),
                PartPose.offset(2F, 17F, 6F)
        );

        // BODY
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(20, 8)
                        .addBox(-3F, -4F, -8F, 6, 8, 16, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 13F, 0F)
        );

        // TAIL
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(22, 8)
                        .addBox(-1.5F, -1F, 0F, 3, 2, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 10F, 8F)
        );

        // LEFT EAR
        root.addOrReplaceChild("l_ear",
                CubeListBuilder.create()
                        .texOffs(52, 8)
                        .addBox(1.5F, -2F, 0F, 2, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT EAR
        root.addOrReplaceChild("r_ear",
                CubeListBuilder.create()
                        .texOffs(52, 8)
                        .addBox(-3.5F, -2F, 0F, 2, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // HEAD
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(52, 16)
                        .addBox(-1.5F, -2F, -2F, 3, 5, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // NOSE
        root.addOrReplaceChild("nose",
                CubeListBuilder.create()
                        .texOffs(52, 10)
                        .addBox(-1.5F, -1F, -5F, 3, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // TONGUE
        root.addOrReplaceChild("tongue",
                CubeListBuilder.create()
                        .texOffs(56, 5)
                        .addBox(-0.5F, 2F, -5F, 1, 0, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // MOUTH
        root.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(54, 0)
                        .addBox(-1F, 2F, -5F, 2, 1, 3, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT HORN 1
        root.addOrReplaceChild("r_horn1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -3F, -0.7F, 1, 1, 1, new CubeDeformation(0.1F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT HORN 2
        root.addOrReplaceChild("r_horn2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.9F, -4F, -0.2F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT HORN 3
        root.addOrReplaceChild("r_horn3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.1F, -4.8F, 0.5F, 1, 1, 1, new CubeDeformation(-0.05F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT HORN 4
        root.addOrReplaceChild("r_horn4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.3F, -5.2F, 1.4F, 1, 1, 1, new CubeDeformation(-0.10F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // RIGHT HORN 5
        root.addOrReplaceChild("r_horn5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.6F, -4.9F, 2.0F, 1, 1, 1, new CubeDeformation(-0.15F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // LEFT HORN 1
        root.addOrReplaceChild("l_horn1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.5F, -3F, -0.7F, 1, 1, 1, new CubeDeformation(0.1F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // LEFT HORN 2
        root.addOrReplaceChild("l_horn2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.9F, -4F, -0.2F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // LEFT HORN 3
        root.addOrReplaceChild("l_horn3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1.2F, -4.9F, 0.5F, 1, 1, 1, new CubeDeformation(-0.05F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // LEFT HORN 4
        root.addOrReplaceChild("l_horn4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1.4F, -5.3F, 1.4F, 1, 1, 1, new CubeDeformation(-0.10F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // LEFT HORN 5
        root.addOrReplaceChild("l_horn5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(1.7F, -4.9F, 2.1F, 1, 1, 1, new CubeDeformation(-0.15F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // GOATIE (goat beard)
        root.addOrReplaceChild("goatie",
                CubeListBuilder.create()
                        .texOffs(52, 5)
                        .addBox(-0.5F, 3F, -4F, 1, 2, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 8F, -12F)
        );

        // NECK
        root.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(18, 14)
                        .addBox(-1.5F, -2F, -5F, 3, 4, 6, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(0F, 11F, -8F, -0.418879F, 0F, 0F) // –24° ≈ –0.418879 rad
        );

        // TITS (udder)
        root.addOrReplaceChild("tits",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(-2.5F, 0F, -2F, 5, 1, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 17F, 3F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }
}
