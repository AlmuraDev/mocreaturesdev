/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.model;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hostile.MoCEntityHorseMob;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of MoCModelHorseMob (1.16.5) to Minecraft 1.20.1.
 * Extends MoCModelAbstractHorse, reusing its horse geometry.
 */
@OnlyIn(Dist.CLIENT)
public class MoCModelHorseMob<T extends MoCEntityHorseMob> extends MoCModelAbstractHorse<T> {

    // Reuse the same layer location as the regular horse
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("mocreatures", "horse"), "main"
    );

    // Dynamic state fields, set each tick in setupAnim
    private int typeMob;
    private boolean openMouth;
    private boolean flyer;
    private boolean isUnicorned;

    public MoCModelHorseMob(ModelPart root) {
        super(root);
    }

    /**
     * Copies entity state (typeMob, openMouth, flyer, isUnicorned, etc.) from MoCEntityHorseMob
     * into model fields. Called before animations are applied.
     */
    @Override
    public void setupAnim(
            T entityIn,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        // Always treat as base type 0 (hostile variant)
        type = 0;
        typeMob     = entityIn.getTypeMoC();
        wings = entityIn.isFlyer();
        flyer       = entityIn.isFlyer();
        eating      = (entityIn.eatingCounter != 0);
        standing    = (entityIn.standCounter != 0 && entityIn.getVehicle() == null);
        openMouth   = (entityIn.mouthCounter != 0);
        moveTail    = (entityIn.tailCounter != 0);
        flapwings   = (entityIn.wingFlapCounter != 0);
        rider       = entityIn.isPassenger();
        floating    = (entityIn.isFlyer() && entityIn.isOnAir());
        isUnicorned = entityIn.isUnicorned();
        saddled     = false;
        shuffling   = false;

        // Delegate to parent for head/neck/leg/tail animations
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /**
     * Renders the hostile horse model without saddles or chest bags.
     * Omits any saddle-specific parts and adjusts for hostile-only features.
     */
    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        // Ears (always render)
        this.ear1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.ear2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Neck and head
        this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Mouth (open or closed)
        if (openMouth) {
            this.uMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        } else {
            this.uMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.lMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Mane, body, and tail segments
        this.mane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.body.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailA.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        this.tailC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

        // Legs (each in three segments)
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

        // Unicorn horn (if applicable)
        if (isUnicorned) {
            this.unicorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }

        // Pegasus wings (only if flyer and not specific mob types 34 or 36)
        if (flyer && typeMob != 34 && typeMob != 36) {
            this.midWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.innerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.outerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.innerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.midWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.outerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
        }
    }

    /**
     * Since MoCModelHorseMob reuses the same geometry as a regular horse,
     * simply return the LayerDefinition from MoCModelAbstractHorse.
     */
    public static LayerDefinition createBodyLayer() {
        return MoCModelAbstractHorse.createBodyLayer();
    }
}
