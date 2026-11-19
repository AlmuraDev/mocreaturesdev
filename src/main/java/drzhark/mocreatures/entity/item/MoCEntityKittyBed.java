/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.neutral.MoCEntityKitty;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.HitResult;

public class MoCEntityKittyBed extends Mob {

    private static final EntityDataAccessor<Boolean> HAS_MILK = SynchedEntityData.defineId(MoCEntityKittyBed.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_FOOD = SynchedEntityData.defineId(MoCEntityKittyBed.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PICKED_UP = SynchedEntityData.defineId(MoCEntityKittyBed.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHEET_COLOR = SynchedEntityData.defineId(MoCEntityKittyBed.class, EntityDataSerializers.INT);
    public float milkLevel;

    public MoCEntityKittyBed(EntityType<? extends MoCEntityKittyBed> type, Level world) {
        super(type, world);
        setNoAi(true);
        this.milkLevel = 0.0F;
    }

    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("kitty_bed.png");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_MILK, false);
        builder.define(HAS_FOOD, false);
        builder.define(PICKED_UP, false);
        builder.define(SHEET_COLOR, 0);
    }

    public boolean getHasFood() {
        return this.entityData.get(HAS_FOOD);
    }

    public void setHasFood(boolean flag) {
        this.entityData.set(HAS_FOOD, flag);
    }

    public boolean getHasMilk() {
        return this.entityData.get(HAS_MILK);
    }

    public void setHasMilk(boolean flag) {
        this.entityData.set(HAS_MILK, flag);
    }

    public boolean getPickedUp() {
        return this.entityData.get(PICKED_UP);
    }

    public void setPickedUp(boolean flag) {
        this.entityData.set(PICKED_UP, flag);
    }

    public int getSheetColor() {
        return this.entityData.get(SHEET_COLOR);
    }

    public void setSheetColor(int i) {
        this.entityData.set(SHEET_COLOR, i);
    }

    @Override
    public boolean isPushable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return this.level().clip(new ClipContext(
            new Vec3(this.getX(), this.getY() + getEyeHeight(), this.getZ()),
            new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()),
            ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE,
            this
        )).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.0F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0D;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() && this.getVehicle() == null) {
            if (!this.level().isClientSide()) {
                giveOrDrop(player, new ItemStack(MoCItems.KITTYBED[getSheetColor()].get()));
                if (getHasFood()) giveOrDrop(player, new ItemStack(MoCItems.PET_FOOD.get()));
                else if (getHasMilk()) giveOrDrop(player, new ItemStack(Items.MILK_BUCKET));
                MoCTools.playCustomSound(this, SoundEvents.ITEM_PICKUP, 0.2F);
                this.discard();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!getHasFood() && !getHasMilk()) {
            if (stack.is(MoCItems.PET_FOOD.get())) {
                if (!this.level().isClientSide()) {
                    if (!player.isCreative()) stack.shrink(1);
                    setHasFood(true);
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_KITTYBED_POURINGFOOD.get());
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (stack.is(Items.MILK_BUCKET)) {
                if (!this.level().isClientSide()) {
                    if (!player.isCreative()) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    setHasMilk(true);
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_KITTYBED_POURINGMILK.get());
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        if (this.getVehicle() == null) {
            if (!this.level().isClientSide()) {
                setYHeadRot((float) MoCTools.roundToNearest90Degrees(this.getYHeadRot()) + 90.0F);
                MoCTools.playCustomSound(this, SoundEvents.ITEM_FRAME_ROTATE_ITEM);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    private void giveOrDrop(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) player.drop(stack, false);
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        if (!this.level().isClientSide() && (this.getVehicle() != null || !this.onGround() || !MoCreatures.proxy.staticBed)) {
            super.move(type, pos);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround()) {
            setPickedUp(false);
        }
        if (!this.level().isClientSide() && (getHasMilk() || getHasFood()) && this.isVehicle() && getFirstPassenger() instanceof MoCEntityKitty) {
            MoCEntityKitty kitty = (MoCEntityKitty) getFirstPassenger();
            if (kitty.getKittyState() != 12) {
                this.milkLevel += 0.003F;
                if (this.milkLevel > 2.0F) {
                    this.milkLevel = 0.0F;
                    setHasMilk(false);
                    setHasFood(false);
                }
            }
        }
        if (this.isPassenger()) MoCTools.dismountSneakingPlayer(this);
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        setHasMilk(compound.getBooleanOr("HasMilk", false));
        setSheetColor(compound.getIntOr("SheetColour", 0));
        setHasFood(compound.getBooleanOr("HasFood", false));
        this.milkLevel = compound.getFloatOr("MilkLevel", 0.0F);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        compound.putBoolean("HasMilk", getHasMilk());
        compound.putInt("SheetColour", getSheetColor());
        compound.putBoolean("HasFood", getHasFood());
        compound.putFloat("MilkLevel", this.milkLevel);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        return false;
    }
}
