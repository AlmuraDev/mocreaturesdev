/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.neutral.MoCEntityKitty;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelKitty<T extends MoCEntityKitty> extends MoCEntityModel<T> {

    private ModelPart body;
    public boolean isSitting;
    public boolean isSwinging;
    public float swingProgress;
    public int kittystate;
    public ModelPart[] headParts;
    public ModelPart tail;
    public ModelPart rightArm;
    public ModelPart leftArm;
    public ModelPart rightLeg;
    public ModelPart leftLeg;
    
    // Store constructor params to pass to factory
    private static float inflation = 0.0F;
    private static float yOffset = 15f;

    public static final ModelLayerLocation LAYER_LOCATION = 
        new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "kitty"), "main");

    /**
     * Constructor matching original, delegates to createLayerDefinition
     */
    public MoCModelKitty() {
        this(0.0F);
    }

    /**
     * Constructor matching original, delegates to createLayerDefinition
     */
    public MoCModelKitty(float limbSwing) {
        // In a real implementation, this would be done via EntityRenderers.register
        // For now, we directly create the parts
        ModelPart root = createBodyLayer().bakeRoot();
        setupParts(root);
    }

    /**
     * Constructor for use with EntityRendererProvider, loads from baked model
     */
    public MoCModelKitty(ModelPart root) {
        super(root);
        setupParts(root);
    }
    
    /**
     * Setup parts from root model part
     */
    private void setupParts(ModelPart root) {
        this.headParts = new ModelPart[10];
        
        this.body = root.getChild("body");
        this.headParts[0] = root.getChild("head_0");
        this.headParts[1] = root.getChild("head_1");
        this.headParts[2] = root.getChild("head_2");
        this.headParts[3] = root.getChild("head_3");
        this.headParts[4] = root.getChild("head_4");
        this.headParts[5] = root.getChild("head_5");
        this.headParts[6] = root.getChild("head_6");
        this.headParts[7] = root.getChild("head_7");
        this.headParts[8] = root.getChild("head_8");
        this.headParts[9] = root.getChild("head_9");
        this.tail = root.getChild("tail");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    /**
     * Static factory method for creating layer definition
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        // Head parts - using original setRotationPoint values converted to PartPose
        partdefinition.addOrReplaceChild("head_0", 
            CubeListBuilder.create()
                .texOffs(16, 0)
                .addBox(-2F, -5F, -3F, 1, 1, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_1", 
            CubeListBuilder.create()
                .texOffs(16, 0)
                .mirror(true)
                .addBox(1.0F, -5F, -3F, 1, 1, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_2", 
            CubeListBuilder.create()
                .texOffs(20, 0)
                .addBox(-2.5F, -4F, -3F, 2, 1, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_3", 
            CubeListBuilder.create()
                .texOffs(20, 0)
                .mirror(true)
                .addBox(0.5F, -4F, -3F, 2, 1, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_4", 
            CubeListBuilder.create()
                .texOffs(40, 0)
                .addBox(-4F, -1.5F, -5F, 3, 3, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_5", 
            CubeListBuilder.create()
                .texOffs(40, 0)
                .mirror(true)
                .addBox(1.0F, -1.5F, -5F, 3, 3, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_6", 
            CubeListBuilder.create()
                .texOffs(21, 6)
                .addBox(-1F, -1F, -5F, 2, 2, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_7", 
            CubeListBuilder.create()
                .texOffs(50, 0)
                .addBox(-2.5F, 0.5F, -1F, 5, 4, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_8", 
            CubeListBuilder.create()
                .texOffs(60, 0)
                .addBox(-1.5F, -2F, -4.1F, 3, 1, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        partdefinition.addOrReplaceChild("head_9", 
            CubeListBuilder.create()
                .texOffs(1, 1)
                .addBox(-2.5F, -3F, -4F, 5, 4, 4, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        // Body - using original setRotationPoint values
        partdefinition.addOrReplaceChild("body", 
            CubeListBuilder.create()
                .texOffs(20, 0)
                .addBox(-2.5F, -2F, -0F, 5, 5, 10, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, yOffset, -2F));
            
        // Arms - using original setRotationPoint values
        partdefinition.addOrReplaceChild("right_arm", 
            CubeListBuilder.create()
                .texOffs(0, 9)
                .addBox(-1F, 0.0F, -1F, 2, 6, 2, new CubeDeformation(inflation)),
            PartPose.offset(-1.5F, 3F + yOffset, -1F));
            
        partdefinition.addOrReplaceChild("left_arm", 
            CubeListBuilder.create()
                .texOffs(0, 9)
                .mirror(true)
                .addBox(-1F, 0.0F, -1F, 2, 6, 2, new CubeDeformation(inflation)),
            PartPose.offset(1.5F, 3F + yOffset, -1F));
            
        // Legs - using original setRotationPoint values
        partdefinition.addOrReplaceChild("right_leg", 
            CubeListBuilder.create()
                .texOffs(8, 9)
                .addBox(-1F, 0.0F, -1F, 2, 6, 2, new CubeDeformation(inflation)),
            PartPose.offset(-1.5F, 3F + yOffset, 7F));
            
        partdefinition.addOrReplaceChild("left_leg", 
            CubeListBuilder.create()
                .texOffs(8, 9)
                .mirror(true)
                .addBox(-1F, 0.0F, -1F, 2, 6, 2, new CubeDeformation(inflation)),
            PartPose.offset(1.5F, 3F + yOffset, 7F));
            
        // Tail - using original setRotationPoint values
        partdefinition.addOrReplaceChild("tail", 
            CubeListBuilder.create()
                .texOffs(16, 9)
                .mirror(true)
                .addBox(-0.5F, -8F, -1F, 1, 8, 1, new CubeDeformation(inflation)),
            PartPose.offset(0.0F, -0.5F + yOffset, 7.5F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.isSitting = entityIn.getIsSitting();
        this.isSwinging = entityIn.getIsSwinging();
        this.swingProgress = entityIn.attackAnim;
        this.kittystate = entityIn.getKittyState();
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Head rotation - using exact degree to radian conversion from original
        this.headParts[9].yRot = netHeadYaw / 57.29578F;
        this.headParts[9].xRot = headPitch / 57.29578F;
        
        for (int i = 0; i < 9; i++) {
            this.headParts[i].yRot = this.headParts[9].yRot;
            this.headParts[i].xRot = this.headParts[9].xRot;
        }
        
        // Arms swing with walking animation - using original values
        this.rightArm.xRot = Mth.cos((limbSwing * 0.6662F) + 3.141593F) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        
        // Legs walk animation - using original values
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos((limbSwing * 0.6662F) + 3.141593F) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        
        // Swinging animation - using original values
        if (this.isSwinging) {
            this.rightArm.xRot = -2F + this.swingProgress;
            this.rightArm.yRot = 2.25F - (this.swingProgress * 2.0F);
        } else {
            this.rightArm.yRot = 0.0F;
        }
        
        this.leftArm.yRot = 0.0F;
        
        // Tail default position and swing based on leg movement - using original values
        this.tail.xRot = -0.5F;
        this.tail.zRot = this.leftLeg.xRot * 0.625F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if (this.isSitting) {
            poseStack.translate(0.0F, 0.25F, 0.0F);
            this.tail.zRot = 0.0F;
            this.tail.xRot = -2.3F;
        }
        for (int i = 0; i < 7; i++) {
            this.headParts[i].render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        if (this.kittystate > 2) {
            this.headParts[7].render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        if (this.kittystate == 12) {
            this.headParts[8].render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        this.headParts[9].render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        if (this.isSitting) {
            poseStack.translate(0.0F, 0.0625F, 0.0625F);
            float f6 = -1.570796F;
            this.rightArm.xRot = f6;
            this.leftArm.xRot = f6;
            this.rightLeg.xRot = f6;
            this.leftLeg.xRot = f6;
            this.rightLeg.yRot = 0.1F;
            this.leftLeg.yRot = -0.1F;
        }
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        poseStack.popPose();
    }
}
