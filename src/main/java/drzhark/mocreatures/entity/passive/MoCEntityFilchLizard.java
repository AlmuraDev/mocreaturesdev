/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.entity.EntitySpawnReason;
import com.google.common.collect.Sets;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;

// Courtesy of Daveyx0, permission given
public class MoCEntityFilchLizard extends MoCEntityAnimal {

    protected ItemStack[] stealItems;

    public MoCEntityFilchLizard(EntityType<? extends MoCEntityFilchLizard> type, Level worldIn) {
        super(type, worldIn);
        this.setDropChance(EquipmentSlot.MAINHAND, 0f);
        this.setDropChance(EquipmentSlot.OFFHAND, 0f);
        //this.setSize(0.6f, 0.5f);
        this.xpReward = 3;
    }

    @Override
    protected void registerGoals() {
        stealItems = getStealItemsFromLootTable();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new EntityAIGrabItemFromFloor(this, 1.2D, Sets.newHashSet(stealItems), true));
        this.goalSelector.addGoal(3, new EntityAIStealFromPlayer(this, 0.8D, Sets.newHashSet(stealItems), true));
        this.goalSelector.addGoal(4, new MoCEntityFilchLizard.AIAvoidWhenNasty(this, Player.class, 16.0F, 1.0D, 1.33D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /**
     * Get the steal items from the loot table definition.
     * This method manually defines the items based on filch_lizard_steal.json
     */
    private ItemStack[] getStealItemsFromLootTable() {
        // These items match the entries in loot_tables/entities/filch_lizard/filch_lizard_steal.json
        return new ItemStack[] {
            new ItemStack(Items.GOLD_INGOT),
            new ItemStack(Items.IRON_INGOT),
            new ItemStack(Items.LAPIS_LAZULI),
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.EMERALD),
            new ItemStack(Items.ENDER_PEARL),
            new ItemStack(Items.FLINT)
        };
    }

    /**
     * Get the spawn items for the filch lizard when it spawns with an item.
     * Based on the spawn loot table definition.
     */
    private ItemStack getRandomSpawnItem() {
        // These would normally come from the spawn loot table
        ItemStack[] spawnItems = {
            new ItemStack(Items.GOLD_INGOT),
            new ItemStack(Items.IRON_INGOT),
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.EMERALD),
            new ItemStack(Items.ENDER_PEARL)
        };
        return spawnItems[this.random.nextInt(spawnItems.length)];
    }

