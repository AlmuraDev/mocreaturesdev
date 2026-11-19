package drzhark.mocreatures.dimension.worldgen;

import drzhark.mocreatures.registry.MoCPOI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.tags.BlockTags;

public class MoCWorldGenPortal {

    private final BlockState pillarBlock;
    private final BlockState stairBlock;
    private final BlockState wallBlock;
    private final BlockState centerBlock;

    public MoCWorldGenPortal(BlockState pillar, BlockState stair, BlockState wall, BlockState center) {
        this.pillarBlock = pillar;
        this.stairBlock = stair;
        this.wallBlock = wall;
        this.centerBlock = center;
    }

    public void generatePillar(ServerLevel world, BlockPos pos) {
        for (int y = 0; y < 6; y++) {
            BlockPos target = pos.above(y);
            world.setBlock(target, pillarBlock, 2);
        }
    }

    public void generate(ServerLevel world, RandomSource rand, BlockPos pos) {
        if (!world.isEmptyBlock(pos)) return;

        int x = pos.getX();
        int y = pos.getY() - 1;
        int z = pos.getZ();
        
        // Create a solid foundation first
        createFoundation(world, x, y, z);

        // Place stairs
        for (int nZ = z - 3; nZ <= z + 2; nZ += 5) {
            Direction facing = (nZ < z) ? Direction.SOUTH : Direction.NORTH;
            for (int nX = x - 2; nX < x + 2; nX++) {
                BlockPos stairPos = new BlockPos(nX, y + 1, nZ);
                BlockState facingStair = stairBlock.setValue(StairBlock.FACING, facing);
                world.setBlock(stairPos, facingStair, 2);
            }
        }

        // Inner wall
        for (int nX = x - 2; nX < x + 2; nX++) {
            for (int nZ = z - 2; nZ < z + 2; nZ++) {
                world.setBlock(new BlockPos(nX, y + 1, nZ), wallBlock, 2);
            }
        }

        // Center platform
        BlockPos centerPos = new BlockPos(x, y + 1, z);
        for (int nX = x - 1; nX < x + 1; nX++) {
            for (int nZ = z - 1; nZ < z + 1; nZ++) {
                world.setBlock(new BlockPos(nX, y + 1, nZ), centerBlock, 2);
            }
        }

        // Top blocks
        for (int j = x - 3; j < x + 3; j += 5) {
            for (int nZ = z - 3; nZ < z + 3; nZ++) {
                world.setBlock(new BlockPos(j, y + 6, nZ), wallBlock, 2);
            }
        }

        // Pillars
        generatePillar(world, new BlockPos(x - 3, y, z - 3));
        generatePillar(world, new BlockPos(x - 3, y, z + 2));
        generatePillar(world, new BlockPos(x + 2, y, z - 3));
        generatePillar(world, new BlockPos(x + 2, y, z + 2));
        
        // Register the portal as a POI
        registerPortalPOI(world, centerPos);
    }
    
    /**
     * Creates a solid foundation for the portal structure
     */
    private void createFoundation(ServerLevel world, int x, int y, int z) {
        // Create a solid foundation platform
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                // Create a thicker foundation in the center
                int depth = (Math.abs(dx) <= 2 && Math.abs(dz) <= 2) ? 3 : 2;
                
                for (int dy = 0; dy < depth; dy++) {
                    BlockPos foundationPos = new BlockPos(x + dx, y - dy, z + dz);
                    
                    // Don't replace existing blocks except for air, water, or leaves
                    BlockState existingState = world.getBlockState(foundationPos);
                    if (existingState.isAir() || 
                        existingState.getBlock() == Blocks.WATER ||
                        existingState.is(BlockTags.LEAVES) ||
                        existingState.getBlock() == Blocks.OAK_LEAVES ||
                        existingState.getBlock() == Blocks.BIRCH_LEAVES ||
                        existingState.getBlock() == Blocks.SPRUCE_LEAVES ||
                        existingState.getBlock() == Blocks.JUNGLE_LEAVES ||
                        existingState.getBlock() == Blocks.ACACIA_LEAVES ||
                        existingState.getBlock() == Blocks.DARK_OAK_LEAVES) {
                        
                        // Use different blocks for visual interest
                        BlockState foundationBlock;
                        if (dy == 0) {
                            foundationBlock = wallBlock; // Top layer
                        } else {
                            foundationBlock = (dx + dz) % 2 == 0 ? 
                                Blocks.QUARTZ_BLOCK.defaultBlockState() : 
                                Blocks.QUARTZ_BRICKS.defaultBlockState();
                        }
                        
                        world.setBlock(foundationPos, foundationBlock, 2);
                    }
                }
            }
        }
    }
    
    /**
     * Registers the portal as a Point of Interest for future teleportation
     */
    private void registerPortalPOI(ServerLevel world, BlockPos pos) {
        PoiManager poiManager = world.getPoiManager();
        poiManager.ensureLoadedAndValid(world, pos, 8);
        
        // Force the POI to be registered at this position
        poiManager.add(pos, MoCPOI.WYVERN_PORTAL.getDelegate());
        
        // Log the POI registration
        System.out.println("[MoC Portal] Registered wyvern portal POI at " + pos);
    }
} 