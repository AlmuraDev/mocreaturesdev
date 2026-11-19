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

public class MoCEntityTiger extends MoCEntityBigCat {

    public MoCEntityTiger(EntityType<? extends MoCEntityTiger> type, Level world) {
        super(type, world);
        //setSize(1.25F, 1.275F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBigCat.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            if (this.random.nextInt(20) == 0) {
                setTypeMoC(2);
            } else {
                setTypeMoC(1);
            }
        }
        super.selectType();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculateMaxHealth());
        this.setHealth(getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(calculateAttackDmg());
    }

    @Override
    public Identifier getTexture() {
        if (MoCreatures.proxy.legacyBigCatModels) {
            switch (getTypeMoC()) {
                case 2:
                case 3:
                    return MoCreatures.proxy.getModelTexture("big_cat_white_tiger_legacy.png");
                default:
                    return MoCreatures.proxy.getModelTexture("big_cat_tiger_legacy.png");
            }
        }
        switch (getTypeMoC()) {
            case 2: // White Tiger
            case 3: // Winged White Tiger
                return MoCreatures.proxy.getModelTexture("big_cat_white_tiger.png");
            default: // Orange Tiger
                return MoCreatures.proxy.getModelTexture("big_cat_tiger.png");
        }
    }

    @Override
    public boolean isFlyer() {
        return this.getTypeMoC() == 3;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && getTypeMoC() == 2 && (stack.is(MoCItems.ESSENCE_LIGHT.get()))) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            setTypeMoC(3);
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
        return MoCLootTables.TIGER;
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return "Liger";
        }
        if (mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return "Panthger";
        }
        if (mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return "Leoger";
        }
        return "Tiger";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return 1; // Liger
        }
        if (mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return 1; // Leoger
        }
        if (mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return 1; // Panthger
        }
        return this.getTypeMoC();
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return (mate instanceof MoCEntityTiger && ((MoCEntityTiger) mate).getTypeMoC() < 3)
                || (mate instanceof MoCEntityLion && ((MoCEntityLion) mate).getTypeMoC() == 2)
                || (mate instanceof MoCEntityLeopard && ((MoCEntityLeopard) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityPanther && ((MoCEntityPanther) mate).getTypeMoC() == 1);
    }

    @Override
    public boolean readytoBreed() {
        return this.getTypeMoC() < 3 && super.readytoBreed();
    }

    public double calculateMaxHealth() {
        // White Tiger
        if (this.getTypeMoC() == 2 || this.getTypeMoC() == 3) {
            return 40.0D;
        }
        // Orange Tiger
        else {
            return 35.0D;
        }
    }

    public double calculateAttackDmg() {
        // White Tiger
        if (this.getTypeMoC() == 2 || this.getTypeMoC() == 3) {
            return 7.5D;
        }
        // Orange Tiger
        return 7.0D;
    }

    @Override
    public int getMoCMaxAge() {
        // White Tiger
        if (this.getTypeMoC() == 2 || this.getTypeMoC() == 3) {
            return 130;
        }
        // Orange Tiger
        return 120;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        if (!this.getIsAdult() && (this.getMoCAge() < this.getMoCMaxAge() * 0.8)) {
            return false;
        }
        if (entity instanceof MoCEntityTiger) {
            return false;
        }
        return entity.getBbHeight() < 2F && entity.getBbWidth() < 2F;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.92F;
    }
}
