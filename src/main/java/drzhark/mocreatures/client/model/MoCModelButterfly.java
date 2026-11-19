package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityButterfly;
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

public class MoCModelButterfly<T extends MoCEntityButterfly> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "butterfly"), "main");

    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart right_antenna;
    private final ModelPart left_antenna;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart front_legs;
    private final ModelPart mid_legs;
    private final ModelPart rear_legs;
    private final ModelPart wing_left_front;
    private final ModelPart wing_left;
    private final ModelPart wing_left_back;
    private final ModelPart wing_right_front;
    private final ModelPart wing_right;
    private final ModelPart wing_right_back;

    private boolean getIsFlying;

    public MoCModelButterfly(ModelPart root) {
        super(root);
        this.head             = root.getChild("head");
        this.mouth            = root.getChild("mouth");
        this.right_antenna    = root.getChild("right_antenna");
        this.left_antenna     = root.getChild("left_antenna");
        this.thorax           = root.getChild("thorax");
        this.abdomen          = root.getChild("abdomen");
        this.front_legs       = root.getChild("front_legs");
        this.mid_legs         = root.getChild("mid_legs");
        this.rear_legs        = root.getChild("rear_legs");
        this.wing_left_front  = root.getChild("wing_left_front");
        this.wing_left        = root.getChild("wing_left");
        this.wing_left_back   = root.getChild("wing_left_back");
        this.wing_right_front = root.getChild("wing_right_front");
        this.wing_right       = root.getChild("wing_right");
        this.wing_right_back  = root.getChild("wing_right_back");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-0.5F, 0F, 0F, 1, 1, 1),
                PartPose.offsetAndRotation(0F, 21.9F, -1.3F, -2.171231F, 0F, 0F)
        );

        // Mouth
        root.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(0F, 0F, 0F, 1, 2, 0),
                PartPose.offsetAndRotation(-0.2F, 22.0F, -2.5F, 0.6548599F, 0F, 0F)
        );

        // Right Antenna
        root.addOrReplaceChild("right_antenna",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(-0.5F, 21.7F, -2.3F, -1.041001F, 0.7853982F, 0F)
        );

        // Left Antenna
        root.addOrReplaceChild("left_antenna",
                CubeListBuilder.create()
                        .texOffs(4, 7)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(0.5F, 21.7F, -2.3F, -1.041001F, -0.7853982F, 0F)
        );

        // Thorax
        root.addOrReplaceChild("thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, 1.5F, -1F, 1, 1, 2),
                PartPose.offsetAndRotation(0F, 20.0F, -1.0F, 0F, 0F, 0F)
        );

        // Abdomen
        root.addOrReplaceChild("abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 1)
                        .addBox(-0.5F, 0F, -1F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, 21.5F, 0F, 1.427659F, 0F, 0F)
        );

        // Front Legs
        root.addOrReplaceChild("front_legs",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(0F, 21.5F, -1.8F, 0.1487144F, 0F, 0F)
        );

        // Mid Legs
        root.addOrReplaceChild("mid_legs",
                CubeListBuilder.create()
                        .texOffs(4, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(0F, 22.0F, -1.2F, 0.5948578F, 0F, 0F)
        );

        // Rear Legs
        root.addOrReplaceChild("rear_legs",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(0F, 22.5F, -0.4F, 1.070744F, 0F, 0F)
        );

        // Wing Left Front
        root.addOrReplaceChild("wing_left_front",
                CubeListBuilder.create()
                        .texOffs(4, 20)
                        .addBox(0F, 0F, -4F, 8, 0, 6),
                PartPose.offsetAndRotation(0.3F, 21.4F, -1.0F, 0F, 0F, 0F)
        );

        // Wing Left (middle)
        root.addOrReplaceChild("wing_left",
                CubeListBuilder.create()
                        .texOffs(4, 26)
                        .addBox(0F, 0F, -1F, 8, 0, 6),
                PartPose.offsetAndRotation(0.3F, 21.5F, -0.5F, 0F, 0F, 0F)
        );

        // Wing Left Back
        root.addOrReplaceChild("wing_left_back",
                CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(0F, 0F, -1F, 5, 0, 8),
                PartPose.offsetAndRotation(0.3F, 21.2F, -1.0F, 0F, 0F, 0.5934119F)
        );

        // Wing Right Front
        root.addOrReplaceChild("wing_right_front",
                CubeListBuilder.create()
                        .texOffs(4, 8)
                        .addBox(-8F, 0F, -4F, 8, 0, 6),
                PartPose.offsetAndRotation(-0.3F, 21.4F, -1.0F, 0F, 0F, 0F)
        );

        // Wing Right (middle)
        root.addOrReplaceChild("wing_right",
                CubeListBuilder.create()
                        .texOffs(4, 14)
                        .addBox(-8F, 0F, -1F, 8, 0, 6),
                PartPose.offsetAndRotation(-0.3F, 21.5F, -0.5F, 0F, 0F, 0F)
        );

        // Wing Right Back
        root.addOrReplaceChild("wing_right_back",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-5F, 0F, -1F, 5, 0, 8),
                PartPose.offsetAndRotation(0.3F, 21.2F, -1.0F, 0F, 0F, -0.5934119F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void prepareMobModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
        this.getIsFlying = p_102614_.getIsFlying() || p_102614_.getDeltaMovement().y < -0.1D;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        float f2a = ageInTicks % 100F;
        float wingRot = 0F;
        float legMov, legMovB;

        // Determine wing/flapping speed and leg movement
        if (entityIn.getIsFlying() || entityIn.getDeltaMovement().y < -0.1D) {
            // fast flapping
            wingRot  = Mth.cos(ageInTicks * 0.9F) * 0.9F;
            legMov   = limbSwingAmount * 1.5F;
            legMovB  = legMov;
        } else {
            // walking legs
            legMov   = Mth.cos(limbSwing * 1.5F + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB  = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
            // occasional slow flutter
            if (f2a > 40F && f2a < 60F) {
                wingRot = Mth.cos(ageInTicks * 0.15F) * 0.9F;
            }
        }

        float baseAngle = 0.52359F; // ~30°

        // Wings: left and right
        this.wing_left.zRot       = -baseAngle + wingRot;
        this.wing_right.zRot      =  baseAngle - wingRot;
        this.wing_left_front.zRot = -baseAngle + wingRot;
        this.wing_left_back.zRot  = 0.5934119F - baseAngle + wingRot;
        this.wing_right_front.zRot = baseAngle - wingRot;
        this.wing_right_back.zRot  = -0.5934119F + baseAngle - wingRot;

        // Legs: front, mid, rear
        this.front_legs.xRot = 0.1487144F + legMov;
        this.mid_legs.xRot   = 0.5948578F + legMovB;
        this.rear_legs.xRot  = 1.070744F + legMov;

        // Head rotation from player-looking
        float headX = -headPitch * Mth.DEG_TO_RAD;
        float headY =  netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot   = headX;
        this.head.yRot   = headY;
        this.mouth.xRot  = 0.6548599F + 0F; // mouth box itself has its own X‐rotation baked in, so we leave mouth's pivot rotation only if you want to animate it later
        this.mouth.yRot  = 0F;
        // (If you want the mouth to follow head yaw/pitch, add: this.mouth.xRot = 0.6548599F + headX; this.mouth.yRot = headY; )

        // Antennae follow head yaw/pitch? If so, do likewise:
        this.right_antenna.xRot = -1.041001F + headX;
        this.right_antenna.yRot =  0.7853982F + headY;
        this.left_antenna.xRot  = -1.041001F + headX;
        this.left_antenna.yRot  = -0.7853982F + headY;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Render opaque parts first
        renderOpaqueParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        
        // Render transparent parts with blending if needed
        if (shouldRenderPartialTransparency()) {
            poseStack.pushPose();
            
            renderTransparentParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            
            poseStack.popPose();
        } else {
            // Render folded wings as opaque when not flying
            renderFoldedWings(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render body parts in roughly the same order as original:
        this.abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.front_legs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.right_antenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.left_antenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rear_legs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.mid_legs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.mouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render unfolded wings with transparency
        this.wing_right.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_right_front.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left_front.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_right_back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left_back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    private void renderFoldedWings(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // folded wings when idle
        this.wing_right.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_right_front.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left_front.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_right_back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.wing_left_back.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.8F; // 80% transparency for wings
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return this.getIsFlying; // Only when flying
    }
}
