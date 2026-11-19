package drzhark.mocreatures.network.command.multision;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.EntitySpawnReason;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityData;
import drzhark.mocreatures.init.MoCEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.registries.BuiltInRegistries;

public class MoCDebugSpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("mocdebug")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("spawn")
                    .then(Commands.literal("info")
                        .executes(MoCDebugSpawnCommand::showSpawnInfo))
                    .then(Commands.literal("test")
                        .then(Commands.argument("entity", StringArgumentType.word())
                            .executes(MoCDebugSpawnCommand::testSpawn)))
                    .then(Commands.literal("check")
                        .executes(MoCDebugSpawnCommand::checkSpawnConditions)))
                .then(Commands.literal("debug")
                    .then(Commands.literal("on")
                        .executes(MoCDebugSpawnCommand::enableDebug))
                    .then(Commands.literal("off")
                        .executes(MoCDebugSpawnCommand::disableDebug)))
        );
    }

    private static int showSpawnInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();
        BlockPos pos = player.blockPosition();
        
        // Get current biome
        Holder<Biome> biome = level.getBiome(pos);
        Identifier biomeName = biome.unwrap().map(
            resourceKey -> resourceKey.identifier(),
            biomeObj -> net.minecraft.resources.Identifier.fromNamespaceAndPath("unknown", "biome")
        );
        
        source.sendSuccess(() -> Component.literal("=== MoCreatures Spawn Debug Info ==="), false);
        source.sendSuccess(() -> Component.literal("Current biome: " + biomeName), false);
        source.sendSuccess(() -> Component.literal("Entity map size: " + MoCreatures.entityMap.size()), false);
        source.sendSuccess(() -> Component.literal("MocEntity map size: " + MoCreatures.mocEntityMap.size()), false);
        source.sendSuccess(() -> Component.literal("Spawn multiplier: " + MoCreatures.proxy.spawnMultiplier), false);
        
        // Check biome spawn lists
        net.minecraft.world.level.biome.MobSpawnSettings spawnSettings = level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.NATURAL_MOB_SPAWNS, player.position());
        java.util.List<net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData>> creatures = 
            spawnSettings.getMobsToSpawn(net.minecraft.world.entity.MobCategory.CREATURE).unwrap();
        java.util.List<net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData>> monsters = 
            spawnSettings.getMobsToSpawn(net.minecraft.world.entity.MobCategory.MONSTER).unwrap();
        java.util.List<net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData>> water = 
            spawnSettings.getMobsToSpawn(net.minecraft.world.entity.MobCategory.WATER_CREATURE).unwrap();
        java.util.List<net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData>> ambient = 
            spawnSettings.getMobsToSpawn(net.minecraft.world.entity.MobCategory.AMBIENT).unwrap();
        
        source.sendSuccess(() -> Component.literal("Biome spawn counts - Creatures: " + creatures.size() + 
            ", Monsters: " + monsters.size() + ", Water: " + water.size() + ", Ambient: " + ambient.size()), false);
        
        // Show some sample entity data
        if (!MoCreatures.mocEntityMap.isEmpty()) {
            MoCEntityData sampleData = MoCreatures.mocEntityMap.values().iterator().next();
            source.sendSuccess(() -> Component.literal("Sample entity: " + sampleData.getEntityName() + 
                " (CanSpawn: " + sampleData.getCanSpawn() + ", Freq: " + sampleData.getFrequency() + ")"), false);
        }
        
        source.sendSuccess(() -> Component.literal("Use '/mocdebug spawn test <entity>' to test spawning"), false);
        
        return 1;
    }

    private static int testSpawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();
        String entityName = StringArgumentType.getString(context, "entity");
        
        try {
            // Try to find the entity type
            EntityType<?> entityType = null;
            
            // Simple entity name mapping for testing
            switch (entityName.toLowerCase()) {
                case "bunny":
                    entityType = MoCEntities.BUNNY.get();
                    break;
                case "bear":
                case "blackbear":
                    entityType = MoCEntities.BLACK_BEAR.get();
                    break;
                case "bird":
                    entityType = MoCEntities.BIRD.get();
                    break;
                case "ant":
                    entityType = MoCEntities.ANT.get();
                    break;
                case "mouse":
                    entityType = MoCEntities.MOUSE.get();
                    break;
                default:
                    source.sendFailure(Component.literal("Unknown entity: " + entityName + ". Try: bunny, bear, bird, ant, mouse"));
                    return 0;
            }
            
            // Create and spawn the entity
            Mob entity = (Mob) entityType.create(level, EntitySpawnReason.COMMAND);
            if (entity != null) {
                BlockPos spawnPos = player.blockPosition().offset(2, 1, 0);
                entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                level.addFreshEntity(entity);
                
                source.sendSuccess(() -> Component.literal("Spawned " + entityName + " at " + spawnPos), false);
                
                // Show entity data if available
                MoCEntityData data = MoCreatures.entityMap.get(entityType);
                if (data != null) {
                    source.sendSuccess(() -> Component.literal("Entity data - CanSpawn: " + data.getCanSpawn() + 
                        ", Frequency: " + data.getFrequency()), false);
                } else {
                    source.sendSuccess(() -> Component.literal("Warning: No entity data found for " + entityName), false);
                }
            } else {
                source.sendFailure(Component.literal("Failed to create entity: " + entityName));
                return 0;
            }
            
        } catch (Exception e) {
            source.sendFailure(Component.literal("Error spawning entity: " + e.getMessage()));
            MoCreatures.LOGGER.error("Error in debug spawn command", e);
            return 0;
        }
        
        return 1;
    }

    private static int checkSpawnConditions(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();
        BlockPos pos = player.blockPosition();
        
        source.sendSuccess(() -> Component.literal("=== Spawn Conditions Check ==="), false);
        
        // Check game rules
        boolean doMobSpawning = level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.SPAWN_MOBS);
        source.sendSuccess(() -> Component.literal("doMobSpawning: " + doMobSpawning), false);
        
        // Check difficulty
        net.minecraft.world.Difficulty difficulty = level.getDifficulty();
        source.sendSuccess(() -> Component.literal("Difficulty: " + difficulty), false);
        
        // Check light level
        int lightLevel = level.getMaxLocalRawBrightness(pos);
        source.sendSuccess(() -> Component.literal("Light level: " + lightLevel), false);
        
        // Check if player is in spawn range
        net.minecraft.core.BlockPos spawnPos = level.getRespawnData().pos();
        double distanceToSpawn = Math.sqrt(pos.distSqr(spawnPos));
        source.sendSuccess(() -> Component.literal("Distance to spawn: " + String.format("%.1f", distanceToSpawn)), false);
        
        // Check biome spawn lists for MoCreatures entities
        Holder<net.minecraft.world.level.biome.Biome> biome = level.getBiome(pos);
        net.minecraft.world.level.biome.MobSpawnSettings spawnSettings = level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.NATURAL_MOB_SPAWNS, player.position());
        
        final int[] mocCreatureCount = {0};
        
        // Check all mob categories
        for (net.minecraft.world.entity.MobCategory category : net.minecraft.world.entity.MobCategory.values()) {
            java.util.List<net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData>> spawners = 
                spawnSettings.getMobsToSpawn(category).unwrap();
            
            if (!spawners.isEmpty()) {
                boolean foundMocInCategory = false;
                for (net.minecraft.util.random.Weighted<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawner : spawners) {
                    String entityName = spawner.value().type().getDescriptionId();
                    if (entityName.contains("mocreatures")) {
                        if (!foundMocInCategory) {
                            final String finalCategoryName = category.getName();
                            source.sendSuccess(() -> Component.literal("=== " + finalCategoryName.toUpperCase() + " Category ==="), false);
                            foundMocInCategory = true;
                        }
                        mocCreatureCount[0]++;
                        final String finalEntityName = entityName;
                        final int finalWeight = spawner.weight();
                        source.sendSuccess(() -> Component.literal("  " + finalEntityName + " (weight: " + finalWeight + ")"), false);
                    }
                }
            }
        }
        
        source.sendSuccess(() -> Component.literal("Total MoCreatures spawns in this biome: " + mocCreatureCount[0]), false);
        
        if (!doMobSpawning) {
            source.sendSuccess(() -> Component.literal("⚠ Issue: Mob spawning is disabled!"), false);
        }
        if (difficulty == net.minecraft.world.Difficulty.PEACEFUL) {
            source.sendSuccess(() -> Component.literal("⚠ Issue: Peaceful mode prevents monster spawning!"), false);
        }
        if (mocCreatureCount[0] == 0) {
            source.sendSuccess(() -> Component.literal("⚠ Issue: No MoCreatures spawns found in this biome!"), false);
        }
        
        return 1;
    }
    
    private static int enableDebug(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        MoCreatures.proxy.debug = true;
        MoCreatures.setDebug(true);
        source.sendSuccess(() -> Component.literal("Mo'Creatures debug mode enabled"), false);
        return 1;
    }
    
    private static int disableDebug(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        MoCreatures.proxy.debug = false;
        MoCreatures.setDebug(false);
        source.sendSuccess(() -> Component.literal("Mo'Creatures debug mode disabled"), false);
        return 1;
    }
} 