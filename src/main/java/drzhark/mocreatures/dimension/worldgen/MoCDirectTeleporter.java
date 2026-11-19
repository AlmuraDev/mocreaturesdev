package drzhark.mocreatures.dimension.worldgen;
import net.minecraft.world.level.portal.TeleportTransition;

import drzhark.mocreatures.registry.MoCPOI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


/** Moves an entity to a fixed spot in another dimension, building the wyvern portal there first if asked. */
public class MoCDirectTeleporter {
    private final BlockPos pos;
    private final boolean generateStructure;

    public MoCDirectTeleporter(BlockPos pos, boolean generateStructure) {
        this.pos = pos;
        this.generateStructure = generateStructure;
    }

    /** Builds the portal (if asked) and teleports the entity to stand on it. */
    public void teleport(Entity entity, ServerLevel destWorld) {
        if (this.generateStructure) {
            BlockState block = destWorld.getBlockState(this.pos);
            if (!block.is(Blocks.QUARTZ_BLOCK)) {
                MoCWorldGenPortal portalGen = new MoCWorldGenPortal(
                        Blocks.QUARTZ_PILLAR.defaultBlockState(),
                        Blocks.QUARTZ_STAIRS.defaultBlockState(),
                        Blocks.QUARTZ_BLOCK.defaultBlockState(),
                        Blocks.QUARTZ_BLOCK.defaultBlockState()
                );
                portalGen.generate(destWorld, destWorld.getRandom(), this.pos);
                registerPortalPOI(destWorld, this.pos);
                ensureSafeLanding(destWorld, this.pos);
            }
        }
        Vec3 target = new Vec3(this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5);
        entity.teleport(new TeleportTransition(destWorld, target, Vec3.ZERO, entity.getYRot(), entity.getXRot(),
                e -> e.resetFallDistance()));
    }

    /**
     * Ensures there's a safe landing platform at the destination
     */
    private void ensureSafeLanding(ServerLevel world, BlockPos pos) {
        // Check if we need to create a platform
        boolean needsPlatform = false;
        
        // Check if there's solid ground below
        for (int y = 1; y <= 5; y++) {
            BlockPos checkPos = pos.below(y);
            if (!world.getBlockState(checkPos).isAir()) {
                // Found solid ground within 5 blocks, no need for platform
                return;
            }
        }
        
        // If we get here, we need a platform
        System.out.println("[MoC Portal] Creating safety platform at " + pos);
        
        // Create a 5x5 platform under the portal
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos platformPos = pos.offset(dx, -1, dz);
                world.setBlock(platformPos, Blocks.QUARTZ_BLOCK.defaultBlockState(), 3);
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