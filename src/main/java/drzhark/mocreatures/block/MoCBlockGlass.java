/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;
import net.minecraft.world.level.block.TransparentBlock;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class MoCBlockGlass extends TransparentBlock {

    public MoCBlockGlass(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.NONE)
                .strength(0.3F)
                .noOcclusion()
                .sound(SoundType.GLASS)
                .isValidSpawn((state, getter, pos, entityType) -> false)
                .isSuffocating((state, getter, pos) -> false)
                .isViewBlocking((state, getter, pos, box) -> false));
    }
}
