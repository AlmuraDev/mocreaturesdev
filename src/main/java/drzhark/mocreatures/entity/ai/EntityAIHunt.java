/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.ai;

import com.google.common.base.Predicate;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.tameable.MoCEntityTameableAnimal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class EntityAIHunt<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final PathfinderMob hunter;
    private final Class<T> targetClass;

    public EntityAIHunt(PathfinderMob entity, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, Predicate<LivingEntity> predicate) {
        super(entity, classTarget, chance, checkSight, onlyNearby, predicate == null ? null : (target, level) -> predicate.test(target));
        this.hunter = entity;
        this.targetClass = classTarget;
    }

    public EntityAIHunt(PathfinderMob entityCreature, Class<T> classTarget, boolean checkSight) {
        this(entityCreature, classTarget, checkSight, false);
    }

    public EntityAIHunt(PathfinderMob entity, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(entity, classTarget, 10, checkSight, onlyNearby, null);

    }

    @Override
    public boolean canUse() {
        //return ((MoCEntityAnimal) this.hunter).getIsHunting() && super.shouldExecute();
        boolean hunterHasOwner = ((MoCEntityTameableAnimal)this.hunter).getIsTamed();
        return !hunterHasOwner && ((MoCEntityAnimal) this.hunter).getIsHunting() && super.canUse();
    }
}
