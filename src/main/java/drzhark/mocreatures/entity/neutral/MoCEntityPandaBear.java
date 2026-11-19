/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.hunter.MoCEntityBear;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityPandaBear extends MoCEntityBear {

    public MoCEntityPandaBear(EntityType<? extends MoCEntityPandaBear> type, Level world) {
        super(type, world);
        //setSize(0.8F, 1.05F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBear.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
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
        return MoCreatures.proxy.getModelTexture("bear_panda.png");
    }

    @Override
    public float getBearSize() {
        return 0.8F;
    }

    @Override
    public int getMoCMaxAge() {
        return 80;
    }

    @Override
    public boolean isReadyToHunt() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        return super.hurtServer(serverLevel, damagesource, i);
    }

    @Override
    public boolean shouldAttackPlayers() {
        return false;
    }

    @Override
    public boolean isMyFavoriteFood(ItemStack stack) {
        return this.getTypeMoC() == 3 && !stack.isEmpty() && stack.is(Items.SUGAR_CANE);
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return this.getTypeMoC() == 3 && !stack.isEmpty() && stack.is(Items.SUGAR_CANE);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && (stack.is(MoCItems.SUGAR_LUMP.get()) || stack.is(Items.SUGAR_CANE))) {
            if (!player.getAbilities().instabuild) stack.shrink(1);

            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }

            this.setHealth(getMaxHealth());
            eatingAnimal();
            if (!this.level().isClientSide() && !getIsAdult() && (getMoCAge() < 100)) {
                setMoCAge(getMoCAge() + 1);
            }

            return InteractionResult.SUCCESS;
        }
        if (!stack.isEmpty() && getIsTamed() && stack.is(MoCItems.WHIP.get())) {
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
        return MoCLootTables.PANDA_BEAR;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        /*
         * panda bears and cubs will sit down sometimes
         */
        if (!this.level().isClientSide() && !getIsTamed() && this.random.nextInt(300) == 0) {
            setBearState(2);
        }
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        return "PandaBear";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        return 1;
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return mate instanceof MoCEntityPandaBear;
    }

    // TODO: Change depending on whether it's sitting or not
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.76F;
    }
}
