/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.neutral;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;
import java.util.List;

public class MoCEntityOstrich extends MoCEntityTameableAnimal {
    private static final BiMap<DyeColor, Item> WOOL_BY_COLOR = Util.make(EnumHashBiMap.create(DyeColor.class), (p_203402_0_) -> {
        p_203402_0_.put(DyeColor.WHITE, Items.WOOL.pick(DyeColor.WHITE));
        p_203402_0_.put(DyeColor.ORANGE, Items.WOOL.pick(DyeColor.ORANGE));
        p_203402_0_.put(DyeColor.MAGENTA, Items.WOOL.pick(DyeColor.MAGENTA));
        p_203402_0_.put(DyeColor.LIGHT_BLUE, Items.WOOL.pick(DyeColor.LIGHT_BLUE));
        p_203402_0_.put(DyeColor.YELLOW, Items.WOOL.pick(DyeColor.YELLOW));
        p_203402_0_.put(DyeColor.LIME, Items.WOOL.pick(DyeColor.LIME));
        p_203402_0_.put(DyeColor.PINK, Items.WOOL.pick(DyeColor.PINK));
        p_203402_0_.put(DyeColor.GRAY, Items.WOOL.pick(DyeColor.GRAY));
        p_203402_0_.put(DyeColor.LIGHT_GRAY, Items.WOOL.pick(DyeColor.LIGHT_GRAY));
        p_203402_0_.put(DyeColor.CYAN, Items.WOOL.pick(DyeColor.CYAN));
        p_203402_0_.put(DyeColor.PURPLE, Items.WOOL.pick(DyeColor.PURPLE));
        p_203402_0_.put(DyeColor.BLUE, Items.WOOL.pick(DyeColor.BLUE));
        p_203402_0_.put(DyeColor.BROWN, Items.WOOL.pick(DyeColor.BROWN));
        p_203402_0_.put(DyeColor.GREEN, Items.WOOL.pick(DyeColor.GREEN));
        p_203402_0_.put(DyeColor.RED, Items.WOOL.pick(DyeColor.RED));
        p_203402_0_.put(DyeColor.BLACK, Items.WOOL.pick(DyeColor.BLACK));
    });

    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EGG_WATCH = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HIDING = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FERTILE = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HELMET_TYPE = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FLAG_COLOR = SynchedEntityData.defineId(MoCEntityOstrich.class, EntityDataSerializers.INT);
    public int mouthCounter;
    public int wingCounter;
    public int sprintCounter;
    public int jumpCounter;
    public int transformCounter;
    public int transformType;
    public MoCAnimalChest localchest;
    public ItemStack localstack;
    private int eggCounter;
    private int hidingCounter;
    private boolean isImmuneToFire;


