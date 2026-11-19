/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
// todo freeze for some time if close to flower
// attack player if player attacks hive?
// hive block (honey, bee spawner)

package drzhark.mocreatures.entity.ambient;
import net.minecraft.tags.BlockItemTags;

import net.minecraft.server.level.ServerLevel;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.entity.MoCEntityInsect;
import drzhark.mocreatures.init.MoCLootTables;
import drzhark.mocreatures.init.MoCSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MoCEntityBee extends MoCEntityInsect {

    private int soundCount;

    public MoCEntityBee(EntityType<? extends MoCEntityBee> type, Level world) {
        super(type, world);
        this.texture = "bee.png";
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.25D);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (getIsFlying() && --this.soundCount == -1) {
                Player player = this.level().getNearestPlayer(this, 5D);
                if (player != null) {
                    MoCTools.playCustomSound(this, getMySound());
                    this.soundCount = 20;
                }
            }
        }
    }

    private SoundEvent getMySound() {
        if (getTarget() != null) {
            return MoCSoundEvents.ENTITY_BEE_UPSET.get();
        }
        return MoCSoundEvents.ENTITY_BEE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MoCSoundEvents.ENTITY_BEE_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MoCSoundEvents.ENTITY_BEE_HURT.get();
    }

    @Nullable
    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BEE;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 2000;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damagesource, float damage) {
        if (super.hurtServer(serverLevel, damagesource, damage)) {
            Entity entity = damagesource.getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity entityliving = (LivingEntity) entity;
                if ((entity != this) && (this.level().getDifficulty().getId() > 0)) {
                    setTarget(entityliving);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMyFavoriteFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(BlockItemTags.FLOWERS.item());
    }

    @Override
    public float getSpeed() {
        if (getIsFlying()) {
            return 0.15F;
        }
        return 0.12F;
    }

    @Override
    public boolean isFlyer() {
        return true;
    }
    
    @Override
    public boolean shouldRenderTransparent() {
        return true; // Bees have transparent wings when flying
    }
}
