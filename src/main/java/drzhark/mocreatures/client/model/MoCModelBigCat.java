package drzhark.mocreatures.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelBigCat<T extends MoCEntityBigCat> extends MoCModelAbstractBigCat<T> {

    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("mocreatures", "bigcat"), "main");

    public MoCModelBigCat(ModelPart root) {
        super(root);
    }
    
    public static LayerDefinition createBodyLayer() {
        return MoCModelAbstractBigCat.createBodyLayer();
    }

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        // store a reference so updateGhostTransparency() can read `bigcat.tFloat()`
        this.bigcat = entityIn;

        // Copy all the Boolean/float flags from the entity into our protected fields:
        this.isFlyer             = entityIn.isFlyer();
        this.isSaddled           = entityIn.getIsRideable();
        this.flapwings           = (entityIn.wingFlapCounter != 0);
        this.onAir               = entityIn.isOnAir();
        this.floating            = (this.isFlyer && this.onAir);
        this.openMouthCounter    = entityIn.mouthCounter;
        this.isRidden            = entityIn.isPassenger();
        this.hasMane             = entityIn.hasMane();
        this.isTamed             = entityIn.getHasAmulet();
        this.isSitting           = entityIn.getIsSitting();
        this.movingTail          = (entityIn.tailCounter != 0);
        this.hasSaberTeeth       = entityIn.hasSaberTeeth();
        this.hasChest            = entityIn.getIsChested();
        this.hasStinger          = entityIn.getHasStinger();
        this.isGhost             = entityIn.getIsGhost();
        this.isMovingVertically  = (entityIn.getDeltaMovement().y != 0.0F && !entityIn.onGround());
    }

    /**
     * Return the ghost‐alpha value (0…1) for rendering. Delegates to entity’s tFloat().
     */
    @Override
    public float updateGhostTransparency() {
        return bigcat.tFloat();
    }
}
