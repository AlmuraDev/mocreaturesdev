/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.IPlantable;

public class MoCBlockSand extends FallingBlock {

    public MoCBlockSand(BlockBehaviour.Properties properties) {
        super(properties
                .strength(0.5F)
                .sound(SoundType.SAND));
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
        return 12107978;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(direction));
        return (plant.getBlock() == Blocks.CACTUS || plant.getBlock() == Blocks.DEAD_BUSH) && this == MoCBlocks.silverSand.get()
                || super.canSustainPlant(state, world, pos, direction, plantable);
    }
}
