/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import net.minecraft.world.entity.EntitySpawnReason;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityMob;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Random;

public class MoCEntityDarkManticore extends MoCEntityManticore {

    public MoCEntityDarkManticore(EntityType<? extends MoCEntityDarkManticore> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityManticore.createAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 6.5D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("manticore_dark.png");
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        if (!getIsPoisoning() && this.random.nextInt(5) == 0 && target instanceof LivingEntity livingTarget) {
            setPoisoning(true);
            livingTarget.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 15 * 20, 0)); // 15 seconds
            livingTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15 * 20, 0));
        } else {
            openMouth();
        }
        super.doEnchantDamageEffects(attacker, target);
    }

    public static boolean getCanSpawnHere(EntityType<? extends MoCEntityMob> type, ServerLevel world, EntitySpawnReason reason, BlockPos pos, Random randomIn) {
        return MoCEntityManticore.getCanSpawnHere(type, world, reason, pos, randomIn) && !world.canSeeSky(pos) && (pos.getY() < 50.0D);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.DARK_MANTICORE;
    }
}
