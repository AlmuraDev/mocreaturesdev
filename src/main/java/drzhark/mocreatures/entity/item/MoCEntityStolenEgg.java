/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.item;

import net.minecraft.network.syncher.SynchedEntityData;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MoCEntityStolenEgg extends Entity {

    public MoCEntityStolenEgg(EntityType<? extends MoCEntityStolenEgg> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // Implementation here
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput compound) {
        // Implementation here
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput compound) {
        // Implementation here
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }
}
