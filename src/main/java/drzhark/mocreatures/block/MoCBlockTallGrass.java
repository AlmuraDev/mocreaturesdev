/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;
import net.minecraft.world.level.block.FarmlandBlock;

import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

public class MoCBlockTallGrass extends TallGrassBlock {

    protected static final VoxelShape SHAPE = Shapes.box(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D);

    public MoCBlockTallGrass(BlockBehaviour.Properties properties) {
        super(properties
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XYZ)
                .pushReaction(PushReaction.POPPED));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof GrassBlock
                || state.getBlock() instanceof MoCBlockGrass
                || state.getBlock() instanceof MoCBlockDirt
                || state.getBlock() instanceof FarmlandBlock
                || state.is(net.minecraft.tags.BlockTags.DIRT);
    }
}
