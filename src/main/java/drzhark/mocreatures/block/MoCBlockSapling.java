/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class MoCBlockSapling extends SaplingBlock {

    public MoCBlockSapling(BlockBehaviour.Properties properties) {
        super(WyvwoodTreeGrower.WYVWOOD, properties
                .noCollision()
                .randomTicks()
                .strength(0.0F)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.POPPED));
    }
    
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        
        // Allow saplings on wyvgrass and wyvdirt
        if (blockstate.is(MoCBlocks.wyvgrass.get()) || blockstate.is(MoCBlocks.wyvdirt.get())) {
            return true;
        }
        
        // Also allow on vanilla soils as fallback
        return blockstate.is(BlockTags.DIRT) || super.canSurvive(state, level, pos);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Same as canSurvive but for actual placement
        return state.is(MoCBlocks.wyvgrass.get()) || 
               state.is(MoCBlocks.wyvdirt.get()) || 
               state.is(BlockTags.DIRT);
    }
}
