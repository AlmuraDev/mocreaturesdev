/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityPanther extends MoCEntityBigCat {

    public MoCEntityPanther(EntityType<? extends MoCEntityPanther> type, Level world) {
        super(type, world);
        //setSize(1.175F, 1.065F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBigCat.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public Identifier getTexture() {
        if (MoCreatures.proxy.legacyBigCatModels)
            return MoCreatures.proxy.getModelTexture("big_cat_panther_legacy.png");
        return MoCreatures.proxy.getModelTexture("big_cat_panther.png");
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(1);
        }
        super.selectType();
    }

    @Override
    public boolean isFlyer() {
        return this.getTypeMoC() == 2;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && getTypeMoC() == 1 && (stack.is(MoCItems.ESSENCE_DARKNESS.get()))) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            setTypeMoC(2);
            return InteractionResult.SUCCESS;
        }
        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isShiftKeyDown()) && !this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setSitting(false);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.PANTHER;
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        if (mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return "Panthard";//3; //panthard
        }
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() == 1) {
            return "Panthger";//4; //panthger
        }
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return "Lither";//5; //lither
        }

        return "Panther";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        if (mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return 1;//3; //panthard
        }
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() == 1) {
            return 1;//4; //panthger
        }
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return 1;//5; //lither
        }
        return 1;
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return (mate instanceof MoCEntityLeopard && ((MoCEntityLeopard) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityPanther && ((MoCEntityPanther) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityTiger && ((MoCEntityTiger) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityLion && ((MoCEntityLion) mate).getTypeMoC() == 2);
    }

    @Override
    public int getMoCMaxAge() {
        if (getTypeMoC() >= 4) return 110;
        return 100;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        if (!this.getIsAdult() && (this.getMoCAge() < this.getMoCMaxAge() * 0.8)) {
            return false;
        }
        if (entity instanceof MoCEntityPanther) {
            return false;
        }
        return entity.getBbHeight() < 1.5F && entity.getBbWidth() < 1.5F;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.92F;
    }
}
