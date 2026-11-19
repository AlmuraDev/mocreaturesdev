/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.entity.item.MoCEntityThrowableRock;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.network.message.MoCMessageTwoBytes;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.NetworkHooks;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoCEntityGolem extends MoCEntityMob {

    private static final EntityDataAccessor<Integer> GOLEM_STATE = SynchedEntityData.defineId(MoCEntityGolem.class, EntityDataSerializers.INT);
    public int tCounter;
    private byte[] golemCubes;
    private int dCounter = 0;
    private int sCounter;

    public MoCEntityGolem(EntityType<? extends MoCEntityGolem> type, Level world) {
        super(type, world);
        this.texture = "golem.png";
        //this.setSize(1.8F, 4.3F);
        this.xpReward = 20;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoCEntityGolem.AIGolemAttack(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MoCEntityGolem.AIGolemTarget<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new MoCEntityGolem.AIGolemTarget<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // These methods will be called by NetworkHooks when spawning the entity
    public void writeSpawnData(FriendlyByteBuf data) {
        if (this.golemCubes == null) {
            this.initGolemCubes();
        }
        for (int i = 0; i < 23; i++) data.writeByte(this.golemCubes[i]);
    }

    public void readSpawnData(FriendlyByteBuf data) {
        if (this.golemCubes == null) {
            this.golemCubes = new byte[23];
        }
        for (int i = 0; i < 23; i++) this.golemCubes[i] = data.readByte();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        this.initGolemCubes();
        builder.define(GOLEM_STATE, 0); // 0: spawned / 1: summoning rocks / 2: has enemy / 3: half life (harder) / 4: dying
    }

    public int getGolemState() {
        return this.entityData.get(GOLEM_STATE);
    }

    public void setGolemState(int i) {
        this.entityData.set(GOLEM_STATE, i);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (getGolemState() == 0) // just spawned
            {
                Player entityplayer1 = this.level().getNearestPlayer(this, 8D);
                if (entityplayer1 != null) setGolemState(1); // activated
            }

            if (getGolemState() == 1 && !isMissingCubes()) setGolemState(2); // is complete

            if (getGolemState() > 2 && getGolemState() != 4 && this.getTarget() == null) setGolemState(1);

            if (getGolemState() > 1 && this.getTarget() != null && this.random.nextInt(20) == 0) {
                if (getHealth() >= 30) setGolemState(2);
                if (getHealth() < 30 && getHealth() >= 10) setGolemState(3); // more dangerous
                if (getHealth() < 10) setGolemState(4); // dying
            }

            if (getGolemState() != 0 && getGolemState() != 4 && isMissingCubes()) {
                int freq = 42 - (getGolemState() * this.level().getDifficulty().getId());
                if (getGolemState() == 1) freq = 20;
                if (this.random.nextInt(freq) == 0) acquireRock(2);
            }

            if (getGolemState() == 4) {
                this.getNavigation().stop();
                this.dCounter++;

                if (this.dCounter < 80 && this.random.nextInt(3) == 0) acquireRock(4);

                if (this.dCounter == 120) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOLEM_DYING.get(), 3F);
                    if (!this.level().isClientSide()) {
                        ServerLevel serverWorld = (ServerLevel) this.level();
                        MoCMessageHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new MoCMessageAnimation(this.getId(), 1));
                    }
                }

                if (this.dCounter > 140) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOLEM_EXPLODE.get(), 3F);
                    destroyGolem();
                }
            }
        }

        if (this.tCounter == 0 && this.getTarget() != null && this.canShoot()) {
            float distanceToTarget = this.distanceTo(this.getTarget());
            if (distanceToTarget > 6F) {
                this.tCounter = 1;
                if (!this.level().isClientSide()) {
                    ServerLevel serverWorld = (ServerLevel) this.level();
                    MoCMessageHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new MoCMessageAnimation(this.getId(), 0));
                }
            }

        }
        if (this.tCounter != 0) {
            if (this.tCounter++ == 70 && this.getTarget() != null && this.canShoot() && !this.getTarget().isRemoved() && this.hasLineOfSight(this.getTarget())) {
                shootBlock(this.getTarget());
            } else if (this.tCounter > 90) this.tCounter = 0;
        }

        if (MoCreatures.proxy.getParticleFX() > 0 && getGolemState() == 4 && this.sCounter > 0) {
            for (int i = 0; i < 10; i++) {
                this.level().addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());
            }
        }
    }

    private void destroyGolem() {
        List<Integer> usedBlocks = usedCubes();
        if (!usedBlocks.isEmpty() && MoCTools.mobGriefing(this.level()) && MoCreatures.proxy.golemDestroyBlocks) {
            for (Integer usedBlock : usedBlocks) {
                Block block = generateBlock(this.golemCubes[usedBlock]).getBlock();
                ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(block, 1));
                entityitem.setDefaultPickUpDelay();
                this.level().addFreshEntity(entityitem);
            }
        }
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public boolean isMovementCeased() {
        return getGolemState() == 4;
    }

    /*
     * Finds a missing rock spot in its body, looks for a random rock around it
     */
    protected void acquireRock(int type) {
        if (this.level().isClientSide()) return;

        BlockPos blockPos = this.blockPosition(); // default to golem position
        BlockState blockState = returnRandomCheapBlock(); // default to cheap block
        boolean canDestroyBlock = MoCTools.mobGriefing(this.level()) && MoCreatures.proxy.golemDestroyBlocks;
        
        if (canDestroyBlock) {
            // Use our custom method that returns both position and state
            Object[] result = destroyRandomBlockAndGetPosition(12D);
            
            if (result != null) {
                // Block was successfully broken, use the exact position and state
                blockPos = (BlockPos) result[0];
                blockState = (BlockState) result[1];
            } else {
                canDestroyBlock = false;
            }
        }
        
        if (!canDestroyBlock) {
            blockPos = this.blockPosition(); // use golem position as fallback
            blockState = returnRandomCheapBlock(); // get cheap rocks
        }

        MoCEntityThrowableRock tRock = MoCEntityThrowableRock.build(this.level(), this, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        
        // CRITICAL: Set state and behavior BEFORE adding to world so spawn packet has correct data
        tRock.setState(blockState);
        tRock.setBehavior(type); // 2: rock follows the golem / 3: rock gets around the golem
        
        this.level().addFreshEntity(tRock); // spawn with correct state already set
    }

    /**
     * Returns a random block when the golem is unable to break blocks
     */
    private BlockState returnRandomCheapBlock() {
        int i = this.random.nextInt(4);
        switch (i) {
            case 1:
                return Blocks.COBBLESTONE.defaultBlockState();
            case 2:
                return Blocks.OAK_PLANKS.defaultBlockState();
            case 3:
                return Blocks.ICE.defaultBlockState();
            default:
                return Blocks.DIRT.defaultBlockState();
        }
    }

    /**
     * Custom block breaking method for golem that returns both position and state
     * Returns an array where [0] = BlockPos, [1] = BlockState, or null if no block found
     */
    @Nullable
    private Object[] destroyRandomBlockAndGetPosition(double distance) {
        int l = (int) (distance * distance * distance);
        Level level = this.level();

        for (int i = 0; i < l; i++) {
            int x = (int) (this.getX() + level.getRandom().nextInt((int) distance) - (distance / 2));
            int y = (int) (this.getY() + level.getRandom().nextInt((int) distance) - (distance / 2));
            int z = (int) (this.getZ() + level.getRandom().nextInt((int) distance) - (distance / 2));
            BlockPos pos = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
            BlockState stateAbove = level.getBlockState(pos.above());
            BlockState stateTarget = level.getBlockState(pos);

            if (pos.getY() == (int) this.getY() - 1D && pos.getX() == Mth.floor(this.getX()) && pos.getZ() == Mth.floor(this.getZ())) {
                continue;
            }

            if (!stateTarget.isAir() && stateTarget.getBlock() != Blocks.WATER && stateTarget.getBlock() != Blocks.BEDROCK && stateAbove.isAir()) {
                if (MoCTools.mobGriefing(level)) {
                    BlockEvent.BreakEvent event = null;
                    if (!level.isClientSide()) {
                        event = new BlockEvent.BreakEvent(level, pos, stateTarget, FakePlayerFactory.get((ServerLevel) level, MoCreatures.MOCFAKEPLAYER));
                    }
                    if (event != null && !event.isCanceled()) {
                        level.removeBlock(pos, false);
                        // Return both position and state
                        return new Object[]{pos, stateTarget};
                    }
                }
            }
        }

        return null;
    }

    /**
     * When the golem receives the rock, called from within EntityTRock
     */
    public void receiveRock(BlockState state) {
        if (!this.level().isClientSide()) {
            byte myBlock = translateOre(state);
            byte slot = (byte) getRandomCubeAdj();
            if (slot != -1 && slot < 23 && myBlock != -1 && getGolemState() != 4) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOLEM_ATTACH.get(), 3F);
                int h = this.level().getDifficulty().getId();
                this.setHealth(getHealth() + h);
                if (getHealth() > getMaxHealth()) this.setHealth(getMaxHealth());
                saveGolemCube(slot, myBlock);
            } else {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_TURTLE_HURT.get(), 2F);
                if ((MoCTools.mobGriefing(this.level())) && (MoCreatures.proxy.golemDestroyBlocks)) {
                    ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(state.getBlock(), 1));
                    entityitem.setDefaultPickUpDelay();
                    entityitem.lifespan = 1200;
                    this.level().addFreshEntity(entityitem);
                }
            }
        }
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) this.tCounter = 1; // rock throwing animation
        else if (animationType == 1) this.sCounter = 1; // smoking animation
    }

    /**
     * Shoots one block to the target
     */
    private void shootBlock(Entity entity) {
        if (entity == null) return;
    
        // Collect available arm cubes
        List<Integer> armBlocks = new ArrayList<>();
        for (int i = 9; i < 15; i++) {
            if (this.golemCubes[i] != 30) armBlocks.add(i);
        }
        if (armBlocks.isEmpty()) return;
    
        // Pick a cube and resolve to the outermost available segment
        int baseIndex = armBlocks.get(this.random.nextInt(armBlocks.size()));
        int x = baseIndex;
    
        // Follow same logic as 1.16.5 — prefer deeper segments
        if ((baseIndex == 9 || baseIndex == 12)) {
            if (this.golemCubes[baseIndex + 2] != 30) {
                x = baseIndex + 2;
            } else if (this.golemCubes[baseIndex + 1] != 30) {
                x = baseIndex + 1;
            }
        } else if ((baseIndex == 10 || baseIndex == 13) && this.golemCubes[baseIndex + 1] != 30) {
            x = baseIndex + 1;
        }
    
        if (this.golemCubes[x] == 30) return; // sanity check
    
        BlockState shootState = generateBlock(this.golemCubes[x]);
    
        // Throw with sound and state
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOLEM_SHOOT.get(), 3F);
        MoCTools.throwStone(this, entity, shootState, 10D, 0.3D);
    
        // Remove the thrown cube from the golem
        saveGolemCube((byte) x, (byte) 30);
    
        // Reset throw timer
        this.tCounter = 0;
    }
    

    private boolean canShoot() {
        int x = 0;
        for (byte i = 9; i < 15; i++) {
            if (this.golemCubes[i] != 30) x++;
        }
        return (x != 0) && getGolemState() != 4 && getGolemState() != 1;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (damagesource.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurtServer(serverLevel, damagesource, i);
        }
        if (getGolemState() == 4) return false;

        List<Integer> missingChestBlocks = missingChestCubes();
        boolean uncoveredChest = (missingChestBlocks.size() == 4);
        if (!openChest() && !uncoveredChest && getGolemState() != 1) {
            int j = this.level().getDifficulty().getId();
            if (!this.level().isClientSide() && this.random.nextInt(j) == 0) destroyRandomGolemCube();
            else MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_TURTLE_HURT.get(), 2F);

            Entity entity = damagesource.getEntity();
            if ((entity != this) && (this.level().getDifficulty().getId() > 0) && entity instanceof LivingEntity) {
                this.setTarget((LivingEntity) entity);
                return true;
            } else return false;
        }

        if (i > 5) i = 5; //so you can't hit a Golem too hard
        if (getGolemState() != 1 && super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if ((entity != this) && (this.level().getDifficulty().getId() > 0) && entity instanceof LivingEntity) {
                this.setTarget((LivingEntity) entity);
                return true;
            } else return false;
        }
        if (getGolemState() == 1) {
            Entity entity = damagesource.getEntity();
            if ((entity != this) && (this.level().getDifficulty().getId() > 0) && entity instanceof LivingEntity) {
                this.setTarget((LivingEntity) entity);
                return true;
            } else return false;
        }
        return false;
    }

    /**
     * Destroys a random cube, with the proper check for extremities and spawns
     * a block in world
     */
    private void destroyRandomGolemCube() {
        int i = getRandomUsedCube();
        if (i == 4) return; // do not destroy the valuable back cube

        int x = i;
        if (i == 10 || i == 13 || i == 16 || i == 19) {
            if (this.golemCubes[i + 1] != 30) x = i + 1;
        }

        if (i == 9 || i == 12 || i == 15 || i == 18) {
            if (this.golemCubes[i + 2] != 30) x = i + 2;
            else if (this.golemCubes[i + 1] != 30) x = i + 1;
        }

        if (x != -1 && this.golemCubes[x] != 30) {
            Block block = generateBlock(this.golemCubes[x]).getBlock();
            saveGolemCube((byte) x, (byte) 30);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GOLEM_HURT.get(), 3F);
            if ((MoCTools.mobGriefing(this.level())) && (MoCreatures.proxy.golemDestroyBlocks)) {
                ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(block, 1));
                entityitem.setDefaultPickUpDelay();
                this.level().addFreshEntity(entityitem);
            }
        }
    }

    @Override
    public float getAdjustedYOffset() {
        if (this.golemCubes[17] != 30 || this.golemCubes[20] != 30) return 0F; // has feet
        if (this.golemCubes[16] != 30 || this.golemCubes[19] != 30) return 0.4F; // has knees but not feet
        if (this.golemCubes[15] != 30 || this.golemCubes[18] != 30) return 0.7F; // has thighs but not knees or feet
        if (this.golemCubes[1] != 30 || this.golemCubes[3] != 30) return 0.8F; // has lower chest
        return 1.45F; //missing everything
    }

    /**
     * Stretches the model to that size
     */
    @Override
    public float getSizeFactor() {
        return 1.8F;
    }

    /**
     * @param i = slot
     * @return the block type stored in that slot. 30 = empty
     */
    public byte getBlockText(int i) {
        return this.golemCubes[i];
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putInt("golemState", getGolemState());
        ListTag cubeLists = new ListTag();

        for (int i = 0; i < 23; i++) {
            CompoundTag nbttag = new CompoundTag();
            nbttag.putByte("Slot", this.golemCubes[i]);
            cubeLists.add(nbttag);
        }
        nbttagcompound.put("GolemBlocks", cubeLists);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setGolemState(nbttagcompound.getIntOr("golemState", 0));
        ListTag nbttaglist = nbttagcompound.getList("GolemBlocks", 10);
        for (int i = 0; i < 23; i++) {
            CompoundTag var4 = nbttaglist.getCompound(i);
            this.golemCubes[i] = var4.getByte("Slot");
        }
    }

    /**
     * Initializes the goleCubes array
     */
    private void initGolemCubes() {
        this.golemCubes = new byte[23];

        for (int i = 0; i < 23; i++) this.golemCubes[i] = 30;

        int j = this.random.nextInt(4);
        switch (j) {
            case 0:
                j = 7;
                break;
            case 1:
                j = 11;
                break;
            case 2:
                j = 15;
                break;
            case 3:
                j = 21;
                break;
        }
        saveGolemCube((byte) 4, (byte) j);
    }

    /**
     * Saves the type of Cube(value) on the given 'slot' if server, then sends a
     * packet to the clients
     */
    public void saveGolemCube(byte slot, byte value) {
        this.golemCubes[slot] = value;
        if (!this.level().isClientSide() && MoCreatures.proxy.worldInitDone) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), new MoCMessageTwoBytes(this.getId(), slot, value));
        }
    }

    /**
     * returns a list of the empty blocks
     */
    private List<Integer> missingCubes() {
        List<Integer> emptyBlocks = new ArrayList<>();

        for (int i = 0; i < 23; i++) {
            if (this.golemCubes[i] == 30) emptyBlocks.add(i);
        }
        return emptyBlocks;
    }

    /**
     * Returns true if is 'missing' any cube, false if it's full
     */
    public boolean isMissingCubes() {
        for (int i = 0; i < 23; i++) {
            if (this.golemCubes[i] == 30) return true;
        }
        return false;
    }

    private List<Integer> missingChestCubes() {
        List<Integer> emptyChestBlocks = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (this.golemCubes[i] == 30) emptyChestBlocks.add(i);
        }
        return emptyChestBlocks;
    }

    /**
     * returns a list of the used block spots
     */
    private List<Integer> usedCubes() {
        List<Integer> usedBlocks = new ArrayList<>();

        for (int i = 0; i < 23; i++) {
            if (this.golemCubes[i] != 30) usedBlocks.add(i);
        }
        return usedBlocks;
    }

    /**
     * Returns a random used cube position if the golem is empty, returns -1
     */
    private int getRandomUsedCube() {
        List<Integer> usedBlocks = usedCubes();
        if (usedBlocks.isEmpty()) return -1;
        int randomEmptyBlock = this.random.nextInt(usedBlocks.size());
        return usedBlocks.get(randomEmptyBlock);
    }

    /**
     * Returns a random empty cube position if the golem is full, returns -1
     */
    private int getRandomMissingCube() {
        //first it makes sure it has the four chest cubes
        List<Integer> emptyChestBlocks = missingChestCubes();
        if (!emptyChestBlocks.isEmpty()) {
            int randomEmptyBlock = this.random.nextInt(emptyChestBlocks.size());
            return emptyChestBlocks.get(randomEmptyBlock);
        }

        //otherwise returns any other cube
        List<Integer> emptyBlocks = missingCubes();
        if (emptyBlocks.isEmpty()) {
            return -1;
        }
        int randomEmptyBlock = this.random.nextInt(emptyBlocks.size());
        return emptyBlocks.get(randomEmptyBlock);
    }

    /**
     * returns the position of the cube to be added, contains logic for the
     * extremities
     */
    private int getRandomCubeAdj() {
        int i = getRandomMissingCube();

        if (i == 10 || i == 13 || i == 16 || i == 19) {
            if (this.golemCubes[i - 1] != 30) saveGolemCube((byte) i, this.golemCubes[i - 1]);
            return i - 1;
        }

        if (i == 11 || i == 14 || i == 17 || i == 20) {
            if (this.golemCubes[i - 2] == 30 && this.golemCubes[i - 1] == 30) return i - 2;
            if (this.golemCubes[i - 1] != 30) saveGolemCube((byte) i, this.golemCubes[i - 1]);
            saveGolemCube((byte) (i - 1), this.golemCubes[i - 2]);
            return i - 2;
        }
        return i;
    }

    @Override
    public float rollRotationOffset() {
        int leftLeg = 0;
        int rightLeg = 0;
        if (this.golemCubes[15] != 30) leftLeg++;
        if (this.golemCubes[16] != 30) leftLeg++;
        if (this.golemCubes[17] != 30) leftLeg++;
        if (this.golemCubes[18] != 30) rightLeg++;
        if (this.golemCubes[19] != 30) rightLeg++;
        if (this.golemCubes[20] != 30) rightLeg++;
        return (leftLeg - rightLeg) * 10F;
    }

    /**
     * The chest opens when the Golem is missing cubes and the summoned blocks
     * are close
     */
    public boolean openChest() {
        if (isMissingCubes()) {
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(2D));
            for (Entity entity1 : list) {
                if (entity1 instanceof MoCEntityThrowableRock) {
                    if (MoCreatures.proxy.getParticleFX() > 0) MoCreatures.proxy.VacuumFX(this);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts the world block into the golem block texture if not found,
     * returns -1
     */
    private byte translateOre(BlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.STONE) return 0;
        if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK) return 1;
        if (block == Blocks.COBBLESTONE || block == Blocks.MOSSY_COBBLESTONE) return 2;
        if (block == Blocks.OAK_PLANKS) return 3;
        if (block == Blocks.SAND) return 4;
        if (block == Blocks.GRAVEL) return 5;
        if (block == Blocks.OAK_LOG || block == Blocks.STRIPPED_OAK_LOG) return 6;
        if (block == Blocks.GOLD_BLOCK || block == Blocks.GOLD_ORE) return 7;
        if (block == Blocks.GLASS) return 8;
        if (block == Blocks.BLUE_WOOL || block == Blocks.LAPIS_BLOCK) return 9;
        if (block == Blocks.OAK_LEAVES) return 10;
        if (block == Blocks.IRON_BLOCK || block == Blocks.IRON_ORE) return 11;
        if (block == Blocks.BRICKS) return 12;
        if (block == Blocks.OBSIDIAN) return 14;
        if (block == Blocks.DIAMOND_BLOCK || block == Blocks.DIAMOND_ORE) return 15;
        if (block == Blocks.CRAFTING_TABLE) return 16;
        if (block == Blocks.NETHERRACK) return 23;
        if (block == Blocks.GLOWSTONE) return 25;
        if (block == Blocks.STONE_BRICKS || block == Blocks.CRACKED_STONE_BRICKS) return 26;
        if (block == Blocks.NETHER_BRICKS) return 27;
        if (block == Blocks.EMERALD_BLOCK || block == Blocks.EMERALD_ORE) return 21;
        if (block == Blocks.PUMPKIN || block == Blocks.JACK_O_LANTERN || block == Blocks.MELON) return 22;
        if (block == Blocks.ICE || block == Blocks.PACKED_ICE) return 18;
        if (block == Blocks.CACTUS) return 19;
        if (block == Blocks.CLAY) return 20;

        // fallback
        return -1;
    }

    /**
     * Provides the BlockState originated from the golem's block
     */
    private BlockState generateBlock(int golemBlock) {
        switch (golemBlock) {
            case 0:
                return Blocks.STONE.defaultBlockState();
            case 1:
                return Blocks.DIRT.defaultBlockState();
            case 2:
                return Blocks.COBBLESTONE.defaultBlockState();
            case 3:
                return Blocks.OAK_PLANKS.defaultBlockState();
            case 4:
                return Blocks.SAND.defaultBlockState();
            case 5:
                return Blocks.GRAVEL.defaultBlockState();
            case 6:
                return Blocks.OAK_LOG.defaultBlockState();
            case 7:
                return Blocks.GOLD_BLOCK.defaultBlockState();
            case 8:
                return Blocks.GLASS.defaultBlockState();
            case 9:
                return Blocks.BLUE_WOOL.defaultBlockState();
            case 10:
                return Blocks.OAK_LEAVES.defaultBlockState();
            case 11:
                return Blocks.IRON_BLOCK.defaultBlockState();
            case 12:
                return Blocks.BRICKS.defaultBlockState();
            case 13: // unused
                return Blocks.GRASS_BLOCK.defaultBlockState();
            case 14:
                return Blocks.OBSIDIAN.defaultBlockState();
            case 15:
                return Blocks.DIAMOND_BLOCK.defaultBlockState();
            case 16:
                return Blocks.CRAFTING_TABLE.defaultBlockState();
            case 17:
                return Blocks.FIRE.defaultBlockState();
            case 18:
                return Blocks.ICE.defaultBlockState();
            case 19:
                return Blocks.CACTUS.defaultBlockState();
            case 20:
                return Blocks.CLAY.defaultBlockState();
            case 21:
                return Blocks.EMERALD_BLOCK.defaultBlockState();
            case 22:
                return Blocks.PUMPKIN.defaultBlockState();
            case 23:
                return Blocks.NETHERRACK.defaultBlockState();
            case 24:
                return Blocks.DIAMOND_ORE.defaultBlockState();
            case 25:
                return Blocks.GLOWSTONE.defaultBlockState();
            case 26:
                return Blocks.STONE_BRICKS.defaultBlockState();
            case 27:
                return Blocks.NETHER_BRICKS.defaultBlockState();
            default:
                return Blocks.GRASS_BLOCK.defaultBlockState();
        }
    }

    private int countLegBlocks() {
        int x = 0;
        for (byte i = 15; i < 21; i++) {
            if (this.golemCubes[i] != 30) x++;
        }
        return x;
    }

    @Override
    public float getSpeed() {
        return 0.15F * (countLegBlocks() / 6F);
    }

    /**
     * Used for the power texture used on the golem
     */
    public Identifier getEffectTexture() {
        switch (getGolemState()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("golem_effect_red.png");
            case 2:
                return MoCreatures.proxy.getModelTexture("golem_effect_yellow.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("golem_effect_orange.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("golem_effect_blue.png");
            default:
                return null;
        }
    }

    /**
     * Used for the particle FX
     */
    public float colorFX(int i) {
        switch (getGolemState()) {
            case 1:
                if (i == 1) return 65F / 255F;
                if (i == 2) return 157F / 255F;
                if (i == 3) return 254F / 255F;
            case 2:
                if (i == 1) return 244F / 255F;
                if (i == 2) return 248F / 255F;
                if (i == 3) return 36F / 255F;
            case 3:
                if (i == 1) return 1F;
                if (i == 2) return 154F / 255F;
                if (i == 3) return 21F / 255F;
            case 4:
                if (i == 1) return 248F / 255F;
                if (i == 2) return 10F / 255F;
                if (i == 3) return 10F / 255F;
            default:
                return 0;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(MoCSoundEvents.ENTITY_GOLEM_WALK.get(), 1.0F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_GOLEM_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BIG_GOLEM;
    }

    public static boolean getCanSpawnHere(EntityType<? extends MoCEntityMob> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        return MoCEntityMob.getCanSpawnHere(type, world, reason, pos, randomIn);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.8F;
    }

    static class AIGolemAttack extends MeleeAttackGoal {
        public AIGolemAttack(MoCEntityGolem golem, double speed, boolean useLongMemory) {
            super(golem, speed, useLongMemory);
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();

            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }

    static class AIGolemTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AIGolemTarget(MoCEntityGolem golem, Class<T> classTarget, boolean checkSight) {
            super(golem, classTarget, checkSight);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return !(f >= 0.5F) && super.canUse();
        }
    }
}