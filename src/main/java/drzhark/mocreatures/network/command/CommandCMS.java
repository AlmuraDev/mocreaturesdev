/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * The information and clean-up parts of the 1.12.2 CustomMobSpawner {@code /cms} command: counting entities and
 * removing them by type. Its spawn settings (spawn tick rate, caps, light levels, biome groups) belonged to
 * CustomMobSpawner's own spawner, which this version doesn't have; spawning is set in config/MoCreatures/MoCreatures.json.
 */
public class CommandCMS {

    private static final int PAGE_SIZE = 10;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> cms = Commands.literal("cms")
                .requires(MoCCommandPermissions.require("cms"))
                .executes(context -> help(context.getSource()))
                .then(Commands.literal("help").executes(context -> help(context.getSource())))
                .then(Commands.literal("countentities")
                        .executes(context -> countDimension(context.getSource(), false, 1))
                        .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                .executes(context -> countDimension(context.getSource(), false, IntegerArgumentType.getInteger(context, "page"))))
                        .then(Commands.literal("all")
                                .executes(context -> countDimension(context.getSource(), true, 1))
                                .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                        .executes(context -> countDimension(context.getSource(), true, IntegerArgumentType.getInteger(context, "page")))))
                        .then(Commands.literal("chunk")
                                .executes(context -> countChunk(context.getSource()))))
                .then(Commands.literal("killall")
                        .executes(context -> killallHelp(context.getSource()))
                        .then(Commands.literal("force")
                                .executes(context -> killForce(context.getSource())))
                        .then(Commands.literal("tamed")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder))
                                        .executes(context -> killTamed(context.getSource(), StringArgumentType.getString(context, "player")))))
                        .then(Commands.argument("type", IdentifierArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ENTITY_TYPE.keySet(), builder))
                                .executes(context -> killType(context.getSource(), IdentifierArgument.getId(context, "type")))));
        dispatcher.register(cms);
    }

    private static int help(CommandSourceStack source) {
        source.sendSystemMessage(Component.literal("--- /cms ---").withStyle(ChatFormatting.AQUA));
        line(source, "/cms countentities [page]", "creatures in this dimension, by type");
        line(source, "/cms countentities all [page]", "every entity in this dimension, by type");
        line(source, "/cms countentities chunk", "entities in the chunk you're in");
        line(source, "/cms killall <type>", "removes untamed creatures of a type in every dimension");
        line(source, "/cms killall tamed <player>", "removes a player's tamed pets in every dimension");
        line(source, "/cms killall force", "removes every entity in this dimension except players and decorations");
        return 1;
    }

    private static void line(CommandSourceStack source, String command, String what) {
        source.sendSystemMessage(Component.literal(command).withStyle(ChatFormatting.GREEN)
                .append(Component.literal(" - " + what).withStyle(ChatFormatting.WHITE)));
    }

    // ---- countentities ----

    private static int countDimension(CommandSourceStack source, boolean all, int page) {
        ServerLevel level = source.getLevel();
        Predicate<Entity> include = all ? entity -> !(entity instanceof Player)
                : entity -> entity instanceof LivingEntity && !(entity instanceof Player) && !(entity instanceof ArmorStand);
        Map<EntityType<?>, Integer> counts = count(level.getAllEntities(), include);
        String where = "dimension " + level.dimension().identifier();
        if (counts.isEmpty()) {
            source.sendSystemMessage(Component.literal("No " + (all ? "entities" : "creatures") + " found in " + where + "."));
            return 0;
        }
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        sendPage(source, "Showing " + (all ? "entities" : "creatures") + " in " + where, counts, page);
        source.sendSystemMessage(Component.literal("Total " + (all ? "entities " : "creatures ")).withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(total)).withStyle(ChatFormatting.AQUA)));
        return total;
    }

    private static int countChunk(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        ChunkPos chunk = ChunkPos.containing(net.minecraft.core.BlockPos.containing(source.getPosition()));
        Map<EntityType<?>, Integer> counts = count(level.getAllEntities(),
                entity -> !(entity instanceof Player) && entity.chunkPosition().equals(chunk));
        String where = "chunk " + chunk.x() + ", " + chunk.z();
        if (counts.isEmpty()) {
            source.sendSystemMessage(Component.literal("No entities found in " + where + "."));
            return 0;
        }
        sendPage(source, "Showing entities in " + where, counts, 1);
        return counts.values().stream().mapToInt(Integer::intValue).sum();
    }

    private static Map<EntityType<?>, Integer> count(Iterable<Entity> entities, Predicate<Entity> include) {
        Map<EntityType<?>, Integer> counts = new HashMap<>();
        for (Entity entity : entities) {
            if (include.test(entity)) {
                counts.merge(entity.getType(), 1, Integer::sum);
            }
        }
        return counts;
    }

    private static void sendPage(CommandSourceStack source, String title, Map<EntityType<?>, Integer> counts, int page) {
        List<Map.Entry<EntityType<?>, Integer>> rows = new ArrayList<>(counts.entrySet());
        rows.sort(Map.Entry.<EntityType<?>, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(entry -> EntityType.getKey(entry.getKey()).toString()));
        int pages = (rows.size() + PAGE_SIZE - 1) / PAGE_SIZE;
        int shown = Math.min(Math.max(page, 1), pages);
        source.sendSystemMessage(Component.literal(title + " (page " + shown + " of " + pages + ")").withStyle(ChatFormatting.WHITE));
        for (Map.Entry<EntityType<?>, Integer> row : rows.subList((shown - 1) * PAGE_SIZE, Math.min(shown * PAGE_SIZE, rows.size()))) {
            source.sendSystemMessage(Component.literal(" " + row.getValue() + " ").withStyle(ChatFormatting.AQUA).append(typeName(row.getKey())));
        }
    }

    private static MutableComponent typeName(EntityType<?> type) {
        Identifier id = EntityType.getKey(type);
        return Component.literal(id.getNamespace()).withStyle(ChatFormatting.LIGHT_PURPLE)
                .append(Component.literal("|").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(id.getPath()).withStyle(ChatFormatting.GREEN));
    }

    // ---- killall ----

    private static int killallHelp(CommandSourceStack source) {
        source.sendSystemMessage(Component.literal("Name an entity type to remove, e.g. /cms killall mocreatures:bunny. ")
                .append(Component.literal("Also: /cms killall tamed <player>, /cms killall force.")).withStyle(ChatFormatting.WHITE));
        return 0;
    }

    private static int killType(CommandSourceStack source, Identifier id) {
        Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(id);
        if (type.isEmpty()) {
            source.sendFailure(Component.literal("Unknown entity type " + id + "."));
            return 0;
        }
        int count = 0;
        for (ServerLevel level : source.getServer().getAllLevels()) {
            count += discard(level, entity -> entity.getType() == type.get() && !(entity instanceof Player)
                    && !(entity instanceof AbstractVillager) && !isTamed(entity));
        }
        final int killed = count;
        source.sendSuccess(() -> Component.literal("Killed ").withStyle(ChatFormatting.RED)
                .append(Component.literal(killed + " ").withStyle(ChatFormatting.AQUA))
                .append(typeName(type.get()))
                .append(Component.literal(" (untamed, in every dimension).").withStyle(ChatFormatting.WHITE)), true);
        return count;
    }

    private static int killTamed(CommandSourceStack source, String playerName) {
        Optional<NameAndId> player = source.getServer().services().nameToIdCache().get(playerName);
        if (player.isEmpty()) {
            source.sendFailure(Component.literal("No player called " + playerName + " has played on this server."));
            return 0;
        }
        UUID owner = player.get().id();
        int count = 0;
        for (ServerLevel level : source.getServer().getAllLevels()) {
            count += discard(level, entity -> owner.equals(ownerOf(entity)));
        }
        final int killed = count;
        source.sendSuccess(() -> Component.literal("Killed ").withStyle(ChatFormatting.RED)
                .append(Component.literal(killed + " ").withStyle(ChatFormatting.AQUA))
                .append(Component.literal("tamed").withStyle(ChatFormatting.LIGHT_PURPLE))
                .append(Component.literal(" pets of ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(player.get().name()).withStyle(ChatFormatting.GREEN))
                .append(Component.literal(".").withStyle(ChatFormatting.WHITE)), true);
        return count;
    }

    private static int killForce(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        int count = discard(level, entity -> !(entity instanceof Player) && !(entity instanceof ArmorStand)
                && !(entity instanceof BlockAttachedEntity));
        source.sendSuccess(() -> Component.literal("Killed ").withStyle(ChatFormatting.RED)
                .append(Component.literal(count + " ").withStyle(ChatFormatting.AQUA))
                .append(Component.literal("entities in dimension " + level.dimension().identifier() + ".").withStyle(ChatFormatting.WHITE)), true);
        return count;
    }

    /** Removes the matching entities (no drops, as 1.12.2 did); collected first so the level's list isn't changed while read. */
    private static int discard(ServerLevel level, Predicate<Entity> match) {
        List<Entity> found = new ArrayList<>();
        for (Entity entity : level.getAllEntities()) {
            if (entity.isAlive() && match.test(entity)) {
                found.add(entity);
            }
        }
        found.forEach(Entity::discard);
        return found.size();
    }

    private static boolean isTamed(Entity entity) {
        if (entity instanceof IMoCEntity moc) {
            return moc.getIsTamed();
        }
        return entity instanceof OwnableEntity ownable && ownable.getOwnerReference() != null;
    }

    private static UUID ownerOf(Entity entity) {
        if (entity instanceof IMoCEntity moc) {
            return moc.getIsTamed() ? moc.getOwnerId() : null;
        }
        if (entity instanceof OwnableEntity ownable) {
            EntityReference<LivingEntity> owner = ownable.getOwnerReference();
            return owner != null ? owner.getUUID() : null;
        }
        return null;
    }
}
