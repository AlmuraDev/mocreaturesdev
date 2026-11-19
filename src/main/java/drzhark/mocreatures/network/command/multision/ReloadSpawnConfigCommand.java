package drzhark.mocreatures.network.command.multision;
import net.minecraft.server.permissions.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import drzhark.mocreatures.config.biome.BiomeSpawnConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadSpawnConfigCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mocreloadspawns")
            .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) // Requires OP level 2
            .executes(ReloadSpawnConfigCommand::execute)
        );
    }
    
    private static int execute(CommandContext<CommandSourceStack> context) {
        try {
            BiomeSpawnConfig.reloadConfig();
            context.getSource().sendSuccess(() -> Component.literal("Mo' Creatures spawn configuration reloaded successfully!"), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to reload spawn configuration: " + e.getMessage()));
            return 0;
        }
    }
} 