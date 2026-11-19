/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */

package drzhark.mocreatures.client.model;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import net.minecraft.util.ARGB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class MoCModelHorse<T extends MoCEntityHorse> extends MoCModelAbstractHorse<T> {

    // Model parts
    protected ModelPart headSaddle;
    protected ModelPart saddle;
    protected ModelPart saddleB;
    protected ModelPart saddleC;
    protected ModelPart saddleL;
    protected ModelPart saddleL2;
    protected ModelPart saddleR;
    protected ModelPart saddleR2;
    protected ModelPart saddleMouthL;
    protected ModelPart saddleMouthR;
    protected ModelPart saddleMouthLine;
    protected ModelPart saddleMouthLineR;
    protected ModelPart muleEarL;
    protected ModelPart muleEarR;
    protected ModelPart ear1;
    protected ModelPart ear2;
    protected ModelPart neck;
    protected ModelPart head;
    protected ModelPart uMouth;
    protected ModelPart lMouth;
    protected ModelPart uMouth2;
    protected ModelPart lMouth2;
    protected ModelPart mane;
    protected ModelPart body;
    protected ModelPart tailA;
    protected ModelPart tailB;
    protected ModelPart tailC;
    protected ModelPart leg1A;
    protected ModelPart leg1B;
    protected ModelPart leg1C;
    protected ModelPart leg2A;
    protected ModelPart leg2B;
    protected ModelPart leg2C;
    protected ModelPart leg3A;
    protected ModelPart leg3B;
    protected ModelPart leg3C;
    protected ModelPart leg4A;
    protected ModelPart leg4B;
    protected ModelPart leg4C;
    protected ModelPart unicorn;
    protected ModelPart bag1;
    protected ModelPart bag2;
    protected ModelPart midWing;
    protected ModelPart innerWing;
    protected ModelPart outerWing;
    protected ModelPart innerWingR;
    protected ModelPart midWingR;
    protected ModelPart outerWingR;
    protected ModelPart butterflyL;
    protected ModelPart butterflyR;
    
    private boolean chested;
    private boolean flyer;
    private boolean isGhost;
    private boolean isUnicorned;
    private float transparency;
    private int vanishingInt;
    private int wingflapInt;
    private boolean openMouth;
    
    public MoCModelHorse(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        
        // Initialize model parts from the provided root
        this.headSaddle = root.getChild("headSaddle");
        this.saddle = root.getChild("saddle");
        this.saddleB = root.getChild("saddleB");
        this.saddleC = root.getChild("saddleC");
        this.saddleL = root.getChild("saddleL");
        this.saddleL2 = root.getChild("saddleL2");
        this.saddleR = root.getChild("saddleR");
        this.saddleR2 = root.getChild("saddleR2");
        this.saddleMouthL = root.getChild("saddleMouthL");
        this.saddleMouthR = root.getChild("saddleMouthR");
        this.saddleMouthLine = root.getChild("saddleMouthLine");
        this.saddleMouthLineR = root.getChild("saddleMouthLineR");
        this.muleEarL = root.getChild("muleEarL");
        this.muleEarR = root.getChild("muleEarR");
        this.ear1 = root.getChild("ear1");
        this.ear2 = root.getChild("ear2");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.uMouth = root.getChild("uMouth");
        this.lMouth = root.getChild("lMouth");
        this.uMouth2 = root.getChild("uMouth2");
        this.lMouth2 = root.getChild("lMouth2");
        this.mane = root.getChild("mane");
        this.body = root.getChild("body");
        this.tailA = root.getChild("tailA");
        this.tailB = root.getChild("tailB");
        this.tailC = root.getChild("tailC");
        this.leg1A = root.getChild("leg1A");
        this.leg1B = root.getChild("leg1B");
        this.leg1C = root.getChild("leg1C");
        this.leg2A = root.getChild("leg2A");
        this.leg2B = root.getChild("leg2B");
        this.leg2C = root.getChild("leg2C");
        this.leg3A = root.getChild("leg3A");
        this.leg3B = root.getChild("leg3B");
        this.leg3C = root.getChild("leg3C");
        this.leg4A = root.getChild("leg4A");
        this.leg4B = root.getChild("leg4B");
        this.leg4C = root.getChild("leg4C");
        this.unicorn = root.getChild("unicorn");
        this.bag1 = root.getChild("bag1");
        this.bag2 = root.getChild("bag2");
        this.midWing = root.getChild("midWing");
        this.innerWing = root.getChild("innerWing");
        this.outerWing = root.getChild("outerWing");
        this.innerWingR = root.getChild("innerWingR");
        this.midWingR = root.getChild("midWingR");
        this.outerWingR = root.getChild("outerWingR");
        this.butterflyL = root.getChild("butterflyL");
        this.butterflyR = root.getChild("butterflyR");
    }

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        type = entityIn.getTypeMoC();
        vanishingInt = entityIn.getVanishC();
        wingflapInt = entityIn.wingFlapCounter;
        saddled = entityIn.getIsRideable();
        openMouth = (entityIn.mouthCounter != 0);
        rider = entityIn.isVehicle();
        chested = entityIn.getIsChested();
        flyer = entityIn.isFlyer();
        isGhost = entityIn.getIsGhost();
        isUnicorned = entityIn.isUnicorned();
        if (vanishingInt != 0)
        {
            transparency = 1.0F - (((float) (vanishingInt)) / 100);
        } else {
            transparency = entityIn.tFloat();
        }
        flapwings = (entityIn.wingFlapCounter != 0);
        shuffling = (entityIn.shuffleCounter != 0);
        wings = (entityIn.isFlyer() && !entityIn.getIsGhost() && type < 45);
        eating = entityIn.getIsSitting();
        standing = (entityIn.standCounter != 0 && entityIn.getVehicle() == null);
        moveTail = (entityIn.tailCounter != 0);
        
        // TODO: THIS IS STILL BROKEN TO AN EXTENT. NOTED IN THE KNOWN ISSUES SECTION.

        // Fix: Only set floating when the entity is actually flying or is a ghost
        // We need to check if the entity is actively flying, not just if it's not on the ground
        // For ghosts, set floating to true always - they should float even on the ground
        if (entityIn.getIsGhost()) {
            floating = true; // Ghost horses should always have floating=true
        } else if (entityIn.isFlyer()) {
            // For flying horses, check if they're really in the air, not just briefly stepping off a block
            // We can use either vertical movement or fall distance to determine if it's flying
            // If it has a rider, we don't want to use fall distance as a criterion
            if (entityIn.isVehicle()) {
                floating = !entityIn.onGround();
            } else {
                // When not ridden, we need to make sure it's actually flying and not just stepping off a block
                floating = !entityIn.onGround() && (
                    Math.abs(entityIn.getDeltaMovement().y) > 0.03D || // Vertical movement
                    entityIn.fallDistance > 0.5F // Some fall distance (not just stepping off)
                );
            }
        } else {
            floating = false;
        }
        
        // Force floating to false when the horse is standing on a block, EXCEPT for ghost horses
        if (entityIn.onGround() && !entityIn.getIsGhost()) {
            floating = false;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isGhost && vanishingInt == 0) {
            if (saddled) {
                this.headSaddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleL2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleR2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleMouthL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleMouthR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                if (rider) {
                    this.saddleMouthLine.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                    this.saddleMouthLineR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                }
            }

            if (type == 65 || type == 66 || type == 67) //mule, donkey or zonkey
            {
                this.muleEarL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.muleEarR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            } else {
                this.ear1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.ear2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            if (openMouth) {
                this.uMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            } else {
                this.uMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            this.mane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
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

            if (isUnicorned) {
                this.unicorn.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }

            if (chested) {
                this.bag1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.bag2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }

            if (flyer && type < 45) { //pegasus
                this.midWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.innerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.outerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.innerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.midWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.outerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            } else if (type > 44 && type < 60) { //fairys
                poseStack.pushPose();
                float transparencyVal = 0.7F;
                poseStack.scale(1.3F, 1.0F, 1.3F);
                this.butterflyL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.butterflyR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                poseStack.popPose();
            }
        } else
        //rendering a ghost or vanishing
        {
            poseStack.pushPose();
            poseStack.scale(1.3F, 1.0F, 1.3F);

            this.ear1.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.ear2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));

            this.neck.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            this.head.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            if (openMouth) {
                this.uMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lMouth2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            } else {
                this.uMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.lMouth.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }

            this.mane.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
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

            if (type == 39 || type == 40 || type == 28) {
                this.midWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.innerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.outerWing.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.innerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.midWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.outerWingR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }
            if (type >= 50 && type < 60) {
                this.butterflyL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.butterflyR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
            }

            if (saddled) {
                this.headSaddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddle.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleB.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleC.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleL2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleR2.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleMouthL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.saddleMouthR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                if (rider) {
                    this.saddleMouthLine.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                    this.saddleMouthLineR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                }
            }

            poseStack.popPose();

            if (type == 21 || type == 22)//|| (type >=50 && type <60))
            {
                float wingTransparency = 0F;
                if (wingflapInt != 0) {
                    wingTransparency = 1F - (((float) wingflapInt) / 25);
                }
                if (wingTransparency > transparency) {
                    wingTransparency = transparency;
                }
                poseStack.pushPose();
                poseStack.scale(1.3F, 1.0F, 1.3F);
                this.butterflyL.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                this.butterflyR.render(poseStack, buffer, packedLight, packedOverlay, ARGB.colorFromFloat(alpha, red, green, blue));
                poseStack.popPose();
            }
        }
    }

    public static LayerDefinition createBodyLayer() {
        return MoCModelAbstractHorse.createBodyLayer();
    }
}
