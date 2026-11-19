/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.ai.*;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class MoCEntityFox extends MoCEntityTameableAnimal {

    public MoCEntityFox(EntityType<? extends MoCEntityFox> type, Level world) {
        super(type, world);
        setMoCAge(this.random.nextInt(15) + 50);
        setAdult(this.random.nextInt(3) != 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIPanicMoC(this, 1.0D));
        this.goalSelector.addGoal(3, new EntityAIFleeFromPlayer(this, 1.0D, 4D));
        this.goalSelector.addGoal(3, new EntityAIFollowOwnerPlayer(this, 0.8D, 2F, 10F));
        this.goalSelector.addGoal(4, new EntityAIFollowAdult(this, 1.0D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new EntityAIWanderMoC2(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.targetSelector.addGoal(1, new EntityAIHunt<>(this, AnimalEntity.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityTameableAnimal.createAttributes()
            .add(Attributes.FOLLOW_RANGE, 12.0D)
            .add(Attributes.MAX_HEALTH, 8.0D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void selectType() {
        checkSpawningBiome();

        if (getTypeMoC() == 0) {
            setTypeMoC(1);
        }
    }

    @Override
    public Identifier getTexture() {

        if (!getIsAdult()) {
            if (getTypeMoC() == 2) {
                return MoCreatures.proxy.getModelTexture("fox_snow.png");
            }
            return MoCreatures.proxy.getModelTexture("fox_cub.png");
        }
        if (getTypeMoC() == 2) {
            return MoCreatures.proxy.getModelTexture("fox_snow.png");
        }
        return MoCreatures.proxy.getModelTexture("fox.png");
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        if (super.hurtServer(serverLevel, damagesource, i)) {
            Entity entity = damagesource.getEntity();
            if (entity != null && this.isPassengerOfSameVehicle(entity)) {
                return true;
            }
            if (entity != this && this.isNotScared() && entity instanceof LivingEntity && super.shouldAttackPlayers()) {
                setTarget((LivingEntity) entity);
                setLastHurtByMob((LivingEntity) entity);
                return true;
            }

        }
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() == MoCItems.RAW_TURKEY.get()) {
            if (!player.isCreative()) stack.shrink(1);

            if (!this.level().isClientSide()) {
                if (!getIsTamed()) MoCTools.tameWithName(player, this);
            }
            this.setHealth(getMaxHealth());

            if (!this.level().isClientSide() && !getIsAdult() && (getMoCAge() < 100)) {
                setMoCAge(getMoCAge() + 1);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isNotScared() {
        return getIsAdult();
    }

    @Override
    public boolean checkSpawningBiome() {
        BlockPos pos = new BlockPos(
            Mth.floor(this.getX()), 
            Mth.floor(getBoundingBox().minY),
            Mth.floor(this.getZ())
        );
        
        // In 1.20.1, we need to check the biome path directly instead of using BiomeDictionary
        String biomePath = this.level().getBiome(pos).unwrapKey().orElseThrow().location().getPath();
        if (biomePath.contains("snow") || biomePath.contains("frozen") || biomePath.contains("cold")) {
            setTypeMoC(2);
        }
        
        return true;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_FOX_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_FOX_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MoCSoundEvents.ENTITY_FOX_AMBIENT.get();
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.FOX;
    }

    @Override
    protected float getSoundVolume() {
        return 0.3F;
    }

    @Override
    public boolean isMyHealFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == MoCItems.RAT_RAW.get();
    }

    @Override
    public int nameYOffset() {
        return -50;
    }

    @Override
    public boolean isReadyToFollowOwnerPlayer() { return !this.isMovementCeased(); }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        return !(entity instanceof MoCEntityFox) && entity.getBbHeight() <= 0.7D && entity.getBbWidth() <= 0.7D;
    }

    @Override
    public boolean isReadyToHunt() {
        return this.getIsAdult() && !this.isMovementCeased();
    }

    @Override
    public float getSizeFactor() {
        if (getIsAdult()) {
            return 0.9F;
        }
        return 0.9F * getMoCAge() * 0.01F;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.86F;
    }
}
