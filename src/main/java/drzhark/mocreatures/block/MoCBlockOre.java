/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MoCBlockOre extends Block {

    public MoCBlockOre(BlockBehaviour.Properties properties) {
        super(properties.sound(SoundType.STONE));
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
        boolean silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.SILK_TOUCH)
                .map(silk -> EnchantmentHelper.getItemEnchantmentLevel(silk, tool) > 0)
                .orElse(false);
        return silkTouch ? 0 : getExperience(level.getRandom());
    }

    private int getExperience(RandomSource rand) {
        if (this == MoCBlocks.ancientOre.get()) {
            return Mth.nextInt(rand, 2, 5);
        } else if (this == MoCBlocks.wyvernDiamondOre.get()) {
            return Mth.nextInt(rand, 4, 8);
        } else if (this == MoCBlocks.wyvernEmeraldOre.get()) {
            return Mth.nextInt(rand, 4, 8);
        } else if (this == MoCBlocks.wyvernLapisOre.get()) {
            return Mth.nextInt(rand, 3, 6);
        } else {
            return 0;
        }
    }
}
