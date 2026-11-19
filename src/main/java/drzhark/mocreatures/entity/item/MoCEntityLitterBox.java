/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.item;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.hostile.MoCEntityOgre;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;

import java.util.List;

public class MoCEntityLitterBox extends Mob {

    private static final EntityDataAccessor<Boolean> PICKED_UP = SynchedEntityData.defineId(MoCEntityLitterBox.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> USED_LITTER = SynchedEntityData.defineId(MoCEntityLitterBox.class, EntityDataSerializers.BOOLEAN);
    public int litterTime;

    public MoCEntityLitterBox(EntityType<? extends MoCEntityLitterBox> type, Level world) {
        super(type, world);
        setNoAi(true);
    }

    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("litter_box.png");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PICKED_UP, false);
        builder.define(USED_LITTER, false);
    }

    public boolean getPickedUp() {
        return this.entityData.get(PICKED_UP);
    }

    public void setPickedUp(boolean flag) {
        this.entityData.set(PICKED_UP, flag);
    }

    public boolean getUsedLitter() {
        return this.entityData.get(USED_LITTER);
    }

    public void setUsedLitter(boolean flag) {
        this.entityData.set(USED_LITTER, flag);
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
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.0F;
    }

    /** The kitty sits on the floor of the bed or box. */
    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return Vec3.ZERO;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.is(Item.byBlock(Blocks.SAND))) {
            MoCTools.playCustomSound(this, SoundEvents.SAND_PLACE);
            if (!player.isCreative()) stack.shrink(1);
            setUsedLitter(false);
            this.litterTime = 0;
            return InteractionResult.SUCCESS;
        }
        if (this.getVehicle() == null) {
            if (player.isCrouching()) {
                player.getInventory().add(new ItemStack(MoCItems.LITTER_BOX.get()));
                MoCTools.playCustomSound(this, SoundEvents.ITEM_PICKUP, 0.2F);
                remove(RemovalReason.DISCARDED);
            } else {
                setYHeadRot((float) MoCTools.roundToNearest90Degrees(this.getYHeadRot()) + 90.0F);
                MoCTools.playCustomSound(this, SoundEvents.ITEM_FRAME_ROTATE_ITEM);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        if (!this.level().isClientSide() && (getVehicle() != null || !this.onGround() || !MoCreatures.proxy.staticLitter)) {
            super.move(type, pos);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround()) {
            setPickedUp(false);
        }
        if (getUsedLitter()) {
            if (!this.level().isClientSide()) {
                this.litterTime++;
                List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(12D, 4D, 12D), 
                    entity -> entity instanceof Monster);
                
                for (Entity entity : list) {
                    if (entity instanceof Monster) {
                        Monster entityMob = (Monster) entity;
                        entityMob.setTarget(this);
                        if (entityMob instanceof Creeper) {
                            ((Creeper) entityMob).setSwellDir(-1);
                        }
                        if (entityMob instanceof MoCEntityOgre) {
                            ((MoCEntityOgre) entityMob).smashCounter = 0;
                        }
                    }
                }
            } else {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.litterTime > 5000 && !this.level().isClientSide()) {
            setUsedLitter(false);
            this.litterTime = 0;
        }
        if (this.isPassenger()) MoCTools.dismountSneakingPlayer(this);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compound) {
        compound.putBoolean("UsedLitter", getUsedLitter());
    }

    @Override
    public void readAdditionalSaveData(ValueInput compound) {
        setUsedLitter(compound.getBooleanOr("UsedLitter", false));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        return false;
    }
}
