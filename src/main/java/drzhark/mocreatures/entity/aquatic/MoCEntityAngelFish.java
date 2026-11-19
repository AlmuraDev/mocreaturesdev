/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.aquatic;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityAngelFish extends MoCEntitySmallFish {

    public MoCEntityAngelFish(EntityType<? extends MoCEntityAngelFish> type, Level world) {
        super(type, world);
        this.setTypeMoC(2);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("smallfish_angelfish.png");
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.ANGELFISH;
    }
}
