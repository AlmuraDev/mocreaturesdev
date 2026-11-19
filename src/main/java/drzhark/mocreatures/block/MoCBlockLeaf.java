/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class MoCBlockLeaf extends LeavesBlock {
    public MoCBlockLeaf(BlockBehaviour.Properties properties) {
        super(AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos, box) -> false));
    }
}
