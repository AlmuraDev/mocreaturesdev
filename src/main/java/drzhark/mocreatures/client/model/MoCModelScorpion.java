/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 *
 * Ported to Minecraft 1.20.1:
 *  • Replace setLivingAnimations(...) with prepareMobModel(...)
 *  • No ModelRenderer → ModelPart changes here, since all parts live in MoCModelAbstractScorpion
 */

package drzhark.mocreatures.client.model;

import drzhark.mocreatures.entity.hostile.MoCEntityScorpion;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCModelScorpion<T extends MoCEntityScorpion> extends MoCModelAbstractScorpion<T> {

    public MoCModelScorpion(ModelPart root) {
        super(root);
    }

    /**
     * In 1.20.1, living‐animation logic belongs in prepareMobModel(...) rather than setLivingAnimations(...).
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        // Tail “poisoning” animation flag
        poisoning = entity.swingingTail();
        // Adult vs. baby
        isAdult   = entity.getIsAdult();
        // “Talking” (mouthCounter > 0) flag
        isTalking = entity.mouthCounter != 0;
        // Has babies?
        babies    = entity.getHasBabies();
        // Arm‐swing attack state
        attacking = entity.armCounter;
    }
}
