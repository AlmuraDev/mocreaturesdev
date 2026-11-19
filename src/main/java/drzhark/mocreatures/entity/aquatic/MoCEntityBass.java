/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityBass extends MoCEntityMediumFish {

    public MoCEntityBass(EntityType<? extends MoCEntityBass> type, Level world) {
        super(type, world);
        this.setTypeMoC(3);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("mediumfish_bass.png");
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.BASS;
    }
}
