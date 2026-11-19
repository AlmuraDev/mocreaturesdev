/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.tameable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityAmbient;
import drzhark.mocreatures.init.MoCItems;
import drzhark.mocreatures.init.MoCSoundEvents;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageHealth;
import drzhark.mocreatures.network.message.MoCMessageHeart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MoCEntityTameableAmbient extends MoCEntityAmbient implements IMoCTameable {

    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(MoCEntityTameableAmbient.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> PET_ID = SynchedEntityData.defineId(MoCEntityTameableAmbient.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(MoCEntityTameableAmbient.class, EntityDataSerializers.BOOLEAN);
    private boolean hasEaten;
    private int gestationtime;

    public MoCEntityTameableAmbient(EntityType<? extends MoCEntityTameableAmbient> type, Level world) {
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
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID uniqueId) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uniqueId));
    }

    @Override
    public void setTamed(boolean tamed) {
        this.entityData.set(TAMED, tamed);
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
        //this avoids damage done by Players to a tamed creature that is not theirs
        if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null && entity instanceof Player && !entity.getUUID().equals(this.getOwnerId())
                && !MoCTools.isThisPlayerAnOP(((ServerPlayer) entity))) {
            return false;
        }

        if (!this.level().isClientSide() && getIsTamed()) {
            MoCMessageHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MoCMessageHealth(this.getId(), this.getHealth()));
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
                // Remove when client is updated
                ((ServerPlayer) player).containerMenu.sendAllDataToRemote();
                Component message = Component.translatable("msg.mocreatures.foreignpet");
                ((MutableComponent) message).withStyle(ChatFormatting.RED);
                player.sendSystemMessage(message);
            }
            return false;
        }

        //if the player interacting is not the owner, do nothing!
        if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null
                && !player.getUUID().equals(this.getOwnerId())) {
            Component message = Component.translatable("msg.mocreatures.foreignpet");
            ((MutableComponent) message).withStyle(ChatFormatting.RED);
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

    // This should always run first for all tameable ambients
    public InteractionResult processTameInteract(Player player, InteractionHand hand) {
        if (!this.checkOwnership(player, hand)) {
            return InteractionResult.PASS;
        }

        final ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return super.mobInteract(player, hand);
        }

        //before ownership check
        if (getIsTamed() && stack.is(MoCItems.SCROLLOFOWNER.get()) && MoCreatures.proxy.enableResetOwnership
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
        if (MoCreatures.proxy.enableOwnership && this.getOwnerId() != null
                && !player.getUUID().equals(this.getOwnerId()) && !MoCTools.isThisPlayerAnOP(player)) {
            return InteractionResult.SUCCESS;
        }

        //changes name
        if (!this.level().isClientSide() && getIsTamed()
                && (stack.is(MoCItems.MEDALLION.get()) || stack.is(Items.BOOK) || stack.is(Items.NAME_TAG))) {
            return MoCTools.tameWithName(player, this);
        }

        //sets it free, untamed
        if (getIsTamed() && stack.is(MoCItems.SCROLLFREEDOM.get())) {
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
        if (getIsTamed() && stack.is(MoCItems.SCROLLOFSALE.get())) {
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

        if (getIsTamed() && isMyHealFood(stack)) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            if (!this.level().isClientSide()) {
                this.setHealth(getMaxHealth());
            }
            return InteractionResult.SUCCESS;
        }

        //stores in fishnet
        if (stack.is(MoCItems.FISH_NET.get()) && this.canBeTrappedInNet()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            if (!this.level().isClientSide()) {
                MoCPetData petData = MoCreatures.instance.mapData.getPetData(this.getOwnerId());
                if (petData != null) {
                    petData.setInAmulet(this.getOwnerPetId(), true);
                }
                MoCTools.dropAmulet(this, 1, player);
                this.discard();
            }

            return InteractionResult.SUCCESS;
        }

        //heals
        if (!stack.isEmpty() && getIsTamed() && this.getHealth() < this.getMaxHealth() && isMyHealFood(stack)) {
            if (!player.isCreative()) stack.shrink(1);
            MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_GENERIC_EATING.get());
            if (!this.level().isClientSide()) {
                this.setHealth(getMaxHealth());
            }
            return InteractionResult.SUCCESS;
        }

        if (getIsTamed() && stack.is(Items.SHEARS)) {
            if (!this.level().isClientSide()) {
                dropMyStuff();
            }

            return InteractionResult.SUCCESS;
        }

        return null;
    }

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
        SimpleParticleType particle = ParticleTypes.HEART;
        if (!par1) {
            particle = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; i++) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(particle, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), d0, d1, d2);
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
        if (nbttagcompound.contains("OwnerUUID", 8)) {
            s = nbttagcompound.getStringOr("OwnerUUID", "");
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
        if (nbttagcompound.contains("PetId")) {
            setOwnerPetId(nbttagcompound.getIntOr("PetId", 0));
        }
        if (this.getIsTamed() && nbttagcompound.contains("PetId")) {
            MoCPetData petData = MoCreatures.instance.mapData.getPetData(this.getOwnerId());
            if (petData != null) {
                ListTag tag = petData.getOwnerRootNBT().getList("TamedList", 10);
                for (int i = 0; i < tag.size(); i++) {
                    CompoundTag nbt = tag.getCompound(i);
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
        this.level().addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), var2, var4, var6);
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
     * Breeding code
     */
    @SuppressWarnings("removal")
    protected void doBreeding() {
        int i = 0;

        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(8D, 3D, 8D));
        for (Entity entity : list) {
            if (compatibleMate(entity)) {
                i++;
            }
        }

        if (i > 1) {
            return;
        }

        List<Entity> list1 = this.level().getEntities(this, this.getBoundingBox().inflate(4D, 2D, 4D));
        for (Entity mate : list1) {
            if (!(compatibleMate(mate)) || (mate == this)) {
                continue;
            }

            if (!this.readytoBreed()) {
                return;
            }

            if (!((IMoCTameable) mate).readytoBreed()) {
                return;
            }

            setGestationTime(getGestationTime() + 1);
            if (!this.level().isClientSide()) {
                MoCMessageHandler.INSTANCE.send(
                    PacketDistributor.NEAR.with(() -> 
                        new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64, this.level().dimension())), 
                    new MoCMessageHeart(this.getId())
                );
            }

            if (getGestationTime() <= 50) {
                continue;
            }

            try {
                String offspringName = this.getOffspringClazz((IMoCTameable) mate);

                Mob offspring = (Mob) BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(MoCConstants.MOD_PREFIX + offspringName.toLowerCase())).create(this.level());
                if (offspring instanceof IMoCTameable) {
                    IMoCTameable baby = (IMoCTameable) offspring;
                    ((Mob) baby).setPos(this.getX(), this.getY(), this.getZ());
                    this.level().addFreshEntity((Mob) baby);
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
    }

    @Override
    public boolean getHasEaten() {
        return this.hasEaten;
    }

    @Override
    public void setHasEaten(boolean flag) {
        this.hasEaten = flag;
    }

    @Override
    public int getGestationTime() {
        return this.gestationtime;
    }

    @Override
    public void setGestationTime(int time) {
        this.gestationtime = time;
    }
}
