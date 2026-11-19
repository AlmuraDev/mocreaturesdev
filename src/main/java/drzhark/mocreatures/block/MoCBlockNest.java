/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MoCBlockNest extends Block {

    public MoCBlockNest(BlockBehaviour.Properties properties) {
        super(properties
                .strength(0.5F)
                .sound(SoundType.GRASS));
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double distance) {
        entity.causeFallDamage(distance, 0.2F, level.damageSources().fall());
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        return Blocks.HAY_BLOCK.getFlammability(state, getter, pos, direction);
    }
}
