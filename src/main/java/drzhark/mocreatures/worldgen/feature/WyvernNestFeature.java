/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.worldgen.feature;

import com.mojang.serialization.Codec;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WyvernNestFeature extends Feature<NoneFeatureConfiguration> {

    private static final ResourceKey<Level> WYVERN_DIMENSION = 
            ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("mocreatures", "wyvernlairworld"));

    public WyvernNestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Always log attempt
        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Attempting to generate wyvern nest at " + origin);
        }
        
        // Additional rarity check - only 1 in 3 attempts will proceed
        if (random.nextInt(3) != 0) {
            if (MoCreatures.proxy.debug) {
                MoCreatures.LOGGER.info("Wyvern nest generation skipped by additional rarity check");
            }
            return false;
        }
        
        // Check dimension - allow both wyvern dimension and overworld
        ResourceKey<?> dimensionType = level.getLevel().dimension();
        boolean isWyvernDimension = dimensionType.identifier().toString().contains("wyvernlairworld");
        
        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Dimension check: " + dimensionType.identifier() + ", isWyvernDimension: " + isWyvernDimension);
        }
        
        int x = origin.getX();
        int z = origin.getZ();
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
        BlockPos base = new BlockPos(x, y - 1, z);
        
        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Wyvern nest base position: " + base);
        }

        // Check the ground - allow more block types
        boolean validGround = true;
        int validBlocks = 0;
        int totalBlocks = 0;
        
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos check = base.offset(dx, 0, dz);
                totalBlocks++;
                
                // Check if the block below is solid
                if (level.isEmptyBlock(check.below())) {
                    continue;
                }
                
                // Check if the current block is a valid ground type
                BlockState state = level.getBlockState(check);
                if (state.getBlock() == MoCBlocks.wyvgrass.get() || 
                    state.getBlock() == MoCBlocks.silverSand.get() ||
                    state.getBlock() == Blocks.SAND ||
                    state.getBlock() == Blocks.RED_SAND ||
                    state.getBlock() == Blocks.STONE ||
                    state.getBlock() == Blocks.DEEPSLATE ||
                    state.getBlock() == Blocks.GRASS_BLOCK ||
                    state.getBlock() == Blocks.DIRT) {
                    validBlocks++;
                }
            }
        }
        
        // Allow generation if at least 75% of blocks are valid
        validGround = validBlocks >= (totalBlocks * 0.75);
        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Wyvern nest ground check: " + validBlocks + "/" + totalBlocks + " blocks valid, result: " + validGround);
        }
        
        if (!validGround) {
            return false;
        }

        return buildNest(level, base, random);
    }
    
    /**
     * Helper method to build the nest structure
     */
    private boolean buildNest(WorldGenLevel level, BlockPos base, RandomSource random) {
        // Create central log pillar
        BlockState log = random.nextBoolean() 
            ? MoCBlocks.wyvwoodLog.get().defaultBlockState() 
            : Blocks.BONE_BLOCK.defaultBlockState();
            
        for (int i = 0; i < 8; i++) {
            level.setBlock(base.above(i), log, 2);
        }

        BlockState nest = MoCBlocks.wyvernNestBlock.get().defaultBlockState();
        BlockPos top = base.above(8);

        // Build the circular nest platform
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int distSq = dx * dx + dz * dz;
                if (distSq <= 9) {
                    BlockPos nestPos = top.offset(dx, 0, dz);
                    level.setBlock(nestPos, nest, 2);
                }
            }
        }

        // Raise a ring wall around the nest (1 block higher)
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                int distSq = dx * dx + dz * dz;
                if (distSq >= 10 && distSq <= 13) {
                    BlockPos wallPos = top.offset(dx, 1, dz);
                    level.setBlock(wallPos, nest, 2);
                }
            }
        }

        // Place loot chest on top of the central nest
        BlockPos chestPos = top.above();
        level.setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 2);
        BlockEntity tile = level.getBlockEntity(chestPos);
        if (tile instanceof ChestBlockEntity) {
            ((ChestBlockEntity) tile).setLootTable(Identifier.parse("mocreatures:chests/wyvern_nest"), random.nextLong());
        }

        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Successfully generated wyvern nest at " + base);
        }
        return true;
    }
    
    /**
     * Public method to force spawn a nest at a specific location (for testing/commands)
     */
    public static boolean forceSpawnNest(Level level, BlockPos pos) {
        WyvernNestFeature feature = new WyvernNestFeature(NoneFeatureConfiguration.CODEC);
        return feature.buildNest((WorldGenLevel) level, pos, level.getRandom());
    }
} 