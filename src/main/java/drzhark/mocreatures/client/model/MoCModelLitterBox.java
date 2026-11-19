/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.item.MoCEntityLitterBox;

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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.16.5 → 1.20.1. This model has seven child parts:
 * Table1, Table2, Table3, Table4, Bottom, Litter, and LitterUsed.
 * A public boolean `usedlitter` toggles which “litter” quad is rendered.
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelLitterBox<T extends MoCEntityLitterBox> extends MoCEntityModel<T> {
        
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "litter_box"), "main"
    );

    /** When true, render the “used” litter; otherwise render the clean one. */
    public boolean usedlitter;

    // Each of these corresponds to one ModelRenderer in 1.16.5. In 1.20.1 they are ModelPart children.
    private final ModelPart Table1;
    private final ModelPart Table2;
    private final ModelPart Table3;
    private final ModelPart Table4;
    private final ModelPart Bottom;
    private final ModelPart Litter;
    private final ModelPart LitterUsed;

    public MoCModelLitterBox(ModelPart root) {
        super(root);
        this.Table1     = root.getChild("Table1");
        this.Table3     = root.getChild("Table3");
        this.Table2     = root.getChild("Table2");
        this.Table4     = root.getChild("Table4");
        this.Bottom     = root.getChild("Bottom");
        this.Litter     = root.getChild("Litter");
        this.LitterUsed = root.getChild("LitterUsed");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        //---------------------------------------------
        // Table1 (was: addBox(-8, 0, 7, 16, 6, 1), pivot at (0,18,0))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Table1",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-8.0F, 0.0F, 7.0F, 16, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 18.0F, 0.0F)
        );

        //---------------------------------------------
        // Table3 (was: addBox(-8, 18, -8, 16, 6, 1), pivot at (0,0,0))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Table3",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-8.0F, 18.0F, -8.0F, 16, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        //---------------------------------------------
        // Table2 (was: addBox(-8, -3, 0, 16, 6, 1), pivot (8,21,0), rotate Y = +π/2)
        //---------------------------------------------
        part.addOrReplaceChild(
                "Table2",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-8.0F, -3.0F, 0.0F, 16, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        8.0F,   // pivotX
                        21.0F,  // pivotY
                        0.0F,   // pivotZ
                        0.0F,   // xRot
                        (float)Math.PI / 2F, // yRot = 1.5708F
                        0.0F    // zRot
                )
        );

        //---------------------------------------------
        // Table4 (was: addBox(-8, -3, 0, 16, 6, 1), pivot (-9,21,0), rotate Y = +π/2)
        //---------------------------------------------
        part.addOrReplaceChild(
                "Table4",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-8.0F, -3.0F, 0.0F, 16, 6, 1, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(
                        -9.0F,  // pivotX
                        21.0F,  // pivotY
                        0.0F,   // pivotZ
                        0.0F,   // xRot
                        (float)Math.PI / 2F, // yRot
                        0.0F    // zRot
                )
        );

        //---------------------------------------------
        // Litter   (was: addBox(0, 0, 0, 16, 2, 14), pivot (-8,21,-7))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Litter",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(0.0F, 0.0F, 0.0F, 16, 2, 14, new CubeDeformation(0.0F)),
                PartPose.offset(-8.0F, 21.0F, -7.0F)
        );

        //---------------------------------------------
        // LitterUsed (was: addBox(0, 0, 0, 16, 2, 14), pivot (-8,21,-7), texOffs = (16,15))
        //---------------------------------------------
        part.addOrReplaceChild(
                "LitterUsed",
                CubeListBuilder.create()
                        .texOffs(16, 15)
                        .addBox(0.0F, 0.0F, 0.0F, 16, 2, 14, new CubeDeformation(0.0F)),
                PartPose.offset(-8.0F, 21.0F, -7.0F)
        );

        //---------------------------------------------
        // Bottom (was: addBox(-10, 0, -7, 16, 1, 14), pivot (2,23,0), texOffs = (16,15))
        //---------------------------------------------
        part.addOrReplaceChild(
                "Bottom",
                CubeListBuilder.create()
                        .texOffs(16, 15)
                        .addBox(-10.0F, 0.0F, -7.0F, 16, 1, 14, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 23.0F, 0.0F)
        );

        // The original texture was sized 64×32 (it used offsets up to ~46×17), so we keep that here.
        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * No dynamic animation; keep empty. (In 1.16.5 this was setRotationAngles(...), now called setupAnim(...).)
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
        // no animations for a static litter box
    }

    /**
     * Render all parts. If `usedlitter == true`, draw LitterUsed; else draw Litter.
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
        this.Table1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Table3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Table2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Table4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Bottom.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        if (this.usedlitter) {
            this.LitterUsed.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.Litter.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
}
