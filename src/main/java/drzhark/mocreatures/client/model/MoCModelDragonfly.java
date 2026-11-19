package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityDragonfly;
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
public class MoCModelDragonfly<T extends MoCEntityDragonfly> extends MoCEntityModel<T> implements IPartialTransparencyModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "dragonfly"), "main"
    );

    private final ModelPart Head;
    private final ModelPart RAntenna;
    private final ModelPart LAntenna;
    private final ModelPart Mouth;
    private final ModelPart Thorax;
    private final ModelPart Abdomen;
    private final ModelPart FrontLegs;
    private final ModelPart MidLegs;
    private final ModelPart RearLegs;
    private final ModelPart WingFrontRight;
    private final ModelPart WingFrontLeft;
    private final ModelPart WingRearRight;
    private final ModelPart WingRearLeft;

    public MoCModelDragonfly(ModelPart root) {
        super(root);
        this.Head            = root.getChild("Head");
        this.RAntenna        = root.getChild("RAntenna");
        this.LAntenna        = root.getChild("LAntenna");
        this.Mouth           = root.getChild("Mouth");
        this.Thorax          = root.getChild("Thorax");
        this.Abdomen         = root.getChild("Abdomen");
        this.FrontLegs       = root.getChild("FrontLegs");
        this.MidLegs         = root.getChild("MidLegs");
        this.RearLegs        = root.getChild("RearLegs");
        this.WingFrontRight  = root.getChild("WingFrontRight");
        this.WingFrontLeft   = root.getChild("WingFrontLeft");
        this.WingRearRight   = root.getChild("WingRearRight");
        this.WingRearLeft    = root.getChild("WingRearLeft");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head: texOffs(0,4), addBox(-1,0,-1, 2,1,2), pivot(0,21,-2), rotateX = -2.171231
        root.addOrReplaceChild("Head",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2),
                PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, -2.171231F, 0.0F, 0.0F)
        );

        // RAntenna: texOffs(0,7), addBox(-0.5,0,-1, 1,0,1), pivot(-0.5,19.7,-2.3), rotateX=-1.041001, rotateY=0.7853982
        root.addOrReplaceChild("RAntenna",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-0.5F, 0.0F, -1.0F, 1, 0, 1),
                PartPose.offsetAndRotation(-0.5F, 19.7F, -2.3F, -1.041001F, 0.7853982F, 0.0F)
        );

        // LAntenna: texOffs(4,7), addBox(-0.5,0,-1, 1,0,1), pivot(0.5,19.7,-2.3), rotateX=-1.041001, rotateY=-0.7853982
        root.addOrReplaceChild("LAntenna",
                CubeListBuilder.create()
                        .texOffs(4, 7)
                        .addBox(-0.5F, 0.0F, -1.0F, 1, 0, 1),
                PartPose.offsetAndRotation(0.5F, 19.7F, -2.3F, -1.041001F, -0.7853982F, 0.0F)
        );

        // Mouth: texOffs(0,11), addBox(-0.5,0,0, 1,1,1), pivot(0,21.1,-2.3), rotateX=-2.171231
        root.addOrReplaceChild("Mouth",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-0.5F, 0.0F, 0.0F, 1, 1, 1),
                PartPose.offsetAndRotation(0.0F, 21.1F, -2.3F, -2.171231F, 0.0F, 0.0F)
        );

        // Thorax: texOffs(0,0), addBox(-1,0,-1, 2,2,2), pivot(0,20,-1)
        root.addOrReplaceChild("Thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2),
                PartPose.offset(0.0F, 20.0F, -1.0F)
        );

        // Abdomen: texOffs(8,0), addBox(-0.5,0,-1, 1,7,1), pivot(0,20.5,0), rotateX=1.427659
        root.addOrReplaceChild("Abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-0.5F, 0.0F, -1.0F, 1, 7, 1),
                PartPose.offsetAndRotation(0.0F, 20.5F, 0.0F, 1.427659F, 0.0F, 0.0F)
        );

        // FrontLegs: texOffs(0,8), addBox(-1,0,0, 2,3,0), pivot(0,21.5,-1.8), rotateX=0.1487144
        root.addOrReplaceChild("FrontLegs",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1.0F, 0.0F, 0.0F, 2, 3, 0),
                PartPose.offsetAndRotation(0.0F, 21.5F, -1.8F, 0.1487144F, 0.0F, 0.0F)
        );

        // MidLegs: texOffs(4,8), addBox(-1,0,0, 2,3,0), pivot(0,22,-1.2), rotateX=0.5948578
        root.addOrReplaceChild("MidLegs",
                CubeListBuilder.create()
                        .texOffs(4, 8)
                        .addBox(-1.0F, 0.0F, 0.0F, 2, 3, 0),
                PartPose.offsetAndRotation(0.0F, 22.0F, -1.2F, 0.5948578F, 0.0F, 0.0F)
        );

        // RearLegs: texOffs(8,8), addBox(-1,0,0, 2,3,0), pivot(0,22,-0.4), rotateX=1.070744
        root.addOrReplaceChild("RearLegs",
                CubeListBuilder.create()
                        .texOffs(8, 8)
                        .addBox(-1.0F, 0.0F, 0.0F, 2, 3, 0),
                PartPose.offsetAndRotation(0.0F, 22.0F, -0.4F, 1.070744F, 0.0F, 0.0F)
        );

        // WingFrontRight: texOffs(0,28), addBox(-7,0,-1, 7,0,2), pivot(-1,20,-1), rotateY=-0.1396263, rotateZ=0.0872665
        root.addOrReplaceChild("WingFrontRight",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-7.0F, 0.0F, -1.0F, 7, 0, 2),
                PartPose.offsetAndRotation(-1.0F, 20.0F, -1.0F, 0.0F, -0.1396263F, 0.0872665F)
        );

        // WingFrontLeft: texOffs(0,30), addBox(0,0,-1, 7,0,2), pivot(1,20,-1), rotateY=0.1396263, rotateZ=-0.0872665
        root.addOrReplaceChild("WingFrontLeft",
                CubeListBuilder.create()
                        .texOffs(0, 30)
                        .addBox(0.0F, 0.0F, -1.0F, 7, 0, 2),
                PartPose.offsetAndRotation(1.0F, 20.0F, -1.0F, 0.0F, 0.1396263F, -0.0872665F)
        );

        // WingRearRight: texOffs(0,24), addBox(-7,0,-1, 7,0,2), pivot(-1,20,-1), rotateY=0.3490659, rotateZ=-0.0872665
        root.addOrReplaceChild("WingRearRight",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-7.0F, 0.0F, -1.0F, 7, 0, 2),
                PartPose.offsetAndRotation(-1.0F, 20.0F, -1.0F, 0.0F, 0.3490659F, -0.0872665F)
        );

        // WingRearLeft: texOffs(0,26), addBox(0,0,-1, 7,0,2), pivot(1,20,-1), rotateY=-0.3490659, rotateZ=0.0872665
        root.addOrReplaceChild("WingRearLeft",
                CubeListBuilder.create()
                        .texOffs(0, 26)
                        .addBox(0.0F, 0.0F, -1.0F, 7, 0, 2),
                PartPose.offsetAndRotation(1.0F, 20.0F, -1.0F, 0.0F, -0.3490659F, 0.0872665F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }
    
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        float WingRot = 0F;
        float legMov;
        float legMovB;

        if (entity.getIsFlying() || entity.getDeltaMovement().y < -0.1D) {
            WingRot = Mth.cos(ageInTicks * 2.0F) * 0.5F;
            legMov = limbSwingAmount * 1.5F;
            legMovB = legMov;
        } else {
            legMov = Mth.cos((limbSwing * 1.5F) + (float)Math.PI) * 2.0F * limbSwingAmount;
            legMovB = Mth.cos(limbSwing * 1.5F) * 2.0F * limbSwingAmount;
        }

        // Apply Z-rotation to front/rear pair
        this.WingFrontRight.zRot =  WingRot;
        this.WingRearLeft.zRot   =  WingRot;
        this.WingFrontLeft.zRot   = -WingRot;
        this.WingRearRight.zRot   = -WingRot;

        // Legs from original setRotationAngles:
        this.FrontLegs.xRot = 0.1487144F + legMov;
        this.MidLegs.xRot   = 0.5948578F + legMovB;
        this.RearLegs.xRot  = 1.070744F + legMov;
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Render opaque parts first
        renderOpaqueParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        
        // Render transparent parts with blending
        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.clearColor(getTransparencyColor()[0], getTransparencyColor()[1], getTransparencyColor()[2], getTransparencyValue());
        
        renderTransparentParts(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
    
    @Override
    public void renderOpaqueParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render all opaque parts:
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Abdomen.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.FrontLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RAntenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.LAntenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.RearLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.MidLegs.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Mouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Thorax.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public void renderTransparentParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Render wings with transparency
        this.WingRearRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.WingFrontRight.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.WingFrontLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.WingRearLeft.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
    
    @Override
    public float getTransparencyValue() {
        return 0.6F; // 60% transparency for wings
    }
    
    @Override
    public boolean shouldRenderPartialTransparency() {
        return true; // Dragonflies always have transparent wings
    }
}
