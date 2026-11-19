/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.passive;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.EntityAIFleeFromEntityMoC;
import drzhark.mocreatures.entity.ai.EntityAIFollowOwnerPlayer;
import drzhark.mocreatures.entity.ai.EntityAIWanderMoC2;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

import javax.annotation.Nullable;
import java.util.List;

public class MoCEntityBird extends MoCEntityTameableAnimal {

    private static final EntityDataAccessor<Boolean> PRE_TAMED = SynchedEntityData.defineId(MoCEntityBird.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(MoCEntityBird.class, EntityDataSerializers.BOOLEAN);
    public float wingb;
    public float wingc;
    public float wingd;
    public float winge;
    public float wingh;
    public boolean textureSet;
    protected EntityAIWanderMoC2 wander;
    private boolean fleeing;
    private int jumpTimer;

    public MoCEntityBird(EntityType<? extends MoCEntityBird> type, Level world) {
        super(type, world);
        //setSize(0.5F, 0.9F);
        this.verticalCollision = true;
        this.wingb = 0.0F;
        this.wingc = 0.0F;
        this.wingh = 1.0F;
        this.fleeing = false;
        this.textureSet = false;
        setTamed(false);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIFleeFromEntityMoC(this, entity -> !(entity instanceof MoCEntityBird) && (entity.getBbHeight() > 0.4F || entity.getBbWidth() > 0.4F), 6.0F, 1.D, 1.3D));
        this.goalSelector.addGoal(3, new EntityAIFollowOwnerPlayer(this, 0.8D, 2F, 10F));
        this.goalSelector.addGoal(4, this.wander = new EntityAIWanderMoC2(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return MoCEntityTameableAnimal.createAttributes().add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();

        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(6) + 1);
        }
    }

    @Override
    public Identifier getTexture() {

        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("bird_white.png");
            case 2:
                return MoCreatures.proxy.getModelTexture("bird_black.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("bird_green.png");
            case 5:
                return MoCreatures.proxy.getModelTexture("bird_yellow.png");
            case 6:
                return MoCreatures.proxy.getModelTexture("bird_red.png");
            default:
                return MoCreatures.proxy.getModelTexture("bird_blue.png");
        }
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(getBoundingBox().minY), Mth.floor(this.getZ()));
        
        try {
            // In 1.20.1, check biome directly
            String biomeName = this.level().getBiome(pos).unwrapKey().orElseThrow().location().getPath();
            if (biomeName.contains("mesa")) {
                setTypeMoC(2); // only black birds
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PRE_TAMED, Boolean.FALSE);
        builder.define(IS_FLYING, Boolean.FALSE);
    }

    public boolean getPreTamed() {
        return this.entityData.get(PRE_TAMED);
    }

    public void setPreTamed(boolean flag) {
        this.entityData.set(PRE_TAMED, flag);
    }

    public boolean getIsFlying() {
        return this.entityData.get(IS_FLYING);
    }

    public void setIsFlying(boolean flag) {
        this.entityData.set(IS_FLYING, flag);
    }

