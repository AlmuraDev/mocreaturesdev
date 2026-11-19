/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of MoCModelGolem (1.16.5) to 1.20.1.
 * Replaces ModelRenderer[][] with ModelPart[][], and merges setLivingAnimations + setRotationAngles into setupAnim.
 * 
 * Will attempt to separate the setupAnim into two methods:
 * 1. setupAnim
 * 2. prepareMobModel
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelGolem<T extends MoCEntityGolem> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "golem"), "main"
    );

    private final ModelPart[][] blocks; // [23][28]
    private final ModelPart head;
    private final ModelPart headb;
    private final ModelPart chest;
    private final ModelPart chestb;

    private final float radianF = 57.29578F;
    private final int w = 32;
    private final int h = 16;

    private final int[] blocksText = new int[23];

    private MoCEntityGolem entityG;
    private boolean angry;

    public MoCModelGolem(ModelPart root) {
        super(root);
        this.blocks = new ModelPart[23][28];

        // Retrieve head/chest parts
        this.head   = root.getChild("head");
        this.headb  = root.getChild("headb");
        this.chest  = root.getChild("chest");
        this.chestb = root.getChild("chestb");

        // Retrieve all block variants
        for (int g = 0; g < 23; g++) {
            ModelPart groupPart = root.getChild("group_" + g);
            for (int v = 0; v < 28; v++) {
                this.blocks[g][v] = groupPart.getChild("block_" + g + "_" + v);
            }
        }
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
        // Update indices for which variant to display
        for (int i = 0; i < 23; i++) {
            this.blocksText[i] = entity.getBlockText(i);
        }

        // Head yaw: 45° + netHeadYaw
        float headYawRad = (45F + netHeadYaw) / this.radianF;
        this.head.yRot  = headYawRad;
        this.headb.yRot = headYawRad;

        // Chest rotation: 45°, or if summoning, 45° + (ageInTicks/2)
        boolean isSummoning = entity.isMissingCubes();
        float chestYaw = 45F / this.radianF;
        if (isSummoning) {
            chestYaw += ageInTicks / 2F;
        }
        this.chest.yRot  = chestYaw;
        this.chestb.yRot = chestYaw;

        // Determine if chest is open
        boolean openChest = entity.openChest();
        if (openChest) {
            // Pivot chest at z = -7
            this.chest.setPos(0F, -3F, -7F);
            this.chestb.setPos(0F, -3F, -7F);
            // Update front‐chest block rotations
            if (this.blocksText[0]  != 30) this.blocks[0][ this.blocksText[0] ].yRot = -60F / this.radianF;
            if (this.blocksText[1]  != 30) this.blocks[1][ this.blocksText[1] ].yRot = -55F / this.radianF;
            if (this.blocksText[2]  != 30) this.blocks[2][ this.blocksText[2] ].yRot =  60F / this.radianF;
            if (this.blocksText[3]  != 30) this.blocks[3][ this.blocksText[3] ].yRot =  55F / this.radianF;
        } else {
            // Pivot chest at z = -4
            this.chest.setPos(0F, -3F, -4F);
            this.chestb.setPos(0F, -3F, -4F);
            // Reset front‐chest block rotations
            if (this.blocksText[0]  != 30) this.blocks[0][ this.blocksText[0] ].yRot = -40F / this.radianF;
            if (this.blocksText[1]  != 30) this.blocks[1][ this.blocksText[1] ].yRot = -41F / this.radianF;
            if (this.blocksText[2]  != 30) this.blocks[2][ this.blocksText[2] ].yRot =  40F / this.radianF;
            if (this.blocksText[3]  != 30) this.blocks[3][ this.blocksText[3] ].yRot =  41F / this.radianF;
        }

        // Leg rotations
        float RLegXRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;

        // Blocks 15–20: thighs, knees, feet
        if (this.blocksText[15] != 30) this.blocks[15][ this.blocksText[15] ].xRot = (-20F / this.radianF) + LLegXRot;
        if (this.blocksText[16] != 30) this.blocks[16][ this.blocksText[16] ].xRot = LLegXRot;
        if (this.blocksText[17] != 30) this.blocks[17][ this.blocksText[17] ].xRot = LLegXRot;
        if (this.blocksText[18] != 30) this.blocks[18][ this.blocksText[18] ].xRot = (-20F / this.radianF) + RLegXRot;
        if (this.blocksText[19] != 30) this.blocks[19][ this.blocksText[19] ].xRot = RLegXRot;
        if (this.blocksText[20] != 30) this.blocks[20][ this.blocksText[20] ].xRot = RLegXRot;

        // Arm swaying
        boolean throwing = (entity.tCounter > 25);
        float RArmZRot = -(Mth.cos(ageInTicks * 0.09F) * 0.05F) + 0.05F;
        float LArmZRot =  (Mth.cos(ageInTicks * 0.09F) * 0.05F) - 0.05F;

        if (throwing) {
            LLegXRot = -90F / this.radianF;
            RLegXRot = -90F / this.radianF;
            RArmZRot = 0F;
            LArmZRot = 0F;
        }

        // Blocks 9–14: shoulders, arms, hands
        if (this.blocksText[12] != 30) {
            ModelPart p = this.blocks[12][ this.blocksText[12] ];
            p.zRot = (40F  / this.radianF) + RArmZRot;
            p.xRot = LLegXRot;
        }
        if (this.blocksText[13] != 30) {
            ModelPart p = this.blocks[13][ this.blocksText[13] ];
            p.zRot = (12F  / this.radianF) + RArmZRot;
            p.xRot = LLegXRot;
        }
        if (this.blocksText[14] != 30) {
            ModelPart p = this.blocks[14][ this.blocksText[14] ];
            p.zRot = RArmZRot;
            p.xRot = LLegXRot;
        }
        if (this.blocksText[9]  != 30) {
            ModelPart p = this.blocks[9][  this.blocksText[9]  ];
            p.zRot = (-40F / this.radianF) + LArmZRot;
            p.xRot = RLegXRot;
        }
        if (this.blocksText[10] != 30) {
            ModelPart p = this.blocks[10][ this.blocksText[10] ];
            p.zRot = (-12F / this.radianF) + LArmZRot;
            p.xRot = RLegXRot;
        }
        if (this.blocksText[11] != 30) {
            ModelPart p = this.blocks[11][ this.blocksText[11] ];
            p.zRot = LArmZRot;
            p.xRot = RLegXRot;
        }
    }

    @Override
    public void prepareMobModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
        this.entityG = p_102614_;
        this.angry = p_102614_.getGolemState() > 1;
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
        // Render all visible blocks
        float yOffset = entityG.getAdjustedYOffset();

        poseStack.pushPose();
        poseStack.translate(0F, yOffset, 0F);

        for (int g = 0; g < 23; g++) {
            int idx = this.blocksText[g];
            if (idx != 30) {
                this.blocks[g][idx].render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
        }

        // Render head/chest based on angry state
        if (this.angry) {
            this.headb.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.chestb.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
        
        poseStack.popPose();
    }

    /**
     * Defines the mesh and texture size (128×128). Bake this layer via EntityRenderersEvent.RegisterLayerDefinitions.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh    = new MeshDefinition();
        PartDefinition root    = mesh.getRoot();

        // HEAD (default rotation 45° yaw)
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(96, 64)
                        .addBox(-4F, -4F, -4F, 8, 8, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, -10F, 0F, 0F, 0.7853982F, 0F)
        );
        root.addOrReplaceChild("headb",
                CubeListBuilder.create()
                        .texOffs(96, 80)
                        .addBox(-4F, -4F, -4F, 8, 8, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, -10F, 0F, 0F, 0.7853982F, 0F)
        );

        // CHEST (default yaw 45°)
        root.addOrReplaceChild("chest",
                CubeListBuilder.create()
                        .texOffs(96, 96)
                        .addBox(-4F, -4F, -4F, 8, 8, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, -3F, -7F, 0F, 0.7853982F, 0F)
        );
        root.addOrReplaceChild("chestb",
                CubeListBuilder.create()
                        .texOffs(96, 112)
                        .addBox(-4F, -4F, -4F, 8, 8, 8, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, -3F, -7F, 0F, 0.7853982F, 0F)
        );

        // Now define 23 groups, each with 28 texture variants
        for (int g = 0; g < 23; g++) {
            PartDefinition groupPart = root.addOrReplaceChild("group_" + g,
                    CubeListBuilder.create(), PartPose.offset(0F, 0F, 0F)
            );

            // Each variant gets its own child
            for (int v = 0; v < 28; v++) {
                int textX = (v / 8) * 32;
                int textY = (v % 8) * 16;

                // We build the cube for group g, variant v
                CubeListBuilder builder = CubeListBuilder.create().texOffs(textX, textY);
                PartPose pose;

                switch (g) {
                    case 0: // lchest1
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                -97F / 57.29578F, -40F / 57.29578F, 0F);
                        break;
                    case 1: // lchest2
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                -55F / 57.29578F, -41F / 57.29578F, 0F);
                        break;
                    case 2: // rchest1
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                -97F / 57.29578F,  40F / 57.29578F, 0F);
                        break;
                    case 3: // rchest2
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                -55F / 57.29578F,  41F / 57.29578F, 0F);
                        break;
                    case 4: // back
                        builder.addBox(-7F, -14F, -1F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, 6F, 3F,
                                0F, 45F / 57.29578F, 0F);
                        break;
                    case 5: // lback1
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                110F / 57.29578F,  40F / 57.29578F, 0F);
                        break;
                    case 6: // lback2
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                67.8F / 57.29578F,  40F / 57.29578F, 0F);
                        break;
                    case 7: // rback1
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                110F / 57.29578F, -40F / 57.29578F, 0F);
                        break;
                    case 8: // rback2
                        builder.addBox(-4F, 3F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, -3F, 0F,
                                67.8F / 57.29578F, -40F / 57.29578F, 0F);
                        break;
                    case 9: // lshoulder
                        builder.addBox(0F, -2F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(8F, -3F, 0F,
                                0F, 0F, -40F / 57.29578F);
                        break;
                    case 10: // larm
                        builder.addBox(2F, 4F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(8F, -3F, 0F,
                                0F, 0F, -12F / 57.29578F);
                        break;
                    case 11: // lhand
                        builder.addBox(4.5F, 11F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(8F, -3F, 0F);
                        break;
                    case 12: // rshoulder
                        builder.addBox(-8F, -2F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(-8F, -3F, 0F,
                                0F, 0F, 40F / 57.29578F);
                        break;
                    case 13: // rarm
                        builder.addBox(-10F, 4F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(-8F, -3F, 0F,
                                0F, 0F, 12F / 57.29578F);
                        break;
                    case 14: // rhand
                        builder.addBox(-12.5F, 11F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(-8F, -3F, 0F);
                        break;
                    case 15: // lthigh
                        builder.addBox(-3.5F, 0F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(5F, 4F, 0F,
                                -20F / 57.29578F, 0F, 0F);
                        break;
                    case 16: // lknee
                        builder.addBox(-4F, 6F, -7F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(5F, 4F, 0F);
                        break;
                    case 17: // lfoot
                        builder.addBox(-3.5F, 12F, -5F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(5F, 4F, 0F);
                        break;
                    case 18: // rthigh
                        builder.addBox(-4.5F, 0F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(-5F, 4F, 0F,
                                -20F / 57.29578F, 0F, 0F);
                        break;
                    case 19: // rknee
                        builder.addBox(-4F, 6F, -7F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(-5F, 4F, 0F);
                        break;
                    case 20: // rfoot
                        builder.addBox(-4.5F, 12F, -5F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offset(-5F, 4F, 0F);
                        break;
                    case 21: // groin
                        builder.addBox(0F, -4F, -8F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, 6F, 3F,
                                0F, 45F / 57.29578F, 0F);
                        break;
                    case 22: // butt
                        builder.addBox(-4F, -4F, -4F, 8, 8, 8, new CubeDeformation(0.0F));
                        pose = PartPose.offsetAndRotation(0F, 6F, 3F,
                                -42.6F / 57.29578F, 0F, 0F);
                        break;
                    default:
                        // Should not occur
                        builder.addBox(0F, 0F, 0F, 0, 0, 0, new CubeDeformation(0.0F));
                        pose = PartPose.ZERO;
                        break;
                }

                groupPart.addOrReplaceChild("block_" + g + "_" + v, builder, pose);
            }
        }

        return LayerDefinition.create(mesh, 128, 128);
    }
}
