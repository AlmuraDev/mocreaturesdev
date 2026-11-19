/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.aquatic.MoCEntityFishy;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelFishy<T extends MoCEntityFishy> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "fishy"), "main"
    );

    public final ModelPart Body;
    public final ModelPart Tail;
    // Declare the fins but don't initialize them, just like in 1.16
    public final ModelPart UpperFin;
    public final ModelPart LowerFin;
    public final ModelPart RightFin;
    public final ModelPart LeftFin;
    private MoCEntityFishy smallFish;

    public MoCModelFishy(ModelPart root) {
        super(root);
        this.Body = root.getChild("body");
        this.Tail = root.getChild("tail");
        // These fields are declared but not used, to match 1.16 behavior
        this.UpperFin = null;
        this.LowerFin = null;
        this.RightFin = null;
        this.LeftFin = null;
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
        
        this.smallFish = entity;

        // Tail wagging logic (was in setRotationAngles)
        this.Tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
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
        // Before rendering, apply the "fish offset" from the entity
        float yOffset = this.smallFish.getAdjustedYOffset();
        float xOffset = this.smallFish.getAdjustedXOffset();
        float zOffset = this.smallFish.getAdjustedZOffset();

        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, zOffset);

        // Only render body and tail, to match the 1.16 behavior
        this.Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        poseStack.popPose();
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ─── BODY ──────────────────────────────────────────────────────────────────
        // In 1.16.5: new ModelRenderer(this, 0, 0).addBox(0,0,-3.5,1,5,5); setRotationPoint(0,18,-1); rotateAngleX = π/4
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -3.5F, 1, 5, 5, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 18.0F, -1.0F, 0.7853981F, 0.0F, 0.0F)
        );

        // ─── TAIL ──────────────────────────────────────────────────────────────────
        // In 1.16.5: new ModelRenderer(this, 12, 0).addBox(0,0,0,1,3,3); setRotationPoint(0,20.5,3); rotateAngleX = π/4
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 20.5F, 3.0F, 0.7853981F, 0.0F, 0.0F)
        );

        // Texture size: implicitly 64×32 (matches default if none was set explicitly).
        return LayerDefinition.create(mesh, 64, 32);
    }
}
