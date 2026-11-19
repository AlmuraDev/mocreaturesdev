/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hostile;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class MoCEntityGreenOgre extends MoCEntityOgre {

    public MoCEntityGreenOgre(EntityType<? extends MoCEntityGreenOgre> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityOgre.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Override
    public Identifier getTexture() {
        return MoCreatures.proxy.getModelTexture("ogre_green.png");
    }

    /**
     * Returns the strength of the blasting power
     */
    @Override
    public float getDestroyForce() {
        return MoCreatures.proxy.ogreStrength;
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.GREEN_OGRE;
    }
}
