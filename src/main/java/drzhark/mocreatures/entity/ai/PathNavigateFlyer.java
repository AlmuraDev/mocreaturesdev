/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;

/**
 * PathNavigateFlyer - 1.20.1 compatible implementation for MoCreatures flying entities
 * Extends the vanilla FlyingPathNavigation with some custom behavior
 */
public class PathNavigateFlyer extends FlyingPathNavigation {

    public PathNavigateFlyer(Mob mob, Level level) {
        super(mob, level);
        this.setCanOpenDoors(false);
        this.setCanFloat(true);
        this.getNodeEvaluator().setCanPassDoors(true);
    }

    @Override
    protected boolean canUpdatePath() {
        // Always allow path updates for flying creatures
        return true;
    }
}
