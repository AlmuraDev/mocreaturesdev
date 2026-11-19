/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.aquatic.MoCEntityShark;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Ported from 1.16.5 Forge → 1.20.1 Forge. All ModelRenderer → ModelPart;
 * setRotationAngles(...) → setupAnim(...); render(...) → renderToBuffer(...).
 */
public class MoCModelShark<T extends MoCEntityShark> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "shark"), "main"
    );

    private final ModelPart body;
    private final ModelPart torso1;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart upper_head;
    private final ModelPart jaw_base_r1;
    private final ModelPart teeth_row_left_r1;
    private final ModelPart teeth_row_front_r1;
    private final ModelPart upper_skull_r1;
    private final ModelPart nose_bot_r1;
    private final ModelPart lower_jaw;
    private final ModelPart teeth_row_front_r2;
    private final ModelPart dorsal_fin;
    private final ModelPart dorsal_fin_d_r1;
    private final ModelPart dorsal_fin_c_r1;
    private final ModelPart dorsal_fin_b_r1;
    private final ModelPart dorsal_fin_a_r1;
    private final ModelPart pectoral_fins;
    private final ModelPart left_fin;
    private final ModelPart left_fin_d_r1;
    private final ModelPart left_fin_a_r1;
    private final ModelPart left_fin_a_r2;
    private final ModelPart right_fin;
    private final ModelPart right_fin_a_r1;
    private final ModelPart right_fin_a_r2;
    private final ModelPart right_fin_d_r1;
    private final ModelPart torso2;
    private final ModelPart pelvic_fin_right_r1;
    private final ModelPart pelvic_fin_left_r1;
    private final ModelPart tail;
    private final ModelPart caudal_fin;
    private final ModelPart caudal_fin_bot_c_r1;
    private final ModelPart caudal_fin_bot_b_r1;
    private final ModelPart caudal_fin_bot_a_r1;
    private final ModelPart caudal_fin_top_d_r1;
    private final ModelPart caudal_fin_top_c_r1;
    private final ModelPart caudal_fin_top_b_r1;
    private final ModelPart caudal_fin_top_a_r1;

    public MoCModelShark(ModelPart root) {
        super(root);
        this.body         = root.getChild("body");
        this.torso1       = body.getChild("torso1");
        this.neck         = torso1.getChild("neck");
        this.head         = neck.getChild("head");
        this.upper_head   = head.getChild("upper_head");
        this.jaw_base_r1  = upper_head.getChild("jaw_base_r1");
        this.teeth_row_left_r1  = upper_head.getChild("teeth_row_left_r1");
        this.teeth_row_front_r1 = upper_head.getChild("teeth_row_front_r1");
        this.upper_skull_r1      = upper_head.getChild("upper_skull_r1");
        this.nose_bot_r1         = upper_head.getChild("nose_bot_r1");
        this.lower_jaw           = head.getChild("lower_jaw");
        this.teeth_row_front_r2  = lower_jaw.getChild("teeth_row_front_r2");
        this.dorsal_fin          = torso1.getChild("dorsal_fin");
        this.dorsal_fin_d_r1     = dorsal_fin.getChild("dorsal_fin_d_r1");
        this.dorsal_fin_c_r1     = dorsal_fin.getChild("dorsal_fin_c_r1");
        this.dorsal_fin_b_r1     = dorsal_fin.getChild("dorsal_fin_b_r1");
        this.dorsal_fin_a_r1     = dorsal_fin.getChild("dorsal_fin_a_r1");
        this.pectoral_fins       = torso1.getChild("pectoral_fins");
        this.left_fin            = pectoral_fins.getChild("left_fin");
        this.left_fin_d_r1       = left_fin.getChild("left_fin_d_r1");
        this.left_fin_a_r1       = left_fin.getChild("left_fin_a_r1");
        this.left_fin_a_r2       = left_fin.getChild("left_fin_a_r2");
        this.right_fin           = pectoral_fins.getChild("right_fin");
        this.right_fin_a_r1      = right_fin.getChild("right_fin_a_r1");
        this.right_fin_a_r2      = right_fin.getChild("right_fin_a_r2");
        this.right_fin_d_r1      = right_fin.getChild("right_fin_d_r1");
        this.torso2              = torso1.getChild("torso2");
        this.pelvic_fin_right_r1 = torso2.getChild("pelvic_fin_right_r1");
        this.pelvic_fin_left_r1  = torso2.getChild("pelvic_fin_left_r1");
        this.tail                = torso2.getChild("tail");
        this.caudal_fin          = tail.getChild("caudal_fin");
        this.caudal_fin_bot_c_r1 = caudal_fin.getChild("caudal_fin_bot_c_r1");
        this.caudal_fin_bot_b_r1 = caudal_fin.getChild("caudal_fin_bot_b_r1");
        this.caudal_fin_bot_a_r1 = caudal_fin.getChild("caudal_fin_bot_a_r1");
        this.caudal_fin_top_d_r1 = caudal_fin.getChild("caudal_fin_top_d_r1");
        this.caudal_fin_top_c_r1 = caudal_fin.getChild("caudal_fin_top_c_r1");
        this.caudal_fin_top_b_r1 = caudal_fin.getChild("caudal_fin_top_b_r1");
        this.caudal_fin_top_a_r1 = caudal_fin.getChild("caudal_fin_top_a_r1");
    }

    /**
     * Defines all of the parts, their cubes, offsets, and rotations.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ----------------------
        // body (root)
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create(),
                PartPose.offset(-0.5F, 16.0F, -3.0F)
        );

        // torso_1
        PartDefinition torso1 = body.addOrReplaceChild("torso1",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-10.5F, -9.0F, -30.0F, 22, 20, 33, CubeDeformation.NONE),
                PartPose.offset(0.0F, -4.0F, 16.0F)
        );

        // neck
        PartDefinition neck = torso1.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(56, 73).addBox(-9.0F, -10.0F, -14.0F, 20, 18, 15, CubeDeformation.NONE),
                PartPose.offset(-0.5F, 2.0F, -30.0F)
        );

        // head (no cubes here—just a pivot)
        PartDefinition head = neck.addOrReplaceChild("head",
                CubeListBuilder.create(),
                PartPose.offset(-1.5F, 0.0F, -14.0F)
        );

        // upper_head
        PartDefinition upper_head = head.addOrReplaceChild("upper_head",
                CubeListBuilder.create()
                        .texOffs(89, 24).mirror().addBox(7.451F, 4.5311F, -43.1503F, 0, 4, 4, CubeDeformation.NONE).mirror(false)
                        .texOffs(89, 24).addBox(-4.749F, 4.5311F, -43.1503F, 0, 4, 4, CubeDeformation.NONE),
                PartPose.offset(1.15F, -5.5F, 30.5F)
        );

        // jaw_base_r1
        PartDefinition jaw_base_r1 = upper_head.addOrReplaceChild("jaw_base_r1",
                CubeListBuilder.create()
                        .texOffs(160, 4).addBox(-7.001F, -1.9315F, -4.8207F, 14, 9, 8, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-5.9794F, -10.9315F, 2.1793F, 12, 20, 3, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.351F, 5.5014F, -37.2182F, 1.5708F, 0.0F, 0.0F)
        );

        // teeth_row_left_r1
        PartDefinition teeth_row_left_r1 = upper_head.addOrReplaceChild("teeth_row_left_r1",
                CubeListBuilder.create()
                        .texOffs(8, 92).addBox(5.0F, -3.5704F, -5.5322F, 0, 3, 8, CubeDeformation.NONE)
                        .texOffs(8, 92).addBox(-7.0F, -3.5704F, -5.5322F, 0, 3, 8, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.351F, 7.5014F, -41.2182F, 0.0F, 0.0F, 0.0F)
        );

        // teeth_row_front_r1
        PartDefinition teeth_row_front_r1 = upper_head.addOrReplaceChild("teeth_row_front_r1",
                CubeListBuilder.create()
                        .texOffs(1, 80).addBox(5.4612F, -4.5704F, -6.072F, 0, 3, 12, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.351F, 8.5014F, -41.2182F, 0.0F, 1.5708F, 0.0F)
        );

        // upper_skull_r1
        PartDefinition upper_skull_r1 = upper_head.addOrReplaceChild("upper_skull_r1",
                CubeListBuilder.create()
                        .texOffs(78, 0).addBox(-7.9794F, -11.3154F, 1.1579F, 16, 19, 7, CubeDeformation.NONE)
                        .texOffs(68, 107).addBox(-7.9794F, -16.3154F, 5.1579F, 16, 5, 3, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.351F, 5.5014F, -36.7182F, 1.7017F, 0.0F, 0.0F)
        );

        // nose_bot_r1
        PartDefinition nose_bot_r1 = upper_head.addOrReplaceChild("nose_bot_r1",
                CubeListBuilder.create()
                        .texOffs(129, 1).addBox(-8.0F, 0.0207F, -0.0117F, 16, 7, 3, new CubeDeformation(-0.02F)),
                PartPose.offsetAndRotation(1.3716F, 2.5064F, -53.5873F, 1.1083F, 0.0F, 0.0F)
        );

        // lower_jaw
        PartDefinition lower_jaw = head.addOrReplaceChild("lower_jaw",
                CubeListBuilder.create()
                        .texOffs(55, 57).addBox(-5.9828F, -0.25F, -6.8927F, 12, 3, 10, new CubeDeformation(0.05F))
                        .texOffs(10, 89).addBox(5.0182F, -3.25F, -5.8927F, 0, 3, 7, CubeDeformation.NONE)
                        .texOffs(10, 89).addBox(-4.9818F, -3.25F, -5.8927F, 0, 3, 7, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.4828F, 1.6811F, -8.3576F, 0.1309F, 0.0F, 0.0F)
        );

        // teeth_row_front_r2
        PartDefinition teeth_row_front_r2 = lower_jaw.addOrReplaceChild("teeth_row_front_r2",
                CubeListBuilder.create()
                        .texOffs(4, 78).addBox(16.0196F, -6.0052F, -4.571F, 0, 3, 10, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-0.4828F, 2.7552F, 10.1978F, 0.0F, 1.5708F, 0.0F)
        );

        // dorsal_fin
        PartDefinition dorsal_fin = torso1.addOrReplaceChild("dorsal_fin",
                CubeListBuilder.create(),
                PartPose.offset(0.5F, -20.1292F, -6.2277F)
        );
        // dorsal_fin_d_r1
        dorsal_fin.addOrReplaceChild("dorsal_fin_d_r1",
                CubeListBuilder.create()
                        .texOffs(111, 39).addBox(8.0F, -5.1949F, 7.2152F, 2, 7, 9, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.0F, 18.324F, -2.6875F, 1.5708F, 0.0F, 0.0F)
        );
        // dorsal_fin_c_r1
        dorsal_fin.addOrReplaceChild("dorsal_fin_c_r1",
                CubeListBuilder.create()
                        .texOffs(170, 28).addBox(-1.0F, -4.0F, -1.0F, 2, 4, 12, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 10.7262F, 3.0681F, 1.8326F, 0.0F, 0.0F)
        );
        // dorsal_fin_b_r1
        dorsal_fin.addOrReplaceChild("dorsal_fin_b_r1",
                CubeListBuilder.create()
                        .texOffs(161, 22).addBox(-1.0F, -8.0F, -1.0F, 2, 4, 8, new CubeDeformation(-0.01F)),
                PartPose.offsetAndRotation(0.0F, 7.6647F, -2.5701F, 0.6981F, 0.0F, 0.0F)
        );
        // dorsal_fin_a_r1
        dorsal_fin.addOrReplaceChild("dorsal_fin_a_r1",
                CubeListBuilder.create()
                        .texOffs(143, 25).addBox(-1.0F, -4.0F, -1.0F, 2, 4, 10, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 12.0248F, -8.2271F, 1.1781F, 0.0F, 0.0F)
        );

        // pectoral_fins
        PartDefinition pectoral_fins = torso1.addOrReplaceChild("pectoral_fins",
                CubeListBuilder.create(),
                PartPose.ZERO
        );

        // left_fin
        PartDefinition left_fin = pectoral_fins.addOrReplaceChild("left_fin",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(11.5F, 7.0F, -21.0F, 0.0F, 0.0F, -0.7854F)
        );
        // left_fin_d_r1
        left_fin.addOrReplaceChild("left_fin_d_r1",
                CubeListBuilder.create()
                        .texOffs(225, 0).addBox(-1.0F, 0.3827F, -3.0761F, 2, 19, 5, new CubeDeformation(-0.01F)),
                PartPose.offsetAndRotation(0.3933F, -2.7076F, -1.1278F, 0.3316F, 0.0F, 0.0F)
        );
        // left_fin_a_r1
        left_fin.addOrReplaceChild("left_fin_a_r1",
                CubeListBuilder.create()
                        .texOffs(216, 24).addBox(-1.0F, 3.9561F, 4.8879F, 2, 11, 4, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.3933F, 12.7347F, -9.897F, 0.9076F, 0.0F, 0.0F)
        );
        // left_fin_a_r2
        left_fin.addOrReplaceChild("left_fin_a_r2",
                CubeListBuilder.create()
                        .texOffs(200, 17).addBox(-1.0F, -1.2777F, -2.8042F, 2, 14, 5, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.3933F, -1.6235F, -5.2607F, 0.3316F, 0.0F, 0.0F)
        );

        // right_fin
        PartDefinition right_fin = pectoral_fins.addOrReplaceChild("right_fin",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-10.5F, 6.0F, -21.0F, 0.0F, 0.0F, 0.7854F)
        );
        // right_fin_a_r1
        right_fin.addOrReplaceChild("right_fin_a_r1",
                CubeListBuilder.create()
                        .texOffs(200, 17).addBox(-1.0F, -1.2777F, -2.8042F, 2, 14, 5, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.9246F, -1.4211F, -5.2607F, 0.3316F, 0.0F, 0.0F)
        );
        // right_fin_a_r2
        right_fin.addOrReplaceChild("right_fin_a_r2",
                CubeListBuilder.create()
                        .texOffs(216, 24).addBox(-1.0F, 3.9561F, 4.8879F, 2, 11, 4, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.9246F, 12.9372F, -9.897F, 0.9076F, 0.0F, 0.0F)
        );
        // right_fin_d_r1
        right_fin.addOrReplaceChild("right_fin_d_r1",
                CubeListBuilder.create()
                        .texOffs(225, 0).mirror(true).addBox(-1.0F, 0.3827F, -3.0761F, 2, 19, 5, new CubeDeformation(-0.01F)).mirror(false),
                PartPose.offsetAndRotation(0.9246F, -2.5052F, -1.1278F, 0.3316F, 0.0F, 0.0F)
        );

        // torso_2
        PartDefinition torso2 = torso1.addOrReplaceChild("torso2",
                CubeListBuilder.create()
                        .texOffs(0, 54).addBox(-8.0F, -8.0F, 0.0F, 18, 16, 17, CubeDeformation.NONE),
                PartPose.offset(-0.5F, 0.0F, 2.0F)
        );

        // pelvic_fin_right_r1
        torso2.addOrReplaceChild("pelvic_fin_right_r1",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(1.5F, 0.0F, -4.5F, 0, 6, 9, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-5.0F, 7.0F, 10.5F, 0.0F, 0.0F, 0.3927F)
        );

        // pelvic_fin_left_r1
        torso2.addOrReplaceChild("pelvic_fin_left_r1",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.5F, 0.0F, -4.5F, 0, 6, 9, CubeDeformation.NONE),
                PartPose.offsetAndRotation(6.0F, 7.0F, 10.5F, 0.0F, 0.0F, -0.3927F)
        );

        // tail
        PartDefinition tail = torso2.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(0, 88).addBox(-3.5F, -6.2608F, -4.1248F, 8, 10, 25, CubeDeformation.NONE)
                        .texOffs(78, 22).addBox(0.5F, -10.8811F, 0.3854F, 0, 5, 5, CubeDeformation.NONE)
                        .texOffs(10, 98).addBox(0.5F, 3.35F, 4.3854F, 0, 7, 7, CubeDeformation.NONE),
                PartPose.offset(0.5F, -1.0F, 17.0F)
        );

        // caudal_fin
        PartDefinition caudal_fin = tail.addOrReplaceChild("caudal_fin",
                CubeListBuilder.create()
                        .texOffs(148, 69).addBox(-2.75F, -4.1371F, -50.1669F, 6, 6, 9, CubeDeformation.NONE),
                PartPose.offset(0.25F, 0.229F, 69.8752F)
        );

        // caudal_fin_bot_c_r1
        caudal_fin.addOrReplaceChild("caudal_fin_bot_c_r1",
                CubeListBuilder.create()
                        .texOffs(189, 49).addBox(-1.0F, -3.8859F, -6.906F, 2, 4, 15, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.25F, 5.2995F, -40.2294F, -1.1781F, 0.0F, 0.0F)
        );

        // caudal_fin_bot_b_r1
        caudal_fin.addOrReplaceChild("caudal_fin_bot_b_r1",
                CubeListBuilder.create()
                        .texOffs(146, 45).addBox(-0.99F, -0.0384F, -9.3692F, 2, 4, 19, new CubeDeformation(0.02F)),
                PartPose.offsetAndRotation(0.25F, 5.2995F, -40.2294F, -0.7418F, 0.0F, 0.0F)
        );

        // caudal_fin_bot_a_r1
        caudal_fin.addOrReplaceChild("caudal_fin_bot_a_r1",
                CubeListBuilder.create()
                        .texOffs(129, 13).addBox(-1.01F, 0.1241F, -8.396F, 2, 4, 8, new CubeDeformation(-0.01F)),
                PartPose.offsetAndRotation(0.25F, 5.2995F, -40.2294F, -1.1781F, 0.0F, 0.0F)
        );

        // caudal_fin_top_d_r1
        caudal_fin.addOrReplaceChild("caudal_fin_top_d_r1",
                CubeListBuilder.create()
                        .texOffs(113, 27).addBox(-2.0F, -3.5F, 3.0F, 2, 3, 6, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(1.24F, -7.2547F, -40.024F, 0.9599F, 0.0F, 0.0F)
        );

        // caudal_fin_top_c_r1
        caudal_fin.addOrReplaceChild("caudal_fin_top_c_r1",
                CubeListBuilder.create()
                        .texOffs(171, 45).addBox(-2.0F, -3.9791F, -14.9833F, 2, 4, 14, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.25F, -19.9763F, -26.8568F, 0.9599F, 0.0F, 0.0F)
        );

        // caudal_fin_top_b_r1
        caudal_fin.addOrReplaceChild("caudal_fin_top_b_r1",
                CubeListBuilder.create()
                        .texOffs(124, 41).addBox(-2.02F, -2.0F, -9.0F, 2, 4, 18, new CubeDeformation(0.03F)),
                PartPose.offsetAndRotation(1.26F, -16.7717F, -35.6639F, 0.5236F, 0.0F, 0.0F)
        );

        // caudal_fin_top_a_r1
        caudal_fin.addOrReplaceChild("caudal_fin_top_a_r1",
                CubeListBuilder.create()
                        .texOffs(112, 65).addBox(-1.0F, -4.0F, -3.0F, 2, 11, 12, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.25F, -4.3334F, -46.33F, 0.9599F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Swing the tail side‐to‐side depending on movement. /.4f matches the old "/4"
        this.tail.yRot = (Mth.cos(limbSwing * 0.6662F) * limbSwingAmount) * 0.25F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
