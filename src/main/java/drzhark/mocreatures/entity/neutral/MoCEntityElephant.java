/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;
import net.minecraft.tags.ItemTags;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ChestMenu;
import drzhark.mocreatures.entity.inventory.CombinedContainer;

import java.util.List;

public class MoCEntityElephant extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Integer> TUSK_TYPE = SynchedEntityData.defineId(MoCEntityElephant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STORAGE_TYPE = SynchedEntityData.defineId(MoCEntityElephant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HARNESS_TYPE = SynchedEntityData.defineId(MoCEntityElephant.class, EntityDataSerializers.INT);
    public int sprintCounter;
    public int sitCounter;
    public MoCAnimalChest localelephantchest;
    public MoCAnimalChest localelephantchest2;
    public MoCAnimalChest localelephantchest3;
    public MoCAnimalChest localelephantchest4;
    public ItemStack localstack;
    public int tailCounter;
    public int trunkCounter;
    public int earCounter;
    boolean hasPlatform;
    private byte tuskUses;
    private byte temper;
    private int xpReward;


    public MoCEntityElephant(EntityType<? extends MoCEntityElephant> type, Level world) {
        super(type, world);
        setAdult(true);
        setTamed(false);
        setMoCAge(50);
        // TODO: Different hitboxes for each elephant type
        //setSize(1.1F, 3F);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
        this.xpReward = 10;

        if (!this.level().isClientSide()) {
            setAdult(this.random.nextInt(4) != 0);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(2) + 1);
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculateMaxHealth());
        this.setHealth(getMaxHealth());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TUSK_TYPE, 0);// tusks: 0 nothing, 1 wood, 2 iron, 3 diamond
        builder.define(STORAGE_TYPE, 0);// storage: 0 nothing, 1 chest, 2 chests....
        builder.define(HARNESS_TYPE, 0);// harness: 0 nothing, 1 harness, 2 cabin
    }

    public int getTusks() {
        return this.entityData.get(TUSK_TYPE);
    }

    public void setTusks(int i) {
        this.entityData.set(TUSK_TYPE, i);
    }

    @Override
    public int getArmorType() {
        return this.entityData.get(HARNESS_TYPE);
    }

    @Override
    public void setArmorType(int i) {
        this.entityData.set(HARNESS_TYPE, i);
    }

    public int getStorage() {
        return this.entityData.get(STORAGE_TYPE);
    }

    public void setStorage(int i) {
        this.entityData.set(STORAGE_TYPE, i);
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("elephant_asian.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("mammoth_woolly.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("mammoth_songhua.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("elephant_asian_decorated.png");
            default:
                return MoCreatures.proxy.getModelTexture("elephant_african.png");
        }
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    public float calculateMaxHealth() {
        switch (getTypeMoC()) {
            case 1:
            case 5:
                return 40;
            case 3:
                return 50;
            case 4:
                return 60;
            default:
                return 30;
        }
    }

    @Override
    public double getCustomSpeed() {
        if (this.sitCounter != 0) {
            return 0D;
        }
        double tSpeed = 0.5D;
        if (getTypeMoC() == 1) {
            tSpeed = 0.55D;
        } else if (getTypeMoC() == 2) {
            tSpeed = 0.6D;
        } else if (getTypeMoC() == 4) {
            tSpeed = 0.55D;
        }
        if (this.sprintCounter > 0 && this.sprintCounter < 150) {
            tSpeed *= 1.5D;
        }
        if (this.sprintCounter > 150) {
            tSpeed *= 0.5D;
        }
        return tSpeed;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (!this.level().isClientSide()) {
            if ((this.sprintCounter > 0 && this.sprintCounter < 150) && (this.isVehicle()) && random.nextInt(15) == 0) {
                MoCTools.buckleMobsNotPlayers(this, 3D, this.level());
            }

            if (this.sprintCounter > 0 && ++this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }

            if (getIsTamed() && getIsAdult() && !this.isVehicle() && this.sitCounter == 0 && getArmorType() >= 1 && this.random.nextInt(20) == 0) {
                Player ep = this.level().getNearestPlayer(this, 3D);
                if (ep != null && (!MoCreatures.proxy.enableOwnership || ep.getUUID().equals(this.getOwnerId())) && ep.isCrouching()) {
                    sit();
                }
            }

            if (MoCreatures.proxy.elephantBulldozer && getIsTamed() && (this.isVehicle()) && (getTusks() > 0)) {
                int height = 2;
                if (getTypeMoC() == 3) {
                    height = 3;
                }
                if (getTypeMoC() == 4) {
                    height = 3;
                }
                int dmg = MoCTools.destroyBlocksInFront(this, 2D, this.getTusks(), height);
                checkTusks(dmg);
            }
        } else { //client only animation counters
            if (this.tailCounter > 0 && ++this.tailCounter > 8) {
                this.tailCounter = 0;
            }

            if (this.random.nextInt(200) == 0) {
                this.tailCounter = 1;
            }

            if (this.trunkCounter > 0 && ++this.trunkCounter > 38) {
                this.trunkCounter = 0;
            }

            if (this.trunkCounter == 0 && this.random.nextInt(200) == 0) {
                this.trunkCounter = random.nextInt(10) + 1;
            }

            if (this.earCounter > 0 && ++this.earCounter > 10) {
                this.earCounter = 0;
            }

            if (this.earCounter == 0 && this.random.nextInt(250) == 0) {
                this.earCounter = 1;
            }
        }

        if (this.sitCounter != 0 && ++this.sitCounter > 100) {
            this.sitCounter = 0;
        }
    }

    /**
     * Checks if the tusks sets need to break or not (wood = 59, stone = 131,
     * iron = 250, diamond = 1561, gold = 32)
     */
    private void checkTusks(int dmg) {
        this.tuskUses += (byte) dmg;
        if ((this.getTusks() == 1 && this.tuskUses > 59) || (this.getTusks() == 2 && this.tuskUses > 250)
                || (this.getTusks() == 3 && this.tuskUses > 1000)) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_TURTLE_HURT.get());
            setTusks((byte) 0);
        }
    }

    private void sit() {
        this.sitCounter = 1;
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToTracking(this, new MoCMessageAnimation(this.getId(), 0));
        }
        this.getNavigation().stop();
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType == 0) {
            this.sitCounter = 1;
            this.getNavigation().stop();
        } else if (animationType == 1) {
            this.sitCounter = 0;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        final Item item = stack.getItem();

        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }
        
        if (!stack.isEmpty() && !getIsTamed() && !getIsAdult() && stack.getItem() == Items.CAKE) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            this.temper += 2;
            this.setHealth(getMaxHealth());
            if (!this.level().isClientSide() && !getIsAdult() && !getIsTamed() && this.temper >= 10) {
                MoCTools.tameWithName(player, this);
            }
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && !getIsTamed() && !getIsAdult() && stack.getItem() == MoCItems.SUGAR_LUMP.get()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            this.temper += 1;
            this.setHealth(getMaxHealth());
            if (!this.level().isClientSide() && !getIsAdult() && !getIsTamed() && this.temper >= 10) {
                setTamed(true);
                MoCTools.tameWithName(player, this);
            }
            return InteractionResult.SUCCESS;
        }

        // Growth behavior reported in Clickibait and Pug's 1.0.5 community fix.
        if (getIsTamed() && !getIsAdult() && stack.is(MoCItems.SUGAR_LUMP.get())) {
            if (!this.level().isClientSide()) {
                setMoCAge(Math.min(getMoCAge() + 1, getMoCMaxAge()));
                if (!player.getAbilities().instabuild) stack.shrink(1);
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            }
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && stack.isEmpty() && !player.isShiftKeyDown() && getStorage() > 0 && this.sitCounter == 0) {
            // Open inventory
            if (!this.level().isClientSide()) {
                if (this.localelephantchest == null) {
                    initChest();
                }
                
                // Handle different chest configurations
                if (getStorage() == 1) {
                    player.openMenu(new SimpleMenuProvider(
                        (id, inventory, p) -> new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x2, id, inventory, this.localelephantchest, 2),
                        Component.translatable("container.elephant_chest")
                    ));
                } else if (getStorage() == 2) {
                    // Create a combined container with both chests
                    CombinedContainer combinedContainer = new CombinedContainer(
                        this.localelephantchest, 
                        this.localelephantchest2
                    );
                    // Two small chests = 18 + 18 = 36 slots = 4 rows
                    player.openMenu(new SimpleMenuProvider(
                            (id, inventory, p) -> new ChestMenu(MenuType.GENERIC_9x4, id, inventory, combinedContainer,
                                    4),
                            Component.translatable("container.elephant_chest")));
                } else if (getStorage() == 3) {
                    // Create a combined container with all three chests
                    CombinedContainer combinedContainer = new CombinedContainer(
                        this.localelephantchest,
                        this.localelephantchest2,
                        this.localelephantchest3
                    );
                    // Two small chests + one tiny chest = 18 + 18 + 9 = 45 slots = 5 rows
                    player.openMenu(new SimpleMenuProvider(
                            (id, inventory, p) -> new ChestMenu(MenuType.GENERIC_9x5, id, inventory, combinedContainer,
                                    5),
                            Component.translatable("container.elephant_chest")));
                } else if (getStorage() == 4) {
                    // Create a combined container with all four chests
                    CombinedContainer combinedContainer = new CombinedContainer(
                        this.localelephantchest,
                        this.localelephantchest2,
                        this.localelephantchest3,
                        this.localelephantchest4
                    );
                    // Two small chests + two tiny chests = 18 + 18 + 9 + 9 = 54 slots = 6 rows
                    player.openMenu(new SimpleMenuProvider(
                        (id, inventory, p) -> ChestMenu.sixRows(id, inventory, combinedContainer),
                        Component.translatable("container.elephant_chest")
                    ));
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (getStorage() < 2 && getIsTamed() && stack.is(MoCItems.ELEPHANTCHEST.get()) && getArmorType() != 0) {
            if (!this.level().isClientSide()) {
                this.setStorage(getStorage() + 1);
                initChest();
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (getStorage() < 4 && getStorage() > 1 && getIsTamed() && stack.is(Blocks.CHEST.asItem()) && getArmorType() != 0) {
            if (!this.level().isClientSide()) {
                this.setStorage(getStorage() + 1);
                initChest();
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (getArmorType() == 0 && stack.is(MoCItems.ELEPHANTHARNESS.get()) && getIsTamed()) {
            if (!this.level().isClientSide()) {
                this.setArmorType(1);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        //giving a garment to an indian elephant with a harness will make it pretty
        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && getArmorType() == 1 && getTypeMoC() == 2
                && stack.getItem() == MoCItems.ELEPHANTGARMENT.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ROPING.get());
            setArmorType((byte) 2);
            setTypeMoC(5);
            return InteractionResult.SUCCESS;
        }

        //giving a howdah to a pretty indian elephant with a garment will attach the howdah
        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && getArmorType() == 2 && getTypeMoC() == 5
                && stack.getItem() == MoCItems.ELEPHANTHOWDAH.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ROPING.get());
            setArmorType((byte) 3);
            return InteractionResult.SUCCESS;
        }

        //giving a platform to a ? mammoth with harness will attach the platform
        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && getArmorType() == 1 && getTypeMoC() == 4
                && stack.getItem() == MoCItems.MAMMOTHPLATFORM.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
            setArmorType((byte) 3);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && stack.getItem() == MoCItems.TUSKSWOOD.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
            dropTusks();
            this.tuskUses = (byte) stack.getDamageValue();
            setTusks((byte) 1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && stack.getItem() == MoCItems.TUSKSIRON.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
            dropTusks();
            this.tuskUses = (byte) stack.getDamageValue();
            setTusks((byte) 2);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && getIsAdult() && stack.getItem() == MoCItems.TUSKSDIAMOND.get()) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
            dropTusks();
            this.tuskUses = (byte) stack.getDamageValue();
            setTusks((byte) 3);
            return InteractionResult.SUCCESS;
        }

        if (player.isCrouching()) {
            initChest();
            if (getStorage() >= 1) {
                // Chest handling would go here with 1.20.1 menu system
                return InteractionResult.SUCCESS;
            }
        }
        
        if (!stack.isEmpty() && getTusks() > 0 && stack.is(ItemTags.PICKAXES)) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
            dropTusks();
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && getIsAdult() && getArmorType() >= 1 && this.sitCounter != 0) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                this.sitCounter = 0;
                MoCMessageHandler.sendToTracking(this, new MoCMessageAnimation(this.getId(), 1));
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    private void initChest() {
        if (getStorage() > 0 && this.localelephantchest == null) {
            this.localelephantchest = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.small);
        }

        if (getStorage() > 1 && this.localelephantchest2 == null) {
            this.localelephantchest2 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.small);
        }

        if (getStorage() > 2 && this.localelephantchest3 == null) {
            this.localelephantchest3 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.tiny);
        }

        if (getStorage() > 3 && this.localelephantchest4 == null) {
            this.localelephantchest4 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.tiny);
        }
    }

    /**
     * Drops tusks, makes sound
     */
    private void dropTusks() {
        if (this.level().isClientSide()) {
            return;
        }
        int i = getTusks();
        setTusks(0);
        if (i == 1) {
            ItemEntity entityitem =
                    new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(MoCItems.TUSKSWOOD.get(), 1));
            entityitem.getItem().setDamageValue(this.tuskUses);
            entityitem.setPickUpDelay(10);
            this.level().addFreshEntity(entityitem);
        }
        if (i == 2) {
            ItemEntity entityitem =
                    new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(MoCItems.TUSKSIRON.get(), 1));
            entityitem.getItem().setDamageValue(this.tuskUses);
            entityitem.setPickUpDelay(10);
            this.level().addFreshEntity(entityitem);
        }
        if (i == 3) {
            ItemEntity entityitem =
                    new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(MoCItems.TUSKSDIAMOND.get(), 1));
            entityitem.getItem().setDamageValue(this.tuskUses);
            entityitem.setPickUpDelay(10);
            this.level().addFreshEntity(entityitem);
        }
        setTusks((byte) 0);
    }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    /*@Override
    public boolean updateMount() {
        return getIsTamed();
    }
    */
    /*@Override
    public boolean forceUpdates() {
        return getIsTamed();
    }*/

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getBoundingBox().minY), Mth.floor(this.getZ()));
        ResourceKey<Biome> currentbiome = MoCTools.biomeKind(this.level(), pos);
        String biomeName = currentbiome.identifier().toString().toLowerCase();

        // African - savanna or desert biomes
        if (biomeName.contains("savanna") || biomeName.contains("desert")) {
            setTypeMoC(1);
            return true;
        }
        // Indian - jungle biomes
        if (biomeName.contains("jungle")) {
            setTypeMoC(2);
            return true;
        }
        // Mammoth - snowy biomes
        if (biomeName.contains("snowy") || biomeName.contains("frozen") || biomeName.contains("cold")) {
            setTypeMoC(3 + this.random.nextInt(2));
            return true;
        }

        return false;
    }

    @Override
    public float getSizeFactor() {
        float sizeF = 1.25F;

        switch (getTypeMoC()) {
            case 4:
                sizeF *= 1.2F;
                break;
            case 2:
            case 5:
                sizeF *= 0.80F;
                break;
        }

        if (!getIsAdult()) {
            sizeF = sizeF * (getMoCAge() * 0.01F);
        }
        return sizeF;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putInt("Tusks", getTusks());
        nbttagcompound.putInt("Harness", getArmorType());
        nbttagcompound.putInt("Storage", getStorage());
        nbttagcompound.putByte("Temper", this.temper);
        nbttagcompound.putByte("TuskUses", this.tuskUses);

        if (getStorage() > 0 && this.localelephantchest != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localelephantchest.getContainerSize(); i++) {
                this.localstack = this.localelephantchest.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items", nbttaglist);
        }

        if (getStorage() >= 2 && this.localelephantchest2 != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localelephantchest2.getContainerSize(); i++) {
                this.localstack = this.localelephantchest2.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items2", nbttaglist);
        }

        if (getStorage() >= 3 && this.localelephantchest3 != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localelephantchest3.getContainerSize(); i++) {
                this.localstack = this.localelephantchest3.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items3", nbttaglist);
        }

        if (getStorage() >= 4 && this.localelephantchest4 != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localelephantchest4.getContainerSize(); i++) {
                this.localstack = this.localelephantchest4.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items4", nbttaglist);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setTusks(nbttagcompound.getIntOr("Tusks", 0));
        setArmorType(nbttagcompound.getIntOr("Harness", 0));
        setStorage(nbttagcompound.getIntOr("Storage", 0));
        this.temper = nbttagcompound.getByteOr("Temper", (byte) 0);
        this.tuskUses = nbttagcompound.getByteOr("TuskUses", (byte) 0);
        if (getStorage() > 0) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items");
            this.localelephantchest = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.small);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localelephantchest.getContainerSize()) {
                    this.localelephantchest.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }
        if (getStorage() >= 2) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items2");
            this.localelephantchest2 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.small);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localelephantchest2.getContainerSize()) {
                    this.localelephantchest2.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }

        if (getStorage() >= 3) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items3");
            this.localelephantchest3 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.tiny);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localelephantchest3.getContainerSize()) {
                    this.localelephantchest3.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }
        if (getStorage() >= 4) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items4");
            this.localelephantchest4 = new MoCAnimalChest("ElephantChest", MoCAnimalChest.Size.tiny);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localelephantchest4.getContainerSize()) {
                    this.localelephantchest4.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty()
                && (stack.getItem() == Items.BAKED_POTATO || stack.getItem() == Items.BREAD || stack.getItem() == MoCItems.HAYSTACK.get());
    }

    @Override
    public boolean isMovementCeased() {
        return (this.isVehicle()) || this.sitCounter != 0;
    }

    @Override
    public void riding() {
        if (this.isVehicle() && this.getFirstPassenger() instanceof Player) {
            Player player = (Player) this.getFirstPassenger();
            List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D));
            
            for (Entity entity : list) {
                if (!entity.isRemoved() && this.distanceTo(entity) < 2.0D && !(entity instanceof Player)) {
                    entity.push(this);
                }
            }

            if (player.isCrouching()) {
                if (!this.level().isClientSide()) {
                    if (this.sitCounter == 0) {
                        sit();
                    }
                    if (this.sitCounter >= 50) {
                        player.stopRiding();
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    // Adapted from ItsFadedMaybe's elephant riding fix: use Minecraft's rider
    // positioning callback so the normal tick cannot move the passenger back.
    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (!this.hasPassenger(passenger)) {
            return;
        }
        double distance = switch (getTypeMoC()) {
            case 1, 3 -> 0.8D;
            case 2, 5 -> 0.1D;
            case 4 -> 1.2D;
            default -> 1.0D;
        };
        double yaw = Math.toRadians(this.getYRot() - 90F);
        moveFunction.accept(passenger,
                this.getX() - distance * Math.cos(yaw),
                this.getY() + getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y),
                this.getZ() - distance * Math.sin(yaw));
        passenger.setYRot(this.getYRot());
    }

    @Override
    public double getPassengersRidingOffset() {
        double yOff = 0F;
        boolean sit = (this.sitCounter != 0);

        switch (getTypeMoC()) {
            case 1:
            case 3:
                yOff = 0.55D;
                if (sit) {
                    yOff = -0.05D;
                }
                break;
            case 2:
            case 5:
                yOff = -0.17D;
                if (sit) {
                    yOff = -0.5D;
                }
                break;
            case 4:
                yOff = 1.2D;
                if (sit) {
                    yOff = 0.45D;
                }
                break;
        }
        return yOff + (this.getBbHeight() * 0.75D);
    }

    /**
     * Had to set to false to avoid damage due to the collision boxes
     */
    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_ELEPHANT_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_ELEPHANT_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!getIsAdult() && getMoCAge() < 80) {
            return MoCSoundEvents.ENTITY_ELEPHANT_AMBIENT_BABY.get();
        }
        return MoCSoundEvents.ENTITY_ELEPHANT_AMBIENT.get();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        int looting = MoCTools.lootingLevel(serverLevel, source);
        int i = this.random.nextInt(3);

        if (looting > 0) {
            i += this.random.nextInt(looting + 1);
        }
        this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(MoCItems.ANIMALHIDE.get(), i));
    }

    @Override
    public void dropMyStuff() {
        if (!this.level().isClientSide()) {
            dropTusks();
            //dropSaddle(this, world);
            if (getStorage() > 0) {
                if (getStorage() > 0) {
                    MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.ELEPHANTCHEST.get(), 1));
                    if (this.localelephantchest != null) {
                        MoCTools.dropInventory(this, this.localelephantchest);
                    }

                }
                if (getStorage() >= 2) {
                    if (this.localelephantchest2 != null) {
                        MoCTools.dropInventory(this, this.localelephantchest2);
                    }
                    MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.ELEPHANTCHEST.get(), 1));
                }
                if (getStorage() >= 3) {
                    if (this.localelephantchest3 != null) {
                        MoCTools.dropInventory(this, this.localelephantchest3);
                    }
                    MoCTools.dropCustomItem(this, this.level(), new ItemStack(Blocks.CHEST, 1));
                }
                if (getStorage() >= 4) {
                    if (this.localelephantchest4 != null) {
                        MoCTools.dropInventory(this, this.localelephantchest4);
                    }
                    MoCTools.dropCustomItem(this, this.level(), new ItemStack(Blocks.CHEST, 1));
                }
                setStorage((byte) 0);
            }
            dropArmor();
        }
    }

    @Override
    public void dropArmor() {
        if (this.level().isClientSide()) {
            return;
        }
        if (getArmorType() >= 1) {
            MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.ELEPHANTHARNESS.get(), 1));
        }
        if (getTypeMoC() == 5 && getArmorType() >= 2) {

            MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.ELEPHANTGARMENT.get(), 1));
            if (getArmorType() == 3) {
                MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.ELEPHANTHOWDAH.get(), 1));
            }
            setTypeMoC(2);
        }
        if (getTypeMoC() == 4 && getArmorType() == 3) {
            MoCTools.dropCustomItem(this, this.level(), new ItemStack(MoCItems.MAMMOTHPLATFORM.get(), 1));
        }
        setArmorType((byte) 0);

    }

    @Override
    public int nameYOffset() {
        int yOff = (int) ((100 / getMoCAge()) * (getSizeFactor() * -110));
        if (getIsAdult()) {
            yOff = (int) (getSizeFactor() * -110);
        }
        if (sitCounter != 0)
            yOff += 25;
        return yOff;
    }

    @Override
    public boolean isNotScared() {
        return getIsAdult() || getMoCAge() > 80 || getIsTamed();
    }
    
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float amount) {
        if (super.hurtServer(serverLevel, damagesource, amount)) {
            Entity entity = damagesource.getEntity();
            if ((entity != null && getIsTamed() && entity instanceof Player) || !(entity instanceof LivingEntity)) {
                return false;
            }
            if (this.isVehicle() && this.hasPassenger(entity)) {
                return true;
            }
            if (entity != this && super.shouldAttackPlayers()) {
                this.setTarget((LivingEntity) entity);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        int i = (int) Math.ceil(distance - 3F);
        if ((i > 0)) {
            i /= 2;
            if (i > 0) {
                this.hurtOrSimulate(this.damageSources().fall(), i);
            }
            if ((this.isVehicle()) && (i > 0)) {
                for (Entity entity : this.getPassengers()) {
                    entity.hurtOrSimulate(this.damageSources().fall(), i);
                }
            }
            BlockPos blockPos = this.blockPosition();
            BlockState blockState = this.level().getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (!this.isSilent()) {
                SoundType soundtype = block.getSoundType(blockState, this.level(), blockPos, this);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), soundtype.getStepSound(), this.getSoundSource(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
            return true;
        }
        return false;
    }
}
