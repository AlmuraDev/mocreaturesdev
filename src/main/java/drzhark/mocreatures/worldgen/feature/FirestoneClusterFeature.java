/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.worldgen.feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.init.MoCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;

public class FirestoneClusterFeature implements Feature {

    public static final MapCodec<FirestoneClusterFeature> CODEC = MapCodec.unit(FirestoneClusterFeature::new);
    
    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[] {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };
    
    public FirestoneClusterFeature() {
    }

    @Override
    public MapCodec<FirestoneClusterFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        boolean success = false;
        
        if (MoCreatures.proxy.debug) {
            MoCreatures.LOGGER.info("Attempting to generate firestone cluster at " + origin);
        }

        // Try multiple clusters per call
        for (int attempt = 0; attempt < 5; ++attempt) {
            BlockPos base = origin.offset(
                    random.nextInt(16),
                    random.nextInt(50) + 20,  // Y range: 20-70 (avoid surface level)
                    random.nextInt(16)
            );
            
            if (MoCreatures.proxy.debug) {
                MoCreatures.LOGGER.debug("Firestone attempt " + attempt + " at " + base);
            }

            // Find a good spot - search positions
            for (int offsetY = 0; offsetY < 20; ++offsetY) {
                BlockPos check = base.below(offsetY);
                
                // Skip if we're too close to the surface
                if (isTooCloseToSurface(level, check)) {
                    continue;
                }
                
                // Try ceiling or wall placement
                boolean canPlace = false;
                Direction attachDirection = null;
                
                // Check for ceiling attachment
                if (level.isEmptyBlock(check)) {
                    BlockState blockAbove = level.getBlockState(check.above());
                    if (blockAbove.isFaceSturdy(level, check.above(), Direction.DOWN)) {
                        // Skip if the block is wyvwood or wyvwood leaves
                        if (isWyvwoodBlock(blockAbove.getBlock())) {
                            continue;
                        }
                        
                        // Make sure this isn't the bottom of a thin overhang by checking if there's open space below
                        boolean isGoodCeiling = true;
                        for (int i = 1; i <= 3 && isGoodCeiling; i++) {
                            if (!level.isEmptyBlock(check.below(i))) {
                                isGoodCeiling = false;
                            }
                        }
                        
                        if (isGoodCeiling) {
                            canPlace = true;
                            attachDirection = Direction.DOWN;
                        }
                    }
                }
                
                // If ceiling placement failed, try wall attachment
                if (!canPlace) {
                    for (Direction dir : HORIZONTAL_DIRECTIONS) {
                        if (level.isEmptyBlock(check) && !level.isEmptyBlock(check.relative(dir))) {
                            BlockState blockSide = level.getBlockState(check.relative(dir));
                            
                            // Skip if the block is wyvwood or wyvwood leaves
                            if (isWyvwoodBlock(blockSide.getBlock())) {
                                continue;
                            }
                            
                            if (blockSide.isFaceSturdy(level, check.relative(dir), dir.getOpposite())) {
                                // Make sure this is truly a wall by checking if there's open space in opposite direction
                                boolean isGoodWall = true;
                                for (int i = 1; i <= 3 && isGoodWall; i++) {
                                    if (!level.isEmptyBlock(check.relative(dir.getOpposite(), i))) {
                                        isGoodWall = false;
                                    }
                                }
                                
                                if (isGoodWall) {
                                    canPlace = true;
                                    attachDirection = dir.getOpposite();
                                    break;
                                }
                            }
                        }
                    }
                }
                
                // Double-check we're not on ground
                if (canPlace) {
                    // Check if there's a solid block below with air above it (typical ground scenario)
                    BlockPos below = check.below();
                    if (!level.isEmptyBlock(below) && level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                        // This looks like ground placement, skip it
                        canPlace = false;
                    }
                }
                
                if (canPlace && attachDirection != null) {
                    // Place the core block
                    BlockState firestoneState = MoCBlocks.firestone.get().defaultBlockState();
                    level.setBlock(check, firestoneState, 2);
                    success = true;
                    
                    if (MoCreatures.proxy.debug) {
                        MoCreatures.LOGGER.info("Placed firestone cluster at " + check + " on " + attachDirection);
                    }

                    // Place additional blocks around it (blob effect)
                    for (int i = 0; i < 12 + random.nextInt(8); ++i) {
                        int xOffset = random.nextInt(5) - 2;
                        int yOffset = random.nextInt(3) - 1;
                        int zOffset = random.nextInt(5) - 2;
                        
                        // Bias growth away from attachment surface
                        if (attachDirection == Direction.DOWN) {
                            yOffset = -Math.abs(yOffset); // Grow downward from ceiling
                        } else if (attachDirection == Direction.NORTH) {
                            zOffset = -Math.abs(zOffset); // Grow southward from north wall
                        } else if (attachDirection == Direction.SOUTH) {
                            zOffset = Math.abs(zOffset);  // Grow northward from south wall
                        } else if (attachDirection == Direction.WEST) {
                            xOffset = -Math.abs(xOffset); // Grow eastward from west wall
                        } else if (attachDirection == Direction.EAST) {
                            xOffset = Math.abs(xOffset);  // Grow westward from east wall
                        }
                        
                        BlockPos nearby = check.offset(xOffset, yOffset, zOffset);

                        if (level.isEmptyBlock(nearby)) {
                            level.setBlock(nearby, firestoneState, 2);
                        }
                    }

                    break;
                }
            }
        }

        return success;
    }
    
    /**
     * Check if a position is too close to the surface (to avoid surface spawning)
     */
    private boolean isTooCloseToSurface(WorldGenLevel level, BlockPos pos) {
        // Check if we're near the top of the world
        if (pos.getY() > level.getHeight() - 20) {
            return true;
        }
        
        // Check if there's a continuous column of air above the position
        int airBlocksAbove = 0;
        for (int y = 1; y <= 10; y++) {
            BlockPos checkPos = pos.above(y);
            if (level.isEmptyBlock(checkPos)) {
                airBlocksAbove++;
            } else {
                break;
            }
        }
        
        // If there are many air blocks above, we might be near the surface
        return airBlocksAbove >= 8;
    }
    
    /**
     * Check if a block is wyvwood or wyvwood leaves
     */
    private boolean isWyvwoodBlock(Block block) {
        // Check if the block is wyvwood log, planks, or leaves
        return block == MoCBlocks.wyvwoodLog.get() || 
               block == MoCBlocks.wyvwoodPlanks.get() || 
               block == MoCBlocks.wyvwoodLeaves.get();
    }
}