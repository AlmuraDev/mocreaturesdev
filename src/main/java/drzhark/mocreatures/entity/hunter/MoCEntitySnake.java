/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromPlayer;
import drzhark.mocreatures.entity.ai.EntityAIHunt;
import drzhark.mocreatures.entity.ai.EntityAIPanicMoC;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

/**
 * Biome - specific Forest Desert plains Swamp Jungle Tundra Taiga Extreme Hills
 * Ocean
 * <p>
 * swamp: python, bright green, #1 plains: coral, cobra #1, #2, #3, #4 desert:
 * rattlesnake , #2 jungle: all except rattlesnake hills: all except python,
 * bright green, bright orange tundra-taiga: none ocean: leave alone
 */

public class MoCEntitySnake extends MoCEntityTameableAnimal {

    public float bodyswing;
    private float fTongue;
    private float fMouth;
    private boolean isBiting;
    private float fRattle;
    private boolean isPissed;
    private int hissCounter;
    private int movInt;
    private boolean isNearPlayer;

    public MoCEntitySnake(EntityType<? extends MoCEntitySnake> type, Level world) {
        super(type, world);
        //setSize(1.4F, 0.5F);
        this.bodyswing = 2F;
        this.movInt = this.random.nextInt(10);
        setMoCAge(50 + this.random.nextInt(50));
        this.xpReward = 3;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 0.8D));
        this.goalSelector.addGoal(3, new EntityAIFleeFromPlayer(this, 0.8D, 4D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new EntityAIWanderMoC2(this, 0.8D, 30));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, Animal.class, true));
        this.targetSelector.addGoal(3, new EntityAIHunt<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        if (this.level().dimension() == MoCreatures.proxy.wyvernDimension) this.setPersistenceRequired();
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean isPersistenceRequired() {
        return this.level().dimension() == MoCreatures.proxy.wyvernDimension || super.isPersistenceRequired();
    }

    @Override
    public void selectType() {
        checkSpawningBiome();
        // snake types:
        // 1 small blackish/dark snake (passive)
        // 2 dark green /brown snake (passive)
        // 3 bright orangy snake aggressive venomous swamp, jungle, forest
        // 4 bright green snake aggressive venomous swamp, jungle, forest
        // 5 coral (aggressive - venomous) small / plains, forest
        // 6 cobra (aggressive - venomous - spitting) plains, forest
        // 7 rattlesnake (aggressive - venomous) desert
        // 8 python (aggressive - non-venomous) big - swamp
        // 9 sea snake (aggressive - venomous)
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(8) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 2:
                return MoCreatures.proxy.getModelTexture("snake_wolf.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("snake_orange.png");
            case 4:
                return MoCreatures.proxy.getModelTexture("snake_green_bright.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("snake_coral.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("snake_cobra.png");
            case 7:
                return MoCreatures.proxy.getModelTexture("snake_rattle.png");
            case 8:
                return MoCreatures.proxy.getModelTexture("snake_python.png");
            default:
                return MoCreatures.proxy.getModelTexture("snake_green_dark.png");
        }
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }

    @Override
    // snakes can't jump
    public void jumpFromGround() {
        if (this.isInWater()) {
            super.jumpFromGround();
        }
    }

    public boolean pickedUp() {
        return (this.getVehicle() != null);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        if (!getIsTamed()) {
            return InteractionResult.FAIL;
        }

        if (this.getVehicle() == null) {
            if (this.startRiding(player)) {
                this.setYRot(player.getYRot());
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isNotScared() {
        return getTypeMoC() > 2 && getMoCAge() > 50;
    }

    /**
     * returns true when is climbing up
     */
    public boolean isClimbing() {
        return onClimbable() && this.getDeltaMovement().y > 0.01F;
    }

    public boolean isResting() {
        return (!getNearPlayer() && this.onGround() && (this.getDeltaMovement().x < 0.01D && this.getDeltaMovement().x > -0.01D) && (this.getDeltaMovement().z < 0.01D && this.getDeltaMovement().z > -0.01D));
    }

    public boolean getNearPlayer() {
        return (this.isNearPlayer || this.isBiting());
    }

    public void setNearPlayer(boolean flag) {
        this.isNearPlayer = flag;
    }

    public int getMovInt() {
        return this.movInt;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getVehicle() instanceof Player) {
            return 0.1F;
        }

        return getPassengersRidingOffset();
    }

    public float getSizeF() {
        float factor = 1.0F;
        if (getTypeMoC() == 1 || getTypeMoC() == 2)// small shy snakes
        {
            factor = 0.8F;
        } else if (getTypeMoC() == 5)// coral
        {
            factor = 0.6F;
        }
        if (getTypeMoC() == 6)// cobra 1.1
        {
            factor = 1.1F;
        }
        if (getTypeMoC() == 7)// rattlesnake
        {
            factor = 0.9F;
        }
        if (getTypeMoC() == 8)// python
        {
            factor = 1.5F;
        }
        return this.getMoCAge() * 0.01F * factor;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (getfTongue() != 0.0F) {
                setfTongue(getfTongue() + 0.2F);
                if (getfTongue() > 8.0F) {
                    setfTongue(0.0F);
                }
            }

            if (getfMouth() != 0.0F && this.hissCounter == 0) //biting
            {
                setfMouth(getfMouth() + 0.1F);
                if (getfMouth() > 0.5F) {
                    setfMouth(0.0F);
                }
            }

            if (getTypeMoC() == 7 && getfRattle() != 0.0F) // rattling
            {
                setfRattle(getfRattle() + 0.2F);
                if (getfRattle() == 1.0F) {
                    // TODO synchronize
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SNAKE_RATTLE.get());
                }
                if (getfRattle() > 8.0F) {
                    setfRattle(0.0F);
                }
            }

            /*
             * stick tongue
             */
            if (this.random.nextInt(50) == 0 && getfTongue() == 0.0F) {
                setfTongue(0.1F);
            }

            /*
             * Open mouth
             */
            if (this.random.nextInt(100) == 0 && getfMouth() == 0.0F) {
                setfMouth(0.1F);
            }
            if (getTypeMoC() == 7) {
                int chance;
                if (getNearPlayer()) {
                    chance = 30;
                } else {
                    chance = 100;
                }

                if (this.random.nextInt(chance) == 0) {
                    setfRattle(0.1F);
                }
            }
            /*
             * change in movement pattern
             */
            if (!isResting() && !pickedUp() && this.random.nextInt(50) == 0) {
                this.movInt = this.random.nextInt(10);
            }

            /*
             * Biting animation
             */
            if (isBiting()) {
                this.bodyswing -= 0.5F;
                setfMouth(0.3F);

                if (this.bodyswing < 0F) {
                    MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SNAKE_SNAP.get());
                    this.bodyswing = 2.5F;
                    setfMouth(0.0F);
                    setBiting(false);
                }
            }

        }
        if (pickedUp()) {
            this.movInt = 0;
        }

        if (isResting()) {
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }

        if (!this.onGround() && (this.getVehicle() != null)) {
            this.setYRot(this.getVehicle().getYRot());
        }

        if (this.level().getDifficulty().getId() > 0 && getNearPlayer() && !getIsTamed() && isNotScared()) {

            this.hissCounter++;

            // TODO synchronize and get sound
            // hiss
            if (this.hissCounter % 25 == 0) {
                setfMouth(0.3F);
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SNAKE_ANGRY.get());
            }
            if (this.hissCounter % 35 == 0) {
                setfMouth(0.0F);
            }

            if (this.hissCounter > 100 && this.random.nextInt(50) == 0) {
                // then randomly get pissed
                setPissed(true);
                this.hissCounter = 0;
            }

        }
        if (this.hissCounter > 500) {
            this.hissCounter = 0;
        }

    }

    /**
     * from 0.0 to 4.0F 0.0 = inside mouth 2.0 = completely stuck out 3.0 =
     * returning 4.0 = in.
     */
    public float getfTongue() {
        return this.fTongue;
    }

    public void setfTongue(float fTongue) {
        this.fTongue = fTongue;
    }

    public float getfMouth() {
        return this.fMouth;
    }

    public void setfMouth(float fMouth) {
        this.fMouth = fMouth;
    }

    public float getfRattle() {
        return this.fRattle;
    }

    public void setfRattle(float fRattle) {
        this.fRattle = fRattle;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        /*
         * this stops chasing the target randomly
         */
        if (getTarget() != null && this.random.nextInt(300) == 0) {
            setTarget(null);
        }

        Player entityplayer1 = this.level().getNearestPlayer(this, 12D);
        if (entityplayer1 != null) {
            double distP = MoCTools.getSqDistanceTo(entityplayer1, this.getX(), this.getY(), this.getZ());
            if (isNotScared()) {
                setNearPlayer(distP < 5D);

                /*if (entityplayer1.isVehicle()
                        && (entityplayer1.getPassengers().get(0) instanceof MoCEntityMouse || entityplayer1.getPassengers().get(0) instanceof MoCEntityBird)) {
                    PathEntity pathentity = this.getNavigation().createPath(entityplayer1, 1.0);
                    this.getNavigation().moveTo(pathentity, 1D);
                    setPissed(false);
                    this.hissCounter = 0;
                }*/
            } else {
                setNearPlayer(false);
            }

        } else {
            setNearPlayer(false);
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entityIn) {
        boolean flag = super.doHurtTarget(serverLevel, serverLevel, entityIn);
        if (flag && entityIn instanceof LivingEntity && getTypeMoC() > 2) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MoCMessageAnimation(this.getId(), 1));
            setBiting(true);
        }
        return flag;
    }

    @Override
    public void performAnimation(int i) {
        if (i == 1) setBiting(true);
    }

    public boolean isBiting() {
        return this.isBiting;
    }

    public void setBiting(boolean flag) {
        this.isBiting = flag;
        if (!this.level().isClientSide() && flag) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MoCMessageAnimation(this.getId(), 1));
        }
    }

    public boolean isPissed() {
        return this.isPissed;
    }

    public void setPissed(boolean isPissed) {
        this.isPissed = isPissed;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {

        if (getTypeMoC() < 3) {
            return super.hurtServer(serverLevel, damagesource, i);
        }

        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getDirectEntity();
            if (entity != null && this.isVehicle()) {
                return true;
            }
            if ((entity != this) && entity instanceof LivingEntity && (super.shouldAttackPlayers())) {
                setPissed(true);
                setTarget((LivingEntity) entity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        if (getMoCAge() > 60) {
            int j = this.random.nextInt(3);
            for (int l = 0; l < j; l++) {

                int snakeEggType = getTypeMoC() + 20;
                ItemStack snakeEgg = new ItemStack(MoCItems.MOC_EGG.get(), 1);

                snakeEgg.getOrCreateTag().putInt("EggType", snakeEggType);

                this.spawnAtLocation(snakeEgg, 1);
            }
        }
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntitySnake) && entity.getBbHeight() < 0.5D && entity.getBbWidth() < 0.5D;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState par4) {
        if (isEyeInFluid(FluidTags.WATER)) {
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_SNAKE_SWIM.get());
        }
        // TODO - add sound for slither
        /*
         * else { world.playSoundAtEntity(this, "snakeslither", 1.0F, 1.0F);
         * }
         */
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_SNAKE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_SNAKE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_SNAKE_AMBIENT.get();
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        /*
         * swamp: python, bright green, #1 (done) plains: coral, cobra #1, #2,
         * #3, #4 (everyone but 7) desert: rattlesnake , #2 jungle: all except
         * rattlesnake forest: all except rattlesnake hills: all except python,
         * bright green, bright orange, rattlesnake tundra-taiga: none ocean:
         * leave alone
         */

        /*
         * Biome lists: Ocean Plains Desert Extreme Hills Forest Taiga Swampland
         * River Frozen Ocean Frozen River Ice Plains Ice Mountains Mushroom
         * Island Mushroom Island Shore Beach DesertHills ForestHills TaigaHills
         * Extreme Hills Edge Jungle JungleHills
         *
         */
        try {
            // In 1.20.1, we need to check the biome path directly instead of using BiomeDictionary
            String biomePath = this.level().getBiome(pos).unwrapKey().orElseThrow().location().getPath();
            int l = this.random.nextInt(10);

            // Snowy biomes - no snakes
            if (biomePath.contains("snow") || biomePath.contains("frozen") || biomePath.contains("ice") || biomePath.contains("cold")) {
                return false;
            }

            // Desert/Mesa biomes - rattlesnake or spotted
            if (biomePath.contains("desert") || biomePath.contains("mesa") || biomePath.contains("badlands")) {
                if (l < 5) {
                    setTypeMoC(7);
                } else {
                    setTypeMoC(2);
                }
            }

            // Plains biomes - dark green, coral, or spotted
            if (biomePath.contains("plains")) {
                if (l < 3) {
                    setTypeMoC(1);
                } else if (l < 5) {
                    setTypeMoC(5);
                } else {
                    setTypeMoC(2);
                }
            }

            // Savanna biomes - python, spotted, or rattlesnake
            if (biomePath.contains("savanna")) {
                if (l < 4) {
                    setTypeMoC(8);
                } else if (l < 8) {
                    setTypeMoC(2);
                } else {
                    setTypeMoC(7);
                }
            }

            // Forest biomes - dark green or spotted
            if (biomePath.contains("forest") && !biomePath.contains("jungle")) {
                if (l < 5) {
                    setTypeMoC(1);
                } else {
                    setTypeMoC(2);
                }
            }

            // Swamp biomes - python, cobra, or dark green
            if (biomePath.contains("swamp")) {
                if (l < 5) {
                    setTypeMoC(8);
                } else if (l < 7) {
                    setTypeMoC(6);
                } else {
                    setTypeMoC(1);
                }
            }

            // Jungle biomes - various snake types except rattlesnake
            if (biomePath.contains("jungle")) {
                if (l < 3) {
                    setTypeMoC(4); // bright green
                } else if (l < 5) {
                    setTypeMoC(3); // bright orange
                } else if (l < 7) {
                    setTypeMoC(6); // cobra
                } else if (l < 9) {
                    setTypeMoC(8); // python
                } else {
                    setTypeMoC(1); // dark green
                }
            }

            // Magical biomes - dark green
            if (biomePath.contains("magical")) {
                setTypeMoC(1);
            }

            // Wyvern lair specific types
            if (this.level().dimension() == MoCreatures.proxy.wyvernDimension) {
                if (l < 3) {
                    setTypeMoC(4); // bright green
                } else if (l < 5) {
                    setTypeMoC(3); // bright orange
                } else if (l < 7) {
                    setTypeMoC(2); // spotted
                } else {
                    setTypeMoC(1); // dark green
                }
            }

            // Override rattlesnake in non-desert/savanna biomes
            if (getTypeMoC() == 7 && !(biomePath.contains("desert") || biomePath.contains("mesa") || biomePath.contains("badlands") || biomePath.contains("savanna"))) {
                setTypeMoC(2); // spotted
            }
            
        } catch (Exception ignored) { }
        return true;
    }

    @Override
    public int nameYOffset() {
        return -30;
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == MoCItems.RAT_RAW.get();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isReadyToHunt() {
        return this.getIsAdult() && !this.isMovementCeased();
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity entityLivingBaseIn, Entity entityIn) {
        if (isVenomous() && entityIn instanceof LivingEntity) {
            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 150, 2));
        }
        super.doEnchantDamageEffects(entityLivingBaseIn, entityIn);
    }

    private boolean isVenomous() {
        return getTypeMoC() == 3 || getTypeMoC() == 4 || getTypeMoC() == 5 || getTypeMoC() == 6 || getTypeMoC() == 7 || getTypeMoC() == 9;
    }

    @Override
    public boolean shouldAttackPlayers() {
        return this.isPissed() && super.shouldAttackPlayers();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    public boolean isAmphibian() {
        return true;
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }

    @Override
    protected double maxDivingDepth() {
        return (this.getMoCAge() / 100D);
    }
}

