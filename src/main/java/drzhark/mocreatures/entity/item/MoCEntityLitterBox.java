/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.item;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.monster.MoCEntityOgre;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class MoCEntityLitterBox extends EntityLiving {

    public int littertime;
    private static final DataParameter<Boolean> PICKED_UP = EntityDataManager.<Boolean>createKey(MoCEntityLitterBox.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USED_LITTER = EntityDataManager.<Boolean>createKey(MoCEntityLitterBox.class, DataSerializers.BOOLEAN);

    public MoCEntityLitterBox(World world) {
        super(world);
        setSize(1.0F, 0.3F);
    }

    public ResourceLocation getTexture() {
        return MoCreatures.proxy.getTexture("litterbox.png");
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D); // setMaxHealth
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PICKED_UP, Boolean.valueOf(false));
        this.dataManager.register(USED_LITTER, Boolean.valueOf(false));
    }

    public boolean getPickedUp() {
        return ((Boolean)this.dataManager.get(PICKED_UP)).booleanValue();
    }

    public boolean getUsedLitter() {
        return ((Boolean)this.dataManager.get(USED_LITTER)).booleanValue();
    }

    public void setPickedUp(boolean flag) {
        this.dataManager.set(PICKED_UP, Boolean.valueOf(flag));
    }

    public void setUsedLitter(boolean flag) {
        this.dataManager.set(USED_LITTER, Boolean.valueOf(flag));
    }

    public boolean attackEntityFrom(Entity entity, int i) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean canBePushed() {
        return !this.isDead;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void fall(float f, float f1) {
    }

    @Override
    protected float getSoundVolume() {
        return 0.0F;
    }

    @Override
    public double getYOffset() {
        if (this.getRidingEntity() instanceof EntityPlayer)
        {
            return ((EntityPlayer) this.getRidingEntity()).isSneaking() ? 0.25 : 0.5F;
        }
        return super.getYOffset();

    }

    @Override
    public void handleStatusUpdate(byte byte0) {
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty() && ((stack.getItem() == Items.STONE_PICKAXE) || (stack.getItem() == Items.WOODEN_PICKAXE)
                        || (stack.getItem() == Items.IRON_PICKAXE) || (stack.getItem() == Items.GOLDEN_PICKAXE) || (stack.getItem() == Items.DIAMOND_PICKAXE))) {
            player.inventory.addItemStackToInventory(new ItemStack(MoCItems.litterbox));
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, (((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F) + 1.0F) * 2.0F);
            setDead();
            return true;
        }

        if (!stack.isEmpty() && (stack.getItem() == Item.getItemFromBlock(Blocks.SAND))) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            setUsedLitter(false);
            this.littertime = 0;
            return true;
        }
        
        if (this.getRidingEntity() == null) {
            if (this.startRiding(player)) {
                setPickedUp(true);
                this.rotationYaw = player.rotationYaw;
            }

            return true;
        }

        return true;
    }

    @Override
    public void move(MoverType type, double d, double d1, double d2) {
        if ((this.getRidingEntity() != null) || !this.onGround || !MoCreatures.proxy.staticLitter) {
            if (!this.world.isRemote) {
                super.move(type, d, d1, d2);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onGround) {
            setPickedUp(false);
        }
        if (getUsedLitter() && !this.world.isRemote) {
            this.littertime++;
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(12D, 4D, 12D));
            for (int i = 0; i < list.size(); i++) {
                Entity entity = list.get(i);
                if (!(entity instanceof EntityMob)) {
                    continue;
                }
                EntityMob entitymob = (EntityMob) entity;
                entitymob.setAttackTarget(this);
                if (entitymob instanceof EntityCreeper) {
                    ((EntityCreeper) entitymob).setCreeperState(-1);
                }
                if (entitymob instanceof MoCEntityOgre) {
                    ((MoCEntityOgre) entitymob).smashCounter = 0;
                }
            }

        }
        if (this.littertime > 5000 && !this.world.isRemote) {
            setUsedLitter(false);
            this.littertime = 0;
        }
        
        if (this.isRiding()) MoCTools.dismountSneakingPlayer(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = MoCTools.getEntityData(this);
        nbttagcompound.setBoolean("UsedLitter", getUsedLitter());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = MoCTools.getEntityData(this);
        setUsedLitter(nbttagcompound.getBoolean("UsedLitter"));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        return false;
    }
}
