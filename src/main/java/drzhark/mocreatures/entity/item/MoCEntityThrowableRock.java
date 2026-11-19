/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import java.util.List;

public class MoCEntityThrowableRock extends Entity implements IEntityWithComplexSpawn {

    private static final EntityDataAccessor<Integer> ROCK_STATE = SynchedEntityData.defineId(MoCEntityThrowableRock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MASTERS_ID = SynchedEntityData.defineId(MoCEntityThrowableRock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BEHAVIOUR_TYPE = SynchedEntityData.defineId(MoCEntityThrowableRock.class, EntityDataSerializers.INT);
    public int acceleration = 100;
    private int rockTimer;
    private double oPosX;
    private double oPosY;
    private double oPosZ;

    public MoCEntityThrowableRock(EntityType<? extends MoCEntityThrowableRock> type, Level world) {
        super(type, world);
        this.noPhysics = false;
    }

    public static MoCEntityThrowableRock build(Level world, Entity entitythrower, double posX, double posY, double posZ) {
        MoCEntityThrowableRock rock = new MoCEntityThrowableRock(MoCEntities.TROCK.get(), world);
        rock.setPos(posX, posY, posZ);
        rock.rockTimer = 250;
        rock.xo = rock.oPosX = posX;
        rock.yo = rock.oPosY = posY;
        rock.zo = rock.oPosZ = posZ;
        rock.setMasterID(entitythrower.getId());
        return rock;
    }

    public BlockState getState() {
        BlockState state = Block.stateById(this.entityData.get(ROCK_STATE) & 65535);
        return (state == null || state.isAir()) ? Blocks.STONE.defaultBlockState() : state; // fallback for safety
    }

    public void setState(BlockState state) {
        this.entityData.set(ROCK_STATE, (Block.getId(state) & 65535));
    }

    public int getMasterID() {
        return this.entityData.get(MASTERS_ID);
    }

    public void setMasterID(int i) {
        this.entityData.set(MASTERS_ID, i);
    }

    public int getBehavior() {
        return this.entityData.get(BEHAVIOUR_TYPE);
    }

    public void setBehavior(int i) {
        this.entityData.set(BEHAVIOUR_TYPE, i);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BEHAVIOUR_TYPE, 0);
        builder.define(ROCK_STATE, 0);
        builder.define(MASTERS_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        BlockState iblockstate = this.getState();
        nbttagcompound.putInt("Behavior", getBehavior());
        nbttagcompound.putInt("MasterID", getMasterID());
        nbttagcompound.putShort("BlockID", (short) (Block.getId(iblockstate) & 65535));
    }


    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        setBehavior(nbttagcompound.getIntOr("Behavior", 0));
        setMasterID(nbttagcompound.getIntOr("MasterID", 0));
        BlockState iblockstate = Block.stateById(nbttagcompound.getShortOr("BlockID", (short) 0) & 65535);
        this.setState(iblockstate);
    }

    @Override
    public boolean isPushable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        Entity master = getMaster();
        if (this.rockTimer-- <= -50 && getBehavior() == 0 || master == null) transformToItem();

        // held TRocks don't need to adjust its position
        if (getBehavior() == 1) return;

        // rock damage code (for all rock behaviors)
        if (!this.onGround()) {
            List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().contract(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z).inflate(1.0D, 1.0D, 1.0D));
            for (Entity entity : list) {
                if (master != null && entity.getId() == master.getId()) continue;
                if (entity instanceof MoCEntityGolem) continue;
                if (entity != null && !(entity instanceof LivingEntity)) continue;
                if (entity instanceof LivingEntity) {
                    if (master instanceof LivingEntity) {
                        entity.hurtOrSimulate(this.damageSources().mobAttack((LivingEntity) master), 4);
                    } else {
                        entity.hurtOrSimulate(this.damageSources().generic(), 4);
                    }
                }
            }
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (getBehavior() == 2) {
            if (master == null) {
                setBehavior(0);
                this.rockTimer = -50;
                return;
            }

            // moves towards the master entity the bigger the number, the slower
            --this.acceleration;
            if (this.acceleration < 10) {
                this.acceleration = 10;
            }

            float tX = (float) this.getX() - (float) master.getX();
            float tZ = (float) this.getZ() - (float) master.getZ();
            float distXZToMaster = tX * tX + tZ * tZ;

            if (distXZToMaster < 1.5F && master instanceof MoCEntityGolem) {
                ((MoCEntityGolem) master).receiveRock(this.getState());
                this.setBehavior(0);
                this.remove(RemovalReason.DISCARDED);
            }

            double summonedSpeed = this.acceleration;
            this.setDeltaMovement((master.getX() - this.getX()) / summonedSpeed, (master.getY() - this.getY()) / 20D + 0.15D, (master.getZ() - this.getZ()) / summonedSpeed);
            if (!this.level().isClientSide())
                this.move(MoverType.SELF, this.getDeltaMovement());
            return;
        }

        // imploding / exploding rock
        if (getBehavior() == 4) {
            if (master == null) {
                if (!this.level().isClientSide()) setBehavior(5);
                return;
            }

            // moves towards the master entity
            // the bigger the number, the slower
            this.acceleration = 10;

            float tX = (float) this.getX() - (float) master.getX();
            float tZ = (float) this.getZ() - (float) master.getZ();
            float distXZToMaster = tX * tX + tZ * tZ;

            double summonedSpeed = this.acceleration;
            this.setDeltaMovement((master.getX() - this.getX()) / summonedSpeed, (master.getY() - this.getY()) / 20D + 0.15D, (master.getZ() - this.getZ()) / summonedSpeed);

            if (distXZToMaster < 2.5F && master instanceof MoCEntityGolem) {
                this.setDeltaMovement(0.0D, 0.0D, 0.0D);
            }
        }

        if (getBehavior() == 5) {
            --this.acceleration;
            if (this.acceleration < 3) {
                this.acceleration = 3;
            }

            // explodes away from the player
            // the smaller, the faster
            double explodingSpeed = this.acceleration / 5;
            this.setDeltaMovement(this.getDeltaMovement().x / explodingSpeed, this.getDeltaMovement().y / explodingSpeed, this.getDeltaMovement().z / explodingSpeed);
        }

        if (getBehavior() == 0) {
            // Simulate projectile gravity and slowdown
            Vec3 motion = this.getDeltaMovement();
        
            // Apply gravity
            if (!this.isNoGravity()) {
                motion = motion.add(0.0D, -0.04D, 0.0D);
            }
        
            // Apply friction
            motion = motion.scale(0.98F); // basic air drag
        
            // Apply to entity
            this.setDeltaMovement(motion);
        
            // Move by updated motion
            this.move(MoverType.SELF, motion);
        }
        

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public void transformToItem() {
        if (!this.level().isClientSide()) {
            ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(this.getState().getBlock(), 1));
            entityitem.setDeltaMovement(0.0D, 0.1D, 0.0D);
            entityitem.setDefaultPickUpDelay();
            this.level().addFreshEntity(entityitem);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private Entity getMaster() {
        Entity master = null;
        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(120D, 120D, 120D));
        for (Entity entity : list) {
            if (entity.getId() == getMasterID()) {
                master = entity;
                return master;
            }
        }
        return master;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        BlockState state = this.getState();
        buffer.writeInt(Block.getId(state));
        buffer.writeInt(getBehavior());
        buffer.writeInt(getMasterID());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        this.setState(Block.stateById(buffer.readInt()));
        this.setBehavior(buffer.readInt());
        this.setMasterID(buffer.readInt());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }
}