    @Override
    public void selectType() {
        checkSpawningBiome();
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("lizard_filch_sand.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("lizard_filch_sand_red.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("lizard_filch_sand_silver.png");
            default:
                return MoCreatures.proxy.getModelTexture("lizard_filch.png");
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        
        // In 1.20.1, check biome directly
        String biomePath = this.level().getBiome(pos).unwrapKey().orElseThrow().identifier().getPath();
        if (biomePath.contains("desert") || biomePath.contains("sand")) {
            setTypeMoC(2);
        } else if (biomePath.contains("mesa") || biomePath.contains("badlands")) {
            setTypeMoC(3);
        } else if (this.level().dimension() == MoCreatures.proxy.wyvernDimension) {
            setTypeMoC(4);
        } else {
            setTypeMoC(1);
        }
        return true;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        // taller while carrying a stolen item
        EntityDimensions base = super.getDefaultDimensions(poseIn);
        return this.getMainHandItem().isEmpty() ? base : base.scale(1.0F, 2.5F);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return xpReward;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityAnimal.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        if (random.nextInt(100 / MoCreatures.proxy.filchLizardSpawnItemChance) == 0) {
            while (this.getMainHandItem().isEmpty() && !level().isClientSide()) {
                this.setItemSlot(EquipmentSlot.MAINHAND, getRandomSpawnItem());
            }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    public void dropItemStack(ItemStack itemIn, float offsetY) {
        this.spawnAtLocation((ServerLevel) this.level(), itemIn, offsetY);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource par1DamageSource, float par2) {
        if (par1DamageSource.getEntity() != null) {
            this.setLastHurtByMob(par1DamageSource.getEntity() instanceof LivingEntity ? (LivingEntity) par1DamageSource.getEntity() : null);
        }
        ItemStack stack = this.getMainHandItem();
        if (!stack.isEmpty() && !level().isClientSide()) {
            ItemStack newStack = new ItemStack(stack.getItem(), 1);
            this.dropItemStack(newStack, 1);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurtServer(serverLevel, par1DamageSource, par2);
    }

    // Sneaky...
    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FILCH_LIZARD;
    }

    @Nullable
    protected Identifier getSpawnLootTable() {
        // Reference to loot_tables/entities/filch_lizard/filch_lizard_spawn.json
        return MoCLootTables.FILCH_LIZARD_SPAWN;
    }

    @Nullable
    protected Identifier getStealLootTable() {
        // Reference to loot_tables/entities/filch_lizard/filch_lizard_steal.json
        return MoCLootTables.FILCH_LIZARD_STEAL;
    }

    static class AIAvoidWhenNasty extends AvoidEntityGoal<Player> {
        public AIAvoidWhenNasty(PathfinderMob theEntityIn, Class<Player> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
            super(theEntityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
        }

        /**
         * Returns whether the Goal should begin execution.
         */
        @Override
        public boolean canUse() {
            return !((MoCEntityFilchLizard)mob).getMainHandItem().isEmpty() && super.canUse();
        }
    }

    static class EntityAIGrabItemFromFloor extends Goal {
        /**
         * The entity using this AI that is tempted by the player.
         */
        private final PathfinderMob temptedEntity;
        private final double speed;
        private final Set<ItemStack> temptItem;
        private final boolean canGetScared;
        /**
         * X position of player tempting this mob
         */
        private double targetX;
        /**
         * Y position of player tempting this mob
         */
        private double targetY;
        /**
         * Z position of player tempting this mob
         */
        private double targetZ;
        /**
         * Tempting player's pitch
         */
        private double pitch;
        /**
         * Tempting player's yaw
         */
        private double yaw;
        /**
         * The player that is tempting the entity that is using this AI.
         */
        private ItemEntity temptingItem;
        private boolean isRunning;
        private int stealDelay = 0;

        public EntityAIGrabItemFromFloor(PathfinderMob temptedEntityIn, double speedIn, Set<ItemStack> temptItemIn, boolean canGetScared) {
            this.temptedEntity = temptedEntityIn;
            this.speed = speedIn;
            this.temptItem = temptItemIn;
            this.canGetScared = canGetScared;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether the Goal should begin execution.
         */
        @Override
        public boolean canUse() {
            if (this.temptedEntity.getLastHurtByMob() != null && canGetScared && stealDelay <= 0) {
                this.stop();
                return false;
            }
            if (!(this.temptedEntity instanceof MoCEntityFilchLizard) || !((MoCEntityFilchLizard)this.temptedEntity).getMainHandItem().isEmpty()) {
                return false;
            }
            List<ItemEntity> list = this.temptedEntity.level().getEntitiesOfClass(ItemEntity.class, this.temptedEntity.getBoundingBox().inflate(6D, 4D, 6D));
            if (this.stealDelay > 0) {
                --this.stealDelay;
                if (stealDelay == 0) {
                    this.temptedEntity.setLastHurtByMob(null);
                }
                return false;
            }
            
            double d0 = Double.MAX_VALUE;
            ItemEntity selectedItem = null;
            
            for (ItemEntity itemEntity : list) {
                if (itemEntity != null && this.isTempting(itemEntity.getItem())) {
                    double d1 = this.temptedEntity.distanceToSqr(itemEntity);
                    if (d1 < d0) {
                        d0 = d1;
                        selectedItem = itemEntity;
                    }
                }
            }
            
            if (selectedItem == null) {
                return false;
            } else {
                this.temptingItem = selectedItem;
                return true;
            }
        }

        /**
         * Returns whether an in-progress Goal should continue executing
         */
        protected boolean isTempting(ItemStack stack) {
            if (this.temptItem.isEmpty()) {
                return false;
            } else {
                for (ItemStack itemstack : this.temptItem) {
                    if (itemstack != null && !itemstack.isEmpty() && itemstack.getItem() == stack.getItem()) {
                        return true;
                    }
                }
                return false;
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void start() {
            this.targetX = this.temptingItem.getX();
            this.targetY = this.temptingItem.getY();
            this.targetZ = this.temptingItem.getZ();
            this.isRunning = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void stop() {
            this.temptingItem = null;
            this.temptedEntity.getNavigation().stop();
            this.isRunning = false;
            if (canGetScared) {
                this.stealDelay = 100;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            this.temptedEntity.getLookControl().setLookAt(this.temptingItem, (float) (this.temptedEntity.getMaxHeadYRot() + 20), (float) this.temptedEntity.getMaxHeadXRot());
            if (this.temptedEntity.distanceToSqr(this.temptingItem) < 1.0D) {
                this.temptedEntity.getNavigation().stop();
                ItemStack loot = temptingItem.getItem().copy();
                temptingItem.discard();
                this.temptedEntity.setItemSlot(EquipmentSlot.MAINHAND, loot);
            } else {
                this.temptedEntity.getNavigation().moveTo(this.temptingItem, this.speed);
            }
        }

        /**
         * @see #isRunning
         */
        public boolean isRunning() {
            return this.isRunning;
        }
    }

    public class EntityAIStealFromPlayer extends Goal {
        /**
         * The entity using this AI that is tempted by the player.
         */
        private final PathfinderMob temptedEntity;
        private final double speed;
        private final Set<ItemStack> temptItem;
        private final boolean canGetScared;
        /**
         * X position of player tempting this mob
         */
        private double targetX;
        /**
         * Y position of player tempting this mob
         */
        private double targetY;
        /**
         * Z position of player tempting this mob
         */
        private double targetZ;
        /**
         * Tempting player's pitch
         */
        private double pitch;
        /**
         * Tempting player's yaw
         */
        private double yaw;
        /**
         * The player that is tempting the entity that is using this AI.
         */
        private Player temptingPlayer;
        private boolean isRunning;
        private int stealDelay = 0;

        public EntityAIStealFromPlayer(PathfinderMob temptedEntityIn, double speedIn, Set<ItemStack> temptItemIn, boolean canGetScared) {
            this.temptedEntity = temptedEntityIn;
            this.speed = speedIn;
            this.temptItem = temptItemIn;
            this.canGetScared = canGetScared;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether the Goal should begin execution.
         */
        @Override
        public boolean canUse() {
            if (this.temptedEntity.getLastHurtByMob() != null && canGetScared && stealDelay <= 0) {
                this.stop();
                return false;
            }
            if (!(this.temptedEntity instanceof MoCEntityFilchLizard) || !((MoCEntityFilchLizard)this.temptedEntity).getMainHandItem().isEmpty()) {
                return false;
            }
            this.temptingPlayer = this.temptedEntity.level().getNearestPlayer(this.temptedEntity, 10.0D);
            if (this.stealDelay > 0) {
                --this.stealDelay;
                if (stealDelay == 0) {
                    this.temptedEntity.setLastHurtByMob(null);
                }
                return false;
            } else if (temptingPlayer != null) {
                for (int i = 0; i < this.temptingPlayer.getInventory().getContainerSize(); i++) {
                    ItemStack item = this.temptingPlayer.getInventory().getItem(i);
                    if (!item.isEmpty() && this.isTempting(item)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Returns whether an in-progress Goal should continue executing
         */
        protected boolean isTempting(ItemStack stack) {
            if (this.temptItem.isEmpty()) {
                return false;
            } else {
                for (ItemStack itemstack : this.temptItem) {
                    if (itemstack != null && !itemstack.isEmpty() && itemstack.getItem() == stack.getItem()) {
                        return true;
                    }
                }
                return false;
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void start() {
            this.targetX = this.temptingPlayer.getX();
            this.targetY = this.temptingPlayer.getY();
            this.targetZ = this.temptingPlayer.getZ();
            this.isRunning = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void stop() {
            this.temptingPlayer = null;
            this.temptedEntity.getNavigation().stop();
            this.isRunning = false;
            if (canGetScared) {
                this.stealDelay = 100;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            this.temptedEntity.getLookControl().setLookAt(this.temptingPlayer, (float) (this.temptedEntity.getMaxHeadYRot() + 20), (float) this.temptedEntity.getMaxHeadXRot());
            if (temptingPlayer.isCreative()) return;
            if (this.temptedEntity.distanceToSqr(this.temptingPlayer) < 3.25D) {
                this.temptedEntity.getNavigation().stop();
                for (int i = 0; i < this.temptingPlayer.getInventory().getContainerSize(); i++) {
                    ItemStack item = this.temptingPlayer.getInventory().getItem(i);
                    if (!item.isEmpty()) {
                        for (ItemStack itemstack : temptItem) {
                            if (itemstack != null && !itemstack.isEmpty() && itemstack.getItem() == item.getItem()) {
                                MoCTools.playCustomSound(this.temptedEntity, SoundEvents.ITEM_PICKUP);
                                ItemStack loot = item.copy();
                                this.temptedEntity.setItemSlot(EquipmentSlot.MAINHAND, loot);
                                item.shrink(1);
                                return;
                            }
                        }
                    }
                }
            } else {
                this.temptedEntity.getNavigation().moveTo(this.temptingPlayer, this.speed);
            }
        }

        /**
         * @see #isRunning
         */
        public boolean isRunning() {
            return this.isRunning;
        }
    }
}
