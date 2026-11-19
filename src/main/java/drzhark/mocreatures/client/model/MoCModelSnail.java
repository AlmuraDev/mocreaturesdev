/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.ambient.MoCEntitySnail;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Port of MoCModelSnail from 1.16.5 → 1.20.1.
 */
public class MoCModelSnail<T extends MoCEntitySnail> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "snail"), "main"
    );

    private final ModelPart head;
    private final ModelPart antenna;
    private final ModelPart body;
    private final ModelPart shellUp;
    private final ModelPart shellDown;
    private final ModelPart tail;

    // Animation state for rendering
    private boolean isHiding;
    private int type;

    public MoCModelSnail(ModelPart root) {
        super(root);
        this.head      = root.getChild("head");
        this.antenna   = root.getChild("antenna");
        this.body      = root.getChild("body");
        this.shellUp   = root.getChild("shellUp");
        this.shellDown = root.getChild("shellDown");
        this.tail      = root.getChild("tail");
    }

    /**
     * Define the snail's geometry (all cubes and part poses).
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-1F, 0F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0F, 21.8F, -1F, -0.4537856F, 0F, 0F)
        );

        // Antenna
        PartDefinition antenna = root.addOrReplaceChild("antenna",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-1.5F, 0F, -1F, 3, 2, 0),
                PartPose.offsetAndRotation(0F, 19.4F, -1F, 0.0523599F, 0F, 0F)
        );

        // Body
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1F, 0F, -1F, 2, 2, 4),
                PartPose.offset(0F, 22F, 0F)
        );

        // ShellUp
        PartDefinition shellUp = root.addOrReplaceChild("shellUp",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(-1F, -3F, 0F, 2, 3, 3),
                PartPose.offsetAndRotation(0F, 22.3F, -0.2F, 0.2268928F, 0F, 0F)
        );

        // ShellDown
        PartDefinition shellDown = root.addOrReplaceChild("shellDown",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(-1F, 0F, 0F, 2, 3, 3),
                PartPose.offset(0F, 21F, 0F)
        );

        // Tail
        PartDefinition tail = root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(1, 2)
                        .addBox(-1F, 0F, 0F, 2, 1, 3),
                PartPose.offset(0F, 23F, 3F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    /**
     * Capture “hiding” state and “type” from the entity each tick, so renderToBuffer can choose which parts to draw.
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.isHiding = entity.getIsHiding();
        this.type     = entity.getTypeMoC();

        // Tail animation: swing based on “ageInTicks”, but only if snail is not nearly stationary.
        float tailMov = Mth.cos(ageInTicks * 0.3F) * 0.8F;
        if (limbSwingAmount < 0.1F) {
            tailMov = 0F;
        }
        // Move the tail forward/back by tailMov units:
        this.tail.setPos(0F, 23F, 3F + tailMov);
        // Slightly tilt the shellUp based on tail movement:
        this.shellUp.xRot = 0.2268928F + (tailMov / 10F);
    }

    /**
     * Render all parts—but if “hiding” and type<5, only render shellDown; otherwise render the rest.
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.isHiding && this.type < 5) {
            this.shellDown.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.antenna.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.shellUp.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.tail.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }
}
