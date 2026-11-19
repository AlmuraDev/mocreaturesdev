/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;
import net.minecraft.core.component.DataComponents;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFollowAdult;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.inventory.MoCAnimalChest;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import drzhark.mocreatures.network.message.MoCMessageVanish;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class MoCEntityHorse extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> RIDEABLE = SynchedEntityData.defineId(MoCEntityHorse.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(MoCEntityHorse.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(MoCEntityHorse.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BRED = SynchedEntityData.defineId(MoCEntityHorse.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ARMOR_TYPE = SynchedEntityData.defineId(MoCEntityHorse.class, EntityDataSerializers.INT);
    public int shuffleCounter;
    public int wingFlapCounter;
    public MoCAnimalChest localChest;
    public boolean eatenPumpkin;
    public ItemStack localStack;
    public int mouthCounter;
    public int standCounter;
    public int tailCounter;
    public int vanishCounter;
    public int sprintCounter;
    public int transformType;
    public int transformCounter;
    protected EntityAIWanderMoC2 wander;
    private int gestationTime;
    private int countEating;
    private int textCounter;
    private int fCounter;
    private float transFloat = 0.2F;
    private boolean hasReproduced;
    private int nightmareInt;
    private boolean isImmuneToFire;

    public MoCEntityHorse(EntityType<? extends MoCEntityHorse> type, Level world) {
        super(type, world);
        this.gestationTime = 0;
        this.eatenPumpkin = false;
        this.nightmareInt = 0;
        this.isImmuneToFire = false;
        setMoCAge(50);
        setIsChested(false);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);

        if (!this.level().isClientSide()) {
            setAdult(this.random.nextInt(5) != 0);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(4, this.wander = new EntityAIWanderMoC2(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RIDEABLE, Boolean.FALSE); // rideable: 0 nothing, 1 saddle
        builder.define(SITTING, Boolean.FALSE); // rideable: 0 nothing, 1 saddle
        builder.define(CHESTED, Boolean.FALSE);
        builder.define(BRED, Boolean.FALSE);
        builder.define(ARMOR_TYPE, 0);
    }

    @Override
    public int getArmorType() {
        return this.entityData.get(ARMOR_TYPE);
    }

    @Override
    public void setArmorType(int i) {
        this.entityData.set(ARMOR_TYPE, i);
    }

    public boolean getIsChested() {
        return this.entityData.get(CHESTED);
    }

    public void setIsChested(boolean flag) {
        this.entityData.set(CHESTED, flag);
    }

    @Override
    public boolean getIsSitting() {
        return this.entityData.get(SITTING);
    }

    public boolean getHasBred() {
        return this.entityData.get(BRED);
    }

    public void setBred(boolean flag) {
        this.entityData.set(BRED, flag);
    }

    @Override
    public boolean getIsRideable() {
        return this.entityData.get(RIDEABLE);
    }

    @Override
    public void setRideable(boolean flag) {
        this.entityData.set(RIDEABLE, flag);
    }

    public void setSitting(boolean flag) {
        this.entityData.set(SITTING, flag);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if ((this.isVehicle()) && (entity == this.getVehicle())) return false;
        if (entity instanceof Wolf) {
            Mob entitycreature = (Mob) entity;
            entitycreature.setTarget(null);
            return false;
        } else {
            i = i - (getArmorType() + 2);
            if (i < 0F) i = 0F;
            return super.hurtServer(serverLevel, damagesource, i);
        }
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        ResourceKey<net.minecraft.world.level.biome.Biome> currentbiome = MoCTools.biomeKind(this.level(), pos);
        try {
            if (currentbiome.identifier().getPath().contains("savanna")) setTypeMoC(60); // zebra
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * returns one of the RGB color codes
     *
     * @param sColor  : 1 will return the Red component, 2 will return the Green
     *                and 3 the blue
     * @param typeInt : which set of colors to inquiry about, corresponds with
     *                the horse types.
     */
    public float colorFX(int sColor, int typeInt) {
        switch (typeInt) {
            case 23: // green for undeads
            case 24: // green for undeads
            case 25: // green for undeads
                if (sColor == 1) {
                    return (float) 60 / 256;
                }
                if (sColor == 2) {
                    return (float) 179 / 256;
                }
                return (float) 112 / 256;
            case 40: // dark red for black pegasus
                if (sColor == 1) {
                    return (float) 139 / 256;
                }
                if (sColor == 2) {
                    return 0F;
                }
                return 0F;
            case 48: // yellow
                if (sColor == 1) {
                    return (float) 179 / 256;
                } else if (sColor == 2) {
                    return (float) 160 / 256;
                }
                return (float) 22 / 256;
            case 49: // purple
                if (sColor == 1) {
                    return (float) 147 / 256;
                } else if (sColor == 2) {
                    return (float) 90 / 256;
                }
                return (float) 195 / 256;
            case 51: // blue
                if (sColor == 1) {
                    return (float) 30 / 256;
                } else if (sColor == 2) {
                    return (float) 144 / 256;
                }
                return (float) 255 / 256;
            case 52: // pink
                if (sColor == 1) {
                    return (float) 255 / 256;
                }
                if (sColor == 2) {
                    return (float) 105 / 256;
                }
                return (float) 180 / 256;
            case 53: // lightgreen
                if (sColor == 1) {
                    return (float) 188 / 256;
                }
                if (sColor == 2) {
                    return (float) 238 / 256;
                }
                return (float) 104 / 256;
            case 54: // black fairy
                if (sColor == 1) {
                    return (float) 110 / 256;
                }
                if (sColor == 2) {
                    return (float) 123 / 256;
                }
                return (float) 139 / 256;
            case 55: // red fairy
                if (sColor == 1) {
                    return (float) 194 / 256;
                }
                if (sColor == 2) {
                    return (float) 29 / 256;
                }
                return (float) 34 / 256;
            case 56: // dark blue fairy
                if (sColor == 1) {
                    return (float) 63 / 256;
                }
                if (sColor == 2) {
                    return (float) 45 / 256;
                }
                return (float) 255 / 256;
            case 57: // cyan
                if (sColor == 1) {
                    return (float) 69 / 256;
                }
                if (sColor == 2) {
                    return (float) 146 / 256;
                }
                return (float) 145 / 256;
            case 58: // green
                if (sColor == 1) {
                    return (float) 90 / 256;
                }
                if (sColor == 2) {
                    return (float) 136 / 256;
                }
                return (float) 43 / 256;
            case 59: // orange
                if (sColor == 1) {
                    return (float) 218 / 256;
                }
                if (sColor == 2) {
                    return (float) 40 / 256;
                }
                return (float) 0 / 256;
            default: // by default will return clear gold
                if (sColor == 1) {
                    return (float) 255 / 256;
                } else if (sColor == 2) {
                    return (float) 236 / 256;
                } else {
                    return (float) 139 / 256;
                }
        }
    }

    /**
     * Called to vanish a Horse without FX
     */
    public void dissapearHorse() {
        this.discard();
    }

    /** Fairy breeding consumes a parent, but its equipment belongs to the player. */
    public void disappearAfterBreeding() {
        if (!this.level().isClientSide() && !this.isRemoved()) {
            this.dropMyStuff();
            this.discard();
        }
    }

    private void drinkingHorse() {
        openMouth();
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_DRINKING.get());
    }

    /**
     * Drops the current armor if the horse has one
     */
    @Override
    public void dropArmor() {
        if (this.level().isClientSide()) return;
        int armorType = this.getArmorType();
        if (armorType != 0) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_OFF.get());
        ItemStack armorStack;
        switch (armorType) {
            case 1:
                armorStack = new ItemStack(Items.IRON_HORSE_ARMOR, 1);
                break;
            case 2:
                armorStack = new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1);
                break;
            case 3:
                armorStack = new ItemStack(Items.DIAMOND_HORSE_ARMOR, 1);
                break;
            case 4:
                armorStack = new ItemStack(MoCItems.HORSEARMORCRYSTAL.get(), 1);
                break;
            default:
                return; // No armor to drop
        }
        ItemEntity entityItem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), armorStack);
        entityItem.setPickUpDelay(10);
        this.level().addFreshEntity(entityItem);
        setArmorType(0);
    }

    /**
     * Drops a chest block if the horse is bagged
     */
    public void dropBags() {
        if (!isBagger() || !getIsChested() || this.level().isClientSide()) {
            return;
        }

        ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(Blocks.CHEST, 1));
        double f3 = 0.05D;
        entityitem.setDeltaMovement(this.level().getRandom().nextGaussian() * f3, (this.level().getRandom().nextGaussian() * f3) + 0.2D, this.level().getRandom().nextGaussian() * f3);
        this.level().addFreshEntity(entityitem);
        setIsChested(false);
    }

    private void eatingHorse() {
        openMouth();
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
    }

    // Method has been completely replaced by causeFallDamage in the updated version at the end of the file

    public MoCAnimalChest.Size getInventorySize() {
        if (getTypeMoC() == 40) return MoCAnimalChest.Size.small;
        if (getTypeMoC() > 64) return MoCAnimalChest.Size.medium;
        return MoCAnimalChest.Size.tiny;
    }

    @Override
    public double getCustomJump() {
        double horseJump = 0.35D;
        if (getTypeMoC() < 6) // tier 1
        {
            horseJump = 0.35;
        } else if (getTypeMoC() > 5 && getTypeMoC() < 11) // tier 2
        {
            horseJump = 0.40D;
        } else if (getTypeMoC() > 10 && getTypeMoC() < 16) // tier 3
        {
            horseJump = 0.45D;
        } else if (getTypeMoC() > 15 && getTypeMoC() < 21) // tier 4
        {
            horseJump = 0.50D;
        } else if (getTypeMoC() > 20 && getTypeMoC() < 26) // ghost and undead
        {
            horseJump = 0.45D;
        } else if (getTypeMoC() > 25 && getTypeMoC() < 30) // skelly
        {
            horseJump = 0.5D;
        } else if (getTypeMoC() >= 30 && getTypeMoC() < 40) // magics
        {
            horseJump = 0.55D;
        } else if (getTypeMoC() >= 40 && getTypeMoC() < 60) // black pegasus and fairies
        {
            horseJump = 0.6D;
        } else if (getTypeMoC() >= 60) // donkeys - zebras and the like
        {
            horseJump = 0.4D;
        }
        return horseJump;
    }

    @Override
    public double getCustomSpeed() {
        double horseSpeed = 0.8D;
        if (getTypeMoC() < 6) // tier 1
        {
            horseSpeed = 0.9;
        } else if (getTypeMoC() > 5 && getTypeMoC() < 11) // tier 2
        {
            horseSpeed = 1.0D;
        } else if (getTypeMoC() > 10 && getTypeMoC() < 16) // tier 3
        {
            horseSpeed = 1.1D;
        } else if (getTypeMoC() > 15 && getTypeMoC() < 21) // tier 4
        {
            horseSpeed = 1.2D;
        } else if (getTypeMoC() > 20 && getTypeMoC() < 26) // ghost and undead
        {
            horseSpeed = 0.8D;
        } else if (getTypeMoC() > 25 && getTypeMoC() < 30) // skelly
        {
            horseSpeed = 1.0D;
        } else if (getTypeMoC() > 30 && getTypeMoC() < 40) // magics
        {
            horseSpeed = 1.2D;
        } else if (getTypeMoC() >= 40 && getTypeMoC() < 60) // black pegasus and fairies
        {
            horseSpeed = 1.3D;
        } else if (getTypeMoC() == 60 || getTypeMoC() == 61) // zebras and zorse
        {
            horseSpeed = 1.1D;
        } else if (getTypeMoC() == 65) // donkeys
        {
            horseSpeed = 0.7D;
        } else if (getTypeMoC() > 65) // mule and zorky
        {
            horseSpeed = 0.9D;
        }
        if (this.sprintCounter > 0 && this.sprintCounter < 150) {
            horseSpeed *= 1.5D;
        }
        if (this.sprintCounter > 150) {
            horseSpeed *= 0.5D;
        }
        return horseSpeed;
    }

    @Override
    protected SoundEvent getDeathSound() {
        openMouth();
        if (this.isUndead()) return MoCSoundEvents.ENTITY_HORSE_DEATH_UNDEAD.get();
        if (this.getIsGhost()) return MoCSoundEvents.ENTITY_HORSE_DEATH_GHOST.get();
        if (this.getTypeMoC() == 60 || this.getTypeMoC() == 61) return MoCSoundEvents.ENTITY_HORSE_HURT_ZEBRA.get();
        if (this.getTypeMoC() > 64 && this.getTypeMoC() < 68) return MoCSoundEvents.ENTITY_HORSE_DEATH_DONKEY.get();
        return MoCSoundEvents.ENTITY_HORSE_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (!blockIn.getFluidState().isEmpty()) {
            SoundType soundtype = blockIn.getSoundType(level(), pos, this);

            if (this.level().getBlockState(pos.above()).is(Blocks.SNOW)) {
                soundtype = Blocks.SNOW.getSoundType(blockIn, level(), pos, this);
            }

            if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound(SoundEvents.HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
        }
    }

    @Override
    public boolean renderName() {
        if (getIsGhost() && getMoCAge() < 10) return false;
        return super.renderName();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        int looting = MoCTools.lootingLevel(serverLevel, source);
        super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
        
        boolean flag = (this.random.nextInt(100) < MoCreatures.proxy.rareItemDropChance);

        Item drop = Items.LEATHER;

        if (flag && (this.getTypeMoC() == 36 || (this.getTypeMoC() > 49 && this.getTypeMoC() < 60))) // unicorn
        {
            drop = MoCItems.UNICORNHORN.get();
        }
        if (this.getTypeMoC() == 39 || this.getTypeMoC() == 40) // (dark) pegasus
        {
            drop = Items.FEATHER;
        }
        if (this.getTypeMoC() == 38 && flag && this.level().environmentAttributes().getDimensionValue(EnvironmentAttributes.WATER_EVAPORATES)) // nightmare
        {
            drop = MoCItems.HEARTFIRE.get();
        }
        if (this.getTypeMoC() == 32 && flag) // bat horse
        {
            drop = MoCItems.HEARTDARKNESS.get();
        }
        if (this.getTypeMoC() == 26)// skelly
        {
            drop = Items.BONE;
        }
        if ((this.getTypeMoC() == 23 || this.getTypeMoC() == 24 || this.getTypeMoC() == 25)) {
            if (flag) {
                drop = MoCItems.HEARTUNDEAD.get();
            } else {
                drop = Items.ROTTEN_FLESH;
            }
        }
        if (this.getTypeMoC() == 21 || this.getTypeMoC() == 22) {
            drop = Items.GHAST_TEAR;
        }

        int i = this.random.nextInt(3);

        if (looting > 0)
        {
            i += this.random.nextInt(looting + 1);
        }

        this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(drop, i));
    }

    public boolean getHasReproduced() {
        return this.hasReproduced;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        openMouth();
        if (isFlyer() && !this.isVehicle()) wingFlap();
        else if (this.random.nextInt(3) == 0) stand();

        if (this.isUndead()) return MoCSoundEvents.ENTITY_HORSE_HURT_UNDEAD.get();
        if (this.getIsGhost()) return MoCSoundEvents.ENTITY_HORSE_HURT_GHOST.get();
        if (this.getTypeMoC() == 60 || this.getTypeMoC() == 61) return MoCSoundEvents.ENTITY_HORSE_HURT_ZEBRA.get();
        if (this.getTypeMoC() > 64 && this.getTypeMoC() < 68) return MoCSoundEvents.ENTITY_HORSE_HURT_DONKEY.get();

        return MoCSoundEvents.ENTITY_HORSE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        openMouth();
        if (this.random.nextInt(10) == 0 && !isMovementCeased()) stand();

        if (this.isUndead()) return MoCSoundEvents.ENTITY_HORSE_AMBIENT_UNDEAD.get();
        if (this.getIsGhost()) return MoCSoundEvents.ENTITY_HORSE_AMBIENT_GHOST.get();
        if (this.getTypeMoC() == 60 || this.getTypeMoC() == 61) return MoCSoundEvents.ENTITY_HORSE_AMBIENT_ZEBRA.get();
        if (this.getTypeMoC() > 64 && this.getTypeMoC() < 68) return MoCSoundEvents.ENTITY_HORSE_HURT_DONKEY.get();

        return MoCSoundEvents.ENTITY_HORSE_AMBIENT.get();
    }

    /**
     * sound played when an untamed mount buckles rider
     */
    @Override
    protected SoundEvent getAngrySound() {
        openMouth();
        stand();
        if (this.isUndead()) return MoCSoundEvents.ENTITY_HORSE_ANGRY_UNDEAD.get();
        if (this.getIsGhost()) return MoCSoundEvents.ENTITY_HORSE_ANGRY_GHOST.get();
        if (this.getTypeMoC() == 60 || this.getTypeMoC() == 61) return MoCSoundEvents.ENTITY_HORSE_HURT_ZEBRA.get();
        if (this.getTypeMoC() > 64 && this.getTypeMoC() < 68) return MoCSoundEvents.ENTITY_HORSE_HURT_DONKEY.get();
        return MoCSoundEvents.ENTITY_HORSE_MAD.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    public float calculateMaxHealth() {
        int maximumHealth = 30;
        if (getTypeMoC() < 6) // tier 1
        {
            maximumHealth = 25;
        } else if (getTypeMoC() > 5 && getTypeMoC() < 11) // tier 2
        {
            maximumHealth = 30;
        } else if (getTypeMoC() > 10 && getTypeMoC() < 16) // tier 3
        {
            maximumHealth = 35;
        } else if (getTypeMoC() > 15 && getTypeMoC() < 21) // tier 4
        {
            maximumHealth = 40;
        } else if (getTypeMoC() > 20 && getTypeMoC() < 26) // ghost and undead
        {
            maximumHealth = 35;
        } else if (getTypeMoC() > 25 && getTypeMoC() < 30) // skelly
        {
            maximumHealth = 35;
        } else if (getTypeMoC() >= 30 && getTypeMoC() < 40) // magics
        {
            maximumHealth = 50;
        } else if (getTypeMoC() == 40) // black pegasus
        {
            maximumHealth = 50;
        } else if (getTypeMoC() > 40 && getTypeMoC() < 60) // fairies
        {
            maximumHealth = 40;
        } else if (getTypeMoC() >= 60) // donkeys - zebras and the like
        {
            maximumHealth = 30;
        }

        return maximumHealth;
    }

    /**
     * How difficult is the creature to be tamed? the Higher the number, the
     * more difficult
     */
    @Override
    public int getMaxTemper() {
        if (getTypeMoC() == 60) return 200; // zebras are harder to tame
        return 100;
    }

    public int getNightmareInt() {
        return this.nightmareInt;
    }

    public void setNightmareInt(int i) {
        this.nightmareInt = i;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    /**
     * Overridden for the dynamic nightmare texture.
     */
    @Override
    public Identifier getTexture() {
        String tempTexture;

        switch (getTypeMoC()) {
            case 1:
                tempTexture = "horsewhite.png";
                break;
            case 2:
                tempTexture = "horsecreamy.png";
                break;
            case 3:
                tempTexture = "horsebrown.png";
                break;
            case 4:
                tempTexture = "horsedarkbrown.png";
                break;
            case 5:
                tempTexture = "horseblack.png";
                break;
            case 6:
                tempTexture = "horsebrightcreamy.png";
                break;
            case 7:
                tempTexture = "horsespeckled.png";
                break;
            case 8:
                tempTexture = "horsepalebrown.png";
                break;
            case 9:
                tempTexture = "horsegrey.png";
                break;
            case 11:
                tempTexture = "horsepinto.png";
                break;
            case 12:
                tempTexture = "horsebrightpinto.png";
                break;
            case 13:
                tempTexture = "horsepalespeckles.png";
                break;
            case 16:
                tempTexture = "horsespotted.png";
                break;
            case 17:
                tempTexture = "horsecow.png";
                break;
            case 21:
                tempTexture = "horseghost.png";
                break;
            case 22:
                tempTexture = "horseghostb.png";
                break;
            case 23:
                tempTexture = "horseundead.png";
                break;
            case 24:
                tempTexture = "horseundeadunicorn.png";
                break;
            case 25:
                tempTexture = "horseundeadpegasus.png";
                break;
            case 26:
                tempTexture = "horseskeleton.png";
                break;
            case 27:
                tempTexture = "horseunicornskeleton.png";
                break;
            case 28:
                tempTexture = "horsepegasusskeleton.png";
                break;
            case 32:
                tempTexture = "horsebat.png";
                break;
            case 36:
                tempTexture = "horseunicorn.png";
                break;
            case 38:
                //this.isImmuneToFire = true;
                tempTexture = "horsenightmare.png";
                break;
            case 39:
                tempTexture = "horsepegasus.png";
                break;
            case 40:
                //this.isImmuneToFire = true;
                tempTexture = "horsedarkpegasus.png";
                break;
            /*
             * case 44: tempTexture = "horsefairydarkblue.png"; break; case 45:
             * tempTexture = "horsefairydarkblue.png"; break; case 46:
             * tempTexture = "horsefairydarkblue.png"; break; case 47:
             * tempTexture = "horsefairydarkblue.png"; break;
             */
            case 48:
                tempTexture = "horsefairyyellow.png";
                break;
            case 49:
                tempTexture = "horsefairypurple.png";
                break;
            case 50:
                tempTexture = "horsefairywhite.png";
                break;
            case 51:
                tempTexture = "horsefairyblue.png";
                break;
            case 52:
                tempTexture = "horsefairypink.png";
                break;
            case 53:
                tempTexture = "horsefairylightgreen.png";
                break;
            case 54:
                tempTexture = "horsefairyblack.png";
                break;
            case 55:
                tempTexture = "horsefairyred.png";
                break;
            case 56:
                tempTexture = "horsefairydarkblue.png";
                break;
            case 57:
                tempTexture = "horsefairycyan.png";
                break;
            case 58:
                tempTexture = "horsefairygreen.png";
                break;
            case 59:
                tempTexture = "horsefairyorange.png";
                break;
            case 60:
                tempTexture = "horsezebra.png";
                break;
            case 61:
                tempTexture = "horsezorse.png";
                break;
            case 65:
                tempTexture = "horsedonkey.png";
                break;
            case 66:
                tempTexture = "horsemule.png";
                break;
            case 67:
                tempTexture = "horsezonky.png";
                break;
            default:
                tempTexture = "horsebug.png";
        }

        if ((isArmored() || isMagicHorse()) && getArmorType() > 0) {
            String armorTex = "";
            if (getArmorType() == 1) armorTex = "metal.png";
            if (getArmorType() == 2) armorTex = "gold.png";
            if (getArmorType() == 3) armorTex = "diamond.png";
            if (getArmorType() == 4) armorTex = "crystaline.png";
            return MoCreatures.proxy.getModelTexture(tempTexture.replace(".png", armorTex));
        }

        if (this.isUndead() && this.getTypeMoC() < 26) {
            String baseTex = "horseundead";
            int max = 79;
            if (this.getTypeMoC() == 25) // undead pegasus
            {
                baseTex = "horseundeadpegasus";
                // max = 79; //undead pegasus have an extra animation

            }
            if (this.getTypeMoC() == 24)// undead unicorn
            {
                baseTex = "horseundeadunicorn";
                max = 69; // undead unicorn have an animation less
            }

            String iteratorTex = "1";
            if (MoCreatures.proxy.getAnimateTextures()) {
                if (this.random.nextInt(3) == 0) this.textCounter++;
                if (this.textCounter < 10) this.textCounter = 10;
                if (this.textCounter > max) this.textCounter = 10;
                iteratorTex = String.valueOf(this.textCounter);
                iteratorTex = iteratorTex.substring(0, 1);
            }

            String decayTex = String.valueOf(getMoCAge() / 100);
            decayTex = decayTex.substring(0, 1);
            return MoCreatures.proxy.getModelTexture(baseTex + decayTex + iteratorTex + ".png");
        }

        // if animate textures is off, return plain textures
        if (!MoCreatures.proxy.getAnimateTextures()) {
            return MoCreatures.proxy.getModelTexture(tempTexture);
        }

        if (this.isNightmare()) {
            this.random.nextInt(1);
            this.textCounter++;
            if (this.textCounter < 10) this.textCounter = 10;
            if (this.textCounter > 59) this.textCounter = 10;
            String NTA = "horsenightmare";
            String NTB = String.valueOf(this.textCounter);
            NTB = NTB.substring(0, 1);
            String NTC = ".png";

            return MoCreatures.proxy.getModelTexture(NTA + NTB + NTC);
        }

        if (this.transformCounter != 0 && this.transformType != 0) {
            String newText;
            switch (this.transformType) {
                case 24:
                    newText = "horseundeadunicorn.png";
                    break;
                case 25:
                    newText = "horseundeadpegasus.png";
                    break;
                case 32:
                    newText = "horsebat.png";
                    break;
                case 36:
                    newText = "horseunicorn.png";
                    break;
                case 38:
                    newText = "horsenightmare1.png";
                    break;
                case 39:
                    newText = "horsepegasus.png";
                    break;
                case 40:
                    newText = "horseblackpegasus.png";
                    break;
                case 48:
                    newText = "horsefairyyellow.png";
                    break;
                case 49:
                    newText = "horsefairypurple.png";
                    break;
                case 50:
                    newText = "horsefairywhite.png";
                    break;
                case 51:
                    newText = "horsefairyblue.png";
                    break;
                case 52:
                    newText = "horsefairypink.png";
                    break;
                case 53:
                    newText = "horsefairylightgreen.png";
                    break;
                case 54:
                    newText = "horsefairyblack.png";
                    break;
                case 55:
                    newText = "horsefairyred.png";
                    break;
                case 56:
                    newText = "horsefairydarkblue.png";
                    break;
                case 57:
                    newText = "horsefairycyan.png";
                    break;
                case 58:
                    newText = "horsefairygreen.png";
                    break;
                case 59:
                    newText = "horsefairyorange.png";
                    break;
                default:
                    newText = "horseundead.png";
                    break;
            }

            if (this.transformCounter > 75 && this.transformCounter % 4 == 0)
                return MoCreatures.proxy.getModelTexture(newText);
        }

        return MoCreatures.proxy.getModelTexture(tempTexture);
    }

    /**
     * New networked to fix SMP issues
     */
    public byte getVanishC() {
        return (byte) this.vanishCounter;
    }

    /**
     * New networked to fix SMP issues
     */
    public void setVanishC(byte i) {
        this.vanishCounter = i;
    }

    /**
     * Breeding rules for the horses
     */
    //private int horseGenetics(MoCEntityHorse entityhorse, MoCEntityHorse entityhorse1)
    private int horseGenetics(int typeA, int typeB) {
        boolean flag = MoCreatures.proxy.easyHorseBreeding;
        //int typeA = entityhorse.getTypeMoC();
        //int typeB = entityhorse1.getTypeMoC();

        // identical horses have so spring
        if (typeA == typeB) {
            return typeA;
        }

        // zebras plus any horse
        if (typeA == 60 && typeB < 21 || typeB == 60 && typeA < 21) {
            return 61; // zorse
        }

        // dokey plus any horse
        if (typeA == 65 && typeB < 21 || typeB == 65 && typeA < 21) {
            return 66; // mule
        }

        // zebra plus donkey
        if (typeA == 60 && typeB == 65 || typeB == 60 && typeA == 65) {
            return 67; // zonky
        }

        if (typeA > 20 && typeB < 21 || typeB > 20 && typeA < 21) // rare horses plus  ordinary horse always returns ordinary horse
        {
            return Math.min(typeA, typeB);
        }

        // unicorn plus white pegasus (they will both vanish!)
        if (typeA == 36 && typeB == 39 || typeB == 36 && typeA == 39) {
            return 50; // white fairy
        }

        // unicorn plus black pegasus (they will both vanish!)
        if (typeA == 36 && typeB == 40 || typeB == 36 && typeA == 40) {
            return 54; // black fairy
        }

        // rare horse mixture: produces a regular horse 1-5
        if (typeA > 20) {
            return (this.random.nextInt(5)) + 1;
        }

        // rest of cases will return either typeA, typeB or new mix
        if (!flag) {
            int chanceInt = (this.random.nextInt(4)) + 1;
            // 25%
            if (chanceInt == 1) return typeA;
            // 25%
            if (chanceInt == 2) return typeB;
        }

        if ((typeA == 1 && typeB == 2) || (typeA == 2 && typeB == 1)) {
            return 6;
        }

        if ((typeA == 1 && typeB == 3) || (typeA == 3 && typeB == 1)) {
            return 2;
        }

        if ((typeA == 1 && typeB == 4) || (typeA == 4 && typeB == 1)) {
            return 7;
        }

        if ((typeA == 1 && typeB == 5) || (typeA == 5 && typeB == 1)) {
            return 9;
        }

        if ((typeA == 1 && typeB == 7) || (typeA == 7 && typeB == 1)) {
            return 12;
        }

        if ((typeA == 1 && typeB == 8) || (typeA == 8 && typeB == 1)) {
            return 7;
        }

        if ((typeA == 1 && typeB == 9) || (typeA == 9 && typeB == 1)) {
            return 13;
        }

        if ((typeA == 1 && typeB == 11) || (typeA == 11 && typeB == 1)) {
            return 12;
        }

        if ((typeA == 1 && typeB == 12) || (typeA == 12 && typeB == 1)) {
            return 13;
        }

        if ((typeA == 1 && typeB == 17) || (typeA == 17 && typeB == 1)) {
            return 16;
        }

        if ((typeA == 2 && typeB == 4) || (typeA == 4 && typeB == 2)) {
            return 3;
        }

        if ((typeA == 2 && typeB == 5) || (typeA == 5 && typeB == 2)) {
            return 4;
        }

        if ((typeA == 2 && typeB == 7) || (typeA == 7 && typeB == 2)) {
            return 8;
        }

        if ((typeA == 2 && typeB == 8) || (typeA == 8 && typeB == 2)) {
            return 3;
        }

        if ((typeA == 2 && typeB == 12) || (typeA == 12 && typeB == 2)) {
            return 6;
        }

        if ((typeA == 2 && typeB == 16) || (typeA == 16 && typeB == 2)) {
            return 13;
        }

        if ((typeA == 2 && typeB == 17) || (typeA == 17 && typeB == 2)) {
            return 12;
        }

        if ((typeA == 3 && typeB == 4) || (typeA == 4 && typeB == 3)) {
            return 8;
        }

        if ((typeA == 3 && typeB == 5) || (typeA == 5 && typeB == 3)) {
            return 8;
        }

        if ((typeA == 3 && typeB == 6) || (typeA == 6 && typeB == 3)) {
            return 2;
        }

        if ((typeA == 3 && typeB == 7) || (typeA == 7 && typeB == 3)) {
            return 11;
        }

        if ((typeA == 3 && typeB == 9) || (typeA == 9 && typeB == 3)) {
            return 8;
        }

        if ((typeA == 3 && typeB == 12) || (typeA == 12 && typeB == 3)) {
            return 11;
        }

        if ((typeA == 3 && typeB == 16) || (typeA == 16 && typeB == 3)) {
            return 11;
        }

        if ((typeA == 3 && typeB == 17) || (typeA == 17 && typeB == 3)) {
            return 11;
        }

        if ((typeA == 4 && typeB == 6) || (typeA == 6 && typeB == 4)) {
            return 3;
        }

        if ((typeA == 4 && typeB == 7) || (typeA == 7 && typeB == 4)) {
            return 8;
        }

        if ((typeA == 4 && typeB == 9) || (typeA == 9 && typeB == 4)) {
            return 7;
        }

        if ((typeA == 4 && typeB == 11) || (typeA == 11 && typeB == 4)) {
            return 7;
        }

        if ((typeA == 4 && typeB == 12) || (typeA == 12 && typeB == 4)) {
            return 7;
        }

        if ((typeA == 4 && typeB == 13) || (typeA == 13 && typeB == 4)) {
            return 7;
        }

        if ((typeA == 4 && typeB == 16) || (typeA == 16 && typeB == 4)) {
            return 13;
        }

        if ((typeA == 4 && typeB == 17) || (typeA == 17 && typeB == 4)) {
            return 5;
        }

        if ((typeA == 5 && typeB == 6) || (typeA == 6 && typeB == 5)) {
            return 4;
        }

        if ((typeA == 5 && typeB == 7) || (typeA == 7 && typeB == 5)) {
            return 4;
        }

        if ((typeA == 5 && typeB == 8) || (typeA == 8 && typeB == 5)) {
            return 4;
        }

        if ((typeA == 5 && typeB == 11) || (typeA == 11 && typeB == 5)) {
            return 17;
        }

        if ((typeA == 5 && typeB == 12) || (typeA == 12 && typeB == 5)) {
            return 13;
        }

        if ((typeA == 5 && typeB == 13) || (typeA == 13 && typeB == 5)) {
            return 16;
        }

        if ((typeA == 5 && typeB == 16) || (typeA == 16 && typeB == 5)) {
            return 17;
        }

        if ((typeA == 6 && typeB == 8) || (typeA == 8 && typeB == 6)) {
            return 2;
        }

        if ((typeA == 6 && typeB == 17) || (typeA == 17 && typeB == 6)) {
            return 7;
        }

        if ((typeA == 7 && typeB == 16) || (typeA == 16 && typeB == 7)) {
            return 13;
        }

        if ((typeA == 8 && typeB == 11) || (typeA == 11 && typeB == 8)) {
            return 7;
        }

        if ((typeA == 8 && typeB == 12) || (typeA == 12 && typeB == 8)) {
            return 7;
        }

        if ((typeA == 8 && typeB == 13) || (typeA == 13 && typeB == 8)) {
            return 7;
        }

        if ((typeA == 8 && typeB == 16) || (typeA == 16 && typeB == 8)) {
            return 7;
        }

        if ((typeA == 8 && typeB == 17) || (typeA == 17 && typeB == 8)) {
            return 7;
        }

        if ((typeA == 9 && typeB == 16) || (typeA == 16 && typeB == 9)) {
            return 13;
        }

        if ((typeA == 11 && typeB == 16) || (typeA == 16 && typeB == 11)) {
            return 13;
        }

        if ((typeA == 11 && typeB == 17) || (typeA == 17 && typeB == 11)) {
            return 7;
        }

        if ((typeA == 12 && typeB == 16) || (typeA == 16 && typeB == 12)) {
            return 13;
        }

        if ((typeA == 13 && typeB == 17) || (typeA == 17 && typeB == 13)) {
            return 9;
        }

        return typeA; // breed is not in the table, so it will return the first parent type
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);

        if (tameResult != null) return tameResult;

        if (this.getTypeMoC() == 60 && !getIsTamed() && isZebraRunning()) return InteractionResult.FAIL;

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && !getIsRideable() && (stack.is(Items.SADDLE) || stack.getItem() == MoCItems.HORSE_SADDLE.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            setRideable(true);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == Items.IRON_HORSE_ARMOR && isArmored()) {
            if (getArmorType() == 0) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_ON.get());
            dropArmor();
            setArmorType(1);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == Items.GOLDEN_HORSE_ARMOR && isArmored()) {
            if (getArmorType() == 0) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_ON.get());
            dropArmor();
            setArmorType(2);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == Items.DIAMOND_HORSE_ARMOR && isArmored()) {
            if (getArmorType() == 0) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_ON.get());
            dropArmor();
            setArmorType(3);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == MoCItems.HORSEARMORCRYSTAL.get() && isMagicHorse()) {
            if (getArmorType() == 0) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_ARMOR_ON.get());
            dropArmor();
            setArmorType(4);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        // transform to undead, or heal undead horse
        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == MoCItems.ESSENCE_UNDEAD.get()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            else player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

            if (this.isUndead() || getIsGhost()) this.setHealth(getMaxHealth());

            // pegasus, dark pegasus, or bat horse
            if (this.getTypeMoC() == 39 || this.getTypeMoC() == 32 || this.getTypeMoC() == 40) {
                // transformType = 25; //undead pegasus
                transform(25);

            } else if (this.getTypeMoC() == 36 || (this.getTypeMoC() > 47 && this.getTypeMoC() < 60)) // unicorn or fairies
            {
                // transformType = 24; //undead unicorn
                transform(24);
            } else if (this.getTypeMoC() < 21 || this.getTypeMoC() == 60 || this.getTypeMoC() == 61) // regular horses or zebras
            {
                // transformType = 23; //undead
                transform(23);
            }
            drinkingHorse();
            return InteractionResult.SUCCESS;
        }

        // to transform to nightmares: only pure breeds
        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == MoCItems.ESSENCE_FIRE.get()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            else player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

            if (this.isNightmare()) {
                if (getIsAdult() && getHealth() == getMaxHealth()) this.eatenPumpkin = true;
                this.setHealth(getMaxHealth());
            }
            if (this.getTypeMoC() == 61) {
                //nightmare
                transform(38);
            }
            drinkingHorse();
            return InteractionResult.SUCCESS;
        }

        // transform to dark pegasus
        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == MoCItems.ESSENCE_DARKNESS.get()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            else player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

            if (this.getTypeMoC() == 32) {
                if (getIsAdult() && getHealth() == getMaxHealth()) this.eatenPumpkin = true;
                this.setHealth(getMaxHealth());
            }
            if (this.getTypeMoC() == 61) {
                transform(32); //horsezorse to bat horse
            }
            if (this.getTypeMoC() == 39) // pegasus to darkpegasus
            {
                //darkpegasus
                transform(40);
            }
            drinkingHorse();
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && stack.getItem() == MoCItems.ESSENCE_LIGHT.get()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            if (this.isMagicHorse()) {
                if (getIsAdult() && getHealth() == getMaxHealth()) {
                    this.eatenPumpkin = true;
                }
                this.setHealth(getMaxHealth());
            }
            if (this.isNightmare()) {
                // unicorn
                transform(36);
            }
            if (this.getTypeMoC() == 32 && this.getY() > 128D) // bathorse to pegasus
            {
                // pegasus
                transform(39);
            }
            // to return undead horses to pristine conditions
            if (this.isUndead() && this.getIsAdult() && !this.level().isClientSide()) {
                setMoCAge(10);
                if (this.getTypeMoC() >= 26) setTypeMoC(getTypeMoC() - 3);
            }
            drinkingHorse();
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && this.isAmuletHorse() && getIsTamed()) {
            if ((this.getTypeMoC() == 26 || this.getTypeMoC() == 27 || this.getTypeMoC() == 28) && stack.getItem() == MoCItems.AMULET_BONE.get()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                vanishHorse();
                return InteractionResult.SUCCESS;
            }
            if ((this.getTypeMoC() > 47 && this.getTypeMoC() < 60) && stack.getItem() == MoCItems.AMULET_FAIRY.get()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                vanishHorse();
                return InteractionResult.SUCCESS;
            }
            if ((this.getTypeMoC() == 39 || this.getTypeMoC() == 40) && (stack.getItem() == MoCItems.AMULET_PEGASUS.get())) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                vanishHorse();
                return InteractionResult.SUCCESS;
            }
            if ((this.getTypeMoC() == 21 || this.getTypeMoC() == 22) && (stack.getItem() == MoCItems.AMULET_GHOST.get())) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                vanishHorse();
                return InteractionResult.SUCCESS;
            }
        }

        if (!stack.isEmpty() && stack.has(DataComponents.DYE) && this.getTypeMoC() == 50) {
            int colorInt = stack.get(DataComponents.DYE).getId();
            switch (colorInt) {
                case 1: //orange
                    transform(59);
                    break;
                case 2: //magenta TODO
                    //transform(46);
                    break;
                case 3: //light blue
                    transform(51);
                    break;
                case 4: //yellow
                    transform(48);
                    break;
                case 5: //light green
                    transform(53);
                    break;
                case 6: //pink
                    transform(52);
                    break;
                case 7: //gray TODO
                    //transform(50);
                    break;
                case 8: //light gray TODO
                    //transform(50);
                    break;
                case 9: //cyan
                    transform(57);
                    break;
                case 10: //purple
                    transform(49);
                    break;
                case 11: //dark blue
                    transform(56);
                    break;
                case 12: //brown TODO
                    //transform(50);
                    break;
                case 13: //green
                    transform(58);
                    break;
                case 14: //red
                    transform(55);
                    break;
                case 15: //black
                    transform(54);
                    break;
            }

            if (!player.getAbilities().instabuild) stack.shrink(1);
            eatingHorse();
            return InteractionResult.SUCCESS;
        }

        // zebra easter egg
        if (!stack.isEmpty() && (this.getTypeMoC() == 60) && stack.has(DataComponents.JUKEBOX_PLAYABLE) && MoCreatures.proxy.easterEggs) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            if (!this.level().isClientSide()) {
                ItemEntity entityitem1 = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(MoCItems.RECORD_SHUFFLE.get(), 1));
                entityitem1.setPickUpDelay(20);
                this.level().addFreshEntity(entityitem1);
            }
            eatingHorse();
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && (stack.getItem() == Items.WHEAT) && !isMagicHorse() && !isUndead()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!this.level().isClientSide()) {
                setTemper(getTemper() + 25);
                if (getTemper() > getMaxTemper()) setTemper(getMaxTemper() - 5);
            }
            if ((getHealth() + 5) > getMaxHealth()) this.setHealth(getMaxHealth());
            eatingHorse();
            if (!getIsAdult() && (getMoCAge() < getMoCMaxAge())) setMoCAge(getMoCAge() + 1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && (stack.getItem() == MoCItems.SUGAR_LUMP.get()) && !isMagicHorse() && !isUndead()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!this.level().isClientSide()) {
                setTemper(getTemper() + 25);
                if (getTemper() > getMaxTemper()) setTemper(getMaxTemper() - 5);
            }
            if ((getHealth() + 10) > getMaxHealth()) this.setHealth(getMaxHealth());
            eatingHorse();
            if (!getIsAdult() && (getMoCAge() < getMoCMaxAge())) setMoCAge(getMoCAge() + 2);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && (stack.getItem() == Items.BREAD) && !isMagicHorse() && !isUndead()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!this.level().isClientSide()) {
                setTemper(getTemper() + 100);
                if (getTemper() > getMaxTemper()) setTemper(getMaxTemper() - 5);
            }
            if ((getHealth() + 20) > getMaxHealth()) this.setHealth(getMaxHealth());
            eatingHorse();
            if (!getIsAdult() && (getMoCAge() < getMoCMaxAge())) setMoCAge(getMoCAge() + 3);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && ((stack.getItem() == Items.APPLE) || (stack.getItem() == Items.GOLDEN_APPLE)) && !isMagicHorse() && !isUndead()) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!this.level().isClientSide() && !getIsTamed()) MoCTools.tameWithName(player, this);
            this.setHealth(getMaxHealth());
            eatingHorse();
            if (!getIsAdult() && (getMoCAge() < getMoCMaxAge()) && !this.level().isClientSide()) setMoCAge(getMoCAge() + 1);
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && (stack.getItem() == Item.byBlock(Blocks.CHEST)) && (isBagger())) {
            if (getIsChested()) return InteractionResult.FAIL;
            if (!player.getAbilities().instabuild) stack.shrink(1);

            setIsChested(true);
            MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
            return InteractionResult.SUCCESS;
        }
        
        if (!stack.isEmpty() && getIsTamed() && (stack.getItem() == MoCItems.HAYSTACK.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            setSitting(true);
            eatingHorse();
            if (!isMagicHorse() && !isUndead()) this.setHealth(getMaxHealth());
            return InteractionResult.SUCCESS;
        }
        
        if (getIsChested() && player.isShiftKeyDown()) {
            // if first time opening horse chest, we must initialize it
            if (this.localChest == null) this.localChest = new MoCAnimalChest("HorseChest", getInventorySize());
            // only open this chest on server side
            if (!this.level().isClientSide()) {
                MoCAnimalChest.Size chestSize = this.localChest.getSize();
                player.openMenu(new SimpleMenuProvider(
                    (id, inventory, p) -> {
                        // Use the appropriate ChestMenu constructor based on chest size
                        switch (chestSize) {
                            case tiny:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x1, id, inventory, this.localChest, 1);
                            case small:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x2, id, inventory, this.localChest, 2);
                            case medium:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x3, id, inventory, this.localChest, 3);
                            case large:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x4, id, inventory, this.localChest, 4);
                            case huge:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x5, id, inventory, this.localChest, 5);
                            case gigantic:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x6, id, inventory, this.localChest, 6);
                            default:
                                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x3, id, inventory, this.localChest, 3);
                        }
                    },
                    Component.translatable("container.horse_chest")
                ));
            }
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty()
                && ((stack.getItem() == Item.byBlock(Blocks.CARVED_PUMPKIN)) || (stack.getItem() == Items.PUMPKIN) || (stack.getItem() == Items.MUSHROOM_STEW)
                || (stack.getItem() == Items.CAKE) || (stack.getItem() == Items.GOLDEN_CARROT))) {
            if (!getIsAdult() || isMagicHorse() || isUndead()) return InteractionResult.FAIL;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (stack.getItem() == Items.MUSHROOM_STEW) {
                if (stack.isEmpty()) player.setItemInHand(hand, new ItemStack(Items.BOWL));
                else player.getInventory().add(new ItemStack(Items.BOWL));
            } else if (stack.isEmpty()) player.setItemInHand(hand, ItemStack.EMPTY);
            this.eatenPumpkin = true;
            this.setHealth(getMaxHealth());
            eatingHorse();
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && (stack.getItem() == MoCItems.WHIP.get()) && getIsTamed() && (!this.isVehicle())) {
            setSitting(!getIsSitting());
            setIsJumping(false);
            getNavigation().stop();
            setTarget(null);
            return InteractionResult.SUCCESS;
        }

        if (getIsRideable() && getIsAdult() && (!this.isVehicle())) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setSitting(false);
                this.gestationTime = 0;
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Can this horse be trapped in a special amulet?
     */
    public boolean isAmuletHorse() {
        return this.getTypeMoC() == 21
                || this.getTypeMoC() == 22
                || this.getTypeMoC() == 26
                || this.getTypeMoC() == 27
                || this.getTypeMoC() == 28
                || this.getTypeMoC() == 39
                || this.getTypeMoC() == 40
                || (this.getTypeMoC() > 47 && this.getTypeMoC() < 60);
    }

    /**
     * Can wear regular armor
     */
    public boolean isArmored() {
        return (this.getTypeMoC() < 21);
    }

    /**
     * able to carry bags
     */
    public boolean isBagger() {
        return (this.getTypeMoC() == 66) // mule
                || (this.getTypeMoC() == 65) // donkey
                || (this.getTypeMoC() == 67) // zonkey
                || (this.getTypeMoC() == 39) // pegasi
                || (this.getTypeMoC() == 40) // black pegasi
                || (this.getTypeMoC() == 25) // undead pegasi
                || (this.getTypeMoC() == 28) // skelly pegasi
                || (this.getTypeMoC() > 44 && this.getTypeMoC() < 60) // fairy
                ;
    }

    /**
     * Falls slowly
     */
    public boolean isFloater() {
        return this.getTypeMoC() == 36 // unicorn
                || this.getTypeMoC() == 27 // skelly unicorn
                || this.getTypeMoC() == 24 // undead unicorn
                || this.getTypeMoC() == 22; // not winged ghost

    }

    @Override
    public boolean isFlyer() {
        return this.getTypeMoC() == 39 // pegasus
                || this.getTypeMoC() == 40 // dark pegasus
                || (this.getTypeMoC() > 44 && this.getTypeMoC() < 60) //fairy
                || this.getTypeMoC() == 32 // bat horse
                || this.getTypeMoC() == 21 // ghost winged
                || this.getTypeMoC() == 25 // undead pegasus
                || this.getTypeMoC() == 28;// skelly pegasus
    }

    /**
     * Is this a ghost horse?
     */
    @Override
    public boolean getIsGhost() {
        return this.getTypeMoC() == 21 || this.getTypeMoC() == 22;
    }

    /**
     * Can wear magic armor
     */
    public boolean isMagicHorse() {
        return this.getTypeMoC() == 39
                || this.getTypeMoC() == 36
                || this.getTypeMoC() == 32
                || this.getTypeMoC() == 40
                || (this.getTypeMoC() > 44 && this.getTypeMoC() < 60) //fairy
                || this.getTypeMoC() == 21
                || this.getTypeMoC() == 22;
    }

    @Override
    public boolean isMovementCeased() {
        return this.getIsSitting() || this.standCounter != 0 || this.shuffleCounter != 0 || this.getVanishC() != 0;
    }

    /**
     * Is this a Nightmare horse?
     */
    public boolean isNightmare() {
        return this.getTypeMoC() == 38;
    }

    /**
     * Rare horse that can be transformed into Nightmares or Bathorses or give
     * ghost horses on dead
     */
    public boolean isPureBreed() {
        return (this.getTypeMoC() > 10 && this.getTypeMoC() < 21);
    }

    /**
     * Mobs don't attack you if you're riding one of these they won't reproduce
     * either
     */
    public boolean isUndead() {
        return (this.getTypeMoC() > 22 && this.getTypeMoC() < 29);
    }

    /**
     * Has a unicorn? to render it and buckle entities!
     */
    public boolean isUnicorned() {
        return this.getTypeMoC() == 36 || (this.getTypeMoC() >= 45 && this.getTypeMoC() < 60) || this.getTypeMoC() == 27 || this.getTypeMoC() == 24;
    }

    public boolean isZebraRunning() {
        boolean flag = false;
        Player ep1 = this.level().getNearestPlayer(this, 8D);
        if (ep1 != null) {
            flag = true;
            if (ep1.getVehicle() instanceof MoCEntityHorse) {
                MoCEntityHorse playerHorse = (MoCEntityHorse) ep1.getVehicle();
                if (playerHorse.getTypeMoC() == 16 || playerHorse.getTypeMoC() == 17 || playerHorse.getTypeMoC() == 60 || playerHorse.getTypeMoC() == 61) {
                    flag = false;
                }
            }
        }
        if (flag) {
            MoCTools.runLikeHell(this, ep1);
        }
        return flag;
    }

    public void LavaFX() {
        MoCreatures.proxy.LavaFX(this);
    }

    public void MaterializeFX() {
        MoCreatures.proxy.MaterializeFX(this);
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    @Override
    public int nameYOffset() {
        if (this.getIsAdult()) return -80;
        else return (-5 - getMoCAge());
    }

    private boolean nearMusicBox() {
        // only works server side
        if (this.level().isClientSide() || !MoCreatures.proxy.easterEggs) return false;
        boolean flag = false;
        JukeboxBlockEntity jukebox = MoCTools.nearJukeBoxRecord(this, 6D);
        if (jukebox != null) {
            ItemStack recordStack = jukebox.getTheItem();
            Item shuffleRecord = MoCItems.RECORD_SHUFFLE.get();
            if (recordStack.getItem() == shuffleRecord) {
                flag = true;
                if (this.shuffleCounter > 1000) {
                    this.shuffleCounter = 0;
                    MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 102));
                    flag = false;
                }
            }
        }
        return flag;
    }

    // changed to public since we need to send this info to server
    public void nightmareEffect() {
        if (!MoCTools.mobGriefing(this.level())) {
            setNightmareInt(getNightmareInt() - 1);
            return;
        }
        int i = Mth.floor(this.getX());
        int j = Mth.floor(getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos pos = new BlockPos(i, j, k);
        BlockState blockstate = this.level().getBlockState(pos.offset(-1, 0, -1));
        BreakBlockEvent event = null;
        if (!this.level().isClientSide()) {
            try {
                event = new BreakBlockEvent(this.level(), pos, blockstate, FakePlayerFactory.getMinecraft((net.minecraft.server.level.ServerLevel)this.level()));
            } catch (Throwable ignored) {
            }
        }
        if (event != null && !event.isCanceled()) {
            this.level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
            Player player = (Player) this.getVehicle();
            if ((player != null) && (player.isOnFire())) player.clearFire();
            setNightmareInt(getNightmareInt() - 1);
        }
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (!this.level().isClientSide()) {
            if ((this.random.nextInt(10) == 0) && (this.getTypeMoC() == 23) || (this.getTypeMoC() == 24) || (this.getTypeMoC() == 25))
                MoCTools.spawnMaggots(this.level(), this);

            if (getIsTamed() && (isMagicHorse() || isPureBreed()) && !getIsGhost() && this.random.nextInt(4) == 0) {
                MoCEntityHorse entityhorse1 = MoCEntities.WILDHORSE.get().create(this.level(), EntitySpawnReason.BREEDING);
                entityhorse1.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(entityhorse1);
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_MAGIC_APPEAR.get());

                entityhorse1.setOwnerId(this.getOwnerId());
                entityhorse1.setTamed(true);
                Player player = this.level().getNearestPlayer(this, 24D);
                if (player != null) MoCTools.tameWithName(player, entityhorse1);

                entityhorse1.setAdult(false);
                entityhorse1.setMoCAge(1);
                int l = 22;
                if (this.isFlyer()) l = 21;
                entityhorse1.setTypeMoC(l);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        
        if (this.shuffleCounter > 0) {
            ++this.shuffleCounter;
            if (this.level().isClientSide() && this.shuffleCounter % 20 == 0) {
                double var2 = this.random.nextGaussian() * 0.5D;
                double var4 = this.random.nextGaussian() * -0.1D;
                double var6 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.NOTE, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY()
                        + 0.5D + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), var2, var4, var6);
            }

            if (!this.level().isClientSide() && !nearMusicBox()) {
                this.shuffleCounter = 0;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 102));
            }
        }

        if (this.mouthCounter > 0 && ++this.mouthCounter > 30) this.mouthCounter = 0;

        if (this.standCounter > 0 && ++this.standCounter > 20) this.standCounter = 0;

        if (this.tailCounter > 0 && ++this.tailCounter > 8) this.tailCounter = 0;

        if (getVanishC() > 0) {
            setVanishC((byte) (getVanishC() + 1));

            if (getVanishC() < 15 && this.level().isClientSide()) VanishFX();

            if (getVanishC() > 100) {
                setVanishC((byte) 101);
                MoCTools.dropHorseAmulet(this);
                dissapearHorse();
            }

            if (getVanishC() == 1) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_VANISH.get());

            if (getVanishC() == 70) stand();
        }

        if (this.sprintCounter > 0) {
            ++this.sprintCounter;
            if (this.sprintCounter < 150 && this.sprintCounter % 2 == 0 && this.level().isClientSide()) StarFX();
            if (this.sprintCounter > 300) this.sprintCounter = 0;
        }

        if (this.transformCounter > 0) {
            if (this.transformCounter == 40) MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_TRANSFORM.get());

            if (++this.transformCounter > 100) {
                this.transformCounter = 0;
                if (this.transformType != 0) {
                    dropArmor();
                    setTypeMoC(this.transformType);
                }
            }
        }

        if (getIsGhost() && getMoCAge() < 10 && this.random.nextInt(7) == 0) setMoCAge(getMoCAge() + 1);

        if (getIsGhost() && getMoCAge() == 9) {
            setMoCAge(100);
            setAdult(true);
        }
    }
    
    @Override
    public void aiStep() {
        /*
         * slow falling
         */
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D && (isFlyer() || isFloater()))
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));

        if (this.random.nextInt(200) == 0) moveTail();

        if ((getTypeMoC() == 38) && (this.random.nextInt(50) == 0) && this.level().isClientSide()) LavaFX();

        if ((getTypeMoC() == 36) && isOnAir() && this.level().isClientSide()) StarFX();

        if (!this.level().isClientSide() && isFlyer() && isOnAir()) {
            float myFlyingSpeed = MoCTools.getMyMovementSpeed(this);
            int wingFlapFreq = (int) (25 - (myFlyingSpeed * 10));
            if (!this.isVehicle() || wingFlapFreq < 5) wingFlapFreq = 5;
            if (this.random.nextInt(wingFlapFreq) == 0) wingFlap();
        }

        if (isFlyer()) {
            if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) this.wingFlapCounter = 0;
            if (this.wingFlapCounter != 0 && this.wingFlapCounter % 5 == 0 && this.level().isClientSide()) StarFX();
            if (this.wingFlapCounter == 5 && !this.level().isClientSide())
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_WINGFLAP.get());
        }

        if (isUndead() && (this.getTypeMoC() < 26) && getIsAdult() && (this.random.nextInt(20) == 0)) {
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(16) == 0) setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= 399) setTypeMoC(this.getTypeMoC() + 3);
            } else UndeadFX();
        }

        super.aiStep();

        if (!this.level().isClientSide()) {
            /*
             * Shuffling LMFAO!
             */
            if (this.getTypeMoC() == 60 && getIsTamed() && this.random.nextInt(50) == 0 && nearMusicBox() && shuffleCounter == 0) {
                shuffleCounter = 1;
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 101));
            }

            if ((this.random.nextInt(300) == 0) && (this.deathTime == 0)) {
                this.setHealth(getHealth() + 1);
                if (getHealth() > getMaxHealth()) this.setHealth(getMaxHealth());
            }

            if (!getIsSitting() && !getIsTamed() && this.random.nextInt(300) == 0) setSitting(true);

            if (getIsSitting() && ++this.countEating > 50 && !getIsTamed()) {
                this.countEating = 0;
                setSitting(false);
            }

            if ((getTypeMoC() == 38) && (this.isVehicle()) && (getNightmareInt() > 0) && (this.random.nextInt(2) == 0))
                nightmareEffect();

            /*
             * Buckling logic from 1.16.5 - not sure if it works
             */
            if ((this.sprintCounter > 0 && this.sprintCounter < 150) && isUnicorned() && this.isVehicle()) {
                MoCTools.buckleMobs(this, 2D, this.level());
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_HORSE_MAD.get());
            }

            // Bucking logic for untamed horses
            if (this.isVehicle() && !this.getIsTamed()) {
                Entity passenger = this.getFirstPassenger();
                if (passenger instanceof Player) {
                    // 1 in 50 chance per tick to buck off (maybe add option to MoCSettings)
                    if (this.random.nextInt(50) == 0) {
                        passenger.stopRiding();
                        // Teleport the player a bit away to avoid remounting instantly
                        double offsetX = this.random.nextGaussian() * 0.5D;
                        double offsetZ = this.random.nextGaussian() * 0.5D;
                        passenger.teleportTo(this.getX() + offsetX, this.getY() + 0.5D, this.getZ() + offsetZ);
                        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_HORSE_MAD.get());
                        
                        this.ejectPassengers();
                        this.refreshDimensions();
                        this.jumpFromGround();
                    }
                    // 1 in 400 chance per tick to tame (maybe add option to MoCSettings)
                    if (this.random.nextInt(400) == 0) {
                        MoCTools.tameWithName((Player) passenger, this);
                        // Heart effect played in MoCTools.tameWithName
                    }
                }
            }

            if (isFlyer() && !getIsTamed() && this.random.nextInt(100) == 0 && !isMovementCeased() && !getIsSitting())
                wingFlap();

            if (!readyForParenting(this)) return;

            int i = 0;

            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(8D, 3D, 8D), 
                entity -> entity != this && (entity instanceof MoCEntityHorse || entity instanceof AbstractHorse));
            
            i = list.size();
            if (i > 1) return;

            List<Entity> list1 = this.level().getEntities(this, this.getBoundingBox().inflate(4D, 2D, 4D),
                entity -> entity != this && (entity instanceof MoCEntityHorse || entity instanceof AbstractHorse));
            
            for (Entity horsemate : list1) {
                boolean flag = (horsemate instanceof AbstractHorse);
                if (!(horsemate instanceof MoCEntityHorse || flag) || (horsemate == this)) continue;

                if (!flag && !readyForParenting((MoCEntityHorse) horsemate)) return;

                if (MoCreatures.proxy.originalHorseBreeding) {
                    // 1% chance per tick to increment gestation time, following the old one day cycle breeding method.
                    if (this.random.nextInt(100) == 0) this.gestationTime++;
                } else {
                    this.gestationTime++;
                }

                if (this.gestationTime % 3 == 0) {
                    MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHeart(this.getId()));
                }

                if (MoCreatures.proxy.originalHorseBreeding) {
                    if (this.gestationTime <= 50) continue; // The old RNG based delay
                } else {
                    if (this.gestationTime <= 300) continue; // it takes ~15 seconds to breed.
                }

                MoCEntityHorse baby = MoCEntities.WILDHORSE.get().create(this.level(), EntitySpawnReason.BREEDING);
                baby.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(baby);
                MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                this.eatenPumpkin = false;
                this.gestationTime = 0;

                int horsemateType;
                if (flag) {
                    horsemateType = translateVanillaHorseType((AbstractHorse) horsemate);
                    if (horsemateType == -1) return;
                } else {
                    horsemateType = ((MoCEntityHorse) horsemate).getTypeMoC();
                    ((MoCEntityHorse) horsemate).eatenPumpkin = false;
                    ((MoCEntityHorse) horsemate).gestationTime = 0;
                }
                int l = horseGenetics(this.getTypeMoC(), horsemateType);

                if (l == 50 || l == 54) // fairy horse!
                {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_MAGIC_APPEAR.get());
                    if (!flag) {
                        MoCEntityHorse parent = (MoCEntityHorse) horsemate;
                        parent.disappearAfterBreeding();
                    }
                    this.disappearAfterBreeding();
                }
                baby.setOwnerId(this.getOwnerId());
                baby.setTamed(true);
                baby.setAdult(false);
                UUID ownerId = this.getOwnerId();
                Player entityplayer = null;
                if (ownerId != null && this.level() instanceof ServerLevel) {
                    entityplayer = ((ServerLevel)this.level()).getServer().getPlayerList().getPlayer(this.getOwnerId());
                }
                if (entityplayer != null) MoCTools.tameWithName(entityplayer, baby);
                baby.setTypeMoC(l);
                break;
            }
        }
    }

    /**
     * Obtains the 'Type' of vanilla horse for inbreeding with MoC Horses
     */
    private int translateVanillaHorseType(AbstractHorse horse) {
        if (horse instanceof Donkey) return 65; // donkey
        if (horse instanceof Horse) {
            // In 1.20.1, we need to use variant() instead of getMarkings()
            int markingsId = 0;
            try {
                // Try to get variant - this is approximate since we don't have direct access to markings
                Horse horseEntity = (Horse) horse;
                markingsId = horseEntity.getVariant().ordinal();
            } catch (Exception e) {
                // Default to brown if we can't get the variant
                return 3; 
            }
            
            switch (markingsId) {
                case 0: //white
                    return 1;
                case 1: //creamy
                    return 2;
                case 3: //brown
                    return 3;
                case 4: //black
                    return 5;
                case 5: //gray
                    return 9;
                case 6: //dark brown
                    return 4;
                default:
                    return 3;
            }
        }
        return -1;
    }



    private void openMouth() {
        this.mouthCounter = 1;
    }

    public boolean readyForParenting(MoCEntityHorse entityhorse) {
        int i = entityhorse.getTypeMoC();
        return (!entityhorse.isVehicle()) && (!entityhorse.isPassenger()) && entityhorse.getIsTamed() && entityhorse.eatenPumpkin
                && entityhorse.getIsAdult() && !entityhorse.isUndead() && !entityhorse.getIsGhost() && (i != 61) && (i < 66);
    }

    @Override
    public boolean rideableEntity() {
        return true;
    }

    /**
     * Horse Types
     * <p>
     * 1 White . 2 Creamy. 3 Brown. 4 Dark Brown. 5 Black.
     * <p>
     * 6 Bright Creamy. 7 Speckled. 8 Pale Brown. 9 Grey. 10 11 Pinto . 12
     * Bright Pinto . 13 Pale Speckles.
     * <p>
     * 16 Spotted 17 Cow.
     * <p>
     * <p>
     * <p>
     * <p>
     * 21 Ghost (winged) 22 Ghost B
     * <p>
     * 23 Undead 24 Undead Unicorn 25 Undead Pegasus
     * <p>
     * 26 skeleton 27 skeleton unicorn 28 skeleton pegasus
     * <p>
     * 30 bug horse
     * <p>
     * 32 Bat Horse
     * <p>
     * 36 Unicorn
     * <p>
     * 38 Nightmare? 39 White Pegasus 40 Black Pegasus
     * <p>
     * 50 fairy white 51 fairy blue 52 fairy pink 53 fairy light green
     * <p>
     * 60 Zebra 61 Zorse
     * <p>
     * 65 Donkey 66 Mule 67 Zonky
     */

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            checkSpawningBiome();
        }
        if (getTypeMoC() == 0) {
            if (this.random.nextInt(5) == 0) setAdult(false);
            int j = this.random.nextInt(100);
            if (j <= (33)) setTypeMoC(6);
            else if (j <= (66)) setTypeMoC(7);
            else if (j <= (98)) setTypeMoC(8);
            else setTypeMoC(60);// zebra
        }
    }

    public void setReproduced(boolean var1) {
        this.hasReproduced = var1;
    }

    private void stand() {
        if (!this.isVehicle() && !this.isOnAir()) this.standCounter = 1;
    }

    public void StarFX() {
        MoCreatures.proxy.StarFX(this);
    }

    /**
     * Used to flicker ghosts
     */
    public float tFloat() {
        if (++this.fCounter > 60) {
            this.fCounter = 0;
            this.transFloat = (this.random.nextFloat() * (0.6F - 0.3F) + 0.3F);
        }

        if (getIsGhost() && getMoCAge() < 10) this.transFloat = 0;

        return this.transFloat;
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

    public void UndeadFX() {
        MoCreatures.proxy.UndeadFX(this);
    }

    public void VanishFX() {
        MoCreatures.proxy.VanishFX(this);
    }

    /**
     * Called to vanish Horse
     */

    public void vanishHorse() {
        this.getNavigation().stop();
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);

        if (this.isBagger()) {
            MoCTools.dropInventory(this, this.localChest);
            dropBags();
        }
        if (!this.level().isClientSide()) {
            MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageVanish(this.getId()));
            setVanishC((byte) 1);
        }
        MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_VANISH.get());
    }

    @Override
    public void dropMyStuff() {
        dropArmor();
        MoCTools.dropSaddle(this, this.level());
        if (this.isBagger()) {
            MoCTools.dropInventory(this, this.localChest);
            dropBags();
        }
    }

    public void wingFlap() {
        if (this.isFlyer() && this.wingFlapCounter == 0) {
            this.wingFlapCounter = 1;
            if (!this.level().isClientSide()) {
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageAnimation(this.getId(), 3));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Saddle", getIsRideable());
        nbttagcompound.putBoolean("EatingHaystack", getIsSitting());
        nbttagcompound.putBoolean("ChestedHorse", getIsChested());
        nbttagcompound.putBoolean("HasReproduced", getHasReproduced());
        nbttagcompound.putBoolean("Bred", getHasBred());
        nbttagcompound.putInt("ArmorType", getArmorType());

        if (getIsChested() && this.localChest != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.localChest.getContainerSize(); i++) {
                // grab the current item stack
                this.localStack = this.localChest.getItem(i);
                if (!this.localStack.isEmpty()) {
                    CompoundTag nbttagcompound1 = new CompoundTag();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    MoCTools.saveStack(this, this.localStack, nbttagcompound1);
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
        setSitting(nbttagcompound.getBooleanOr("EatingHaystack", false));
        setBred(nbttagcompound.getBooleanOr("Bred", false));
        setIsChested(nbttagcompound.getBooleanOr("ChestedHorse", false));
        setReproduced(nbttagcompound.getBooleanOr("HasReproduced", false));
        setArmorType(nbttagcompound.getIntOr("ArmorType", 0));
        if (getIsChested()) {
            ListTag nbttaglist = MoCTools.readList(nbttagcompound, "Items");
            this.localChest = new MoCAnimalChest("HorseChest", getInventorySize());
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompoundOrEmpty(i);
                int j = nbttagcompound1.getByteOr("Slot", (byte) 0) & 0xff;
                if (j < this.localChest.getContainerSize()) {
                    this.localChest.setItem(j, MoCTools.loadStack(this, nbttagcompound1));
                }
            }
        }
    }

    @Override
    public void performAnimation(int animationType) {
        //23,24,25,32,36,38,39,40,51,52,53
        if (animationType >= 23 && animationType < 60) //transform
        {
            this.transformType = animationType;
            this.transformCounter = 1;
        }
        if (animationType == 3) //wing flap 
        {
            this.wingFlapCounter = 1;
        }
        if (animationType == 101) //zebra Shuffle starts
        {
            this.shuffleCounter = 1;
        }
        if (animationType == 102) //zebra Shuffle ends
        {
            this.shuffleCounter = 0;
        }
    }

    /** These variants are undead: healing harms them and harming heals them, as with vanilla undead. */
    @Override
    public boolean isInvertedHealAndHarm() {
        return isUndead() || super.isInvertedHealAndHarm();
    }

    @Override
    protected boolean canBeTrappedInNet() {
        return getIsTamed() && !isAmuletHorse();
    }

    @Override
    public void setTypeMoC(int i) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculateMaxHealth());
        this.setHealth(getMaxHealth());
        this.isImmuneToFire = i == 38 || i == 40;
        super.setTypeMoC(i);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        double dist = getSizeFactor() * (0.25D);
        double newPosX = this.getX() + (dist * Math.sin(this.yBodyRot / 57.29578F));
        double newPosZ = this.getZ() - (dist * Math.cos(this.yBodyRot / 57.29578F));
        moveFunction.accept(passenger, newPosX, this.getY() + this.getPassengersRidingOffset() + (-passenger.getVehicleAttachmentPoint(this).y), newPosZ);
    }

    @Override
    public void makeEntityJump() {
        wingFlap();
        super.makeEntityJump();
    }

    // Adjusted to avoid most of the roof suffocation for now
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.9F));
    }

    @Override
    public boolean fireImmune() {
        return this.isImmuneToFire || super.fireImmune();
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource damageSource) {
        if (isFlyer() || isFloater()) return false;
        float i = (float) (Math.ceil(distance - 3F) / 2F);
        if (!this.level().isClientSide() && (i > 0)) {
            if (getTypeMoC() >= 10) i /= 2;
            if (i > 1F) hurtOrSimulate(damageSource, i);
            if ((this.isVehicle()) && (i > 1F)) {
                for (Entity entity : this.getPassengers()) entity.hurtOrSimulate(damageSource, i);
            }
            BlockPos pos = this.blockPosition().below();
            BlockState iblockstate = this.level().getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (!iblockstate.isAir() && !this.isSilent()) {
                SoundType soundtype = block.getSoundType(iblockstate, level(), pos, this);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), soundtype.getStepSound(), this.getSoundSource(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
            return true;
        }
        return false;
    }
}
