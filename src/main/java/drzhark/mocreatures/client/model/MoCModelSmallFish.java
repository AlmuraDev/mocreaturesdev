/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import drzhark.mocreatures.entity.aquatic.MoCEntitySmallFish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.16.5 Forge → 1.20.1 Forge.
 * All ModelRenderer → ModelPart; setRotationAngles(...) → setupAnim(...); render(...) → renderToBuffer(...).
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelSmallFish<T extends MoCEntitySmallFish> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "small_fish"), "main"
    );

    private final ModelPart bodyFlat;
    private final ModelPart bodyRomboid;
    private final ModelPart midBodyFin;
    private final ModelPart upperFinA;
    private final ModelPart upperFinB;
    private final ModelPart upperFinC;
    private final ModelPart lowerFinA;
    private final ModelPart lowerFinB;
    private final ModelPart lowerFinC;
    private final ModelPart tail;

    // We store the entity here in setLivingAnimations so that render() can use its offsets.
    private MoCEntitySmallFish smallFish;

    public MoCModelSmallFish(ModelPart root) {
        super(root);
        this.bodyFlat    = root.getChild("bodyFlat");
        this.bodyRomboid = root.getChild("bodyRomboid");
        this.midBodyFin  = root.getChild("midBodyFin");
        this.upperFinA   = root.getChild("upperFinA");
        this.upperFinB   = root.getChild("upperFinB");
        this.upperFinC   = root.getChild("upperFinC");
        this.lowerFinA   = root.getChild("lowerFinA");
        this.lowerFinB   = root.getChild("lowerFinB");
        this.lowerFinC   = root.getChild("lowerFinC");
        this.tail        = root.getChild("tail");
    }

    /**
     * Build the mesh exactly as in the old ModelRenderer version, but using ModelPart builders.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // BodyFlat
        PartDefinition bodyFlat = root.addOrReplaceChild("bodyFlat",
                CubeListBuilder.create()
                        .texOffs(0, 2)
                        .addBox(0F, -1.5F, -1F, 5, 3, 2),
                PartPose.offset(-3F, 15F, 0F)
        );

        // BodyRomboid
        PartDefinition bodyRomboid = root.addOrReplaceChild("bodyRomboid",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(0F, 0F, -0.5F, 4, 4, 1),
                PartPose.offsetAndRotation(-4F, 15F, 0F, 0F, 0F, -0.7853982F)
        );

        // MidBodyFin
        PartDefinition midBodyFin = root.addOrReplaceChild("midBodyFin",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0F, -0.5F, 0F, 4, 2, 4),
                PartPose.offsetAndRotation(-3F, 15F, 0F, 0F, 0.7853982F, 0F)
        );

        // UpperFinA
        PartDefinition upperFinA = root.addOrReplaceChild("upperFinA",
                CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(-0.5F, -1.3F, -0.5F, 2, 1, 1),
                PartPose.offset( -0.65F, 13.5F, 0F)
        );

        // UpperFinB
        PartDefinition upperFinB = root.addOrReplaceChild("upperFinB",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.5F, -1F, -0.5F, 4, 1, 1),
                PartPose.offset(0F, 13.5F, 0F)
        );

        // UpperFinC
        PartDefinition upperFinC = root.addOrReplaceChild("upperFinC",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-5F, -2F, 0F, 8, 3, 0),
                PartPose.offset(0F, 13.5F, 0F)
        );

        // LowerFinA
        PartDefinition lowerFinA = root.addOrReplaceChild("lowerFinA",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-0.5F, -0.3F, -0.5F, 2, 1, 1),
                PartPose.offset(-0.65F, 17.2F, 0F)
        );

        // LowerFinB
        PartDefinition lowerFinB = root.addOrReplaceChild("lowerFinB",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(0F, 0F, -3F, 5, 0, 6),
                PartPose.offset(-3F, 16F, 0F)
        );

        // LowerFinC
        PartDefinition lowerFinC = root.addOrReplaceChild("lowerFinC",
                CubeListBuilder.create()
                        .texOffs(16, 18)
                        .addBox(-5F, 0F, 0F, 8, 3, 0),
                PartPose.offset(0F, 15.5F, 0F)
        );

        // Tail
        PartDefinition tail = root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(10, 7)
                        .addBox(0F, 0F, -0.5F, 3, 3, 1),
                PartPose.offsetAndRotation(1.3F, 15F, 0F, 0F, 0F, -0.7853982F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    /**
     * Called before rendering: store a reference to the entity so we can pull offsets in renderToBuffer().
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.smallFish = entity;

        // Tail swinging:
        float tailMov = Mth.cos(limbSwing * 0.8F) * limbSwingAmount * 0.6F;
        this.tail.yRot = tailMov;

        // Mid‐body fin wiggle:
        float finMov = Mth.cos(ageInTicks * 0.4F) * 0.2F;
        this.midBodyFin.yRot = 0.7853982F + finMov;

        // Lower fin wave:
        this.lowerFinB.zRot = finMov;
    }

    /**
     * Render everything, first translating by the fish's custom offsets, then rotating to face "sideways."
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.smallFish == null) {
            // Fallback just in case; shouldn't normally happen.
            this.smallFish = null;
        }
        // Get the fish's own offsets:
        float yOffset = (this.smallFish != null) ? this.smallFish.getAdjustedYOffset() : 0F;
        float xOffset = (this.smallFish != null) ? this.smallFish.getAdjustedXOffset() : 0F;
        float zOffset = (this.smallFish != null) ? this.smallFish.getAdjustedZOffset() : 0F;

        poseStack.pushPose();
        // Translate by the small fish's offsets:
        poseStack.translate(xOffset, yOffset, zOffset);
        // Rotate 90° around Y so that the fish's “front” faces sideways in world space:
        poseStack.rotate(Axis.YP.rotationDegrees(90.0F));

        this.bodyFlat.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.bodyRomboid.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midBodyFin.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.upperFinA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.upperFinB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.upperFinC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lowerFinA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lowerFinB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lowerFinC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }
}
