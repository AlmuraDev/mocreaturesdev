/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.init.MoCEntities;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.UUID;

public class MoCEntityTurkey extends MoCEntityTameableAnimal {
    private static final Ingredient SEEDS = Ingredient.of(Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    /** Almura feed that also tempts and breeds turkeys; matched by id, so nothing changes without Almura. */
    private static final Set<Identifier> ALMURA_FEED = Set.of(
            Identifier.parse("almura:food/food/soybean"),
            Identifier.parse("almura:food/food/corn"));
    private static final Predicate<ItemStack> TEMPTATION_ITEMS = stack -> !stack.isEmpty()
            && (SEEDS.test(stack) || ALMURA_FEED.contains(BuiltInRegistries.ITEM.getKey(stack.getItem())));
    private int gestationTime = 0;

    public MoCEntityTurkey(EntityType<? extends MoCEntityTurkey> type, Level world) {
        super(type, world);
        setAdult(true);
        selectType(); // Ensure type is set when spawned
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        // Removed EntityAIMateMoC - now uses inherited doBreeding() system
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    // Removed getBreedOffspring - now uses doBreeding() system from MoCEntityTameableAnimal

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(2) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        if (getTypeMoC() == 1 && this.getIsAdult()) {
            return MoCreatures.proxy.getModelTexture("turkey_male.png");
        } else {
            return MoCreatures.proxy.getModelTexture("turkey_female.png");
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_TURKEY_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_TURKEY_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_TURKEY_AMBIENT.get();
    }

    // TODO: Add unique sound event
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.CHICKEN_STEP.value(), 0.15F, 1.0F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.TURKEY;
    }

    @Override
    public boolean compatibleMate(Entity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (otherAnimal.getClass() != this.getClass()) {
            return false;
        } else if (this.isMale() == ((MoCEntityTurkey) otherAnimal).isMale()) {
            return false;
        } else {
            return true; // Compatible if different genders of same species
        }
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        return "Turkey";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        // Randomly choose male (1) or female (2)
        return this.random.nextInt(2) + 1;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!itemstack.isEmpty()) {
            if (this.isFood(itemstack) && this.getIsAdult() && !this.getHasEaten()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setHasEaten(true); // Ready to breed using doBreeding() system
                return InteractionResult.SUCCESS;
            }

            if (!this.getIsAdult() && this.isFood(itemstack)) {
                this.usePlayerItem(player, hand, itemstack);
                // Age up baby turkeys when fed
                this.setMoCAge(this.getMoCAge() + 10);
                if (this.getMoCAge() >= this.getMoCMaxAge()) {
                    this.setAdult(true);
                }
                return InteractionResult.SUCCESS;
            }
        }

        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!getIsTamed() && !stack.isEmpty() && (stack.getItem() == Items.MELON_SEEDS)) {
            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.8D, 1.0D));
        }
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == Items.PUMPKIN_SEEDS;
    }

    @Override
    public int getMoCMaxAge() {
        return 35;
    }

    @Override
    public int nameYOffset() {
        return -50;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 18) {
            for (int i = 0; i < 7; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.HEART, 
                    this.getX() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                    this.getY() + 0.5D + (this.random.nextFloat() * this.getBbHeight()),
                    this.getZ() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                    d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        EntityDimensions sizeIn = super.getDefaultDimensions(poseIn);
        return sizeIn.withEyeHeight((float) (sizeIn.height() * 0.945F));
    }
    
    @Override
    public boolean isMale() {
        return getTypeMoC() == 1;
    }

    @Override
    public float getSizeFactor() {
        if (!this.getIsAdult()) {
            return 0.5f + (this.getMoCAge() * 0.01f);
        }
        return 1f;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putInt("GestationTime", this.gestationTime);
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        this.gestationTime = nbttagcompound.getIntOr("GestationTime", 0);
    }

    @Override
    public void tick() {
        super.tick();
        
        // Custom breeding system (similar to horses)
        if (!this.level().isClientSide() && this.readytoBreed()) {
            this.doTurkeyBreeding();
        }
    }

    /**
     * Custom breeding system for turkeys based on the horse breeding system
     */
    private void doTurkeyBreeding() {
        // Check for too many turkeys nearby
        int nearbyTurkeys = 0;
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(8D, 3D, 8D), 
            entity -> entity != this && entity instanceof MoCEntityTurkey);
        nearbyTurkeys = list.size();
        
        if (nearbyTurkeys > 1) return;

        // Look for nearby compatible mate
        List<Entity> nearbyMates = this.level().getEntities(this, this.getBoundingBox().inflate(4D, 2D, 4D),
            entity -> entity != this && entity instanceof MoCEntityTurkey);
        
        for (Entity potentialMate : nearbyMates) {
            if (!(potentialMate instanceof MoCEntityTurkey) || potentialMate == this) continue;
            
            MoCEntityTurkey mate = (MoCEntityTurkey) potentialMate;
            
            // Check if both are ready to breed
            if (!this.readytoBreed() || !mate.readytoBreed()) continue;
            
            // Check compatibility (different genders)
            if (!this.compatibleMate(mate)) continue;

            this.gestationTime++;

            // Show hearts during breeding process
            if (this.gestationTime % 3 == 0) {
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHeart(this.getId()));
            }

            // After 50 ticks (~2.5 seconds), create baby
            if (this.gestationTime <= 50) continue;

            // Create baby turkey
            MoCEntityTurkey baby = MoCEntities.TURKEY.get().create(this.level(), EntitySpawnReason.BREEDING);
            if (baby != null) {
                baby.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(baby);
                MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);
                
                // Set baby properties
                baby.setAdult(false);
                baby.setMoCAge(1);
                baby.setTamed(true);
                baby.setOwnerId(this.getOwnerId());
                baby.setTypeMoC(this.getOffspringTypeInt(mate));

                // Set owner if available
                UUID ownerId = this.getOwnerId();
                if (ownerId != null && this.level() instanceof ServerLevel) {
                    Player entityplayer = ((ServerLevel)this.level()).getServer().getPlayerList().getPlayer(ownerId);
                    if (entityplayer != null) {
                        MoCTools.tameWithName(entityplayer, baby);
                    }
                }
            }

            // Reset breeding state
            this.setHasEaten(false);
            this.gestationTime = 0;
            mate.setHasEaten(false);
            mate.gestationTime = 0;
            break;
        }
    }
}
