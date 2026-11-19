/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.block;

import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.neoforged.neoforge.common.IPlantable;

import java.util.List;

public class MoCBlockGrass extends GrassBlock implements BonemealableBlock {

    public MoCBlockGrass(BlockBehaviour.Properties properties) {
        super(properties
                .mapColor(MapColor.GRASS)
                .randomTicks()
                .strength(0.6F)
                .sound(SoundType.GRASS));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isAreaLoaded(pos, 3)) return;

        if (world.getMaxLocalRawBrightness(pos.above()) < 4 &&
                world.getBlockState(pos.above()).getLightBlock(world, pos.above()) > 2) {
            if (this == MoCBlocks.wyvgrass.get()) {
                world.setBlockAndUpdate(pos, MoCBlocks.wyvdirt.get().defaultBlockState());
            }
        } else {
            if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (!world.isLoaded(blockpos)) continue;

                    BlockState stateAbove = world.getBlockState(blockpos.above());
                    BlockState targetState = world.getBlockState(blockpos);

                    // Use tag-based check instead of hardcoded block check
                    if (targetState.is(BlockTags.DIRT) &&
                            world.getMaxLocalRawBrightness(blockpos.above()) >= 4 &&
                            stateAbove.getLightBlock(world, blockpos.above()) <= 2) {
                        world.setBlockAndUpdate(blockpos, MoCBlocks.wyvgrass.get().defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();

        if (this == MoCBlocks.wyvgrass.get()) {
            for (int i = 0; i < 128; ++i) {
                BlockPos spawnPos = above;
                int j = 0;

                while (j < i / 16) {
                    spawnPos = spawnPos.offset(random.nextInt(3) - 1,
                            (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                            random.nextInt(3) - 1);

                    // Use tag-based check instead of hardcoded block check
                    if (!world.getBlockState(spawnPos.below()).is(BlockTags.DIRT) ||
                            world.getBlockState(spawnPos).isCollisionShapeFullBlock(world, spawnPos)) {
                        break;
                    }

                    ++j;
                }

                if (world.getBlockState(spawnPos).isAir()) {
                    if (random.nextInt(24) == 0) {
                        // Chance to spawn mushrooms
                        if (random.nextBoolean()) {
                            world.setBlock(spawnPos, Blocks.BROWN_MUSHROOM.defaultBlockState(), 3);
                        } else {
                            world.setBlock(spawnPos, Blocks.RED_MUSHROOM.defaultBlockState(), 3);
                        }
                    } else if (random.nextInt(8) == 0) {
                        // Reduced chance for vanilla flowers
                        List<ConfiguredFeature<?, ?>> flowers = world.getBiome(spawnPos).value().getGenerationSettings().getFlowerFeatures();
                        if (!flowers.isEmpty()) {
                            // Check if a vanilla grass would be placed and replace with tall wyvgrass
                            ConfiguredFeature<?, ?> feature = flowers.get(0);
                            BlockState beforeState = world.getBlockState(spawnPos);
                            feature.place(world, world.getChunkSource().getGenerator(), random, spawnPos);
                            BlockState afterState = world.getBlockState(spawnPos);
                            
                            // If vanilla features replaced with something we don't want, use our block instead
                            if (afterState.is(Blocks.GRASS) || afterState.is(Blocks.TALL_GRASS)) {
                                world.setBlock(spawnPos, MoCBlocks.tallWyvgrass.get().defaultBlockState(), 3);
                            }
                        }
                    } else {
                        // Mostly tall wyvgrass
                        BlockState tallState = MoCBlocks.tallWyvgrass.get().defaultBlockState();
                        if (((MoCBlockTallGrass) MoCBlocks.tallWyvgrass.get()).canSurvive(tallState, world, spawnPos)) {
                            world.setBlock(spawnPos, tallState, 3);
                        }
                    }
                }
            }
        }
    }
    
    // Allow mushrooms to grow on this block
    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        BlockState plantState = plantable.getPlant(world, pos.relative(facing));
        if (plantState.getBlock() instanceof MushroomBlock) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
