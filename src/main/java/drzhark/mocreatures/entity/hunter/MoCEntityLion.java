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

public class MoCEntityLion extends MoCEntityBigCat {

    public MoCEntityLion(EntityType<? extends MoCEntityLion> type, Level world) {
        super(type, world);
        // TODO: Separate hitbox for the lioness
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBigCat.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            if (random.nextInt(20) == 0) {
                setTypeMoC(random.nextInt(2) + 6); // White Lion
            } else {
                setTypeMoC(random.nextInt(2) + 1);
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
                case 6:
                case 7:
                case 8:
                    return MoCreatures.proxy.getModelTexture("big_cat_white_lion_legacy.png");
                default:
                    return MoCreatures.proxy.getModelTexture("big_cat_lion_legacy.png");
            }
        }
        switch (getTypeMoC()) {
            case 2:
            case 3:
                return MoCreatures.proxy.getModelTexture("big_cat_lion_male.png");
            case 6:
            case 7:
            case 8:
                return MoCreatures.proxy.getModelTexture("big_cat_white_lion.png");
            default:
                return MoCreatures.proxy.getModelTexture("big_cat_lion_female.png");
        }
    }

    @Override
    public boolean hasMane() {
        // Only adult male lions should have manes
        // Types 2, 3, 7, and 8 are male lions, including winged white lions
        // Add extra validation to prevent synchronization issues
        return this.getIsAdult() 
            && this.getMoCAge() >= (this.getMoCMaxAge() * 0.8) // At least 80% of max age
            && (this.getTypeMoC() == 2 || this.getTypeMoC() == 3 || this.getTypeMoC() == 7 || this.getTypeMoC() == 8);
    }

    @Override
    public boolean isFlyer() {
        return this.getTypeMoC() == 3 || this.getTypeMoC() == 5 || this.getTypeMoC() == 8;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && (getTypeMoC() == 2 || getTypeMoC() == 7) && (stack.getItem() == MoCItems.ESSENCE_LIGHT.get())) {
            if (!player.isCreative()) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            setTypeMoC(getTypeMoC() + 1);
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
        return MoCLootTables.LION;
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() < 3) {
            return "Liger"; // Liger
        }
        if (getTypeMoC() == 2 && mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return "Liard"; // Liard
        }
        if (getTypeMoC() == 2 && mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return "Lither"; // Lither
        }
        return "Lion";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        int x = 0;
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() < 3) {
            return 1; // Liger
        }
        if (getTypeMoC() == 2 && mate instanceof MoCEntityLeopard && mate.getTypeMoC() == 1) {
            return 1; // Liard
        }
        if (getTypeMoC() == 2 && mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return 1; // Lither
        }
        if (mate instanceof MoCEntityLion) {
            int lionMateType = mate.getTypeMoC();
            if (this.getTypeMoC() == 1 && lionMateType == 2) {
                x = this.random.nextInt(2) + 1;
            }
            if (this.getTypeMoC() == 2 && lionMateType == 1) {
                x = this.random.nextInt(2) + 1;
            }
            if (this.getTypeMoC() == 6 && lionMateType == 7) {
                x = this.random.nextInt(2) + 6;
            }
            if (this.getTypeMoC() == 7 && lionMateType == 6) {
                x = this.random.nextInt(2) + 6;
            }
            if (this.getTypeMoC() == 7 && lionMateType == 1) {
                x = this.random.nextInt(2) + 1;
            }
            if (this.getTypeMoC() == 6 && lionMateType == 2) {
                x = this.random.nextInt(2) + 1;
            }
            if (this.getTypeMoC() == 1 && lionMateType == 7) {
                x = this.random.nextInt(2) + 1;
            }
            if (this.getTypeMoC() == 2 && lionMateType == 6) {
                x = this.random.nextInt(2) + 1;
            }
        }
        return x;
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        if (this.getTypeMoC() == 2 && mate instanceof MoCEntityTiger && ((MoCEntityTiger) mate).getTypeMoC() < 3) {
            return true;
        }
        if (this.getTypeMoC() == 2 && mate instanceof MoCEntityLeopard && ((MoCEntityLeopard) mate).getTypeMoC() == 1) {
            return true;
        }
        if (this.getTypeMoC() == 2 && mate instanceof MoCEntityPanther && ((MoCEntityPanther) mate).getTypeMoC() == 1) {
            return true;
        }
        if (mate instanceof MoCEntityLion) {
            return (getOffspringTypeInt((MoCEntityLion) mate) != 0);
        }
        return false;
    }

    @Override
    public boolean readytoBreed() {
        return (this.getTypeMoC() < 3 || this.getTypeMoC() == 6 || this.getTypeMoC() == 7) && super.readytoBreed();
    }

    public float calculateMaxHealth() {
        // ?
        if (this.getTypeMoC() == 2 || this.getTypeMoC() == 7) {
            return 35F;
        }
        // ?
        if (this.getTypeMoC() == 4) {
            return 40F;
        }
        // ?
        return 30F;
    }

    @Override
    public int getMoCMaxAge() {
        // ?
        if (getTypeMoC() == 1 || getTypeMoC() == 6) {
            return 110;
        }
        // ?
        if (getTypeMoC() == 9) {
            return 100;
        }
        // ?
        return 120;
    }

    public double calculateAttackDmg() {
        // White Lion
        if (this.getTypeMoC() == 6 || this.getTypeMoC() == 7 || this.getTypeMoC() == 8) {
            return 7.5D;
        }
        // Lion
        return 7.0D;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        if (!this.getIsAdult() && (this.getMoCAge() < this.getMoCMaxAge() * 0.8)) {
            return false;
        }
        if (entity instanceof MoCEntityLion) {
            return false;
        }
        return entity.getBbHeight() < 2F && entity.getBbWidth() < 2F;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.92F;
    }
}
