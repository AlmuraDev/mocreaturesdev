/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.tameable;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityReference;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAquatic;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidType;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MoCEntityTameableAquatic extends MoCEntityAquatic implements IMoCTameable {

    protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(MoCEntityTameableAquatic.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    protected static final EntityDataAccessor<Integer> PET_ID = SynchedEntityData.defineId(MoCEntityTameableAquatic.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(MoCEntityTameableAquatic.class, EntityDataSerializers.BOOLEAN);

    private boolean hasEaten;
    private int gestationtime;

    public MoCEntityTameableAquatic(EntityType<? extends MoCEntityTameableAquatic> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UNIQUE_ID, Optional.empty());
        builder.define(PET_ID, -1);
        builder.define(TAMED, false);
    }

    @Override
    public int getOwnerPetId() {
        return this.entityData.get(PET_ID);
    }

    @Override
    public void setOwnerPetId(int i) {
        this.entityData.set(PET_ID, i);
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).map(EntityReference::getUUID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID uniqueId) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uniqueId).map(EntityReference::<LivingEntity>of));
    }

    @Override
    public void setTamed(boolean flag) {
        this.entityData.set(TAMED, flag);
    }

    @Override
    public boolean getIsTamed() {
        return this.entityData.get(TAMED);
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    /**
     * If the rider should be dismounted from the entity when the entity goes
     * underwater
     *
     * @param rider The entity that is riding
     * @return if the entity should be dismounted when underwater
     */
    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return this.getIsTamed();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !(getIsTamed() && target.getUUID().equals(getOwnerId())) && super.canAttack(target);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        if (target instanceof LivingEntity living && !canAttack(living)) {
            return false;
        }
        return super.doHurtTarget(serverLevel, target);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if ((this.isVehicle() && entity == this.getVehicle()) || (this.getVehicle() != null && entity == this.getVehicle())) {
            return false;
        }
        if (usesNewAI()) {
            return super.hurtServer(serverLevel, damagesource, i);
        }

        //this avoids damage done by Players to a tamed creature that is not theirs
        if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null && entity instanceof Player player && !entity.getUUID().equals(this.getOwnerId())
                && !MoCTools.isThisPlayerAnOP(player)) {
            return false;
        }

        return super.hurtServer(serverLevel, damagesource, i);
    }

    private boolean checkOwnership(Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!this.getIsTamed() || MoCTools.isThisPlayerAnOP(player)) {
            return true;
        }

        if (this.getIsGhost() && !stack.isEmpty() && stack.is(MoCItems.PET_AMULET.get())) {
            if (!this.level().isClientSide()) {
                // Send message to player that this is not their pet
                Component message = Component.translatable("msg.mocreatures.foreignpet");
                message.getStyle().withColor(ChatFormatting.RED);
                player.sendSystemMessage(message);
            }
            return false;
        }

        //if the player interacting is not the owner, do nothing!
        if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null
                && !player.getUUID().equals(this.getOwnerId())) {
            Component message = Component.translatable("msg.mocreatures.foreignpet");
            message.getStyle().withColor(ChatFormatting.RED);
            player.sendSystemMessage(message);
            return false;
        }

        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        return super.mobInteract(player, hand);
    }

    // This should always run first for all tameable aquatics
    public InteractionResult processTameInteract(Player player, InteractionHand hand) {
        if (!this.checkOwnership(player, hand)) {
            return InteractionResult.PASS;
        }

        final ItemStack stack = player.getItemInHand(hand);
        //before ownership check
        if (!stack.isEmpty() && getIsTamed() && stack.is(MoCItems.SCROLLOFOWNER.get()) && MoCreatures.proxy.enableResetOwnership
                && MoCTools.isThisPlayerAnOP(player)) {
            if (!player.isCreative()) stack.shrink(1);
            if (!this.level().isClientSide()) {
                if (this.getOwnerPetId() != -1) // required since getInt will always return 0 if no key is found
                {
                    MoCreatures.instance.mapData.removeOwnerPet(this, this.getOwnerPetId());
                }
                this.setOwnerId(null);

            }
            return InteractionResult.SUCCESS;
        }
        //if the player interacting is not the owner, do nothing!
        if (MoCreatures.proxy.enableOwnership && getOwnerId() != null
                && !player.getUUID().equals(this.getOwnerId()) && !MoCTools.isThisPlayerAnOP(player)) {
            return InteractionResult.SUCCESS;
        }

        //changes name
        if (!this.level().isClientSide() && !stack.isEmpty() && getIsTamed()
                && (stack.is(MoCItems.MEDALLION.get()) || stack.is(Items.BOOK) || stack.is(Items.NAME_TAG))) {
            return MoCTools.tameWithName(player, this);
        }

        //sets it free, untamed
        if (!stack.isEmpty() && getIsTamed() && stack.is(MoCItems.SCROLLFREEDOM.get())) {
            if (!player.isCreative()) stack.shrink(1);
            if (!this.level().isClientSide()) {
                if (this.getOwnerPetId() != -1) // required since getInt will always return 0 if no key is found
                {
                    MoCreatures.instance.mapData.removeOwnerPet(this, this.getOwnerPetId());
                }
                this.setOwnerId(null);
                this.setPetName("");
                this.dropMyStuff();
                this.setTamed(false);
            }

            return InteractionResult.SUCCESS;
        }

        //removes owner, any other player can claim it by renaming it
        if (!stack.isEmpty() && getIsTamed() && stack.is(MoCItems.SCROLLOFSALE.get())) {
            if (!player.isCreative()) stack.shrink(1);
            if (!this.level().isClientSide()) {
                if (this.getOwnerPetId() != -1) // required since getInt will always return 0 if no key is found
                {
                    MoCreatures.instance.mapData.removeOwnerPet(this, this.getOwnerPetId());
                }
                this.setOwnerId(null);
            }
            return InteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && getIsTamed() && isMyHealFood(stack)) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            if (!this.level().isClientSide()) {
                this.setHealth(getMaxHealth());
            }
            return InteractionResult.SUCCESS;
        }

        //stores in fishnet
        if (!stack.isEmpty() && stack.is(MoCItems.FISH_NET.get()) && this.canBeTrappedInNet()) {
            if (!this.level().isClientSide()) {
                MoCPetData petData = MoCreatures.instance.mapData.getPetData(this.getOwnerId());
                if (petData != null) {
                    petData.setInAmulet(this.getOwnerPetId(), true);
                }
            }
            player.setItemInHand(hand, ItemStack.EMPTY);
            if (!this.level().isClientSide()) {
                MoCTools.dropAmulet(this, 1, player);
                this.remove(Entity.RemovalReason.DISCARDED);
            }

            return InteractionResult.SUCCESS;
        }

        return null;
    }

    // Keep pets from natural despawning while allowing normal chunk/dimension removal.
    @Override
    public boolean requiresCustomPersistence() {
        return getIsTamed() || super.requiresCustomPersistence();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !getIsTamed() && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    /**
     * Play the taming effect, will either be hearts or smoke depending on
     * status
     */
    @Override
    public void playTameEffect(boolean par1) {
        SimpleParticleType particleType = ParticleTypes.HEART;

        if (!par1) {
            particleType = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(particleType, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D
                    + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), d0, d1, d2);
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putBoolean("Tamed", getIsTamed());
        if (this.getOwnerId() != null) {
            nbttagcompound.putString("OwnerUUID", this.getOwnerId().toString());
        }
        if (getOwnerPetId() != -1) {
            nbttagcompound.putInt("PetId", this.getOwnerPetId());
        }
        if (getIsTamed() && MoCreatures.instance.mapData != null) {
            MoCreatures.instance.mapData.updateOwnerPet(this);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        setTamed(nbttagcompound.getBooleanOr("Tamed", false));
        String s = "";
        if (MoCTools.has(nbttagcompound, "OwnerUUID")) {
            s = nbttagcompound.getStringOr("OwnerUUID", "");
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
        if (MoCTools.has(nbttagcompound, "PetId")) {
            setOwnerPetId(nbttagcompound.getIntOr("PetId", 0));
        }
        if (this.getIsTamed() && MoCTools.has(nbttagcompound, "PetId")) {
            MoCPetData petData = MoCreatures.instance.mapData.getPetData(this.getOwnerId());
            if (petData != null) {
                ListTag tag = petData.getOwnerRootNBT().getListOrEmpty("TamedList");
                for (int i = 0; i < tag.size(); i++) {
                    CompoundTag nbt = tag.getCompoundOrEmpty(i);
                    if (nbt.getIntOr("PetId", 0) == nbttagcompound.getIntOr("PetId", 0)) {
                        // update amulet flag
                        nbt.putBoolean("InAmulet", false);
                        // check if cloned and if so kill
                        if (nbt.contains("Cloned")) {
                            // entity was cloned
                            nbt.remove("Cloned"); // clear flag
                            this.setTamed(false);
                            this.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                }
            } else // no pet data was found, mocreatures.dat could have been deleted, so reset petId to -1
            {
                this.setOwnerPetId(-1);
            }
        }
    }

    // Override to fix heart animation on clients
    @Override
    public void handleEntityEvent(byte par1) {
        if (par1 == 2) {
            this.walkAnimation.setSpeed(1.5F);
            this.setInvulnerableTime(20);
            this.hurtTime = this.hurtDuration = 10;
            playSound(getHurtSound(this.damageSources().generic()), getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            hurtOrSimulate(this.damageSources().generic(), 0.0F);
        } else if (par1 == 3) {
            playSound(getDeathSound(), getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            setHealth(0.0F);
            die(this.damageSources().generic());
        } else {
            super.handleEntityEvent(par1);
        }
    }

    @Override
    public float getPetHealth() {
        return this.getHealth();
    }

    @Override
    public boolean isRiderDisconnecting() {
        return this.riderIsDisconnecting;
    }

    @Override
    public void setRiderDisconnecting(boolean flag) {
        this.riderIsDisconnecting = flag;
    }

    /**
     * Used to spawn hearts at this location
     */
    @Override
    public void spawnHeart() {
        double var2 = this.random.nextGaussian() * 0.02D;
        double var4 = this.random.nextGaussian() * 0.02D;
        double var6 = this.random.nextGaussian() * 0.02D;

        this.level().addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D
                + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), var2, var4, var6);
    }

    /**
     * ready to start breeding
     */
    @Override
    public boolean readytoBreed() {
        return !this.isVehicle() && this.getVehicle() == null && this.getIsTamed() && this.getHasEaten() && this.getIsAdult();
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        return "";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        return 0;
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return mate instanceof IMoCTameable;
    }

    @Override
    public void tick() {
        super.tick();
        //breeding code
        if (!this.level().isClientSide() && readytoBreed() && this.random.nextInt(100) == 0) {
            doBreeding();
        }
    }

    /**
     * fixes despawning tamed creatures
     */
    @Override
    public boolean isInWall() {
        if (this.getIsTamed()) {
            return false;
        }

        return super.isInWall();
    }

    /**
     * Breeding code
     */
    @SuppressWarnings("removal")
    protected void doBreeding() {
        int i = 0;

        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(8D, 3D, 8D), entity -> entity != this);
        for (Entity entity : list) {
            if (compatibleMate(entity)) {
                i++;
            }
        }

        if (i > 1) {
            // Clear the list to help GC
            list.clear();
            return;
        }

        List<Entity> list1 = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(4D, 2D, 4D), entity -> entity != this);
        for (Entity mate : list1) {
            if (!(compatibleMate(mate)) || (mate == this)) {
                continue;
            }

            if (!this.readytoBreed()) {
                // Clear lists before returning
                list.clear();
                list1.clear();
                return;
            }

            if (!((IMoCTameable) mate).readytoBreed()) {
                return;
            }

            setGestationTime(getGestationTime() + 1);
            if (!this.level().isClientSide()) {
                MoCMessageHandler.sendToNear(this.level(), this.getX(), this.getY(), this.getZ(), 64, new MoCMessageHeart(this.getId()));
            }

            if (getGestationTime() <= 50) {
                continue;
            }

            try {
                String offspringName = this.getOffspringClazz((IMoCTameable) mate);

                Mob offspring = (Mob) BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(MoCConstants.MOD_PREFIX + offspringName.toLowerCase())).create(this.level(), EntitySpawnReason.BREEDING);
                if (offspring instanceof IMoCTameable) {
                    IMoCTameable baby = (IMoCTameable) offspring;
                    offspring.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    this.level().addFreshEntity(offspring);
                    baby.setAdult(false);
                    baby.setMoCAge(35);
                    baby.setTamed(true);
                    baby.setOwnerId(this.getOwnerId());
                    baby.setTypeMoC(getOffspringTypeInt((IMoCTameable) mate));

                    UUID ownerId = this.getOwnerId();
                    Player entityplayer = null;
                    if (ownerId != null) {
                        entityplayer = this.level().getPlayerByUUID(this.getOwnerId());
                    }
                    if (entityplayer != null) {
                        MoCTools.tameWithName(entityplayer, baby);
                    }
                }
                MoCTools.playCustomSound(this, SoundEvents.CHICKEN_EGG);

            } catch (Exception ignored) {
            }

            this.setHasEaten(false);
            this.setGestationTime(0);
            ((IMoCTameable) mate).setHasEaten(false);
            ((IMoCTameable) mate).setGestationTime(0);
            break;
        }
        // Clear the lists to help GC
        list.clear();
        list1.clear();
    }

    /**
     * used to determine if the entity has eaten and is ready to breed
     */
    @Override
    public boolean getHasEaten() {
        return hasEaten;
    }

    @Override
    public void setHasEaten(boolean flag) {
        hasEaten = flag;
    }

    /**
     * returns breeding timer
     */
    @Override
    public int getGestationTime() {
        return gestationtime;
    }

    @Override
    public void setGestationTime(int time) {
        gestationtime = time;
    }
}
