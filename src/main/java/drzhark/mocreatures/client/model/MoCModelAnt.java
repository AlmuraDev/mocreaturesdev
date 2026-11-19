package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntityAnt;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MoCModelAnt<T extends MoCEntityAnt> extends MoCEntityModel<T> {
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath("mocreatures", "ant"),
                    "main"
            );

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart rightAntenna;
    private final ModelPart leftAntenna;
    private final ModelPart thorax;
    private final ModelPart abdomen;
    private final ModelPart midLegs;
    private final ModelPart frontLegs;
    private final ModelPart rearLegs;

    public MoCModelAnt(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("head");
        this.mouth = root.getChild("mouth");
        this.rightAntenna = root.getChild("right_antenna");
        this.leftAntenna = root.getChild("left_antenna");
        this.thorax = root.getChild("thorax");
        this.abdomen = root.getChild("abdomen");
        this.midLegs = root.getChild("mid_legs");
        this.frontLegs = root.getChild("front_legs");
        this.rearLegs = root.getChild("rear_legs");
    }

    /**
     * Registers all parts of the Ant model and returns a LayerDefinition.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-0.5F, 0F, 0F, 1, 1, 1),
                PartPose.offsetAndRotation(
                        0F, 21.9F, -1.3F,
                        -2.171231F, 0F, 0F
                )
        );

        // Mouth
        root.addOrReplaceChild(
                "mouth",
                CubeListBuilder.create()
                        .texOffs(8, 10)
                        // Note: original depth was zero (2,1,0)
                        .addBox(0F, 0F, 0F, 2, 1, 0),
                PartPose.offsetAndRotation(
                        -1F, 22.3F, -1.9F,
                        -0.8286699F, 0F, 0F
                )
        );

        // Right Antenna
        root.addOrReplaceChild(
                "right_antenna",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        // This is 1×0×1 (height zero)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(
                        -0.5F, 21.7F, -2.3F,
                        -1.041001F, 0.7853982F, 0F
                )
        );

        // Left Antenna
        root.addOrReplaceChild(
                "left_antenna",
                CubeListBuilder.create()
                        .texOffs(4, 6)
                        .addBox(-0.5F, 0F, -1F, 1, 0, 1),
                PartPose.offsetAndRotation(
                        0.5F, 21.7F, -2.3F,
                        -1.041001F, -0.7853982F, 0F
                )
        );

        // Thorax
        root.addOrReplaceChild(
                "thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, 1.5F, -1F, 1, 1, 2),
                PartPose.offset(0F, 20F, -0.5F)
        );

        // Abdomen
        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create()
                        .texOffs(8, 1)
                        .addBox(-0.5F, -0.2F, -1F, 1, 2, 1),
                PartPose.offsetAndRotation(
                        0F, 21.5F, 0.3F,
                        1.706911F, 0F, 0F
                )
        );

        // Mid Legs
        root.addOrReplaceChild(
                "mid_legs",
                CubeListBuilder.create()
                        .texOffs(4, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(
                        0F, 22F, -0.7F,
                        0.5948578F, 0F, 0F
                )
        );

        // Front Legs
        root.addOrReplaceChild(
                "front_legs",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(
                        0F, 22F, -0.8F,
                        -0.6192304F, 0F, 0F
                )
        );

        // Rear Legs
        root.addOrReplaceChild(
                "rear_legs",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1F, 0F, 0F, 2, 3, 0),
                PartPose.offsetAndRotation(
                        0F, 22F, 0F,
                        0.9136644F, 0F, 0F
                )
        );

        // No other parts (we only had those nine ModelRenderers)

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(
            @NotNull T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Exactly the same logic as setRotationAngles in 1.16.5, but now applied to xRot fields:

        float legMov   = Mth.cos(limbSwing + (float)Math.PI) * limbSwingAmount;
        float legMovB  = Mth.cos(limbSwing) * limbSwingAmount;

        this.frontLegs.xRot = -0.6192304F + legMov;
        this.midLegs.xRot   = 0.5948578F + legMovB;
        this.rearLegs.xRot  = 0.9136644F + legMov;
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        // Just render the root; it will draw all nine children.
        this.root.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
