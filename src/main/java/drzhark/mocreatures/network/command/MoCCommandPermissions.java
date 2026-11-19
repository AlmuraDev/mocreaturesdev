/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.command;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Permission nodes for the Mo' Creatures commands, {@code mocreatures.command.<name>}. A permission plugin such as
 * LuckPerms can grant them; without one they fall back to op level 2, as before. The console and command blocks always pass.
 */
public final class MoCCommandPermissions {

    private static final Map<String, PermissionNode<Boolean>> NODES = new LinkedHashMap<>();

    private MoCCommandPermissions() {
    }

    /** The {@code requires} check for a command; registers its node the first time. */
    public static Predicate<CommandSourceStack> require(String command) {
        PermissionNode<Boolean> node = NODES.computeIfAbsent(command, name -> new PermissionNode<>(MoCConstants.MOD_ID, "command." + name,
                PermissionTypes.BOOLEAN, (player, uuid, context) -> player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)));
        return source -> {
            ServerPlayer player = source.getPlayer();
            return player == null ? source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) : PermissionAPI.getPermission(player, node);
        };
    }

    static {
        for (String command : new String[] {"cms", "moc", "mocpets", "mocspawn", "moctp", "mocsummon", "mocreloadspawns", "mocdebug"}) {
            require(command);
        }
    }

    @SubscribeEvent
    public static void onPermissionNodes(PermissionGatherEvent.Nodes event) {
        NODES.values().forEach(event::addNodes);
    }
}
