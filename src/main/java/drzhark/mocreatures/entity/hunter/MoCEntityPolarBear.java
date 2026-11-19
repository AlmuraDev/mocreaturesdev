/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityPolarBear extends MoCEntityBear {

    public MoCEntityPolarBear(EntityType<? extends MoCEntityPolarBear> type, Level world) {
        super(type, world);
        //setSize(1.5F, 1.834F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBear.createAttributes()
                .add(Attributes.MAX_HEALTH, 45.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(1);
        }
        super.selectType();
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("bear_polar.png");
    }

    @Override
    public float getBearSize() {
        return 1.4F;
    }

    @Override
    public int getMoCMaxAge() {
        return 140;
    }

    public double getAttackRange() {
        int factor = 1;
        if (this.level().getDifficulty().getId() > 1) {
            factor = 2;
        }
        return 8D * factor;
    }

    /*public int getAttackStrength() {
        int factor = (this.level().getDifficulty().getId());
        return 4 * factor;
    }*/

    @Override
    public boolean shouldAttackPlayers() {
        return super.shouldAttackPlayers();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && this.getMoCAge() < 80 && MoCTools.isItemEdibleForCarnivores(stack.getItem())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);

            if (!getIsTamed() && !this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }

            this.setHealth(getMaxHealth());
            eatingAnimal();
            if (!this.level().isClientSide() && !getIsAdult() && (getMoCAge() < 100)) {
                setMoCAge(getMoCAge() + 1);
            }

            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getIsTamed() && (stack.is(MoCItems.WHIP.get()))) {
            if (getBearState() == 0) {
                setBearState(2);
            } else {
                setBearState(0);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isShiftKeyDown()) && !this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setBearState(0);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.POLAR_BEAR;
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        return "WildPolarBear";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        return 1;
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return mate instanceof MoCEntityPolarBear;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.76F;
    }
}