    public MoCEntityOstrich(EntityType<? extends MoCEntityOstrich> type, Level world) {
        super(type, world);
        //setSize(0.8F, 2.225F);
        setAdult(true);
        setMoCAge(35);
        this.eggCounter = this.random.nextInt(1000) + 1000;
        // stepHeight renamed to maxUpStep in 1.20.1
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
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
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EGG_WATCH, Boolean.FALSE);
        builder.define(CHESTED, Boolean.FALSE);
        builder.define(RIDEABLE, Boolean.FALSE);
        builder.define(IS_HIDING, Boolean.FALSE);
        builder.define(FERTILE, Boolean.FALSE);
        builder.define(HELMET_TYPE, 0);
        builder.define(FLAG_COLOR, -1);
    }

    @Override
    public boolean getIsRideable() {
        return this.entityData.get(RIDEABLE);
    }

    @Override
    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    public boolean getEggWatching() {
        return this.entityData.get(EGG_WATCH);
    }

    public void setEggWatching(boolean flag) {
        this.entityData.set(EGG_WATCH, flag);
    }

    public boolean getHiding() {
        return this.entityData.get(IS_HIDING);
    }

    public void setHiding(boolean flag) {
        this.entityData.set(IS_HIDING, flag);
    }

    public int getHelmet() {
        return this.entityData.get(HELMET_TYPE);
    }

    public void setHelmet(int i) {
        this.entityData.set(HELMET_TYPE, i);
    }

    public DyeColor getFlagColor() {
        int i = this.entityData.get(FLAG_COLOR);
        return i == -1 ? null : DyeColor.byId(i);
    }

    public void setFlagColor(@Nullable DyeColor color) {
        this.entityData.set(FLAG_COLOR, color == null ? -1 : color.getId());
    }

    public int getFlagColorRaw() {
        return this.entityData.get(FLAG_COLOR);
    }

    public void setFlagColorRaw(int i) {
        this.entityData.set(FLAG_COLOR, i);
    }

    public boolean getIsChested() {
        return this.entityData.get(CHESTED);
    }

    public void setIsChested(boolean flag) {
        this.entityData.set(CHESTED, flag);
    }

    public boolean getIsFertile() {
        return this.entityData.get(FERTILE);
    }

    public void setFertile(boolean flag) {
        this.entityData.set(FERTILE, flag);
    }

    @Override
    public boolean isMovementCeased() {
        return (getHiding() || this.isVehicle());
    }

    @Override
    public boolean isNotScared() {
        return (getTypeMoC() == 2 && getTarget() != null) || (getTypeMoC() > 2);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        //dmg reduction
        if (getIsTamed() && getHelmet() != 0) {
            int j = 0;
            switch (getHelmet()) {
                case 1:
                    j = 1;
                    break;
                case 5:
                case 6:
                case 2:
                    j = 2;
                    break;
                case 7:
                case 3:
                    j = 3;
                    break;
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    j = 4;
                    break;
            }
            i -= j;
            if (i <= 0) {
                i = 1;
            }
        }

        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (!(entity instanceof LivingEntity) || ((this.isVehicle()) && (entity == this.getFirstPassenger())) || (entity instanceof Player && getIsTamed())) {
                return false;
            }

            if ((entity != this) && (super.shouldAttackPlayers()) && getTypeMoC() > 2) {
                setTarget((LivingEntity) entity);
                flapWings();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        dropMyStuff();
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        if (entityIn instanceof Player && !shouldAttackPlayers()) {
            return false;
        }
        openMouth();
        flapWings();
        return super.doHurtTarget(serverLevel, entityIn);
    }

    public float calculateMaxHealth() {
        switch (getTypeMoC()) {
            case 1:
                return 10;
            case 3:
            case 4:
                return 25;
            case 5:
            case 6:
                return 35;
            default:
                return 20;
        }
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * 0.75D) - 0.4D;
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            /*
             * 1 = chick /2 = female /3 = male /4 = albino male /5 = nether ostrich /6 = wyvern
             */
            int j = this.random.nextInt(100);
            if (j <= (20)) {
                setTypeMoC(1);
            } else if (j <= (65)) {
                setTypeMoC(2);
            } else if (j <= (95)) {
                setTypeMoC(3);
            } else {
                setTypeMoC(4);
            }
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculateMaxHealth());
        this.setHealth(getMaxHealth());
    }

    @Override
    public Identifier getTexture() {
        if (this.transformCounter != 0 && this.transformType > 4) {
            String newText = "ostrich_male.png";
            if (this.transformType == 5) {
                newText = "ostrich_fire.png";
            }
            if (this.transformType == 6) {
                newText = "ostrich_dark.png";
            }
            if (this.transformType == 7) {
                newText = "ostrich_undead.png";
            }
            if (this.transformType == 8) {
                newText = "ostrich_light.png";
            }

            if ((this.transformCounter % 5) == 0) {
                return MoCreatures.proxy.getModelTexture(newText);
            }
            if (this.transformCounter > 50 && (this.transformCounter % 3) == 0) {
                return MoCreatures.proxy.getModelTexture(newText);
            }
            if (this.transformCounter > 75 && (this.transformCounter % 4) == 0) {
                return MoCreatures.proxy.getModelTexture(newText);
            }
        }

        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("ostrich_baby.png");
            case 2:
                return MoCreatures.proxy.getModelTexture("ostrich_female.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("ostrich_white.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("ostrich_fire.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("ostrich_dark.png");
            case 7:
                return MoCreatures.proxy.getModelTexture("ostrich_undead.png");
            case 8:
                return MoCreatures.proxy.getModelTexture("ostrich_light.png");
            default:
                return MoCreatures.proxy.getModelTexture("ostrich_male.png");
        }
    }

    @Override
    public double getCustomSpeed() {
        double ostrichSpeed = 0.8D;
        if (getTypeMoC() == 3) {
            ostrichSpeed = 1.1D;
        } else if (getTypeMoC() == 4) {
            ostrichSpeed = 1.3D;
        } else if (getTypeMoC() == 5) {
            ostrichSpeed = 1.4D;
        }
        if (this.sprintCounter > 0 && this.sprintCounter < 200) {
            ostrichSpeed *= 1.5D;
        }
        if (this.sprintCounter > 200) {
            ostrichSpeed *= 0.5D;
        }
        return ostrichSpeed;
    }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (getHiding()) {
            this.yBodyRot = this.yHeadRot = this.getYRot();
        }

        if (this.mouthCounter > 0 && ++this.mouthCounter > 20) {
            this.mouthCounter = 0;
        }

        if (this.wingCounter > 0 && ++this.wingCounter > 80) {
            this.wingCounter = 0;
        }

        if (this.jumpCounter > 0 && ++this.jumpCounter > 8) {
            this.jumpCounter = 0;
        }

        if (this.sprintCounter > 0 && ++this.sprintCounter > 300) {
            this.sprintCounter = 0;
        }

        if (this.transformCounter > 0) {
            if (this.transformCounter == 40) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_TRANSFORM.get());
            }

            if (++this.transformCounter > 100) {
                this.transformCounter = 0;
                if (this.transformType != 0) {
                    dropArmor();
                    setTypeMoC(this.transformType);
                    selectType();
                }
            }
        }
    }

    public void transform(int tType) {
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), tType));
        }
        this.transformType = tType;
        if (!this.isVehicle() && this.transformType != 0) {
            dropArmor();
            this.transformCounter = 1;
        }
    }

    @Override
    public void performAnimation(int animationType) {
        if (animationType >= 5 && animationType < 9) //transform 5 - 8
        {
            this.transformType = animationType;
            this.transformCounter = 1;
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (getIsTamed() && !this.level().isClientSide() && (this.random.nextInt(300) == 0) && (getHealth() <= getMaxHealth()) && (this.deathTime == 0)) {
            this.setHealth(getHealth() + 1);
        }

        if (!this.level().isClientSide()) {
            //ostrich buckle!
            if (getTypeMoC() == 8 && (this.sprintCounter > 0 && this.sprintCounter < 150) && (this.isVehicle()) && random.nextInt(15) == 0) {
                MoCTools.buckleMobs(this, 2D, this.level());
            }

            // TODO
            //shy ostriches will run and hide
            /*if (!isNotScared() && fleeingTick > 0 && fleeingTick < 2) {
                fleeingTick = 0;
                setHiding(true);
                this.getNavigation().stop();
            }*/

            if (getHiding()) {
                //wild shy ostriches will hide their heads only for a short term
                //tamed ostriches will keep their heads hidden until the whip is used again
                if (++this.hidingCounter > 500 && !getIsTamed()) {
                    setHiding(false);
                    this.hidingCounter = 0;
                }
            }

            if (getTypeMoC() == 1 && (this.random.nextInt(200) == 0)) {
                //when is chick and becomes adult, change over to different type
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= 100) {
                    setAdult(true);
                    setTypeMoC(0);
                    selectType();
                }
            }

            //egg laying
            if (getIsFertile() && getTypeMoC() == 2 && !getEggWatching() && --this.eggCounter <= 0 && this.random.nextInt(5) == 0) {
                int ostrichEggType = 30;
                MoCEntityOstrich maleOstrich = getClosestMaleOstrich(this, 8D);
                if (maleOstrich != null && this.random.nextInt(100) < MoCreatures.proxy.ostrichEggDropChance) {
                    MoCEntityEgg entityegg = MoCEntities.EGG.get().create(this.level(), EntitySpawnReason.BREEDING);
                    entityegg.setEggType(ostrichEggType);
                    entityegg.setPos(this.getX(), this.getY(), this.getZ());
                    this.level().addFreshEntity(entityegg);

                    if (!this.getIsTamed()) {
                        setEggWatching(true);
                        maleOstrich.setEggWatching(true);
                        openMouth();
                    }

                    //TODO change sound
                    MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                    this.eggCounter = this.random.nextInt(2000) + 2000;
                    setFertile(false);
                }
            }

            //egg protection
            if (getEggWatching()) {
                //look for and protect eggs and move close
                MoCEntityEgg myEgg = (MoCEntityEgg) getBoogey(8D);
                if (myEgg != null && MoCTools.getSqDistanceTo(myEgg, this.getX(), this.getY(), this.getZ()) > 4D) {
                    Path pathEntity = this.getNavigation().createPath(myEgg.blockPosition(), 0);
                    this.getNavigation().moveTo(pathEntity, 2D);
                }
                //didn't find egg
                if (myEgg == null) {
                    setEggWatching(false);

                    Player eggStealer = this.level().getNearestPlayer(this, 10D);
                    if (eggStealer != null) {
                        this.level().getDifficulty();
                        if (!getIsTamed() && this.level().getDifficulty() != Difficulty.PEACEFUL) {
                            setTarget(eggStealer);
                            flapWings();
                        }
                    }
                }
            }
        }
    }

    protected MoCEntityOstrich getClosestMaleOstrich(Entity entity, double d) {
        double d1 = -1D;
        MoCEntityOstrich entityliving = null;
        List<MoCEntityOstrich> list = this.level().getEntitiesOfClass(MoCEntityOstrich.class, entity.getBoundingBox().inflate(d));
        for (MoCEntityOstrich entity1 : list) {
            if (entity1.getTypeMoC() < 3) {
                continue;
            }

            double d2 = entity1.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
            if (((d < 0.0D) || (d2 < (d * d))) && ((d1 == -1D) || (d2 < d1))) {
                d1 = d2;
                entityliving = entity1;
            }
        }
        return entityliving;
    }

    @Override
    public boolean entitiesToInclude(Entity entity) {
        return ((entity instanceof MoCEntityEgg) && (((MoCEntityEgg) entity).eggType == 30));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        final Item item = stack.getItem();
        
        if (getIsTamed() && stack.isEmpty() && !player.isShiftKeyDown() && getIsChested()) {
            // Open inventory
            if (!this.level().isClientSide()) {
                if (this.localchest == null) {
                    this.localchest = new MoCAnimalChest("OstrichChest", MoCAnimalChest.Size.tiny);
                }
                
                player.openMenu(new SimpleMenuProvider(
                    (id, inventory, p) -> new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x1, id, inventory, this.localchest, 1),
                    Component.translatable("container.ostrich_chest")
                ));
            }
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && (getTypeMoC() > 1) && !stack.isEmpty() && !getIsRideable() && (stack.is(Items.SADDLE) || stack.is(MoCItems.HORSE_SADDLE.get()))) {
            if (!this.level().isClientSide()) {
                if (!player.getAbilities().instabuild) stack.shrink(1);
                MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                setRideable(true);
            }
            return InteractionResult.SUCCESS;
        }

        if (!getIsFertile() && !stack.isEmpty() && getTypeMoC() == 2 && stack.getItem() == Items.MELON_SEEDS) {
            if (!player.getAbilities().instabuild) stack.shrink(1);

            openMouth();
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            setFertile(true);
            return InteractionResult.SUCCESS;
        }

        //makes the ostrich stay by hiding their heads
        if (!stack.isEmpty() && stack.is(MoCItems.WHIP.get()) && getIsTamed() && (!this.isVehicle())) {
            setHiding(!getHiding());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.getIsTamed() && getTypeMoC() > 1 && stack.is(MoCItems.ESSENCE_DARKNESS.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            if (getTypeMoC() == 6) {
                this.setHealth(getMaxHealth());
            } else {
                transform(6);
            }
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_DRINKING.get());
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.getIsTamed() && getTypeMoC() > 1 && stack.is(MoCItems.ESSENCE_UNDEAD.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            if (getTypeMoC() == 7) {
                this.setHealth(getMaxHealth());
            } else {
                transform(7);
            }
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_DRINKING.get());
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.getIsTamed() && getTypeMoC() > 1 && stack.is(MoCItems.ESSENCE_LIGHT.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            if (getTypeMoC() == 8) {
                this.setHealth(getMaxHealth());
            } else {
                transform(8);
            }
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_DRINKING.get());
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.getIsTamed() && getTypeMoC() > 1 && stack.is(MoCItems.ESSENCE_FIRE.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            if (getTypeMoC() == 5) {
                this.setHealth(getMaxHealth());
            } else {
                transform(5);
            }
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_DRINKING.get());
            return InteractionResult.SUCCESS;
        }
        
        if (getIsTamed() && getIsChested() && (getTypeMoC() > 1) && !stack.isEmpty() && stack.is(ItemTags.WOOL)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
            dropFlag();
            setFlagColor(WOOL_BY_COLOR.inverse().get(stack.getItem()));
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && (getTypeMoC() > 1) && getIsTamed() && !getIsChested() && (stack.getItem() == Blocks.CHEST.asItem())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);

            //entityplayer.inventory.addItemStackToInventory(new ItemStack(MoCreatures.key));
            setIsChested(true);
            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
            return InteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown() && getIsChested()) {
            // if first time opening a chest, we must initialize it
            if (this.localchest == null) {
                this.localchest = new MoCAnimalChest("OstrichChest", MoCAnimalChest.Size.tiny);
            }
            if (!this.level().isClientSide()) {
                player.openMenu(new SimpleMenuProvider(
                    (id, inventory, p) -> new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x1, id, inventory, this.localchest, 1),
                    Component.translatable("container.ostrich_chest")
                ));
            }
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && (getTypeMoC() > 1) && !stack.isEmpty()) {
            // Get the item without declaring a new variable
            Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
            if (equippable != null && equippable.slot() == EquipmentSlot.HEAD) {
                byte helmetType = 0;
                if (stack.getItem() == Items.LEATHER_HELMET) {
                    helmetType = 1;
                } else if (stack.getItem() == Items.IRON_HELMET) {
                    helmetType = 2;
                } else if (stack.getItem() == Items.GOLDEN_HELMET) {
                    helmetType = 3;
                } else if (stack.getItem() == Items.DIAMOND_HELMET) {
                    helmetType = 4;
                } else if (stack.getItem() == MoCItems.HELMET_HIDE.get()) {
                    helmetType = 5;
                } else if (stack.getItem() == MoCItems.HELMET_FUR.get()) {
                    helmetType = 6;
                } else if (stack.getItem() == MoCItems.HELMET_CROC.get()) {
                    helmetType = 7;
                } else if (stack.getItem() == MoCItems.HELMET_SCORP_D.get()) {
                    helmetType = 9;
                } else if (stack.getItem() == MoCItems.HELMET_SCORP_F.get()) {
                    helmetType = 10;
                } else if (stack.getItem() == MoCItems.HELMET_SCORP_C.get()) {
                    helmetType = 11;
                } else if (stack.getItem() == MoCItems.HELMET_SCORP_N.get()) {
                    helmetType = 12;
                }

                if (helmetType != 0) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    dropArmor();
                    this.setItemSlot(EquipmentSlot.HEAD, stack);
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
                    setHelmet(helmetType);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isShiftKeyDown()) && !this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setHiding(false);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    /**
     * Drops a block of the color of the flag if carrying one
     */
    private void dropFlag() {
        if (!this.level().isClientSide() && getFlagColor() != null) {
            DyeColor color = getFlagColor();
            ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(WOOL_BY_COLOR.get(color), 1));
            entityitem.setPickUpDelay(10);
            this.level().addFreshEntity(entityitem);
            setFlagColor(null);
        }
    }

    private void openMouth() {
        this.mouthCounter = 1;
    }

    private void flapWings() {
        this.wingCounter = 1;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        return MoCSoundEvents.ENTITY_OSTRICH_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        if (getTypeMoC() == 1) {
            return MoCSoundEvents.ENTITY_OSTRICH_AMBIENT_BABY.get();
        }
        return MoCSoundEvents.ENTITY_OSTRICH_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        openMouth();
        return MoCSoundEvents.ENTITY_OSTRICH_DEATH.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.OSTRICH;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        int looting = MoCTools.lootingLevel(serverLevel, source);
        boolean flag = (this.random.nextInt(100) < MoCreatures.proxy.rareItemDropChance);
        int i = this.random.nextInt(3);

        if (looting > 0)
        {
            i += this.random.nextInt(looting + 1);
        }
        if (flag && (this.getTypeMoC() == 8)) // unicorn
        {
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(MoCItems.UNICORNHORN.get(), i));
        }
        if (this.getTypeMoC() == 5 && flag) {
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(MoCItems.HEARTFIRE.get(), i));
        }
        if (this.getTypeMoC() == 6 && flag) // bat horse
        {
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(MoCItems.HEARTDARKNESS.get(), i));
        }
        if (this.getTypeMoC() == 7) {
            if (flag) {
                this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(MoCItems.HEARTUNDEAD.get(), i));
            }
            this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(Items.ROTTEN_FLESH, i));
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Saddle", getIsRideable());
        nbttagcompound.putBoolean("EggWatch", getEggWatching());
        nbttagcompound.putBoolean("Hiding", getHiding());
        nbttagcompound.putInt("Helmet", getHelmet());
        nbttagcompound.putInt("FlagColor", getFlagColorRaw());
        nbttagcompound.putBoolean("Bagged", getIsChested());
        nbttagcompound.putBoolean("Fertile", getIsFertile());

        if (getIsChested() && this.localchest != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localchest.getContainerSize(); i++) {
                this.localstack = this.localchest.getItem(i);
                if (!this.localstack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localstack, nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            MoCTools.writeTag(nbttagcompound, "Items", nbttaglist);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setRideable(nbttagcompound.getBooleanOr("Saddle", false));
        setEggWatching(nbttagcompound.getBooleanOr("EggWatch", false));
        setHiding(nbttagcompound.getBooleanOr("Hiding", false));
        setHelmet(nbttagcompound.getIntOr("Helmet", 0));
        setFlagColorRaw(nbttagcompound.getIntOr("FlagColor", 0));
        setIsChested(nbttagcompound.getBooleanOr("Bagged", false));
        setFertile(nbttagcompound.getBooleanOr("Fertile", false));

        if (getIsChested()) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items");
            this.localchest = new MoCAnimalChest("OstrichChest", MoCAnimalChest.Size.tiny);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localchest.getContainerSize()) {
                    this.localchest.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }
    }

    @Override
    public int nameYOffset() {
        if (getTypeMoC() > 1) {
            return -105;
        } else {
            return (-5 - getMoCAge());
        }
    }

    /*@Override
    public boolean updateMount() {
        return getIsTamed();
    }*/

    /* @Override
     public boolean forceUpdates() {
         return getIsTamed();
     }*/

    @Override
    public boolean isMyHealFood(ItemStack par1ItemStack) {
        return MoCTools.isItemEdible(par1ItemStack.getItem());
    }

    @Override
    public void dropMyStuff() {
        if (!this.level().isClientSide()) {
            dropArmor();
            MoCTools.dropSaddle(this, this.level());

            if (getIsChested()) {
                MoCTools.dropInventory(this, this.localchest);
                MoCTools.dropCustomItem(this, this.level(), new ItemStack(Blocks.CHEST, 1));
                setIsChested(false);
            }
        }
    }

    /**
     * Drops the helmet
     */
    @Override
    public void dropArmor() {
        if (!this.level().isClientSide()) {
            final ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!itemStack.isEmpty() && itemStack.has(DataComponents.EQUIPPABLE)) {
                final ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack.copy());
                entityitem.setPickUpDelay(10);
                this.level().addFreshEntity(entityitem);
            }
            setHelmet((byte) 0);
        }
    }

    @Override
    public boolean isFlyer() {
        return this.isVehicle() && (getTypeMoC() == 5 || getTypeMoC() == 6);
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        if (isFlyer()) {
            return false;
        }
        return super.causeFallDamage(distance, damageMultiplier, source);
    }

    @Override
    protected double myFallSpeed() {
        return 0.89D;
    }

    @Override
    protected double flyerThrust() {
        return 0.8D;
    }

    @Override
    protected float flyerFriction() {
        return 0.96F;
    }

    @Override
    protected boolean selfPropelledFlyer() {
        return getTypeMoC() == 6;
    }

    @Override
    public void makeEntityJump() {
        if (this.jumpCounter > 5) {
            this.jumpCounter = 1;
        }
        if (this.jumpCounter == 0) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_WINGFLAP.get());
            this.jumpPending = true;
            this.jumpCounter = 1;
        }
    }

    /** These variants are undead: healing harms them and harming heals them, as with vanilla undead. */
    @Override
    public boolean isInvertedHealAndHarm() {
        return getTypeMoC() == 7 || super.isInvertedHealAndHarm();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    //TODO 
    //improve fall flapping wing animation
    //IMPROVE DIVE CODE
    //ATTACK!
    //EGG LYING

    @Override
    public int getMoCMaxAge() {
        return 20;
    }

    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.945F));
    }

    @Override
    public void setTypeMoC(int i) {
        this.isImmuneToFire = i == 5;
        super.setTypeMoC(i);
    }

    @Override
    public boolean fireImmune() {
        return this.isImmuneToFire ? true : super.fireImmune();
    }

    // Helper method to handle armor types for ostrich
    private void updateItemReferences(ItemStack stack, EquipmentSlot slot) {
        // This is a stub method for resolving item fields
        // Will be replaced when field names are properly identified
    }
}