    @Override
    public boolean causeFallDamage(double distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    private int[] FindTreeTop(int i, int j, int k) {
        int l = i - 5;
        int i1 = k - 5;
        int j1 = i + 5;
        int k1 = j + 7;
        int l1 = k + 5;
        for (int i2 = l; i2 < j1; i2++) {
            label0:
            for (int j2 = i1; j2 < l1; j2++) {
                BlockPos pos = new BlockPos(i2, j, j2);
                BlockState blockstate = this.level().getBlockState(pos);
                if (blockstate.isAir() || (!blockstate.toString().contains("wood"))) {
                    continue;
                }
                int l2 = j;
                do {
                    if (l2 >= k1) {
                        continue label0;
                    }
                    BlockPos pos1 = new BlockPos(i2, l2, j2);
                    BlockState blockstate1 = this.level().getBlockState(pos1);
                    if (blockstate1.isAir()) {
                        return (new int[]{i2, l2 + 2, j2});
                    }
                    l2++;
                } while (true);
            }

        }

        return (new int[]{0, 0, 0});
    }

    private void FlyToNextEntity(Entity entity) {
        if (entity != null) {
            int i = Mth.floor(entity.getX());
            int j = Mth.floor(entity.getY());
            int k = Mth.floor(entity.getZ());
            faceLocation(i, j, k, 30F);
            if (Mth.floor(this.getY()) < j) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.14999999999999999D, 0.0D));
            }
            if (this.getX() < entity.getX()) {
                double d = entity.getX() - this.getX();
                if (d > 0.5D) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.050000000000000003D, 0.0D, 0.0D));
                }
            } else {
                double d1 = this.getX() - entity.getX();
                if (d1 > 0.5D) {
                    this.setDeltaMovement(this.getDeltaMovement().subtract(0.050000000000000003D, 0.0D, 0.0D));
                }
            }
            if (this.getZ() < entity.getZ()) {
                double d2 = entity.getZ() - this.getZ();
                if (d2 > 0.5D) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.0D,0.050000000000000003D));
                }
            } else {
                double d3 = this.getZ() - entity.getZ();
                if (d3 > 0.5D) {
                    this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.0D, 0.050000000000000003D));
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private boolean FlyToNextTree() {
        int[] ai = ReturnNearestWoodCoord(this, 20D);
        int[] ai1 = FindTreeTop(ai[0], ai[1], ai[2]);
        if (ai1[1] != 0) {
            int i = ai1[0];
            int j = ai1[1];
            int k = ai1[2];
            faceLocation(i, j, k, 30F);
            if ((j - Mth.floor(this.getY())) > 2) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.14999999999999999D, 0.0D));
            }
            int l;
            int i1;
            if (this.getX() < i) {
                l = i - Mth.floor(this.getX());
                this.setDeltaMovement(this.getDeltaMovement().add(0.050000000000000003D, 0.0D, 0.0D));
            } else {
                l = Mth.floor(this.getX()) - i;
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.050000000000000003D, 0.0D, 0.0D));
            }
            if (this.getZ() < k) {
                i1 = k - Mth.floor(this.getZ());
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.0D,0.050000000000000003D));
            } else {
                i1 = Mth.floor(this.getX()) - k;
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.0D, 0.050000000000000003D));
            }
            double d = l + i1;
            return d < 3D;
        }
        return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getTypeMoC() == 1) {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_WHITE.get();
        }
        if (getTypeMoC() == 2) {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_BLACK.get();
        }
        if (getTypeMoC() == 3) {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_GREEN.get();
        }
        if (getTypeMoC() == 4) {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_BLUE.get();
        }
        if (getTypeMoC() == 5) {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_YELLOW.get();
        } else {
            return MoCSoundEvents.ENTITY_BIRD_AMBIENT_RED.get();
        }
    }

    // TODO: Add unique sound event
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BIRD;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getVehicle() instanceof Player) {
            return this.getVehicle().isCrouching() ? 0.2 : 0.45F;
        }
        return 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && getPreTamed() && !getIsTamed() && stack.getItem() == Items.WHEAT_SEEDS) {
            if (!player.isCreative()) stack.shrink(1);
            if (!this.level().isClientSide()) {
                MoCTools.tameWithName(player, this);
            }
            return InteractionResult.SUCCESS;
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

    // TODO: Add updated flap ai based on vanilla's parrot
    @Override
    public void aiStep() {
        super.aiStep();

        this.winge = this.wingb;
        this.wingd = this.wingc;
        this.wingc = (float) (this.wingc + ((this.onGround() ? -1 : 4) * 0.3D));
        if (this.wingc < 0.0F) {
            this.wingc = 0.0F;
        }
        if (this.wingc > 1.0F) {
            this.wingc = 1.0F;
        }
        if (!this.onGround() && (this.wingh < 1.0F)) {
            this.wingh = 1.0F;
        }
        this.wingh = (float) (this.wingh * 0.9D);
        if (!this.onGround() && (this.getDeltaMovement().y < 0.0D)) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.8D, 1.0D));
        }
        this.wingb += this.wingh * 2.0F;

        //check added to avoid duplicating behavior on client / server
        if (!this.level().isClientSide()) {

            if (isMovementCeased() && getIsFlying()) {
                setIsFlying(false);
            }

            if (getIsFlying() && this.getNavigation().isDone() && !isMovementCeased() && this.getTarget() == null && this.random.nextInt(30) == 0) {
                this.wander.makeUpdate();
            }

            if (!getIsFlying() && !getIsTamed() && this.random.nextInt(10) == 0) {
                List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(4D), entity -> entity != this);
                for (Entity entity1 : list) {
                    if (!(entity1 instanceof LivingEntity) || entity1 instanceof MoCEntityBird) {
                        continue;
                    }
                    if (entity1.getBbWidth() >= 0.4F && entity1.getBbHeight() >= 0.4F && this.hasLineOfSight(entity1)) {
                        setIsFlying(true);
                        this.fleeing = true;
                        this.wander.makeUpdate();
                    }
                }
            }

            if (!isMovementCeased() && !getIsFlying() && this.random.nextInt(getIsTamed() ? 1000 : 400) == 0) {
                setIsFlying(true);
                this.wander.makeUpdate();
            }

            if (getIsFlying() && this.random.nextInt(200) == 0) {
                setIsFlying(false);
            }

            if (this.fleeing && this.random.nextInt(50) == 0) {
                this.fleeing = false;
            }

            //TODO move to new AI
            if (!this.fleeing) {
                ItemEntity entityitem = this.getClosestItem(this, 12D, Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS));
                if (entityitem != null) {
                    FlyToNextEntity(entityitem);
                    ItemEntity entityitem1 = this.getClosestItem(this, 1.0D, Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS));
                    if ((this.random.nextInt(50) == 0) && (entityitem1 != null)) {
                        entityitem1.remove(RemovalReason.DISCARDED);
                        setPreTamed(true);
                    }
                }
            }
            if (this.random.nextInt(10) == 0 && this.isEyeInFluid(FluidTags.WATER)) {
                WingFlap();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getVehicle() != null) {
            this.setYRot(this.getVehicle().getYRot());
        }

        if ((this.getVehicle() != null) && (this.getVehicle() instanceof Player)) {
            Player entityplayer = (Player) this.getVehicle();
            this.setYRot(entityplayer.getYRot());
            entityplayer.fallDistance = 0.0F;
            if (entityplayer.getDeltaMovement().y < -0.1D)
                entityplayer.setDeltaMovement(entityplayer.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (--this.jumpTimer <= 0 && this.onGround()
                && ((this.getDeltaMovement().x > 0.05D) || (this.getDeltaMovement().z > 0.05D) || (this.getDeltaMovement().x < -0.05D) || (this.getDeltaMovement().z < -0.05D))) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.25D, this.getDeltaMovement().z);
            float velX = Mth.sin(this.getYRot() * (float) Math.PI / 180.0F);
            float velZ = Mth.cos(this.getYRot() * (float) Math.PI / 180.0F);

            this.setDeltaMovement(this.getDeltaMovement().add((-0.2F * velX), 0.0D, (0.2F * velZ)));
            this.jumpTimer = 15;
        }
    }

    public int[] ReturnNearestWoodCoord(Entity entity, Double double1) {
        AABB axisalignedbb = entity.getBoundingBox().inflate(double1);
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.floor(axisalignedbb.maxX + 1.0D);
        int k = Mth.floor(axisalignedbb.minY);
        int l = Mth.floor(axisalignedbb.maxY + 1.0D);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.floor(axisalignedbb.maxZ + 1.0D);
        for (int k1 = i; k1 < j; k1++) {
            for (int l1 = k; l1 < l; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    BlockPos pos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = this.level().getBlockState(pos);
                    if (!blockstate.isAir() && blockstate.toString().contains("wood")) {
                        return (new int[]{k1, l1, i2});
                    }
                }
            }
        }
        return (new int[]{0, 0, 0});
    }



    private void WingFlap() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
        if (this.random.nextInt(30) == 0) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.2D, 0.0D, 0.0D));
        }
        if (this.random.nextInt(30) == 0) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.2D, 0.0D, 0.0D));
        }
        if (this.random.nextInt(30) == 0) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.0D, 0.2D));
        }
        if (this.random.nextInt(30) == 0) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.0D, 0.2D));
        }
    }

    @Override
    public int nameYOffset() {
        return -40;
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == Items.WHEAT_SEEDS || stack.getItem() == Items.MELON_SEEDS);
    }

    @Override
    public boolean isNotScared() {
        return getIsTamed();
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public int maxFlyingHeight() {
        return getIsTamed() ? 3 : 7;
    }

    @Override
    public int minFlyingHeight() {
        return 2;
    }

    @Override
    public boolean canRidePlayer() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.8F;
    }
}
