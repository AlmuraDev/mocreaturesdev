/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.event;

import drzhark.mocreatures.MoCreatures;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Debug mode only ({@code /mocdebug debug on}): counts the natural spawner's water creature attempts per dimension and
 * logs a summary every 10 seconds - how many attempts, how many landed in water, and the spawn list the spawner saw.
 */
public final class MoCSpawnDebug {

    private static final Map<String, int[]> ATTEMPTS = new TreeMap<>();
    private static final Map<String, String> LISTS = new TreeMap<>();
    private static final Map<String, Integer> REASONS = new TreeMap<>();
    private static final java.util.Set<net.minecraft.world.entity.EntityType<?>> DUMPED = new java.util.HashSet<>();
    private static int ticks;

    private MoCSpawnDebug() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotentialSpawns(LevelEvent.PotentialSpawns event) {
        if (!MoCreatures.proxy.debug || event.getMobCategory() != MobCategory.WATER_CREATURE || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        String dimension = level.dimension().identifier().toString();
        int[] counts = ATTEMPTS.computeIfAbsent(dimension, key -> new int[2]);
        counts[0]++;
        if (level.getFluidState(event.getPos()).is(FluidTags.WATER)) {
            counts[1]++;
            // Run the spawner's remaining checks for the Mo' Creatures water mobs on the list, and count which one fails
            net.minecraft.core.BlockPos pos = event.getPos();
            net.minecraft.world.entity.player.Player player = level.getNearestPlayer(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, -1.0, false);
            double distSqr = player == null ? Double.MAX_VALUE : player.distanceToSqr(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            for (Weighted<MobSpawnSettings.SpawnerData> entry : event.getSpawnerDataList()) {
                net.minecraft.world.entity.EntityType<?> type = entry.value().type();
                if (!BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals("mocreatures")) {
                    continue;
                }
                String reason;
                if (distSqr <= 576.0) {
                    reason = "within 24 blocks of a player";
                } else if (distSqr > 128.0 * 128.0) {
                    reason = "over 128 blocks from a player";
                } else if (!net.minecraft.world.entity.SpawnPlacements.hasPlacement(type)) {
                    reason = "no spawn placement registered";
                } else if (!net.minecraft.world.entity.SpawnPlacements.isSpawnPositionOk(type, level, pos)) {
                    reason = "placement check failed (" + net.minecraft.world.entity.SpawnPlacements.getPlacementType(type) + ")";
                } else if (!net.minecraft.world.entity.SpawnPlacements.checkSpawnRules((net.minecraft.world.entity.EntityType) type, level,
                        net.minecraft.world.entity.EntitySpawnReason.NATURAL, pos, level.getRandom())) {
                    reason = "spawn rules refused";
                    if (DUMPED.add(type)) {
                        MoCreatures.LOGGER.info("Water spawn debug: stored spawn rule for {}: {}", BuiltInRegistries.ENTITY_TYPE.getKey(type), storedPredicate(type));
                    }
                } else if (!level.noCollision(type.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))) {
                    reason = "no room";
                } else {
                    reason = "would pass";
                }
                REASONS.merge(dimension + " " + BuiltInRegistries.ENTITY_TYPE.getKey(type) + ": " + reason, 1, Integer::sum);
            }
            String biome = level.getBiome(event.getPos()).unwrapKey().map(key -> key.identifier().toString()).orElse("?");
            String mobs = event.getSpawnerDataList().stream()
                    .map(Weighted::value)
                    .map(MobSpawnSettings.SpawnerData::type)
                    .map(type -> BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())
                    .collect(Collectors.joining(", "));
            LISTS.put(dimension + " " + biome, mobs.isEmpty() ? "(none)" : mobs);
        }
    }

    /** A predicate and everything it captured (lambda arguments, the merged predicate's lists), as text. */
    private static String describe(Object value, int depth) {
        if (value == null) {
            return "null";
        }
        String name = value.getClass().getName();
        if (depth > 4 || !(value instanceof net.minecraft.world.entity.SpawnPlacements.SpawnPredicate<?>
                || value instanceof net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.MergedSpawnPredicate<?>
                || value instanceof java.util.Collection<?>)) {
            return name;
        }
        if (value instanceof java.util.Collection<?> collection) {
            return collection.stream().map(item -> describe(item, depth + 1)).collect(Collectors.joining(", ", "[", "]"));
        }
        StringBuilder text = new StringBuilder(name).append(" {");
        for (java.lang.reflect.Field field : value.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                text.append(' ').append(field.getName()).append('=').append(describe(field.get(value), depth + 1));
            } catch (ReflectiveOperationException | RuntimeException e) {
                text.append(' ').append(field.getName()).append("=?");
            }
        }
        return text.append(" }").toString();
    }

    /** The predicate SpawnPlacements stored for a type, read by reflection (debug only). */
    private static String storedPredicate(net.minecraft.world.entity.EntityType<?> type) {
        try {
            java.lang.reflect.Field mapField = null;
            for (java.lang.reflect.Field field : net.minecraft.world.entity.SpawnPlacements.class.getDeclaredFields()) {
                if (Map.class.isAssignableFrom(field.getType())) {
                    mapField = field;
                }
            }
            if (mapField == null) {
                return "?";
            }
            mapField.setAccessible(true);
            Object data = ((Map<?, ?>) mapField.get(null)).get(type);
            if (data == null) {
                return "none";
            }
            for (java.lang.reflect.Field field : data.getClass().getDeclaredFields()) {
                if (net.minecraft.world.entity.SpawnPlacements.SpawnPredicate.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return describe(field.get(data), 0);
                }
            }
            return "?";
        } catch (ReflectiveOperationException | RuntimeException e) {
            return "error " + e;
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (++ticks < 200) {
            return;
        }
        ticks = 0;
        if (!MoCreatures.proxy.debug) {
            ATTEMPTS.clear();
            LISTS.clear();
            REASONS.clear();
            return;
        }
        if (ATTEMPTS.isEmpty()) {
            MoCreatures.LOGGER.info("Water spawn debug: no water creature spawn attempts in the last 10 seconds");
        }
        ATTEMPTS.forEach((dimension, counts) -> MoCreatures.LOGGER.info("Water spawn debug: {} - {} water creature attempts, {} in water",
                dimension, counts[0], counts[1]));
        LISTS.forEach((where, mobs) -> MoCreatures.LOGGER.info("Water spawn debug: list at {}: {}", where, mobs));
        REASONS.forEach((what, count) -> MoCreatures.LOGGER.info("Water spawn debug: {} x{}", what, count));
        ATTEMPTS.clear();
        LISTS.clear();
        REASONS.clear();
    }
}
