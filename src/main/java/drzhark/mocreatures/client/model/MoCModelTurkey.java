/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityTurkey;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Port of MoCModelTurkey from 1.16.5 → 1.20.1 Forge.
 */
public class MoCModelTurkey<T extends MoCEntityTurkey> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "turkey"), "main"
    );

    // All the parts that correspond to your old ModelRenderers:
    private final ModelPart beak;
    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart chest;
    private final ModelPart rWing;
    private final ModelPart lWing;
    private final ModelPart uBody;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart rLeg;
    private final ModelPart rFoot;
    private final ModelPart lLeg;
    private final ModelPart lFoot;

    // Animation state
    private boolean male;

    public MoCModelTurkey(ModelPart root) {
        super(root);
        // Grab each child by name from the baked root:
        this.beak   = root.getChild("beak");
        this.head   = root.getChild("head");
        this.neck   = root.getChild("neck");
        this.chest  = root.getChild("chest");
        this.rWing  = root.getChild("rWing");
        this.lWing  = root.getChild("lWing");
        this.uBody  = root.getChild("uBody");
        this.body   = root.getChild("body");
        this.tail   = root.getChild("tail");
        this.rLeg   = root.getChild("rLeg");
        this.rFoot  = root.getChild("rFoot");
        this.lLeg   = root.getChild("lLeg");
        this.lFoot  = root.getChild("lFoot");
    }

    /**
     * Defines all turkey parts (beak, head, neck, chest, wings, body, tail, legs, feet).
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // BEAK
        root.addOrReplaceChild(
                "beak",
                CubeListBuilder.create()
                        .texOffs(17, 17)
                        .addBox(-0.5F, -1.866667F, -3.366667F, 1, 1, 2),
                PartPose.offsetAndRotation(
                        0F, 9.7F, -5.1F,
                        0.7807508F, 0F, 0F
                )
        );

        // HEAD
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(-1F, -2F, -2F, 2, 2, 3),
                PartPose.offsetAndRotation(
                        0F, 9.7F, -5.1F,
                        0.4833219F, 0F, 0F
                )
        );

        // NECK
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-1F, -6F, -1F, 2, 6, 2),
                PartPose.offsetAndRotation(
                        0F, 14.7F, -6.5F,
                        -0.2246208F, 0F, 0F
                )
        );

        // CHEST
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-3F, 0F, -4F, 6, 6, 4),
                PartPose.offsetAndRotation(
                        0F, 12.5F, -4F,
                        0.5934119F, 0F, 0F
                )
        );

        // RIGHT WING
        root.addOrReplaceChild(
                "rWing",
                CubeListBuilder.create()
                        .texOffs(32, 30)
                        .addBox(-1F, -2F, 0F, 1, 6, 7),
                PartPose.offsetAndRotation(
                        -4F, 14F, -3F,
                        -0.3346075F, 0F, 0F
                )
        );

        // LEFT WING
        root.addOrReplaceChild(
                "lWing",
                CubeListBuilder.create()
                        .texOffs(48, 30)
                        .addBox(0F, -2F, 0F, 1, 6, 7),
                PartPose.offsetAndRotation(
                        4F, 14F, -3F,
                        -0.3346075F, 0F, 0F
                )
        );

        // UPPER BODY (male tail fan)
        root.addOrReplaceChild(
                "uBody",
                CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(-2.5F, -4F, 0F, 5, 7, 9),
                PartPose.offset(0F, 15F, -3F)
        );

        // BODY
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4F, -4F, 0F, 8, 8, 9),
                PartPose.offset(0F, 16F, -4F)
        );

        // TAIL
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                        .texOffs(32, 17)
                        .addBox(-8F, -9F, 0F, 16, 12, 0),
                PartPose.offsetAndRotation(
                        0F, 14F, 6F,
                        -0.2974289F, 0F, 0F
                )
        );

        // RIGHT LEG
        root.addOrReplaceChild(
                "rLeg",
                CubeListBuilder.create()
                        .texOffs(27, 17)
                        .addBox(-0.5F, 0F, -0.5F, 1, 5, 1),
                PartPose.offset( -2F, 19F, 0.5F )
        );

        // RIGHT FOOT
        root.addOrReplaceChild(
                "rFoot",
                CubeListBuilder.create()
                        .texOffs(20, 23)
                        .addBox(-1.5F, 5F, -2.5F, 3, 0, 3),
                PartPose.offset( -2F, 19F, 0.5F )
        );

        // LEFT LEG
        root.addOrReplaceChild(
                "lLeg",
                CubeListBuilder.create()
                        .texOffs(23, 17)
                        .addBox(-0.5F, 0F, -0.5F, 1, 5, 1),
                PartPose.offset( 2F, 19F, 0.5F )
        );

        // LEFT FOOT
        root.addOrReplaceChild(
                "lFoot",
                CubeListBuilder.create()
                        .texOffs(20, 26)
                        .addBox(-1.5F, 5F, -2.5F, 3, 0, 3),
                PartPose.offset( 2F, 19F, 0.5F )
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    /**
     * Equivalent to 1.16.5's setLivingAnimations - sets the male flag before animations
     */
    @Override
    public void prepareMobModel(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick
    ) {
        // Set male flag first (equivalent to setLivingAnimations in 1.16.5)
        this.male = entity.getTypeMoC() == 1;
    }

    /**
     * Called once before rendering; this now just delegates to setRotationAngles.
     */
    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Call setRotationAngles with the exact same parameters as 1.16.5
        setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /**
     * Exact port of 1.16.5 setRotationAngles method
     */
    public void setRotationAngles(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Exact same calculations as 1.16.5
        float LLegXRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        float RLegXRot = Mth.cos((limbSwing * 0.6662F) + 3.141593F) * 1.4F * limbSwingAmount;
        float wingF = (Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) / 4F;

        // HEAD pitch & yaw - exact same as 1.16.5
        this.head.xRot = 0.4833219F + headPitch / 57.29578F;
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);

        // BEAK follows head - EXACT same calculation as 1.16.5
        this.beak.xRot = 0.2974F + this.head.xRot;
        this.beak.yRot = this.head.yRot;

        // LEGS - exact same as 1.16.5
        this.lLeg.xRot  = LLegXRot;
        this.lFoot.xRot = this.lLeg.xRot;
        this.rLeg.xRot  = RLegXRot;
        this.rFoot.xRot = this.rLeg.xRot;

        // WINGS - exact same as 1.16.5
        this.lWing.yRot = wingF;
        this.rWing.yRot = -wingF;

        // TAIL & body‐positioning - exact same as 1.16.5
        if (this.male) {
            this.tail.xRot = -0.2974289F + wingF;
            this.tail.y = 14F;
            this.tail.z = 6F;
            this.chest.y = 12.5F;
            this.body.y  = 16F;
            this.lWing.x = 4F;
            this.rWing.x = -4F;
        } else {
            this.tail.xRot = wingF - (110 / 57.29578F);
            this.tail.y = 17F;
            this.tail.z = 7F;
            this.chest.y = 16F;
            this.body.y  = 20F;
            this.lWing.x = 3.2F;
            this.rWing.x = -3.2F;
        }
    }

    /**
     * Renders all parts. Exact port of 1.16.5 render method.
     */
    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        if (this.young) {
            // All children rendered as "female" - exact same as 1.16.5
            poseStack.pushPose();
            poseStack.translate(0.0F, 5.0F, 2.0F);
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0F, 24.0F, 0.0F);

            // render head/neck/wings/tail/legs/feet
            this.beak.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            poseStack.pushPose();
            poseStack.scale(0.8F, 0.8F, 1F);
            this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            poseStack.popPose();

            poseStack.popPose();
        } else {
            // Adult rendering - exact same as 1.16.5
            this.beak.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.rFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lFoot.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            if (male) {
                this.uBody.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            } else {
                poseStack.pushPose();
                poseStack.scale(0.8F, 0.8F, 1F);
                this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.chest.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                poseStack.popPose();
            }
        }
    }
}
