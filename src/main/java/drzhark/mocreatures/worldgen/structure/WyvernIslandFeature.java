/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.worldgen.structure;

import com.mojang.serialization.Codec;
import drzhark.mocreatures.MoCreatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import java.util.Optional;

public class WyvernIslandFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final String[] STRUCTURE_NAMES = {
        "wyvernisland1", "wyvernisland2", "wyvernisland3", "wyvernisland4"
    };
    
    // Enable debug mode
    private static final boolean DEBUG = false;
    
    public WyvernIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        // Only generate in wyvern dimension
        ResourceKey<?> dimensionType = context.level().getLevel().dimension();
        if (!dimensionType.location().toString().contains("wyvernlairworld")) {
            return false;
        }
        
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        
        // Get chunk coordinates like in 1.16
        int ci = origin.getX() >> 4 << 4;
        int ck = origin.getZ() >> 4 << 4;
        
        // Generate 1-2 islands per successful generation
        int count = random.nextInt(2) + 1;
        boolean success = false;
        
        for (int a = 0; a < count; ++a) {
            // Position within chunk with randomness
            int i = ci + random.nextInt(16);
            int k = ck + random.nextInt(16);
            
            // Height calculation similar to 1.16
            // First get surface height
            int j = context.level().getHeight(Heightmap.Types.OCEAN_FLOOR_WG, i, k);
            // Then add random height (16-80 blocks above surface)
            j += random.nextInt(84) + 16;
            
            // Choose random structure
            String structureName = STRUCTURE_NAMES[random.nextInt(STRUCTURE_NAMES.length)];
            
            // Random rotation and mirror
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
            
            BlockPos spawnPos = new BlockPos(i, j, k);
            
            if (DEBUG) {
                MoCreatures.LOGGER.info("Attempting to place wyvern island {} at {}", structureName, spawnPos);
            }
            
            // Get template
            StructureTemplateManager templateManager = context.level().getLevel().getStructureManager();
            Identifier location = Identifier.fromNamespaceAndPath("mocreatures", structureName);
            
            // Try loading the template
            StructureTemplate template = null;
            try {
                Optional<StructureTemplate> optionalTemplate = templateManager.get(location);
                if (!optionalTemplate.isPresent()) {
                    MoCreatures.LOGGER.error("Failed to load structure template: {}", location);
                    MoCreatures.LOGGER.error("Make sure the structure file exists at data/mocreatures/structures/{}.nbt", structureName);
                    continue;
                }
                template = optionalTemplate.get();
                
                Vec3i size = template.getSize();
                if (size.getX() == 0 || size.getY() == 0 || size.getZ() == 0) {
                    MoCreatures.LOGGER.error("Structure template {} has invalid size {}. The NBT file may be corrupted.", location, size);
                    continue;
                }

                if (DEBUG) {
                    MoCreatures.LOGGER.info("Successfully loaded template {} with size {}", structureName, template.getSize());
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.error("Exception while loading structure template: {}", location);
                MoCreatures.LOGGER.error("Error: {}", e.getMessage());
                e.printStackTrace();
                continue;
            }
            
            // Create placement settings similar to 1.16
            StructurePlaceSettings placementSettings = new StructurePlaceSettings()
                    .setRotation(rotation)
                    .setMirror(mirror)
                    .setRandom(random)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
                    .setIgnoreEntities(false)
                    .setKeepLiquids(false)  // Don't require liquids to match
                    .setKnownShape(false);  // More lenient placement
            
            if (DEBUG) {
                MoCreatures.LOGGER.info("Placement settings: rotation={}, mirror={}", rotation, mirror);
                MoCreatures.LOGGER.info("Template size: {}", template.getSize());
                MoCreatures.LOGGER.info("Attempting to place at Y={} (surface height + random offset)", j);
            }
            
            try {
                // Place the structure
                boolean result = template.placeInWorld(
                        (ServerLevelAccessor) context.level(),
                        spawnPos,
                        spawnPos,
                        placementSettings,
                        random,
                        2
                );
                
                if (result) {
                    MoCreatures.LOGGER.info("Successfully generated wyvern island {} at {}", structureName, spawnPos);
                    success = true;
                } else {
                    MoCreatures.LOGGER.error("Failed to place wyvern island {} at {}", structureName, spawnPos);
                    MoCreatures.LOGGER.error("This could be due to terrain interference or other structures in the area");
                }
            } catch (Exception e) {
                MoCreatures.LOGGER.error("Exception while placing wyvern island: {}", e.getMessage());
                MoCreatures.LOGGER.error("Stack trace:");
                e.printStackTrace();
            }
        }
        
        // Return true even if placement fails to prevent retries, just like 1.16 code
        return true;
    }
} 