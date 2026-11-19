/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ambient;

import net.minecraft.world.entity.EntitySpawnReason;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityInsect;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityDragonfly extends MoCEntityInsect {

    private int soundCount;

    public MoCEntityDragonfly(EntityType<? extends MoCEntityDragonfly> type, Level world) {
        super(type, world);
        this.texture = "dragonflya.png";
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityInsect.registerAttributes()
                .add(Attributes.ARMOR, 1.0D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
        if (this.level().dimension() == MoCreatures.proxy.wyvernDimension) this.setPersistenceRequired();
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.level().dimension() != MoCreatures.proxy.wyvernDimension;
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            setTypeMoC(this.random.nextInt(4) + 1);
        }
    }

    @Override
    public Identifier getTexture() {
        switch (getTypeMoC()) {
            case 1:
                return MoCreatures.proxy.getModelTexture("dragonfly_green.png");
            case 2:
                return MoCreatures.proxy.getModelTexture("dragonfly_cyan.png");
            case 3:
                return MoCreatures.proxy.getModelTexture("dragonfly_red.png");
            default:
                return MoCreatures.proxy.getModelTexture("dragonfly_blue.png");
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            Player player = this.level().getNearestPlayer(this, 5D);
            if (player != null && getIsFlying() && --this.soundCount == -1) {
                MoCTools.playCustomSound(this, MoCSoundEvents.ENTITY_DRAGONFLY_AMBIENT.get());
                this.soundCount = 20;
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_DRAGONFLY_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_DRAGONFLY_HURT.get();
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.DRAGONFLY;
    }

    @Override
    public boolean isFlyer() {
        return true;
    }

    @Override
    public float getSpeed() {
        if (getIsFlying()) {
            return 0.25F;
        }
        return 0.12F;
    }
    
    @Override
    public boolean shouldRenderTransparent() {
        return true; // Dragonflies always have transparent wings
    }
}
