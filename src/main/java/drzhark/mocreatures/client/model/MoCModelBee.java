package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityBee;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MoCModelBee<T extends MoCEntityBee> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "bee"), "main"
    );

    private final ModelPart root;

    // Children (porting each old ModelRenderer)
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart rAntenna;
    private final ModelPart lAntenna;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart tail;
    private final ModelPart frontLegs;
    private final ModelPart midLegs;
    private final ModelPart rearLegs;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart foldedWings;

    private boolean flying;

    public MoCModelBee(ModelPart root) {
        super(root);
        this.root = root;

        this.head         = root.getChild("head");
        this.mouth        = root.getChild("mouth");
        this.rAntenna     = root.getChild("r_antenna");
        this.lAntenna     = root.getChild("l_antenna");
        this.thorax       = root.getChild("thorax");
        this.abdomen      = root.getChild("abdomen");
        this.tail         = root.getChild("tail");
        this.frontLegs    = root.getChild("front_legs");
        this.midLegs      = root.getChild("mid_legs");
        this.rearLegs     = root.getChild("rear_legs");
        this.leftWing     = root.getChild("left_wing");
        this.rightWing    = root.getChild("right_wing");
        this.foldedWings  = root.getChild("folded_wings");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1F, 0F, -1F, 2, 1, 2),
                PartPose.offsetAndRotation(0F, 21.5F, -2F, -2.171231F, 0F, 0F)
        );

        // Antennae
        root.addOrReplaceChild(
                "r_antenna",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(-0.5F, 20.2F, -2.3F, -1.041001F, 0.7853982F, 0F)
        );
        root.addOrReplaceChild(
                "l_antenna",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(0.5F, 20.2F, -2.3F, -1.041001F, -0.7853982F, 0F)
        );

        // Mouth
        root.addOrReplaceChild(
                "mouth",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(0F, 0F, -1F, 1, 1, 1),
                PartPose.offsetAndRotation(0F, 21.5F, -2F, -0.4461433F, 0.3569147F, 0.7853982F)
        );

        // Thorax
        root.addOrReplaceChild(
                "thorax",
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(-1F, 0F, -1F, 2, 2, 2),
                PartPose.offset(0F, 20.5F, -1F)
        );

        // Abdomen
        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 3, 2),
                PartPose.offsetAndRotation(0F, 21.5F, 0F, 1.249201F, 0F, 0F)
        );

        // Tail
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(-0.5F, 0F, 0F, 1, 1, 1),
                PartPose.offsetAndRotation(0F, 22F, 2F, 0.2379431F, 0F, 0F)
        );

        // Front Legs
        root.addOrReplaceChild(
                "front_legs",
                CubeListBuilder.create()
                        .texOffs(4, 14)
                        .addBox(-1F, 0F, 0F, 2, 2, 0),
                PartPose.offsetAndRotation(0F, 22F, -1.8F, 0.1487144F, 0F, 0F)
        );

        // Mid Legs
        root.addOrReplaceChild(
                "mid_legs",
                CubeListBuilder.create()
                        .texOffs(4, 12)
                        .addBox(-1F, 0F, 0F, 2, 2, 0),
                PartPose.offsetAndRotation(0F, 22.5F, -1.2F, 0.5948578F, 0F, 0F)
        );

        // Rear Legs
        root.addOrReplaceChild(
                "rear_legs",
                CubeListBuilder.create()
                        .texOffs(8, 1)
                        .addBox(-1.5F, 0F, 0F, 3, 3, 0),
                PartPose.offsetAndRotation(0F, 22.5F, -0.4F, 0.8922867F, 0F, 0F)
        );

        // Wings (unfolded)
        root.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-1F, 0F, 0.5F, 2, 0, 4),
                PartPose.offsetAndRotation(0F, 20.4F, -1F, 0F, 1.047198F, 0F)
        );
        root.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-1F, 0F, 0.5F, 2, 0, 4),
                PartPose.offsetAndRotation(0F, 20.4F, -1F, 0F, -1.047198F, 0F)
        );

        // Folded Wings (rendered when not flying)
        root.addOrReplaceChild(
                "folded_wings",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-1F, 0F, 0F, 2, 0, 4),
                PartPose.offsetAndRotation(0F, 20.5F, -1F, 0.0001745F, 0F, 0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    /**
     * Called every frame to update whether the bee is in “flying” state.
     */

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        // Bee is “flying” if getIsFlying() or moving downward quickly
        this.flying = entityIn.getIsFlying() || entityIn.getDeltaMovement().y < -0.1D;
    }

    /**
     * Port of setRotationAngles(...) → setupAnim(...) in 1.19+
     */
    @Override
    public void setupAnim(
            T entityIn,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Wing flapping animation
        float wingRot = Mth.cos(ageInTicks * 3.0F) * 0.7F;
        this.rightWing.zRot = wingRot;
        this.leftWing.zRot  = -wingRot;

        // Leg movement: if flying, keep legs tucked; otherwise, animate walking
        float legMov;
        float legMovB;

        if (this.flying) {
            legMov = limbSwingAmount * 1.5F;
            legMovB = legMov;
        } else {
            legMov   = Mth.cos((limbSwing * 1.5F) + (float) Math.PI) * 2.0F * limbSwingAmount;
            legMovB  = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        this.frontLegs.xRot = 0.1487144F + legMov;
        this.midLegs.xRot   = 0.5948578F + legMovB;
        this.rearLegs.xRot  = 1.070744F + legMov;
    }

    /**
     * Render all parts using the new transparency system.
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
        // Render opaque parts first
        renderOpaqueParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        
        // Render transparent parts with blending if needed
        if (shouldRenderPartialTransparency()) {
            poseStack.pushPose();
            
            renderTransparentParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            
            poseStack.popPose();
        } else {
            // Render folded wings as opaque when not flying
            this.foldedWings.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Draw static geometry:
        this.abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.frontLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rAntenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lAntenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rearLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.mouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render unfolded wings with transparency
        this.leftWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.6F; // 60% transparency for wings
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return this.flying; // Only when flying
    }
}
