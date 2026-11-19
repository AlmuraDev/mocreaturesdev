/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;
import net.minecraft.tags.BlockItemTags;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityInsect;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityButterfly extends MoCEntityInsect {

    private int fCounter;

    public MoCEntityButterfly(EntityType<? extends MoCEntityButterfly> type, Level world) {
        super(type, world);
    }

    /**
     * Register attributes for this entity
     */
    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityInsect.registerAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.12D);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(10) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("butterfly_agalais_urticae.png");
            case 2:
                return MoCreatures.proxy.getModelTexture("butterfly_argyreus_hyperbius.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("butterfly_athyma_nefte.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("butterfly_catopsilia_pomona.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("butterfly_morpho_peleides.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("butterfly_vanessa_atalanta.png");
            case 8:
                return MoCreatures.proxy.getModelTexture("moth_camptogramma_bilineata.png");
            case 9:
                return MoCreatures.proxy.getModelTexture("moth_idia_aemula.png");
            case 10:
                return MoCreatures.proxy.getModelTexture("moth_thyatira_batis.png");
            default:
                return MoCreatures.proxy.getModelTexture("butterfly_pieris_rapae.png");
        }
    }

    public float tFloat() {
        if (!getIsFlying()) {
            return 0F;
        }
        if (++this.fCounter > 1000) {
            this.fCounter = 0;
        }

        return Mth.cos((this.fCounter * 0.1F)) * 0.2F;
    }

    @Override
    public float getSizeFactor() {
        if (getTypeMoC() < 8) {
            return 0.7F;
        }
        return 1.0F;
    }

    @Override
    public boolean isMyFavoriteFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(BlockItemTags.FLOWERS.item());
    }

    @Override
    public boolean isAttractedToLight() {
        return getTypeMoC() > 7;
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public float getSpeed() {
        if (getIsFlying()) {
            return 0.13F;
        }
        return 0.10F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BUTTERFLY;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (0.1F));
    }
    
    @Override
    public boolean shouldRenderTransparent() {
        return true; // Butterflies have transparent wings when flying
    }
}
