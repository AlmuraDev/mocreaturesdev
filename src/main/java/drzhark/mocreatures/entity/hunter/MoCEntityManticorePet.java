/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityManticorePet extends MoCEntityBigCat {

    public MoCEntityManticorePet(EntityType<? extends MoCEntityManticorePet> type, Level world) {
        super(type, world);
        this.chestName = "ManticoreChest";
    }

    // TODO: Varied stats depending on type
    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBigCat.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(4) + 1);
        }
        super.selectType();
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("manticore_dark.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("manticore_frost.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("manticore_toxic.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("manticore_plain.png");
            default:
                return MoCreatures.proxy.getModelTexture("manticore_fire.png");
        }
    }

    @Override
    public boolean hasMane() {
        return true;
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isShiftKeyDown()) && !this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setSitting(false);
            }

            return InteractionResult.SUCCESS;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getIsTamed() && (stack.is(MoCItems.WHIP.get()))) {
            setSitting(!getIsSitting());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return false;
    }

    @Override
    public boolean readytoBreed() {
        return false;
    }

    @Override
    public int getMoCMaxAge() {
        return 130;
    }

    @Override
    public boolean getHasStinger() {
        return true;
    }

    @Override
    public boolean hasSaberTeeth() {
        return true;
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        switch (getTypeMoC()) {
            case 2:
                return MoCLootTables.DARK_MANTICORE;
            case 3:
                return MoCLootTables.FROST_MANTICORE;
            case 4:
                return MoCLootTables.TOXIC_MANTICORE;
            case 5:
                return MoCLootTables.PLAIN_MANTICORE;
            default:
                return MoCLootTables.FIRE_MANTICORE;
        }
    }
}
