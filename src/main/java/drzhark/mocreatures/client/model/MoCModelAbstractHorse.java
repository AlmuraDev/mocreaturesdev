package drzhark.mocreatures.client.model;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.entity.hostile.MoCEntityHorseMob;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public abstract class MoCModelAbstractHorse<T extends Entity> extends MoCEntityModel<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "horse"), "main");

    // ModelPart fields (matching 1.16 names, snake_case)
    protected final ModelPart body;
    protected final ModelPart tailA, tailB, tailC;
    protected final ModelPart leg1A, leg1B, leg1C;
    protected final ModelPart leg2A, leg2B, leg2C;
    protected final ModelPart leg3A, leg3B, leg3C;
    protected final ModelPart leg4A, leg4B, leg4C;
    protected final ModelPart head, uMouth, lMouth, uMouth2, lMouth2, unicorn, ear1, ear2, muleEarL, muleEarR, neck;
    protected final ModelPart bag1, bag2;
    protected final ModelPart saddle, saddleB, saddleC, saddleL, saddleL2, saddleR, saddleR2, saddleMouthL, saddleMouthR,
            saddleMouthLine, saddleMouthLineR, headSaddle;
    protected final ModelPart mane;
    protected final ModelPart midWing, innerWing, outerWing, innerWingR, midWingR, outerWingR;
    protected final ModelPart butterflyL, butterflyR;

    // State flags (copied from 1.16)
    protected int type;
    protected boolean saddled;
    protected boolean rider;
    protected boolean flapwings;
    protected boolean shuffling;
    protected boolean wings;
    protected boolean eating;
    protected boolean standing;
    protected boolean moveTail;
    protected boolean floating;

    public MoCModelAbstractHorse(ModelPart root) {
        this(root, RenderTypes::entityCutout);
    }

    public MoCModelAbstractHorse(ModelPart root, java.util.function.Function<net.minecraft.resources.Identifier, net.minecraft.client.renderer.rendertype.RenderType> renderType) {
        super(root, renderType);
        this.body              = root.getChild("body");
        this.tailA             = root.getChild("tailA");
        this.tailB             = root.getChild("tailB");
        this.tailC             = root.getChild("tailC");

        this.leg1A             = root.getChild("leg1A");
        this.leg1B             = root.getChild("leg1B");
        this.leg1C             = root.getChild("leg1C");
        this.leg2A             = root.getChild("leg2A");
        this.leg2B             = root.getChild("leg2B");
        this.leg2C             = root.getChild("leg2C");
        this.leg3A             = root.getChild("leg3A");
        this.leg3B             = root.getChild("leg3B");
        this.leg3C             = root.getChild("leg3C");
        this.leg4A             = root.getChild("leg4A");
        this.leg4B             = root.getChild("leg4B");
        this.leg4C             = root.getChild("leg4C");

        this.head              = root.getChild("head");
        this.uMouth            = root.getChild("uMouth");
        this.lMouth            = root.getChild("lMouth");
        this.uMouth2           = root.getChild("uMouth2");
        this.lMouth2           = root.getChild("lMouth2");
        this.unicorn           = root.getChild("unicorn");
        this.ear1              = root.getChild("ear1");
        this.ear2              = root.getChild("ear2");
        this.muleEarL          = root.getChild("muleEarL");
        this.muleEarR          = root.getChild("muleEarR");
        this.neck              = root.getChild("neck");

        this.bag1              = root.getChild("bag1");
        this.bag2              = root.getChild("bag2");

        this.saddle            = root.getChild("saddle");
        this.saddleB           = root.getChild("saddleB");
        this.saddleC           = root.getChild("saddleC");
        this.saddleL           = root.getChild("saddleL");
        this.saddleL2          = root.getChild("saddleL2");
        this.saddleR           = root.getChild("saddleR");
        this.saddleR2          = root.getChild("saddleR2");
        this.saddleMouthL      = root.getChild("saddleMouthL");
        this.saddleMouthR      = root.getChild("saddleMouthR");
        this.saddleMouthLine   = root.getChild("saddleMouthLine");
        this.saddleMouthLineR  = root.getChild("saddleMouthLineR");
        this.headSaddle        = root.getChild("headSaddle");

        this.mane              = root.getChild("mane");

        this.midWing           = root.getChild("midWing");
        this.innerWing         = root.getChild("innerWing");
        this.outerWing         = root.getChild("outerWing");
        this.innerWingR        = root.getChild("innerWingR");
        this.midWingR          = root.getChild("midWingR");
        this.outerWingR        = root.getChild("outerWingR");

        this.butterflyL        = root.getChild("butterflyL");
        this.butterflyR        = root.getChild("butterflyR");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 34).addBox(-5F, -8F, -19F, 10, 10, 24),
                PartPose.offset(0F, 11F, 9F));

        // Tail segments
        root.addOrReplaceChild("tailA", CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-1F, -1F, 0F, 2, 2, 3),
                PartPose.offsetAndRotation(0F, 3F, 14F, -1.134464F, 0F, 0F));
        root.addOrReplaceChild("tailB", CubeListBuilder.create()
                        .texOffs(38, 7).addBox(-1.5F, -2F, 3F, 3, 4, 7),
                PartPose.offsetAndRotation(0F, 3F, 14F, -1.134464F, 0F, 0F));
        root.addOrReplaceChild("tailC", CubeListBuilder.create()
                        .texOffs(24, 3).addBox(-1.5F, -4.5F, 9F, 3, 4, 7),
                PartPose.offsetAndRotation(0F, 3F, 14F, -1.40215F, 0F, 0F));

        // Legs – rear right (leg1)
        root.addOrReplaceChild("leg1A", CubeListBuilder.create()
                        .texOffs(78, 29).addBox(-2.5F, -2F, -2.5F, 4, 9, 5),
                PartPose.offset(4F, 9F, 11F));
        root.addOrReplaceChild("leg1B", CubeListBuilder.create()
                        .texOffs(78, 43).addBox(-2F, 0F, -1.5F, 3, 5, 3),
                PartPose.offset(4F, 16F, 11F));
        root.addOrReplaceChild("leg1C", CubeListBuilder.create()
                        .texOffs(78, 51).addBox(-2.5F, 5.1F, -2F, 4, 3, 4),
                PartPose.offset(4F, 16F, 11F));

        // Legs – rear left (leg2)
        root.addOrReplaceChild("leg2A", CubeListBuilder.create()
                        .texOffs(96, 29).addBox(-1.5F, -2F, -2.5F, 4, 9, 5),
                PartPose.offset(-4F, 9F, 11F));
        root.addOrReplaceChild("leg2B", CubeListBuilder.create()
                        .texOffs(96, 43).addBox(-1F, 0F, -1.5F, 3, 5, 3),
                PartPose.offset(-4F, 16F, 11F));
        root.addOrReplaceChild("leg2C", CubeListBuilder.create()
                        .texOffs(96, 51).addBox(-1.5F, 5.1F, -2F, 4, 3, 4),
                PartPose.offset(-4F, 16F, 11F));

        // Legs – front right (leg3)
        root.addOrReplaceChild("leg3A", CubeListBuilder.create()
                        .texOffs(44, 29).addBox(-1.9F, -1F, -2.1F, 3, 8, 4),
                PartPose.offset(4F, 9F, -8F));
        root.addOrReplaceChild("leg3B", CubeListBuilder.create()
                        .texOffs(44, 41).addBox(-1.9F, 0F, -1.6F, 3, 5, 3),
                PartPose.offset(4F, 16F, -8F));
        root.addOrReplaceChild("leg3C", CubeListBuilder.create()
                        .texOffs(44, 51).addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4),
                PartPose.offset(4F, 16F, -8F));

        // Legs – front left (leg4)
        root.addOrReplaceChild("leg4A", CubeListBuilder.create()
                        .texOffs(60, 29).addBox(-1.1F, -1F, -2.1F, 3, 8, 4),
                PartPose.offset(-4F, 9F, -8F));
        root.addOrReplaceChild("leg4B", CubeListBuilder.create()
                        .texOffs(60, 41).addBox(-1.1F, 0F, -1.6F, 3, 5, 3),
                PartPose.offset(-4F, 16F, -8F));
        root.addOrReplaceChild("leg4C", CubeListBuilder.create()
                        .texOffs(60, 51).addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4),
                PartPose.offset(-4F, 16F, -8F));

        // Head and facial parts
        root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5F, -10F, -1.5F, 5, 5, 7),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("uMouth", CubeListBuilder.create()
                        .texOffs(24, 18).addBox(-2F, -10F, -7F, 4, 3, 6),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("lMouth", CubeListBuilder.create()
                        .texOffs(24, 27).addBox(-2F, -7F, -6.5F, 4, 2, 5),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("uMouth2", CubeListBuilder.create()
                        .texOffs(24, 18).addBox(-2F, -10F, -8F, 4, 3, 6),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.4363323F, 0F, 0F));
        root.addOrReplaceChild("lMouth2", CubeListBuilder.create()
                        .texOffs(24, 27).addBox(-2F, -7F, -5.5F, 4, 2, 5),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.7853982F, 0F, 0F));
        root.addOrReplaceChild("unicorn", CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-0.5F, -18F, 2F, 1, 8, 1),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("ear1", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(0.45F, -12F, 4F, 2, 3, 1),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("ear2", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.45F, -12F, 4F, 2, 3, 1),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("muleEarL", CubeListBuilder.create()
                        .texOffs(0, 12).addBox(-2F, -16F, 4F, 2, 7, 1),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0.2617994F));
        root.addOrReplaceChild("muleEarR", CubeListBuilder.create()
                        .texOffs(0, 12).addBox(0F, -16F, 4F, 2, 7, 1),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, -0.2617994F));
        root.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(0, 12).addBox(-2.05F, -9.8F, -2F, 4, 14, 8),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));

        // Bags
        root.addOrReplaceChild("bag1", CubeListBuilder.create()
                        .texOffs(0, 34).addBox(-3F, 0F, 0F, 8, 8, 3),
                PartPose.offsetAndRotation(-7.5F, 3F, 10F, 0F, (float)Math.PI / 2F, 0F));
        root.addOrReplaceChild("bag2", CubeListBuilder.create()
                        .texOffs(0, 47).addBox(-3F, 0F, 0F, 8, 8, 3),
                PartPose.offsetAndRotation(4.5F, 3F, 10F, 0F, (float)Math.PI / 2F, 0F));

        // Saddle pieces
        root.addOrReplaceChild("saddle", CubeListBuilder.create()
                        .texOffs(80, 0).addBox(-5F, 0F, -3F, 10, 1, 8),
                PartPose.offset(0F, 2F, 2F));
        root.addOrReplaceChild("saddleB", CubeListBuilder.create()
                        .texOffs(106, 9).addBox(-1.5F, -1F, -3F, 3, 1, 2),
                PartPose.offset(0F, 2F, 2F));
        root.addOrReplaceChild("saddleC", CubeListBuilder.create()
                        .texOffs(80, 9).addBox(-4F, -1F, 3F, 8, 1, 2),
                PartPose.offset(0F, 2F, 2F));
        root.addOrReplaceChild("saddleL2", CubeListBuilder.create()
                        .texOffs(74, 0).addBox(-0.5F, 6F, -1F, 1, 2, 2),
                PartPose.offset(5F, 3F, 2F));
        root.addOrReplaceChild("saddleL", CubeListBuilder.create()
                        .texOffs(70, 0).addBox(-0.5F, 0F, -0.5F, 1, 6, 1),
                PartPose.offset(5F, 3F, 2F));
        root.addOrReplaceChild("saddleR2", CubeListBuilder.create()
                        .texOffs(74, 4).addBox(-0.5F, 6F, -1F, 1, 2, 2),
                PartPose.offset(-5F, 3F, 2F));
        root.addOrReplaceChild("saddleR", CubeListBuilder.create()
                        .texOffs(80, 0).addBox(-0.5F, 0F, -0.5F, 1, 6, 1),
                PartPose.offset(-5F, 3F, 2F));
        root.addOrReplaceChild("saddleMouthL", CubeListBuilder.create()
                        .texOffs(74, 13).addBox(1.5F, -8F, -4F, 1, 2, 2),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("saddleMouthR", CubeListBuilder.create()
                        .texOffs(74, 13).addBox(-2.5F, -8F, -4F, 1, 2, 2),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("saddleMouthLine", CubeListBuilder.create()
                        .texOffs(44, 10).addBox(2.6F, -6F, -6F, 0, 3, 16),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("saddleMouthLineR", CubeListBuilder.create()
                        .texOffs(44, 5).addBox(-2.6F, -6F, -6F, 0, 3, 16),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));
        root.addOrReplaceChild("headSaddle", CubeListBuilder.create()
                        .texOffs(80, 12).addBox(-2.5F, -10.1F, -7F, 5F, 5F, 12F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));

        // Mane
        root.addOrReplaceChild("mane", CubeListBuilder.create()
                        .texOffs(58, 0).addBox(-1F, -11.5F, 5F, 2, 16, 4),
                PartPose.offsetAndRotation(0F, 4F, -10F, 0.5235988F, 0F, 0F));

        // Wings
        root.addOrReplaceChild("midWing", CubeListBuilder.create()
                        .texOffs(82, 68).addBox(1F, 0.1F, 1F, 12, 2, 11),
                PartPose.offsetAndRotation(5F, 3F, -6F, 0F, 0.0872665F, 0F));
        root.addOrReplaceChild("innerWing", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(0F, 0F, 0F, 7, 2, 11),
                PartPose.offsetAndRotation(5F, 3F, -6F, 0F, -0.3490659F, 0F));
        root.addOrReplaceChild("outerWing", CubeListBuilder.create()
                        .texOffs(0, 68).addBox(0F, 0F, 0F, 22, 2, 11),
                PartPose.offsetAndRotation(17F, 3F, -6F, 0F, -0.3228859F, 0F));
        root.addOrReplaceChild("innerWingR", CubeListBuilder.create()
                        .texOffs(0, 110).addBox(-7F, 0F, 0F, 7, 2, 11),
                PartPose.offsetAndRotation(-5F, 3F, -6F, 0F, 0.3490659F, 0F));
        root.addOrReplaceChild("midWingR", CubeListBuilder.create()
                        .texOffs(82, 82).addBox(-13F, 0.1F, 1F, 12, 2, 11),
                PartPose.offsetAndRotation(-5F, 3F, -6F, 0F, -0.0872665F, 0F));
        root.addOrReplaceChild("outerWingR", CubeListBuilder.create()
                        .texOffs(0, 82).addBox(-22F, 0F, 0F, 22, 2, 11),
                PartPose.offsetAndRotation(-17F, 3F, -6F, 0F, 0.3228859F, 0F));

        // Butterflies
        root.addOrReplaceChild("butterflyL", CubeListBuilder.create()
                        .texOffs(0, 98).addBox(-1F, 0F, -14F, 26, 0, 30),
                PartPose.offsetAndRotation(4.5F, 3F, -2F, 0F, 0F, -0.78539F));
        root.addOrReplaceChild("butterflyR", CubeListBuilder.create()
                        .texOffs(0, 68).addBox(-25F, 0F, -14F, 26, 0, 30),
                PartPose.offsetAndRotation(-4.5F, 3F, -2F, 0F, 0F, 0.78539F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float RLegXRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.8F * limbSwingAmount;
        float LLegXRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
        float HeadXRot = headPitch * (1F / 57.29578F);

        if (entityIn instanceof MoCEntityHorse horse) {
            this.type = horse.getTypeMoC();
            this.shuffling = (this.type == 60) && horse.shuffleCounter > 0;
        } else {
            this.shuffling = false;
        }

        if (netHeadYaw > 20F) netHeadYaw = 20F;
        if (netHeadYaw < -20F) netHeadYaw = -20F;

        if (this.shuffling) {
            HeadXRot += Mth.cos(ageInTicks * 0.4F) * 0.15F;
        } else if (limbSwingAmount > 0.2F && !floating) {
            HeadXRot += Mth.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;
        }

        // HEAD
        head.y = 4.0F;
        head.z = -10F;
        head.xRot = 0.5235988F + HeadXRot;
        head.yRot = netHeadYaw * (1F / 57.29578F);
        tailA.y = 3F;
        tailB.z = 14F;
        bag2.y = 3F;
        bag2.z = 10F;
        body.xRot = 0F;

        // Adjust for standing
        if (standing && !this.shuffling) {
            head.y = -6F;
            head.z = -1F;
            head.xRot = (15F / 57.29578F) + HeadXRot;
            head.yRot = netHeadYaw * (1F / 57.29578F);
            tailA.y = 9F;
            tailB.z = 18F;
            bag2.y = 5.5F;
            bag2.z = 15F;
            body.xRot = -45F / 57.29578F;
        } else if (eating && !this.shuffling) {
            head.y = 11.0F;
            head.z = -10F;
            head.xRot = 2.18166F;
            head.yRot = 0F;
        }

        // Sync head-linked parts
        for (ModelPart part : new ModelPart[]{ear1, ear2, muleEarL, muleEarR, neck, uMouth, uMouth2, lMouth, lMouth2, mane, unicorn}) {
            part.y = head.y;
            part.z = head.z;
            part.xRot = head.xRot;
            part.yRot = head.yRot;
        }

        uMouth2.xRot = uMouth2.xRot - 0.0872664F; // -5°
        lMouth2.xRot = lMouth2.xRot + 0.261799F; // +15°
        
        this.saddleMouthLine.y = head.y;
            this.saddleMouthLineR.y = head.y;
            this.headSaddle.y = head.y;
            this.saddleMouthL.y = head.y;
            this.saddleMouthR.y = head.y;

            this.saddleMouthLine.z = head.z;
            this.saddleMouthLineR.z = head.z;
            this.headSaddle.z = head.z;
            this.saddleMouthL.z = head.z;
            this.saddleMouthR.z = head.z;

            this.saddleMouthLine.xRot = HeadXRot;
            this.saddleMouthLineR.xRot = HeadXRot;
            this.headSaddle.xRot = head.xRot;
            this.saddleMouthL.xRot = head.xRot;
            this.saddleMouthR.xRot = head.xRot;
            this.headSaddle.yRot = head.yRot;
            this.saddleMouthL.yRot = head.yRot;
            this.saddleMouthLine.yRot = head.yRot;
            this.saddleMouthR.yRot = head.yRot;
            this.saddleMouthLineR.yRot = head.yRot;

        // Bags (chest)
        bag1.xRot = RLegXRot / 5F;
        bag2.xRot = -RLegXRot / 5F;

        // Wings setup
        if (wings) {
            // Sync wing X rotation with body
            innerWing.xRot = body.xRot;
            midWing.xRot   = body.xRot;
            outerWing.xRot = body.xRot;
            innerWingR.xRot = body.xRot;
            midWingR.xRot   = body.xRot;
            outerWingR.xRot = body.xRot;

            if (standing) {
                innerWing.y = -5F;
                innerWing.z = 4F;
            } else {
                innerWing.y = 3F;
                innerWing.z = -6F;
            }

            float WingRot;
            if (flapwings) {
                WingRot = Mth.cos((ageInTicks * 0.3F) + (float)Math.PI) * 1.2F;
            } else {
                WingRot = Mth.cos(limbSwing * 0.5F) * 0.1F;
            }

            if (floating) {
                outerWing.yRot = -0.3228859F + (WingRot / 2F);
                outerWingR.yRot = 0.3228859F - (WingRot / 2F);
            } else {
                WingRot = 60F / 57.29578F;
                outerWing.yRot   = -90F / 57.29578F;
                outerWingR.yRot =  90F / 57.29578F;
            }

            innerWingR.y = innerWing.y;
            innerWingR.z = innerWing.z;
            outerWing.x = innerWing.x + Mth.cos(WingRot) * 12F;
            outerWingR.x = innerWingR.x - Mth.cos(WingRot) * 12F;
            midWing.y = innerWing.y;
            midWingR.y = innerWing.y;
            outerWing.y = innerWing.y + Mth.sin(WingRot) * 12F;
            outerWingR.y = innerWingR.y + Mth.sin(WingRot) * 12F;
            midWing.z = innerWing.z;
            midWingR.z = innerWing.z;
            outerWing.z = innerWing.z;
            outerWingR.z = innerWing.z;

            midWing.zRot = WingRot;
            innerWing.zRot = WingRot;
            outerWing.zRot = WingRot;
            innerWingR.zRot = -WingRot;
            midWingR.zRot = -WingRot;
            outerWingR.zRot = -WingRot;
        }

        // Butterflies or ghost horse (type 45–59 or 21)
        if ((type > 44 && type < 60) || type == 21) {
            float f2a = ageInTicks % 100F;
            float WingRot = 0F;

            if (type != 21) {
                if (flapwings) {
                    WingRot = Mth.cos(ageInTicks * 0.9F) * 0.9F;
                } else {
                    if (floating) {
                        WingRot = Mth.cos(ageInTicks * 0.6662F) * 0.5F;
                    } else {
                        if (f2a > 40F && f2a < 60F) {
                            WingRot = Mth.cos(ageInTicks * 0.15F) * 1.20F;
                        }
                    }
                }
            } else {
                WingRot = Mth.cos(ageInTicks * 0.1F);
            }

            if (standing) {
                butterflyL.y = -2.5F;
                butterflyL.z = 6.5F;
            } else {
                butterflyL.y = 3F;
                butterflyL.z = -2F;
            }

            butterflyR.y = butterflyL.y;
            butterflyR.z = butterflyL.z;
            butterflyL.xRot = body.xRot;
            butterflyR.xRot = body.xRot;

            float baseAngle = (type == 21) ? 0F : 0.52359F;
            butterflyL.zRot = -baseAngle + WingRot;
            butterflyR.zRot = baseAngle - WingRot;
        }

        // Legs animation
        float RLegXRotB = RLegXRot;
        float LLegXRotB = LLegXRot;
        float RLegXRotC = RLegXRot;
        float LLegXRotC = LLegXRot;

        if (floating) {
            RLegXRot = 15F / 57.29578F;
            LLegXRot = RLegXRot;
            RLegXRotB = 45F / 57.29578F;
            RLegXRotC = RLegXRotB;
            LLegXRotB = RLegXRotB;
            LLegXRotC = RLegXRotB;
        }

        if (standing) {
            leg3A.y = -2F;
            leg3A.z = -2F;
            leg4A.y = leg3A.y;
            leg4A.z = leg3A.z;

            RLegXRot = (-60F / 57.29578F) + Mth.cos((ageInTicks * 0.4F) + (float)Math.PI);
            LLegXRot = (-60F / 57.29578F) + Mth.cos(ageInTicks * 0.4F);

            RLegXRotB = 45F / 57.29578F;
            LLegXRotB = RLegXRotB;

            RLegXRotC = (-15F / 57.29578F);
            LLegXRotC = (15F / 57.29578F);

            leg3B.y = leg3A.y + Mth.sin((90F / 57.29578F) + RLegXRot) * 7F;
            leg3B.z = leg3A.z + Mth.cos((270F / 57.29578F) + RLegXRot) * 7F;

            leg4B.y = leg4A.y + Mth.sin((90F / 57.29578F) + LLegXRot) * 7F;
            leg4B.z = leg4A.z + Mth.cos((270F / 57.29578F) + LLegXRot) * 7F;

            leg1B.y = leg1A.y + Mth.sin((90F / 57.29578F) + RLegXRotC) * 7F;
            leg1B.z = leg1A.z + Mth.cos((270F / 57.29578F) + RLegXRotC) * 7F;

            leg2B.y = leg1B.y;
            leg2B.z = leg1B.z;

            leg1A.xRot = RLegXRotC;
            leg1B.xRot = LLegXRotC;
            leg1C.xRot = leg1B.xRot;

            leg2A.xRot = RLegXRotC;
            leg2B.xRot = LLegXRotC;
            leg2C.xRot = leg2B.xRot;
        } else {
            leg3A.y = 9F;
            leg3A.z = -8F;
            leg4A.y = leg3A.y;
            leg4A.z = leg3A.z;

            RLegXRot = Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
            LLegXRot = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.4F * limbSwingAmount;
            RLegXRotC = Mth.cos((limbSwing * 0.6662F) + (float)Math.PI) * 0.4F * limbSwingAmount;
            LLegXRotC = Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;

            leg1A.xRot = RLegXRotC;
            leg2A.xRot = LLegXRotC;

            leg3B.y = leg3A.y + Mth.sin((90F / 57.29578F) + RLegXRot) * 7F;
            leg3B.z = leg3A.z + Mth.cos((270F / 57.29578F) + RLegXRot) * 7F;
            
            leg4B.y = leg4A.y + Mth.sin((90F / 57.29578F) + LLegXRot) * 7F;
            leg4B.z = leg4A.z + Mth.cos((270F / 57.29578F) + LLegXRot) * 7F;
            
            leg1A.y = 9F;
            leg1A.z = 11F;
            leg2A.y = leg1A.y;
            leg2A.z = leg1A.z;
            
            leg1B.y = leg1A.y + Mth.sin((90F / 57.29578F) + RLegXRotC) * 7F;
            leg1B.z = leg1A.z + Mth.cos((270F / 57.29578F) + RLegXRotC) * 7F;
            
            leg2B.y = leg2A.y + Mth.sin((90F / 57.29578F) + LLegXRotC) * 7F;
            leg2B.z = leg2A.z + Mth.cos((270F / 57.29578F) + LLegXRotC) * 7F;
        }

        leg3A.xRot = RLegXRot;
        leg3B.xRot = RLegXRotB;
        leg3C.xRot = RLegXRotB;
        leg4A.xRot = LLegXRot;
        leg4B.xRot = LLegXRotB;
        leg4C.xRot = LLegXRotB;

        // Type 60 (sprinting/shuffling) adjustments
        if (this.type == 60 && this.shuffling) {
            leg3A.y = 9F;
            leg3A.z = -8F;
            leg4A.y = leg3A.y;
            leg4A.z = leg3A.z;

            if (!floating) {
                float RLegXRot2 = Mth.cos(ageInTicks * 0.4F);
                if (RLegXRot2 > 0.1F) RLegXRot2 = 0.3F;
                float LLegXRot2 = Mth.cos((ageInTicks * 0.4F) + (float)Math.PI);
                if (LLegXRot2 > 0.1F) LLegXRot2 = 0.3F;
                RLegXRot = RLegXRot2;
                LLegXRot = LLegXRot2;
            }

            leg1B.y = leg1A.y + Mth.sin((90F / 57.29578F) + LLegXRot) * 7F;
            leg1B.z = leg1A.z + Mth.cos((270F / 57.29578F) + LLegXRot) * 7F;
            leg2B.y = leg2A.y + Mth.sin((90F / 57.29578F) + RLegXRot) * 7F;
            leg2B.z = leg2A.z + Mth.cos((270F / 57.29578F) + RLegXRot) * 7F;
            leg3B.y = leg3A.y + Mth.sin((90F / 57.29578F) + LLegXRot) * 7F;
            leg3B.z = leg3A.z + Mth.cos((270F / 57.29578F) + LLegXRot) * 7F;
            leg4B.y = leg4A.y + Mth.sin((90F / 57.29578F) + RLegXRot) * 7F;
            leg4B.z = leg4A.z + Mth.cos((270F / 57.29578F) + RLegXRot) * 7F;

            leg1A.xRot = LLegXRot;
            leg1B.xRot = LLegXRotB;
            leg1C.xRot = LLegXRotB;
            leg3A.xRot = LLegXRot;
            leg3B.xRot = LLegXRotB;
            leg3C.xRot = LLegXRotB;
            leg2A.xRot = RLegXRot;
            leg2B.xRot = RLegXRotB;
            leg2C.xRot = RLegXRotB;
            leg4A.xRot = RLegXRot;
            leg4B.xRot = RLegXRotB;
            leg4C.xRot = RLegXRotB;
        }

        // Sync lower leg parts to upper leg positions
        leg1C.y = leg1B.y;
        leg1C.z = leg1B.z;
        leg2C.y = leg2B.y;
        leg2C.z = leg2B.z;
        leg3C.y = leg3B.y;
        leg3C.z = leg3B.z;
        leg4C.y = leg4B.y;
        leg4C.z = leg4B.z;

        // Saddle adjustments
        if (saddled) {
            if (standing) {
                saddle.y = 0.5F;
                saddle.z = 11F;
            } else {
                saddle.y = 2F;
                saddle.z = 2F;
            }
            
            this.saddleB.y = saddle.y;
            this.saddleC.y = saddle.y;
            this.saddleL.y = saddle.y;
            this.saddleR.y = saddle.y;
            this.saddleL2.y = saddle.y;
            this.saddleR2.y = saddle.y;
            this.bag1.y = bag2.y;

            this.saddleB.z = saddle.z;
            this.saddleC.z = saddle.z;
            this.saddleL.z = saddle.z;
            this.saddleR.z = saddle.z;
            this.saddleL2.z = saddle.z;
            this.saddleR2.z = saddle.z;
            this.bag1.z = bag2.z;

            this.saddle.xRot = body.xRot;
            this.saddleB.xRot = body.xRot;
            this.saddleC.xRot = body.xRot;

            if (rider) {
                saddleL.xRot = -60F / 57.29578F;
                saddleL2.xRot = -60F / 57.29578F;
                saddleR.xRot = -60F / 57.29578F;
                saddleR2.xRot = -60F / 57.29578F;
                saddleL.zRot = 0F;
                saddleL2.zRot = 0F;
                saddleR.zRot = 0F;
                saddleR2.zRot = 0F;
            } else {
                saddleL.xRot = RLegXRot / 3F;
                saddleL2.xRot = RLegXRot / 3F;
                saddleR.xRot = RLegXRot / 3F;
                saddleR2.xRot = RLegXRot / 3F;
                saddleL.zRot = RLegXRot / 5F;
                saddleL2.zRot = RLegXRot / 5F;
                saddleR.zRot = -RLegXRot / 5F;
                saddleR2.zRot = -RLegXRot / 5F;
            }
        }

        // Tail swinging
        float tailMov = -1.3089F + (limbSwingAmount * 1.5F);
        if (tailMov > 0F) tailMov = 0F;
        if (moveTail) {
            tailA.yRot = Mth.cos(ageInTicks * 0.7F);
            tailMov = 0F;
        } else {
            tailA.yRot = 0F;
        }
        tailB.yRot = tailA.yRot;
        tailC.yRot = tailA.yRot;

        tailB.y = tailA.y;
        tailC.y = tailA.y;
        tailB.z = tailA.z;
        tailC.z = tailA.z;

        tailA.xRot = tailMov;
        tailB.xRot = tailMov;
        tailC.xRot = -0.2618F + tailMov;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.leg1A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg1B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg1C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg2C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg3C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4A.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4B.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.leg4C.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.uMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.uMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.lMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.unicorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ear1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ear2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.muleEarL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.muleEarR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.bag1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.bag2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.saddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleL2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleR2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleMouthL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleMouthR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleMouthLine.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.saddleMouthLineR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.headSaddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.mane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.midWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.innerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.outerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.innerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.midWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.outerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        this.butterflyL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.butterflyR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
    }
}
