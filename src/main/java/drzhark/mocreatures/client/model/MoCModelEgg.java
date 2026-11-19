package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelEgg<T extends MoCEntityEgg> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "egg"), "main"
    );

    private final ModelPart egg1;
    private final ModelPart egg2;
    private final ModelPart egg3;
    private final ModelPart egg4;
    private final ModelPart egg5;

    public MoCModelEgg(ModelPart root) {
        super(root);
        this.egg1 = root.getChild("egg1");
        this.egg2 = root.getChild("egg2");
        this.egg3 = root.getChild("egg3");
        this.egg4 = root.getChild("egg4");
        this.egg5 = root.getChild("egg5");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        /*
         * Recreating the five boxes from the 1.16 version. All pivots and box sizes
         * exactly match the old ModelRenderer.addBox / setRotationPoint calls.
         *
         * Egg1:  addBox(0,0,0, 3,3,3),  pivot at (0,20,0)
         * Egg2:  addBox(0,0,0, 2,1,2),  pivot at (0.5,19.5,0.5)
         * Egg3:  addBox(0,0,0, 2,1,2),  pivot at (0.5,22.5,0.5)
         * Egg4:  addBox(0,0,0, 1,2,2),  pivot at (-0.5,20.5,0.5)
         * Egg5:  addBox(0,0,0, 1,2,2),  pivot at (2.5,20.5,0.5)
         *
         * (Note: No rotations were applied in the original code, so PartPose.offset(...) only.)
         */
        root.addOrReplaceChild("egg1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 3, 3, 3),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );

        root.addOrReplaceChild("egg2",
                CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2, 1, 2),
                PartPose.offset(0.5F, 19.5F, 0.5F)
        );

        root.addOrReplaceChild("egg3",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2, 1, 2),
                PartPose.offset(0.5F, 22.5F, 0.5F)
        );

        root.addOrReplaceChild("egg4",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 2, 2),
                PartPose.offset(-0.5F, 20.5F, 0.5F)
        );

        root.addOrReplaceChild("egg5",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1, 2, 2),
                PartPose.offset(2.5F, 20.5F, 0.5F)
        );

        // You can pick any reasonable texture size. The original had textureWidth=64, textureHeight=32 commented out.
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // The egg does not animate, so leave empty.
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        egg1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        egg2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        egg3.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        egg4.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        egg5.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
