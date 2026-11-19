/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.item.MoCEntityKittyBed;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of MoCModelKittyBed (1.16.5) to Minecraft 1.20.1.
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelKittyBed<T extends MoCEntityKittyBed> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "kitty_bed"), "main"
    );

    public boolean hasMilk;
    public boolean hasFood;
    public boolean pickedUp;
    public float milklevel;

    private final ModelPart TableL;
    private final ModelPart TableR;
    private final ModelPart Table_B;
    private final ModelPart FoodT;
    private final ModelPart FoodTraySide;
    private final ModelPart FoodTraySideB;
    private final ModelPart FoodTraySideC;
    private final ModelPart FoodTraySideD;
    private final ModelPart Milk;
    private final ModelPart PetFood;
    private final ModelPart Bottom;

    public MoCModelKittyBed(ModelPart root) {
        super(root);
        this.TableL         = root.getChild("TableL");
        this.TableR         = root.getChild("TableR");
        this.Table_B        = root.getChild("Table_B");
        this.FoodT          = root.getChild("FoodT");
        this.FoodTraySide   = root.getChild("FoodTraySide");
        this.FoodTraySideB  = root.getChild("FoodTraySideB");
        this.FoodTraySideC  = root.getChild("FoodTraySideC");
        this.FoodTraySideD  = root.getChild("FoodTraySideD");
        this.Milk           = root.getChild("Milk");
        this.PetFood        = root.getChild("PetFood");
        this.Bottom         = root.getChild("Bottom");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh     = new MeshDefinition();
        PartDefinition root     = mesh.getRoot();
        float limbSwing = 0.0F;

        // Left table side
        root.addOrReplaceChild("TableL",
                CubeListBuilder.create()
                        .texOffs(30, 8)
                        .addBox(-8F, 0F, 7F, 16, 6, 1, new CubeDeformation(limbSwing)),
                PartPose.offset(0F, 18F, 0F)
        );

        // Right table side
        root.addOrReplaceChild("TableR",
                CubeListBuilder.create()
                        .texOffs(30, 8)
                        .addBox(-8F, 18F, -8F, 16, 6, 1, new CubeDeformation(limbSwing)),
                PartPose.offset(0F, 0F, 0F)
        );

        // Back table side, rotated 90° around Y
        root.addOrReplaceChild("Table_B",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-8F, -3F, 0F, 16, 6, 1, new CubeDeformation(limbSwing)),
                PartPose.offsetAndRotation(8F, 21F, 0F, 0F, (float)Math.PI / 2F, 0F)
        );

        // Food tray top
        root.addOrReplaceChild("FoodT",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(1F, 1F, 1F, 4, 1, 4, new CubeDeformation(limbSwing)),
                PartPose.offset(-16F, 22F, 0F)
        );

        // Food tray sides
        root.addOrReplaceChild("FoodTraySide",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-16F, 21F, 5F, 5, 3, 1, new CubeDeformation(limbSwing)),
                PartPose.offset(0F, 0F, 0F)
        );
        root.addOrReplaceChild("FoodTraySideB",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-15F, 21F, 0F, 5, 3, 1, new CubeDeformation(limbSwing)),
                PartPose.offset(0F, 0F, 0F)
        );
        root.addOrReplaceChild("FoodTraySideC",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3F, -1F, 0F, 5, 3, 1, new CubeDeformation(limbSwing)),
                PartPose.offsetAndRotation(-16F, 22F, 2F, 0F, (float)Math.PI / 2F, 0F)
        );
        root.addOrReplaceChild("FoodTraySideD",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3F, -1F, 0F, 5, 3, 1, new CubeDeformation(limbSwing)),
                PartPose.offsetAndRotation(-11F, 22F, 3F, 0F, (float)Math.PI / 2F, 0F)
        );

        // Milk container (cube)
        root.addOrReplaceChild("Milk",
                CubeListBuilder.create()
                        .texOffs(14, 9)
                        .addBox(0F, 0F, 0F, 4, 1, 4, new CubeDeformation(limbSwing)),
                PartPose.offset(-15F, 21F, 1F)
        );

        // Pet food block (same initial pivot)
        root.addOrReplaceChild("PetFood",
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(0F, 0F, 0F, 4, 1, 4, new CubeDeformation(limbSwing)),
                PartPose.offset(-15F, 21F, 1F)
        );

        // Bottom platform
        root.addOrReplaceChild("Bottom",
                CubeListBuilder.create()
                        .texOffs(16, 15)
                        .addBox(-10F, 0F, -7F, 16, 1, 14, new CubeDeformation(limbSwing)),
                PartPose.offset(2F, 23F, 0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entityIn,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        // KittyBed has no animations; we only read entity state.
        this.hasMilk   = entityIn.getHasMilk();
        this.hasFood   = entityIn.getHasFood();
        this.pickedUp  = entityIn.getPickedUp();
        this.milklevel = entityIn.milkLevel;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn,
                               VertexConsumer bufferIn,
                               int packedLightIn,
                               int packedOverlayIn,
                               float red,
                               float green,
                               float blue,
                               float alpha) {
        // Always render table surfaces and bottom
        this.TableL.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
        this.TableR.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Table_B.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
        this.Bottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));

        if (!this.pickedUp) {
            // Render food tray and sides
            this.FoodT.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            this.FoodTraySide.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            this.FoodTraySideB.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            this.FoodTraySideC.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            this.FoodTraySideD.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));

            // If milk present, adjust Milk pivot Y and render
            if (this.hasMilk) {
                this.Milk.y = 21F + this.milklevel;
                this.Milk.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            // If pet food present, adjust PetFood pivot Y and render
            if (this.hasFood) {
                this.PetFood.y = 21F + this.milklevel;
                this.PetFood.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }
    }
}
