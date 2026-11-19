/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityHippoTang extends MoCEntitySmallFish {

    public MoCEntityHippoTang(EntityType<? extends MoCEntityHippoTang> type, Level world) {
        super(type, world);
        this.setTypeMoC(6);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("smallfish_hippotang.png");
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.HIPPO_TANG;
    }
}
