/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;

import drzhark.mocreatures.entity.ambient.MoCEntityMaggot;

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
public class MoCModelMaggot<T extends MoCEntityMaggot> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "maggot"), "main"
    );

    // We store limbSwing & limbSwingAmount (set in prepareMobModel) for the Z‐scale in render.
    private float limbSwing;
    private float limbSwingAmount;

    // In 1.20.1, each child part is a ModelPart. These replace the old ModelRenderer fields.
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart Tail;
    private final ModelPart Tailtip;

    public MoCModelMaggot(ModelPart root) {
        super(root);
        this.Head    = root.getChild("Head");
        this.Body    = root.getChild("Body");
        this.Tail    = root.getChild("Tail");
        this.Tailtip = root.getChild("Tailtip");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        //---------------------------------------------
        // Head (was: new ModelRenderer(this, 0,11); addBox(-1,-1,-2, 2,2,2); pivot(0,23,-2))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1.0F, -1.0F, -2.0F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, -2.0F)
        );

        //---------------------------------------------
        // Body (was: new ModelRenderer(this, 0,0); addBox(-1.5,-2,0, 3,3,4); pivot(0,23,-2))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -2.0F, 0.0F, 3, 3, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, -2.0F)
        );

        //---------------------------------------------
        // Tail (was: new ModelRenderer(this, 0,7); addBox(-1,-1,0, 2,2,2); pivot(0,23,2))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1.0F, -1.0F, 0.0F, 2, 2, 2, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, 2.0F)
        );

        //---------------------------------------------
        // Tailtip (was: new ModelRenderer(this, 8,7); addBox(-0.5,0,0, 1,1,1); pivot(0,23,4))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Tailtip",
                CubeListBuilder.create()
                        .texOffs(8, 7)
                        .addBox(-0.5F, 0.0F, 0.0F, 1, 1, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, 4.0F)
        );

        // The original texture sheet was 32×32
        return LayerDefinition.create(mesh, 32, 32);
    }

    /**
     * Capture limbSwing & limbSwingAmount before any animation logic.
     * In 1.16.5 this was setLivingAnimations(...); in 1.20.1 we override prepareMobModel.
     */
    @Override
    public void prepareMobModel(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick
    ) {
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
    }

    /**
     * No extra rotation/pose updates—empty as in the original setRotationAngles(...)
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
        // no head/body/tail rotations beyond the Z‐scale handled in renderToBuffer
    }

    /**
     * Render all parts, applying the same z‐scale “wiggle” based on limbSwing & limbSwingAmount,
     * with blending enabled exactly as in the old render(...) method.
     */
    @Override
    public void renderToBuffer(
            PoseStack        poseStack,
            VertexConsumer   buffer,
            int              packedLight,
            int              packedOverlay,
            float            red,
            float            green,
            float            blue,
            float            alpha
    ) {
        poseStack.pushPose();

        // Mirror the old RenderSystem.enableBlend() / defaultBlendFunc() calls:
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // f9 = -(cos(limbSwing * 3)) * limbSwingAmount * 2
        float f9 = -Mth.cos(this.limbSwing * 3.0F) * this.limbSwingAmount * 2.0F;
        // scale Z by (1 + f9)
        poseStack.scale(1.0F, 1.0F, 1.0F + f9);

        // Render each part with the same light/overlay/colour
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Tailtip.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Disable blending and pop
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
