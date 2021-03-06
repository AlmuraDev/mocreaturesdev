/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCTameable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MoCEntityManticorePet extends MoCEntityBigCat {

    public MoCEntityManticorePet(World world) {
        super(world);
        this.chestName = "ManticoreChest";
    }

    @Override
    public void selectType() {

        if (getType() == 0) {
            setType(this.rand.nextInt(4) + 1);
        }
        super.selectType();
    }

    @Override
    public ResourceLocation getTexture() {
        switch (getType()) {
            case 1:
                return MoCreatures.proxy.getTexture("bcmanticore.png");
            case 2:
                return MoCreatures.proxy.getTexture("bcmanticoredark.png");
            case 3:
                return MoCreatures.proxy.getTexture("bcmanticoreblue.png");
            case 4:
                return MoCreatures.proxy.getTexture("bcmanticoregreen.png");
            default:
                return MoCreatures.proxy.getTexture("bcmanticore.png");
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
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        final Boolean tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isSneaking()) && !this.isBeingRidden()) {
            if (!this.world.isRemote && player.startRiding(this)) {
                player.rotationYaw = this.rotationYaw;
                player.rotationPitch = this.rotationPitch;
                setSitting(false);
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        return "";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        return 0;
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
    public float calculateMaxHealth() {
        return 40F;
    }

    @Override
    public double calculateAttackDmg() {
        return 7D;
    }

    @Override
    public double getAttackRange() {
        return 8D;
    }

    @Override
    public int getMaxEdad() {
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

}
