package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.dimension.worldgen.MoCDirectTeleporter;
import drzhark.mocreatures.registry.MoCPOI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ItemStaffPortal extends MoCItem {

    private int portalPosX;
    private int portalPosY;
    private int portalPosZ;
    private ResourceKey<Level> portalDimension;

    public static final ResourceKey<Level> WYVERN_DIM =
            ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("mocreatures", "wyvernlairworld"));

    public ItemStaffPortal(Item.Properties properties) {
        super(properties.stacksTo(1).durability(3));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = player.getItemInHand(context.getHand());

        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.FAIL;
        ServerLevel level = (ServerLevel) context.getLevel();

        // Disallow if mounted
        if (player.getVehicle() != null || !player.getPassengers().isEmpty()) {
            return InteractionResult.FAIL;
        }

        // Disallow Mending or Unbreaking
        boolean hasMending = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0;
        boolean hasUnbreaking = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) > 0;
        if (hasMending || hasUnbreaking) {
            String enchantments = hasMending && hasUnbreaking ? "mending, unbreaking"
                    : hasMending ? "mending" : "unbreaking";
            player.sendSystemMessage(Component.literal(MoCreatures.MOC_LOGO + " §cDetected illegal enchantment(s) '§a" + enchantments + "§c' on Staff Portal!\nThe item has been removed from your inventory."));
            player.getInventory().removeItem(stack);
            return InteractionResult.SUCCESS;
        }

        CompoundTag tag = stack.getOrCreateTag();

        if (!level.dimension().equals(WYVERN_DIM)) {
            // Save return portal info
            this.portalDimension = level.dimension();
            this.portalPosX = (int) player.getX();
            this.portalPosY = (int) player.getY();
            this.portalPosZ = (int) player.getZ();
            writeToNBT(tag);

            // Teleport to Wyvern Lair
            ServerLevel wyvernWorld = serverPlayer.server.getLevel(WYVERN_DIM);
            if (wyvernWorld != null) {
                // Find a suitable spawn point in the Wyvern dimension
                BlockPos targetPos = findDestinationPortal(wyvernWorld);
                
                wyvernWorld.getChunkSource().addRegionTicket(
                        TicketType.PORTAL, new ChunkPos(targetPos), 1, targetPos);
                serverPlayer.changeDimension(wyvernWorld, new MoCDirectTeleporter(targetPos, true));
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
            }
        } else {
            // Return to original location
            readFromNBT(tag);
            ResourceKey<Level> returnDim = this.portalDimension != null ? this.portalDimension : Level.OVERWORLD;
            ServerLevel returnWorld = serverPlayer.server.getLevel(returnDim);

            if (returnWorld != null) {
                BlockPos returnPos = new BlockPos(portalPosX, portalPosY + 1, portalPosZ);
                returnWorld.getChunkSource().addRegionTicket(
                        TicketType.PORTAL, new ChunkPos(returnPos), 1, returnPos);
                serverPlayer.changeDimension(returnWorld, new MoCDirectTeleporter(returnPos, false));
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
            } else {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.SUCCESS;
    }
    
    /**
     * Find a suitable portal location in the destination dimension
     */
    private BlockPos findDestinationPortal(ServerLevel world) {
        // First, try to find an existing portal POI with limited radius
        PoiManager poiManager = world.getPoiManager();
        
        // Search for existing portals in a much smaller radius to avoid hanging
        Optional<BlockPos> existingPortal = findExistingPortal(poiManager, world);
        
        if (existingPortal.isPresent()) {
            // Use existing portal
            return existingPortal.get();
        }
        
        // No existing portal, find a suitable island location quickly
        BlockPos spawnPos = world.getSharedSpawnPos();
        BlockPos portalPos = findSuitableIslandLocation(world, spawnPos);
        
        return portalPos;
    }
    
    /**
     * Find an existing portal POI with limited search radius
     */
    private Optional<BlockPos> findExistingPortal(PoiManager poiManager, ServerLevel world) {
        // Define the predicate to match our portal POI type
        Predicate<Holder<PoiType>> poiPredicate = (holder) -> 
            holder.is(MoCPOI.WYVERN_PORTAL_KEY);
        
        // Search in a much smaller radius to avoid hanging (500 blocks instead of 10000)
        BlockPos centerPos = world.getSharedSpawnPos();
        
        try {
            Stream<PoiRecord> records = poiManager.getInRange(
                    poiPredicate,
                    centerPos, 
                    500,  // Reduced from 10000 to 500
                    PoiManager.Occupancy.ANY);
            
            return records.findFirst().map(record -> record.getPos());
        } catch (Exception e) {
            // If POI search fails, return empty to use fallback
            return Optional.empty();
        }
    }
    
    /**
     * Find a suitable location for portal placement using Nether portal-style algorithm
     */
    private BlockPos findSuitableIslandLocation(ServerLevel world, BlockPos startPos) {
        // Use Nether portal-style systematic search
        // Search in expanding squares around the start position
        int searchRadius = 16; // Start with 16 block radius
        int maxSearchRadius = 128; // Don't search beyond 128 blocks
        
        while (searchRadius <= maxSearchRadius) {
            // Search in a square pattern around the start position
            for (int x = startPos.getX() - searchRadius; x <= startPos.getX() + searchRadius; x += 8) {
                for (int z = startPos.getZ() - searchRadius; z <= startPos.getZ() + searchRadius; z += 8) {
                    // Only check the perimeter of the square for efficiency
                    if (Math.abs(x - startPos.getX()) == searchRadius || Math.abs(z - startPos.getZ()) == searchRadius) {
                        BlockPos candidatePos = findSafePortalPosition(world, new BlockPos(x, 0, z));
                        if (candidatePos != null) {
                            return candidatePos;
                        }
                    }
                }
            }
            searchRadius += 16; // Expand search radius
        }
        
        // If no suitable location found, create a safe platform at spawn
        return createSafePlatform(world, startPos);
    }
    
    /**
     * Find a safe portal position at the given XZ coordinates, similar to Nether portal placement
     */
    private BlockPos findSafePortalPosition(ServerLevel world, BlockPos basePos) {
        // Scan from build height down to find a suitable location
        int worldHeight = world.getMaxBuildHeight();
        int minHeight = world.getMinBuildHeight() + 10;
        
        // Start scanning from a reasonable height
        for (int y = Math.min(worldHeight - 10, 100); y >= minHeight; y--) {
            BlockPos testPos = new BlockPos(basePos.getX(), y, basePos.getZ());
            
            if (isValidPortalSite(world, testPos)) {
                return testPos;
            }
        }
        
        return null; // No suitable position found
    }
    
    /**
     * Check if a position is valid for portal placement (Nether portal style validation)
     */
    private boolean isValidPortalSite(ServerLevel world, BlockPos pos) {
        // Check if we have enough air space above (like Nether portals need)
        for (int dy = 0; dy < 4; dy++) {
            BlockPos checkPos = pos.above(dy);
            if (!world.getBlockState(checkPos).isAir() && !world.getBlockState(checkPos).canBeReplaced()) {
                return false;
            }
        }
        
        // Check if we have solid ground below or can place one
        BlockPos groundPos = pos.below();
        if (world.getBlockState(groundPos).isAir()) {
            // Check if there's solid ground within a reasonable distance below
            boolean foundSolid = false;
            for (int dy = 1; dy <= 5; dy++) {
                BlockPos checkPos = pos.below(dy);
                if (!world.getBlockState(checkPos).isAir()) {
                    foundSolid = true;
                    break;
                }
            }
            if (!foundSolid) {
                return false;
            }
        }
        
        // Check for stability - ensure we're not floating in mid-air
        int solidCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos checkPos = pos.below().offset(dx, 0, dz);
                if (!world.getBlockState(checkPos).isAir()) {
                    solidCount++;
                }
            }
        }
        
        return solidCount >= 3; // Need some stability around the position
    }
    
    /**
     * Create a safe platform for portal placement when no suitable location is found
     */
    private BlockPos createSafePlatform(ServerLevel world, BlockPos basePos) {
        // Find a reasonable height using heightmap
        int terrainHeight = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, basePos.getX(), basePos.getZ());
        int platformHeight = Math.max(terrainHeight + 5, 70); // At least 5 blocks above terrain or Y=70
        
        BlockPos platformCenter = new BlockPos(basePos.getX(), platformHeight, basePos.getZ());
        
        // Don't actually place blocks - just return a safe position
        // The game's natural chunk generation will handle terrain
        return platformCenter;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @SuppressWarnings("removal")
    public void readFromNBT(CompoundTag nbt) {
        this.portalPosX = nbt.getIntOr("portalPosX", 0);
        this.portalPosY = nbt.getIntOr("portalPosY", 0);
        this.portalPosZ = nbt.getIntOr("portalPosZ", 0);
        String dimKey = nbt.getStringOr("portalDimension", "");
        this.portalDimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(dimKey));
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("portalPosX", this.portalPosX);
        nbt.putInt("portalPosY", this.portalPosY);
        nbt.putInt("portalPosZ", this.portalPosZ);
        nbt.putString("portalDimension", this.portalDimension.identifier().toString());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
