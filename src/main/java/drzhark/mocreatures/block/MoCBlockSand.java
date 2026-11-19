/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;
import net.minecraft.util.TriState;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.ColoredFallingBlock;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MoCBlockSand extends ColoredFallingBlock {

    public MoCBlockSand(BlockBehaviour.Properties properties) {
        super(new ColorRGBA(0xFFB8B8CA), properties
                .strength(0.5F)
                .sound(SoundType.SAND));
    }


    /** Silver sand grows cacti and dead bushes like sand. */
    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, BlockState plant) {
        if ((plant.is(Blocks.CACTUS) || plant.is(Blocks.DEAD_BUSH)) && this == MoCBlocks.silverSand.get()) {
            return TriState.TRUE;
        }
        return super.canSustainPlant(state, world, pos, direction, plant);
    }
}
